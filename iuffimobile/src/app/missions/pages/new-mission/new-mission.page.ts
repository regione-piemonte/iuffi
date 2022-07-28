import { Component, ViewChild } from '@angular/core';
import { RootNavController } from '@app/core/split-view/services/root-nav.controller';
import { HomePage } from '@app/home/pages/home/home.page';
import { NewMissionForm } from '@app/missions/components/new-mission-form/new-mission-form.component';
import { MissionDto } from '@app/missions/dto/mission.dto';
import { Mission } from '@app/missions/models/mission.model';
import { MissionService } from '@app/missions/services/mission.service';
import { DeviceService } from '@core/device/services/device.service';
import { NavParams } from '@ionic/angular';
import { MissionDetailPage } from '../mission-detail/mission-detail.page';


@Component({
    selector: 'new-mission',
    templateUrl: 'new-mission.page.html',
    styleUrls: ['new-mission.page.scss']
})
export class NewMissionPage {
    public mission: Mission;
    public action = 'CREATE';
    @ViewChild(NewMissionForm) public newMissionForm!: NewMissionForm
    constructor(
        private rootNavController: RootNavController,
        private navParams: NavParams,
        private deviceService: DeviceService,
        private missionService: MissionService
    ) {
        this.mission = this.navParams.get('mission');
        this.action = this.navParams.get('action') || 'CREATE';
    }

    public createMission(): void {
        const missionDto: MissionDto | null = this.newMissionForm.valid();
        if (missionDto) {
            this.missionService.insertMission(missionDto).then(
                (newMission: MissionDto | Mission) => {
                    this.deviceService.alert('MISSION_CREATION_SUCCESS', {
                        handler: () => {
                            if (newMission) {
                                this.rootNavController.setRoot(MissionDetailPage, { missionId: newMission.idMissione }, {
                                    animated: true,
                                    direction: 'forward'
                                });
                            }
                            else {
                                this.rootNavController.setRoot(MissionDetailPage, { missionId: newMission.idMissione, missionDate: newMission.dataMissione }, {
                                    animated: true,
                                    direction: 'forward'
                                });
                                // this.rootNavController.push(HomePage);
                            }
                        }
                    })

                },
                (err: any) => {
                    if (err.status == '409') {
                        this.deviceService.alert('L\'ispettore affiancato ha già una missione assegnata', {
                            handler: () => {
                                this.rootNavController.push(HomePage);
                            }
                        })
                    } else {
                        this.deviceService.alert('MISSION_CREATION_ERROR', {
                            handler: () => {
                                this.rootNavController.push(HomePage);
                            }
                        })
                    }

                }
            );

        }
    }

    public updateMission(): void {
        const missionDto: MissionDto | null = this.newMissionForm.valid();
        if (missionDto) {
            // if (missionDto.ispettoriAggiunti) {
            //     missionDto.ispettoriAggiunti.forEach(element => {
            //         element.selected = null;
            //     });
            // }
            this.missionService.updateMission(missionDto).then(
                () => {
                    this.deviceService.alert('MISSION_UPDATE_SUCCESS', {
                        handler: () => {
                            //this.rootNavController.push(HomePage);
                            this.rootNavController.setRoot(MissionDetailPage, { missionId: missionDto.idMissione }, {
                                animated: true,
                                direction: 'forward'
                            });
                        }
                    })
                },
                (err: any) => {
                    if (err.status == '409') {
                        this.deviceService.alert('L\'ispettore affiancato ha già una missione assegnata', {
                            handler: () => {
                                this.rootNavController.push(HomePage);
                            }
                        })
                    } else {
                        this.deviceService.alert('MISSION_UPDATE_ERROR', {
                            handler: () => {
                                this.rootNavController.push(HomePage);
                            }
                        })
                    }
                }
            );

        }
    }
}
