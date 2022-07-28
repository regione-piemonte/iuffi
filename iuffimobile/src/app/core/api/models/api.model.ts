import { HttpHeaders } from '@angular/common/http';

import { RequestMethods } from './request-methods';

export class Api {
    public name: string;
    public url: string;
    public method?: string;
    public headers?: HttpHeaders | { [header: string]: string | string[] };
    public timeout?: number;

    constructor(
        api: Api
    ) {
        this.name = api.name || '';
        this.url = api.url;
        this.method = (api.method || RequestMethods.GET).toUpperCase();
        this.headers = new HttpHeaders().set('Content-Type', 'application/json; charset=UTF-8');
        if (api.headers) {
            for (const key in api.headers) {
                this.headers = (this.headers as HttpHeaders).set(key, (api.headers as any)[key].toString());
            }
        }
        this.timeout = api.timeout || 30000;
    }
}
