package br.gov.ba.seia.entity;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.entity.generic.AbstractEntityHist;

@Entity
@Table(name="cerh_outros_usos")
public class CerhOutrosUsos extends AbstractEntityHist{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ide_cerh_outros_usos")
	private Integer ideCerhOutrosUsos;

	@Historico(name="Outros Usos")
	@Column(name="dsc_outros_usos")
	private String dscOutrosUsos;

	@OneToMany(mappedBy="ideCerhOutrosUsos")
	private Collection<CerhCaptacaoOutrosUsos> cerhCaptacaoOutrosUsoCollection;

	@OneToMany(mappedBy="ideCerhOutrosUsos")
	private Collection<CerhLancamentoOutrosUsos> cerhLancamentoOutrosUsoCollection;
	
	
	public CerhOutrosUsos() {
		
	}
	
	public CerhOutrosUsos(Integer ide, String dsc) {
		this.ideCerhOutrosUsos = ide;
		this.dscOutrosUsos = dsc;
	}

	public Integer getIdeCerhOutrosUsos() {
		return ideCerhOutrosUsos;
	}

	public void setIdeCerhOutrosUsos(Integer ideCerhOutrosUsos) {
		this.ideCerhOutrosUsos = ideCerhOutrosUsos;
	}

	public String getDscOutrosUsos() {
		return dscOutrosUsos;
	}

	public void setDscOutrosUsos(String dscOutrosUsos) {
		this.dscOutrosUsos = dscOutrosUsos;
	}

	public Collection<CerhCaptacaoOutrosUsos> getCerhCaptacaoOutrosUsoCollection() {
		return cerhCaptacaoOutrosUsoCollection;
	}

	public void setCerhCaptacaoOutrosUsoCollection(Collection<CerhCaptacaoOutrosUsos> cerhCaptacaoOutrosUsoCollection) {
		this.cerhCaptacaoOutrosUsoCollection = cerhCaptacaoOutrosUsoCollection;
	}

	public Collection<CerhLancamentoOutrosUsos> getCerhLancamentoOutrosUsoCollection() {
		return cerhLancamentoOutrosUsoCollection;
	}

	public void setCerhLancamentoOutrosUsoCollection(Collection<CerhLancamentoOutrosUsos> cerhLancamentoOutrosUsoCollection) {
		this.cerhLancamentoOutrosUsoCollection = cerhLancamentoOutrosUsoCollection;
	}

	public boolean isOutros() {
		return this.dscOutrosUsos.compareTo("Outros") == 0;
	}

}