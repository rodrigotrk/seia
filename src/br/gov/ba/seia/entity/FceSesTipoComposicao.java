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
@Table(name="fce_ses_tipo_composicao")
public class FceSesTipoComposicao {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_ses_tipo_composicao_seq")
	@SequenceGenerator(name = "fce_ses_tipo_composicao_seq", sequenceName = "fce_ses_tipo_composicao_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_fce_ses_tipo_composicao")
	private Integer ideFceSesTipoComposicao;
	
	@Column(name="desc_fce_ses_tipo_composicao")
	private String descricaoFceSesTipoComposicao;
	
	@Column(name="ind_ativo")
	private Boolean indAtivo;
	
	
	public Integer getIdeFceSesTipoComposicao() {
		return ideFceSesTipoComposicao;
	}

	public void setIdeFceSesTipoComposicao(Integer ideFceSesTipoComposicao) {
		this.ideFceSesTipoComposicao = ideFceSesTipoComposicao;
	}

	public String getDescricaoFceSesTipoComposicao() {
		return descricaoFceSesTipoComposicao;
	}

	public void setDescricaoFceSesTipoComposicao(
			String descricaoFceSesTipoComposicao) {
		this.descricaoFceSesTipoComposicao = descricaoFceSesTipoComposicao;
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
	   if (ob instanceof FceSesTipoComposicao) {
		   FceSesTipoComposicao other = (FceSesTipoComposicao) ob;
	       return this.ideFceSesTipoComposicao.equals(other.getIdeFceSesTipoComposicao());
	   }
	   return false;
	}

}
