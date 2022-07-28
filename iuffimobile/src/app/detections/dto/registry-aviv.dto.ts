export interface BusinessCenterDto {
	idUte: number;
	denomiUte?: any;
	comuneUte: string;
	indirizzoUte: string;
	numProgr: number;
}

export interface AvivRegistryDto {
	anagId: number;
	anagCuaa: string;
	anagPiva: string;
	anagDenom: string;
    anagFAcorrente: number;
    anagRuop: string;
    arrCA: BusinessCenterDto[];
}
