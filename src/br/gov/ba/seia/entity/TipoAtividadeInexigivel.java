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
@Table(name = "tipo_atividade_inexigivel")
@XmlRootElement
public class TipoAtividadeInexigivel extends AbstractEntity {
	
	private static final long serialVersionUID = 4422306194675516404L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "ide_tipo_atividade_inexigivel", nullable = false)
	private Integer ideTipoAtividadeInexigivel;
	
	@Column(name = "des_tipo_atividade_inexigivel", nullable = false)
	private String desTipoAtividadeInexigivel;
	
	@Column(name = "dtc_criacao", nullable = false)
	private Date dtcCriacao;
	
	@Column(name = "dtc_exclusao")
	private Date dtcExclusao;
	
	@Column(name = "ind_ativo", nullable = false)
	private Boolean indAtivo;
	
	public TipoAtividadeInexigivel() {
	}
	
	public TipoAtividadeInexigivel(Integer ideTipoAtividadeInexigivel) {
		this.ideTipoAtividadeInexigivel = ideTipoAtividadeInexigivel;
	}
	
	public Integer getIdeTipoAtividadeInexigivel() {
		return ideTipoAtividadeInexigivel;
	}
	
	public String getDesTipoAtividadeInexigivel() {
		return desTipoAtividadeInexigivel;
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
	
	public void setIdeTipoAtividadeInexigivel(Integer ideTipoAtividadeInexigivel) {
		this.ideTipoAtividadeInexigivel = ideTipoAtividadeInexigivel;
	}
	
	public void setDesTipoAtividadeInexigivel(String desTipoAtividadeInexigivel) {
		this.desTipoAtividadeInexigivel = desTipoAtividadeInexigivel;
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((desTipoAtividadeInexigivel == null) ? 0 : desTipoAtividadeInexigivel.hashCode());
		result = prime * result + ((ideTipoAtividadeInexigivel == null) ? 0 : ideTipoAtividadeInexigivel.hashCode());
		result = prime * result + ((indAtivo == null) ? 0 : indAtivo.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		TipoAtividadeInexigivel other = (TipoAtividadeInexigivel) obj;
		if(desTipoAtividadeInexigivel == null) {
			if(other.desTipoAtividadeInexigivel != null) return false;
		} else if(!desTipoAtividadeInexigivel.equals(other.desTipoAtividadeInexigivel)) return false;
		if(ideTipoAtividadeInexigivel == null) {
			if(other.ideTipoAtividadeInexigivel != null) return false;
		} else if(!ideTipoAtividadeInexigivel.equals(other.ideTipoAtividadeInexigivel)) return false;
		if(indAtivo == null) {
			if(other.indAtivo != null) return false;
		} else if(!indAtivo.equals(other.indAtivo)) return false;
		return true;
	}
}
