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

import br.gov.ba.seia.entity.generic.AbstractEntity;


@Entity
@Table(name="declaracao_queima_controlada_responsavel_tecnico")
public class DeclaracaoQueimaControladaResponsavelTecnico extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dqc_responsavel_tecnico_ide_dqc_responsavel_tecnico_seq")
	@SequenceGenerator(name = "dqc_responsavel_tecnico_ide_dqc_responsavel_tecnico_seq", sequenceName = "dqc_responsavel_tecnico_ide_dqc_responsavel_tecnico_seq", allocationSize = 1)
	@Column(name="ide_declaracao_queima_controlada_responsavel_tecnico")
	private Integer ideDeclaracaoQueimaControladaResponsavelTecnico;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_pessoa_fisica", referencedColumnName = "ide_pessoa_fisica")
	private PessoaFisica idePessoaFisica;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_declaracao_queima_controlada", referencedColumnName = "ide_declaracao_queima_controlada")
	private DeclaracaoQueimaControlada ideDeclaracaoQueimaControlada;

	public DeclaracaoQueimaControladaResponsavelTecnico() {
	}

	public DeclaracaoQueimaControladaResponsavelTecnico(PessoaFisica idePessoaFisica, DeclaracaoQueimaControlada ideDeclaracaoQueimaControlada) {
		this.idePessoaFisica = idePessoaFisica;
		this.ideDeclaracaoQueimaControlada = ideDeclaracaoQueimaControlada;
	}

	/**
	 * 
	 * 
	 * GET'S AND SET'S
	 * 
	 * 
	 */

	public Integer getIdeDeclaracaoQueimaControladaResponsavelTecnico() {
		return ideDeclaracaoQueimaControladaResponsavelTecnico;
	}

	public void setIdeDeclaracaoQueimaControladaResponsavelTecnico(Integer ideDeclaracaoQueimaControladaResponsavelTecnico) {
		this.ideDeclaracaoQueimaControladaResponsavelTecnico = ideDeclaracaoQueimaControladaResponsavelTecnico;
	}

	public PessoaFisica getIdePessoaFisica() {
		return idePessoaFisica;
	}

	public void setIdePessoaFisica(PessoaFisica idePessoaFisica) {
		this.idePessoaFisica = idePessoaFisica;
	}

	public DeclaracaoQueimaControlada getIdeDeclaracaoQueimaControlada() {
		return ideDeclaracaoQueimaControlada;
	}

	public void setIdeDeclaracaoQueimaControlada(DeclaracaoQueimaControlada ideDeclaracaoQueimaControlada) {
		this.ideDeclaracaoQueimaControlada = ideDeclaracaoQueimaControlada;
	}
}