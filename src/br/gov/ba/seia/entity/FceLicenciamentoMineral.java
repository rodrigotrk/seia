package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * The persistent class for the fce_licenciamento_mineral database table.
 * 
 */
@Entity
@Table(name = "fce_licenciamento_mineral")
public class FceLicenciamentoMineral implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "FCE_LICENCIAMENTO_MINERAL_IDEFCELICENCIAMENTOMINERAL_GENERATOR", sequenceName = "FCE_LICENCIAMENTO_MINERAL_IDE_FCE_LICENCIAMENTO_MINERAL_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FCE_LICENCIAMENTO_MINERAL_IDEFCELICENCIAMENTOMINERAL_GENERATOR")
	@Column(name = "ide_fce_licenciamento_mineral")
	private Integer ideFceLicenciamentoMineral;

	@JoinColumn(name = "ide_fce", referencedColumnName = "ide_fce", nullable = false)
	@OneToOne(fetch = FetchType.LAZY)
	private Fce ideFce;

	@JoinColumn(name = "ide_localizacao_geografica_lavra", referencedColumnName = "ide_localizacao_geografica", nullable = true)
	@OneToOne(fetch = FetchType.LAZY)
	private LocalizacaoGeografica ideLocalizacaoGeograficaLavra;

	@JoinColumn(name = "ide_localizacao_geografica_servidao", referencedColumnName = "ide_localizacao_geografica", nullable = true)
	@OneToOne(fetch = FetchType.LAZY)
	private LocalizacaoGeografica ideLocalizacaoGeograficaServidao;

	@Column(name = "ind_app")
	private Boolean indApp;

	@Column(name = "ind_beneficiamento_mineracao")
	private Boolean indBeneficiamentoMineracao;

	@Column(name = "ind_esfera_estadual")
	private Boolean indEsferaEstadual;

	@Column(name = "ind_esfera_federal")
	private Boolean indEsferaFederal;

	@Column(name = "ind_explosivos")
	private Boolean indExplosivos;

	@Column(name = "ind_pesqinterv_recurso_hidrico")
	private Boolean indPesqintervRecursoHidrico;

	@Column(name = "ind_supressao")
	private Boolean indSupressao;

	@Transient
	private Double areaDeLavra;

	@Transient
	private Double areaDeServidao;

	@Transient
	private List<ProcessoDnpm> listaProcessoDNPM;

	public FceLicenciamentoMineral() {

	}

	public FceLicenciamentoMineral(Fce ideFce) {
		this.ideFce = ideFce;
		this.listaProcessoDNPM = new ArrayList<ProcessoDnpm>();
	}

	public Integer getIdeFceLicenciamentoMineral() {
		return ideFceLicenciamentoMineral;
	}

	public void setIdeFceLicenciamentoMineral(Integer ideFceLicenciamentoMineral) {
		this.ideFceLicenciamentoMineral = ideFceLicenciamentoMineral;
	}

	public Fce getIdeFce() {
		return ideFce;
	}

	public void setIdeFce(Fce ideFce) {
		this.ideFce = ideFce;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeograficaLavra() {
		return ideLocalizacaoGeograficaLavra;
	}

	public void setIdeLocalizacaoGeograficaLavra(LocalizacaoGeografica ideLocalizacaoGeograficaLavra) {
		this.ideLocalizacaoGeograficaLavra = ideLocalizacaoGeograficaLavra;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeograficaServidao() {
		return ideLocalizacaoGeograficaServidao;
	}

	public void setIdeLocalizacaoGeograficaServidao(LocalizacaoGeografica ideLocalizacaoGeograficaServidao) {
		this.ideLocalizacaoGeograficaServidao = ideLocalizacaoGeograficaServidao;
	}

	public Boolean getIndApp() {
		return indApp;
	}

	public void setIndApp(Boolean indApp) {
		this.indApp = indApp;
	}

	public Boolean getIndBeneficiamentoMineracao() {
		return indBeneficiamentoMineracao;
	}

	public void setIndBeneficiamentoMineracao(Boolean indBeneficiamentoMineracao) {
		this.indBeneficiamentoMineracao = indBeneficiamentoMineracao;
	}

	public Boolean getIndEsferaEstadual() {
		return indEsferaEstadual;
	}

	public void setIndEsferaEstadual(Boolean indEsferaEstadual) {
		this.indEsferaEstadual = indEsferaEstadual;
	}

	public Boolean getIndEsferaFederal() {
		return indEsferaFederal;
	}

	public void setIndEsferaFederal(Boolean indEsferaFederal) {
		this.indEsferaFederal = indEsferaFederal;
	}

	public Boolean getIndExplosivos() {
		return indExplosivos;
	}

	public void setIndExplosivos(Boolean indExplosivos) {
		this.indExplosivos = indExplosivos;
	}

	public Boolean getIndPesqintervRecursoHidrico() {
		return indPesqintervRecursoHidrico;
	}

	public void setIndPesqintervRecursoHidrico(Boolean indPesqintervRecursoHidrico) {
		this.indPesqintervRecursoHidrico = indPesqintervRecursoHidrico;
	}

	public Boolean getIndSupressao() {
		return indSupressao;
	}

	public void setIndSupressao(Boolean indSupressao) {
		this.indSupressao = indSupressao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideFceLicenciamentoMineral == null) ? 0 : ideFceLicenciamentoMineral.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		FceLicenciamentoMineral other = (FceLicenciamentoMineral) obj;
		if(ideFceLicenciamentoMineral == null){
			if(other.ideFceLicenciamentoMineral != null)
				return false;
		}
		else if(!ideFceLicenciamentoMineral.equals(other.ideFceLicenciamentoMineral))
			return false;
		return true;
	}

	public Double getAreaDeLavra() {
		return areaDeLavra;
	}

	public void setAreaDeLavra(Double areaDeLavra) {
		this.areaDeLavra = areaDeLavra;
	}

	public Double getAreaDeServidao() {
		return areaDeServidao;
	}

	public void setAreaDeServidao(Double areaDeServidao) {
		this.areaDeServidao = areaDeServidao;
	}

	public List<ProcessoDnpm> getListaProcessoDNPM() {
		return listaProcessoDNPM;
	}

	public void setListaProcessoDNPM(List<ProcessoDnpm> listaProcessoDNPM) {
		this.listaProcessoDNPM = listaProcessoDNPM;
	}

}