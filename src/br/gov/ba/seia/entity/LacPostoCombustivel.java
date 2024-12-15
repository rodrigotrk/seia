package br.gov.ba.seia.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.util.Util;

/**
 * 
 * @author luis
 */
@Entity
@Table(name = "lac_posto_combustivel")
@XmlRootElement
@PrimaryKeyJoinColumn(name = "ide_lac_posto_combustivel")
@NamedQueries({
		@NamedQuery(name = "LacPostoCombustivel.findAll", query = "SELECT p FROM LacPostoCombustivel p"),
		@NamedQuery(name = "LacPostoCombustivel.findByIdeLacPostoCombustivel", query = "SELECT p FROM LacPostoCombustivel p WHERE p.ideLac = :ideLacPostoCombustivel"),
		@NamedQuery(name = "LacPostoCombustivel.findByDtcValidadeContratoDistribuidora", query = "SELECT p FROM LacPostoCombustivel p WHERE p.dtcValidadeContratoDistribuidora = :dtcValidadeContratoDistribuidora"),
		@NamedQuery(name = "LacPostoCombustivel.findByIndFlutuante", query = "SELECT p FROM LacPostoCombustivel p WHERE p.indFlutuante = :indFlutuante"),
		@NamedQuery(name = "LacPostoCombustivel.findByQtdAreaTotal", query = "SELECT p FROM LacPostoCombustivel p WHERE p.qtdAreaTotal = :qtdAreaTotal"),
		@NamedQuery(name = "LacPostoCombustivel.findByQtdAreaConstruida", query = "SELECT p FROM LacPostoCombustivel p WHERE p.qtdAreaConstruida = :qtdAreaConstruida"),
		@NamedQuery(name = "LacPostoCombustivel.findByQtdAreaTratamento", query = "SELECT p FROM LacPostoCombustivel p WHERE p.qtdAreaTratamento = :qtdAreaTratamento"),
		@NamedQuery(name = "LacPostoCombustivel.findByQtdAreaAmpliacao", query = "SELECT p FROM LacPostoCombustivel p WHERE p.qtdAreaAmpliacao = :qtdAreaAmpliacao"),
		@NamedQuery(name = "LacPostoCombustivel.findByQtdAreaOutras", query = "SELECT p FROM LacPostoCombustivel p WHERE p.qtdAreaOutras = :qtdAreaOutras"),
		@NamedQuery(name = "LacPostoCombustivel.findByDscDestinoTanquesRemovidos", query = "SELECT p FROM LacPostoCombustivel p WHERE p.dscDestinoTanquesRemovidos = :dscDestinoTanquesRemovidos"),
		@NamedQuery(name = "LacPostoCombustivel.findByIndSistemaControle", query = "SELECT p FROM LacPostoCombustivel p WHERE p.indSistemaControleAutomatico = :indSistemaControleAutomatico"),
		@NamedQuery(name = "LacPostoCombustivel.findByIndAcidente", query = "SELECT p FROM LacPostoCombustivel p WHERE p.indAcidente = :indAcidente"),
		@NamedQuery(name = "LacPostoCombustivel.findByDscOcorrenciaAcidente", query = "SELECT p FROM LacPostoCombustivel p WHERE p.dscOcorrenciaAcidente = :dscOcorrenciaAcidente"),
		@NamedQuery(name = "LacPostoCombustivel.findByQtdLavagemVeiculos", query = "SELECT p FROM LacPostoCombustivel p WHERE p.qtdLavagemVeiculos = :qtdLavagemVeiculos"),
		@NamedQuery(name = "LacPostoCombustivel.findByQtdTrocaOleo", query = "SELECT p FROM LacPostoCombustivel p WHERE p.qtdTrocaOleo = :qtdTrocaOleo"),
		@NamedQuery(name = "LacPostoCombustivel.findByIndAreaIndigena", query = "SELECT p FROM LacPostoCombustivel p WHERE p.indAreaIndigena = :indAreaIndigena"),
		@NamedQuery(name = "LacPostoCombustivel.findByIndSitioArqueologico", query = "SELECT p FROM LacPostoCombustivel p WHERE p.indSitioArqueologico = :indSitioArqueologico"),
		@NamedQuery(name = "LacPostoCombustivel.findByDtcCriacao", query = "SELECT p FROM LacPostoCombustivel p WHERE p.dtcCriacao = :dtcCriacao"),
		@NamedQuery(name = "LacPostoCombustivel.findByIndExcluido", query = "SELECT p FROM LacPostoCombustivel p WHERE p.indExcluido = :indExcluido"),
		@NamedQuery(name = "LacPostoCombustivel.findByDtcExclusao", query = "SELECT p FROM LacPostoCombustivel p WHERE p.dtcExclusao = :dtcExclusao") })
public class LacPostoCombustivel extends Lac {

	private static final long serialVersionUID = 1L;

	@Column(name = "dtc_validade_contrato_distribuidora")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcValidadeContratoDistribuidora;

	@Column(name = "ind_flutuante")
	private Boolean indFlutuante;

	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields
	// consider using these annotations to enforce field validation
	@Column(name = "qtd_area_total")
	private BigDecimal qtdAreaTotal;

	@Column(name = "qtd_area_construida")
	private BigDecimal qtdAreaConstruida;

	@Column(name = "qtd_area_tratamento")
	private BigDecimal qtdAreaTratamento;

	@Column(name = "qtd_area_ampliacao")
	private BigDecimal qtdAreaAmpliacao;

	@Column(name = "qtd_area_outras")
	private BigDecimal qtdAreaOutras;

	@Column(name = "qtd_prof_lencol")
	private BigDecimal profundidadeLencol;

	@Size(max = 20)
	@Column(name = "cod_anp")
	private String codAnp;

	@Size(max = 1000)
	@Column(name = "dsc_destino_tanques_removidos")
	private String dscDestinoTanquesRemovidos;

	@Column(name = "ind_controle_automatico")
	private Boolean indSistemaControleAutomatico;

	@Column(name = "ind_reformado")
	private Boolean indReformado;

	@Column(name = "ind_acidente")
	private Boolean indAcidente;

	@Size(max = 1000)
	@Column(name = "dsc_ocorrencia_acidente")
	private String dscOcorrenciaAcidente;

	@Column(name = "dsc_outros_distribuidora")
	private String dscOutrosDistribuidora;

	@Column(name = "qtd_lavagem_veiculos")
	private Integer qtdLavagemVeiculos;

	@Column(name = "qtd_troca_oleo")
	private Integer qtdTrocaOleo;

	@Column(name = "ind_area_indigena")
	private Boolean indAreaIndigena;

	@Column(name = "ind_sitio_arqueologico")
	private Boolean indSitioArqueologico;

	@Basic(optional = false)
	@Column(name = "dtc_inicio_operacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcInicioOperacao;

	@Basic(optional = false)
	@Column(name = "dtc_criacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcCriacao;

	@Basic(optional = false)
	@Column(name = "ind_excluido")
	private boolean indExcluido;

	@Column(name = "dtc_exclusao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcExclusao;

	@JoinTable(name = "entorno_posto_combustivel", joinColumns = { @JoinColumn(name = "ide_posto_combustivel", referencedColumnName = "ide_lac_posto_combustivel") }, inverseJoinColumns = { @JoinColumn(name = "ide_tipo_equipamento_entorno_posto", referencedColumnName = "ide_tipo_equipamento_entorno_posto") })
	@ManyToMany
	private Collection<TipoEquipamentoEntornoPosto> tipoEquipamentoEntornoPostoCollection;

	@OneToMany(mappedBy = "postoCombustivel")
	private Collection<PostoCombustivelTipoServico> postoCombustivelTipoServicoCollection;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idePostoCombustivel")
	private Collection<SistemaControlePosto> sistemaControlePostoCollection;

	@JoinColumn(name = "ide_tipo_bandeira_posto_combustivel", referencedColumnName = "ide_tipo_bandeira_posto_combustivel")
	@ManyToOne(optional = false)
	private TipoBandeiraPostoCombustivel ideTipoBandeiraPostoCombustivel;

	@JoinColumn(name = "ide_distribuidora_posto", referencedColumnName = "ide_distribuidora_posto")
	@ManyToOne(optional = false)
	private DistribuidoraPosto ideDistribuidoraPosto;

	@JoinColumn(name = "ide_classe_nbr_posto", referencedColumnName = "ide_classe_nbr_posto")
	@ManyToOne(optional = false)
	private ClasseNbrPosto ideClasseNbrPosto;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idePostoCombustivel")
	private Collection<BombaCombustivel> bombaCombustivelCollection;

	@OneToMany(mappedBy = "postoCombustivel")
	private Collection<AreaAbastecimentoPostoCombustivel> areaAbastecimentoPostoCombustivelCollection;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idePostoCombustivel")
	private Collection<PostoCombustivelTanque> postoCombustivelTanqueCollection;

	@OneToMany(mappedBy = "postoCombustivel")
	private Collection<PostoCombustivelProdutoComercializado> postoCombustivelProdutosComercializadosCollection;

	public LacPostoCombustivel() {
	}

	public LacPostoCombustivel(Integer ideLac) {
		super.ideLac = ideLac;
	}

	public LacPostoCombustivel(Integer ideLac, Date dtcCriacao, boolean indExcluido) {
		super.ideLac = ideLac;
		this.dtcCriacao = dtcCriacao;
		this.indExcluido = indExcluido;
	}

	public Date getDtcValidadeContratoDistribuidora() {
		return dtcValidadeContratoDistribuidora;
	}

	public void setDtcValidadeContratoDistribuidora(Date dtcValidadeContratoDistribuidora) {
		this.dtcValidadeContratoDistribuidora = dtcValidadeContratoDistribuidora;
	}

	public Boolean getIndFlutuante() {
		return indFlutuante;
	}

	public void setIndFlutuante(Boolean indFlutuante) {
		this.indFlutuante = indFlutuante;
	}

	public String getCodAnp() {
		return codAnp;
	}

	public void setCodAnp(String codAnp) {
		this.codAnp = codAnp;
	}

	public BigDecimal getQtdAreaTotal() {
		return qtdAreaTotal;
	}

	public void setQtdAreaTotal(BigDecimal qtdAreaTotal) {
		this.qtdAreaTotal = qtdAreaTotal;
	}

	public BigDecimal getQtdAreaConstruida() {
		return qtdAreaConstruida;
	}

	public void setQtdAreaConstruida(BigDecimal qtdAreaConstruida) {
		this.qtdAreaConstruida = qtdAreaConstruida;
	}

	public BigDecimal getQtdAreaTratamento() {
		return qtdAreaTratamento;
	}

	public void setQtdAreaTratamento(BigDecimal qtdAreaTratamento) {
		this.qtdAreaTratamento = qtdAreaTratamento;
	}

	public BigDecimal getQtdAreaAmpliacao() {
		return qtdAreaAmpliacao;
	}

	public void setQtdAreaAmpliacao(BigDecimal qtdAreaAmpliacao) {
		this.qtdAreaAmpliacao = qtdAreaAmpliacao;
	}

	public BigDecimal getQtdAreaOutras() {
		return qtdAreaOutras;
	}

	public void setQtdAreaOutras(BigDecimal qtdAreaOutras) {
		this.qtdAreaOutras = qtdAreaOutras;
	}

	public BigDecimal getProfundidadeLencol() {
		return profundidadeLencol;
	}

	public void setProfundidadeLencol(BigDecimal profundidadeLencol) {
		this.profundidadeLencol = profundidadeLencol;
	}

	public String getDscDestinoTanquesRemovidos() {
		return dscDestinoTanquesRemovidos;
	}

	public void setDscDestinoTanquesRemovidos(String dscDestinoTanquesRemovidos) {
		this.dscDestinoTanquesRemovidos = dscDestinoTanquesRemovidos;
	}

	public Boolean getIndSistemaControleAutomatico() {
		return indSistemaControleAutomatico;
	}

	public void setIndSistemaControleAutomatico(Boolean indSistemaControleAutomatico) {
		this.indSistemaControleAutomatico = indSistemaControleAutomatico;
	}

	public Boolean getIndAcidente() {
		return Util.isNull(indAcidente) ? false : indAcidente;
	}

	public void setIndAcidente(Boolean indAcidente) {
		this.indAcidente = indAcidente;
	}

	public String getDscOcorrenciaAcidente() {
		return dscOcorrenciaAcidente;
	}

	public void setDscOcorrenciaAcidente(String dscOcorrenciaAcidente) {
		this.dscOcorrenciaAcidente = dscOcorrenciaAcidente;
	}

	public Integer getQtdLavagemVeiculos() {
		return qtdLavagemVeiculos;
	}

	public void setQtdLavagemVeiculos(Integer qtdLavagemVeiculos) {
		this.qtdLavagemVeiculos = qtdLavagemVeiculos;
	}

	public Integer getQtdTrocaOleo() {
		return qtdTrocaOleo;
	}

	public void setQtdTrocaOleo(Integer qtdTrocaOleo) {
		this.qtdTrocaOleo = qtdTrocaOleo;
	}

	public Boolean getIndAreaIndigena() {
		return indAreaIndigena;
	}

	public void setIndAreaIndigena(Boolean indAreaIndigena) {
		this.indAreaIndigena = indAreaIndigena;
	}

	public Boolean getIndReformado() {
		return indReformado;
	}

	public void setIndReformado(Boolean indReformado) {
		this.indReformado = indReformado;
	}

	public Boolean getIndSitioArqueologico() {
		return indSitioArqueologico;
	}

	public void setIndSitioArqueologico(Boolean indSitioArqueologico) {
		this.indSitioArqueologico = indSitioArqueologico;
	}

	public Date getDtcCriacao() {
		return dtcCriacao;
	}

	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}

	public boolean getIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	public Date getDtcExclusao() {
		return dtcExclusao;
	}

	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}

	public String getDscOutrosDistribuidora() {
		return dscOutrosDistribuidora;
	}

	public void setDscOutrosDistribuidora(String dscOutrosDistribuidora) {
		this.dscOutrosDistribuidora = dscOutrosDistribuidora;
	}

	@XmlTransient
	public Collection<TipoEquipamentoEntornoPosto> getTipoEquipamentoEntornoPostoCollection() {
		return Util.isNullOuVazio(tipoEquipamentoEntornoPostoCollection) ? tipoEquipamentoEntornoPostoCollection = new ArrayList<TipoEquipamentoEntornoPosto>()
				: tipoEquipamentoEntornoPostoCollection;
	}

	public void setTipoEquipamentoEntornoPostoCollection(Collection<TipoEquipamentoEntornoPosto> tipoEquipamentoEntornoPostoCollection) {
		this.tipoEquipamentoEntornoPostoCollection = tipoEquipamentoEntornoPostoCollection;
	}

	@XmlTransient
	public Collection<SistemaControlePosto> getSistemaControlePostoCollection() {
		return Util.isNullOuVazio(sistemaControlePostoCollection) ? sistemaControlePostoCollection = new ArrayList<SistemaControlePosto>()
				: sistemaControlePostoCollection;
	}

	public void setSistemaControlePostoCollection(Collection<SistemaControlePosto> sistemaControlePostoCollection) {
		this.sistemaControlePostoCollection = sistemaControlePostoCollection;
	}

	public Date getDtcInicioOperacao() {
		return dtcInicioOperacao;
	}

	public void setDtcInicioOperacao(Date dtcInicioOperacao) {
		this.dtcInicioOperacao = dtcInicioOperacao;
	}

	@XmlTransient
	public Collection<PostoCombustivelTipoServico> getPostoCombustivelTipoServicoCollection() {
		return postoCombustivelTipoServicoCollection;
	}

	public void setPostoCombustivelTipoServicoCollection(Collection<PostoCombustivelTipoServico> postoCombustivelTipoServicoCollection) {
		this.postoCombustivelTipoServicoCollection = postoCombustivelTipoServicoCollection;
	}

	public TipoBandeiraPostoCombustivel getIdeTipoBandeiraPostoCombustivel() {
		return ideTipoBandeiraPostoCombustivel;
	}

	public void setIdeTipoBandeiraPostoCombustivel(TipoBandeiraPostoCombustivel ideTipoBandeiraPostoCombustivel) {
		this.ideTipoBandeiraPostoCombustivel = ideTipoBandeiraPostoCombustivel;
	}

	public ClasseNbrPosto getIdeClasseNbrPosto() {
		return ideClasseNbrPosto;
	}

	public void setIdeClasseNbrPosto(ClasseNbrPosto ideClasseNbrPosto) {
		this.ideClasseNbrPosto = ideClasseNbrPosto;
	}

	public DistribuidoraPosto getIdeDistribuidoraPosto() {
		return ideDistribuidoraPosto;
	}

	public void setIdeDistribuidoraPosto(DistribuidoraPosto ideDistribuidoraPosto) {
		this.ideDistribuidoraPosto = ideDistribuidoraPosto;
	}

	@XmlTransient
	public Collection<BombaCombustivel> getBombaCombustivelCollection() {
		return bombaCombustivelCollection;
	}

	public void setBombaCombustivelCollection(Collection<BombaCombustivel> bombaCombustivelCollection) {
		this.bombaCombustivelCollection = bombaCombustivelCollection;
	}

	@XmlTransient
	public Collection<AreaAbastecimentoPostoCombustivel> getAreaAbastecimentoPostoCombustivelCollection() {
		return Util.isNullOuVazio(areaAbastecimentoPostoCombustivelCollection) ? areaAbastecimentoPostoCombustivelCollection = new ArrayList<AreaAbastecimentoPostoCombustivel>()
				: areaAbastecimentoPostoCombustivelCollection;
	}

	public void setAreaAbastecimentoPostoCombustivelCollection(Collection<AreaAbastecimentoPostoCombustivel> areaAbastecimentoPostoCombustivelCollection) {
		this.areaAbastecimentoPostoCombustivelCollection = areaAbastecimentoPostoCombustivelCollection;
	}

	@XmlTransient
	public Collection<PostoCombustivelTanque> getPostoCombustivelTanqueCollection() {
		return Util.isNullOuVazio(postoCombustivelTanqueCollection) ? postoCombustivelTanqueCollection = new ArrayList<PostoCombustivelTanque>()
				: postoCombustivelTanqueCollection;
	}

	public void setPostoCombustivelTanqueCollection(Collection<PostoCombustivelTanque> postoCombustivelTanqueCollection) {
		this.postoCombustivelTanqueCollection = postoCombustivelTanqueCollection;
	}

	@XmlTransient
	public Collection<PostoCombustivelProdutoComercializado> getPostoCombustivelProdutosComercializadosCollection() {
		return Util.isNullOuVazio(postoCombustivelProdutosComercializadosCollection) ? postoCombustivelProdutosComercializadosCollection = new ArrayList<PostoCombustivelProdutoComercializado>()
				: postoCombustivelProdutosComercializadosCollection;
	}

	public void setPostoCombustivelProdutosComercializadosCollection(
			Collection<PostoCombustivelProdutoComercializado> postoCombustivelProdutosComercializadosCollection) {
		this.postoCombustivelProdutosComercializadosCollection = postoCombustivelProdutosComercializadosCollection;
	}

	@Override
	public String toString() {
		return "PostoCombustivel[ ideLacPostoCombustivel=" + super.ideLac + " ]";
	}
	
	public String getCondicionantesFormularioLAC(){
		return 	"O EMPREENDEDOR, juntamente com o RESPONSÁVEL TÉCNICO pelo empreendimento, "
				+ "assumem o compromisso, perante o INEMA, de cumprir rigorosamente a "
				+ "legislação ambiental e atender aos seguintes condicionantes para implantação e operação do "
				+ "empreendimento: \n"
				+

				"1 - Elaborar os projetos de construção, modificação, reforma e ampliação dos empreendimentos "
				+ "de que trata esta Licença por Adesão e Compromisso - LAC em conformidade com as normas "
				+ "técnicas expedidas pela Associação Brasileira de Normas Técnicas (ABNT). \n"
				+

				"2 - Instalar os tanques subterrâneos mantendo distância mínima de 1,50 m do lençol freático. \n"
				+

				"3 - Solicitar previamente ao INEMA, quando for o caso, autorização ambiental para realizar "
				+ "remediação de áreas contaminadas ou desativação de instalações. \n"
				+

				"4 - A remediação das áreas contaminadas em casos de acidentes deverá ser realizada com base "
				+ "no Plano de Remediação para as áreas contaminadas, apresentado previamente ao INEMA, "
				+ "contemplando objetivos, metodologia, resultados esperados e cronograma de implementação "
				+ "das ações. A depender da gravidade e extensão do dano, o INEMA poderá permitir a execução "
				+ "de ações emergenciais de remediação pelo empreendedor, antes da obtenção da autorização "
				+ "ambiental pertinente. \n"
				+

				"5 - Seguir as recomendações da Portaria nº 3.214 do Ministério do Trabalho e Emprego (MTE) – "
				+ "NR 23, relativa aos equipamentos de combate a incêndio. \n"
				+

				"6 - Gerenciar adequadamente o lixo e resíduos sólidos não perigosos gerados, destinando-os "
				+ "a reciclagem ou recolhimento, seja pelo serviço de limpeza pública da localidade ou por meios "
				+ "próprios, para disposição em local devidamente licenciado para este fim. Os resíduos sólidos não "
				+ "poderão, em hipótese alguma, serem queimados a céu aberto ou dispostos diretamente no solo ou "
				+ "em corpos d’água. \n"
				+

				"7 - Escoar completamente e inutilizar as embalagens vazias de produtos automotivos "
				+ "anteriormente à sua reciclagem ou descarte, mediante perfuração e amassamento, de modo a "
				+ "evitar a sua reutilização inadequada. \n"
				+

				"8 - Acondicionar e enviar para tratamento e/ou disposição em instalação devidamente licenciada "
				+ "para este fim, os resíduos perigosos gerados em decorrência das operações de armazenamento e "
				+ "manipulação de produtos combustíveis. \n"
				+

				"9 - Acondicionar o óleo usado proveniente das operações de troca, a borra do separador água/óleo "
				+ "e o óleo resultante do escoamento das embalagens de produtos automotivos, preferencialmente, "
				+ "em tanques subterrâneos. Caso o acondicionamento seja em tambores ou bombonas, armazená-los em área coberta, com piso impermeabilizado, provida de contenção para eventuais "
				+ "vazamentos. \n"
				+

				"10 - Destinar os resíduos a que se refere o item 9 a empresas re-refinadoras devidamente licenciadas pelo órgão ambiental competente, em conformidade com a Resolução CONAMA n° 362/2005. \n"
				+

				"11 - As áreas de lavagem e de lubrificação deverão dispor de piso de concreto impermeabilizado "
				+ "provido de sistema de drenagem para coleta de seus efluentes líquidos, devidamente direcionado "
				+ "para o sistema separador de água/óleo. \n" +

				"12 - Dar destinação adequada aos efluentes provenientes dos Sistemas de Separação Água/Óleo "
				+ "(SAO), lançando-os na rede pública de esgotos, após tratamento para adequação aos padrões de "
				+ "lançamento estabelecidos pelo CONAMA, ou em corpo hídrico, desde que obtida a devida outorga " + "ou sua dispensa, emitida pelo INEMA. \n" +

				"13 - Segregar os esgotos sanitários dos demais efluentes gerados pelo empreendimento "
				+ "lançando-os em rede pública coletora ou, no caso de inexistência da mesma, dar tratamento "
				+ "adequado e lançar em local apropriado, em conformidade com as normas técnicas e ambientais "
				+ "vigentes relativas a esgotamento sanitário, poluição do solo e dos corpos hídricos. \n" +

				"14 - Sistemas com Tanques Subterrâneos \n" +

				"14.1 - Implantar e operar os Sistemas de Abastecimento com Tanques Subterrâneos de "
				+ "Combustível (SASC) conforme as normas e critérios estabelecidos na NBR 13.786 da ABNT "
				+ "(Posto de Serviço – Seleção dos Equipamentos para Sistemas para Instalações Subterrâneas " + "de Combustíveis). \n" +

				"14.2 - Dotar as áreas de abastecimento e descarga dos sistemas com tanques subterrâneos "
				+ "de piso de concreto impermeabilizado e canaletas para coleta dos efluentes líquidos, os quais "
				+ "deverão ser direcionados para o Sistema de Separação Água/Óleo (SAO); \n" +

				"14.3 - Instalar os respiros dos tanques subterrâneos de armazenamento de combustíveis em "
				+ "conformidade com as normas técnicas da ABNT e manter a distância mínima horizontal de 3 m "
				+ "(três metros) entre estes e quaisquer edificações. \n" +

				"14.4 - Efetuar teste de estanqueidade nos tanques subterrâneos, inclusive tanques de óleo "
				+ "queimado, tubulações e conexões, em conformidade com a NBR 13.784 da ABNT (Detecção de "
				+ "Vazamento em Postos de Serviço), com a seguinte periodicidade: \n" +

				"• Tanque de parede simples – a cada 2 anos \n" + "• Tanque de parede dupla – a cada 3 anos \n"
				+ "• Tanque de parede dupla com monitoramento intersticial contínuo – a cada 5 anos \n" +

				"14.5 - Interditar imediatamente a operação dos tanques subterrâneos que acusarem vazamento "
				+ "após o teste de estanqueidade. As operações de retirada e destinação dos tanques deverão "
				+ "ser realizadas de acordo com a NBR 14.973 (Posto de serviço - Remoção e Destinação de "
				+ "Tanques Subterrâneos Usados), da ABNT, devendo a sua destinação final estar de acordo com " + "as normas ambientais vigentes. \n" +

				"14.6 - Realizar investigação prévia de contaminação do solo e lençol freático, quando da "
				+ "operações de troca de tanques ou tubulações, e encaminhar os resultados ao INEMA. \n" +

				"14.7 - Não utilizar tanques recuperados em instalações subterrâneas (SASCs), mesmo que " + "jaquetados. \n" +

				"14.8 - Não utilizar tanques subterrâneos de parede simples sem revestimento externo. Os "
				+ "empreendimentos que ainda possuem este tipo de equipamento devem substituí-los por tanque "
				+ "de parede dupla (jaquetados), no prazo de até 2 anos. \n" +

				"15 - Instalar os Sistemas de Abastecimento de Gás Natural (GNV/GNC) em conformidade com "
				+ "as recomendações da NBR 12.236 da ABNT (Critérios de Projeto, Montagem e Operação de "
				+ "Postos de Gás Combustível Comprimido) respeitando as distâncias e afastamentos entre prédios "
				+ "linhas-limite, áreas de estocagem e unidades de abastecimento, conforme estabelecido na citada " + "norma. \n" +

				"16 - Sistemas de Armazenamento Aéreo \n" +

				"16.1 - Implantar e operar os Sistemas de Armazenamento Aéreo de Combustíveis em "
				+ "conformidade com a NBR 7.505 da ABNT (Armazenagem de Líquidos Inflamáveis e " + "Combustíveis). \n" +

				"16.2 - Dotar a área onde se localizam as bombas de transferência de produto, assim "
				+ "como a descarga dos caminhões, de piso impermeabilizado e muretas de contenção, cuja "
				+ "drenagem deverá ser direcionada para caixa separadora água/óleo, ou outra alternativa de "
				+ "tratamento, de acordo com a legislação ambiental e normas técnicas da ABNT. \n" +

				"16.3 - No caso de descarga por meio da transferência de produto para tanque subterrâneo "
				+ "intermediário, obedecer às exigências inerentes a este tipo de equipamento, estabelecidas " + "na NBR 13.786 da ABNT. \n" +

				"16.4 - Prover o armazenamento aéreo de combustível de sistema de proteção de "
				+ "segurança antiabalroamento ou válvula de proteção, para proteger contra o abalroamento "
				+ "nas unidades de abastecimento ligadas a reservatório de combustível instalado no nível da " + "pista. \n" +

				"16.5 - Efetuar ensaio hidrostático nos tanques, inclusive os tanques de óleo queimado, "
				+ "tubulações e conexões conforme recomendação da NBR 7.821 da ABNT (Tanques Soldados "
				+ "para Armazenamento de Petróleo e Derivados), com a seguinte periodicidade: \n" +

				"Situação normal de operação – a cada 8 anos \n" + "Situação severa de operação – a cada 5 anos \n" +

				"16.6 - Interditar imediatamente a operação dos tanques que acusarem vazamentos, após "
				+ "inspeção visual ou ensaio hidrostático. Após serem esvaziados, drenados, desgaseificado "
				+ "e limpos, os tanques deverão ser inspecionados para verificação da necessidade de sua " + "reparação ou substituição. \n" +

				"17 - Comunicar imediatamente ao INEMA a ocorrência de quaisquer vazamentos ou acidentes "
				+ "responsabilizando-se pela adoção de medidas para controle da situação emergencial e para o "
				+ "saneamento das áreas impactadas, de acordo com as exigências formuladas pelo INEMA.\n" +

				"18 - Promover o treinamento dos empregados, visando orientar a adoção de medidas de  "
				+ "prevenção de acidentes e ações cabíveis imediatas para controle de situações de emergências de risco. \n" +

				"19 - Investigar as causas e tomar providências imediatas para eliminação da fonte ativa de "
				+ "contaminação, nos casos de ocorrência de vazamento ou acidentes com derramamento de " + "combustíveis para o solo. \n" +

				"20 - Respeitar as Áreas de Preservação Permanente (APPs) existentes na área de "
				+ "empreendimento, conforme definidas em legislação específica, mantendo as distâncias mínimas "
				+ "legais em relação a qualquer ocupação nestas áreas.\n" +

				"21 - Indenizar ou reparar os danos causados pelo empreendimento ao meio ambiente "
				+ "independentemente da existência de culpa, conforme previsto na Constituição Federal e Estadual "
				+ "bem como nos demais instrumentos legais e normativos aplicáveis.\n" +

				"22 - Fazer com que seus prepostos, funcionários e outros sob sua responsabilidade cumpram o " + "estabelecido nesta LAC.\n" +

				"23 - Atualizar esta LAC, junto ao INEMA, previamente a quaisquer alterações que impliquem "
				+ "em reforma de equipamentos, ampliação das instalações ou dos serviços oferecidos pelo empreendimento.";
	}
}
