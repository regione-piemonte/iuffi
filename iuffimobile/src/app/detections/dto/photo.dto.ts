export enum FileStatus {
    TO_SYNCHRONIZE = 'TO_SYNCHRONIZE',
    SYNCHRONIZED = 'SYNCHRONIZED',
    TO_DELETE = 'TO_DELETE'
}

export interface PhotoDto {
    photoId: number;
    missionId: number;
    detectionId: number;
    visualInspectionId: number;
    sampleId: number;
    trapId: number;
    latitude: number;
    longitude: number;
    nativeFilePath: string;
    displayUrl: string;
    fileName: string;
    status: FileStatus;
}
