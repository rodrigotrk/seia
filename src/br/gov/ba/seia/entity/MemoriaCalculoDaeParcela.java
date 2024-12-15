package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="memoria_calculo_dae_parcela")
@XmlRootElement
public class MemoriaCalculoDaeParcela implements Serializable {

	private static final long serialVersionUID = -8720856134403762086L;

	@Id
	@SequenceGenerator(name="MEMORIA_CALCULO_DAE_SEQ", sequenceName="MEMORIA_CALCULO_DAE_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MEMORIA_CALCULO_DAE_SEQ")
	@Basic(optional = false)
	@NotNull
	@Column(name="ide_memoria_calculo_dae_parcela")
	private Integer ideMemoriaCalculoDaeParcela;

	@JoinColumn(name = "ide_memoria_calculo_dae", referencedColumnName = "ide_memoria_calculo_dae")
	@ManyToOne(fetch=FetchType.LAZY, optional = false)
	private MemoriaCalculoDae ideMemoriaCalculoDae;
	
	@JoinColumn(name = "ide_dae", referencedColumnName = "ide_dae", nullable=true)
	@ManyToOne(optional = false)
	private Dae ideDae;
	
	@Column(name = "valor", nullable = false, precision = 12, scale = 2)
	private Double valor;
	
	@Column(name = "num_parcela_referencia", nullable = false)
	private Integer numParcelaReferencia;
	
	@Column(name = "dtc_exclusao", nullable = true)
	private Date dtcExclusao;
	
	@Column(name = "ind_excluido", nullable = false)
	private Boolean indExcluido;
	
	@Column(name = "ind_parcela_unica", nullable = true)
	private Boolean indParcelaUnica;
	
	public MemoriaCalculoDaeParcela() {}
	
	public MemoriaCalculoDaeParcela(Integer ideMemoriaCalculoDaeParcela) {
		this.ideMemoriaCalculoDaeParcela = ideMemoriaCalculoDaeParcela;
	}

	public Integer getIdeMemoriaCalculoDaeParcela() {
		return ideMemoriaCalculoDaeParcela;
	}

	public void setIdeMemoriaCalculoDaeParcela(Integer ideMemoriaCalculoDaeParcela) {
		this.ideMemoriaCalculoDaeParcela = ideMemoriaCalculoDaeParcela;
	}

	public MemoriaCalculoDae getIdeMemoriaCalculoDae() {
		return ideMemoriaCalculoDae;
	}

	public void setIdeMemoriaCalculoDae(MemoriaCalculoDae ideMemoriaCalculoDae) {
		this.ideMemoriaCalculoDae = ideMemoriaCalculoDae;
	}

	public Dae getIdeDae() {
		return ideDae;
	}

	public void setIdeDae(Dae ideDae) {
		this.ideDae = ideDae;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Integer getNumParcelaReferencia() {
		return numParcelaReferencia;
	}

	public void setNumParcelaReferencia(Integer numParcelaReferencia) {
		this.numParcelaReferencia = numParcelaReferencia;
	}

	public Date getDtcExclusao() {
		return dtcExclusao;
	}

	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}

	public Boolean getIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(Boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	public Boolean getIndParcelaUnica() {
		return indParcelaUnica;
	}

	public void setIndParcelaUnica(Boolean indParcelaUnica) {
		this.indParcelaUnica = indParcelaUnica;
	}
	
	
}
