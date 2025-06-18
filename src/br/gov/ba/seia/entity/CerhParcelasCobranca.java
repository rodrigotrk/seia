package br.gov.ba.seia.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name = "cerh_parcelas_cobranca")
public class CerhParcelasCobranca extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cerh_parcelas_cobranca_seq")
	@SequenceGenerator(name = "cerh_parcelas_cobranca_seq", sequenceName = "cerh_parcelas_cobranca_seq", allocationSize = 1)
	@Column(name = "ide_cerh_parcelas_cobranca")
	private Integer ideCerhParcelasCobranca;
	
	@Column(name = "num_parcela")
	private Integer numParcela;
	
	@Column(name = "vlr_original_parcela")
	private Double vlrOriginalParcela;
	
	@OneToMany(mappedBy = "ideDae",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Dae> cerhDaesCollection;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ide_cerh_cobranca")
	private CerhCobranca ideCerhCobranca;

	public CerhParcelasCobranca() {
		this.cerhDaesCollection = new ArrayList<Dae>();
	}
	
	/**
	 * @return the ideCerhParcelasCobranca
	 */
	public Integer getIdeCerhParcelasCobranca() {
		return ideCerhParcelasCobranca;
	}

	/**
	 * @param ideCerhParcelasCobranca the ideCerhParcelasCobranca to set
	 */
	public void setIdeCerhParcelasCobranca(Integer ideCerhParcelasCobranca) {
		this.ideCerhParcelasCobranca = ideCerhParcelasCobranca;
	}

	/**
	 * @return the numParcela
	 */
	public Integer getNumParcela() {
		return numParcela;
	}

	/**
	 * @param numParcela the numParcela to set
	 */
	public void setNumParcela(Integer numParcela) {
		this.numParcela = numParcela;
	}

	/**
	 * @return the vlrOriginalParcela
	 */
	public Double getVlrOriginalParcela() {
		return vlrOriginalParcela;
	}

	/**
	 * @param vlrOriginalParcela the vlrOriginalParcela to set
	 */
	public void setVlrOriginalParcela(Double vlrOriginalParcela) {
		this.vlrOriginalParcela = vlrOriginalParcela;
	}

	public CerhCobranca getIdeCerhCobranca() {
		return ideCerhCobranca;
	}

	public void setIdeCerhCobranca(CerhCobranca ideCerhCobranca) {
		this.ideCerhCobranca = ideCerhCobranca;
	}

	/**
	 * @return the cerhDaesCollection
	 */
	public List<Dae> getCerhDaesCollection() {
		return cerhDaesCollection;
	}

	/**
	 * @param cerhDaesCollection the cerhDaesCollection to set
	 */
	public void setCerhDaesCollection(List<Dae> cerhDaesCollection) {
		this.cerhDaesCollection = cerhDaesCollection;
	}
}
