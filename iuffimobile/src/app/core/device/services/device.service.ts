import { Injectable, Optional } from '@angular/core';
import { LoggerService } from '@core/logger';
import { Device } from '@ionic-native/device/ngx';
import { Dialogs } from '@ionic-native/dialogs/ngx';
import { Globalization } from '@ionic-native/globalization/ngx';
import { Keyboard } from '@ionic-native/keyboard/ngx';
import { Network } from '@ionic-native/network/ngx';
import { ScreenOrientation } from '@ionic-native/screen-orientation/ngx';
import { SpinnerDialog } from '@ionic-native/spinner-dialog/ngx';
import { SplashScreen } from '@ionic-native/splash-screen/ngx';
import { StatusBar } from '@ionic-native/status-bar/ngx';
import { AlertController, LoadingController, Platform } from '@ionic/angular';
import { AlertButton, AlertOptions, IonicSafeString } from '@ionic/core';
import { TranslateService } from '@ngx-translate/core';
import { Subject, Subscription } from 'rxjs';
import { DeviceModuleOptions } from '../models/device-module-options.model';

export enum ConnectionStatusEnum {
    Online,
    Offline
}

@Injectable()
export class DeviceService {
    private _modalHeader!: string;
    private _dialogsMode!: string;
    private _loadingMode!: string;
    private _ionLoading!: HTMLIonLoadingElement;
    private previousStatus: ConnectionStatusEnum;

    public networkStatusChanges$: Subject<boolean> = new Subject();
    public keyboardVisibilityChanges$: Subject<boolean> = new Subject();
    public onResume$: Subject<boolean> = new Subject();
    public onPause$: Subject<boolean> = new Subject();

    constructor(
        @Optional() options: DeviceModuleOptions,
        private platform: Platform,
        private network: Network,

        private keyboard: Keyboard,
        private device: Device,
        private splashScreen: SplashScreen,
        private spinnerDialog: SpinnerDialog,
        private loadingCtrl: LoadingController,
        private dialogs: Dialogs,
        private screenOrientation: ScreenOrientation,
        private statusBar: StatusBar,
        private globalization: Globalization,
        private alertCtrl: AlertController,
        private translateService: TranslateService,
        private logger: LoggerService
    ) {
        if (options) {
            if (options.modalHeader) this._modalHeader = options.modalHeader;
            if (options.dialogsMode) this._dialogsMode = options.dialogsMode;
            if (options.loadingMode) this._loadingMode = options.loadingMode;
        }
        this.previousStatus = ConnectionStatusEnum.Online;
        // Init observables when device is ready

        const self = this;
        if (this.isCordova()) {
            document.addEventListener('deviceready', () => {
                self._initSubscriptions();
            }, true);
        }
        else {
            self._initSubscriptions();
        }
    }

    private _initSubscriptions(): void {
        // Init subscription for online/offline events
        this.logger.debug('_initSubscriptions');
        this.network.onConnect().subscribe(() => {
            if (this.previousStatus === ConnectionStatusEnum.Offline) {
                this.networkStatusChanges$.next(true);
            }
            this.previousStatus = ConnectionStatusEnum.Online;
        });
        this.network.onDisconnect().subscribe(() => {
            if (this.previousStatus === ConnectionStatusEnum.Online) {
                this.networkStatusChanges$.next(false);
            }
            this.previousStatus = ConnectionStatusEnum.Offline;
        });;

        // Init subscription for keyboard show/hide events
        this.keyboard.onKeyboardWillShow().subscribe(() => {
            (window.document.querySelector('ion-app') as HTMLElement).classList.add('keyboard-is-visible');
            this.keyboardVisibilityChanges$.next(true);
        });
        this.keyboard.onKeyboardWillHide().subscribe(() => {
            setTimeout(() => {
                (window.document.querySelector('ion-app') as HTMLElement).classList.remove('keyboard-is-visible');
            }, 250);
            this.keyboardVisibilityChanges$.next(false);
        });

        // Init subscription for platform pause event
        this.platform.pause.subscribe(() => {
            this.onPause$.next(true);
        });

        // Init subscription for platform resume event
        this.platform.resume.subscribe(() => {
            this.onResume$.next(true);
        });
    }

    /**
    * Return true if the app is running on Cordova, false otherwise
    * @returns {boolean}
    */
    public isCordova(): boolean {
        return this.platform.is('cordova');
    }

    /**
    * Return true if the app is running on Android device, false otherwise
    * @returns {boolean}
    */
    public isAndroid(): boolean {
        return this.platform.is('android');
    }

    /**
    * Return true if the app is running on iOS device, false otherwise
    * @returns {boolean}
    */
    public isIos(): boolean {
        return this.platform.is('ios');
    }

    /**
    * Return true if the app is running on tablet
    * @returns {boolean}
    */
    public isTablet(): boolean {
        return this.platform.is('tablet');
    }

    /**
    * Return true if the device has internet connection available, false otherwise
    * @returns {boolean}
    */
    public isOnline(): boolean {
        if (this.isCordova()) {
            const connectionType = this.network.type
            return connectionType !== this.network.Connection.UNKNOWN && connectionType !== this.network.Connection.NONE;
        }
        else {
            return (window.navigator && window.navigator.onLine);
        }
    }

    /**
    * Return true if the device doesn't have internet connection available, false otherwise
    * @returns {boolean}
    */
    public isOffline(): boolean {
        return !this.isOnline();
    }

    /**
     * Return the device’s Universally Unique Identifier (UUID) if the app is running on device
     * @returns string
     */
    public getUUID(): string {
        if (this.isCordova()) {
            const udidWithoutDash = this.device.uuid.replace(/-/g, '');
            return udidWithoutDash;
        }
        else {
            const udid = 'LK724LHLJLKEPHGX86KPXHZQX1DE8QE7W52G8';
            // const possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            // for (let i = 0; i < 37; i++) {
            //     udid += possible.charAt(Math.floor(Math.random() * possible.length));
            // };
            return udid
        }
    }

    /**
     * Return the operating system version if the app is running on device
     * @returns string
     */
    public getOSVersion(): string {
        if (this.isCordova()) {
            return this.device.version;
        }
        return 'FAKE_VERSION';
    }

    /**
     * Return the device’s operating system name if the app is running on device
     * @returns string
     */
    public getOS(): string {
        if (this.isCordova()) {
            return this.device.platform;
        }
        return 'FAKE_PLATFORM';
    }

    /**
     * Return the platform of the device’s model or product if the app is running on device
     * @returns string
     */
    public getDeviceType(): string {
        if (this.isCordova()) {
            return this.device.model;
        }
        return 'WebBrowser';
    }

    /**
    * Show the app's splash screen
    * @returns void
    */
    public showSplashscreen(): void {
        this.splashScreen.show();
    }

    /**
    * Hide the app's splash screen
    * @returns void
    */
    public hideSplashscreen(): void {
        this.splashScreen.hide();
    }

    /**
    * Force keyboard to be shown
    * @returns void
    */
    public showKeyboard(): void {
        this.keyboard.show();
    }

    /**
    * Close the keyboard if open
    * @returns void
    */
    public closeKeyboard(): void {
        this.keyboard.hide();
    }

    /**
     * Set the default status bar style: dark text, for light backgrounds
     * @returns void
     */
    public styleStatusBarAsDefault(): void {
        if (this.isCordova()) {
            this.statusBar.styleDefault();
        }
    }

    /**
     * Get the language set on device
     * @returns {Promise<string>}
     */
    public getDeviceLanguage(): Promise<string> {
        return this.globalization.getPreferredLanguage().then(lang => {
            return lang.value;
        });
    }

    /**
    * Show the native spinner dialog
    * or the Ionic Loading if the app is running on browser and there isn't any `message`
    * or the Ionic
    * @param  {string} message Message to display in the spinner dialog
    * @returns void
    */
    public showLoading(message?: string): void {
        this.closeKeyboard();

        if (message) {
            message = this.translateService.instant(message);
        }

        if (this.isCordova() && this._loadingMode === 'native') {
            if (message) {
                this.spinnerDialog.show(this._modalHeader, message, true);
            }
            else {
                this.spinnerDialog.show(undefined, undefined, true);
            }
        }
        else if (!this._ionLoading) {
            this.loadingCtrl.create({
                message
            }).then(loading => {
                this._ionLoading = loading;
                this._ionLoading.present();
            });
        }
    }

    /**
    * Close the native spinner dialog or the Ionic one
    * @returns void
    */
    public hideLoading(): void {
        if (this.isCordova() && this._loadingMode === 'native') {
            this.spinnerDialog.hide();
        }
        else if (this._ionLoading) {
            this._ionLoading.dismiss();
            delete this._ionLoading;
        }
    }

    public ORIENTATIONS = this.screenOrientation.ORIENTATIONS;

    /**
     * Lock the orientation to the passed value
     * @param  {string} orientation One of the @ionic-native/screen-orientation's ORIENTATIONS
     * @returns void
     */
    public lockOrientation(orientation: string): void {
        if (this.isCordova()) {
            this.screenOrientation.lock(orientation);
        }

    }

    /**
     * Unlock and allow all orientations
     * @returns void
     */
    public unlockOrientation(): void {
        this.screenOrientation.unlock();
    }

    /**
     * Get the current orientation of the device
     * @returns string
     */
    public getOrientation(): string {
        return this.screenOrientation.type;
    }

    /**
     * Check if device's orientation is portrait
     * @returns boolean
     */
    public isPortrait(): boolean {
        const orientation = this.getOrientation();
        if (
            orientation === this.screenOrientation.ORIENTATIONS.PORTRAIT ||
            orientation === this.screenOrientation.ORIENTATIONS.PORTRAIT_PRIMARY ||
            orientation === this.screenOrientation.ORIENTATIONS.PORTRAIT_SECONDARY
        ) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Check if device's orientation is landscape
     * @returns boolean
     */
    public isLandscape(): boolean {
        return !this.isPortrait();
    }

    /**
     * Check if device is not rooted
     * @returns Promise<boolean>
     */
    // public isNotRooted(): Promise<boolean> {
    //     const self = this;
    //     return new Promise((resolve, reject) => {
    //         if (self.isCordova()) {
    //             IRoot.isRooted(
    //                 (data: any) => {
    //                     console.log('isNotRooted - data', data)
    //                     if (data && data === true) {
    //                         reject();
    //                     }
    //                     else {
    //                         resolve()
    //                     }
    //                 },
    //                 resolve
    //             );
    //         }
    //         else {
    //             resolve();
    //         }
    //     });
    // }

    /**
     * Listen to orientation change event
     * @param  {Function|undefined} callback Callback to execute when orientation event is fired
     * @returns Observable
     */
    public onOrientationChange$(callback: () => void | undefined): Subscription {
        return this.screenOrientation.onChange().subscribe(
            () => {
                if (callback) {
                    callback();
                }
            });
    }

    /**
    * Shows a native or ionic simple alert dialog
    * @param  {string} message Dialog message
    * @param  {string} title Dialog title
    * @returns void
    */
    public alert(message: string,
        options: {
            handler?: () => void;
            title?: string;
            buttonName?: string;
        } = {}
    ): void {
        this.hideLoading();

        if (!options.handler) {
            options.handler = () => null;
        }

        if (!options.title) {
            options.title = this._modalHeader;
        }

        if (!options.buttonName) {
            options.buttonName = 'OK';
        }

        try {
            message = this.translateService.instant(message);
            options.title = this.translateService.instant(options.title);
            options.buttonName = this.translateService.instant(options.buttonName);
        }
        catch (e) { }

        if (this.isCordova() && this._dialogsMode === 'native') {
            this.dialogs.alert(message, options.title, options.buttonName).then(() => {
                if (options.handler) {
                    options.handler();
                }
            });
        }
        else {
            this.ionicCustomAlert({
                header: options.title,
                message: message,
                buttons: [
                    {
                        text: options.buttonName as string,
                        handler: options.handler
                    }
                ]
            });
        }
    }

    /**
    * Shows a native confirm dialog or the simple browser confirm
    * @param {string} message Dialog message
    * @param {string} title Dialog title
    * @param {AlertButton[]} buttons List of <AlertButton>
    */
    public confirm(message: string,
        options: {
            title?: string;
            buttons?: AlertButton[];
        } = {}
    ): void {
        this.hideLoading();

        if (!options.title) {
            options.title = this._modalHeader;
        }

        if (!options.buttons) {
            options.buttons = [{
                text: 'CANCEL',
                cssClass: 'primary',
                role: 'cancel',
                handler: () => { }
            }, {
                text: 'OK',
                cssClass: 'primary',
                handler: () => { }
            }];
        }

        message = this.translateService.instant(message);
        options.title = this.translateService.instant(options.title);

        const buttonLabels = options.buttons.map((b: AlertButton) => {
            return this.translateService.instant(b.text as string);
        });

        if (this.isCordova() && this._dialogsMode === 'native') {
            this.dialogs.confirm(message, options.title, buttonLabels).then(
                (buttonIndex: number) => {
                    // Decrement clicked button index because the plugin use 'one-based indexing'
                    buttonIndex--;
                    // Then execute the 'onClick' function if is defined
                    if ((options.buttons as AlertButton[])[buttonIndex] && (options.buttons as AlertButton[])[buttonIndex].handler) {
                        ((options.buttons as AlertButton[])[buttonIndex] as any).handler();
                    }
                }
            );
        }
        else {
            this.ionicCustomAlert({
                header: options.title,
                message: message,
                buttons: options.buttons
            });
        }
    }

    /**
    * Shows an ionic custom alert dialog
    * @param  {AlertOptions} options All Ionic alert options
    * @returns void
    */
    public ionicCustomAlert(options: AlertOptions = {}): void {
        this.hideLoading();

        if (!options.header) options.header = this._modalHeader;
        options.header = this.translateService.instant(options.header);
        if (options.subHeader) {
            options.subHeader = this.translateService.instant(options.subHeader);
        }
        else {
            options.subHeader = '';
        }
        if (options.message) {
            if (options.message instanceof IonicSafeString) {
                options.message = options.message.value;
            }
            options.message = this.translateService.instant(options.message as string);
        }
        else {
            options.message = '';
        }
        if (!options.cssClass) options.cssClass = 'primary';
        if (!options.inputs) options.inputs = [];
        if (!options.buttons) {
            options.buttons = [
                {
                    text: 'OK',
                    handler: () => { },
                    cssClass: 'primary',
                    role: ''
                }, {
                    text: 'CANCEL',
                    handler: () => { },
                    cssClass: 'primary',
                    role: 'cancel'
                }
            ];
        }
        options.buttons.forEach(b => {
            (b as AlertButton).text = this.translateService.instant((b as AlertButton).text as string);
        });
        if (!options.backdropDismiss) options.backdropDismiss = false;
        if (this.isIos()) {
            options.mode = 'ios';
        }
        this.alertCtrl.create(options).then((alert: HTMLIonAlertElement) => {
            alert.present();
        });
    }

    /**
     * Shows an ionic toast
     * @param  {string} message Main message text of toast
     * @param  {ToastOptions?} options All Ionic toast options
     * @returns void
     */
    // public showToast(message: string, options?: ToastOptions): void {
    //     if (!options) {
    //         options = {};
    //     }
    //     options.message = this.translateService.instant(message);
    //     if (!options.position) options.position = 'bottom';
    //     if (!options.duration) options.duration = 5000;
    //     if (options.closeButtonText) {
    //         options.showCloseButton = true;
    //         options.closeButtonText = this.translateService.instant(options.closeButtonText);
    //     }

    //     const toast = this.toastCtrl.create(options);
    //     toast.present();
    // }

}
