package br.gov.ba.seia.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.entity.generic.AbstractEntity;

/**
 * @author lesantos
 *
 */
@Entity
@Table(name = "cerh_classe_corpo_hidrico")
public class CerhClasseCorpoHidrico extends AbstractEntity{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cerh_classe_corpo_hidrico_seq")
	@SequenceGenerator(name = "cerh_classe_corpo_hidrico_seq", sequenceName = "cerh_classe_corpo_hidrico_seq", allocationSize = 1)
	@Column(name = "ide_cerh_classe_corpo_hidrico")
	private Integer ideCerhClasseCorpoHidrico;
	
	@Column( name = "dsc_classe_corpo_hidrico")
	private String dscClasseCorpoHidrico;

	public Integer getIdeCerhClasseCorpoHidrico() {
		return ideCerhClasseCorpoHidrico;
	}

	public void setIdeCerhClasseCorpoHidrico(Integer ideCerhClasseCorpoHidrico) {
		this.ideCerhClasseCorpoHidrico = ideCerhClasseCorpoHidrico;
	}

	public String getDscClasseCorpoHidrico() {
		return dscClasseCorpoHidrico;
	}

	public void setDscClasseCorpoHidrico(String dscClasseCorpoHidrico) {
		this.dscClasseCorpoHidrico = dscClasseCorpoHidrico;
	}

}
