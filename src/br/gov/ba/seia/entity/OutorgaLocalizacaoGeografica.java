package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.enumerator.TipoFinalidadeUsoAguaEnum;
import br.gov.ba.seia.util.Util;

@Entity
@Table(name = "outorga_localizacao_geografica")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "OutorgaLocalizacaoGeografica.excluirByIdeOutorga", query = "DELETE FROM OutorgaLocalizacaoGeografica olg WHERE olg.ideOutorga = :ideOutorga"),
	@NamedQuery(name = "OutorgaLocalizacaoGeografica.excluirByIdeOutorgaLocGeo", query = "DELETE FROM OutorgaLocalizacaoGeografica olg WHERE olg.ideOutorgaLocalizacaoGeografica = :ideOutorgaLocalizacaoGeografica")
})
public class OutorgaLocalizacaoGeografica implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "outorga_loc_geo_ide_outorga_loc_geo_generator")
	@SequenceGenerator(name = "outorga_loc_geo_ide_outorga_loc_geo_generator", sequenceName = "outorga_loc_geo_ide_outorga_loc_geo_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "ide_outorga_localizacao_geografica", nullable = false)
	private Integer ideOutorgaLocalizacaoGeografica;

	@Column(name = "num_vazao", precision = 10, scale = 2, nullable = true, length = 50)
	private BigDecimal numVazao;

	@Column(name = "num_vazao_final", precision = 10, scale = 2, nullable = true, length = 50)
	private BigDecimal numVazaoFinal;

	@Column(name = "num_area_irrigada", precision = 10, scale = 2, nullable = true, length = 50)
	private BigDecimal numAreaIrrigada;

	@Column(name = "num_area_irrigada_captacao", precision = 10, scale = 2, nullable = true, length = 50)
	private BigDecimal numAreaIrrigadaCaptacao;

	@Column(name = "num_area_pulverizada", precision = 10, scale = 2, nullable = true, length = 50)
	private BigDecimal numAreaPulverizada;
	
	@Column(name = "num_area_dessedentacao_animal", precision = 10, scale = 2, nullable = true, length = 50)
	private BigDecimal numAreaDessedentacaoAnimal;
	
	@Column(name = "num_volume_acumulacao_barragem", precision = 10, scale = 2, nullable = true, length = 50)
	private BigDecimal numVolumeAcumulacaoBarragem;
	
	@Column(name = "num_descarga_fundo", precision = 10, scale = 2, nullable = true, length = 50)
	private BigDecimal numDescargaFundo;
	
	@Column(name = "num_volume_maximo_acumulado", precision = 11, scale = 2, nullable = true)
	private BigDecimal numVolumeMaximoAcumulado;
	
	@Column(name = "num_area_inundada_reservatorio", precision = 10, scale = 2, nullable = true, length = 50)
	private BigDecimal numAreaInundadaReservatorio;
	
	@Column(name = "num_area_empreendimento", precision = 10, scale = 2, nullable = true, length = 50)
	private BigDecimal numAreaEmpreendimento;

	@Column(name = "dtc_perfuracao_poco", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcPerfuracaoPoco;
	
	@Column(name = "dtc_emissao_oficio", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcEmissaoOficio;
	
	@Column(name = "dtc_publicacao_portaria_barragem", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcPublicacaoPortariaBarragem;
	
	@Size(min = 1, max = 50)
	@Column(name = "num_oficio", nullable = true, length = 50)
	private String numOficio;
	
	@Size(min = 1, max = 50)
	@Column(name = "num_processo_barragem", nullable = true, length = 50)
	private String numProcessoBarragem;
	
	@Size(min = 1, max = 50)
	@Column(name = "num_portaria_barragem", nullable = true, length = 50)
	private String numPortariaBarragem;
	
	@Column(name = "num_portaria_licenca_barragem", nullable = true, length = 50)
	private String numPortariaLicencaBarragem;

	@Size(min = 1, max = 50)
	@Column(name = "nom_intervencao", nullable = true, length = 50)
	private String nomIntervencao;

	@Column(name = "ind_finalidade", nullable = true)
	private Integer indFinalidade;
	
	@Column(name = "ind_abastecimento_em_distrito_industrial")
	private Boolean indAbastecimentoEmDistritoIndustrial;

	@JoinColumn(name = "ide_outorga", referencedColumnName = "ide_outorga", nullable = true)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Outorga ideOutorga;

	@JoinColumn(name = "ide_localizacao_geografica", referencedColumnName = "ide_localizacao_geografica", nullable = true)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private LocalizacaoGeografica ideLocalizacaoGeografica;

	@JoinColumn(name = "ide_localizacao_geografica_final", referencedColumnName = "ide_localizacao_geografica", nullable = true)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private LocalizacaoGeografica ideLocalizacaoGeograficaFinal;
	
	@JoinColumn(name = "ide_tipo_barragem", referencedColumnName = "ide_tipo_barragem", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private TipoBarragem ideTipoBarragem;

	@JoinColumn(name = "ide_tipo_travessia", referencedColumnName = "ide_tipo_travessia", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private TipoTravessia ideTipoTravessia;
	
	@JoinColumn(name = "ide_tipo_intervencao", referencedColumnName = "ide_tipo_intervencao", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private TipoIntervencao tipoIntervencao;

	@OneToMany(mappedBy = "ideOutorgaLocalizacaoGeografica" ,fetch = FetchType.LAZY)
	private Collection<OutorgaLocalizacaoGeograficaFinalidade> outorgaLocalizacaoGeograficaFinalidadeCollection;
	
	@Transient
	private boolean localizacaoFinal;

	@Transient
	private TipoIntervencao tipoIntervencaoPai;

	@Transient
	private HashMap<String, String> pontos;
	@Transient
	private List<Imovel> listaImovel;
	@Transient
	private Imovel imovel;
	@Transient
	private String nomeImovel;
	@Transient
	private List<TipoFinalidadeUsoAgua> listaFinalidadeUsoAgua;
	@Transient
	private List<TipoFinalidadeUsoAgua> listaFinalidadeUsoAguaCaptacao;
	@Transient
	private Imovel imovelToList;

	@Transient
	private boolean desabilitado;
	@Transient
	private Integer tempoCaptacao;
	@Transient
	private boolean desabilitaTipoBarragem;

	@Transient
	private boolean desabilitaTempoVazao;
	@Transient
	private String strVazao;
	@Transient
	private String strTempo;

	/*********************
	 * 					 *
	 * XXX: CONSTRUTORES *
	 * 					 *
	 *********************/
	
	public OutorgaLocalizacaoGeografica(){}

	public OutorgaLocalizacaoGeografica(Outorga ideOutorga) {
		super();
		this.ideOutorga = ideOutorga;
	}

	public OutorgaLocalizacaoGeografica(Integer ideOutorgaLocalizacaoGeografica, LocalizacaoGeografica localizacaoGeografica){
		this.ideOutorgaLocalizacaoGeografica = ideOutorgaLocalizacaoGeografica;
		this.ideLocalizacaoGeografica = localizacaoGeografica;
	}

	public OutorgaLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public OutorgaLocalizacaoGeografica(Integer ideOutorgaLocalizacaoGeografica) {
		this.ideOutorgaLocalizacaoGeografica = ideOutorgaLocalizacaoGeografica;
	}
	
	/********************
	 * 					*
	 * XXX: AUXILIARES	*
	 * 					*
	 ********************/
	
	public boolean isIrrigacao() {
		return this.getListaFinalidadeUsoAgua().contains(new TipoFinalidadeUsoAgua(TipoFinalidadeUsoAguaEnum.IRRIGACAO.getId(),false));
	}

	public boolean isIrrigacaoCaptacao() {
		return this.getListaFinalidadeUsoAguaCaptacao().contains(new TipoFinalidadeUsoAgua(TipoFinalidadeUsoAguaEnum.IRRIGACAO.getId(),true));
	}

	public boolean isPulverizacao() {
		return this.getListaFinalidadeUsoAgua().contains(new TipoFinalidadeUsoAgua(TipoFinalidadeUsoAguaEnum.PULVERIZACAO_AGRICOLA.getId(),false));
	}

	public boolean isPulverizacaoCaptacao() {
		return this.getListaFinalidadeUsoAguaCaptacao().contains(new TipoFinalidadeUsoAgua(TipoFinalidadeUsoAguaEnum.PULVERIZACAO_AGRICOLA.getId(),true));
	}
	
	public boolean isDessedentacaoAnimal() {
		return this.getListaFinalidadeUsoAguaCaptacao().contains(new TipoFinalidadeUsoAgua(TipoFinalidadeUsoAguaEnum.DESSEDENTACAO_ANIMAL.getId(),true));
	}
	
	public boolean isAbastecimentoIndustrial() {
		return this.getListaFinalidadeUsoAguaCaptacao().contains(new TipoFinalidadeUsoAgua(TipoFinalidadeUsoAguaEnum.ABASTECIMENTO_INDUSTRIAL.getId(),true));
	}
	
	public List<Imovel> getListaImovel() {
		return Util.isNullOuVazio(listaImovel) ? listaImovel = new ArrayList<Imovel>() : listaImovel;
	}
	
	public HashMap<String, String> getPontos() {
		try {
			HashMap<String, String> pontosLocGeoOutorga = new HashMap<String, String>();
			pontosLocGeoOutorga.put("pontos", ideLocalizacaoGeografica.getDadoGeograficoCollection().iterator().next().getCoordGeoNumerica());
			this.setPontos(pontosLocGeoOutorga);
		} catch (Exception e) {
			System.err.println("Erro ao carregar pontos");
		}
		return pontos;
	}
	
	public void setPontos(HashMap<String, String> pontos) {
		int index;
		String ponto;
		ponto = pontos.get("pontos").replace("POINT (", "");
		ponto = ponto.replace(")", "");
		// Deixa apenas os números na string
		index = ponto.indexOf(" ");
		// captura o indicie de separação entre latitude e longitude
		pontos.put("latitude", ponto.substring(0, index));
		pontos.put("longitude", ponto.substring(index));
		this.pontos = pontos;
	}

	public Imovel getImovelToList() {

		if(Util.isNullOuVazio(imovelToList) && !Util.isNullOuVazio(listaImovel)) {
			imovelToList = listaImovel.get(0);
		}

		return imovelToList;
	}

	public void setImovelToList(Imovel imovelToList) {

		if(!Util.isNullOuVazio(imovelToList)) {
			listaImovel = new ArrayList<Imovel>();
			listaImovel.add(imovelToList);
		}

		this.imovelToList = imovelToList;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result)
				+ ((ideOutorgaLocalizacaoGeografica == null) ? 0 : ideOutorgaLocalizacaoGeografica.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}

		OutorgaLocalizacaoGeografica other = (OutorgaLocalizacaoGeografica) obj;

		if (ideOutorgaLocalizacaoGeografica == null) {
			return false;
		} else if (!ideOutorgaLocalizacaoGeografica.equals(other.ideOutorgaLocalizacaoGeografica)) {
			return false;
		}

		return true;
	}

	public List<TipoFinalidadeUsoAgua> getListaFinalidadeUsoAgua() {
		return Util.isNullOuVazio(listaFinalidadeUsoAgua) ? listaFinalidadeUsoAgua = new ArrayList<TipoFinalidadeUsoAgua>() : listaFinalidadeUsoAgua;
	}
	

	public List<TipoFinalidadeUsoAgua> getListaFinalidadeUsoAguaCaptacao() {
		return Util.isNullOuVazio(listaFinalidadeUsoAguaCaptacao) 
				? listaFinalidadeUsoAguaCaptacao = new ArrayList<TipoFinalidadeUsoAgua>()
				: listaFinalidadeUsoAguaCaptacao;
	}
	
	/********************
	 * 					*
	 * XXX: GETS E SETS	*
	 * 					*
	 ********************/
	
	public Integer getIdeOutorgaLocalizacaoGeografica() {
		return ideOutorgaLocalizacaoGeografica;
	}

	public void setIdeOutorgaLocalizacaoGeografica(Integer ideOutorgaLocalizacaoGeografica) {
		this.ideOutorgaLocalizacaoGeografica = ideOutorgaLocalizacaoGeografica;
	}

	public Outorga getIdeOutorga() {
		return ideOutorga;
	}

	public void setIdeOutorga(Outorga ideOutorga) {
		this.ideOutorga = ideOutorga;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public BigDecimal getNumVazao() {
		return numVazao;
	}

	public void setNumVazao(BigDecimal numVazao) {
		this.numVazao = numVazao;
	}

	public BigDecimal getNumAreaIrrigada() {
		return numAreaIrrigada;
	}

	public void setNumAreaIrrigada(BigDecimal numAreaIrrigada) {
		this.numAreaIrrigada = numAreaIrrigada;
	}

	public BigDecimal getNumAreaIrrigadaCaptacao() {
		return numAreaIrrigadaCaptacao;
	}

	public void setNumAreaIrrigadaCaptacao(BigDecimal numAreaIrrigadaCaptacao) {
		this.numAreaIrrigadaCaptacao = numAreaIrrigadaCaptacao;
	}

	public Date getDtcPerfuracaoPoco() {
		return dtcPerfuracaoPoco;
	}

	public void setDtcPerfuracaoPoco(Date dtcPerfuracaoPoco) {
		this.dtcPerfuracaoPoco = dtcPerfuracaoPoco;
	}

	public String getNumOficio() {
		return numOficio;
	}

	public void setNumOficio(String numOficio) {
		this.numOficio = numOficio;
	}

	public Date getDtcEmissaoOficio() {
		return dtcEmissaoOficio;
	}

	public void setDtcEmissaoOficio(Date dtcEmissaoOficio) {
		this.dtcEmissaoOficio = dtcEmissaoOficio;
	}

	public String getNumProcessoBarragem() {
		return numProcessoBarragem;
	}

	public void setNumProcessoBarragem(String numProcessoBarragem) {
		this.numProcessoBarragem = numProcessoBarragem;
	}

	public String getNumPortariaBarragem() {
		return numPortariaBarragem;
	}

	public void setNumPortariaBarragem(String numPortariaBarragem) {
		this.numPortariaBarragem = numPortariaBarragem;
	}

	public Date getDtcPublicacaoPortariaBarragem() {
		return dtcPublicacaoPortariaBarragem;
	}

	public void setDtcPublicacaoPortariaBarragem(Date dtcPublicacaoPortariaBarragem) {
		this.dtcPublicacaoPortariaBarragem = dtcPublicacaoPortariaBarragem;
	}

	public BigDecimal getNumVolumeAcumulacaoBarragem() {
		return numVolumeAcumulacaoBarragem;
	}

	public void setNumVolumeAcumulacaoBarragem(BigDecimal numVolumeAcumulacaoBarragem) {
		this.numVolumeAcumulacaoBarragem = numVolumeAcumulacaoBarragem;
	}

	public BigDecimal getNumDescargaFundo() {
		return numDescargaFundo;
	}

	public void setNumDescargaFundo(BigDecimal numDescargaFundo) {
		this.numDescargaFundo = numDescargaFundo;
	}

	public String getNomIntervencao() {
		return nomIntervencao;
	}

	public void setNomIntervencao(String nomIntervencao) {
		this.nomIntervencao = nomIntervencao;
	}

	public BigDecimal getNumVolumeMaximoAcumulado() {
		return numVolumeMaximoAcumulado;
	}

	public void setNumVolumeMaximoAcumulado(BigDecimal numVolumeMaximoAcumulado) {
		this.numVolumeMaximoAcumulado = numVolumeMaximoAcumulado;
	}

	public BigDecimal getNumAreaInundadaReservatorio() {
		return numAreaInundadaReservatorio;
	}

	public void setNumAreaInundadaReservatorio(BigDecimal numAreaInundadaReservatorio) {
		this.numAreaInundadaReservatorio = numAreaInundadaReservatorio;
	}

	public TipoBarragem getIdeTipoBarragem() {
		return ideTipoBarragem;
	}

	public void setIdeTipoBarragem(TipoBarragem ideTipoBarragem) {
		this.ideTipoBarragem = ideTipoBarragem;
	}

	public TipoTravessia getIdeTipoTravessia() {
		return ideTipoTravessia;
	}

	public void setIdeTipoTravessia(TipoTravessia ideTipoTravessia) {
		this.ideTipoTravessia = ideTipoTravessia;
	}

	public TipoIntervencao getTipoIntervencao() {
		return tipoIntervencao;
	}

	public void setTipoIntervencao(TipoIntervencao tipoIntervencao) {
		this.tipoIntervencao = tipoIntervencao;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeograficaFinal() {
		return ideLocalizacaoGeograficaFinal;
	}

	public void setIdeLocalizacaoGeograficaFinal(LocalizacaoGeografica ideLocalizacaoGeograficaFinal) {
		this.ideLocalizacaoGeograficaFinal = ideLocalizacaoGeograficaFinal;
	}

	public void setListaImovel(List<Imovel> listaImovel) {
		this.listaImovel = listaImovel;
	}

	public Integer getIndFinalidade() {
		return indFinalidade;
	}

	public void setIndFinalidade(Integer indFinalidade) {
		this.indFinalidade = indFinalidade;
	}

	public void setListaFinalidadeUsoAgua(List<TipoFinalidadeUsoAgua> listaFinalidadeUsoAgua) {
		this.listaFinalidadeUsoAgua = listaFinalidadeUsoAgua;
	}

	public boolean isLocalizacaoFinal() {
		return localizacaoFinal;
	}

	public void setLocalizacaoFinal(boolean localizacaoFinal) {
		this.localizacaoFinal = localizacaoFinal;
	}

	public Imovel getImovel() {
		return imovel;
	}

	public void setImovel(Imovel imovel) {
		this.imovel = imovel;
	}

	public void setListaFinalidadeUsoAguaCaptacao(List<TipoFinalidadeUsoAgua> listaFinalidadeUsoAguaCaptacao) {
		this.listaFinalidadeUsoAguaCaptacao = listaFinalidadeUsoAguaCaptacao;
	}

	public String getNumPortariaLicencaBarragem() {
		return numPortariaLicencaBarragem;
	}

	public void setNumPortariaLicencaBarragem(String numPortariaLicencaBarragem) {
		this.numPortariaLicencaBarragem = numPortariaLicencaBarragem;
	}

	public Collection<OutorgaLocalizacaoGeograficaFinalidade> getOutorgaLocalizacaoGeograficaFinalidadeCollection() {
		return outorgaLocalizacaoGeograficaFinalidadeCollection;
	}

	public void setOutorgaLocalizacaoGeograficaFinalidadeCollection(Collection<OutorgaLocalizacaoGeograficaFinalidade> outorgaLocalizacaoGeograficaFinalidadeCollection) {
		this.outorgaLocalizacaoGeograficaFinalidadeCollection = outorgaLocalizacaoGeograficaFinalidadeCollection;
	}

	public boolean isDesabilitado() {
		return desabilitado;
	}

	public void setDesabilitado(boolean desabilitado) {
		this.desabilitado = desabilitado;
	}

	public Integer getTempoCaptacao() {
		return tempoCaptacao;
	}

	public void setTempoCaptacao(Integer tempoCaptacao) {
		this.tempoCaptacao = tempoCaptacao;
	}
	
	public boolean isDesabilitaTempoVazao() {
		return desabilitaTempoVazao;
	}

	public void setDesabilitaTempoVazao(boolean desabilitaTempoVazao) {
		this.desabilitaTempoVazao = desabilitaTempoVazao;
	}

	public String getStrVazao() {
		return strVazao;
	}

	public void setStrVazao(String strVazao) {
		this.strVazao = strVazao;
	}

	public String getStrTempo() {
		return strTempo;
	}

	public void setStrTempo(String strTempo) {
		this.strTempo = strTempo;
	}

	public boolean isDesabilitaTipoBarragem() {
		return desabilitaTipoBarragem;
	}

	public void setDesabilitaTipoBarragem(boolean desabilitaTipoBarragem) {
		this.desabilitaTipoBarragem = desabilitaTipoBarragem;
	}

	public String getNomeImovel() {
		return nomeImovel;
	}

	public void setNomeImovel(String nomeImovel) {
		this.nomeImovel = nomeImovel;
	}

	public BigDecimal getNumVazaoFinal() {
		return numVazaoFinal;
	}

	public void setNumVazaoFinal(BigDecimal numVazaoFinal) {
		this.numVazaoFinal = numVazaoFinal;
	}

	public BigDecimal getNumAreaPulverizada() {
		return numAreaPulverizada;
	}

	public void setNumAreaPulverizada(BigDecimal numAreaPulverizada) {
		this.numAreaPulverizada = numAreaPulverizada;
	}

	public TipoIntervencao getTipoIntervencaoPai() {
		return tipoIntervencaoPai;
	}

	public void setTipoIntervencaoPai(TipoIntervencao tipoIntervencaoPai) {
		this.tipoIntervencaoPai = tipoIntervencaoPai;
	}

	public BigDecimal getNumAreaDessedentacaoAnimal() {
		return numAreaDessedentacaoAnimal;
	}

	public void setNumAreaDessedentacaoAnimal(BigDecimal numAreaDessedentacaoAnimal) {
		this.numAreaDessedentacaoAnimal = numAreaDessedentacaoAnimal;
	}
	
	public BigDecimal getNumAreaEmpreendimento() {
		return numAreaEmpreendimento;
	}

	public void setNumAreaEmpreendimento(BigDecimal numAreaEmpreendimento) {
		this.numAreaEmpreendimento = numAreaEmpreendimento;
	}

	public Boolean getIndAbastecimentoEmDistritoIndustrial() {
		return indAbastecimentoEmDistritoIndustrial;
	}

	public void setIndAbastecimentoEmDistritoIndustrial(Boolean indAbastecimentoEmDistritoIndustrial) {
		this.indAbastecimentoEmDistritoIndustrial = indAbastecimentoEmDistritoIndustrial;
	}
	
	@Override
    public OutorgaLocalizacaoGeografica clone() throws CloneNotSupportedException {
    	return (OutorgaLocalizacaoGeografica) super.clone();
    }

}