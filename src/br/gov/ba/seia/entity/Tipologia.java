package br.gov.ba.seia.entity;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.enumerator.OpcaoEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;
import flexjson.JSON;

@Entity
@Table(name = "tipologia")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Tipologia.findAll", query = "SELECT t FROM Tipologia t"),
	@NamedQuery(name = "Tipologia.findByIdeTipologia", query = "SELECT t FROM Tipologia t WHERE t.ideTipologia = :ideTipologia"),
	@NamedQuery(name = "Tipologia.findByCodTipologia", query = "SELECT t FROM Tipologia t WHERE t.codTipologia = :codTipologia"),
	@NamedQuery(name = "Tipologia.findByDesTipologia", query = "SELECT t FROM Tipologia t WHERE t.desTipologia = :desTipologia"),
	@NamedQuery(name = "Tipologia.findByDtcCriacao", query = "SELECT t FROM Tipologia t WHERE t.dtcCriacao = :dtcCriacao"),
	@NamedQuery(name = "Tipologia.findByDtcExclusao", query = "SELECT t FROM Tipologia t WHERE t.dtcExclusao = :dtcExclusao"),
	@NamedQuery(name = "Tipologia.findByIndExcluido", query = "SELECT t FROM Tipologia t WHERE t.indExcluido = :indExcluido"),
	@NamedQuery(name = "Tipologia.findByIndPossuiFilhos", query = "SELECT t FROM Tipologia t WHERE t.indPossuiFilhos = :indPossuiFilhos") })
public class Tipologia extends AbstractEntity implements Cloneable, Comparable<Tipologia> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TIPOLOGIA_IDE_TIPOLOGIA_seq")
	@SequenceGenerator(name = "TIPOLOGIA_IDE_TIPOLOGIA_seq", sequenceName = "TIPOLOGIA_IDE_TIPOLOGIA_seq", allocationSize = 1)
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_tipologia", nullable = false)
	private Integer ideTipologia;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 20)
	@Column(name = "cod_tipologia", nullable = false, length = 20)
	private String codTipologia;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 250)
	@Column(name = "des_tipologia", nullable = false, length = 250)
	private String desTipologia;

	@Basic(optional = false)
	@NotNull
	@Column(name = "dtc_criacao", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcCriacao;

	@Column(name = "dtc_exclusao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcExclusao;

	@Basic(optional = false)
	@NotNull
	@Column(name = "ind_excluido", nullable = false)
	private boolean indExcluido;

	@Column(name = "ind_autorizacao", nullable = false)
	private Boolean indAutorizacao;

	@Basic(optional = false)
	@NotNull
	@Column(name = "ind_possui_filhos", nullable = false)
	private boolean indPossuiFilhos;

	@JoinColumn(name = "ide_nivel_tipologia", referencedColumnName = "ide_nivel_tipologia", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private NivelTipologia ideNivelTipologia;

	@OneToOne(mappedBy = "ideTipologia", fetch = FetchType.LAZY)
	private TipologiaGrupo tipologiaGrupo;

	@JoinColumn(name = "ide_tipologia_pai", referencedColumnName = "ide_tipologia")
	@ManyToOne(fetch = FetchType.LAZY)
	private Tipologia ideTipologiaPai;

	@ManyToMany(mappedBy = "tipologiaCollection", fetch = FetchType.LAZY)
	private Collection<Cnae> cnaeCollection;

	@OneToMany(mappedBy = "ideTipologiaPai", fetch = FetchType.LAZY)
	private Collection<Tipologia> tipologiaCollection;

	@OneToMany(mappedBy = "ideTipologia", fetch = FetchType.LAZY)
	private Collection<AtoAmbientalTipologia> atoAmbientalTipologiaCollection;
	
	@OneToMany(mappedBy = "ideTipologia", fetch = FetchType.LAZY)
	private Collection<SubstanciaMineralTipologia> substanciaMineralTipologiaCollection;
	
	@OneToMany(mappedBy = "tipologia", fetch = FetchType.LAZY)
	private Collection<ProcessoAto> processoAtoCollection;

	@Transient
	private Porte porte;

	@Transient
	private String valAtividade;

	@Transient
	private BigDecimal valorAtividade;

	@Transient
	private boolean indPrincipal;

	@Transient
	private OpcaoEnum opcao;

	@Transient
	private Integer option;//UTILIZADO SOMENTE PARA CARREGAR VALOR DA OPCAO VINDO DA CONSULTA RequerimentoTipologiaService.buscarTipologias()

	@Transient
	private boolean doEmpreendimento;
	
	@Transient
	private Classe classe;
	
	@Transient
	private BigDecimal valorTaxa;
	
	@Transient
	private Collection<CerhLocalizacaoGeografica> listaCerhLocalizacaoGeografica;
	
	@Transient
	private String textoCompleto;

	public Tipologia() {
	}

	public Tipologia(Integer ideTipologia) {
		this.ideTipologia = ideTipologia;
	}

	public Tipologia(Tipologia ideTipologiaPai) {
		this.ideTipologiaPai = ideTipologiaPai;
	}

	public Tipologia(Integer ideTipologia, String codTipologia, String desTipologia, Date dtcCriacao,	boolean indExcluido, boolean indPossuiFilhos) {
		this.ideTipologia = ideTipologia;
		this.codTipologia = codTipologia;
		this.desTipologia = desTipologia;
		this.dtcCriacao = dtcCriacao;
		this.indExcluido = indExcluido;
		this.indPossuiFilhos = indPossuiFilhos;
	}

	public Tipologia(TipologiaEnum tipologiaEnum) {
		this.ideTipologia = tipologiaEnum.getId();
		this.codTipologia = tipologiaEnum.getCodTipologia();
	}
	
	@JSON(include = false)
	public Integer getIdeTipologia() {
		return ideTipologia;
	}

	public void setIdeTipologia(Integer ideTipologia) {
		this.ideTipologia = ideTipologia;
	}

	@JSON(include = false)
	public String getCodTipologia() {
		return codTipologia;
	}

	public void setCodTipologia(String codTipologia) {
		this.codTipologia = codTipologia;
	}

	public String getDesTipologia() {
		return desTipologia;
	}
	
	public String getDesTipologiaNotNull() {
		if(desTipologia == null){
			return " --- " ;
		}
		
		return desTipologia;
	}

	public void setDesTipologia(String desTipologia) {
		this.desTipologia = desTipologia;
	}

	@JSON(include = false)
	public Date getDtcCriacao() {
		return dtcCriacao;
	}

	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}

	@JSON(include = false)
	public Date getDtcExclusao() {
		return dtcExclusao;
	}

	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}

	@JSON(include = false)
	public boolean getIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	@JSON(include = false)
	public boolean getIndPossuiFilhos() {
		return indPossuiFilhos;
	}

	public void setIndPossuiFilhos(boolean indPossuiFilhos) {
		this.indPossuiFilhos = indPossuiFilhos;
	}

	@XmlTransient
	public Collection<Cnae> getCnaeCollection() {
		return cnaeCollection;
	}

	public void setCnaeCollection(Collection<Cnae> cnaeCollection) {
		this.cnaeCollection = cnaeCollection;
	}

	@JSON(include = false)
	public TipologiaGrupo getTipologiaGrupo() {
		return tipologiaGrupo;
	}

	public void setTipologiaGrupo(TipologiaGrupo tipologiaGrupo) {
		this.tipologiaGrupo = tipologiaGrupo;
	}

	@XmlTransient
	public Collection<Tipologia> getTipologiaCollection() {
		return Util.isNullOuVazio(tipologiaCollection) ? tipologiaCollection = new ArrayList<Tipologia>()	: tipologiaCollection;
	}

	public void setTipologiaCollection(Collection<Tipologia> tipologiaCollection) {
		this.tipologiaCollection = tipologiaCollection;
	}

	@JSON(include = false)
	public Tipologia getIdeTipologiaPai() {
		return ideTipologiaPai;
	}

	public void setIdeTipologiaPai(Tipologia ideTipologiaPai) {
		this.ideTipologiaPai = ideTipologiaPai;
	}

	@JSON(include = false)
	public NivelTipologia getIdeNivelTipologia() {
		return ideNivelTipologia;
	}

	public void setIdeNivelTipologia(NivelTipologia ideNivelTipologia) {
		this.ideNivelTipologia = ideNivelTipologia;
	}

	@JSON(include = false)
	@Transient
	public String getDescricaoNo() {

		if (!Util.isNullOuVazio(this.getTipologiaGrupo())) {
			if (!Util.isNullOuVazio(this.codTipologia)){
				String lDescricaoNo = "Divisão " + this.codTipologia.concat(":").concat(this.desTipologia);
				return lDescricaoNo;
			}
			return this.desTipologia;

		} else {

			return this.desTipologia;
		}
	}

	@JSON(include = false)
	public Collection<AtoAmbientalTipologia> getAtoAmbientalTipologiaCollection() {
		return atoAmbientalTipologiaCollection;
	}

	public void setAtoAmbientalTipologiaCollection(Collection<AtoAmbientalTipologia> atoAmbientalTipologiaCollection) {
		this.atoAmbientalTipologiaCollection = atoAmbientalTipologiaCollection;
	}

	@JSON(include = false)
	@Transient
	public String getCodigoDescricaoTipologia() {
		if(!Util.isNullOuVazio(this.codTipologia)) {
			return "Divisão " + this.codTipologia.concat(": ").concat(this.desTipologia);
		} else {
			JsfUtil.addErrorMessage("Erro ao carregar as tipologias.");
			return StringUtils.EMPTY;
		}
	}

	@JSON(include = false)
	public String getValAtividade() {
		return Util.isNull(valAtividade) ? (valAtividade = ""):valAtividade;
	}

	@JSON(include = false)
	public String getValAtividadeFormatada() {
		if(!Util.isNull(valorAtividade)){
			DecimalFormat df = Util.getDecimalFormatPtBr();
			return df.format(valorAtividade);
		} else {
			return null;
		}
	}

	public void setValAtividade(String valAtividade) {
		this.valAtividade = valAtividade;
		if(!Util.isNullOuVazio(valAtividade)) {
			if(valAtividade.contains(",")) {
				this.valorAtividade =  new BigDecimal(valAtividade.replace(".", "").replace(",", "."), new MathContext(0));
			} else {
				this.valorAtividade = new BigDecimal(valAtividade, new MathContext(0));
			}
		}
	}

	@JSON(include = false)
	public BigDecimal getValorAtividade() {
		return valorAtividade;
	}

	public void setValorAtividade(BigDecimal valorAtividade) {
		this.valorAtividade = valorAtividade;
		if(!Util.isNullOuVazio(valorAtividade)) {
			this.valAtividade = valorAtividade.toString();
		}
	}

	@JSON(include = false)
	public Boolean getIndAutorizacao() {
		return indAutorizacao;
	}

	public void setIndAutorizacao(Boolean indAutorizacao) {
		this.indAutorizacao = indAutorizacao;
	}

	@JSON(include = false)
	public Porte getPorte() {
		return porte;
	}

	public void setPorte(Porte porte) {
		this.porte = porte;
	}

	@JSON(include = false)
	public boolean isIndPrincipal() {
		return indPrincipal;
	}

	public void setIndPrincipal(boolean indPrincipal) {
		this.indPrincipal = indPrincipal;
	}

	@JSON(include = false)
	public OpcaoEnum getOpcao() {
		return opcao;
	}

	public void setOpcao(OpcaoEnum opcao) {
		this.opcao = opcao;
	}

	public void setOpcao(Integer opcao) {
		this.opcao = OpcaoEnum.EXCLUSAO.getOpcao(opcao);
	}

	@JSON(include = false)
	public boolean isAtoFlorestal(){
		return "Y".equals(this.codTipologia);
	}

	@JSON(include = false)
	public boolean isOutorga(){
		return "X".equals(this.codTipologia);
	}

	public boolean isCaptacaoSubterranea(){
		return ideTipologia.equals(TipologiaEnum.CAPTACAO_SUBTERRANEA.getId());
	}

	public boolean isCaptacaoSuperficial(){
		return ideTipologia.equals(TipologiaEnum.CAPTACAO_SUPERFICIAL.getId());
	}
	
	public boolean isLancamentoEfluentes(){
		return ideTipologia.equals(TipologiaEnum.LANCAMENTO_EFLUENTES.getId());
	}
	
	public boolean isIntervencao(){
		return ideTipologia.equals(TipologiaEnum.INTERVENCAO_CORPO_HIDRICO.getId());
	}
	
	public boolean isCriacaoFaunaSilvestre(){
		return ideTipologia.equals(TipologiaEnum.CRIACAO_FAUNA_SILVESTRE.getId());
	}
	
    public boolean isTipologiaTransportadoraResiduosPerigosos(){
    	return ideTipologia.equals(TipologiaEnum.TRANSPORTADORA_RESIDUOS_PERIGOSOS.getId());
    }
	
    public boolean isTipologiaEspecial(){
		return ideTipologia.equals(TipologiaEnum.AGRICULTURA_DE_SEQUEIRO.getId())
			|| ideTipologia.equals(TipologiaEnum.AGRICULTURA_IRRIGADA_APE.getId())
			|| ideTipologia.equals(TipologiaEnum.PECUARIA_EXTENSIVA.getId())
			|| ideTipologia.equals(TipologiaEnum.SILVICULTURA_PSS_ATE_200_HA.getId())
		;
	}
	
	@JSON(include = false)
	public boolean isDoEmpreendimento() {
		return doEmpreendimento;
	}

	public void setDoEmpreendimento(boolean doEmpreendimento) {
		this.doEmpreendimento = doEmpreendimento;
	}

	public Integer getOption() {
		return option;
	}

	public void setOption(Integer option) {
		this.option = option;
		setOpcao(option);
	}

	@Override
	public Tipologia clone() throws CloneNotSupportedException {
		return (Tipologia) super.clone();
	}
	

	public Tipologia cloneImpl() {
		
		Tipologia tipologia = new  Tipologia();
		tipologia.setIdeTipologia(ideTipologia);
		
		if(!Util.isNullOuVazio(desTipologia)){
			tipologia.setDesTipologia(desTipologia);
		}
		
		if(!Util.isNullOuVazio(codTipologia)){
			tipologia.setDesTipologia(codTipologia);
		}
		
		return (tipologia) ;
	}
	
	@Transient
	public Tipologia getClone() {
		try {
			return (Tipologia) this.clone();
		} catch (CloneNotSupportedException e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}

		return null;
	}

	@Override
	public int compareTo(Tipologia tipologia) {
		if(Util.isNullOuVazio(tipologia.getIdeTipologia()) || Util.isNullOuVazio(this.getIdeTipologia())){
			return 0;
		} else {
			return this.getIdeTipologia().compareTo(tipologia.getIdeTipologia());
		}
	}

	public Classe getClasse() {
		return classe;
	}

	public void setClasse(Classe classe) {
		this.classe = classe;
	}

	public BigDecimal getValorTaxa() {
		return valorTaxa;
	}

	public void setValorTaxa(BigDecimal valorTaxa) {
		this.valorTaxa = valorTaxa;
	}

	public Collection<SubstanciaMineralTipologia> getSubstanciaMineralTipologiaCollection() {
		return substanciaMineralTipologiaCollection;
	}

	public void setSubstanciaMineralTipologiaCollection(Collection<SubstanciaMineralTipologia> substanciaMineralCollection) {
		this.substanciaMineralTipologiaCollection = substanciaMineralCollection;
	}

	public Collection<ProcessoAto> getProcessoAtoCollection() {
		return processoAtoCollection;
	}

	public void setProcessoAtoCollection(Collection<ProcessoAto> processoAtoCollection) {
		this.processoAtoCollection = processoAtoCollection;
	}

	public Collection<CerhLocalizacaoGeografica> getListaCerhLocalizacaoGeografica() {
		return listaCerhLocalizacaoGeografica;
	}

	public void setListaCerhLocalizacaoGeografica(Collection<CerhLocalizacaoGeografica> listaCerhLocalizacaoGeografica) {
		this.listaCerhLocalizacaoGeografica = listaCerhLocalizacaoGeografica;
	}

	public String getTextoCompleto() {
		return textoCompleto;
	}

	public void setTextoCompleto(String textoCompleto) {
		this.textoCompleto = textoCompleto;
	}

	
}