import { ModuleWithProviders, NgModule, Optional, SkipSelf } from '@angular/core';
import { AppError } from '@core/error';
import { Device } from '@ionic-native/device/ngx';
import { Dialogs } from '@ionic-native/dialogs/ngx';
import { Globalization } from '@ionic-native/globalization/ngx';
import { Keyboard } from '@ionic-native/keyboard/ngx';
import { Network } from '@ionic-native/network/ngx';
import { ScreenOrientation } from '@ionic-native/screen-orientation/ngx';
import { SpinnerDialog } from '@ionic-native/spinner-dialog/ngx';
import { SplashScreen } from '@ionic-native/splash-screen/ngx';
import { StatusBar } from '@ionic-native/status-bar/ngx';
import { IonicModule } from '@ionic/angular';

import { DeviceModuleOptions } from './models/device-module-options.model';
import { DeviceService } from './services/device.service';

/**
* @name DeviceModule
* @description
* DeviceModule is an ngModule that imports a lot of services and utils for a Cordova app
*/
@NgModule({
    imports: [
        IonicModule
    ],
    providers: [
        DeviceService,
        Network,
        SplashScreen,
        SpinnerDialog,
        Dialogs,
        StatusBar,
        ScreenOrientation,
        Keyboard,
        Device,
        Globalization
    ]
})
export class DeviceModule {
    constructor(@Optional() @SkipSelf() parentModule: DeviceModule) {
        if (parentModule) {
            throw new AppError('DeviceModule is already loaded. Import it in the AppModule only');
        }
    }

    /**
    * Allow to pass a <DeviceModuleOptions> configuration to DeviceService
    * @param  {DeviceModuleOptions} options all available configuration for <DeviceModule>
    * @returns {ModuleWithProviders}
    */
    public static forRoot(options?: Partial<DeviceModuleOptions>): ModuleWithProviders<DeviceModule> {
        return {
            ngModule: DeviceModule,
            providers: [
                { provide: DeviceModuleOptions, useValue: options }
            ]
        }
    }
}
