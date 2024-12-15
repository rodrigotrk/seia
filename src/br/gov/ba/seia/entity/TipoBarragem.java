package br.gov.ba.seia.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name = "tipo_barragem")
public class TipoBarragem extends AbstractEntity  {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ide_tipo_barragem", nullable = false)
	private Integer ideTipoBarragem;

	@Historico(name="Tipo do Barramento")
	@Basic(optional = false)
	@NotNull
	@Column(name = "nom_tipo_barragem")
	private String nomTipoBarragem;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	public TipoBarragem() {
	}

	public TipoBarragem(Integer ideTipoBarragem) {
		this.ideTipoBarragem = ideTipoBarragem;
	}

	public TipoBarragem(Integer ideTipoBarragem, String nomTipoBarragem) {
		this.ideTipoBarragem = ideTipoBarragem;
		this.nomTipoBarragem = nomTipoBarragem;
	}

	public Integer getIdeTipoBarragem() {
		return ideTipoBarragem;
	}

	public void setIdeTipoBarragem(Integer ideTipoBarragem) {
		this.ideTipoBarragem = ideTipoBarragem;
	}

	public String getNomTipoBarragem() {
		return nomTipoBarragem;
	}

	public void setNomTipoBarragem(String nomTipoBarragem) {
		this.nomTipoBarragem = nomTipoBarragem;
	}

	public Boolean getIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}
}
