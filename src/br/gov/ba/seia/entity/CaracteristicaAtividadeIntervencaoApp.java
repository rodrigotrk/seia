package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.util.Util;


/**
 * @author eduardo.fernandes
 * @since 09/01/2016
 * @see <a href="http://10.105.12.26/redmine/issues/8263">#8263</a>
 */
@Entity
@Table(name="caracteristica_atividade_intervencao_app")
@NamedQuery(name="CaracteristicaAtividadeIntervencaoApp.findAll", query="SELECT c FROM CaracteristicaAtividadeIntervencaoApp c")
public class CaracteristicaAtividadeIntervencaoApp implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="caracteristica_atividade_intervencao_app_seq", sequenceName="caracteristica_atividade_intervencao_app_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="caracteristica_atividade_intervencao_app_seq")
	@Column(name="ide_caracteristica_atividade_intervencao_app")
	private Integer ideCaracteristicaAtividadeIntervencaoApp;

	@Column(name="dtc_criacao")
	private Date dtcCriacao;

	@Column(name="dtc_exclusao")
	private Date dtcExclusao;

	@Column(name="ind_excluido")
	private Boolean indExcluido;

	//bi-directional many-to-one association to AtividadeIntervencaoApp
	@ManyToOne
	@JoinColumn(name="ide_atividade_intervencao_app")
	private AtividadeIntervencaoApp atividadeIntervencaoApp;

	//bi-directional many-to-one association to CaracteristicaIntervencaoApp
	@ManyToOne
	@JoinColumn(name="ide_caracteristica_intervencao_app")
	private CaracteristicaIntervencaoApp caracteristicaIntervencaoApp;

	//bi-directional many-to-one association to DeclaracaoIntervencaoAppCaracteristca
	@OneToMany(mappedBy="caracteristicaAtividadeIntervencaoApp")
	private List<DeclaracaoIntervencaoAppCaracteristca> declaracaoIntervencaoAppCaracteristcas;

	public CaracteristicaAtividadeIntervencaoApp() {
	}

	/**
	 * @param caracteristica
	 * @param atividade
	 */
	public CaracteristicaAtividadeIntervencaoApp(CaracteristicaIntervencaoApp caracteristica, AtividadeIntervencaoApp atividade) {
		this.atividadeIntervencaoApp = atividade;
		this.caracteristicaIntervencaoApp = caracteristica;
	}

	public Integer getIdeCaracteristicaAtividadeIntervencaoApp() {
		return this.ideCaracteristicaAtividadeIntervencaoApp;
	}

	public void setIdeCaracteristicaAtividadeIntervencaoApp(Integer ideCaracteristicaAtividadeIntervencaoApp) {
		this.ideCaracteristicaAtividadeIntervencaoApp = ideCaracteristicaAtividadeIntervencaoApp;
	}

	public Date getDtcCriacao() {
		return this.dtcCriacao;
	}

	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}

	public Date getDtcExclusao() {
		return this.dtcExclusao;
	}

	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}

	public Boolean getIndExcluido() {
		return this.indExcluido;
	}

	public void setIndExcluido(Boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	public AtividadeIntervencaoApp getAtividadeIntervencaoApp() {
		return this.atividadeIntervencaoApp;
	}

	public void setAtividadeIntervencaoApp(AtividadeIntervencaoApp atividadeIntervencaoApp) {
		this.atividadeIntervencaoApp = atividadeIntervencaoApp;
	}

	public CaracteristicaIntervencaoApp getCaracteristicaIntervencaoApp() {
		return this.caracteristicaIntervencaoApp;
	}

	public void setCaracteristicaIntervencaoApp(CaracteristicaIntervencaoApp caracteristicaIntervencaoApp) {
		this.caracteristicaIntervencaoApp = caracteristicaIntervencaoApp;
	}

	public List<DeclaracaoIntervencaoAppCaracteristca> getDeclaracaoIntervencaoAppCaracteristcas() {
		return this.declaracaoIntervencaoAppCaracteristcas;
	}

	public void setDeclaracaoIntervencaoAppCaracteristcas(List<DeclaracaoIntervencaoAppCaracteristca> declaracaoIntervencaoAppCaracteristcas) {
		this.declaracaoIntervencaoAppCaracteristcas = declaracaoIntervencaoAppCaracteristcas;
	}

	public DeclaracaoIntervencaoAppCaracteristca addDeclaracaoIntervencaoAppCaracteristca(DeclaracaoIntervencaoAppCaracteristca declaracaoIntervencaoAppCaracteristca) {
		getDeclaracaoIntervencaoAppCaracteristcas().add(declaracaoIntervencaoAppCaracteristca);
		declaracaoIntervencaoAppCaracteristca.setCaracteristicaAtividadeIntervencaoApp(this);

		return declaracaoIntervencaoAppCaracteristca;
	}

	public DeclaracaoIntervencaoAppCaracteristca removeDeclaracaoIntervencaoAppCaracteristca(DeclaracaoIntervencaoAppCaracteristca declaracaoIntervencaoAppCaracteristca) {
		getDeclaracaoIntervencaoAppCaracteristcas().remove(declaracaoIntervencaoAppCaracteristca);
		declaracaoIntervencaoAppCaracteristca.setCaracteristicaAtividadeIntervencaoApp(null);

		return declaracaoIntervencaoAppCaracteristca;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideCaracteristicaAtividadeIntervencaoApp == null) ? 0
						: ideCaracteristicaAtividadeIntervencaoApp.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CaracteristicaAtividadeIntervencaoApp other = (CaracteristicaAtividadeIntervencaoApp) obj;
		if (ideCaracteristicaAtividadeIntervencaoApp == null) {
			if (other.ideCaracteristicaAtividadeIntervencaoApp != null)
				return false;
		} else if (!ideCaracteristicaAtividadeIntervencaoApp
				.equals(other.ideCaracteristicaAtividadeIntervencaoApp))
			return false;
		return true;
	}

	public boolean isOutrasAtividades(){
		return !Util.isNull(this.atividadeIntervencaoApp) && this.atividadeIntervencaoApp.isOutrasAtividades();
	}
}