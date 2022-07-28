import { Config } from './config.model';

export const LocalConfig: Config = {
    versioning: [
        {
            platform: 'android',
            lastVersion: '0.0.1',
            isMandatoryUpdate: false,
            storeLink: ''
        },
        {
            platform: 'ios',
            lastVersion: '0.0.1',
            isMandatoryUpdate: false,
            storeLink: ''
        }
    ],
    backend: {
        environment: 'PROD',
        baseUrl: 'https://secure.sistemapiemonte.it/iuffiweb/rest',
        api: [
            {
                name: 'getLastChangeTimestamp',
                method: 'GET',
                url: '/last-change-timestamp'
            },
            {
                name: 'getAreaTypes',
                method: 'GET',
                url: '/tipo-aree'
            },
            {
                name: 'getPlantSpecies',
                method: 'GET',
                url: '/specie-vegetali'
            },
            {
                name: 'getHarmfulOrganisms',
                method: 'GET',
                url: '/organismi-nocivi'
            },
            {
                name: 'getMonitoringSummary',
                method: 'GET',
                url: '/riepilogo-monitoraggio'
            },
            {
                name: 'getTrapTypes',
                method: 'GET',
                url: '/tipo-trappole'
            },
            {
                name: 'getTrapTypesByHO',
                method: 'GET',
                url: '/trappola-on'
            },
            {
                name: 'getSampleTypes',
                method: 'GET',
                url: '/tipo-campioni'
            },
            {
                name: 'getMunicipality',
                method: 'GET',
                url: '/comuni'
            },
            {
                name: 'getMissionList',
                method: 'POST',
                url: '/missioni'
            },
            {
                name: 'getInspectorList',
                method: 'GET',
                url: '/ispettori'
            },
            {
                name: 'saveMission',
                method: 'POST',
                url: '/missione'
            },
            {
                name: 'deleteMission',
                method: 'DELETE',
                url: '/missione/{idMissione}'
            },
            {
                name: 'getMissionDetail',
                method: 'GET',
                url: '/missione/{idMissione}'
            },
            {
                name: 'getPdfTrasferta',
                method: 'GET',
                url: '/pdf-trasferta/{idMissione}'
            },
            {
                name: 'saveDetection',
                method: 'POST',
                url: '/rilevazione'
            },
            {
                name: 'deleteDetection',
                method: 'DELETE',
                url: '/rilevazione/{idRilevazione}'
            },
            {
                name: 'saveVisualInspection',
                method: 'POST',
                url: '/ispezione-visiva'
            },
            {
                name: 'deleteVisualInspection',
                method: 'DELETE',
                url: '/visual/{idIspezione}'
            },
            {
                name: 'getAnagraficheAviv',
                method: 'GET',
                url: '/anagrafica-aviv/{codAz}'
            },
            {
                name: 'getSpecieAviv',
                method: 'GET',
                url: '/specie-aviv/{codAz}'
            },
            {
                name: 'getVerbaleAviv',
                method: 'GET',
                url: '/verbale-aviv/{idUte}'
            },
            {
                name: 'savePhoto',
                method: 'POST',
                url: '/immagine'
            },
            {
                name: 'getPhotos',
                method: 'POST',
                url: '/immagini'
            },
            {
                name: 'deletePhoto',
                method: 'DELETE',
                url: '/immagine/{idPhoto}'
            },
            {
                name: 'saveSampling',
                method: 'POST',
                url: '/campione'
            },
            {
                name: 'deleteSampling',
                method: 'DELETE',
                url: '/campione/{idCampionamento}'
            },
            {
                name: 'wmsService',
                method: 'GET',
                url: '/proxywms'
            },
            {
                name: 'getTraps',
                method: 'GET',
                url: '/trappole/{lat}/{lng}/{radius}/'
            },
            {
                name: 'trappolaggio',
                method: 'POST',
                url: '/trappola/'
            },
            {
                name: 'deleteTrap',
                method: 'DELETE',
                url: '/trappolaggio/{idTrappolaggio}'
            },
            {
                name: 'getTrapByCode',
                method: 'GET',
                url: '/trappola/{sfrCode}'
            },
            {
                name: 'getTrapHistory',
                method: 'GET',
                url: '/trappolaggi/{trapId}'
            }
        ]
    },
    configCustomProperties: {
        key: '',
        languageFlagUrl: '/',
    },
    loggerLevel: 'ERROR',
    devMode: false
};
