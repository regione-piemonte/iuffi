import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { ApiService } from '@core/api';
import { ConfigService } from '@core/config';
import { DeviceService } from '@core/device';
import { AppError } from '@core/error';
import { IuffiI18nService } from '@core/i18n';
import { InAppBrowser, InAppBrowserEvent, InAppBrowserOptions } from '@ionic-native/in-app-browser/ngx';
import { Storage } from '@ionic/storage';
import * as CryptoJS from 'crypto-js';
import { AES } from 'crypto-js';
import * as moment from 'moment';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { TokenResponse } from '../api/token-response';
import { JwtDto } from '../dto/jwt.dto';
import { AuthModuleOptions } from '../models/auth-module-options.model';
import { LoginConfig } from '../models/login-config.model';
import { SessionStates } from '../models/session-states';
import { UserCredentials } from '../models/user-credentials.model';
import { User } from '../models/user.model';
import { LoggerService } from './../../logger/services/logger.service';

const storageKeys = {
    userCredentials: 'userCredentials',
    user: 'user',
    fingerprint: 'fingerprint'
};

@Injectable()
export class AuthService {
    private _user!: User;
    private jwtHelper = new JwtHelperService();
    private _accessToken: string | null = null;
    private _storage: Storage;
    public onSessionChanges$: BehaviorSubject<number> = new BehaviorSubject(SessionStates.INITIAL);
    private inAppBrowserOptions: InAppBrowserOptions = {
        location: 'no', //Or 'no'
        // hidden : 'no', //Or  'yes'
        clearcache: 'yes',
        clearsessioncache: 'yes'
        // zoom : 'yes',//Android only ,shows browser zoom controls
        // hardwareback : 'yes',
        // mediaPlaybackRequiresUserAction : 'no',
        // shouldPauseOnSuspend : 'no', //Android only
        // closebuttoncaption : 'Close', //iOS only
        // disallowoverscroll : 'no', //iOS only
        // toolbar : 'yes', //iOS only
        // enableViewportScale : 'no', //iOS only
        // allowInlineMediaPlayback : 'no',//iOS only
        // presentationstyle : 'pagesheet',//iOS only
        // fullscreen : 'yes',//Windows only
    };

    constructor(
        public options: AuthModuleOptions,
        private configService: ConfigService,
        private deviceService: DeviceService,
        private ionic5AngularI18nService: IuffiI18nService,
        private apiService: ApiService,
        private logger: LoggerService,
        private iab: InAppBrowser
    ) {
        this._storage = new Storage({
            name: options.storePrefix || 'storage',
            storeName: 'auth',
            driverOrder: ['localstorage'],
        });
    }

    /**
     * Recupera le credenziali dal LocalStorage
     */
    public getCredentials(): Promise<UserCredentials> {
        return this._storage.get(storageKeys.userCredentials);
    }

    /**
     * Salva le credenziali nel LocalStorage
     */
    public async saveCredentials(credentials: UserCredentials): Promise<void> {
        let savedCredentials = await this.getCredentials();
        if (!savedCredentials) {
            savedCredentials = {};
        }
        if (credentials.username) {
            savedCredentials.username = credentials.username;
        }
        if (credentials.password) {
            savedCredentials.password = credentials.password;
        }
        return this._storage.set(storageKeys.userCredentials, savedCredentials);
    }

    /**
     * Cancella le credenziali dal LocalStorage
     */
    public cleanCredentials(): Promise<void> {
        return this._storage.remove(storageKeys.userCredentials);
    }

    /**
     * Ritorna il valore corrente dell'accessToken
     * @returns string
     */
    public getAccessToken(): string | null {
        return this._accessToken;
    }

    /**
     * Imposta un nuovo valore per l'accessToken
     * @param  {string|null=null} accessToken
     */
    public setAccessToken(accessToken: string | null = null): void {
        this._accessToken = accessToken;
    }

    /**
     * Cancella l'accessToken
     */
    public resetAccessToken(): void {
        this.setAccessToken();
    }

    /**
     * Returns the user instance
     * @returns User | null
     */
    public getUser(): User | null {
        if (this._user) {
            return this._user;
        }
        return null;
    }

    /**
     * Si autentica sui sistemi del cliente recuperanto un ticket utente
     * @param  {string} username
     * @param  {string} password
     * @returns Promise
     */
    private _authenticate(configUrls: LoginConfig): Promise<any> {
        const self = this;

        return new Promise((resolve, reject) => {
            const target = '_blank';
            const browser = self.iab.create(configUrls.authUrl, target, self.inAppBrowserOptions);

            browser.on('loadstart').subscribe((event: InAppBrowserEvent) => {
                self.logger.debug('#INAPPBROWSER LOADSTART', event);

            });

            browser.on('loadstop').subscribe((event: InAppBrowserEvent) => {
                self.logger.debug('#INAPPBROWSER LOADSTOP', event);
                if (event.url === configUrls.redirectUrl) {
                    browser.executeScript({
                        code: 'document.getElementsByTagName(\'pre\')[0].innerHTML'
                    }).then((html: any[]) => {
                        browser.close();
                        self.logger.debug(html);
                        const len: number = html.length;
                        if (len && len === 1) {
                            self.logger.debug(html[len - 1]);
                            const jsonTxt = html[len - 1];
                            const json: TokenResponse = JSON.parse(jsonTxt)
                            self.logger.debug(json.authenticationToken);
                            resolve(json.authenticationToken);
                        }
                    },
                    (err: any) => {
                        console.log(err)
                    }
                    );
                }
            });

            browser.on('loaderror').subscribe((event: InAppBrowserEvent) => {
                self.logger.debug('#INAPPBROWSER LOADERROR', event);
                const err = new AppError('LOGIN_ERROR');
                browser.close();
                reject(err);
            });

            browser.on('exit').subscribe((event: InAppBrowserEvent) => {
                self.logger.debug('#INAPPBROWSER EXIT', event);
            });

        //     const authBody = new AuthBody({
        //         userId: username,
        //         userPassword: password
        //     });
        //     self.apiService.callApi<string>('auth', {
        //         body: authBody.toHttpParams(),
        //         responseType: ResponseTypes.TEXT
        //     }).subscribe(
        //         response => {
        //             const responseString = self._parseUrsXmlResponse(response);
        //             if (responseString && responseString.indexOf(UrsStatus.RESPONSE) === -1) {
        //                 const encryptedToken = AES.encrypt(password, self.deviceService.getUUID());
        //                 self.saveCredentials({ password: encryptedToken.toString() });
        //                 resolve(encodeURIComponent(responseString));
        //             }
        //             else {
        //                 let err = new AppError('LOGIN_ERROR');
        //                 if (responseString.indexOf(UrsStatus.FIRST_ACCESS) > -1) {
        //                     err = new AppError('USER_AUTHENTICATION_FIRST_ACCESS', {
        //                         status: UrsStatus.FIRST_ACCESS
        //                     });
        //                 }
        //                 else if (responseString.indexOf(UrsStatus.PASSWORD_EXPIRED) > -1) {
        //                     err = new AppError('USER_AUTHENTICATION_PASSWORD_EXPIRED', {
        //                         status: UrsStatus.PASSWORD_EXPIRED
        //                     });
        //                 }
        //                 else {
        //                     err = new AppError('USER_AUTHENTICATION_WRONG_CREDENTIAL', {
        //                         status: UrsStatus.WRONG_CREDENTIALS
        //                     });
        //                 }
        //                 reject(err)
        //             }
        //         },
        //         reject
        //     );
        });
    }

    /**
     * Recupera il token di sessione
     */
    public login(configUrls: LoginConfig): Promise<void> {
        const self = this;
        return new Promise((resolve, reject) => {
            this._authenticate(configUrls).then(
                (jwtToken: string) => {
                    const encryptedToken = AES.encrypt(jwtToken, self.deviceService.getUUID());
                    const decodedUser: JwtDto = this.jwtHelper.decodeToken(jwtToken);
                    this.logger.debug(decodedUser);
                    self._user = User.fromJwtDto(decodedUser);
                    self.saveCredentials({ password: encryptedToken.toString() });
                    self.setAccessToken(jwtToken);
                    self.startSession(self._user);
                    resolve();
                },
                reject
            );
        });
    }

    public autologin(): Promise<void> {
        const self = this;
        return new Promise(async(resolve, reject) => {
            try {
                const jwtToken = AES.decrypt((await self.getCredentials()).password as string, self.deviceService.getUUID()).toString(CryptoJS.enc.Utf8);
                self.logger.debug(jwtToken);
                const decodedUser: JwtDto = self.jwtHelper.decodeToken(jwtToken.toString());
                const now = moment();
                const d = new Date(0);
                d.setUTCSeconds(decodedUser.exp);
                const expiryDate = moment(d);
                self.logger.debug(jwtToken);
                if (now.isBefore(expiryDate)) {
                    self._user = User.fromJwtDto(decodedUser);
                    self.setAccessToken(jwtToken.toString());
                    self.startSession(self._user);
                    resolve();
                }
                else {
                    self.cleanCredentials();
                    reject();
                }
            } catch (error) {
                reject()
            }
        });
    }

    /**
     * Set the user information in localStorage,
     * init the user info in memory
     * @param {string} User
     */
    public startSession(user: User): void {
        this._storage.get(storageKeys.user).then((lastUser: User) => {
            this._storage.set(storageKeys.user, user);
            if (lastUser && lastUser.fiscalCode === user.fiscalCode) {
                this.onSessionChanges$.next(SessionStates.LOGGED_LAST_USER);
            }
            else {
                this.onSessionChanges$.next(SessionStates.LOGGED_NEW_USER);
            }
        });
    }

    /**
     * Logout the user,
     * destroy all his references
     * and get new accessToken for public access
     * @param  {LoginStates} loginState LOGOUT if user logs out or THROW_OUT if the refreshToken expire
     */
    public logout(sessionStates: SessionStates = SessionStates.LOGOUT): void {
        this._endSession();
        setTimeout(() => {
            if (this.onSessionChanges$.getValue() !== SessionStates.LOGOUT && this.onSessionChanges$.getValue() !== SessionStates.THROW_OUT) {
                this.onSessionChanges$.next(sessionStates);
            }
        }, 500);
    }

    /**
     * Destroy the user session and the user info in DB
     */
    private _endSession(): void {
        this._user = {} as User;
        this.resetAccessToken();
    }
}
