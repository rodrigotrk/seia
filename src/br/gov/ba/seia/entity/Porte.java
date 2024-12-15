package br.gov.ba.seia.entity;

import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.enumerator.PorteEnum;

@Entity
@Table(name = "porte", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_porte"}),
    @UniqueConstraint(columnNames = {"sgl_porte"})})
@XmlRootElement
@NamedQueries({ 
    @NamedQuery(name = "Porte.findAll", query = "SELECT p FROM Porte p"),
    @NamedQuery(name = "Porte.findByAtivos", query = "SELECT p FROM Porte p WHERE indAtivo = true"),
    @NamedQuery(name = "Porte.findDifIndentificado", query = "SELECT p FROM Porte p WHERE p.idePorte <> :idePorte"),
    @NamedQuery(name = "Porte.findByIdePorte", query = "SELECT p FROM Porte p WHERE p.idePorte = :idePorte"),
    @NamedQuery(name = "Porte.findBySglPorte", query = "SELECT p FROM Porte p WHERE p.sglPorte = :sglPorte"),
    @NamedQuery(name = "Porte.findByNomPorte", query = "SELECT p FROM Porte p WHERE p.nomPorte = :nomPorte"),
    @NamedQuery(name = "Porte.findByAtividades", query = "SELECT p FROM Porte p right join p.porteTipologiaCollection pt left join pt.ideTipologiaGrupo tg WHERE tg.ideTipologiaGrupo = :ideTipologiaGrupo and :valor between coalesce(pt.valMinimo, 0) and coalesce(pt.valMaximo, (:valor +1)) order by p.idePorte desc")})
public class Porte extends AbstractEntity {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @Basic(optional = false)
    @Column(name = "ide_porte", nullable = false)
    private Integer idePorte;
    
	@Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "sgl_porte", nullable = false, length = 2)
    private String sglPorte;
    
	@Column(name = "nom_porte", nullable = false, length = 15)
    private String nomPorte;
    
	@Column(name = "ind_ativo", nullable = true)
    private Boolean indAtivo;
 
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idePorte", fetch = FetchType.LAZY)
    private Collection<InstrumentoPorte> instrumentoPorteCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idePorte", fetch = FetchType.LAZY)
    private Collection<PorteTipologia> porteTipologiaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "porte", fetch = FetchType.LAZY)
    private Collection<PorteCompetencia> porteCompetenciaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idePorte", fetch = FetchType.LAZY)
    private Collection<RequerimentoUnico> requerimentoUnicoCollection;
    @Transient
    private Integer idTipologiaGrupo;

    @Transient
    private PorteTipologia porteTipologia = new PorteTipologia();

    public Porte() {
    }

    public Porte(Integer idePorte) {
        this.idePorte = idePorte;
    }

    public Porte(Integer idePorte, String sglPorte, String nomPorte) {
        this.idePorte = idePorte;
        this.sglPorte = sglPorte;
        this.nomPorte = nomPorte;
    }

    public Porte(Integer idePorte, String nomPorte) {
		super();
		this.idePorte = idePorte;
		this.nomPorte = nomPorte;
	}

	public Integer getIdePorte() {
        return idePorte;
    }

    public void setIdePorte(Integer idePorte) {
        this.idePorte = idePorte;
    }

    public String getSglPorte() {
        return sglPorte;
    }

    public void setSglPorte(String sglPorte) {
        this.sglPorte = sglPorte;
    }

    public String getNomPorte() {
        return nomPorte;
    }

    public void setNomPorte(String nomPorte) {
        this.nomPorte = nomPorte;
    }

    @XmlTransient
    public Collection<InstrumentoPorte> getInstrumentoPorteCollection() {
        return instrumentoPorteCollection;
    }

    public void setInstrumentoPorteCollection(Collection<InstrumentoPorte> instrumentoPorteCollection) {
        this.instrumentoPorteCollection = instrumentoPorteCollection;
    }

    @XmlTransient
    public Collection<PorteTipologia> getPorteTipologiaCollection() {
        return porteTipologiaCollection;
    }

    public void setPorteTipologiaCollection(Collection<PorteTipologia> porteTipologiaCollection) {
        this.porteTipologiaCollection = porteTipologiaCollection;
    }

    @XmlTransient
    public Collection<PorteCompetencia> getPorteCompetenciaCollection() {
        return porteCompetenciaCollection;
    }

    public void setPorteCompetenciaCollection(Collection<PorteCompetencia> porteCompetenciaCollection) {
        this.porteCompetenciaCollection = porteCompetenciaCollection;
    }

    @XmlTransient
    public Collection<RequerimentoUnico> getRequerimentoUnicoCollection() {
        return requerimentoUnicoCollection;
    }

    public void setRequerimentoUnicoCollection(Collection<RequerimentoUnico> requerimentoUnicoCollection) {
        this.requerimentoUnicoCollection = requerimentoUnicoCollection;
    }

    public Integer getIdTipologiaGrupo() {
		return idTipologiaGrupo;
	}

	public void setIdTipologiaGrupo(Integer idTipologiaGrupo) {
		this.idTipologiaGrupo = idTipologiaGrupo;
	}
	
	public boolean isIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(boolean indAtivo) {
		this.indAtivo = indAtivo;
	}
	
	public PorteTipologia getPorteTipologia() {
		return porteTipologia;
	}

	public void setPorteTipologia(PorteTipologia porteTipologia) {
		this.porteTipologia = porteTipologia;
	}

	public boolean isNi() {
		return PorteEnum.NAO_IDENTIFICADO.getId().equals(this.idePorte);
	}
	
	public boolean isPequeno() {
		return PorteEnum.PEQUENO.getId().equals(this.idePorte);
	}
	
	public boolean isMedio() {
		return PorteEnum.MEDIO.getId().equals(this.idePorte);
	}
}