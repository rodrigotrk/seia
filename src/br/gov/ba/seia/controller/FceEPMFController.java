package br.gov.ba.seia.controller;

import org.apache.log4j.Level;

import br.gov.ba.seia.dto.DadoConcedidoFceImpl;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.FceFlorestal;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.enumerator.MotivoNotificacaoEnum;
import br.gov.ba.seia.enumerator.PerguntaEnum;
import br.gov.ba.seia.enumerator.TipoEspecieFlorestalEnum;
import br.gov.ba.seia.facade.FceFlorestalAbstractService;
import br.gov.ba.seia.facade.FceServiceFacade;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Util;

public class FceEPMFController extends FceFlorestalAbstractController {

	public FceEPMFController(Requerimento requerimento, DocumentoObrigatorio documentoObrigatorio, DadoConcedidoFceImpl dadoConcedidoImpl, FceServiceFacade fceServiceFacade, FceFlorestalAbstractService fceFlorestalService) {
		super(requerimento, documentoObrigatorio, dadoConcedidoImpl, fceServiceFacade, fceFlorestalService);
	}
	
	@Override
	public void init() {
		try{
			super.init();
			carregarEpmf();
			verificarEdicao();
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public void carregarEpmf() {
		super.setLabelAto("Execução do Plano de Manejo Florestal Sustentável");
		super.setLabelAreaTotal("*Área total do plano de manejo(ha)");
		super.setRenderedAreaUnidadeProducao(true);
	}
	
	public void carregarEspecieFlorestalAutorizacao() throws Exception{
		if(!Util.isNullOuVazio(super.fceFlorestal)){
			super.especieFlorestalAutorizacaoList = super.fceFlorestalService.buscarEspecieFlorestalAutorizacaoNativa(super.fceFlorestal, TipoEspecieFlorestalEnum.NATIVA);
			super.fceFlorestalService.removerListaEspecieSupressaoEdicao(this);
		}
	}
	
	@Override
	public void verificarEdicao() {
		try {
			if(!Util.isNullOuVazio(super.getDocumentoObrigatorio())){
				if(!Util.isNullOuVazio(super.fce)) {
					super.fceFlorestal = super.fceFlorestalService.buscarFceFlorestal(super.fce);
					carregarEspecieFlorestalAutorizacao();
				}
			}
			if(!isFceSalvo()){
				super.iniciarFce(documentoObrigatorio);
			}
		} 
		catch (Exception e) {
			Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@Override
	public boolean validarAba() {
		FceFlorestal fceFlorestal = super.getFceFlorestal();
		
		boolean retorno = true;
		
		if(!Util.isNullOuVazio(fceFlorestal.getValArea()) && !Util.isNullOuVazio(fceFlorestal.getValAreaUndProducao()) 
				&& fceFlorestal.getValArea().compareTo(fceFlorestal.getValAreaUndProducao()) == -1 ) {
			retorno = false;
			MensagemUtil.alerta("Área da unidade de produção não pode ser maior do que a área total!");
		}
		
		if(Util.isNullOuVazio(fceFlorestal.getValArea())) {
			retorno = false;
			MensagemUtil.alerta("O campo Área total(ha) é de preenchimento obrigatório!");
		}
		
		if(Util.isNullOuVazio(fceFlorestal.getValAreaUndProducao())) {
			retorno = false;
			MensagemUtil.alerta("O campo Área da Unidade de Produção(ha) é de preenchimento obrigatório!");
		}
		
		if(isExibirPerguntaMaterialLenhoso()) {
			if (Util.isNullOuVazio(fceFlorestal.getIndMaterialLenhoso())) {
				retorno = false;
				MensagemUtil.alerta("O campo Haverá rendimento de material lenhoso é de preenchimento obrigatório!");
			}
			else {
				if(fceFlorestal.getIndMaterialLenhoso()) {
					if(!validarEspecieFlorestalAutorizacaoList()) {
						retorno = false;
					}
				}
			}
		} else {
			if(!validarEspecieFlorestalAutorizacaoList()) {
				retorno = false;
			}
		}
		
		return retorno;
	}
	
	@Override
	public boolean isExibirPerguntaMaterialLenhoso() {
		return false;
	}

	@Override
	protected MotivoNotificacaoEnum getMotivoNotificacao() {
		return MotivoNotificacaoEnum.SHAPE_EPMF;
	}

	@Override
	protected PerguntaEnum getPergunta() {
		return PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA5_D1_19;
	}

	@Override
	public String getLabelArea() {
		return "Área do plano de manejo (ha)";
	}
	
}