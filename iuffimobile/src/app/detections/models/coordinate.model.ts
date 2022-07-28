export class Coordinate {
    public latitudine = 0;
    public longitudine = 0;
    constructor(latitude?: number, longitude?: number) {
        this.latitudine = latitude ? latitude : 0;
        this.longitudine = longitude ? longitude : 0;
    }
}
