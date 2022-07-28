import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { ModuleWithProviders, NgModule, Optional, SkipSelf } from '@angular/core';
import { DeviceService } from '@core/device';
import { IonicModule } from '@ionic/angular';
import { SharedModule } from '@shared/shared.module';

import { AuthModuleOptions } from './models/auth-module-options.model';
import { AuthInterceptor } from './services/auth.interceptor';
import { AuthService } from './services/auth.service';

/**
 * @name AuthModule
 * @description
 * AuthModule is an ngModule that allows to centrilize all step for
 * authentication and uer verification
 */
@NgModule({
    imports: [
        IonicModule,
        HttpClientModule,
        SharedModule
    ],
    providers: [
        AuthService,
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthInterceptor,
            multi: true,
            deps: [
                AuthService,
                DeviceService
            ]
        },
    ]
})
export class AuthModule {
    constructor(@Optional() @SkipSelf() parentModule: AuthModule) {
        if (parentModule) {
            throw new Error('AuthModule is already loaded');
        }
    }

    /**
    * Allow to pass a <AuthModuleOptions> configuration to services in AuthModule
    * @param  {AuthModuleOptions} options all available configuration for <AuthModule>
    * @returns {ModuleWithProviders}
    */
    public static forRoot(options?: AuthModuleOptions): ModuleWithProviders<AuthModule> {
        return {
            ngModule: AuthModule,
            providers: [
                { provide: AuthModuleOptions, useValue: options }
            ]
        }
    }
}
