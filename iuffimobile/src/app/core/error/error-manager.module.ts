import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';

import { ErrorService } from './services/error.service';
import { ResponseErrorInterceptor } from './services/response-error.interceptor';

/**
 * @name ErrorManagerModule
 * @description
 * ErrorManagerModule is an ngModule that allows to centrilize all errors
 * using alert or modal client side notification
 */
@NgModule({
    imports: [
        HttpClientModule
    ],
    providers: [
        ErrorService,
        { provide: HTTP_INTERCEPTORS, useClass: ResponseErrorInterceptor, multi: true },
    ]
})
export class ErrorManagerModule {

}
