package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;

/**
 * 
 * @author Lucas Reis
 * 
 **/
@Entity
@Table(name = "tipo_prorrogacao_prazo_validade")
@XmlRootElement
@NamedNativeQuery(name = "TipoProrrogacaoPrazoValidade.insertTipoProrrogacaoPrazoValidade", query = "INSERT into tipo_prorrogacao_prazo_validade (ide_tipo_prorrogacao_prazo_validade_pai, nom_tipo_prorrogacao_prazo_validade) VALUES (:ideTipoProrrogacaoPai,:nomTipoProrrogacao)", resultClass = TipoProrrogacaoPrazoValidade.class)
@NamedQuery(name = "TipoProrrogacaoPrazoValidade.findByIde", query = "SELECT t FROM TipoProrrogacaoPrazoValidade t WHERE t.ideTipoProrrogacaoPrazoValidade = :ideTipoProrrogacaoPrazoValidade")
public class TipoProrrogacaoPrazoValidade implements Serializable, BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_prorrogacao_prazo_validade_ide_tipo_prorrogacao_prazo_validade_seq")
	@SequenceGenerator(name = "tipo_prorrogacao_prazo_validade_ide_tipo_prorrogacao_prazo_validade_seq", sequenceName = "tipo_prorrogacao_prazo_validade_ide_tipo_prorrogacao_prazo_validade_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "ide_tipo_prorrogacao_prazo_validade", nullable = false)
	private Integer ideTipoProrrogacaoPrazoValidade;

	@OneToMany(mappedBy = "ideTipoProrrogacaoPrazoValidade", fetch = FetchType.LAZY)
	private List<TipoProrrogacaoPrazoValidade> tipoProrrogacaoList;

	@JoinColumn(name = "ide_tipo_prorrogacao_prazo_validade_pai", referencedColumnName = "ide_tipo_prorrogacao_prazo_validade", nullable = true)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private TipoProrrogacaoPrazoValidade ideTipoProrrogacaoPrazoValidadePai;

	@Basic(optional = false)
	
	@Column(name = "nom_tipo_prorrogacao_prazo_validade", nullable = true, length = 50)
	private String nomTipoProrrogacaoPrazoValidade;

	@Transient
	private LocalizacaoGeografica localizacaoGeograficaTransient;
	
	@Transient
	private boolean ativarIncluirPontos = false;
	
	@Transient
	private boolean checked;

	public TipoProrrogacaoPrazoValidade() {

	}

	public Integer getIdeTipoProrrogacaoPrazoValidade() {
		return ideTipoProrrogacaoPrazoValidade;
	}

	public void setIdeTipoProrrogacaoPrazoValidade(Integer ideTipoProrrogacaoPrazoValidade) {
		this.ideTipoProrrogacaoPrazoValidade = ideTipoProrrogacaoPrazoValidade;
	}

	public TipoProrrogacaoPrazoValidade getIdeTipoProrrogacaoPrazoValidadePai() {
		return ideTipoProrrogacaoPrazoValidadePai;
	}

	public void setIdeTipoProrrogacaoPrazoValidadePai(TipoProrrogacaoPrazoValidade ideTipoProrrogacaoPrazoValidadePai) {
		this.ideTipoProrrogacaoPrazoValidadePai = ideTipoProrrogacaoPrazoValidadePai;
	}

	public String getNomTipoProrrogacaoPrazoValidade() {
		return nomTipoProrrogacaoPrazoValidade;
	}

	public void setNomTipoProrrogacaoPrazoValidade(String nomTipoProrrogacaoPrazoValidade) {
		this.nomTipoProrrogacaoPrazoValidade = nomTipoProrrogacaoPrazoValidade;
	}

	public List<TipoProrrogacaoPrazoValidade> getTipoProrrogacaoList() {
		return tipoProrrogacaoList;
	}

	public void setTipoProrrogacaoList(List<TipoProrrogacaoPrazoValidade> tipoProrrogacaoList) {
		this.tipoProrrogacaoList = tipoProrrogacaoList;
	}

	public LocalizacaoGeografica getLocalizacaoGeograficaTransient() {
		return localizacaoGeograficaTransient;
	}

	public void setLocalizacaoGeograficaTransient(LocalizacaoGeografica localizacaoGeograficaTransient) {
		this.localizacaoGeograficaTransient = localizacaoGeograficaTransient;
	}

	public boolean isAtivarIncluirPontos() {
		return ativarIncluirPontos;
	}

	public void setAtivarIncluirPontos(boolean ativarIncluirPontos) {
		this.ativarIncluirPontos = ativarIncluirPontos;
	}

	@Override
	public Long getId() {
		return new Long(this.ideTipoProrrogacaoPrazoValidade);
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideTipoProrrogacaoPrazoValidade == null) ? 0 : ideTipoProrrogacaoPrazoValidade.hashCode());
		result = prime * result
				+ ((nomTipoProrrogacaoPrazoValidade == null) ? 0 : nomTipoProrrogacaoPrazoValidade.hashCode());
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
		TipoProrrogacaoPrazoValidade other = (TipoProrrogacaoPrazoValidade) obj;
		if (ideTipoProrrogacaoPrazoValidade == null) {
			if (other.ideTipoProrrogacaoPrazoValidade != null)
				return false;
		} else if (!ideTipoProrrogacaoPrazoValidade.equals(other.ideTipoProrrogacaoPrazoValidade))
			return false;
		if (nomTipoProrrogacaoPrazoValidade == null) {
			if (other.nomTipoProrrogacaoPrazoValidade != null)
				return false;
		} else if (!nomTipoProrrogacaoPrazoValidade.equals(other.nomTipoProrrogacaoPrazoValidade))
			return false;
		return true;
	}

}
