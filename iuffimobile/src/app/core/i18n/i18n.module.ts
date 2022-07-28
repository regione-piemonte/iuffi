import { ModuleWithProviders, NgModule, Optional, SkipSelf } from '@angular/core';
import { AppError } from '@core/error';
import { Globalization } from '@ionic-native/globalization/ngx';
import { IonicStorageModule } from '@ionic/storage';
import { TranslateModule } from '@ngx-translate/core';

import { I18nModuleOptions } from './models/i18n-module-options.model';
import { IuffiI18nService } from './services/iuffi.i18n.service';

/**
* @name I18nModule
* @description
* I18nModule is an ngModule that imports a service to manage the external JSON language file
* and initialize the ngx-translate library
*/
@NgModule({
    imports: [
        IonicStorageModule.forRoot(),
        TranslateModule.forRoot(),
    ],
    providers: [
        Globalization,
        IuffiI18nService,
    ]
})
export class I18nModule {
    constructor(@Optional() @SkipSelf() parentModule: I18nModule) {
        if (parentModule) {
            throw new AppError('I18nModule is already loaded');
        }
    }

    /**
    * Allow to pass a <I18nModuleOptions> configuration to services in I18nModule
    * @param  {I18nModuleOptions} options all available configuration for <I18nModule>
    * @returns {ModuleWithProviders}
    */
    public static forRoot(options?: I18nModuleOptions): ModuleWithProviders<I18nModule> {
        return {
            ngModule: I18nModule,
            providers: [
                { provide: I18nModuleOptions, useValue: options }
            ]
        }
    }
}
