package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.enumerator.FinalidadeReenquadramentoEnum;
import br.gov.ba.seia.util.Util;

@Entity
@Table(name = "reenquadramento_processo")
@NamedQueries({
	@NamedQuery(name = "ReenquadramentoProcesso.findAll", query = "SELECT m FROM ReenquadramentoProcesso m where m.indExcluido=false")
})
public class ReenquadramentoProcesso implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REENQUADRAMENTO_PROCESSO_IDEREENQUADRAMENTOPROCESSO_GENERATOR")
	@SequenceGenerator(name = "REENQUADRAMENTO_PROCESSO_IDEREENQUADRAMENTOPROCESSO_GENERATOR", sequenceName = "REENQUADRAMENTO_PROCESSO_SEQ", allocationSize=1)
	@Column(name = "ide_reenquadramento_processo")
	private Integer ideReenquadramentoProcesso;
	
	@Column(name = "ind_excluido", nullable = false)
	private boolean indExcluido;
	
	@JoinColumn(name = "ide_notificacao", referencedColumnName = "ide_notificacao", nullable = false)
	@OneToOne(optional = false, fetch = FetchType.EAGER)
	private Notificacao ideNotificacao;

	@JoinColumn(name = "ide_classe_empreendimento_inicial", referencedColumnName = "ide_classe")
	@ManyToOne(fetch = FetchType.EAGER)
	private Classe ideClasseEmpreendimentoInicial;

	@JoinColumn(name = "ide_nova_classe_empreendimento", referencedColumnName = "ide_classe")
	@ManyToOne(fetch = FetchType.EAGER)
	private Classe ideNovaClasseEmpreendimento;

	@JoinColumn(name = "ide_porte_empreendimento_inicial", referencedColumnName = "ide_porte")
	@ManyToOne(fetch = FetchType.EAGER)
	private Porte idePorteEmpreendimentoInicial;

	@JoinColumn(name = "ide_novo_porte_empreendimento", referencedColumnName = "ide_porte")
	@ManyToOne(fetch = FetchType.EAGER)
	private Porte ideNovoPorteEmpreendimento;

	@OneToMany(mappedBy = "ideReenquadramentoProcesso", fetch = FetchType.LAZY)
	private Collection<FinalidadeReenquadramentoProcesso> finalidadeReenquadramentoProcessoCollection;

	@OneToMany(mappedBy = "ideReenquadramentoProcesso", fetch = FetchType.LAZY)
	private Collection<ReenquadramentoProcessoAto> reenquadramentoProcessoAtoCollection;

	@OneToMany(mappedBy = "ideReenquadramentoProcesso", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Collection<ReenquadramentoPotencialPoluicao> reenquadramentoPotencialPoluicaoCollection;

	@OneToMany(mappedBy = "ideReenquadramentoProcesso", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Collection<ReenquadramentoTipologia> reenquadramentoTipologiaCollection;

	public ReenquadramentoProcesso(Integer ideReenquadramentoProcesso) {
		this.ideReenquadramentoProcesso = ideReenquadramentoProcesso;
	}
	
	public ReenquadramentoProcesso() {

	}

	public ReenquadramentoProcesso(Notificacao ideNotificacao) {
		this.ideNotificacao = ideNotificacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideReenquadramentoProcesso == null) ? 0 : ideReenquadramentoProcesso.hashCode());
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
		ReenquadramentoProcesso other = (ReenquadramentoProcesso) obj;
		if (ideReenquadramentoProcesso == null) {
			if (other.ideReenquadramentoProcesso != null)
				return false;
		} else if (!ideReenquadramentoProcesso.equals(other.ideReenquadramentoProcesso))
			return false;
		return true;
	}
	
	public boolean possuiFinalidade(FinalidadeReenquadramentoEnum finalidadeReenquadramentoEnum) {
		if(Util.isNullOuVazio(finalidadeReenquadramentoProcessoCollection)) {
			return false;
		}
		else{
			return finalidadeReenquadramentoProcessoCollection.contains(new FinalidadeReenquadramentoProcesso(this, finalidadeReenquadramentoEnum));
		}
	}
	
	public boolean possuiFinalidades() {
		return !Util.isNullOuVazio(finalidadeReenquadramentoProcessoCollection);
	}

	public Integer getIdeReenquadramentoProcesso() {
		return ideReenquadramentoProcesso;
	}

	public void setIdeReenquadramentoProcesso(Integer ideReenquadramentoProcesso) {
		this.ideReenquadramentoProcesso = ideReenquadramentoProcesso;
	}

	public Notificacao getIdeNotificacao() {
		return ideNotificacao;
	}

	public void setIdeNotificacao(Notificacao ideNotificacao) {
		this.ideNotificacao = ideNotificacao;
	}

	public Classe getIdeClasseEmpreendimentoInicial() {
		return ideClasseEmpreendimentoInicial;
	}

	public void setIdeClasseEmpreendimentoInicial(Classe ideClasseEmpreendimentoInicial) {
		this.ideClasseEmpreendimentoInicial = ideClasseEmpreendimentoInicial;
	}

	public Classe getIdeNovaClasseEmpreendimento() {
		return ideNovaClasseEmpreendimento;
	}

	public void setIdeNovaClasseEmpreendimento(Classe ideNovaClasseEmpreendimento) {
		this.ideNovaClasseEmpreendimento = ideNovaClasseEmpreendimento;
	}

	public Porte getIdePorteEmpreendimentoInicial() {
		return idePorteEmpreendimentoInicial;
	}

	public void setIdePorteEmpreendimentoInicial(Porte idePorteEmpreendimentoInicial) {
		this.idePorteEmpreendimentoInicial = idePorteEmpreendimentoInicial;
	}

	public Porte getIdeNovoPorteEmpreendimento() {
		return ideNovoPorteEmpreendimento;
	}

	public void setIdeNovoPorteEmpreendimento(Porte ideNovoPorteEmpreendimento) {
		this.ideNovoPorteEmpreendimento = ideNovoPorteEmpreendimento;
	}

	public Collection<FinalidadeReenquadramentoProcesso> getFinalidadeReequadramentoProcessoCollection() {
		return finalidadeReenquadramentoProcessoCollection;
	}

	public void setFinalidadeReequadramentoProcessoCollection(
			Collection<FinalidadeReenquadramentoProcesso> finalidadeReenquadramentoProcessoCollection) {
		this.finalidadeReenquadramentoProcessoCollection = finalidadeReenquadramentoProcessoCollection;
	}

	public Collection<ReenquadramentoProcessoAto> getReenquadramentoProcessoAtoCollection() {
		return reenquadramentoProcessoAtoCollection;
	}

	public void setReenquadramentoProcessoAtoCollection(
			Collection<ReenquadramentoProcessoAto> reenquadramentoProcessoAtoCollection) {
		this.reenquadramentoProcessoAtoCollection = reenquadramentoProcessoAtoCollection;
	}

	public Collection<ReenquadramentoPotencialPoluicao> getReenquadramentoPotencialPoluicaoCollection() {
		return reenquadramentoPotencialPoluicaoCollection;
	}

	public void setReenquadramentoPotencialPoluicaoCollection(
			Collection<ReenquadramentoPotencialPoluicao> reenquadramentoPotencialPoluicaoCollection) {
		this.reenquadramentoPotencialPoluicaoCollection = reenquadramentoPotencialPoluicaoCollection;
	}

	public Collection<ReenquadramentoTipologia> getReenquadramentoTipologiaCollection() {
		return reenquadramentoTipologiaCollection;
	}

	public void setReenquadramentoTipologiaCollection(
			Collection<ReenquadramentoTipologia> reenquadramentoTipologiaCollection) {
		this.reenquadramentoTipologiaCollection = reenquadramentoTipologiaCollection;
	}

}