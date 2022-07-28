import { Injectable } from '@angular/core';
import { Coordinate } from '@app/detections/models/coordinate.model';
import { ApiService } from '@core/api';
import { LoggerService } from '@core/logger';
import { ENV } from '@env';
import {
    BackgroundGeolocation,
    BackgroundGeolocationConfig,
    BackgroundGeolocationEvents,
    BackgroundGeolocationResponse
} from '@ionic-native/background-geolocation/ngx';
import { BackgroundMode } from '@ionic-native/background-mode/ngx';
import { Geolocation, Geoposition } from '@ionic-native/geolocation/ngx';
import { LocationAccuracy } from '@ionic-native/location-accuracy/ngx';
import { Storage } from '@ionic/storage';
import * as turf from '@turf/turf';
import { FeatureCollection, Geometries, Properties } from '@turf/turf';
import { BehaviorSubject, interval, Subscription } from 'rxjs';
import { DeviceService } from './../../core/device/services/device.service';
import { Comune } from './model/comune.model';
import { Point } from './model/point.model';
declare let cordova: any;

const storageKeys = {
    municipality: 'comuni',
};
@Injectable({
    providedIn: 'root',
})
export class GeoUtilsService {
    public municipalities!: FeatureCollection<Geometries, Properties>;
    private _storage: Storage;
    // private _municipalities: FeatureCollection<Geometries, Properties> | undefined;
    public initCompleted: Promise<FeatureCollection<Geometries, Properties>>;
    public trackingGpsActive = false;
    public whatchSub!: Subscription;
    public whatchInterval!: Subscription;
    public onSendLocation$: BehaviorSubject<Point | null> = new BehaviorSubject<Point | null>(null);
    constructor(
        private apiService: ApiService,
        private logger: LoggerService,
        private geolocation: Geolocation,
        private backgroundGeolocation: BackgroundGeolocation,
        private deviceService: DeviceService,
        private backgroundMode: BackgroundMode,
        private locationAccuracy:LocationAccuracy
    ) {
        this.logger.debug('GeoUtilsService');
        this._storage = new Storage({
            name: ENV.storePrefix || 'storage',
            storeName: 'config',
            driverOrder: ['localstorage'],
        });
        this.initCompleted = this._init();
    }

    public async _init(): Promise<FeatureCollection<Geometries, Properties>> {
        this.municipalities = await this._downloadGeoJson();
        return await this._initConfig(this.municipalities);
    }

    private _downloadGeoJson(): Promise<FeatureCollection<Geometries, Properties>> {
        return new Promise(async(resolve, reject) => {
            // try {
            if (this.deviceService.isOnline()) {
                this.apiService
                    .callApi<FeatureCollection<Geometries, Properties>>('getMunicipality')
                    .subscribe(res => {
                        resolve(res);
                    });
            } else {
                const lastMunicipalities = await this._getLastMunicipalities();
                if (lastMunicipalities) {
                    resolve(lastMunicipalities);
                } else {
                    reject();
                }
            }
        });
    }

    private async _initConfig(
        municipalities: FeatureCollection<Geometries, Properties>,
    ): Promise<FeatureCollection<Geometries, Properties>> {
        await this._storage.set(storageKeys.municipality, municipalities);
        return municipalities;
    }

    /**
     * Returns the last config file stored in localStorage with last modified date
     * @returns {Promise<FeatureCollection<Geometries, Properties>>}
     */
    private _getLastMunicipalities(): Promise<FeatureCollection<Geometries, Properties>> {
        return this._storage.get(storageKeys.municipality);
    }

    public getComuneByPoint(point: Point): Comune | null {
        let properties!: Properties;
        turf.featureEach(this.municipalities, feature => {
            if (this.isWithinPoligon(point.toArray(), feature)) {
                properties = feature.properties;
            }
        });
        if (properties) {
            const istat: string | undefined = properties['comune_ist'];
            const nome: string | undefined = properties['comune_nom'];
            const provinciaIstat: string | undefined = properties['provin_ist'];
            const provinciaNome: string | undefined = properties['provin_nom'];
            return new Comune(istat as string, nome as string, provinciaIstat as string, provinciaNome as string);
        }
        return null;
    }

    private isWithinPoligon(point: number[], polygon: any): boolean {
        const isWithin = turf.booleanPointInPolygon(point, polygon);
        return isWithin;
    }

    public getArea(points: Point[]): number {
        const pointsArray: number[][] = [];
        points.forEach(p => {
            pointsArray.push(p.toArray());
        });
        pointsArray.push(points[0].toArray());

        const polygon = turf.polygon([pointsArray]);
        const area = turf.area(polygon);
        return area;
    }

    public getConvexPolygonArea(points: Point[]): number {
        const pointsArray: any[] = [];
        points.forEach(p => {
            pointsArray.push(turf.point(p.toArray()));
        });

        const polygon = turf.convex(turf.featureCollection(pointsArray));
        const area = turf.area(polygon);
        return area;
    }

    public getCurrentPosition(): Promise<Coordinate> {
        return new Promise((resolve, reject) => {
            if (this.deviceService.isAndroid()) {
                this.geolocation
                    .getCurrentPosition({ enableHighAccuracy: true, timeout: 10000 })
                    .then((res: Geoposition) => {
                        const coordinate = new Coordinate(res.coords.latitude, res.coords.longitude);
                        resolve(coordinate);
                    })
                    .catch(err => {
                        this.geolocation
                            .getCurrentPosition({ enableHighAccuracy: false, timeout: 20000 })
                            .then((res: Geoposition) => {
                                const coordinate = new Coordinate(res.coords.latitude, res.coords.longitude);
                                resolve(coordinate);
                            })
                            .catch(err => {
                                reject(err);
                            });
                    });

            } else {
                this.geolocation
                    .getCurrentPosition({ enableHighAccuracy: true, timeout: 10000 })
                    .then((res: Geoposition) => {
                        const coordinate = new Coordinate(res.coords.latitude, res.coords.longitude);
                        resolve(coordinate);
                    })
                    .catch(err => {
                        reject(err);
                    });
            }
        });
    }

    public configGpsTracking(): void {
        if (this.deviceService.isIos()) {
            this._configIosGpsTracking();
        } else {
            this._configAndroidGpsTracking();
        }
    }

    private _configAndroidGpsTracking(): void {
        // document.addEventListener('pause', () => {

        if (this.deviceService.isAndroid()) {
            this.backgroundMode.setDefaults({
                resume: false,
                text: 'Non disabilitare il GPS',
                title: 'Tracciamento posizione in corso',
            });
            //this.backgroundMode.enable();
            cordova.plugins.backgroundMode.disableBatteryOptimizations();
        }

        //  });
    }

    public startAndroidGpsTracking(): void {
        this.backgroundMode.enable();
        this.logger.debug('GeoUtilsService::startAndroidGpsTracking');
        if (!this.trackingGpsActive) {
            this.logger.debug('GeoUtilsService::startAndroidGpsTracking::started');
            this.trackingGpsActive = true;
            this.whatchSub = this.geolocation.watchPosition().subscribe();

            this.getCurrentPosition().then(coords => {
                const point = new Point(coords.latitudine, coords.longitudine);
                this.onSendLocation$.next(point);
            });

            this.whatchInterval = interval(10000).subscribe(() => {
                this.getCurrentPosition().then(coords => {
                    console.log('Posizione rilevata');
                    const point = new Point(coords.latitudine, coords.longitudine);
                    this.logger.debug('GeoUtilsService::androidGpsTracking::coords ', coords);
                    this.onSendLocation$.next(point);
                });
            });
        }
    }

    public stopAndroidGpsTracking(): void {
        this.logger.debug('GeoUtilsService::stopAndroidGpsTracking');
        if (this.trackingGpsActive) {
            this.whatchInterval.unsubscribe();
            this.whatchSub.unsubscribe();
            this.trackingGpsActive = false;
            this.backgroundMode.disable();
            cordova.plugins.backgroundMode.enableBatteryOptimizations();
            this.logger.debug('GeoUtilsService::stopAndroidGpsTracking::stopped');
        }
    }

    private _configIosGpsTracking(): void {
        this.logger.debug('GeoUtilsService::configIosGpsTracking');
        const config: BackgroundGeolocationConfig = {
            desiredAccuracy: 0,
            stationaryRadius: 10,
            distanceFilter: 10,
            debug: false, //  enable this hear sounds for background-geolocation life-cycle.
            stopOnTerminate: true, // enable this to clear background location settings when the app terminates
        };

        this.backgroundGeolocation.configure(config).then(() => {
            this.backgroundGeolocation
                .on(BackgroundGeolocationEvents.location)
                .subscribe((location: BackgroundGeolocationResponse) => {
                    if (location) {
                        const point = new Point(location.latitude, location.longitude);
                        this.logger.debug('GeoUtilsService::iosGpsTracking::coords ', location);
                        this.onSendLocation$.next(point);
                        this.backgroundGeolocation.finish();
                    }
                });
        });

        // this.backgroundGeolocation.onStationary().then(() => {
        //     this.backgroundGeolocation.stop().then(() => {
        //         this.backgroundGeolocation.start();
        //     });
        // });
    }

    public startIosGpsTracking(): void {
        if (!this.trackingGpsActive) {
            this.logger.debug('GeoUtilsService::startIosGpsTracking');
            this.backgroundGeolocation.start();
            this.trackingGpsActive = true;
            this.logger.debug('GeoUtilsService::startIosGpsTracking::started');
        }
    }

    public stopIosGpsTracking(): void {
        if (this.trackingGpsActive) {
            this.logger.debug('GeoUtilsService::stopIosGpsTracking');
            this.backgroundGeolocation.stop();
            this.logger.debug('GeoUtilsService::startIosGpsTracking::stopped');
            this.trackingGpsActive = false;
        }
    }
}
