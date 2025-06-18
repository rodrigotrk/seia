package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.interfaces.BaseEntity;


@Entity
@Table(name = "tipo_caracteristica_extracao")
public class TipoCaracteristicaExtracao implements Serializable, BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "tipo_caracteristica_extracao_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "tipo_caracteristica_extracao_seq", sequenceName = "tipo_caracteristica_extracao_seq", allocationSize = 1)
	@Column(name = "ide_tipo_caracteristica_extracao", nullable = false)
	private Integer ideTipoCaracteristicaExtracao;
	
	@Column(name = "nom_caracteristica_extracao", nullable = false)
	private String nomCaracteristicaExtracao;
	
	@Column(name = "ind_ativo", nullable = false)
	private Boolean indAtivo;

	public TipoCaracteristicaExtracao() {
	}

	public Integer getIdeTipoCaracteristicaExtracao() {
		return ideTipoCaracteristicaExtracao;
	}

	public void setIdeTipoCaracteristicaExtracao(
			Integer ideTipoCaracteristicaExtracao) {
		this.ideTipoCaracteristicaExtracao = ideTipoCaracteristicaExtracao;
	}

	public String getNomCaracteristicaExtracao() {
		return nomCaracteristicaExtracao;
	}

	public void setNomCaracteristicaExtracao(String nomCaracteristicaExtracao) {
		this.nomCaracteristicaExtracao = nomCaracteristicaExtracao;
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
				+ ((ideTipoCaracteristicaExtracao == null) ? 0
						: ideTipoCaracteristicaExtracao.hashCode());
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
		TipoCaracteristicaExtracao other = (TipoCaracteristicaExtracao) obj;
		if (ideTipoCaracteristicaExtracao == null) {
			if (other.ideTipoCaracteristicaExtracao != null)
				return false;
		} else if (!ideTipoCaracteristicaExtracao
				.equals(other.ideTipoCaracteristicaExtracao))
			return false;
		return true;
	}

	@Override
	public Long getId() {
		
		return new Long(ideTipoCaracteristicaExtracao);
	}
	
	

}
