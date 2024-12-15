package br.gov.ba.seia.dto;

import java.math.BigDecimal;
import java.util.List;

import br.gov.ba.seia.entity.Requerimento;

public class GerarDaeDTO {

	private Requerimento requerimento;
	
	private List<DetalhamentoDaeDTO> listaDetalhamentoDaeDTO;
	
	private List<ParcelaDaeDTO> parcelaDaeDTO;
	
	private List<ParcelaDaeDTO> parcelaUnica;
	
	private boolean indParcela;
	
	private Integer qtdeParcelas;
	
	private ParcelaDaeDTO daeVencido;
	
	private Integer ideMemoriaCalculoDae;
	
	private boolean indAlterarVolume;
	
	private BigDecimal valVolumeReferencia;
	
	private String dscCaminhoAqruivoParecerTecnico;
	
	private String fileName;
	
	private Double valorPecuniario;
	
	private boolean isGerarDae;

	public Requerimento getRequerimento() {
		return requerimento;
	}

	public void setRequerimento(Requerimento requerimento) {
		this.requerimento = requerimento;
	}

	public List<DetalhamentoDaeDTO> getListaDetalhamentoDaeDTO() {
		return listaDetalhamentoDaeDTO;
	}

	public void setListaDetalhamentoDaeDTO(
			List<DetalhamentoDaeDTO> listaDetalhamentoDaeDTO) {
		this.listaDetalhamentoDaeDTO = listaDetalhamentoDaeDTO;
	}

	public List<ParcelaDaeDTO> getParcelaDaeDTO() {
		return parcelaDaeDTO;
	}

	public void setParcelaDaeDTO(List<ParcelaDaeDTO> parcelaDaeDTO) {
		this.parcelaDaeDTO = parcelaDaeDTO;
	}

	public boolean isIndParcela() {
		return indParcela;
	}

	public void setIndParcela(boolean indParcela) {
		this.indParcela = indParcela;
	}

	public Integer getQtdeParcelas() {
		return qtdeParcelas;
	}

	public void setQtdeParcelas(Integer qtdeParcelas) {
		this.qtdeParcelas = qtdeParcelas;
	}

	public ParcelaDaeDTO getDaeVencido() {
		return daeVencido;
	}

	public void setDaeVencido(ParcelaDaeDTO daeVencido) {
		this.daeVencido = daeVencido;
	}

	public Integer getIdeMemoriaCalculoDae() {
		return ideMemoriaCalculoDae;
	}

	public void setIdeMemoriaCalculoDae(Integer ideMemoriaCalculoDae) {
		this.ideMemoriaCalculoDae = ideMemoriaCalculoDae;
	}

	public List<ParcelaDaeDTO> getParcelaUnica() {
		return parcelaUnica;
	}

	public void setParcelaUnica(List<ParcelaDaeDTO> parcelaUnica) {
		this.parcelaUnica = parcelaUnica;
	}

	public boolean isIndAlterarVolume() {
		return indAlterarVolume;
	}

	public void setIndAlterarVolume(boolean indAlterarVolume) {
		this.indAlterarVolume = indAlterarVolume;
	}

	public BigDecimal getValVolumeReferencia() {
		return valVolumeReferencia;
	}

	public void setValVolumeReferencia(BigDecimal valVolumeReferencia) {
		this.valVolumeReferencia = valVolumeReferencia;
	}

	public String getDscCaminhoAqruivoParecerTecnico() {
		return dscCaminhoAqruivoParecerTecnico;
	}

	public void setDscCaminhoAqruivoParecerTecnico(
			String dscCaminhoAqruivoParecerTecnico) {
		this.dscCaminhoAqruivoParecerTecnico = dscCaminhoAqruivoParecerTecnico;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Double getValorPecuniario() {
		return valorPecuniario;
	}

	public void setValorPecuniario(Double valorPecuniario) {
		this.valorPecuniario = valorPecuniario;
	}

	public boolean isGerarDae() {
		return isGerarDae;
	}

	public void setGerarDae(boolean isGerarDae) {
		this.isGerarDae = isGerarDae;
	}
	
}
