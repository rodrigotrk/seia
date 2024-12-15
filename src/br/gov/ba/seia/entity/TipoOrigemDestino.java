package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.gov.ba.seia.enumerator.TipoOrigemDestinoEnum;
import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name="tipo_origem_destino")
@NamedQuery(name="TipoOrigemDestino.findAll", query="SELECT t FROM TipoOrigemDestino t")
public class TipoOrigemDestino implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ide_tipo_origem_destino", updatable=false, unique=true, nullable=false)
	private Integer ideTipoOrigemDestino;

	@Column(name="nom_tipo_origem_destino", nullable=false, length=100)
	private String nomTipoOrigemDestino;

	@OneToMany(mappedBy="ideTipoDestino")
	private List<MovimentacaoFinanceiraTccaProduto> movimentacaoFinanceiraTccaProdutoDestinoCollection;

	@OneToMany(mappedBy="ideTipoOrigem")
	private List<MovimentacaoFinanceiraTccaProduto> movimentacaoFinanceiraTccaProdutoOrigemCollection;

	public TipoOrigemDestino() {
	}
	
	public TipoOrigemDestino(TipoOrigemDestinoEnum todEnum) {
		super();
		this.ideTipoOrigemDestino = todEnum.getId();
		this.nomTipoOrigemDestino = todEnum.getNome();
	}

	@Override
	public Long getId() {
		return ideTipoOrigemDestino.longValue();
	}

	public Integer getIdeTipoOrigemDestino() {
		return this.ideTipoOrigemDestino;
	}

	public void setIdeTipoOrigemDestino(Integer ideTipoOrigemDestino) {
		this.ideTipoOrigemDestino = ideTipoOrigemDestino;
	}

	public String getNomTipoOrigemDestino() {
		return this.nomTipoOrigemDestino;
	}

	public void setNomTipoOrigemDestino(String nomTipoOrigemDestino) {
		this.nomTipoOrigemDestino = nomTipoOrigemDestino;
	}

	public List<MovimentacaoFinanceiraTccaProduto> getMovimentacaoFinanceiraTccaProdutoDestinoCollection() {
		return this.movimentacaoFinanceiraTccaProdutoDestinoCollection;
	}

	public void setMovimentacaoFinanceiraTccaProdutoDestinoCollection(List<MovimentacaoFinanceiraTccaProduto> movimentacaoFinanceiraTccaProdutoDestinoCollection) {
		this.movimentacaoFinanceiraTccaProdutoDestinoCollection = movimentacaoFinanceiraTccaProdutoDestinoCollection;
	}

	public List<MovimentacaoFinanceiraTccaProduto> getMovimentacaoFinanceiraTccaProdutoOrigemCollection() {
		return this.movimentacaoFinanceiraTccaProdutoOrigemCollection;
	}

	public void setMovimentacaoFinanceiraTccaProdutoOrigemCollection(List<MovimentacaoFinanceiraTccaProduto> movimentacaoFinanceiraTccaProdutoOrigemCollection) {
		this.movimentacaoFinanceiraTccaProdutoOrigemCollection = movimentacaoFinanceiraTccaProdutoOrigemCollection;
	}
}