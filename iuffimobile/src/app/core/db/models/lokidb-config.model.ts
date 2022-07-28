export class LokiDbConfig {
    public dbName = '';
    public adapter: 'LOCALSTORAGE' | 'INDEXED_DB';
    public verbose: boolean;
    public env: 'NATIVESCRIPT' | 'NODEJS' | 'CORDOVA' | 'BROWSER' | 'NA';
    public autosave: boolean;
    public autosaveInterval: string | number;
    public autoload: boolean;
    public persistenceMethod: 'fs' | 'localStorage' | 'memory' | null;
    public serializationMethod: 'normal' | 'pretty' | 'destructured' | null;
    public destructureDelimiter: string;
    public throttledSaves: boolean;

    constructor(config: Partial<LokiDbConfig> = {}) {
        this.dbName = config.dbName || 'appDB';
        this.adapter = config.adapter || 'LOCALSTORAGE';
        this.verbose = config.verbose || false;
        this.env = config.env || 'BROWSER';
        this.autosave = config.autosave || false;
        this.autosaveInterval = config.autosaveInterval || 5000;
        this.autoload = config.autoload || false;
        this.persistenceMethod = config.persistenceMethod || 'localStorage';
        this.serializationMethod = config.serializationMethod || 'normal';
        this.destructureDelimiter = config.destructureDelimiter || '$<\n';
        this.throttledSaves = config.throttledSaves || true;
    }
}
