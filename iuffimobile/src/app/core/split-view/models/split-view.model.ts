import { AutoUnsubscribe } from '@core/utils';
import { ENV } from '@env';
import { IonNav } from '@ionic/angular';
import { NavOptions } from '@ionic/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { SplitViewPlaceholderPage } from '../pages/placeholder/split-view-placeholder.page';
import { SplitViewConfig } from './split-view-config.model';

export class SplitView extends AutoUnsubscribe {
    private initialDetailPage!: Partial<SplitViewConfig>;

    constructor(
        private masterNav: IonNav,
        private detailNav: IonNav,
        private isOn: boolean,
        private isOn$: Subject<boolean>
    ) {
        super();
        const self = this;
        this.isOn$
            .pipe(takeUntil(this.destroy$))
            .subscribe((isOn: boolean) => {
                self.isOn = isOn;
                // if the nav controllers have been instantiated...
                if (self.masterNav && self.detailNav) {
                    if (self.isOn) {
                        self._activateSplitView();
                    }
                    else {
                        self._deactivateSplitView();
                    }
                }
            });
    }

    /**
     * Force first page of master view
     * @param  {any} page
     */
    public initMaster(page: any, params: { title?: string; icon?: string; label?: string } = {}): Promise<boolean> {
        return this.masterNav.setRoot(page, params);
    }
    public pushOnMaster(page: any, params: any = {}, options: Partial<NavOptions> | undefined = {}): Promise<boolean> {
        return this.masterNav.push(page, params, options);
    }

    /**
     * * Force first page of detail view
     * @param  {any} page
     */
    public initDetail(page: any, params: { title?: string; icon?: string; label?: string } = {}): Promise<boolean> {
        if (!params.title) {
            params.title = ENV.appName;
        }
        this.initialDetailPage = {
            page,
            params
        };
        return this.detailNav.setRoot(page, params);
    }

    /**
     * If the split view if active set the root of detail nav
     * otherwise make a simple push on the master nav
     * @param  {any} page
     * @param  {any={}} params
     * @param  {Partial<NavOptions>|undefined={}} options
     */
    public setRootOnDetail(page: any, params: any = {}, options: Partial<NavOptions> | undefined = {}): Promise<boolean> {
        if (this.isOn) {
            return this.detailNav.setRoot(page, params, options);
        }
        else {
            return this.pushOnMaster(page, params, options);
        }
    }
    /**
     * Make a simple push on the master nav,
     * or the detail nav if the split view is active
     * @param  {any} page
     * @param  {any={}} params
     * @param  {Partial<NavOptions>|undefined={}} options
     */
    public pushOnDetail(page: any, params: any = {}, options: Partial<NavOptions> | undefined = {}): Promise<boolean> {
        if (this.isOn) {
            return this.detailNav.push(page, params, options);
        }
        else {
            return this.pushOnMaster(page, params, options);
        }
    }

    /**
     * Load the initial detail page of the SplitView
     * @param  {Partial<NavOptions>|undefined={}} options
     * @returns Promise
     */
    public goToFirstDetailPage(options: Partial<NavOptions> | undefined = {}): Promise<boolean> {
        if (this.isOn) {
            return this.detailNav.popToRoot(options);
        }
        else {
            return this.masterNav.popToRoot(options);
        }
    }

    /**
     * Make a back on the master or detail page of the SplitView, when possibile
     * @param  {Partial<NavOptions>|undefined={}} options
     * @returns Promise
     */
    public back(options: Partial<NavOptions> | undefined = {}): Promise<boolean> {
        if (this.isOn) {
            return this.detailNav.canGoBack().then(
                (canGoBack: boolean) => {
                    if (canGoBack) {
                        return this.detailNav.pop(options);
                    }
                    else if (this.initialDetailPage) {
                        return this.detailNav.setRoot(this.initialDetailPage.page, this.initialDetailPage.params);
                    }
                    return Promise.resolve(false);
                }
            );
        }
        else {
            return this.masterNav.canGoBack().then(
                (canGoBack: boolean) => {
                    if (canGoBack) {
                        return this.masterNav.pop(options);
                    }
                    return Promise.resolve(false);
                }
            );
        }

    }

    /**
     * Make a back to the root of the master or detail page of the SplitView, when possibile
     * @param  {Partial<NavOptions>|undefined={}} options
     * @returns Promise
     */
    public backToRoot(options: Partial<NavOptions> | undefined = {}): Promise<boolean> {
        if (this.isOn) {
            return this.detailNav.canGoBack().then(
                (canGoBack: boolean) => {
                    if (canGoBack) {
                        return this.detailNav.popToRoot(options);
                    }
                    else if (this.initialDetailPage) {
                        return this.detailNav.setRoot(this.initialDetailPage.page, this.initialDetailPage.params);
                    }
                    return Promise.resolve(false);
                }
            );
        }
        else {
            return this.masterNav.canGoBack().then(
                (canGoBack: boolean) => {
                    if (canGoBack) {
                        return this.masterNav.popToRoot(options);
                    }
                    return Promise.resolve(false);
                }
            );
        }
    }

    /**
     * Activate the SplitView
     * @returns void
     */
    private _activateSplitView(): void {
        this.masterNav.getActive();
    }

    /**
     * Deactivate the SplitView
     * @returns void
     */
    private _deactivateSplitView(): void {
        this.detailNav.getActive();
        this.detailNav.setRoot(SplitViewPlaceholderPage);
    }
}
