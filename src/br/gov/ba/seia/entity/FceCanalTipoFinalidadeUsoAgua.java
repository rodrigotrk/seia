/**
 * 
 */
package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
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

/**
 * @author lesantos
 *
 */
@Entity
@Table(name = "fce_canal_tipo_finalidade_uso_agua")
@NamedQueries(
		@NamedQuery(name = "FceCanalTipoFinalidadeUsoAgua.removeByIdFceCanalTipoFinalidadeUsoAgua", query = "DELETE FROM FceCanalTipoFinalidadeUsoAgua a WHERE a.ideFceCanalTipoFinalidadeUsoAgua = :ideFceCanalTipoFinalidadeUsoAgua")
)
public class FceCanalTipoFinalidadeUsoAgua implements BaseEntity, Serializable, Comparable<FceCanalTipoFinalidadeUsoAgua>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3877778062116956359L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_canal_tipo_finalidade_uso_agua_seq")
	@SequenceGenerator(name = "fce_canal_tipo_finalidade_uso_agua_seq", sequenceName = "fce_canal_tipo_finalidade_uso_agua_seq", allocationSize = 1)
	@Column(name = "ide_fce_canal_tipo_finalidade_uso_agua")
	private Integer ideFceCanalTipoFinalidadeUsoAgua;
	
	@JoinColumn(name="ide_fce_canal",referencedColumnName = "ide_fce_canal", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private FceCanal ideFceCanal;
	
	@JoinColumn(name = "ide_tipo_finalidade_uso_agua", referencedColumnName = "ide_tipo_finalidade_uso_agua", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private TipoFinalidadeUsoAgua ideTipoFinalidadeUsoAgua;
	
	public FceCanalTipoFinalidadeUsoAgua() {
		
	}

	public FceCanalTipoFinalidadeUsoAgua(FceCanal ideFceCanal,	TipoFinalidadeUsoAgua ideTipoFinalidadeUsoAgua) {
		this.ideFceCanal = ideFceCanal;
		this.ideTipoFinalidadeUsoAgua = ideTipoFinalidadeUsoAgua;
	}

	@Override
	public Long getId() {
		if(ideFceCanalTipoFinalidadeUsoAgua != null){
			return new Long(ideFceCanalTipoFinalidadeUsoAgua);
		}else{
			return new Long(ideTipoFinalidadeUsoAgua.getIdeTipoFinalidadeUsoAgua());
		}
	}

	/**
	 * @return the ideFceCanalTipoFinalidadeUsoAgua
	 */
	public Integer getIdeFceCanalTipoFinalidadeUsoAgua() {
		return ideFceCanalTipoFinalidadeUsoAgua;
	}

	/**
	 * @param ideFceCanalTipoFinalidadeUsoAgua the ideFceCanalTipoFinalidadeUsoAgua to set
	 */
	public void setIdeFceCanalTipoFinalidadeUsoAgua(
			Integer ideFceCanalTipoFinalidadeUsoAgua) {
		this.ideFceCanalTipoFinalidadeUsoAgua = ideFceCanalTipoFinalidadeUsoAgua;
	}

	/**
	 * @return the ideFceCanal
	 */
	public FceCanal getIdeFceCanal() {
		return ideFceCanal;
	}

	/**
	 * @param ideFceCanal the ideFceCanal to set
	 */
	public void setIdeFceCanal(FceCanal ideFceCanal) {
		this.ideFceCanal = ideFceCanal;
	}

	/**
	 * @return the ideTipoFinalidadeUsoAgua
	 */
	public TipoFinalidadeUsoAgua getIdeTipoFinalidadeUsoAgua() {
		return ideTipoFinalidadeUsoAgua;
	}

	/**
	 * @param ideTipoFinalidadeUsoAgua the ideTipoFinalidadeUsoAgua to set
	 */
	public void setIdeTipoFinalidadeUsoAgua(
			TipoFinalidadeUsoAgua ideTipoFinalidadeUsoAgua) {
		this.ideTipoFinalidadeUsoAgua = ideTipoFinalidadeUsoAgua;
	}

	@Override
	public int compareTo(FceCanalTipoFinalidadeUsoAgua o) {
		if(this.ideTipoFinalidadeUsoAgua.getIdeTipoFinalidadeUsoAgua() < o.getIdeTipoFinalidadeUsoAgua().getIdeTipoFinalidadeUsoAgua()){
			return -1;
		}
		if(this.ideTipoFinalidadeUsoAgua.getIdeTipoFinalidadeUsoAgua() > o.getIdeTipoFinalidadeUsoAgua().getIdeTipoFinalidadeUsoAgua()){
			return 1;
		}
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof FceCanalTipoFinalidadeUsoAgua)
			if(this.ideFceCanalTipoFinalidadeUsoAgua != null && ((FceCanalTipoFinalidadeUsoAgua) obj).getIdeFceCanalTipoFinalidadeUsoAgua() != null){
				return this.ideFceCanalTipoFinalidadeUsoAgua.equals(((FceCanalTipoFinalidadeUsoAgua) obj).getIdeFceCanalTipoFinalidadeUsoAgua());
			}else if(this.ideTipoFinalidadeUsoAgua != null && this.ideTipoFinalidadeUsoAgua.getIdeTipoFinalidadeUsoAgua() != null && ((FceCanalTipoFinalidadeUsoAgua) obj).getIdeTipoFinalidadeUsoAgua().getIdeTipoFinalidadeUsoAgua() != null){
				return this.ideTipoFinalidadeUsoAgua.getIdeTipoFinalidadeUsoAgua().equals(((FceCanalTipoFinalidadeUsoAgua) obj).getIdeTipoFinalidadeUsoAgua().getIdeTipoFinalidadeUsoAgua());
			}
		return super.equals(obj);
	}
	
}
