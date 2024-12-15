package br.gov.ba.seia.entity;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.entity.generic.AbstractEntityHist;

@Entity
@Table(name="cerh_situacao_tipo_uso")
public class CerhSituacaoTipoUso extends AbstractEntityHist {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ide_cerh_situacao_tipo_uso")
	private Integer ideCerhSituacaoTipoUso;
	
	@Historico(name="Situação da interferência/Situação da Barragem")
	@Column(name="dsc_situacao_tipo_uso")
	private String dscSituacaoTipoUso;

	@OneToMany(mappedBy="ideCerhSituacaoTipoUso")
	private Collection<CerhBarragemCaracterizacao> cerhBarragemCaracterizacaoCollection;

	@OneToMany(mappedBy="ideCerhSituacaoTipoUso")
	private Collection<CerhCaptacaoCaracterizacao> cerhCaptacaoCaracterizacaoCollection;

	@OneToMany(mappedBy="ideCerhSituacaoTipoUso")
	private Collection<CerhIntervencaoCaracterizacao> cerhIntervencaoCaracterizacaoCollection;

	@OneToMany(mappedBy="ideCerhSituacaoTipoUso")
	private Collection<CerhLancamentoEfluenteCaracterizacao> cerhLancamentoEfluenteCaracterizacaoCollection;

	public CerhSituacaoTipoUso() {
		
	}
	
	public CerhSituacaoTipoUso(Integer ideCerhSituacaoTipoUso) {
		this.ideCerhSituacaoTipoUso = ideCerhSituacaoTipoUso;
	}
	
	public Integer getIdeCerhSituacaoTipoUso() {
		return ideCerhSituacaoTipoUso;
	}

	public void setIdeCerhSituacaoTipoUso(Integer ideCerhSituacaoTipoUso) {
		this.ideCerhSituacaoTipoUso = ideCerhSituacaoTipoUso;
	}

	public String getDscSituacaoTipoUso() {
		return dscSituacaoTipoUso;
	}

	public void setDscSituacaoTipoUso(String dscSituacaoTipoUso) {
		this.dscSituacaoTipoUso = dscSituacaoTipoUso;
	}

	public Collection<CerhBarragemCaracterizacao> getCerhBarragemCaracterizacaoCollection() {
		return cerhBarragemCaracterizacaoCollection;
	}

	public void setCerhBarragemCaracterizacaoCollection(Collection<CerhBarragemCaracterizacao> cerhBarragemCaracterizacaoCollection) {
		this.cerhBarragemCaracterizacaoCollection = cerhBarragemCaracterizacaoCollection;
	}

	public Collection<CerhCaptacaoCaracterizacao> getCerhCaptacaoCaracterizacaoCollection() {
		return cerhCaptacaoCaracterizacaoCollection;
	}

	public void setCerhCaptacaoCaracterizacaoCollection(Collection<CerhCaptacaoCaracterizacao> cerhCaptacaoCaracterizacaoCollection) {
		this.cerhCaptacaoCaracterizacaoCollection = cerhCaptacaoCaracterizacaoCollection;
	}

	public Collection<CerhIntervencaoCaracterizacao> getCerhIntervencaoCaracterizacaoCollection() {
		return cerhIntervencaoCaracterizacaoCollection;
	}

	public void setCerhIntervencaoCaracterizacaoCollection(Collection<CerhIntervencaoCaracterizacao> cerhIntervencaoCaracterizacaoCollection) {
		this.cerhIntervencaoCaracterizacaoCollection = cerhIntervencaoCaracterizacaoCollection;
	}

	public Collection<CerhLancamentoEfluenteCaracterizacao> getCerhLancamentoEfluenteCaracterizacaoCollection() {
		return cerhLancamentoEfluenteCaracterizacaoCollection;
	}

	public void setCerhLancamentoEfluenteCaracterizacaoCollection(Collection<CerhLancamentoEfluenteCaracterizacao> cerhLancamentoEfluenteCaracterizacaoCollection) {
		this.cerhLancamentoEfluenteCaracterizacaoCollection = cerhLancamentoEfluenteCaracterizacaoCollection;
	}

}