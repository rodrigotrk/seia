package br.gov.ba.seia.entity;

import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.entity.generic.AbstractEntity;

/**
 * 
 * @author joao.maciel
 * 
 */

@Entity
@Table(name = "objetivo_atividade_manejo")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "ObjetivoAtividadeManejo.findAll", query = "SELECT t FROM ObjetivoAtividadeManejo t"),
		@NamedQuery(name = "ObjetivoAtividadeManejo.findByIdeFauna", query = "SELECT o FROM ObjetivoAtividadeManejo o INNER JOIN o.faunaCollection f WHERE f.ideFauna = :ideFauna"),
		@NamedQuery(name = "ObjetivoAtividadeManejo.findByIdeObjetivoAtividadeManejo", query = "SELECT t FROM ObjetivoAtividadeManejo t WHERE t.ideObjetivoAtividadeManejo = :ideObjetivoAtividadeManejo"),
		@NamedQuery(name = "ObjetivoAtividadeManejo.findByNoObjetivoAtividadeManejoo", query = "SELECT t FROM ObjetivoAtividadeManejo t WHERE t.nomObjetivoAtividadeManejo = :nomObjetivoAtividadeManejo") })
public class ObjetivoAtividadeManejo extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "objetivo_atividade_manejo_ide_objetivo_atividade_manejo_generator", sequenceName = "objetivo_atividade_manejo_ide_objetivo_atividade_manejo_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "objetivo_atividade_manejo_ide_objetivo_atividade_manejo_generator")
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_objetivo_atividade_manejo", nullable = false)
	private Integer ideObjetivoAtividadeManejo;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 150)
	@Column(name = "nom_objetivo_atividade_manejo")
	private String nomObjetivoAtividadeManejo;

	@Basic(optional = false)
	@Column(name = "ind_ativo")
	private boolean indAtivo;

	@ManyToMany(mappedBy = "objetivoAtividadeManejoCollection", fetch = FetchType.LAZY)
	private Collection<Fauna> faunaCollection;

	public boolean isIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public ObjetivoAtividadeManejo(Integer ideObjetivoAtividadeManejo, String nomObjetivoAtividadeManejo) {
		this.ideObjetivoAtividadeManejo = ideObjetivoAtividadeManejo;
		this.nomObjetivoAtividadeManejo = nomObjetivoAtividadeManejo;
	}

	public ObjetivoAtividadeManejo() {
	}

	public Integer getIdeObjetivoAtividadeManejo() {
		return ideObjetivoAtividadeManejo;
	}

	public void setIdeObjetivoAtividadeManejo(Integer ideObjetivoAtividadeManejo) {
		this.ideObjetivoAtividadeManejo = ideObjetivoAtividadeManejo;
	}

	public String getNomObjetivoAtividadeManejo() {
		return nomObjetivoAtividadeManejo;
	}

	public void setNomObjetivoAtividadeManejo(String nomObjetivoAtividadeManejo) {
		this.nomObjetivoAtividadeManejo = nomObjetivoAtividadeManejo;
	}

	@XmlTransient
	public Collection<Fauna> getFaunaCollection() {
		return faunaCollection;
	}

	public void setFaunaCollection(Collection<Fauna> faunaCollection) {
		this.faunaCollection = faunaCollection;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideObjetivoAtividadeManejo == null) ? 0 : ideObjetivoAtividadeManejo.hashCode());
		result = prime * result + (indAtivo ? 1231 : 1237);
		result = prime * result + ((nomObjetivoAtividadeManejo == null) ? 0 : nomObjetivoAtividadeManejo.hashCode());
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
		ObjetivoAtividadeManejo other = (ObjetivoAtividadeManejo) obj;
		if (ideObjetivoAtividadeManejo == null) {
			if (other.ideObjetivoAtividadeManejo != null)
				return false;
		} else if (!ideObjetivoAtividadeManejo.equals(other.ideObjetivoAtividadeManejo))
			return false;
		if (indAtivo != other.indAtivo)
			return false;
		if (nomObjetivoAtividadeManejo == null) {
			if (other.nomObjetivoAtividadeManejo != null)
				return false;
		} else if (!nomObjetivoAtividadeManejo.equals(other.nomObjetivoAtividadeManejo))
			return false;
		return true;
	}
}