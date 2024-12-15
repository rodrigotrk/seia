package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "empreendimento_tipologia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EmpreendimentoTipologia.findAll", query = "SELECT e FROM EmpreendimentoTipologia e"),
    @NamedQuery(name = "EmpreendimentoTipologia.findByIdeEmpreendimento", query = "SELECT e FROM EmpreendimentoTipologia e WHERE e.empreendimentoTipologiaPK.ideEmpreendimento = :ideEmpreendimento"),
    @NamedQuery(name = "EmpreendimentoTipologia.findByIdeTipologiaGrupo", query = "SELECT e FROM EmpreendimentoTipologia e WHERE e.empreendimentoTipologiaPK.ideTipologiaGrupo = :tipologiaGrupo"),
    @NamedQuery(name = "EmpreendimentoTipologia.findByIndPermanente", query = "SELECT e FROM EmpreendimentoTipologia e WHERE e.indPermanente = :indPermanente"),
    @NamedQuery(name = "EmpreendimentoTipologia.delete", query = "delete from EmpreendimentoTipologia et where et.empreendimentoTipologiaPK.ideEmpreendimento = :ideEmpreendimento and et.empreendimentoTipologiaPK.ideTipologiaGrupo = :ideTipologiaGrupo"),
})
public class EmpreendimentoTipologia implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@EmbeddedId
    protected EmpreendimentoTipologiaPK empreendimentoTipologiaPK;
    
	@Basic(optional = false)
    @NotNull
    @Column(name = "ind_permanente", nullable = false)
    private boolean indPermanente;
    
	@JoinColumn(name = "ide_tipologia_grupo", referencedColumnName = "ide_tipologia_grupo", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TipologiaGrupo tipologiaGrupo;
    
	@JoinColumn(name = "ide_empreendimento", referencedColumnName = "ide_empreendimento", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Empreendimento empreendimento;

    public EmpreendimentoTipologia() {}

    public EmpreendimentoTipologia(EmpreendimentoTipologiaPK empreendimentoTipologiaPK) {
        this.empreendimentoTipologiaPK = empreendimentoTipologiaPK;
    }

    public EmpreendimentoTipologia(EmpreendimentoTipologiaPK empreendimentoTipologiaPK, boolean indPermanente) {
        this.empreendimentoTipologiaPK = empreendimentoTipologiaPK;
        this.indPermanente = indPermanente;
    }

    public EmpreendimentoTipologia(int ideEmpreendimento, int ideTipologiaGrupo) {
        this.empreendimentoTipologiaPK = new EmpreendimentoTipologiaPK(ideEmpreendimento, ideTipologiaGrupo);
    }

    public EmpreendimentoTipologiaPK getEmpreendimentoTipologiaPK() {
        return empreendimentoTipologiaPK;
    }

    public void setEmpreendimentoTipologiaPK(EmpreendimentoTipologiaPK empreendimentoTipologiaPK) {
        this.empreendimentoTipologiaPK = empreendimentoTipologiaPK;
    }

    public EmpreendimentoTipologia(boolean indPermanente) {
		super();
		this.indPermanente = indPermanente;
	}

	public boolean getIndPermanente() {
        return indPermanente;
    }

    public void setIndPermanente(boolean indPermanente) {
        this.indPermanente = indPermanente;
    }

    public TipologiaGrupo getTipologiaGrupo() {
        return tipologiaGrupo;
    }

    public void setTipologiaGrupo(TipologiaGrupo tipologiaGrupo) {
        this.tipologiaGrupo = tipologiaGrupo;
    }

    public Empreendimento getEmpreendimento() {
        return empreendimento;
    }

    public void setEmpreendimento(Empreendimento empreendimento) {
        this.empreendimento = empreendimento;
    }

    @Transient
    public String getDescricaoTipoTipologia() {

    	if (getIndPermanente())
    		return "Permanente";
    	else
    		return "Temporária";
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (empreendimentoTipologiaPK != null ? empreendimentoTipologiaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof EmpreendimentoTipologia)) {
            return false;
        }
        EmpreendimentoTipologia other = (EmpreendimentoTipologia) object;
        if ((this.empreendimentoTipologiaPK == null && other.empreendimentoTipologiaPK != null) || (this.empreendimentoTipologiaPK != null && !this.empreendimentoTipologiaPK.equals(other.empreendimentoTipologiaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.EmpreendimentoTipologia[ empreendimentoTipologiaPK=" + empreendimentoTipologiaPK + " ]";
    }
}