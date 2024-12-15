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
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.enumerator.CadastroAtividadeNaoSujeitaLicTipoStatusEnum;


/**
 * @author eduardo.fernandes
 * @since 03/11/2016
 * @see <a href="http://10.105.17.77/redmine/issues/">#8187</a>
 */
@Entity
@Table(name="cadastro_atividade_nao_sujeita_lic_status")
@NamedQuery(name="CadastroAtividadeNaoSujeitaLicStatus.findAll", query="SELECT c FROM CadastroAtividadeNaoSujeitaLicStatus c")
public class CadastroAtividadeNaoSujeitaLicStatus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "CADASTRO_ATIVIDADE_NAO_SUJEITA_LIC_TIPO_STATUS_GENERATOR", sequenceName = "cadastro_atividade_nao_sujeita_lic_status_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CADASTRO_ATIVIDADE_NAO_SUJEITA_LIC_TIPO_STATUS_GENERATOR")
	@Column(name="ide_cadastro_atividade_nao_sujeita_lic_status")
	private Integer ideCadastroAtividadeNaoSujeitaLicStatus;

	@Column(name="dtc_status")
	private Date dtcStatus;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_pessoa_fisica", referencedColumnName = "ide_pessoa_fisica", nullable = false)
	private PessoaFisica idePessoaFisica;

	//bi-directional many-to-one association to CadastroAtividadeNaoSujeitaLic
	@ManyToOne
	@JoinColumn(name="ide_cadastro_atividade_nao_sujeita_lic")
	private CadastroAtividadeNaoSujeitaLic cadastroAtividadeNaoSujeitaLic;

	//bi-directional many-to-one association to CadastroAtividadeNaoSujeitaLicTipoStatus
	@ManyToOne
	@JoinColumn(name="ide_cadastro_atividade_nao_sujeita_lic_tipo_status")
	private CadastroAtividadeNaoSujeitaLicTipoStatus cadastroAtividadeNaoSujeitaLicTipoStatus;

	public CadastroAtividadeNaoSujeitaLicStatus() {
	}

	public CadastroAtividadeNaoSujeitaLicStatus(CadastroAtividadeNaoSujeitaLic cadastroAtividade, CadastroAtividadeNaoSujeitaLicTipoStatusEnum tipoStatusEnum, PessoaFisica pessoaFisica) {
		this.cadastroAtividadeNaoSujeitaLic = cadastroAtividade;
		this.cadastroAtividadeNaoSujeitaLicTipoStatus = new CadastroAtividadeNaoSujeitaLicTipoStatus(tipoStatusEnum);
		this.idePessoaFisica = pessoaFisica;
		this.dtcStatus = new Date();
	}

	public Integer getIdeCadastroAtividadeNaoSujeitaLicStatus() {
		return this.ideCadastroAtividadeNaoSujeitaLicStatus;
	}

	public void setIdeCadastroAtividadeNaoSujeitaLicStatus(Integer ideCadastroAtividadeNaoSujeitaLicStatus) {
		this.ideCadastroAtividadeNaoSujeitaLicStatus = ideCadastroAtividadeNaoSujeitaLicStatus;
	}

	public Date getDtcStatus() {
		return this.dtcStatus;
	}

	public void setDtcStatus(Date dtcStatus) {
		this.dtcStatus = dtcStatus;
	}

	public PessoaFisica getIdePessoaFisica() {
		return this.idePessoaFisica;
	}

	public void setIdePessoaFisica(PessoaFisica idePessoaFisica) {
		this.idePessoaFisica = idePessoaFisica;
	}

	public CadastroAtividadeNaoSujeitaLic getCadastroAtividadeNaoSujeitaLic() {
		return this.cadastroAtividadeNaoSujeitaLic;
	}

	public void setCadastroAtividadeNaoSujeitaLic(CadastroAtividadeNaoSujeitaLic cadastroAtividadeNaoSujeitaLic) {
		this.cadastroAtividadeNaoSujeitaLic = cadastroAtividadeNaoSujeitaLic;
	}

	public CadastroAtividadeNaoSujeitaLicTipoStatus getCadastroAtividadeNaoSujeitaLicTipoStatus() {
		return this.cadastroAtividadeNaoSujeitaLicTipoStatus;
	}

	public void setCadastroAtividadeNaoSujeitaLicTipoStatus(CadastroAtividadeNaoSujeitaLicTipoStatus cadastroAtividadeNaoSujeitaLicTipoStatus) {
		this.cadastroAtividadeNaoSujeitaLicTipoStatus = cadastroAtividadeNaoSujeitaLicTipoStatus;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cadastroAtividadeNaoSujeitaLic == null) ? 0 : cadastroAtividadeNaoSujeitaLic.hashCode());
		result = prime * result + ((cadastroAtividadeNaoSujeitaLicTipoStatus == null) ? 0 : cadastroAtividadeNaoSujeitaLicTipoStatus.hashCode());
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
		CadastroAtividadeNaoSujeitaLicStatus other = (CadastroAtividadeNaoSujeitaLicStatus) obj;
		if (cadastroAtividadeNaoSujeitaLic == null) {
			if (other.cadastroAtividadeNaoSujeitaLic != null)
				return false;
		}
		else if (!cadastroAtividadeNaoSujeitaLic.equals(other.cadastroAtividadeNaoSujeitaLic))
			return false;
		if (cadastroAtividadeNaoSujeitaLicTipoStatus == null) {
			if (other.cadastroAtividadeNaoSujeitaLicTipoStatus != null)
				return false;
		}
		else if (!cadastroAtividadeNaoSujeitaLicTipoStatus.equals(other.cadastroAtividadeNaoSujeitaLicTipoStatus))
			return false;
		return true;
	}

}