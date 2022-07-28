import { AfterViewInit, Component, ViewChild } from '@angular/core';
import { RootNavController } from '@core/split-view/services/root-nav.controller';
import { SplitViewService } from '@core/split-view/services/split-view.service';
import { AutoUnsubscribe } from '@core/utils';
import { IonNav, NavParams } from '@ionic/angular';
import { takeUntil } from 'rxjs/operators';

import { SplitViewPageParams } from './split-view.params';

@Component({
    selector: 'split-view.page',
    templateUrl: './split-view.page.html',
    styleUrls: ['./split-view.page.scss']
})
export class SplitViewPage extends AutoUnsubscribe implements AfterViewInit {
    @ViewChild('masterNav', { read: IonNav }) public masterNav!: IonNav;
    @ViewChild('detailNav', { read: IonNav }) public detailNav!: IonNav;
    public splitViewIsActive = false;
    public showBack = false;

    private _params: SplitViewPageParams;

    constructor(
        public navParams: NavParams,
        public rootNavCtrl: RootNavController,
        public splitViewService: SplitViewService
    ) {
        super();
        this._params = this.navParams.data as SplitViewPageParams;
        this.showBack = this._params.options?.showBack || false;

        this.splitViewService.isOn$
            .pipe(takeUntil(this.destroy$))
            .subscribe(isOn => this.splitViewIsActive = isOn);
    }

    ngAfterViewInit(): void {
        this.splitViewService.initSplitView(
            {
                nav: this.masterNav,
                page: this._params.master.page,
                params: this._params.master.params
            },
            {
                nav: this.detailNav,
                page: this._params.detail?.page,
                params: this._params.detail?.params
            },
            this._params.index
        );
    }

    public back(): void {
        this.rootNavCtrl.pop();
    }
}
