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
@Table(name = "fce_linha_energia_residuo_gerado")
@NamedQuery(name = "FceLinhaEnergiaResiduoGerado.removeFceLinhaEnergiaResiduoGerado", query = "DELETE FROM FceLinhaEnergiaResiduoGerado f WHERE f.ideFceLinhaEnergia = :ideFceLinhaEnergia and f.ideTipoResiduoGerado = :ideTipoResiduoGerado")
public class FceLinhaEnergiaResiduoGerado extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ide_fce_linha_energia_residuo_gerado")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ide_fce_linha_energia_residuo_gerado_seq")
	@SequenceGenerator(name = "ide_fce_linha_energia_residuo_gerado_seq", sequenceName = "ide_fce_linha_energia_residuo_gerado_seq", allocationSize = 1)
	private Integer ideFceLinhaEnergiaResiduoGerado;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ide_fce_linha_energia", referencedColumnName = "ide_fce_linha_energia", nullable = false)
	private FceLinhaEnergia ideFceLinhaEnergia;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ide_tipo_residuo_gerado", referencedColumnName = "ide_tipo_residuo_gerado", nullable = false)
	private TipoResiduoGerado ideTipoResiduoGerado;

	@Transient
	private List<TipoResiduoGerado> tipoResiduoGeradoCollection;
	
	public FceLinhaEnergiaResiduoGerado() {
	}
	
	public FceLinhaEnergiaResiduoGerado(FceLinhaEnergia fceLinhaEnergia,TipoResiduoGerado tipoResiduoGerado) {
		
		this.ideFceLinhaEnergia = fceLinhaEnergia;
		this.ideTipoResiduoGerado = tipoResiduoGerado;
	}

	public Integer getIdeFceLinhaEnergiaResiduoGerado() {
		return ideFceLinhaEnergiaResiduoGerado;
	}

	public void setIdeFceLinhaEnergiaResiduoGerado(
			Integer ideFceLinhaEnergiaResiduoGerado) {
		this.ideFceLinhaEnergiaResiduoGerado = ideFceLinhaEnergiaResiduoGerado;
	}

	public FceLinhaEnergia getIdeFceLinhaEnergia() {
		return ideFceLinhaEnergia;
	}

	public void setIdeFceLinhaEnergia(FceLinhaEnergia ideFceLinhaEnergia) {
		this.ideFceLinhaEnergia = ideFceLinhaEnergia;
	}

	public TipoResiduoGerado getIdeTipoResiduoGerado() {
		return ideTipoResiduoGerado;
	}

	public void setIdeTipoResiduoGerado(TipoResiduoGerado ideTipoResiduoGerado) {
		this.ideTipoResiduoGerado = ideTipoResiduoGerado;
	}

	public List<TipoResiduoGerado> getTipoResiduoGeradoCollection() {
		return tipoResiduoGeradoCollection;
	}

	public void setTipoResiduoGeradoCollection(
			List<TipoResiduoGerado> tipoResiduoGeradoCollection) {
		this.tipoResiduoGeradoCollection = tipoResiduoGeradoCollection;
	}
}
