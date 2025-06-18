package br.gov.ba.seia.dto;

import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.ReservaLegal;
import br.gov.ba.seia.entity.TipoImovelRequerimento;

public class LocalizacaoGeograficaReqDTO {
	
	private ImovelRural imovelRural;
	private Requerimento requerimento;
	private TipoImovelRequerimento tipoImovelRequerimento;
	private ReservaLegal reservaLegal;
	private Empreendimento empreendimento;
	//CAMPOS INSERIDOS PARA DIMINUIR O NUMERO DE PARAMETROS DO METODO "ImovelRuralService.listarPorCriteriaDemanda(*)" QUE CONSULTA OS IMOVEIS RURAIS CADASTRADOS.
	private Pessoa proprietarioOuProcurador;
	private Pessoa solicitante;
	private Pessoa requerente;
	private Boolean vinculaRequerenteDeProcurador;
	
	

	public LocalizacaoGeograficaReqDTO () {
		imovelRural = null;
		reservaLegal = null;
		empreendimento = null;
		proprietarioOuProcurador = null;
		solicitante = null;
	}	
	
	
	public ImovelRural getImovelRural() {
		return imovelRural;
	}
	public void setImovelRural(ImovelRural imovelRural) {
		this.imovelRural = imovelRural;
	}
	public ReservaLegal getReservaLegal() {
		return reservaLegal;
	}
	public void setReservaLegal(ReservaLegal reservaLegal) {
		this.reservaLegal = reservaLegal;
	}


	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}


	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
	}


	public Pessoa getProprietarioOuProcurador() {
		return proprietarioOuProcurador;
	}


	public void setProprietarioOuProcurador(Pessoa proprietarioOuProcurador) {
		this.proprietarioOuProcurador = proprietarioOuProcurador;
	}


	public Pessoa getSolicitante() {
		return solicitante;
	}


	public void setSolicitante(Pessoa solicitante) {
		this.solicitante = solicitante;
	}


	public Boolean getVinculaRequerenteDeProcurador() {
		return vinculaRequerenteDeProcurador;
	}


	public void setVinculaRequerenteDeProcurador(
			Boolean vinculaRequerenteDeProcurador) {
		this.vinculaRequerenteDeProcurador = vinculaRequerenteDeProcurador;
	}


	public Pessoa getRequerente() {
		return requerente;
	}


	public void setRequerente(Pessoa requerente) {
		this.requerente = requerente;
	}


	public Requerimento getRequerimento() {
		return requerimento;
	}


	public void setRequerimento(Requerimento requerimento) {
		this.requerimento = requerimento;
	}


	public TipoImovelRequerimento getTipoImovelRequerimento() {
		return tipoImovelRequerimento;
	}


	public void setTipoImovelRequerimento(
			TipoImovelRequerimento tipoImovelRequerimento) {
		this.tipoImovelRequerimento = tipoImovelRequerimento;
	}
	
	

}
