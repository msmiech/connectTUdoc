import {Component, ElementRef, Input, OnDestroy, OnInit, ViewChild} from "@angular/core";
import {AngularFireAuth} from "@angular/fire/auth";
import {AngularFireDatabase, AngularFireList} from "@angular/fire/database";
import {AccessService} from "../services/access.service";
import {saveAs as importedSaveAs} from "file-saver";
import {MatDialog} from "@angular/material";
import {ScreenshotDialogComponent} from "./screenshot-dialog.component";
import {ActivatedRoute} from "@angular/router";
import {Patient} from "../models/patient";

@Component({
  selector: 'video-call',
  templateUrl: '../views/video-call.component.html',
  styleUrls: ['../styles/video-call.component.css']
})
export class VideoCallComponent implements OnInit, OnDestroy {

  private constraints = {
    audio: true,
    video: true
  };

  @ViewChild('doctorVideo') doctorVideo: any;
  @ViewChild('patientVideo') patientVideo: any;
  @ViewChild('videoScreenshot') videoScreenshot: ElementRef;

  screenshotSrc: any;

  private SERVERS: any = {
    iceServers: [
      {urls: "stun:stun.l.google.com:19302"},
      {
        urls: 'turn:numb.viagenie.ca',
        credential: 'muazkh',
        username: 'webrtc@live.com'
      }
    ]
  };

  peerConnection: RTCPeerConnection;
  channel: AngularFireList<any[]>;
  database: firebase.database.Reference;
  senderId: string;
  onDestroyAttempt: boolean = false;
  connectionPerformed: boolean = false;


  patientId: string;
  patientName: string;

  constructor(public dialog: MatDialog, private accessService: AccessService, private firebaseAuth: AngularFireAuth, private fireDatabase: AngularFireDatabase, private route: ActivatedRoute) {
    this.route.params.subscribe((params) => {
      this.patientId = params['patientId'];
      this.patientName = params['patientName'];
    });
  }

  ngOnInit(): void {
    if (this.hasGetUserMedia()) {
      // Good to go!
      this.setupWebRtc();
    } else {
      alert('getUserMedia() is not supported by your browser');
    }
  }

  setupWebRtc() {
    this.senderId = this.accessService.medicalWorker.id.toString();
    let channelName = "/patient/" + this.patientId;
    this.channel = this.fireDatabase.list(channelName);
    this.database = this.fireDatabase.database.ref(channelName);
    this.database.remove();
    console.log('changed remove');

    this.database.on("child_added", data => this.readMessage(data));
    this.database.on("child_changed", data => this.readMessage(data));
    this.initPeerConnection();
  }

  initPeerConnection() {
    this.peerConnection = new RTCPeerConnection(this.SERVERS);
    this.peerConnection.onicecandidate = event =>
      event.candidate
        ? this.sendMessage(
        this.senderId,
        JSON.stringify({ice: event.candidate})
        )
        : console.log("Sent All Ice");

    this.peerConnection.oniceconnectionstatechange = event => {
      console.log("Ice state changed to: " + this.peerConnection.iceConnectionState);
      if (this.peerConnection.iceConnectionState == "closed" || this.peerConnection.iceConnectionState == "disconnected") {
        this.clearPatientStream();
        if(!this.onDestroyAttempt){
          this.initPeerConnection();
        }
      }
      if(this.peerConnection.iceConnectionState == "connected"){
        this.connectionPerformed = true;
      }
    };
    this.peerConnection.onsignalingstatechange = event => {
      console.log("Signaling state changed to: " + this.peerConnection.signalingState);
      if(this.peerConnection.signalingState == "have-local-offer"){
        this.connectionPerformed = false;
        this.waitForConnection();
      }
    };

    this.peerConnection.ontrack = event => {
      (this.patientVideo.nativeElement.srcObject = event.streams[0]);
      console.log("patient stream was set")
    };

    this.showMe();
  }

  async waitForConnection(){
    await this.sleep(8000);
    if(this.peerConnection.iceConnectionState != "connected" && this.peerConnection.iceConnectionState != "completed" && !this.connectionPerformed){
      alert("Verbindung konnte nicht aufgebaut werden. Beenden sie den Verbindungsversuch durch drücken auf \"Anruf beenden\" und versuchen sie es noch einmal");
    }
  }

  sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
  }

  sendMessage(senderId, data) {
    let msg = this.channel.push([{
      sender: senderId,
      message: data
    }]);
    msg.remove();
  }

  readMessage(data) {
    //console.log('read');
    if (!data) return;
    //console.log('data != null');
    let msg = JSON.parse(data.val()[0].message);
    let sender = data.val()[0].sender;
    if (sender != this.senderId) {
      console.log('Sender: ' + sender);
      if (msg.ice != undefined) {
        console.log('received ice message');
        this.peerConnection.addIceCandidate(new RTCIceCandidate(msg.ice));
      }
      else if (msg.sdp.type == "offer") {
        console.log('received offer');
        this.peerConnection
          .setRemoteDescription(new RTCSessionDescription(msg.sdp))
          .then(() => this.peerConnection.createAnswer())
          .then(answer => this.peerConnection.setLocalDescription(answer))
          .then(() =>
            this.sendMessage(
              this.senderId,
              JSON.stringify({sdp: this.peerConnection.localDescription})
            )
          );
      }
      else if (msg.sdp.type == "answer") {
        console.log('received answer');
        this.peerConnection.setRemoteDescription(new RTCSessionDescription(msg.sdp));
      } else {
        console.log('Could not parse message ' + data.val()[0].message);
      }
    }
  }

  showMe() {
    if(this.onDestroyAttempt){
      return;
    }
    navigator.mediaDevices
      .getUserMedia(this.constraints)
      .then(stream => this.doctorVideo.nativeElement.srcObject = stream)
      .then(stream => stream.getTracks().forEach(track => this.peerConnection.addTrack(track, stream)))
      .then(stream => this.doctorVideo.nativeElement.muted = true)
    //mute own audio track so that the user does not hear himself
  }

  showRemote() {
    if (this.peerConnection == null) {
      //this.initPeerConnection();
    }
    this.peerConnection
      .createOffer()
      .then(offer => this.peerConnection.setLocalDescription(offer))
      .then(() =>
        this.sendMessage(
          this.senderId,
          JSON.stringify({sdp: this.peerConnection.localDescription})
        )
      );
  }

  startVideoCall() {
    this.showRemote()
  }

  endVideoCall() {
    if (this.peerConnection.iceConnectionState != "closed" && this.peerConnection.connectionState != "closed") {
      this.peerConnection.close();
      this.peerConnection = null;
    }
    this.clearPatientStream();
    this.initPeerConnection();
  }

  clearPatientStream() {
    let src: MediaStream = this.patientVideo.nativeElement.srcObject;
    if (src != null && src.getVideoTracks().length > 0 && src.getAudioTracks().length > 0) {
      let videoTrack: MediaStreamTrack = src.getVideoTracks()[0];
      videoTrack.stop();
      src.removeTrack(videoTrack);
      let audioTrack: MediaStreamTrack = src.getAudioTracks()[0];
      audioTrack.stop();
      src.removeTrack(audioTrack);
    }
  }
  clearDoctorStream() {
    let src: MediaStream = this.doctorVideo.nativeElement.srcObject;
    if (src != null && src.getVideoTracks().length > 0 && src.getAudioTracks().length > 0) {
      let videoTrack: MediaStreamTrack = src.getVideoTracks()[0];
      videoTrack.stop();
      src.removeTrack(videoTrack);
      let audioTrack: MediaStreamTrack = src.getAudioTracks()[0];
      audioTrack.stop();
      src.removeTrack(audioTrack);
    }
    this.doctorVideo.nativeElement.srcObject = null;
  }

  takeScreenshot() {
    let canvas = this.videoScreenshot.nativeElement;
    canvas.getContext("2d").drawImage(this.patientVideo.nativeElement, 0, 0, 1280, 720);
    this.screenshotSrc = canvas.toDataURL("image/png");
    this.openDialog();
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(ScreenshotDialogComponent, {
      width: '550px',
      data: {imgSrc: this.screenshotSrc}
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }

  private hasGetUserMedia() {
    return !!(navigator.mediaDevices &&
      navigator.mediaDevices.getUserMedia);
  }

  ngOnDestroy() {
    this.onDestroyAttempt = true;
    if (this.peerConnection.iceConnectionState != "closed" && this.peerConnection.connectionState != "closed") {
      this.peerConnection.close();
    }
    this.clearPatientStream();
    this.clearDoctorStream();
  }


}
