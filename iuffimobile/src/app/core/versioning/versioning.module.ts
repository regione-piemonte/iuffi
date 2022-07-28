import { NgModule } from '@angular/core';
import { DeviceService } from '@core/device';
import { AppVersion } from '@ionic-native/app-version/ngx';

import { VersioningService } from './services/versioning.service';

/**
* @name VersioningModule
* @description
* VersioningModule is an ngModule that imports a service to manage the versioning of the mobile app
*/
@NgModule({
    providers: [
        VersioningService,
        DeviceService,
        AppVersion
    ]
})
export class VersioningModule { }
