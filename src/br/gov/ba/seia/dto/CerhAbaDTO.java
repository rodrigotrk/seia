package br.gov.ba.seia.dto;

import java.util.ArrayList;
import java.util.List;

import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.enumerator.TipoUsoRecursoHidricoEnum;

public class CerhAbaDTO {
	
	// Localizacao que séra manipulada em memória
	protected LocalizacaoGeografica localizacaoGeografica;
	protected TipoUsoRecursoHidricoEnum tipoUsoRecursoHidricoEnum;
	
	private CerhCaracterizacaoDTO caracterizacaoDTO;
	private List<CerhCaracterizacaoDTO> listaCaracterizacaoDTO;
	
	public CerhAbaDTO() {
		localizacaoGeografica = new LocalizacaoGeografica();
		listaCaracterizacaoDTO = new ArrayList<CerhCaracterizacaoDTO>();
	}
	
	public CerhAbaDTO(TipoUsoRecursoHidricoEnum tipoUsoRecursoHidricoEnum) {
		this();
		this.tipoUsoRecursoHidricoEnum = tipoUsoRecursoHidricoEnum;
	}

	public List<LocalizacaoGeografica> getLista(){
		List<LocalizacaoGeografica> lista = new ArrayList<LocalizacaoGeografica>();
		lista.add(localizacaoGeografica);
		return lista;
	}
	
	public LocalizacaoGeografica getLocalizacaoGeografica() {
		return localizacaoGeografica;
	}

	public void setLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica) {
		this.localizacaoGeografica = localizacaoGeografica;
	}

	public TipoUsoRecursoHidricoEnum getTipoUsoRecursoHidricoEnum() {
		return tipoUsoRecursoHidricoEnum;
	}

	public List<CerhCaracterizacaoDTO> getListaCaracterizacaoDTO() {
		return listaCaracterizacaoDTO;
	}

	public void setListaCaracterizacaoDTO(List<CerhCaracterizacaoDTO> listaCaracterizacaoDTO) {
		this.listaCaracterizacaoDTO = listaCaracterizacaoDTO;
	}

	public CerhCaracterizacaoDTO getCaracterizacaoDTO() {
		return caracterizacaoDTO;
	}

	public void setCaracterizacaoDTO(CerhCaracterizacaoDTO caracterizacaoDTO) {
		this.caracterizacaoDTO = caracterizacaoDTO;
	}
	
}
