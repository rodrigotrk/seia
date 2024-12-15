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

@Entity
@Table(name = "geofisica")
public class Geofisica implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "GEOFISICA_IDEGEOFISICA_GENERATOR", sequenceName = "GEOFISICA_IDE_GEOFISICA_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEOFISICA_IDEGEOFISICA_GENERATOR")
	@Column(name = "ide_geofisica")
	private Integer ideGeofisica;

	@Column(name = "nom_geofisica")
	private String nomGeofisica;

	@OneToMany(mappedBy="ideGeofisica", fetch=FetchType.LAZY)
	private List<FcePesquisaMineralProspeccaoGeofisica> listaFcePesquisaMineralProspeccaoGeofisica;
	
	@Transient
	private boolean checked;
	
	public Geofisica() {
	}

	public Integer getIdeGeofisica() {
		return this.ideGeofisica;
	}

	public void setIdeGeofisica(Integer ideGeofisica) {
		this.ideGeofisica = ideGeofisica;
	}

	public String getNomGeofisica() {
		return this.nomGeofisica;
	}

	public void setNomGeofisica(String nomGeofisica) {
		this.nomGeofisica = nomGeofisica;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	

	public List<FcePesquisaMineralProspeccaoGeofisica> getListaFcePesquisaMineralProspeccaoGeofisica() {
		return listaFcePesquisaMineralProspeccaoGeofisica;
	}

	public void setListaFcePesquisaMineralProspeccaoGeofisica(List<FcePesquisaMineralProspeccaoGeofisica> listaFcePesquisaMineralProspeccaoGeofisica) {
		this.listaFcePesquisaMineralProspeccaoGeofisica = listaFcePesquisaMineralProspeccaoGeofisica;
	}

	public boolean isOutros(){
		if(nomGeofisica.contains("Outros")){
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideGeofisica == null) ? 0 : ideGeofisica.hashCode());
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
		Geofisica other = (Geofisica) obj;
		if(ideGeofisica == null){
			if(other.ideGeofisica != null)
				return false;
		}
		else if(!ideGeofisica.equals(other.ideGeofisica))
			return false;
		return true;
	}

}