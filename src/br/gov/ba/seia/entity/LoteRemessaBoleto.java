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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "lote_remessa_boleto", uniqueConstraints = { @UniqueConstraint(columnNames = { "ide_lote_remessa_boleto" }) })
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "LoteRemessaBoleto.findByIdeLoteBoleto", query = "SELECT l FROM LoteRemessaBoleto l WHERE l.ideLoteBoleto.ideLoteBoleto = :ideLoteBoleto"),
		})
public class LoteRemessaBoleto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="lote_remessa_boleto_ide_lote_remessa_boleto_seq", sequenceName="lote_remessa_boleto_ide_lote_remessa_boleto_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="lote_remessa_boleto_ide_lote_remessa_boleto_seq")
	@Column(name="ide_lote_remessa_boleto", nullable = false)
	private Integer ideLoteRemessaBoleto;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_lote_boleto", referencedColumnName = "ide_lote_boleto", nullable = false)
	private LoteBoleto ideLoteBoleto;
	
	@Column(name="dtc_envio_remessa")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcEnvioRemessa;
	
	@JoinColumn(name = "ide_pessoa_geracao", referencedColumnName = "ide_pessoa")
	@ManyToOne(optional = false)
	private Pessoa idePessoaGeracao;
	
	@JoinColumn(name = "ide_pessoa_confirmacao", referencedColumnName = "ide_pessoa")
	@ManyToOne
	private Pessoa idePessoaConfirmacao;
	
	@OneToMany(mappedBy = "ideLoteRemessaBoleto", fetch = FetchType.LAZY)
    private Collection<BoletoPagamentoRequerimento> boletoPagamentoRequerimentoCollection;

    public LoteRemessaBoleto() {
    }

	public Integer getIdeLoteRemessaBoleto() {
		return ideLoteRemessaBoleto;
	}

	public void setIdeLoteRemessaBoleto(Integer ideLoteRemessaBoleto) {
		this.ideLoteRemessaBoleto = ideLoteRemessaBoleto;
	}

	public LoteBoleto getIdeLoteBoleto() {
		return ideLoteBoleto;
	}

	public void setIdeLoteBoleto(LoteBoleto ideLoteBoleto) {
		this.ideLoteBoleto = ideLoteBoleto;
	}

	public Date getDtcEnvioRemessa() {
		return dtcEnvioRemessa;
	}

	public void setDtcEnvioRemessa(Date dtcEnvioRemessa) {
		this.dtcEnvioRemessa = dtcEnvioRemessa;
	}

	public Pessoa getIdePessoaGeracao() {
		return idePessoaGeracao;
	}

	public void setIdePessoaGeracao(Pessoa idePessoaGeracao) {
		this.idePessoaGeracao = idePessoaGeracao;
	}

	public Pessoa getIdePessoaConfirmacao() {
		return idePessoaConfirmacao;
	}

	public void setIdePessoaConfirmacao(Pessoa idePessoaConfirmacao) {
		this.idePessoaConfirmacao = idePessoaConfirmacao;
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
				+ ((ideLoteRemessaBoleto == null) ? 0 : ideLoteRemessaBoleto.hashCode());
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
		LoteRemessaBoleto other = (LoteRemessaBoleto) obj;
		if (ideLoteRemessaBoleto == null) {
			if (other.ideLoteRemessaBoleto != null)
				return false;
		} else if (!ideLoteRemessaBoleto.equals(other.ideLoteRemessaBoleto))
			return false;
		return true;
	}
	
}