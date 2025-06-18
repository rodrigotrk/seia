package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "lac_erb_equipamento")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "LacErbEquipamento.findAll", query = "SELECT l FROM LacErbEquipamento l"),
		@NamedQuery(name = "LacErbEquipamento.findByIdeLacErb", query = "SELECT l FROM LacErbEquipamento l WHERE l.lacErbEquipamentoPK.ideLacErb = :ideLacErb"),
		@NamedQuery(name = "LacErbEquipamento.findByIdeErbEquipamento", query = "SELECT l FROM LacErbEquipamento l WHERE l.lacErbEquipamentoPK.ideErbEquipamento = :ideErbEquipamento"),
		@NamedQuery(name = "LacErbEquipamento.findByIdeErbEquipamentoAndIdeLacErbAndIdeTipoCanalErb", query = "SELECT l FROM LacErbEquipamento l WHERE l.lacErbEquipamentoPK.ideErbEquipamento = :ideErbEquipamento AND l.lacErbEquipamentoPK.ideLacErb = :ideLacErb AND l.lacErbEquipamentoPK.ideTipoCanalErb = :ideTipoCanalErb"),
		@NamedQuery(name = "LacErbEquipamento.findByIdeTipoCanalErb", query = "SELECT l FROM LacErbEquipamento l WHERE l.lacErbEquipamentoPK.ideTipoCanalErb = :ideTipoCanalErb"),
		@NamedQuery(name = "LacErbEquipamento.findByQtdCanais", query = "SELECT l FROM LacErbEquipamento l WHERE l.qtdCanais = :qtdCanais"), 
		@NamedQuery(name = "LacErbEquipamento.removerLacErbEquipamento", query = "delete from LacErbEquipamento l where l.lacErbEquipamentoPK.ideErbEquipamento = :ideErbEquipamento AND l.lacErbEquipamentoPK.ideLacErb = :ideLacErb AND l.lacErbEquipamentoPK.ideTipoCanalErb = :ideTipoCanalErb")})
public class LacErbEquipamento implements Serializable {
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	protected LacErbEquipamentoPK lacErbEquipamentoPK;
	@Column(name = "qtd_canais")
	private Integer qtdCanais;
	@JoinColumn(name = "ide_tipo_canal_erb", referencedColumnName = "ide_tipo_canal_erb", nullable = false, insertable = false, updatable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private TipoCanalErb tipoCanalErb;
	@JoinColumn(name = "ide_lac_erb", referencedColumnName = "ide_lac_erb", nullable = false, insertable = false, updatable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private LacErb lacErb;

	public LacErbEquipamento() {
	}

	public LacErbEquipamento(LacErbEquipamentoPK lacErbEquipamentoPK) {
		this.lacErbEquipamentoPK = lacErbEquipamentoPK;
	}

	public LacErbEquipamento(int ideLacErb, int ideErbEquipamento, int ideTipoLacErb) {
		this.lacErbEquipamentoPK = new LacErbEquipamentoPK(ideLacErb, ideErbEquipamento, ideTipoLacErb);
	}

	public LacErbEquipamento(Integer qtdCanais, TipoCanalErb tipoCanalErb, LacErb lacErb) {
		super();
		this.qtdCanais = qtdCanais;
		this.tipoCanalErb = tipoCanalErb;
		this.lacErb = lacErb;
	}

	public LacErbEquipamentoPK getLacErbEquipamentoPK() {
		return lacErbEquipamentoPK;
	}

	public void setLacErbEquipamentoPK(LacErbEquipamentoPK lacErbEquipamentoPK) {
		this.lacErbEquipamentoPK = lacErbEquipamentoPK;
	}

	public Integer getQtdCanais() {
		return qtdCanais;
	}

	public void setQtdCanais(Integer qtdCanais) {
		this.qtdCanais = qtdCanais;
	}

	public TipoCanalErb getTipoCanalErb() {
		return tipoCanalErb;
	}

	public void setTipoCanalErb(TipoCanalErb tipoCanalErb) {
		this.tipoCanalErb = tipoCanalErb;
	}

	public LacErb getLacErb() {
		return lacErb;
	}

	public void setLacErb(LacErb lacErb) {
		this.lacErb = lacErb;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (lacErbEquipamentoPK != null ? lacErbEquipamentoPK.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		
		
		if (!(object instanceof LacErbEquipamento)) {
			return false;
		}
		LacErbEquipamento other = (LacErbEquipamento) object;
		if ((this.lacErbEquipamentoPK == null && other.lacErbEquipamentoPK != null)
				|| (this.lacErbEquipamentoPK != null && !this.lacErbEquipamentoPK.equals(other.lacErbEquipamentoPK))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "br.gov.ba.seia.entity.LacErbEquipamento[ lacErbEquipamentoPK=" + lacErbEquipamentoPK + " ]";
	}

	@Override
	public LacErbEquipamento clone() {
		return new LacErbEquipamento(qtdCanais, tipoCanalErb, lacErb);
	}
}
