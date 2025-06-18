package br.gov.ba.seia.controller;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.entity.AnaliseTecnica;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeograficaFinalidade;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.enumerator.FceFinalidadeUsoAguaEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.facade.FceOutorgaLocalizacaoGeograficaFinalidadeServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

@Named("fceOutorgaLocalizacaoGeograficaFinalidadeController")
@ViewScoped
public class FceOutorgaLocalizacaoGeograficaFinalidadeController {

	@EJB
	private FceOutorgaLocalizacaoGeograficaFinalidadeServiceFacade serviceFacade;
	
	private List<FceOutorgaLocalizacaoGeograficaFinalidade> listaFceOutorgaLocalizacaoGeograficaFinalidade;
	private FceOutorgaLocalizacaoGeograficaFinalidade fceOutorgaLocalizacaoGeograficaFinalidade;
	
	public void listarTipoFinalidadeUsoAgua(ActionEvent event){
		FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica = (FceOutorgaLocalizacaoGeografica) event.getComponent().getAttributes().get("ideFceOutorgaLocalizacaoGeografica");
		try {
			listaFceOutorgaLocalizacaoGeograficaFinalidade = serviceFacade.listarTipoFinalidadeUsoAguaByFce(fceOutorgaLocalizacaoGeografica);
		} 
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de finalidades.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void excluirFceOutorgaLocalizacaoGeograficaFinalidade(){
		try {
			TipoFinalidadeUsoAgua finalidadeUsoAgua = getFinalidade();
			AnaliseTecnica analiseTecnica = getAnaliseTecnica();
			serviceFacade.excluirFceOutorgaLocalizacaoGeograficaFinalidade(fceOutorgaLocalizacaoGeograficaFinalidade);
			
			List<FceOutorgaLocalizacaoGeograficaFinalidade> listaDePontosComFinalidades = listarOutrosPontoParaFinalidade(finalidadeUsoAgua, analiseTecnica); 
			
			if(Util.isNullOuVazio(listaDePontosComFinalidades)){
				removerFceAnaliseTecnica(finalidadeUsoAgua, analiseTecnica);
			} 
			/*else{  
				isFceOutorgaAquicultura(finalidadeUsoAgua);
			}*/
			JsfUtil.addSuccessMessage("Finalidade excluida.");
			RequestContext.getCurrentInstance().addPartialUpdateTarget(":formFinalidade");
		} 
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_excluir") + " a finalidade.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * @author eduardo (eduardo.fernandes@avansys.com.br)
	 * @param finalidadeUsoAgua
	 * @since 15/03/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/">#</a> #ticket
	 */
	public void isFceOutorgaAquicultura(TipoFinalidadeUsoAgua finalidadeUsoAgua) {
		if(finalidadeUsoAgua.equals(FceFinalidadeUsoAguaEnum.AQUICULTURA_VIVEIRO_ESCAVADO.getIdeTipoFinalidadeUsoAgua())){
			if(fceOutorgaLocalizacaoGeograficaFinalidade.getIdeFceOutorgaLocalizacaoGeografica().getIdeTipologia().getIdeTipologia().equals(
					TipologiaEnum.LANCAMENTO_EFLUENTES.getId()
					)){
				
			}
			else if(fceOutorgaLocalizacaoGeograficaFinalidade.getIdeFceOutorgaLocalizacaoGeografica().getIdeTipologia().getIdeTipologia().equals(
					TipologiaEnum.CAPTACAO_SUPERFICIAL.getId()
					)){
				
			}
			else if(fceOutorgaLocalizacaoGeograficaFinalidade.getIdeFceOutorgaLocalizacaoGeografica().getIdeTipologia().getIdeTipologia().equals(
					TipologiaEnum.CAPTACAO_SUBTERRANEA.getId()
					)){
				
			}
		}
	}

	/**
	 * Método para remover o {@link Fce} que representa o {@link TipoFinalidadeUsoAgua} da {@link AnaliseTecnica}. 
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @param finalidadeUsoAgua
	 * @param analiseTecnica
	 * @throws Exception
	 * @since 01/03/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7437">#7437</a>
	 */
	private void removerFceAnaliseTecnica(TipoFinalidadeUsoAgua finalidadeUsoAgua, AnaliseTecnica analiseTecnica) throws Exception {
		Map<String, Object> param = obterParametrosBy(finalidadeUsoAgua);
		DocumentoObrigatorio doc = (DocumentoObrigatorio) param.get("doc");
		if(!Util.isNull(doc)){
			String function = (String) param.get("function");
			Fce fceExcluido = obterFceParaExclusao(analiseTecnica, doc);
			excluirFce(function, fceExcluido);
			atualizarAnaliseTecnica(fceExcluido);
		}
	}

	/**
	 * Método que retorna a {@link AnaliseTecnica} associada ao {@link FceOutorgaLocalizacaoGeograficaFinalidade}.
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @return {@link AnaliseTecnica}
	 * @since 01/03/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7437">#7437</a>
	 */
	private AnaliseTecnica getAnaliseTecnica() {
		return fceOutorgaLocalizacaoGeograficaFinalidade.getIdeFceOutorgaLocalizacaoGeografica().getIdeFce().getIdeAnaliseTecnica();
	}

	/**
	 * Método que retorna o {@link TipoFinalidadeUsoAgua} associado ao {@link FceOutorgaLocalizacaoGeograficaFinalidade}.
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @return {@link TipoFinalidadeUsoAgua}
	 * @since 01/03/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7437">#7437</a>
	 */
	private TipoFinalidadeUsoAgua getFinalidade() {
		return fceOutorgaLocalizacaoGeograficaFinalidade.getIdeTipoFinalidadeUsoAgua();
	}

	/**
	 * Método que retorna um {@link Map} com o {@link DocumentoObrigatorio} e a <i>function</i> para exclusão do {@link Fce} de acordo com o {@link TipoFinalidadeUsoAgua} passado no parâmetro.
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @param finalidadeUsoAgua
	 * @return {@link Map}
	 * @since 01/03/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7437">#7437</a>
	 */
	private Map<String, Object> obterParametrosBy(TipoFinalidadeUsoAgua finalidadeUsoAgua) {
		return serviceFacade.obterParametrosBy(finalidadeUsoAgua);
	}

	/**
	 * Método que exclui o {@link Fce} pela <i>function</i>.
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @param function
	 * @param fceExcluido
	 * @throws Exception
	 * @since 01/03/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7437">#7437</a>
	 */
	private void excluirFce(String function, Fce fceExcluido) throws Exception {
		serviceFacade.apagarFce(function, fceExcluido);
	}

	/**
	 * Método que retorna o {@link Fce} para ser excluído.
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @param analiseTecnica
	 * @param doc
	 * @return
	 * @throws Exception
	 * @since 01/03/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7437">#7437</a>
	 */
	private Fce obterFceParaExclusao(AnaliseTecnica analiseTecnica, DocumentoObrigatorio doc) throws Exception {
		return serviceFacade.buscarFce(analiseTecnica, doc);
	}

	/**
	 * Método que remove o {@link Fce} da lista de Fce's na {@link AnaliseTecnica} e atualiza sua tela. 
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @param fceExcluido
	 * @since 01/03/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/">#</a> #ticket
	 */
	private void atualizarAnaliseTecnica(Fce fceExcluido) {
		ProcessoAnaliseTecnicaController analiseTecnicaController = (ProcessoAnaliseTecnicaController) SessaoUtil.recuperarManagedBean("#{processoAnaliseTecnicaController}", ProcessoAnaliseTecnicaController.class);
		analiseTecnicaController.getListaFce().remove(fceExcluido);
		RequestContext.getCurrentInstance().addPartialUpdateTarget("pnlAnaliseTecnica");
	}

	/**
	 * Método que vai listar as outras {@link FceOutorgaLocalizacaoGeograficaFinalidade} para o {@link TipoFinalidadeUsoAgua} da {@link AnaliseTecnica}.
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @param finalidadeUsoAgua
	 * @param analiseTecnica 
	 * @return List<FceOutorgaLocalizacaoGeograficaFinalidade>
	 * @throws Exception
	 * @since 29/02/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7437">#7437</a>
	 */
	private List<FceOutorgaLocalizacaoGeograficaFinalidade> listarOutrosPontoParaFinalidade(TipoFinalidadeUsoAgua finalidadeUsoAgua, AnaliseTecnica analiseTecnica) throws Exception {
		return serviceFacade.listarFceOutorgaLocalizacaoGeograficaFinalidadeByTipoFinalidade(finalidadeUsoAgua, analiseTecnica);
	}

	
	/*
	 * 
	 * getters && setters
	 * 
	 */
	public FceOutorgaLocalizacaoGeograficaFinalidade getFceOutorgaLocalizacaoGeograficaFinalidade() {
		return fceOutorgaLocalizacaoGeograficaFinalidade;
	}

	public void setFceOutorgaLocalizacaoGeograficaFinalidade(FceOutorgaLocalizacaoGeograficaFinalidade fceOutorgaLocalizacaoGeograficaFinalidade) {
		this.fceOutorgaLocalizacaoGeograficaFinalidade = fceOutorgaLocalizacaoGeograficaFinalidade;
	}

	public List<FceOutorgaLocalizacaoGeograficaFinalidade> getListaFceOutorgaLocalizacaoGeograficaFinalidade() {
		return listaFceOutorgaLocalizacaoGeograficaFinalidade;
	}

}