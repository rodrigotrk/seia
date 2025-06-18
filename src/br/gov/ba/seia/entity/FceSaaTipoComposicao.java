package br.gov.ba.seia.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="fce_saa_tipo_composicao")
public class FceSaaTipoComposicao {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_saa_composicoes_seq")
	@SequenceGenerator(name = "fce_saa_composicoes_seq", sequenceName = "fce_saa_composicoes_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_fce_saa_tipo_composicao")
	private Integer ideFceSaaTipoComposicao;
	
	@Column(name="dsc_fce_saa_tipo_composicao")
	private String descricaoFceSaaTipoComposicao;
	
	@Column(name="ind_ativo")
	private Boolean indAtivo;

	public FceSaaTipoComposicao() {
		// Auto-generated constructor stub
	}
	
	public Integer getIdeFceSaaTipoComposicao() {
		return ideFceSaaTipoComposicao;
	}

	public void setIdeFceSaaTipoComposicao(Integer ideFceSaaTipoComposicao) {
		this.ideFceSaaTipoComposicao = ideFceSaaTipoComposicao;
	}

	public String getDescricaoFceSaaTipoComposicao() {
		return descricaoFceSaaTipoComposicao;
	}

	public void setDescricaoFceSaaTipoComposicao(
			String descricaoFceSaaTipoComposicao) {
		this.descricaoFceSaaTipoComposicao = descricaoFceSaaTipoComposicao;
	}

	public Boolean getIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}
	
	@Override
	public boolean equals(Object ob) {
	   if (ob == null) {
	       return false;
	   }
	   if (this == ob) {
	       return true;
	   }
	   if (ob instanceof FceSaaTipoComposicao) {
		   FceSaaTipoComposicao other = (FceSaaTipoComposicao) ob;
	       return this.ideFceSaaTipoComposicao.equals(other.getIdeFceSaaTipoComposicao());
	   }
	   return false;
	}
	
}
