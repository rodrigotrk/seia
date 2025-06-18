package br.gov.ba.seia.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.enumerator.TipoTerritorioPctEnum;

@Entity
@Table(name="tipo_territorio_pct")
public class TipoTerritorioPct extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "tipo_territorio_pct_generator", sequenceName = "tipo_territorio_pct_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_territorio_pct_generator")
	@Column(name="ide_tipo_territorio_pct")
	private Integer ideTipoTerritorioPct;
	
	@Column(name="dsc_tipo_territorio_pct", nullable=false)
	private String dscTipoTerritorioPct;
	
	@Column(name="ind_excluido", nullable=false)
	private Boolean indExcluido;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dtc_criacao", nullable=false)
	private Date dtcCriacao;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dtc_exclusao", nullable=false)
	private Date dtcExclusao;

	public TipoTerritorioPct() {
	}
	
	public TipoTerritorioPct(TipoTerritorioPctEnum tipoTerritorioPctEnum) {
		this.ideTipoTerritorioPct=tipoTerritorioPctEnum.getId();
	}

	public Integer getIdeTipoTerritorioPct() {
		return ideTipoTerritorioPct;
	}

	public void setIdeTipoTerritorioPct(Integer ideTipoTerritorioPct) {
		this.ideTipoTerritorioPct = ideTipoTerritorioPct;
	}

	public String getDscTipoTerritorioPct() {
		return dscTipoTerritorioPct;
	}

	public void setDscTipoTerritorioPct(String dscTipoTerritorioPct) {
		this.dscTipoTerritorioPct = dscTipoTerritorioPct;
	}

	public Boolean getIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(Boolean indExcluido) {
		this.indExcluido = indExcluido;
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
	
	
}
