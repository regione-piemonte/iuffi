import { Component, OnInit } from '@angular/core';
import { HomePage } from '@app/home/pages/home/home.page';
import { LoginPage } from '@app/login/pages/login/login.page';
import { ApiService } from '@core/api';
import { AuthService, User } from '@core/auth';
import { ConfigService } from '@core/config';
import { DeviceService } from '@core/device';
import { IuffiI18nService } from '@core/i18n';
import { LoggerService } from '@core/logger';
import { RootNavController } from '@core/split-view';
import { ENV } from '@env';
import { OfflineService } from '@shared/offline/services/offline.service';

/**
 * Pagina di atterraggio all'avvio dell'app in caso di errore
 */
@Component({
    selector: 'app-starter',
    templateUrl: './starter.page.html',
    styleUrls: ['./starter.page.scss'],
})
export class StarterPage implements OnInit {
    /**
     * Testo di errore che viene mostrato al centro della pagina
     */
    public status = '';

    constructor(
        private configService: ConfigService,
        private logger: LoggerService,
        private apiService: ApiService,
        private authService: AuthService,
        private iuffiI18nService: IuffiI18nService,
        private rootNavCtrl: RootNavController,
        private deviceService: DeviceService,
        private offlineService: OfflineService
    ) { }

    async ngOnInit(): Promise<void> {
        try {
            const config = await this.configService.initCompleted;
            this.logger.changeLevel(config.loggerLevel);
            await this.offlineService.init(ENV.dbConfig);
            this.apiService.init(config.backend);
            await this.iuffiI18nService.setStartupLanguage();
            if (this.deviceService.isCordova()) {
                if (ENV.devMode) {
                    await this.offlineService.initUserDb(ENV.dbConfig, 'AAAAAA00B77B000F');
                    this.rootNavCtrl.setRoot(HomePage).then(() => {
                        this.deviceService.hideSplashscreen();
                    });
                }
                else {
                    this.authService.autologin().then(
                        () => {
                            const user = this.authService.getUser() as User
                            this.offlineService.initUserDb(ENV.dbConfig, user.fiscalCode);
                            this.rootNavCtrl.setRoot(HomePage).then(() => {
                                this.deviceService.hideSplashscreen();
                            });
                        }
                    ).catch(() => {
                        this.rootNavCtrl.setRoot(LoginPage).then(() => {
                            this.deviceService.hideSplashscreen();
                        });
                    })

                }
            }
            else {
                await this.offlineService.initUserDb(ENV.dbConfig, 'AAAAAA00B77B000F');
                this.rootNavCtrl.setRoot(HomePage).then(() => {
                    this.deviceService.hideSplashscreen();
                });
            }
        }
        catch (err) {
            this.status = err.message;
            this.deviceService.hideSplashscreen();
        }
    }
}
