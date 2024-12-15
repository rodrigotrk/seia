package br.gov.ba.seia.controller;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.TipoRequerimento;
import br.gov.ba.seia.enumerator.TipoRequerimentoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.Util;

@Named("urlReqUnicoController")
@ViewScoped
public class UrlReqUnicoController extends SeiaControllerAb {
	private TipoRequerimento tipoRequerimento;
	private Boolean bloqueiaEndContatoTemporario;
	private Boolean visualizandoImovel;
	private Boolean disableImovelRural = false;

	@PostConstruct
	public void init() {
		if (!Util.isNullOuVazio(ContextoUtil.getContexto().getTipoRequerimento())) {
			tipoRequerimento = ContextoUtil.getContexto().getTipoRequerimento();
		}else{
			tipoRequerimento = new TipoRequerimento(TipoRequerimentoEnum.REGULACAO_AMBIENTAL_DO_EMPREENDIMENTO.getIde(),
													TipoRequerimentoEnum.REGULACAO_AMBIENTAL_DO_EMPREENDIMENTO.getNomeTipoRequerimento());
		}
		ContextoUtil.getContexto().setTipoRequerimento(tipoRequerimento);
		if(!Util.isNullOuVazio(ContextoUtil.getContexto().getTipoRequerimento()) && !Util.isNullOuVazio(ContextoUtil.getContexto().getTipoRequerimento().getIdeTipoRequerimento()) && 
				ContextoUtil.getContexto().getTipoRequerimento().getIdeTipoRequerimento().equals(TipoRequerimentoEnum.REQUERIMENTO_DE_CADASTRO_DE_IMOVEL_RURAL.getIde())){
			bloqueiaEndContatoTemporario = true;
		}else{
			bloqueiaEndContatoTemporario = false;	
		}
	}
	
	protected void controleDeCampos() {
		this.visualizandoImovel = ContextoUtil.getContexto().getVisualizandoImovel();
		if(!Util.isNullOuVazio(this.visualizandoImovel) && this.visualizandoImovel){
			this.disableImovelRural  = true;
		}else{
			this.disableImovelRural = false;
		}
	}
	
	/**
	 * Retorna a url do questionário
	 * @return
	 */
	public String getCarregarUrlQuestionario() {
		Exception erro = null;
		try {
			this.tipoRequerimento = ContextoUtil.getContexto().getTipoRequerimento();// o valor desse contexto é setado no changeTipoRequerimento
			if(tipoRequerimento.getIdeTipoRequerimento().equals(TipoRequerimentoEnum.REGULACAO_AMBIENTAL_DO_EMPREENDIMENTO.getIde())){
				ContextoUtil.getContexto().setLabelTitutoRequerimento("Requerimento Único");
				return "/paginas/requerimentos/formQuestionarioReqUnico.xhtml";
			}else if(tipoRequerimento.getIdeTipoRequerimento().equals(TipoRequerimentoEnum.REQUERIMENTO_ATO_DECLARATORIO.getIde())){
				ContextoUtil.getContexto().setLabelTitutoRequerimento("Requerimento De Ato Declaratório");
				return "/paginas/requerimentos/formQuestionarioReqUnico.xhtml";
			}else if(tipoRequerimento.getIdeTipoRequerimento().equals(TipoRequerimentoEnum.REQUERIMENTO_DE_ATO_ADMINISTRATIVO.getIde())){
				ContextoUtil.getContexto().setLabelTitutoRequerimento("Requerimento de Ato Administrativo");
				return "/paginas/requerimentos/formQuestionarioReqUnico.xhtml";
			}else if(tipoRequerimento.getIdeTipoRequerimento().equals(TipoRequerimentoEnum.REQUERIMENTO_DE_CADASTRO_DE_IMOVEL_RURAL.getIde())){
				ContextoUtil.getContexto().setLabelTitutoRequerimento("Cadastro de Imóvel Rural");
				return "/paginas/requerimentos/formQuestionarioCadImovelRural.xhtml";
			}else{
				return "/paginas/requerimentos/formQuestionarioReqUnico.xhtml";
			}
		} catch (Exception e) {
			erro = e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			System.out.println("UrlReqUnicoController.getCarregarUrlQuestionario() ERRO!");
			return "/paginas/requerimentos/formQuestionarioCadImovelRural.xhtml";
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}
	
	/**
	 * Retorna a url do questionário
	 * @return
	 */
	public String getCarregarUrlPaginaLocalizacaoGeoGraficaAll() {
		Exception erro = null;
		try {
			return "/paginas/manter-localizacao-geografica/req-localizacao-geografica.xhtml";
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			System.out.println("UrlReqUnicoController.getCarregarUrlQuestionario() ERRO!");
			return "";
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}

	public TipoRequerimento getTipoRequerimento() {
		return tipoRequerimento;
	}

	public void setTipoRequerimento(TipoRequerimento tipoRequerimento) {
		this.tipoRequerimento = tipoRequerimento;
	}

	public Boolean getBloqueiaEndContatoTemporario() {
		return bloqueiaEndContatoTemporario;
	}


	public void setBloqueiaEndContatoTemporario(Boolean bloqueiaEndContatoTemporario) {
		this.bloqueiaEndContatoTemporario = bloqueiaEndContatoTemporario;
	}

	public Boolean getDisableImovelRural() {
		controleDeCampos();
		return disableImovelRural;
	}

	public void setDisableImovelRural(Boolean disableImovelRural) {
		this.disableImovelRural = disableImovelRural;
	}

	public Boolean getVisualizandoImovel() {
		return visualizandoImovel;
	}

	public void setVisualizandoImovel(Boolean visualizandoImovel) {
		this.visualizandoImovel = visualizandoImovel;
	}

}