package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.enumerator.MetodoProspeccaoEnum;
import br.gov.ba.seia.interfaces.BaseEntity;

/**
 * The persistent class for the metodo_prospeccao database table.
 * 
 */
@Entity
@Table(name = "metodo_prospeccao")
public class MetodoProspeccao implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "METODO_PROSPECCAO_IDEMETODOPROSPECCAO_GENERATOR", sequenceName = "METODO_PROSPECCAO_IDE_METODO_PROSPECCAO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "METODO_PROSPECCAO_IDEMETODOPROSPECCAO_GENERATOR")
	@Column(name = "ide_metodo_prospeccao")
	private Integer ideMetodoProspeccao;

	@Column(name = "nom_metodo_prospeccao")
	private String nomMetodoProspeccao;

	@OneToMany(mappedBy="ideFcePesquisaMineralProspeccao", fetch=FetchType.LAZY)
	private List<FcePesquisaMineralProspeccao> listaFcePesquisaMineralProspeccao;
	
	@OneToMany(mappedBy="ideProcessoDnpmProspecao", fetch=FetchType.LAZY)
	private List<ProcessoDnpmProspecao> listaProcessoDnpmProspecao;
	
	@Transient
	private boolean checked;
	
	public MetodoProspeccao() {
	}

	public MetodoProspeccao(Integer ideMetodoProspeccao) {
		this.ideMetodoProspeccao = ideMetodoProspeccao;
	}

	public MetodoProspeccao(MetodoProspeccaoEnum metodoEnum) {
		this.ideMetodoProspeccao = metodoEnum.getId();
	}

	public Integer getIdeMetodoProspeccao() {
		return this.ideMetodoProspeccao;
	}

	public void setIdeMetodoProspeccao(Integer ideMetodoProspeccao) {
		this.ideMetodoProspeccao = ideMetodoProspeccao;
	}

	public String getNomMetodoProspeccao() {
		return this.nomMetodoProspeccao;
	}

	public void setNomMetodoProspeccao(String nomMetodoProspeccao) {
		this.nomMetodoProspeccao = nomMetodoProspeccao;
	}
	
	public List<FcePesquisaMineralProspeccao> getListaFcePesquisaMineralProspeccao() {
		return listaFcePesquisaMineralProspeccao;
	}

	public void setListaFcePesquisaMineralProspeccao(List<FcePesquisaMineralProspeccao> listaFcePesquisaMineralProspeccao) {
		this.listaFcePesquisaMineralProspeccao = listaFcePesquisaMineralProspeccao;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideMetodoProspeccao == null) ? 0 : ideMetodoProspeccao.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		MetodoProspeccao other = (MetodoProspeccao) obj;
		if(ideMetodoProspeccao == null){
			if(other.ideMetodoProspeccao != null)
				return false;
		}
		else if(!ideMetodoProspeccao.equals(other.ideMetodoProspeccao))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return  ideMetodoProspeccao	+" "+ nomMetodoProspeccao  + " " + checked ;
	}

	public List<ProcessoDnpmProspecao> getListaProcessoDnpmProspecao() {
		return listaProcessoDnpmProspecao;
	}

	public void setListaProcessoDnpmProspecao(List<ProcessoDnpmProspecao> listaProcessoDnpmProspecao) {
		this.listaProcessoDnpmProspecao = listaProcessoDnpmProspecao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.gov.ba.seia.entity.BaseEntity#getId()
	 */
	@Override
	public Long getId() {
		return new Long(this.ideMetodoProspeccao);
	}

}