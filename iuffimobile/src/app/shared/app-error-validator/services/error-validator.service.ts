import { Injectable, Renderer2, RendererFactory2 } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ValidationErrors, ValidatorFn } from '@angular/forms';
import { IuffiI18nService } from '@core/i18n';

@Injectable()
export class ErrorValidatorService {
    private _renderer: Renderer2;

    constructor(
        private rendererFactory: RendererFactory2,
        private ionic5AngularI18nService: IuffiI18nService
    ) {
        this._renderer = this.rendererFactory.createRenderer(null, null);
    }

    /**
     * Validatore per verificare la precisa lunghezza della string
     * @param  {number} length
     * @returns ValidationErrors
     */
    public perfectLength(length: number): ValidatorFn {
        return (control: AbstractControl): ValidationErrors | null => {
            if (control.value && control.value.length !== length) {
                return {
                    length: {
                        requiredLength: length
                    }
                };
            }
            return null;
        };
    }

    /**
     * Validatore per verificare il valore minimo se si inserisce un carattere con la virgola
     * @param  {number} min
     * @returns ValidationErrors
     */
    public minWithComma(min: number): ValidatorFn {
        return (control: AbstractControl): ValidationErrors | null => {
            if (control && control.value) {
                const controlValue = control.value.replace(',', '.');
                if (controlValue && +controlValue < min) {
                    return { min: true };
                }
            }

            return null;
        };
    }

    /**
     * Validatore che sostituisce gli apostri con l'unico ammesso
     * @param  {AbstractControl} control
     * @returns ValidationErrors
     */
    public aposReplacer(control: AbstractControl): ValidationErrors | null {
        if (!control.value) {
            return null;
        }

        // Sostituisco i diversi tipi di apostrofi (se presenti)
        // con l'unico autorizzato (apice semplice)
        const bannedApos = /[`´‘’]/gi;
        if (control.value.match(bannedApos)) {
            try {
                control.setValue(control.value.replace(bannedApos, '\''));
            } catch (e) { }
            return null;
        }

        return null;
    }

    /**
    * Validatore per verificare il match tra 2 valori
    * @param  {AbstractControl} control
    * @returns ValidationErrors
    */
    public mustMatch(controlName: string, matchingControlName: string): (f: FormGroup) => void {
        return (formGroup: FormGroup) => {
            const control = formGroup.controls[controlName];
            const matchingControl = formGroup.controls[matchingControlName];

            if (matchingControl.errors && !matchingControl.errors.mustMatch) {
                return;
            }

            if (control.value !== matchingControl.value) {
                matchingControl.setErrors({
                    mustMatch: {
                        controlName: this.ionic5AngularI18nService.translate(controlName),
                        matchingControlName: this.ionic5AngularI18nService.translate(matchingControlName),
                    }
                });
            }
            else {
                matchingControl.setErrors(null);
            }
        }
    }

    /**
     * Validatore per verificare che un campo sia diverso dal valore (value) passato in input
     * @param  {string} value valore con cui fare il confronto
     * @param  {string} controlName nome del campo
     * @param  {string} matchingControlName nome del campo con cui fare il confronto
     * @returns ValidationErrors
     */
    public mustNotMatch(value: string, controlName: string, matchingControlName: string): ValidatorFn {
        return (control: AbstractControl): ValidationErrors | null => {
            if (control.value) {
                const controlValue = control.value.replace(/,/, '.');
                const valueToCompare = value.replace(/,/, '.');
                if (controlValue == valueToCompare) {
                    return {
                        mustNotMatch: {
                            controlName: this.ionic5AngularI18nService.translate(controlName),
                            matchingControlName: this.ionic5AngularI18nService.translate(matchingControlName),
                        }
                    };
                }

            }
            return null;
        };
    }

    /**
    * Validatore per verificare che sia popolato almeno un campo della form
    * @param  {FormGroup} formGroup
    * @returns ValidationErrors
    */
    public atLeastOne() {
        return (formGroup: FormGroup): ValidationErrors | null => {
            const controls = formGroup.controls;
            const theOne = Object.keys(controls).find(key => controls[key].value !== '');

            if (!theOne) {
                return {
                    atLeastOneRequired: {
                        text: 'AT_LEAST_ONE'
                    }
                }
            }
            else {
                return null
            }

        }
    }

    /**
    * Se un campo 'master' è popolato, lo deve essere
    * anche il campo 'slave', in caso contrario viene settato l'errore sul campo 'slave'
    * @param  {FormGroup} formGroup
    * @returns ValidationErrors
    */

    public masterSlaveValue(masterControlName: string, slaveControlName: string): (f: FormGroup) => void {
        return (formGroup: FormGroup) => {
            const masterControl = formGroup.controls[masterControlName];
            const slaveControl = formGroup.controls[slaveControlName];

            if (masterControl.value !== '' && slaveControl.value == '') {

                slaveControl.setErrors({
                    empty: true
                });

            } else if (slaveControl.hasError('empty')) {
                const { empty, ...errors } = slaveControl.errors as any;
                if (Object.keys(errors as any).length === 0) {
                    slaveControl.setErrors(null);
                } else {
                    slaveControl.setErrors(errors);
                }
            }

        }
    }

    /**
     * Metodo per resettare lo stile di un control settato come invalido
     * @param  {FormControl} control campo
     * @param  {string} controlName nome del campo
     * @returns void
     */
    public resetControl(control: FormControl, controlName: string): void {
        const element = document.querySelector('[formControlName="' + controlName + '"]');
        if (control instanceof FormControl) {
            control.markAsPristine({ onlySelf: true });
            control.markAsUntouched({ onlySelf: true });
            if (element) {
                const ionItem = element.closest('ion-item');
                this._renderer.addClass(ionItem, 'ng-untouched');
                this._renderer.removeClass(ionItem, 'ng-touched');
                this._renderer.addClass(ionItem, 'ng-pristine');
                this._renderer.removeClass(ionItem, 'ng-dirty');
                this._renderer.addClass(ionItem, 'ng-valid');
                this._renderer.removeClass(ionItem, 'ng-invalid');
            }
        }
    }

    /**
     * Aggiorna tutti i controlli della form richiesta
     * @param  {FormGroup} formGroup
     * @returns void
     */
    public updateAllValidators(formGroup: FormGroup): void {
        // Per ogni input della form faccio girare una funzione che resetta le classi degli ion-item contenitori
        Object.keys(formGroup.controls).forEach(controlName => {
            const control = formGroup.get(controlName);

            // Se l'oggetto della form è un FormControl applico lo stile
            if (control instanceof FormControl) {
                control.updateValueAndValidity();
            }
            // Altrimenti se l'oggetto è una form innestata richiamo la funzione ricorsivamente
            else if (control instanceof FormGroup) {
                this.updateAllValidators(control);
            }
        });
    }

    /**
     * Resetta la validazione della form richiesta
     * @param  {FormGroup} formGroup
     * @returns void
     */
    public resetValidators(formGroup: FormGroup): void {
        // Per ogni input della form faccio girare una funzione che resetta le classi degli ion-item contenitori
        Object.keys(formGroup.controls).forEach(controlName => {
            const control = formGroup.get(controlName);
            const element = document.querySelector('[formControlName="' + controlName + '"]');

            // Se l'oggetto della form è un FormControl applico lo stile
            if (control instanceof FormControl) {
                control.markAsPristine({ onlySelf: true });
                control.markAsUntouched({ onlySelf: true });
                if (element) {
                    const ionItem = element.closest('ion-item');
                    this._renderer.addClass(ionItem, 'ng-untouched');
                    this._renderer.removeClass(ionItem, 'ng-touched');
                    this._renderer.addClass(ionItem, 'ng-pristine');
                    this._renderer.removeClass(ionItem, 'ng-dirty');
                    this._renderer.addClass(ionItem, 'ng-valid');
                    this._renderer.removeClass(ionItem, 'ng-invalid');
                }
            }
            // Altrimenti se l'oggetto è una form innestata richiamo la funzione ricorsivamente
            else if (control instanceof FormGroup) {
                this.resetValidators(control);
            }
        });
    }
}
