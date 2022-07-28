import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { DeviceService } from '@core/device';
import { ENV } from '@env';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

    constructor(
        private authService: AuthService,
        private deviceService: DeviceService,
    ) { }

    public intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        let newHeaders = req.headers;

        if (ENV.basicAuth.enabled) {
            newHeaders = newHeaders.set('Authorization', 'Basic ' + btoa(ENV.basicAuth.userid + ':' + ENV.basicAuth.password));
        }

        let accessToken: string | null = null;
        if (ENV.devMode) {
            // CSI DEMO 20
            accessToken = 'eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIyZGZjMjFhMS02ZmRlLTQ0NDMtOGJmYS1kZTk2OTNmYjQyNGMiLCJpYXQiOjE2MzMwOTIxMzMsImV4cCI6MTYzMzEzNTMzMywiaXNzIjoiaXVmZmlhdXRoIC0gQ1NJIFBpZW1vbnRlIiwiYXVkIjoiSVVGRkkgbW9iaWxlIHN5c3RlbSIsInN1YiI6IkFBQUFBQTAwQjc3QjAwMEYiLCJub21lIjoiQ3NpIFBpZW1vbnRlIiwiY29nbm9tZSI6IkRlbW8gMjAifQ.M4Wnbhluvb2-fyEzeJACE6dv7yBmH7pqe3OB4AB654ctyrs2vNPgVd2sUXZzQuJOyia10e5VgOE56B1Iww2wqQ';
            // CSI DEMO 21
            // accessToken = 'eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJmYjczOWYyYy0xZjIyLTQyYTItYTc2Mi0xOTZmYzE2MDg5M2IiLCJpYXQiOjE2MTEwNzU0NDIsImV4cCI6MTYxMTA3NTUwMiwiaXNzIjoiaXVmZmlhdXRoIC0gQ1NJIFBpZW1vbnRlIiwiYXVkIjoiSVVGRkkgbW9iaWxlIHN5c3RlbSIsInN1YiI6IkFBQUFBQTAwQTExQjAwMEoiLCJub21lIjoiQ1NJIFBJRU1PTlRFIiwiY29nbm9tZSI6IkRFTU8gMjEifQ.bzpkwmtZ-cGlQOQzzhEuL2MttLv4z7czUEFIGE7Awu87x6DrdDIjITTTSIoEYdcp32WXmFV5nt-zIBLdkiLu2A';
        }
        else {
            accessToken = this.authService.getAccessToken();
        }

        if (accessToken) {
            newHeaders = newHeaders.set('Token', 'Bearer ' + accessToken);
        }

        const copiedReq = req.clone({ headers: newHeaders });

        return next.handle(copiedReq);
    }
}
