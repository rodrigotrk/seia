package br.gov.ba.seia.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.enumerator.AquiculturaTipoAtividadeEnum;


/**
 * Tabela criada para diferenciar o FCE Aquicultura por abas
 * PSICULTURA = 1, CARCINICULTURA = 2, RANICULTURA = 3, ALGICUTURA = 4, MALOCOCULTURA = 5;
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 05/11/2014
 */
@Entity
@Table(name="aquicultura_tipo_atividade")
@NamedQuery(name="AquiculturaTipoAtividade.findAll", query="SELECT a FROM AquiculturaTipoAtividade a")
public class AquiculturaTipoAtividade extends AbstractEntity implements Comparable<AquiculturaTipoAtividade> {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ide_aquicultura_tipo_atividade")
	private Integer ideAquiculturaTipoAtividade;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	@Column(name="nom_aquicultura_tipo_atividade")
	private String nomAquiculturaTipoAtividade;

	@Transient
	private List<Especie> listaEspecies;
	@Transient
	private List<EspecieAquiculturaTipoAtividade> listaEspecieAquiculturaTipoAtividades;
	@Transient
	private List<EspecieAquiculturaTipoAtividade> listaEspecieAquiculturaTipoAtividadesSelected;
	@Transient
	private boolean selecionado;

	@OneToMany(fetch=FetchType.LAZY, mappedBy="ideAquiculturaTipoAtividade")
	private Collection<EspecieAquiculturaTipoAtividade> especieAquiculturaTipoAtividadeCollection;
	
	public AquiculturaTipoAtividade() {

	}

	public AquiculturaTipoAtividade(AquiculturaTipoAtividadeEnum aquiculturaTipoAtividadeEnum) {
		this.ideAquiculturaTipoAtividade = aquiculturaTipoAtividadeEnum.getId();
	}

	public Integer getIdeAquiculturaTipoAtividade() {
		return this.ideAquiculturaTipoAtividade;
	}

	public void setIdeAquiculturaTipoAtividade(Integer ideAquiculturaTipoAtividade) {
		this.ideAquiculturaTipoAtividade = ideAquiculturaTipoAtividade;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public String getNomAquiculturaTipoAtividade() {
		return this.nomAquiculturaTipoAtividade;
	}

	public void setNomAquiculturaTipoAtividade(String nomAquiculturaTipoAtividade) {
		this.nomAquiculturaTipoAtividade = nomAquiculturaTipoAtividade;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(AquiculturaTipoAtividade o) {
		return getIdeAquiculturaTipoAtividade().compareTo(o.getIdeAquiculturaTipoAtividade());
	}

	public List<Especie> getListaEspecies() {
		return listaEspecies;
	}

	public void setListaEspecies(List<Especie> listaEspecies) {
		this.listaEspecies = listaEspecies;
	}

	public List<EspecieAquiculturaTipoAtividade> getListaEspecieAquiculturaTipoAtividades() {
		return listaEspecieAquiculturaTipoAtividades;
	}

	public void setListaEspecieAquiculturaTipoAtividades(List<EspecieAquiculturaTipoAtividade> listaEspecieAquiculturaTipoAtividades) {
		this.listaEspecieAquiculturaTipoAtividades = listaEspecieAquiculturaTipoAtividades;
	}

	public List<EspecieAquiculturaTipoAtividade> getListaEspecieAquiculturaTipoAtividadesSelected() {
		return listaEspecieAquiculturaTipoAtividadesSelected;
	}

	public void setListaEspecieAquiculturaTipoAtividadesSelected(List<EspecieAquiculturaTipoAtividade> listaEspecieAquiculturaTipoAtividadesSelected) {
		this.listaEspecieAquiculturaTipoAtividadesSelected = listaEspecieAquiculturaTipoAtividadesSelected;
	}
	
	public boolean isSelecionado() {
		return selecionado;
	}

	public void setSelecionado(boolean selecionado) {
		if(!selecionado){
			this.listaEspecieAquiculturaTipoAtividadesSelected = new ArrayList<EspecieAquiculturaTipoAtividade>();
		}
		this.selecionado = selecionado;
	}

	public Collection<EspecieAquiculturaTipoAtividade> getEspecieAquiculturaTipoAtividadeCollection() {
		return especieAquiculturaTipoAtividadeCollection;
	}

	public void setEspecieAquiculturaTipoAtividadeCollection(
			Collection<EspecieAquiculturaTipoAtividade> especieAquiculturaTipoAtividadeCollection) {
		this.especieAquiculturaTipoAtividadeCollection = especieAquiculturaTipoAtividadeCollection;
	}
}