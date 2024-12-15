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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.interfaces.ComunicacaoRequerenteInterface;

@Entity
@Table(name = "comunicacao_reenquadramento_processo")
public class ComunicacaoReenquadramentoProcesso implements Serializable, ComunicacaoRequerenteInterface {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "COMUNICACAO_REENQUADRAMENTO_PROCESSO_IDECOMUNICACAOREENQUADRAMENTOPROCESSO_GENERATOR", sequenceName = "COMUNICACAO_REENQUADRAMENTO_PROCESSO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMUNICACAO_REENQUADRAMENTO_PROCESSO_IDECOMUNICACAOREENQUADRAMENTOPROCESSO_GENERATOR")
	@Column(name = "ide_comunicacao_reenquadramento_processo")
	private Integer ideComunicacaoReenquadramentoProcesso;

	@Column(name = "des_mensagem")
	private String desMensagem;

	@Column(name = "dtc_comunicacao")
	private Date dtcComunicacao;

	@JoinColumn(name = "ide_processo_reenquadramento", referencedColumnName = "ide_processo_reenquadramento", nullable = false)
	@OneToOne(optional = false, fetch = FetchType.EAGER)
	private ProcessoReenquadramento ideProcessoReenquadramento;

	public ComunicacaoReenquadramentoProcesso() {

	}

	public ComunicacaoReenquadramentoProcesso(ProcessoReenquadramento processoReenquadramento) {
		this.ideProcessoReenquadramento = processoReenquadramento;
	}

	public Integer getIdeComunicacaoReenquadramentoProcesso() {
		return ideComunicacaoReenquadramentoProcesso;
	}

	public void setIdeComunicacaoReenquadramentoProcesso(Integer ideComunicacaoReenquadramentoProcesso) {
		this.ideComunicacaoReenquadramentoProcesso = ideComunicacaoReenquadramentoProcesso;
	}

	public String getDesMensagem() {
		return desMensagem;
	}

	public void setDesMensagem(String desMensagem) {
		this.desMensagem = desMensagem;
	}

	public Date getDtcComunicacao() {
		return dtcComunicacao;
	}

	public void setDtcComunicacao(Date dtcComunicacao) {
		this.dtcComunicacao = dtcComunicacao;
	}

	public ProcessoReenquadramento getIdeProcessoReenquadramento() {
		return ideProcessoReenquadramento;
	}

	public void setIdeProcessoReenquadramento(ProcessoReenquadramento ideProcessoReenquadramento) {
		this.ideProcessoReenquadramento = ideProcessoReenquadramento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideComunicacaoReenquadramentoProcesso == null) ? 0
				: ideComunicacaoReenquadramentoProcesso.hashCode());
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
		ComunicacaoReenquadramentoProcesso other = (ComunicacaoReenquadramentoProcesso) obj;
		if (ideComunicacaoReenquadramentoProcesso == null) {
			if (other.ideComunicacaoReenquadramentoProcesso != null)
				return false;
		} else if (!ideComunicacaoReenquadramentoProcesso.equals(other.ideComunicacaoReenquadramentoProcesso))
			return false;
		return true;
	}

}