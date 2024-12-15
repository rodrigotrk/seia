package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "lote_boleto", uniqueConstraints = { @UniqueConstraint(columnNames = { "ide_lote_boleto" }) })
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "LoteBoleto.findLast", query = "SELECT l FROM LoteBoleto l WHERE l.ideLoteBoleto = (SELECT max(l.ideLoteBoleto) FROM LoteBoleto l) "),
})
public class LoteBoleto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="lote_boleto_ide_lote_boleto_seq", sequenceName="lote_boleto_ide_lote_boleto_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="lote_boleto_ide_lote_boleto_seq")
	@Column(name="ide_lote_boleto", nullable = false)
	private Integer ideLoteBoleto;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tipo_lote_boleto",nullable = false)
	private TipoLoteBoleto ideTipoLoteBoleto;

	@Column(name="dtc_criacao",nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcCriacao;

	@Column(name="num_lote_boleto", length = 20, nullable = false)
	private String numLoteBoleto;
	
	@OneToOne(mappedBy = "ideLoteBoleto", fetch = FetchType.LAZY)
	private LoteRemessaBoleto loteRemessaBoleto;
	
	@OneToOne(mappedBy = "ideLoteBoleto", fetch = FetchType.LAZY)
	private LoteRetornoBoleto loteRetornoBoleto;
	
	@Transient
	private Integer quantidadeBoletos;

    public LoteBoleto() {
    }
    
    public LoteBoleto(TipoLoteBoleto tipoLoteBoleto, Date dataCriacao, String numLote) {
    	this.ideTipoLoteBoleto = tipoLoteBoleto;
    	this.dtcCriacao = dataCriacao;
    	this.numLoteBoleto = numLote;
    }
	
	public Integer getIdeLoteBoleto() {
		return ideLoteBoleto;
	}

	public void setIdeLoteBoleto(Integer ideLoteBoleto) {
		this.ideLoteBoleto = ideLoteBoleto;
	}

	public TipoLoteBoleto getIdeTipoLoteBoleto() {
		return ideTipoLoteBoleto;
	}

	public void setIdeTipoLoteBoleto(TipoLoteBoleto ideTipoLoteBoleto) {
		this.ideTipoLoteBoleto = ideTipoLoteBoleto;
	}

	public Date getDtcCriacao() {
		return dtcCriacao;
	}

	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}

	public String getNumLoteBoleto() {
		return numLoteBoleto;
	}

	public void setNumLoteBoleto(String numLoteBoleto) {
		this.numLoteBoleto = numLoteBoleto;
	}

	public LoteRemessaBoleto getLoteRemessaBoleto() {
		return loteRemessaBoleto;
	}

	public void setLoteRemessaBoleto(LoteRemessaBoleto loteRemessaBoleto) {
		this.loteRemessaBoleto = loteRemessaBoleto;
	}

	public LoteRetornoBoleto getLoteRetornoBoleto() {
		return loteRetornoBoleto;
	}

	public void setLoteRetornoBoleto(LoteRetornoBoleto loteRetornoBoleto) {
		this.loteRetornoBoleto = loteRetornoBoleto;
	}

	public Integer getQuantidadeBoletos() {
		return quantidadeBoletos;
	}

	public void setQuantidadeBoletos(Integer quantidadeBoletos) {
		this.quantidadeBoletos = quantidadeBoletos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideLoteBoleto == null) ? 0 : ideLoteBoleto.hashCode());
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
		LoteBoleto other = (LoteBoleto) obj;
		if (ideLoteBoleto == null) {
			if (other.ideLoteBoleto != null)
				return false;
		} else if (!ideLoteBoleto.equals(other.ideLoteBoleto))
			return false;
		return true;
	}
	
}