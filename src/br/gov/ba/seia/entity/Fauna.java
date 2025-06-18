package br.gov.ba.seia.entity;

import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.entity.generic.AbstractEntity;

/**
 * 
 * @author joao.maciel
 * 
 **/
@Entity
@Table(name = "fauna")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Fauna.findByIdeRequerimento", query = "SELECT f FROM Fauna f inner join f.ideRequerimento r WHERE r.ideRequerimento = :ideRequerimento") })
public class Fauna extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fauna_ide_fauna_seq")
	@SequenceGenerator(name = "fauna_ide_fauna_seq", sequenceName = "fauna_ide_fauna_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "ide_fauna", nullable = false)
	private Integer ideFauna;

	@JoinColumn(name = "ide_tipo_solicitacao", referencedColumnName = "ide_tipo_solicitacao", nullable = true)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private TipoSolicitacao ideTipoSolicitacao;

	@JoinColumn(name = "ide_requerimento", referencedColumnName = "ide_requerimento", nullable = true)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Requerimento ideRequerimento;

    @Column(name = "ind_excluido")
    private boolean indExcluido;
    
	public boolean isIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	@JoinTable(name = "fauna_objetivo_atividade_manejo", joinColumns = { @JoinColumn(name = "ide_fauna", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ide_objetivo_atividade_manejo", nullable = false, updatable = false) })
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	private Collection<ObjetivoAtividadeManejo> objetivoAtividadeManejoCollection;

	@JoinTable(name = "fauna_tipo_atividade_fauna", joinColumns = { @JoinColumn(name = "ide_fauna", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ide_tipo_atividade_fauna", nullable = false, updatable = false) })
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	private Collection<TipoAtividadeFauna> tipoAtividadeFaunaCollection;

	@JoinTable(name = "fauna_tipo_criadouro_fauna", joinColumns = { @JoinColumn(name = "ide_fauna", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ide_tipo_criadouro_fauna", nullable = false, updatable = false) })
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	private Collection<TipoCriadouroFauna> tipoCriadouroFaunaCollection;
	
	public Fauna() {
	}

	public Fauna(Requerimento ideRequerimento) {
		super();
		this.ideRequerimento = ideRequerimento;
	}

	public Integer getIdeFauna() {
		return ideFauna;
	}

	public void setIdeFauna(Integer ideFauna) {
		this.ideFauna = ideFauna;
	}

	public TipoSolicitacao getIdeTipoSolicitacao() {
		return ideTipoSolicitacao;
	}

	public void setIdeTipoSolicitacao(TipoSolicitacao ideTipoSolicitacao) {
		this.ideTipoSolicitacao = ideTipoSolicitacao;
	}

	public Requerimento getIdeRequerimento() {
		return ideRequerimento;
	}

	public void setIdeRequerimento(Requerimento ideRequerimento) {
		this.ideRequerimento = ideRequerimento;
	}

	@XmlTransient
	public Collection<TipoAtividadeFauna> getTipoAtividadeFaunaCollection() {
		return tipoAtividadeFaunaCollection;
	}

	public void setTipoAtividadeFaunaCollection(Collection<TipoAtividadeFauna> tipoAtividadeFaunaCollection) {
		this.tipoAtividadeFaunaCollection = tipoAtividadeFaunaCollection;
	}

	@XmlTransient
	public Collection<ObjetivoAtividadeManejo> getObjetivoAtividadeManejoCollection() {
		return objetivoAtividadeManejoCollection;
	}

	public void setObjetivoAtividadeManejoCollection(
			Collection<ObjetivoAtividadeManejo> objetivoAtividadeManejoCollection) {
		this.objetivoAtividadeManejoCollection = objetivoAtividadeManejoCollection;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideFauna == null) ? 0 : ideFauna.hashCode());
		result = prime * result + ((ideRequerimento == null) ? 0 : ideRequerimento.hashCode());
		result = prime * result + ((ideTipoSolicitacao == null) ? 0 : ideTipoSolicitacao.hashCode());
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
		Fauna other = (Fauna) obj;
		if (ideFauna == null) {
			if (other.ideFauna != null)
				return false;
		} else if (!ideFauna.equals(other.ideFauna))
			return false;
		if (ideRequerimento == null) {
			if (other.ideRequerimento != null)
				return false;
		} else if (!ideRequerimento.equals(other.ideRequerimento))
			return false;
		if (ideTipoSolicitacao == null) {
			if (other.ideTipoSolicitacao != null)
				return false;
		} else if (!ideTipoSolicitacao.equals(other.ideTipoSolicitacao))
			return false;
		return true;
	}

	public Collection<TipoCriadouroFauna> getTipoCriadouroFaunaCollection() {
		return tipoCriadouroFaunaCollection;
	}

	public void setTipoCriadouroFaunaCollection(
			Collection<TipoCriadouroFauna> tipoCriadouroFaunaCollection) {
		this.tipoCriadouroFaunaCollection = tipoCriadouroFaunaCollection;
	}
}
