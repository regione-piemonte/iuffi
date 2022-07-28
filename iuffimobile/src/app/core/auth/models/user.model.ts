import { UserDto } from '../dto/user.dto';
import { JwtDto } from './../dto/jwt.dto';
import { UserLanguage } from './user-language.model';

export class User {
    public codLanguage = '';
    public email = '';
    public fiscalCode = '';
    public firstName = '';
    public languages: UserLanguage[] = [];
    public lastName = '';
    public loginName = '';

    public static fromUserDto(userDto: UserDto): User {
        return new User({
            loginName: userDto.loginname || '',
            firstName: userDto.firstname || '',
            lastName: userDto.lastname || '',
            email: userDto.email || '',
            languages: userDto.languages ? userDto.languages.map(l => UserLanguage.fromLanguageDto(l)) : [],
            codLanguage: userDto.codlanguage || '1',
        });
    }

    public static fromJwtDto(jwt: JwtDto): User {
        return new User({
            loginName: '',
            firstName: jwt.nome || '',
            lastName: jwt.cognome || '',
            fiscalCode: jwt.sub || '',
            email: '',
            languages: [],
            codLanguage: '1',
        });
    }

    constructor(user: Partial<User>) {
        Object.assign(this, user);
    }

    public isLogged(): boolean {
        return this.loginName !== undefined;
    }

    public getFullName(): string {
        return `${this.firstName} ${this.lastName}`;
    }
}
