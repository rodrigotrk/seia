package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.text.Collator;
import java.util.Locale;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;


/**
 * The persistent class for the tipo_uso_solo database table.
 * 
 */
@Entity
@XmlRootElement
@Table(name="tipo_uso_solo")
public class TipoUsoSolo implements Serializable,BaseEntity,Comparable<TipoUsoSolo>  {
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_uso_solo_ide_tipo_uso_solo_generator")
	@SequenceGenerator(name = "tipo_uso_solo_ide_tipo_uso_solo_generator", sequenceName = "tipo_uso_solo_ide_tipo_uso_solo_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_tipo_uso_solo")
	private Integer ideTipoUsoSolo;

	@NotNull
	@Size(min = 1, max = 25)
	@Column(name="dsc_tipo_uso_solo" ,nullable = false, length = 25)
	private String dscTipoUsoSolo;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

    public TipoUsoSolo() {
    }

	public Integer getIdeTipoUsoSolo() {
		return this.ideTipoUsoSolo;
	}

	public void setIdeTipoUsoSolo(Integer ideTipoUsoSolo) {
		this.ideTipoUsoSolo = ideTipoUsoSolo;
	}

	public String getDscTipoUsoSolo() {
		return this.dscTipoUsoSolo;
	}

	public void setDscTipoUsoSolo(String dscTipoUsoSolo) {
		this.dscTipoUsoSolo = dscTipoUsoSolo;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideTipoUsoSolo == null) ? 0 : ideTipoUsoSolo.hashCode());
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
		TipoUsoSolo other = (TipoUsoSolo) obj;
		if (ideTipoUsoSolo == null) {
			if (other.ideTipoUsoSolo != null)
				return false;
		} else if (!ideTipoUsoSolo.equals(other.ideTipoUsoSolo))
			return false;
		return true;
	}

	@Override
	public Long getId() {
		return new Long(ideTipoUsoSolo);
	}

	@Override
	public int compareTo(TipoUsoSolo arg0) {
		Collator collator = Collator.getInstance (new Locale ("pt", "BR"));  
	    collator.setStrength(Collator.PRIMARY);
	    return  collator.compare(dscTipoUsoSolo, arg0.dscTipoUsoSolo);
	}

}