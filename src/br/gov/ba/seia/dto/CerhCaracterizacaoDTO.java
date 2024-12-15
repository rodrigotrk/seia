package br.gov.ba.seia.dto;

import java.math.BigDecimal;
import java.util.Collection;

import br.gov.ba.seia.entity.CerhLocalizacaoGeografica;
import br.gov.ba.seia.entity.CerhProcesso;
import br.gov.ba.seia.entity.CerhTipoUso;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.interfaces.CerhFinalidadeUsoAguaInterface;
import br.gov.ba.seia.util.Util;

public class CerhCaracterizacaoDTO implements Cloneable, Comparable<CerhCaracterizacaoDTO> {
	
	private Collection<TipoFinalidadeUsoAgua> tipoFinalidadeUsoAguaCollection; 
	
	private CerhLocalizacaoGeografica cerhLocalizacaoGeografica;

	private boolean visualizacao;
	private boolean confirmaNomeRio;
	private boolean oriundoAnaliseTecnica;
	
	private BigDecimal mediaVazaoDiaria;
	private BigDecimal vazaoAnual;
	
	public CerhCaracterizacaoDTO() {
	}
	
	public CerhCaracterizacaoDTO(CerhLocalizacaoGeografica cerhLocalizacaoGeografica) {
		this.cerhLocalizacaoGeografica = cerhLocalizacaoGeografica;
	}
	
	public CerhCaracterizacaoDTO(CerhLocalizacaoGeografica cerhLocalizacaoGeografica, CerhProcesso cerhProcesso) {
		this(cerhLocalizacaoGeografica);
		this.oriundoAnaliseTecnica = !Util.isNull(cerhLocalizacaoGeografica.getIdeCerhProcesso()) && isDadoConcedido(cerhProcesso);
	}
	
	public CerhCaracterizacaoDTO(LocalizacaoGeografica localizacaoGeografica, CerhTipoUso cerhTipoUso) {
		this.cerhLocalizacaoGeografica = new CerhLocalizacaoGeografica(localizacaoGeografica, cerhTipoUso);
	}

	public String getHeader(CerhFinalidadeUsoAguaInterface finalidade) {
		String nomTipoFinalidadeUsoAgua = finalidade.getIdeTipoFinalidadeUsoAgua().getNomTipoFinalidadeUsoAgua();
		if(finalidade.getIdeTipoFinalidadeUsoAgua().isFinalidadeOutrosUsos()) {
			return nomTipoFinalidadeUsoAgua + " Usos";
		} 
		return Util.getString("cerh_informacaoes_uso", nomTipoFinalidadeUsoAgua);
	}

	public boolean isPossivelCaracterizar() {
		return !Util.isNull(cerhLocalizacaoGeografica.getIdeCerhTipoUso()) 
				&& !Util.isNull(cerhLocalizacaoGeografica.getIdeCerhTipoUso().getIdeCerhTipoUso()); 
	}
	
	public boolean isOriundoAnaliseTecnica() {
		return oriundoAnaliseTecnica;
	}
	
	public boolean isDadoConcedido(CerhProcesso cerhProcesso) {
		return !Util.isNull(cerhProcesso.getIndDadosConcedidos()) && cerhProcesso.getIndDadosConcedidos();
	}

	public boolean isTipoUsoCorreto(CerhProcesso cerhProcesso) {
		for (CerhTipoUso cerhTipoUso : cerhProcesso.getCerhTipoUsoCollection()) {
			if(!Util.isNull(cerhLocalizacaoGeografica.getIdeCerhTipoUso())
					&& cerhTipoUso.getIdeTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico().equals(cerhLocalizacaoGeografica.getIdeCerhTipoUso().getIdeTipoUsoRecursoHidrico().getIdeTipoUsoRecursoHidrico())){
				return true;
			}
		}
		return false;
	}
	
	public boolean isVisualizacao() {
		return visualizacao;
	}

	public void setVisualizacao(boolean visualizacao) {
		this.visualizacao = visualizacao;
	}

	public boolean isConfirmaNomeRio() {
		return confirmaNomeRio;
	}

	public void setConfirmaNomeRio(boolean confirmaNomeRio) {
		this.confirmaNomeRio = confirmaNomeRio;
	}

	public Collection<TipoFinalidadeUsoAgua> getTipoFinalidadeUsoAguaCollection() {
		return tipoFinalidadeUsoAguaCollection;
	}

	public void setTipoFinalidadeUsoAguaCollection(Collection<TipoFinalidadeUsoAgua> tipoFinalidadeUsoAguaCollection) {
		this.tipoFinalidadeUsoAguaCollection = tipoFinalidadeUsoAguaCollection;
	}
	
	@Override
	public CerhCaracterizacaoDTO clone() throws CloneNotSupportedException{
		return (CerhCaracterizacaoDTO) super.clone();
	}

	public CerhLocalizacaoGeografica getCerhLocalizacaoGeografica() {
		return cerhLocalizacaoGeografica;
	}

	public void setCerhLocalizacaoGeografica(CerhLocalizacaoGeografica cerhLocalizacaoGeografica) {
		this.cerhLocalizacaoGeografica = cerhLocalizacaoGeografica;
	}
	
	public BigDecimal getMediaVazaoDiaria() {
		return mediaVazaoDiaria;
	}
	
	public void setMediaVazaoDiaria(BigDecimal mediaVazaoDiaria) {
		this.mediaVazaoDiaria = mediaVazaoDiaria;
	}

	public BigDecimal getVazaoAnual() {
		return vazaoAnual;
	}

	public void setVazaoAnual(BigDecimal vazaoAnual) {
		this.vazaoAnual = vazaoAnual;
	}
	
	@Override
	public int compareTo(CerhCaracterizacaoDTO o) {
		if(Util.isNull(o)){
			return 0;
		} else if(!Util.isNullOuVazio(o.getCerhLocalizacaoGeografica().getIdeCerhProcesso())
				&& !Util.isNullOuVazio(o.getCerhLocalizacaoGeografica().getIdeCerhProcesso().getIndDadosConcedidos())){
			if(o.getCerhLocalizacaoGeografica().getIdeCerhProcesso().getIndDadosConcedidos()){
				return -1;
			}
			else{
				return 1;
			}
		}
		return 0;
	}

}
