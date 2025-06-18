package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "hist_situacao_dae")
public class HistSituacaoDae implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ide_hist_situacao_dae")
	@GeneratedValue(generator = "hist_situacao_dae_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(sequenceName = "hist_situacao_dae_seq",name = "hist_situacao_dae_seq",allocationSize = 1)
	private Integer ideHistSituacaoDae;
	
	@JoinColumn(name = "ide_dae", nullable = false)
	@ManyToOne(fetch = FetchType.EAGER)
	private Dae ideDae;
	
	@JoinColumn(name = "ide_usuario")
	@ManyToOne(fetch = FetchType.EAGER)
	private Usuario ideUsuario;

	@JoinColumn(name = "ide_situacao_dae", nullable = false)
	@ManyToOne(fetch = FetchType.EAGER)
	private SituacaoDae ideSituacaoDae;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dt_alteracao", nullable = false)
	private Date dtAlteracao;
	
	public HistSituacaoDae(){
		
	}

	public Integer getIdeCerhHistSituacaoDae() {
		return ideHistSituacaoDae;
	}

	public void setIdeCerhHistSituacaoDae(Integer ideCerhHistSituacaoDae) {
		this.ideHistSituacaoDae = ideCerhHistSituacaoDae;
	}

	public Dae getIdeDae() {
		return ideDae;
	}

	public void setIdeDae(Dae ideDae) {
		this.ideDae = ideDae;
	}

	public Usuario getIdeUsuario() {
		return ideUsuario;
	}

	public void setIdeUsuario(Usuario ideUsuario) {
		this.ideUsuario = ideUsuario;
	}

	public SituacaoDae getIdeSituacaoDae() {
		return ideSituacaoDae;
	}

	public void setIdeSituacaoDae(SituacaoDae ideSituacaoDae) {
		this.ideSituacaoDae = ideSituacaoDae;
	}

	public Date getDtAlteracao() {
		return dtAlteracao;
	}

	public void setDtAlteracao(Date dtAlteracao) {
		this.dtAlteracao = dtAlteracao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideHistSituacaoDae == null) ? 0
						: ideHistSituacaoDae.hashCode());
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
		HistSituacaoDae other = (HistSituacaoDae) obj;
		if (ideHistSituacaoDae == null) {
			if (other.ideHistSituacaoDae != null)
				return false;
		} else if (!ideHistSituacaoDae.equals(other.ideHistSituacaoDae))
			return false;
		return true;
	}

	
}
