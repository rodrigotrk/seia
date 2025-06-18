package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.enumerator.TipoAtividadeNaoSujeitaLicenciamentoEnum;
import br.gov.ba.seia.interfaces.BaseEntity;


/**
 * @author eduardo.fernandes
 * @since 03/11/2016
 * @see <a href="http://10.105.17.77/redmine/issues/">#8187</a>
 */
@Entity
@Table(name="tipo_atividade_nao_sujeita_licenciamento")
@NamedQuery(name="TipoAtividadeNaoSujeitaLicenciamento.findAll", query="SELECT t FROM TipoAtividadeNaoSujeitaLicenciamento t")
public class TipoAtividadeNaoSujeitaLicenciamento implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "TIPO_ATIVIDADE_NAO_SUJEITA_LICENCIAMENTO_GENERATOR", sequenceName = "TIPO_ATIVIDADE_NAO_SUJEITA_LICENCIAMENTO_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TIPO_ATIVIDADE_NAO_SUJEITA_LICENCIAMENTO_GENERATOR")
	@Column(name="ide_tipo_atividade_nao_sujeita_licenciamento")
	private Integer ideTipoAtividadeNaoSujeitaLicenciamento;

	@Column(name="nom_atividade")
	private String nomAtividade;

	@Column(name="ind_ativo")
	private Boolean indAtivo;
	
	//bi-directional many-to-one association to AtividadeNaoSujeitaLicenciamentoDocumento
	@OneToMany(mappedBy="tipoAtividadeNaoSujeitaLicenciamento")
	private List<AtividadeNaoSujeitaLicenciamentoDocumento> atividadeNaoSujeitaLicenciamentoDocumentos;

	//bi-directional many-to-one association to CadastroAtividadeNaoSujeitaLic
	@OneToMany(mappedBy="tipoAtividadeNaoSujeitaLicenciamento")
	private List<CadastroAtividadeNaoSujeitaLic> cadastroAtividadeNaoSujeitaLics;

	public TipoAtividadeNaoSujeitaLicenciamento() {
	}

	public TipoAtividadeNaoSujeitaLicenciamento(TipoAtividadeNaoSujeitaLicenciamentoEnum atividadeNaoSujeitaLicenciamentoEnum) {
		this.ideTipoAtividadeNaoSujeitaLicenciamento = atividadeNaoSujeitaLicenciamentoEnum.getIde();
	}

	public Integer getIdeTipoAtividadeNaoSujeitaLicenciamento() {
		return this.ideTipoAtividadeNaoSujeitaLicenciamento;
	}

	public void setIdeTipoAtividadeNaoSujeitaLicenciamento(Integer ideTipoAtividadeNaoSujeitaLicenciamento) {
		this.ideTipoAtividadeNaoSujeitaLicenciamento = ideTipoAtividadeNaoSujeitaLicenciamento;
	}

	public String getNomAtividade() {
		return this.nomAtividade;
	}

	public void setNomAtividade(String nomAtividade) {
		this.nomAtividade = nomAtividade;
	}

	public List<AtividadeNaoSujeitaLicenciamentoDocumento> getAtividadeNaoSujeitaLicenciamentoDocumentos() {
		return this.atividadeNaoSujeitaLicenciamentoDocumentos;
	}

	public void setAtividadeNaoSujeitaLicenciamentoDocumentos(List<AtividadeNaoSujeitaLicenciamentoDocumento> atividadeNaoSujeitaLicenciamentoDocumentos) {
		this.atividadeNaoSujeitaLicenciamentoDocumentos = atividadeNaoSujeitaLicenciamentoDocumentos;
	}

	public AtividadeNaoSujeitaLicenciamentoDocumento addAtividadeNaoSujeitaLicenciamentoDocumento(AtividadeNaoSujeitaLicenciamentoDocumento atividadeNaoSujeitaLicenciamentoDocumento) {
		getAtividadeNaoSujeitaLicenciamentoDocumentos().add(atividadeNaoSujeitaLicenciamentoDocumento);
		atividadeNaoSujeitaLicenciamentoDocumento.setTipoAtividadeNaoSujeitaLicenciamento(this);

		return atividadeNaoSujeitaLicenciamentoDocumento;
	}

	public AtividadeNaoSujeitaLicenciamentoDocumento removeAtividadeNaoSujeitaLicenciamentoDocumento(AtividadeNaoSujeitaLicenciamentoDocumento atividadeNaoSujeitaLicenciamentoDocumento) {
		getAtividadeNaoSujeitaLicenciamentoDocumentos().remove(atividadeNaoSujeitaLicenciamentoDocumento);
		atividadeNaoSujeitaLicenciamentoDocumento.setTipoAtividadeNaoSujeitaLicenciamento(null);

		return atividadeNaoSujeitaLicenciamentoDocumento;
	}

	public List<CadastroAtividadeNaoSujeitaLic> getCadastroAtividadeNaoSujeitaLics() {
		return this.cadastroAtividadeNaoSujeitaLics;
	}

	public void setCadastroAtividadeNaoSujeitaLics(List<CadastroAtividadeNaoSujeitaLic> cadastroAtividadeNaoSujeitaLics) {
		this.cadastroAtividadeNaoSujeitaLics = cadastroAtividadeNaoSujeitaLics;
	}

	public CadastroAtividadeNaoSujeitaLic addCadastroAtividadeNaoSujeitaLic(CadastroAtividadeNaoSujeitaLic cadastroAtividadeNaoSujeitaLic) {
		getCadastroAtividadeNaoSujeitaLics().add(cadastroAtividadeNaoSujeitaLic);
		cadastroAtividadeNaoSujeitaLic.setTipoAtividadeNaoSujeitaLicenciamento(this);

		return cadastroAtividadeNaoSujeitaLic;
	}

	public CadastroAtividadeNaoSujeitaLic removeCadastroAtividadeNaoSujeitaLic(CadastroAtividadeNaoSujeitaLic cadastroAtividadeNaoSujeitaLic) {
		getCadastroAtividadeNaoSujeitaLics().remove(cadastroAtividadeNaoSujeitaLic);
		cadastroAtividadeNaoSujeitaLic.setTipoAtividadeNaoSujeitaLicenciamento(null);

		return cadastroAtividadeNaoSujeitaLic;
	}

	@Override
	public Long getId() {
		return new Long(this.ideTipoAtividadeNaoSujeitaLicenciamento);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideTipoAtividadeNaoSujeitaLicenciamento == null) ? 0 : ideTipoAtividadeNaoSujeitaLicenciamento.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TipoAtividadeNaoSujeitaLicenciamento other = (TipoAtividadeNaoSujeitaLicenciamento) obj;
		if (ideTipoAtividadeNaoSujeitaLicenciamento == null) {
			if (other.ideTipoAtividadeNaoSujeitaLicenciamento != null)
				return false;
		}
		else if (!ideTipoAtividadeNaoSujeitaLicenciamento.equals(other.ideTipoAtividadeNaoSujeitaLicenciamento))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TipoAtividadeNaoSujeitaLicenciamento [nomAtividade=" + nomAtividade + "]";
	}

	public Boolean getIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	
}