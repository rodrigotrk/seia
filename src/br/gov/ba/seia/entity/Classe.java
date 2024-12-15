package br.gov.ba.seia.entity;

import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name = "classe")
public class Classe extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@Column(name = "ide_classe")
	private Integer ideClasse;

	@Basic(optional = false)
	@Column(name = "nom_classe")
	private String nomClasse;

	@Column(name = "ind_ativo")
	private Boolean indAtivo;

	@OneToMany(mappedBy = "ideClasse")
	private Collection<EmpreendimentoRequerimento> empreendimentoRequerimentoCollection;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "classe")
	private Collection<ClassePortePotencialPoluicao> classePortePotencialPoluicaoCollection;

	@Transient
	private Boolean isSalvarNull;

	public Classe() {
	}

	public Classe(Integer ideClasse) {
		this.ideClasse = ideClasse;
	}

	public Classe(Integer ideClasse, String nomClasse) {
		this.ideClasse = ideClasse;
		this.nomClasse = nomClasse;
	}

	public Integer getIdeClasse() {
		return ideClasse;
	}

	public void setIdeClasse(Integer ideClasse) {
		this.ideClasse = ideClasse;
	}

	public String getNomClasse() {
		return nomClasse;
	}

	public void setNomClasse(String nomClasse) {
		this.nomClasse = nomClasse;
	}

	public Boolean getIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public Collection<EmpreendimentoRequerimento> getEmpreendimentoRequerimentoCollection() {
		return empreendimentoRequerimentoCollection;
	}

	public void setEmpreendimentoRequerimentoCollection(
			Collection<EmpreendimentoRequerimento> empreendimentoRequerimentoCollection) {
		this.empreendimentoRequerimentoCollection = empreendimentoRequerimentoCollection;
	}

	public Collection<ClassePortePotencialPoluicao> getClassePortePotencialPoluicaoCollection() {
		return classePortePotencialPoluicaoCollection;
	}

	public void setClassePortePotencialPoluicaoCollection(
			Collection<ClassePortePotencialPoluicao> classePortePotencialPoluicaoCollection) {
		this.classePortePotencialPoluicaoCollection = classePortePotencialPoluicaoCollection;
	}

	@Transient
	public Boolean isSalvarNull() {
		if (isSalvarNull == null) {
			return false;
		}

		return isSalvarNull;
	}

	@Transient
	public void setSalvarNull(Boolean isSalvarNull) {
		this.isSalvarNull = isSalvarNull;
	}
}
