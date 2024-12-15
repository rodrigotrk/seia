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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.enumerator.StatusReenquadramentoEnum;
import br.gov.ba.seia.interfaces.TramitacaoInterface;
import br.gov.ba.seia.util.ContextoUtil;

@Entity
@Table(name = "tramitacao_reenquadramento_processo")
public class TramitacaoReenquadramentoProcesso implements Serializable, TramitacaoInterface {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRAMITACAO_REENQUADRAMENTO_PROCESSO_IDETRAMITACAOREENQUADRAMENTOPROCESSO_GENERATOR")
	@SequenceGenerator(name = "TRAMITACAO_REENQUADRAMENTO_PROCESSO_IDETRAMITACAOREENQUADRAMENTOPROCESSO_GENERATOR", sequenceName = "TRAMITACAO_REENQUADRAMENTO_PROCESSO_SEQ", allocationSize=1)
	@Column(name = "ide_tramitacao_reenquadramento_processo")
	private Integer ideTramitacaoReenquadramentoProcesso;

	@Column(name = "dtc_movimentacao")
	private Date dtcMovimentacao;

	@JoinColumn(name = "ide_pessoa", referencedColumnName = "ide_pessoa", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Pessoa idePessoa;

	@JoinColumn(name = "ide_processo_reenquadramento", referencedColumnName = "ide_processo_reenquadramento", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private ProcessoReenquadramento ideProcessoReenquadramento;

	@JoinColumn(name = "ide_status_reenquadramento", referencedColumnName = "ide_status_reenquadramento", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private StatusReenquadramento ideStatusReenquadramento;

	public TramitacaoReenquadramentoProcesso() {
	}
	
	public TramitacaoReenquadramentoProcesso(ProcessoReenquadramento ideProcessoReenquadramento, StatusReenquadramentoEnum statusReenquadramentoEnum) {
		this.ideProcessoReenquadramento=ideProcessoReenquadramento;
		this.dtcMovimentacao = new Date();
		this.ideStatusReenquadramento = new StatusReenquadramento(statusReenquadramentoEnum);
		this.idePessoa=ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa();
	}

	public Integer getIdeTramitacaoReenquadramentoProcesso() {
		return ideTramitacaoReenquadramentoProcesso;
	}

	public void setIdeTramitacaoReenquadramentoProcesso(Integer ideTramitacaoReenquadramentoProcesso) {
		this.ideTramitacaoReenquadramentoProcesso = ideTramitacaoReenquadramentoProcesso;
	}

	public Date getDtcMovimentacao() {
		return dtcMovimentacao;
	}

	public void setDtcMovimentacao(Date dtcMovimentacao) {
		this.dtcMovimentacao = dtcMovimentacao;
	}

	public Pessoa getIdePessoa() {
		return idePessoa;
	}

	public void setIdePessoa(Pessoa idePessoa) {
		this.idePessoa = idePessoa;
	}

	public ProcessoReenquadramento getIdeProcessoReenquadramento() {
		return ideProcessoReenquadramento;
	}

	public void setIdeProcessoReenquadramento(ProcessoReenquadramento ideProcessoReenquadramento) {
		this.ideProcessoReenquadramento = ideProcessoReenquadramento;
	}

	public StatusReenquadramento getIdeStatusReenquadramento() {
		return ideStatusReenquadramento;
	}

	public void setIdeStatusReenquadramento(StatusReenquadramento ideStatusReenquadramento) {
		this.ideStatusReenquadramento = ideStatusReenquadramento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideTramitacaoReenquadramentoProcesso == null) ? 0
				: ideTramitacaoReenquadramentoProcesso.hashCode());
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
		TramitacaoReenquadramentoProcesso other = (TramitacaoReenquadramentoProcesso) obj;
		if (ideTramitacaoReenquadramentoProcesso == null) {
			if (other.ideTramitacaoReenquadramentoProcesso != null)
				return false;
		} else if (!ideTramitacaoReenquadramentoProcesso.equals(other.ideTramitacaoReenquadramentoProcesso))
			return false;
		return true;
	}

}