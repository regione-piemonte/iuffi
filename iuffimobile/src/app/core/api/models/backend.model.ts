import { Api } from './api.model';

export class Backend {
    public baseUrl: string;
    public api: Api[];
    public environment?: string;

    constructor(
        backend: Partial<Backend>
    ) {
        this.baseUrl = backend.baseUrl as string;
        this.api = (backend.api as Api[]).map((a: Api) => { return new Api(a); });
        this.environment = backend.environment || 'PROD';
    }
}
