package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "ocupacao", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"cod_ocupacao"}),
    @UniqueConstraint(columnNames = {"nom_ocupacao"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ocupacao.findAll", query = "SELECT o FROM Ocupacao o"),
    @NamedQuery(name = "Ocupacao.findByIdeOcupacao", query = "SELECT o FROM Ocupacao o WHERE o.ideOcupacao = :ideOcupacao"),
    @NamedQuery(name = "Ocupacao.findByCodOcupacao", query = "SELECT o FROM Ocupacao o WHERE o.codOcupacao = :codOcupacao"),
    @NamedQuery(name = "Ocupacao.findByNomOcupacao", query = "SELECT o FROM Ocupacao o WHERE o.nomOcupacao = :nomOcupacao")})
public class Ocupacao implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_ocupacao", nullable = false)
    private Integer ideOcupacao;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 7)
    @Column(name = "cod_ocupacao", nullable = false, length = 7)
    private String codOcupacao;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nom_ocupacao", nullable = false, length = 200)
    private String nomOcupacao;
    @OneToMany(mappedBy = "ideOcupacao", fetch = FetchType.LAZY)
    private Collection<PessoaFisica> pessoaFisicaCollection;

    public Ocupacao() {
    }

    public Ocupacao(Integer ideOcupacao) {
        this.ideOcupacao = ideOcupacao;
    }

    public Ocupacao(Integer ideOcupacao, String codOcupacao, String nomOcupacao) {
        this.ideOcupacao = ideOcupacao;
        this.codOcupacao = codOcupacao;
        this.nomOcupacao = nomOcupacao;
    }

    public Integer getIdeOcupacao() {
        return ideOcupacao;
    }

    public void setIdeOcupacao(Integer ideOcupacao) {
        this.ideOcupacao = ideOcupacao;
    }

    public String getCodOcupacao() {
        return codOcupacao;
    }

    public void setCodOcupacao(String codOcupacao) {
        this.codOcupacao = codOcupacao;
    }

    public String getNomOcupacao() {
        return nomOcupacao;
    }

    public void setNomOcupacao(String nomOcupacao) {
        this.nomOcupacao = nomOcupacao;
    }

    @XmlTransient
    public Collection<PessoaFisica> getPessoaFisicaCollection() {
        return pessoaFisicaCollection;
    }

    public void setPessoaFisicaCollection(Collection<PessoaFisica> pessoaFisicaCollection) {
        this.pessoaFisicaCollection = pessoaFisicaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideOcupacao != null ? ideOcupacao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof Ocupacao)) {
            return false;
        }
        Ocupacao other = (Ocupacao) object;
        if ((this.ideOcupacao == null && other.ideOcupacao != null) || (this.ideOcupacao != null && !this.ideOcupacao.equals(other.ideOcupacao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {

    	return String.valueOf(ideOcupacao);
    }
}