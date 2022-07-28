import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoggerService } from '@core/logger';
import { ENV } from '@env';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable()
export class ApiLoggerInterceptor implements HttpInterceptor {
    constructor(
        private logger: LoggerService
    ) { }
    public intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

        if (ENV.devMode) {
            console.info('REQUEST', req);
        }
        else {
            this.logger.debug('REQUEST', req);
        }

        return next
            .handle(req)
            .pipe(tap((response: HttpEvent<any>) => {
                if (ENV.devMode) {
                    console.info('RESPONSE OK', response);
                }
                else {
                    this.logger.debug('RESPONSE OK', response);
                }
            }));
    }
}
