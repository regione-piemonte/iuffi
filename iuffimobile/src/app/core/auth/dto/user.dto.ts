/* eslint-disable camelcase */

export interface UserDto {
    codlanguage: string;
    email: string;
    firstname: string;
    languages: UserLanguageDto[];
    lastname: string;
    loginname: string;
    token: string;
}

export interface UserLanguageDto {
    CODLANGUAGE: string;
    ISOCODE: string;
    NAME: string;
    NAMELANGUAGE: string;
    ORACLE_LANGUAGE: string;
    PREFERENCEORDER?: string;
}
