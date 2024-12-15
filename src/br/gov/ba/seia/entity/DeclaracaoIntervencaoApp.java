package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.entity.generic.AbstractEntity;


/**
 * @author eduardo.fernandes
 * @since 09/01/2016
 * @see <a href="http://10.105.12.26/redmine/issues/8263">#8263</a>
 */
@Entity
@Table(name="declaracao_intervencao_app")
@NamedQuery(name="DeclaracaoIntervencaoApp.findAll", query="SELECT d FROM DeclaracaoIntervencaoApp d")
public class DeclaracaoIntervencaoApp extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="declaracao_intervencao_app_seq", sequenceName="declaracao_intervencao_app_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="declaracao_intervencao_app_seq")
	@Column(name="ide_declaracao_intervencao_app")
	private Integer ideDeclaracaoIntervencaoApp;

	@Column(name="des_objetivo_intervencao_app")
	private String desObjetivoIntervencaoApp;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ide_localizacao_geografica", referencedColumnName = "ide_localizacao_geografica", nullable = false)
	private LocalizacaoGeografica ideLocalizacaoGeografica;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ide_ato_declaratorio", referencedColumnName = "ide_ato_declaratorio", nullable = false)
	private AtoDeclaratorio ideAtoDeclaratorio;

	@Column(name="ind_aceite_responsabilidade")
	private Boolean indAceiteResponsabilidade;

	@Column(name="ind_ciente_portaria")
	private Boolean indCientePortaria;

	//bi-directional many-to-one association to DeclaracaoIntervencaoAppCaracteristca
	@OneToMany(mappedBy="declaracaoIntervencaoApp")
	private List<DeclaracaoIntervencaoAppCaracteristca> declaracaoIntervencaoAppCaracteristcas;

	public DeclaracaoIntervencaoApp() {
	}

	public DeclaracaoIntervencaoApp(AtoDeclaratorio atoDeclaratorio) {
		this.ideAtoDeclaratorio = atoDeclaratorio;
		this.ideLocalizacaoGeografica = new LocalizacaoGeografica();
		this.declaracaoIntervencaoAppCaracteristcas = new ArrayList<DeclaracaoIntervencaoAppCaracteristca>();
	}

	public Integer getIdeDeclaracaoIntervencaoApp() {
		return this.ideDeclaracaoIntervencaoApp;
	}

	public void setIdeDeclaracaoIntervencaoApp(Integer ideDeclaracaoIntervencaoApp) {
		this.ideDeclaracaoIntervencaoApp = ideDeclaracaoIntervencaoApp;
	}

	public String getDesObjetivoIntervencaoApp() {
		return this.desObjetivoIntervencaoApp;
	}

	public void setDesObjetivoIntervencaoApp(String desObjetivoIntervencaoApp) {
		this.desObjetivoIntervencaoApp = desObjetivoIntervencaoApp;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return this.ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public Boolean getIndAceiteResponsabilidade() {
		return this.indAceiteResponsabilidade;
	}

	public void setIndAceiteResponsabilidade(Boolean indAceiteResponsabilidade) {
		this.indAceiteResponsabilidade = indAceiteResponsabilidade;
	}

	public Boolean getIndCientePortaria() {
		return this.indCientePortaria;
	}

	public void setIndCientePortaria(Boolean indCientePortaria) {
		this.indCientePortaria = indCientePortaria;
	}

	public List<DeclaracaoIntervencaoAppCaracteristca> getDeclaracaoIntervencaoAppCaracteristcas() {
		return this.declaracaoIntervencaoAppCaracteristcas;
	}

	public void setDeclaracaoIntervencaoAppCaracteristcas(List<DeclaracaoIntervencaoAppCaracteristca> declaracaoIntervencaoAppCaracteristcas) {
		this.declaracaoIntervencaoAppCaracteristcas = declaracaoIntervencaoAppCaracteristcas;
	}

	public AtoDeclaratorio getIdeAtoDeclaratorio() {
		return ideAtoDeclaratorio;
	}

	public void setIdeAtoDeclaratorio(AtoDeclaratorio ideAtoDeclaratorio) {
		this.ideAtoDeclaratorio = ideAtoDeclaratorio;
	}
}