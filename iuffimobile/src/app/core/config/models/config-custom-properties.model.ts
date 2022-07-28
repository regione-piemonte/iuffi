
export class ConfigCustomProperties {
    public key: string;
    public languageFlagUrl: string;

    constructor(
        configCustomProperties: ConfigCustomProperties
    ) {
        this.key = configCustomProperties.key || '';
        this.languageFlagUrl = configCustomProperties.languageFlagUrl || 'assets/imgs/flags/{codLanguage}-round.png';
    }
}
