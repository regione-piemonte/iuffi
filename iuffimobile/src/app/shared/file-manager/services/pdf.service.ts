import { Injectable } from '@angular/core';
import { DeviceService } from '@core/device';
import { BackgroundGeolocation } from '@ionic-native/background-geolocation/ngx';
import { BackgroundMode } from '@ionic-native/background-mode/ngx';
import { FileOpener } from '@ionic-native/file-opener/ngx';
import { File } from '@ionic-native/file/ngx';
import { Platform } from '@ionic/angular';

@Injectable()
export class PDFService {
    constructor(private opener: FileOpener, private file: File, private deviceService: DeviceService, private platform: Platform,
        private backgroundMode: BackgroundMode,
        private backgroundGeolocation: BackgroundGeolocation) { }

    public saveAndOpenPdf(b64Data: string, filename: string): void {
        this.deviceService.showLoading();

        this.platform.ready().then(() => {
            this.backgroundMode.disable();
            this.backgroundGeolocation.stop();
            const writeDirectory = this.deviceService.isIos() ? this.file.dataDirectory : this.file.externalDataDirectory;
            setTimeout(() => {
                const fileConvert = this.convertBase64ToBlob(b64Data, 'data:application/pdf;base64');
                this.file
                    .writeFile(writeDirectory, filename, fileConvert, { replace: true })
                    .then(() => {
                        this.deviceService.hideLoading();
                        this.opener.showOpenWithDialog(writeDirectory + filename, 'application/pdf')
                            .then(() => {
                                console.log('File is opened');
                                setTimeout(() => { this.backgroundMode.enable(); },1000);
                            })
                            .catch(() => {
                                this.deviceService.hideLoading();
                                console.error('Error open pdf File');
                                this.deviceService.alert('OPEN_PDF_ERROR');
                            });
                    })
                    .catch(error => {
                        console.error('Error writing pdf file' + error);
                        this.deviceService.hideLoading();
                        this.deviceService.alert('OPEN_PDF_ERROR');
                    });
            }, 1000);
        });
    }

    public readDoc(blob: Blob, name: string, vcExtension: string | null): void {
        const format = this._getFormat(vcExtension);
        let dataDirectory: string;
        let newBlob: Blob;
        if (this.deviceService.isCordova()) {
            newBlob = new Blob([blob], { type: format });
            name = name.replace(/ /g, '');
            if (this.deviceService.isIos()) {
                dataDirectory = this.file.documentsDirectory;
            } else {
                dataDirectory = this.file.externalDataDirectory;
            }
            this.file
                .writeFile(dataDirectory, name, blob, { replace: true })
                .then((res: any) => {
                    this.opener
                        .open(res.toURL(), format)
                        .then((res: any) => { })
                        .catch((err: any) => {
                            console.log('open error');
                        });
                })
                .catch((err: any) => {
                    console.log('open error');
                });
        } else {

        }
    }

    private _getFormat(vcExtension: string | null): string {
        let format = 'application/pdf';
        if (vcExtension) {
            vcExtension = vcExtension.replace('.', '');
            format = vcExtension;
        }
        if (vcExtension == 'pdf') {
            format = 'application/pdf';
        } else if (vcExtension == 'doc') {
            format = 'application/msword';
        } else if (vcExtension == 'docx') {
            format = 'application/vnd.openxmlformats-officedocument.wordprocessingml.document';
        } else if (vcExtension == 'xls') {
            format = 'application/vnd.ms-excel';
        } else if (vcExtension == 'xlsx') {
            format = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';
        } else if (vcExtension == 'txt') {
            format = 'text/txt';
        } else if (vcExtension == 'jpg' || vcExtension == 'jpeg') {
            format = 'image/jpeg';
        } else if (vcExtension == 'png') {
            format = 'image/png';
        }
        return format;
    }

    public convertBase64ToBlob(b64Data: string, contentType: string): Blob {
        contentType = contentType || '';
        const sliceSize = 512;
        b64Data = b64Data.replace(/^[^,]+,/, '');
        b64Data = b64Data.replace(/\s/g, '');
        const byteCharacters = window.atob(b64Data);
        const byteArrays = [];

        for (let offset = 0; offset < byteCharacters.length; offset += sliceSize) {
            const slice = byteCharacters.slice(offset, offset + sliceSize);
            const byteNumbers = new Array(slice.length);
            for (let i = 0; i < slice.length; i++) {
                byteNumbers[i] = slice.charCodeAt(i);
            }
            const byteArray = new Uint8Array(byteNumbers);
            byteArrays.push(byteArray);
        }

        return new Blob(byteArrays, { type: contentType });
    }
}
