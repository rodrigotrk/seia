package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name = "orgao_expedidor", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"dsc_orgao_expedidor"}),
    @UniqueConstraint(columnNames = {"dsc_sigla_orgao_expedidor"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OrgaoExpedidor.findAll", query = "SELECT o FROM OrgaoExpedidor o"),
    @NamedQuery(name = "OrgaoExpedidor.findByIdeOrgaoExpedidor", query = "SELECT o FROM OrgaoExpedidor o WHERE o.ideOrgaoExpedidor = :ideOrgaoExpedidor"),
    @NamedQuery(name = "OrgaoExpedidor.findByDscOrgaoExpedidor", query = "SELECT o FROM OrgaoExpedidor o WHERE o.dscOrgaoExpedidor = :dscOrgaoExpedidor"),
    @NamedQuery(name = "OrgaoExpedidor.findByDscSiglaOrgaoExpedidor", query = "SELECT o FROM OrgaoExpedidor o WHERE o.dscSiglaOrgaoExpedidor = :dscSiglaOrgaoExpedidor"),
    @NamedQuery(name = "OrgaoExpedidor.findByDtcCriacao", query = "SELECT o FROM OrgaoExpedidor o WHERE o.dtcCriacao = :dtcCriacao"),
    @NamedQuery(name = "OrgaoExpedidor.findByIndConselhoProfissional", query = "SELECT o FROM OrgaoExpedidor o WHERE o.indConselhoProfissional = :indConselhoProfissional"),
    @NamedQuery(name = "OrgaoExpedidor.findByIndExcluido", query = "SELECT o FROM OrgaoExpedidor o WHERE o.indExcluido = :indExcluido"),
    @NamedQuery(name = "OrgaoExpedidor.findByDtcExclusao", query = "SELECT o FROM OrgaoExpedidor o WHERE o.dtcExclusao = :dtcExclusao")})

public class OrgaoExpedidor extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;
    
	@Id
    @Column(name = "ide_orgao_expedidor", nullable = false)
    private Integer ideOrgaoExpedidor;
    
	@Basic(optional = false)
    @NotNull    
    @Column(name = "dsc_orgao_expedidor", nullable = false, length = 50)
    private String dscOrgaoExpedidor;
    
	@Basic(optional = false)
    @NotNull    
    @Column(name = "dsc_sigla_orgao_expedidor", nullable = false, length = 10)
    private String dscSiglaOrgaoExpedidor;
    
	@Basic(optional = false)
    @NotNull
    @Column(name = "dtc_criacao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcCriacao;
    
	@Basic(optional = false)
    @NotNull
    @Column(name = "ind_conselho_profissional", nullable = false)
    private boolean indConselhoProfissional;
  
	@Basic(optional = false)
    @NotNull
    @Column(name = "ind_excluido", nullable = false)
    private boolean indExcluido;
   
	@Column(name = "dtc_exclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcExclusao;
   
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideOrgaoExpedidor", fetch = FetchType.LAZY)
    private Collection<DocumentoIdentificacao> documentoIdentificacaoCollection;
   
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideOrgaoExpedidor", fetch = FetchType.LAZY)
    private Collection<TipoIdentificacaoOrgaoExpedidor> tipoIdentificacaoOrgaoExpedidorCollection;

    public OrgaoExpedidor() {
    }

    public OrgaoExpedidor(Integer ideOrgaoExpedidor) {
        this.ideOrgaoExpedidor = ideOrgaoExpedidor;
    }

    public OrgaoExpedidor(Integer ideOrgaoExpedidor, String dscOrgaoExpedidor, String dscSiglaOrgaoExpedidor, Date dtcCriacao, boolean indConselhoProfissional, boolean indExcluido) {
        this.ideOrgaoExpedidor = ideOrgaoExpedidor;
        this.dscOrgaoExpedidor = dscOrgaoExpedidor;
        this.dscSiglaOrgaoExpedidor = dscSiglaOrgaoExpedidor;
        this.dtcCriacao = dtcCriacao;
        this.indConselhoProfissional = indConselhoProfissional;
        this.indExcluido = indExcluido;
    }

    public Integer getIdeOrgaoExpedidor() {
        return ideOrgaoExpedidor;
    }

    public void setIdeOrgaoExpedidor(Integer ideOrgaoExpedidor) {
        this.ideOrgaoExpedidor = ideOrgaoExpedidor;
    }

    public String getDscOrgaoExpedidor() {
        return dscOrgaoExpedidor;
    }

    public void setDscOrgaoExpedidor(String dscOrgaoExpedidor) {
        this.dscOrgaoExpedidor = dscOrgaoExpedidor;
    }

    public String getDscSiglaOrgaoExpedidor() {
        return dscSiglaOrgaoExpedidor;
    }

    public void setDscSiglaOrgaoExpedidor(String dscSiglaOrgaoExpedidor) {
        this.dscSiglaOrgaoExpedidor = dscSiglaOrgaoExpedidor;
    }

    public Date getDtcCriacao() {
        return dtcCriacao;
    }

    public void setDtcCriacao(Date dtcCriacao) {
        this.dtcCriacao = dtcCriacao;
    }

    public boolean getIndConselhoProfissional() {
        return indConselhoProfissional;
    }

    public void setIndConselhoProfissional(boolean indConselhoProfissional) {
        this.indConselhoProfissional = indConselhoProfissional;
    }

    public boolean getIndExcluido() {
        return indExcluido;
    }

    public void setIndExcluido(boolean indExcluido) {
        this.indExcluido = indExcluido;
    }

    public Date getDtcExclusao() {
        return dtcExclusao;
    }

    public void setDtcExclusao(Date dtcExclusao) {
        this.dtcExclusao = dtcExclusao;
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
