
package br.gov.ba.seia.entity;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.TipoAtoEnum;
import br.gov.ba.seia.util.Util;
import flexjson.JSON;

/**
 * 
 * @author luis
 */
@Entity
@Table(name = "ato_ambiental", uniqueConstraints = { @UniqueConstraint(columnNames = { "sgl_ato_ambiental" }),
		@UniqueConstraint(columnNames = { "nom_ato_ambiental" }) })
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "AtoAmbiental.findAll", query = "SELECT a FROM AtoAmbiental a"),
		@NamedQuery(name = "AtoAmbiental.removeByIdeAtoAmbiental", query = "DELETE FROM AtoAmbiental a WHERE a.ideAtoAmbiental = :ideAtoAmbiental"),
		@NamedQuery(name = "AtoAmbiental.findByIdeAtoAmbiental", query = "SELECT a FROM AtoAmbiental a WHERE a.ideAtoAmbiental = :ideAtoAmbiental"),
		@NamedQuery(name = "AtoAmbiental.findBySglAtoAmbiental", query = "SELECT a FROM AtoAmbiental a WHERE a.sglAtoAmbiental = :sglAtoAmbiental"),
		@NamedQuery(name = "AtoAmbiental.findByIdeTipoAto", query = "SELECT a FROM AtoAmbiental a WHERE a.ideTipoAto.ideTipoAto = :ideTipoAto and a.indAtivo = true and (a.indAutomatico = false or a.indAutomatico is null)"),
		@NamedQuery(name = "AtoAmbiental.findByNomAtoAmbiental", query = "SELECT a FROM AtoAmbiental a WHERE a.nomAtoAmbiental = :nomAtoAmbiental"),
		@NamedQuery(name = "AtoAmbiental.findByIdeProcesso", query = "SELECT a FROM AtoAmbiental a, ProcessoAto pa WHERE a.ideAtoAmbiental = pa.atoAmbiental.ideAtoAmbiental AND pa.processo.ideProcesso = :ideProcesso"),
		@NamedQuery(name = "AtoAmbiental.findByIdeProcessoTipoAto", query = "SELECT a FROM AtoAmbiental a, ProcessoAto pa WHERE a.ideAtoAmbiental = pa.atoAmbiental.ideAtoAmbiental AND pa.processo.ideProcesso = :ideProcesso and a.ideTipoAto.ideTipoAto = :ideTipoAto"),
		@NamedQuery(name = "AtoAmbiental.findByIdeProcessoTipoAtoAto", query = "SELECT a FROM AtoAmbiental a, ProcessoAto pa WHERE a.ideAtoAmbiental = pa.atoAmbiental.ideAtoAmbiental AND pa.processo.ideProcesso = :ideProcesso and a.ideTipoAto.ideTipoAto = :ideTipoAto and a.ideAtoAmbiental = :ideAtoAmbiental") })
@NamedNativeQuery(name = "AtoAmbiental.inserirEnquadramentoAto", query = "insert into enquadramento_ato_ambiental(ide_ato_ambiental,ide_enquadramento) values(:ideAtoAmbiental,:ideEnquadramento)", resultClass = AtoAmbiental.class)
public class AtoAmbiental extends AbstractEntity implements Comparable<AtoAmbiental>, Cloneable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "ide_ato_ambiental")
	private Integer ideAtoAmbiental;
	
	@Basic(optional = false)
	@Column(name = "sgl_ato_ambiental")
	private String sglAtoAmbiental;
	
	@Basic(optional = false)
	@Column(name = "nom_ato_ambiental")
	private String nomAtoAmbiental;
	
	@Basic(optional = false)
	@Column(name = "ind_declaratorio")
	private boolean indDeclaratorio;
	
	@Column(name = "num_dias_validade")
	private Integer numDiasValidade;
	
	@Column(name = "ind_ativo")
	private Boolean indAtivo;
	
	@Column(name = "ind_visivel_solicitacao_tla")
	private Boolean indVisivelSolicitacaoTla;
	
	@Column(name = "ind_automatico" )
	private Boolean indAutomatico ;
	
	@JoinColumn(name = "ide_tipo_ato", referencedColumnName = "ide_tipo_ato")
	@ManyToOne(optional = false)
	private TipoAto ideTipoAto;
	
	@JoinColumn(name = "ide_ato_sinaflor", referencedColumnName = "ide_ato_sinaflor")
	@ManyToOne
	private AtoSinaflor ideAtoSinaflor;
	
	@JoinTable(name = "enquadramento_ato_ambiental", joinColumns = { @JoinColumn(name = "ide_ato_ambiental", referencedColumnName = "ide_ato_ambiental") }, inverseJoinColumns = { @JoinColumn(name = "ide_enquadramento", referencedColumnName = "ide_enquadramento") })
	@ManyToMany
	private Collection<Enquadramento> enquadramentoCollection;
	
	@JoinTable(name = "documento_ato", joinColumns = { @JoinColumn(name = "ide_ato_ambiental", referencedColumnName = "ide_ato_ambiental") }, inverseJoinColumns = { @JoinColumn(name = "ide_documento_obrigatorio", referencedColumnName = "ide_documento_obrigatorio") })
	@ManyToMany
	private Collection<DocumentoObrigatorio> documentoObrigatorioCollection;
	
	@OneToMany(mappedBy = "atoAmbiental", fetch = FetchType.LAZY)
	private Collection<ParametroTaxaCertificado> parametrosTaxaCertificado;
	
	@OneToMany(mappedBy = "ideAtoAmbiental", fetch = FetchType.LAZY)
	private Collection<ParametroCalculo> parametroCalculoCollection;
	
	@OneToMany(mappedBy = "ideAtoAmbiental", fetch = FetchType.LAZY)
	private Collection<AtoAmbientalTipologia> atoAmbientalTipologiaCollection;
	
	@JoinTable(name = "solicitacao_administrativo_ato_ambiental", joinColumns = { @JoinColumn(name = "ide_ato_ambiental", referencedColumnName = "ide_ato_ambiental", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ide_solicitacao_administrativo", referencedColumnName = "ide_solicitacao_administrativo", nullable = false, updatable = false) })
	@ManyToMany(fetch = FetchType.LAZY)
	private Collection<SolicitacaoAdministrativo> solicitacaoCollection;
	
	@Transient
	private Enquadramento enquadramentoRequerimento;
	
	@Transient
	private Collection<SelectItem> atosAmbientaisDocs;
	
	@Transient
	private boolean rowSelect = false;
	
	@Transient
	private String numPortariaRVFR;

	@Transient
	private Tipologia tipologia;
	
	@Transient
	private Collection<TipoFinalidadeUsoAgua> listaTipoFinalidadeUsoAgua;
	
	@Transient
	private String dscJustificativa;
	
    @OneToMany( mappedBy = "atoAmbiental")
    private Collection<EnquadramentoAtoAmbiental> enquadramentoAtoAmbientalCollection;
    
    @OneToMany( mappedBy = "ideAtoAmbiental")
    private Collection<DocumentoAto> documentoAtoCollection;
	
	@Transient
	private Collection<DocumentoObrigatorioRequerimento> listaDocumentos;
	
	@Transient
	private List<DocumentoAto> listaDocumentoAtoSelecionado;
	
	@OneToMany(mappedBy="ideAtoAmbiental")
	private Collection<CondicionanteAtoAmbiental> condicionanteAtoAmbientalCollection;
	
	@Column(name = "ind_prazo_indeterminado")
	private Boolean indPrazoIndeterminado;
	
	@Transient
	private Collection<Tipologia> listaReenquadramentoTipologiaEmpreendimento;
	
	@Transient
	private Collection<ObjetivoAtividadeManejo> listaObjetivoAtividadeManejo;
	
	@Transient
	private Collection<TipoAtividadeFauna> listaTipoAtividadeFauna;
	
	@Transient
	private Boolean indAlteracao;

	public AtoAmbiental() {
	}

	public AtoAmbiental(Integer ideAtoAmbiental) {
		this.ideAtoAmbiental = ideAtoAmbiental;
	}
	
	public AtoAmbiental(AtoAmbientalEnum atoAmbientalEnum) {
		this.ideAtoAmbiental = atoAmbientalEnum.getId();
	}

	public AtoAmbiental(Integer ideAtoAmbiental, String sglAtoAmbiental, String nomAtoAmbiental, boolean indDeclaratorio) {
		this.ideAtoAmbiental = ideAtoAmbiental;
		this.sglAtoAmbiental = sglAtoAmbiental;
		this.nomAtoAmbiental = nomAtoAmbiental;
		this.indDeclaratorio = indDeclaratorio;
	}

	public AtoAmbiental(Integer ideAtoAmbiental, String sglAtoAmbiental) {
		this.ideAtoAmbiental = ideAtoAmbiental;
		this.sglAtoAmbiental = sglAtoAmbiental;
	}

	public AtoAmbiental(int ideAtoAmbiental, String sglAtoAmbiental, String nomAtoAmbiental) {
		this.ideAtoAmbiental = ideAtoAmbiental;
		this.nomAtoAmbiental = nomAtoAmbiental;
	}

	@JSON(include = false)
	public Integer getIdeAtoAmbiental() {
		return ideAtoAmbiental;
	}

	public void setIdeAtoAmbiental(Integer ideAtoAmbiental) {
		this.ideAtoAmbiental = ideAtoAmbiental;
	}

	@JSON(include = false)
	public String getSglAtoAmbiental() {
		return sglAtoAmbiental;
	}

	public void setSglAtoAmbiental(String sglAtoAmbiental) {
		this.sglAtoAmbiental = sglAtoAmbiental;
	}

	public String getNomAtoAmbiental() {
		return nomAtoAmbiental;
	}

	public void setNomAtoAmbiental(String nomAtoAmbiental) {
		this.nomAtoAmbiental = nomAtoAmbiental;
	}

	@JSON(include = false)
	public boolean getIndDeclaratorio() {
		return indDeclaratorio;
	}

	public void setIndDeclaratorio(boolean indDeclaratorio) {
		this.indDeclaratorio = indDeclaratorio;
	}

	@JSON(include = false)
	public Integer getNumDiasValidade() {
		return numDiasValidade;
	}

	public void setNumDiasValidade(Integer numDiasValidade) {
		this.numDiasValidade = numDiasValidade;
	}

	@JSON(include = false)
	public Boolean getIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	@JSON(include = false)
	public Collection<Enquadramento> getEnquadramentoCollection() {
		return enquadramentoCollection;
	}

	public void setEnquadramentoCollection(Collection<Enquadramento> enquadramentoCollection) {
		this.enquadramentoCollection = enquadramentoCollection;
	}

	@JSON(include = false)
	public Collection<DocumentoObrigatorio> getDocumentoObrigatorioCollection() {
		return documentoObrigatorioCollection;
	}

	public void setDocumentoObrigatorioCollection(Collection<DocumentoObrigatorio> documentoObrigatorioCollection) {
		this.documentoObrigatorioCollection = documentoObrigatorioCollection;
	}

	@JSON(include = false)
	public TipoAto getIdeTipoAto() {	
		return ideTipoAto;
	}

	public void setIdeTipoAto(TipoAto ideTipoAto) {
		this.ideTipoAto = ideTipoAto;
	}
	
	public boolean isAutorizacao(){
		return new Integer(TipoAtoEnum.AUTORIZACAO.getId()).equals(ideTipoAto.getIdeTipoAto());
	}
	
	@JSON(include = false)
	public boolean isLac() {
		return AtoAmbientalEnum.LAC.getId().equals(this.ideAtoAmbiental);
	}

	@JSON(include = false)
	public boolean isRfp() {
		return AtoAmbientalEnum.REGISTRO_FLORESTA_PRODUCAO.getId().equals(this.ideAtoAmbiental);
	}

	@JSON(include = false)
	public boolean isRcfp() {
		return AtoAmbientalEnum.CORTE_FLORESTA_PRODUCAO.getId().equals(this.ideAtoAmbiental);
	}
	
	@JSON(include = false)
	public boolean isDqc() {
		return AtoAmbientalEnum.DQC.getId().equals(this.ideAtoAmbiental);
	}
	
	public boolean isDiap() {
		return AtoAmbientalEnum.DIAP.getId().equals(this.ideAtoAmbiental);
	}

	public boolean isFlorestal() {
		List<AtoAmbientalEnum> asList = Arrays.asList(new AtoAmbientalEnum[] {
				AtoAmbientalEnum.EPMF, 
				AtoAmbientalEnum.ASV, 
				AtoAmbientalEnum.AMC
			});		
		return asList.contains(AtoAmbientalEnum.getEnum(this));
	}
	
	@JSON(include = false)
	public boolean isRowSelect() {
		return rowSelect;
	}

	public void setRowSelect(boolean rowSelect) {
		this.rowSelect = rowSelect;
	}

	@JSON(include = false)
	public String getNumPortariaRVFR() {
		return numPortariaRVFR;
	}

	public void setNumPortariaRVFR(String numPortariaRVFR) {
		this.numPortariaRVFR = numPortariaRVFR;
	}

	@JSON(include = false)
	public Collection<DocumentoObrigatorioRequerimento> getListaDocumentos() {
		return listaDocumentos;
	}

	public void setListaDocumentos(Collection<DocumentoObrigatorioRequerimento> listaDocumentos) {
		this.listaDocumentos = listaDocumentos;
	}

	@JSON(include = false)
	public Collection<EnquadramentoAtoAmbiental> getEnquadramentoAtoAmbientalCollection() {
		return enquadramentoAtoAmbientalCollection;
	}

	public void setEnquadramentoAtoAmbientalCollection(
			Collection<EnquadramentoAtoAmbiental> enquadramentoAtoAmbientalCollection) {
		this.enquadramentoAtoAmbientalCollection = enquadramentoAtoAmbientalCollection;
	}

	@JSON(include = false)
	public Collection<DocumentoAto> getDocumentoAtoCollection() {
		return documentoAtoCollection;
	}

	public void setDocumentoAtoCollection(Collection<DocumentoAto> documentoAtoCollection) {
		this.documentoAtoCollection = documentoAtoCollection;
	}

	@JSON(include = false)
	public Enquadramento getEnquadramentoRequerimento() {
		return enquadramentoRequerimento;
	}

	public void setEnquadramentoRequerimento(Enquadramento enquadramentoRequerimento) {
		this.enquadramentoRequerimento = enquadramentoRequerimento;
	}

	@JSON(include = false)
	public Collection<SolicitacaoAdministrativo> getSolicitacaoCollection() {
		return solicitacaoCollection;
	}

	public void setSolicitacaoCollection(Collection<SolicitacaoAdministrativo> solicitacaoCollection) {
		this.solicitacaoCollection = solicitacaoCollection;
	}

	@JSON(include = false)
	public Boolean getIndAutomatico() {
		return indAutomatico;
	}

	public void setIndAutomatico(Boolean indAutomatico) {
		this.indAutomatico = indAutomatico;
	}

	@JSON(include = false)
	public Boolean getIndVisivelSolicitacaoTla() {
		return indVisivelSolicitacaoTla;
	}

	public void setIndVisivelSolicitacaoTla(Boolean indVisivelSolicitacaoTla) {
		this.indVisivelSolicitacaoTla = indVisivelSolicitacaoTla;
	}

	@JSON(include = false)
	public boolean isOutorga() {
		return AtoAmbientalEnum.OUTORGA.getId().equals(this.ideAtoAmbiental);
	}
	
	public boolean isOutorgaPreventiva(){
		return AtoAmbientalEnum.OUTP.getId().equals(this.ideAtoAmbiental);
	}
	
	public boolean isTLA() {
		return AtoAmbientalEnum.TLA.getId().equals(this.ideAtoAmbiental);
	}
	
	public boolean isPerfuracaoDePoco() {
		return AtoAmbientalEnum.PERFURACAO_POCO.getId().equals(this.ideAtoAmbiental);
	}

	public boolean isRlac(){
		return AtoAmbientalEnum.RLAC.getId().equals(this.ideAtoAmbiental);
	}
	
	public boolean isAOUT(){
		return AtoAmbientalEnum.AOUT.getId().equals(this.ideAtoAmbiental);
	}
	
	public boolean isDispensaOutorga(){
		return AtoAmbientalEnum.DOUT.getId().equals(this.ideAtoAmbiental);
	}
	
	public boolean isSisfauna(){
		return AtoAmbientalEnum.SISFAUNA.getId().equals(this.ideAtoAmbiental);
	}
	
	public boolean isLA(){
		return AtoAmbientalEnum.LA.getId().equals(this.ideAtoAmbiental);
	}
	
	public boolean isAPE(){
		return AtoAmbientalEnum.APE.getId().equals(this.ideAtoAmbiental);
	}
	
	public boolean isARL(){
		return AtoAmbientalEnum.ARL.getId().equals(this.ideAtoAmbiental);
	}
	public boolean isARRL(){
		return AtoAmbientalEnum.ARRL.getId().equals(this.ideAtoAmbiental);
	}
	
	public boolean isAMC(){
		return AtoAmbientalEnum.AMC.getId().equals(this.ideAtoAmbiental);
	}
	
	public boolean isAMF(){
		return AtoAmbientalEnum.AMF.getId().equals(this.ideAtoAmbiental);
	}
	
	public boolean isARTA(){
		return AtoAmbientalEnum.ARTA.getId().equals(this.ideAtoAmbiental);
	}

	public boolean isASV() {
		return AtoAmbientalEnum.ASV.getId().equals(this.ideAtoAmbiental);
	}
	
	public boolean isCOUT(){
		return AtoAmbientalEnum.COUT.getId().equals(this.ideAtoAmbiental);
	}
	
	public boolean isCRF(){
		return AtoAmbientalEnum.CRF.getId().equals(this.ideAtoAmbiental);
	}
	
	@Override
	public int compareTo(AtoAmbiental arg0) {
		if(Util.isNullOuVazio(nomAtoAmbiental)){
			return 0;
		}
		return this.nomAtoAmbiental.compareTo(arg0.nomAtoAmbiental);
	}

	public List<DocumentoAto> getListaDocumentoAtoSelecionado() {
		return listaDocumentoAtoSelecionado;
	}

	public void setListaDocumentoAtoSelecionado(List<DocumentoAto> listaDocumentoAtoSelecionado) {
		this.listaDocumentoAtoSelecionado = listaDocumentoAtoSelecionado;
	}

	public Collection<ParametroCalculo> getParametroCalculoCollection() {
		return parametroCalculoCollection;
	}

	public void setParametroCalculoCollection(	Collection<ParametroCalculo> parametroCalculoCollection) {
		this.parametroCalculoCollection = parametroCalculoCollection;
	}

	public Tipologia getTipologia() {
		return tipologia;
	}

	public void setTipologia(Tipologia tipologia) {
		this.tipologia = tipologia;
	}	
	
	@Transient
	public String getNomAtoAmbientalTipologia(){
		if(Util.isNullOuVazio(tipologia)|| Util.isNullOuVazio(tipologia.getDesTipologia())){
			return nomAtoAmbiental;
		}
		else {
			return nomAtoAmbiental + " - "+ tipologia.getDesTipologia();
		}
	}

	public Collection<AtoAmbientalTipologia> getAtoAmbientalTipologiaCollection() {
		return atoAmbientalTipologiaCollection;
	}

	public void setAtoAmbientalTipologiaCollection(Collection<AtoAmbientalTipologia> atoAmbientalTipologiaCollection) {
		this.atoAmbientalTipologiaCollection = atoAmbientalTipologiaCollection;
	}
	
	@Override
	public AtoAmbiental clone() throws CloneNotSupportedException {
		return (AtoAmbiental) super.clone();
	}
	
	@Override
	public String toString() {
	
		if(tipologia!= null && tipologia.getIdeTipologia()!=null){
			return "[" + " Ato " + String.valueOf(ideAtoAmbiental) + " Tipologia "+ String.valueOf(tipologia.getIdeTipologia())+ "]";
		}else {
			return String.valueOf(ideAtoAmbiental);			
		}
	}

	/**
	 * Getter do campo condicionanteAtoAmbientalCollection
	 *	
	 * @return the condicionanteAtoAmbientalCollection
	 */
	public Collection<CondicionanteAtoAmbiental> getCondicionanteAtoAmbientalCollection() {
		return condicionanteAtoAmbientalCollection;
	}

	/**
	 * Setter do campo  condicionanteAtoAmbientalCollection
	 * @param condicionanteAtoAmbientalCollection the condicionanteAtoAmbientalCollection to set
	 */
	public void setCondicionanteAtoAmbientalCollection(Collection<CondicionanteAtoAmbiental> condicionanteAtoAmbientalCollection) {
		this.condicionanteAtoAmbientalCollection = condicionanteAtoAmbientalCollection;
	}

	public Boolean getIndPrazoIndeterminado() {
		return indPrazoIndeterminado;
	}

	public void setIndPrazoIndeterminado(Boolean indPrazoIndeterminado) {
		this.indPrazoIndeterminado = indPrazoIndeterminado;
	}
	
	public AtoSinaflor getIdeAtoSinaflor() {
		return ideAtoSinaflor;
	}

	public void setIdeAtoSinaflor(AtoSinaflor ideAtoSinaflor) {
		this.ideAtoSinaflor = ideAtoSinaflor;
	}

	public String getDscJustificativa() {
		return dscJustificativa;
	}

	public void setDscJustificativa(String dscJustificativa) {
		this.dscJustificativa = dscJustificativa;
	}

	public Collection<Tipologia> getListaReenquadramentoTipologiaEmpreendimento() {
		return listaReenquadramentoTipologiaEmpreendimento;
	}

	public void setListaReenquadramentoTipologiaEmpreendimento(Collection<Tipologia> listaReenquadramentoTipologiaEmpreendimento) {
		this.listaReenquadramentoTipologiaEmpreendimento = listaReenquadramentoTipologiaEmpreendimento;
	}

	public Collection<TipoFinalidadeUsoAgua> getListaTipoFinalidadeUsoAgua() {
		return listaTipoFinalidadeUsoAgua;
	}

	public void setListaTipoFinalidadeUsoAgua(Collection<TipoFinalidadeUsoAgua> listaTipoFinalidadeUsoAgua) {
		this.listaTipoFinalidadeUsoAgua = listaTipoFinalidadeUsoAgua;
	}

	public Boolean getIndAlteracao() {
		return indAlteracao;
	}

	public void setIndAlteracao(Boolean indAlteracao) {
		this.indAlteracao = indAlteracao;
	}

	public Collection<ObjetivoAtividadeManejo> getListaObjetivoAtividadeManejo() {
		return listaObjetivoAtividadeManejo;
	}

	public void setListaObjetivoAtividadeManejo(Collection<ObjetivoAtividadeManejo> listaObjetivoAtividadeManejo) {
		this.listaObjetivoAtividadeManejo = listaObjetivoAtividadeManejo;
	}

	public Collection<TipoAtividadeFauna> getListaTipoAtividadeFauna() {
		return listaTipoAtividadeFauna;
	}

	public void setListaTipoAtividadeFauna(Collection<TipoAtividadeFauna> listaTipoAtividadeFauna) {
		this.listaTipoAtividadeFauna = listaTipoAtividadeFauna;
	}

	
}
