import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/internal/Observable';
import {Router} from '@angular/router';
import {environment} from '../../environments/environment';
import {HttpClient, HttpErrorResponse, HttpHeaders, HttpParams} from '@angular/common/http';
import {catchError, retry} from 'rxjs/operators';
import {AngularFireAuth} from '@angular/fire/auth';
import * as firebase from 'firebase';
import {MedicalWorker} from '../models/medicalworker';
import {throwError} from 'rxjs';
import {EThree} from '@virgilsecurity/e3kit';
import {HashAlgorithm, VirgilCrypto} from 'virgil-crypto';
import {Office} from '../models/office';

@Injectable()
export class AccessService {
  static CLOUD_FUNCTION_ENDPOINT = 'https://us-central1-medconnect-5ac1b.cloudfunctions.net/api/virgil-jwt';
  static OFFICE_ENDPOINT = '/office/searchOfficeByLoggedInMedicalUser';
  private readonly _serverProtocol: string = environment.serverProtocol; // default is "http"
  private _serverIp: string = environment.serverIp; // default is localhost
  private _serverPort: number = environment.serverPort; // backend port (9100)
  private _apiPrefixEnabled: boolean = environment.apiPrefixEnabled; // backend port (9100)
  // private _user: Observable<User>; // not needed?
  private _userDetails: firebase.User = null;
  private _tokenID: string;
  private _UID: string;
  private _medicalWorker: MedicalWorker;
  private _office: Office;
  private _errorMsg: string;
  private eThree;
  private eThreePromise;



  constructor(private firebaseAuth: AngularFireAuth, private router: Router, private http: HttpClient) {
    this._medicalWorker = null;
  }

  get firebaseAuthObject(): AngularFireAuth {
    return this.firebaseAuth;
  }

  get serverIp(): string {
    return this._serverIp;
  }

  set serverIp(value: string) {
    this._serverIp = value;
  }

  get serverPort(): number {
    return this._serverPort;
  }

  set serverPort(value: number) {
    this._serverPort = value;
  }


  get medicalWorker(): MedicalWorker {
    return this._medicalWorker;
  }

  get httpOptions(): { headers: HttpHeaders } {
    return {
      headers: new HttpHeaders({
        'X-Firebase-Auth': this.tokenID
      })
    };
  }

  get tokenID(): string {
    return this._tokenID;
  }

  get UID(): string {
    return this._UID;
  }

  /**
   * Builds a complete URL path with current server access config
   * @param path Path after protocol, IP, port; e.g. "/index"
   * @returns Complete URL
   */
  buildUrl(path: string) {
    let base = this._serverProtocol + '://' + this._serverIp + ':' + this._serverPort;
    if (this._apiPrefixEnabled === true) {
      base += '/api';
    }
    return base + path;
  }

  getRequest<T>(path: string): Observable<T> {
    return this.http.get<T>(this.buildUrl(path), this.httpOptions).pipe(retry(3), catchError(this.handleError));
  }


  getRequestWithParams<T>(path: string, params: HttpParams): Observable<T> {
    return this.http.get<T>(this.buildUrl(path), {headers: this.httpOptions.headers, params: params}).pipe(retry(3));
  }

  getRequestWithResponseType<T>(path: string, responseContentType: string): Observable<T> {
    const options = Object.assign({}, this.httpOptions);
    options['responseType'] = responseContentType;
    return this.http.get<T>(this.buildUrl(path), options).pipe(retry(3), catchError(this.handleError));

  }

  postRequest<T>(path: string, body: any | null): Observable<T> {
    console.log('New Insert: ' + path);
    console.log('Object: ' + JSON.stringify(body));
    return this.http.post<T>(this.buildUrl(path), body, this.httpOptions).pipe(catchError(this.handleError));
  }

  postRequestWithTextBody(path: string, body: any | null) {
    console.log('New Insert: ' + path);
    console.log('Object: ' + JSON.stringify(body));
    const options = {...this.httpOptions};
    options.headers.append('Content-Type', 'text/plain');
    this.http.post(this.buildUrl(path), body, options).subscribe(msg => console.log('Response: ' + msg));
  }


  deleteRequest<T>(path: string): Observable<T> {
    return this.http.delete<T>(this.buildUrl(path), this.httpOptions).pipe(retry(3), catchError(this.handleError));
  }

  putRequest<T>(path: string, body: any | null): Observable<T> {
    console.log('New Update: ' + path);
    console.log('Object: ' + JSON.stringify(body));
    return this.http.put<T>(this.buildUrl(path), body, this.httpOptions).pipe(retry(3), catchError(this.handleError));
  }

  encrypt(text: string, identities: string[]): Promise<string> | null {
    if (this.eThree == null) { return null; }
    /*
    this.getRequest(OfficeService.OFFICE_PATH + OfficeService.LOGGED_USER_OFFICE)
      .subscribe(office => {
        for (let i = 0; i < office.officeWorkers.length; i++) {
          identities.push(office.officeWorkers[i].uid);
        }
        console.log(JSON.stringify(identities));
        return this.eThree.lookupPublicKeys(identities)
          .then((keys) => {
            console.log('lookupPublicKeys success');
            return this.eThree.encrypt(text, keys);
          })
          .catch(e => console.error('Error at lookupPublicKeys: ' + e));
    });*/
    if (this._office == null) { return null; }
    for (let i = 0; i < this._office.officeWorkers.length; i++) {
      identities.push(this._office.officeWorkers[i].uid);
    }
    console.log(JSON.stringify(identities));
    return this.eThree.lookupPublicKeys(identities)
      .then((keys) => {
        console.log('lookupPublicKeys success');
        return this.eThree.encrypt(text, keys);
      })
      .catch(e => console.error('Error at lookupPublicKeys: ' + e));
  }

  encryptBuffer(data: Buffer, identities: string[]): Promise<Buffer> | null {
    if (this.eThree == null) { return null; }

    const encryptedData = null;

    identities.push(this.eThree.identity);
    console.log(JSON.stringify(identities));
    return this.eThree.lookupPublicKeys(identities)
      .then((keys) => {
        return this.eThree.encrypt(data, keys);
      })
      .catch(e => console.error('Error at lookupPublicKeys: ' + e));
  }

  decrypt(encryptedText: string, indetity: string): Promise<string> | null {
    if (this.eThree == null) { return null; }

    return this.eThree.lookupPublicKeys(indetity)
      .then((key) => {
        return this.eThree.decrypt(encryptedText, key);
      })
      .catch(e => console.error('Error at lookupPublicKeys: ' + e));
  }

  decryptBuffer(encryptedData: Buffer, indetity: string): Promise<Buffer> | null {
    if (this.eThree == null) { return null; }

    return this.eThree.lookupPublicKeys(indetity)
      .then((key) => {
        return this.eThree.decrypt(encryptedData, key);
      })
      .catch(e => console.error('Error at lookupPublicKeys: ' + e));
  }

  login(email: string, password: string): Promise<string> {
    // Initialization callback that returns a Virgil JWT string from the E3kit firebase function
    async function fetchToken(authToken) {
      const CLOUD_FUNCTION_ENDPOINT = AccessService.CLOUD_FUNCTION_ENDPOINT;
      const response = await fetch(
        CLOUD_FUNCTION_ENDPOINT,
        {
          headers: new Headers({
            'Content-Type': 'application/json',
            Authorization: `Bearer ${authToken}`,
          })
        },
      );
      if (!response.ok) {
        throw new Error(`Error code: ${response.status} \nMessage: ${response.statusText}`);
      }
      return response.json().then(data => data.token);

    }

// Once Firebase user authenticated, we wait for eThree client initialization
    this.eThreePromise = new Promise((resolve, reject) => {
      firebase.auth().onAuthStateChanged(user => {
        if (user) {
          const getToken = () => user.getIdToken().then(fetchToken);
          this.eThreePromise = EThree.initialize(getToken);
          this.eThreePromise.then(resolve).catch(reject);
        }
      });
    });

    const promise = new Promise<string>(((resolve, reject) => {
      this.firebaseAuth
        .auth
        .signInWithEmailAndPassword(email, password)
        .then(value => {
          console.log('Login successfully!', value);
          console.log('Logged in user:', value.user);




          this._userDetails = value.user;
          this._UID = value.user.uid;
          value.user.getIdToken().then((token) => {
            this._tokenID = token;
            this.getRequest<Office>(AccessService.OFFICE_ENDPOINT)
              .subscribe((office) => {
              console.log('Office ID set to: ' + office.id);
              if (office) {
                this._office = office;
                this._medicalWorker = office.officeWorkers.find(value1 => value1.uid == this.UID);
                this.eThreePromise.then(eThree => {
                  this.eThree = eThree;
                  console.log('eThree identity: ' + eThree.identity);

                  const passhash: string = email + password + (email.length + password.length);
                  const crypto = new VirgilCrypto();
                  // passhash = crypto.calculateHash(passhash, HashAlgorithm.SHA512).toString();
                  // console.log(crypto.calculateHash(passhash, HashAlgorithm.SHA512).toString());
                  eThree.register()
                  // doctor registers first time
                    .then(() => {
                      console.log('Success on register');
                      eThree.backupPrivateKey(passhash)
                        .then(() => {
                          console.log('Success on backupPrivateKey');
                          resolve('');
                        })
                        .catch(e => {
                          console.error('Error at backupPrivateKey ' + e);
                          reject(e);
                        });
                    })
                    // doctor is already registered
                    .catch((e) => {
                      console.error('Error at register ' + e);
                      eThree.restorePrivateKey(passhash)
                        .then(() => {
                          console.log('Success on restorePrivateKey');
                          resolve('');
                        })
                        .catch(e => {
                          console.error('Error at restorePrivateKey ' + JSON.stringify(e));
                          // Can't load private key because there is an private key in the local storage
                          if (e.name == 'PrivateKeyAlreadyExistsError') {
                            console.log('Private key already exists -> delete and restore');
                            eThree.cleanup().then(() => {
                              console.log('Success on cleanup');
                              eThree.restorePrivateKey(passhash)
                                .then(() => {
                                  console.log('Success on restorePrivateKey');
                                  resolve('');
                                })
                                .catch(e => {
                                  console.error('Error at restorePrivateKey ' + e);
                                  console.error('Error at restorePrivateKey - will die now!');
                                  reject(e);
                                });
                            }).catch(e => {
                              console.error('Error at cleanup ' + e);
                              reject(e);
                            });
                          } else
                          //
                          if (e.name == 'WrongKeyknoxPasswordError') {
                            console.error('Ich weiß nicht genau was ich mit dem Error anfangen soll... ' +
                              'keine andere Methode von eThree kann ihn beheben, alle führen zum selben Fehler...');
                            reject(e);
                          } else {
                            eThree.hasLocalPrivateKey().then(hasPrivateKey => {
                              if (hasPrivateKey) {
                                eThree.backupPrivateKey(passhash)
                                  .then(() => {
                                    console.log('Success on backupPrivateKey');
                                    resolve('');
                                  })
                                  .catch(e => {
                                    console.error('Error at backupPrivateKey ' + e);
                                    reject(e);
                                  });
                              } else {
                                eThree.rotatePrivateKey()
                                  .then(() => {
                                    console.log('Success on rotatePrivateKey');
                                    eThree.backupPrivateKey(passhash)
                                      .then(() => {
                                        console.log('Success on backupPrivateKey');
                                        resolve('');
                                      })
                                      .catch(e => {
                                        console.error('Error at backupPrivateKey ' + e);
                                        reject(e);
                                      });
                                  })
                                  .catch(e => {
                                    console.error('Error at rotatePrivateKey ' + e);
                                    reject(e);
                                  });
                              }
                            });
                          }
                        });
                    });
                })
                .catch((error) => {
                  console.log('error: ' + error);
                });
              } else {
                this._medicalWorker = null;
                reject('Could not find login in MedConnect-Database');
              }
            }, (err) => {
              reject(err['message']);
            });
          });
        })
        .catch(err => {
          console.log('Login went wrong:', err.message);
          reject(err.message);
        });
    }));
    return promise;
  }

  logout() {
    if (this.eThree != null) { this.eThree.cleanup(); }
    return this.firebaseAuth
      .auth
      .signOut();
  }

  signup(email: string, password: string) {
    this.firebaseAuth
      .auth
      .createUserWithEmailAndPassword(email, password)
      .then(value => {
        console.log('SignUp success!', value);
      })
      .catch(err => {
        console.log('SignUp went wrong:', err.message);
      });
  }

  isLoggedIn() {
    if (this._userDetails == null || this._medicalWorker == null) {
      return false;
    } else {
      return true;
    }
  }

  handleError(error: HttpErrorResponse) {
    // switch error codes
    // custom errors
    if (error.status >= 400 && error.status <= 500) {

      this._errorMsg = 'Es ist ein Fehler aufgetreten: ' + error.error.message;
      console.error(this._errorMsg);
      return throwError(this._errorMsg);

    } else if (error.status >= 500 && error.status <= 600) {

      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong,
      console.error(
        `Backend returned code ${error.status}, ` +
        `body was: ${error.error}`);
      return throwError('Es wurde ein Problem in der Netzwerkommunikation festgestellt. Bitte kontatieren sie ihren Systemadministrator.');

    } else {

      return throwError(
        'Es ist ein schwerwiegender Fehler aufgetreten. Bitte versuchen sie es später wieder.');
    }

    /*if (error.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      this._errorMsg = 'Es ist ein Fehler aufgetreten: ' + error.error.message;
      console.error( this._errorMsg);
      return throwError( this._errorMsg);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong,
      console.error(
        `Backend returned code ${error.status}, ` +
        `body was: ${error.error}`);
      return throwError('Es wurde ein Problem in der Netzwerkommunikation festgestellt. Bitte kontatieren sie ihren Systemadministrator.');
    }
    // return an observable with a user-facing error message
    return throwError(
      'Es ist ein schwerwiegender Fehler aufgetreten. Bitte versuchen sie es später wieder.');*/
  }

}
