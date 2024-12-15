package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name = "motivo_edicao_notificacao", uniqueConstraints = {@UniqueConstraint(columnNames = {"ide_motivo_edicao_notificacao"})})
public class MotivoEdicaoNotificacao implements Serializable, BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="motivo_edicao_notificacao_ide_motivo_edicao_notificacao_seq")    
	@SequenceGenerator(name="motivo_edicao_notificacao_ide_motivo_edicao_notificacao_seq", sequenceName="motivo_edicao_notificacao_ide_motivo_edicao_notificacao_seq", allocationSize=1)
	@Basic(optional = false)
	@Column(name = "ide_motivo_edicao_notificacao", nullable = false)
	private Integer ideMotivoEdicaoNotificacao;

	@Column(name = "nom_motivo_edicao_notificacao", nullable = false)
	private String nomMotivoEdicaoNotificacao;

	@JoinTable(name = "notificacao_motivo_edicao_notificacao",  joinColumns = {
			@JoinColumn(name = "ide_motivo_edicao_notificacao", referencedColumnName = "ide_motivo_edicao_notificacao", nullable = false)},inverseJoinColumns = {
			@JoinColumn(name = "ide_notificacao", referencedColumnName = "ide_notificacao", nullable = false)})
	@ManyToMany(fetch = FetchType.LAZY)
	private Collection<Notificacao> notificacaoCollection;

	public MotivoEdicaoNotificacao(){}

	public MotivoEdicaoNotificacao(Integer ideMotivoEdicaoNotificacao){
		this.ideMotivoEdicaoNotificacao = ideMotivoEdicaoNotificacao;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ideMotivoEdicaoNotificacao != null ? ideMotivoEdicaoNotificacao.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof MotivoEdicaoNotificacao)) {
			return false;
		}
		MotivoEdicaoNotificacao other = (MotivoEdicaoNotificacao) object;
		if ((this.ideMotivoEdicaoNotificacao == null && other.ideMotivoEdicaoNotificacao != null) || (this.ideMotivoEdicaoNotificacao != null && !this.ideMotivoEdicaoNotificacao.equals(other.ideMotivoEdicaoNotificacao))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return String.valueOf(ideMotivoEdicaoNotificacao);
	}

	@Override
	public Long getId() {
		return ideMotivoEdicaoNotificacao.longValue();
	}

	public Integer getIdeMotivoEdicaoNotificacao() {
		return ideMotivoEdicaoNotificacao;
	}

	public void setIdeMotivoEdicaoNotificacao(Integer ideMotivoEdicaoNotificacao) {
		this.ideMotivoEdicaoNotificacao = ideMotivoEdicaoNotificacao;
	}

	public String getNomMotivoEdicaoNotificacao() {
		return nomMotivoEdicaoNotificacao;
	}

	public void setNomMotivoEdicaoNotificacao(String dscMotivoEdicaoNotificacao) {
		this.nomMotivoEdicaoNotificacao = dscMotivoEdicaoNotificacao;
	}

	public Collection<Notificacao> getNotificacaoCollection() {
		return notificacaoCollection;
	}

	public void setNotificacaoCollection(Collection<Notificacao> notificacaoCollection) {
		this.notificacaoCollection = notificacaoCollection;
	}

}