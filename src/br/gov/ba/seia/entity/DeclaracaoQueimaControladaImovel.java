package br.gov.ba.seia.entity;

import java.util.Arrays;
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

import br.gov.ba.seia.entity.generic.AbstractEntityLocalizacaoGeografica;

@Entity
@Table(name="declaracao_queima_controlada_imovel")
public class DeclaracaoQueimaControladaImovel extends AbstractEntityLocalizacaoGeografica {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dqc_imovel_ide_declaracao_queima_controlada_imovel_seq")
	@SequenceGenerator(name = "dqc_imovel_ide_declaracao_queima_controlada_imovel_seq", sequenceName = "dqc_imovel_ide_declaracao_queima_controlada_imovel_seq", allocationSize = 1)
	@Column(name="ide_declaracao_queima_controlada_imovel")
	private Integer ideDeclaracaoQueimaControladaImovel;

	@Column(name="ind_arrendado")
	private Boolean indArrendado;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_imovel", referencedColumnName = "ide_imovel")
	private Imovel ideImovel;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_localizacao_geografica", referencedColumnName = "ide_localizacao_geografica")
	private LocalizacaoGeografica ideLocalizacaoGeografica;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_declaracao_queima_controlada", referencedColumnName = "ide_declaracao_queima_controlada")
	private DeclaracaoQueimaControlada ideDeclaracaoQueimaControlada;

	@OneToMany(mappedBy="ideDeclaracaoQueimaControladaImovel", fetch = FetchType.LAZY)
	private List<DeclaracaoQueimaControladaImovelObjetivoQueimaControlada> declaracaoQueimaControladaImovelObjetivoQueimaControladaCollection;

	public DeclaracaoQueimaControladaImovel() {
	}
	
	public DeclaracaoQueimaControladaImovel(DeclaracaoQueimaControlada ideDeclaracaoQueimaControlada) {
		this.ideDeclaracaoQueimaControlada = ideDeclaracaoQueimaControlada;
	}

	
	/**
	 * 
	 * 
	 * GET'S AND SET'S
	 * 
	 * 
	 */
	
	public Integer getIdeDeclaracaoQueimaControladaImovel() {
		return ideDeclaracaoQueimaControladaImovel;
	}

	public void setIdeDeclaracaoQueimaControladaImovel(Integer ideDeclaracaoQueimaControladaImovel) {
		this.ideDeclaracaoQueimaControladaImovel = ideDeclaracaoQueimaControladaImovel;
	}

	public Boolean getIndArrendado() {
		return indArrendado;
	}

	public void setIndArrendado(Boolean indArrendado) {
		this.indArrendado = indArrendado;
	}

	public Imovel getIdeImovel() {
		return ideImovel;
	}

	public void setIdeImovel(Imovel ideImovel) {
		this.ideImovel = ideImovel;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public DeclaracaoQueimaControlada getIdeDeclaracaoQueimaControlada() {
		return ideDeclaracaoQueimaControlada;
	}

	public void setIdeDeclaracaoQueimaControlada(DeclaracaoQueimaControlada ideDeclaracaoQueimaControlada) {
		this.ideDeclaracaoQueimaControlada = ideDeclaracaoQueimaControlada;
	}

	public List<DeclaracaoQueimaControladaImovelObjetivoQueimaControlada> getDeclaracaoQueimaControladaImovelObjetivoQueimaControladaCollection() {
		return declaracaoQueimaControladaImovelObjetivoQueimaControladaCollection;
	}

	public void setDeclaracaoQueimaControladaImovelObjetivoQueimaControladaCollection(List<DeclaracaoQueimaControladaImovelObjetivoQueimaControlada> declaracaoQueimaControladaImovelObjetivoQueimaControladaCollection) {
		this.declaracaoQueimaControladaImovelObjetivoQueimaControladaCollection = declaracaoQueimaControladaImovelObjetivoQueimaControladaCollection;
	}

	@Override
	public List<LocalizacaoGeografica> getLocalizacoesGeograficas() {
		return Arrays.asList(ideLocalizacaoGeografica);
	}
}