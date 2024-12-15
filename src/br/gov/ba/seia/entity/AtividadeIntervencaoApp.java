package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * @author eduardo.fernandes
 * @since 09/01/2016
 * @see <a href="http://10.105.12.26/redmine/issues/8263">#8263</a>
 */
@Entity
@Table(name="atividade_intervencao_app")
@NamedQuery(name="AtividadeIntervencaoApp.findAll", query="SELECT a FROM AtividadeIntervencaoApp a")
public class AtividadeIntervencaoApp implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final int OUTRAS_ATIVIDADES = 5;

	@Id
	@SequenceGenerator(name="atividade_intervencao_app_seq", sequenceName="atividade_intervencao_app_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="atividade_intervencao_app_seq")
	@Column(name="ide_atividade_intervencao_app")
	private Integer ideAtividadeIntervencaoApp;

	@Column(name="des_atividade_intervencao_app")
	private String desAtividadeIntervencaoApp;

	@Column(name="dtc_criacao")
	private Date dtcCriacao;

	@Column(name="dtc_exclusao")
	private Date dtcExclusao;

	@Column(name="ind_excluido")
	private Boolean indExcluido;

	//bi-directional many-to-one association to CaracteristicaAtividadeIntervencaoApp
	@OneToMany(mappedBy="atividadeIntervencaoApp")
	private List<CaracteristicaAtividadeIntervencaoApp> caracteristicaAtividadeIntervencaoApps;

	@Transient
	private boolean selecionado;
	
	public AtividadeIntervencaoApp() {
	}

	public Integer getIdeAtividadeIntervencaoApp() {
		return this.ideAtividadeIntervencaoApp;
	}

	public void setIdeAtividadeIntervencaoApp(Integer ideAtividadeIntervencaoApp) {
		this.ideAtividadeIntervencaoApp = ideAtividadeIntervencaoApp;
	}

	public String getDesAtividadeIntervencaoApp() {
		return this.desAtividadeIntervencaoApp;
	}

	public void setDesAtividadeIntervencaoApp(String desAtividadeIntervencaoApp) {
		this.desAtividadeIntervencaoApp = desAtividadeIntervencaoApp;
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

	public List<CaracteristicaAtividadeIntervencaoApp> getCaracteristicaAtividadeIntervencaoApps() {
		return this.caracteristicaAtividadeIntervencaoApps;
	}

	public void setCaracteristicaAtividadeIntervencaoApps(List<CaracteristicaAtividadeIntervencaoApp> caracteristicaAtividadeIntervencaoApps) {
		this.caracteristicaAtividadeIntervencaoApps = caracteristicaAtividadeIntervencaoApps;
	}

	public CaracteristicaAtividadeIntervencaoApp addCaracteristicaAtividadeIntervencaoApp(CaracteristicaAtividadeIntervencaoApp caracteristicaAtividadeIntervencaoApp) {
		getCaracteristicaAtividadeIntervencaoApps().add(caracteristicaAtividadeIntervencaoApp);
		caracteristicaAtividadeIntervencaoApp.setAtividadeIntervencaoApp(this);

		return caracteristicaAtividadeIntervencaoApp;
	}

	public CaracteristicaAtividadeIntervencaoApp removeCaracteristicaAtividadeIntervencaoApp(CaracteristicaAtividadeIntervencaoApp caracteristicaAtividadeIntervencaoApp) {
		getCaracteristicaAtividadeIntervencaoApps().remove(caracteristicaAtividadeIntervencaoApp);
		caracteristicaAtividadeIntervencaoApp.setAtividadeIntervencaoApp(null);

		return caracteristicaAtividadeIntervencaoApp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideAtividadeIntervencaoApp == null) ? 0
						: ideAtividadeIntervencaoApp.hashCode());
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
		AtividadeIntervencaoApp other = (AtividadeIntervencaoApp) obj;
		if (ideAtividadeIntervencaoApp == null) {
			if (other.ideAtividadeIntervencaoApp != null)
				return false;
		} else if (!ideAtividadeIntervencaoApp
				.equals(other.ideAtividadeIntervencaoApp))
			return false;
		return true;
	}
	
	/**
	 * Método que retorna TRUE caso a {@link AtividadeIntervencaoApp} seja 'Outras atividades similares 
	 * devidamente caracterizadas e motivadas em procedimento administrativo próprio, quando inexistir 
	 * alternativa técnica e locacional ao empreendimento proposto, definidas em ato do Chefe do Poder 
	 * Executivo federal'
	 * 
	 * @author eduardo.fernandes 
	 * @since 11/01/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8263">#8263</a>
	 * @return [ideAtividadeIntervencaoApp == 5]
	 */
	public boolean isOutrasAtividades(){
		return this.ideAtividadeIntervencaoApp == OUTRAS_ATIVIDADES;
	}

	public boolean isSelecionado() {
		return selecionado;
	}

	public void setSelecionado(boolean selecionado) {
		this.selecionado = selecionado;
	}

	@Override
	public String toString() {
		return "AtividadeIntervencaoApp [ide="
				+ ideAtividadeIntervencaoApp + ", des="
				+ desAtividadeIntervencaoApp + "]";
	}

}