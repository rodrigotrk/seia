package br.gov.ba.seia.controller;

import javax.ejb.EJB;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.Pauta;
import br.gov.ba.seia.entity.TipoPauta;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.enumerator.TipoPautaEnum;
import br.gov.ba.seia.service.AreaService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;


public class FuncionalidadePautaController {
	
	@EJB
	private AreaService areaService;
	
	protected Area area;
	protected VwConsultaProcesso vwProcesso;
	protected Pauta pauta;
	
	public void load(VwConsultaProcesso vwProcesso, Pauta pauta) {
		try{
			this.pauta = pauta;
			this.vwProcesso = vwProcesso;
			carregarArea(pauta);
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private void carregarArea(Pauta pauta) throws Exception {
		
		if(Util.isNull(pauta)) {
			area = areaService.buscarAreaPorPessoaFisica(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica());
		}
		else{
			area = new TipoPauta(TipoPautaEnum.PAUTA_AREA.getTipo()).equals(pauta.getIdeTipoPauta()) ? pauta.getIdeArea() : pauta.getIdePessoaFisica().getIdeArea();
		}
	}

	public VwConsultaProcesso getVwProcesso() {
		return vwProcesso;
	}

	public void setVwProcesso(VwConsultaProcesso vwProcesso) {
		this.vwProcesso = vwProcesso;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
}