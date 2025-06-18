package br.gov.ba.seia.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name = "fce_linha_energia_destino_residuo")
@NamedQuery(name = "FceLinhaEnergiaDestinoResiduo.removeFceLinhaEnergiaDestinoResiduo", query = "DELETE FROM FceLinhaEnergiaDestinoResiduo f WHERE f.ideFceLinhaEnergia = :ideFceLinhaEnergia and f.ideDestinoResiduo = :ideDestinoResiduo")
public class FceLinhaEnergiaDestinoResiduo extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ide_fce_linha_energia_destino_residuo")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ide_fce_linha_energia_destino_residuo_seq")
	@SequenceGenerator(name = "ide_fce_linha_energia_destino_residuo_seq", sequenceName = "ide_fce_linha_energia_destino_residuo_seq", allocationSize = 1)
	private Integer ideFceLinhaEnergiaDestinoResiduo;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ide_fce_linha_energia", referencedColumnName= "ide_fce_linha_energia", nullable = false)
	private FceLinhaEnergia ideFceLinhaEnergia;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ide_destino_residuo", referencedColumnName = "ide_destino_residuo", nullable = false)
	private DestinoResiduo ideDestinoResiduo;
	
	@Transient
	private List<DestinoResiduo> destinoResiduoCollection;  

	public FceLinhaEnergiaDestinoResiduo() {
	}

	public FceLinhaEnergiaDestinoResiduo(FceLinhaEnergia fceLinhaEnergia, DestinoResiduo destinoResiduo) {
		this.ideFceLinhaEnergia = fceLinhaEnergia;
		this.ideDestinoResiduo = destinoResiduo;
	}
	
	public Integer getIdeFceLinhaEnergiaDestinoResiduo() {
		return ideFceLinhaEnergiaDestinoResiduo;
	}

	public void setIdeFceLinhaEnergiaDestinoResiduo(Integer ideFceLinhaEnergiaDestinoResiduo) {
		this.ideFceLinhaEnergiaDestinoResiduo = ideFceLinhaEnergiaDestinoResiduo;
	}

	public FceLinhaEnergia getIdeFceLinhaEnergia() {
		return ideFceLinhaEnergia;
	}

	public void setIdeFceLinhaEnergia(FceLinhaEnergia ideFceLinhaEnergia) {
		this.ideFceLinhaEnergia = ideFceLinhaEnergia;
	}

	public DestinoResiduo getIdeDestinoResiduo() {
		return ideDestinoResiduo;
	}

	public void setIdeDestinoResiduo(DestinoResiduo destinoResiduo) {
		this.ideDestinoResiduo = destinoResiduo;
	}

	public List<DestinoResiduo> getDestinoResiduoCollection() {
		return destinoResiduoCollection;
	}

	public void setDestinoResiduoCollection(List<DestinoResiduo> destinoResiduoCollection) {
		this.destinoResiduoCollection = destinoResiduoCollection;
	}
}
