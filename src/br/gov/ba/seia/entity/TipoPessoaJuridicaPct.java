package br.gov.ba.seia.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.enumerator.TipoPessoaJuridicaPctEnum;

@Entity
@Table(name="tipo_pessoa_juridica_pct")
public class TipoPessoaJuridicaPct extends AbstractEntity {
	
	private static final long serialVersionUID = 2081403369150882984L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_pessoa_juridica_pct_seq")
	@SequenceGenerator(name = "tipo_pessoa_juridica_pct_seq", sequenceName = "tipo_pessoa_juridica_pct_seq", allocationSize = 1)
	@Column(name="ide_tipo_pessoa_juridica_pct", nullable=false)
	private Integer ideTipoPessoaJuridicaPct;
	
	@Column(name="dsc_tipo_pessoa_juridica_pct", nullable=false)
	private String dscTipoPessoaJuridicaPct;
	
	@Column(name="dtc_criacao", nullable=false)
	private Date dtcCriacao;
	
	@Column(name="dtc_exclusao", nullable=true)
	private Date dtcExclusao;
	
	@Column(name="ind_excluido", nullable=true)
	private Boolean indExcluido;
	
	public TipoPessoaJuridicaPct() {
		
	}
	
	public TipoPessoaJuridicaPct(TipoPessoaJuridicaPctEnum tipoPessoaJuridicaPctEnum) {
		this.ideTipoPessoaJuridicaPct=tipoPessoaJuridicaPctEnum.getId();
	}

	public Integer getIdeTipoPessoaJuridicaPct() {
		return ideTipoPessoaJuridicaPct;
	}

	public void setIdeTipoPessoaJuridicaPct(Integer ideTipoPessoaJuridicaPct) {
		this.ideTipoPessoaJuridicaPct = ideTipoPessoaJuridicaPct;
	}

	public String getDscTipoPessoaJuridicaPct() {
		return dscTipoPessoaJuridicaPct;
	}

	public void setDscTipoPessoaJuridicaPct(String dscTipoPessoaJuridicaPct) {
		this.dscTipoPessoaJuridicaPct = dscTipoPessoaJuridicaPct;
	}

	public Date getDtcCriacao() {
		return dtcCriacao;
	}

	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}

	public Date getDtcExclusao() {
		return dtcExclusao;
	}

	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}

	public Boolean getIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(Boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideTipoPessoaJuridicaPct == null) ? 0
						: ideTipoPessoaJuridicaPct.hashCode());
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
		TipoPessoaJuridicaPct other = (TipoPessoaJuridicaPct) obj;
		if (ideTipoPessoaJuridicaPct == null) {
			if (other.ideTipoPessoaJuridicaPct != null)
				return false;
		} else if (!ideTipoPessoaJuridicaPct
				.equals(other.ideTipoPessoaJuridicaPct))
			return false;
		return true;
	}

}
