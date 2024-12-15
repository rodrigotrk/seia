package br.gov.ba.seia.dto;

import br.gov.ba.seia.entity.Dae;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.GeoRpga;
import br.gov.ba.seia.entity.PagamentoDae;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.SituacaoDae;

public class CerhConsultarDaeDto {
	
	private Pessoa idePessoaRequerente;
	
	private Empreendimento ideEmpreendimento;
	
	private GeoRpga geoRpga;
	
	private Dae cerhDae;
	
	private PagamentoDae cerhPagamentoDae;

	private SituacaoDae cerhSituacaoDae;

	public Pessoa getIdePessoaRequerente() {
		return idePessoaRequerente;
	}

	public void setIdePessoaRequerente(Pessoa idePessoaRequerente) {
		this.idePessoaRequerente = idePessoaRequerente;
	}

	public Empreendimento getIdeEmpreendimento() {
		return ideEmpreendimento;
	}

	public void setIdeEmpreendimento(Empreendimento ideEmpreendimento) {
		this.ideEmpreendimento = ideEmpreendimento;
	}

	public GeoRpga getGeoRpga() {
		return geoRpga;
	}

	public void setGeoRpga(GeoRpga geoRpga) {
		this.geoRpga = geoRpga;
	}

	public Dae getCerhDae() {
		return cerhDae;
	}

	public void setCerhDae(Dae cerhDae) {
		this.cerhDae = cerhDae;
	}

	public PagamentoDae getCerhPagamentoDae() {
		return cerhPagamentoDae;
	}

	public void setCerhPagamentoDae(PagamentoDae cerhPagamentoDae) {
		this.cerhPagamentoDae = cerhPagamentoDae;
	}

	public SituacaoDae getCerhSituacaoDae() {
		return cerhSituacaoDae;
	}

	public void setCerhSituacaoDae(SituacaoDae cerhSituacaoDae) {
		this.cerhSituacaoDae = cerhSituacaoDae;
	}
	
	
	
	
	

}
