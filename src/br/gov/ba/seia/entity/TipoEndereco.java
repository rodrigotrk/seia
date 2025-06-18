package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
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

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "tipo_endereco", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_tipo_endereco"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoEndereco.findAll", query = "SELECT t FROM TipoEndereco t"),
    @NamedQuery(name = "TipoEndereco.findByIdeTipoEndereco", query = "SELECT t FROM TipoEndereco t WHERE t.ideTipoEndereco = :ideTipoEndereco"),
    @NamedQuery(name = "TipoEndereco.findByNomTipoEndereco", query = "SELECT t FROM TipoEndereco t WHERE t.nomTipoEndereco = :nomTipoEndereco")})
public class TipoEndereco implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_endereco", nullable = false)
    private Integer ideTipoEndereco;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "nom_tipo_endereco", nullable = false, length = 20)
    private String nomTipoEndereco;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideTipoEndereco", fetch = FetchType.LAZY)
    private Collection<EnderecoEmpreendimento> enderecoEmpreendimentoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideTipoEndereco", fetch = FetchType.LAZY)
    private Collection<EnderecoPessoa> enderecoPessoaCollection;

    public TipoEndereco() {
    }
    public TipoEndereco(Integer ideTipoEndereco) {
        this.ideTipoEndereco = ideTipoEndereco;
    }
    public TipoEndereco(Integer ideTipoEndereco, String nomTipoEndereco) {
        this.ideTipoEndereco = ideTipoEndereco;
        this.nomTipoEndereco = nomTipoEndereco;
    }

    public Integer getIdeTipoEndereco() {
        return ideTipoEndereco;
    }

    public void setIdeTipoEndereco(Integer ideTipoEndereco) {
        this.ideTipoEndereco = ideTipoEndereco;
    }

    public String getNomTipoEndereco() {
        return nomTipoEndereco;
    }

    public void setNomTipoEndereco(String nomTipoEndereco) {
        this.nomTipoEndereco = nomTipoEndereco;
    }

    @XmlTransient
    public Collection<EnderecoEmpreendimento> getEnderecoEmpreendimentoCollection() {
        return enderecoEmpreendimentoCollection;
    }

    public void setEnderecoEmpreendimentoCollection(Collection<EnderecoEmpreendimento> enderecoEmpreendimentoCollection) {
        this.enderecoEmpreendimentoCollection = enderecoEmpreendimentoCollection;
    }

    @XmlTransient
    public Collection<EnderecoPessoa> getEnderecoPessoaCollection() {
        return enderecoPessoaCollection;
    }

    public void setEnderecoPessoaCollection(Collection<EnderecoPessoa> enderecoPessoaCollection) {
        this.enderecoPessoaCollection = enderecoPessoaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoEndereco != null ? ideTipoEndereco.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoEndereco)) {
            return false;
        }
        TipoEndereco other = (TipoEndereco) object;
        if ((this.ideTipoEndereco == null && other.ideTipoEndereco != null) || (this.ideTipoEndereco != null && !this.ideTipoEndereco.equals(other.ideTipoEndereco))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {

    	return String.valueOf(ideTipoEndereco);
    }
}