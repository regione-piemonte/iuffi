import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AppError } from '@core/error';
import { Observable } from 'rxjs';

import { Api } from '../models/api.model';
import { Backend } from '../models/backend.model';
import { HttpClientOptions } from '../models/http-client-options.model';
import { HttpObserve } from '../models/http-observe';
import { RequestMethods } from '../models/request-methods';
import { ResponseTypes } from '../models/response-types';

@Injectable()
export class ApiService {
    private _backend: Backend | null = null;

    constructor(
        private http: HttpClient
    ) { }

    /**
     * Get the main API baseUrl
     * @returns string
     */
    public getBaseUrl(): string {
        if (this._backend) {
            return this._backend.baseUrl;
        }
        return '';
    }

    /**
     * Init the Api Service with configuration fetched from config file
     * @param backend Beckend configuration for Api Service
     */
    public init(backend: Backend): void {
        this._backend = (backend instanceof Backend) ? backend : new Backend(backend);
    }

    /**
     * Get the API configuration from its name
     * @param  {string} apiName
     * @returns Api
     */
    private _getApi(apiName: string): Api | null {
        let api;
        try {
            api = this._backend?.api.find(api => api.name === apiName) as Api;
            api.url = this._prepareBaseUrl(api.url);
        }
        catch (err) {
            api = null;
        }
        return api;
    }

    /**
     * Create a new API configuration
     * @param url string HTTP request's url
     * @param method string HTTP request's method
     * @returns {Api|null}
     */
    public createNewApi(url: string, method: string = RequestMethods.GET): Api {
        url = this._prepareBaseUrl(url);
        const api = new Api({
            name: 'newApi',
            url: url,
            method: method
        });
        return api;
    }

    /**
     * Create a new request with dynamic method, params, body, headers.
     * Create a new Api instance from given api name setting method, url, headers and timeout directly from config.json file.
     * After that add/set HTTP query params, body (for POST, PUT, PATCH requests), and HTTP headers.
     * @param  {string} api Property name of Api class
     * @param  {object} options.params HTTP query params to use in all request
     * @param  {object} options.paths HTTP path params to use in all request
     * @param  {any} options.body HTTP body for a POST, PUT or PATCH request
     * @param  {object} options.headers HTTP header to add to request
     * @param  {ResposeTypes} options.responseType HTTP response type for request
     * @returns Promise
     */
    public callApi<T>(
        apiName: string,
        options: {
            params?: Record<string, any>;
            paths?: Record<string, any>;
            body?: any;
            headers?: { [header: string]: string | string[] };
            observe?: HttpObserve;
            responseType?: ResponseTypes;
        } = {}
    ): Observable<T> {
        // Use getApi in configService to define all options for api
        const api = Object.assign({}, this._getApi(apiName));

        if (api && api.url) {
            // Set the request's url
            api.url = this._prepareUrl(api.url, options.paths);

            const httpClientOptions = this._prepareOptions(api, options);

            return this.call(api, httpClientOptions);
        }
        else {
            throw new AppError(`API ${apiName} non configurata`);
        }
    }

    /**
     * Build a url of api from the global configuration
     * of model and optionaly the pass params
     * @param {string} apiName a api name
     * @param {object={}} paths a path params
     * @return {string | null} api's url
     */
    public getApiUrl(apiName: string, paths?: Record<string, any>): string | null {
        const api = Object.assign({}, this._getApi(apiName));
        if (api) {
            return this._prepareUrl(api.url, paths);
        }
        else {
            return null;
        }
    }

    /**
     * Add baseUrl as prefix if the api url is relative
     * @param {string} url Relative api url
     */
    private _prepareBaseUrl(url: string): string {
        if (url.trim().indexOf('http') !== 0) {
            url = (this._backend?.baseUrl + url).trim();
        }
        return url;
    }

    /**
     * Build a url of api from the global configuration
     * of model and optionaly the pass params
     * @param {string} url a api url
     * @param {object={}} paths a path params
     * @return {string} api's url
     */
    private _prepareUrl(url: string, paths?: Record<string, any>): string {
        return paths && Object.keys(paths).length
            ? this._bindPathParams(url, paths)
            : url;
    }

    /**
     * Bind a path param name with the pass value
     * @param {string} url request url to replace
     * @param {object={}} paths object key => val
     * @return {string}
     */
    private _bindPathParams(url: string, params: Record<string, any>): string {
        for (const key in params) {
            url = url.replace(new RegExp(`{${key}}`, 'g'), params[key]);
        }
        return url;
    }

    private _prepareOptions(
        api: Api,
        options: {
            params?: Record<string, any>;
            body?: any;
            headers?: { [header: string]: string | string[] };
            observe?: HttpObserve;
            responseType?: ResponseTypes;
        } = {}
    ): HttpClientOptions {

        // Create a new HttpRequest base on API method
        const httpClientOptions = new HttpClientOptions();

        // Add all requested HttpParams
        if (!options.params) {
            options.params = {};
        }
        let queryParams = new HttpParams();
        for (const qKey in options.params) {
            queryParams = queryParams.set(qKey, (options.params as any)[qKey]);
        }
        httpClientOptions.params = queryParams;

        // Add all requested HttpHeaders
        if (options.headers) {
            let newHeaders = new HttpHeaders();
            for (const hKey in options.headers) {
                newHeaders = newHeaders.set(hKey, (options as any).headers[hKey]);
            }
            httpClientOptions.headers = newHeaders;
        }
        else {
            httpClientOptions.headers = api.headers;
        }

        httpClientOptions.body = options.body;
        httpClientOptions.observe = options.observe || 'body';
        httpClientOptions.responseType = options.responseType || ResponseTypes.JSON;

        return httpClientOptions;
    }

    /**
     * Make the HTTP request with the prepared Api and options
     * @param  {Api} api
     * @param  {HttpClientOptions} options
     * @returns Observable
     */
    public call<T>(api: Api, httpClientOptions: HttpClientOptions): Observable<T> {
        return this.http.request(api.method as string, api.url, httpClientOptions);
    }
}
