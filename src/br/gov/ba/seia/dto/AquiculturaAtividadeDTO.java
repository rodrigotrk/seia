package br.gov.ba.seia.dto;

import java.math.BigDecimal;
import java.util.List;

import br.gov.ba.seia.entity.AquiculturaTipoAtividade;
import br.gov.ba.seia.entity.EspecieAquiculturaTipoAtividade;
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividade;
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica;
import br.gov.ba.seia.entity.TipoLocalizacaoCultivo;
import br.gov.ba.seia.enumerator.AquiculturaTipoAtividadeEnum;

/**
 * DTO que representa a {@link AquiculturaTipoAtividade} que vai ser caracterizada.
 * <li>1 - Psicultura</li>
 * <li>2 - Carcinicultura</li>
 * <li>3 - Ranicultura</li>
 * <li>4 - Algicutura</li>
 * <li>5 - Malococultura</li>
 * 
 * @author eduardo (eduardo.fernandes@zcr.com.br)
 * @since 27/10/2015
 */
public class AquiculturaAtividadeDTO implements Cloneable {

	// Tipo de Atividade para Caracterizar
	private AquiculturaTipoAtividade tipoAtividade;

	// Poligonal do Cultivo;
	private List<FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica> listaPoligonalCultivo;
	
	private FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica poligonalCultivo;
	
	private BigDecimal somatorioAreaPoligonal;

	// Espécies para cadastradar no FCE - Licença para Aquicultura;
	private List<EspecieAquiculturaTipoAtividade> listaEspecie;
	private List<EspecieAquiculturaTipoAtividade> listaEspecieSelecionada;

	// Lista de Caracterização do Cultivo;
	private List<FceAquiculturaLicencaTipoAtividade> listaCaracterizacaoCultivo;
	
	// Lista do Tipo Localização Cultivo
	private List<TipoLocalizacaoCultivo> listaTipoLocalizacaoCultivo;

	// Somatório exibido no rodapé da aba;
	private BigDecimal somatorioVolume;
	private BigDecimal somatorioAreaCultivo;

	private boolean podePreencherNaLicenca;
	
	public AquiculturaAtividadeDTO() {
		this.podePreencherNaLicenca = true;
	}
	
	public AquiculturaAtividadeDTO(AquiculturaTipoAtividadeEnum aquiculturaTipoAtividadeEnum) {
		this.tipoAtividade = new AquiculturaTipoAtividade(aquiculturaTipoAtividadeEnum);
	}

	/* Getters && Setters */
	public List<FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica> getListaPoligonalCultivo() {
		return listaPoligonalCultivo;
	}

	public void setListaPoligonalCultivo(List<FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica> listaPoligonalCultivo) {
		this.listaPoligonalCultivo = listaPoligonalCultivo;
	}

	public FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica getPoligonalCultivo() {
		return poligonalCultivo;
	}

	public void setPoligonalCultivo(FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica poligonalCultivo) {
		this.poligonalCultivo = poligonalCultivo;
	}

	public List<EspecieAquiculturaTipoAtividade> getListaEspecie() {
		return listaEspecie;
	}

	public void setListaEspecie(List<EspecieAquiculturaTipoAtividade> listaEspecie) {
		this.listaEspecie = listaEspecie;
	}

	public List<EspecieAquiculturaTipoAtividade> getListaEspecieSelecionada() {
		return listaEspecieSelecionada;
	}

	public void setListaEspecieSelecionada(List<EspecieAquiculturaTipoAtividade> listaEspecieSelecionada) {
		this.listaEspecieSelecionada = listaEspecieSelecionada;
	}

	public List<FceAquiculturaLicencaTipoAtividade> getListaCaracterizacaoCultivo() {
		return listaCaracterizacaoCultivo;
	}

	public void setListaCaracterizacaoCultivo(List<FceAquiculturaLicencaTipoAtividade> listaCaracterizacaoCultivo) {
		this.listaCaracterizacaoCultivo = listaCaracterizacaoCultivo;
	}

	public BigDecimal getSomatorioVolume() {
		return somatorioVolume;
	}

	public void setSomatorioVolume(BigDecimal somatorioVolume) {
		this.somatorioVolume = somatorioVolume;
	}

	public BigDecimal getSomatorioAreaCultivo() {
		return somatorioAreaCultivo;
	}

	public void setSomatorioAreaCultivo(BigDecimal somatorioAreaCultivo) {
		this.somatorioAreaCultivo = somatorioAreaCultivo;
	}

	public AquiculturaTipoAtividade getTipoAtividade() {
		return tipoAtividade;
	}

	public void setTipoAtividade(AquiculturaTipoAtividade tipoAtividade) {
		this.tipoAtividade = tipoAtividade;
	}

	public BigDecimal getSomatorioAreaPoligonal() {
		return somatorioAreaPoligonal;
	}

	public void setSomatorioAreaPoligonal(BigDecimal somatorioAreaPoligonal) {
		this.somatorioAreaPoligonal = somatorioAreaPoligonal;
	}
	
	public List<TipoLocalizacaoCultivo> getListaTipoLocalizacaoCultivo() {
		return listaTipoLocalizacaoCultivo;
	}

	public void setListaTipoLocalizacaoCultivo(List<TipoLocalizacaoCultivo> listaTipoLocalizacaoCultivo) {
		this.listaTipoLocalizacaoCultivo = listaTipoLocalizacaoCultivo;
	}

	public boolean isPodePreencherNaLicenca() {
		return podePreencherNaLicenca;
	}

	public void setPodePreencherNaLicenca(boolean podePreencherNaLicenca) {
		this.podePreencherNaLicenca = podePreencherNaLicenca;
	}
	
	public AquiculturaAtividadeDTO clone() throws CloneNotSupportedException{
		return (AquiculturaAtividadeDTO) super.clone();
	}
}