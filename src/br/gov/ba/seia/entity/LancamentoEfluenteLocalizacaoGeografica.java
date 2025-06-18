package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;

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

import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name = "lancamento_efluente_localizacao_geografica")
@NamedQueries({ @NamedQuery(name = "LancamentoEfluenteLocalizacaoGeografica.deleteByIde", query = "DELETE FROM LancamentoEfluenteLocalizacaoGeografica l WHERE l.ideLancamentoEfluenteLocalizacaoGeografica = :ideLancamentoEfluenteLocalizacaoGeografica") })
public class LancamentoEfluenteLocalizacaoGeografica implements Serializable, BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "lancamento_efluente_localizacao_geografica_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "lancamento_efluente_localizacao_geografica_seq", sequenceName = "lancamento_efluente_localizacao_geografica_seq", allocationSize = 1)
	@Column(name = "ide_lancamento_efluente_localizacao_geografica", nullable = false)
	private Integer ideLancamentoEfluenteLocalizacaoGeografica;
	
	@JoinColumn(name = "ide_fce_intervencao_mineracao", referencedColumnName = "ide_fce_intervencao_mineracao", nullable = false)
	@ManyToOne(fetch = FetchType.EAGER)
	private FceIntervencaoMineracao ideFceIntervencaoMineracao;
	
	@JoinColumn(name = "ide_localizacao_geografica", referencedColumnName = "ide_localizacao_geografica", nullable = false)
	@ManyToOne(fetch = FetchType.EAGER)
	private LocalizacaoGeografica ideLocalizacaoGeografica;
	
	@Column(name = "val_vazao_lancamento_manancial", nullable = true, scale = 10, precision = 2)
	private BigDecimal valVazaoLancamentoManancial;
	
	@Transient
	private boolean editar = true;
	
	public LancamentoEfluenteLocalizacaoGeografica() {
	}
	
	public LancamentoEfluenteLocalizacaoGeografica(FceIntervencaoMineracao ideFceIntervencaoMineracao) {
		this.ideFceIntervencaoMineracao = ideFceIntervencaoMineracao;
		this.ideLocalizacaoGeografica = new LocalizacaoGeografica();
	}
	
	public Integer getIdeLancamentoEfluenteLocalizacaoGeografica() {
		return ideLancamentoEfluenteLocalizacaoGeografica;
	}
	
	public void setIdeLancamentoEfluenteLocalizacaoGeografica(Integer ideLancamentoEfluenteLocalizacaoGeografica) {
		this.ideLancamentoEfluenteLocalizacaoGeografica = ideLancamentoEfluenteLocalizacaoGeografica;
	}
	
	public FceIntervencaoMineracao getIdeFceIntervencaoMineracao() {
		return ideFceIntervencaoMineracao;
	}
	
	public void setIdeFceIntervencaoMineracao(FceIntervencaoMineracao ideFceIntervencaoMineracao) {
		this.ideFceIntervencaoMineracao = ideFceIntervencaoMineracao;
	}
	
	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}
	
	public void setIdeLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideLancamentoEfluenteLocalizacaoGeografica == null) ? 0 : ideLancamentoEfluenteLocalizacaoGeografica.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		LancamentoEfluenteLocalizacaoGeografica other = (LancamentoEfluenteLocalizacaoGeografica) obj;
		if (ideLancamentoEfluenteLocalizacaoGeografica == null) {
			if (other.ideLancamentoEfluenteLocalizacaoGeografica != null) return false;
		} else if (!ideLancamentoEfluenteLocalizacaoGeografica.equals(other.ideLancamentoEfluenteLocalizacaoGeografica)) return false;
		return true;
	}
	
	@Override
	public Long getId() {
		return new Long(ideLancamentoEfluenteLocalizacaoGeografica);
	}
	
	public BigDecimal getValVazaoLancamentoManancial() {
		return valVazaoLancamentoManancial;
	}
	
	public void setValVazaoLancamentoManancial(BigDecimal valVazaoLancamentoManancial) {
		this.valVazaoLancamentoManancial = valVazaoLancamentoManancial;
	}

	public boolean isEditar() {
		return editar;
	}

	public void setEditar(boolean editar) {
		this.editar = editar;
	}
}