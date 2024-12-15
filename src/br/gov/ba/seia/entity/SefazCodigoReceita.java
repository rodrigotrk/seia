package br.gov.ba.seia.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table( name = "sefaz_codigo_receita")
public class SefazCodigoReceita extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sefaz_codigo_receita_seq")
	@SequenceGenerator(name = "sefaz_codigo_receita_seq", sequenceName = "sefaz_codigo_receita_seq", allocationSize = 1)
	@Column(name = "ide_sefaz_codigo_receita")
	private Integer ideSefazCodigoReceita;
	
	@Column(name = "num_codigo_receita")
	private Integer numCodigoCeceita;
	
	@Column(name = "dsc_codigo_receita")
	private String dscCodigoReceita;
	
	
	public SefazCodigoReceita(Integer ideCodigoSefaz) {
		this.ideSefazCodigoReceita = ideCodigoSefaz;
	}
	
	public SefazCodigoReceita() {
	}

	/**
	 * @return the ideCerhCodigoReceita
	 */
	public Integer getIdeSefazCodigoReceita() {
		return ideSefazCodigoReceita;
	}

	/**
	 * @param ideSefazCodigoReceita the ideCerhCodigoReceita to set
	 */
	public void setIdeSefazCodigoReceita(Integer ideSefazCodigoReceita) {
		this.ideSefazCodigoReceita = ideSefazCodigoReceita;
	}

	/**
	 * @return the numCodigoCeceita
	 */
	public Integer getNumCodigoCeceita() {
		return numCodigoCeceita;
	}

	/**
	 * @param numCodigoCeceita the numCodigoCeceita to set
	 */
	public void setNumCodigoCeceita(Integer numCodigoCeceita) {
		this.numCodigoCeceita = numCodigoCeceita;
	}

	/**
	 * @return the dscCodigoReceita
	 */
	public String getDscCodigoReceita() {
		return dscCodigoReceita;
	}

	/**
	 * @param dscCodigoReceita the dscCodigoReceita to set
	 */
	public void setDscCodigoReceita(String dscCodigoReceita) {
		this.dscCodigoReceita = dscCodigoReceita;
	}
}
