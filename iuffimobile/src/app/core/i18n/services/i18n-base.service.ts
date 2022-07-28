import 'moment/min/locales';

import { Injectable } from '@angular/core';
import { DeviceService } from '@core/device';
import { AppError } from '@core/error';
import { Storage } from '@ionic/storage';
import { TranslateService } from '@ngx-translate/core';
import * as Moment from 'moment';

import { I18nModuleOptions } from '../models/i18n-module-options.model';
import { Language } from '../models/language.model';

const storageKeys = {
    i18n: 'i18n',
    lastLang: 'last_lang',
    lang: 'lang_{CODE}'
};

@Injectable()
export class I18nBaseService {
    public Moment = Moment;
    private _storage: Storage;
    private _currentLanguage?: Language;

    constructor(
        public options: I18nModuleOptions,
        protected deviceService: DeviceService,
        protected translateService: TranslateService,
        // private http: HttpClient
    ) {
        this._storage = new Storage({
            name: options.storePrefix || 'storage',
            storeName: 'i18n',
            driverOrder: ['localstorage']
        });
    }

    public downloadLang(lang: Language): Promise<Language> {
        console.log(`Download ${lang.code}`);
        throw new AppError('downloadLangs not defined');
    }

    /**
     * Get the last used language if exists
     * If there is no last language set fetch the system language or defaut one if not available
     *
     * @returns {Promise<Language>}
     */
    public getLastLanguage(): Promise<Language | null> {
        return new Promise(resolve => {
            // Search last used language in localStorage
            this._storage.get(storageKeys.lastLang).then((lastLang: Language) => {
                resolve(lastLang);
            });
        });
    }

    /**
     * Set the translations of requested language in translateService
     * @param  {Language} lang
     * @returns Promise<Language>
     */
    public async setTranslations(lang: Language, translations: Record<string, any>): Promise<Language> {
        const newLang = { ...lang };
        this.translateService.setTranslation(lang.code, translations);
        newLang.translations = translations;
        await this._storage.set(storageKeys.lang.replace('{CODE}', lang.code), newLang);
        return newLang;
    }

    /**
     * Set the last used language for the next app bootstrap
     * @param  {Language} lang Language to set as last
     * @returns Promise<void>
     */
    public async setLanguage(lang: Language): Promise<void> {
        if (lang) {
            this._currentLanguage = lang;
            Moment.locale(lang.code);
            await this._storage.set(storageKeys.lastLang, lang);
            this.translateService.use(lang.code);
        }
    }

    /**
     * Get the current used language
     * @returns {Language | undefined}
     */
    public getCurrentLanguage(): Language | undefined {
        return this._currentLanguage;
    }

    /**
     * Get the language set on device
     * @returns {Promise<string>}
     */
    public getDeviceLanguageCode(): Promise<string | null> {
        let defer: Promise<string>;
        if (this.deviceService.isCordova()) {
            defer = this.deviceService.getDeviceLanguage();
        }
        else {
            defer = Promise.resolve(this.options.defaultLanguageCode);
        }
        return defer.then(lang => {
            let final;
            try {
                final = lang.split('-')[0].toLowerCase();
            } catch (e) { final = null; }
            return final;
        });
    }

    /**
     * @param {string} key
     * @param  {object={}} params
     * @returns string
     */
    public translate(key: string, params: Record<string, any> = {}): string {
        return this.translateService.instant(key, params);
    }

    public onLangChange$ = this.translateService.onLangChange;

}
