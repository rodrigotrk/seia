package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.interfaces.BaseEntity;
import br.gov.ba.seia.util.Util;

@Entity
@Table(name = "reenquadramento_processo_ato")
@NamedQuery(name = "ReenquadramentoProcessoAto.findAll", query = "SELECT r FROM ReenquadramentoProcessoAto r")
public class ReenquadramentoProcessoAto implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REENQUADRAMENTO_PROCESSO_ATO_IDEREENQUADRAMENTOPROCESSOATO_GENERATOR")
	@SequenceGenerator(name = "REENQUADRAMENTO_PROCESSO_ATO_IDEREENQUADRAMENTOPROCESSOATO_GENERATOR", sequenceName = "REENQUADRAMENTO_PROCESSO_ATO_SEQ", allocationSize = 1)
	@Column(name = "ide_reenquadramento_processo_ato")
	private Integer ideReenquadramentoProcessoAto;

	@JoinColumn(name = "ide_nova_tipologia", referencedColumnName = "ide_tipologia")
	@ManyToOne(fetch = FetchType.LAZY)
	private Tipologia ideNovaTipologia;

	@JoinColumn(name = "ide_novo_ato_ambiental", referencedColumnName = "ide_ato_ambiental")
	@ManyToOne(fetch = FetchType.LAZY)
	private AtoAmbiental ideNovoAtoAmbiental;
	
	@Transient
	private String dscJustificativa;

	@JoinColumn(name = "ide_processo_ato", referencedColumnName = "ide_processo_ato", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private ProcessoAto ideProcessoAto;

	@JoinColumn(name = "ide_reenquadramento_processo", referencedColumnName = "ide_reenquadramento_processo")
	@ManyToOne(fetch = FetchType.LAZY)
	private ReenquadramentoProcesso ideReenquadramentoProcesso;

	@MapsId("reenquadramentoTipologiaEmpreendimentoPK.ideReenquadramentoProcessoAto")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "reenquadramentoTipologiaEmpreendimentoPK.ideReenquadramentoProcessoAto")
	private Collection<ReenquadramentoTipologiaEmpreendimento> reenquadramentoTipologiaEmpreendimentoCollection;
	  
	@MapsId("reenquadramentoTipoFinalidadeUsoAguaPK.ideReenquadramentoProcessoAto")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "reenquadramentoTipoFinalidadeUsoAguaPK.ideReenquadramentoProcessoAto")
	private Collection<ReenquadramentoTipoFinalidadeUsoAgua> reenquadramentoTipoFinalidadeUsoAguaCollection;
	
	@MapsId("reenquadramentoProcessoAtoObjetivoAtividadeManejoPK.ideReenquadramentoProcessoAto")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "reenquadramentoProcessoAtoObjetivoAtividadeManejoPK.ideReenquadramentoProcessoAto")
	private Collection<ReenquadramentoProcessoAtoObjetivoAtividadeManejo> reenquadramentoProcessoAtoObjetivoAtividadeManejoCollection;
	
	@MapsId("reenquadramentoProcessoAtoTipoAtividadeFaunaPK.ideReenquadramentoProcessoAto")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "reenquadramentoProcessoAtoTipoAtividadeFaunaPK.ideReenquadramentoProcessoAto")
	private Collection<ReenquadramentoProcessoAtoTipoAtividadeFauna> reenquadramentoProcessoAtoTipoAtividadeFaunaCollection;

	@Transient
	private boolean isAlteracao;
	
	public ReenquadramentoProcessoAto() {
	}

	public Tipologia getIdeNovaTipologia() {
		return ideNovaTipologia;
	}

	public void setIdeNovaTipologia(Tipologia ideNovaTipologia) {
		this.ideNovaTipologia = ideNovaTipologia;
	}

	public AtoAmbiental getIdeNovoAtoAmbiental() {
		return ideNovoAtoAmbiental;
	}

	public void setIdeNovoAtoAmbiental(AtoAmbiental ideNovoAtoAmbiental) {
		this.ideNovoAtoAmbiental = ideNovoAtoAmbiental;
	}

	public ProcessoAto getIdeProcessoAto() {
		return ideProcessoAto;
	}

	public void setIdeProcessoAto(ProcessoAto ideProcessoAto) {
		this.ideProcessoAto = ideProcessoAto;
	}

	public ReenquadramentoProcesso getIdeReenquadramentoProcesso() {
		return ideReenquadramentoProcesso;
	}

	public void setIdeReenquadramentoProcesso(ReenquadramentoProcesso ideReenquadramentoProcesso) {
		this.ideReenquadramentoProcesso = ideReenquadramentoProcesso;
	}

	public Integer getIdeReenquadramentoProcessoAto() {
		return ideReenquadramentoProcessoAto;
	}

	public void setIdeReenquadramentoProcessoAto(Integer ideReenquadramentoProcessoAto) {
		this.ideReenquadramentoProcessoAto = ideReenquadramentoProcessoAto;
	}
	
	public boolean isRenderedNovoAto() {
		return !Util.isNull(ideNovoAtoAmbiental) && !Util.isNullOuVazio(ideNovoAtoAmbiental.getNomAtoAmbiental());
	}
	
	public boolean isRenderedFinalidade() {
		return !Util.isNullOuVazio(reenquadramentoTipoFinalidadeUsoAguaCollection) || !Util.isNullOuVazio(reenquadramentoProcessoAtoObjetivoAtividadeManejoCollection);
	}
	
	public boolean isRenderedTipologia() {
		return !Util.isNull(ideNovaTipologia) && !Util.isNullOuVazio(ideNovaTipologia.getDesTipologia());
	}
	
	public boolean isRenderedTipoPlano() {
		return !Util.isNullOuVazio(reenquadramentoProcessoAtoTipoAtividadeFaunaCollection);
	}
	
	public boolean isRenderedTipologiaEmpreendimento() {
		return !Util.isNullOuVazio(reenquadramentoTipologiaEmpreendimentoCollection);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideReenquadramentoProcessoAto == null) ? 0 : ideReenquadramentoProcessoAto.hashCode());
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
		ReenquadramentoProcessoAto other = (ReenquadramentoProcessoAto) obj;
		if (ideReenquadramentoProcessoAto == null) {
			if (other.ideReenquadramentoProcessoAto != null)
				return false;
		} else if (!ideReenquadramentoProcessoAto.equals(other.ideReenquadramentoProcessoAto))
			return false;
		return true;
	}

	public String getDscJustificativa() {
		return dscJustificativa;
	}

	public void setDscJustificativa(String dscJustificativa) {
		this.dscJustificativa = dscJustificativa;
	}
	
	public Collection<ReenquadramentoTipologiaEmpreendimento> getReenquadramentoTipologiaEmpreendimento() {
		return reenquadramentoTipologiaEmpreendimentoCollection;
	}

	public void setReenquadramentoTipologiaEmpreendimento(
			Collection<ReenquadramentoTipologiaEmpreendimento> reenquadramentoTipologiaEmpreendimentoCollection) {
		this.reenquadramentoTipologiaEmpreendimentoCollection = reenquadramentoTipologiaEmpreendimentoCollection;
	}
	
	public Collection<ReenquadramentoTipologiaEmpreendimento> getReenquadramentoTipologiaEmpreendimentoCollection() {
		return reenquadramentoTipologiaEmpreendimentoCollection;
	}

	public void setReenquadramentoTipologiaEmpreendimentoCollection(
			Collection<ReenquadramentoTipologiaEmpreendimento> reenquadramentoTipologiaEmpreendimentoCollection) {
		this.reenquadramentoTipologiaEmpreendimentoCollection = reenquadramentoTipologiaEmpreendimentoCollection;
	}

	public boolean isAlteracao() {
		return isAlteracao;
	}

	public void setAlteracao(boolean isAlteracao) {
		this.isAlteracao = isAlteracao;
	}

	public Collection<ReenquadramentoTipoFinalidadeUsoAgua> getReenquadramentoTipoFinalidadeUsoAguaCollection() {
		return reenquadramentoTipoFinalidadeUsoAguaCollection;
	}

	public void setReenquadramentoTipoFinalidadeUsoAguaCollection(
			Collection<ReenquadramentoTipoFinalidadeUsoAgua> reenquadramentoTipoFinalidadeUsoAguaCollection) {
		this.reenquadramentoTipoFinalidadeUsoAguaCollection = reenquadramentoTipoFinalidadeUsoAguaCollection;
	}
	
	public Collection<ReenquadramentoProcessoAtoObjetivoAtividadeManejo> getReenquadramentoProcessoAtoObjetivoAtividadeManejoCollection() {
		return reenquadramentoProcessoAtoObjetivoAtividadeManejoCollection;
	}

	public void setReenquadramentoProcessoAtoObjetivoAtividadeManejoCollection(
			Collection<ReenquadramentoProcessoAtoObjetivoAtividadeManejo> reenquadramentoProcessoAtoObjetivoAtividadeManejoCollection) {
		this.reenquadramentoProcessoAtoObjetivoAtividadeManejoCollection = reenquadramentoProcessoAtoObjetivoAtividadeManejoCollection;
	}

	public Collection<ReenquadramentoProcessoAtoTipoAtividadeFauna> getReenquadramentoProcessoAtoTipoAtividadeFaunaCollection() {
		return reenquadramentoProcessoAtoTipoAtividadeFaunaCollection;
	}

	public void setReenquadramentoProcessoAtoTipoAtividadeFaunaCollection(
			Collection<ReenquadramentoProcessoAtoTipoAtividadeFauna> reenquadramentoProcessoAtoTipoAtividadeFaunaCollection) {
		this.reenquadramentoProcessoAtoTipoAtividadeFaunaCollection = reenquadramentoProcessoAtoTipoAtividadeFaunaCollection;
	}

	@Override
	public Long getId() {
		return Long.valueOf(ideReenquadramentoProcessoAto);
	}

}