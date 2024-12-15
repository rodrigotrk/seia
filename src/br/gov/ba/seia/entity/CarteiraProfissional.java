package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "carteira_profissional")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CarteiraProfissional.findAll", query = "SELECT c FROM CarteiraProfissional c"),
    @NamedQuery(name = "CarteiraProfissional.findByIdeDocumentoIdentificacao", query = "SELECT c FROM CarteiraProfissional c WHERE c.ideDocumentoIdentificacao = :ideDocumentoIdentificacao"),
    @NamedQuery(name = "CarteiraProfissional.findByNumCarteiraProfissional", query = "SELECT c FROM CarteiraProfissional c WHERE c.numCarteiraProfissional = :numCarteiraProfissional")})
public class CarteiraProfissional implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_documento_identificacao", nullable = false)
    private Integer ideDocumentoIdentificacao;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "num_carteira_profissional", nullable = false, length = 30)
    private String numCarteiraProfissional;

    public CarteiraProfissional() {
    }

    public CarteiraProfissional(Integer ideDocumentoIdentificacao) {
        this.ideDocumentoIdentificacao = ideDocumentoIdentificacao;
    }

    public CarteiraProfissional(Integer ideDocumentoIdentificacao, String numCarteiraProfissional) {
        this.ideDocumentoIdentificacao = ideDocumentoIdentificacao;
        this.numCarteiraProfissional = numCarteiraProfissional;
    }

    public Integer getIdeDocumentoIdentificacao() {
        return ideDocumentoIdentificacao;
    }

    public void setIdeDocumentoIdentificacao(Integer ideDocumentoIdentificacao) {
        this.ideDocumentoIdentificacao = ideDocumentoIdentificacao;
    }

    public String getNumCarteiraProfissional() {
        return numCarteiraProfissional;
    }

    public void setNumCarteiraProfissional(String numCarteiraProfissional) {
        this.numCarteiraProfissional = numCarteiraProfissional;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideDocumentoIdentificacao != null ? ideDocumentoIdentificacao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof CarteiraProfissional)) {
            return false;
        }
        CarteiraProfissional other = (CarteiraProfissional) object;
        if ((this.ideDocumentoIdentificacao == null && other.ideDocumentoIdentificacao != null) || (this.ideDocumentoIdentificacao != null && !this.ideDocumentoIdentificacao.equals(other.ideDocumentoIdentificacao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.CarteiraProfissional[ ideDocumentoIdentificacao=" + ideDocumentoIdentificacao + " ]";
    }
    
}
