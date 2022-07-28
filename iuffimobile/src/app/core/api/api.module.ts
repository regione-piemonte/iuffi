import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { ConfigService } from '@core/config';
import { LoggerService } from '@core/logger';

import { ApiLoggerInterceptor } from './services/api-logger.interceptor';
import { ApiService } from './services/api.service';

/**
 * @name ApiModule
 * @description
 * ApiModule is an ngModule that allows to use all backend API
 * using ConfigService to get api's configuration
 * and HttpService to make the requests
 */
@NgModule({
    imports: [
        HttpClientModule
    ],
    providers: [
        ApiService,
        ConfigService,
        {
            provide: HTTP_INTERCEPTORS,
            useClass: ApiLoggerInterceptor,
            multi: true,
            deps: [LoggerService]
        },
    ]
})
export class ApiModule {

}
