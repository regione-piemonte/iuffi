import { NgModule } from '@angular/core';
import { ENV } from '@env';

import { ApiModule } from './api/api.module';
import { AuthModule } from './auth/auth.module';
import { ConfigModule } from './config/config.module';
import { DeviceModule } from './device/device.module';
import { ErrorManagerModule } from './error/error-manager.module';
import { I18nModule } from './i18n/i18n.module';
import { LoggerModule } from './logger/logger.module';
import { SplitViewModule } from './split-view/split-view.module';
import { VersioningModule } from './versioning/versioning.module';

@NgModule({
    imports: [
        LoggerModule,
        DeviceModule.forRoot({
            dialogsMode: 'native',
            loadingMode: 'ionic',
            modalHeader: ENV.appName
        }),
        ConfigModule.forRoot({
            config: ENV.config,
            storePrefix: ENV.storePrefix
        }),
        ApiModule,
        VersioningModule,
        I18nModule.forRoot({
            defaultLanguageCode: ENV.defaultLanguageCode,
            storePrefix: ENV.storePrefix
        }),
        AuthModule.forRoot({
            storePrefix: ENV.storePrefix
        }),
        SplitViewModule,
        ErrorManagerModule
    ]
})
export class CoreModule { }
