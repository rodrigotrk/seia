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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "fce_pesquisa_mineral")
public class FcePesquisaMineral implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "FCE_PESQUISA_MINERAL_IDEFCEPESQUISAMINERAL_GENERATOR", sequenceName = "FCE_PESQUISA_MINERAL_IDE_FCE_PESQUISA_MINERAL_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FCE_PESQUISA_MINERAL_IDEFCEPESQUISAMINERAL_GENERATOR")
	@Column(name = "ide_fce_pesquisa_mineral")
	private Integer ideFcePesquisaMineral;

	@NotNull
	@JoinColumn(name = "ide_fce", referencedColumnName = "ide_fce", nullable = false)
	@OneToOne(fetch = FetchType.LAZY)
	private Fce ideFce;

	@Column(name = "ind_esfera_estadual")
	private Boolean indEsferaEstadual;

	@Column(name = "ind_esfera_federal")
	private Boolean indEsferaFederal;

	@Column(name = "ind_pesqinterv_recurso_hidrico")
	private Boolean indPesqIntervRecursoHidrico;
				  
	@Column(name = "ind_supressao")
	private Boolean indSupressao;
	
	@OneToMany(mappedBy="ideFcePesquisaMineral", fetch=FetchType.LAZY)
	private List<FcePesquisaMineralOrigemEnergia> listaFcePesquisaMineralOrigemEnergia;

	@OneToMany(mappedBy="ideFcePesquisaMineral", fetch=FetchType.LAZY)
	private List<FcePesquisaMineralSubstanciaMineralTipologia> listaFcePesquisaMineralSubstanciaMineral;
	
	@OneToMany(mappedBy="ideFcePesquisaMineral", fetch=FetchType.LAZY )
	private List<FcePesquisaMineralProspeccao> listaFcePesquisaMineralProspeccao;

	@OneToMany(mappedBy="ideFcePesquisaMineral", fetch=FetchType.LAZY)
	private List<FcePesquisaMineralTipoResiduo> listaFcePesquisaMineralTipoResiduo;
	
	@OneToMany(mappedBy="ideFcePesquisaMineral", fetch=FetchType.LAZY)
	private List<FcePesquisaMineralDestinoResiduo> listaFcePesquisaMineralDestinoResiduo;
	
	@OneToMany(mappedBy="ideFcePesquisaMineral", fetch=FetchType.LAZY)
	private List<ProcessoDnpm> listaProcessoDNPM;
	
	public FcePesquisaMineral() {
		inicializarListas();
	}

	public FcePesquisaMineral(Fce ideFce) {
		this.ideFce = ideFce;
		inicializarListas();
	}
	
	private void inicializarListas(){
		listaProcessoDNPM = new ArrayList<ProcessoDnpm>();
	}

	public Integer getIdeFcePesquisaMineral() {
		return ideFcePesquisaMineral;
	}

	public void setIdeFcePesquisaMineral(Integer ideFcePesquisaMineral) {
		this.ideFcePesquisaMineral = ideFcePesquisaMineral;
	}

	public Fce getIdeFce() {
		return ideFce;
	}

	public void setIdeFce(Fce ideFce) {
		this.ideFce = ideFce;
	}

	public Boolean getIndEsferaEstadual() {
		return indEsferaEstadual;
	}

	public void setIndEsferaEstadual(Boolean indEsferaEstadual) {
		this.indEsferaEstadual = indEsferaEstadual;
	}

	public Boolean getIndEsferaFederal() {
		return indEsferaFederal;
	}

	public void setIndEsferaFederal(Boolean indEsferaFederal) {
		this.indEsferaFederal = indEsferaFederal;
	}

	public Boolean getIndPesqIntervRecursoHidrico() {
		return indPesqIntervRecursoHidrico;
	}

	public void setIndPesqIntervRecursoHidrico(Boolean indPesqIntervRecursoHidrico) {
		this.indPesqIntervRecursoHidrico = indPesqIntervRecursoHidrico;
	}

	public Boolean getIndSupressao() {
		return indSupressao;
	}

	public void setIndSupressao(Boolean indSupressao) {
		this.indSupressao = indSupressao;
	}
	
	public List<FcePesquisaMineralSubstanciaMineralTipologia> getListaFcePesquisaMineralSubstanciaMineral() {
		return listaFcePesquisaMineralSubstanciaMineral;
	}

	public void setListaFcePesquisaMineralSubstanciaMineral(List<FcePesquisaMineralSubstanciaMineralTipologia> listaFcePesquisaMineralSubstanciaMineral) {
		this.listaFcePesquisaMineralSubstanciaMineral = listaFcePesquisaMineralSubstanciaMineral;
	}
	
	public List<FcePesquisaMineralProspeccao> getListaFcePesquisaMineralProspeccao() {
		return listaFcePesquisaMineralProspeccao;
	}

	public void setListaFcePesquisaMineralProspeccao(List<FcePesquisaMineralProspeccao> listaFcePesquisaMineralProspeccao) {
		this.listaFcePesquisaMineralProspeccao = listaFcePesquisaMineralProspeccao;
	}

	public List<ProcessoDnpm> getListaProcessoDNPM() {
		return listaProcessoDNPM;
	}

	public void setListaProcessoDNPM(List<ProcessoDnpm> listaProcessoDNPM) {
		this.listaProcessoDNPM = listaProcessoDNPM;
	}

	
	public List<FcePesquisaMineralOrigemEnergia> getListaFcePesquisaMineralOrigemEnergia() {
		return listaFcePesquisaMineralOrigemEnergia;
	}

	public void setListaFcePesquisaMineralOrigemEnergia(List<FcePesquisaMineralOrigemEnergia> listaFcePesquisaMineralOrigemEnergia) {
		this.listaFcePesquisaMineralOrigemEnergia = listaFcePesquisaMineralOrigemEnergia;
	}
	
	public List<FcePesquisaMineralTipoResiduo> getListaFcePesquisaMineralTipoResiduo() {
		return listaFcePesquisaMineralTipoResiduo;
	}

	public void setListaFcePesquisaMineralTipoResiduo(
			List<FcePesquisaMineralTipoResiduo> listaFcePesquisaMineralTipoResiduo) {
		this.listaFcePesquisaMineralTipoResiduo = listaFcePesquisaMineralTipoResiduo;
	}

	public List<FcePesquisaMineralDestinoResiduo> getListaFcePesquisaMineralDestinoResiduo() {
		return listaFcePesquisaMineralDestinoResiduo;
	}

	public void setListaFcePesquisaMineralDestinoResiduo(List<FcePesquisaMineralDestinoResiduo> listaFcePesquisaMineralDestinoResiduo) {
		this.listaFcePesquisaMineralDestinoResiduo = listaFcePesquisaMineralDestinoResiduo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideFce == null) ? 0 : ideFce.hashCode());
		result = prime * result	+ ((ideFcePesquisaMineral == null) ? 0 : ideFcePesquisaMineral.hashCode());
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
		FcePesquisaMineral other = (FcePesquisaMineral) obj;
		if (ideFce == null) {
			if (other.ideFce != null)
				return false;
		} else if (!ideFce.equals(other.ideFce))
			return false;
		if (ideFcePesquisaMineral == null) { 
			if (other.ideFcePesquisaMineral != null) return false;
		} else if (!ideFcePesquisaMineral.equals(other.ideFcePesquisaMineral))	return false;
		
		return true;
	}



}