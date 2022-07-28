import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { SharedModule } from '@shared/shared.module';

import { MissionCard } from './components/mission-card/mission-card.component';
import { SearchBox } from './components/search-box/search-box.component';
import { HomePage } from './pages/home/home.page';
import { HomeService } from './services/home.service';

@NgModule({
    imports: [
        IonicModule,
        CommonModule,
        SharedModule,
        FormsModule,
        ReactiveFormsModule
    ],
    declarations: [
        HomePage,
        MissionCard,
        SearchBox
    ],
    providers: [
        HomeService
    ]
})
export class HomeModule { }
