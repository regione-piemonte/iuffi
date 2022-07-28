import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

import { SplitViewConfig } from '../models/split-view-config.model';
import { SplitView } from '../models/split-view.model';

@Injectable()
export class SplitViewService {
    private isOn = false;
    private splitViewList: SplitView[] = [];
    private visibileSplitViewIndex = 0;
    public isOn$: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

    /**
     * Inizialize a SplitView with master and detail configurations
     *
     * In no `index` will be provided, will be use the next index
     * @param  {SplitViewConfig} masterView
     * @param  {SplitViewConfig} detailView
     * @param  {number} index?
     * @returns SplitView
     */
    public initSplitView(masterView: SplitViewConfig, detailView: SplitViewConfig, index?: number): SplitView {
        if (index === undefined) {
            index = this.splitViewList.length;
        }
        this.splitViewList[index] = new SplitView(masterView.nav, detailView.nav, this.isOn, this.isOn$);
        this.splitViewList[index].initMaster(masterView.page, masterView.params);
        this.splitViewList[index].initDetail(detailView.page, detailView.params);
        return this.splitViewList[index];
    }

    /**
     * Get the SplitView instance related to the provided index
     * @param  {number} index
     * @returns SplitView
     */
    public getSplitView(index: number): SplitView {
        return this.splitViewList[index];
    }

    /**
     * Get the main (last) inizialized SplitView
     * @returns SplitView
     */
    public getMainSplitView(): SplitView {
        return this.splitViewList[this.splitViewList.length - 1];
    }

    /**
     * Check if SplitView is active
     * @returns boolean
     */
    public isActive(): boolean {
        return this.isOn;
    }

    /**
     * Set the SplitView with provided index as the visbile one
     * @param  {number} index
     * @returns void
     */
    public setVisibleSplitView(index: number): void {
        if (typeof index !== undefined && this.splitViewList[index]) {
            this.visibileSplitViewIndex = index;
        }
    }

    /**
     * Make a back to the current visible SplitView
     * @returns void
     */
    public back(): void {
        this.splitViewList[this.visibileSplitViewIndex].back();
    }

    public onSplitPaneChanged(isOn: boolean): void {
        this.isOn = isOn;
        this.isOn$.next(this.isOn);
    }

    /**
     * Activate SplitView system
     * @returns void
     */
    public activateSplitView(): void {
        this.onSplitPaneChanged(true);
    }

    /**
     * Deactivate SplitView system
     * @returns void
     */
    public deactivateSplitView(): void {
        this.onSplitPaneChanged(false);
    }

    /**
     * Remove the last SplitView
     * @returns void
     */
    public removeSplitView(): void {
        this.splitViewList.pop();
    }

    /**
     * Remove all SplitView except for the first
     * @returns void
     */
    public removeSplitViewToRoot(): void {
        if (this.splitViewList.length > 1) {
            this.splitViewList.pop();
            this.removeSplitViewToRoot();
        }
    }

    /**
     * Reset the SplitView list
     * @returns void
     */
    public resetSplitViews(): void {
        this.splitViewList = [];
    }

}
