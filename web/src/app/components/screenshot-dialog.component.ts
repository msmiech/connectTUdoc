import {Component, Inject} from "@angular/core";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";
import {saveAs as importedSaveAs} from "file-saver";

@Component({
  selector: 'screenshot-dialog',
  templateUrl: '../views/screenshot-dialog.component.html'
})
export class ScreenshotDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<ScreenshotDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) {}

  onNoClick(): void {
    this.dialogRef.close();
  }

  confirm(){
    let currentDate = new Date();
    let dateString = currentDate.getDate() + "-" + currentDate.getMonth() + "-" + currentDate.getFullYear() + "_" + currentDate.toLocaleTimeString();
    this.dialogRef.close();
    importedSaveAs(this.data.imgSrc, "Screenshot_" + dateString);
    alert("Screenshot wurde im \"Downloads\" Ordner gespeichert.");
  }
}
