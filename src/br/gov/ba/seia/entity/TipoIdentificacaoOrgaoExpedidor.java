package br.gov.ba.seia.entity;

import java.io.Serializable;

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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "tipo_identificacao_orgao_expedidor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoIdentificacaoOrgaoExpedidor.findAll", query = "SELECT t FROM TipoIdentificacaoOrgaoExpedidor t"),
    @NamedQuery(name = "TipoIdentificacaoOrgaoExpedidor.findByIdeTipoIdentificacaoOrgaoExpedidor", query = "SELECT t FROM TipoIdentificacaoOrgaoExpedidor t WHERE t.ideTipoIdentificacaoOrgaoExpedidor = :ideTipoIdentificacaoOrgaoExpedidor")})
public class TipoIdentificacaoOrgaoExpedidor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_identificacao_orgao_expedidor", nullable = false)
    private Integer ideTipoIdentificacaoOrgaoExpedidor;
    @JoinColumn(name = "ide_tipo_identificacao", referencedColumnName = "ide_tipo_identificacao", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TipoIdentificacao ideTipoIdentificacao;
    @JoinColumn(name = "ide_orgao_expedidor", referencedColumnName = "ide_orgao_expedidor", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private OrgaoExpedidor ideOrgaoExpedidor;

    public TipoIdentificacaoOrgaoExpedidor() {
    }

    public TipoIdentificacaoOrgaoExpedidor(Integer ideTipoIdentificacaoOrgaoExpedidor) {
        this.ideTipoIdentificacaoOrgaoExpedidor = ideTipoIdentificacaoOrgaoExpedidor;
    }

    public Integer getIdeTipoIdentificacaoOrgaoExpedidor() {
        return ideTipoIdentificacaoOrgaoExpedidor;
    }

    public void setIdeTipoIdentificacaoOrgaoExpedidor(Integer ideTipoIdentificacaoOrgaoExpedidor) {
        this.ideTipoIdentificacaoOrgaoExpedidor = ideTipoIdentificacaoOrgaoExpedidor;
    }

    public TipoIdentificacao getIdeTipoIdentificacao() {
        return ideTipoIdentificacao;
    }

    public void setIdeTipoIdentificacao(TipoIdentificacao ideTipoIdentificacao) {
        this.ideTipoIdentificacao = ideTipoIdentificacao;
    }

    public OrgaoExpedidor getIdeOrgaoExpedidor() {
        return ideOrgaoExpedidor;
    }

    public void setIdeOrgaoExpedidor(OrgaoExpedidor ideOrgaoExpedidor) {
        this.ideOrgaoExpedidor = ideOrgaoExpedidor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoIdentificacaoOrgaoExpedidor != null ? ideTipoIdentificacaoOrgaoExpedidor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoIdentificacaoOrgaoExpedidor)) {
            return false;
        }
        TipoIdentificacaoOrgaoExpedidor other = (TipoIdentificacaoOrgaoExpedidor) object;
        if ((this.ideTipoIdentificacaoOrgaoExpedidor == null && other.ideTipoIdentificacaoOrgaoExpedidor != null) || (this.ideTipoIdentificacaoOrgaoExpedidor != null && !this.ideTipoIdentificacaoOrgaoExpedidor.equals(other.ideTipoIdentificacaoOrgaoExpedidor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.TipoIdentificacaoOrgaoExpedidor[ ideTipoIdentificacaoOrgaoExpedidor=" + ideTipoIdentificacaoOrgaoExpedidor + " ]";
    }
    
}
