package br.gov.ba.seia.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name = "programa_governo")
@XmlRootElement
public class ProgramaGoverno extends AbstractEntity {

	private static final long serialVersionUID = -4870503125943011546L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "programa_governo_ide_programa_governo_seq")
	@SequenceGenerator(name = "programa_governo_ide_programa_governo_seq", sequenceName = "programa_governo_ide_programa_governo_seq", allocationSize = 1)
	@NotNull
	@Basic(optional = false)
	@Column(name = "ide_programa_governo", nullable = false)
	private Integer ideProgramaGoverno;

	@NotNull
	@Basic(optional = false)
	@Column(name = "nom_programa_governo", nullable = false, length = 100)
	private String nomProgramaGoverno;
	
	@NotNull
	@Basic(optional = false)
	@Column(name = "ind_ativo", nullable = false)
	private boolean indAtivo;
	
	public ProgramaGoverno() {}
	
	public ProgramaGoverno(Integer ideProgramaGoverno, String nomProgramaGoverno, boolean indAtivo) {
		super();
		this.ideProgramaGoverno = ideProgramaGoverno;
		this.nomProgramaGoverno = nomProgramaGoverno;
		this.indAtivo = indAtivo;
	}
	
	public Integer getIdeProgramaGoverno() {
		return ideProgramaGoverno;
	}
	
	public void setIdeProgramaGoverno(Integer ideProgramaGoverno) {
		this.ideProgramaGoverno = ideProgramaGoverno;
	}

	public String getNomProgramaGoverno() {
		return nomProgramaGoverno;
	}
	
	public void setNomProgramaGoverno(String nomProgramaGoverno) {
		this.nomProgramaGoverno = nomProgramaGoverno;
	}
	
	public boolean isIndAtivo() {
		return indAtivo;
	}
	
	public void setIndAtivo(boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	@Override
	public String toString() {
		return "ProgramaGoverno [ideProgramaGoverno=" + ideProgramaGoverno + ", nomProgramaGoverno=" + nomProgramaGoverno + ", indAtivo=" + indAtivo + "]";
	}
}