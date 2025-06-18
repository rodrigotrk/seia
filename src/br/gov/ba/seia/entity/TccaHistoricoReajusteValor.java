package br.gov.ba.seia.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name="tcca_historico_reajuste_valor")
@NamedQuery(name="TccaHistoricoReajusteValor.findAll", query="SELECT t FROM TccaHistoricoReajusteValor t")
public class TccaHistoricoReajusteValor extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TCCA_HISTORICO_REAJUSTE_VALOR_IDETCCAHISTORICOREAJUSTEVALOR_GENERATOR", sequenceName="TCCA_HISTORICO_REAJUSTE_VALOR_IDE_TCCA_HISTORICO_REAJUSTE_VALOR_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TCCA_HISTORICO_REAJUSTE_VALOR_IDETCCAHISTORICOREAJUSTEVALOR_GENERATOR")
	@Column(name="ide_tcca_historico_reajuste_valor", updatable=false, unique=true, nullable=false)
	private Integer ideTccaHistoricoReajusteValor;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dtc_reajuste", nullable=false)
	private Date dtcReajuste;

	/**
	 * Valor usado como porcentagem usada para o reajuste.
	 * */
	@Column(name="indice_reajuste", nullable=false, precision=20, scale=2)
	private BigDecimal indiceReajuste;
	
	/** 
	 * Valor do TCCA antes do reajuste.
	 * */
	@Column(name="val_tcca_anterior", nullable=false, precision=20, scale=2)
	private BigDecimal valTccaAnterior;
	
	/**
	 * Valor que será acrescido ao TCCA.
	 * */
	@Column(name="val_reajuste", nullable=false ,  precision=20, scale=2)
	private BigDecimal valReajuste;
	
	/** 
	 * Valor final do TCCA após o reajuste.
	 * */
	@Column(name="val_tcca", nullable=false, precision=20, scale=2)
	private BigDecimal valTcca;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tcca", nullable=false)
	private Tcca ideTcca;
	
	@OneToMany(mappedBy="ideTccaHistoricoReajusteValor")
	private List<MovimentacaoFinanceira> movimentacaoFinanceiraCollection;

	public TccaHistoricoReajusteValor() {
	}
	
	public TccaHistoricoReajusteValor(Tcca ideTcca) {
		this.ideTcca = ideTcca;
	}

	public Integer getIdeTccaHistoricoReajusteValor() {
		return this.ideTccaHistoricoReajusteValor;
	}

	public void setIdeTccaHistoricoReajusteValor(Integer ideTccaHistoricoReajusteValor) {
		this.ideTccaHistoricoReajusteValor = ideTccaHistoricoReajusteValor;
	}
	
	public BigDecimal getIndiceReajuste() {
		return indiceReajuste;
	}

	public void setIndiceReajuste(BigDecimal indiceReajuste) {
		this.indiceReajuste = indiceReajuste;
	}

	public Date getDtcReajuste() {
		return dtcReajuste;
	}

	public void setDtcReajuste(Date dtcReajuste) {
		this.dtcReajuste = dtcReajuste;
	}

	public BigDecimal getValReajuste() {
		return valReajuste;
	}

	public void setValReajuste(BigDecimal valReajuste) {
		this.valReajuste = valReajuste;
	}

	public BigDecimal getValTcca() {
		return valTcca;
	}

	public void setValTcca(BigDecimal valTcca) {
		this.valTcca = valTcca;
	}

	public List<MovimentacaoFinanceira> getMovimentacaoFinanceiraCollection() {
		return this.movimentacaoFinanceiraCollection;
	}

	public void setMovimentacaoFinanceiraCollection(List<MovimentacaoFinanceira> movimentacaoFinanceiraCollection) {
		this.movimentacaoFinanceiraCollection = movimentacaoFinanceiraCollection;
	}

	public Tcca getIdeTcca() {
		return this.ideTcca;
	}

	public void setIdeTcca(Tcca ideTcca) {
		this.ideTcca = ideTcca;
	}

	public BigDecimal getValTccaAnterior() {
		return valTccaAnterior;
	}

	public void setValTccaAnterior(BigDecimal valTccaAnterior) {
		this.valTccaAnterior = valTccaAnterior;
	}
}