import { SplitViewPlaceholderPage } from '../placeholder/split-view-placeholder.page';

export class SplitViewPageParams {
    /**
     * Indice numerico della SplitView all'interno dell'app
     */
    public index?: number;

    /**
     * Configurazione della pagina principale (destra) della SplitView
     *
     * Questa configurazione è obbligatoria e richiede che venga specificata una pagina da caricare,
     * e, in caso occorrano, tutti i parametri che verranno recuperati dalla pagina stessa tramimte l'oggetto `NavParams`
     */
    public master: {
        page: any;
        params?: {
            [key: string]: any;
        };
    };

    /**
     * Configurazione della pagina di dettaglio (sinistra) della SplitView
     *
     * Questa configurazione è facoltativa, ma se valorizzata richiede che venga specificata una pagina da caricare,
     * e, in caso occorrano, tutti i parametri che verranno recuperati dalla pagina stessa tramimte l'oggetto `NavParams`
     *
     * Nel caso in cui non venga valorizzata verrà caricata la pagina di default SplitViewPlaceholderPage
     */
    public detail?: {
        page: any;
        params?: {
            [key: string]: any;
        };
    };

    /**
     * Opzioni di configurazione della SplitView
     */
    public options?: {
        /**
         * Flag per visualizzare il tasto back in alto sulla SplitViewPage
         */
        showBack?: boolean;
    };

    constructor(
        params: SplitViewPageParams
    ) {
        this.index = params.index;
        this.master = params.master;
        this.detail = params.detail || { page: SplitViewPlaceholderPage };
        this.options = params.options || { showBack: false };
    }
}
