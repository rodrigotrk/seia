package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name = "motivo_notificacao_imovel")
public class MotivoNotificacaoImovel implements Serializable, BaseEntity, Cloneable {
    
	private static final long serialVersionUID = 6235688216337023989L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="motivo_notificacao_imovel_seq")    
	@SequenceGenerator(name="motivo_notificacao_imovel_seq", sequenceName="motivo_notificacao_imovel_seq", allocationSize=1)
    @Column(name = "ide_motivo_notificacao_imovel", unique=true, nullable=false)
    private Integer ideMotivoNotificacaoImovel;
	
	@JoinColumn(name = "ide_notificacao_motivo_notificacao", referencedColumnName = "ide_notificacao_motivo_notificacao", nullable = false)
	@ManyToOne(optional=false, fetch=FetchType.EAGER)
	private NotificacaoMotivoNotificacao ideNotificacaoMotivoNotificacao;
	
	@JoinColumn(name = "ide_imovel", referencedColumnName = "ide_imovel", nullable = false)
	@ManyToOne(optional=false, fetch=FetchType.EAGER)
	private Imovel ideImovel;
	
	@Transient
	private	boolean visualizarShape;
	

	public Integer getIdeMotivoNotificacaoImovel() {
		return ideMotivoNotificacaoImovel;
	}

	public void setIdeMotivoNotificacaoImovel(Integer ideMotivoNotificacaoImovel) {
		this.ideMotivoNotificacaoImovel = ideMotivoNotificacaoImovel;
	}

	public NotificacaoMotivoNotificacao getIdeNotificacaoMotivoNotificacao() {
		return ideNotificacaoMotivoNotificacao;
	}

	public void setIdeNotificacaoMotivoNotificacao(
			NotificacaoMotivoNotificacao ideNotificacaoMotivoNotificacao) {
		this.ideNotificacaoMotivoNotificacao = ideNotificacaoMotivoNotificacao;
	}

	public Imovel getIdeImovel() {
		return ideImovel;
	}

	public void setIdeImovel(Imovel ideImovel) {
		this.ideImovel = ideImovel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideMotivoNotificacaoImovel == null) ? 0
						: ideMotivoNotificacaoImovel.hashCode());
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
		MotivoNotificacaoImovel other = (MotivoNotificacaoImovel) obj;
		if (ideMotivoNotificacaoImovel == null) {
			if (other.ideMotivoNotificacaoImovel != null)
				return false;
		} else if (!ideMotivoNotificacaoImovel
				.equals(other.ideMotivoNotificacaoImovel))
			return false;
		return true;
	}

	@Override
	public Long getId() {
		return Long.valueOf(ideMotivoNotificacaoImovel);
	}
	
	public MotivoNotificacaoImovel clone() throws CloneNotSupportedException {
		return (MotivoNotificacaoImovel) super.clone();
	}

	public boolean isVisualizarShape() {
		return visualizarShape;
	}

	public void setVisualizarShape(boolean visualizarShape) {
		this.visualizarShape = visualizarShape;
	}

	
    
}