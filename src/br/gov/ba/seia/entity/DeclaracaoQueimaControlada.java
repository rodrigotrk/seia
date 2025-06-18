package br.gov.ba.seia.entity;

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

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name="declaracao_queima_controlada")
public class DeclaracaoQueimaControlada extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dqc_ide_declaracao_queima_controlada_seq")
	@SequenceGenerator(name = "dqc_ide_declaracao_queima_controlada_seq", sequenceName = "dqc_ide_declaracao_queima_controlada_seq", allocationSize = 1)
	@Column(name="ide_declaracao_queima_controlada")
	private Integer ideDeclaracaoQueimaControlada;
	
	@Column(name="ind_aceite_responsabilidade")
	private Boolean indAceiteResponsabilidade;
	
	@Column(name="ind_ciente_termo_compromisso")
	private Boolean indCienteTermoCompromisso;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_ato_declaratorio", referencedColumnName = "ide_ato_declaratorio")
	private AtoDeclaratorio ideAtoDeclaratorio;

	/**
	 * 
	 * COLLECTIONS
	 * 
	 */
	
	@OneToMany(mappedBy="ideDeclaracaoQueimaControlada", fetch = FetchType.LAZY)
	private List<DeclaracaoQueimaControladaElementoIntervencao> declaracaoQueimaControladaElementoIntervencaoCollection;

	@OneToMany(mappedBy="ideDeclaracaoQueimaControlada", fetch = FetchType.LAZY)
	private List<DeclaracaoQueimaControladaImovel> declaracaoQueimaControladaImovelCollection;

	@OneToMany(mappedBy="ideDeclaracaoQueimaControlada", fetch = FetchType.LAZY)
	private List<DeclaracaoQueimaControladaResponsavelTecnico> declaracaoQueimaControladaResponsavelTecnicoCollection;

	@OneToMany(mappedBy="ideDeclaracaoQueimaControlada", fetch = FetchType.LAZY)
	private List<DeclaracaoQueimaControladaTecnicaUtilizada> declaracaoQueimaControladaTecnicaUtilizadaCollection;

	public DeclaracaoQueimaControlada() {
	}
	
	/**
	 * 
	 * 
	 * GET'S AND SET'S
	 * 
	 * 
	 */

	public Integer getIdeDeclaracaoQueimaControlada() {
		return ideDeclaracaoQueimaControlada;
	}

	public void setIdeDeclaracaoQueimaControlada(Integer ideDeclaracaoQueimaControlada) {
		this.ideDeclaracaoQueimaControlada = ideDeclaracaoQueimaControlada;
	}
	
	public Boolean getIndAceiteResponsabilidade() {
		return indAceiteResponsabilidade;
	}

	public void setIndAceiteResponsabilidade(Boolean indAceiteResponsabilidade) {
		this.indAceiteResponsabilidade = indAceiteResponsabilidade;
	}

	public Boolean getIndCienteTermoCompromisso() {
		return indCienteTermoCompromisso;
	}

	public void setIndCienteTermoCompromisso(Boolean indCienteTermoCompromisso) {
		this.indCienteTermoCompromisso = indCienteTermoCompromisso;
	}

	public List<DeclaracaoQueimaControladaElementoIntervencao> getDeclaracaoQueimaControladaElementoIntervencaoCollection() {
		return declaracaoQueimaControladaElementoIntervencaoCollection;
	}

	public void setDeclaracaoQueimaControladaElementoIntervencaoCollection(
			List<DeclaracaoQueimaControladaElementoIntervencao> declaracaoQueimaControladaElementoIntervencaoCollection) {
		this.declaracaoQueimaControladaElementoIntervencaoCollection = declaracaoQueimaControladaElementoIntervencaoCollection;
	}

	public List<DeclaracaoQueimaControladaImovel> getDeclaracaoQueimaControladaImovelCollection() {
		return declaracaoQueimaControladaImovelCollection;
	}

	public void setDeclaracaoQueimaControladaImovelCollection(List<DeclaracaoQueimaControladaImovel> declaracaoQueimaControladaImovelCollection) {
		this.declaracaoQueimaControladaImovelCollection = declaracaoQueimaControladaImovelCollection;
	}

	public List<DeclaracaoQueimaControladaResponsavelTecnico> getDeclaracaoQueimaControladaResponsavelTecnicoCollection() {
		return declaracaoQueimaControladaResponsavelTecnicoCollection;
	}

	public void setDeclaracaoQueimaControladaResponsavelTecnicoCollection(
			List<DeclaracaoQueimaControladaResponsavelTecnico> declaracaoQueimaControladaResponsavelTecnicoCollection) {
		this.declaracaoQueimaControladaResponsavelTecnicoCollection = declaracaoQueimaControladaResponsavelTecnicoCollection;
	}

	public List<DeclaracaoQueimaControladaTecnicaUtilizada> getDeclaracaoQueimaControladaTecnicaUtilizadaCollection() {
		return declaracaoQueimaControladaTecnicaUtilizadaCollection;
	}

	public void setDeclaracaoQueimaControladaTecnicaUtilizadaCollection(
			List<DeclaracaoQueimaControladaTecnicaUtilizada> declaracaoQueimaControladaTecnicaUtilizadaCollection) {
		this.declaracaoQueimaControladaTecnicaUtilizadaCollection = declaracaoQueimaControladaTecnicaUtilizadaCollection;
	}

	public AtoDeclaratorio getIdeAtoDeclaratorio() {
		return ideAtoDeclaratorio;
	}

	public void setIdeAtoDeclaratorio(AtoDeclaratorio ideAtoDeclaratorio) {
		this.ideAtoDeclaratorio = ideAtoDeclaratorio;
	}
}