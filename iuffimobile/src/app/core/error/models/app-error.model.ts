export class AppError {
    public message: string;
    public status?: string;
    public code?: number;

    constructor(
        message: string,
        extra: {
            status?: string;
            code?: number;
        } = {}
    ) {
        this.message = message;
        this.status = extra.status;
        this.code = extra.code;
    }
}
