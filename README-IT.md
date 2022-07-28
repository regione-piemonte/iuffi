# Project Title
Analisi chimica di campioni biologici

# Project Description
Il sistema gestisce le richieste di analisi chimiche presentate dal cittadino o da un tecnico delegato ai laboratori regionali di analisi, consentendo a questi ultimi la registrazione dei risultati delle analisi. Il sistema è predisposto per il pagamento delle richieste presentate con il sistema di pagamento della pubblica amministrazione PagoPA tramite un altro progetto del CSI Piemonte, che potrà essere fornito su richiesta.

# Getting Started
Il prodotto IUFFI è composto dalle seguenti componenti:
- [IUFFIWEB](https://github.com/regione-piemonte/iuffi/iuffiweb) (applicazione web di front office e back end dell'app per dispositivi mobili)
- [IUFFIDB](https://github.com/regione-piemonte/iuffi/iuffidb) (script per la creazione ed il mantenimento del DB proprietario)
- [IUFFFIAUTH](https://github.com/regione-piemonte/iuffi/iuffiauth) (applicazione web per l'autenticazione con Shibboleth dell'app per dispositivi mobili)
- [IUFFIMOBILE](https://github.com/regione-piemonte/iuffi/iuffimobule) (progetto Ionic per la generazione del codice dell'app per dispositivi mobili)

# Prerequisites
I prerequisiti per l'installazione delle componenti sono i seguenti:
## Software
- [JDK 8](https://www.apache.org)
- [Apache 2.4](https://www.apache.org)
- [RedHat JBoss 6.4 GA](https://developers.redhat.com)  
- [Oracle Database ](https://www.oracle.com)  

- Tutte le librerie elencate nel file BOM.csv devono essere accessibili per compilare il progetto. Le librerie sono pubblicate nel Repository degli Artefatti del CSI, ma per semplicità sono state incluse nella cartella lib/ di ogni singola componente, ad esclusione della libreria weblogic-client-3.1.0.jar, che è utilizzata dalle componenti AGRCFO e AGRCBO per invocare servizi esterni con protocollo t3 e che deve essere scaricata autonomamente dal sito di Oracle.

# Versioning
Per la gestione del codice sorgente viene utilizzato Git. Per la gestione del versioning si fa riferimento alla metodologia [Semantic Versioning](https://semver.org) 

# Copyrights
(C) Copyright 2020 Regione Piemonte

# License
Questo software è distribuito con licenza EUPL-1.2.
Consultare i file EUPL v1_2 IT-LICENSE.txt e EUPL v1_2 EN-LICENSE.txt per maggiori dettagli.
