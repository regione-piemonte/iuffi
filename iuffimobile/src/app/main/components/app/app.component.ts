import { Component, ViewChild } from '@angular/core';
import { DeviceService } from '@core/device';
import { RootNavController, SplitViewService } from '@core/split-view';
import { ENV } from '@env';
import { IonNav, Platform } from '@ionic/angular';

import { StarterPage } from '../../pages/starter/starter.page';

@Component({
    selector: 'app-root',
    templateUrl: 'app.component.html',
    styleUrls: ['app.component.scss']
})
export class AppComponent {
    public env = ENV.target;
    @ViewChild('rootNav', { read: IonNav }) public rootNav!: IonNav;
    public rootPage: any;
    public splitViewIsActive = false;

    constructor(
        private deviceService: DeviceService,
        private rootNavCtrl: RootNavController,
        private splitViewService: SplitViewService,
        private platform: Platform
    ) {
        const self = this;
        this.platform.ready().then(() => {
            self._initOrientation();
            self.deviceService.styleStatusBarAsDefault();
            self.rootPage = StarterPage;
        });
    }

    ngAfterViewInit(): void {
        this.rootNavCtrl.init(this.rootNav);
    }

    ngOnInit(): void {

        const self = this;
        // If device is tablet activate split view and unlock orientation
        if (this.deviceService.isTablet()) {
            this.splitViewIsActive = true;
            this.splitViewService.isOn$
                .subscribe((isActive: boolean) => {
                    self.splitViewIsActive = isActive;
                });
            this.splitViewService.activateSplitView();
        }
        // Otherwise deactivate split view and lock orientation in portrait
        else {
            this.splitViewIsActive = false;
            this.splitViewService.deactivateSplitView();
        }
    }

    /**
     * Initialize the native device orientation
     */
    private _initOrientation(): void {
        // If device is tablet activate split view and unlock orientation
        if (this.deviceService.isTablet()) {
            if (this.deviceService.isCordova()) {
                this.deviceService.lockOrientation(this.deviceService.ORIENTATIONS.PORTRAIT_PRIMARY);
            }
        }
        // Otherwise deactivate split view and lock orientation in portrait
        this.deviceService.lockOrientation(this.deviceService.ORIENTATIONS.PORTRAIT_PRIMARY);
    }
}
