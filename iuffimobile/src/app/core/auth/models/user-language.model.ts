import { UserLanguageDto } from '../dto/user.dto';

export class UserLanguage {
    public codLanguage: string;
    public isoCode: string;
    public icon?: string;
    public name: string;
    public nameLanguage: string;
    public oracleLanguage: string;
    public preferenceOrder?: string;

    constructor(
        l: UserLanguage
    ) {
        this.codLanguage = l.codLanguage;
        this.isoCode = l.isoCode;
        this.icon = l.icon;
        this.name = l.name;
        this.nameLanguage = l.nameLanguage;
        this.oracleLanguage = l.oracleLanguage;
        this.preferenceOrder = l.preferenceOrder;
    }

    public static fromLanguageDto(languageDto: UserLanguageDto): UserLanguage {
        return new UserLanguage({
            codLanguage: languageDto.CODLANGUAGE,
            isoCode: languageDto.ISOCODE,
            name: languageDto.NAME,
            nameLanguage: languageDto.NAMELANGUAGE,
            oracleLanguage: languageDto.ORACLE_LANGUAGE,
            preferenceOrder: languageDto.PREFERENCEORDER
        });
    }
}
