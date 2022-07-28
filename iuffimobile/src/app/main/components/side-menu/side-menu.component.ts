import { Component } from '@angular/core';
import { HomePage } from '@app/home/pages/home/home.page';
import { LoginPage } from '@app/login/pages/login/login.page';
import { Mission } from '@app/missions/models/mission.model';
import { NewMissionPage } from '@app/missions/pages/new-mission/new-mission.page';
import { AuthService, SessionStates, User } from '@core/auth';
import { DeviceService } from '@core/device';
import { RootNavController } from '@core/split-view';
import { AutoUnsubscribe } from '@core/utils';
import { VersioningService } from '@core/versioning';
import { ENV } from '@env';
import { Platform } from '@ionic/angular';
import { filter, takeUntil } from 'rxjs/operators';
import { Environments } from 'src/environments/models/environments';

@Component({
    selector: 'side-menu',
    templateUrl: './side-menu.component.html',
    styleUrls: ['./side-menu.component.scss'],
})
export class SideMenuComponent extends AutoUnsubscribe {
    public selectedIndex = 0;
    public user!: User;
    public appName = ENV.appName;
    public appVersion = '';
    public environment = '';

    public appPages = [
        {
            title: 'HOMEPAGE',
            icon: 'iuffi-home',
            handler: (i: number): void => {
                this.selectedIndex = i;
                this._openHomePage();
            }
        },
        {
            title: 'CREATE_MISSION',
            icon: 'iuffi-newmission',
            handler: (): void => {
                // Mission apre una modale, non c'è bisogno di disabilitare l'index selezionato
                this._newMission();
            }
        },
        {
            title: 'LOGOUT',
            icon: 'iuffi-logout',
            handler: (i: number): void => {
                this.selectedIndex = i;
                this._logout();
            }
        }
    ];

    constructor(
        private deviceService: DeviceService,
        private rootNavCtrl: RootNavController,
        private authService: AuthService,
        private versioning: VersioningService,
        private platform: Platform
    ) {
        super();
        this.platform.ready().then(() => {
            this._setAppVersion();
        });
        this._initSubscriptions();
    }

    private async _setAppVersion(): Promise<void> {
        this.appVersion = await this.versioning.getAppVersion();
        if (ENV.target !== Environments.PROD) {
            this.environment = ENV.target;
        }
    }

    /**
     * Apre la pagina principale dell'app
     * @returns void
     */
    private _openHomePage(): void {
        this.rootNavCtrl.setRoot(HomePage);
    }

    /**
     * Apre la sezione delle impostazioni dell'app
     * @returns void
     */
    private _newMission(): void {
        this.rootNavCtrl.push(NewMissionPage, { mission: new Mission() });
        // this.rootNavCtrl.setRoot(
        //     SplitViewPage,
        //     this.deviceService.isTablet() ? SettingsPage.getSplitViewPageParamsForTablet() : SettingsPage.getSplitViewPageParamsForSmartphone()
        // );
    }

    private _logout(): void {
        this.authService.logout(SessionStates.LOGOUT);
        this.rootNavCtrl.setRoot(LoginPage, {}, {
            animated: true,
            direction: 'back'
        });
    }

    private _initSubscriptions(): void {
        this.authService.onSessionChanges$
            .pipe(
                takeUntil(this.destroy$),
                filter(sessionState => sessionState === SessionStates.LOGGED_NEW_USER || sessionState === SessionStates.LOGGED_LAST_USER)
            )
            .subscribe(() => this.user = this.authService.getUser() as User);
    }

}
