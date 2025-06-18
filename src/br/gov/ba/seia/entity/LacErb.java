package br.gov.ba.seia.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.util.Util;

@Entity
@Table(name = "lac_erb", uniqueConstraints = { @UniqueConstraint(columnNames = { "ide_lac_erb" }) })
@XmlRootElement
@PrimaryKeyJoinColumn(name = "ide_lac_erb")
@NamedQueries({
		@NamedQuery(name = "LacErb.findAll", query = "SELECT l FROM LacErb l"),
		@NamedQuery(name = "LacErb.findByIdeLacErb", query = "SELECT l FROM LacErb l WHERE l.ideLac = :ideLacErb"),
		@NamedQuery(name = "LacErb.findByDscNomeErb", query = "SELECT l FROM LacErb l WHERE l.dscNomeErb = :dscNomeErb"),
		@NamedQuery(name = "LacErb.findByCodErb", query = "SELECT l FROM LacErb l WHERE l.codErb = :codErb"),
		@NamedQuery(name = "LacErb.findByVlrAreaTotalTerreno", query = "SELECT l FROM LacErb l WHERE l.vlrAreaTotalTerreno = :vlrAreaTotalTerreno"),
		@NamedQuery(name = "LacErb.findByVlrAlturaTorre", query = "SELECT l FROM LacErb l WHERE l.vlrAlturaTorre = :vlrAlturaTorre"),
		@NamedQuery(name = "LacErb.findByVlrAlturaAntenaBase", query = "SELECT l FROM LacErb l WHERE l.vlrAlturaAntenaBase = :vlrAlturaAntenaBase"),
		@NamedQuery(name = "LacErb.findByVlrMenorDistLimite", query = "SELECT l FROM LacErb l WHERE l.vlrMenorDistLimite = :vlrMenorDistLimite"),
		@NamedQuery(name = "LacErb.findByDscNomeEdificacao", query = "SELECT l FROM LacErb l WHERE l.dscNomeEdificacao = :dscNomeEdificacao"),
		@NamedQuery(name = "LacErb.findByVlrAlturaEdificacao", query = "SELECT l FROM LacErb l WHERE l.vlrAlturaEdificacao = :vlrAlturaEdificacao"),
		@NamedQuery(name = "LacErb.findByVlrAlturaMaior", query = "SELECT l FROM LacErb l WHERE l.vlrAlturaMaior = :vlrAlturaMaior"),
		@NamedQuery(name = "LacErb.findByVlrAlturaMenor", query = "SELECT l FROM LacErb l WHERE l.vlrAlturaMenor = :vlrAlturaMenor"),
		@NamedQuery(name = "LacErb.findByVlrAlturaAntenaEdificacao", query = "SELECT l FROM LacErb l WHERE l.vlrAlturaAntenaEdificacao = :vlrAlturaAntenaEdificacao"),
		@NamedQuery(name = "LacErb.findByDscOutrosTipoDelimitacao", query = "SELECT l FROM LacErb l WHERE l.dscOutrosTipoDelimitacao = :dscOutrosTipoDelimitacao"),
		@NamedQuery(name = "LacErb.findByDscNomeEstabelecimento", query = "SELECT l FROM LacErb l WHERE l.dscNomeEstabelecimento = :dscNomeEstabelecimento"),
		@NamedQuery(name = "LacErb.findByDscAtividadeEstabelecimento", query = "SELECT l FROM LacErb l WHERE l.dscAtividadeEstabelecimento = :dscAtividadeEstabelecimento"),
		@NamedQuery(name = "LacErb.findByVlrPotenciaTransmissor", query = "SELECT l FROM LacErb l WHERE l.vlrPotenciaTransmissor = :vlrPotenciaTransmissor"),
		@NamedQuery(name = "LacErb.findByVlrFrequenciaUtilizada", query = "SELECT l FROM LacErb l WHERE l.vlrFrequenciaUtilizada = :vlrFrequenciaUtilizada") })
public class LacErb extends Lac {
	
	private static final long serialVersionUID = 1L;
	
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 150)
	@Column(name = "dsc_nome_erb", nullable = false, length = 150)
	private String dscNomeErb;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 50)
	@Column(name = "cod_erb", nullable = false, length = 50)
	private String codErb;
	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields
	// consider using these annotations to enforce field validation
	@Column(name = "vlr_area_total_terreno", precision = 10, scale = 2)
	private BigDecimal vlrAreaTotalTerreno;
	@Column(name = "vlr_altura_torre", precision = 10, scale = 2)
	private BigDecimal vlrAlturaTorre;
	@Basic(optional = false)
	@Column(name = "vlr_azimute_antena_base")
	private Integer vlrAzimuteAntenaBase;
	@Column(name = "vlr_altura_antena_base", precision = 10, scale = 2)
	private BigDecimal vlrAlturaAntenaBase;
	@Column(name = "vlr_menor_dist_limite", precision = 10, scale = 2)
	private BigDecimal vlrMenorDistLimite;
	@Basic(optional = false)
	@NotNull
	@Column(name = "ind_compartilhado", nullable = false)
	private Boolean indCompartilhado;
	@Size(max = 150)
	@Column(name = "dsc_nome_edificacao", length = 150)
	private String dscNomeEdificacao;
	@Column(name = "vlr_altura_edificacao", precision = 10, scale = 2)
	private BigDecimal vlrAlturaEdificacao;
	@Column(name = "vlr_altura_maior", precision = 10, scale = 2)
	private BigDecimal vlrAlturaMaior;
	@Column(name = "vlr_altura_menor", precision = 10, scale = 2)
	private BigDecimal vlrAlturaMenor;
	@Column(name = "vlr_altura_antena_edificacao", precision = 10, scale = 2)
	private BigDecimal vlrAlturaAntenaEdificacao;
	@Size(max = 200)
	@Column(name = "dsc_outros_tipo_delimitacao", length = 200)
	private String dscOutrosTipoDelimitacao;
	@Size(max = 150)
	@Column(name = "dsc_nome_estabelecimento", length = 150)
	private String dscNomeEstabelecimento;
	@Size(max = 2147483647)
	@Column(name = "dsc_atividade_estabelecimento", length = 500)
	private String dscAtividadeEstabelecimento;
	@Basic(optional = false)
	@NotNull
	@Column(name = "vlr_potencia_transmissor", nullable = false, precision = 10, scale = 2)
	private BigDecimal vlrPotenciaTransmissor;
	@Basic(optional = false)
	@NotNull
	@Column(name = "vlr_frequencia_utilizada", nullable = false, precision = 10, scale = 2)
	private BigDecimal vlrFrequenciaUtilizada;
	
	@JoinTable(name = "lac_erb_tipo_delimitacao", joinColumns = { @JoinColumn(name = "ide_lac_erb", referencedColumnName = "ide_lac_erb", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "ide_tipo_delimitacao_terreno", referencedColumnName = "ide_tipo_delimitacao_terreno", nullable = false) })
	@ManyToMany(fetch = FetchType.LAZY)
	private Collection<TipoDelimitacaoTerreno> tipoDelimitacaoTerrenoCollection;
	
	@JoinColumn(name = "ide_tipo_modalidade_erb", referencedColumnName = "ide_tipo_modalidade_erb", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private TipoModalidadeErb ideTipoModalidadeErb;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "lacErb", fetch = FetchType.LAZY)
	private Collection<LacErbEquipamento> lacErbEquipamentoCollection;

	public LacErb() {
	}

	public LacErb(Integer ideLacErb) {
		super.ideLac = ideLacErb;
	}

	public LacErb(Integer ideLacErb, String dscNomeErb, String codErb, BigDecimal vlrPotenciaTransmissor, BigDecimal vlrFrequenciaUtilizada) {
		super.ideLac = ideLacErb;
		this.dscNomeErb = dscNomeErb;
		this.codErb = codErb;
		this.vlrPotenciaTransmissor = vlrPotenciaTransmissor;
		this.vlrFrequenciaUtilizada = vlrFrequenciaUtilizada;
	}

	public String getDscNomeErb() {
		return dscNomeErb;
	}

	public void setDscNomeErb(String dscNomeErb) {
		this.dscNomeErb = dscNomeErb;
	}

	public String getCodErb() {
		return codErb;
	}

	public void setCodErb(String codErb) {
		this.codErb = codErb;
	}

	public BigDecimal getVlrAreaTotalTerreno() {
		return vlrAreaTotalTerreno;
	}

	public void setVlrAreaTotalTerreno(BigDecimal vlrAreaTotalTerreno) {
		this.vlrAreaTotalTerreno = vlrAreaTotalTerreno;
	}

	public BigDecimal getVlrAlturaTorre() {
		return vlrAlturaTorre;
	}

	public void setVlrAlturaTorre(BigDecimal vlrAlturaTorre) {
		this.vlrAlturaTorre = vlrAlturaTorre;
	}

	public Integer getVlrAzimuteAntenaBase() {
		return vlrAzimuteAntenaBase;
	}

	public void setVlrAzimuteAntenaBase(Integer vlrAzimuteAntenaBase) {
		this.vlrAzimuteAntenaBase = vlrAzimuteAntenaBase;
	}

	public BigDecimal getVlrAlturaAntenaBase() {
		return vlrAlturaAntenaBase;
	}

	public void setVlrAlturaAntenaBase(BigDecimal vlrAlturaAntenaBase) {
		this.vlrAlturaAntenaBase = vlrAlturaAntenaBase;
	}

	public BigDecimal getVlrMenorDistLimite() {
		return vlrMenorDistLimite;
	}

	public void setVlrMenorDistLimite(BigDecimal vlrMenorDistLimite) {
		this.vlrMenorDistLimite = vlrMenorDistLimite;
	}

	public Boolean getIndCompartilhado() {
		return Util.isNull(indCompartilhado) ? false : indCompartilhado;
	}

	public void setIndCompartilhado(Boolean indCompartilhado) {
		this.indCompartilhado = indCompartilhado;
	}

	public String getDscNomeEdificacao() {
		return dscNomeEdificacao;
	}

	public void setDscNomeEdificacao(String dscNomeEdificacao) {
		this.dscNomeEdificacao = dscNomeEdificacao;
	}

	public BigDecimal getVlrAlturaEdificacao() {
		return vlrAlturaEdificacao;
	}

	public void setVlrAlturaEdificacao(BigDecimal vlrAlturaEdificacao) {
		this.vlrAlturaEdificacao = vlrAlturaEdificacao;
	}

	public BigDecimal getVlrAlturaMaior() {
		return vlrAlturaMaior;
	}

	public void setVlrAlturaMaior(BigDecimal vlrAlturaMaior) {
		this.vlrAlturaMaior = vlrAlturaMaior;
	}

	public BigDecimal getVlrAlturaMenor() {
		return vlrAlturaMenor;
	}

	public void setVlrAlturaMenor(BigDecimal vlrAlturaMenor) {
		this.vlrAlturaMenor = vlrAlturaMenor;
	}

	public BigDecimal getVlrAlturaAntenaEdificacao() {
		return vlrAlturaAntenaEdificacao;
	}

	public void setVlrAlturaAntenaEdificacao(BigDecimal vlrAlturaAntenaEdificacao) {
		this.vlrAlturaAntenaEdificacao = vlrAlturaAntenaEdificacao;
	}

	public String getDscOutrosTipoDelimitacao() {
		return dscOutrosTipoDelimitacao;
	}

	public void setDscOutrosTipoDelimitacao(String dscOutrosTipoDelimitacao) {
		this.dscOutrosTipoDelimitacao = dscOutrosTipoDelimitacao;
	}

	public String getDscNomeEstabelecimento() {
		return dscNomeEstabelecimento;
	}

	public void setDscNomeEstabelecimento(String dscNomeEstabelecimento) {
		this.dscNomeEstabelecimento = dscNomeEstabelecimento;
	}

	public String getDscAtividadeEstabelecimento() {
		return dscAtividadeEstabelecimento;
	}

	public void setDscAtividadeEstabelecimento(String dscAtividadeEstabelecimento) {
		this.dscAtividadeEstabelecimento = dscAtividadeEstabelecimento;
	}

	public BigDecimal getVlrPotenciaTransmissor() {
		return vlrPotenciaTransmissor;
	}

	public void setVlrPotenciaTransmissor(BigDecimal vlrPotenciaTransmissor) {
		this.vlrPotenciaTransmissor = vlrPotenciaTransmissor;
	}

	public BigDecimal getVlrFrequenciaUtilizada() {
		return vlrFrequenciaUtilizada;
	}

	public void setVlrFrequenciaUtilizada(BigDecimal vlrFrequenciaUtilizada) {
		this.vlrFrequenciaUtilizada = vlrFrequenciaUtilizada;
	}

	@XmlTransient
	public Collection<TipoDelimitacaoTerreno> getTipoDelimitacaoTerrenoCollection() {
		return Util.isNull(tipoDelimitacaoTerrenoCollection) ? tipoDelimitacaoTerrenoCollection = new ArrayList<TipoDelimitacaoTerreno>()
				: tipoDelimitacaoTerrenoCollection;
	}

	public void setTipoDelimitacaoTerrenoCollection(Collection<TipoDelimitacaoTerreno> tipoDelimitacaoTerrenoCollection) {
		this.tipoDelimitacaoTerrenoCollection = tipoDelimitacaoTerrenoCollection;
	}

	public TipoModalidadeErb getIdeTipoModalidadeErb() {
		return ideTipoModalidadeErb;
	}

	public void setIdeTipoModalidadeErb(TipoModalidadeErb ideTipoModalidadeErb) {
		this.ideTipoModalidadeErb = ideTipoModalidadeErb;
	}

	public Requerimento getIdeRequerimento() {
		return Util.isNull(ideRequerimento) ? ideRequerimento = new Requerimento() : ideRequerimento;
	}

	public void setIdeRequerimento(Requerimento ideRequerimento) {
		this.ideRequerimento = ideRequerimento;
	}

	@XmlTransient
	public Collection<LacLegislacao> getLacErbLegislacaoCollection() {
		return lacLegislacaoCollection;
	}

	public void setLacErbLegislacaoCollection(Collection<LacLegislacao> lacErbLegislacaoCollection) {
		this.lacLegislacaoCollection = lacErbLegislacaoCollection;
	}

	@XmlTransient
	public Collection<LacErbEquipamento> getLacErbEquipamentoCollection() {
		return lacErbEquipamentoCollection;
	}

	public void setLacErbEquipamentoCollection(Collection<LacErbEquipamento> lacErbEquipamentoCollection) {
		this.lacErbEquipamentoCollection = lacErbEquipamentoCollection;
	}

	public DocumentoObrigatorio getIdeDocumentoObrigatorio() {
		return ideDocumentoObrigatorio;
	}

	public void setIdeDocumentoObrigatorio(DocumentoObrigatorio ideDocumentoObrigatorio) {
		this.ideDocumentoObrigatorio = ideDocumentoObrigatorio;
	}
	
	public String getCondicionantesFormularioLAC() {
		return "Condicionantes pertinentes a atividade descrita: \n"
				+ "1 - Antenas instaladas em torres, postes ou similares e sobre edificações:\n"
				+ "1.1 - em hospitais, creches, escolas, centros comerciais e clínicas médicas que utilizam equipamentos suscetíveis a interferências eletromagnéticas, o nível de radiação não poderá ultrapassar os seguintes valores: 1,94 V/m ou 0,01 W/m²;\n"
				+ "1.2 - em qualquer unidade habitacional, o nível de radiação não poderá ultrapassar os seguintes valores: 9,0 V/m ou 0,21 W/m²;\n"
				+ "1.3 - a instalação de estações rádio-base e equipamentos de telefonia sem fio deve respeitar a distância mínima de 02 (dois) metros, medidos do ponto mais próximo do pé da torre, poste ou similar, até qualquer limite do terreno ou unidade habitável;\n"
				+ "1.4 - a empresa responsável pelo serviço de telefonia deverá fornecer aos responsáveis pelo imóvel, material informativo (cartilhas/cartazes/panfletos, etc.) sobre o perigo da permanência de pessoas nas proximidades da antena.\n"
				+ "2 - Antenas instaladas internamente (indoor):\n"
				+ "2.1 - no interior das edificações que abrigam centros de saúde, centros comerciais, clínicas médicas que utilizam equipamentos suscetíveis a radiações eletromagnéticas, escolas e creches, o nível de radiação não poderá ultrapassar os seguintes valores: 1,94 V/m ou 0,01 W/m²;\n"
				+ "2.2 - no interior de qualquer outra edificação, o nível de radiação não poderá ultrapassar os seguintes valores: 9,0 V/m ou 0,21 W/m²;\n"
				+ "2.3 - as antenas devem ser instaladas fora do alcance do público não devendo o nível máximo de radiação em relação a este ultrapassar os valores especificados. Recomendando-se o uso de cabos irradiantes.\n"
				+ "2.4 - a empresa responsável pelo serviço de telefonia deverá fornecer à administração da edificação, material informativo sobre o perigo da permanência de pessoas nas proximidades da antena.\n"
				+ "3 - As torres e/ou antenas devem ter uma área de proteção delimitada de forma a impedir o acesso de pessoas e animais, devidamente sinalizada, com advertência de exposição à radiação eletromagnética, informando as distâncias de afastamento mínimo recomendadas e os números de telefones gratuitos para contato com a operadora, a Agência Nacional de Telecomunicação (ANATEL) e o INEMA.\n"
				+ "4 - A ERB deverá dispor de sistema de proteção contra as descargas atmosféricas, conforme a NBR 5.419 e suas revisões.\n"
				+ "5 - As medições para avaliação das radiações devem ser realizadas dentro da faixa de 100 KHz a 3 GHz. Caso o valor medido esteja acima do especificado deverão ser tomadas medidas para adequação á faixa de operação utilizada pela empresa.\n"
				+ "6 - Respeitar as áreas de Preservação Permanente (APPs) existentes na área do empreendimento, conforme definidas em legislação específica, mantendo as distãncias mínimas legais em relação a qualquer ocupação nestas áreas;\n"
				+ "7 - Desenvolver programa de informação para a comunidade local, previamente é implantação da ERB, divulgando informações sobre os possíveis efeitos da atividade, de forma a esclarecer a população quanto a exposição à radiação eletromagnética, mediante palestras, distribuição de folhetos ou outros instrumentos de comunicaçã contendo, no mínimo: informações sobre a estação, perigos de permanência de pessoas nas proximidades das antenas e número telefônico para que a comunidade possa dirimir suas dúvidas.\n"
				+ "8 - Indenizar ou reparar os danos causados ao meio ambiente causados pelo empreendimento, independentemente da existência de culpa, conforme previsto na Constituição Federal e Estadual, bem como nos demais instrumentos legais e normativos aplicáveis à espécie.\n"
				+ "9 - Fazer com que seus prepostos, funcionários e outros sob sua responsabilidade cumpram o estabelecido neste documento.\n"
				+ "10 - Permitir livre acesso à área do empreendimento, a qualquer tempo, aos funcionários do INEMA, no exercício das suas funções de vistoria e fiscalização, devendo disponibilizar, quando requerido, os documentos relativos à regularidade ambiental do empreendimento.\n"
				+ "11 - Atualizar a Licença por Adesão, junto ao INEMA, previamente a quaisquer alterações, seja na posição das antenas instaladas e/ou aumento da potência do transmissor do empreendimento, com conseqüente alteração dos níveis de radiação emitidos, bem como nos casos de compartilhamento da infraestrutura da ERB por outra operadora ou alteração de titularidade.\n";
	}
	
	@Override
	public String toString() {
		return "br.gov.ba.seia.entity.LacErb[ ideLacErb=" + ideLac + " ]";
	}

}
