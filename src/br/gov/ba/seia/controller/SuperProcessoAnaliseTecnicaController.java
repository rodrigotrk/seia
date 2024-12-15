package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.dto.DadoConcedidoFceImpl;
import br.gov.ba.seia.dto.DadoConcedidoImpl;
import br.gov.ba.seia.entity.AnaliseTecnica;
import br.gov.ba.seia.entity.ControleProcessoAto;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.Florestal;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.StatusProcessoAto;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.PerguntaEnum;
import br.gov.ba.seia.enumerator.StatusProcessoAtoEnum;
import br.gov.ba.seia.facade.AnaliseTecnicaServiceFacade;
import br.gov.ba.seia.interfaces.DadoConcedidoInterface;
import br.gov.ba.seia.service.FlorestalService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.fce.FceUtil;

public class SuperProcessoAnaliseTecnicaController {
	
	@EJB
	protected AnaliseTecnicaServiceFacade analiseTecnicaServiceFacade;
	
	@EJB
	private FlorestalService florestalService;
	
	protected VwConsultaProcesso vwProcesso;
	protected AnaliseTecnica analiseTecnica;
	protected Collection<Fce> listaFce;
	protected Collection<DadoConcedidoInterface> listaDadoConcedido;
	protected Collection<StatusProcessoAto> listaStatusProcessoAto;
	protected Collection<Fce> listaOutorgasConcluidos;
	
	protected void load(VwConsultaProcesso vwProcesso) throws Exception{
		limpar();
		this.vwProcesso = vwProcesso;
		buscarAnaliseTecnicaBy(this.vwProcesso.getIdeProcesso());
		listarFceByAnaliseTecnica();
		listarStatusProcessoAto();
		verificarDadoConcedido();
	}

	private void limpar() {
		analiseTecnica = null;
		listaFce = null;
		listaDadoConcedido = null;
		listaStatusProcessoAto = null;
		listaOutorgasConcluidos = null;
	}
	
	public void abrirDadoConcedido(DadoConcedidoInterface dadoConcedido, boolean visualizacao) {
		DadoConcedidoImpl dado = ((DadoConcedidoImpl) dadoConcedido);
		if(dado.isAtoRealocacaoReservaLegal()) {
			RelocacaoReservaLegalController relocacaoReservaLegalController = (RelocacaoReservaLegalController) SessaoUtil.recuperarManagedBean("#{relocacaoReservaLegalController}", RelocacaoReservaLegalController.class);
			relocacaoReservaLegalController.load(dado, visualizacao);
		}
		else if(dado.isAtoAutorizacaoSupressaoVegetacao()) {
			DadoConcedidoAsvController dadoConcedidoController = (DadoConcedidoAsvController) SessaoUtil.recuperarManagedBean("#{dadoConcedidoAsvController}", DadoConcedidoAsvController.class);
			
			try {
				AsvDadosGeraisController asvDadosGeraisController = (AsvDadosGeraisController) SessaoUtil.recuperarManagedBean("#{asvDadosGeraisController}", AsvDadosGeraisController.class);
				
				asvDadosGeraisController.setIsNotFceASV(true);
				if(visualizacao){
					FceUtil.visualizarFce(analiseTecnicaServiceFacade.listarFceParaASV(analiseTecnica, null).get(0));
				}else{
					FceUtil.abrirFce(analiseTecnicaServiceFacade.listarFceParaASV(analiseTecnica, null).get(0));
					
				}
			} catch (Exception e) {
				
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
			
			dadoConcedidoController.load(dado, visualizacao);
			
		} else if(dado.isAMC()) {
			AutorizacaoManejoCabrucaController autorizacaoManejoController = (AutorizacaoManejoCabrucaController) SessaoUtil.recuperarManagedBean("#{autorizacaoManejoCabrucaController}", AutorizacaoManejoCabrucaController.class);
			autorizacaoManejoController.load(dado, visualizacao);
		}
		
	}

	private void verificarDadoConcedido() throws Exception {
		if(!isRenderedDadoConcedido()) {
			listaDadoConcedido = new ArrayList<DadoConcedidoInterface>();
		}
		for(ProcessoAto pa : analiseTecnicaServiceFacade.listarProcessoAtoPor(vwProcesso, this)) {
			if(pa.getAtoAmbiental().isARRL()) {
				if(perguntaRespondida(PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA5_D1_14)){
					listaDadoConcedido.add(new DadoConcedidoImpl(pa, !Util.isNullOuVazio(analiseTecnicaServiceFacade.listarReservaLegalConcedida(pa))));
					break;
				}
			}else if(pa.getAtoAmbiental().isAMC()) {
				if(perguntaRespondida(PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA5_D1_1p11)){
					listaDadoConcedido.add(new DadoConcedidoImpl(pa, analiseTecnicaServiceFacade.existeProcessoAtoConcedido(pa)));
					break;
				}
			}
			else if(pa.getAtoAmbiental().isASV()) {
				if(perguntaRespondida(PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA5_D1_15)){
					listaDadoConcedido.add(new DadoConcedidoImpl(pa, analiseTecnicaServiceFacade.existeProcessoAtoConcedido(pa)));
					break;
				}
			}
		}
	}

	private boolean perguntaRespondida(PerguntaEnum perguntaEnum) throws Exception {
		Collection<PerguntaRequerimento> listaPergunta = analiseTecnicaServiceFacade.buscarPerguntaRequerimento(vwProcesso, perguntaEnum);
		if(!Util.isNullOuVazio(listaPergunta)) {
			for (PerguntaRequerimento pr : listaPergunta) {
				if(pr.getIndResposta()) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 3. Ordenar lista de FCEs: Primeiro tipologias e depois finalidades
	 * @author eduardo (eduardo.fernandes@avansys.com.br)
	 * @since 07/03/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7470">#7470</a>
	 */
	protected void ordenarListaFce() {
		Fce fceCaptacaoSubterranea = null;
		Fce fceCaptacaoSuperficial = null;
		Fce fceLancamentoEfluente = null;
		Fce fceBarragem = null;
		Fce fceCanais = null;
		if(!Util.isNullOuVazio(listaFce) && listaFce.size() > 0){
			for(Fce fce : listaFce){
				if(!Util.isNullOuVazio(fce)){
					if(FceUtil.isFceOutorgaCaptacaoSubterranea(fce.getIdeDocumentoObrigatorio())){
						fceCaptacaoSubterranea = fce;
					} else if(FceUtil.isFceOutorgaCaptacaoSuperficial(fce.getIdeDocumentoObrigatorio())){
						fceCaptacaoSuperficial = fce;
					} else if(FceUtil.isFceOutorgaLancamentoEfluentes(fce.getIdeDocumentoObrigatorio())){
						fceLancamentoEfluente = fce;
					} else if(FceUtil.isFceBarragem(fce.getIdeDocumentoObrigatorio())){
						fceBarragem = fce;
					}else if(FceUtil.isFceCanais(fce.getIdeDocumentoObrigatorio())){
						fceCanais = fce;
					}
				}
			}
			List<Fce> listaTemp = new ArrayList<Fce>();
			listaTemp.addAll(listaFce);
			listaFce.clear();
			if(!Util.isNull(fceCaptacaoSubterranea)){
				listaFce.add(fceCaptacaoSubterranea);
				listaTemp.remove(fceCaptacaoSubterranea);
			}
			if(!Util.isNull(fceCaptacaoSuperficial)){
				listaFce.add(fceCaptacaoSuperficial);
				listaTemp.remove(fceCaptacaoSuperficial);
			}
			if(!Util.isNull(fceLancamentoEfluente)){
				listaFce.add(fceLancamentoEfluente);
				listaTemp.remove(fceLancamentoEfluente);
			}
			if(!Util.isNull(fceBarragem)){
				listaFce.add(fceBarragem);
				listaTemp.remove(fceBarragem);
			}
			if(!Util.isNull(fceCanais)){
				listaFce.add(fceCanais);
				listaTemp.remove(fceCanais);
			}
			Collections.sort(listaTemp);
			listaFce.addAll(listaTemp);
		}
	}
	
	/**
	 * @author eduardo (eduardo.fernandes@avansys.com.br)
	 * @throws Exception
	 * @since 02/03/2016
	 */
	public void listarFceByAnaliseTecnica() throws Exception {
		if(isExisteAnaliseTecnica()){
			
			listaFce = analiseTecnicaServiceFacade.listarFce(analiseTecnica);
			
			if(!Util.isNullOuVazio(listaFce)){
				
				if(listaFce.size() == 1){
					listaFce.iterator().next().getIdeAnaliseTecnica().setIdeProcesso(analiseTecnica.getIdeProcesso());
				}
				
				//Obtem o enquadramento do Requerimento
				analiseTecnicaServiceFacade.carregarDocumentoAtoFce(new ArrayList<Fce>(listaFce));
				ordenarListaFce();
				preparaListaDadoConcedidoFce();
			}
		}
	}

	protected void preparaListaDadoConcedidoFce() {
		
		if(!isRenderedDadoConcedido()) {
			listaDadoConcedido = new ArrayList<DadoConcedidoInterface>();
		}
		
		for(Fce fce : listaFce) {
			
			if(!isRenderedDadoConcedido()) {
				listaDadoConcedido.add(new DadoConcedidoFceImpl(fce));
				
			} else if(!listaDadoConcedido.contains(new DadoConcedidoFceImpl(fce))) {
				listaDadoConcedido.add(new DadoConcedidoFceImpl(fce));
			}
		}
	}
	
	/**
	 * @author eduardo (eduardo.fernandes@avansys.com.br)
	 * @return
	 * @since 02/03/2016
	 */
	protected boolean isExisteAnaliseTecnica() {
		return !Util.isNull(analiseTecnica);
	}
	
	private void buscarAnaliseTecnicaBy(Integer ideProcesso) throws Exception{
		analiseTecnica = analiseTecnicaServiceFacade.buscarAnaliseTecnica(ideProcesso);
	}
	
	private void listarStatusProcessoAto() throws Exception{
		listaStatusProcessoAto = analiseTecnicaServiceFacade.listarStatusProcessoAto();
	}
	
	public boolean isRenderedPrazo(StatusProcessoAto statusProcessoAto) {
		return new StatusProcessoAto(StatusProcessoAtoEnum.DEFERIDO_PELO_TECNICO.getId()).equals(statusProcessoAto);
	}
	
	public boolean isRenderedJustificativa(StatusProcessoAto statusProcessoAto) {
		return new StatusProcessoAto(StatusProcessoAtoEnum.INDEFERIDO_PELO_TECNICO.getId()).equals(statusProcessoAto);
	}
	
	public boolean isRenderedDadoConcedido() {
		return !Util.isNullOuVazio(listaDadoConcedido);
	}
	
	public boolean isRenderedGridDadoConcedido() {
		ArrayList<Fce> fces = null;
		if(!Util.isNullOuVazio(listaFce)){
			if(listaFce.size() == 1) {
				fces = (ArrayList) listaFce;
				
				if(fces.get(0).getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio().equals(DocumentoObrigatorioEnum.FCE_ASV.getId())) {
					List<Florestal> listFlorestal = florestalService.carregarListaFlorestal(fces.get(0).getIdeRequerimento());
					if(Util.isNullOuVazio(listFlorestal)) {
						return false;
					}
					return true;
				}
			}
		}
		
		return true;
		
	}
	
	public boolean isRenderedPrazoIndeterminado(ProcessoAto processoAto) {
		return processoAto.getAtoAmbiental() != null && processoAto.getAtoAmbiental().getIndPrazoIndeterminado() != null 
				&& processoAto.getAtoAmbiental().getIndPrazoIndeterminado();
	}
	
	public void changePrazoIndeterminado(ControleProcessoAto controle) {
//		ControleProcessoAto controle =(ControleProcessoAto) event.getComponent().getAttributes().get("controleProcessoAto");
		if(controle.getIndPrazoIndeterminado() != null && controle.getIndPrazoIndeterminado()){
			controle.setNumPrazoValidade(null);
		} else{
			controle.setNumPrazoValidade(0);
		}
	}
	
	protected RequestContext rc() {
		return RequestContext.getCurrentInstance();
	}
	
	public VwConsultaProcesso getVwProcesso() {
		return vwProcesso;
	}

	public Collection<Fce> getListaFce() {
		return listaFce;
	}

	public Collection<StatusProcessoAto> getListaStatusProcessoAto() {
		return listaStatusProcessoAto;
	}

	public AnaliseTecnica getAnaliseTecnica() {
		return analiseTecnica;
	}

	public Collection<DadoConcedidoInterface> getListaDadoConcedido() {
		return listaDadoConcedido;
	}

	public void setListaDadoConcedido(Collection<DadoConcedidoInterface> listaDadoConcedido) {
		this.listaDadoConcedido = listaDadoConcedido;
	}
}