import { Component } from '@angular/core';
import { NavParams } from '@ionic/angular';

import { SplitViewPlaceholderPageParams } from './split-view-placeholder.params';

@Component({
    selector: 'split-view-placeholder-page',
    templateUrl: './split-view-placeholder.page.html',
    styleUrls: ['./split-view-placeholder.page.scss'],
})
export class SplitViewPlaceholderPage {
    public title: string;
    public subtitle: string;
    public showMenu: boolean;

    constructor(
        private navParams: NavParams
    ) {
        const params = this.navParams.data as SplitViewPlaceholderPageParams;
        this.title = params.title;
        this.subtitle = params.subtitle;
        this.showMenu = params.showMenu || false;
    }

}
