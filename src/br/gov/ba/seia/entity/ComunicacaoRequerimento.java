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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.ComunicacaoRequerenteInterface;
import flexjson.JSON;

@Entity
@Table(name = "comunicacao_requerimento")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "ComunicacaoRequerimento.findAll", query = "SELECT c FROM ComunicacaoRequerimento c"),
		@NamedQuery(name = "ComunicacaoRequerimento.findByIdeComunicacaoRequerimento", query = "SELECT c FROM ComunicacaoRequerimento c WHERE c.ideComunicacaoRequerimento = :ideComunicacaoRequerimento"),
		@NamedQuery(name = "ComunicacaoRequerimento.findByDtcComunicacao", query = "SELECT c FROM ComunicacaoRequerimento c WHERE c.dtcComunicacao = :dtcComunicacao"),
		@NamedQuery(name = "ComunicacaoRequerimento.findByDesMensagem", query = "SELECT c FROM ComunicacaoRequerimento c WHERE c.desMensagem = :desMensagem") })
public class ComunicacaoRequerimento implements Serializable, ComunicacaoRequerenteInterface {
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "COMUNICACAO_REQUERIMENTO_IDECOMUNICACAOREQUERIMENTO_GENERATOR", sequenceName = "COMUNICACAO_REQUERIMENTO_IDE_COMUNICACAO_REQUERIMENTO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMUNICACAO_REQUERIMENTO_IDECOMUNICACAOREQUERIMENTO_GENERATOR")
	@Column(name = "ide_comunicacao_requerimento")
	private Integer ideComunicacaoRequerimento;
	@Basic(optional = false)
	@Column(name = "dtc_comunicacao", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcComunicacao;
	@Column(name = "des_mensagem", length = 4000)
	private String desMensagem;
	@Column(name = "ind_enviado")
	private Boolean indEnviado;
	@Column(name = "assunto", length = 4000)
	private String assunto;

	@JoinColumn(name = "ide_requerimento", referencedColumnName = "ide_requerimento", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Requerimento ideRequerimento;

	public ComunicacaoRequerimento() {
	}

	public ComunicacaoRequerimento(Integer ideComunicacaoRequerimento) {
		this.ideComunicacaoRequerimento = ideComunicacaoRequerimento;
	}

	public ComunicacaoRequerimento(Integer ideComunicacaoRequerimento, Date dtcComunicacao) {
		this.ideComunicacaoRequerimento = ideComunicacaoRequerimento;
		this.dtcComunicacao = dtcComunicacao;
	}

	public Integer getIdeComunicacaoRequerimento() {
		return ideComunicacaoRequerimento;
	}

	public void setIdeComunicacaoRequerimento(Integer ideComunicacaoRequerimento) {
		this.ideComunicacaoRequerimento = ideComunicacaoRequerimento;
	}

	public Date getDtcComunicacao() {
		return dtcComunicacao;
	}

	public void setDtcComunicacao(Date dtcComunicacao) {
		this.dtcComunicacao = dtcComunicacao;
	}

	@JSON(include = false)
	public String getDesMensagem() {
		return desMensagem;
	}

	public String getDesMensagemHtml() {
		return desMensagem.replace("\n", "<br/>");
	}

	public void setDesMensagem(String desMensagem) {
		this.desMensagem = desMensagem;
	}

	@JSON(include = false)
	public Requerimento getIdeRequerimento() {
		return ideRequerimento;
	}

	public void setIdeRequerimento(Requerimento ideRequerimento) {
		this.ideRequerimento = ideRequerimento;
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	@JSON(include = false)
	public Boolean getIndEnviado() {
		return indEnviado;
	}

	public void setIndEnviado(Boolean indEnviado) {
		this.indEnviado = indEnviado;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ideComunicacaoRequerimento != null ? ideComunicacaoRequerimento.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		
		
		if (!(object instanceof ComunicacaoRequerimento)) {
			return false;
		}
		ComunicacaoRequerimento other = (ComunicacaoRequerimento) object;
		if ((this.ideComunicacaoRequerimento == null && other.ideComunicacaoRequerimento != null)
				|| (this.ideComunicacaoRequerimento != null && !this.ideComunicacaoRequerimento
						.equals(other.ideComunicacaoRequerimento))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "br.gov.ba.seia.entity.ComunicacaoRequerimento[ ideComunicacaoRequerimento="
				+ ideComunicacaoRequerimento + " ]";
	}

}
