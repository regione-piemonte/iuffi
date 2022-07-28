import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { SharedModule } from '@shared/shared.module';

import { SplitViewPlaceholderPage } from './pages/placeholder/split-view-placeholder.page';
import { SplitViewPage } from './pages/split-view/split-view.page';
import { RootNavController } from './services/root-nav.controller';
import { SplitViewService } from './services/split-view.service';

@NgModule({
    declarations: [
        SplitViewPlaceholderPage,
        SplitViewPage
    ],
    imports: [
        IonicModule,
        CommonModule,
        SharedModule
    ],
    providers: [
        SplitViewService,
        RootNavController
    ],
    exports: [
        SplitViewPlaceholderPage,
        SplitViewPage
    ]
})
export class SplitViewModule { }
