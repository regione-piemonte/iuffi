package it.csi.iuffi.iuffiweb.manager;

import java.util.Date;

public class Formule
{

  public Formule()
  {
    super();
    // TODO Auto-generated constructor stub
  }

  /**
   * Calcolato: se le ore lavorate sono + di 5:30 allora viene scalata una mezz'ora   
   * Formula: =SE(((G2-F2)-INT(G2-F2))*24>5,5;TESTO(G2-F2-ORARIO.VALORE("00.30.00");"HH:mm");TESTO(G2-F2;"HH:mm"))
   * @return
   */
  public static Double oreTotaliSiti(Date oraInizio,Date oraFine) {
  //in milliseconds
    if(oraFine!=null) {
      long diff = oraFine.getTime() - oraInizio.getTime();
    //pippo
        //Long diffSeconds = diff / 1000 % 60;
        Long diffMinutes = diff / (60 * 1000) % 60;
        Long diffHours = diff / (60 * 60 * 1000) % 24;
        //Long diffDays = diff / (24 * 60 * 60 * 1000);

        Double totale = new Double(diffHours.doubleValue()+(diffMinutes.doubleValue()/60)); //+(diffSeconds.doubleValue()/3600));
        
        if(totale>5.5) {
          //totale=totale-(new Double(30).doubleValue()/60);
          totale=totale-0.5;
        }
        return totale;
    }else {
      return Double.valueOf(0);
    }
  }
  
  /**
   * Calcolato (*) Messo una condizione nel caso venisse la divisione per 0 
   * Formula: =((ORE_TOTALI SITI-INT(IORE_TOTALI SITI))*24)/peso verbale - > =SE(J2>0;((H2-INT(H2))*24)/J2;0)
   * @return
   */
  public static Double oreOperazione() {
    return null; 
 }  
  
  /**
   * Somma azioni descritte in tutte le righe del verbale Calcolato (*) 
   * Formula: SOMMA.SE(colonna N_VERBALE;N_VERBALE;peso riga) -> =SOMMA.SE($D:$D;D2;$L:$L)
   * @return
   */
  public static Double pesoVerbale() {
    return null; 
 }  

  /**
   * Azioni descritte nella riga (organismi nocivi+trappole posate/prese/ricaricate + presi campioni) Calcolato (*) 
   * Formula:  SOMMA(SE(AZ2>0;(SE(R2>0;SE(R2>CERCA.VERT(Q2;$TIPOLOGY.$A$2:$TIPOLOGY.$E$18;5;FALSO())*0,0001;
   * LOG(R2*10000;CERCA.VERT(Q2;$TIPOLOGY.$A$2:$TIPOLOGY.$E$18;5;FALSO()));1);SE(S2>CERCA.VERT(Q2;$TIPOLOGY.$A$2:$TIPOLOGY.$E$18;5;FALSO());
   * LOG(S2;CERCA.VERT(Q2;$TIPOLOGY.$A$3:$TIPOLOGY.$E$18;5;FALSO()));1)));0);CONTA.SE(BA2;">0");BI2)
   * @return
   */
  public static Double pesoRiga(Integer numOn,Integer numPiante,Double ha,Integer numCampioni, Integer numTrappole, Integer velocita) {
    Double pesoRiga =new Double(0);
    ha=ha/10000;
    if(numOn>0) {
      //uso la superfice
        if(ha>0) {//20 valore da prendere da una tabella
          if(ha>(velocita*0.0001)) {
            pesoRiga+= Math.log(ha*10000)/Math.log(velocita);
          }else {
            pesoRiga+= Double.valueOf(1);
          }  
        //uso le piante
        } else {
          if(numPiante>velocita) {       
            pesoRiga+= Math.log(numPiante)/Math.log(velocita);
          }  else {
            pesoRiga+= Double.valueOf(1);
          }
        }        
    }else {
      pesoRiga+= Double.valueOf(0);
    }
    
    if(numCampioni>0)pesoRiga+=1;
    if(numTrappole>0)pesoRiga+=numTrappole;
    return pesoRiga; 
 }  

  /**
   * Numero ore (decimali) impiegate per l'ispezione visuale complessiva(andrà poi diviso per il numero di ON in sede di rendicontazione) 
   * Calcolato (*) 
   * Formula: =ore per operazione*(peso riga a cui si sottrae 1 se ci sono campioni e  si sottrate il numero di trappole)
   *  -> =I2*SOMMA(K2;SE(BA2>0;-1;0);SE(BI2>0;-BI2;0))
   * @return
   */
  public static Double nHoursInspecting(Double pesoRiga,Double oreOperazione,Integer numCampioni, Integer numTrappole) {
    if(numCampioni>0) pesoRiga=pesoRiga-1;
    if(numTrappole>0) pesoRiga=pesoRiga-numTrappole;
    
    return pesoRiga*oreOperazione; 
  }  
  
  /**
   * Costo orario tecnico   Da Colonna paga oraria Foglio TECHNICIAN  
   * Formula: = CERCA.VERT(B2;$TECHNICIAN.$A$2:$TECHNICIAN.$B$47;2;FALSO())
   * @return
   */
  public static Double hourlyCostInspecting() {
    return null; 
  } 
  
  /**
   * Totale costo per ispezione visuale   
   * Calcolato: N_HOURS_INSPECTING X HOURLY_COST_INSPECTING  Formula:-> =U2*V2
   * @return
   */
  public static Double visualTotalCost() {
    return null; 
  }
  
  
  /**
   * Nome latino pianta Codificato in CERCA_VERTIC  
   * Formula: -> =CERCA.VERT(Z2;CERCA_VERTIC!$A$2:$AA$144;27)
   * @return
   */
  public static String nomeLatino() {
    return null; 
  }


}
