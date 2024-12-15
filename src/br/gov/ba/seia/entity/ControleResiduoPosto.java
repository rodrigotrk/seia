package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author luis
 */
@Entity
@Table(name = "controle_residuo_posto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ControleResiduoPosto.findAll", query = "SELECT c FROM ControleResiduoPosto c"),
    @NamedQuery(name = "ControleResiduoPosto.findByIdeControleResiduoPosto", query = "SELECT c FROM ControleResiduoPosto c WHERE c.ideControleResiduoPosto = :ideControleResiduoPosto"),
    @NamedQuery(name = "ControleResiduoPosto.findByQtdGeracaoMensal", query = "SELECT c FROM ControleResiduoPosto c WHERE c.qtdGeracaoMensal = :qtdGeracaoMensal"),
    @NamedQuery(name = "ControleResiduoPosto.findByQtdTempoPermanencia", query = "SELECT c FROM ControleResiduoPosto c WHERE c.qtdTempoPermanencia = :qtdTempoPermanencia"),
    @NamedQuery(name = "ControleResiduoPosto.findByDscLocalArmazenagem", query = "SELECT c FROM ControleResiduoPosto c WHERE c.dscLocalArmazenagem = :dscLocalArmazenagem"),
    @NamedQuery(name = "ControleResiduoPosto.findByDscDestinacaoFinal", query = "SELECT c FROM ControleResiduoPosto c WHERE c.dscDestinacaoFinal = :dscDestinacaoFinal")})
public class ControleResiduoPosto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_controle_residuo_posto")
    private Integer ideControleResiduoPosto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "qtd_geracao_mensal")
    private int qtdGeracaoMensal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "qtd_tempo_permanencia")
    private int qtdTempoPermanencia;
    @Size(max = 100)
    @Column(name = "dsc_local_armazenagem")
    private String dscLocalArmazenagem;
    @Size(max = 100)
    @Column(name = "dsc_destinacao_final")
    private String dscDestinacaoFinal;
    @JoinColumn(name = "ide_tipo_residuo", referencedColumnName = "ide_tipo_residuo")
    @ManyToOne(optional = false)
    private TipoResiduo ideTipoResiduo;
    @JoinColumn(name = "ide_tipo_controle_tempo", referencedColumnName = "ide_tipo_controle_tempo")
    @ManyToOne(optional = false)
    private TipoControleTempo ideTipoControleTempo;
    @JoinColumn(name = "ide_tipo_acondicionamento_posto", referencedColumnName = "ide_tipo_acondicinamento_posto")
    @ManyToOne(optional = false)
    private TipoAcondicionamentoPosto ideTipoAcondicionamentoPosto;
    @JoinColumn(name = "ide_lac_posto_combustivel", referencedColumnName = "ide_lac_posto_combustivel")
    @ManyToOne(optional = false)
    private LacPostoCombustivel idePostoCombustivel;

    public ControleResiduoPosto() {
    }

    public ControleResiduoPosto(Integer ideControleResiduoPosto) {
        this.ideControleResiduoPosto = ideControleResiduoPosto;
    }

    public ControleResiduoPosto(Integer ideControleResiduoPosto, int qtdGeracaoMensal, int qtdTempoPermanencia) {
        this.ideControleResiduoPosto = ideControleResiduoPosto;
        this.qtdGeracaoMensal = qtdGeracaoMensal;
        this.qtdTempoPermanencia = qtdTempoPermanencia;
    }

    public Integer getIdeControleResiduoPosto() {
        return ideControleResiduoPosto;
    }

    public void setIdeControleResiduoPosto(Integer ideControleResiduoPosto) {
        this.ideControleResiduoPosto = ideControleResiduoPosto;
    }

    public int getQtdGeracaoMensal() {
        return qtdGeracaoMensal;
    }

    public void setQtdGeracaoMensal(int qtdGeracaoMensal) {
        this.qtdGeracaoMensal = qtdGeracaoMensal;
    }

    public int getQtdTempoPermanencia() {
        return qtdTempoPermanencia;
    }

    public void setQtdTempoPermanencia(int qtdTempoPermanencia) {
        this.qtdTempoPermanencia = qtdTempoPermanencia;
    }

    public String getDscLocalArmazenagem() {
        return dscLocalArmazenagem;
    }

    public void setDscLocalArmazenagem(String dscLocalArmazenagem) {
        this.dscLocalArmazenagem = dscLocalArmazenagem;
    }

    public String getDscDestinacaoFinal() {
        return dscDestinacaoFinal;
    }

    public void setDscDestinacaoFinal(String dscDestinacaoFinal) {
        this.dscDestinacaoFinal = dscDestinacaoFinal;
    }

    public TipoResiduo getIdeTipoResiduo() {
        return ideTipoResiduo;
    }

    public void setIdeTipoResiduo(TipoResiduo ideTipoResiduo) {
        this.ideTipoResiduo = ideTipoResiduo;
    }

    public TipoControleTempo getIdeTipoControleTempo() {
        return ideTipoControleTempo;
    }

    public void setIdeTipoControleTempo(TipoControleTempo ideTipoControleTempo) {
        this.ideTipoControleTempo = ideTipoControleTempo;
    }

    public TipoAcondicionamentoPosto getIdeTipoAcondicionamentoPosto() {
        return ideTipoAcondicionamentoPosto;
    }

    public void setIdeTipoAcondicionamentoPosto(TipoAcondicionamentoPosto ideTipoAcondicionamentoPosto) {
        this.ideTipoAcondicionamentoPosto = ideTipoAcondicionamentoPosto;
    }

    public LacPostoCombustivel getIdePostoCombustivel() {
        return idePostoCombustivel;
    }

    public void setIdePostoCombustivel(LacPostoCombustivel idePostoCombustivel) {
        this.idePostoCombustivel = idePostoCombustivel;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideControleResiduoPosto != null ? ideControleResiduoPosto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ControleResiduoPosto)) {
            return false;
        }
        ControleResiduoPosto other = (ControleResiduoPosto) object;
        if ((this.ideControleResiduoPosto == null && other.ideControleResiduoPosto != null) || (this.ideControleResiduoPosto != null && !this.ideControleResiduoPosto.equals(other.ideControleResiduoPosto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ControleResiduoPosto[ ideControleResiduoPosto=" + ideControleResiduoPosto + " ]";
    }
    
}
