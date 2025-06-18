package br.gov.ba.seia.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.TipoAtividadeNaoSujeitaLicenciamento;
import br.gov.ba.seia.enumerator.PaginaEnum;
import br.gov.ba.seia.enumerator.TipoAtividadeNaoSujeitaLicenciamentoEnum;
import br.gov.ba.seia.facade.ConsultaAtividadeSemLicenciamentoFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemLacFceUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

/**
 * 
 * @author eduardo.fernandes 
 * @since 13/12/2016
 * @see <a href="http://10.105.17.77/redmine/issues/8191">#8191</a>
 */
@Named("identificacaoAtividadeController")
@ViewScoped

public class IdentificacaoAtividadeController {

	@EJB
	private ConsultaAtividadeSemLicenciamentoFacade facade;
	
	@SuppressWarnings("unused")
	private Integer tipoSolicitante;
	private TipoAtividadeNaoSujeitaLicenciamento ativiade;
	private List<TipoAtividadeNaoSujeitaLicenciamento> listaAtividade;
	private Boolean aceitaInstrucoes;

	@PostConstruct
	public void carregarListaAtividade() {
		
		try {
			tipoSolicitante = ContextoUtil.getContexto().getTipoSolicitante();
			//ContextoUtil.getContexto().setTipoSolicitante(null);

			listaAtividade = facade.listarAtividadesSemLicenciamentoAmbiental();
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de Atividades.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public String voltar() {
		return PaginaEnum.IDENTIFICAR_PAPEL.getUrl() + "?faces-redirect=true";
	}

	public String avancar() {
		String url = "";
		if(!Util.isNull(ativiade)){
			if (ativiade.getIdeTipoAtividadeNaoSujeitaLicenciamento().equals(TipoAtividadeNaoSujeitaLicenciamentoEnum.PERFURACAO_DE_POCOS.getIde())) {
				url = PaginaEnum.CADASTRO_OLEO_GAS.getUrl();
			}
			else if (ativiade.getIdeTipoAtividadeNaoSujeitaLicenciamento().equals(TipoAtividadeNaoSujeitaLicenciamentoEnum.PESQUISA_MINERAL.getIde())) {
				if (tipoSolicitante != 1 && tipoSolicitante != 3) {
					url = PaginaEnum.PESQUISA_MINERAL_SEM_GUIA.getUrl();
				}
				else {
					JsfUtil.addErrorMessage(Util.getString("msg_info_065"));
					return "";
				}
			} else if (ativiade.getIdeTipoAtividadeNaoSujeitaLicenciamento().equals(TipoAtividadeNaoSujeitaLicenciamentoEnum.INSTALACAO_DE_TORRES.getIde())) {
				RequestContext.getCurrentInstance().execute("dialogInstrucoesCadastroTorres.show()");
				return "";
			}else if(ativiade.getIdeTipoAtividadeNaoSujeitaLicenciamento().equals(TipoAtividadeNaoSujeitaLicenciamentoEnum.SILOS_E_ARMAZENS.getIde())){
//				url = "/paginas/manter-atividade-nao-sujeita-licenciamento/silos_armazens/instrucoes_cadastro.xhtml";
				
				SilosArmazensController silosArmazensController = (SilosArmazensController) SessaoUtil.recuperarManagedBean("#{silosArmazensController}", SilosArmazensController.class);
				
				Pessoa requerente = ContextoUtil.getContexto().getReqPapeisDTO().getRequerente().getPessoa();
				
				if(!Util.isNullOuVazio(requerente)){
					Html.exibir("dialogInstrucoesCadastro");
				}

			} 
			
			return url  + "?faces-redirect=true";
		} 
		else {
			MensagemLacFceUtil.exibirInformacao041("uma atividade");
		}
		return url;
	}
	
	public String avancarTorresAnenometricas(){
		String retorno = "";
		if (this.aceitaInstrucoes) {
			RequestContext.getCurrentInstance().execute("dialogInstrucoesCadastro.hide()");
			retorno = "/paginas/manter-atividade-nao-sujeita-licenciamento/torres-anenometricas/cadastro.xhtml?faces-redirect=true";
		} else {
			JsfUtil.addErrorMessage("É obrigatório a seleção da(s) opção(ões) na tela de Instrução do Cadastro.");
		}
		return retorno;
	}

	
	public TipoAtividadeNaoSujeitaLicenciamento getAtiviade() {
		return ativiade;
	}

	public void setAtiviade(TipoAtividadeNaoSujeitaLicenciamento ativiade) {
		this.ativiade = ativiade;
	}

	public List<TipoAtividadeNaoSujeitaLicenciamento> getListaAtividade() {
		return listaAtividade;
	}

	/**
	 * Getter do campo aceitaInstrucoes
	 *	
	 * @return the aceitaInstrucoes
	 */
	public Boolean getAceitaInstrucoes() {
		return aceitaInstrucoes;
	}

	/**
	 * Setter do campo  aceitaInstrucoes
	 * @param aceitaInstrucoes the aceitaInstrucoes to set
	 */
	public void setAceitaInstrucoes(Boolean aceitaInstrucoes) {
		this.aceitaInstrucoes = aceitaInstrucoes;
	}

	
}
