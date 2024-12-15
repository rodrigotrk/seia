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

@Entity
@Table(name="tipo_seguimento_pct")
public class TipoSeguimentoPct extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "tipo_seguimento_pct_seq_generator", sequenceName = "tipo_seguimento_pct_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_seguimento_pct_seq_generator")
	@Column(name="ide_tipo_seguimento_pct")
	private Integer ideTipoSeguimentoPct;
	
	@Column(name="dsc_tipo_seguimento_pct", nullable=false)
	private String dscTipoSeguimentoPct;
	
	@Column(name="dtc_criacao", nullable=false)
	private Date dtcCriacao;
	
	@Column(name="dtc_exclusao")
	private Date dtcExclusao;
	
	@Column(name="ind_excluido", nullable=false)
	private Boolean indExcluido;
	
	@Column(name="sgl_tipo_seguimento", nullable=false)
	private String sglTipoSeguimento;

	public TipoSeguimentoPct() {
	}

	public Integer getIdeTipoSeguimentoPct() {
		return ideTipoSeguimentoPct;
	}

	public void setIdeTipoSeguimentoPct(Integer ideTipoSeguimentoPct) {
		this.ideTipoSeguimentoPct = ideTipoSeguimentoPct;
	}

	public String getDscTipoSeguimentoPct() {
		return dscTipoSeguimentoPct;
	}

	public void setDscTipoSeguimentoPct(String dscTipoSeguimentoPct) {
		this.dscTipoSeguimentoPct = dscTipoSeguimentoPct;
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

	public String getSglTipoSeguimento() {
		return sglTipoSeguimento;
	}

	public void setSglTipoSeguimento(String sglTipoSeguimento) {
		this.sglTipoSeguimento = sglTipoSeguimento;
	}
	
	
	
}
