import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { SharedModule } from '@shared/shared.module';

import {
    HarmfulOrganismSelectionComponent,
} from './components/harmful-organism-selection/harmful-organism-selection.component';
import { LeafletMapComponent } from './components/leaflet-map/leaflet-map.component';
import { PlantSpeciesSelectionComponent } from './components/plant-species-selection/plant-species-selection.component';
import { SampleTypeSelectionComponent } from './components/sample-type-selection/sample-type-selection.component';
import { SingleInspectionCardComponent } from './components/single-inspection-card/single-inspection-card.component';
import { TrapMapItemSelectionComponent } from './components/trap-map-item-selection/trap-map-item-selection.component';
import { TrapTypeSelectionComponent } from './components/trap-type-selection/trap-type-selection.component';
import { AreaTypeModalPage } from './pages/area-type-modal/area-type-modal.page';
import { AvivModalPage } from './pages/aviv-modal/aviv-modal.page';
import { AvivPlantSpeciesModalPage } from './pages/aviv-plant-species-modal/aviv-plant-species-modal.page';
import { AvivServiceModalPage } from './pages/aviv-service-modal/aviv-service-modal.page';
import { DetectionPage } from './pages/detection/detection.page';
import { HarmfulOrganismModalPage } from './pages/harmful-organism-modal/harmful-organism-modal.page';
import { MapModalPage } from './pages/map-modal/map-modal.page';
import { PlantSpeciesModalPage } from './pages/plant-species-modal/plant-species-modal.page';
import { SampleConfirmPage } from './pages/sample-confirm/sample-confirm.page';
import { SampleTypeModalPage } from './pages/sample-type-modal/sample-type-modal.page';
import { SamplePage } from './pages/sample/sample.page';
import { TrapBottomModalPage } from './pages/trap-bottom-modal/trap-bottom-modal.page';
import { TrapInstallationConfirmPage } from './pages/trap-installation-confirm/trap-installation-confirm.page';
import { TrapInstallationPage } from './pages/trap-installation/trap-installation.page';
import { TrapMaintenanceRemovePage } from './pages/trap-maintenance-remove/trap-maintenance-remove.page';
import { TrapOpSelectionPage } from './pages/trap-op-selection/trap-op-selection.page';
import { TrapSearchPage } from './pages/trap-search/trap-search.page';
import { TrapTypeModalPage } from './pages/trap-type-modal/trap-type-modal.page';
import { TrapsMapPage } from './pages/traps-map/traps-map.page';
import { VisualInspectionConfirmPage } from './pages/visual-inspection-confirm/visual-inspection-confirm.page';
import { VisualInspectionPage } from './pages/visual-inspection/visual-inspection.page';
import { DetectionService } from './services/detection.service';
import { SampleService } from './services/sample.service';
import { TrapService } from './services/trap.service';
import { VisualInspectionService } from './services/visual-inspection.service';

@NgModule({
    imports: [
        IonicModule,
        CommonModule,
        SharedModule,
        FormsModule,
        ReactiveFormsModule
    ],
    declarations: [
        DetectionPage,
        AreaTypeModalPage,
        VisualInspectionPage,
        VisualInspectionConfirmPage,
        PlantSpeciesModalPage,
        HarmfulOrganismModalPage,
        SingleInspectionCardComponent,
        AvivServiceModalPage,
        AvivPlantSpeciesModalPage,
        SamplePage,
        PlantSpeciesSelectionComponent,
        HarmfulOrganismSelectionComponent,
        SampleTypeSelectionComponent,
        SampleTypeModalPage,
        SampleConfirmPage,
        TrapOpSelectionPage,
        TrapInstallationPage,
        TrapTypeModalPage,
        TrapTypeSelectionComponent,
        TrapInstallationConfirmPage,
        TrapsMapPage,
        TrapBottomModalPage,
        LeafletMapComponent,
        TrapSearchPage,
        TrapMaintenanceRemovePage,
        TrapMapItemSelectionComponent,
        MapModalPage,
        AvivModalPage
    ],
    providers: [
        DetectionService,
        VisualInspectionService,
        SampleService,
        TrapService
    ]
})
export class DetectionsModule { }
