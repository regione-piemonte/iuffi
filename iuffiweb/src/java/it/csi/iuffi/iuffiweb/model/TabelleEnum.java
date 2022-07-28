package it.csi.iuffi.iuffiweb.model;

public enum TabelleEnum
{

  IUF_T_PREVISIONE_ON_EST("Previsione attività di monitoraggio annuale"),
  IUF_T_PREVISIONE_ON_REG("Previsione attività di monitoraggio annuale"),
  IUF_D_TRAPPOLA_ON("Associazione Trappola Organismo Nocivo"),
  IUF_R_CAMPIONAMENTO_SPEC_ON("Ispezione visiva"),
  IUF_R_SPECIE_ON_PERIODO("Compatibilità specie/campione/periodo/organismo nocivo"),
  IUF_T_TRAPPOLAGGIO("Trappole"),
  IUF_T_CAMPIONAMENTO("Campioni"),
  IUF_T_ISPEZIONE_VISIVA("Ispezione visiva"),
  IUF_T_ISP_VISIVA_PIANTA("Ispezione visiva"),
  IUF_T_RILEVAZIONE("Rilevazione"),
  IUF_T_TRAPPOLA("Trappole"),
  IUF_T_ESITO_CAMPIONE("Esito campioni"),
  IUF_R_ISPETTORE_AGGIUNTO("Ispettore aggiunto"),
  IUF_T_MISSIONE("Missione"),
  IUF_T_SOPRALLUOGO_VIVAIO("Sopralluogo vivaio"),
  IUF_T_VERBALE("Verbale");
  
  private String descrizione;

  TabelleEnum(String descrizione) {
    this.descrizione = descrizione;
  }

  public String getDescrizione() {
    return descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public String getName() {
    return this.name();
  }

}
