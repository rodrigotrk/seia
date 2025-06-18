package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "controle_fluxo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ControleFluxo.findAll", query = "SELECT c FROM ControleFluxo c"),
    @NamedQuery(name = "ControleFluxo.findByIndMovimentacaoAutomatica", query = "SELECT c FROM ControleFluxo c WHERE c.indMovimentacaoAutomatica = :indMovimentacaoAutomatica"),
    @NamedQuery(name = "ControleFluxo.findByIdeControleFluxo", query = "SELECT c FROM ControleFluxo c WHERE c.ideControleFluxo = :ideControleFluxo"),
    @NamedQuery(name = "ControleFluxo.findByIdeGrupoProcessoIdeStatusAnterior", query = "SELECT c FROM ControleFluxo c WHERE c.ideGrupoProcesso.ideGrupoProcesso = :ideGrupoProcesso AND c.ideStatusAnterior.ideStatusFluxo = :ideStatusAnterior")})
public class ControleFluxo implements Serializable {
   
	private static final long serialVersionUID = 1L;
    
	@Basic(optional = false)
    @NotNull
    @Column(name = "ind_movimentacao_automatica", nullable = false)
    private boolean indMovimentacaoAutomatica;
    
	@Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_controle_fluxo", nullable = false)
    private Integer ideControleFluxo;
    
	@JoinColumn(name = "ide_tipo_ato", referencedColumnName = "ide_tipo_ato")
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoAto ideTipoAto;
    
	@JoinColumn(name = "ide_status_posterior", referencedColumnName = "ide_status_fluxo", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private StatusFluxo ideStatusPosterior;
    
	@JoinColumn(name = "ide_status_anterior", referencedColumnName = "ide_status_fluxo", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private StatusFluxo ideStatusAnterior;
    
	@JoinColumn(name = "ide_grupo_processo", referencedColumnName = "ide_grupo_processo", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private GrupoProcesso ideGrupoProcesso;
    
	@JoinColumn(name = "ide_area_destino", referencedColumnName = "ide_area")
    @ManyToOne(fetch = FetchType.LAZY)
    private Area ideAreaDestino;
    
	@JoinColumn(name = "ide_acao_fluxo", referencedColumnName = "ide_acao_fluxo")
    @ManyToOne(fetch = FetchType.EAGER)
    private AcaoFluxo ideAcaoFluxo;

    public ControleFluxo() {
    }

    public ControleFluxo(Integer ideControleFluxo) {
        this.ideControleFluxo = ideControleFluxo;
    }

    public ControleFluxo(Integer ideControleFluxo, boolean indMovimentacaoAutomatica) {
        this.ideControleFluxo = ideControleFluxo;
        this.indMovimentacaoAutomatica = indMovimentacaoAutomatica;
    }

    public boolean getIndMovimentacaoAutomatica() {
        return indMovimentacaoAutomatica;
    }

    public void setIndMovimentacaoAutomatica(boolean indMovimentacaoAutomatica) {
        this.indMovimentacaoAutomatica = indMovimentacaoAutomatica;
    }

    public Integer getIdeControleFluxo() {
        return ideControleFluxo;
    }

    public void setIdeControleFluxo(Integer ideControleFluxo) {
        this.ideControleFluxo = ideControleFluxo;
    }

    public TipoAto getIdeTipoAto() {
        return ideTipoAto;
    }

    public void setIdeTipoAto(TipoAto ideTipoAto) {
        this.ideTipoAto = ideTipoAto;
    }

    public StatusFluxo getIdeStatusPosterior() {
        return ideStatusPosterior;
    }

    public void setIdeStatusPosterior(StatusFluxo ideStatusPosterior) {
        this.ideStatusPosterior = ideStatusPosterior;
    }

    public StatusFluxo getIdeStatusAnterior() {
        return ideStatusAnterior;
    }

    public void setIdeStatusAnterior(StatusFluxo ideStatusAnterior) {
        this.ideStatusAnterior = ideStatusAnterior;
    }

    public GrupoProcesso getIdeGrupoProcesso() {
        return ideGrupoProcesso;
    }

    public void setIdeGrupoProcesso(GrupoProcesso ideGrupoProcesso) {
        this.ideGrupoProcesso = ideGrupoProcesso;
    }

    public Area getIdeAreaDestino() {
        return ideAreaDestino;
    }

    public void setIdeAreaDestino(Area ideAreaDestino) {
        this.ideAreaDestino = ideAreaDestino;
    }

    public AcaoFluxo getIdeAcaoFluxo() {
        return ideAcaoFluxo;
    }

    public void setIdeAcaoFluxo(AcaoFluxo ideAcaoFluxo) {
        this.ideAcaoFluxo = ideAcaoFluxo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideControleFluxo != null ? ideControleFluxo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ControleFluxo)) {
            return false;
        }
        ControleFluxo other = (ControleFluxo) object;
        if ((this.ideControleFluxo == null && other.ideControleFluxo != null) || (this.ideControleFluxo != null && !this.ideControleFluxo.equals(other.ideControleFluxo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.ControleFluxo[ ideControleFluxo=" + ideControleFluxo + " ]";
    }
    
}
