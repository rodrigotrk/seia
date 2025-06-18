package br.gov.ba.seia.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name="tipo_vinculo_pct")
public class TipoVinculoPCT extends AbstractEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "tipo_vinculo_pct_generator", sequenceName = "tipo_vinculo_pct_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_vinculo_pct_generator")
	@Column(name="ide_tipo_vinculo_pct")
	private Integer ideTipoVinculoPCT;
	
	@Column(name="dsc_tipo_vinculo_pct", nullable = false)
	private String dscTipoVinculoPCT;
	
	@Column(name="ind_excluido", nullable=false)
	private Boolean indExcluido; 

	@Column(name="dtc_criacao", nullable=false)
	private Date dtcCriacao; 
	
	@Column(name="dtc_exclusao", nullable=true)
	private Date dtcEclusao;
	
	@Transient
	private String dscTipoVinculoPCTOutros;
	
	@Transient
	private boolean indAceite;

	public TipoVinculoPCT() {
	}

	public Integer getIdeTipoVinculoPCT() {
		return ideTipoVinculoPCT;
	}

	public void setIdeTipoVinculoPCT(Integer ideTipoVinculoPCT) {
		this.ideTipoVinculoPCT = ideTipoVinculoPCT;
	}

	public String getDscTipoVinculoPCT() {
		return dscTipoVinculoPCT;
	}

	public void setDscTipoVinculoPCT(String dscTipoVinculoPCT) {
		this.dscTipoVinculoPCT = dscTipoVinculoPCT;
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

	public Date getDtcEclusao() {
		return dtcEclusao;
	}

	public void setDtcEclusao(Date dtcEclusao) {
		this.dtcEclusao = dtcEclusao;
	}

	public String getDscTipoVinculoPCTOutros() {
		return dscTipoVinculoPCTOutros;
	}

	public void setDscTipoVinculoPCTOutros(String dscTipoVinculoPCTOutros) {
		this.dscTipoVinculoPCTOutros = dscTipoVinculoPCTOutros;
	}

	public boolean isIndAceite() {
		return indAceite;
	}

	public void setIndAceite(boolean indAceite) {
		this.indAceite = indAceite;
	}

}
