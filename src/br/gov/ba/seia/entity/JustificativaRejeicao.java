package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.NamedNativeQuery;

import flexjson.JSON;

/**
 *
 * @author micael.coutinho
 */
@Entity
@Table(name = "justificativa_rejeicao")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "JustificativaRejeicao.findAll", query = "SELECT j FROM JustificativaRejeicao j"),
    @NamedQuery(name = "JustificativaRejeicao.findByIdeJustificativaRejeicao", query = "SELECT j FROM JustificativaRejeicao j WHERE j.ideJustificativaRejeicao = :ideJustificativaRejeicao"),
    @NamedQuery(name = "JustificativaRejeicao.findByNomJustificativaRejeicao", query = "SELECT j FROM JustificativaRejeicao j WHERE j.nomJustificativaRejeicao = :nomJustificativaRejeicao")})
@NamedNativeQuery(name = "JustificativaRejeicao.findByIdeNotificacao", query = "select jr.* from notificacao_justificativa_rejeicao njr ,justificativa_rejeicao jr where njr.ide_notificacao  = :ideNotificacao and njr.ide_justificativa_rejeicao = jr.ide_justificativa_rejeicao",resultClass=JustificativaRejeicao.class)
public class JustificativaRejeicao implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_justificativa_rejeicao")
    private Integer ideJustificativaRejeicao;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nom_justificativa_rejeicao")
    private String nomJustificativaRejeicao;
    @ManyToMany(mappedBy = "justificativaRejeicaoCollection",fetch = FetchType.LAZY)
    private Collection<Notificacao> notificacaoCollection;

    public JustificativaRejeicao() {
    }

    public JustificativaRejeicao(Integer ideJustificativaRejeicao) {
        this.ideJustificativaRejeicao = ideJustificativaRejeicao;
    }

    public JustificativaRejeicao(Integer ideJustificativaRejeicao, String nomJustificativaRejeicao) {
        this.ideJustificativaRejeicao = ideJustificativaRejeicao;
        this.nomJustificativaRejeicao = nomJustificativaRejeicao;
    }

    @JSON(include = false)
    public Integer getIdeJustificativaRejeicao() {
        return ideJustificativaRejeicao;
    }

    public void setIdeJustificativaRejeicao(Integer ideJustificativaRejeicao) {
        this.ideJustificativaRejeicao = ideJustificativaRejeicao;
    }

    public String getNomJustificativaRejeicao() {
        return nomJustificativaRejeicao;
    }

    public void setNomJustificativaRejeicao(String nomJustificativaRejeicao) {
        this.nomJustificativaRejeicao = nomJustificativaRejeicao;
    }
    
    @JSON(include = false)
    public Collection<Notificacao> getNotificacaoCollection() {
		return notificacaoCollection;
	}

	public void setNotificacaoCollection(
			Collection<Notificacao> notificacaoCollection) {
		this.notificacaoCollection = notificacaoCollection;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (ideJustificativaRejeicao != null ? ideJustificativaRejeicao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof JustificativaRejeicao)) {
            return false;
        }
        JustificativaRejeicao other = (JustificativaRejeicao) object;
        if ((this.ideJustificativaRejeicao == null && other.ideJustificativaRejeicao != null) || (this.ideJustificativaRejeicao != null && !this.ideJustificativaRejeicao.equals(other.ideJustificativaRejeicao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
    	return String.valueOf(ideJustificativaRejeicao);
    }
    
}