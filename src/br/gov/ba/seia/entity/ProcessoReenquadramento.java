package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "processo_reenquadramento")
public class ProcessoReenquadramento implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROCESSO_REENQUADRAMENTO_IDEPROCESSOREENQUADRAMENTO_GENERATOR")
	@SequenceGenerator(name = "PROCESSO_REENQUADRAMENTO_IDEPROCESSOREENQUADRAMENTO_GENERATOR", sequenceName = "PROCESSO_REENQUADRAMENTO_SEQ", allocationSize=1)
	@Column(name = "ide_processo_reenquadramento")
	private Integer ideProcessoReenquadramento;

	@Column(name = "num_processo")
	private String numProcesso;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dtc_formacao")
	private Date dtcFormacao;
	
	@Column(name = "ind_excluido")
	private Boolean indExcluido;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dtc_exclusao")
	private Date dtcExclusao;
	
	@Column(name = "ind_aceite_requerente")
	private Boolean indAceiteRequerente;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dtc_aceite_requerente")
	private Date dtcAceiteRequerente;

	@JoinColumn(name = "ide_processo", referencedColumnName = "ide_processo", nullable = false)
	@OneToOne(optional = false, fetch = FetchType.EAGER)
	private Processo ideProcesso;

	@OneToMany(mappedBy = "ideProcessoReenquadramento", cascade = CascadeType.PERSIST)
	private Collection<TramitacaoReenquadramentoProcesso> tramitacaoReenquadramentoProcessoCollection;

	@OneToMany(mappedBy = "ideProcessoReenquadramento")
	private Collection<Enquadramento> enquadramentoCollection;
	
	@Transient
	private long diasEncaminhamento;
	
	public ProcessoReenquadramento() {
	}
	
	public ProcessoReenquadramento(Integer ideProcessoReenquadramento) {
		this.ideProcessoReenquadramento = ideProcessoReenquadramento;
	}
	
	public ProcessoReenquadramento(Processo ideProcesso) {
		this.ideProcesso = ideProcesso;
		this.dtcFormacao = new Date();
		this.indExcluido = false;
	}

	public Integer getIdeProcessoReenquadramento() {
		return ideProcessoReenquadramento;
	}

	public void setIdeProcessoReenquadramento(Integer ideProcessoReenquadramento) {
		this.ideProcessoReenquadramento = ideProcessoReenquadramento;
	}

	public String getNumProcesso() {
		return numProcesso;
	}

	public void setNumProcesso(String numProcesso) {
		this.numProcesso = numProcesso;
	}

	public Date getDtcExclusao() {
		return dtcExclusao;
	}

	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}

	public Date getDtcFormacao() {
		return dtcFormacao;
	}

	public void setDtcFormacao(Date dtcFormacao) {
		this.dtcFormacao = dtcFormacao;
	}

	public Boolean getIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(Boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	public Processo getIdeProcesso() {
		return ideProcesso;
	}

	public void setIdeProcesso(Processo ideProcesso) {
		this.ideProcesso = ideProcesso;
	}

	public Collection<TramitacaoReenquadramentoProcesso> getTramitacaoReenquadramentoProcessoCollection() {
		return tramitacaoReenquadramentoProcessoCollection;
	}

	public void setTramitacaoReenquadramentoProcessoCollection(
			Collection<TramitacaoReenquadramentoProcesso> tramitacaoReenquadramentoProcessoCollection) {
		this.tramitacaoReenquadramentoProcessoCollection = tramitacaoReenquadramentoProcessoCollection;
	}

	public Collection<Enquadramento> getEnquadramentoCollection() {
		return enquadramentoCollection;
	}

	public void setEnquadramentoCollection(
			Collection<Enquadramento> enquadramentoCollection) {
		this.enquadramentoCollection = enquadramentoCollection;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideProcessoReenquadramento == null) ? 0
						: ideProcessoReenquadramento.hashCode());
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
		ProcessoReenquadramento other = (ProcessoReenquadramento) obj;
		if (ideProcessoReenquadramento == null) {
			if (other.ideProcessoReenquadramento != null)
				return false;
		} else if (!ideProcessoReenquadramento
				.equals(other.ideProcessoReenquadramento))
			return false;
		return true;
	}

	public long getDiasEncaminhamento() {

			return (new Date().getTime() - dtcFormacao.getTime()) /1000/60/60/24;   

	}

	public void setDiasEncaminhamento(long diasEncaminhamento) {
		this.diasEncaminhamento = diasEncaminhamento;
	}

	public Boolean getIndAceiteRequerente() {
		return indAceiteRequerente;
	}

	public void setIndAceiteRequerente(Boolean indAceiteRequerente) {
		this.indAceiteRequerente = indAceiteRequerente;
	}

	public Date getDtcAceiteRequerente() {
		return dtcAceiteRequerente;
	}

	public void setDtcAceiteRequerente(Date dtcAceiteRequerente) {
		this.dtcAceiteRequerente = dtcAceiteRequerente;
	}

}