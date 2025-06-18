package br.gov.ba.seia.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name="faixa_diametro_adutora")
public class FaixaDiametroAdutora extends AbstractEntity{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "faixa_diametro_adutora_seq")
	@SequenceGenerator(name = "faixa_diametro_adutora_seq", sequenceName = "faixa_diametro_adutora_seq", allocationSize = 1)
	@Column(name="ide_faixa_diametro_adutora")
	private Integer ideFaixaDiametroAdutora;
	
	@Column(name="dsc_faixa_diametro_adutora")
	private String descricaoFaixaDiametroAdutora;
	
	@Column(name="tipo_fase_agua")
	private Integer tipoFaseAgua;
	
	@Column(name="ind_ativo")
	private Boolean indAtivo;
	
	public FaixaDiametroAdutora() {
	}

	public Integer getIdeFaixaDiametroAdutora() {
		return ideFaixaDiametroAdutora;
	}

	public void setIdeFaixaDiametroAdutora(Integer ideFaixaDiametroAdutora) {
		this.ideFaixaDiametroAdutora = ideFaixaDiametroAdutora;
	}

	public String getDescricaoFaixaDiametroAdutora() {
		return descricaoFaixaDiametroAdutora;
	}

	public void setDescricaoFaixaDiametroAdutora(String descricaoFaixaDiametroAdutora) {
		this.descricaoFaixaDiametroAdutora = descricaoFaixaDiametroAdutora;
	}

	public Integer getTipoFaseAgua() {
		return tipoFaseAgua;
	}

	public void setTipoFaseAgua(Integer tipoFaseAgua) {
		this.tipoFaseAgua = tipoFaseAgua;
	}

	public Boolean getIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}
	
}
