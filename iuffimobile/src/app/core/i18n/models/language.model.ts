
export class Language {
    public code: string;
    public label?: string;
    public url?: string;
    public isDefault?: boolean;
    public lastModified?: string;
    public translations?: Record<string, any>;

    constructor(
        language: Language
    ) {
        this.code = language.code;
        this.label = language.label;
        this.url = language.url;
        this.isDefault = language.isDefault || false;
        this.lastModified = language.lastModified;
        this.translations = language.translations;
    }
}
