package br.gov.ba.seia.controller;

import java.util.Collection;

import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.entity.Bairro;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.Logradouro;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.TipoEndereco;
import br.gov.ba.seia.entity.TipoLogradouro;
import br.gov.ba.seia.enumerator.TelaDestinoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.EmpreendimentoServiceFacade;
import br.gov.ba.seia.service.EnderecoEmpreendimentoService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MetodoUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

/**
 * Controller genérico responsável pelo <i>dialogSelecionarEmpreendimento.xhtml</i>.
 * 
 * @author eduardo.fernandes 
 */
@Named("selecionarEmpreendimentoController")
@ViewScoped
public class SelecionarEmpreendimentoController{
	
	@EJB
	private EmpreendimentoServiceFacade facade;
	
	private Pessoa requerente;
	private String nomeEmpreendimento;
	private Collection<Empreendimento> listaEmpreendimento;
	
	@EJB
	private EnderecoEmpreendimentoService enderecoEmpreendimentoService;
	
	/**
	 * {@link Empreendimento} selecionado no <i>dialogSelecionarEmpreendimento.xhtml</i>.
	 */
	private Empreendimento empreendimentoSelecionado;
	/**
	 * {@link TelaDestinoEnum} deve ser implementado em cada tela de 'Consulta'.
	 */
	private TelaDestinoEnum destinoEnum;
	
	private MetodoUtil metodoSelecionarEmpreendimento;
	private MetodoUtil metodoIncluirEmpreendimento;

	public void load(ActionEvent evt) {
		try {
			inicializarVariaveis();
			this.requerente = (Pessoa) evt.getComponent().getAttributes().get("requerente");
			this.metodoSelecionarEmpreendimento = (MetodoUtil) evt.getComponent().getAttributes().get("metodoSelecionarEmpreendimento");		
			this.metodoIncluirEmpreendimento = (MetodoUtil) evt.getComponent().getAttributes().get("metodoIncluirEmpreendimento");		
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void limpar(){
		enviarEmpreendimentoParaTelaDestino();
		inicializarVariaveis();
	}

	private void inicializarVariaveis() {
		requerente = null;
		nomeEmpreendimento = null;
		listaEmpreendimento = null;
		empreendimentoSelecionado = null;
		destinoEnum = null;
	}
	
	public void consultarEmpreendimento() {
		try {
			listaEmpreendimento = facade.listarEmpreendimento(requerente, nomeEmpreendimento);
		} catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que deve ser implementado para enviar o 'empreendimentoSelecionado' para o <b>Controller</b> adequado de acordo com a {@link TelaDestinoEnum}.
	 * 
	 * @author eduardo.fernandes 
	 * @since 09/11/2016
	 */
	public void enviarEmpreendimentoParaTelaDestino(){
		try {
			if(!Util.isNull(empreendimentoSelecionado)){
				if(!Util.isNull(metodoSelecionarEmpreendimento)) {
					metodoSelecionarEmpreendimento.executeMethod(empreendimentoSelecionado);				
				}
				else {
					String form = "";
					carregarEnderecoEmpreendimento();
					TelaDestinoEnum enumerator = TelaDestinoEnum.getEnum(getDestinoEnum().getIdeTela());
					if(enumerator.equals(TelaDestinoEnum.CADASTRO_OLEO_GAS)){
						// Implementar para o Cadastro de Óleo e Gás
					} else if(enumerator.equals(TelaDestinoEnum.PESQUISA_MINERAL_SEM_GUIA)) {
						PesquisaMineralController pesquisaMineralController = (PesquisaMineralController) SessaoUtil.recuperarManagedBean("#{pesquisaMineralController}", PesquisaMineralController.class);
						pesquisaMineralController.carregarInformacoesDoEmpreendimento(empreendimentoSelecionado);
						form = "tabCadastroPesquisaMineral";
					} else if(enumerator.equals(TelaDestinoEnum.TORRES_ANEMOMETRICCAS)) {
						TorresAnemometricasController  torresAnemometricasController = (TorresAnemometricasController) SessaoUtil.recuperarManagedBean("#{torresAnemometricasController}", TorresAnemometricasController.class);
						torresAnemometricasController.salvarEmpreendimento(empreendimentoSelecionado);
						torresAnemometricasController.removerDadosDoEmpreendimentoAnterior();
						form = "formRmpreendimentos";
					}
					else if(enumerator.equals(TelaDestinoEnum.SILOS_ARMAZENS)){
						SilosArmazensController silosArmazensController = (SilosArmazensController) SessaoUtil.recuperarManagedBean("#{silosArmazensController}", SilosArmazensController.class);
						silosArmazensController.carregarEmpreendimento(empreendimentoSelecionado);
						form = "formSilosArmazen";
					}
					Html.atualizar(form);
				}
			}
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage("As informações deste Empreendimento estão incompletas, atualize o cadastro.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	/**
	 * Método que irá carregar o {@link Empreendimento} com seu {@link Endereco}, {@link TipoEndereco}, {@link Logradouro}, {@link Municipio}, {@link TipoLogradouro} e {@link Bairro}.  
	 * 
	 * @author eduardo.fernandes 
	 * @throws Exception 
	 * @since 09/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8187">#8187</a>
	 */
	private void carregarEnderecoEmpreendimento() throws Exception {
		empreendimentoSelecionado = facade.carregarComMunicipio(empreendimentoSelecionado);
	}
	
	public String incluirNovoEmpreendimento(){
		EmpreendimentoController empreendimentoController = (EmpreendimentoController)SessaoUtil.recuperarManagedBean("#{empreendimentoController}", EmpreendimentoController.class); 
		empreendimentoController.novoEmpreendimento();
		empreendimentoController.incluirEmpreendimento();
		ContextoUtil.getContexto().setPessoa(requerente);
		if(!Util.isNull(getDestinoEnum())) {
			SessaoUtil.adicionarObjetoSessao("URL_EMPREENDIMENTO_ORIGEM", getDestinoEnum().getUrlTela());
		} 
		else {
			incluirEmpreendimento();
		}
		return TelaDestinoEnum.CADASTRO_EMPREENDIMENTO.getUrlTela();
	}

	private void incluirEmpreendimento() {
		try {
			metodoIncluirEmpreendimento.executeMethod();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("")); 
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public void verificarEnderecoEmpreendimento(Empreendimento empreendimento){
		try {
			if(enderecoEmpreendimentoService.enderecoLocalizacaoEmpreendimentoExists(empreendimento)){
				this.empreendimentoSelecionado = empreendimento;
				enviarEmpreendimentoParaTelaDestino();
				RequestContext.getCurrentInstance().execute("dialogSelecionarEmpreendimento.hide()");
			}else{
				JsfUtil.addErrorMessage("Empreendimento não possui endereço de localização");
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	/*
	 * ------------------------
	 * ---getters && setters---
	 * ------------------------
	 */
	public void setRequerente(Pessoa requerente) {
		this.requerente = requerente;
	}

	public String getNomeEmpreendimento() {
		return nomeEmpreendimento;
	}

	public void setNomeEmpreendimento(String nomeEmpreendimento) {
		this.nomeEmpreendimento = nomeEmpreendimento;
	}
	
	public Collection<Empreendimento> getlistaEmpreendimento() {
		return listaEmpreendimento;
	}

	public Empreendimento getEmpreendimentoSelecionado() {
		return empreendimentoSelecionado;
	}

	public void setEmpreendimentoSelecionado(Empreendimento empreendimentoSelecionado) {
		this.empreendimentoSelecionado = empreendimentoSelecionado;
	}

	public TelaDestinoEnum getDestinoEnum() {
		return destinoEnum;
	}

	public void setDestinoEnum(TelaDestinoEnum destinoEnum) {
		this.destinoEnum = destinoEnum;
	}

	public MetodoUtil getMetodoSelecionarEmpreendimento() {
		return metodoSelecionarEmpreendimento;
	}

	public void setMetodoSelecionarEmpreendimento(
			MetodoUtil metodoSelecionarEmpreendimento) {
		this.metodoSelecionarEmpreendimento = metodoSelecionarEmpreendimento;
	}
}