import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { ErrorService } from './error.service';

@Injectable()
export class ResponseErrorInterceptor implements HttpInterceptor {

    constructor(
        private errorService: ErrorService
    ) { }

    public intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(req).pipe(catchError((apiError: HttpErrorResponse) => {
            const appError = this.errorService.createError(apiError);
            return throwError(appError);
        }));
    }
}
