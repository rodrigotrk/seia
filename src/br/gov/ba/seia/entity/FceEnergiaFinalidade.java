package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@XmlRootElement
@Table(name="FCE_ENERGIA_FINALIDADE")
public class FceEnergiaFinalidade implements Serializable {
	

	private static final long serialVersionUID = 9083767876242195485L;
	
	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_energia_finalidade_seq")
	@SequenceGenerator(name = "fce_energia_finalidade_seq", sequenceName = "fce_energia_finalidade_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="IDE_FCE_ENERGIA_FINALIDADE")
	private Integer ideFceEnergiaFinalidade;

	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "IDE_FCE_ENERGIA")
	private FceEnergia ideFceEnergia;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "IDE_FINALIDADE_GERACAO_ENERGIA")
	private FinalidadeGeracaoEnergia finalidadeGeracaoEnergia;

	
	
	public Integer getIdeFceEnergiaFinalidade() {
		return ideFceEnergiaFinalidade;
	}

	public void setIdeFceEnergiaFinalidade(Integer ideFceEnergiaFinalidade) {
		this.ideFceEnergiaFinalidade = ideFceEnergiaFinalidade;
	}

	public FceEnergia getIdeFceEnergia() {
		return ideFceEnergia;
	}

	public void setIdeFceEnergia(FceEnergia ideFceEnergia) {
		this.ideFceEnergia = ideFceEnergia;
	}

	public FinalidadeGeracaoEnergia getFinalidadeGeracaoEnergia() {
		return finalidadeGeracaoEnergia;
	}

	public void setFinalidadeGeracaoEnergia(
			FinalidadeGeracaoEnergia finalidadeGeracaoEnergia) {
		this.finalidadeGeracaoEnergia = finalidadeGeracaoEnergia;
	}
	
}