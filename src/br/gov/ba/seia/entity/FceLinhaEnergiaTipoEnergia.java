package br.gov.ba.seia.entity;

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

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name = "fce_linha_energia_tipo_energia")
@NamedQuery(name = "FceLinhaEnergiaTipoEnergia.removeFceLinhaEnergiaTipoEnergia", query = "DELETE FROM FceLinhaEnergiaTipoEnergia f WHERE f.ideFceLinhaEnergia = :ideFceLinhaEnergia and f.ideFceLinhaEnergiaTipoEnergia = :ideFceLinhaEnergiaTipoEnergia")
public class FceLinhaEnergiaTipoEnergia extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ide_fce_linha_energia_tipo_energia")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ide_fce_linha_energia_tipo_energia_seq")
	@SequenceGenerator(name = "ide_fce_linha_energia_tipo_energia_seq", sequenceName = "ide_fce_linha_energia_tipo_energia_seq", allocationSize = 1)
	private Integer ideFceLinhaEnergiaTipoEnergia;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ide_fce_linha_energia", referencedColumnName = "ide_fce_linha_energia", nullable = false)
	private FceLinhaEnergia ideFceLinhaEnergia;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ide_tipo_energia", referencedColumnName = "ide_tipo_energia", nullable= false)
	private TipoEnergia tipoEnergia;

	public FceLinhaEnergiaTipoEnergia() {
	}

	public FceLinhaEnergiaTipoEnergia(FceLinhaEnergia fceLinhaEnergia){
		this.ideFceLinhaEnergia = fceLinhaEnergia;
	}
	
	public Integer getIdeFceLinhaEnergiaTipoEnergia() {
		return ideFceLinhaEnergiaTipoEnergia;
	}

	public void setIdeFceLinhaEnergiaTipoEnergia(Integer ideFceLinhaEnergiaTipoEnergia) {
		this.ideFceLinhaEnergiaTipoEnergia = ideFceLinhaEnergiaTipoEnergia;
	}

	public FceLinhaEnergia getIdeFceLinhaEnergia() {
		return ideFceLinhaEnergia;
	}

	public void setIdeFceLinhaEnergia(FceLinhaEnergia ideFceLinhaEnergia) {
		this.ideFceLinhaEnergia = ideFceLinhaEnergia;
	}

	public TipoEnergia getTipoEnergia() {
		return tipoEnergia;
	}
	
	public void setTipoEnergia(TipoEnergia tipoEnergia) {
		this.tipoEnergia = tipoEnergia;
	}
}
