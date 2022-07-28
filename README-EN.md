# Project Title
Analisi chimica di campioni biologici - Chemical analysis of biological samples

# Project Description
The system handles the requests for chemical analysis submitted by the citizen or a technical delegate to the regional analysis laboratories, allowing the laboratories to record the analisys results. The system is set up for the payment of the submitted requests with the payment system of the public administration PagoPA through another project of CSI Piemonte, which can be provided on request.

# Getting Started
The IUFFI product is composed of the following components:
- [IUFFIWEB](https://github.com/regione-piemonte/iuffi/iuffiweb) (front office web application and mobile app back end)
- [IUFFIDB](https://github.com/regione-piemonte/iuffi/iuffidb) (script for creating and maintaining the proprietary DB)
- [IUFFFIAUTH](https://github.com/regione-piemonte/iuffi/iuffiauth) (web application for Shibboleth authentication of the mobile app)
- [IUFFIMOBILE](https://github.com/regione-piemonte/iuffi/iuffimobule) (Ionic project for mobile app code generation)

# Prerequisites
The prerequisites for installing the components are as follows:
## Software
- [JDK 8](https://www.apache.org)
- [Apache 2.4](https://www.apache.org)
- [RedHat JBoss 6.4 GA](https://developers.redhat.com)  
- [Oracle 11g](https://www.oracle.com)  

- All libraries listed in the BOM.csv file must be accessible to build the project. The libraries are published in the CSI's Artifact Repository , but for simplicity they have been included in the /lib of each individual component, with the exception of the weblogic-client-3.1.0.jar library, which is used by the components AGRCFO and AGRCBO must be downloaded independently from the Oracle website.

# Versioning
Git is used for managing the source code. For versioning management, reference is made to the [Semantic Versioning](https://semver.org) methodology.

# Copyrights
(C) Copyright 2021 Regione Piemonte

# License
This software is distributed under the EUPL-1.2 license.
See the files EUPL v1_2 IT-LICENSE.txt and EUPL v1_2 EN-LICENSE.txt for more details.
