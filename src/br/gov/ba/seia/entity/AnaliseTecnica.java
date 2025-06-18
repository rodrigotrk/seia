package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name="analise_tecnica")
public class AnaliseTecnica implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="analise_tecnica_seq")    
	@SequenceGenerator(name="analise_tecnica_seq", sequenceName="analise_tecnica_seq", allocationSize=1)
	@Column(name="ide_analise_tecnica", unique=true, nullable=false)
	private Integer ideAnaliseTecnica;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dtc_fim_analise_processo")
	private Date dtcFimAnaliseProcesso;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dtc_inicio_analise_tecnica")
	private Date dtcInicioAnaliseTecnica;
	
	@Column(name="ind_aprovado")
	private Boolean indAprovado;
	
	@Column(name="observacao")
	private String observacao;
	
	@JoinColumn(name = "ide_processo", referencedColumnName = "ide_processo", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Processo ideProcesso;
	
	@OneToMany(mappedBy="ideAnaliseTecnica", fetch=FetchType.LAZY)
	private Collection<Fce> fceCollection;
	
	@OneToMany(mappedBy="ideAnaliseTecnica", fetch=FetchType.LAZY)
	private Collection<CondicionanteAnaliseTecnica> condicionanteAnaliseTecnicaCollection;
	
	@Column(name = "ind_devidamente_preenchido", nullable = true)
	private Boolean indDevidamentePreenchido;
	
	public AnaliseTecnica() {
		
	}
	public AnaliseTecnica(Integer ideProcesso) {
		this.ideProcesso= new Processo(ideProcesso);
		this.dtcInicioAnaliseTecnica = new Date();
	}

	public Integer getIdeAnaliseTecnica() {
		return ideAnaliseTecnica;
	}

	public void setIdeAnaliseTecnica(Integer ideAnaliseTecnica) {
		this.ideAnaliseTecnica = ideAnaliseTecnica;
	}

	public Date getDtcFimAnaliseProcesso() {
		return dtcFimAnaliseProcesso;
	}

	public void setDtcFimAnaliseProcesso(Date dtcFimAnaliseProcesso) {
		this.dtcFimAnaliseProcesso = dtcFimAnaliseProcesso;
	}

	public Date getDtcInicioAnaliseTecnica() {
		return dtcInicioAnaliseTecnica;
	}

	public void setDtcInicioAnaliseTecnica(Date dtcInicioAnaliseTecnica) {
		this.dtcInicioAnaliseTecnica = dtcInicioAnaliseTecnica;
	}

	public Processo getIdeProcesso() {
		return ideProcesso;
	}

	public void setIdeProcesso(Processo ideProcesso) {
		this.ideProcesso = ideProcesso;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideAnaliseTecnica == null) ? 0 : ideAnaliseTecnica.hashCode());
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
		AnaliseTecnica other = (AnaliseTecnica) obj;
		if (ideAnaliseTecnica == null) {
			if (other.ideAnaliseTecnica != null)
				return false;
		} else if (!ideAnaliseTecnica.equals(other.ideAnaliseTecnica))
			return false;
		return true;
	}

	public Collection<Fce> getFceCollection() {
		return fceCollection;
	}

	public void setFceCollection(Collection<Fce> fceCollection) {
		this.fceCollection = fceCollection;
	}

	public Boolean getIndAprovado() {
		return indAprovado;
	}

	public void setIndAprovado(Boolean indAprovado) {
		this.indAprovado = indAprovado;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public Boolean getIndDevidamentePreenchido() {
		return indDevidamentePreenchido;
	}
	public void setIndDevidamentePreenchido(Boolean indDevidamentePreenchido) {
		this.indDevidamentePreenchido = indDevidamentePreenchido;
	}

}