import { HttpParams } from '@angular/common/http';

export class ChangeUserLanguagePath {
    constructor(
        public path: {
            username: string;
        }
    ) { }

    public toJson(): Record<string, any> {
        return {
            username: this.path.username
        }
    }
}

export class ChangeUserLanguageBody {
    constructor(
        public body: {
            codLanguage: string;
        }
    ) { }

    public toHttpParams(): HttpParams {
        return new HttpParams()
            .set('codlanguage', this.body.codLanguage);
    }
}
