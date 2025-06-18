package br.gov.ba.seia.entity;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.gov.ba.seia.entity.generic.AbstractEntityLocalizacaoGeografica;

/**
 * @author Alexandre Queiroz
 * @since 15/12/2016 
 */

@Entity
@Table(name="registro_floresta_producao_imovel_plantio")
public class RegistroFlorestaProducaoImovelPlantio extends AbstractEntityLocalizacaoGeografica{
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="registro_floresta_producao_imovel_plantio_seq", sequenceName="registro_floresta_producao_imovel_plantio_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="registro_floresta_producao_imovel_plantio_seq")
	@Column(name="ide_registro_floresta_imovel_plantio", unique=true, nullable=false)
	private Integer ideRegistroFlorestaImovelPlantio;

	@Temporal(TemporalType.DATE)
	@Column(name="dt_fim_periodo_implantacao")
	private Date dtFimPeriodoImplantacao;

	@Temporal(TemporalType.DATE)
	@Column(name="dt_fim_prevista_implantacao")
	private Date dtFimPrevistaImplantacao;
	
	@Temporal(TemporalType.DATE)
	@Column(name="dt_inicio_periodo_implantacao")
	private Date dtInicioPeriodoImplantacao;

	@Temporal(TemporalType.DATE)
	@Column(name="dt_inicio_prevista_implantacao")
	private Date dtInicioPrevistaImplantacao;

	@Column(name="val_area_plantio", nullable=false, precision=13, scale=4)
	private BigDecimal valAreaPlantio;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_localizacao_geografica")
	private LocalizacaoGeografica ideLocalizacaoGeografica;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_situacao_atual_floresta_producao", nullable=false)
	private SituacaoAtualFlorestaProducao ideSituacaoAtualFlorestaProducao;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_registro_floresta_producao_imovel", nullable=false)
	private RegistroFlorestaProducaoImovel ideRegistroFlorestaProducaoImovel;

	
	public RegistroFlorestaProducaoImovelPlantio() {
		this.ideLocalizacaoGeografica = new LocalizacaoGeografica();
		this.ideLocalizacaoGeografica.setVlrArea(BigDecimal.ZERO);
	}

	public Integer getIdeRegistroFlorestaImovelPlantio() {
		return this.ideRegistroFlorestaImovelPlantio;
	}

	public void setIdeRegistroFlorestaImovelPlantio(Integer ideRegistroFlorestaImovelPlantio) {
		this.ideRegistroFlorestaImovelPlantio = ideRegistroFlorestaImovelPlantio;
	}

	public Date getDtFimPeriodoImplantacao() {
		return this.dtFimPeriodoImplantacao;
	}

	public void setDtFimPeriodoImplantacao(Date dtFimPeriodoImplantacao) {
		this.dtFimPeriodoImplantacao = dtFimPeriodoImplantacao;
	}

	public Date getDtFimPrevistaImplantacao() {
		return this.dtFimPrevistaImplantacao;
	}

	public void setDtFimPrevistaImplantacao(Date dtFimPrevistaImplantacao) {
		this.dtFimPrevistaImplantacao = dtFimPrevistaImplantacao;
	}

	public Date getDtInicioPeriodoImplantacao() {
		return this.dtInicioPeriodoImplantacao;
	}

	public void setDtInicioPeriodoImplantacao(Date dtInicioPeriodoImplantacao) {
		this.dtInicioPeriodoImplantacao = dtInicioPeriodoImplantacao;
	}

	public Date getDtInicioPrevistaImplantacao() {
		return this.dtInicioPrevistaImplantacao;
	}

	public void setDtInicioPrevistaImplantacao(Date dtInicioPrevistaImplantacao) {
		this.dtInicioPrevistaImplantacao = dtInicioPrevistaImplantacao;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public SituacaoAtualFlorestaProducao getIdeSituacaoAtualFlorestaProducao() {
		return ideSituacaoAtualFlorestaProducao;
	}

	public void setIdeSituacaoAtualFlorestaProducao(SituacaoAtualFlorestaProducao ideSituacaoAtualFlorestaProducao) {
		this.ideSituacaoAtualFlorestaProducao = ideSituacaoAtualFlorestaProducao;
	}

	public BigDecimal getValAreaPlantio() {
		return this.valAreaPlantio;
	}

	public void setValAreaPlantio(BigDecimal valAreaPlantio) {
		this.valAreaPlantio = valAreaPlantio;
	}

	public RegistroFlorestaProducaoImovel getIdeRegistroFlorestaProducaoImovel() {
		return ideRegistroFlorestaProducaoImovel;
	}

	public void setIdeRegistroFlorestaProducaoImovel(RegistroFlorestaProducaoImovel ideRegistroFlorestaProducaoImovel) {
		this.ideRegistroFlorestaProducaoImovel = ideRegistroFlorestaProducaoImovel;
	}

	@Transient
	public String getPeriodo() {

		StringBuilder periodo = new StringBuilder();
												
		if(dtInicioPeriodoImplantacao!=null && dtFimPeriodoImplantacao!=null){
			periodo
				.append(new SimpleDateFormat("dd/MM/yyyy").format(dtInicioPeriodoImplantacao)).append(" a ")
				.append(new SimpleDateFormat("dd/MM/yyyy").format(dtFimPeriodoImplantacao));
			
		}
		else if (dtInicioPrevistaImplantacao!=null && dtFimPrevistaImplantacao!=null){
			periodo
				.append(new SimpleDateFormat("dd/MM/yyyy").format(dtInicioPrevistaImplantacao)).append(" a ")
				.append(new SimpleDateFormat("dd/MM/yyyy").format(dtFimPrevistaImplantacao));
		}
		
		return periodo.toString();
	}

	@Override
	public List<LocalizacaoGeografica> getLocalizacoesGeograficas() {
		return Arrays.asList(ideLocalizacaoGeografica);
	}

}