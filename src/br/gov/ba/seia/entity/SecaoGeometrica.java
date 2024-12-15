package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;


@Entity
@Table(name = "secao_geometrica")
@XmlRootElement
public class SecaoGeometrica implements BaseEntity, Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ide_secao_geometrica")
	private Integer ideSecaoGeometrica;

	@Column(name = "dsc_secao_geometrica")
	private String dscSecaoGeometrica;
	
	@Transient
	private boolean selecionado;
	
	public Integer getIdeSecaoGeometrica() {
		return ideSecaoGeometrica;
	}

	public void setIdeSecaoGeometrica(Integer ideSecaoGeometrica) {
		this.ideSecaoGeometrica = ideSecaoGeometrica;
	}

	public String getDscSecaoGeometrica() {
		return dscSecaoGeometrica;
	}

	public void setDscSecaoGeometrica(String dscSecaoGeometrica) {
		this.dscSecaoGeometrica = dscSecaoGeometrica;
	}

	public boolean isSelecionado() {
		return selecionado;
	}

	public void setSelecionado(boolean selecionado) {
		this.selecionado = selecionado;
	}

	@Override
	public Long getId() {
		return new Long(getIdeSecaoGeometrica());
	}
	
	@Override
	public boolean equals(Object object) {
        if (!(object instanceof SecaoGeometrica)) {
            return false;
        }
        SecaoGeometrica other = (SecaoGeometrica) object;
        if ((this.getIdeSecaoGeometrica() == null && other.getIdeSecaoGeometrica() != null) || (this.getIdeSecaoGeometrica() != null && !this.getIdeSecaoGeometrica().equals(other.getIdeSecaoGeometrica()))) {
            return false;
        }
        return true;
	}
}
