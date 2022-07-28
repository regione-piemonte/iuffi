import { Injector } from '@angular/core';
import { TranslateLoader } from '@ngx-translate/core';
import { Observable, Observer } from 'rxjs';

import { I18nBaseService } from '../services/i18n-base.service';
import { Language } from './language.model';

export class CustomTranslateHttpLoader implements TranslateLoader {

    constructor(
        private injector: Injector
    ) { }

    public getTranslation(lang: string): Observable<any> {
        return Observable.create((observer: Observer<any>) => {
            this.injector.get(I18nBaseService).downloadLang({ code: lang }).then(
                (res: Language) => {
                    observer.next(res.translations);
                    observer.complete();
                },
                () => {
                    observer.next({});
                    observer.complete();
                }
            )
        });
    }
}
