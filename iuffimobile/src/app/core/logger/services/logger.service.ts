import { Injectable, Optional } from '@angular/core';

import { LoggerLevels } from '../models/logger-levels';
import { LoggerModuleOptions } from '../models/logger-module-options.model';

declare const InstallTrigger: any;
declare const window: any;
declare const opr: any;
declare const document: any;
declare const safari: any;
// Opera 8.0+
// const isOpera = (!!window.opr && !!opr.addons) || !!window.opera || navigator.userAgent.indexOf(' OPR/') >= 0;
// Firefox 1.0+
const isFirefox = typeof InstallTrigger !== 'undefined';
// Safari 3.0+ "[object HTMLElementConstructor]"
// const isSafari = /constructor/i.test(window.HTMLElement) || (function(p) { return p.toString() === '[object SafariRemoteNotification]'; })(!window['safari'] || safari.pushNotification);
// Internet Explorer 6-11
// const isIE = /*@cc_on!@*/false || !!document.documentMode;
// Edge 20+
// const isEdge = !isIE && !!window.StyleMedia;
// Chrome 1+
//var isChrome = !!window.chrome && !!window.chrome.webstore;
// If isChrome is undefined, then use:
const isChrome = !!window.chrome && (!!window.chrome.webstore || !!window.chrome.runtime);
// Blink engine detection
// const isBlink = (isChrome || isOpera) && !!window.CSS;

@Injectable()
export class LoggerService {
    private logLevel: number = LoggerLevels.DEBUG;
    private overrideLogLevel: number | null = null;

    constructor(
        @Optional() config: LoggerModuleOptions
    ) {
        if (config) {
            // Check if the override log lever exists and if it's one of the available levels
            if (config.overrideLevel && config.overrideLevel in LoggerLevels) {
                this.overrideLogLevel = config.overrideLevel;
                this.logLevel = this.overrideLogLevel;
            }
        }
    }

    /**
     * Return the arg to string if is necessary
     * @param  {any} arg Argument to parse
     * @returns {string}
     */
    private parseArg(arg: any): string {
        if (arg instanceof Array || arg instanceof Object) {
            return JSON.stringify(arg);
        }
        else {
            return arg;
        }
    }

    /**
     * Print all supplied arguments with the right level function (on browser)
     * and convert all array and object to string (on device)
     * @param  {string} func Console function
     * @param  {any[]} others All other arguments to print
     */
    private _print(func: string, others: any[]): void {

        // If the app is running on device all arguments to print will be transformed into string
        if (!!(window as any).cordova) {
            const strings = others.map(this.parseArg);
            console.log(`${func.toUpperCase()}: ${strings.join(', ')}`);
        }
        else {
            // The app is running on browser
            // Get the line which call the logger
            let line = '';

            if (isChrome) {
                line = (new Error().stack as string).split('\n')[3];
                line = (line.indexOf(' (') >= 0) ? line.split(' (')[1].substring(0, line.length - 1) : line.split('at ')[1];
                line = line.lastIndexOf(')') !== -1 ? line.substring(0, line.lastIndexOf(')')) : line;
            }
            else if (isFirefox) {
                line = (new Error().stack as string).split('\n')[3].split('@')[1];
            }

            // Then print all arguments with the right console function
            const functionToCall = eval('console[func]');
            functionToCall(`${line}: `, ...others);
        }
    }

    /**
     * Change the logger level for future uses
     * @param  {string='DEBUG'} requestedLogLevel
     */
    public changeLevel(newLogLevel: string): void {
        // If the override was set the new log level will be ignored
        if (!this.overrideLogLevel) {
            // Check if the new log lever exists and if it's one of the available levels
            if (newLogLevel && newLogLevel.toUpperCase() in LoggerLevels) {
                this.logLevel = parseInt(LoggerLevels[newLogLevel.toUpperCase() as any]);
            }
        }

        console.log(`Logger is active with level <${LoggerLevels[this.logLevel]}>`);
    }

    /**
     * Print with warn function
     * @param  {any[]} ...args
     */
    public error(...args: any[]): void {
        this._print('error', args);
    }

    /**
     * Print with warn function
     * @param  {any[]} ...args
     */
    public warn(...args: any[]): void {
        if (this.logLevel >= LoggerLevels.WARN) {
            this._print('warn', args);
        }
    }

    /**
     * Print with info function
     * @param  {any[]} ...args
     */
    public info(...args: any[]): void {
        if (this.logLevel >= LoggerLevels.INFO) {
            this._print('info', args);
        }
    }

    /**
     * Print with debug function
     * @param  {any[]} ...args
     */
    public debug(...args: any[]): void {
        if (this.logLevel >= LoggerLevels.DEBUG) {
            this._print('debug', args);
        }
    }
}
