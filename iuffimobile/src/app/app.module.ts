import { HttpBackend, HttpXhrBackend } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouteReuseStrategy } from '@angular/router';
import { BackgroundGeolocation } from '@ionic-native/background-geolocation/ngx';
import { BackgroundMode } from '@ionic-native/background-mode/ngx';
import { Camera } from '@ionic-native/camera/ngx';
import { FileOpener } from '@ionic-native/file-opener/ngx';
import { FilePath } from '@ionic-native/file-path/ngx';
import { File } from '@ionic-native/file/ngx';
import { Geolocation } from '@ionic-native/geolocation/ngx';
import { ImagePicker } from '@ionic-native/image-picker/ngx';
import { InAppBrowser } from '@ionic-native/in-app-browser/ngx';
import { WebView } from '@ionic-native/ionic-webview/ngx';
import { LocationAccuracy } from '@ionic-native/location-accuracy/ngx';
import { SpeechRecognition } from '@ionic-native/speech-recognition/ngx';
import { IonicModule, IonicRouteStrategy, Platform } from '@ionic/angular';
import { IonicStorageModule } from '@ionic/storage';
import { NativeHttpBackend, NativeHttpFallback, NativeHttpModule } from 'ionic-native-http-connection-backend';
import { ionicConfig } from '../ionic.config';
import { AppRoutingModule } from './app-routing.module';
import { CoreModule } from './core/core.module';
import { DetectionsModule } from './detections/detections.module';
import { HomeModule } from './home/home.module';
import { LoginModule } from './login/login.module';
import { AppComponent } from './main/components/app/app.component';
import { SideMenuComponent } from './main/components/side-menu/side-menu.component';
import { StarterPage } from './main/pages/starter/starter.page';
import { MissionsModule } from './missions/missions.module';
import { SharedModule } from './shared/shared.module';

@NgModule({
    declarations: [
        AppComponent,
        StarterPage,
        SideMenuComponent
    ],
    entryComponents: [],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        IonicModule.forRoot(ionicConfig),
        IonicStorageModule.forRoot(),
        AppRoutingModule,
        CoreModule,
        SharedModule,
        NativeHttpModule,
        LoginModule,
        HomeModule,
        MissionsModule,
        DetectionsModule
    ],
    providers: [
        InAppBrowser,
        SpeechRecognition,
        Geolocation,
        LocationAccuracy,
        BackgroundMode,
        BackgroundGeolocation,
        Camera,
        FileOpener,
        File,
        FilePath,
        WebView,
        ImagePicker,
        { provide: RouteReuseStrategy, useClass: IonicRouteStrategy },
        { provide: HttpBackend, useClass: NativeHttpFallback, deps: [Platform, NativeHttpBackend, HttpXhrBackend] },
        { provide: RouteReuseStrategy, useClass: IonicRouteStrategy }
    ],
    bootstrap: [AppComponent]
})
export class AppModule { }
