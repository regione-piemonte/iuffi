export class Point {
    public latitude: number;
    public longitude: number;

    constructor(latitude: number, longitude: number) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public toArray(): number[] {
        return [this.longitude, this.latitude];
    }
}
