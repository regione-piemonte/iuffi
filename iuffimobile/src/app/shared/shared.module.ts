import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { IonicModule } from '@ionic/angular';
import { TranslateModule } from '@ngx-translate/core';
import { PhotoGalleryComponent } from '@shared/components/photo-gallery/photo-gallery.component';

import { AppErrorMessage, ErrorValidatorService } from './app-error-validator';
import { AppItemButtonComponent } from './components/app-item-button/app-item-button.component';
import { AppSpinner } from './components/app-spinner/app-spinner';
import {
    DetectionCardContentComponent,
} from './components/detection-card/detection-card-content/detection-card-content.component';
import {
    SamplingContentComponent,
} from './components/detection-card/detection-card-content/sampling-content/sampling-content.component';
import {
    TrappingContentComponent,
} from './components/detection-card/detection-card-content/trapping-content/trapping-content.component';
import {
    VisualInspectionContentComponent,
} from './components/detection-card/detection-card-content/visual-inspection-content/visual-inspection-content.component';
import {
    DetectionCardHeaderComponent,
} from './components/detection-card/detection-card-header/detection-card-header.component';
import { DetectionCardComponent } from './components/detection-card/detection-card.component';
import {
    SingleInspectionCardDetailComponent,
} from './components/detection-card/single-inspection-card-detail/single-inspection-card-detail.component';
import { IuffiAnimatedCardComponent } from './components/iuffi-animated-card/iuffi-animated-card';
import {
    IuffiAnimatedCardIndicator,
} from './components/iuffi-animated-card/iuffi-animated-card-indicator/iuffi-animated-card-indicator.component';
import { IuffiButtonComponent } from './components/iuffi-button/iuffi-button.component';
import { IuffiFabComponent } from './components/iuffi-fab/iuffi-fab.component';
import { IuffiFooterButtonComponent } from './components/iuffi-footer-button/iuffi-footer-button.component';
import { ModalNavPage } from './components/modal-nav';
import { DirectivesModule } from './directives/directives.module';
import { FileManagerService } from './file-manager/services/file-manager.service';
import { PDFService } from './file-manager/services/pdf.service';
import { GeoUtilsService } from './geo-utils/geo-utils.service';
import { OfflineService } from './offline/services/offline.service';
import { PipesModule } from './pipes/pipes.module';

@NgModule({
    declarations: [
        IuffiButtonComponent,
        IuffiFooterButtonComponent,
        IuffiFabComponent,
        AppItemButtonComponent,
        AppSpinner,
        AppErrorMessage,
        IuffiAnimatedCardComponent,
        IuffiAnimatedCardIndicator,
        DetectionCardComponent,
        DetectionCardHeaderComponent,
        DetectionCardContentComponent,
        VisualInspectionContentComponent,
        TrappingContentComponent,
        SamplingContentComponent,
        ModalNavPage,
        SingleInspectionCardDetailComponent,
        PhotoGalleryComponent
    ],
    imports: [
        BrowserModule,
        CommonModule,
        IonicModule,
        CommonModule,
        TranslateModule,
        FormsModule,
        ReactiveFormsModule,
    ],
    exports: [
        TranslateModule,
        PipesModule,
        DirectivesModule,
        IuffiButtonComponent,
        IuffiFooterButtonComponent,
        IuffiFabComponent,
        AppItemButtonComponent,
        AppSpinner,
        AppErrorMessage,
        IuffiAnimatedCardComponent,
        IuffiAnimatedCardIndicator,
        DetectionCardComponent,
        DetectionCardHeaderComponent,
        DetectionCardContentComponent,
        VisualInspectionContentComponent,
        TrappingContentComponent,
        SamplingContentComponent,
        ModalNavPage,
        SingleInspectionCardDetailComponent,
        PhotoGalleryComponent
    ],
    providers: [
        ErrorValidatorService,
        OfflineService,
        GeoUtilsService,
        PDFService,
        FileManagerService
    ]
})
export class SharedModule { }
