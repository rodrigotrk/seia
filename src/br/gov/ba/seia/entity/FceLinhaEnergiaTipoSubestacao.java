package br.gov.ba.seia.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name = "fce_linha_energia_tipo_subestacao")
@NamedQuery(name = "FceLinhaEnergiaTipoSubestacao.removeFceLinhaEnergiaTipoSubestacao", query = "DELETE FROM FceLinhaEnergiaTipoSubestacao f WHERE f.ideFceLinhaEnergia = :ideFceLinhaEnergia and f.ideTipoSubestacao = :ideTipoSubestacao")
public class FceLinhaEnergiaTipoSubestacao extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ide_fce_linha_energia_tipo_subestacao")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_linha_energia_tipo_subestacao_seq")
	@SequenceGenerator(name = "fce_linha_energia_tipo_subestacao_seq", sequenceName = "fce_linha_energia_tipo_subestacao_seq", allocationSize = 1)
	private Integer ideFceLinhaEnergiaTipoSubestacao;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ide_fce_linha_energia",referencedColumnName = "ide_fce_linha_energia", nullable = false)
	private FceLinhaEnergia ideFceLinhaEnergia;
	
	@Column(name = "dsc_subestacao")
	private String dscSubestacao;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ide_tipo_subestacao", referencedColumnName = "ide_tipo_subestacao", nullable = false)
	private TipoSubestacao ideTipoSubestacao;

	public FceLinhaEnergiaTipoSubestacao() {
	}

	public FceLinhaEnergiaTipoSubestacao(FceLinhaEnergia fceLinhaEnergia, TipoSubestacao tipoSubestacao) {
		
		this.ideFceLinhaEnergia = fceLinhaEnergia;
		this.ideTipoSubestacao = tipoSubestacao;
	}
	
	public Integer getIdeSubestacao() {
		return ideFceLinhaEnergiaTipoSubestacao;
	}

	public void setIdeSubestacao(Integer ideFceLinhaEnergiaTipoSubestacao) {
		this.ideFceLinhaEnergiaTipoSubestacao = ideFceLinhaEnergiaTipoSubestacao;
	}

	public FceLinhaEnergia getIdeFceLinhaEnergia() {
		return ideFceLinhaEnergia;
	}

	public void setIdeFceLinhaEnergia(FceLinhaEnergia ideFceLinhaEnergia) {
		this.ideFceLinhaEnergia = ideFceLinhaEnergia;
	}

	public String getDscSubestacao() {
		return dscSubestacao;
	}

	public void setDscSubestacao(String dscSubestacao) {
		this.dscSubestacao = dscSubestacao;
	}

	public TipoSubestacao getIdeTipoSubestacao() {
		return ideTipoSubestacao;
	}

	public void setIdeTipoSubestacao(TipoSubestacao ideTipoSubestacao) {
		this.ideTipoSubestacao = ideTipoSubestacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dscSubestacao == null) ? 0 : dscSubestacao.hashCode());
		result = prime
				* result
				+ ((ideFceLinhaEnergia == null) ? 0 : ideFceLinhaEnergia
						.hashCode());
		result = prime * result
				+ ((ideFceLinhaEnergiaTipoSubestacao == null) ? 0 : ideFceLinhaEnergiaTipoSubestacao.hashCode());
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
		FceLinhaEnergiaTipoSubestacao other = (FceLinhaEnergiaTipoSubestacao) obj;
		if (dscSubestacao == null) {
			if (other.dscSubestacao != null)
				return false;
		} else if (!dscSubestacao.equals(other.dscSubestacao))
			return false;
		if (ideFceLinhaEnergia == null) {
			if (other.ideFceLinhaEnergia != null)
				return false;
		} else if (!ideFceLinhaEnergia.equals(other.ideFceLinhaEnergia))
			return false;
		if (ideFceLinhaEnergiaTipoSubestacao == null) {
			if (other.ideFceLinhaEnergiaTipoSubestacao != null)
				return false;
		} else if (!ideFceLinhaEnergiaTipoSubestacao.equals(other.ideFceLinhaEnergiaTipoSubestacao))
			return false;
		if (ideTipoSubestacao == null) {
			if (other.ideTipoSubestacao != null)
				return false;
		} else if (!ideTipoSubestacao.equals(other.ideTipoSubestacao))
			return false;
		return true;
	}
}
