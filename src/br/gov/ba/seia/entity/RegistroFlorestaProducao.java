package br.gov.ba.seia.entity;

import java.util.Date;
import java.util.List;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;

/**
 * @author Alexandre Queiroz
 * @since 15/12/2016 
 */

@Entity
@Table(name="registro_floresta_producao")

public class RegistroFlorestaProducao extends AbstractEntity{
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="registro_floresta_producao_seq", sequenceName="registro_floresta_producao_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="registro_floresta_producao_seq")
	@Column(name="ide_registro_floresta_producao", unique=true, nullable=false)
	private Integer ideRegistroFlorestaProducao;

	@NotNull
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_ato_declaratorio", nullable=false)
	private AtoDeclaratorio ideAtoDeclaratorio;

	@Temporal(TemporalType.DATE)
	@Column(name="dt_prevista_ultimo_corte")
	private Date dtPrevistaUltimoCorte;

	@Column(name="ind_aceite_responsabilidade")
	private Boolean indAceiteResponsabilidade;

	@Column(name="ind_ciente_termo_compromisso", nullable=false)
	private Boolean indCienteTermoCompromisso;
	
	@OneToMany(mappedBy="ideRegistroFlorestaProducao")
	private List<RegistroFlorestaProducaoImovel> registroFlorestaProducaoImovelList;

	@OneToMany(mappedBy="ideRegistroFlorestaProducao")
	private List<RegistroFlorestaProducaoResponsavelTecnico> registroFlorestaProducaoResponsavelTecnicoList;
	
	@Transient
	private RegistroFlorestaProducaoImovel ideRegistroFlorestaProducaoImovel;
	
	@Transient
	private RegistroFlorestaProducaoResponsavelTecnico ideRegistroFlorestaProducaoResponsavelTecnico;
	
	
	public RegistroFlorestaProducao() {
	}

	public RegistroFlorestaProducao(Requerimento requerimento) {
		this.ideAtoDeclaratorio = new AtoDeclaratorio(requerimento, DocumentoObrigatorioEnum.FORMULARIO_RFP);
		
	}

	public Integer getIdeRegistroFlorestaProducao() {
		return this.ideRegistroFlorestaProducao;
	}

	public void setIdeRegistroFlorestaProducao(Integer ideRegistroFlorestaProducao) {
		this.ideRegistroFlorestaProducao = ideRegistroFlorestaProducao;
	}

	public Date getDtPrevistaUltimoCorte() {
		return this.dtPrevistaUltimoCorte;
	}

	public void setDtPrevistaUltimoCorte(Date dtPrevistaUltimoCorte) {
		this.dtPrevistaUltimoCorte = dtPrevistaUltimoCorte;
	}

	public AtoDeclaratorio getIdeAtoDeclaratorio() {
		return ideAtoDeclaratorio;
	}

	public void setIdeAtoDeclaratorio(AtoDeclaratorio ideAtoDeclaratorio) {
		this.ideAtoDeclaratorio = ideAtoDeclaratorio;
	}

	public Boolean getIndAceiteResponsabilidade() {
		return indAceiteResponsabilidade;
	}

	public void setIndAceiteResponsabilidade(Boolean indAceiteResponsabilidade) {
		this.indAceiteResponsabilidade = indAceiteResponsabilidade;
	}

	public Boolean getIndCienteTermoCompromisso() {
		return this.indCienteTermoCompromisso;
	}

	public void setIndCienteTermoCompromisso(Boolean indCienteTermoCompromisso) {
		this.indCienteTermoCompromisso = indCienteTermoCompromisso;
	}

	public List<RegistroFlorestaProducaoImovel> getRegistroFlorestaProducaoImovelList() {
		return registroFlorestaProducaoImovelList;
	}

	public void setRegistroFlorestaProducaoImovelList(List<RegistroFlorestaProducaoImovel> registroFlorestaProducaoImovelList) {
		this.registroFlorestaProducaoImovelList = registroFlorestaProducaoImovelList;
	}

	public List<RegistroFlorestaProducaoResponsavelTecnico> getRegistroFlorestaProducaoResponsavelTecnicoList() {
		return registroFlorestaProducaoResponsavelTecnicoList;
	}

	public void setRegistroFlorestaProducaoResponsavelTecnicoList(List<RegistroFlorestaProducaoResponsavelTecnico> registroFlorestaProducaoResponsavelTecnicoList) {
		this.registroFlorestaProducaoResponsavelTecnicoList = registroFlorestaProducaoResponsavelTecnicoList;
	}

	public RegistroFlorestaProducaoResponsavelTecnico getIdeRegistroFlorestaProducaoResponsavelTecnico() {
		return ideRegistroFlorestaProducaoResponsavelTecnico;
	}

	public void setIdeRegistroFlorestaProducaoResponsavelTecnico(RegistroFlorestaProducaoResponsavelTecnico ideRegistroFlorestaProducaoResponsavelTecnico) {
		this.ideRegistroFlorestaProducaoResponsavelTecnico = ideRegistroFlorestaProducaoResponsavelTecnico;
	}

	public RegistroFlorestaProducaoImovel getIdeRegistroFlorestaProducaoImovel() {
		return ideRegistroFlorestaProducaoImovel;
	}

	public void setIdeRegistroFlorestaProducaoImovel(RegistroFlorestaProducaoImovel ideRegistroFlorestaProducaoImovel) {
		this.ideRegistroFlorestaProducaoImovel = ideRegistroFlorestaProducaoImovel;
	}

}