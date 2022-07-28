import { LoginConfig } from '@core/auth/models/login-config.model';
import { Config } from '@core/config';
import { BasicAuthConfig } from './../../app/core/auth/models/basic-auth-config.model';
import { LokiDbConfig } from './../../app/core/db/models/lokidb-config.model';
import { Environments } from './environments';

export interface Environment {
    /**
     * Environment name
     */
    target: Environments;

     professionalsLogin: LoginConfig;

     publicLogin: LoginConfig;

    /**
     * Basic authentication config
     */
    basicAuth: BasicAuthConfig;

    /**
     * API base url
     */
    // baseUrl: string;

    /**
     * App name
     */
    appName: string;

    /**
     * URL of remote config file or local Config instance
     */
    config: string | Config;

    /**
     * local LokiDbConfig instance
     */
    dbConfig: LokiDbConfig;

    /**
     * ISO code of default language
     */
    defaultLanguageCode: string;

    /**
     * App's prefix used by Ionic storage
     */
    storePrefix: string;

    /**
     * Flag to enable the Angular's `enableProdMode()` method
     */
    productionMode: boolean;

    /**
     * FLag to enable the devMode used during the development process
     */
    devMode: boolean;
}
