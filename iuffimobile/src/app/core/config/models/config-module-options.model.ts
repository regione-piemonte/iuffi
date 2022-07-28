import { Config } from './config.model';

export class ConfigModuleOptions {
    constructor(
        public config: string | Config,
        public storePrefix?: string
    ) { }
}
