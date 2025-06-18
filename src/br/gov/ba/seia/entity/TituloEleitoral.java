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
@Table(name = "titulo_eleitoral", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"num_titulo"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TituloEleitoral.findAll", query = "SELECT t FROM TituloEleitoral t"),
    @NamedQuery(name = "TituloEleitoral.findByIdeDocumentoIdentificacao", query = "SELECT t FROM TituloEleitoral t WHERE t.ideDocumentoIdentificacao = :ideDocumentoIdentificacao"),
    @NamedQuery(name = "TituloEleitoral.findByNumTitulo", query = "SELECT t FROM TituloEleitoral t WHERE t.numTitulo = :numTitulo"),
    @NamedQuery(name = "TituloEleitoral.findByNumZona", query = "SELECT t FROM TituloEleitoral t WHERE t.numZona = :numZona"),
    @NamedQuery(name = "TituloEleitoral.findByNumSecao", query = "SELECT t FROM TituloEleitoral t WHERE t.numSecao = :numSecao")})
public class TituloEleitoral implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_documento_identificacao", nullable = false)
    private Integer ideDocumentoIdentificacao;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "num_titulo", nullable = false, length = 30)
    private String numTitulo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "num_zona", nullable = false)
    private int numZona;
    @Basic(optional = false)
    @NotNull
    @Column(name = "num_secao", nullable = false)
    private int numSecao;

    public TituloEleitoral() {
    }

    public TituloEleitoral(Integer ideDocumentoIdentificacao) {
        this.ideDocumentoIdentificacao = ideDocumentoIdentificacao;
    }

    public TituloEleitoral(Integer ideDocumentoIdentificacao, String numTitulo, int numZona, int numSecao) {
        this.ideDocumentoIdentificacao = ideDocumentoIdentificacao;
        this.numTitulo = numTitulo;
        this.numZona = numZona;
        this.numSecao = numSecao;
    }

    public Integer getIdeDocumentoIdentificacao() {
        return ideDocumentoIdentificacao;
    }

    public void setIdeDocumentoIdentificacao(Integer ideDocumentoIdentificacao) {
        this.ideDocumentoIdentificacao = ideDocumentoIdentificacao;
    }

    public String getNumTitulo() {
        return numTitulo;
    }

    public void setNumTitulo(String numTitulo) {
        this.numTitulo = numTitulo;
    }

    public int getNumZona() {
        return numZona;
    }

    public void setNumZona(int numZona) {
        this.numZona = numZona;
    }

    public int getNumSecao() {
        return numSecao;
    }

    public void setNumSecao(int numSecao) {
        this.numSecao = numSecao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideDocumentoIdentificacao != null ? ideDocumentoIdentificacao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TituloEleitoral)) {
            return false;
        }
        TituloEleitoral other = (TituloEleitoral) object;
        if ((this.ideDocumentoIdentificacao == null && other.ideDocumentoIdentificacao != null) || (this.ideDocumentoIdentificacao != null && !this.ideDocumentoIdentificacao.equals(other.ideDocumentoIdentificacao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.TituloEleitoral[ ideDocumentoIdentificacao=" + ideDocumentoIdentificacao + " ]";
    }
    
}
