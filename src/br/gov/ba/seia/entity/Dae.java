/**
 * 
 */
package br.gov.ba.seia.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.entity.generic.AbstractEntity;


/**
 * @author lesantos
 *
 */
@Entity
@Table(name = "dae")
public class Dae extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 797580661440586214L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dae_seq")
	@SequenceGenerator(name = "dae_seq", sequenceName = "dae_seq", allocationSize = 1)
	@Column( name = "ide_dae")
	private Integer ideDae;
	
	@ManyToOne
	@JoinColumn(name="ide_dae_pai")
	private Dae DaePai;
	
	@Column( name = "dsc_nosso_numero")
	private String dscNossoNumero;
	
	@Column( name = "dt_emissao")
	private Date dtEmissao;
	
	@Column( name = "dt_vencimento")
	private Date dtVencimento;
	
	@Column( name = "cod_documento_origem")
	private Integer codDocumentoOrigem;
	
	@Column( name = "num_documento_origem")
	private String numDocumentoOrigem;

	@Column( name = "cod_referencia")
	private Integer codReferencia;
	
	@Column( name = "num_mes_referencia")
	private Integer numMesReferencia;
	
	@Column( name = "num_ano_referencia")
	private Integer numAnoReferencia;
	
	@Column( name = "num_parcela_referencia")
	private Integer numParcelaReferencia;
	
	@Column( name = "num_total_parcela_referencia")
	private Integer numTotalParcelaReferencia;
	
	@Column( name = "url_dae")
	private String urlDae;
	
	@Column( name = "cod_barras")
	private String codbarras;
	
	@Column( name = "des_aviso")
	private String desAviso;
	
	@Column( name = "dsc_informacoes_complementares")
	private String dscInformacoesComplementares;
	
	@ManyToOne
	@JoinColumn(name="ide_sefaz_codigo_receita")
	private SefazCodigoReceita sefazCodigoReceita; 
	
	@ManyToOne
	@JoinColumn(name="ide_cerh_parcelas_cobranca")
	private CerhParcelasCobranca cerhParcelasCobranca;
	
	@OneToMany(mappedBy = "dae", cascade = CascadeType.ALL)
	private List<CerhDaeTipoUso> cerhDaetipoUsos;
	
	@OneToMany(mappedBy = "ideDae", cascade = CascadeType.ALL)
	private List<HistSituacaoDae> histSituacaoDae;
	
	@Transient
	private String rpga;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "ideDae")
	private List<PagamentoDae> pagamentoDaeCollection;
	
	@Transient
	private boolean atraso;
	
	@Transient
	private Date dtcMaxPagamento;
	
	public Dae() {
		cerhDaetipoUsos = new ArrayList<CerhDaeTipoUso>();
		histSituacaoDae = new ArrayList<HistSituacaoDae>();
		pagamentoDaeCollection = new ArrayList<PagamentoDae>();
	}
	
	/**
	 * @return the ideDae
	 */
	public Integer getIdeDae() {
		return ideDae;
	}

	/**
	 * @param ideDae the ideCerhDae to set
	 */
	public void setIdeDae(Integer ideDae) {
		this.ideDae = ideDae;
	}

	/**
	 * @return the cerhDaePai
	 */
	public Dae getDaePai() {
		return DaePai;
	}

	/**
	 * @param daePai the cerhDaePai to set
	 */
	public void setDaePai(Dae daePai) {
		DaePai = daePai;
	}

	/**
	 * @return the dscNossoNumero
	 */
	public String getDscNossoNumero() {
		return dscNossoNumero;
	}

	/**
	 * @param dscNossoNumero the dscNossoNumero to set
	 */
	public void setDscNossoNumero(String dscNossoNumero) {
		this.dscNossoNumero = dscNossoNumero;
	}

	/**
	 * @return the dtEmissao
	 */
	public Date getDtEmissao() {
		return dtEmissao;
	}

	/**
	 * @param dtEmissao the dtEmissao to set
	 */
	public void setDtEmissao(Date dtEmissao) {
		this.dtEmissao = dtEmissao;
	}

	/**
	 * @return the dtVencimento
	 */
	public Date getDtVencimento() {
		return dtVencimento;
	}

	/**
	 * @param dtVencimento the dtVencimento to set
	 */
	public void setDtVencimento(Date dtVencimento) {
		this.dtVencimento = dtVencimento;
	}

	/**
	 * @return the codDocumentoOrigem
	 */
	public Integer getCodDocumentoOrigem() {
		return codDocumentoOrigem;
	}

	/**
	 * @param codDocumentoOrigem the codDocumentoOrigem to set
	 */
	public void setCodDocumentoOrigem(Integer codDocumentoOrigem) {
		this.codDocumentoOrigem = codDocumentoOrigem;
	}

	/**
	 * @return the numDocumentoOrigem
	 */
	public String getNumDocumentoOrigem() {
		return numDocumentoOrigem;
	}

	/**
	 * @param numDocumentoOrigem the numDocumentoOrigem to set
	 */
	public void setNumDocumentoOrigem(String numDocumentoOrigem) {
		this.numDocumentoOrigem = numDocumentoOrigem;
	}

	/**
	 * @return the codReferencia
	 */
	public Integer getCodReferencia() {
		return codReferencia;
	}

	/**
	 * @param codReferencia the codReferencia to set
	 */
	public void setCodReferencia(Integer codReferencia) {
		this.codReferencia = codReferencia;
	}

	/**
	 * @return the numMesReferencia
	 */
	public Integer getNumMesReferencia() {
		return numMesReferencia;
	}

	/**
	 * @param numMesReferencia the numMesReferencia to set
	 */
	public void setNumMesReferencia(Integer numMesReferencia) {
		this.numMesReferencia = numMesReferencia;
	}

	/**
	 * @return the numAnoReferencia
	 */
	public Integer getNumAnoReferencia() {
		return numAnoReferencia;
	}

	/**
	 * @param numAnoReferencia the numAnoReferencia to set
	 */
	public void setNumAnoReferencia(Integer numAnoReferencia) {
		this.numAnoReferencia = numAnoReferencia;
	}

	/**
	 * @return the numParcelaReferencia
	 */
	public Integer getNumParcelaReferencia() {
		return numParcelaReferencia;
	}

	/**
	 * @param numParcelaReferencia the numParcelaReferencia to set
	 */
	public void setNumParcelaReferencia(Integer numParcelaReferencia) {
		this.numParcelaReferencia = numParcelaReferencia;
	}

	/**
	 * @return the numTotalParcelaReferencia
	 */
	public Integer getNumTotalParcelaReferencia() {
		return numTotalParcelaReferencia;
	}

	/**
	 * @param numTotalParcelaReferencia the numTotalParcelaReferencia to set
	 */
	public void setNumTotalParcelaReferencia(Integer numTotalParcelaReferencia) {
		this.numTotalParcelaReferencia = numTotalParcelaReferencia;
	}

	/**
	 * @return the urlDae
	 */
	public String getUrlDae() {
		return urlDae;
	}

	/**
	 * @param urlDae the urlDae to set
	 */
	public void setUrlDae(String urlDae) {
		this.urlDae = urlDae;
	}

	/**
	 * @return the codbarras
	 */
	public String getCodbarras() {
		return codbarras;
	}

	/**
	 * @param codbarras the codbarras to set
	 */
	public void setCodbarras(String codbarras) {
		this.codbarras = codbarras;
	}

	/**
	 * @return the desAviso
	 */
	public String getDesAviso() {
		return desAviso;
	}

	/**
	 * @param desAviso the desAviso to set
	 */
	public void setDesAviso(String desAviso) {
		this.desAviso = desAviso;
	}

	/**
	 * @return the dscInformacoesComplementares
	 */
	public String getDscInformacoesComplementares() {
		return dscInformacoesComplementares;
	}

	/**
	 * @param dscInformacoesComplementares the dscInformacoesComplementares to set
	 */
	public void setDscInformacoesComplementares(String dscInformacoesComplementares) {
		this.dscInformacoesComplementares = dscInformacoesComplementares;
	}

	/**
	 * @return the rpga
	 */
	public String getRpga() {
		return rpga;
	}

	/**
	 * @param rpga the rpga to set
	 */
	public void setRpga(String rpga) {
		this.rpga = rpga;
	}

	/**
	 * @return the atraso
	 */
	public boolean isAtraso() {
		return atraso;
	}

	/**
	 * @param atraso the atraso to set
	 */
	public void setAtraso(boolean atraso) {
		this.atraso = atraso;
	}

/*	*//**
	 * @return the cerhParcelasCobranca
	 *//*
	public CerhParcelasCobranca getCerhParcelasCobranca() {
		return cerhParcelasCobranca;
	}

	*//**
	 * @param cerhParcelasCobranca the cerhParcelasCobranca to set
	 *//*
	public void setCerhParcelasCobranca(CerhParcelasCobranca cerhParcelasCobranca) {
		this.cerhParcelasCobranca = cerhParcelasCobranca;
	}*/

	/**
	 * @return the cerhDaetipoUsos
	 */
	public List<CerhDaeTipoUso> getCerhDaetipoUsos() {
		return cerhDaetipoUsos;
	}

	/**
	 * @param cerhDaetipoUsos the cerhDaetipoUsos to set
	 */
	public void setCerhDaetipoUsos(List<CerhDaeTipoUso> cerhDaetipoUsos) {
		this.cerhDaetipoUsos = cerhDaetipoUsos;
	}
	
	public BigDecimal getValorTotal(){
		BigDecimal valor = new BigDecimal(0);
		for(CerhDaeTipoUso c : this.cerhDaetipoUsos){
			valor = valor.add(c.getValorOriginal()).add(c.getValorAcrescimo());
		}
		return valor;
	}
	
	public BigDecimal getValorAcrescimo(){
		BigDecimal valor = new BigDecimal(0);
		for(CerhDaeTipoUso c : this.cerhDaetipoUsos){
			valor = valor.add(c.getValorAcrescimo()).add(c.getValorAcrescimo());
		}
		return valor;
	}
	
	public BigDecimal getValorDae(){
		BigDecimal valor = new BigDecimal(0);
		for(CerhDaeTipoUso c : this.cerhDaetipoUsos){
			valor = valor.add(c.getValorOriginal());
		}
		return valor;
	}

	public String getTiposUsoRecusroHidrico(){
		List<String> tipos = new ArrayList<String>();
		for(CerhDaeTipoUso t : this.cerhDaetipoUsos){
			if(!tipos.contains(t.getTipoUsoRecursoHidrico().getDscTipoUsoRecursoHidrico())){
				tipos.add(t.getTipoUsoRecursoHidrico().getDscTipoUsoRecursoHidrico());
			}
		}
		
		StringBuilder sb = new StringBuilder();
		for(int x = 0; x < tipos.size(); x++){
			sb.append(tipos.get(x));
			if(x < tipos.size() -1){
				sb.append(" / ");
			}
		}
		return sb.toString();
	}

	public List<TipoUsoRecursoHidrico> getTipoUsoRecursoHidricos(){
		List<TipoUsoRecursoHidrico> tipos = new ArrayList<TipoUsoRecursoHidrico>();
		for(CerhDaeTipoUso t : this.cerhDaetipoUsos){
			if(!tipos.contains(t.getTipoUsoRecursoHidrico().getDscTipoUsoRecursoHidrico())){
				tipos.add(t.getTipoUsoRecursoHidrico());
			}
		}
		return tipos;
	}
	
	/**
	 * @return the cerhHistSituacaoDae
	 */
	public List<HistSituacaoDae> getHistSituacaoDae() {
		return histSituacaoDae;
	}

	/**
	 * @param histSituacaoDae the cerhHistSituacaoDae to set
	 */
	public void setHistSituacaoDae(List<HistSituacaoDae> histSituacaoDae) {
		this.histSituacaoDae = histSituacaoDae;
	}

	/**
	 * @return the cerhCodigoReceita
	 */
	public SefazCodigoReceita getCerhCodigoReceita() {
		return sefazCodigoReceita;
	}

	/**
	 * @param cerhCodigoReceita the cerhCodigoReceita to set
	 */
	public void setCerhCodigoReceita(SefazCodigoReceita cerhCodigoReceita) {
		this.sefazCodigoReceita = cerhCodigoReceita;
	}

	/**
	 * @return the cerhPagamentoDaeCollection
	 */
	public List<PagamentoDae> getCerhPagamentoDaeCollection() {
		return pagamentoDaeCollection;
	}

	/**
	 * @param cerhPagamentoDaeCollection the cerhPagamentoDaeCollection to set
	 */
	public void setCerhPagamentoDaeCollection(
			List<PagamentoDae> cerhPagamentoDaeCollection) {
		this.pagamentoDaeCollection = cerhPagamentoDaeCollection;
	}

	public Date getDtcMaxPagamento() {
		return dtcMaxPagamento;
	}

	public void setDtcMaxPagamento(Date dtcMaxPagamento) {
		this.dtcMaxPagamento = dtcMaxPagamento;
	}

	public CerhParcelasCobranca getCerhParcelasCobranca() {
		return cerhParcelasCobranca;
	}

	public void setCerhParcelasCobranca(CerhParcelasCobranca cerhParcelasCobranca) {
		this.cerhParcelasCobranca = cerhParcelasCobranca;
	}

}
