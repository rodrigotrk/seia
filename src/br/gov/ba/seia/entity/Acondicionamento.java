package br.gov.ba.seia.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.util.Util;
/**
 * 		
 * @author anderson.silva
 *
 */
@Entity
@Table(name = "ACONDICIONAMENTO")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Acondicionamento.findAll", query = "SELECT A FROM Acondicionamento A") })
public class Acondicionamento extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "IDE_ACONDICIONAMENTO")
	private Integer ideAcondicionamento;
	
	@Basic(optional = false)
	@Column(name = "COD_ACONDICIONAMENTO", length = 20)
	private String codAcondicionamento;
	
	@Basic(optional = false)
	@Column(name = "DES_ACONDICIONAMENTO", length = 40)
	private String dscAcondicionamento;
	
	@Basic(optional = false)
    @Column(name = "DTC_CRIACAO", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcCriacao;
    
	@Basic(optional = false)
    @Column(name = "IND_EXCLUIDO", nullable = false)
    private boolean indExcluido;
    
	@Column(name = "DTC_EXCLUSAO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcExclusao;

	@Transient
	private String descricaoFormatada;
	
	public Integer getIdeAcondicionamento() {
		return ideAcondicionamento;
	}

	public void setIdeAcondicionamento(Integer ideAcondicionamento) {
		this.ideAcondicionamento = ideAcondicionamento;
	}

	public String getCodAcondicionamento() {
		return codAcondicionamento;
	}

	public void setCodAcondicionamento(String codAcondicionamento) {
		this.codAcondicionamento = codAcondicionamento;
	}

	public String getDscAcondicionamento() {
		return dscAcondicionamento;
	}

	public void setDscAcondicionamento(String dscAcondicionamento) {
		this.dscAcondicionamento = dscAcondicionamento;
	}

	public Date getDtcCriacao() {
		return dtcCriacao;
	}

	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}

	public boolean isIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	public Date getDtcExclusao() {
		return dtcExclusao;
	}

	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}

	public String getDescricaoFormatada() {
		if(!Util.isNullOuVazio(this.codAcondicionamento) && !Util.isNullOuVazio(this.dscAcondicionamento)){
			return this.codAcondicionamento + '-' + this.dscAcondicionamento;
		}
		return descricaoFormatada;
	}

	public void setDescricaoFormatada(String descricaoFormatada) {
		this.descricaoFormatada = descricaoFormatada;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result	+ ((ideAcondicionamento == null) ? 0 : ideAcondicionamento.hashCode());
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
		Acondicionamento other = (Acondicionamento) obj;
		if (ideAcondicionamento == null) {
			if (other.ideAcondicionamento != null)
				return false;
		} else if (!ideAcondicionamento.equals(other.ideAcondicionamento))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.valueOf(ideAcondicionamento);
	}
	
}
