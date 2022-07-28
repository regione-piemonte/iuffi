### Gestione icone con Icomoon

Le icone vanno sempre gestite tramite progetto Icomoon, ma a differenza di Ionic
3 dove venivano usate attraverso font, Ionic 5 le gestisce via SVG. Pertanto,
una volta aggiunte le icone (e aggiunto al nome il prefisso dell'app, ad esempio
'app-') sul progetto Icomoon, andrà eseguito un export in SVG e successivamente
copiate le icone nella cartella `src/assets/icons`.

La sintassi di utilizzo rimane la stessa:

```
<ion-icon name="app-menu"></ion-icon>
```

Mentre il blocco

```
{
    "glob": "**/*.svg",
    "input": "src/assets/icons",
    "output": "./svg"
}
```

nell'`angular.json` si occuperà di risolvere il path dell'svg dell'icona
richiesta.
