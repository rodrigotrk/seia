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
import javax.persistence.Transient;

import br.gov.ba.seia.interfaces.BaseEntity;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Util;


/**
 * Entidade que representa a tabela <i>organismo</i>, utilizada na <b>Abas Viveiro Escavado e Tanque Rede</b> do FCE - Licenciamento para Aquicultura.
 * <ul>
 * 		<li>1 -'Alevinos'</li>
 * 		<li>2 -'Larvas'</li>
 * 		<li>3 -'Pós-larvas'</li>
 * 		<li>4 -'Girinos'</li>
 * 		<li>5 -'Imagos'</li>
 * 		<li>6 -'Outros'</li>
 * </ul>
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 28/05/2015
 * @see #6934
 */
@Entity
@Table(name="organismo")
@NamedQuery(name="Organismo.findAll", query="SELECT o FROM Organismo o")
public class Organismo implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ORGANISMO_IDEORGANISMO_GENERATOR", sequenceName="ORGANISMO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ORGANISMO_IDEORGANISMO_GENERATOR")
	@Column(name="ide_organismo", nullable = false)
	private Integer ideOrganismo;

	@Column(name="ind_ativo", nullable = false)
	private Boolean indAtivo;

	@Column(name="nom_organismo", nullable = false)
	private String nomOrganismo;

	@Transient
	private boolean rowSelect = false;

	public Organismo() {
	}

	public Organismo(Integer ideOrganismo) {
		this.ideOrganismo = ideOrganismo;
	}

	public Integer getIdeOrganismo() {
		return this.ideOrganismo;
	}

	public void setIdeOrganismo(Integer ideOrganismo) {
		this.ideOrganismo = ideOrganismo;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public String getNomOrganismo() {
		return this.nomOrganismo;
	}

	public void setNomOrganismo(String nomOrganismo) {
		this.nomOrganismo = nomOrganismo;
	}

	@Override
	public Long getId() {
		return new Long(this.ideOrganismo);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideOrganismo == null) ? 0 : ideOrganismo.hashCode());
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
		Organismo other = (Organismo) obj;
		if (ideOrganismo == null) {
			if (other.ideOrganismo != null) {
				return false;
			}
		} else if (!ideOrganismo.equals(other.ideOrganismo)) {
			return false;
		}
		return true;
	}

	public boolean isOutros(){
		return this.nomOrganismo.compareTo("Outros") == 0;
	}

	public boolean isRowSelect() {
		return rowSelect;
	}

	public void setRowSelect(boolean rowSelect) {
		this.rowSelect = rowSelect;
		if(this.rowSelect && isOutros()){
			JsfUtil.addWarnMessage(Util.getString("msg_generica_cadastro_outros"));
		}
	}
}