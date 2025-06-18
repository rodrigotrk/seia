package br.gov.ba.seia.entity;

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name="tcca_status")
@NamedQuery(name="TccaStatus.findAll", query="SELECT t FROM TccaStatus t")
public class TccaStatus extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TCCA_STATUS_IDETCCASTATUS_GENERATOR", sequenceName="TCCA_STATUS_IDE_TCCA_STATUS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TCCA_STATUS_IDETCCASTATUS_GENERATOR")
	@Column(name="ide_tcca_status", updatable=false, unique=true, nullable=false)
	private Integer ideTccaStatus;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dtc_tcca_status", nullable=false)
	private Date dtcTccaStatus;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_pessoa_fisica", nullable=false)
	private PessoaFisica idePessoaFisica;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tcca", nullable=false)
	private Tcca ideTcca;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tcca_projeto_tipo_status", nullable=false)
	private TccaProjetoTipoStatus ideTccaProjetoTipoStatus;

	public TccaStatus() {
	}

	public Integer getIdeTccaStatus() {
		return this.ideTccaStatus;
	}

	public void setIdeTccaStatus(Integer ideTccaStatus) {
		this.ideTccaStatus = ideTccaStatus;
	}

	public Date getDtcTccaStatus() {
		return this.dtcTccaStatus;
	}

	public void setDtcTccaStatus(Date dtcTccaStatus) {
		this.dtcTccaStatus = dtcTccaStatus;
	}

	public PessoaFisica getIdePessoaFisica() {
		return this.idePessoaFisica;
	}

	public void setIdePessoaFisica(PessoaFisica idePessoaFisica) {
		this.idePessoaFisica = idePessoaFisica;
	}

	public Tcca getIdeTcca() {
		return this.ideTcca;
	}

	public void setIdeTcca(Tcca ideTcca) {
		this.ideTcca = ideTcca;
	}

	public TccaProjetoTipoStatus getIdeTccaProjetoTipoStatus() {
		return this.ideTccaProjetoTipoStatus;
	}

	public void setIdeTccaProjetoTipoStatus(TccaProjetoTipoStatus ideTccaProjetoTipoStatus) {
		this.ideTccaProjetoTipoStatus = ideTccaProjetoTipoStatus;
	}
}