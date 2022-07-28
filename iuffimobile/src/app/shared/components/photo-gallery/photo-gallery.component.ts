import { Component, Input } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { FileStatus } from '@app/detections/dto/photo.dto';
import { Photo } from '@app/detections/models/photo.model';
import { DetectionService } from '@app/detections/services/detection.service';
import { DeviceService } from '@core/device';
import { LoggerService } from '@core/logger';
import { FileManagerService } from '@shared/file-manager/services/file-manager.service';

@Component({
    selector: 'photo-gallery',
    templateUrl: './photo-gallery.component.html',
    styleUrls: ['./photo-gallery.component.scss'],
})
export class PhotoGalleryComponent {

    @Input('photos') public photos: Photo[] = [];
    @Input('loggerInfo') public loggerInfo = '';
    @Input('editable') public editable = true;

    constructor(
        public fileManagerService: FileManagerService,
        private logger: LoggerService,
        public sanitizer: DomSanitizer,
        private deviceService: DeviceService,
        private detectionService: DetectionService,
    ) {

    }

    public viewPhoto(photo: Photo): void {
        this.logger.debug(this.loggerInfo + '::viewPhoto');
        this.fileManagerService.openImage(photo.nativeFilePath);
    }

    public deletePhoto(index: number): void {
        this.logger.debug(this.loggerInfo + '::deletePhoto');
        this.deviceService.alert('DELETE_PHOTO_MESSAGE', {
            handler: () => {
                if (this.photos[index].status === FileStatus.TO_SYNCHRONIZE) {
                    this.photos.splice(index, 1);
                }
                else if (this.photos[index].status === FileStatus.SYNCHRONIZED) {
                    this.detectionService.removePhoto(this.photos[index]).then(() => {
                        this.photos.splice(index, 1);
                    }).catch(
                        err => {
                            this.logger.debug(err);
                        }
                    )
                }

            }
        })

    }
}
