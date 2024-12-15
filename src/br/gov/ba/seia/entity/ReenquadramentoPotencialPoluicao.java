package br.gov.ba.seia.entity;

import java.io.Serializable;

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

@Entity
@Table(name = "reenquadramento_potencial_poluicao")
@NamedQuery(name = "ReenquadramentoPotencialPoluicao.findAll", query = "SELECT r FROM ReenquadramentoPotencialPoluicao r")
public class ReenquadramentoPotencialPoluicao implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REENQUADRAMENTO_POTENCIAL_POLUICAO_IDEREENQUADRAMENTOPOTENCIALPOLUICAO_GENERATOR")
	@SequenceGenerator(name = "REENQUADRAMENTO_POTENCIAL_POLUICAO_IDEREENQUADRAMENTOPOTENCIALPOLUICAO_GENERATOR", sequenceName = "REENQUADRAMENTO_POTENCIAL_POLUICAO_SEQ", allocationSize=1)
	@Column(name = "ide_reenquadramento_potencial_poluicao")
	private Integer ideReenquadramentoPotencialPoluicao;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "ide_potencial_poluicao_inicial")
	private PotencialPoluicao idePotencialPoluicaoInicial;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "ide_potencial_poluicao_novo")
	private PotencialPoluicao idePotencialPoluicaoNovo;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "ide_requerimento_tipologia")
	private RequerimentoTipologia ideRequerimentoTipologia;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "ide_reenquadramento_processo")
	private ReenquadramentoProcesso ideReenquadramentoProcesso;

	public ReenquadramentoPotencialPoluicao() {
	}
	
	public ReenquadramentoPotencialPoluicao(ReenquadramentoProcesso ideReenquadramentoProcesso) {
		this.ideReenquadramentoProcesso = ideReenquadramentoProcesso;
	}

	public Integer getIdeReenquadramentoPotencialPoluicao() {
		return ideReenquadramentoPotencialPoluicao;
	}

	public void setIdeReenquadramentoPotencialPoluicao(Integer ideReenquadramentoPotencialPoluicao) {
		this.ideReenquadramentoPotencialPoluicao = ideReenquadramentoPotencialPoluicao;
	}

	public PotencialPoluicao getIdePotencialPoluicaoInicial() {
		return idePotencialPoluicaoInicial;
	}

	public void setIdePotencialPoluicaoInicial(PotencialPoluicao idePotencialPoluicaoInicial) {
		this.idePotencialPoluicaoInicial = idePotencialPoluicaoInicial;
	}

	public PotencialPoluicao getIdePotencialPoluicaoNovo() {
		return idePotencialPoluicaoNovo;
	}

	public void setIdePotencialPoluicaoNovo(PotencialPoluicao idePotencialPoluicaoNovo) {
		this.idePotencialPoluicaoNovo = idePotencialPoluicaoNovo;
	}

	public RequerimentoTipologia getIdeRequerimentoTipologia() {
		return ideRequerimentoTipologia;
	}

	public void setIdeRequerimentoTipologia(RequerimentoTipologia ideRequerimentoTipologia) {
		this.ideRequerimentoTipologia = ideRequerimentoTipologia;
	}

	public ReenquadramentoProcesso getIdeReenquadramentoProcesso() {
		return ideReenquadramentoProcesso;
	}

	public void setIdeReenquadramentoProcesso(ReenquadramentoProcesso ideReenquadramentoProcesso) {
		this.ideReenquadramentoProcesso = ideReenquadramentoProcesso;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideReenquadramentoPotencialPoluicao == null) ? 0 : ideReenquadramentoPotencialPoluicao.hashCode());
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
		ReenquadramentoPotencialPoluicao other = (ReenquadramentoPotencialPoluicao) obj;
		if (ideReenquadramentoPotencialPoluicao == null) {
			if (other.ideReenquadramentoPotencialPoluicao != null)
				return false;
		} else if (!ideReenquadramentoPotencialPoluicao.equals(other.ideReenquadramentoPotencialPoluicao))
			return false;
		return true;
	}
}