package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name = "orgao_emissor_auto")
@XmlRootElement
public class OrgaoEmissorAuto implements Serializable, BaseEntity {
	
	private static final long serialVersionUID = 3040375746755128356L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORGAO_EMISSOR_AUTO_SEQ")
	@SequenceGenerator(name = "ORGAO_EMISSOR_AUTO_SEQ", sequenceName = "ORGAO_EMISSOR_AUTO_SEQ", allocationSize = 1)
	@NotNull
	@Column(name = "ide_orgao_emissor_auto")
	private Integer ideOrgaoEmissorAuto;

	@Column(name = "nom_orgao_emissor_auto", length = 100, nullable = true)
	private String nomOrgaoEmissorAuto;

	public Integer getIdeOrgaoEmissorAuto() {
		return ideOrgaoEmissorAuto;
	}

	public void setIdeOrgaoEmissorAuto(Integer ideOrgaoEmissorAuto) {
		this.ideOrgaoEmissorAuto = ideOrgaoEmissorAuto;
	}

	public String getNomOrgaoEmissorAuto() {
		return nomOrgaoEmissorAuto;
	}

	public void setNomOrgaoEmissorAuto(String nomOrgaoEmissorAuto) {
		this.nomOrgaoEmissorAuto = nomOrgaoEmissorAuto;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideOrgaoEmissorAuto == null) ? 0 : ideOrgaoEmissorAuto
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
		OrgaoEmissorAuto other = (OrgaoEmissorAuto) obj;
		if (ideOrgaoEmissorAuto == null) {
			if (other.ideOrgaoEmissorAuto != null)
				return false;
		} else if (!ideOrgaoEmissorAuto.equals(other.ideOrgaoEmissorAuto))
			return false;
		return true;
	}

	@Override
	public Long getId() {
		return new Long(ideOrgaoEmissorAuto);
	}

}
