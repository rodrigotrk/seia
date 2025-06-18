package br.gov.ba.seia.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.enumerator.TccaProjetoTipoStatusEnum;

@Entity
@Table(name="tcca_projeto_tipo_status")
@NamedQuery(name="TccaProjetoTipoStatus.findAll", query="SELECT t FROM TccaProjetoTipoStatus t")
public class TccaProjetoTipoStatus extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ide_tcca_projeto_tipo_status", updatable=false, unique=true, nullable=false)
	private Integer ideTccaProjetoTipoStatus;

	@Column(name="nom_tcca_tipo_status", nullable=false, length=100)
	private String nomTccaTipoStatus;

	@OneToMany(mappedBy="ideTccaProjetoTipoStatus")
	private List<ProjetoStatus> projetoStatusCollection;

	@OneToMany(mappedBy="ideTccaProjetoTipoStatus")
	private List<TccaStatus> tccaStatusCollection;

	public TccaProjetoTipoStatus() {
	}
	
	public TccaProjetoTipoStatus(TccaProjetoTipoStatusEnum statusEnum) {
		ideTccaProjetoTipoStatus = statusEnum.getId();
		nomTccaTipoStatus = statusEnum.getNome();
	}

	public Integer getIdeTccaProjetoTipoStatus() {
		return this.ideTccaProjetoTipoStatus;
	}

	public void setIdeTccaProjetoTipoStatus(Integer ideTccaProjetoTipoStatus) {
		this.ideTccaProjetoTipoStatus = ideTccaProjetoTipoStatus;
	}

	public String getNomTccaTipoStatus() {
		return this.nomTccaTipoStatus;
	}

	public void setNomTccaTipoStatus(String nomTccaTipoStatus) {
		this.nomTccaTipoStatus = nomTccaTipoStatus;
	}

	public List<ProjetoStatus> getProjetoStatusCollection() {
		return this.projetoStatusCollection;
	}

	public void setProjetoStatusCollection(List<ProjetoStatus> projetoStatusCollection) {
		this.projetoStatusCollection = projetoStatusCollection;
	}

	public List<TccaStatus> getTccaStatusCollection() {
		return this.tccaStatusCollection;
	}

	public void setTccaStatusCollection(List<TccaStatus> tccaStatusCollection) {
		this.tccaStatusCollection = tccaStatusCollection;
	}
}