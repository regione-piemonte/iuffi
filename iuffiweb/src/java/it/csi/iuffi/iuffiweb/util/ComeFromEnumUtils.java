package it.csi.iuffi.iuffiweb.util;

public enum ComeFromEnumUtils {
    RICERCA("RICERCA"), 
    RICERCA_PROFESSIONISTA("RICERCA_PROFESSIONISTA"), 
    IACS("IACS"),
    VISUALIZZA_ESTRAZIONI("VISUALIZZA_ESTRAZIONI"), 
    GESTIONE_ESTRAZIONI_SIMULAZIONE("GESTIONE_ESTRAZIONI_SIMULAZIONE"),
    GESTIONE_ESTRAZIONI_DETTAGLIO("GESTIONE_ESTRAZIONI_DETTAGLIO"), 
    REGISTRO_FATTURE("REGISTRO_FATTURE");

    private final String nome;

    ComeFromEnumUtils(String nome) {
	this.nome = nome;
    }

    public String getNome() {
	return this.nome;
    }

    @Override
    public String toString() {
	return this.nome.toString();
    }
}
