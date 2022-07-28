import { Injectable } from '@angular/core';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { DeviceService } from '@core/device/services/device.service';
import { LoggerService } from '@core/logger/services/logger.service';
import { FileOpener } from '@ionic-native/file-opener/ngx';
import { File, FileEntry } from '@ionic-native/file/ngx';
import { ImagePicker } from '@ionic-native/image-picker/ngx';

declare const window: any;

@Injectable()
export class FileManagerService {
    public downloadDir: any;
    public dataDirectory: any;
    public fileName: any;
    constructor(
        private logger: LoggerService,
        private file: File,
        private deviceService: DeviceService,
        private fileOpener: FileOpener,
        private sanitizer: DomSanitizer,
        // private fileChooser: FileChooser,
        // private filePicker: IOSFilePicker,
        private imagePicker: ImagePicker,
        // private diagnostic: Diagnostic
    ) {
        if (this.deviceService.isIos()) {
            this.dataDirectory = this.file.documentsDirectory;
        }
        else {
            this.dataDirectory = this.file.externalDataDirectory;
        }
    }

    public getDoc(blob: Blob, name: string, vcExtension: string | null): void {
        const format = this.getFormat(vcExtension);
        if (this.deviceService.isCordova()) {
            name = name.replace(/ /g, '');

            this.file.writeFile(this.dataDirectory, name, blob, { replace: true }).then((res: any) => {
                this.fileOpener.open(
                    res.toURL(),
                    format
                ).then((res: any) => {
                }).catch((err: any) => {
                    console.log('open error')
                });
            }).catch((err: any) => {
                console.log('open error')
            });
        } else {
            const fileURL = URL.createObjectURL(blob);
            window.open(fileURL);
        }
    }
    public getDocFromBase64(base64: string, name: string, vcExtension: string | null): void {
        const binaryString = window.atob(base64);
        const len = binaryString.length;
        const bytes = new Uint8Array(len);
        for (let i = 0; i < len; i++) {
            bytes[i] = binaryString.charCodeAt(i);
        }
        const format = this.getFormat(vcExtension);
        if (this.deviceService.isCordova()) {
            name = name.replace(/ /g, '');
            name = name.replace('\\', '');
            this.file.writeFile(this.dataDirectory, name, bytes.buffer, { replace: true }).then((res: any) => {
                this.fileOpener.open(
                    res.toURL(),
                    format
                ).then((res: any) => {
                }).catch((err: any) => {
                    console.log('open error')
                });
            }).catch((err: any) => {
                console.log('open error')
            });
        } else {
            const fileURL = URL.createObjectURL(bytes.buffer);
            window.open(fileURL);
        }
    }

    public openImage(filePath: any): void {
        this.deviceService.hideLoading();
        this.fileOpener.open(filePath, 'image/jpeg')
            .then(() => this.logger.debug('File is opened'))
            .catch((e: any) => {
                this.logger.error(e);
                this.deviceService.alert('lbl_alert_generic_error')
            });
    }

    public saveImageFromBase64(base64: string, fileName: string, contentType = 'image/jpeg',): Promise<FileEntry> {
        return new Promise((resolve, reject) => {
            const blob: Blob = this.b64toBlob(base64, contentType);
            this.file
                .writeFile(this.file.dataDirectory, fileName, blob, { replace: true })
                .then((res: FileEntry) => {
                    this.logger.debug(res);
                    resolve(res);
                })
                .catch((err: any) => {
                    reject();
                    this.logger.debug(err);
                });
        });
    }

    public openFile(filePath: any, extension: string): void {
        const format = this.getFormat(extension)

        this.fileOpener.open(filePath, format)
            .then(() => this.logger.debug('File is opened'))
            .catch((e: any) => {
                this.logger.error(e);
                this.deviceService.hideLoading();
                this.deviceService.alert('lbl_alert_generic_error')
            });
    }

    public getFormat(vcExtension: string | null): string {
        let format = 'application/pdf'
        if (vcExtension) {
            vcExtension = vcExtension.replace('.', '');
            format = vcExtension;
        }
        if (vcExtension == 'pdf') {
            format = 'application/pdf'
        }
        else if (vcExtension == 'doc') {
            format = 'application/msword'
        } else if (vcExtension == 'docx') {
            format = 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'
        }
        else if (vcExtension == 'xls') {
            format = 'application/vnd.ms-excel'
        }
        else if (vcExtension == 'xlsx') {
            format = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
        } else if (vcExtension == 'txt') {
            format = 'text/txt'
        } else if (vcExtension == 'jpg' || vcExtension == 'jpeg') {
            format = 'image/jpeg'
        } else if (vcExtension == 'png') {
            format = 'image/png'
        }
        return format
    }

    /**
     * Convert an image
     * to a base64 url
     * @param  {String}   path
     * @param  {Function} callback
     */
    public getFileContentAsBase64(path: string): Promise<any> {
        return new Promise((resolve, reject) => {
            window.resolveLocalFileSystemURL(path, (FileEntry: any) => {
                // FileEntry.getMetadata((metadata: any) => {
                // if (metadata.size > 1048576) {
                //     this.deviceService.alert('lbl_file_dimensions_error')
                //     reject();
                // }
                // else {
                FileEntry.file((file: any) => {
                    const type = file.type;
                    const reader = new FileReader();
                    reader.onloadend = (evt: any) => {
                        const fileEncoded: any = evt.target.result
                        resolve({ file: fileEncoded, type: type });
                    };
                    reader.readAsDataURL(file);
                    reader.onerror = e => {
                        reject();
                    };
                });
                // }
                // });

            });
        })
    }

    public b64toBlob(b64Data: string, contentType = '', sliceSize = 512): Blob {
        const byteCharacters = atob(b64Data);
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

        const blob = new Blob(byteArrays, { type: contentType });
        return blob;
    }

    public readFile(imageDataURI: any): Promise<any> {
        return new Promise((resolve, reject) => {
            window.resolveLocalFileSystemURL(imageDataURI, (FileEntry: any) => {

                FileEntry.file((file: any) => {
                    const type = file.type;
                    const reader = new FileReader();
                    reader.onloadend = () => {

                        const imgBlob = new Blob([reader.result as ArrayBuffer], {
                            type: file.type
                        });
                        resolve({ file: imgBlob, type: type });
                    };
                    reader.readAsArrayBuffer(file);
                    reader.onerror = e => {
                        reject();
                    };
                });

            });
        });
    }

    public readFileAsBase64(file: any): Promise<any> {
        return new Promise((resolve, reject) => {
            const type = file.type;
            const reader = new FileReader();
            reader.onloadend = () => {

                const imgBlob = new Blob([reader.result as any], {
                    type: file.type
                });
                resolve({ file: imgBlob, type: type });
            };
            reader.readAsDataURL(file);
            reader.onerror = e => {
                reject();
            };
        });
    }

    public readFileAsByteArray(file: any): Promise<any> {
        return new Promise((resolve, reject) => {
            const type = file.type;
            const reader = new FileReader();
            reader.onloadend = () => {
                const imgBlob = new Blob([new Uint8Array(reader.result as ArrayBuffer)], { type: file.type });

                resolve({ file: imgBlob, type: type });
            };
            reader.readAsArrayBuffer(file);
            reader.onerror = e => {
                reject();
            };
        });
    }

    public getImgContent(imgFile: any): SafeUrl {
        return this.sanitizer.bypassSecurityTrustUrl(imgFile);
    }

    // public selectFile(): Promise<string> {
    //     return new Promise((resolve, reject) => {
    //         this.diagnostic.isExternalStorageAuthorized().then(
    //             authorized => {
    //                 if (authorized) {
    //                     if (!this.deviceService.isIos()) {
    //                         this.fileChooser.open()
    //                             .then(uri => {
    //                                 resolve(uri)
    //                             }).catch(error => {
    //                                 reject(error)
    //                             });
    //                     } else {
    //                         this.filePicker.pickFile()
    //                             .then(uri => console.log(uri))
    //                             .catch(err => console.log('Error', err));
    //                     }
    //                 } else {
    //                     // Ask for permission from the user
    //                     this.diagnostic.requestExternalStorageAuthorization().then(
    //                         request => {
    //                             if (request !== this.diagnostic.permissionStatus.GRANTED) {
    //                                 this.deviceService.alert('lbl_permission_alert');
    //                             } else {
    //                                 if (!this.deviceService.isIos()) {
    //                                     this.fileChooser.open()
    //                                         .then(uri => {
    //                                             resolve(uri)
    //                                         }).catch(error => {
    //                                             reject(error)
    //                                         });
    //                                 } else {
    //                                     this.filePicker.pickFile()
    //                                         .then(uri => console.log(uri))
    //                                         .catch(err => console.log('Error', err));
    //                                 }
    //                             }
    //                         }
    //                     );
    //                 }
    //             }
    //         );

    //     })
    // }

    public selectImage(): Promise<string> {
        const options = {
            maximumImagesCount: 1,
            quality: 20,
        };
        return new Promise((resolve, reject) => {
            this.imagePicker.getPictures(options).then((result: any) => {

                resolve(result[0]);

            }, (err: string) => {
                console.log('errore:' + err)
            });
        })
    }
}
