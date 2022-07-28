import { HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { DeviceService } from '@core/device';

import { ApiError } from '../models/api-error.model';
import { AppError } from '../models/app-error.model';

@Injectable()
export class ErrorService {

    constructor(
        private deviceService: DeviceService,
    ) { }

    private _parseAPIError(err: ApiError | null): AppError {
        if (err) {
            switch (err.status) {

            case 400:
                return this._handleError(err);

            case 401:
                return this._handle401Error();

                // Accesso negato – necessarie ulteriori operazioni
            case 403:
                return this._handleError(err);

                // Risorsa non trovata
            case 404:
                return this._handleError(err);

            case 409:
                return this._handle409Error(err);

            case 500:
                return this._handleError(err);
            }
        }
        // Altri errori
        return this._handleError(err);
    }

    public createError(errorResponse: HttpErrorResponse): AppError {
        if (this.deviceService.isOnline()) {
            if (errorResponse.status < 0) {
                return new AppError('UNKNOW_ERROR', { status: errorResponse.status+'' });
            }
            else {
                return this._parseAPIError(errorResponse);
            }

        }
        else {
            return new AppError('DEVICE_OFFLINE', { status: '-1' });
        }
    }

    /**
     * Gestione dell'errore generico
     * @param  {ApiError} err
     * @returns AppError
     */
    private _handleError(err: any | null): AppError {
        return new AppError(err.message);
    }

    /**
     * Gestione dell'errore generico
     * @param  {ApiError} err
     * @returns AppError
     */
    private _handle401Error(): AppError {
        return new AppError('');
    }

    /**
     * Gestione dell'errore generico
     * @param  {ApiError} err
     * @returns AppError
     */
    private _handle409Error(err: any | null): AppError {
        return new AppError('', {status: err.status, code: err.error.codErrore});
    }
}
