package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.enumerator.StatusProcessoAtoEnum;
import br.gov.ba.seia.util.Util;


@Entity
@Table(name="tipo_status_processo_ato")
public class StatusProcessoAto implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="tipo_status_processo_ato")    
	@SequenceGenerator(name="tipo_status_processo_ato", sequenceName="tipo_status_processo_ato", allocationSize=1)
	@Column(name="ide_tipo_status_processo_ato", unique=true, nullable=false)
	private Integer ideStatusProcessoAto;

	@Column(name="nom_tipo_status_processo_ato", nullable=false, length=50)
	private String nomStatusProcessoAto;
				  
	@OneToMany(mappedBy="ideStatusProcessoAto", fetch=FetchType.LAZY)
	private Collection<ControleProcessoAto> controleProcessoAtoCollection;

	public StatusProcessoAto() {
		
	}
	
	public StatusProcessoAto(StatusProcessoAtoEnum statusProcessoAtoEnum) {
		this.ideStatusProcessoAto = statusProcessoAtoEnum.getId();
	}
	
	public String getDescricao() {
		
		if(StatusProcessoAtoEnum.DEFERIDO_PELO_TECNICO.getId().equals(ideStatusProcessoAto)) {
			return Util.getString("analise_tecnica_lbl_status_ato_deferido");
		}
		else if(StatusProcessoAtoEnum.INDEFERIDO_PELO_TECNICO.getId().equals(ideStatusProcessoAto)) {
			return Util.getString("analise_tecnica_lbl_status_ato_indeferido");
		}
		else {
			return nomStatusProcessoAto;
		}
	}
	
	public boolean isEmAnalise() {
		return new StatusProcessoAto(StatusProcessoAtoEnum.EM_ANALISE).equals(this);
	}
	
	public boolean isAguardandoAnalise() {
		return new StatusProcessoAto(StatusProcessoAtoEnum.AGUARDANDO_ANALISE).equals(this);
	}
	
	public boolean isDeferido() {
		return new StatusProcessoAto(StatusProcessoAtoEnum.DEFERIDO).equals(this);
	}
	
	public boolean isDeferidoPeloTecnico() {
		return new StatusProcessoAto(StatusProcessoAtoEnum.DEFERIDO_PELO_TECNICO).equals(this);
	}
	
	public boolean isTransferido() {
		return new StatusProcessoAto(StatusProcessoAtoEnum.TRANSFERIDO).equals(this);
	}
	
	public boolean isIndeferido() {
		return new StatusProcessoAto(StatusProcessoAtoEnum.INDEFERIDO).equals(this);
	}
	
	public boolean isIndeferidoPeloTecnico() {
		return new StatusProcessoAto(StatusProcessoAtoEnum.INDEFERIDO_PELO_TECNICO).equals(this);
	}
	
	public StatusProcessoAto(Integer ideStatusProcessoAto) {
		this.ideStatusProcessoAto = ideStatusProcessoAto; 
	}

	public Integer getIdeStatusProcessoAto() {
		return ideStatusProcessoAto;
	}

	public void setIdeStatusProcessoAto(Integer ideStatusProcessoAto) {
		this.ideStatusProcessoAto = ideStatusProcessoAto;
	}

	public String getNomStatusProcessoAto() {
		return nomStatusProcessoAto;
	}

	public void setNomStatusProcessoAto(String nomStatusProcessoAto) {
		this.nomStatusProcessoAto = nomStatusProcessoAto;
	}

	public Collection<ControleProcessoAto> getControleProcessoAtoCollection() {
		return controleProcessoAtoCollection;
	}

	public void setControleProcessoAtoCollection(Collection<ControleProcessoAto> controleProcessoAtoCollection) {
		this.controleProcessoAtoCollection = controleProcessoAtoCollection;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideStatusProcessoAto == null) ? 0
						: ideStatusProcessoAto.hashCode());
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
		StatusProcessoAto other = (StatusProcessoAto) obj;
		if (ideStatusProcessoAto == null) {
			if (other.ideStatusProcessoAto != null)
				return false;
		} else if (!ideStatusProcessoAto
				.equals(other.ideStatusProcessoAto))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.valueOf(ideStatusProcessoAto);
	}
	
	
}