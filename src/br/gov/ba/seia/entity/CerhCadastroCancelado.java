package br.gov.ba.seia.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.util.ContextoUtil;

@Entity
@Table(name="cerh_cadastro_cancelado")
public class CerhCadastroCancelado extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cerh_cadastro_cancelado_seq")
	@SequenceGenerator(name = "cerh_cadastro_cancelado_seq", sequenceName = "cerh_cadastro_cancelado_seq", allocationSize = 1)
	@Column(name="ide_cerh_cadastro_cancelado")
	private Integer ideCerhCadastroCancelado;
	                
	@Column(name="dsc_observacao")
	private String dscObservacao;

	@Temporal(TemporalType.DATE)
	@Column(name="dt_cancelamento")
	private Date dtCancelamento;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dtc_pessoa_fisica_cancelamento")
	private Date dtcPessoaFisicaCancelamento;

	@Column(name="url_documento")
	private String urlDocumento;
	
	@ManyToOne
	@JoinColumn(name="ide_pessoa_fisica_cancelamento")
	private PessoaFisica idePessoaFisicaCancelamento;

	@OneToOne
	@JoinColumn(name="ide_cerh")
	private Cerh ideCerh;

	public CerhCadastroCancelado() {
	}

	public CerhCadastroCancelado(Cerh cerh) {
		this.ideCerh = cerh;
		this.dtCancelamento = new Date();
		this.idePessoaFisicaCancelamento = ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica();
	}

	public Integer getIdeCerhCadastroCancelado() {
		return ideCerhCadastroCancelado;
	}

	public void setIdeCerhCadastroCancelado(Integer ideCerhCadastroCancelado) {
		this.ideCerhCadastroCancelado = ideCerhCadastroCancelado;
	}

	public String getDscObservacao() {
		return dscObservacao;
	}

	public void setDscObservacao(String dscObservacao) {
		this.dscObservacao = dscObservacao;
	}

	public Date getDtCancelamento() {
		return dtCancelamento;
	}

	public void setDtCancelamento(Date dtCancelamento) {
		this.dtCancelamento = dtCancelamento;
	}

	public Date getDtcPessoaFisicaCancelamento() {
		return dtcPessoaFisicaCancelamento;
	}

	public void setDtcPessoaFisicaCancelamento(Date dtcPessoaFisicaCancelamento) {
		this.dtcPessoaFisicaCancelamento = dtcPessoaFisicaCancelamento;
	}

	public String getUrlDocumento() {
		return urlDocumento;
	}

	public void setUrlDocumento(String urlDocumento) {
		this.urlDocumento = urlDocumento;
	}

	public PessoaFisica getIdePessoaFisicaCancelamento() {
		return idePessoaFisicaCancelamento;
	}

	public void setIdePessoaFisicaCancelamento(
			PessoaFisica idePessoaFisicaCancelamento) {
		this.idePessoaFisicaCancelamento = idePessoaFisicaCancelamento;
	}

	public Cerh getIdeCerh() {
		return ideCerh;
	}

	public void setIdeCerh(Cerh ideCerh) {
		this.ideCerh = ideCerh;
	}
}