import { LocalConfigDev } from '@core/config/models/local-config.dev';
import { Environment } from './models/environment';
import { Environments } from './models/environments';

export const ENV: Environment = {
    target: Environments.DEV,
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
    productionMode: false,
    storePrefix: 'IUFFI-Piemonte',
    devMode: true
};
// https:/tst-secure.sistemapiemonte.it/ssp_liv1_sisp_liv1_spid_GASPRP_AGRIC/Shibboleth.sso/Login?entityID=IDENTITY_PROVIDER_2_PA_SECURE.RUPARPIEMONTE.IT&target=https:**Atst-secure.sistemapiemonte.it*iuffiauth_col*login__;Ly8vLw!!LQkDIss!EF_SWhoDclnR-FSBcKVBTruMPelt7rimy5XoTRiMXg0ykf_AaneDVLaqFtEbGS36sk3n5Q$
