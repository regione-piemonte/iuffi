import * as L from 'leaflet';

export class CustomMarker extends L.Marker {
    private data: any;

    constructor(latLng: L.LatLngExpression, data: any, options?: L.MarkerOptions) {
        super(latLng, options);
        this.data = data;
    }

    public getData(): any {
        return this.data;
    }

    public setData(data: any): void {
        this.data = data;
    }
}
