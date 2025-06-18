package br.gov.ba.seia.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.entity.generic.AbstractEntityHist;
import br.gov.ba.seia.interfaces.CerhFinalidadeUsoAguaInterface;

@Entity
@Table(name="cerh_barragem_caracterizacao_finalidade")
public class CerhBarragemCaracterizacaoFinalidade extends AbstractEntityHist implements CerhFinalidadeUsoAguaInterface  {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cerh_barragem_caracterizacao_finalidade_seq")
	@SequenceGenerator(name = "cerh_barragem_caracterizacao_finalidade_seq", sequenceName = "cerh_barragem_caracterizacao_finalidade_seq", allocationSize = 1)
	@Column(name="ide_cerh_barragem_caracterizacao_finalidade")
	private Integer ideCerhBarragemCaracterizacaoFinalidade;

	@Historico
	@ManyToOne
	@JoinColumn(name="ide_tipo_finalidade_uso_agua")
	private TipoFinalidadeUsoAgua ideTipoFinalidadeUsoAgua;

	@ManyToOne
	@JoinColumn(name="ide_cerh_barragem_caracterizacao")
	private CerhBarragemCaracterizacao ideCerhBarragemCaracterizacao;

	@Historico
	@OneToOne(mappedBy="ideCerhBarragemCaracterizacaoFinalidade", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private CerhBarragemAproveitamentoHidreletrico ideCerhBarragemAproveitamentoHidreletrico;

	public CerhBarragemCaracterizacaoFinalidade() {
	}

	public CerhBarragemCaracterizacaoFinalidade(CerhBarragemCaracterizacao cerhCaracterizacao, TipoFinalidadeUsoAgua tipoFinalidadeUsoAgua) {
		this.ideCerhBarragemCaracterizacao = cerhCaracterizacao;
		this.ideTipoFinalidadeUsoAgua = tipoFinalidadeUsoAgua;
	}

	public Integer getIdeCerhBarragemCaracterizacaoFinalidade() {
		return ideCerhBarragemCaracterizacaoFinalidade;
	}

	public void setIdeCerhBarragemCaracterizacaoFinalidade(Integer ideCerhBarragemCaracterizacaoFinalidade) {
		this.ideCerhBarragemCaracterizacaoFinalidade = ideCerhBarragemCaracterizacaoFinalidade;
	}

	public TipoFinalidadeUsoAgua getIdeTipoFinalidadeUsoAgua() {
		return ideTipoFinalidadeUsoAgua;
	}

	public void setIdeTipoFinalidadeUsoAgua(TipoFinalidadeUsoAgua ideTipoFinalidadeUsoAgua) {
		this.ideTipoFinalidadeUsoAgua = ideTipoFinalidadeUsoAgua;
	}

	public CerhBarragemCaracterizacao getIdeCerhBarragemCaracterizacao() {
		return ideCerhBarragemCaracterizacao;
	}

	public void setIdeCerhBarragemCaracterizacao(CerhBarragemCaracterizacao ideCerhBarragemCaracterizacao) {
		this.ideCerhBarragemCaracterizacao = ideCerhBarragemCaracterizacao;
	}

	public CerhBarragemAproveitamentoHidreletrico getIdeCerhBarragemAproveitamentoHidreletrico() {
		return ideCerhBarragemAproveitamentoHidreletrico;
	}

	public void setIdeCerhBarragemAproveitamentoHidreletrico(CerhBarragemAproveitamentoHidreletrico cerhBarragemAproveitamentoHidreletricoCollection) {
		this.ideCerhBarragemAproveitamentoHidreletrico = cerhBarragemAproveitamentoHidreletricoCollection;
	}
}