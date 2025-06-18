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

import br.gov.ba.seia.entity.generic.AbstractEntity;


@Entity
@Table(name = "tipo_identificacao", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_tipo_identificacao"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoIdentificacao.findAll", query = "SELECT t FROM TipoIdentificacao t"),
    @NamedQuery(name = "TipoIdentificacao.findByIdeTipoIdentificacao", query = "SELECT t FROM TipoIdentificacao t WHERE t.ideTipoIdentificacao = :ideTipoIdentificacao"),
    @NamedQuery(name = "TipoIdentificacao.findByNomTipoIdentificacao", query = "SELECT t FROM TipoIdentificacao t WHERE t.nomTipoIdentificacao = :nomTipoIdentificacao")})
public class TipoIdentificacao extends AbstractEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ide_tipo_identificacao", nullable = false)
    private Integer ideTipoIdentificacao;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nom_tipo_identificacao", nullable = false, length = 100)
    private String nomTipoIdentificacao;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideTipoIdentificacao", fetch = FetchType.LAZY)
    private Collection<DocumentoIdentificacao> documentoIdentificacaoCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideTipoIdentificacao", fetch = FetchType.LAZY)
    private Collection<TipoIdentificacaoOrgaoExpedidor> tipoIdentificacaoOrgaoExpedidorCollection;

    public TipoIdentificacao() {
    }

    public TipoIdentificacao(Integer ideTipoIdentificacao) {
        this.ideTipoIdentificacao = ideTipoIdentificacao;
    }

    public TipoIdentificacao(Integer ideTipoIdentificacao, String nomTipoIdentificacao) {
        this.ideTipoIdentificacao = ideTipoIdentificacao;
        this.nomTipoIdentificacao = nomTipoIdentificacao;
    }

    public Integer getIdeTipoIdentificacao() {
        return ideTipoIdentificacao;
    }

    public void setIdeTipoIdentificacao(Integer ideTipoIdentificacao) {
        this.ideTipoIdentificacao = ideTipoIdentificacao;
    }

    public String getNomTipoIdentificacao() {
        return nomTipoIdentificacao;
    }

    public void setNomTipoIdentificacao(String nomTipoIdentificacao) {
        this.nomTipoIdentificacao = nomTipoIdentificacao;
    }

    @XmlTransient
    public Collection<DocumentoIdentificacao> getDocumentoIdentificacaoCollection() {
        return documentoIdentificacaoCollection;
    }

    public void setDocumentoIdentificacaoCollection(Collection<DocumentoIdentificacao> documentoIdentificacaoCollection) {
        this.documentoIdentificacaoCollection = documentoIdentificacaoCollection;
    }

    @XmlTransient
    public Collection<TipoIdentificacaoOrgaoExpedidor> getTipoIdentificacaoOrgaoExpedidorCollection() {
        return tipoIdentificacaoOrgaoExpedidorCollection;
    }

    public void setTipoIdentificacaoOrgaoExpedidorCollection(Collection<TipoIdentificacaoOrgaoExpedidor> tipoIdentificacaoOrgaoExpedidorCollection) {
        this.tipoIdentificacaoOrgaoExpedidorCollection = tipoIdentificacaoOrgaoExpedidorCollection;
    }
}
