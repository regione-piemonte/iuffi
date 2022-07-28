/* eslint-disable @typescript-eslint/no-var-requires */

declare const require: any;
export const LokiIndexedDbAdapter = require('lokijs/src/loki-indexed-adapter');
export const LokiIncrementalIndexedDbAdapter = require('lokijs/src/incremental-indexeddb-adapter');
export { LokiDBOptions } from './models/lokidb-options.model';
export { LokiDB } from './models/lokidb.model';
