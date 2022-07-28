import 'moment/min/locales';

import { Injectable, Optional } from '@angular/core';
import { ApiService } from '@core/api';
import { UserLanguage } from '@core/auth/models/user-language.model';
import { DeviceService } from '@core/device';
import { AppError } from '@core/error';
import { TranslateService } from '@ngx-translate/core';

import { ChangeUserLanguageBody, ChangeUserLanguagePath } from '../api/change-user-language.api';
import { GetTranslationsResponse } from '../api/get-translations.api';
import { I18nModuleOptions } from '../models/i18n-module-options.model';
import { Language } from '../models/language.model';
import { IT } from './../langs/IT';
import { I18nBaseService } from './i18n-base.service';

/**
 * Classe di gestione i18n relativa all'applicazione e che estende la classe base I18nService
 */
@Injectable()
export class IuffiI18nService extends I18nBaseService {
    private _languages: UserLanguage[] = [];

    constructor(
        @Optional() public options: I18nModuleOptions,
        protected translateService: TranslateService,
        protected deviceService: DeviceService,
        private apiService: ApiService
    ) {
        super(options, deviceService, translateService);
    }

    /**
     * Ritorna la lista di tutte le lingue disponbili
     * @returns UserLanguage
     */
    public getAllLanguages(): UserLanguage[] {
        return this._languages;
    }

    /**
     * Imposta tutte le lingue che devono essere gestite dall'app
     * @param  {UserLanguage[]} languages
     * @returns void
     */
    public setAllLanguages(languages: UserLanguage[]): void {
        this._languages = [...languages];
    }

    /**
     * Carica l'ultima lingua impostata dall'utente o scarica la lingua base per la login page
     * @returns Promise
     */
    public setStartupLanguage(): Promise<void> {
        const lang = new Language({
            code: 'it',
            label: 'Italiano',
            isDefault: true
        });
        return this.setTranslations(lang, IT).then(() => {
            return this.setLanguage(lang)
        });
    }

    /**
     * Scarica la lingua dal server, la registra sul TranslateModule e la salva nel localStorge
     * @param  {string} lang
     * @returns Promise
     */
    public async downloadLang(lang: Language): Promise<Language> {
        const self = this;
        if (self.deviceService.isOnline()) {
            try {
                const translationResponse = await self.apiService.callApi<GetTranslationsResponse>('getTranslation').toPromise();
                return await self.setTranslations(lang, translationResponse.translation);
            }
            catch (e) {
                throw e;
            }
        }
        throw new AppError('DEVICE_OFFLINE');
    }

    /**
     * Scarica la lingua di default dell'utente
     * Se il download non riesce tenta un recupero dal localStorage
     * @param  {string} codLanguage
     */
    public async setUserLanguage(codLanguage: string): Promise<void> {
        const userLanguage = this._languages.find(l => l.codLanguage === codLanguage);
        if (userLanguage) {
            let lang = new Language({ code: userLanguage.isoCode });
            try {
                lang = await this.downloadLang(lang);
                this.setLanguage(lang);
            } catch (e) {
                this.setLanguage(lang);
            }
        }
    }

    /**
     * Aggiorna la lingua lato server e poi scarica la nuova lingua impostata
     * @param  {string} username
     * @param  {string} codLanguage
     * @returns Promise
     */
    public async changeUserLanguage(username: string, codLanguage: string): Promise<void> {
        const path = new ChangeUserLanguagePath({
            username
        });
        const body = new ChangeUserLanguageBody({
            codLanguage
        });

        try {
            await this.apiService.callApi('changeUserLanguage', {
                body: body.toHttpParams(),
                paths: path.toJson()
            }).toPromise();
            return this.setUserLanguage(codLanguage);
        }
        catch (e) {
            throw new AppError(e);
        }

    }

}
