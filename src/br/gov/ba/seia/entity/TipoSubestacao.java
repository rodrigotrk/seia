package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name = "tipo_subestacao")
@NamedQueries({@NamedQuery(name = "TipoSubestacao.findAll", query = "Select ts FROM TipoSubestacao ts")})
public class TipoSubestacao implements Serializable, BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@Column(name = "ide_tipo_subestacao")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ide_tipo_subestacao_seq")
	@SequenceGenerator(name = "ide_tipo_subestacao_seq", sequenceName = "ide_tipo_subestacao_seq", allocationSize = 1)
	private Integer ideTipoSubestacao;

	@Column(name = "dsc_tipo_subestacao", nullable = false)
	private String dscTipoSubestacao;

	@Column(name = "ind_ativo", nullable = false)
	private Boolean indAtivo;

	public TipoSubestacao() {
	}

	public Integer getIdeTipoSubestacao() {
		return ideTipoSubestacao;
	}

	public void setIdeTipoSubestacao(Integer ideTipoSubestacao) {
		this.ideTipoSubestacao = ideTipoSubestacao;
	}

	public String getDscTipoSubestacao() {
		return dscTipoSubestacao;
	}

	public void setDscTipoSubestacao(String dscTipoSubestacao) {
		this.dscTipoSubestacao = dscTipoSubestacao;
	}

	public Boolean getIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideTipoSubestacao == null) ? 0 : ideTipoSubestacao
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TipoSubestacao other = (TipoSubestacao) obj;
		if (ideTipoSubestacao == null) {
			if (other.ideTipoSubestacao != null)
				return false;
		} else if (!ideTipoSubestacao.equals(other.ideTipoSubestacao))
			return false;
		return true;
	}

	@Override
	public Long getId() {
		return Long.valueOf(this.ideTipoSubestacao);
	}

}
