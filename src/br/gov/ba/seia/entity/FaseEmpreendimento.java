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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name = "fase_empreendimento", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_fase_empreendimento"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FaseEmpreendimento.findAll", query = "SELECT f FROM FaseEmpreendimento f order by f.nomFaseEmpreendimento"),
    @NamedQuery(name = "FaseEmpreendimento.findByIdeFaseEmpreendimento", query = "SELECT f FROM FaseEmpreendimento f WHERE f.ideFaseEmpreendimento = :ideFaseEmpreendimento"),
    @NamedQuery(name = "FaseEmpreendimento.findByNomFaseEmpreendimento", query = "SELECT f FROM FaseEmpreendimento f WHERE f.nomFaseEmpreendimento = :nomFaseEmpreendimento")})
public class FaseEmpreendimento extends AbstractEntity {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="FASE_EMPREENDIMENTO_IDEFASEEMPREENDIMENTO_GENERATOR", sequenceName="FASE_EMPREENDIMENTO_IDE_FASE_EMPREENDIMENTO_SEQ")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FASE_EMPREENDIMENTO_IDEFASEEMPREENDIMENTO_GENERATOR")    
    @NotNull
    @Column(name = "ide_fase_empreendimento", nullable = false)
    private Integer ideFaseEmpreendimento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "nom_fase_empreendimento", nullable = false, length = 20)
    private String nomFaseEmpreendimento;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideFaseEmpreendimento", fetch = FetchType.LAZY)
    private Collection<RequerimentoUnico> requerimentoUnicoCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideFaseEmpreendimento", fetch = FetchType.LAZY)
    private Collection<EmpreendimentoRequerimento> empreendimentoRequerimentoCollection;

    public FaseEmpreendimento() {
    }

    public FaseEmpreendimento(Integer ideFaseEmpreendimento) {
        this.ideFaseEmpreendimento = ideFaseEmpreendimento;
    }

    public FaseEmpreendimento(Integer ideFaseEmpreendimento, String nomFaseEmpreendimento) {
        this.ideFaseEmpreendimento = ideFaseEmpreendimento;
        this.nomFaseEmpreendimento = nomFaseEmpreendimento;
    }

    public Integer getIdeFaseEmpreendimento() {
        return ideFaseEmpreendimento;
    }

    public void setIdeFaseEmpreendimento(Integer ideFaseEmpreendimento) {
        this.ideFaseEmpreendimento = ideFaseEmpreendimento;
    }

    public String getNomFaseEmpreendimento() {
        return nomFaseEmpreendimento;
    }

    public void setNomFaseEmpreendimento(String nomFaseEmpreendimento) {
        this.nomFaseEmpreendimento = nomFaseEmpreendimento;
    }

    @XmlTransient
    public Collection<RequerimentoUnico> getRequerimentoUnicoCollection() {
        return requerimentoUnicoCollection;
    }

    public void setRequerimentoUnicoCollection(Collection<RequerimentoUnico> requerimentoUnicoCollection) {
        this.requerimentoUnicoCollection = requerimentoUnicoCollection;
    }

	/**
	 * @return the empreendimentoRequerimentoCollection
	 */
	public Collection<EmpreendimentoRequerimento> getEmpreendimentoRequerimentoCollection() {
		return empreendimentoRequerimentoCollection;
	}

	/**
	 * @param empreendimentoRequerimentoCollection the empreendimentoRequerimentoCollection to set
	 */
	public void setEmpreendimentoRequerimentoCollection(
			Collection<EmpreendimentoRequerimento> empreendimentoRequerimentoCollection) {
		this.empreendimentoRequerimentoCollection = empreendimentoRequerimentoCollection;
	}
}
