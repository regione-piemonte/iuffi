import { FileStatus, PhotoDto } from './../dto/photo.dto';

export class Photo {
    public photoId = 0;
    public missionId = 0;
    public detectionId = 0;
    public visualInspectionId = 0;
    public sampleId = 0;
    public trapId = 0;
    public latitude = 0;
    public longitude = 0;
    public nativeFilePath = '';
    public displayUrl = '';
    public fileName = '';
    public status: FileStatus = FileStatus.TO_SYNCHRONIZE;

    constructor(photoDto?: PhotoDto) {
        if (photoDto) {
            this.photoId = photoDto.photoId || 0;
            this.missionId = photoDto.missionId;
            this.detectionId = photoDto.detectionId;
            this.visualInspectionId = photoDto.visualInspectionId;
            this.sampleId = photoDto.sampleId;
            this.trapId = photoDto.trapId;
            this.latitude = photoDto.latitude;
            this.longitude = photoDto.longitude;
            this.nativeFilePath = photoDto.nativeFilePath;
            this.displayUrl = photoDto.displayUrl;
            this.fileName = photoDto.fileName;
            this.status = photoDto.status || FileStatus.TO_SYNCHRONIZE;
        }
    }
}
