package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "fce_pesquisa_mineral_prospeccao")
@NamedQueries({
	@NamedQuery(name = "FcePesquisaMineralProspeccao.removeByIdeFcePesquisaMineralProspeccao",query = "DELETE FROM FcePesquisaMineralProspeccao fpmp WHERE fpmp.ideFcePesquisaMineral.ideFcePesquisaMineral = :ideFcePesquisaMineral"),
	@NamedQuery(name = "FcePesquisaMineralProspeccao.remove", query = "DELETE FROM FcePesquisaMineralProspeccao fpmp WHERE fpmp.ideFcePesquisaMineralProspeccao = :ideFcePesquisaMineralProspeccao")
})

public class FcePesquisaMineralProspeccao implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "FCE_PESQUISA_MINERAL_PROSPECCAO_IDE_FCEPESQUISAMINERAL_PROSPECCAO_GENERATOR", sequenceName = "FCE_PESQUISA_MINERAL_PROSPECCAO_IDE_FCE_PESQUISA_MINERAL_PROSPECCAO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FCE_PESQUISA_MINERAL_PROSPECCAO_IDE_FCEPESQUISAMINERAL_PROSPECCAO_GENERATOR")
	@Column(name = "ide_fce_pesquisa_mineral_prospeccao")
	private Integer ideFcePesquisaMineralProspeccao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_fce_pesquisa_mineral", nullable = false)
	private FcePesquisaMineral ideFcePesquisaMineral;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_metodo_prospeccao", nullable = false)
	private MetodoProspeccao ideMetodoProspeccao;
	
	@OneToMany(mappedBy="ideFcePesquisaMineralProspeccao", fetch=FetchType.LAZY)
	private List<FcePesquisaMineralProspeccaoGeofisica> listaFcePesquisaMineralProspeccaoGeofisica;
	
	@OneToMany(mappedBy="ideFcePesquisaMineralProspeccao", fetch=FetchType.LAZY)
	private List<FceProspeccao> listaFceProspeccao;
	
	public FcePesquisaMineralProspeccao() {
		inicializarListas();
	}
	
	public FcePesquisaMineralProspeccao(FcePesquisaMineral ideFcePesquisaMineral,MetodoProspeccao ideMetodoProspeccao) {
		this.ideFcePesquisaMineral = ideFcePesquisaMineral;
		this.ideMetodoProspeccao = ideMetodoProspeccao;
		inicializarListas();
	}

	private void inicializarListas(){
		listaFcePesquisaMineralProspeccaoGeofisica = new ArrayList<FcePesquisaMineralProspeccaoGeofisica>();
		listaFceProspeccao = new ArrayList<FceProspeccao>();
	}

	public Integer getIdeFcePesquisaMineralProspeccao() {
		return this.ideFcePesquisaMineralProspeccao;
	}

	public void setIdeFcePesquisaMineralProspeccao(Integer ideFcePesquisaMineralProspeccao) {
		this.ideFcePesquisaMineralProspeccao = ideFcePesquisaMineralProspeccao;
	}

	public FcePesquisaMineral getFcePesquisaMineral() {
		return this.ideFcePesquisaMineral;
	}

	public void setFcePesquisaMineral(FcePesquisaMineral fcePesquisaMineral) {
		this.ideFcePesquisaMineral = fcePesquisaMineral;
	}

	public FcePesquisaMineral getIdeFcePesquisaMineral() {
		return ideFcePesquisaMineral;
	}

	public void setIdeFcePesquisaMineral(FcePesquisaMineral ideFcePesquisaMineral) {
		this.ideFcePesquisaMineral = ideFcePesquisaMineral;
	}

	public MetodoProspeccao getIdeMetodoProspeccao() {
		return ideMetodoProspeccao;
	}

	public void setIdeMetodoProspeccao(MetodoProspeccao ideMetodoProspeccao) {
		this.ideMetodoProspeccao = ideMetodoProspeccao;
	}

	public List<FcePesquisaMineralProspeccaoGeofisica> getListaFcePesquisaMineralProspeccaoGeofisica() {
		return listaFcePesquisaMineralProspeccaoGeofisica;
	}

	public void setListaFcePesquisaMineralProspeccaoGeofisica(List<FcePesquisaMineralProspeccaoGeofisica> listaFcePesquisaMineralProspeccaoGeofisica) {
		this.listaFcePesquisaMineralProspeccaoGeofisica = listaFcePesquisaMineralProspeccaoGeofisica;
	}

	public List<FceProspeccao> getListaFceProspeccao() {
		return listaFceProspeccao;
	}

	public void setListaFceProspeccao(List<FceProspeccao> listaFceProspeccao) {
		this.listaFceProspeccao = listaFceProspeccao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideFcePesquisaMineralProspeccao == null) ? 0 : ideFcePesquisaMineralProspeccao.hashCode());
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
		FcePesquisaMineralProspeccao other = (FcePesquisaMineralProspeccao) obj;
		if(ideFcePesquisaMineralProspeccao == null){
			if(other.ideFcePesquisaMineralProspeccao != null)
				return false;
		}
		else if(!ideFcePesquisaMineralProspeccao.equals(other.ideFcePesquisaMineralProspeccao))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ideFcePesquisaMineralProspeccao.toString();
	}
	
	
	

}