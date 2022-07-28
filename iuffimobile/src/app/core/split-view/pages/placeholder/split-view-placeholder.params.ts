
export class SplitViewPlaceholderPageParams {
    public title: string;
    public subtitle: string;
    public showMenu?= false;

    constructor(
        params: SplitViewPlaceholderPageParams
    ) {
        this.title = params.title;
        this.subtitle = params.subtitle;
        this.showMenu = params.showMenu;
    }
}
