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


/**
 * @author eduardo.fernandes
 * @since 09/01/2016
 * @see <a href="http://10.105.12.26/redmine/issues/8263">#8263</a>
 */
@Entity
@Table(name="caracteristica_intervencao_app")
@NamedQuery(name="CaracteristicaIntervencaoApp.findAll", query="SELECT c FROM CaracteristicaIntervencaoApp c")
public class CaracteristicaIntervencaoApp implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="caracteristica_intervencao_app_seq", sequenceName="caracteristica_intervencao_app_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="caracteristica_intervencao_app_seq")
	@Column(name="ide_caracteristica_intervencao_app")
	private Integer ideCaracteristicaIntervencaoApp;

	@Column(name="dtc_criacao")
	private Date dtcCriacao;

	@Column(name="dtc_exclusao")
	private Date dtcExclusao;

	@Column(name="ind_excluido")
	private Boolean indExcluido;

	@Column(name="nom_caracteristica_intervencao")
	private String nomCaracteristicaIntervencao;

	//bi-directional many-to-one association to CaracteristicaAtividadeIntervencaoApp
	@OneToMany(mappedBy="caracteristicaIntervencaoApp")
	private List<CaracteristicaAtividadeIntervencaoApp> caracteristicaAtividadeIntervencaoApps;

	public CaracteristicaIntervencaoApp() {
	}

	public Integer getIdeCaracteristicaIntervencaoApp() {
		return this.ideCaracteristicaIntervencaoApp;
	}

	public void setIdeCaracteristicaIntervencaoApp(Integer ideCaracteristicaIntervencaoApp) {
		this.ideCaracteristicaIntervencaoApp = ideCaracteristicaIntervencaoApp;
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

	public String getNomCaracteristicaIntervencao() {
		return this.nomCaracteristicaIntervencao;
	}

	public void setNomCaracteristicaIntervencao(String nomCaracteristicaIntervencao) {
		this.nomCaracteristicaIntervencao = nomCaracteristicaIntervencao;
	}

	public List<CaracteristicaAtividadeIntervencaoApp> getCaracteristicaAtividadeIntervencaoApps() {
		return this.caracteristicaAtividadeIntervencaoApps;
	}

	public void setCaracteristicaAtividadeIntervencaoApps(List<CaracteristicaAtividadeIntervencaoApp> caracteristicaAtividadeIntervencaoApps) {
		this.caracteristicaAtividadeIntervencaoApps = caracteristicaAtividadeIntervencaoApps;
	}

	public CaracteristicaAtividadeIntervencaoApp addCaracteristicaAtividadeIntervencaoApp(CaracteristicaAtividadeIntervencaoApp caracteristicaAtividadeIntervencaoApp) {
		getCaracteristicaAtividadeIntervencaoApps().add(caracteristicaAtividadeIntervencaoApp);
		caracteristicaAtividadeIntervencaoApp.setCaracteristicaIntervencaoApp(this);

		return caracteristicaAtividadeIntervencaoApp;
	}

	public CaracteristicaAtividadeIntervencaoApp removeCaracteristicaAtividadeIntervencaoApp(CaracteristicaAtividadeIntervencaoApp caracteristicaAtividadeIntervencaoApp) {
		getCaracteristicaAtividadeIntervencaoApps().remove(caracteristicaAtividadeIntervencaoApp);
		caracteristicaAtividadeIntervencaoApp.setCaracteristicaIntervencaoApp(null);

		return caracteristicaAtividadeIntervencaoApp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideCaracteristicaIntervencaoApp == null) ? 0
						: ideCaracteristicaIntervencaoApp.hashCode());
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
		CaracteristicaIntervencaoApp other = (CaracteristicaIntervencaoApp) obj;
		if (ideCaracteristicaIntervencaoApp == null) {
			if (other.ideCaracteristicaIntervencaoApp != null)
				return false;
		} else if (!ideCaracteristicaIntervencaoApp
				.equals(other.ideCaracteristicaIntervencaoApp))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CaracteristicaIntervencaoApp [ide="
				+ ideCaracteristicaIntervencaoApp
				+ ", nome="
				+ nomCaracteristicaIntervencao + "]";
	}

}