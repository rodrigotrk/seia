package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.enumerator.TipoCadastroImovelRuralEnum;

@Entity
@Table(name = "tipo_cadastro_imovel_rural")
@XmlRootElement
@NamedQuery(name = "TipoCadastroImovelRural.findAll", query = "SELECT t FROM TipoCadastroImovelRural t")
public class TipoCadastroImovelRural implements Serializable {
	private static final long serialVersionUID = 575675722877810954L;

	@Id
	@SequenceGenerator(name = "TIPO_CADASTRO_IMOVEL_RURAL_SEQ", sequenceName = "TIPO_CADASTRO_IMOVEL_RURAL_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TIPO_CADASTRO_IMOVEL_RURAL_SEQ")
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_tipo_cadastro_imovel_rural", nullable = false)
	private Integer ideTipoCadastroImovelRural;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "dsc_tipo_cadastro_imovel_rural")
	private String dscTipoCadastroImovelRural;

	public TipoCadastroImovelRural() {
	}

	public TipoCadastroImovelRural(Integer ideTipoCadastroImovelRural) {
		this.ideTipoCadastroImovelRural = ideTipoCadastroImovelRural;
	}

	public TipoCadastroImovelRural(TipoCadastroImovelRuralEnum tipoCadastroImovelRuralEnum) {
		this.ideTipoCadastroImovelRural = tipoCadastroImovelRuralEnum.getTipo();
	}

	public TipoCadastroImovelRural(Integer ideTipoCadastroImovelRural,
			String dscTipoCadastroImovelRural) {
		this.ideTipoCadastroImovelRural = ideTipoCadastroImovelRural;
		this.dscTipoCadastroImovelRural = dscTipoCadastroImovelRural;
	}

	public Integer getIdeTipoCadastroImovelRural() {
		return this.ideTipoCadastroImovelRural;
	}

	public void setIdeTipoCadastroImovelRural(Integer ideTipoCadastroImovelRural) {
		this.ideTipoCadastroImovelRural = ideTipoCadastroImovelRural;
	}

	public String getDscTipoCadastroImovelRural() {
		return this.dscTipoCadastroImovelRural;
	}

	public void setDscTipoCadastroImovelRural(String dscTipoCadastroImovelRural) {
		this.dscTipoCadastroImovelRural = dscTipoCadastroImovelRural;
	}

	@Override
	public String toString() {
		return String.valueOf(ideTipoCadastroImovelRural);
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ideTipoCadastroImovelRural != null ? ideTipoCadastroImovelRural
				.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof TipoCadastroImovelRural)) {
			return false;
		}
		TipoCadastroImovelRural other = (TipoCadastroImovelRural) object;
		if ((this.ideTipoCadastroImovelRural == null && other.ideTipoCadastroImovelRural != null)
				|| (this.ideTipoCadastroImovelRural != null && !this.ideTipoCadastroImovelRural
						.equals(other.ideTipoCadastroImovelRural))) {
			return false;
		}
		return true;
	}

}