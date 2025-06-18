package br.gov.ba.seia.facade;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.AtividadeNaoSujeitaLicenciamentoDocumento;
import br.gov.ba.seia.entity.CadastroAtividadeDocumentoIdentificacaoRepresentacao;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLic;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLicComunicacao;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLicDocApensado;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLicImovel;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLicStatus;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLicTipoStatus;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.EnderecoEmpreendimento;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.PesquisaMineral;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.ResponsavelEmpreendimento;
import br.gov.ba.seia.entity.TipoAtividadeNaoSujeitaLicenciamento;
import br.gov.ba.seia.entity.TipoEndereco;
import br.gov.ba.seia.enumerator.CadastroAtividadeNaoSujeitaLicTipoStatusEnum;
import br.gov.ba.seia.enumerator.TipoAtividadeNaoSujeitaLicenciamentoEnum;
import br.gov.ba.seia.enumerator.TipoEnderecoEnum;
import br.gov.ba.seia.service.AtividadeNaoSujeitaLicenciamentoDocumentoService;
import br.gov.ba.seia.service.CadastroAtividadeDocumentoIdentificacaoRepresentacaoService;
import br.gov.ba.seia.service.CadastroAtividadeNaoSujeitaLicComunicacaoService;
import br.gov.ba.seia.service.CadastroAtividadeNaoSujeitaLicDocApensadoService;
import br.gov.ba.seia.service.CadastroAtividadeNaoSujeitaLicResponsavelEmpreendimentoService;
import br.gov.ba.seia.service.CadastroAtividadeNaoSujeitaLicService;
import br.gov.ba.seia.service.CadastroAtividadeNaoSujeitaLicStatusService;
import br.gov.ba.seia.service.CadastroAtividadeNaoSujeitoLicImovelService;
import br.gov.ba.seia.service.DocumentoIdentificacaoService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.service.ProcuradorRepEmpreendimentoService;
import br.gov.ba.seia.service.RepresentanteLegalService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * @author eduardo.fernandes
 * @since 02/12/2016
 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CadastroAtividadeFacade {

	@EJB
	private CadastroAtividadeNaoSujeitaLicService cadastroService;

	@EJB
	private CadastroAtividadeNaoSujeitaLicStatusService cadastroStatusService;

	@EJB
	private CadastroAtividadeNaoSujeitaLicDocApensadoService docApensadoService;

	@EJB
	private CadastroAtividadeDocumentoIdentificacaoRepresentacaoService docRepresentacaoService;

	@EJB
	private CadastroAtividadeNaoSujeitaLicComunicacaoService comunicacaoService;
	
	@EJB
	private AtividadeNaoSujeitaLicenciamentoDocumentoService atividadeNaoSujeitaLicenciamentoDocumentoService;

	@EJB
	private EnderecoFacade enderecoFacade;

	@EJB
	private GerenciaArquivoService arquivoService;
	
	@EJB
	private PessoaFacade pessoaFacade;
	
	@EJB 
	private DocumentoIdentificacaoService docIdentificacaoService;
	
	@EJB 
	private RepresentanteLegalService repLegalService;
	
	@EJB 
	private ProcuradorRepEmpreendimentoService procuradorRepService;
	
	@EJB
	private PesquisaMineralFacade pesquisaMineralFacade;
	
	@EJB
	private EmpreendimentoService empreendimentoService;
	
	@EJB
	private CadastroAtividadeNaoSujeitaLicResponsavelEmpreendimentoService cadastroAtividadeNaoSujeitaLicResponsavelEmpreendimentoService;
	
	@EJB
	private CadastroAtividadeNaoSujeitoLicImovelService cadastroAtividadeNaoSujeitaLicImovelService;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void tramitar(CadastroAtividadeNaoSujeitaLic cadastro, CadastroAtividadeNaoSujeitaLicTipoStatusEnum tipoStatusEnum) {
		try {
			CadastroAtividadeNaoSujeitaLicStatus cadastroStatus = new CadastroAtividadeNaoSujeitaLicStatus(cadastro, tipoStatusEnum, ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica());
			if(!Util.isNullOuVazio(cadastro.getCadastroAtividadeNaoSujeitaLicStatus())){
				if (!cadastroStatus.getCadastroAtividadeNaoSujeitaLicTipoStatus().equals(cadastro.getCadastroUltimoStatus().getCadastroAtividadeNaoSujeitaLicTipoStatus())){
					cadastro.addCadastroAtividadeNaoSujeitaLicStatus(cadastroStatus);
					cadastroStatusService.salvar(cadastroStatus);
				}
			} 
			else {
				cadastro.addCadastroAtividadeNaoSujeitaLicStatus(cadastroStatus);
				cadastroStatusService.salvar(cadastroStatus);
			}
			
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " o novo status do Cadastro.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);

		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void tramitarCadastroConcluido(CadastroAtividadeNaoSujeitaLic cadastro) {
		try {
			salvarDocumentos(cadastro);
			tramitar(cadastro, CadastroAtividadeNaoSujeitaLicTipoStatusEnum.CONCLUIDO);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " os documentos do Cadastro.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		}
	}

	private void salvarDocumentos(CadastroAtividadeNaoSujeitaLic cadastro) {
		salvarListaDocumentoApensado(cadastro);
		salvarListaDocumentoIdentificacaoRepresentacao(cadastro);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void tramitarCadastroPendenciaValidacao(CadastroAtividadeNaoSujeitaLic cadastro) {
		try {
			salvarDocumentos(cadastro);
			tramitar(cadastro, CadastroAtividadeNaoSujeitaLicTipoStatusEnum.PENDENCIA_VALIDACAO);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " os documentos do Cadastro.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void tramitarCadastroSendoValidado(CadastroAtividadeNaoSujeitaLic cadastro) {
		tramitar(cadastro, CadastroAtividadeNaoSujeitaLicTipoStatusEnum.SENDO_VALIDADO);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void tramitarCadastroAguardandoValidacao(CadastroAtividadeNaoSujeitaLic cadastro) {
		tramitar(cadastro, CadastroAtividadeNaoSujeitaLicTipoStatusEnum.AGUARDANDO_VALIDACAO);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void tramitarCadastroCompleto(CadastroAtividadeNaoSujeitaLic cadastro) {
		try {
			if(Util.isNullOuVazio(cadastro.getNumCadastro())){
				cadastro.setNumCadastro(gerarNumeroCadastro(getSiglaCadastro(cadastro)));
			}
			tramitar(cadastro, CadastroAtividadeNaoSujeitaLicTipoStatusEnum.CADASTRO_COMPLETO);
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " o número do Cadastro.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizarCadastro(CadastroAtividadeNaoSujeitaLic cadastro) {
		if(cadastro.getCadastroUltimoStatus().getCadastroAtividadeNaoSujeitaLicTipoStatus().equals(new CadastroAtividadeNaoSujeitaLicTipoStatus(CadastroAtividadeNaoSujeitaLicTipoStatusEnum.CADASTRO_INCOMPLETO))){
			tramitarCadastroCompleto(cadastro);
			tramitarCadastroAguardandoValidacao(cadastro);
		} 
		else if (cadastro.getCadastroUltimoStatus().getCadastroAtividadeNaoSujeitaLicTipoStatus().equals(new CadastroAtividadeNaoSujeitaLicTipoStatus(CadastroAtividadeNaoSujeitaLicTipoStatusEnum.PENDENCIA_VALIDACAO))){
			tramitarCadastroAguardandoValidacao(cadastro);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void tramitarCadastroIncompleto(CadastroAtividadeNaoSujeitaLic cadastro) {
		tramitar(cadastro, CadastroAtividadeNaoSujeitaLicTipoStatusEnum.CADASTRO_INCOMPLETO);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCadastroAtividadeNaoSujeitaLicenciamento(CadastroAtividadeNaoSujeitaLic cadastro){
		cadastroService.salvar(cadastro);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String gerarNumeroCadastro(String siglaCadastro) {
		StringBuilder numeroCaepog = new StringBuilder();

		String ultimoNumero = cadastroService.getUltimoNumeroCadastro();

		if (Util.isNullOuVazio(ultimoNumero)) {
			ultimoNumero = String.valueOf(1);
		}
		else {
			ultimoNumero = String.valueOf(Integer.parseInt(ultimoNumero) + 1);
		}

		numeroCaepog
				.append(new SimpleDateFormat("yyyy").format(new Date()))
				.append('.');

		numeroCaepog
				.append("001")
				.append('.');

		numeroCaepog
				.append(Util.lpad(ultimoNumero, '0', 6))
				.append('/');

		numeroCaepog
				.append("INEMA")
				.append("/" + siglaCadastro);

		return numeroCaepog.toString();
	}

	public String getSiglaCadastro(CadastroAtividadeNaoSujeitaLic cadastro) {
		if (cadastro.getTipoAtividadeNaoSujeitaLicenciamento().getIdeTipoAtividadeNaoSujeitaLicenciamento().equals(TipoAtividadeNaoSujeitaLicenciamentoEnum.PERFURACAO_DE_POCOS.getIde())) {
			return "CPP";
		}
		else if (cadastro.getTipoAtividadeNaoSujeitaLicenciamento().getIdeTipoAtividadeNaoSujeitaLicenciamento().equals(TipoAtividadeNaoSujeitaLicenciamentoEnum.PESQUISA_MINERAL.getIde())) {
			return "CPM";
		}
		else if(cadastro.getTipoAtividadeNaoSujeitaLicenciamento().getIdeTipoAtividadeNaoSujeitaLicenciamento().equals(TipoAtividadeNaoSujeitaLicenciamentoEnum.SILOS_E_ARMAZENS.getIde())){
			return "CSA";
		}
		else if (cadastro.getTipoAtividadeNaoSujeitaLicenciamento().getIdeTipoAtividadeNaoSujeitaLicenciamento().equals(TipoAtividadeNaoSujeitaLicenciamentoEnum.INSTALACAO_DE_TORRES.getIde())) {
			return "CTA";
		}
		else {
			return "NF";
		}
	}

	/**
	 * Método para montar a lista de {@link CadastroAtividadeNaoSujeitaLicDocApensado} do {@link CadastroAtividadeNaoSujeitaLic}.
	 * 
	 * @author eduardo.fernandes
	 * @param cadastroAtividade
	 * @since 02/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CadastroAtividadeNaoSujeitaLicDocApensado> listarDocumentosApensados(CadastroAtividadeNaoSujeitaLic cadastroAtividade)  {
		if (!Util.isNullOuVazio(cadastroAtividade)) {
			cadastroAtividade.setCadastroAtividadeNaoSujeitaLicDocApensados(docApensadoService.listar(cadastroAtividade));
		}
		for (AtividadeNaoSujeitaLicenciamentoDocumento docs : listarDocumentos(cadastroAtividade)) {
			CadastroAtividadeNaoSujeitaLicDocApensado docApensado = new CadastroAtividadeNaoSujeitaLicDocApensado(cadastroAtividade, docs.getIdeDocumentoObrigatorio());
			if (!cadastroAtividade.getCadastroAtividadeNaoSujeitaLicDocApensados().contains(docApensado)) {
				cadastroAtividade.addCadastroAtividadeNaoSujeitaLicDocApensado(docApensado);
			}
		}
		return cadastroAtividade.getCadastroAtividadeNaoSujeitaLicDocApensados();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CadastroAtividadeDocumentoIdentificacaoRepresentacao> listarDocumentoIdentificacaoRepresentacao(CadastroAtividadeNaoSujeitaLic cadastroAtividade)  {
		if (!Util.isNullOuVazio(cadastroAtividade)) {
			cadastroAtividade.setCadastroAtividadeDocumentoIdentificacaoRepresentacaos(docRepresentacaoService.listar(cadastroAtividade));
		}
		if(Util.isNullOuVazio(cadastroAtividade.getCadastroAtividadeDocumentoIdentificacaoRepresentacaos())){
			cadastroAtividade.setCadastroAtividadeDocumentoIdentificacaoRepresentacaos(new ArrayList<CadastroAtividadeDocumentoIdentificacaoRepresentacao>());
		}
		return cadastroAtividade.getCadastroAtividadeDocumentoIdentificacaoRepresentacaos();
	}

	/**
	 * Método que ira listar os documentos da {@link PesquisaMineral} de acordo com seu {@link TipoAtividadeNaoSujeitaLicenciamento}..
	 * 
	 * @author eduardo.fernandes
	 * @param cadastroAtividade 
	 * @throws Exception
	 * @since 24/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private List<AtividadeNaoSujeitaLicenciamentoDocumento> listarDocumentos(CadastroAtividadeNaoSujeitaLic cadastroAtividade) {
		if(cadastroAtividade.getTipoAtividadeNaoSujeitaLicenciamento().equals(new TipoAtividadeNaoSujeitaLicenciamento(TipoAtividadeNaoSujeitaLicenciamentoEnum.PERFURACAO_DE_POCOS))){
			return atividadeNaoSujeitaLicenciamentoDocumentoService.listarAtividadesByTipoAtividade(TipoAtividadeNaoSujeitaLicenciamentoEnum.PERFURACAO_DE_POCOS);
		} 
		else if(cadastroAtividade.getTipoAtividadeNaoSujeitaLicenciamento().equals(new TipoAtividadeNaoSujeitaLicenciamento(TipoAtividadeNaoSujeitaLicenciamentoEnum.PESQUISA_MINERAL))){
			return atividadeNaoSujeitaLicenciamentoDocumentoService.listarAtividadesByTipoAtividade(TipoAtividadeNaoSujeitaLicenciamentoEnum.PESQUISA_MINERAL);
		}
		else if(cadastroAtividade.getTipoAtividadeNaoSujeitaLicenciamento().equals(new TipoAtividadeNaoSujeitaLicenciamento(TipoAtividadeNaoSujeitaLicenciamentoEnum.SILOS_E_ARMAZENS))){
			return atividadeNaoSujeitaLicenciamentoDocumentoService.listarAtividadesByTipoAtividade(TipoAtividadeNaoSujeitaLicenciamentoEnum.SILOS_E_ARMAZENS);
		}
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarDocumentoApensado(CadastroAtividadeNaoSujeitaLicDocApensado docApensado){
		docApensadoService.salvar(docApensado, null);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaDocumentoApensado(CadastroAtividadeNaoSujeitaLic cadastroAtividadeNaoSujeitaLic) {
		docApensadoService.salvar(null, cadastroAtividadeNaoSujeitaLic.getCadastroAtividadeNaoSujeitaLicDocApensados());
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public StreamedContent baixarArquivo(Object obj) {
		try {
			if(obj instanceof CadastroAtividadeDocumentoIdentificacaoRepresentacao ){
				CadastroAtividadeDocumentoIdentificacaoRepresentacao doc = ((CadastroAtividadeDocumentoIdentificacaoRepresentacao) obj);
				if(doc.isDocumentoProcuradorPessoaJuridica()){
					return arquivoService.getContentFile(doc.getDscCaminhoProcuracaoPJ());
				}
				else if(doc.isDocumentoRepresentanteLegal()){
					return arquivoService.getContentFile(doc.getDscCaminhoContratoSocial());
				} 
				else {
					return arquivoService.getContentFile(doc.getDscCaminhoDocIdentificacao());
				}
			}
			else if(obj instanceof CadastroAtividadeNaoSujeitaLicDocApensado){
				CadastroAtividadeNaoSujeitaLicDocApensado docApensado = ((CadastroAtividadeNaoSujeitaLicDocApensado) obj);
				return arquivoService.getContentFile(docApensado.getUrlDocumento());
			} 
			else {
				return arquivoService.getContentFile((String) obj);
			}
				
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_arquivo_nao_encontrado"));
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirDocumentosApensados(Object object){
		if (object instanceof CadastroAtividadeNaoSujeitaLic) {
			docApensadoService.excluir(object);
			List<CadastroAtividadeNaoSujeitaLicDocApensado> listaDocs = ((CadastroAtividadeNaoSujeitaLic) object).getCadastroAtividadeNaoSujeitaLicDocApensados();
			if (!Util.isNullOuVazio(listaDocs)) {
				for(CadastroAtividadeNaoSujeitaLicDocApensado doc : listaDocs) {
					arquivoService.deletarArquivo(doc.getUrlDocumento());
				}
			}
		}
		else if (object instanceof CadastroAtividadeNaoSujeitaLicDocApensado) {
			docApensadoService.excluir(object);
			arquivoService.deletarArquivo(((CadastroAtividadeNaoSujeitaLicDocApensado) object).getUrlDocumento());

		} 
		else if (object instanceof String) {
			arquivoService.deletarArquivo(((String) object));
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Endereco carregarEnderecoBy(Empreendimento empreendimento, TipoEnderecoEnum tipoEnderecoEnum) throws Exception {
		return enderecoFacade.filtrarEnderecoByEnderecoEmpreendimento(new EnderecoEmpreendimento(empreendimento, new TipoEndereco(tipoEnderecoEnum.getId())));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Endereco carregarEndereco(Empreendimento empreendimento) throws Exception {
		return enderecoFacade.filtrarEnderecoByEnderecoEmpreendimento(new EnderecoEmpreendimento(empreendimento, new TipoEndereco(TipoEnderecoEnum.LOCALIZACAO.getId())));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CadastroAtividadeNaoSujeitaLic buscar(PesquisaMineral pesquisa) throws Exception {
		CadastroAtividadeNaoSujeitaLic cadastro = cadastroService.buscar(pesquisa);
		cadastro.setCadastroAtividadeNaoSujeitaLicDocApensados(docApensadoService.listar(cadastro));
		cadastro.setCadastroAtividadeDocumentoIdentificacaoRepresentacaos(docRepresentacaoService.listar(cadastro));
		cadastro.setCadastroAtividadeNaoSujeitaLicStatus(listarCadastroStatus(cadastro));
		cadastro.getIdeEmpreendimento().setEndereco(carregarEndereco(cadastro.getIdeEmpreendimento()));
		if (Util.isNullOuVazio(cadastro.getCadastroAtividadeNaoSujeitaLicDocApensados())) {
			cadastro.setCadastroAtividadeNaoSujeitaLicDocApensados(new ArrayList<CadastroAtividadeNaoSujeitaLicDocApensado>());
		}
		if (Util.isNullOuVazio(cadastro.getCadastroAtividadeNaoSujeitaLicStatus())) {
			cadastro.setCadastroAtividadeNaoSujeitaLicStatus(new ArrayList<CadastroAtividadeNaoSujeitaLicStatus>());
		}
		return cadastro;
	}

	public List<CadastroAtividadeNaoSujeitaLicStatus> listarCadastroStatus(CadastroAtividadeNaoSujeitaLic cadastro)  {
		return cadastroStatusService.listar(cadastro);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CadastroAtividadeNaoSujeitaLic buscar(Integer ide) throws Exception {
		CadastroAtividadeNaoSujeitaLic cadastro = cadastroService.buscar(ide);
		cadastro.setCadastroAtividadeNaoSujeitaLicStatus(cadastroStatusService.listar(cadastro));
		cadastro.setCadastroAtividadeNaoSujeitaLicDocApensados(docApensadoService.listar(cadastro));
		cadastro.setCadastroAtividadeDocumentoIdentificacaoRepresentacaos(docRepresentacaoService.listar(cadastro));
		cadastro.setIdePessoaFisicaCadastro(carregarPessoaFisicaByIde(cadastro.getIdePessoaFisicaCadastro().getIdePessoaFisica()));
		PessoaJuridica pessoaJuridica = carregarPessoaJuridicaByIde(cadastro.getIdePessoaRequerente().getIdePessoa());
		if(pessoaJuridica != null){
			cadastro.setIdePessoaRequerente(carregarPessoaJuridicaByIde(cadastro.getIdePessoaRequerente().getIdePessoa()).getPessoa());
		}else{
			cadastro.getIdePessoaRequerente().setPessoaFisica(pessoaFacade.buscarPessoaFisica(cadastro.getIdePessoaRequerente().getIdePessoa()));
		}
		return cadastro;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaFisica carregarPessoaFisicaByIde(Integer idePessoaFisica) throws Exception {
		return pessoaFacade.buscarPessoaFisica(idePessoaFisica);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaJuridica carregarPessoaJuridicaByIde(Integer idePessoaJuridica) throws Exception{
		return pessoaFacade.buscarPessoaJuridica(idePessoaJuridica);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CadastroAtividadeNaoSujeitaLicComunicacao> listarComunicacao(CadastroAtividadeNaoSujeitaLic cadastro)  {
		return comunicacaoService.listar(cadastro);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarComunicacao(CadastroAtividadeNaoSujeitaLicComunicacao comunicacao){		
		comunicacaoService.salvar(comunicacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirDocumentoIdentificacaoRepresentacaoComDocumentoIdentificacaoExcluido(CadastroAtividadeNaoSujeitaLic cadastro){
		docRepresentacaoService.excluirDocumentoIdentificacaoRepresentacaoComDocumentoIdentificacaoExcluido(cadastro);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirDocumentoIdentificacaoRepresentacao(Object obj) {
		docRepresentacaoService.excluir(obj);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarDocumentoIdentificacaoRepresentacao(CadastroAtividadeDocumentoIdentificacaoRepresentacao doc) {
		docRepresentacaoService.salvar(doc, null);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaDocumentoIdentificacaoRepresentacao(CadastroAtividadeNaoSujeitaLic cadastro) {
		docRepresentacaoService.salvar(null, cadastro.getCadastroAtividadeDocumentoIdentificacaoRepresentacaos());
	}
	
	public List<CadastroAtividadeDocumentoIdentificacaoRepresentacao> getListaDocumentoIdentificacoFrom(List<CadastroAtividadeDocumentoIdentificacaoRepresentacao> lista){
		List<CadastroAtividadeDocumentoIdentificacaoRepresentacao> listaTemp = new ArrayList<CadastroAtividadeDocumentoIdentificacaoRepresentacao>();
		for(CadastroAtividadeDocumentoIdentificacaoRepresentacao doc : lista){
			if(doc.isDocumentoIdentificacao()){
				listaTemp.add(doc);
			}
		}
		return listaTemp;
	}

	public List<CadastroAtividadeDocumentoIdentificacaoRepresentacao> getListaDocumentoProcuradorPJFrom(List<CadastroAtividadeDocumentoIdentificacaoRepresentacao> lista){
		List<CadastroAtividadeDocumentoIdentificacaoRepresentacao> listaTemp = new ArrayList<CadastroAtividadeDocumentoIdentificacaoRepresentacao>();
		for(CadastroAtividadeDocumentoIdentificacaoRepresentacao doc : lista){
			if(doc.isDocumentoProcuradorPessoaJuridica()){
				listaTemp.add(doc);
			}
		}
		return listaTemp;
	}
	
	public List<CadastroAtividadeDocumentoIdentificacaoRepresentacao> getListaDocumentoRepresentanteLegalFrom(List<CadastroAtividadeDocumentoIdentificacaoRepresentacao> lista){
		List<CadastroAtividadeDocumentoIdentificacaoRepresentacao> listaTemp = new ArrayList<CadastroAtividadeDocumentoIdentificacaoRepresentacao>();
		for(CadastroAtividadeDocumentoIdentificacaoRepresentacao doc : lista){
			if(doc.isDocumentoRepresentanteLegal()){
				listaTemp.add(doc);
			}
		}
		return listaTemp;
	}

	/**
	 * Método que irá atualizar a URL do documento que foi atualizado de acordo com o tipo de documento.
	 * 
	 * @author eduardo.fernandes 
	 * @since 19/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8191">#8191</a>
	 * @param documento
	 * @throws Exception 
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarDocumento(CadastroAtividadeDocumentoIdentificacaoRepresentacao documento) throws Exception {
		if(documento.isDocumentoIdentificacao()){
			docIdentificacaoService.salvarDocumentoIdentificacao(documento.getIdeDocumentoIdentificacao());
		} 
		else if(documento.isDocumentoProcuradorPessoaJuridica()){
			procuradorRepService.salvarOuAtualizar(documento.getIdeProcuradorRepEmpreendimento());
		} 
		else if(documento.isDocumentoRepresentanteLegal()){
			repLegalService.salvarRepresentanteLegal(documento.getIdeRepresentanteLegal());
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<String> obterEmailsDosResponsaveis(CadastroAtividadeNaoSujeitaLic cadastro) throws Exception{
		List<Pessoa> lista = pessoaFacade.listarPessoasResponsaveisBy(cadastro);
		List<String> emails = new ArrayList<String>();
		for(Pessoa pessoa : lista){
			if(!Util.isNullOuVazio(pessoa.getDesEmail())){
				emails.add(pessoa.getDesEmail());
			}
		}
		return emails;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCadastroAtividadeComStatusIncompleto(CadastroAtividadeNaoSujeitaLic cadastroAtividade, 
			Collection<ResponsavelEmpreendimento> listaResponsaveisSelecionadosTela,
			Collection<Imovel> imoveis) {
		
		//salvar ou atualizar registro em cadastro_atividade_nao_sujeita_lic
		cadastroService.salvar(cadastroAtividade);
		
		//inserir status de cadastro incompleto
		if(Util.isNullOuVazio(cadastroStatusService.listar(cadastroAtividade))){
			salvarStatusImcompleto(cadastroAtividade);
		}
		
		//busca responsáveis do empreendimento associados anteriormente ao cadastro de atividade, caso exista
		List<CadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento> listaResponsaveisSelecionadosBanco = 
				cadastroAtividadeNaoSujeitaLicResponsavelEmpreendimentoService.listarPorCadastroAtividade(cadastroAtividade.getIdeCadastroAtividadeNaoSujeitaLic());
		
		//inserir responsáveis selecionados na tela
		for(ResponsavelEmpreendimento responsavelSelecionadoTela : listaResponsaveisSelecionadosTela){
			if(verificarSePrecisaInserir(listaResponsaveisSelecionadosBanco,	responsavelSelecionadoTela)){
				salvarCadastroAtividadeNaoSujeitaLicResponsavel(cadastroAtividade, responsavelSelecionadoTela);
			}
		}
		
		//excluir responsáveis que foram desmarcados na tela e existiam anteriormente no banco
		for(CadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento responsavelSelecionadoBanco : listaResponsaveisSelecionadosBanco){
			if(verificarSePrecisaExcluir(listaResponsaveisSelecionadosTela,	responsavelSelecionadoBanco)){
				cadastroAtividadeNaoSujeitaLicResponsavelEmpreendimentoService.remover(responsavelSelecionadoBanco);
			}
		}
		
		//salvar imóveis associados
		for(Imovel imovel : imoveis){
			
			if(Util.isNull(cadastroAtividadeNaoSujeitaLicImovelService.carregarPorCadastroEImovel(cadastroAtividade, imovel.getImovelRural()))){
				CadastroAtividadeNaoSujeitaLicImovel cadastroImovel = new CadastroAtividadeNaoSujeitaLicImovel(cadastroAtividade, imovel.getImovelRural());
				cadastroAtividadeNaoSujeitaLicImovelService.salvar(cadastroImovel);
			}
			
		}
	}

	private boolean verificarSePrecisaExcluir(Collection<ResponsavelEmpreendimento> listaResponsaveisSelecionadosTela,
			CadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento responsavelSelecionadoBanco) {
		return !listaResponsaveisSelecionadosTela.contains(responsavelSelecionadoBanco.getIdeResponsavelEmpreendimento());
	}

	private boolean verificarSePrecisaInserir(List<CadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento> listaResponsaveisSelecionadosBanco,
			ResponsavelEmpreendimento responsavelSelecionadoTela) {
		
		boolean contains = false;
		for(CadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento responsavelSelecionadoBanco : listaResponsaveisSelecionadosBanco){
			if(responsavelSelecionadoTela.equals(responsavelSelecionadoBanco.getIdeResponsavelEmpreendimento())){
				contains = true;
			}
		}
		return !contains;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarCadastroAtividadeNaoSujeitaLicResponsavel(CadastroAtividadeNaoSujeitaLic cadastroAtividade,
			ResponsavelEmpreendimento responsavelSelecionado){
		
		CadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento cadastroResponsavelEmpreendimento = 
				new CadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento(cadastroAtividade, responsavelSelecionado);
		
		cadastroAtividadeNaoSujeitaLicResponsavelEmpreendimentoService.salvar(cadastroResponsavelEmpreendimento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarStatusImcompleto(CadastroAtividadeNaoSujeitaLic cadastroAtividade)  {
		CadastroAtividadeNaoSujeitaLicStatus cadastroStatus = new CadastroAtividadeNaoSujeitaLicStatus(cadastroAtividade, 
				CadastroAtividadeNaoSujeitaLicTipoStatusEnum.CADASTRO_INCOMPLETO, cadastroAtividade.getIdePessoaFisicaCadastro());
		
		cadastroStatusService.salvar(cadastroStatus);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void tramitarCadastroTorreParaConcluido(CadastroAtividadeNaoSujeitaLic cadastroAtividade) {
		CadastroAtividadeNaoSujeitaLicStatus cadastroStatus =  new CadastroAtividadeNaoSujeitaLicStatus(cadastroAtividade, CadastroAtividadeNaoSujeitaLicTipoStatusEnum.CONCLUIDO, 
				cadastroAtividade.getIdePessoaFisicaCadastro());
		cadastroStatusService.salvar(cadastroStatus);
		if(Util.isNullOuVazio(cadastroAtividade.getNumCadastro())){
			cadastroAtividade.setNumCadastro(gerarNumeroCadastro(getSiglaCadastro(cadastroAtividade)));
			cadastroService.salvar(cadastroAtividade);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CadastroAtividadeNaoSujeitaLic buscarCadastroIncompletoPorPessoa(Pessoa pessoaRequerente) throws Exception{
		CadastroAtividadeNaoSujeitaLic cadastro = cadastroService.buscarCadastroIncompletoPorPessoa(pessoaRequerente);
		if(cadastro != null){
			cadastro.setIdeEmpreendimento(empreendimentoService.carregarPorIdComMunicipio(cadastro.getIdeEmpreendimento().getIdeEmpreendimento()));
			Collection<ResponsavelEmpreendimento> responsavelEmpreendimentoCollection = pesquisaMineralFacade.listarResponsaveisTecnicos(cadastro.getIdeEmpreendimento());
			cadastro.getIdeEmpreendimento().setResponsavelEmpreendimentoCollection(responsavelEmpreendimentoCollection);
		}
		return cadastro;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CadastroAtividadeNaoSujeitaLic buscarCadastroIncompletoPorPessoaEmpreendimento(Pessoa pessoaRequerente, Empreendimento empreendimento, TipoAtividadeNaoSujeitaLicenciamentoEnum tipoAtividadeNaoSujeitaLicenciamentoEnum) throws Exception{
		CadastroAtividadeNaoSujeitaLic cadastro = cadastroService.buscarCadastroIncompletoPorPessoaEmpreendimento(pessoaRequerente, empreendimento, tipoAtividadeNaoSujeitaLicenciamentoEnum);
		if(cadastro != null){
			cadastro.setIdeEmpreendimento(empreendimentoService.carregarPorIdComMunicipio(cadastro.getIdeEmpreendimento().getIdeEmpreendimento()));
			Collection<ResponsavelEmpreendimento> responsavelEmpreendimentoCollection = pesquisaMineralFacade.listarResponsaveisTecnicos(cadastro.getIdeEmpreendimento());
			cadastro.getIdeEmpreendimento().setResponsavelEmpreendimentoCollection(responsavelEmpreendimentoCollection);
		}
		return cadastro;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CadastroAtividadeNaoSujeitaLic buscarCadastroPorIdeCadastro(Integer ideCadastroAtividadeNaoSujeitaLic) throws Exception{
		CadastroAtividadeNaoSujeitaLic cadastro = cadastroService.buscarCadastroPorId(ideCadastroAtividadeNaoSujeitaLic);
		if(cadastro != null){
			cadastro.setIdeEmpreendimento(empreendimentoService.carregarPorIdComMunicipio(cadastro.getIdeEmpreendimento().getIdeEmpreendimento()));
			Collection<ResponsavelEmpreendimento> responsavelEmpreendimentoCollection = pesquisaMineralFacade.listarResponsaveisTecnicos(cadastro.getIdeEmpreendimento());
			cadastro.getIdeEmpreendimento().setResponsavelEmpreendimentoCollection(responsavelEmpreendimentoCollection);
		}
		return cadastro;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerDadosEmpreendimentoCadastro(Integer ideCadastroAtividadeNaoSujeitaLic){
		if(ideCadastroAtividadeNaoSujeitaLic != null){
			//Responsáveis
			List<CadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento> listaResponsaveis = 
					cadastroAtividadeNaoSujeitaLicResponsavelEmpreendimentoService.listarPorCadastroAtividade(ideCadastroAtividadeNaoSujeitaLic);
			
			for (CadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento responsavelEmpreendimentoCadastro : listaResponsaveis) {
				cadastroAtividadeNaoSujeitaLicResponsavelEmpreendimentoService.remover(responsavelEmpreendimentoCadastro);
			}	
			
			//Imóveis
			List<CadastroAtividadeNaoSujeitaLicImovel> imoveisCadastro = cadastroAtividadeNaoSujeitaLicImovelService.listarPorCadastro(ideCadastroAtividadeNaoSujeitaLic);
			
			for(CadastroAtividadeNaoSujeitaLicImovel imovelCadastro : imoveisCadastro){
				cadastroAtividadeNaoSujeitaLicImovelService.remover(imovelCadastro);
			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirImovelCadastro(ImovelRural imovelRural, CadastroAtividadeNaoSujeitaLic ideCadastro){
		if(ideCadastro.getIdeCadastroAtividadeNaoSujeitaLic() != null){
			CadastroAtividadeNaoSujeitaLicImovel imovelCadastro = 
					cadastroAtividadeNaoSujeitaLicImovelService.carregarPorCadastroEImovel(ideCadastro, imovelRural);
			
			if(imovelCadastro != null){
				cadastroAtividadeNaoSujeitaLicImovelService.remover(imovelCadastro);
			}
		}
	}
	
}
