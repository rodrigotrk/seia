package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.enumerator.MetodoProspeccaoEnum;


/**
 * @author eduardo.fernandes
 * @since 03/11/2016
 * @see <a href="http://10.105.17.77/redmine/issues/">#8187</a>
 */
@Entity
@Table(name="prospecao_detalhamento")
@NamedQueries({
		@NamedQuery(name = "ProspecaoDetalhamento.removeByIdeProcessoDnpmProspecao",
				query = "DELETE FROM ProspecaoDetalhamento p WHERE p.processoDnpmProspecao.ideProcessoDnpmProspecao = :ideProcessoDnpmProspecao"),
		@NamedQuery(name = "ProspecaoDetalhamento.removeByIdeProcessoDnpm",
				query = "DELETE FROM ProspecaoDetalhamento p WHERE p.processoDnpmProspecao.ideProcessoDnpmProspecao IN (SELECT pdp.ideProcessoDnpmProspecao FROM ProcessoDnpmProspecao pdp WHERE pdp.processoDnpm.ideProcessoDnpm = :ideProcessoDnpm)"),
		@NamedQuery(name = "ProspecaoDetalhamento.removeByIde",
				query = "DELETE FROM ProspecaoDetalhamento p WHERE p.ideProspeccaoDetalhamento = :ideProspeccaoDetalhamento")
})
public class ProspecaoDetalhamento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "PROSPECAO_DETALHAMENTO_GENERATOR", sequenceName = "PROSPECAO_DETALHAMENTO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROSPECAO_DETALHAMENTO_GENERATOR")
	@Column(name="ide_prospeccao_detalhamento")
	private Integer ideProspeccaoDetalhamento;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_localizacao_geografica", referencedColumnName = "ide_localizacao_geografica")
	private LocalizacaoGeografica ideLocalizacaoGeografica;

	@ManyToOne(optional = false, fetch=FetchType.LAZY)
	@JoinColumn(name="ide_processo_dnpm_prospecao")
	private ProcessoDnpmProspecao processoDnpmProspecao;
	
	@Column(name="nom_identificacao_alvo")
	private String nomIdentificacaoAlvo;

	@Column(name = "nom_identificacao")
	private String nomIdentificacao;

	@Column(name="nom_imovel_rural")
	private String nomImovelRural;

	@Column(name="num_area_praca")
	private BigDecimal numAreaPraca;

	@Column(name="num_comprimento")
	private BigDecimal numComprimento;

	@Column(name="num_diametro")
	private BigDecimal numDiametro;

	@Column(name="num_largura")
	private BigDecimal numLargura;

	@Column(name="num_profundidade")
	private BigDecimal numProfundidade;

	public ProspecaoDetalhamento() {
	}

	public ProspecaoDetalhamento(ProcessoDnpm processoDnpm, MetodoProspeccaoEnum metodoProspeccaoEnum) {
		this.processoDnpmProspecao = processoDnpm.getProcessoDnpmProspecao(metodoProspeccaoEnum);
		this.processoDnpmProspecao.setProcessoDnpm(processoDnpm);
		this.ideLocalizacaoGeografica = new LocalizacaoGeografica();
	}

	public Integer getIdeProspeccaoDetalhamento() {
		return this.ideProspeccaoDetalhamento;
	}

	public void setIdeProspeccaoDetalhamento(Integer ideProspeccaoDetalhamento) {
		this.ideProspeccaoDetalhamento = ideProspeccaoDetalhamento;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return this.ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public String getNomIdentificacaoAlvo() {
		return this.nomIdentificacaoAlvo;
	}

	public void setNomIdentificacaoAlvo(String nomIdentificacaoAlvo) {
		this.nomIdentificacaoAlvo = nomIdentificacaoAlvo;
	}

	public String getNomIdentificacao() {
		return this.nomIdentificacao;
	}

	public void setNomIdentificacao(String nomIdentiificacao) {
		this.nomIdentificacao = nomIdentiificacao;
	}

	public String getNomImovelRural() {
		return this.nomImovelRural;
	}

	public void setNomImovelRural(String nomImovelRural) {
		this.nomImovelRural = nomImovelRural;
	}

	public BigDecimal getNumAreaPraca() {
		return this.numAreaPraca;
	}

	public void setNumAreaPraca(BigDecimal numAreaPraca) {
		this.numAreaPraca = numAreaPraca;
	}

	public BigDecimal getNumComprimento() {
		return this.numComprimento;
	}

	public void setNumComprimento(BigDecimal numComprimento) {
		this.numComprimento = numComprimento;
	}

	public BigDecimal getNumDiametro() {
		return this.numDiametro;
	}

	public void setNumDiametro(BigDecimal numDiametro) {
		this.numDiametro = numDiametro;
	}

	public BigDecimal getNumLargura() {
		return this.numLargura;
	}

	public void setNumLargura(BigDecimal numLargura) {
		this.numLargura = numLargura;
	}

	public BigDecimal getNumProfundidade() {
		return this.numProfundidade;
	}

	public void setNumProfundidade(BigDecimal numProfundidade) {
		this.numProfundidade = numProfundidade;
	}

	public ProcessoDnpmProspecao getProcessoDnpmProspecao() {
		return this.processoDnpmProspecao;
	}

	public void setProcessoDnpmProspecao(ProcessoDnpmProspecao processoDnpmProspecao) {
		this.processoDnpmProspecao = processoDnpmProspecao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideLocalizacaoGeografica == null) ? 0 : ideLocalizacaoGeografica.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProspecaoDetalhamento other = (ProspecaoDetalhamento) obj;
		if (ideLocalizacaoGeografica == null) {
			if (other.ideLocalizacaoGeografica != null)
				return false;
		}
		else if (!ideLocalizacaoGeografica.equals(other.ideLocalizacaoGeografica))
			return false;
		return true;
	}
}