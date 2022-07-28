export interface JwtDto {
    jti: string;
    iat: number;
    exp: number;
    iss: string;
    aud: string;
    sub: string;
    nome: string;
    cognome: string;
}
