package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The persistent class for the silvicultura database table.
 * 
 */
@Entity
@XmlRootElement
@Table(name="silvicultura")
@NamedQuery(name = "Silvicultura.deleteByideFceAgrossilvopastoril", query = "DELETE FROM Silvicultura s WHERE s.ideFceAgrossilvopastoril.ideFceAgrossilvopastoril = :ideFceAgrossilvopastoril")
public class Silvicultura implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "silvicultura_ide_silvicultura_generator")
	@SequenceGenerator(name = "silvicultura_ide_silvicultura_generator", sequenceName = "silvicultura_ide_silvicultura_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_silvicultura", nullable = false)
	private Integer ideSilvicultura;

	@Column(name="dtc_ano_previsao_corte", nullable = false)
	private Integer dtcAnoPrevisaoCorte;

	@Column(name="dtc_ano_previsao_plantio", nullable = false)
	private Integer dtcAnoPrevisaoPlantio;
	
	@JoinColumn(name = "dtc_mes_previsao_corte", referencedColumnName = "ide_mes", nullable = true)
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private Mes dtcMesPrevisaoCorte;

	@JoinColumn(name = "dtc_mes_previsao_plantio", referencedColumnName = "ide_mes", nullable = true)
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private Mes dtcMesPrevisaoPlantio;
	
	@JoinColumn(name = "ide_especie_hibrido", referencedColumnName = "ide_especie_hibrido", nullable = true)
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private EspecieHibrido ideEspecieHibrido;

	@JoinColumn(name = "ide_fce_agrossilvopastoril", referencedColumnName = "ide_fce_agrossilvopastoril", nullable = true)
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private FceAgrossilvopastoril ideFceAgrossilvopastoril;

	@JoinColumn(name = "ide_tipo_dado_silvicultura", referencedColumnName = "ide_tipo_dado_silvicultura", nullable = true)
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private TipoDadoSilvicultura ideTipoDadoSilvicultura;

	@Column(name="ind_floresta_implantada")
	private Boolean indFlorestaImplantada;

	@Column(name="ind_projeto")
	private Boolean indProjeto;

	@Column(name="num_area_total",precision = 10, scale = 2, nullable = true)
	private Double numAreaTotal;

	@Column(name="num_volume_final",precision = 10, scale = 2, nullable = true)
	private Double numVolumeFinal;
	
	//Indica no se a Silvicultura é do tipo Projeto o Floresta Implantada na aba de Silvicultura, EXCETO espécies ou hibridos.
	@Transient
	private String tipoProjetoOuFloresta;

	@Transient
	private Boolean desabilitado;
	
    public Silvicultura() {
    	
    }

	public Silvicultura(Boolean indFlorestaImplantada, Boolean indProjeto) {
		this.indFlorestaImplantada = indFlorestaImplantada;
		this.indProjeto = indProjeto;
	}

	public Silvicultura(TipoDadoSilvicultura ideTipoDadoSilvicultura, Boolean indFlorestaImplantada, Boolean indProjeto) {
		this.ideTipoDadoSilvicultura = ideTipoDadoSilvicultura;
		this.indFlorestaImplantada = indFlorestaImplantada;
		this.indProjeto = indProjeto;
	}

	public Silvicultura(TipoDadoSilvicultura ideTipoDadoSilvicultura,String tipoProjetoOuFloresta) {
		this.ideTipoDadoSilvicultura = ideTipoDadoSilvicultura;
		this.tipoProjetoOuFloresta = tipoProjetoOuFloresta;
	}

	public Silvicultura(EspecieHibrido ideEspecieHibrido) {
		this.ideEspecieHibrido = ideEspecieHibrido;
	}

	public Integer getIdeSilvicultura() {
		return this.ideSilvicultura;
	}

	public void setIdeSilvicultura(Integer ideSilvicultura) {
		this.ideSilvicultura = ideSilvicultura;
	}


	public Integer getDtcAnoPrevisaoCorte() {
		return dtcAnoPrevisaoCorte;
	}

	public void setDtcAnoPrevisaoCorte(Integer dtcAnoPrevisaoCorte) {
		this.dtcAnoPrevisaoCorte = dtcAnoPrevisaoCorte;
	}

	public Integer getDtcAnoPrevisaoPlantio() {
		return dtcAnoPrevisaoPlantio;
	}

	public void setDtcAnoPrevisaoPlantio(Integer dtcAnoPrevisaoPlantio) {
		this.dtcAnoPrevisaoPlantio = dtcAnoPrevisaoPlantio;
	}

	public Mes getDtcMesPrevisaoCorte() {
		return dtcMesPrevisaoCorte;
	}

	public void setDtcMesPrevisaoCorte(Mes dtcMesPrevisaoCorte) {
		this.dtcMesPrevisaoCorte = dtcMesPrevisaoCorte;
	}

	public Mes getDtcMesPrevisaoPlantio() {
		return dtcMesPrevisaoPlantio;
	}

	public void setDtcMesPrevisaoPlantio(Mes dtcMesPrevisaoPlantio) {
		this.dtcMesPrevisaoPlantio = dtcMesPrevisaoPlantio;
	}

	public EspecieHibrido getIdeEspecieHibrido() {
		return ideEspecieHibrido;
	}

	public void setIdeEspecieHibrido(EspecieHibrido ideEspecieHibrido) {
		this.ideEspecieHibrido = ideEspecieHibrido;
	}

	public FceAgrossilvopastoril getIdeFceAgrossilvopastoril() {
		return ideFceAgrossilvopastoril;
	}

	public void setIdeFceAgrossilvopastoril(
			FceAgrossilvopastoril ideFceAgrossilvopastoril) {
		this.ideFceAgrossilvopastoril = ideFceAgrossilvopastoril;
	}

	public TipoDadoSilvicultura getIdeTipoDadoSilvicultura() {
		return ideTipoDadoSilvicultura;
	}

	public void setIdeTipoDadoSilvicultura(
			TipoDadoSilvicultura ideTipoDadoSilvicultura) {
		this.ideTipoDadoSilvicultura = ideTipoDadoSilvicultura;
	}

	public Boolean getIndFlorestaImplantada() {
		return this.indFlorestaImplantada;
	}

	public void setIndFlorestaImplantada(Boolean indFlorestaImplantada) {
		this.indFlorestaImplantada = indFlorestaImplantada;
	}

	public Boolean getIndProjeto() {
		return this.indProjeto;
	}

	public void setIndProjeto(Boolean indProjeto) {
		this.indProjeto = indProjeto;
	}

	public Double getNumAreaTotal() {
		return numAreaTotal;
	}

	public void setNumAreaTotal(Double numAreaTotal) {
		this.numAreaTotal = numAreaTotal;
	}

	public Double getNumVolumeFinal() {
		return numVolumeFinal;
	}

	public void setNumVolumeFinal(Double numVolumeFinal) {
		this.numVolumeFinal = numVolumeFinal;
	}

	public String getTipoProjetoOuFloresta() {
		return tipoProjetoOuFloresta;
	}

	public void setTipoProjetoOuFloresta(String tipoProjetoOuFloresta) {
		this.tipoProjetoOuFloresta = tipoProjetoOuFloresta;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideEspecieHibrido == null) ? 0 : ideEspecieHibrido
						.hashCode());
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
		Silvicultura other = (Silvicultura) obj;
		if (ideEspecieHibrido == null) {
			if (other.ideEspecieHibrido != null)
				return false;
		} else if (!ideEspecieHibrido.equals(other.ideEspecieHibrido))
			return false;
		return true;
	}

	public Boolean getDesabilitado() {
		return desabilitado;
	}

	public void setDesabilitado(Boolean desabilitado) {
		this.desabilitado = desabilitado;
	}
	
	

}