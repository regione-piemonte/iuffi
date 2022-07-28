import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { SharedModule } from '@shared/shared.module';

import { LoginPage } from './pages/login/login.page';

@NgModule({
    imports: [
        IonicModule,
        ReactiveFormsModule,
        SharedModule
    ],
    declarations: [
        LoginPage
    ]
})
export class LoginModule { }
