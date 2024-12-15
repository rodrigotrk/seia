package br.gov.ba.seia.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name="projeto_acao")
@NamedQuery(name="ProjetoAcao.findAll", query="SELECT p FROM ProjetoAcao p")
public class ProjetoAcao extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PROJETO_ACAO_IDEPROJETOACAO_GENERATOR", sequenceName="PROJETO_ACAO_IDE_PROJETO_ACAO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PROJETO_ACAO_IDEPROJETOACAO_GENERATOR")
	@Column(name="ide_projeto_acao", updatable=false, unique=true, nullable=false)
	private Integer ideProjetoAcao;

	@Column(name="ind_excluido", nullable=false)
	private Boolean indExcluido;

	@Column(name="nom_acao", nullable=false, length=200)
	private String nomAcao;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tcca_projeto", nullable=false)
	private TccaProjeto ideTccaProjeto;

	@OneToMany(mappedBy="ideProjetoAcao")
	private List<ProjetoAcaoProduto> projetoAcaoProdutoCollection;
	
	@Transient
	private BigDecimal valorPrevisto;

	@Transient
	private BigDecimal valorContratado;

	@Transient
	private BigDecimal valorExecutado;

	@Transient
	private BigDecimal valorRemanejado;
	
	@Transient
	private BigDecimal valorSuplementado;
	
	@Transient
	private BigDecimal valorSaldo;	

	
	public ProjetoAcao() {
	}

	public Integer getIdeProjetoAcao() {
		return this.ideProjetoAcao;
	}

	public void setIdeProjetoAcao(Integer ideProjetoAcao) {
		this.ideProjetoAcao = ideProjetoAcao;
	}

	public Boolean getIndExcluido() {
		return this.indExcluido;
	}

	public void setIndExcluido(Boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	public String getNomAcao() {
		return this.nomAcao;
	}

	public void setNomAcao(String nomAcao) {
		this.nomAcao = nomAcao;
	}

	public TccaProjeto getIdeTccaProjeto() {
		return this.ideTccaProjeto;
	}

	public void setIdeTccaProjeto(TccaProjeto ideTccaProjeto) {
		this.ideTccaProjeto = ideTccaProjeto;
	}

	public List<ProjetoAcaoProduto> getProjetoAcaoProdutoCollection() {
		return this.projetoAcaoProdutoCollection;
	}

	public void setProjetoAcaoProdutoCollection(List<ProjetoAcaoProduto> projetoAcaoProdutoCollection) {
		this.projetoAcaoProdutoCollection = projetoAcaoProdutoCollection;
	}

	public BigDecimal getValorPrevisto() {
		return valorPrevisto;
	}

	public void setValorPrevisto(BigDecimal valorPrevisto) {
		this.valorPrevisto = valorPrevisto;
	}

	public BigDecimal getValorContratado() {
		return valorContratado;
	}

	public void setValorContratado(BigDecimal valorContratado) {
		this.valorContratado = valorContratado;
	}

	public BigDecimal getValorExecutado() {
		return valorExecutado;
	}

	public void setValorExecutado(BigDecimal valorExecutado) {
		this.valorExecutado = valorExecutado;
	}

	public BigDecimal getValorSaldo() {
		return valorSaldo;
	}

	public void setValorSaldo(BigDecimal valorSaldo) {
		this.valorSaldo = valorSaldo;
	}

	public BigDecimal getValorRemanejado() {
		return valorRemanejado;
	}

	public void setValorRemanejado(BigDecimal valorRemanejado) {
		this.valorRemanejado = valorRemanejado;
	}

	public BigDecimal getValorSuplementado() {
		return valorSuplementado;
	}

	public void setValorSuplementado(BigDecimal valorSuplementado) {
		this.valorSuplementado = valorSuplementado;
	}
}