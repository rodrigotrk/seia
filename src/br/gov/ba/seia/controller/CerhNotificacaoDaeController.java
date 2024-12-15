package br.gov.ba.seia.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.Cerh;
import br.gov.ba.seia.entity.CerhHistEnvioNotificacao;
import br.gov.ba.seia.entity.CerhNotificacaoEmail;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.GeoBahia;
import br.gov.ba.seia.entity.Parametro;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.ProcuradorPessoaFisica;
import br.gov.ba.seia.entity.ProcuradorPfEmpreendimento;
import br.gov.ba.seia.entity.ProcuradorRepEmpreendimento;
import br.gov.ba.seia.entity.ProcuradorRepresentante;
import br.gov.ba.seia.entity.RepresentanteLegal;
import br.gov.ba.seia.entity.ResponsavelEmpreendimento;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.ParametroEnum;
import br.gov.ba.seia.facade.CerhHistEnvioNotificacaoServiceFacade;
import br.gov.ba.seia.facade.CerhNotificacaoEmailServiceFacade;
import br.gov.ba.seia.facade.NotificacaoGeracaoDaeServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.ParametroService;
import br.gov.ba.seia.service.ProcuradorPessoaFisicaService;
import br.gov.ba.seia.service.ProcuradorPfEmpreendimentoService;
import br.gov.ba.seia.service.ProcuradorRepEmpreendimentoService;
import br.gov.ba.seia.service.ProcuradorRepresentanteService;
import br.gov.ba.seia.service.RepresentanteLegalService;
import br.gov.ba.seia.service.ResponsavelEmpreendimentoService;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.security.SecurityService;

@Named("cerhNotificacaoDaeController")
@ViewScoped
public class CerhNotificacaoDaeController {

	private Collection<GeoBahia> listaRpga;
	private GeoBahia rpgaSelecionado;
	
	private Pessoa requerente = new Pessoa();
	private Collection<Empreendimento> empreendimentos;
	private Empreendimento empreendimento;
	private Boolean renderedTableHistorico = Boolean.FALSE;
	
	private CerhHistEnvioNotificacao cerhHistEnvioNotificacao = null;
	
	private CerhNotificacaoEmail cerhNotificacaoEmail = null;
	
	private List<CerhHistEnvioNotificacao> listHistorico = null;
	
	
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("/Bundle");
	
	@EJB
	private EmpreendimentoService empreendimentoService;
	
	@EJB
	private NotificacaoGeracaoDaeServiceFacade notificacaoGeracaoDaeServiceFacade;
	
	@EJB
	private CerhNotificacaoEmailServiceFacade cerhNotificacaoEmailServiceFacade;
	
	@EJB
	private ProcuradorRepEmpreendimentoService procuradorRepEmpreendimentoService;
	
	@EJB
	private ProcuradorPfEmpreendimentoService procuradorPfEmpreendimentoService;
	
	@EJB
	private ResponsavelEmpreendimentoService responsavelEmpreendimentoService;
	
	@EJB
	private ProcuradorRepresentanteService procuradorRepresentanteService;
	
	@EJB
	private CerhHistEnvioNotificacaoServiceFacade cerhHistEnvioNotificacaoServiceFacade;
	
	@EJB
	private ProcuradorPessoaFisicaService procuradorPessoaFisicaService;
	
	@EJB
	private RepresentanteLegalService representanteLegalService;
	
	@EJB
	private ParametroService parametroService;
    
	private List<ProcuradorRepEmpreendimento> listProcuradorRepEmpreendimento;
	private List<ProcuradorPfEmpreendimento> listProcuradorPfEmpreendimento;
	private List<ResponsavelEmpreendimento> listResponsavelEmpreendimento;
	private List<ProcuradorRepresentante> listProcuradorRepresentante;
	private List<ProcuradorPessoaFisica> listProcuradorPessoaFisica;
    private List<RepresentanteLegal> listRepresentantePessoaJuridica;
    private List<RepresentanteLegal> listRepresentantePessoaFisica;
	
	private HashMap<String, Object> map;
	
	private Collection<Cerh> listaCerh = new ArrayList<Cerh>();

	private StringBuilder mensagemEnvioEmail;
	
	private Integer quantidadeTotalEmail = 0;
	
	
	@PostConstruct
	private void load(){
		carregarListaRpga();
	}

	private void carregarListaRpga() {
		try {
			this.listaRpga = notificacaoGeracaoDaeServiceFacade.listarRPGA();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public void selecionarUsuario(PessoaFisica requerente) {
		// atualiza lista de empreedimentos
		empreendimentos = null;
		this.requerente = new Pessoa();
		this.requerente.setIdePessoa(requerente.getIdePessoaFisica());
		PessoaFisica pessoaFisica = new PessoaFisica();
		pessoaFisica.setNomPessoa(requerente.getNomPessoa());
		this.requerente.setPessoaFisica(pessoaFisica);
	}
	
	public void selecionarRequerentePJ(PessoaJuridica pessoaJuridica){
		empreendimentos = null;
		this.requerente = new Pessoa();
		this.requerente.setIdePessoa(pessoaJuridica.getIdePessoaJuridica());
		this.requerente.setPessoaJuridica(pessoaJuridica);
	}
	
	private void consultarEmpreendimentos() {
		Exception erro = null;
		try {
			// Filtrar por requerente
			if (!Util.isNull(requerente)) {
				empreendimentos = empreendimentoService.listarEmpreendimento(requerente);
			}
		} catch (Exception e) {
			erro=e;
			JsfUtil.addErrorMessage("Não foi possível carregar a lista de empreendimentos - " + e.getMessage());
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}
	
	private void carregarHistorico(){
		try {	
			
			inicializarObjetos();
			
			if(Util.isNull(rpgaSelecionado)  && Util.isNull(requerente.getIdePessoa()) && Util.isNull(empreendimento.getIdeEmpreendimento())){
				MensagemUtil.erro("Obrigatório escolher uma opção do filtro (mudar msg)");
			}else{
				
				preencherFiltros();
				listHistorico = new ArrayList<CerhHistEnvioNotificacao>();
				
					listHistorico = (List<CerhHistEnvioNotificacao>) cerhHistEnvioNotificacaoServiceFacade.ListarCerhHistoricoEnvioNotificacao(map);
					renderedTableHistorico = Boolean.TRUE;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}	
	
	private void limpar(){
		requerente = null;
		renderedTableHistorico = Boolean.FALSE;
		empreendimento = null;
		listHistorico = null;
		map = null;
	}
	
	private void prepararEnvio(){
		try {
			inicializarObjetos();
			List<Pessoa> listPessoa = new ArrayList<Pessoa>();
	        
	    	Integer quantidadeCerh= 0;
	    	Integer contador= 0;
	    	Parametro parametro = parametroService.obterPorEnum(ParametroEnum.NUMERO_MAXIMO_EMAIS_POR_CONSULTA);
	    	this.quantidadeTotalEmail = 0;
	    	
			if(Util.isNull(rpgaSelecionado)  && Util.isNull(requerente.getIdePessoa()) && Util.isNull(empreendimento.getIdeEmpreendimento())){
				MensagemUtil.erro("Obrigatório escolher uma opção do filtro (mudar msg)");
			}else{
		        preencherFiltros();
				
					quantidadeCerh = notificacaoGeracaoDaeServiceFacade.listarCerhCount(this.map);
					
					while (contador < quantidadeCerh) {
						
						listaCerh.addAll(notificacaoGeracaoDaeServiceFacade.listarCerh(contador, Integer.parseInt(parametro.getDscValor()), this.map));
						
						contador += Integer.parseInt(parametro.getDscValor());
						
					}
					
					for (Cerh cerh : listaCerh) {
						
						if(!Util.isNull(cerh.getIdePessoaRequerente().getPessoaJuridica())){
							ProcuradorRepresentante procuradorRepresentante = new ProcuradorRepresentante();
							procuradorRepresentante.setIdePessoaJuridica(cerh.getIdePessoaRequerente().getPessoaJuridica());
							
							for(ProcuradorRepresentante pr: procuradorRepresentanteService.listarProcuradorRepresentanteComProjection(procuradorRepresentante)){
								pr.setEmpreendimento(cerh.getIdeEmpreendimento());
								listProcuradorRepresentante.add(pr);
							}
							listRepresentantePessoaJuridica.addAll(representanteLegalService.listarRepresentanteLegalPorPessoaJuridicaComProjection(cerh.getIdePessoaRequerente().getPessoaJuridica()));
						}else if(!Util.isNull(cerh.getIdePessoaRequerente().getPessoaFisica())){
							ProcuradorPessoaFisica procuradorPessoaFisica = new ProcuradorPessoaFisica();
							procuradorPessoaFisica.setIdePessoaFisica(cerh.getIdePessoaRequerente().getPessoaFisica());
							
							
							
							
							for(ProcuradorPessoaFisica pf : procuradorPessoaFisicaService.listarProcuradorPessoaFisicaComProjection(procuradorPessoaFisica)){
								pf.setEmpreendimento(cerh.getIdeEmpreendimento());
								listProcuradorPessoaFisica.add(pf);
							}
							
							cerh.getIdePessoaRequerente().setEmpreendimento(cerh.getIdeEmpreendimento());
							listPessoa.add(cerh.getIdePessoaRequerente());
							
							for(RepresentanteLegal representante: representanteLegalService.buscarRepresentanteLegal(cerh.getIdePessoaRequerente().getIdePessoa())){
								representante.setEmpreendimento(cerh.getIdeEmpreendimento());
								listRepresentantePessoaFisica.add(representante);
							}
							
						}
						
			            listProcuradorRepEmpreendimento.addAll(procuradorRepEmpreendimentoService.listarByEmpreendimentoComProjection(cerh.getIdeEmpreendimento()));
			            listProcuradorPfEmpreendimento.addAll(procuradorPfEmpreendimentoService.listarByEmpreendimentoComProjection(cerh.getIdeEmpreendimento()));    
						listResponsavelEmpreendimento.addAll(responsavelEmpreendimentoService.filtrarResponsaveisPorEmpreendimentoComProjection(cerh.getIdeEmpreendimento()));
					}
					
				
				this.quantidadeTotalEmail = quantidadeCerh + listProcuradorRepEmpreendimento.size() + listProcuradorPfEmpreendimento.size() + listResponsavelEmpreendimento.size() + listProcuradorRepresentante.size() + listProcuradorPessoaFisica.size() + listRepresentantePessoaFisica.size() + listRepresentantePessoaJuridica.size();
				
				mensagemQuantidadeEnvios();//Formatar mensagem de exibição da quantidade de envios
		
				Html.atualizar("pnlDialog");
				Html.exibir("dialogEnviarNotificacoes");
			}
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		  }
		
	}

	private void preencherFiltros() {
		this.map= new HashMap<String, Object>();
		this.map.put("rpgaSelecionado", rpgaSelecionado);
        
        if(!Util.isNull(empreendimento.getIdeEmpreendimento())){
			try {
				empreendimento = empreendimentoService.buscarEmpreendimentoPorIde(empreendimento.getIdeEmpreendimento());
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
        	this.map.put("nomEmpreendimento", empreendimento.getNomEmpreendimento());
        }
        
        if(!Util.isNull(requerente.getIdePessoa())){
        	this.map.put("requerente", requerente);
        }
	}
	
	/**
	 * Método para salvar dados no histórico 
	 * e na tabela que o job irá buscar os dados para enviar os emails
	 */
	public void enviarEmail(){
		try {
		
			cerhHistEnvioNotificacao = new CerhHistEnvioNotificacao();
			cerhNotificacaoEmail = new CerhNotificacaoEmail();
			
			if(Util.isNull(rpgaSelecionado)  && Util.isNull(requerente.getIdePessoa()) && Util.isNull(empreendimento.getIdeEmpreendimento())){
				MensagemUtil.erro("Obrigatório escolher uma opção do filtro (mudar msg)");
			}else{
			
				Usuario usuarioLogado = SecurityService.getUser();
				Date data = new Date(System.currentTimeMillis());
				
				Calendar cal = Calendar.getInstance();
				Integer ano = cal.get(Calendar.YEAR);
				
				if(!Util.isNull(empreendimento.getIdeEmpreendimento())){
					cerhHistEnvioNotificacao.setIdeEmpreendimento(empreendimento);
				}
				if(!Util.isNull(requerente.getIdePessoa())){
					cerhHistEnvioNotificacao.setIdeUsuarioAgua(requerente);
				}
				//Preenchendo a tabela que o job vai buscar para enviar os e-mails
				salvarNotificacaoEmail(usuarioLogado, data, ano);
				
				
				//Preenchendo o objeto de hitorico de notificação
				preencherHistoricoNotificacao(usuarioLogado, data);
				
					//salvar na tabela de histórico
					cerhHistEnvioNotificacaoServiceFacade.salvarCerhHistoricoEnvioNotificacao(cerhHistEnvioNotificacao);
					MensagemUtil.sucesso("E-mails enviados com sucesso!");
			}
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private void salvarNotificacaoEmail(Usuario usuarioLogado, Date data, Integer ano) {
			
			//emails dos requerentes
			for (Cerh cerh : listaCerh) {
				
				cerhNotificacaoEmail = preencherObjetoCerhNotificacaoEmail(data, cerh.getIdePessoaRequerente().getDesEmail(), ano, 
						cerh.getIdeEmpreendimento().getNomEmpreendimento(), usuarioLogado.getIdePessoaFisica());
				//persistir dados na base
				cerhNotificacaoEmailServiceFacade.salvarCerhNotificacaoEmail(cerhNotificacaoEmail);
				
			}
			
			//emails dos procuradoresRepEmpreendimento
			for(ProcuradorRepEmpreendimento procurador: listProcuradorRepEmpreendimento){
				
				cerhNotificacaoEmail = preencherObjetoCerhNotificacaoEmail(data, procurador.getIdeProcuradorRepresentante().getIdeProcurador().getPessoa().getDesEmail(), ano, 
						procurador.getIdeEmpreendimento().getNomEmpreendimento(), usuarioLogado.getIdePessoaFisica());

				//persistir dados na base
				cerhNotificacaoEmailServiceFacade.salvarCerhNotificacaoEmail(cerhNotificacaoEmail);
			}
			
			//emails dos ProcuradorPfEmpreendimento
			for(ProcuradorPfEmpreendimento procuradorPf: listProcuradorPfEmpreendimento){
				
				cerhNotificacaoEmail = preencherObjetoCerhNotificacaoEmail(data, procuradorPf.getIdeProcuradorPessoaFisica().getIdeProcurador().getPessoa().getDesEmail(), ano, 
						procuradorPf.getIdeEmpreendimento().getNomEmpreendimento(), usuarioLogado.getIdePessoaFisica());

				//persistir dados na base
				cerhNotificacaoEmailServiceFacade.salvarCerhNotificacaoEmail(cerhNotificacaoEmail);
			}
			
			//emails dos ResponsavelEmpreendimento
			for(ResponsavelEmpreendimento responsavelEmpreendimento: listResponsavelEmpreendimento){
				
				cerhNotificacaoEmail = preencherObjetoCerhNotificacaoEmail(data, responsavelEmpreendimento.getIdePessoaFisica().getPessoa().getDesEmail(), ano, 
						responsavelEmpreendimento.getIdeEmpreendimento().getNomEmpreendimento(), usuarioLogado.getIdePessoaFisica());

				//persistir dados na base
				cerhNotificacaoEmailServiceFacade.salvarCerhNotificacaoEmail(cerhNotificacaoEmail);
			}

			//emails dos ProcuradorRepresentante
			for(ProcuradorRepresentante procuradorRepresentante: listProcuradorRepresentante){
	
				cerhNotificacaoEmail = preencherObjetoCerhNotificacaoEmail(data, procuradorRepresentante.getIdeProcurador().getPessoa().getDesEmail(), ano, 
						procuradorRepresentante.getEmpreendimento().getNomEmpreendimento(), usuarioLogado.getIdePessoaFisica());

				//persistir dados na base
				cerhNotificacaoEmailServiceFacade.salvarCerhNotificacaoEmail(cerhNotificacaoEmail);
			}
			
			
			//emails dos ProcuradorPessoaFisica
			for(ProcuradorPessoaFisica procuradorPessoaFisica: listProcuradorPessoaFisica){
				
				cerhNotificacaoEmail = preencherObjetoCerhNotificacaoEmail(data, procuradorPessoaFisica.getIdeProcurador().getPessoa().getDesEmail(), ano, 
						procuradorPessoaFisica.getEmpreendimento().getNomEmpreendimento(), usuarioLogado.getIdePessoaFisica());

				//persistir dados na base
				cerhNotificacaoEmailServiceFacade.salvarCerhNotificacaoEmail(cerhNotificacaoEmail);
			}
			
						
			//emails dos Representante Lega para pessoa fisica
			for(RepresentanteLegal representantePessoaFisica: listRepresentantePessoaFisica){
				
				cerhNotificacaoEmail = preencherObjetoCerhNotificacaoEmail(data, representantePessoaFisica.getIdePessoaFisica().getPessoa().getDesEmail(), ano, 
						representantePessoaFisica.getEmpreendimento().getNomEmpreendimento(), usuarioLogado.getIdePessoaFisica());

				//persistir dados na base
				cerhNotificacaoEmailServiceFacade.salvarCerhNotificacaoEmail(cerhNotificacaoEmail);
			}
			
			//emails dos Representante Lega para pessoa juridica
			for(RepresentanteLegal representantePessoaJuridica: listRepresentantePessoaJuridica){
				
				cerhNotificacaoEmail = preencherObjetoCerhNotificacaoEmail(data, representantePessoaJuridica.getIdePessoaFisica().getPessoa().getDesEmail(), ano, 
						representantePessoaJuridica.getEmpreendimento().getNomEmpreendimento(), usuarioLogado.getIdePessoaFisica());

				//persistir dados na base
				cerhNotificacaoEmailServiceFacade.salvarCerhNotificacaoEmail(cerhNotificacaoEmail);
			}
	}

	private CerhNotificacaoEmail preencherObjetoCerhNotificacaoEmail(Date data,
			String desEmail, Integer ano, String nomEmpreendimento,
			Integer idePessoaFisica) {
		
		CerhNotificacaoEmail dadosEmail = new CerhNotificacaoEmail();
		
		dadosEmail.setDataEnvio(data);
		dadosEmail.setAssunto("Notificação Geração DAE"); //Assunto temporário
		dadosEmail.setDestinatario(desEmail);
		dadosEmail.setConteudo("Olá, já está no prazo para geraçãod o DAE para o Empreendimento "+ nomEmpreendimento);
		dadosEmail.setAnoBase(ano - 1); // Ano base é sempre o ano anterior ao atual
		dadosEmail.setIdeUsuarioEnvio(idePessoaFisica);
		dadosEmail.setIndeEnviado(Boolean.FALSE);
		
		return dadosEmail;
	}

	private void preencherHistoricoNotificacao(Usuario usuarioLogado, Date data) {
		cerhHistEnvioNotificacao.setIdeUsuarioEnvio(usuarioLogado.getIdePessoaFisica());
		cerhHistEnvioNotificacao.setIdeGeoRpga(rpgaSelecionado.getGid());
		cerhHistEnvioNotificacao.setQuantidadeEnvio(this.quantidadeTotalEmail);
		cerhHistEnvioNotificacao.setDataEnvio(data);
		
	}

	private void mensagemQuantidadeEnvios() {
		this.mensagemEnvioEmail = new StringBuilder();
		this.mensagemEnvioEmail.append("Serão disparados "+ this.quantidadeTotalEmail + " e-mails para o público selecionado: </br>  </br>  </br>"+ rpgaSelecionado.getNome());

			if(!Util.isNull(requerente.getPessoaFisica())){
				this.mensagemEnvioEmail.append("</br> " + requerente.getPessoaFisica().getNomPessoa());
			}else if(!Util.isNull(requerente.getPessoaJuridica())){
				this.mensagemEnvioEmail.append("</br> "+requerente.getPessoaJuridica().getNomRazaoSocial());
			}
			if(!Util.isNull(empreendimento.getIdeEmpreendimento())){
				this.mensagemEnvioEmail.append("</br> "+empreendimento.getNomEmpreendimento());
			}
			this.mensagemEnvioEmail.append("</br> </br> Deseja confirmar?");
	}

	private void inicializarObjetos() {
	
		requerente = new Pessoa();
		empreendimento = new Empreendimento();
		renderedTableHistorico = Boolean.FALSE;
		empreendimento = new Empreendimento();
		listHistorico = new ArrayList<CerhHistEnvioNotificacao>();
		map = new HashMap<String, Object>();
		listProcuradorRepEmpreendimento = new ArrayList<ProcuradorRepEmpreendimento>();
		listProcuradorPfEmpreendimento = new ArrayList<ProcuradorPfEmpreendimento>();
		listResponsavelEmpreendimento = new ArrayList<ResponsavelEmpreendimento>();
		listProcuradorRepresentante = new ArrayList<ProcuradorRepresentante>();
		listProcuradorPessoaFisica = new ArrayList<ProcuradorPessoaFisica>();
		listRepresentantePessoaJuridica = new ArrayList<RepresentanteLegal>();
		listRepresentantePessoaFisica = new ArrayList<RepresentanteLegal>();
		listaCerh = new ArrayList<Cerh>();
			
	}
	
	public Collection<GeoBahia> getListaRpga() {
		return listaRpga;
	}

	public void setListaRpga(Collection<GeoBahia> listaRpga) {
		this.listaRpga = listaRpga;
	}

	public GeoBahia getRpgaSelecionado() {
		return rpgaSelecionado;
	}

	public void setRpgaSelecionado(GeoBahia rpgaSelecionado) {
		this.rpgaSelecionado = rpgaSelecionado;
	}

	public Pessoa getRequerente() {
		return requerente;
	}
	
	public void setRequerente(Pessoa requerente) {
		this.requerente = requerente;
	}
	
	public Collection<SelectItem> getEmpreendimentos() {
		if (empreendimentos == null) {
			consultarEmpreendimentos();
		}
		List<SelectItem> itens = new ArrayList<SelectItem>();
		itens.add(new SelectItem("", BUNDLE.getString("geral_lbl_selecione")));
		if (empreendimentos != null) {
			for (Empreendimento empreendimentoObj : empreendimentos) {
				itens.add(new SelectItem(empreendimentoObj, empreendimentoObj.getNomEmpreendimento()));
			}
		}
		return itens;
	}

	public void setEmpreendimentos(Collection<Empreendimento> empreendimentos) {
		this.empreendimentos = empreendimentos;
	}


	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}


	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
	}


	public Boolean getRenderedTableHistorico() {
		return renderedTableHistorico;
	}

	public void setRenderedTableHistorico(Boolean renderedTableHistorico) {
		this.renderedTableHistorico = renderedTableHistorico;
	}

	public StringBuilder getMensagemEnvioEmail() {
		return mensagemEnvioEmail;
	}

	public void setMensagemEnvioEmail(StringBuilder mensagemEnvioEmail) {
		this.mensagemEnvioEmail = mensagemEnvioEmail;
	}

	public List<CerhHistEnvioNotificacao> getListHistorico() {
		return listHistorico;
	}

	public void setListHistorico(List<CerhHistEnvioNotificacao> listHistorico) {
		this.listHistorico = listHistorico;
	}
	
}
