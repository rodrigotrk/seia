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

@Entity
@Table(name="transferencia_ambiental")
public class TransferenciaAmbiental implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="transferencia_ambiental_seq")    
	@SequenceGenerator(name="transferencia_ambiental_seq", sequenceName="transferencia_ambiental_seq", allocationSize=1)
	@Column(name="ide_transferencia_ambiental", unique=true, nullable=false)
	private Integer ideTransferenciaAmbiental;

	@JoinColumn(name="ide_empreendimento_destino", referencedColumnName="ide_empreendimento", nullable=false)
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	private Empreendimento ideEmpreendimentoDestino;

	@JoinColumn(name="ide_empreendimento_origem", referencedColumnName="ide_empreendimento", nullable=false)
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	private Empreendimento ideEmpreendimentoOrigem;

	@JoinColumn(name="ide_processo_ato", referencedColumnName="ide_processo_ato", nullable=false)
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	private ProcessoAto ideProcessoAto;

	@JoinColumn(name="ide_processo_tla", referencedColumnName="ide_processo", nullable=false)
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	private Processo ideProcessoTla;

	public TransferenciaAmbiental(Integer ideTransferenciaAmbiental) {
		this.ideTransferenciaAmbiental = ideTransferenciaAmbiental;
	}

	public TransferenciaAmbiental() {
		
	}
	
	public Integer getIdeTransferenciaAmbiental() {
		return ideTransferenciaAmbiental;
	}

	public void setIdeTransferenciaAmbiental(Integer ideTransferenciaAmbiental) {
		this.ideTransferenciaAmbiental = ideTransferenciaAmbiental;
	}

	public Empreendimento getIdeEmpreendimentoDestino() {
		return ideEmpreendimentoDestino;
	}

	public void setIdeEmpreendimentoDestino(Empreendimento ideEmpreendimentoDestino) {
		this.ideEmpreendimentoDestino = ideEmpreendimentoDestino;
	}

	public Empreendimento getIdeEmpreendimentoOrigem() {
		return ideEmpreendimentoOrigem;
	}

	public void setIdeEmpreendimentoOrigem(Empreendimento ideEmpreendimentoOrigem) {
		this.ideEmpreendimentoOrigem = ideEmpreendimentoOrigem;
	}

	public ProcessoAto getIdeProcessoAto() {
		return ideProcessoAto;
	}

	public void setIdeProcessoAto(ProcessoAto ideProcessoAto) {
		this.ideProcessoAto = ideProcessoAto;
	}

	public Processo getIdeProcessoTla() {
		return ideProcessoTla;
	}

	public void setIdeProcessoTla(Processo ideProcessoTla) {
		this.ideProcessoTla = ideProcessoTla;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideTransferenciaAmbiental == null) ? 0
						: ideTransferenciaAmbiental.hashCode());
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
		TransferenciaAmbiental other = (TransferenciaAmbiental) obj;
		if (ideTransferenciaAmbiental == null) {
			if (other.ideTransferenciaAmbiental != null)
				return false;
		} else if (!ideTransferenciaAmbiental
				.equals(other.ideTransferenciaAmbiental))
			return false;
		return true;
	}
}