export interface Error {
	errore: string;
}

export interface ApiErrorDto {
	message: string;
	errors: Error[];
}
