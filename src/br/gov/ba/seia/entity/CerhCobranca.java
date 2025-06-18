package br.gov.ba.seia.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.entity.generic.AbstractEntity;

/**
 * @author lesantos
 *
 */
@Entity
@Table(name = "cerh_cobranca")
public class CerhCobranca extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cerh_cobranca_seq")
	@SequenceGenerator(name = "cerh_cobranca_seq", sequenceName = "cerh_cobranca_seq", allocationSize = 1)
	@Column(name = "ide_cerh_cobranca")
	private Integer ideCerhCobranca;
	
	@ManyToOne
	@JoinColumn(name = "ide_cerh")
	private Cerh cerh;
	
	@Column(name = "num_ano_cobranca")
	private Integer numAnoCobranca;
	
	@Column(name = "ind_parcelado")
	private boolean ind_parcelado;
	
	@Column(name = "num_max_parcelas")
	private Integer numMaxParcelas;
	
	@Column(name = "ind_envio_correios")
	private boolean ind_correios;
	
	@OneToMany(mappedBy = "ideCerhCobranca", cascade = CascadeType.ALL)
	private List<CerhParcelasCobranca> cerhParcelasCobrancasCollection;
	
	public CerhCobranca() {
		this.cerhParcelasCobrancasCollection = new ArrayList<CerhParcelasCobranca>();
	}
	
	public Integer getIdeCerhCobranca() {
		return ideCerhCobranca;
	}
	
	public void setIdeCerhCobranca(Integer ideCerhCobranca) {
		this.ideCerhCobranca = ideCerhCobranca;
	}
	
	public Cerh getCerh() {
		return cerh;
	}
	
	public void setCerh(Cerh cerh) {
		this.cerh = cerh;
	}
	
	public Integer getNumAnoCobranca() {
		return numAnoCobranca;
	}
	
	public void setNumAnoCobranca(Integer numAnoCobranca) {
		this.numAnoCobranca = numAnoCobranca;
	}
	
	public boolean isInd_parcelado() {
		return ind_parcelado;
	}
	
	public void setInd_parcelado(boolean ind_parcelado) {
		this.ind_parcelado = ind_parcelado;
	}
	
	public Integer getNumMaxParcelas() {
		return numMaxParcelas;
	}
	
	public void setNumMaxParcelas(Integer numMaxParcelas) {
		this.numMaxParcelas = numMaxParcelas;
	}
	
	public List<CerhParcelasCobranca> getCerhParcelasCobrancasCollection() {
		return cerhParcelasCobrancasCollection;
	}
	
	public void setCerhParcelasCobrancasCollection(List<CerhParcelasCobranca> cerhParcelasCobrancasCollection) {
		this.cerhParcelasCobrancasCollection = cerhParcelasCobrancasCollection;
	}
	
	public boolean isInd_correios() {
		return ind_correios;
	}
	
	public void setInd_correios(boolean ind_correios) {
		this.ind_correios = ind_correios;
	}
}
