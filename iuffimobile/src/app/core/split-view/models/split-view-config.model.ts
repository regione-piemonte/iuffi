import { IonNav } from '@ionic/angular';

export interface SplitViewConfig {
    nav: IonNav;
    page: any;
    params?: {
        [key: string]: any;
    };
}
