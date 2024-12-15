package br.gov.ba.seia.controller;

import javax.inject.Named;

import br.gov.ba.seia.entity.TipoRequerimento;
import br.gov.ba.seia.enumerator.TipoRequerimentoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.SeiaControllerAb;

@Named
@ViewScoped
public class SetaTipoRequerimentoController extends SeiaControllerAb {

	public SetaTipoRequerimentoController() {}
	
	/**
	 * Seta o tipo de requerimento no contexto como Regulação Ambiental do Empreendimento e retorna a URL '/paginas/identificar-papel/identificar-papel.xhtml'
	 * @return
	 */
	public String setaTipoReqComoReqUnicoNoContexto() {
		ContextoUtil.getContexto().setTipoRequerimento(new TipoRequerimento(TipoRequerimentoEnum.REGULACAO_AMBIENTAL_DO_EMPREENDIMENTO.getIde(), 
														   					TipoRequerimentoEnum.REGULACAO_AMBIENTAL_DO_EMPREENDIMENTO.getNomeTipoRequerimento()
														   					)
													  );
		ContextoUtil.getContexto().setLabelTitutoRequerimento("Requerimento Único");
		return "/paginas/identificar-papel/identificar-papel.xhtml";
	}
}