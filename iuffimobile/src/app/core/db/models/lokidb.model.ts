import * as LokiJS from 'lokijs';

import { LokiDBOptions } from './lokidb-options.model';

const _collectionPrefix = '_';

export class LokiDB {
    private _options: LokiDBOptions;
    private _db: LokiJS;

    constructor(
        name: string,
        options: LokiDBOptions,
        loaders: { [collName: string]: any } = {}
    ) {
        this._options = this._initOptions(options, loaders);
        this._db = new LokiJS(name, this._options);
    }

    /**
     * Verifica se il DB è stato caricato correttamente
     * @returns boolean
     */
    public isLoaded(): boolean {
        return typeof this._db !== 'undefined';
    }

    /**
     * Inizializza le opzioni del database aggiungendo anche le configurazioni dei loader delle varie collection
     * @param  {LokiDBOptions} options
     * @returns LokiDBOptions
     */
    private _initOptions(options: LokiDBOptions, loaders: { [collName: string]: any } = {}): LokiDBOptions {
        const collections = Object.keys(loaders);
        if (collections.length > 0) {
            collections.forEach(collName => {
                (options as any)[_collectionPrefix + collName] = {
                    proto: loaders[collName]
                };
            });
        }
        return options;
    }

    /**
     * Legge il DB dalla memoria persistente e lo carica in RAM
     *
     * Da usare quando il database viene inizializzato con autoload = false
     * @returns Promise
     */
    public load(): Promise<LokiJS> {
        const self = this;
        return new Promise((resolve, reject) => {
            return self._db.loadDatabase(this._options as any, (err: any) => {
                if (err) {
                    reject(err);
                }
                else {
                    resolve(self._db);
                }
            });
        });
    }

    /**
     * Recupera la collection, se esiste,
     * altrimenti la crea
     * @param  {string} name
     */
    // eslint-disable-next-line @typescript-eslint/ban-types
    public initCollection<T extends object = any>(name: string): LokiJS.Collection<T> {
        let newCollection = this._db.getCollection<T>(_collectionPrefix + name) as Collection<T & LokiObj>;
        if (newCollection === null) {
            newCollection = this._db.addCollection<T>(_collectionPrefix + name) as Collection<T & LokiObj>;
        }
        newCollection.data = newCollection.data.filter(
            doc => typeof doc.$loki === 'number' && typeof doc.meta === 'object'
        );
        newCollection.ensureId();
        newCollection.ensureAllIndexes();
        return newCollection as Collection<T>;
    }

    /**
     * Recupera l'istanza del database LokiJS
     * @returns LokiJS
     */
    public getLokiJS(): LokiJS {
        return this._db;
    }

    /**
     * Salva manualmente il database nella memoria persistente
     * @returns void
     */
    public save(): void {
        this._db.saveDatabase();
    }
}
