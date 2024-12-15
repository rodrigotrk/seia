package br.gov.ba.seia.facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;

import br.gov.ba.seia.controller.TorresAnemometricasController;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLic;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.EnderecoPessoa;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.LocalizacaoAtividadeTorre;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.ResponsavelEmpreendimento;
import br.gov.ba.seia.entity.TipoNaturezaTorre;
import br.gov.ba.seia.entity.TipoUnidadeConservacaoTorre;
import br.gov.ba.seia.entity.TorreAnemometrica;
import br.gov.ba.seia.entity.TorreAnemometricaLocalizacaoAtividadeTorre;
import br.gov.ba.seia.entity.TorreAnemometricaLocalizacaoAtividadeTorrePK;
import br.gov.ba.seia.service.EnderecoPessoaService;
import br.gov.ba.seia.service.ImovelService;
import br.gov.ba.seia.service.LocalizacaoAtividadeTorreService;
import br.gov.ba.seia.service.PessoaJuridicaService;
import br.gov.ba.seia.service.ResponsavelEmpreendimentoService;
import br.gov.ba.seia.service.TelefoneService;
import br.gov.ba.seia.service.TipoNaturezaTorreService;
import br.gov.ba.seia.service.TipoUnidadeConservacaoTorreService;
import br.gov.ba.seia.service.TorresAnemometricasService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Classe: TorresAnemometricasFacede.java
 * Projeto: seia
 * Pacote: br.gov.ba.seia.facade
 * @autor: diegoraian em 29 de set de 2017
 * Objetivo: 	
	
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TorresAnemometricasFacede {

	/**
	 * Propriedade: facade
	 * @type: PesquisaMineralFacade
	 */
	@EJB
	private PesquisaMineralFacade facade;
	
	/**
	 * Propriedade: torresAnemometricasService
	 * @type: TorresAnemometricasService
	 */
	@EJB
	private TorresAnemometricasService torresAnemometricasService;
	
	/**
	 * Propriedade: imovelService
	 * @type: ImovelService
	 */
	@EJB
	private ImovelService imovelService;
	
	/**
	 * Propriedade: pessoaJuridicaService
	 * @type: PessoaJuridicaService
	 */
	@EJB
	private PessoaJuridicaService pessoaJuridicaService;
	
	/**
	 * Propriedade: cadastroFacade
	 * @type: CadastroAtividadeFacade
	 */
	@EJB
	private CadastroAtividadeFacade cadastroFacade;
	
	/**
	 * Propriedade: naturezaTorreService
	 * @type: TipoNaturezaTorreService
	 */
	@EJB
	private TipoNaturezaTorreService naturezaTorreService;
	
	/**
	 * Propriedade: tipoUnidadeConservacaoService
	 * @type: TipoUnidadeConservacaoTorreService
	 */
	@EJB
	private TipoUnidadeConservacaoTorreService tipoUnidadeConservacaoService;
	
	/**
	 * Propriedade: localizacaoAtividadeTorreService
	 * @type: LocalizacaoAtividadeTorreService
	 */
	@EJB
	private LocalizacaoAtividadeTorreService localizacaoAtividadeTorreService;
	
	@EJB
	private EnderecoPessoaService enderecoPessoaService;
	
	@EJB
	private TelefoneService telefoneService;
	
	@EJB
	private ResponsavelEmpreendimentoService responsavelEmpreendimentoService;
	
	/**
	 *
	 * @param idePessoaJuridica
	 * @return
	 * @throws Exception
	 * @author diegoraian
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaJuridica obterPessoaJuridicaMontada(Integer idePessoaJuridica){
		return pessoaJuridicaService.obterPessoaJuridicaMontada(idePessoaJuridica);
	}

	/**
	 *
	 * @param cadastro
	 * @throws Exception
	 * @author diegoraian
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void tramitarCadastroIncompleto(CadastroAtividadeNaoSujeitaLic cadastro) {
		cadastroFacade.tramitarCadastroIncompleto(cadastro);
	}
	
	/**
	 *
	 * @param cadastro
	 * @throws Exception
	 * @author diegoraian
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void tramitarCadastroConcluido(CadastroAtividadeNaoSujeitaLic cadastro) {
		cadastroFacade.tramitarCadastroConcluido(cadastro);
	}
	
	/**
	 * 
	 * @param empreendimento
	 * @return
	 * @author diegoraian
	 * @throws Exception 
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ResponsavelEmpreendimento> listarResponsaveisTecnicos(Empreendimento empreendimento) throws Exception {
		return facade.listarResponsaveisTecnicos(empreendimento);
	}
	
	/**
	 *
	 * @param empreendimento
	 * @return
	 * @throws Exception
	 * @author diegoraian
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Endereco carregarEndereco(Empreendimento empreendimento) {
		return facade.carregarEndereco(empreendimento);
	}
	
	/**
	 * Acessa serviço que busca um determinado imóvel a partir do código car dele
	 * @param numSicar
	 * @return
	 * @throws Exception
	 * @author diegoraian
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Imovel buscarImovelPorCar(String numSicar){
		return imovelService.buscarImovelPorNumeroCar(numSicar);
	}

	/**
	 *
	 * @param torresAnemometricasController
	 * @author diegoraian
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(TorresAnemometricasController controller) {
		controller.getEmpreendimento().setResponsavelEmpreendimentoCollection(controller.getResponsavelEmpreendimento());
		salvarCadastro(controller.getTorreAnemometrica().getIdeCadastroAtividadeNaoSugeitaLic());
		if(Util.isNullOuVazio(controller.getTorreAnemometrica().getIdeCadastroAtividadeNaoSugeitaLic().getCadastroAtividadeNaoSujeitaLicStatus())){
			tramitarCadastroIncompleto(controller.getTorreAnemometrica().getIdeCadastroAtividadeNaoSugeitaLic());
		}
	}

	/**
	 * 
	 * @author diegoraian
	 * @param cadastroAtividadeNaoSujeitaLic 
	 * @throws Exception 
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarCadastro(CadastroAtividadeNaoSujeitaLic cadastro)  {
		cadastroFacade.salvarCadastroAtividadeNaoSujeitaLicenciamento(cadastro);
	}
	
	/**
	 * Acessa o serviço que lista todos os tipos de naturezas de torre
	 * @return
	 * @throws Exception
	 * @author diegoraian
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoNaturezaTorre> listarTiposNaturezaTorre() throws Exception{
		return naturezaTorreService.listar();
	}
	
	/**
	 * Acessa o serviço que lista todos os tipos de unidade de conservação cadastrados
	 * @return
	 * @throws Exception
	 * @author diegoraian
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoUnidadeConservacaoTorre> listarTiposUnidadeConservacaoTorre()  throws Exception{
		return tipoUnidadeConservacaoService.listar();
	}
	
	/**
	 *
	 * @return
	 * @throws Exception
	 * @author diegoraian
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<LocalizacaoAtividadeTorre> listarLocalizacoesAtividadeTorre()  {
		return localizacaoAtividadeTorreService.listarLocalizacoesAtividadeTorre();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void obterTelefoneEnderecoPessoa(Pessoa pessoa){
		try {
			EnderecoPessoa ep = enderecoPessoaService.buscarEnderecoPorPessoa(pessoa);
			if(ep != null){
				pessoa.setEndereco(ep.getIdeEndereco());
			}
			pessoa.setTelefoneCollection(telefoneService.buscarTelefonesPorPessoa(pessoa));
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações do Requerente.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarTorreAnemometrica(TorreAnemometrica torre) throws Exception{
		torresAnemometricasService.salvarTorreAnemometrica(torre);
		salvarTorreAnemometricaLocalizacaoAtividadeTorre(torre);
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TorreAnemometrica> carregarTorresPorCadastroAtividade(Integer ideCadastroAtividadeNaoSujeitaLic) throws Exception{
		return torresAnemometricasService.carregarTorresPorCadastroAtividade(ideCadastroAtividadeNaoSujeitaLic);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirTorreAnemometrica(TorreAnemometrica torreAnemometrica) throws Exception{
		torresAnemometricasService.excluirTorreAnemometrica(torreAnemometrica);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarTrorreAnemometrica(TorreAnemometrica torreAnemometrica) throws Exception{
		torresAnemometricasService.atualizarTrorreAnemometrica(torreAnemometrica);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<ResponsavelEmpreendimento> listarRespEmpreendimentoPorCadastroAtividadeNaoSujeitaLic(Integer ideCadastroAtividadeNaoSujeitaLic){
		List<ResponsavelEmpreendimento>  responsaveisSelecionados = responsavelEmpreendimentoService.listarPorCadastroAtividadeNaoSujeitaLic(ideCadastroAtividadeNaoSujeitaLic);
		for(ResponsavelEmpreendimento responsavel : responsaveisSelecionados){
			obterTelefoneEnderecoPessoa(responsavel.getIdePessoaFisica().getPessoa());
		}
		return responsaveisSelecionados;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Imovel> carregarImoveisCadastro(Integer ideCadastroAtividadeNaoSujeitaLic) {
		List<Imovel> imoveis = new ArrayList<Imovel>();
		
		List<Imovel> imoveisSelecionados = imovelService.carregarImoveisPorCadastroAtividadeNaoSujeitaLic(ideCadastroAtividadeNaoSujeitaLic);
		if(!Util.isNullOuVazio(imoveisSelecionados)){
			imoveis.addAll(imoveisSelecionados);
		}
		
		return imoveis;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Imovel> carregarImoveisEmpreendimento(Empreendimento empreendimento) {
		List<Imovel> imoveis = new ArrayList<Imovel>();
		
		if(empreendimento != null){
			List<Imovel> imoveisVinculadosEmpreendimento = imovelService.listarImovelRuralPorEmpreendimento(empreendimento);
			if(!Util.isNullOuVazio(imoveisVinculadosEmpreendimento)){
				imoveis.addAll(imoveisVinculadosEmpreendimento);
			}
		}
		return imoveis;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarTorreAnemometricaLocalizacaoAtividadeTorre(TorreAnemometrica torreAnemometrica) throws Exception{
		

		
		if(!Util.isNullOuVazio(torreAnemometrica.getListaLocalizacaoAtividadeTorres())){
			for(LocalizacaoAtividadeTorre torre : torreAnemometrica.getListaLocalizacaoAtividadeTorres()){
				TorreAnemometricaLocalizacaoAtividadeTorre torreAtividade = new TorreAnemometricaLocalizacaoAtividadeTorre();
				
				torreAtividade.setAnemometricaLocalizacaoAtividadeTorrePK(new TorreAnemometricaLocalizacaoAtividadeTorrePK(
						torreAnemometrica.getIdeTorreAnemometrica(), torre.getIdeLocalizacaoAtividadeTorre()));
				torreAtividade.setIdeTorreAnemometrica(torreAnemometrica);
				torreAtividade.setIdeLocalizacaoAtividadeTorre(torre);
				
				torreAtividade.setIndExcluido(Boolean.FALSE);
				
				if(!Util.isNullOuVazio(torreAnemometrica.getTorreAnemometricaLocalizacaoAtividadeTorreListAuxiliar())){
					
					for(TorreAnemometricaLocalizacaoAtividadeTorre torreLoc: torreAnemometrica.getTorreAnemometricaLocalizacaoAtividadeTorreListAuxiliar()){
						
						if(!torreAnemometrica.getListaLocalizacaoAtividadeTorres().contains(torreLoc.getIdeLocalizacaoAtividadeTorre()) ){
							
							excluirTorreAnemometricaLocalizacaoAtividadeTorre(torreLoc);
						}
					}
					
				}
				
				torresAnemometricasService.salvarTorreAnemometricaLocalizacaoAtividadeTorre(torreAtividade);
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirTorreAnemometricaLocalizacaoAtividadeTorre(TorreAnemometricaLocalizacaoAtividadeTorre anemometricaLocalizacaoAtividadeTorre) throws Exception{
		
		anemometricaLocalizacaoAtividadeTorre.setIndExcluido(Boolean.TRUE);
		torresAnemometricasService.excluirTorreAnemometricaLocalizacaoAtividadeTorre(anemometricaLocalizacaoAtividadeTorre);
	}
}
