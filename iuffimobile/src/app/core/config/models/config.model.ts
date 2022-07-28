import { Backend } from '@core/api';
import { LoggerLevels } from '@core/logger';
import { Versioning } from '@core/versioning';

import { ConfigCustomProperties } from './config-custom-properties.model';

export class Config {
    public lastModified?: string | null;
    public versioning: Versioning[];
    public backend: Backend;
    public configCustomProperties: ConfigCustomProperties;
    public loggerLevel: keyof typeof LoggerLevels;
    public devMode: boolean;

    constructor(config: Config) {
        this.versioning = config.versioning || [];
        this.backend = new Backend(config.backend);
        this.configCustomProperties = new ConfigCustomProperties(config.configCustomProperties);
        this.loggerLevel = config.loggerLevel || 'ERROR';
        this.devMode = config.devMode || false;
        this.lastModified = config.lastModified || null;
    }
}
