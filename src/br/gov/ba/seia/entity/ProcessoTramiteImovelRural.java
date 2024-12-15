package br.gov.ba.seia.entity;



import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author micael.coutinho
 */
@Entity
@Table(name = "processo_tramite_imovel_rural")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProcessoTramiteImovelRural.findAll", query = "SELECT p FROM ProcessoTramiteImovelRural p"),
    @NamedQuery(name = "ProcessoTramiteImovelRural.findByIdeProcessoTramiteImovelRural", query = "SELECT p FROM ProcessoTramiteImovelRural p WHERE p.ideProcessoTramiteImovelRural = :ideProcessoTramiteImovelRural"),
    @NamedQuery(name = "ProcessoTramiteImovelRural.findByNumProcessoTramiteImovelRural", query = "SELECT p FROM ProcessoTramiteImovelRural p WHERE p.numProcessoTramiteImovelRural = :numProcessoTramiteImovelRural"),
    @NamedQuery(name = "ProcessoTramiteImovelRural.findByIdeImovelRural", query = "SELECT p FROM ProcessoTramiteImovelRural p WHERE p.ideImovelRural = :ideImovelRural"),
    @NamedQuery(name = "ProcessoTramiteImovelRural.findByDtcCriacao", query = "SELECT p FROM ProcessoTramiteImovelRural p WHERE p.dtcCriacao = :dtcCriacao")})
public class ProcessoTramiteImovelRural implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="PROCESSO_TRAMITE_IDE_PROCESSO_TRAMITE_SEQ", sequenceName="PROCESSO_TRAMITE_IDE_PROCESSO_TRAMITE_IMOVEL_RURAL_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PROCESSO_TRAMITE_IDE_PROCESSO_TRAMITE_SEQ")
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_processo_tramite_imovel_rural")
    private Integer ideProcessoTramiteImovelRural;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "num_processo_tramite_imovel_rural")
    private String numProcessoTramiteImovelRural;
    @Column(name = "dtc_criacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcCriacao;
    @JoinColumn(name = "ide_imovel_rural", referencedColumnName = "ide_imovel_rural")
    @ManyToOne(optional = false)
    private ImovelRural ideImovelRural;

    public ProcessoTramiteImovelRural() {
    }

    public ProcessoTramiteImovelRural(Integer ideProcessoTramiteImovelRural) {
        this.ideProcessoTramiteImovelRural = ideProcessoTramiteImovelRural;
    }

    public ProcessoTramiteImovelRural(Integer ideProcessoTramiteImovelRural, String numProcessoTramiteImovelRural) {
        this.ideProcessoTramiteImovelRural = ideProcessoTramiteImovelRural;
        this.numProcessoTramiteImovelRural = numProcessoTramiteImovelRural;
    }

    public Integer getIdeProcessoTramiteImovelRural() {
        return ideProcessoTramiteImovelRural;
    }

    public void setIdeProcessoTramiteImovelRural(Integer ideProcessoTramiteImovelRural) {
        this.ideProcessoTramiteImovelRural = ideProcessoTramiteImovelRural;
    }

    public String getNumProcessoTramiteImovelRural() {
        return numProcessoTramiteImovelRural;
    }

    public void setNumProcessoTramiteImovelRural(String numProcessoTramiteImovelRural) {
        this.numProcessoTramiteImovelRural = numProcessoTramiteImovelRural;
    }

    public Date getDtcCriacao() {
        return dtcCriacao;
    }

    public void setDtcCriacao(Date dtcCriacao) {
        this.dtcCriacao = dtcCriacao;
    }

    public ImovelRural getIdeImovelRural() {
        return ideImovelRural;
    }

    public void setIdeImovelRural(ImovelRural ideImovelRural) {
        this.ideImovelRural = ideImovelRural;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideProcessoTramiteImovelRural != null ? ideProcessoTramiteImovelRural.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ProcessoTramiteImovelRural)) {
            return false;
        }
        ProcessoTramiteImovelRural other = (ProcessoTramiteImovelRural) object;
        if ((this.ideProcessoTramiteImovelRural == null && other.ideProcessoTramiteImovelRural != null) || (this.ideProcessoTramiteImovelRural != null && !this.ideProcessoTramiteImovelRural.equals(other.ideProcessoTramiteImovelRural))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ProcessoTramiteImovelRural[ ideProcessoTramiteImovelRural=" + ideProcessoTramiteImovelRural + " ]";
    }
    
}
