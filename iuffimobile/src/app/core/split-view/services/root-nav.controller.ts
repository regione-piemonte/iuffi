import { Injectable } from '@angular/core';
import { DeviceService } from '@core/device';
import { IonNav } from '@ionic/angular';
import { NavOptions } from '@ionic/core';

import { SplitViewService } from './split-view.service';

@Injectable()
export class RootNavController {
    private _rootNav!: IonNav;

    constructor(
        private deviceService: DeviceService,
        private splitViewService: SplitViewService
    ) { }

    /**
     * Inizializza la IonNav principale
     * @param  {IonNav} nav
     * @returns void
     */
    public init(nav: IonNav): void {
        this._rootNav = nav;
    }

    /**
     * Imposta la root della IonNav principale, inizializzando anche la lista delle splitview
     * @param  {any} page Componente pagina su cui navigare
     * @param  {any={}} params Parametri da inviare alla pagina
     * @param  {Partial<NavOptions>|undefined={}} options Opzioni di personalizzazione della navigazione
     * @returns Promise
     */
    public setRoot(page: any, params: any = {}, options: Partial<NavOptions> = {}): Promise<boolean> {
        this.splitViewService.resetSplitViews();
        return this._rootNav.setRoot(page, params, options);
    }

    /**
     * Naviga verso la pagina richiesta
     * @param  {any} page Componente pagina su cui navigare
     * @param  {any={}} params Parametri da inviare alla pagina
     * @param  {Partial<NavOptions>|undefined={}} options Opzioni di personalizzazione della navigazione
     * @returns Promise
     */
    public push(page: any, params: any = {}, options: Partial<NavOptions> = {}): Promise<boolean> {
        return this._rootNav.push(page, params, options);
    }

    /**
     * Torna verso la pagina precedente, rimuovendo anche l'ultima splitview
     * @param  {Partial<NavOptions>|undefined={}} options Opzioni di personalizzazione della navigazione
     * @returns Promise
     */
    public pop(options: Partial<NavOptions> = {}): Promise<boolean> {
        this.splitViewService.removeSplitView();
        return this._rootNav.pop(options);
    }

    /**
     * Torna alla prima pagina dello stack, rimuovendo anche tutte le splitview intermedie
     * @param  {Partial<NavOptions>|undefined={}} options Opzioni di personalizzazione della navigazione
     * @returns Promise
     */
    public popToRoot(options: Partial<NavOptions> = {}): Promise<boolean> {
        if (this.deviceService.isTablet()) {
            return this._rootNav.canGoBack().then(
                (canGoBack: boolean) => {
                    if (canGoBack) {
                        this.splitViewService.removeSplitViewToRoot();
                        return this._rootNav.popToRoot(options);
                    } else {
                        return Promise.resolve(false);
                    }
                }
            );
        }
        else {
            this.splitViewService.getMainSplitView().backToRoot(options);
        }
        return Promise.resolve(false);
    }
}
