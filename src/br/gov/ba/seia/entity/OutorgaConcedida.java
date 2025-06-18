package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


/**
 * Entidade criada para representar as outorgas que foram concedidas após aprovação da análise técnica ser feita pelo coordenador.
 * @author eduardo.fernandes (eduardo.fernandes@avansys.com.br)
 * @since 07/04/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7550">#7550</a>
 */
@Entity
@Table(name="outorga_concedida")
@NamedQuery(name="OutorgaConcedida.findAll", query="SELECT o FROM OutorgaConcedida o")
public class OutorgaConcedida implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="OUTORGA_CONCEDIDA_IDEOUTORGACONCEDIDA_GENERATOR", sequenceName="OUTORGA_CONCEDIDA_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="OUTORGA_CONCEDIDA_IDEOUTORGACONCEDIDA_GENERATOR")
	@Column(name="ide_outorga_concedida", unique=true, nullable=false)
	private Integer ideOutorgaConcedida;

	@NotNull
	@JoinColumn(name="ide_fce_outorga_localizacao_geografica",referencedColumnName = "ide_fce_outorga_localizacao_geografica", nullable = false)
	@OneToOne(fetch = FetchType.LAZY)
	private FceOutorgaLocalizacaoGeografica ideFceOutorgaLocalizacaoGeografica;

	@NotNull
	@JoinColumn(name="ide_outorga_localizacao_geografica",referencedColumnName = "ide_outorga_localizacao_geografica", nullable = false)
	@OneToOne(fetch = FetchType.LAZY)
	private OutorgaLocalizacaoGeografica ideOutorgaLocalizacaoGeografica;

	public OutorgaConcedida() {
	}
	
	public OutorgaConcedida(FceOutorgaLocalizacaoGeografica ideFceOutorgaLocalizacaoGeografica, OutorgaLocalizacaoGeografica ideOutorgaLocalizacaoGeografica) {
		this.ideFceOutorgaLocalizacaoGeografica = ideFceOutorgaLocalizacaoGeografica;
		this.ideOutorgaLocalizacaoGeografica = ideOutorgaLocalizacaoGeografica;
	}

	public Integer getIdeOutorgaConcedida() {
		return this.ideOutorgaConcedida;
	}

	public void setIdeOutorgaConcedida(Integer ideOutorgaConcedida) {
		this.ideOutorgaConcedida = ideOutorgaConcedida;
	}

	public FceOutorgaLocalizacaoGeografica getIdeFceOutorgaLocalizacaoGeografica() {
		return ideFceOutorgaLocalizacaoGeografica;
	}

	public void setIdeFceOutorgaLocalizacaoGeografica(FceOutorgaLocalizacaoGeografica ideFceOutorgaLocalizacaoGeografica) {
		this.ideFceOutorgaLocalizacaoGeografica = ideFceOutorgaLocalizacaoGeografica;
	}

	public OutorgaLocalizacaoGeografica getIdeOutorgaLocalizacaoGeografica() {
		return ideOutorgaLocalizacaoGeografica;
	}

	public void setIdeOutorgaLocalizacaoGeografica(OutorgaLocalizacaoGeografica ideOutorgaLocalizacaoGeografica) {
		this.ideOutorgaLocalizacaoGeografica = ideOutorgaLocalizacaoGeografica;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideFceOutorgaLocalizacaoGeografica == null) ? 0
						: ideFceOutorgaLocalizacaoGeografica.hashCode());
		result = prime
				* result
				+ ((ideOutorgaConcedida == null) ? 0 : ideOutorgaConcedida
						.hashCode());
		result = prime
				* result
				+ ((ideOutorgaLocalizacaoGeografica == null) ? 0
						: ideOutorgaLocalizacaoGeografica.hashCode());
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
		OutorgaConcedida other = (OutorgaConcedida) obj;
		if (ideFceOutorgaLocalizacaoGeografica == null) {
			if (other.ideFceOutorgaLocalizacaoGeografica != null)
				return false;
		} else if (!ideFceOutorgaLocalizacaoGeografica
				.equals(other.ideFceOutorgaLocalizacaoGeografica))
			return false;
		if (ideOutorgaConcedida == null) {
			if (other.ideOutorgaConcedida != null)
				return false;
		} else if (!ideOutorgaConcedida.equals(other.ideOutorgaConcedida))
			return false;
		if (ideOutorgaLocalizacaoGeografica == null) {
			if (other.ideOutorgaLocalizacaoGeografica != null)
				return false;
		} else if (!ideOutorgaLocalizacaoGeografica
				.equals(other.ideOutorgaLocalizacaoGeografica))
			return false;
		return true;
	}


}