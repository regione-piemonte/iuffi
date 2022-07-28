package it.csi.iuffi.iuffiweb.dto.motoriagricoli;

import java.util.Date;

import it.csi.iuffi.iuffiweb.dto.internal.ILoggable;
import it.csi.iuffi.iuffiweb.util.IuffiUtils;

public class MotoriAgricoliDTO implements ILoggable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long idMacchina;
	private String descTipoGenereMacchina;
	private String descTipoCategoria;
	private String descTipoMarca;
	private String tipoMacchina;
	private Long potenzaKw;
	private Date dataCarico;
	private String cuaa;
	
	
	public long getIdMacchina()
	{
		return idMacchina;
	}
	public void setIdMacchina(long idMacchina)
	{
		this.idMacchina = idMacchina;
	}
	public String getDescTipoGenereMacchina()
	{
		return descTipoGenereMacchina;
	}
	public void setDescTipoGenereMacchina(String descTipoGenereMacchina)
	{
		this.descTipoGenereMacchina = descTipoGenereMacchina;
	}
	public String getDescTipoCategoria()
	{
		return descTipoCategoria;
	}
	public void setDescTipoCategoria(String descTipoCategoria)
	{
		this.descTipoCategoria = descTipoCategoria;
	}
	public String getDescTipoMarca()
	{
		return descTipoMarca;
	}
	public void setDescTipoMarca(String descTipoMarca)
	{
		this.descTipoMarca = descTipoMarca;
	}
	public String getTipoMacchina()
	{
		return tipoMacchina;
	}
	public void setTipoMacchina(String tipoMacchina)
	{
		this.tipoMacchina = tipoMacchina;
	}
	public Long getPotenzaKw()
	{
		return potenzaKw;
	}
	public void setPotenzaKw(Long potenzaKw)
	{
		this.potenzaKw = potenzaKw;
	}
	public Date getDataCarico()
	{
		return dataCarico;
	}
	public void setDataCarico(Date dataCarico)
	{
		this.dataCarico = dataCarico;
	}
	public String getDataCaricoFormatted()
	{
		return IuffiUtils.DATE.formatDate(this.dataCarico);
	}
	public String getCuaa()
	{
		return cuaa;
	}
	public void setCuaa(String cuaa)
	{
		this.cuaa = cuaa;
	}
	public String getDescMacchinaDanno()
	{
		return 
			   ((tipoMacchina == null || tipoMacchina.equals("")) ? "" :  tipoMacchina + " ") +
			   ((descTipoMarca == null || descTipoMarca.equals("")) ? "" : descTipoMarca + " ") +
			   ((descTipoGenereMacchina == null || descTipoGenereMacchina.equals("")) ? "" : descTipoGenereMacchina + " ") +  
			   ((descTipoCategoria == null || descTipoCategoria.equals("")) ? "" : descTipoCategoria + " ")
			   ;

	}
	public String getDannoDenominazione()
	{
		return 
				((tipoMacchina == null || tipoMacchina.equals("")) ? "" :  tipoMacchina + " ") +
				((descTipoMarca == null || descTipoMarca.equals("")) ? "" : descTipoMarca + " ") +
				((descTipoCategoria == null || descTipoCategoria.equals("")) ? "" : descTipoCategoria + " ")
				;
	}
	
}
