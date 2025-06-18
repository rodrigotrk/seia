package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import br.gov.ba.seia.enumerator.TipoUsoAgua;
import br.gov.ba.seia.util.Util;

@Entity
@Table(name = "imovel_rural_uso_agua")
@XmlRootElement
public class ImovelRuralUsoAgua implements Serializable {

	private static final long serialVersionUID = -9203629258610460426L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "imovel_rural_uso_agua_ide_uso_agua_seq")
	@SequenceGenerator(name = "imovel_rural_uso_agua_ide_uso_agua_seq", sequenceName = "imovel_rural_uso_agua_ide_uso_agua_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "ide_imovel_rural_uso_agua", nullable = false)
	private Integer ideImovelRuralUsoAgua;

	@JoinColumn(name = "ide_imovel_rural", referencedColumnName = "ide_imovel_rural", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private ImovelRural ideImovelRural;

	@JoinColumn(name = "ide_localizacao_geografica", referencedColumnName = "ide_localizacao_geografica")
	@OneToOne(optional = false, fetch = FetchType.EAGER)
	private LocalizacaoGeografica ideLocalizacaoGeografica;

	@JoinColumn(name = "ide_localizacao_geografica_final", referencedColumnName = "ide_localizacao_geografica",nullable=true)
	@OneToOne(optional = true)
	private LocalizacaoGeografica ideLocalizacaoGeograficaFinal;

	@Basic(optional = false)
	@Column(name = "val_vazao")
	private Double valVazao;

	@Basic(optional = false)
	@Column(name = "ind_dispensa")
	private Boolean indDispensa;

	@Basic(optional = false)
	@Column(name = "ind_processo")
	private Boolean indProcesso;

	@Size(max = 1)
	@NotNull
	@Column(name = "tipo_uso")
	private String tipoUso;

	@Basic(optional = false)
    @NotNull
    @Column(name = "dtc_criacao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcCriacao;

    @Basic(optional = false)
    @NotNull
    @Column(name = "ind_excluido", nullable = false)
    private boolean indExcluido;

    @Column(name = "dtc_exclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcExclusao;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideImovelRuralUsoAgua",fetch=FetchType.EAGER)
	@Cascade(value=org.hibernate.annotations.CascadeType.ALL)
	private Collection<ImovelRuralUsoAguaIntervencao> imovelRuralUsoAguaIntervencaoCollection;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideImovelRuralUsoAgua")
	@LazyCollection(LazyCollectionOption.FALSE)
	@Cascade(value=org.hibernate.annotations.CascadeType.ALL)
	private Collection<ProcessoUsoAgua> processoUsoAguaCollection;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideImovelRuralUsoAgua")
	@LazyCollection(LazyCollectionOption.FALSE)
	@Cascade(value=org.hibernate.annotations.CascadeType.ALL)
	private Collection<ImovelRuralUsoAguaTipoFinalidadeUsoAgua> tipoFinalidadeCollection;

	@Transient
	private boolean indEdicao;

	@Transient
	private DadoGeografico dadoGeografico;

	@Transient
	private DadoGeografico dadoGeograficoPonto2;

	public ImovelRuralUsoAgua() {
	}

	public ImovelRuralUsoAgua(ImovelRural ideImovelRural,
			LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideImovelRural = ideImovelRural;
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}
	public ImovelRuralUsoAgua(Integer ideImovelRuralUsoAgua) {
		this.ideImovelRuralUsoAgua = ideImovelRuralUsoAgua;
	}

	public Integer getIdeImovelRuralUsoAgua() {
		return ideImovelRuralUsoAgua;
	}

	public void setIdeImovelRuralUsoAgua(Integer ideImovelRuralUsoAgua) {
		this.ideImovelRuralUsoAgua = ideImovelRuralUsoAgua;
	}

	public Boolean getIndDispensa() {
		return indDispensa;
	}

	public void setIndDispensa(Boolean indDispensa) {
		this.indDispensa = indDispensa;
	}

	public Boolean getIndProcesso() {
		return indProcesso;
	}

	public void setIndProcesso(Boolean indProcesso) {
		this.indProcesso = indProcesso;
	}

	public String getTipoUso() {
		return tipoUso;
	}

	public void setTipoUso(String tipoUso) {
		this.tipoUso = tipoUso;
	}

	public ImovelRural getIdeImovelRural() {
		return ideImovelRural;
	}

	public void setIdeImovelRural(ImovelRural ideImovelRural) {
		this.ideImovelRural = ideImovelRural;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(
			LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public Collection<ImovelRuralUsoAguaIntervencao> getImovelRuralUsoAguaIntervencaoCollection() {
		return imovelRuralUsoAguaIntervencaoCollection;
	}

	public void setImovelRuralUsoAguaIntervencaoCollection(
			Collection<ImovelRuralUsoAguaIntervencao> imovelRuralUsoAguaIntervencaoCollection) {
		this.imovelRuralUsoAguaIntervencaoCollection = imovelRuralUsoAguaIntervencaoCollection;
	}

	public Double getValVazao() {
		return valVazao;
	}

	public void setValVazao(Double valVazao) {
		this.valVazao = valVazao;
	}

	public Collection<ProcessoUsoAgua> getProcessoUsoAguaCollection() {
		return processoUsoAguaCollection;
	}

	public void setProcessoUsoAguaCollection(
			Collection<ProcessoUsoAgua> processoUsoAguaCollection) {
		this.processoUsoAguaCollection = processoUsoAguaCollection;
	}

	public Date getDtcExclusao() {
		return dtcExclusao;
	}

	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}

	public boolean isIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	public Date getDtcCriacao() {
		return dtcCriacao;
	}

	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeograficaFinal() {
		return ideLocalizacaoGeograficaFinal;
	}

	public void setIdeLocalizacaoGeograficaFinal(
			LocalizacaoGeografica ideLocalizacaoGeograficaFinal) {
		this.ideLocalizacaoGeograficaFinal = ideLocalizacaoGeograficaFinal;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (ideImovelRuralUsoAgua != null ? ideImovelRuralUsoAgua.hashCode() : 0);
        return hash;
    }

	@Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ImovelRuralUsoAgua)) {
            return false;
        }
        ImovelRuralUsoAgua other = (ImovelRuralUsoAgua) object;
        if ((this.ideImovelRuralUsoAgua == null && other.ideImovelRuralUsoAgua != null) || (this.ideImovelRuralUsoAgua != null && !this.ideImovelRuralUsoAgua.equals(other.ideImovelRuralUsoAgua))) {
            return false;
        }
        return true;
    }

	@Override
	public String toString() {
		return this.ideImovelRuralUsoAgua+"";
	}

	public boolean isIndEdicao() {
		return indEdicao;
	}

	public void setIndEdicao(boolean indEdicao) {
		this.indEdicao = indEdicao;
	}

	public Collection<ImovelRuralUsoAguaTipoFinalidadeUsoAgua> getTipoFinalidadeCollection() {
		return tipoFinalidadeCollection;
	}

	public void setTipoFinalidadeCollection(Collection<ImovelRuralUsoAguaTipoFinalidadeUsoAgua> tipoFinalidadeCollection) {
		this.tipoFinalidadeCollection = tipoFinalidadeCollection;
	}

	public DadoGeografico getDadoGeografico() {
		return dadoGeografico;
	}

	public void setDadoGeografico(DadoGeografico dadoGeografico) {
		this.dadoGeografico = dadoGeografico;
	}

	public DadoGeografico getDadoGeograficoPonto2() {
		return dadoGeograficoPonto2;
	}

	public void setDadoGeograficoPonto2(DadoGeografico dadoGeograficoPonto2) {
		this.dadoGeograficoPonto2 = dadoGeograficoPonto2;
	}

	public String getLabelTipoUsoAgua() {
		if (!Util.isNullOuVazio(this.tipoUso)) {
			final List<TipoUsoAgua> values = Arrays.asList(TipoUsoAgua.values());
			for (TipoUsoAgua tipoUsoAgua : values) {
				if(this.tipoUso.equals(tipoUsoAgua.getId().toString())) {
					return Util.getString(tipoUsoAgua.getChaveLabel());
				}
			}
		}
		return "";
	}
}
