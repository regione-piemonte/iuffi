import { LocalConfig } from '@core/config/models/local-config';
import { Environment } from './models/environment';
import { Environments } from './models/environments';

export const ENV: Environment = {
    target: Environments.PROD,
    professionalsLogin: {
        authUrl: 'https://secure.sistemapiemonte.it/iuffiauth/login',
        redirectUrl: 'https://secure.sistemapiemonte.it/iuffiauth/login'
    },
    publicLogin: {
        authUrl: 'https://secure.sistemapiemonte.it/ssp_liv1_sisp_liv1_spid_GASPRP_AGRIC/Shibboleth.sso/Login?entityID=IDENTITY_PROVIDER_2_PA_SECURE.RUPARPIEMONTE.IT&target=https://secure.sistemapiemonte.it/iuffiauth/login',
        redirectUrl: 'https://secure.sistemapiemonte.it/iuffiauth/login',
    },
    appName: 'MonitON',
    config: LocalConfig,
    basicAuth: {
        userid: 'iuffimobile',
        password: '<PASSWORD>',
        enabled: true
    },
    dbConfig: {
        dbName: 'Iuffi.db',
        adapter: 'INDEXED_DB',
        env: 'CORDOVA',
        verbose: true,
        autosave: false,
        autosaveInterval: 5000,
        autoload: false,
        persistenceMethod: 'localStorage',
        serializationMethod: 'normal',
        destructureDelimiter: '$<\n',
        throttledSaves: true
    },
    defaultLanguageCode: 'it-IT',
    productionMode: false,
    storePrefix: 'IUFFI-Piemonte',
    devMode: false
};
