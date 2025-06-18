package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name = "notificacao_motivo_notificacao")
public class NotificacaoMotivoNotificacao implements Serializable, BaseEntity, Cloneable {
    
	private static final long serialVersionUID = 6226065680764001792L;
	
	@Id
    @SequenceGenerator(name="notificacao_motivo_notificacao_seq", sequenceName="notificacao_motivo_notificacao_seq",allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="notificacao_motivo_notificacao_seq")
    @Column(name = "ide_notificacao_motivo_notificacao")
	private Integer ideNotificacaoMotivoNotificacao;
	
	@JoinColumn(name = "ide_notificacao", referencedColumnName = "ide_notificacao", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Notificacao ideNotificacao;
	
	@JoinColumn(name = "ide_motivo_notificacao", referencedColumnName = "ide_motivo_notificacao", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private MotivoNotificacao ideMotivoNotificacao;
	
	@OneToMany(mappedBy = "ideNotificacaoMotivoNotificacao", cascade = CascadeType.MERGE)
    private Collection<MotivoNotificacaoImovel> motivoNotificacaoImovelCollection;

	@Transient
	private boolean semImovel;
	
	public Integer getIdeNotificacaoMotivoNotificacao() {
		return ideNotificacaoMotivoNotificacao;
	}

	public void setIdeNotificacaoMotivoNotificacao(Integer ideNotificacaoMotivoNotificacao) {
		this.ideNotificacaoMotivoNotificacao = ideNotificacaoMotivoNotificacao;
	}

	public Notificacao getIdeNotificacao() {
		return ideNotificacao;
	}

	public void setIdeNotificacao(Notificacao ideNotificacao) {
		this.ideNotificacao = ideNotificacao;
	}

	public MotivoNotificacao getIdeMotivoNotificacao() {
		return ideMotivoNotificacao;
	}

	public void setIdeMotivoNotificacao(MotivoNotificacao ideMotivoNotificacao) {
		this.ideMotivoNotificacao = ideMotivoNotificacao;
	}
	
	public Collection<MotivoNotificacaoImovel> getMotivoNotificacaoImovelCollection() {
		return motivoNotificacaoImovelCollection;
	}

	public void setMotivoNotificacaoImovelCollection(
			Collection<MotivoNotificacaoImovel> motivoNotificacaoImovelCollection) {
		this.motivoNotificacaoImovelCollection = motivoNotificacaoImovelCollection;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideNotificacaoMotivoNotificacao == null) ? 0
						: ideNotificacaoMotivoNotificacao.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NotificacaoMotivoNotificacao other = (NotificacaoMotivoNotificacao) obj;
		if (ideNotificacaoMotivoNotificacao == null) {
			if (other.ideNotificacaoMotivoNotificacao != null)
				return false;
		} else if (!ideNotificacaoMotivoNotificacao
				.equals(other.ideNotificacaoMotivoNotificacao))
			return false;
		return true;
	}

	@Override
	public Long getId() {
		return Long.valueOf(ideNotificacaoMotivoNotificacao);
	}
	
	@Override
	public NotificacaoMotivoNotificacao clone() throws CloneNotSupportedException {
		return (NotificacaoMotivoNotificacao) super.clone();
	}

	public boolean isSemImovel() {
		return semImovel;
	}

	public void setSemImovel(boolean semImovel) {
		this.semImovel = semImovel;
	}
}