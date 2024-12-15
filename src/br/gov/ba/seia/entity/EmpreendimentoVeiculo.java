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
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;


/**
 * The persistent class for the empreendimento_veiculo database table.
 * 
 */
@Entity
@Table(name="empreendimento_veiculo")
@NamedQuery(name = "EmpreendimentoVeiculo.excluirByIdeRequerimento", query = "DELETE FROM EmpreendimentoVeiculo e WHERE e.ideEmpreendimento.ideEmpreendimento = :ideEmpreendimento")
public class EmpreendimentoVeiculo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "empreendimento_veiculo_ide_empreendimento_veiculo_generator")
	@SequenceGenerator(name = "empreendimento_veiculo_ide_empreendimento_veiculo_generator", sequenceName = "empreendimento_veiculo_ide_empreendimento_veiculo_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_empreendimento_veiculo",nullable = false)
	private Integer ideEmpreendimentoVeiculo;
	
	@Basic(optional = false)
	@Size(min = 0, max = 8)
	@Column(name="dsc_placa_carroceria",nullable = false)
	private String dscPlacaCarroceria;
	
	@Basic(optional = false)
	@Size(min = 0, max = 8)
	@Column(name="dsc_placa_cavalo_mecanico")
	private String dscPlacaCavaloMecanico;

	@Column(name="dtc_cadastro_veiculo",nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcCadastroVeiculo;

	@Column(name="dtc_exclusao_veiculo",nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcExclusaoVeiculo;

	@JoinColumn(name = "ide_empreendimento", referencedColumnName = "ide_empreendimento", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Empreendimento ideEmpreendimento;

	@JoinColumn(name="ide_tipo_veiculo", referencedColumnName = "ide_tipo_veiculo", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
	private TipoVeiculo ideTipoVeiculo;

    public EmpreendimentoVeiculo() {
    }

	public Integer getIdeEmpreendimentoVeiculo() {
		return this.ideEmpreendimentoVeiculo;
	}

	public void setIdeEmpreendimentoVeiculo(Integer ideEmpreendimentoVeiculo) {
		this.ideEmpreendimentoVeiculo = ideEmpreendimentoVeiculo;
	}

	public String getDscPlacaCarroceria() {
		return dscPlacaCarroceria;
	}

	public void setDscPlacaCarroceria(String dscPlacaCarroceria) {
		this.dscPlacaCarroceria = dscPlacaCarroceria;
	}

	public String getDscPlacaCavaloMecanico() {
		return  dscPlacaCavaloMecanico;
	}

	public void setDscPlacaCavaloMecanico(String dscPlacaCavaloMecanico) {
		this.dscPlacaCavaloMecanico = dscPlacaCavaloMecanico;
	}
	

	public Date getDtcCadastroVeiculo() {
		return dtcCadastroVeiculo;
	}

	public void setDtcCadastroVeiculo(Date dtcCadastroVeiculo) {
		this.dtcCadastroVeiculo = dtcCadastroVeiculo;
	}

	public Date getDtcExclusaoVeiculo() {
		return dtcExclusaoVeiculo;
	}

	public void setDtcExclusaoVeiculo(Date dtcExclusaoVeiculo) {
		this.dtcExclusaoVeiculo = dtcExclusaoVeiculo;
	}

	public Empreendimento getIdeEmpreendimento() {
		return ideEmpreendimento;
	}

	public void setIdeEmpreendimento(Empreendimento ideEmpreendimento) {
		this.ideEmpreendimento = ideEmpreendimento;
	}

	public TipoVeiculo getIdeTipoVeiculo() {
		return ideTipoVeiculo;
	}

	public void setIdeTipoVeiculo(TipoVeiculo ideTipoVeiculo) {
		this.ideTipoVeiculo = ideTipoVeiculo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideEmpreendimentoVeiculo == null) ? 0
						: ideEmpreendimentoVeiculo.hashCode());
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
		EmpreendimentoVeiculo other = (EmpreendimentoVeiculo) obj;
		if (ideEmpreendimentoVeiculo == null) {
			if (other.ideEmpreendimentoVeiculo != null)
				return false;
		} else if (!ideEmpreendimentoVeiculo
				.equals(other.ideEmpreendimentoVeiculo))
			return false;
		return true;
	}
	
}