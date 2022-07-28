import { Injectable } from '@angular/core';
import { Inspector } from '@app/missions/models/inspector.model';
import { ApiService } from '@core/api';
import { LokiDB } from '@core/db';

import { DeviceService } from './../../core/device/services/device.service';
import { OfflineService } from './../../shared/offline/services/offline.service';

@Injectable()
export class HomeService {
    private _userDB: LokiDB | null = null;
    private inspectorCollection!: Collection<Inspector>;
    constructor(
        private deviceService: DeviceService,
        private apiService: ApiService,
        private offlineService: OfflineService
    ) {
        this._init();
    }

    private _init(): void {
        this._userDB = this.offlineService.getUserDB();
        if (this._userDB) {
            this.inspectorCollection = this._userDB.initCollection<Inspector>('inspectors');
        }
    }

    public getInspectors(): Promise<void> {
        return new Promise((resolve, reject) => {
            if (this.deviceService.isOnline()) {
                this.apiService.callApi<Inspector[]>('getInspectorList')
                    .subscribe(
                        response => {
                            const inspectors = response.map(t => new Inspector(t));
                            this.saveInspectors(inspectors);
                            resolve()
                        },
                        reject
                    );
            }
            else {
                resolve()
            }
        });
    }

    private saveInspectors(inspectors: Inspector[]): void {
        this.inspectorCollection.clear();
        this.inspectorCollection.insert(inspectors);
        this.saveCollections();
    }

    private saveCollections(): void {
        setTimeout(() => {
            this.offlineService.saveUserDB();
        }, 500);
    }
}
