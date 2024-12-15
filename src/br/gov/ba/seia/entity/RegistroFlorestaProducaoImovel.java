package br.gov.ba.seia.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.entity.generic.AbstractEntity;

/**
 * @author Alexandre Queiroz
 * @since 15/12/2016 
 */

@Entity
@Table(name="registro_floresta_producao_imovel")
public class RegistroFlorestaProducaoImovel extends AbstractEntity{
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="registro_floresta_producao_imovel_seq", sequenceName="registro_floresta_producao_imovel_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="registro_floresta_producao_imovel_seq")
	@Column(name="ide_registro_floresta_producao_imovel", unique=true, nullable=false)
	private Integer ideRegistroFlorestaProducaoImovel;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_imovel", nullable=false)	
	private Imovel ideImovel;
	
	@Column(name="ind_arrendado", nullable=false)
	private Boolean indArrendado;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_registro_floresta_producao", nullable=false)
	private RegistroFlorestaProducao ideRegistroFlorestaProducao;

	@OneToMany(mappedBy="ideRegistroFlorestaProducaoImovel", fetch=FetchType.LAZY)
	private List<RegistroFlorestaProducaoImovelPlantio> registroFlorestaProducaoImovelPlantioList;
	
	@OneToMany(mappedBy="ideRegistroFlorestaProducaoImovel" , fetch=FetchType.LAZY)
	private List<RegistroFlorestaProducaoImovelEspecieProducao> registroFlorestaProducaoImovelEspecieProducaoList;
	
	@Transient
	private RegistroFlorestaProducaoImovelPlantio ideRegistroFlorestaProducaoImovelPlantio;
	
	@Transient
	private RegistroFlorestaProducaoImovelEspecieProducao ideRegistroFlorestaProducaoImovelEspecieProducao;
	
	@Transient
	private BigDecimal areaTotalPlantio;
	
	public void atualizarAreasPlantio(){
		BigDecimal areaPlantioTotal = BigDecimal.ZERO;
		for (RegistroFlorestaProducaoImovelPlantio plantio : registroFlorestaProducaoImovelPlantioList) {
			if(plantio.getValAreaPlantio()!=null){
				areaPlantioTotal = areaPlantioTotal.add(plantio.getValAreaPlantio());
			}
		}
		this.areaTotalPlantio = areaPlantioTotal;
	}
	
	public RegistroFlorestaProducaoImovel() {
	}
	
	public Integer getIdeRegistroFlorestaProducaoImovel() {
		return ideRegistroFlorestaProducaoImovel;
	}

	public void setIdeRegistroFlorestaProducaoImovel(Integer ideRegistroFlorestaProducaoImovel) {
		this.ideRegistroFlorestaProducaoImovel = ideRegistroFlorestaProducaoImovel;
	}

	public Imovel getIdeImovel() {
		return ideImovel;
	}

	public void setIdeImovel(Imovel ideImovel) {
		this.ideImovel = ideImovel;
	}

	public Boolean getIndArrendado() {
		return indArrendado;
	}

	public void setIndArrendado(Boolean indArrendado) {
		this.indArrendado = indArrendado;
	}

	public RegistroFlorestaProducao getIdeRegistroFlorestaProducao() {
		return ideRegistroFlorestaProducao;
	}

	public void setIdeRegistroFlorestaProducao(RegistroFlorestaProducao ideRegistroFlorestaProducao) {
		this.ideRegistroFlorestaProducao = ideRegistroFlorestaProducao;
	}

	public List<RegistroFlorestaProducaoImovelPlantio> getRegistroFlorestaProducaoImovelPlantioList() {
		return registroFlorestaProducaoImovelPlantioList;
	}

	public void setRegistroFlorestaProducaoImovelPlantioList(List<RegistroFlorestaProducaoImovelPlantio> registroFlorestaProducaoImovelPlantioList) {
		this.registroFlorestaProducaoImovelPlantioList = registroFlorestaProducaoImovelPlantioList;
	}

	public List<RegistroFlorestaProducaoImovelEspecieProducao> getRegistroFlorestaProducaoImovelEspecieProducaoList() {
		return registroFlorestaProducaoImovelEspecieProducaoList;
	}

	public void setRegistroFlorestaProducaoImovelEspecieProducaoList(List<RegistroFlorestaProducaoImovelEspecieProducao> registroFlorestaProducaoImovelEspecieProducaoList) {
		this.registroFlorestaProducaoImovelEspecieProducaoList = registroFlorestaProducaoImovelEspecieProducaoList;
	}

	public RegistroFlorestaProducaoImovelPlantio getIdeRegistroFlorestaProducaoImovelPlantio() {
		return ideRegistroFlorestaProducaoImovelPlantio;
	}

	public void setIdeRegistroFlorestaProducaoImovelPlantio(RegistroFlorestaProducaoImovelPlantio ideRegistroFlorestaProducaoImovelPlantio) {
		this.ideRegistroFlorestaProducaoImovelPlantio = ideRegistroFlorestaProducaoImovelPlantio;
	}

	public RegistroFlorestaProducaoImovelEspecieProducao getIdeRegistroFlorestaProducaoImovelEspecieProducao() {
		return ideRegistroFlorestaProducaoImovelEspecieProducao;
	}

	public void setIdeRegistroFlorestaProducaoImovelEspecieProducao(RegistroFlorestaProducaoImovelEspecieProducao ideRegistroFlorestaProducaoImovelEspecieProducao) {
		this.ideRegistroFlorestaProducaoImovelEspecieProducao = ideRegistroFlorestaProducaoImovelEspecieProducao;
	}

	public BigDecimal getAreaTotalPlantio() {
		return areaTotalPlantio;
	}

	public void setAreaTotalPlantio(BigDecimal areaTotalPlantio) {
		this.areaTotalPlantio = areaTotalPlantio;
	}
}