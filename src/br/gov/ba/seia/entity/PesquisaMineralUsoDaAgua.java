package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.List;

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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * @author eduardo.fernandes
 * @since 03/11/2016
 * @see <a href="http://10.105.17.77/redmine/issues/">#8187</a>
 */
@Entity
@Table(name="pesquisa_mineral_uso_da_agua")
@NamedQueries({
		@NamedQuery(name = "PesquisaMineralUsoDaAgua.removeByIdePesquisaMineral", query = "DELETE FROM PesquisaMineralUsoDaAgua pmua  WHERE pmua.pesquisaMineral.idePesquisaMineral = :idePesquisaMineral"),
		@NamedQuery(name = "PesquisaMineralUsoDaAgua.removeByIde", query = "DELETE FROM PesquisaMineralUsoDaAgua pmua  WHERE pmua.idePesquisaMineralUsoDaAgua = :idePesquisaMineralUsoDaAgua")
})
public class PesquisaMineralUsoDaAgua implements Serializable, Comparable<PesquisaMineralUsoDaAgua>{
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "PESQUISA_MINERAL_USO_DA_AGUA_GENERATOR", sequenceName = "PESQUISA_MINERAL_USO_DA_AGUA_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PESQUISA_MINERAL_USO_DA_AGUA_GENERATOR")
	@Column(name="ide_pesquisa_mineral_uso_da_agua")
	private Integer idePesquisaMineralUsoDaAgua;

	@Column(name="ind_fonte_embasa")
	private Boolean indFonteEmbasa;

	@Column(name="ind_fonte_saae")
	private Boolean indFonteSaae;

	@Column(name="num_documento")
	private String numDocumento;

	//bi-directional many-to-one association to PesquisaMineral
	@ManyToOne(optional = false, fetch=FetchType.LAZY)
	@JoinColumn(name="ide_pesquisa_mineral")
	private PesquisaMineral pesquisaMineral;

	@OneToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_tipo_captacao", referencedColumnName = "ide_tipo_captacao")
	private TipoCaptacao tipoCaptacao;
	
	//bi-directional many-to-one association to PesquisaMineralDocumentoCaptacao
	@ManyToOne(optional = false, fetch=FetchType.LAZY)
	@JoinColumn(name="ide_pesquisa_mineral_documento_captacao")
	private PesquisaMineralDocumentoCaptacao pesquisaMineralDocumentoCaptacao;
	
	@Transient
	private List<PesquisaMineralDocumentoCaptacao> listaPesquisaMineralDocumentoCaptacao;
	
	@Transient
	private boolean selecionado;
	
	public PesquisaMineralUsoDaAgua() {
	}

	public PesquisaMineralUsoDaAgua(PesquisaMineral pesquisaMineral) {
		this.pesquisaMineral = pesquisaMineral;
	}

	public PesquisaMineralUsoDaAgua(PesquisaMineral pesquisaMineral, TipoCaptacao captacao) {
		this.pesquisaMineral = pesquisaMineral;
		this.tipoCaptacao = captacao;
	}

	public Integer getIdePesquisaMineralUsoDaAgua() {
		return this.idePesquisaMineralUsoDaAgua;
	}

	public void setIdePesquisaMineralUsoDaAgua(Integer idePesquisaMineralUsoDaAgua) {
		this.idePesquisaMineralUsoDaAgua = idePesquisaMineralUsoDaAgua;
	}

	public Boolean getIndFonteEmbasa() {
		return this.indFonteEmbasa;
	}

	public void setIndFonteEmbasa(Boolean indFonteEmbasa) {
		this.indFonteEmbasa = indFonteEmbasa;
	}

	public Boolean getIndFonteSaae() {
		return this.indFonteSaae;
	}

	public void setIndFonteSaae(Boolean indFonteSaae) {
		this.indFonteSaae = indFonteSaae;
	}

	public String getNumDocumento() {
		return this.numDocumento;
	}

	public void setNumDocumento(String numDocumento) {
		this.numDocumento = numDocumento;
	}

	public PesquisaMineral getPesquisaMineral() {
		return this.pesquisaMineral;
	}

	public void setPesquisaMineral(PesquisaMineral pesquisaMineral) {
		this.pesquisaMineral = pesquisaMineral;
	}

	public TipoCaptacao getTipoCaptacao() {
		return tipoCaptacao;
	}

	public void setTipoCaptacao(TipoCaptacao ideTipoCaptacao) {
		this.tipoCaptacao = ideTipoCaptacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((pesquisaMineral == null) ? 0 : pesquisaMineral.hashCode());
		result = prime * result
				+ ((tipoCaptacao == null) ? 0 : tipoCaptacao.hashCode());
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
		PesquisaMineralUsoDaAgua other = (PesquisaMineralUsoDaAgua) obj;
		if (pesquisaMineral == null) {
			if (other.pesquisaMineral != null)
				return false;
		} else if (!pesquisaMineral.equals(other.pesquisaMineral))
			return false;
		if (tipoCaptacao == null) {
			if (other.tipoCaptacao != null)
				return false;
		} else if (!tipoCaptacao.equals(other.tipoCaptacao))
			return false;
		return true;
	}

	public PesquisaMineralDocumentoCaptacao getPesquisaMineralDocumentoCaptacao() {
		return pesquisaMineralDocumentoCaptacao;
	}

	public void setPesquisaMineralDocumentoCaptacao(PesquisaMineralDocumentoCaptacao pesquisaMineralDocumentoCaptacao) {
		this.pesquisaMineralDocumentoCaptacao = pesquisaMineralDocumentoCaptacao;
	}

	public List<PesquisaMineralDocumentoCaptacao> getListaPesquisaMineralDocumentoCaptacao() {
		return listaPesquisaMineralDocumentoCaptacao;
	}

	public void setListaPesquisaMineralDocumentoCaptacao(List<PesquisaMineralDocumentoCaptacao> listaPesquisaMineralDocumentoCaptacao) {
		this.listaPesquisaMineralDocumentoCaptacao = listaPesquisaMineralDocumentoCaptacao;
	}
	
	public boolean isSelecionado() {
		return selecionado;
	}

	public void setSelecionado(boolean selecionado) {
		this.selecionado = selecionado;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(PesquisaMineralUsoDaAgua o) {
		return this.getTipoCaptacao().getIdeTipoCaptacao().compareTo(o.getTipoCaptacao().getIdeTipoCaptacao());
	}
	
}