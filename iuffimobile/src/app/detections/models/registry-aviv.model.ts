import { AvivRegistryDto, BusinessCenterDto } from './../dto/registry-aviv.dto';

export class BusinessCenter {
    public idUte = 0;
    public denomiUte = '';
    public comuneUte = '';
    public indirizzoUte = '';
    public numProgr = 0;

    constructor(businessCenterDto?: BusinessCenterDto) {
        if (businessCenterDto) {
            this.idUte = businessCenterDto.idUte;
            this.denomiUte = businessCenterDto.denomiUte;
            this.comuneUte = businessCenterDto.comuneUte;
            this.indirizzoUte = businessCenterDto.indirizzoUte;
            this.numProgr = businessCenterDto.numProgr;
        }
    }
}

export class AvivRegistry {
    public anagId = 0;
    public anagCuaa = '';
    public anagPiva = '';
    public anagDenom = '';
    public anagFAcorrente = 0;
    public anagRuop = '';
    public arrCA: BusinessCenter[] = [];

    constructor(avivRegistryDto?: AvivRegistryDto) {
        if (avivRegistryDto) {
            this.anagId = avivRegistryDto.anagId;
            this.anagCuaa = avivRegistryDto.anagCuaa;
            this.anagPiva = avivRegistryDto.anagPiva;
            this.anagDenom = avivRegistryDto.anagDenom;
            this.anagFAcorrente = avivRegistryDto.anagFAcorrente;
            this.anagRuop = avivRegistryDto.anagRuop;
            this.arrCA = avivRegistryDto.arrCA ? avivRegistryDto.arrCA.map(t => new BusinessCenter(t)) : [];
        }
    }
}
