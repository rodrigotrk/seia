package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "lote_retorno_boleto", uniqueConstraints = { @UniqueConstraint(columnNames = { "ide_lote_retorno_boleto" }) })
@XmlRootElement
public class LoteRetornoBoleto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="lote_retorno_boleto_ide_lote_retorno_boleto_seq", sequenceName="lote_retorno_boleto_ide_lote_retorno_boleto_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="lote_retorno_boleto_ide_lote_retorno_boleto_seq")
	@Column(name="ide_lote_retorno_boleto", nullable = false)
	private Integer ideLoteRetornoBoleto;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_lote_boleto", referencedColumnName = "ide_lote_boleto", nullable = false)
	private LoteBoleto ideLoteBoleto;
	
	@Column(name = "des_caminho_arquivo", length = 100, nullable = false)
	private String desCaminhoArquivo;
	
	@Column(name="dtc_importacao_retorno", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcImportacaoRetorno;
	
	@JoinColumn(name = "ide_pessoa", referencedColumnName = "ide_pessoa")
	@ManyToOne(optional = false)
	private Pessoa idePessoa;
	
	@OneToMany(mappedBy = "ideLoteRetornoBoleto", fetch = FetchType.LAZY)
    private Collection<BoletoPagamentoRequerimento> boletoPagamentoRequerimentoCollection;
	
    public LoteRetornoBoleto() {
    }

	public Integer getIdeLoteRetornoBoleto() {
		return ideLoteRetornoBoleto;
	}

	public void setIdeLoteRetornoBoleto(Integer ideLoteRetornoBoleto) {
		this.ideLoteRetornoBoleto = ideLoteRetornoBoleto;
	}

	public LoteBoleto getIdeLoteBoleto() {
		return ideLoteBoleto;
	}

	public void setIdeLoteBoleto(LoteBoleto ideLoteBoleto) {
		this.ideLoteBoleto = ideLoteBoleto;
	}

	public String getDesCaminhoArquivo() {
		return desCaminhoArquivo;
	}

	public void setDesCaminhoArquivo(String desCaminhoArquivo) {
		this.desCaminhoArquivo = desCaminhoArquivo;
	}

	public Date getDtcImportacaoRetorno() {
		return dtcImportacaoRetorno;
	}

	public void setDtcImportacaoRetorno(Date dtcImportacaoRetorno) {
		this.dtcImportacaoRetorno = dtcImportacaoRetorno;
	}

	public Pessoa getIdePessoa() {
		return idePessoa;
	}

	public void setIdePessoa(Pessoa idePessoa) {
		this.idePessoa = idePessoa;
	}

	public Collection<BoletoPagamentoRequerimento> getBoletoPagamentoRequerimentoCollection() {
		return boletoPagamentoRequerimentoCollection;
	}

	public void setBoletoPagamentoRequerimentoCollection(
			Collection<BoletoPagamentoRequerimento> boletoPagamentoRequerimentoCollection) {
		this.boletoPagamentoRequerimentoCollection = boletoPagamentoRequerimentoCollection;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideLoteRetornoBoleto == null) ? 0 : ideLoteRetornoBoleto.hashCode());
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
		LoteRetornoBoleto other = (LoteRetornoBoleto) obj;
		if (ideLoteRetornoBoleto == null) {
			if (other.ideLoteRetornoBoleto != null)
				return false;
		} else if (!ideLoteRetornoBoleto.equals(other.ideLoteRetornoBoleto))
			return false;
		return true;
	}
	
}