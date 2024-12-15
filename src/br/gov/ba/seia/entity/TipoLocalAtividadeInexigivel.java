package br.gov.ba.seia.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name = "tipo_local_atividade_inexigivel")
@XmlRootElement
public class TipoLocalAtividadeInexigivel extends AbstractEntity {
	
	private static final long serialVersionUID = -8546492471641787366L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "ide_tipo_local_atividade_inexigivel", nullable = false)
	private Integer ideTipoLocalAtividadeInexigivel;
	
	@Column(name = "des_tipo_local_atividade", nullable = false)
	private String desTipoLocalAtividadeInexigivel;
	
	@Column(name = "dtc_criacao", nullable = false)
	private Date dtcCriacao;
	
	@Column(name = "dtc_exclusao")
	private Date dtcExclusao;
	
	@Column(name = "ind_ativo", nullable = false)
	private Boolean indAtivo;
	
	public TipoLocalAtividadeInexigivel() {
	}
	
	public TipoLocalAtividadeInexigivel(Integer ideTipoLocalAtividadeInexigivel) {
		this.ideTipoLocalAtividadeInexigivel = ideTipoLocalAtividadeInexigivel;
	}
	
	public Integer getIdeTipoLocalAtividadeInexigivel() {
		return ideTipoLocalAtividadeInexigivel;
	}
	
	public String getDesTipoLocalAtividadeInexigivel() {
		return desTipoLocalAtividadeInexigivel;
	}
	
	public Date getDtcCriacao() {
		return dtcCriacao;
	}
	
	public Date getDtcExclusao() {
		return dtcExclusao;
	}
	
	public Boolean getIndAtivo() {
		return indAtivo;
	}
	
	public void setIdeTipoLocalAtividadeInexigivel(Integer ideTipoLocalAtividadeInexigivel) {
		this.ideTipoLocalAtividadeInexigivel = ideTipoLocalAtividadeInexigivel;
	}
	
	public void setDesTipoLocalAtividadeInexigivel(String desTipoLocalAtividadeInexigivel) {
		this.desTipoLocalAtividadeInexigivel = desTipoLocalAtividadeInexigivel;
	}
	
	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}
	
	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}
	
	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}
}
