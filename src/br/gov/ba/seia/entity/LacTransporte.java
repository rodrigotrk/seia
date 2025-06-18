package br.gov.ba.seia.entity;

import java.io.Serializable;

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
import javax.validation.constraints.NotNull;

import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;


/**
 * The persistent class for the lac_transporte database table.
 * 
 */
@Entity
@Table(name="lac_transporte")
public class LacTransporte implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lac_transporte_ide_lac_transporte_generator")
	@SequenceGenerator(name = "lac_transporte_ide_lac_transporte_generator", sequenceName = "lac_transporte_ide_lac_transporte_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_lac_transporte",nullable = false)
	private Integer ideLacTransporte;

	@JoinColumn(name = "ide_lac", referencedColumnName = "ide_lac", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Lac ideLac;

	@JoinColumn(name = "ide_localizacao_geografica", referencedColumnName = "ide_localizacao_geografica", nullable = true)
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private LocalizacaoGeografica ideLocalizacaoGeografica;

	@Column(name="ind_atendimento_emergencia_empresa", nullable = false)
	private boolean indAtendimentoEmergenciaEmpresa;

	@Column(name="ind_atendimento_emergencia_terceiro", nullable = false)
	private boolean indAtendimentoEmergenciaTerceiro;

	@Column(name="ind_base_operacional", nullable = false)
	private Boolean indBaseOperacional;

	@Column(name="ind_produto", nullable = false)
	private boolean indProduto;

	@Column(name="ind_residuo", nullable = false)
	private boolean indResiduo;

	@JoinColumn(name = "ide_pessoa_juridica", referencedColumnName = "ide_pessoa_juridica", nullable = true)
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private PessoaJuridica idePessoaJuridica;

	public LacTransporte(){
		
	}
	
	public LacTransporte(Requerimento requerimento) {
		this.ideLocalizacaoGeografica = new LocalizacaoGeografica();
		this.ideLac = new Lac(requerimento, DocumentoObrigatorioEnum.TRANSPORTADORA);
	}
	
	public LacTransporte(Requerimento requerimento, Lac ideLac) {
		this.ideLocalizacaoGeografica = new LocalizacaoGeografica();
		this.ideLac = ideLac;
	}

	public Integer getIdeLacTransporte() {
		return this.ideLacTransporte;
	}

	public void setIdeLacTransporte(Integer ideLacTransporte) {
		this.ideLacTransporte = ideLacTransporte;
	}

	public boolean getIndAtendimentoEmergenciaEmpresa() {
		return this.indAtendimentoEmergenciaEmpresa;
	}

	public void setIndAtendimentoEmergenciaEmpresa(boolean indAtendimentoEmergenciaEmpresa) {
		this.indAtendimentoEmergenciaEmpresa = indAtendimentoEmergenciaEmpresa;
	}

	public boolean getIndAtendimentoEmergenciaTerceiro() {
		return this.indAtendimentoEmergenciaTerceiro;
	}

	public void setIndAtendimentoEmergenciaTerceiro(boolean indAtendimentoEmergenciaTerceiro) {
		this.indAtendimentoEmergenciaTerceiro = indAtendimentoEmergenciaTerceiro;
	}

	public Boolean getIndBaseOperacional() {
		return this.indBaseOperacional;
	}

	public void setIndBaseOperacional(Boolean indBaseOperacional) {
		this.indBaseOperacional = indBaseOperacional;
	}

	public boolean getIndProduto() {
		return this.indProduto;
	}

	public void setIndProduto(boolean indProduto) {
		this.indProduto = indProduto;
	}

	public boolean getIndResiduo() {
		return this.indResiduo;
	}

	public void setIndResiduo(boolean indResiduo) {
		this.indResiduo = indResiduo;
	}

	public Lac getIdeLac() {
		return ideLac;
	}

	public void setIdeLac(Lac ideLac) {
		this.ideLac = ideLac;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public PessoaJuridica getIdePessoaJuridica() {
		return idePessoaJuridica;
	}

	public void setIdePessoaJuridica(PessoaJuridica idePessoaJuridica) {
		this.idePessoaJuridica = idePessoaJuridica;
	}

}