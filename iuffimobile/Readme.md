# Ionic5Angular

Template Ionic 5 con framework Angular per lo sviluppo di app ibride.

## Progetto Cordova

-   Versione NodeJs 13.13.0
-   Versione Cordova 9.0.0
-   Versione ionic-cli 6.10

*   Le platform utilizzate sono:

    -   "cordova-android": "8.1.0"
    -   "cordova-ios": "5.1.1"

*   Per aggiungere al progetto le platform specificate sopra lanciare i comandi:
    -   cordova platform add android@8.1.0
    -   cordova platform add ios@5.1.1

## Modulo Core

Nel core sono presenti tutte quelle componenti che devono essere inizializzate
una ed una sola volta all'interno dell'app.

Nei paragrafi seguenti i dettagli

-   Configurazione dell'app tramite ConfigModule
-   Gestione degli aggironamenti tramite VersioningModule
-   Utilizzo delle API REST tramite ApiModule
-   Autenticazione ai serivzi tramite AuthModule
-   [Persistenza dei dati con LokiJS](./src/app/core/db/README.md)
-   Utilizzo della sensoristica tramite DeviceService
-   Supporto al multilingua tramite I18nService
-   [Supporto al multilayout smartphone e tablet tramite SplitViewModule](./src/app/core/split-view/README.md)
-   [Gestione icone con Icomoon](./src/assets/icons/README.md)
