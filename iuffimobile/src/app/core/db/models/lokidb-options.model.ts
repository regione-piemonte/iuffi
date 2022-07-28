import { LokiDbConfig } from './lokidb-config.model';
import { LokiLocalStorageAdapter } from 'lokijs';

export class LokiDBOptions implements LokiConstructorOptions, LokiConfigOptions {
    public verbose: boolean;
    public env: 'NATIVESCRIPT' | 'NODEJS' | 'CORDOVA' | 'BROWSER' | 'NA';

    public autosave: boolean;
    public autosaveInterval: string | number;
    public autosaveCallback: (err?: any) => void;
    public autoload: boolean;
    public autoloadCallback: (err: any) => void;
    public adapter: LokiPersistenceAdapter | null;
    public persistenceMethod: 'fs' | 'localStorage' | 'memory' | null;
    public serializationMethod: 'normal' | 'pretty' | 'destructured' | null;
    public destructureDelimiter: string;
    public throttledSaves: boolean;

    constructor(
        options: Partial<LokiDBOptions> = {}
    ) {
        this.verbose = options.verbose || false;
        this.env = options.env || 'BROWSER';

        this.autosave = options.autosave || false;
        this.autosaveInterval = options.autosaveInterval || 5000;
        this.autoload = options.autoload || false;
        if (options.autoloadCallback) {
            this.autoloadCallback = options.autoloadCallback;
        } else {
            this.autoloadCallback = () => { };
        }
        this.adapter = options.adapter || new LokiLocalStorageAdapter();
        this.persistenceMethod = options.persistenceMethod || 'localStorage';
        this.serializationMethod = options.serializationMethod || 'normal';
        this.destructureDelimiter = options.destructureDelimiter || '$<\n';
        this.throttledSaves = options.throttledSaves || true;
        if (options.autosaveCallback) {
            this.autosaveCallback = options.autosaveCallback;
        } else {
            this.autosaveCallback = () => { };
        }
    }

    public static fromConfig(dbConfig: LokiDbConfig): LokiDBOptions {
        const dbOptions = new LokiDBOptions(
            {
                verbose: dbConfig.verbose || false,
                adapter: new LokiLocalStorageAdapter(),
                env: dbConfig.env || 'BROWSER',
                autosave: dbConfig.autosave || false,
                autosaveInterval: dbConfig.autosaveInterval || 5000,
                autoload: dbConfig.autoload || false,
                persistenceMethod: dbConfig.persistenceMethod || 'localStorage',
                serializationMethod: dbConfig.serializationMethod || 'normal',
                destructureDelimiter: dbConfig.destructureDelimiter || '$<\n',
                throttledSaves: dbConfig.throttledSaves || true
            }
        );

        if (dbConfig.autoload) {
            dbOptions.autoloadCallback = () => { };
        }
        dbOptions.adapter = new LokiLocalStorageAdapter();

        if (dbConfig.autosave) {
            dbOptions.autosaveCallback = () => { };
        }
        return dbOptions;
    }
}
