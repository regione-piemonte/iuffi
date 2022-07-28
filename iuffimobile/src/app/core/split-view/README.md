## Supporto al multilayout smartphone e tablet tramite SplitViewModule

Il modulo SplitView raggruppa pagine e servizi che verranno utilizzati nel caso
in cui si voglia gestire dei layout differenti tra smartphone e tablet.

L'idea generale è quella di avere a disposizione una pagina che include una
`<ion-split-pane>` la quale ingloba due `<ion-nav>`, una `#masterNav` e una
`#detailNav`; se l'app sta girando su smartphone verrà utilizzata esclusivamente
la `#masterNav`, mentre su tablet sarà possibile utilizzarle entrambe, così da
fornire una esperienza utente più in linea con lo standard tablet.

Il modulo si compone di modelli, pagine e servizi:

-   models:
    -   `SplitView`
    -   `SplitViewConfig`
    -   `SplitViews`
-   pages
    -   `SplitViewPage` e `SplitViewPageParams`
    -   `SplitViewPlaceholderPage` e `SplitViewPlaceholderParams`
    -   `RootNavController`
-   services
    -   `SplitViewService`

### RootNavController

In Ionic5 non esiste più il concetto di `NavController`, ma nel template è stato
ricreato e nominato `RootNavController`: questo viene inizializzato direttamente
nell'`AppComponent` e permette allo sviluppatore di interagire con la
`<ion-nav>` padre di tutta l'app.

I metodi sono gli stessi già presenti per il "vecchio" `NavController`, ovvero:

-   setRoot
-   push
-   pop
-   popToRoot

### SplitViewPage

Cuore del modulo è ovviamente la pagina `SplitViewPage` che solitamente viene
"pushata" utilizzando il `RootNavController` e prende come dati di input una
istanza della classe `SplitViewPageParams`; quest'ultima classe definisce cosa
andare a visualizzare all'interno delle due porzioni di pagina. Per la
documentazione si rimanda al file `./pages/split-view/split-view.page.ts`.

### Inizializzazione

```typescript
this.rootNavCtrl.setRoot(
    SplitViewPage,
    new SplitViewPageParams({
        master: {
            page: SettingsPage
        },
        detail: {
            page: ChangeLanguagePage,
        }
    });
);
```

La porzione di codice andrà ad impostare nella `<ion-nav>` root dell'app una
`SplitViewPage` che in fase di caricamento (in particolare
all'`ngAfterViewInit`) creerà automaticamente una istanza della classe
`SplitView` attraverso il metodo `initSplitView` del servizio
`SplitViewService`, visualizzando così nella parte sinistra la pagina
`SettingsPage` e nella parte destra la pagina `ChangeLanguagePage`.

### Operazioni sulla SplitView

Quando si vuole effettuare una navigazione su una delle due `<ion-nav>` incluse
nella `SplitViewPage` occorre recuperare l'istanza della classe `SplitView` che
era stata inizializzata dallo `SplitViewService`. Per fare questo è possibile
richiamare il metodo `getMainSplitView` del servizio e a quel punto utilizzare
uno dei metodi che vengono messi a disposizione dalla classe `SplitView`:

-   `pushOnMaster`: Aggiunge una pagina allo stack di navigazione della
    `#masterNav`
-   `pushOnDetail`: Aggiunge una pagina allo stack di navigazione della
    `#detailNav`
-   `setRootOnDetail`: Imposta la pagina root dello stack di navigazione della
    `#detailNav`
-   `goToFirstDetailPage`: Effettua una navigazione all'indietro della
    `#detailNav` fino a visualizzare la prima pagina caricata
-   `back`: Effettua un back sullo stack della `#detailNav` o della `#masterNav`
    a seconda che la SplitView rispettivamente sia attiva o meno
-   `backToRoot`: Effettua un back alla prima pagina caricata nello stack della
    `#detailNav` o della `#masterNav` a seconda che la SplitView rispettivamente
    sia attiva o meno

```typescript
this.splitViewService.getMainSplitView().setRootOnDetail(ChangeLanguagePage);
```

### SplitView multiple

Nel caso si utilizzi un layout a tab è possibile che si vogliano utilizzare più
`SplitViewPage` contemporaneamente, magari una per tab.

Per poter fare questo occorre valorizzare l'attributo `index` dell'istanza della
classe `SplitViewPageParams`; tale indice andrà poi anche utilizzato per
recuperare l'istanza della classe `SplitView` attraverso il metodo
`getSplitView`, potendo così eseguire le operazioni di push, pop o back.

Dato che le SplitView all'interno dell'app saranno quasi sempre statiche, che
gli indici dovranno essere numerici e che potrebbero essere ripetuti in diversi
componenti e pagine dell'app è **vivamente consigliato** utilizzare
l'enumeration `AppSplitViews`, nel quale elencare tutte le SplitView che saranno
utilizzate nell'app

```typescript

export enum AppSplitViews {
    HOME,
    SETTINGS,
}

...

this.rootNavCtrl.setRoot(
    SplitViewPage,
    new SplitViewPageParams({
        index: AppSplitViews.SETTINGS,
        master: {
            page: SettingsPage
        },
        detail: {
            page: ChangeLanguagePage,
        },
    });
);

...

this.splitViewService.getSplitView(AppSplitViews.SETTINGS).setRootOnDetail(ChangeLanguagePage);

```

### Opzioni

In fase di creazione della `SplitViewPage` è possibile specificare una serie di
opzioni della pagina:

-   `showBack`: permette di visualizzare il tasto back in alto a sinistra della
    pagina. Questo back non lavorerà sulla `#masterNav` o sulla `#detailNav`
    bensì sul `RootNavController` permettendo all'app di tornare alla pagina
    precedente, "buttando" la SplitView appena creata.
