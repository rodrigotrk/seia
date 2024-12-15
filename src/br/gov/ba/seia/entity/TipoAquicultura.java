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
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import br.gov.ba.seia.enumerator.TipoAquiculturaEnum;


/**
 * <ul>
 * 	<li>VIVEIRO ESCAVADO = 5</li>
 * 	<li>CAPTAÇÃO = 1</li>
 * 	<li>LANÇAMENTO = 2</li>
 * </ul>
 *
 * <ul>
 *  <li>TANQUE REDE = 6</li>
 * 	<li>RIO = 3</li>
 * 	<li>BARRAGEM = 4</li>
 * </ul>
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 05/11/2014
 */
@Entity
@Table(name="tipo_aquicultura")
@NamedQuery(name="TipoAquicultura.findAll", query="SELECT t FROM TipoAquicultura t")
public class TipoAquicultura implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ide_tipo_aquicultura")
	private Integer ideTipoAquicultura;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	@Column(name="nom_tipo_aquicultura")
	private String nomTipoAquicultura;

	@JoinColumn(name = "ide_tipo_aquicultura_pai", referencedColumnName = "ide_tipo_aquicultura", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private TipoAquicultura ideTipoAquiculturaPai;

	public TipoAquicultura() {
	}

	public TipoAquicultura(TipoAquiculturaEnum tipoAquiculturaEnum){
		this.ideTipoAquicultura = tipoAquiculturaEnum.getId();
		this.ideTipoAquiculturaPai = new TipoAquicultura(tipoAquiculturaEnum.getIdPai());
	}

	public TipoAquicultura(Integer ideTipoAquicultura) {
		this.ideTipoAquicultura = ideTipoAquicultura;
	}

	public Integer getIdeTipoAquicultura() {
		return this.ideTipoAquicultura;
	}

	public void setIdeTipoAquicultura(Integer ideTipoAquicultura) {
		this.ideTipoAquicultura = ideTipoAquicultura;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public String getNomTipoAquicultura() {
		return this.nomTipoAquicultura;
	}

	public void setNomTipoAquicultura(String nomTipoAquicultura) {
		this.nomTipoAquicultura = nomTipoAquicultura;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideTipoAquicultura == null) ? 0 : ideTipoAquicultura
						.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TipoAquicultura other = (TipoAquicultura) obj;
		if (ideTipoAquicultura == null) {
			if (other.ideTipoAquicultura != null) {
				return false;
			}
		} else if (!ideTipoAquicultura.equals(other.ideTipoAquicultura)) {
			return false;
		}
		return true;
	}

	public TipoAquicultura getIdeTipoAquiculturaPai() {
		return ideTipoAquiculturaPai;
	}

	public void setIdeTipoAquiculturaPai(TipoAquicultura ideTipoAquiculturaPai) {
		this.ideTipoAquiculturaPai = ideTipoAquiculturaPai;
	}
}