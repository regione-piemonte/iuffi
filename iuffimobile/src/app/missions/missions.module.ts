import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { SharedModule } from '@shared/shared.module';

import { DetailCardComponent } from './components/detail-card/detail-card.component';
import { DetectionHeaderComponent } from './components/detection-header/detection-header.component';
import { NewMissionForm } from './components/new-mission-form/new-mission-form.component';
import { InspectorsFilterPage } from './pages/inspectors-filter/inspectors-filter.page';
import { MissionDetailPage } from './pages/mission-detail/mission-detail.page';
import { NewMissionPage } from './pages/new-mission/new-mission.page';
import { MissionService } from './services/mission.service';

@NgModule({
    imports: [
        IonicModule,
        CommonModule,
        SharedModule,
        FormsModule,
        ReactiveFormsModule
    ],
    declarations: [
        NewMissionPage,
        MissionDetailPage,
        InspectorsFilterPage,
        NewMissionForm,
        DetailCardComponent,
        DetectionHeaderComponent
    ],
    providers: [
        MissionService
    ]
})
export class MissionsModule { }
