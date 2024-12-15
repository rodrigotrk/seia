package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "porte_competencia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PorteCompetencia.findAll", query = "SELECT p FROM PorteCompetencia p"),
    @NamedQuery(name = "PorteCompetencia.findByIdeTipologiaGrupo", query = "SELECT p FROM PorteCompetencia p WHERE p.porteCompetenciaPK.ideTipologiaGrupo = :ideTipologiaGrupo"),
    @NamedQuery(name = "PorteCompetencia.findByIdeNivelCompetencia", query = "SELECT p FROM PorteCompetencia p WHERE p.porteCompetenciaPK.ideNivelCompetencia = :ideNivelCompetencia"),
    @NamedQuery(name = "PorteCompetencia.findByIdePorte", query = "SELECT p FROM PorteCompetencia p WHERE p.porteCompetenciaPK.idePorte = :idePorte")})
public class PorteCompetencia implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PorteCompetenciaPK porteCompetenciaPK;
    @JoinColumn(name = "ide_tipologia_grupo", referencedColumnName = "ide_tipologia_grupo", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TipologiaGrupo tipologiaGrupo;
    @JoinColumn(name = "ide_porte", referencedColumnName = "ide_porte", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Porte porte;
    @JoinColumn(name = "ide_nivel_competencia", referencedColumnName = "ide_nivel_competencia", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private NivelCompetencia nivelCompetencia;

    public PorteCompetencia() {
    }

    public PorteCompetencia(PorteCompetenciaPK porteCompetenciaPK) {
        this.porteCompetenciaPK = porteCompetenciaPK;
    }

    public PorteCompetencia(int ideTipologiaGrupo, int ideNivelCompetencia, int idePorte) {
        this.porteCompetenciaPK = new PorteCompetenciaPK(ideTipologiaGrupo, ideNivelCompetencia, idePorte);
    }

    public PorteCompetenciaPK getPorteCompetenciaPK() {
        return porteCompetenciaPK;
    }

    public void setPorteCompetenciaPK(PorteCompetenciaPK porteCompetenciaPK) {
        this.porteCompetenciaPK = porteCompetenciaPK;
    }

    public TipologiaGrupo getTipologiaGrupo() {
        return tipologiaGrupo;
    }

    public void setTipologiaGrupo(TipologiaGrupo tipologiaGrupo) {
        this.tipologiaGrupo = tipologiaGrupo;
    }

    public Porte getPorte() {
        return porte;
    }

    public void setPorte(Porte porte) {
        this.porte = porte;
    }

    public NivelCompetencia getNivelCompetencia() {
        return nivelCompetencia;
    }

    public void setNivelCompetencia(NivelCompetencia nivelCompetencia) {
        this.nivelCompetencia = nivelCompetencia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (porteCompetenciaPK != null ? porteCompetenciaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof PorteCompetencia)) {
            return false;
        }
        PorteCompetencia other = (PorteCompetencia) object;
        if ((this.porteCompetenciaPK == null && other.porteCompetenciaPK != null) || (this.porteCompetenciaPK != null && !this.porteCompetenciaPK.equals(other.porteCompetenciaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.PorteCompetencia[ porteCompetenciaPK=" + porteCompetenciaPK + " ]";
    }
    
}
