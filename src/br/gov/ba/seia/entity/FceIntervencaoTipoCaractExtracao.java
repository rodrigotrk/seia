package br.gov.ba.seia.entity;

import java.io.Serializable;

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

import br.gov.ba.seia.interfaces.BaseEntity;


@Entity
@Table(name = "fce_intervencao_tipo_caract_extracao")
@NamedQueries({
	@NamedQuery(name = "FceIntervencaoTipoCaractExtracao.deleteByIde", query = "DELETE FROM FceIntervencaoTipoCaractExtracao f WHERE f.ideFceIntervencaoTipoCaractExtracao = :ideFceIntervencaoTipoCaractExtracao")})
public class FceIntervencaoTipoCaractExtracao implements Serializable, BaseEntity, Comparable<FceIntervencaoTipoCaractExtracao>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "tipo_caracteristica_extracao_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "tipo_caracteristica_extracao_seq", sequenceName = "tipo_caracteristica_extracao_seq", allocationSize = 1)
	@Column(name = "ide_fce_intervencao_tipo_caract_extracao", nullable = false)
	private Integer ideFceIntervencaoTipoCaractExtracao;

	@JoinColumn(name = "ide_tipo_caracteristica_extracao", referencedColumnName = "ide_tipo_caracteristica_extracao", nullable = false)
	@ManyToOne(fetch = FetchType.EAGER)
	private TipoCaracteristicaExtracao ideTipoCaracteristicaExtracao;

	@JoinColumn(name = "ide_fce_intervencao_caracteristica_extracao", referencedColumnName = "ide_fce_intervencao_caracteristica_extracao", nullable = false)
	@ManyToOne(fetch = FetchType.EAGER)
	private FceIntervencaoCaracteristicaExtracao ideFceIntervencaoCaracteristicaExtracao;

	public FceIntervencaoTipoCaractExtracao() {
	}


	public FceIntervencaoTipoCaractExtracao(TipoCaracteristicaExtracao ideTipoCaracteristicaExtracao, FceIntervencaoCaracteristicaExtracao ideFceIntervencaoCaracteristicaExtracao) {
		this.ideTipoCaracteristicaExtracao = ideTipoCaracteristicaExtracao;
		this.ideFceIntervencaoCaracteristicaExtracao = ideFceIntervencaoCaracteristicaExtracao;
	}


	public Integer getIdeFceIntervencaoTipoCaractExtracao() {
		return ideFceIntervencaoTipoCaractExtracao;
	}

	public void setIdeFceIntervencaoTipoCaractExtracao(
			Integer ideFceIntervencaoTipoCaractExtracao) {
		this.ideFceIntervencaoTipoCaractExtracao = ideFceIntervencaoTipoCaractExtracao;
	}

	public TipoCaracteristicaExtracao getIdeTipoCaracteristicaExtracao() {
		return ideTipoCaracteristicaExtracao;
	}

	public void setIdeTipoCaracteristicaExtracao(
			TipoCaracteristicaExtracao ideTipoCaracteristicaExtracao) {
		this.ideTipoCaracteristicaExtracao = ideTipoCaracteristicaExtracao;
	}

	public FceIntervencaoCaracteristicaExtracao getIdeFceIntervencaoCaracteristicaExtracao() {
		return ideFceIntervencaoCaracteristicaExtracao;
	}

	public void setIdeFceIntervencaoCaracteristicaExtracao(
			FceIntervencaoCaracteristicaExtracao ideFceIntervencaoCaracteristicaExtracao) {
		this.ideFceIntervencaoCaracteristicaExtracao = ideFceIntervencaoCaracteristicaExtracao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideFceIntervencaoCaracteristicaExtracao == null) ? 0
						: ideFceIntervencaoCaracteristicaExtracao.hashCode());
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
		FceIntervencaoTipoCaractExtracao other = (FceIntervencaoTipoCaractExtracao) obj;
		if (ideFceIntervencaoCaracteristicaExtracao == null) {
			if (other.ideFceIntervencaoCaracteristicaExtracao != null)
				return false;
		} else if (!ideFceIntervencaoCaracteristicaExtracao
				.equals(other.ideFceIntervencaoCaracteristicaExtracao))
			return false;
		if(!this.ideTipoCaracteristicaExtracao.equals(other.getIdeTipoCaracteristicaExtracao())){
			return false;
		}
		return true;
	}

	@Override
	public Long getId() {
		
		if(ideFceIntervencaoTipoCaractExtracao != null){
			return new Long(ideFceIntervencaoTipoCaractExtracao);
		}else{
			return new Long(this.ideTipoCaracteristicaExtracao.getIdeTipoCaracteristicaExtracao());
		}
	}


	@Override
	public int compareTo(FceIntervencaoTipoCaractExtracao o) {
		if(this.ideTipoCaracteristicaExtracao.getIdeTipoCaracteristicaExtracao() < o.ideTipoCaracteristicaExtracao.getIdeTipoCaracteristicaExtracao()){
			return -1;
		}
		if(this.ideTipoCaracteristicaExtracao.getIdeTipoCaracteristicaExtracao() > o.ideTipoCaracteristicaExtracao.getIdeTipoCaracteristicaExtracao()){
			return 1;
		}
		return 0;
	}


}
