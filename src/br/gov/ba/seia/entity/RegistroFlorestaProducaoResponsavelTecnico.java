package br.gov.ba.seia.entity;

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

import br.gov.ba.seia.entity.generic.AbstractEntity;

/**
 * @author Alexandre Queiroz
 * @since 15/12/2016 
 */
@Entity
@Table(name="registro_floresta_producao_responsavel_tecnico")
public class RegistroFlorestaProducaoResponsavelTecnico extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="registro_floresta_producao_responsavel_tecnico_seq", sequenceName="registro_floresta_producao_responsavel_tecnico_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="registro_floresta_producao_responsavel_tecnico_seq")
	@Column(name="ide_registro_floresta_producao_responsavel_tecnico", unique=true, nullable=false)
	private Integer ideRegistroFlorestaProducaoResponsavelTecnico;

	@NotNull
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_pessoa_fisica", nullable = false)
	private PessoaFisica idePessoaFisica;

	@NotNull
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_registro_floresta_producao",nullable = false)
	private RegistroFlorestaProducao ideRegistroFlorestaProducao;

	
	public RegistroFlorestaProducaoResponsavelTecnico() {
	}

	public Integer getIdeRegistroFlorestaProducaoResponsavelTecnico() {
		return this.ideRegistroFlorestaProducaoResponsavelTecnico;
	}

	public void setIdeRegistroFlorestaProducaoResponsavelTecnico(Integer ideRegistroFlorestaProducaoResponsavelTecnico) {
		this.ideRegistroFlorestaProducaoResponsavelTecnico = ideRegistroFlorestaProducaoResponsavelTecnico;
	}

	public PessoaFisica getIdePessoaFisica() {
		return idePessoaFisica;
	}

	public void setIdePessoaFisica(PessoaFisica idePessoaFisica) {
		this.idePessoaFisica = idePessoaFisica;
	}

	public RegistroFlorestaProducao getIdeRegistroFlorestaProducao() {
		return ideRegistroFlorestaProducao;
	}

	public void setIdeRegistroFlorestaProducao(RegistroFlorestaProducao ideRegistroFlorestaProducao) {
		this.ideRegistroFlorestaProducao = ideRegistroFlorestaProducao;
	}
}