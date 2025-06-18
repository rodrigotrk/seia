package br.gov.ba.seia.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.enumerator.TccaProjetoOperacaoEnum;

@Entity
@Table(name="tcca_projeto_operacao")
@NamedQuery(name="TccaProjetoOperacao.findAll", query="SELECT t FROM TccaProjetoOperacao t")
public class TccaProjetoOperacao extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ide_tcca_projeto_operacao", updatable=false, unique=true, nullable=false)
	private Integer ideTccaProjetoOperacao;
	
	@Column(name="nom_operacao", nullable=false, length=100)
	private String nomOperacao;

	@Column(name="ind_oculto", nullable=false)
	private Boolean indOculto;

	@OneToMany(mappedBy="ideTccaProjetoOperacao")
	private List<MovimentacaoFinanceira> movimentacaoFinanceiraCollection;

	public TccaProjetoOperacao() {
	}

	public TccaProjetoOperacao(TccaProjetoOperacaoEnum tpoEnum) {
		super();
		this.ideTccaProjetoOperacao = tpoEnum.getId();
		this.nomOperacao = tpoEnum.getNome();
		this.indOculto = tpoEnum.getIndOculto();
	}
	
	public Integer getIdeTccaProjetoOperacao() {
		return this.ideTccaProjetoOperacao;
	}

	public void setIdeTccaProjetoOperacao(Integer ideTccaProjetoOperacao) {
		this.ideTccaProjetoOperacao = ideTccaProjetoOperacao;
	}

	public String getNomOperacao() {
		return this.nomOperacao;
	}

	public void setNomOperacao(String nomOperacao) {
		this.nomOperacao = nomOperacao;
	}

	public List<MovimentacaoFinanceira> getMovimentacaoFinanceiraCollection() {
		return this.movimentacaoFinanceiraCollection;
	}

	public void setMovimentacaoFinanceiraCollection(List<MovimentacaoFinanceira> movimentacaoFinanceiraCollection) {
		this.movimentacaoFinanceiraCollection = movimentacaoFinanceiraCollection;
	}

	public Boolean getIndOculto() {
		return indOculto;
	}

	public void setIndOculto(Boolean indOculto) {
		this.indOculto = indOculto;
	}
}