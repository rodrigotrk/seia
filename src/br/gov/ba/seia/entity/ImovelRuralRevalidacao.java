package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author carlos.duarte
 */
@Entity
@Table(name = "imovel_rural_revalidacao")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "ImovelRuralRevalidacao.findAll", query = "SELECT i FROM ImovelRuralRevalidacao i"),
		@NamedQuery(name = "ImovelRuralRevalidacao.findByIdeImovelRural", query = "SELECT i FROM ImovelRuralRevalidacao i WHERE i.ideImovelRural = :ideImovelRural"),
		@NamedQuery(name = "ImovelRuralRevalidacao.findByIdeLocalizacaoGeografica", query = "SELECT i FROM ImovelRuralRevalidacao i WHERE i.ideLocalizacaoGeografica = :ideLocalizacaoGeografica")		
})
public class ImovelRuralRevalidacao implements Serializable,Cloneable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "imovel_rural_revalidacao_ide_imovel_rural_revalidacao_seq")
	@SequenceGenerator(name = "imovel_rural_revalidacao_ide_imovel_rural_revalidacao_seq", sequenceName = "imovel_rural_revalidacao_ide_imovel_rural_revalidacao_seq", allocationSize = 1)	
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_imovel_rural_revalidacao", nullable = false)
	private Integer ideImovelRuralRevalidacao;
	
	@JoinColumn(name = "ide_imovel_rural", referencedColumnName = "ide_imovel_rural", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private ImovelRural ideImovelRural;
	
	@JoinColumn(name = "ide_secao_revalidacao", referencedColumnName = "ide_secao_revalidacao")
	@OneToOne(optional = false)
	private SecaoRevalidacao ideSecaoRevalidacao;
	
	@JoinColumn(name = "ide_localizacao_geografica", referencedColumnName = "ide_localizacao_geografica")
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	private LocalizacaoGeografica ideLocalizacaoGeografica;
			
	@Basic(optional = false)
	@Column(name = "ind_validado")
	private Boolean indValidado;
	
	@Column(name = "dtc_validacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcValidacao;
	
	@Column(name = "dtc_criacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcCriacao;
    
	public ImovelRuralRevalidacao() {
		
	}

	public ImovelRuralRevalidacao(Integer ideImovelRuralRevalidacao) {
		this.ideImovelRuralRevalidacao = ideImovelRuralRevalidacao;
	}

	public Integer getIdeImovelRuralRevalidacao() {
		return ideImovelRuralRevalidacao;
	}

	public void setIdeImovelRuralRevalidacao(Integer ideImovelRuralRevalidacao) {
		this.ideImovelRuralRevalidacao = ideImovelRuralRevalidacao;
	}

	public ImovelRural getIdeImovelRural() {
		return ideImovelRural;
	}

	public void setIdeImovelRural(ImovelRural ideImovelRural) {
		this.ideImovelRural = ideImovelRural;
	}

	public SecaoRevalidacao getIdeSecaoRevalidacao() {
		return ideSecaoRevalidacao;
	}

	public void setIdeSecaoRevalidacao(SecaoRevalidacao ideSecaoRevalidacao) {
		this.ideSecaoRevalidacao = ideSecaoRevalidacao;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(
			LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public Boolean getIndValidado() {
		return indValidado;
	}

	public void setIndValidado(Boolean indValidado) {
		this.indValidado = indValidado;
	}

	public Date getDtcValidacao() {
		return dtcValidacao;
	}

	public void setDtcValidacao(Date dtcValidacao) {
		this.dtcValidacao = dtcValidacao;
	}

	public Date getDtcCriacao() {
		return dtcCriacao;
	}

	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ideImovelRuralRevalidacao != null ? ideImovelRuralRevalidacao.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof ImovelRuralRevalidacao)) {
			return false;
		}
		ImovelRuralRevalidacao other = (ImovelRuralRevalidacao) object;
		if ((this.ideImovelRuralRevalidacao == null && other.ideImovelRuralRevalidacao != null) || (this.ideImovelRuralRevalidacao != null && !this.ideImovelRuralRevalidacao.equals(other.ideImovelRuralRevalidacao))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "" + ideImovelRuralRevalidacao;
	}
}
