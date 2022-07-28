import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { DeviceService } from '@core/device';
import { AppError } from '@core/error';
import { Storage } from '@ionic/storage';

import { ConfigModuleOptions } from '../models/config-module-options.model';
import { Config } from '../models/config.model';

const storageKeys = {
    lastConfig: 'last'
};

@Injectable()
export class ConfigService {
    private _url = '';
    private _config: Config | undefined;
    private _storage: Storage;
    public initCompleted: Promise<Config>;

    constructor(
        public options: ConfigModuleOptions,
        private http: HttpClient,
        private deviceService: DeviceService,
    ) {
        console.log('CONFIG');
        this._storage = new Storage({
            name: options.storePrefix || 'storage',
            storeName: 'config',
            driverOrder: ['localstorage']
        });
        this.initCompleted = this._init(options);
    }

    /**
     *
     * @returns Config
     */
    public getConfig(): Config {
        return this._config as Config;
    }

    /**
     * Returns the last config file stored in localStorage with last modified date
     * @returns {Promise<Config>}
     */
    private _getLastConfig(): Promise<Config> {
        return this._storage.get(storageKeys.lastConfig)
    }

    /**
     * Download config file and init the app
     */
    private async _init(options: ConfigModuleOptions): Promise<Config> {
        try {
            // If requested config is a remote one => download it
            if (typeof options.config === 'string') {
                this._url = options.config as string;
                const config = await this.download();
                return await this._initConfig(config);
            }
            // Otherwise use the local one (if exists)
            else if (typeof options.config === 'object') {
                return await this._initConfig(options.config);
            }
            else {
                throw 'NO_CONFIG_DEFINED';
            }
        } catch (e) {
            throw new AppError(e);
        }
    }

    private async _initConfig(config: Config): Promise<Config> {
        this._config = (config instanceof Config) ? config : new Config(config);
        await this._storage.set(storageKeys.lastConfig, config);
        return this._config;
    }

    /**
     * Download the external config file and store it in localStorage
     * @returns {Promise<Config>}
     */
    private async download(): Promise<Config> {
        try {
            const lastConfig = await this._getLastConfig();
            if (this.deviceService.isOnline()) {
                // Try to download the new config file only if it was modified
                const headers = new HttpHeaders().set('Content-Type', 'application/json');
                if (lastConfig && lastConfig.lastModified) {
                    //headers = headers.set('If-Modified-Since', lastConfig.lastModified);
                }
                const response = await this.http.get<Config>(`${this._url}?t=${new Date().getTime()}`, { headers, observe: 'response' }).toPromise();
                // If config.json was updated initialize it and update the lastModified property
                if (response.body) {
                    response.body.lastModified = response.headers.get('Last-Modified') as string;
                    return response.body;
                }
                throw 'ERR_APP_MISSING_CONFIG_FILE';
            }
            // If the device is offline but I have a local config
            // initialize it with localStorage version
            else {
                throw 'DEVICE_OFFLINE';
            }
        }
        catch (err) {
            if (err instanceof HttpErrorResponse) {
                err = 'ERR_APP_MISSING_CONFIG_FILE';
            }
            const lastConfig = await this._getLastConfig();
            if (lastConfig) {
                return lastConfig;
            }
            // The download fails and a local config doesn't exists, so throw an error
            else {
                throw err;
            }
        }
    }
}
