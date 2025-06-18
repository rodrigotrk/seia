package br.gov.ba.seia.entity;

import java.io.Serializable;
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

@Entity
@Table(name="projeto_empresa_executora")
@NamedQuery(name="ProjetoEmpresaExecutora.findAll", query="SELECT p FROM ProjetoEmpresaExecutora p")
public class ProjetoEmpresaExecutora implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PROJETO_EMPRESA_EXECUTORA_IDEPROJETOEMPRESAEXECUTORA_GENERATOR", sequenceName="PROJETO_EMPRESA_EXECUTORA_IDE_PROJETO_EMPRESA_EXECUTORA_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PROJETO_EMPRESA_EXECUTORA_IDEPROJETOEMPRESAEXECUTORA_GENERATOR")
	@Column(name="ide_projeto_empresa_executora", updatable=false, unique=true, nullable=false)
	private Integer ideProjetoEmpresaExecutora;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dt_vigencia_contrato_fim", nullable=false)
	private Date dtVigenciaContratoFim;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dt_vigencia_contrato_inicio", nullable=false)
	private Date dtVigenciaContratoInicio;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dtc_inativa")
	private Date dtcInativa;
	
	@Column(name="ind_inativa", nullable=false)
	private Boolean indInativa;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_pessoa_executora", nullable=false)
	private Pessoa idePessoaExecutora;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_pessoa_fisica_inativadora", nullable=false)
	private PessoaFisica idePessoaFisicaInativadora;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tcca_projeto", nullable=false)
	private TccaProjeto ideTccaProjeto;

	public ProjetoEmpresaExecutora() {
	}

	public ProjetoEmpresaExecutora(TccaProjeto ideTccaProjeto) {
		super();
		this.ideTccaProjeto = ideTccaProjeto;
	}

	public Integer getIdeProjetoEmpresaExecutora() {
		return this.ideProjetoEmpresaExecutora;
	}

	public void setIdeProjetoEmpresaExecutora(Integer ideProjetoEmpresaExecutora) {
		this.ideProjetoEmpresaExecutora = ideProjetoEmpresaExecutora;
	}

	public Date getDtVigenciaContratoFim() {
		return this.dtVigenciaContratoFim;
	}

	public void setDtVigenciaContratoFim(Date dtVigenciaContratoFim) {
		this.dtVigenciaContratoFim = dtVigenciaContratoFim;
	}

	public Date getDtVigenciaContratoInicio() {
		return this.dtVigenciaContratoInicio;
	}

	public void setDtVigenciaContratoInicio(Date dtVigenciaContratoInicio) {
		this.dtVigenciaContratoInicio = dtVigenciaContratoInicio;
	}

	public Date getDtcInativa() {
		return this.dtcInativa;
	}

	public void setDtcInativa(Date dtcInativa) {
		this.dtcInativa = dtcInativa;
	}

	public Pessoa getIdePessoaExecutora() {
		return this.idePessoaExecutora;
	}

	public void setIdePessoaExecutora(Pessoa idePessoaExecutora) {
		this.idePessoaExecutora = idePessoaExecutora;
	}

	public PessoaFisica getIdePessoaFisicaInativadora() {
		return this.idePessoaFisicaInativadora;
	}

	public void setIdePessoaFisicaInativadora(PessoaFisica idePessoaFisicaInativadora) {
		this.idePessoaFisicaInativadora = idePessoaFisicaInativadora;
	}

	public Boolean getIndInativa() {
		return this.indInativa;
	}

	public void setIndInativa(Boolean indInativa) {
		this.indInativa = indInativa;
	}

	public TccaProjeto getIdeTccaProjeto() {
		return this.ideTccaProjeto;
	}

	public void setIdeTccaProjeto(TccaProjeto ideTccaProjeto) {
		this.ideTccaProjeto = ideTccaProjeto;
	}
}