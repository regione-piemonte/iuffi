import { ApiService } from './../../../core/api/services/api.service';
import { AuthService } from '@core/auth';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { LoggerService } from '@core/logger';
import * as L from 'leaflet';
import { Map } from 'leaflet';
import { Coordinate } from '@app/detections/models/coordinate.model';
import { CustomMarker } from '@app/detections/models/trap-marker.model';
import 'leaflet-wms-header';
import { ENV } from '@env';
const iconRetinaUrl = './assets/marker-icon-2x.png';
const iconUrl = './assets/marker-icon.png';
const shadowUrl = './assets/marker-shadow.png';
const iconDefault = L.icon({
    iconRetinaUrl,
    iconUrl,
    shadowUrl,
    iconSize: [25, 41],
    iconAnchor: [12, 41],
    popupAnchor: [1, -34],
    tooltipAnchor: [16, -28],
    shadowSize: [41, 41]
});
const goldIcon = new L.Icon({
    iconUrl: './assets/imgs/pin-02.png',
    iconSize: [45, 45],
    iconAnchor: [12, 41],
    popupAnchor: [1, -34],
    shadowSize: [41, 41]
});
const redIcon = new L.Icon({
    iconUrl: './assets/imgs/pin-01.png',
    iconSize: [45, 45],
    iconAnchor: [12, 41],
    popupAnchor: [1, -34],
    shadowSize: [41, 41]
});
const greyIcon = new L.Icon({
    iconUrl: './assets/imgs/pin-03.png',
    iconSize: [45, 45],
    iconAnchor: [12, 41],
    popupAnchor: [1, -34],
    shadowSize: [41, 41]
});
L.Marker.prototype.options.icon = iconDefault;

export interface MapData {
    position: Coordinate;
    radius: string;
}
@Component({
    selector: 'leaflet-map-component',
    templateUrl: 'leaflet-map.component.html',
    styleUrls: ['leaflet-map.component.scss']
})
export class LeafletMapComponent {
    public map!: Map
    private originalLayer: L.TileLayer;
    private satelliteLayer: L.TileLayer;
    private wmsLayerUrl: string | null;
    private wmsLayer!: L.TileLayer.WMSHeader;
    public currentPositionMarker!: L.Marker;
    @Input() public currentPosition!: Coordinate;
    @Input() public markers?: any;
    @Input() public draggable = false;
    @Output() public onLoad: EventEmitter<MapData> = new EventEmitter();
    @Output() public onDragEnd: EventEmitter<MapData> = new EventEmitter();
    @Output() public onZoomEnd: EventEmitter<MapData> = new EventEmitter();
    @Output() public onMarkerSelected: EventEmitter<any> = new EventEmitter();
    @Output() public onCurrentPositionChange: EventEmitter<Coordinate> = new EventEmitter();

    constructor(
        private logger: LoggerService,
        private authService: AuthService,
        private apiService: ApiService
    ) {
        let accessToken = this.authService.getAccessToken();
        this.wmsLayerUrl = this.apiService.getApiUrl('wmsService');
        if (ENV.devMode) {
            // CSI DEMO 20
            accessToken = 'eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiI2NzcyYzIzZC05NTBiLTRmODgtOTVmMy1hNThlZTBkM2QzZjUiLCJpYXQiOjE2MDcwNzg1NjEsImV4cCI6MTYwNzA4MDM2MSwiaXNzIjoiaXVmZmlhdXRoIC0gQ1NJIFBpZW1vbnRlIiwiYXVkIjoiSVVGRkkgbW9iaWxlIHN5c3RlbSIsInN1YiI6IkFBQUFBQTAwQjc3QjAwMEYiLCJub21lIjoiQ1NJIFBJRU1PTlRFIiwiY29nbm9tZSI6IkRFTU8gMjAifQ.geYrzIDcv4OytDkRZpR-332m5M7qqnIELhgT0sxC8zootreE51CTIdtlkIQ-2Nl6r4KzhBwqmT-iNdNxYKfcpw';
        }
        this.originalLayer = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { maxZoom: 19 });
        this.satelliteLayer = L.tileLayer('https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}', { maxZoom: 19 });
        if (this.wmsLayerUrl) {
            this.wmsLayer = L.TileLayer.wmsHeader(
                this.wmsLayerUrl,
                {
                    layers: 'regp_agea_2018',
                    format: 'image/png',
                    transparent: true,
                },
                [
                    { header: 'Authorization', value: 'Basic ' + btoa(ENV.basicAuth.userid + ':' + ENV.basicAuth.password) },
                    { header: 'Token', value: 'Bearer ' + accessToken }
                ],
                undefined
            );
        }

        // this.wmsLayer = L.tileLayer.wms('http://tst-agrigeoweb.csi.it/wmspiemonteagri/service', {
        //     layers: 'regp_agea_2018',
        //     format: 'image/png',
        //     transparent: true,
        //     maxZoom: 19
        // });

    }

    ngOnInit(): void {

        if (ENV.devMode) {
            this.currentPosition.latitudine = 45.240884;
            this.currentPosition.longitudine = 8.260742;
        }
        // In assenza di coordinate imposto la posizione sulle coordinate di Torino
        if (this.currentPosition.latitudine === 0 || this.currentPosition.longitudine === 0) {
            this.currentPosition.latitudine = 45.067692;
            this.currentPosition.longitudine = 7.660817;
        }
        if (this.map) {
            //to remove any initialization of a previous map
            this.map.off();
            this.map.remove();
        }

        this.map = L.map('map', {
            zoom: 15,
            attributionControl: false,
            renderer: L.canvas(),
            layers: [this.satelliteLayer, this.wmsLayer]
        });

        this.currentPositionMarker = L.marker([this.currentPosition.latitudine, this.currentPosition.longitudine], { icon: goldIcon, draggable: this.draggable, autoPan: this.draggable })

        this.currentPositionMarker.on('dragend', (e: L.DragEndEvent) => this._onDragEnd(e));

        this.currentPositionMarker.addTo(this.map);

        const baseMaps = {
            Original: this.originalLayer,
            Satellite: this.satelliteLayer,
            WMS: this.wmsLayer
        };

        L.control.layers(baseMaps).addTo(this.map);

        this.map.on('load', () => this._load());

        this.map.on('zoomend', () => this._zoomEnd());

        this.map.on('dragend', () => this._dragEnd());

        this.map.on('click', (event: L.LeafletMouseEvent) => this._onClick(event));

        setTimeout(() => {
            this.map.invalidateSize();
        }, 500);

        this.map.setView([this.currentPosition.latitudine, this.currentPosition.longitudine]);

    }

    ngOnChanges(): void {
        this.logger.debug('ON CHANGES');
        if (this.markers) {
            this._addMarkersToMap(this.markers);
        }

    }

    private _load(): void {
        this.logger.debug('LOAD');
        const mapData: MapData = {
            position: this.currentPosition,
            radius: '1500'
        }
        this.onLoad.emit(mapData);
    }

    private _dragEnd(): void {
        this.logger.debug('DRAG END');
        const center = this.map.getCenter();
        const bounds = this.map.getBounds();
        const radius = center.distanceTo(bounds.getNorthEast()).toFixed(0);
        const newPosition = new Coordinate(center.lat, center.lng);
        const mapData: MapData = {
            position: newPosition,
            radius: radius
        }
        this.onDragEnd.emit(mapData);
    }

    private _zoomEnd(): void {
        this.logger.debug('ZOOM END');
        const center = this.map.getCenter();
        const bounds = this.map.getBounds();
        const radius = center.distanceTo(bounds.getNorthEast()).toFixed(0);
        const newPosition = new Coordinate(center.lat, center.lng);
        const mapData: MapData = {
            position: newPosition,
            radius: radius
        }
        this.onZoomEnd.emit(mapData);
    }

    private _onClick(event: L.LeafletMouseEvent): void {
        this.logger.debug('ON CLICK');
        this.currentPositionMarker.remove();
        this.currentPositionMarker = L.marker([event.latlng.lat, event.latlng.lng], { icon: goldIcon, draggable: this.draggable, autoPan: this.draggable });
        this.map.panTo(this.currentPositionMarker.getLatLng());
        this._updatePosition(this.currentPositionMarker.getLatLng());
        this.currentPositionMarker.on('dragend', (e: L.DragEndEvent) => this._onDragEnd(e));
        this.currentPositionMarker.addTo(this.map);
    }

    private _onDragEnd(event: L.DragEndEvent): void {
        this.logger.debug('MARKER DRAG END');
        const marker = event.target;
        const markerPosition = marker.getLatLng();
        this.map.panTo(markerPosition);
        this._updatePosition(markerPosition);
    }

    private _updatePosition(position: L.LatLng): void {
        this.logger.debug('UPDATE POSITION');
        const coords = new Coordinate(position.lat, position.lng);
        this.onCurrentPositionChange.emit(coords);
    }

    private _addMarkersToMap(markers: any[]): void {
        markers.map(m => {
            new CustomMarker([m.latitudine, m.longitudine], m, {icon: m.dataRimozione? greyIcon : redIcon}).addTo(this.map).on('click', (e: any) => {
                L.DomEvent.stopPropagation(e);
                const marker = e.target as CustomMarker;
                this.logger.debug('marker', marker.getData());
                this.onMarkerSelected.emit(marker.getData());
                return false;
            });

        })
    }

    ngOnDestroy(): void {
        if (this.map) {
            this.map.off();
            this.map.remove();
        }
    }

}
