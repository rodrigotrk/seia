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
@Table(name="projeto_status")
@NamedQuery(name="ProjetoStatus.findAll", query="SELECT p FROM ProjetoStatus p")
public class ProjetoStatus extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PROJETO_STATUS_IDEPROJETOSTATUS_GENERATOR", sequenceName="PROJETO_STATUS_IDE_PROJETO_STATUS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PROJETO_STATUS_IDEPROJETOSTATUS_GENERATOR")
	@Column(name="ide_projeto_status", updatable=false, unique=true, nullable=false)
	private Integer ideProjetoStatus;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dtc_projeto_status", nullable=false)
	private Date dtcProjetoStatus;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_pessoa_fisica_projeto_status", nullable=false)
	private PessoaFisica idePessoaFisicaProjetoStatus;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tcca_projeto", nullable=false)
	private TccaProjeto ideTccaProjeto;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tcca_projeto_tipo_status", nullable=false)
	private TccaProjetoTipoStatus ideTccaProjetoTipoStatus;

	public ProjetoStatus() {
	}

	public Integer getIdeProjetoStatus() {
		return this.ideProjetoStatus;
	}

	public void setIdeProjetoStatus(Integer ideProjetoStatus) {
		this.ideProjetoStatus = ideProjetoStatus;
	}

	public Date getDtcProjetoStatus() {
		return this.dtcProjetoStatus;
	}

	public void setDtcProjetoStatus(Date dtcProjetoStatus) {
		this.dtcProjetoStatus = dtcProjetoStatus;
	}

	public PessoaFisica getIdePessoaFisicaProjetoStatus() {
		return this.idePessoaFisicaProjetoStatus;
	}

	public void setIdePessoaFisicaProjetoStatus(PessoaFisica idePessoaFisicaProjetoStatus) {
		this.idePessoaFisicaProjetoStatus = idePessoaFisicaProjetoStatus;
	}

	public TccaProjeto getIdeTccaProjeto() {
		return this.ideTccaProjeto;
	}

	public void setIdeTccaProjeto(TccaProjeto ideTccaProjeto) {
		this.ideTccaProjeto = ideTccaProjeto;
	}

	public TccaProjetoTipoStatus getIdeTccaProjetoTipoStatus() {
		return this.ideTccaProjetoTipoStatus;
	}

	public void setIdeTccaProjetoTipoStatus(TccaProjetoTipoStatus ideTccaProjetoTipoStatus) {
		this.ideTccaProjetoTipoStatus = ideTccaProjetoTipoStatus;
	}
}