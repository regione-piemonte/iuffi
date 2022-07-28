import { HttpClientModule } from '@angular/common/http';
import { ModuleWithProviders, NgModule, Optional, SkipSelf } from '@angular/core';
import { AppError } from '@core/error';
import { IonicStorageModule } from '@ionic/storage';

import { ConfigModuleOptions } from './models/config-module-options.model';
import { ConfigService } from './services/config.service';

/**
* @name ConfigModule
* @description
* ConfigModule is an ngModule that imports a service to manage the external JSON config file
*/
@NgModule({
    imports: [
        IonicStorageModule.forRoot(),
        HttpClientModule
    ],
    providers: [
        ConfigService
    ]
})
export class ConfigModule {
    constructor(@Optional() @SkipSelf() parentModule: ConfigModule) {
        if (parentModule) {
            throw new AppError('ConfigModule is already loaded');
        }
    }

    /**
    * Allow to pass a <ConfigModuleOptions> configuration to services in ConfigModule
    * @param  {ConfigModuleOptions} options all available configuration for <ConfigModule>
    * @returns {ModuleWithProviders}
    */
    public static forRoot(options: ConfigModuleOptions): ModuleWithProviders<ConfigModule> {
        return {
            ngModule: ConfigModule,
            providers: [
                { provide: ConfigModuleOptions, useValue: options }
            ]
        }
    }
}
