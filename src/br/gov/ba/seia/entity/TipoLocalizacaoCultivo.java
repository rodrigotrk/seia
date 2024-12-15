package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.interfaces.BaseEntity;


/**
 * Entidade que representa a tabela <i>tipo_localizacao_cultivo</i>, utilizada na <b>Aba Tanque Rede</b> do FCE - Licenciamento para Aquicultura.
 * <ul>
 * 		<li>1 -'Rio'</li>
 * 		<li>2 -'Reservatório / Açude'</li>
 * 		<li>3 -'Lago / Lagoa Natural'</li>
 * 		<li>4 -'Estuário'</li>
 * 		<li>5 -'Mar'</li>
 * 		<li>6 -'Área Terrestre'</li>
 * 		<li>7 -'Outros'</li>
 * </ul>
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 28/05/2015
 * @see #6934
 */
@Entity
@Table(name="tipo_localizacao_cultivo")
@NamedQuery(name="TipoLocalizacaoCultivo.findAll", query="SELECT t FROM TipoLocalizacaoCultivo t")
public class TipoLocalizacaoCultivo implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TIPO_LOCALIZACAO_CULTIVO_IDETIPOLOCALIZACAOCULTIVO_GENERATOR", sequenceName="TIPO_LOCALIZACAO_CULTIVO_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TIPO_LOCALIZACAO_CULTIVO_IDETIPOLOCALIZACAOCULTIVO_GENERATOR")
	@Column(name="ide_tipo_localizacao_cultivo")
	private Integer ideTipoLocalizacaoCultivo;

	@Column(name="nom_tipo_localizacao_cultivo")
	private String nomTipoLocalizacaoCultivo;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	public TipoLocalizacaoCultivo() {
	}

	public Integer getIdeTipoLocalizacaoCultivo() {
		return this.ideTipoLocalizacaoCultivo;
	}

	public void setIdeTipoLocalizacaoCultivo(Integer ideTipoLocalizacaoCultivo) {
		this.ideTipoLocalizacaoCultivo = ideTipoLocalizacaoCultivo;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public String getNomTipoLocalizacaoCultivo() {
		return this.nomTipoLocalizacaoCultivo;
	}

	public void setNomTipoLocalizacaoCultivo(String nomTipoLocalizacaoCultivo) {
		this.nomTipoLocalizacaoCultivo = nomTipoLocalizacaoCultivo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideTipoLocalizacaoCultivo == null) ? 0
						: ideTipoLocalizacaoCultivo.hashCode());
		return result;
	}

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
		TipoLocalizacaoCultivo other = (TipoLocalizacaoCultivo) obj;
		if (ideTipoLocalizacaoCultivo == null) {
			if (other.ideTipoLocalizacaoCultivo != null) {
				return false;
			}
		} else if (!ideTipoLocalizacaoCultivo
				.equals(other.ideTipoLocalizacaoCultivo)) {
			return false;
		}
		return true;
	}

	@Override
	public Long getId() {
		return new Long(this.ideTipoLocalizacaoCultivo);
	}

	public boolean isOutros(){
		return this.nomTipoLocalizacaoCultivo.compareTo("Outros") == 0;
	}
}