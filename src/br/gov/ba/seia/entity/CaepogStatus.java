package br.gov.ba.seia.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.ba.seia.entity.generic.AbstractEntity;

/**
 * Tabela que armazena o status de cada cadastro CAEPOG
 */
@Entity
@Table(name = "caepog_status")
public class CaepogStatus extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * Chave primária da tabela CAEPOG_STATUS
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "caepog_status_ide_caepog_status_seq")
	@SequenceGenerator(name = "caepog_status_ide_caepog_status_seq", sequenceName = "caepog_status_ide_caepog_status_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "ide_caepog_status", nullable = false)
	private Integer ideCaepogStatus;
	
	/**
	 * Data registrado do status para o cadastro
	 */
	@Column(name = "dtc_caepog_status")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcCaepogStatus;

	/**
	 * Chave estrangeira da pessoa física responsável pelo registro do status
	 */
	@JoinColumn(name = "ide_pessoa_fisica", referencedColumnName = "ide_pessoa_fisica")
	@ManyToOne(fetch = FetchType.LAZY)
	private PessoaFisica idePessoaFisica;
	
	/**
	 * Chave primária da tabela CAEPOG_TIPO_STATUS
	 */
	@JoinColumn(name = "ide_caepog_tipo_status", referencedColumnName = "ide_caepog_tipo_status")
	@ManyToOne(fetch = FetchType.LAZY)
	private CaepogTipoStatus ideCaepogTipoStatus;
	
	/**
	 * Chave primária da tabela CAEPOG
	 */
	@JoinColumn(name = "ide_caepog", referencedColumnName = "ide_caepog")
	@ManyToOne(fetch = FetchType.LAZY)
	private Caepog ideCaepog;
	
	public CaepogStatus() {
		super();
	}
	
	public CaepogStatus( Integer ideCaepogStatus){
		this.ideCaepogStatus = ideCaepogStatus;
	}
	
	/**
	 * 
	 * 
	 * GET'S AND SET'S
	 * 
	 * 
	 */

	public Integer getIdeCaepogStatus() {
		return ideCaepogStatus;
	}

	public void setIdeCaepogStatus(Integer ideCaepogStatus) {
		this.ideCaepogStatus = ideCaepogStatus;
	}

	public Date getDtcCaepogStatus() {
		return dtcCaepogStatus;
	}

	public void setDtcCaepogStatus(Date dtcCaepogStatus) {
		this.dtcCaepogStatus = dtcCaepogStatus;
	}

	public PessoaFisica getIdePessoaFisica() {
		return idePessoaFisica;
	}

	public void setIdePessoaFisica(PessoaFisica idePessoaFisica) {
		this.idePessoaFisica = idePessoaFisica;
	}

	public CaepogTipoStatus getIdeCaepogTipoStatus() {
		return ideCaepogTipoStatus;
	}

	public void setIdeCaepogTipoStatus(CaepogTipoStatus ideCaepogTipoStatus) {
		this.ideCaepogTipoStatus = ideCaepogTipoStatus;
	}

	public Caepog getIdeCaepog() {
		return ideCaepog;
	}

	public void setIdeCaepog(Caepog ideCaepog) {
		this.ideCaepog = ideCaepog;
	}
}