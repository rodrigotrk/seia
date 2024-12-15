package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.ba.seia.enumerator.TipoCertificadoEnum;
import br.gov.ba.seia.util.Util;

/**
 * The persistent class for the Declaracao_parcial_dae database table.
 * 
 */
@Table(name = "declaracao_parcial_dae")
@Entity
public class DeclaracaoParcialDae implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "declaracao_parcial_dae_ide_generator", sequenceName = "declaracao_parcial_dae_ide_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "declaracao_parcial_dae_ide_generator")
	@Column(name = "ide_declaracao_parcial_dae")
	private Integer ideDeclaracaoParcialDae;

	@Column(name = "dtc_emissao_declaracao_parcial_dae")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcEmissaoDeclaracaoParcialDae;

	@JoinColumn(name = "ide_ato_ambiental", referencedColumnName = "ide_ato_ambiental")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private AtoAmbiental ideAtoAmbiental;

	@JoinColumn(name = "ide_orgao", referencedColumnName = "ide_orgao", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Orgao ideOrgao;

	@JoinColumn(name = "ide_requerimento", referencedColumnName = "ide_requerimento", nullable = false)
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	private Requerimento requerimento;
	
	@JoinColumn(name = "ide_memoria_calculo_dae_parcela", referencedColumnName = "ide_memoria_calculo_dae_parcela", nullable = false)
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	private MemoriaCalculoDaeParcela ideMemoriaCalculoDaeParcela;

	@Column(name = "num_declaracao_parcial_dae")
	private String numDeclaracaoParcialDae;

	@Column(name = "num_token")
	private String numToken;

	@JoinColumn(name = "ide_tipo_certificado", referencedColumnName = "ide_tipo_certificado", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private TipoCertificado tipoCertificado;
	
	public DeclaracaoParcialDae() {
	}

	public Integer getIdeDeclaracaoParcialDae() {
		return this.ideDeclaracaoParcialDae;
	}

	public void setIdeDeclaracaoParcialDae(Integer ideDeclaracaoParcialDae) {
		this.ideDeclaracaoParcialDae = ideDeclaracaoParcialDae;
	}

	public Date getDtcEmissaoDeclaracaoParcialDae() {
		return dtcEmissaoDeclaracaoParcialDae;
	}

	public void setDtcEmissaoDeclaracaoParcialDae(Date dtcEmissaoDeclaracaoParcialDae) {
		this.dtcEmissaoDeclaracaoParcialDae = dtcEmissaoDeclaracaoParcialDae;
	}

	public AtoAmbiental getIdeAtoAmbiental() {
		return ideAtoAmbiental;
	}

	public void setIdeAtoAmbiental(AtoAmbiental ideAtoAmbiental) {
		this.ideAtoAmbiental = ideAtoAmbiental;
	}

	public Orgao getIdeOrgao() {
		return ideOrgao;
	}

	public void setIdeOrgao(Orgao ideOrgao) {
		this.ideOrgao = ideOrgao;
	}

	public Requerimento getRequerimento() {
		return requerimento;
	}

	public void setRequerimento(Requerimento requerimento) {
		this.requerimento = requerimento;
	}

	public String getNumDeclaracaoParcialDae() {
		return this.numDeclaracaoParcialDae;
	}

	public void setNumDeclaracaoParcialDae(String numDeclaracaoParcialDae) {
		this.numDeclaracaoParcialDae = numDeclaracaoParcialDae;
	}

	public String getNumToken() {
		return numToken;
	}

	public void setNumToken(String numToken) {
		this.numToken = numToken;
	}

	public TipoCertificado getTipoCertificado() {
		return tipoCertificado;
	}

	public void setTipoCertificado(TipoCertificado tipoCertificado) {
		this.tipoCertificado = tipoCertificado;
	}
		
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideAtoAmbiental == null) ? 0 : ideAtoAmbiental.hashCode());
		result = prime * result + ((requerimento == null) ? 0 : requerimento.hashCode());
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
		DeclaracaoParcialDae other = (DeclaracaoParcialDae) obj;
		if (ideAtoAmbiental == null) {
			if (other.ideAtoAmbiental != null)
				return false;
		} else if (!ideAtoAmbiental.equals(other.ideAtoAmbiental))
			return false;
		if (requerimento == null) {
			if (other.requerimento != null)
				return false;
		} else if (!requerimento.equals(other.requerimento))
			return false;
		return true;
	}
	
	public boolean isTermoCompromisso(){
		return !Util.isNull(this.tipoCertificado) && !Util.isNull(this.tipoCertificado.getIdeTipoCertificado())  
				&& TipoCertificadoEnum.TERMO_DE_COMPROMISSO.getId().equals(this.tipoCertificado.getIdeTipoCertificado());
	}

	public MemoriaCalculoDaeParcela getIdeMemoriaCalculoDaeParcela() {
		return ideMemoriaCalculoDaeParcela;
	}

	public void setIdeMemoriaCalculoDaeParcela(
			MemoriaCalculoDaeParcela ideMemoriaCalculoDaeParcela) {
		this.ideMemoriaCalculoDaeParcela = ideMemoriaCalculoDaeParcela;
	}

}