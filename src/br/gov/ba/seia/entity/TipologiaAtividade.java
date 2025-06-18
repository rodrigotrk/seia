package br.gov.ba.seia.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.util.Util;

/**
 *
 * @author micael.coutinho
 */
@Entity
@Table(name = "tipologia_atividade")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "TipologiaAtividade.findAll", query = "SELECT t FROM TipologiaAtividade t"),
	@NamedQuery(name = "TipologiaAtividade.findByIdeTipologiaAtividade", query = "SELECT t FROM TipologiaAtividade t WHERE t.ideTipologiaAtividade = :ideTipologiaAtividade"),
	@NamedQuery(name = "TipologiaAtividade.findByDscTipologiaAtividade", query = "SELECT t FROM TipologiaAtividade t WHERE t.dscTipologiaAtividade = :dscTipologiaAtividade"),
	@NamedQuery(name = "TipologiaAtividade.findByIndExcluido", query = "SELECT t FROM TipologiaAtividade t WHERE t.indExcluido = :indExcluido"),
	@NamedQuery(name = "TipologiaAtividade.findByDtcCriacao", query = "SELECT t FROM TipologiaAtividade t WHERE t.dtcCriacao = :dtcCriacao"),
	@NamedQuery(name = "TipologiaAtividade.findByDtcExclusao", query = "SELECT t FROM TipologiaAtividade t WHERE t.dtcExclusao = :dtcExclusao")})
public class TipologiaAtividade extends AbstractEntity implements  Comparable<TipologiaAtividade> {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TIPOLOGIA_ATIVIDADE_IDE_TIPOLOGIA_ATIVIDADE_SEQ")
	@SequenceGenerator(name="TIPOLOGIA_ATIVIDADE_IDE_TIPOLOGIA_ATIVIDADE_SEQ", sequenceName="TIPOLOGIA_ATIVIDADE_IDE_TIPOLOGIA_ATIVIDADE_SEQ", allocationSize=1)
	@Column(name = "ide_tipologia_atividade", nullable = false)
	private Integer ideTipologiaAtividade;

	@Historico(name="Cultura Irrigada")
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "dsc_tipologia_atividade")
	private String dscTipologiaAtividade;

	@Basic(optional = false)
	@NotNull
	@Column(name = "ind_excluido")
	private boolean indExcluido;

	@Basic(optional = false)
	@NotNull
	@Column(name = "dtc_criacao")
	@Temporal(TemporalType.TIME)
	private Date dtcCriacao;

	@Column(name = "dtc_exclusao")
	@Temporal(TemporalType.TIME)
	private Date dtcExclusao;

	@JoinColumn(name = "ide_tipologia", referencedColumnName = "ide_tipologia")
	@ManyToOne(optional = false)
	private Tipologia ideTipologia;

	@Column(name = "num_consumo_diario", precision = 10, scale = 2, nullable = true)
	private BigDecimal numConsumoDiario;

	@Transient
	private List<UnidadeMedida> listaUnidade;

	public TipologiaAtividade() {
	}

	public TipologiaAtividade(Integer ideTipologiaAtividade) {
		this.ideTipologiaAtividade = ideTipologiaAtividade;
	}

	public TipologiaAtividade(Integer ideTipologiaAtividade, String dscTipologiaAtividade, boolean indExcluido, Date dtcCriacao) {
		this.ideTipologiaAtividade = ideTipologiaAtividade;
		this.dscTipologiaAtividade = dscTipologiaAtividade;
		this.indExcluido = indExcluido;
		this.dtcCriacao = dtcCriacao;
	}

	public TipologiaAtividade(String dscTipologiaAtividade) {
		this.dscTipologiaAtividade = dscTipologiaAtividade;
	}

	public Integer getIdeTipologiaAtividade() {
		return ideTipologiaAtividade;
	}

	public void setIdeTipologiaAtividade(Integer ideTipologiaAtividade) {
		this.ideTipologiaAtividade = ideTipologiaAtividade;
	}

	public String getDscTipologiaAtividade() {
		return dscTipologiaAtividade;
	}

	public void setDscTipologiaAtividade(String dscTipologiaAtividade) {
		this.dscTipologiaAtividade = dscTipologiaAtividade;
	}

	public boolean getIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	public Date getDtcCriacao() {
		return dtcCriacao;
	}

	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}

	public Date getDtcExclusao() {
		return dtcExclusao;
	}

	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}

	public Tipologia getIdeTipologia() {
		return ideTipologia;
	}

	public void setIdeTipologia(Tipologia ideTipologia) {
		this.ideTipologia = ideTipologia;
	}

	public List<UnidadeMedida> getListaUnidade() {
		return listaUnidade;
	}

	public void setListaUnidade(List<UnidadeMedida> listaUnidade) {
		this.listaUnidade = listaUnidade;
	}

	@Override
	public int compareTo(TipologiaAtividade arg0) {
		if(Util.isNull(arg0) || Util.isNull(dscTipologiaAtividade) || Util.isNull( arg0.dscTipologiaAtividade)) {
			return 0;
		}
		return arg0.dscTipologiaAtividade.compareTo(dscTipologiaAtividade) * -1;
	}

	public BigDecimal getNumConsumoDiario() {
		return numConsumoDiario;
	}

	public void setNumConsumoDiario(BigDecimal numConsumoDiario) {
		this.numConsumoDiario = numConsumoDiario;
	}

	public boolean isOutros(){
		return dscTipologiaAtividade.compareTo("Outros") == 0;
	}
}
