package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "responsavel_imovel")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ResponsavelImovel.findAll", query = "SELECT r FROM ResponsavelImovel r"),
    @NamedQuery(name = "ResponsavelImovel.findByIdeResponsavelImovel", query = "SELECT r FROM ResponsavelImovel r WHERE r.ideResponsavelImovel = :ideResponsavelImovel"),
    @NamedQuery(name = "ResponsavelImovel.findByIdeArlLocalizacao", query = "SELECT r FROM ResponsavelImovel r WHERE r.ideArlLocalizacao = :ideArlLocalizacao"),
    @NamedQuery(name = "ResponsavelImovel.findByDtcCriacao", query = "SELECT r FROM ResponsavelImovel r WHERE r.dtcCriacao = :dtcCriacao"),
    @NamedQuery(name = "ResponsavelImovel.findByIndExcluido", query = "SELECT r FROM ResponsavelImovel r WHERE r.indExcluido = :indExcluido"),
    @NamedQuery(name = "ResponsavelImovel.findByDtcFinal", query = "SELECT r FROM ResponsavelImovel r WHERE r.dtcFinal = :dtcFinal")})
public class ResponsavelImovel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_responsavel_imovel", nullable = false)
    private Integer ideResponsavelImovel;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_arl_localizacao", nullable = false)
    private int ideArlLocalizacao;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dtc_criacao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcCriacao;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ind_excluido", nullable = false)
    private boolean indExcluido;
    @Column(name = "dtc_final")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcFinal;
    @JoinColumn(name = "ide_pessoa", referencedColumnName = "ide_pessoa", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Pessoa idePessoa;
    @JoinColumn(name = "ide_imovel", referencedColumnName = "ide_imovel", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Imovel ideImovel;

    public ResponsavelImovel() {
    }

    public ResponsavelImovel(Integer ideResponsavelImovel) {
        this.ideResponsavelImovel = ideResponsavelImovel;
    }

    public ResponsavelImovel(Integer ideResponsavelImovel, int ideArlLocalizacao, Date dtcCriacao, boolean indExcluido) {
        this.ideResponsavelImovel = ideResponsavelImovel;
        this.ideArlLocalizacao = ideArlLocalizacao;
        this.dtcCriacao = dtcCriacao;
        this.indExcluido = indExcluido;
    }

    public Integer getIdeResponsavelImovel() {
        return ideResponsavelImovel;
    }

    public void setIdeResponsavelImovel(Integer ideResponsavelImovel) {
        this.ideResponsavelImovel = ideResponsavelImovel;
    }

    public int getIdeArlLocalizacao() {
        return ideArlLocalizacao;
    }

    public void setIdeArlLocalizacao(int ideArlLocalizacao) {
        this.ideArlLocalizacao = ideArlLocalizacao;
    }

    public Date getDtcCriacao() {
        return dtcCriacao;
    }

    public void setDtcCriacao(Date dtcCriacao) {
        this.dtcCriacao = dtcCriacao;
    }

    public boolean getIndExcluido() {
        return indExcluido;
    }

    public void setIndExcluido(boolean indExcluido) {
        this.indExcluido = indExcluido;
    }

    public Date getDtcFinal() {
        return dtcFinal;
    }

    public void setDtcFinal(Date dtcFinal) {
        this.dtcFinal = dtcFinal;
    }

    public Pessoa getIdePessoa() {
        return idePessoa;
    }

    public void setIdePessoa(Pessoa idePessoa) {
        this.idePessoa = idePessoa;
    }

    public Imovel getIdeImovel() {
        return ideImovel;
    }

    public void setIdeImovel(Imovel ideImovel) {
        this.ideImovel = ideImovel;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideResponsavelImovel != null ? ideResponsavelImovel.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ResponsavelImovel)) {
            return false;
        }
        ResponsavelImovel other = (ResponsavelImovel) object;
        if ((this.ideResponsavelImovel == null && other.ideResponsavelImovel != null) || (this.ideResponsavelImovel != null && !this.ideResponsavelImovel.equals(other.ideResponsavelImovel))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.ResponsavelImovel[ ideResponsavelImovel=" + ideResponsavelImovel + " ]";
    }
    
}
