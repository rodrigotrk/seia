package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "declaracao_transporte_residuo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DeclaracaoTransporteResiduo.findAll", query = "SELECT d FROM DeclaracaoTransporteResiduo d"),
})
public class DeclaracaoTransporteResiduo implements Serializable {

	private static final long serialVersionUID = 7651291624497718147L;

	@Id  
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DECLARACAO_TRANSPORTE_RESIDUO_SEQ")    
    @SequenceGenerator(name="DECLARACAO_TRANSPORTE_RESIDUO_SEQ", sequenceName="DECLARACAO_TRANSPORTE_RESIDUO_SEQ", allocationSize=1)
    @Basic(optional = false)
    @Column(name = "ide_declaracao_transporte_residuo", nullable = false)
    private Integer ideDeclaracaoTransporteResiduo;
	
	@JoinColumn(name = "ide_declaracao_transporte", referencedColumnName = "ide_declaracao_transporte")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private DeclaracaoTransporte declaracaoTransporte;
	
	@JoinColumn(name = "ide_residuo", referencedColumnName = "ide_residuo")
	@ManyToOne(optional = false)
	private Residuo residuo;
	
	@Column(name="qtd_transportada", nullable=false, scale=10, precision=2)
	private BigDecimal qtdTransportada;
	
	@JoinColumn(name = "ide_estado_fisico", referencedColumnName = "ide_estado_fisico")
	@ManyToOne(optional = false)
	private EstadoFisico estadoFisico;
	
	@JoinColumn(name = "ide_tipo_veiculo", referencedColumnName = "ide_tipo_veiculo")
	@ManyToOne(optional = false)
	private TipoVeiculo tipoVeiculo;
	
	@JoinColumn(name = "ide_acondicionamento", referencedColumnName = "ide_acondicionamento")
	@ManyToOne(optional = false)
	private Acondicionamento acondicionamento;

	@Transient
	private boolean outro;
	
	public Integer getIdeDeclaracaoTransporteResiduo() {
		return ideDeclaracaoTransporteResiduo;
	}

	public Residuo getResiduo() {
		return residuo;
	}

	public BigDecimal getQtdTransportada() {
		return qtdTransportada;
	}

	public EstadoFisico getEstadoFisico() {
		return estadoFisico;
	}

	public TipoVeiculo getTipoVeiculo() {
		return tipoVeiculo;
	}

	public Acondicionamento getAcondicionamento() {
		return acondicionamento;
	}

	public void setIdeDeclaracaoTransporteResiduo(
			Integer ideDeclaracaoTransporteResiduo) {
		this.ideDeclaracaoTransporteResiduo = ideDeclaracaoTransporteResiduo;
	}

	public void setResiduo(Residuo residuo) {
		this.residuo = residuo;
	}

	public void setQtdTransportada(BigDecimal qtdTransportada) {
		this.qtdTransportada = qtdTransportada;
	}

	public void setEstadoFisico(EstadoFisico estadoFisico) {
		this.estadoFisico = estadoFisico;
	}

	public void setTipoVeiculo(TipoVeiculo tipoVeiculo) {
		this.tipoVeiculo = tipoVeiculo;
	}

	public void setAcondicionamento(Acondicionamento acondicionamento) {
		this.acondicionamento = acondicionamento;
	}

	public boolean isOutro() {
		return outro;
	}

	public void setOutro(boolean outro) {
		this.outro = outro;
	}

	public DeclaracaoTransporte getDeclaracaoTransporte() {
		return declaracaoTransporte;
	}

	public void setDeclaracaoTransporte(DeclaracaoTransporte declaracaoTransporte) {
		this.declaracaoTransporte = declaracaoTransporte;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((declaracaoTransporte == null) ? 0 : declaracaoTransporte
						.hashCode());
		result = prime
				* result
				+ ((ideDeclaracaoTransporteResiduo == null) ? 0
						: ideDeclaracaoTransporteResiduo.hashCode());
		result = prime * result + ((residuo == null) ? 0 : residuo.hashCode());
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
		DeclaracaoTransporteResiduo other = (DeclaracaoTransporteResiduo) obj;
		if (declaracaoTransporte == null) {
			if (other.declaracaoTransporte != null)
				return false;
		} else if (!declaracaoTransporte.equals(other.declaracaoTransporte))
			return false;
		if (ideDeclaracaoTransporteResiduo == null) {
			if (other.ideDeclaracaoTransporteResiduo != null)
				return false;
		} else if (!ideDeclaracaoTransporteResiduo
				.equals(other.ideDeclaracaoTransporteResiduo))
			return false;
		if (residuo == null) {
			if (other.residuo != null)
				return false;
		} else if (!residuo.equals(other.residuo))
			return false;
		return true;
	}


	
}
