package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "natureza_utilizacao_agua", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_natureza_utilizacao"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NaturezaUtilizacaoAgua.findAll", query = "SELECT n FROM NaturezaUtilizacaoAgua n"),
    @NamedQuery(name = "NaturezaUtilizacaoAgua.findByIdeNaturezaUtilizacao", query = "SELECT n FROM NaturezaUtilizacaoAgua n WHERE n.ideNaturezaUtilizacao = :ideNaturezaUtilizacao"),
    @NamedQuery(name = "NaturezaUtilizacaoAgua.findByNomNaturezaUtilizacao", query = "SELECT n FROM NaturezaUtilizacaoAgua n WHERE n.nomNaturezaUtilizacao = :nomNaturezaUtilizacao")})
public class NaturezaUtilizacaoAgua implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_natureza_utilizacao", nullable = false)
    private Integer ideNaturezaUtilizacao;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "nom_natureza_utilizacao", nullable = false, length = 25)
    private String nomNaturezaUtilizacao;

    public NaturezaUtilizacaoAgua() {
    }

    public NaturezaUtilizacaoAgua(Integer ideNaturezaUtilizacao) {
        this.ideNaturezaUtilizacao = ideNaturezaUtilizacao;
    }

    public NaturezaUtilizacaoAgua(Integer ideNaturezaUtilizacao, String nomNaturezaUtilizacao) {
        this.ideNaturezaUtilizacao = ideNaturezaUtilizacao;
        this.nomNaturezaUtilizacao = nomNaturezaUtilizacao;
    }

    public Integer getIdeNaturezaUtilizacao() {
        return ideNaturezaUtilizacao;
    }

    public void setIdeNaturezaUtilizacao(Integer ideNaturezaUtilizacao) {
        this.ideNaturezaUtilizacao = ideNaturezaUtilizacao;
    }

    public String getNomNaturezaUtilizacao() {
        return nomNaturezaUtilizacao;
    }

    public void setNomNaturezaUtilizacao(String nomNaturezaUtilizacao) {
        this.nomNaturezaUtilizacao = nomNaturezaUtilizacao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNaturezaUtilizacao != null ? ideNaturezaUtilizacao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof NaturezaUtilizacaoAgua)) {
            return false;
        }
        NaturezaUtilizacaoAgua other = (NaturezaUtilizacaoAgua) object;
        if ((this.ideNaturezaUtilizacao == null && other.ideNaturezaUtilizacao != null) || (this.ideNaturezaUtilizacao != null && !this.ideNaturezaUtilizacao.equals(other.ideNaturezaUtilizacao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.NaturezaUtilizacaoAgua[ ideNaturezaUtilizacao=" + ideNaturezaUtilizacao + " ]";
    }
    
}
