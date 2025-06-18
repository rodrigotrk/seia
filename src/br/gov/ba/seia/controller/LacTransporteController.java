package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.BaseOperacional;
import br.gov.ba.seia.entity.Certificado;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EmpreendimentoVeiculo;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.Lac;
import br.gov.ba.seia.entity.LacTransporte;
import br.gov.ba.seia.entity.LacTransporteProduto;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.Produto;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.Residuo;
import br.gov.ba.seia.entity.Telefone;
import br.gov.ba.seia.entity.TipoTelefone;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;
import br.gov.ba.seia.enumerator.LacTransporteOutroEnum;
import br.gov.ba.seia.facade.LacPostoServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.BaseOperacionalService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.EmpreendimentoVeiculoService;
import br.gov.ba.seia.service.LacTransporteService;
import br.gov.ba.seia.service.LocalizacaoGeograficaService;
import br.gov.ba.seia.service.PessoaJuridicaService;
import br.gov.ba.seia.service.ProdutoService;
import br.gov.ba.seia.service.ResiduoService;
import br.gov.ba.seia.service.TelefoneService;
import br.gov.ba.seia.service.TipoTelefoneService;
import br.gov.ba.seia.util.CertificadoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.RelatorioUtil;
import br.gov.ba.seia.util.Util;

@Named("lacTransporteController")
@ViewScoped
public class LacTransporteController extends PessoaEndereco {

	@EJB
	private LacTransporteService lacTransporteService;
	@EJB
	private LacPostoServiceFacade lacServiceFacade;
	@EJB
	private ProdutoService produtoService;
	@EJB
	private ResiduoService residuoService;
	@EJB
	private EmpreendimentoVeiculoService veiculoService;
	@EJB
	private EmpreendimentoService empreendimentoService;
	@EJB
	private PessoaJuridicaService pessoaJuridicaService;
	@EJB
	private BaseOperacionalService baseOperacionalService;
	@EJB
	private LocalizacaoGeograficaService localizacaoGeograficaService;
	@EJB
	private TelefoneService telefoneService;
	@EJB
	private TipoTelefoneService tipoTelefoneService;
	@Inject
	private BaseOperacionalController baseOperacionalController;
	@Inject
	private CertificadoUtil certificadoUtil;

	// Geral
	private int activeTab;
	private LacTransporte lacTransporte;
	private Requerimento requerimento;

	// INI DadosGerais
	private Boolean blockForm;
	private boolean pjExistente;
	// FIM DadosGerais

	// INI da NOVA IMPLEMENTACAO
	private String nomeProduto;
	private List<Produto> listProdutos;
	private List<Produto> listProdutoSelected;
	private Produto produtoSelecionado;
	private String nomeResiduo;
	private List<Residuo> listResiduos;
	private List<Residuo> listResiduoSelected;
	private Residuo residuoSelecionado;
	private LacTransporteProduto transporteProduto;
	private List<EmpreendimentoVeiculo> listEmpreendimentoVeiculos;
	private Empreendimento empreendimento;
	private List<Imovel> imovel;
	private boolean mostraRelatorio;
	// FIM da NOVA IMPLEMENTACAO
	
	private String numCnpj;
	private Collection<TipoTelefone> listaTiposTelefone;
	private Telefone telefone;

	public void init() {
		lacTransporte = null;
		activeTab = 0;
		mostraRelatorio = true;
		carregarListaVeiculos();
		carregarListaProdutos();
		carregarListaResiduos();
		enviarId();
		if(isEdicao()){
			carregarLacTransProduto(lacTransporte);
			carregarLacTransResiduo(lacTransporte);
			if(lacTransporte.getIndAtendimentoEmergenciaTerceiro() && !Util.isNull(lacTransporte.getIdePessoaJuridica())){
				pjExistente = true;
				blockForm = true;
				numCnpj = lacTransporte.getIdePessoaJuridica().getNumCnpj();
				super.pessoa  = lacTransporte.getIdePessoaJuridica().getPessoa();
				carregarTelefonePessoaJuridica();
				super.prepararEnderecoGenericoController();
			}
			if(lacTransporte.getIndBaseOperacional()){
				baseOperacionalController.init();
			}
		} 
		else {
			buscarLac(requerimento);
			
			pjExistente = false;
			blockForm = false;
			listProdutoSelected = null;
			listResiduoSelected = null;
		}
	}

	/**
	 * @Comentários Método chamado pela [abaDadosGerais] para retornar uma lista de Veiculo com TODOS os veículos associados à esse empreendimento.
	 * @return listProduto
	 * @author eduardo.fernandes
	 */
	public void carregarListaVeiculos(){
		try{
			if(!Util.isNull(requerimento)){
				if(Util.isNullOuVazio(listEmpreendimentoVeiculos)){
					listEmpreendimentoVeiculos = new ArrayList<EmpreendimentoVeiculo>();
				}
				// Para salvar o shape de localizacaoGeografica precisamos de um IMOVEL, atraves do Requerimento chegamos no Empreendimento e ele tem Imovel.
				List<Empreendimento> coll = (List<Empreendimento>) empreendimentoService.buscarEmpreendimentoPorRequerimento(requerimento);
				empreendimento = empreendimentoService.carregarById(coll.get(0).getIdeEmpreendimento());

				listEmpreendimentoVeiculos = veiculoService.listarEmpreendimentoVeiculoByEmpreedimento(empreendimento);
				imovel = (List<Imovel>) empreendimento.getImovelCollection();
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_mensagens_erro001" + "Veículos: ") + e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * 
	 * @author eduardo.fernandes
	 */
	public void carregarListaProdutos(){
		if(Util.isNullOuVazio(listProdutos)){
			listProdutos = new ArrayList<Produto>();
		}
		listProdutos = produtoService.pesquisarProdutos(!Util.isNullOuVazio(nomeProduto) ? new Produto(nomeProduto) : new Produto());
	}

	/**
	 * 
	 * @author eduardo.fernandes
	 */
	public void carregarListaResiduos(){
		if(Util.isNullOuVazio(listResiduos)){
			listResiduos = new ArrayList<Residuo>();
		}
		listResiduos = residuoService.pesquisarResiduos(!Util.isNullOuVazio(nomeResiduo) ? new Residuo(nomeResiduo) : new Residuo());
	}

	/**
	 * @Comentários Método que busca uma lista de LacTransporteProduto do banco baseado no ideLacTransporte
	 * @param lacTransporte
	 * @author eduardo.fernandes
	 */
	public void carregarLacTransProduto(LacTransporte lacTransporte){
		try {
			if(Util.isNullOuVazio(listProdutoSelected)){
				listProdutoSelected = new ArrayList<Produto>();
			}
			listProdutoSelected = lacTransporteService.carregarListaProdutoSelecionados(lacTransporte);
			removerOsSelecionadosDaListaPrincipal(listProdutoSelected);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_mensagens_erro001" + " Produtos transportados: ") + e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * 
	 * @param list
	 * @author eduardo.fernandes
	 */
	public void removerOsSelecionadosDaListaPrincipal(List<?> list){
		if(!Util.isNullOuVazio(list)){
			for (Object object : list) {
				if(object instanceof Produto){
					listProdutos.remove(object);
				}
				else if(object instanceof Residuo){
					listResiduos.remove(object);
				}
			}
		}
	}

	/**
	 * @Comentários Método que busca uma lista de LacTransporteResiduo do banco baseado no ideLacTransporte.
	 * @param lacTransporte
	 * @author eduardo.fernandes
	 */
	public void carregarLacTransResiduo(LacTransporte lacTransporte){
		try {
			if(Util.isNullOuVazio(listResiduoSelected)){
				listResiduoSelected = new ArrayList<Residuo>();
			}
			listResiduoSelected = lacTransporteService.carregarListaResiduoSelecionados(lacTransporte);
			removerOsSelecionadosDaListaPrincipal(listResiduoSelected);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_mensagens_erro001" + " Resíduos transportados: ") + e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * @Comentários Verifica se já existe uma lacTransporte criada para aquele Requerimento
	 * @return true or false
	 * @author eduardo.fernandes
	 */
	public boolean isEdicao(){
		try {
			if(Util.isNull(lacTransporte)){
				lacTransporte = lacTransporteService.buscarLacTransporteByIdeRequerimento(requerimento);
			}
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage("Ocorreu um erro ao buscar as informações da Lac Transporte do banco.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		return !Util.isNull(lacTransporte) && !Util.isNull(lacTransporte.getIdeLacTransporte());
	}

	/**
	 * @Comentários Método para salvar a Lac no BD. Faz-se necessário salvá-la antes de salvar a LacTransporte.
	 * @author eduardo.fernandes
	 */
	public boolean salvarLac(){
		try{
			if(Util.isNull(lacTransporte.getIdeLac().getIdeLac())){
				lacTransporteService.salvarLac(lacTransporte.getIdeLac());
			}
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_mensagens_erro002") + "à Lac :" + e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}

		return !Util.isNullOuVazio(lacTransporte.getIdeLac().getIdeLac());
	}

	/**
	 * @param event
	 * @author eduardo.fernandes
	 */
	public void controlarAbas(TabChangeEvent event) {
		if("abaDadosGerais".equals(event.getTab().getId())){
			activeTab = 0;
		}
		else if("abaBaseOperacional".equals(event.getTab().getId())){
			activeTab = 1;
		}
	}

	/**
	 * @Comentários Método chamado na [abaBaseOperacional] para voltar a [abaDadosGerais]
	 * @see controlarAbas(TabChangeEvent event)
	 * @author eduardo.fernandes
	 */
	public void voltarAba(){
		activeTab--;
	}

	/**
	 * @Comentários O usuário só irá conseguir avançar para a [abaBaseOperacional] após salvar a [abaDadosGerais]
	 * @see controlarAbas(TabChangeEvent event)
	 * @author eduardo.fernandes
	 */
	public void avancarAba(){
		if(salvarLacTransporte()){
			activeTab++;
		}
	}

	/**
	 * @Comentários Após adicioar um produto na lista, chama-se esse metódo para anular o combo.
	 * @author eduardo.fernandes
	 */
	public void limparComboProduto(){
		if(!Util.isNullOuVazio(listProdutoSelected)){
			for(Produto produto : listProdutoSelected){
				limparResiduoOuProduto(produto);
			}
			listProdutoSelected.clear();
			pesquisarProdutos();
		}
	}

	/**
	 * @Comentários Adiciona o produto selecionado na lista
	 * @author eduardo.fernandes
	 */
	public void adicionarProdutoLista(){
		if(Util.isNullOuVazio(listProdutoSelected)){
			listProdutoSelected = new ArrayList<Produto>();
		}
		if(!listProdutoSelected.contains(produtoSelecionado)){
			listProdutoSelected.add(produtoSelecionado);
			if(isOutro(produtoSelecionado)){
				produtoSelecionado.setOutro(true);
				produtoSelecionado.setQtdTransporteAnual(new Double(1));
				JsfUtil.addWarnMessage(Util.getString("lac_dadosGerais_info006"));
				JsfUtil.addWarnMessage(Util.getString("lac_dadosGerais_info0056"));
			}
			produtoSelecionado = new Produto();
			nomeProduto = null;
			pesquisarProdutos();
		} else {
			JsfUtil.addErrorMessage("O produto " + Util.getString("lac_dadosGerais_mensagens_alerta_escolhido"));
		}
	}

	/**
	 * @Comentários Remove o produto selecionado na grid da lista.
	 * @author eduardo.fernandes
	 */
	public void removerProdutoLista(){
		List<Produto> listaTemp = new ArrayList<Produto>();
		listaTemp.addAll(listProdutoSelected);
		if(listaTemp.contains(produtoSelecionado)){
			listProdutoSelected.remove(produtoSelecionado);
			limparResiduoOuProduto(produtoSelecionado);
			pesquisarProdutos();
		}
		JsfUtil.addSuccessMessage(Util.getString("lac_dadosGerais_msg005"));
		listaTemp = null;
	}

	/**
	 * @Comentários Após adicioar um residuo na lista, chama-se esse metódo para anular o combo.
	 * @author eduardo.fernandes
	 */
	public void limparComboResiduo(){
		if(!Util.isNullOuVazio(listResiduoSelected)){
			for(Residuo residuo : listResiduoSelected){
				limparResiduoOuProduto(residuo);
			}
			listResiduoSelected.clear();
		}
	}

	/**
	 * @Comentários Adiciona o residuo selecionado na lista
	 * @author eduardo.fernandes
	 */
	public void adicionarResiduoLista(){
		if(Util.isNullOuVazio(listResiduoSelected)){
			listResiduoSelected = new ArrayList<Residuo>();
		}
		if(!listResiduoSelected.contains(residuoSelecionado)){
			listResiduoSelected.add(residuoSelecionado);
			if(isOutro(residuoSelecionado)){
				residuoSelecionado.setOutro(true);
				residuoSelecionado.setQtdTransporteAnual(new Double(1));
				JsfUtil.addWarnMessage(Util.getString("lac_dadosGerais_info005"));
				JsfUtil.addWarnMessage(Util.getString("lac_dadosGerais_info0056"));
			}
			residuoSelecionado = new Residuo();
			nomeResiduo = null;
			pesquisarResiduos();
		} else {
			JsfUtil.addErrorMessage("O resíduo " + Util.getString("lac_dadosGerais_mensagens_alerta_escolhido"));
		}
	}

	/**
	 * @Comentários Método genérico para zerar o Produto/Residuo quando ele for removido da lista de selecionado.
	 * @param Produto
	 * @param Residuo
	 * @author eduardo.fernandes
	 */
	public void limparResiduoOuProduto(Object object){
		if (object instanceof Produto) {
			((Produto) object).setDesabilitaQtd(false);
			((Produto) object).setQtdTransporteAnual(null);
		}
		else if(object instanceof Residuo) {
			((Residuo) object).setDesabilitaQtd(false);
			((Residuo) object).setQtdTransporteAnual(null);
		}
	}

	/**
	 * @Comentários Remove o residuo selecionado na grid da lista.
	 * @author eduardo.fernandes
	 */
	public void removerResiduoLista(){
		List<Residuo> listaTemp = new ArrayList<Residuo>();
		listaTemp.addAll(listResiduoSelected);
		if(listaTemp.contains(residuoSelecionado)){
			listResiduoSelected.remove(residuoSelecionado);
			limparResiduoOuProduto(residuoSelecionado);
		}
		JsfUtil.addSuccessMessage(Util.getString("lac_dadosGerais_msg005"));
		listaTemp = null;
	}

	/**
	 * @Comentários Método para tentar salvar a LacTransporte, chamado no clique do botão FINALIZAR ou AVANÇAR na [abaDadosGerais].
	 * Retorna true caso a LacTransporte seja salva no BD ou false caso ocorra algum erro.
	 * @see salvarLac()
	 * @see validarAbaDadosGerais()
	 * @return true or false
	 * @author eduardo.fernandes
	 */
	public boolean salvarLacTransporte(){
		boolean salvo = false;
		if(validarAbaDadosGerais()){
			try {
				if(salvarLac()){
					if(Util.isNullOuVazio(lacTransporte.getIdeLocalizacaoGeografica())){
						lacTransporte.setIdeLocalizacaoGeografica(null);
					}
					if(lacTransporte.getIndAtendimentoEmergenciaTerceiro()){
						if(!pjExistente){
							salvarEnderecoPessoaEndereco();
						}
						/*lacTransporte.setIdePessoaJuridica(pessoaJuridicaController.getPessoaJuridicaSelecionada());*/
					} else {
						lacTransporte.setIdePessoaJuridica(null);
					}
					// Salva-se primeiro a lac_transporte, pois precisamos de seu Ide
					lacTransporteService.salvarLacTransporte(lacTransporte);
					// Depois exclui-se a tabela associativa lac_transporte_produto e lac_transporte_residuo para evitar problemas na EDIÇÃO
					lacTransporteService.excluirLacTransporteProdutoByIdeLacTransporte(lacTransporte);
					lacTransporteService.excluirLacTransporteResiduoByIdeLacTransporte(lacTransporte);
					// Se necessário, salvam-se as novas listas
					if(lacTransporte.getIndProduto() && !Util.isNullOuVazio(listProdutoSelected)){
						lacTransporteService.salvarListaTransportadosLac(lacTransporte, listProdutoSelected);
					}
					if(lacTransporte.getIndResiduo() && !Util.isNullOuVazio(listResiduoSelected)){
						lacTransporteService.salvarListaTransportadosLac(lacTransporte, listResiduoSelected);
					}
					if(!lacTransporte.getIndBaseOperacional()){
						excluirBaseOperacional();
					}
					salvo = true;
				}
			}  catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_mensagens_erro002") + " à Lac Transporte: " + e.getMessage());
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
		return salvo;
	}

	/**
	 * 
	 * @author eduardo.fernandes
	 */
	public void finalizarLacTransporte() {
		if(salvarLacTransporte()){
			JsfUtil.addSuccessMessage(Util.getString("lac_dadosGerais_msg001"));
			RequestContext.getCurrentInstance().execute("lac_transporte.hide()");
			verificarExibicaoRelatorio();
		}
	}

	public void verificarExibicaoRelatorio(){
		if(!isTemVeiculo()){
			JsfUtil.addWarnMessage(Util.getString("lac_dadosGerais_info010"));
			mostraRelatorio = false;
		}
		if(lacTransporte.getIndProduto()){
			avisoOutros(listProdutoSelected);
		}
		if(lacTransporte.getIndResiduo()){
			avisoOutros(listResiduoSelected);
		}
		if(mostraRelatorio){
			RequestContext.getCurrentInstance().execute("rel_transportadora.show()");
			RequestContext.getCurrentInstance().execute("updateDocumento()");
		}
	}

	/**
	 * @Comentários Caso a lista de Produto/Residuo transportado contenha "Outros" deve-se mandar um aviso para o usuário lembrando-lhe que a lac não está finalizada.
	 * @param list
	 * @author eduardo.fernandes
	 */
	public void avisoOutros(List<?> list){
		for (Object object : list) {
			if(object instanceof Produto){
				if(isOutro(object)){
					JsfUtil.addWarnMessage(Util.getString("lac_dadosGerais_info009_produto"));
					mostraRelatorio = false;
					break;
				}
			}
			else if(object instanceof Residuo){
				if(isOutro(object)){
					JsfUtil.addWarnMessage(Util.getString("lac_dadosGerais_info009_residuo"));
					mostraRelatorio = false;
					break;
				}
			}
		}
	}

	/**
	 * @Comentários
	 * @author eduardo.fernandes
	 */
	public void pesquisarProdutos() {
		try {
			listProdutos = produtoService.pesquisarProdutos(!Util.isNullOuVazio(nomeProduto) ? new Produto(nomeProduto) : new Produto());
			if(Util.isNullOuVazio(getListProdutos())){
				listProdutos = new ArrayList<Produto>();
				listProdutos.add(produtoService.obterProdutoByIde(LacTransporteOutroEnum.PRODUTO_OUTROS.getId()));
				JsfUtil.addWarnMessage(Util.getString("lac_dadosGerais_info008_produto"));
			}
			removerOsSelecionadosDaListaPrincipal(listProdutoSelected);
		}
		catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
		}
	}

	/**
	 * @Comentários
	 * @author eduardo.fernandes
	 */
	public void pesquisarResiduos() {
		try {
			listResiduos = residuoService.pesquisarResiduos(!Util.isNullOuVazio(nomeResiduo) ? new Residuo(nomeResiduo) : new Residuo());
			if(Util.isNullOuVazio(listResiduos)){
				listResiduos = new ArrayList<Residuo>();
				listResiduos.add(residuoService.obterResiduoByIde(LacTransporteOutroEnum.RESIDUO_OUTROS.getId()));
				JsfUtil.addWarnMessage(Util.getString("lac_dadosGerais_info008_residuo"));
			}
			removerOsSelecionadosDaListaPrincipal(listResiduoSelected);
		}
		catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
		}
	}

	public void excluirLocGeo(){
		LocalizacaoGeografica geografica = lacTransporte.getIdeLocalizacaoGeografica();
		lacTransporte.setIdeLocalizacaoGeografica(null);
		try {
			lacTransporteService.salvarLacTransporte(lacTransporte);
			localizacaoGeograficaService.excluirByIdLocalizacaoGeografica(geografica);
			geografica = null;
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public StreamedContent imprimirCertificado() throws Exception {
		Map<String, Object> lParametros = new HashMap<String, Object>();
		//Buscar Classes de risco de dos produtos a partir do requerimento informado

		lParametros.put("ide_req", this.requerimento.getIdeRequerimento());
		lParametros.put("classes", tratarClasseRisco());
		return new RelatorioUtil("certificado_lac_transporte_2.jasper", lParametros).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
	}

	public String tratarClasseRisco(){
		List<LacTransporteProduto> listaTransporteProduto;
		String str = "";
		try {
			listaTransporteProduto = lacTransporteService.buscarClasseRisco(this.requerimento);
			
			//Divide as classes no formato padrão de texto.
			for (int x = 0; x < listaTransporteProduto.size(); x++) {
				
				if(listaTransporteProduto.size() - x == 2 
						&& listaTransporteProduto.get(x) != null
						&& listaTransporteProduto.get(x).getIdeProduto() != null
						&& !Util.isNullOuVazio(listaTransporteProduto.get(x).getIdeProduto().getDscClasseRisco())
						&& !str.contains(listaTransporteProduto.get(x).getIdeProduto().getDscClasseRisco().substring(0, 1))){
					
					str +=listaTransporteProduto.get(x).getIdeProduto().getDscClasseRisco().substring(0, 1)+" e ";
					
				} else if(listaTransporteProduto.size() - x == 1
						&& listaTransporteProduto.get(x) != null
						&& listaTransporteProduto.get(x).getIdeProduto() != null
						&& !Util.isNullOuVazio(listaTransporteProduto.get(x).getIdeProduto().getDscClasseRisco())
						&& !str.contains(listaTransporteProduto.get(x).getIdeProduto().getDscClasseRisco().substring(0, 1))){
					
					str +=listaTransporteProduto.get(x).getIdeProduto().getDscClasseRisco().substring(0, 1);
					
				} else if(listaTransporteProduto.get(x) != null
						&& listaTransporteProduto.get(x).getIdeProduto() != null
						&& !Util.isNullOuVazio(listaTransporteProduto.get(x).getIdeProduto().getDscClasseRisco())
						&& !str.contains(listaTransporteProduto.get(x).getIdeProduto().getDscClasseRisco().substring(0, 1))){
					
					str +=listaTransporteProduto.get(x).getIdeProduto().getDscClasseRisco().substring(0, 1)+", ";
				}
			}
			
			//Caso haja apenas uma classe de risco remove a virgula do fim
			if(str.endsWith(", ")){
				str = str.substring(0, str.length()-2);
			}
			
			//Caso a String esteja no formato 1, 2 ou 1,2, 3 e assim sucessivamente adiciona o "e" ficando 1 e 2 ou 1, 2 e 3
			if(!str.contains("e") && str.length() > 4){
				str = str.substring(0, str.length()-3) + str.substring(str.length()-3).replaceAll(",", " e");
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}

		return str;
	}

	public StreamedContent getImprimirRelatorioFinal() throws Exception {
		lacTransporte = lacTransporteService.buscarLacTransporteByIdeRequerimento(requerimento);
		return getImprimirRelatorio();
	}

	public StreamedContent getImprimirRelatorio() throws Exception {
		Map<String, Object> lParametros = new HashMap<String, Object>();
		lParametros.put("ide_lac", lacTransporte.getIdeLac().getIdeLac());
		return new RelatorioUtil("lac_transportadora.jasper", lParametros).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
	}
	
	public int getShape(){
		return ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_SHAPEFILE.getId().intValue();
	}

	/**
	 * @Comentários Validação dos campos da [abaDadosGerais]
	 * @return true or false
	 * @author eduardo.fernandes
	 */
	public boolean validarAbaDadosGerais(){
		boolean valido = true;
		if((Util.isNullOuVazio(lacTransporte.getIndProduto()) || !lacTransporte.getIndProduto()) && (Util.isNullOuVazio(lacTransporte.getIndResiduo()) || !lacTransporte.getIndResiduo())){
			JsfUtil.addErrorMessage("A seleção do tipo de transporte "+Util.getString("lac_dadosGerais_msg003"));
			valido = false;
		}
		if((!Util.isNullOuVazio(lacTransporte.getIndProduto()) && lacTransporte.getIndProduto()) && Util.isNullOuVazio(listProdutoSelected)){
			JsfUtil.addErrorMessage("A seleção do produto transportado "+Util.getString("lac_dadosGerais_msg003"));
			valido = false;
		} else if (!Util.isNullOuVazio(listProdutoSelected) && !listProdutoSelected.isEmpty()){
			for(Produto produto : listProdutoSelected){
				if(Util.isNullOuVazio(produto.getQtdTransporteAnual()) && !produto.isOutro()){
					JsfUtil.addErrorMessage("A quantidade média anual do produto transportado "+Util.getString("lac_dadosGerais_msg003"));
					valido = false;
					break;
				}
			}
		}
		if((!Util.isNullOuVazio(lacTransporte.getIndResiduo()) && lacTransporte.getIndResiduo()) && Util.isNullOuVazio(listResiduoSelected)){
			JsfUtil.addErrorMessage("A seleção do resíduo transportado "+Util.getString("lac_dadosGerais_msg003"));
			valido = false;
		} else if(!Util.isNullOuVazio(listResiduoSelected) && !listResiduoSelected.isEmpty()){
			for(Residuo residuo : listResiduoSelected){
				if(Util.isNullOuVazio(residuo.getQtdTransporteAnual()) && !residuo.isOutro()){
					JsfUtil.addErrorMessage("A quantidade média anual do resíduo transportado "+Util.getString("lac_dadosGerais_msg003"));
					valido = false;
					break;
				}
			}
		}
		/*if(Util.isNullOuVazio(lacTransporte.getIdeLocalizacaoGeografica())){
			JsfUtil.addErrorMessage("O Roteiro Programado para Transporte "+Util.getString("lac_dadosGerais_msg003"));
			valido = false;
		}*/
		if((Util.isNullOuVazio(lacTransporte.getIndAtendimentoEmergenciaEmpresa()) || !lacTransporte.getIndAtendimentoEmergenciaEmpresa())
				&& (Util.isNullOuVazio(lacTransporte.getIndAtendimentoEmergenciaTerceiro()) || !lacTransporte.getIndAtendimentoEmergenciaTerceiro())){
			JsfUtil.addErrorMessage("A caracterização do atendimento a emergências ambientais "+Util.getString("lac_dadosGerais_msg003"));
			valido = false;
		} else if((!Util.isNullOuVazio(lacTransporte.getIndAtendimentoEmergenciaTerceiro()) && lacTransporte.getIndAtendimentoEmergenciaTerceiro()) && !pjExistente){
			if(!validarPJeEndereco()){
				valido = false;
			}
		}
		if(Util.isNullOuVazio(lacTransporte.getIndBaseOperacional())){
			JsfUtil.addErrorMessage("A  declaração da Base Operacional "+Util.getString("lac_dadosGerais_msg003"));
			valido = false;
		}
		return valido;
	}

	/**
	 * @Comentários Quando o usuário desmarca o checkBox "Terceiros" na [abaDadosGerais], faz-se necessário remover as informações da tela sem excluir a PJ do banco.
	 * @author eduardo.fernandes
	 */
	public void limparTerceiro(ValueChangeEvent event){
		numCnpj = null;
		inicializarEndereco();
		
		if((Boolean) event.getNewValue()){
			
			inicializarPessoaJuridica();
			
		} 
		else {
			
			lacTransporte.setIdePessoaJuridica(null);
			super.pessoa  = null;
			
			pjExistente = false;
			blockForm = true;
			
		}

		desabilitarParaVisualizacao();
	}

	private void inicializarPessoaJuridica() {
		lacTransporte.setIdePessoaJuridica(new PessoaJuridica());
		lacTransporte.getIdePessoaJuridica().setPessoa(new Pessoa());
		lacTransporte.getIdePessoaJuridica().getPessoa().setDtcCriacao(new Date());
		
		super.pessoa  = lacTransporte.getIdePessoaJuridica().getPessoa();
	}

	/**
	 * @Comentários Método para buscar o CNPJ digitado pelo usuário no banco de dados.Caso exista, as informações dessa pessoa jurídica são carregados na tela.
	 * Caso não exista, desbloqueia-se os campos e uma mensagem pede para o usuário cadastrar a PJ, telefone e endereço.
	 * @author eduardo.fernandes
	 */
	public void consultarPJ(){
		try{
			
			if(!numCnpj.equals(lacTransporte.getIdePessoaJuridica().getNumCnpj())){
				lacTransporte.setIdePessoaJuridica(pessoaJuridicaService.filtrarPJbyCNPJlacTransporte(new PessoaJuridica(numCnpj)));
			}

			if(!Util.isNullOuVazio(lacTransporte.getIdePessoaJuridica())){
				super.pessoa  = lacTransporte.getIdePessoaJuridica().getPessoa();
				
				// Preenche a lista de telefone na tela;
				carregarTelefonePessoaJuridica();

				// Preenche o cep e faz a busca
				super.prepararEnderecoGenericoController();

				blockForm = true;
				pjExistente = true;
				
				if(Util.isNull(lacTransporte.getIdeLacTransporte())){
					JsfUtil.addSuccessMessage(Util.getString("lac_dadosGerais_msg001"));
				}
			} 
			else {
				inicializarPessoaJuridica();
				inicializarEndereco();
				
				lacTransporte.getIdePessoaJuridica().setNumCnpj(numCnpj);
				pjExistente = false;
				blockForm = false;
				JsfUtil.addWarnMessage(Util.getString("lac_dadosGerais_info007"));
			}
			
			desabilitarParaVisualizacao();
		} 
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_mensagens_erro001") + "de informações da Empresa Contratada.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void carregarTelefonePessoaJuridica() {
		lacTransporte.getIdePessoaJuridica().getPessoa().setTelefoneCollection(carregarListaTelefone());
	}
	
	private Collection<Telefone> carregarListaTelefone() {
		try {
			return telefoneService.listarTelefone(super.pessoa );
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("")); // ADICIONAR MSG DE ERRO
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * @Comentários Validação da Pessoa Jurídica e do Endereço dela.
	 * @return true or false
	 * @author eduardo.fernandes
	 */
	public boolean validarPJeEndereco(){
		boolean valido = true;
		if(Util.isNullOuVazio(lacTransporte.getIdePessoaJuridica().getNumCnpj())){
			JsfUtil.addErrorMessage("O campo CNPJ "+Util.getString("lac_dadosGerais_msg003"));
			valido = false;
		} else if(!validaTamanhoString(lacTransporte.getIdePessoaJuridica().getNumCnpj(), 20)){
			JsfUtil.addErrorMessage("O campo CNPJ excede o limite de caracteres.");
			valido = false;
		}

		if(Util.isNullOuVazio(lacTransporte.getIdePessoaJuridica().getNomRazaoSocial())){
			JsfUtil.addErrorMessage("O campo Razão Social "+Util.getString("lac_dadosGerais_msg003"));
			valido = false;
		} else if (!validaTamanhoString(lacTransporte.getIdePessoaJuridica().getNomRazaoSocial(), 200)){
			JsfUtil.addErrorMessage("O campo Razão Social excede o limite de caracteres.");
			valido = false;
		}

		if((Util.isNullOuVazio(lacTransporte.getIdePessoaJuridica().getPessoa()) || Util.isNullOuVazio(lacTransporte.getIdePessoaJuridica()))
				&& Util.isNullOuVazio(lacTransporte.getIdePessoaJuridica().getPessoa().getDesEmail())){
			JsfUtil.addErrorMessage("O campo e-mail "+Util.getString("lac_dadosGerais_msg003"));
			valido = false;
		} else if(!validaTamanhoString(lacTransporte.getIdePessoaJuridica().getPessoa().getDesEmail(), 70)){
			JsfUtil.addErrorMessage("O campo e-mail excede o limite de caracteres.");
			valido = false;
		}

		if(!isTemTelefoneTerceiro()){
			JsfUtil.addErrorMessage("O campo de telefone "+Util.getString("lac_dadosGerais_msg003"));
			valido = false;
		}
		
		return valido;
	}

	/**
	 * @return boolean
	 * @author eduardo.fernandes
	 * @Commentarios Retorna false se o tamanho da variavel (String) exceder o tamanho especificado
	 */
	public boolean validaTamanhoString(String string, int tamanho){
		boolean valido = true;
		if(string.trim().length() > tamanho){
			valido = false;
		}
		return valido;
	}

	public StreamedContent getImprimirCertificado() {
		try {
			Integer ideRequerimento = this.requerimento.getIdeRequerimento();

			if (!this.lacServiceFacade.hasCertificado(ideRequerimento)) {
				AtoAmbiental ato = new AtoAmbiental(AtoAmbientalEnum.LAC.getId(), AtoAmbientalEnum.LAC.getSigla());
				Certificado certificado = certificadoUtil.gerarCertificado(ato, this.requerimento);
				this.lacServiceFacade.salvarCertificadoLac(certificado);
			}else if(!this.lacServiceFacade.hasToken(ideRequerimento)){
				Certificado certificado = this.lacServiceFacade.carregarCertificado(ideRequerimento);
				this.lacServiceFacade.atualizarTokenCertificadoLac(certificado);
			}
			return this.imprimirCertificado();
		} 
		catch (Exception e) {
			JsfUtil.addErrorMessage("Ocorreu um erro ao tentar imprimir o certificado.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void excluirBaseOperacional(){
		try {
			BaseOperacional baseOperacional = baseOperacionalService.buscarBaseOperacionalByIdeLacTransporte(lacTransporte);
			if(!Util.isNullOuVazio(baseOperacional)){
				baseOperacionalService.excluirBaseOperacionalServicoByIdeBaseOperac(baseOperacional);
				baseOperacionalService.excluirBaseOperacionalControleAmbientalByIdeBaseOperac(baseOperacional);
				baseOperacionalService.excluirBaseOperacional(lacTransporte);
				baseOperacionalController.limparAbaBaseOperacional();
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_mensagens_erro003") + "a Base Operacional");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	

	// TELEFONE
	public void inicializarTelefone(){
		telefone = new Telefone(super.pessoa );
		
		if(Util.isNullOuVazio(listaTiposTelefone)){
			carregarListaTiposTelefone();
		}
		
	}
	
	public void adicionarTelefone(){
		if(isTelefoneValido()){
			if(!isTemTelefoneTerceiro()){
				lacTransporte.getIdePessoaJuridica().getPessoa().setTelefoneCollection(new ArrayList<Telefone>());
			}
			if(!lacTransporte.getIdePessoaJuridica().getPessoa().getTelefoneCollection().contains(telefone)){
				lacTransporte.getIdePessoaJuridica().getPessoa().getTelefoneCollection().add(telefone);
			} 
			else {
				JsfUtil.addWarnMessage("Esse telefone já foi cadastrado na lista.");
			}
			inicializarTelefone();
		}
	}
	
	private boolean isTelefoneValido(){
		boolean valido = true;
		if(Util.isNullOuVazio(telefone.getIdeTipoTelefone())){
			JsfUtil.addWarnMessage("O campo tipo " + Util.getString("lac_dadosGerais_msg003"));
			valido = false;
		}
		if(Util.isNullOuVazio(telefone.getNumTelefone())){
			JsfUtil.addWarnMessage("O campo número " + Util.getString("lac_dadosGerais_msg003"));
			valido = false;
		}
		return valido;
	}
	
	public void excluirTelefone(){
		try {
			lacTransporte.getIdePessoaJuridica().getPessoa().getTelefoneCollection().remove(telefone);
			telefoneService.excluirTelefone(telefone);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("")); // ADICIONAR MSG DE ERRO
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void salvarTelefone(){
		try{
			telefoneService.salvarTelefones(lacTransporte.getIdePessoaJuridica().getPessoa().getTelefoneCollection());
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao salvar a lista de telefone: " + e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	
	public void carregarListaTiposTelefone() {
		try {
			listaTiposTelefone = tipoTelefoneService.listarTipoTelefone();
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public boolean isTemTelefoneTerceiro(){
		return !Util.isNullOuVazio(lacTransporte.getIdePessoaJuridica().getPessoa().getTelefoneCollection());
	}
	
	// ENDEREÇO
	private void inicializarEndereco() {
		super.enderecoGenericoController.init();
		super.enderecoGenericoController.setCepConsultado(null);
		
		super.enderecoPessoa = null;
	}
	
	@Override
	public void enviarId() {
		super.enviarId("tabAbasId:formDadosGerais");
	}
	
	@Override
	public void desabilitarParaVisualizacao() {
		super.enderecoGenericoController.setVisualizacao(blockForm);
		super.enderecoGenericoController.atualizarFormEnderecoPesquisa();
		super.enderecoGenericoController.atualizarFormEndereco();
	}
	
	@Override
	public void salvarEnderecoPessoaEndereco() throws Exception {
		if(!pjExistente){
			
			if(validarPJeEndereco()){
				
				pessoaJuridicaService.salvarOuAtualizarPessoaJuridica(lacTransporte.getIdePessoaJuridica());
				
				super.salvarEnderecoPessoaEndereco();
				
				salvarTelefone();
				
				pjExistente = true;
				blockForm = true;
				
				desabilitarParaVisualizacao();
				
				JsfUtil.addSuccessMessage(Util.getString("lac_dadosGerais_msg001"));
			}
			
		}
		
	}
	
	public br.gov.ba.seia.entity.Lac buscarLac(Requerimento req){
		
		try {
			
			Lac lac = lacTransporteService.buscarLac(requerimento);
			
			if(Util.isNullOuVazio(lac)){
				lacTransporte = new LacTransporte(requerimento);
			}else{
				lacTransporte = new LacTransporte(requerimento, lac);
			}
			
			return lac;
		} catch (Exception e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public int getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(int activeTab) {
		this.activeTab = activeTab;
	}

	public Requerimento getRequerimento() {
		return requerimento;
	}

	public void setRequerimento(Requerimento requerimento) {
		this.requerimento = requerimento;
	}

	public Boolean getBlockForm() {
		return blockForm;
	}

	public boolean isPjExistente() {
		return pjExistente;
	}

	public void setPjExistente(boolean pjExistente) {
		this.pjExistente = pjExistente;
	}

	public LacTransporte getLacTransporte() {
		return lacTransporte;
	}

	public void setLacTransporte(LacTransporte lacTransporte) {
		this.lacTransporte = lacTransporte;
	}

	public List<Produto> getListProdutoSelected() {
		return listProdutoSelected;
	}

	public void setListProdutoSelected(List<Produto> listProdutoSelected) {
		this.listProdutoSelected = listProdutoSelected;
	}

	public LacTransporteProduto getTransporteProduto() {
		return transporteProduto;
	}

	public void setTransporteProduto(LacTransporteProduto transporteProduto) {
		this.transporteProduto = transporteProduto;
	}

	public List<EmpreendimentoVeiculo> getListEmpreendimentoVeiculos() {
		return listEmpreendimentoVeiculos;
	}

	public Produto getProdutoSelecionado() {
		return produtoSelecionado;
	}

	public void setProdutoSelecionado(Produto produtoSelecionado) {
		this.produtoSelecionado = produtoSelecionado;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public String getNomeResiduo() {
		return nomeResiduo;
	}

	public void setNomeResiduo(String nomeResiduo) {
		this.nomeResiduo = nomeResiduo;
	}

	public Residuo getResiduoSelecionado() {
		return residuoSelecionado;
	}

	public void setResiduoSelecionado(Residuo residuoSelecionado) {
		this.residuoSelecionado = residuoSelecionado;
	}

	public List<Residuo> getListResiduoSelected() {
		return listResiduoSelected;
	}

	public void setListResiduoSelected(List<Residuo> listResiduoSelected) {
		this.listResiduoSelected = listResiduoSelected;
	}

	public List<Imovel> getImovel() {
		return imovel;
	}

	public void setImovel(List<Imovel> imovel) {
		this.imovel = imovel;
	}

	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}

	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
	}

	public List<Produto> getListProdutos() {
		return listProdutos;
	}

	public void setListProdutos(List<Produto> listProdutos) {
		this.listProdutos = listProdutos;
	}

	public List<Residuo> getListResiduos() {
		return listResiduos;
	}

	public void setListResiduo(List<Residuo> listResiduo) {
		this.listResiduos = listResiduo;
	}

	// INI -> Métodos para controle de componentes que serão renderizados ou não na [abaDadosGerais].
	public boolean isTemLocGeo(){
		return (!Util.isNullOuVazio(lacTransporte) && !Util.isNullOuVazio(lacTransporte.getIdeLocalizacaoGeografica()));
	}

	public boolean isTemVeiculo(){
		return (!Util.isNullOuVazio(listEmpreendimentoVeiculos));
	}

	public boolean isTemProdutoSelecionado(){
		return ((!Util.isNullOuVazio(listProdutoSelected) && !listProdutoSelected.isEmpty()) && (!Util.isNullOuVazio(lacTransporte) && lacTransporte.getIndProduto()));
	}

	public boolean isTemResiduoSelecionado(){
		return ((!Util.isNullOuVazio(listResiduoSelected) && !listResiduoSelected.isEmpty()) && (!Util.isNullOuVazio(lacTransporte) && lacTransporte.getIndResiduo()));
	}

	public boolean isOutro(Object obj){
		if(obj instanceof Produto){
			if(((Produto) obj).getIdeProduto().equals(LacTransporteOutroEnum.PRODUTO_OUTROS.getId())){
				return true;
			}
		} else if (obj instanceof Residuo){
			if(((Residuo) obj).getIdeResiduo().equals(LacTransporteOutroEnum.RESIDUO_OUTROS.getId())){
				return true;
			}
		}
		return false;
	}
	// FIM -> Métodos para controle de componentes que serão renderizados ou não na [abaDadosGerais].

	public boolean isMostraRelatorio() {
		return mostraRelatorio;
	}

	public String getNumCnpj() {
		return numCnpj;
	}

	public void setNumCnpj(String numCnpj) {
		this.numCnpj = numCnpj;
	}
	
	public Telefone getTelefone() {
		return telefone;
	}

	public void setTelefone(Telefone telefone) {
		this.telefone = telefone;
	}

	public Collection<TipoTelefone> getListaTiposTelefone() {
		return listaTiposTelefone;
	}
}