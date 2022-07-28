import { LocalConfigDev } from '@core/config/models/local-config.dev';
import { Environment } from './models/environment';
import { Environments } from './models/environments';

export const ENV: Environment = {
    target: Environments.TEST,
    professionalsLogin: {
        authUrl: 'https://tst-secure.sistemapiemonte.it/iuffiauth/login',
        redirectUrl: 'https://tst-secure.sistemapiemonte.it/iuffiauth/login'
    },
    publicLogin: {
        authUrl: 'https://tst-secure.sistemapiemonte.it/ssp_liv1_sisp_liv1_spid_GASPRP_AGRIC/Shibboleth.sso/Login?entityID=IDENTITY_PROVIDER_2_PA_SECURE.RUPARPIEMONTE.IT&target=https://tst-secure.sistemapiemonte.it/iuffiauth/login',
        redirectUrl: 'https://tst-secure.sistemapiemonte.it/iuffiauth/login',
    },
    appName: 'MonitON',
    config: LocalConfigDev,
    basicAuth: {
        userid: 'iuffimobile',
        password: '<PASSWORD>',
        enabled: true
    },
    dbConfig: {
        dbName: 'Iuffi.db',
        adapter: 'LOCALSTORAGE',
        env: 'BROWSER',
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
    productionMode: true,
    storePrefix: 'IUFFI-Piemonte',
    devMode: false
};
