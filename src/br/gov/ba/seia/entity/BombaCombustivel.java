package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author luis
 */
@Entity
@Table(name = "bomba_combustivel")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BombaCombustivel.findAll", query = "SELECT b FROM BombaCombustivel b"),
    @NamedQuery(name = "BombaCombustivel.findByIdeBombaCombustivel", query = "SELECT b FROM BombaCombustivel b WHERE b.ideBombaCombustivel = :ideBombaCombustivel"),
    @NamedQuery(name = "BombaCombustivel.findByNomBombaCombustivel", query = "SELECT b FROM BombaCombustivel b WHERE b.nomBombaCombustivel = :nomBombaCombustivel"),
    @NamedQuery(name = "BombaCombustivel.findByIndInstalado", query = "SELECT b FROM BombaCombustivel b WHERE b.indInstalado = :indInstalado"),
    @NamedQuery(name = "BombaCombustivel.findByDtcInstalacao", query = "SELECT b FROM BombaCombustivel b WHERE b.dtcInstalacao = :dtcInstalacao"),
    @NamedQuery(name = "BombaCombustivel.findByDscAntesReforma", query = "SELECT b FROM BombaCombustivel b WHERE b.dscAntesReforma = :dscAntesReforma"),
    @NamedQuery(name = "BombaCombustivel.findByDscDepoisReforma", query = "SELECT b FROM BombaCombustivel b WHERE b.dscDepoisReforma = :dscDepoisReforma")})
public class BombaCombustivel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_bomba_combustivel")
    private Integer ideBombaCombustivel;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nom_bomba_combustivel")
    private String nomBombaCombustivel;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ind_instalado")
    private boolean indInstalado;
    @Column(name = "dtc_instalacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcInstalacao;
    @Size(max = 50)
    @Column(name = "dsc_antes_reforma")
    private String dscAntesReforma;
    @Size(max = 50)
    @Column(name = "dsc_depois_reforma")
    private String dscDepoisReforma;
    @JoinColumn(name = "ide_produto", referencedColumnName = "ide_produto")
    @ManyToOne(optional = false)
    private Produto ideProduto;
    @JoinColumn(name = "ide_lac_posto_combustivel", referencedColumnName = "ide_lac_posto_combustivel")
    @ManyToOne(optional = false)
    private LacPostoCombustivel idePostoCombustivel;

    public BombaCombustivel() {
    }

    public BombaCombustivel(Integer ideBombaCombustivel) {
        this.ideBombaCombustivel = ideBombaCombustivel;
    }

    public BombaCombustivel(Integer ideBombaCombustivel, String nomBombaCombustivel, boolean indInstalado) {
        this.ideBombaCombustivel = ideBombaCombustivel;
        this.nomBombaCombustivel = nomBombaCombustivel;
        this.indInstalado = indInstalado;
    }

    public Integer getIdeBombaCombustivel() {
        return ideBombaCombustivel;
    }

    public void setIdeBombaCombustivel(Integer ideBombaCombustivel) {
        this.ideBombaCombustivel = ideBombaCombustivel;
    }

    public String getNomBombaCombustivel() {
        return nomBombaCombustivel;
    }

    public void setNomBombaCombustivel(String nomBombaCombustivel) {
        this.nomBombaCombustivel = nomBombaCombustivel;
    }

    public boolean getIndInstalado() {
        return indInstalado;
    }

    public void setIndInstalado(boolean indInstalado) {
        this.indInstalado = indInstalado;
    }

    public Date getDtcInstalacao() {
        return dtcInstalacao;
    }

    public void setDtcInstalacao(Date dtcInstalacao) {
        this.dtcInstalacao = dtcInstalacao;
    }

    public String getDscAntesReforma() {
        return dscAntesReforma;
    }

    public void setDscAntesReforma(String dscAntesReforma) {
        this.dscAntesReforma = dscAntesReforma;
    }

    public String getDscDepoisReforma() {
        return dscDepoisReforma;
    }

    public void setDscDepoisReforma(String dscDepoisReforma) {
        this.dscDepoisReforma = dscDepoisReforma;
    }

    public Produto getideProduto() {
        return ideProduto;
    }

    public void setideProduto(Produto ideProduto) {
        this.ideProduto = ideProduto;
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
        hash += (ideBombaCombustivel != null ? ideBombaCombustivel.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof BombaCombustivel)) {
            return false;
        }
        BombaCombustivel other = (BombaCombustivel) object;
        if ((this.ideBombaCombustivel == null && other.ideBombaCombustivel != null) || (this.ideBombaCombustivel != null && !this.ideBombaCombustivel.equals(other.ideBombaCombustivel))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.BombaCombustivel[ ideBombaCombustivel=" + ideBombaCombustivel + " ]";
    }
    
}
