import { Injectable } from '@angular/core';
import { DeviceService } from '@core/device';
import { AppError } from '@core/error';
import { AppVersion } from '@ionic-native/app-version/ngx';
import { find } from 'lodash';
import { lte, valid } from 'semver';

import { VersioningStatus } from '../models/versioning-status';
import { Versioning } from '../models/versioning.model';

@Injectable()
export class VersioningService {
    private _versioning: Versioning[] = [];
    public versioningStatus: VersioningStatus = VersioningStatus.LAST;

    constructor(
        private deviceService: DeviceService,
        private appVersion: AppVersion
    ) { }

    /**
     * Get the installed app version for
     * @return {Promise<string>}
     * @public
     */
    public async getAppVersion(): Promise<string> {
        if (this.deviceService.isCordova()) {
            return await this.appVersion.getVersionNumber();
        }
        else {
            return 'x.x.x';
        }
    }

    /**
     * Check if the installed app is the available last version
     * @param configLastVersion the last version available (declared on the remote conf)
     * @return {Promise<boolean>}
     * @private
     */
    private async _isLastVersion(configLastVersion: string): Promise<boolean> {
        const appVersion = await this.getAppVersion();
        if (valid(configLastVersion) && valid(appVersion as string)) {
            return lte(configLastVersion, appVersion as string);
        }
        else {
            throw 'Versioning.isLastVersion app version not checked';
        };
    }

    /**
     * Check the conditions app update
     * @return {Promise<null>}
     * @private
     */
    public async checkVersioning(versioning?: Versioning[]): Promise<null> {
        if (!versioning) {
            versioning = this._versioning;
        }
        else {
            this._versioning = versioning;
        }
        if (this.deviceService.isCordova()) {
            const platform = this.deviceService.getOS().toLowerCase();
            const platformVersioning = find(versioning, { platform: platform });
            if (platformVersioning) {
                try {
                    const isLastVersion = await this._isLastVersion(platformVersioning.lastVersion);
                    // Se non è l'ultima versione disponibile
                    if (isLastVersion === false) {
                        const storeLink = (platformVersioning as Versioning).storeLink;
                        // Verifico se l'aggiornamento è obbligatorio
                        // e in tal caso mostro un alert e vado allo storeLink
                        if ((platformVersioning as Versioning).isMandatoryUpdate) {
                            console.log(`App mandatory update ${platform}`);
                            this.versioningStatus = VersioningStatus.NOT_LAST_MANDATORY;
                            return this._showMandatoryUpdate(storeLink);
                        }
                        else {
                            // Se invece l'aggiornamento non è obbligatorio mostro una richiesta di aggiornamento
                            // che può essere anche ignorata dall'utente
                            console.log(`App optional update ${platform}`);
                            this.versioningStatus = VersioningStatus.NOT_LAST;
                            return this._showUpdateConfirm(storeLink);
                        }
                    }
                    else {
                        console.log('App alredy update');
                        this.versioningStatus = VersioningStatus.LAST;
                    }
                }
                catch (err) {
                    throw new AppError(err);
                }
            }
            else {
                console.log(`No versioning details for ${platform} platform`);
                this.versioningStatus = VersioningStatus.LAST;
            }
        }
        else {
            console.log('Bypass checkVersioning browser platform');
            this.versioningStatus = VersioningStatus.LAST;
        }
        return Promise.resolve(null);
    }

    /**
     * Redirect app on store link for download and update the app
     * @param storeLink
     * @return {Promise<null>}
     */
    private _showMandatoryUpdate(storeLink: string): Promise<null> {
        const self = this;
        return new Promise((resolve, reject) => {
            self.deviceService.alert(
                'NEW_MANDATORY_APP_VERSION_AVAILABLE',
                {
                    handler: () => {
                        window.open(storeLink, '_system');
                        reject(new AppError('UPDATING'));
                    },
                    title: 'APP_MANDATORY_UPDATE_AVAILABLE'
                }
            );
        })
    }

    /**
     * Ask to the user to update the app
     * @param storeLink
     * @return {Promise<null>}
     */
    private _showUpdateConfirm(storeLink: string): Promise<null> {
        const self = this;
        return new Promise((resolve, reject) => {
            self.deviceService.confirm(
                'NEW_APP_VERSION_AVAILABLE',
                {
                    title: 'APP_UPDATE_AVAILABLE',
                    buttons: [
                        {
                            text: 'UPDATE',
                            handler: () => {
                                window.open(
                                    storeLink,
                                    '_system'
                                );
                                reject(new AppError('UPDATING'));
                            }
                        },
                        {
                            text: 'NOT_UPDATE',
                            handler: () => {
                                resolve();
                            }
                        }
                    ]
                }
            );
        });
    }
}
