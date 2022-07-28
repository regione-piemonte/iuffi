import { Component } from '@angular/core';
import { HomePage } from '@app/home/pages/home/home.page';
import { AuthService, User } from '@core/auth';
import { DeviceService } from '@core/device';
import { AppError } from '@core/error';
import { LoggerService } from '@core/logger';
import { RootNavController } from '@core/split-view';
import { ENV } from '@env';
import { OfflineService } from '@shared/offline/services/offline.service';

@Component({
    selector: 'login-page',
    templateUrl: './login.page.html',
    styleUrls: ['./login.page.scss'],
})
export class LoginPage {
    public appName = ENV.appName;

    constructor(
        private deviceService: DeviceService,
        private logger: LoggerService,
        private rootNavCtrl: RootNavController,
        private authService: AuthService,
        private offlineService: OfflineService
    ) {}

    /**
     * Effettua login
     * @returns void
     */
    public professionalsLogin(): void {
        const self = this;
        if (this.deviceService.isCordova()) {
            if (this.deviceService.isOnline()) {
                this.authService.login(ENV.professionalsLogin).then(
                    () => {
                        self._loginSuccess();
                    },
                    (err: AppError) => {
                        self._loginError(err);
                    }
                );
            }
            else {
                self._loginSuccess();
            }
        }
        else {
            self._loginSuccess();
        }
    }

    public publicLogin(): void {
        const self = this;
        if (this.deviceService.isCordova()) {
            if (this.deviceService.isOnline()) {
                this.authService.login(ENV.publicLogin).then(
                    () => {
                        self._loginSuccess();
                    },
                    (err: AppError) => {
                        self._loginError(err);
                    }
                );
            }
            else {
                self._loginSuccess();
            }
        }
        else {
            self._loginSuccess();
        }
    }

    /**
     * Finalizza la procedura di login
     * @param  {LoginForm} credentials?
     * @returns void
     */
    private _loginSuccess(): void {
        const user = this.authService.getUser() as User
        if (ENV.devMode) {
            this.offlineService.initUserDb(ENV.dbConfig, 'AAAAAA00B77B000F');
        }
        else {
            this.offlineService.initUserDb(ENV.dbConfig, user.fiscalCode);
        }
        this._openHomePage();
    }

    /**
     * Gestisce gli errori della login
     * @param  {AppError} err
     * @returns void
     */
    private _loginError(err: AppError): void {
        this.logger.error(err);
        if (err.message) {
            this.deviceService.alert(err.message);
        }
        this.deviceService.hideLoading();
    }

    /**
     * Carica la pagina principale dell'app
     * @returns void
     */
    private _openHomePage(): void {
        this.rootNavCtrl.setRoot(HomePage, null, {
            animated: true,
            direction: 'forward'
        });
    }
}
