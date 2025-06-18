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
@Table(name = "escolaridade", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_escolaridade"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Escolaridade.findAll", query = "SELECT e FROM Escolaridade e"),
    @NamedQuery(name = "Escolaridade.findByIdeEscolaridade", query = "SELECT e FROM Escolaridade e WHERE e.ideEscolaridade = :ideEscolaridade"),    
    @NamedQuery(name = "Escolaridade.findByNomEscolaridade", query = "SELECT e FROM Escolaridade e WHERE e.nomEscolaridade = :nomEscolaridade")})
public class Escolaridade implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_escolaridade", nullable = false)
    private Integer ideEscolaridade;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nom_escolaridade", nullable = false, length = 30)
    private String nomEscolaridade;
    @OneToMany(mappedBy = "ideEscolaridade", fetch = FetchType.LAZY)
    private Collection<PessoaFisica> pessoaFisicaCollection;

    public Escolaridade() {
    }

    public Escolaridade(Integer ideEscolaridade) {
        this.ideEscolaridade = ideEscolaridade;
    }

    public Escolaridade(Integer ideEscolaridade, String nomEscolaridade) {
        this.ideEscolaridade = ideEscolaridade;
        this.nomEscolaridade = nomEscolaridade;
    }

    public Integer getIdeEscolaridade() {
        return ideEscolaridade;
    }

    public void setIdeEscolaridade(Integer ideEscolaridade) {
        this.ideEscolaridade = ideEscolaridade;
    }

    public String getNomEscolaridade() {
        return nomEscolaridade;
    }

    public void setNomEscolaridade(String nomEscolaridade) {
        this.nomEscolaridade = nomEscolaridade;
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
        hash += (ideEscolaridade != null ? ideEscolaridade.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof Escolaridade)) {
            return false;
        }
        Escolaridade other = (Escolaridade) object;
        if ((this.ideEscolaridade == null && other.ideEscolaridade != null) || (this.ideEscolaridade != null && !this.ideEscolaridade.equals(other.ideEscolaridade))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {

    	return String.valueOf(ideEscolaridade);
    }
}
