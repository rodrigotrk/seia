package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.gov.ba.seia.util.Util;


/**
 * The persistent class for the silos_armazens_responsavel_tecnico database table.
 * 
 */
@Entity
@Table(name="silos_armazens_responsavel_tecnico")
@NamedQueries({
	@NamedQuery(name = "SilosArmazensResponsavelTecnico.removerByIdeSilos", query ="DELETE FROM SilosArmazensResponsavelTecnico s WHERE s.silosArmazen = :ideSilosArmazen") })
public class SilosArmazensResponsavelTecnico implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SILOS_ARMAZENS_RESPONSAVEL_TECNICO_IDESILOSARMAZENSRESPONSAVELTECNICO_GENERATOR", sequenceName="SILOS_ARMAZENS_RESPONSAVEL_TECNICO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SILOS_ARMAZENS_RESPONSAVEL_TECNICO_IDESILOSARMAZENSRESPONSAVELTECNICO_GENERATOR")
	@Column(name="ide_silos_armazens_responsavel_tecnico")
	private Integer ideSilosArmazensResponsavelTecnico;

	@Temporal(TemporalType.DATE)
	@Column(name="dtc_criacao")
	private Date dtcCriacao;

	@ManyToOne
	@JoinColumn(name = "ide_pessoa_fisica")
	private PessoaFisica idePessoaFisica;

	//bi-directional many-to-one association to SilosArmazen
	@ManyToOne
	@JoinColumn(name="ide_silos_armazens")
	private SilosArmazen silosArmazen;
	
	@Transient
	private String numeroCarteiraConselho;
	
	@Transient
	private List<FormacaoProfissional> listaFormacaoProfissional;

	public SilosArmazensResponsavelTecnico() {
	}
	
	public SilosArmazensResponsavelTecnico(SilosArmazen silosArmazen,ResponsavelEmpreendimento empreendimento){
		this.silosArmazen = silosArmazen;
		this.idePessoaFisica = empreendimento.getIdePessoaFisica();
	}
	
	public Integer getIdeSilosArmazensResponsavelTecnico() {
		return this.ideSilosArmazensResponsavelTecnico;
	}

	public void setIdeSilosArmazensResponsavelTecnico(Integer ideSilosArmazensResponsavelTecnico) {
		this.ideSilosArmazensResponsavelTecnico = ideSilosArmazensResponsavelTecnico;
	}

	public Date getDtcCriacao() {
		return this.dtcCriacao;
	}

	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}

	public PessoaFisica getIdePessoaFisica() {
		return idePessoaFisica;
	}

	public void setIdePessoaFisica(PessoaFisica idePessoaFisica) {
		this.idePessoaFisica = idePessoaFisica;
	}

	public SilosArmazen getSilosArmazen() {
		return this.silosArmazen;
	}

	public void setSilosArmazen(SilosArmazen silosArmazen) {
		this.silosArmazen = silosArmazen;
	}

	public String getNumeroCarteiraConselho() {
		return numeroCarteiraConselho;
	}

	public void setNumeroCarteiraConselho(String numeroCarteiraConselho) {
		this.numeroCarteiraConselho = numeroCarteiraConselho;
	}

	public List<FormacaoProfissional> getListaFormacaoProfissional() {
		return listaFormacaoProfissional;
	}

	public void setListaFormacaoProfissional(
			List<FormacaoProfissional> listaFormacaoProfissional) {
		this.listaFormacaoProfissional = listaFormacaoProfissional;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideSilosArmazensResponsavelTecnico == null) ? 0
						: ideSilosArmazensResponsavelTecnico.hashCode());
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
		SilosArmazensResponsavelTecnico other = (SilosArmazensResponsavelTecnico) obj;
		if (ideSilosArmazensResponsavelTecnico == null) {
			if (other.ideSilosArmazensResponsavelTecnico != null)
				return false;
		} else if (!ideSilosArmazensResponsavelTecnico
				.equals(other.ideSilosArmazensResponsavelTecnico))
			return false;
		
		if(this.idePessoaFisica != null && other.idePessoaFisica != null){
			if(this.idePessoaFisica != other.idePessoaFisica){
				return false;
			}
		}
		return true;
	}

	public boolean isExisteCarteiraConselho() {
		return !Util.isNullOuVazio(this.numeroCarteiraConselho) && !this.numeroCarteiraConselho.equals("Não informado");
	}
}