package br.gov.ba.seia.facade;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;

import br.gov.ba.seia.controller.PesquisaMineralController;
import br.gov.ba.seia.entity.CadastroAtividadeDocumentoIdentificacaoRepresentacao;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLic;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLicDocApensado;
import br.gov.ba.seia.entity.Cnae;
import br.gov.ba.seia.entity.DocumentoIdentificacao;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.EnderecoPessoa;
import br.gov.ba.seia.entity.FormacaoProfissional;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.MetodoProspeccao;
import br.gov.ba.seia.entity.PesquisaMineral;
import br.gov.ba.seia.entity.PesquisaMineralDocumentoCaptacao;
import br.gov.ba.seia.entity.PesquisaMineralResponsavelTecnico;
import br.gov.ba.seia.entity.PesquisaMineralSubstanciaMineral;
import br.gov.ba.seia.entity.PesquisaMineralUsoDaAgua;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.PessoaJuridicaCnae;
import br.gov.ba.seia.entity.ProcessoDnpm;
import br.gov.ba.seia.entity.ProcessoDnpmProspecao;
import br.gov.ba.seia.entity.ProcuradorRepEmpreendimento;
import br.gov.ba.seia.entity.ProcuradorRepresentante;
import br.gov.ba.seia.entity.ProspecaoDetalhamento;
import br.gov.ba.seia.entity.RepresentanteLegal;
import br.gov.ba.seia.entity.ResponsavelEmpreendimento;
import br.gov.ba.seia.entity.SubstanciaMineralTipologia;
import br.gov.ba.seia.entity.Telefone;
import br.gov.ba.seia.entity.TipoCaptacao;
import br.gov.ba.seia.entity.TipoIdentificacao;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.enumerator.MetodoProspeccaoEnum;
import br.gov.ba.seia.enumerator.TipoCaptacaoEnum;
import br.gov.ba.seia.enumerator.TipoEnderecoEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.service.CnaeService;
import br.gov.ba.seia.service.DocumentoIdentificacaoService;
import br.gov.ba.seia.service.EnderecoPessoaService;
import br.gov.ba.seia.service.FormacaoProfissionalService;
import br.gov.ba.seia.service.MetodoProspeccaoService;
import br.gov.ba.seia.service.PesquisaMineralDocumentoCaptacaoService;
import br.gov.ba.seia.service.PesquisaMineralResponsavelTecnicoService;
import br.gov.ba.seia.service.PesquisaMineralService;
import br.gov.ba.seia.service.PesquisaMineralSubstanciaMineralService;
import br.gov.ba.seia.service.PesquisaMineralUsoDaAguaService;
import br.gov.ba.seia.service.PessoaJuridicaCnaeService;
import br.gov.ba.seia.service.ProcessoDnpmProspeccaoService;
import br.gov.ba.seia.service.ProcessoDnpmService;
import br.gov.ba.seia.service.ProcuradorRepEmpreendimentoService;
import br.gov.ba.seia.service.ProcuradorRepresentanteService;
import br.gov.ba.seia.service.ProspeccaoDetalhamentoService;
import br.gov.ba.seia.service.RepresentanteLegalService;
import br.gov.ba.seia.service.ResponsavelEmpreendimentoService;
import br.gov.ba.seia.service.SubstanciaMineralTipologiaService;
import br.gov.ba.seia.service.TelefoneService;
import br.gov.ba.seia.service.TipoCaptacaoService;
import br.gov.ba.seia.service.TipologiaService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * Facade responsável pelo Cadastramento da Pesquisa Mineral sem Guia de Utilização.
 * 
 * @author eduardo.fernandes 
 * @since 09/11/2016
 * @see <a href="http://10.105.17.77/redmine/issues/8187">#8187</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PesquisaMineralFacade {
	
	@EJB
	private ResponsavelEmpreendimentoService responsavelEmpreendimentoService;
	
	@EJB
	private PessoaJuridicaCnaeService pessoaJuridicaCnaeService;
	
	@EJB
	private CnaeService cnaeService;
	
	@EJB
	private EnderecoPessoaService enderecoPessoaService;
	
	@EJB
	private TelefoneService telefoneService;
	
	@EJB
	private RepresentanteLegalService representanteLegalService;
	
	@EJB
	private FormacaoProfissionalService formacaoProfissionalService;
	
	@EJB
	private DocumentoIdentificacaoService documentoIdentificacaoService;
	
	@EJB
	private TipologiaService tipologiaService;
	
	@EJB
	private SubstanciaMineralTipologiaService substanciaMineralTipologiaService;
	
	@EJB
	private LocalizacaoGeograficaServiceFacade localizacaoGeograficaFacade;
	
	@EJB
	private TipoCaptacaoService tipoCaptacaoService;
	
	@EJB
	private PesquisaMineralDocumentoCaptacaoService pesquisaMineralDocumentoCaptacaoService;

	@EJB
	private MetodoProspeccaoService metodoProspeccaoService;

	@EJB
	private PesquisaMineralService pesquisaMineralService;

	@EJB
	private PesquisaMineralSubstanciaMineralService pesquisaMineralSubstanciaMineralService;

	@EJB
	private PesquisaMineralResponsavelTecnicoService pesquisaMineralResponsavelTecnicoService;

	@EJB
	private PesquisaMineralUsoDaAguaService pesquisaMineralUsoDaAguaService;

	@EJB
	private ProcessoDnpmService processoDnpmService;

	@EJB
	private ProspeccaoDetalhamentoService prospeccaoDetalhamentoService;

	@EJB
	private ProcessoDnpmProspeccaoService processoDnpmProspeccaoService;

	@EJB
	private CadastroAtividadeFacade cadastroFacade;
	
	@EJB
	private ProcuradorRepresentanteService procuradorService;
	
	@EJB
	private ProcuradorRepEmpreendimentoService procuradorRepEmpreendimentoService;
	
	/**
	 * 
	 * Método para listar os {@link ResponsavelEmpreendimento} do {@link Empreendimento}
	 * 
	 * @author eduardo.fernandes 
	 * @since 09/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8187">#8187</a> 
	 * @param empreendimento
	 * @return lista de Responsável Técnico do Empreendimento
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ResponsavelEmpreendimento> listarResponsaveisTecnicos(Empreendimento empreendimento) throws Exception{
		Collection<ResponsavelEmpreendimento> lista = responsavelEmpreendimentoService.listarResponsaveisPorEmpreendimento(empreendimento);
		for(ResponsavelEmpreendimento responsavelEmpreendimento : lista){
			responsavelEmpreendimento.getIdePessoaFisica().getPessoa().setTelefoneCollection(listarTelefoneBy(responsavelEmpreendimento.getIdePessoaFisica().getPessoa()));
			carregarEndereco(responsavelEmpreendimento);
		}
		return lista;	
	}

	private void carregarEndereco(ResponsavelEmpreendimento responsavelEmpreendimento)  {
		EnderecoPessoa enderecoPessoa = enderecoPessoaService.buscarEnderecoPorPessoa(responsavelEmpreendimento.getIdePessoaFisica().getPessoa());
		if(enderecoPessoa != null){
			responsavelEmpreendimento.getIdePessoaFisica().getPessoa().setEndereco(enderecoPessoa.getIdeEndereco());
		}
	}

	/**
	 * Método que irá retornar uma {@link PessoaJuridica} "montada", já com {@link Endereco} da {@link Pessoa} e {@link PessoaJuridicaCnae} carregados.
	 * 
	 * @author eduardo.fernandes
	 * @since 10/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param idePessoaJuridica
	 * @return
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaJuridica obterPessoaJuridicaMontada(Integer idePessoaJuridica){
		try {
			PessoaJuridica pessoaJuridica = carregarPessoaJuridicaByIde(idePessoaJuridica);
			pessoaJuridica.getPessoa().setEndereco(carregarEnderecoBy(pessoaJuridica.getPessoa()));
			pessoaJuridica.getPessoa().setTelefoneCollection(listarTelefoneBy(pessoaJuridica.getPessoa()));
			pessoaJuridica.setPessoaJuridicaCnaeCollection(listarCnaeSecaoBy(pessoaJuridica));
			pessoaJuridica.setRepresentanteLegalCollection(listarRepresentanteLegalBy(pessoaJuridica));
			return pessoaJuridica;
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações do Requerente.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private PessoaJuridica carregarPessoaJuridicaByIde(Integer idePessoaJuridica) throws Exception{
		return cadastroFacade.carregarPessoaJuridicaByIde(idePessoaJuridica);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private Endereco carregarEnderecoBy(Pessoa pessoa){
		EnderecoPessoa ep = enderecoPessoaService.buscarEnderecoPorPessoa(pessoa);
		return ep.getIdeEndereco();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Endereco carregarEndereco(Empreendimento empreendimento) {
		try {
			return cadastroFacade.carregarEndereco(empreendimento);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " o endereço do empreendimento.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Endereco carregarEnderecoCorrespondencia(Empreendimento empreendimento) throws Exception {
		return cadastroFacade.carregarEnderecoBy(empreendimento, TipoEnderecoEnum.CORRESPONDENCIA);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<PessoaJuridicaCnae> listarCnaeSecaoBy(PessoaJuridica pessoaJuridica) {
		Collection<PessoaJuridicaCnae> pjCnaeCollectionTemp = listarPessoaJuridicaCNAE(pessoaJuridica);
		Collection<PessoaJuridicaCnae> pjCnaeCollectionFinal = new ArrayList<PessoaJuridicaCnae>();
		Collection<Cnae> collCnae = new ArrayList<Cnae>();
		if(!Util.isNullOuVazio(pjCnaeCollectionTemp)){
			for(PessoaJuridicaCnae pjCnae : pjCnaeCollectionTemp){
				Cnae cnae = obterSecaoCnae(pjCnae.getIdeCnae());
				if(!collCnae.contains(cnae)){
					collCnae.add(cnae);
					pjCnae.setIdeCnae(cnae);
					pjCnaeCollectionFinal.add(pjCnae);
				}
			}
		}
		
		return pjCnaeCollectionFinal;
	}

	/**
	 * Método que que retorna uma lista de {@link PessoaJuridicaCnae} associados a {@link PessoaJuridica}.
	 * 
	 * @author eduardo.fernandes 
	 * @since 10/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param pessoaJuridica
	 * @return
	 * @throws Exception 
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private Collection<PessoaJuridicaCnae> listarPessoaJuridicaCNAE(PessoaJuridica pessoaJuridica) {
		return pessoaJuridicaCnaeService.buscaPessoaJuridicaCnaePorPessoaJuridica(pessoaJuridica);
	}

	/**
	 * 
	 * Método recursivo para obter o {@link Cnae} do tipo <b>Seção</b>, sem <i>ide_cnae_pai</i>, a partir de qualquer {@link Cnae}.
	 * 
	 * @author eduardo.fernandes 
	 * @since 10/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param cnae
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private Cnae obterSecaoCnae(Cnae cnae) {
		while (!Util.isNull(cnae.getIdeCnaePai())) {
			cnae = cnaeService.obterCnaePaiPorCnae(cnae);
		}
		return cnae;
	}
	
	/**
	 * 
	 * Método para listar os {@link Telefone} de uma {@link Pessoa}.
	 * 
	 * @author eduardo.fernandes 
	 * @since 11/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param pessoa
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private List<Telefone> listarTelefoneBy(Pessoa pessoa) throws Exception {
		return telefoneService.buscarTelefonesPorPessoa(pessoa);
	}
	
	
	/**
	 * 
	 * Método para listar os {@link RepresentanteLegal} da {@link PessoaJuridica}.
	 * 
	 * @author eduardo.fernandes 
	 * @since 11/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param pessoaJuridica
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private List<RepresentanteLegal> listarRepresentanteLegalBy(PessoaJuridica pessoaJuridica) throws Exception{
		List<RepresentanteLegal> repLegais = representanteLegalService.listarRepresentanteLegalPorPessoaJuridica(pessoaJuridica);
		for(RepresentanteLegal representanteLegal : repLegais){
			representanteLegal.getIdePessoaFisica().getPessoa().setTelefoneCollection(listarTelefoneBy(representanteLegal.getIdePessoaFisica().getPessoa()));
		}
		return repLegais;
	}

	/**
	 * Método para listar as {@link FormacaoProfissional}.
	 * 
	 * @author eduardo.fernandes 
	 * @since 11/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @return
	 * @throws Exception 
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FormacaoProfissional> listarFormacaoProfissional()  {
		return formacaoProfissionalService.listarFormacaoProfissional();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String obterNumeroCarteiraConselhoClasse(Pessoa pessoa) throws Exception{
		for(DocumentoIdentificacao doc : documentoIdentificacaoService.listarDocumentosIdentificacaoPorPessoa(pessoa)){
			if(doc.getIdeTipoIdentificacao().equals(new TipoIdentificacao(5))){
				return doc.getNumDocumento();
			}
		}
		return null; 
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TreeNode montarArvoreTipologiasMineracao(Tipologia divisao, Collection<Tipologia> listaTipologia) throws Exception {
		Collection<Tipologia> tipologiasMineracao = new ArrayList<Tipologia>();
		if(!Util.isNull(listaTipologia)){
			List<Tipologia> tipologiasNo = new ArrayList<Tipologia>();
			for(Tipologia tipologia : listaTipologia){
				if(tipologia.getIdeNivelTipologia().getIdeNivelTipologia().equals(3)){
					Tipologia tipologiaPai = tipologiaService.buscarTipologiaPaiByTipologiaFilho(tipologia);
					if(!tipologiasNo.contains(tipologia)){
						tipologiasNo.add(tipologiaPai);
					}
				}
				else if(tipologia.getIdeNivelTipologia().getIdeNivelTipologia().equals(4)){
					Tipologia tipoAvo = tipologiaService.buscarTipologiaPaiByTipologiaFilho(tipologia.getIdeTipologiaPai());
					if(!tipologiasNo.contains(tipoAvo)){
						tipologiasNo.add(tipoAvo);
					}
				}
			}
			tipologiasMineracao.addAll(tipologiasNo);
		} 
		else {
			tipologiasMineracao = tipologiaService.listarTipologias(divisao, false);
			tipologiasMineracao.remove(new Tipologia(TipologiaEnum.EXTRACAO_PETROLEO_GAS));
		}

		this.carregarTipologiaFilha(tipologiasMineracao);
		
		if(!Util.isNullOuVazio(listaTipologia)){
			List<Tipologia> tipologiasDescartaveis = new ArrayList<Tipologia>();
			for(Tipologia t : tipologiasMineracao){
				preencherTipologiasDescartaveis(t, listaTipologia, tipologiasDescartaveis);
			}
			removerTipologiasDescartaveis(tipologiasMineracao, tipologiasDescartaveis);
		} 

		TreeNode root = new DefaultTreeNode("RAIZ", null);

		this.gerarArvoreTipologia(tipologiasMineracao, root);
		
		return root;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private void carregarTipologiaFilha(Collection<Tipologia> tipologias) throws Exception {
		for (Tipologia tipologia : tipologias) {
			if(tipologia.getIndPossuiFilhos()){
				Collection<Tipologia> filhas = tipologiaService.listarTipologias(tipologia,false);
				tipologia.setTipologiaCollection(filhas);
				this.carregarTipologiaFilha(filhas);
			}
			else {
				tipologia.setSubstanciaMineralTipologiaCollection(listarSubstanciaMineralTipologiaBy(tipologia));
			}
		}
	}
	
	private void preencherTipologiasDescartaveis(Tipologia tipologia, Collection<Tipologia> tipologiasRequerimento, Collection<Tipologia> tipologiasDescartaveis){
		if(!Util.isNullOuVazio(tipologia.getTipologiaCollection())){
			for(Tipologia t : tipologia.getTipologiaCollection()) {
				preencherTipologiasDescartaveis(t, tipologiasRequerimento, tipologiasDescartaveis);
			}
		}
		else {
			if(!tipologiasRequerimento.contains(tipologia)){
				tipologiasDescartaveis.add(tipologia);
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<SubstanciaMineralTipologia> listarSubstanciaMineralTipologiaBy(Tipologia tipologia) throws Exception {
		List<SubstanciaMineralTipologia> substanciasMineraisTipologias = substanciaMineralTipologiaService.listarSubstanciaMineralTipologiaBy(tipologia);
		SubstanciaMineralTipologia outros = substanciaMineralTipologiaService.buscarOutrosBy(tipologia);
		substanciasMineraisTipologias.remove(outros);
		substanciasMineraisTipologias.add(substanciasMineraisTipologias.size(), outros);
		return substanciasMineraisTipologias;
	}

	private void removerTipologiasDescartaveis(Collection<Tipologia> tipologiasNo, Collection<Tipologia> tipologiasDescartaveis){
		Collection<Tipologia> temp = new ArrayList<Tipologia>();
		temp.addAll(tipologiasNo);
		for(Tipologia no : temp){
			if(!Util.isNullOuVazio(no.getTipologiaCollection())){
				removerTipologiasDescartaveis(no.getTipologiaCollection(), tipologiasDescartaveis);
			} 
			else {
				if(tipologiasDescartaveis.contains(no)){
					tipologiasNo.remove(no);
				} 
			}
			if(Util.isNullOuVazio(no.getTipologiaCollection()) && no.getIndPossuiFilhos()){
				tipologiasNo.remove(no);
			}
		}
	}

	private void gerarArvoreTipologia(Collection<Tipologia> collection, TreeNode root) {
		for (Tipologia tipologia : collection) {
			TreeNode node = new DefaultTreeNode("tipologia", tipologia, root);
			if(tipologia.getIndPossuiFilhos()){
				this.gerarArvoreTipologia(tipologia.getTipologiaCollection(), node);
			} 
			else {
				for(SubstanciaMineralTipologia substanciaMineral : tipologia.getSubstanciaMineralTipologiaCollection()){
					new DefaultTreeNode("substancia", substanciaMineral, node);
				}
			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BigDecimal retornarAreaShape(LocalizacaoGeografica localizacaoGeografica) throws Exception {
		return new BigDecimal(localizacaoGeograficaFacade.retornarAreaShape(localizacaoGeografica)).setScale(4, RoundingMode.CEILING);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String retornarGeometriaShapeByLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica) throws Exception {
		return localizacaoGeograficaFacade.retornarGeometriaShapeByLocalizacaoGeografica(localizacaoGeografica);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean naoExisteIntersecao(LocalizacaoGeografica localizacaoGeograficaA, LocalizacaoGeografica localizacaoGeograficaB) throws Exception {
		return localizacaoGeograficaFacade.naoExisteSobreposicao(localizacaoGeograficaA, localizacaoGeograficaB);
	}

	/**
	 * Método que lista os {@link TipoCaptacao} que devem ser exibidos na tela de cadastro da {@link PesquisaMineral}.
	 * 
	 * @author eduardo.fernandes 
	 * @since 16/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @return
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoCaptacao> listarTipoCaptacoes() throws Exception{
		List<TipoCaptacao> lista = tipoCaptacaoService.listarTipoCaptacaoCompleto();
		lista.remove(new TipoCaptacao(TipoCaptacaoEnum.CAPTACAO_PRECIPITACAO_METEOROLOGICA_PLUVIAL.getId()));
		return lista;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<PesquisaMineralDocumentoCaptacao> listarPesquisaMineralDocumentoCaptacao() {
		return pesquisaMineralDocumentoCaptacaoService.listarPesquisaMineralDocumentoCaptacao();
	}

	/**
	 * Método que lista os {@link MetodoProspeccao} que devem ser exibidos na tela de cadastro da {@link PesquisaMineral}.
	 * 
	 * @author eduardo.fernandes 
	 * @since 18/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @return
	 * @throws Exception 
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<MetodoProspeccao> listarMetodoProspeccao() {
		List<MetodoProspeccao> lista = metodoProspeccaoService.listarMetodoProspeccao();
		lista.remove(new MetodoProspeccao(MetodoProspeccaoEnum.GALERIAS));
		lista.remove(new MetodoProspeccao(MetodoProspeccaoEnum.GEOFISICA));
		lista.remove(new MetodoProspeccao(MetodoProspeccaoEnum.AMOSTRAGEM));
		return lista;
	}

	/**
	 * Método para listar os {@link CadastroAtividadeNaoSujeitaLicDocApensado}.
	 * 
	 * @author eduardo.fernandes
	 * @param cadastro
	 * @since 02/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CadastroAtividadeNaoSujeitaLicDocApensado> listarCadastroAtividadeNaoSujeitaLicDocApensados(CadastroAtividadeNaoSujeitaLic cadastro)  {
		return cadastroFacade.listarDocumentosApensados(cadastro);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CadastroAtividadeDocumentoIdentificacaoRepresentacao> listarDocumentoIdentificacaoRepresentacao(CadastroAtividadeNaoSujeitaLic cadastro){
		return cadastroFacade.listarDocumentoIdentificacaoRepresentacao(cadastro);
	}

	/*
	 * 
	 * PERSISTÊNCIA
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAccordionProcessoDnpm(ProcessoDnpm processoDnpm) {
		try {
			excluirProspeccaoDetalhamento(processoDnpm);
			excluirProcessoDnpmProspeccao(processoDnpm);

			limparProcessoDnpmProspeccao(processoDnpm);

			salvarProcessoDnpmProspeccao(processoDnpm);
			salvarProspeccaoDetalhamento(processoDnpm);
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " o Processo DNPM." );
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);

		}
	}

	/**
	 * Método que limpar o {@link ProcessoDnpm} para ser salvo.
	 * 
	 * @author eduardo.fernandes
	 * @since 12/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param processoDnpm
	 */
	private void limparProcessoDnpmProspeccao(ProcessoDnpm processoDnpm) {
		for (ProcessoDnpmProspecao prospeccao : processoDnpm.getListaProcessoDnpmProspecao()) {
			prospeccao.setIdeProcessoDnpmProspecao(null);
			if (!processoDnpm.getListaProcessoDnpmProspecao().contains(prospeccao)) {
				prospeccao.setProspecaoDetalhamentos(new ArrayList<ProspecaoDetalhamento>());
			}
			for (ProspecaoDetalhamento detalhamento : prospeccao.getProspecaoDetalhamentos()) {
				detalhamento.setIdeProspeccaoDetalhamento(null);
				detalhamento.setProcessoDnpmProspecao(prospeccao);
			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(PesquisaMineralController controller) {
		salvarParcialmente(controller);
		if(Util.isNullOuVazio(controller.getPesquisaMineral().getCadastroAtividadeNaoSujeitaLic().getCadastroAtividadeNaoSujeitaLicStatus())){
			tramitarCadastroIncompleto(controller.getPesquisaMineral().getCadastroAtividadeNaoSujeitaLic());
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarParcialmente(PesquisaMineralController controller) {
		try {
			salvarCadastro(controller.getPesquisaMineral().getCadastroAtividadeNaoSujeitaLic());
			salvarCadastroDocumentosApensados(controller);
			salvarCadastroDocumentoIdentificacaoRepresentacao(controller);

			salvarPesquisa(controller.getPesquisaMineral());
			salvarListaProcessoDnpm(controller);
			salvarPesquisaMineralResponsavel(controller.getPesquisaMineral());
			salvarPesquisaMineralUsoDaAgua(controller.getPesquisaMineral());
			salvarPesquisaMineralSubstanciaMineral(controller.getPesquisaMineral());

		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " a Pesquisa Mineral." );
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);

		}
	}

	/**
	 * Método que salva a lista de {@link CadastroAtividadeDocumentoIdentificacaoRepresentacao}.
	 * 
	 * @author eduardo.fernandes 
	 * @since 19/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/">#</a>
	 * @param controller
	 * @throws Exception 
	 */
	private void salvarCadastroDocumentoIdentificacaoRepresentacao(PesquisaMineralController controller) {
		for (CadastroAtividadeDocumentoIdentificacaoRepresentacao doc : controller.getPesquisaMineral().getCadastroAtividadeNaoSujeitaLic().getCadastroAtividadeDocumentoIdentificacaoRepresentacaos()) {
			if (controller.isDocumentoEnviado(doc)){
				cadastroFacade.salvarDocumentoIdentificacaoRepresentacao(doc);
			}
		}
		cadastroFacade.excluirDocumentoIdentificacaoRepresentacaoComDocumentoIdentificacaoExcluido(controller.getCadastro());
	}

	/**
	 * Método que salva {@link ProcessoDnpm}.
	 * 
	 * @author eduardo.fernandes
	 * @since 05/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param controller
	 * @throws Exception
	 */
	public void salvarProcessoDnpm(ProcessoDnpm processoDnpm)  {
		processoDnpmService.salvar(processoDnpm);
	}

	/**
	 * Método que salva {@link ProcessoDnpmProspecao}.
	 * 
	 * @author eduardo.fernandes
	 * @since 05/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param controller
	 * @throws Exception
	 */
	private void salvarProcessoDnpmProspeccao(ProcessoDnpm processoDnpm)  {
		processoDnpmProspeccaoService.salvar(processoDnpm);
	}

	/**
	 * Método que salva {@link ProspecaoDetalhamento}.
	 * 
	 * @author eduardo.fernandes
	 * @since 05/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param controller
	 * @throws Exception
	 */
	public void salvarProspeccaoDetalhamento(Object obj)  {
		prospeccaoDetalhamentoService.salvar(obj);
	}

	/**
	 * Método que salva a lista de {@link ProcessoDnpm}.
	 * 
	 * @author eduardo.fernandes
	 * @since 05/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param controller
	 * @throws Exception
	 */
	private void salvarListaProcessoDnpm(PesquisaMineralController controller)  {
		if (!Util.isNullOuVazio(controller.getPesquisaMineral().getProcessoDnpms())) {
			processoDnpmService.salvarListaProcessoDnpm(controller.getPesquisaMineral().getProcessoDnpms());
		}
	}

	/**
	 * Método que salva {@link PesquisaMineralSubstanciaMineral}.
	 * 
	 * @author eduardo.fernandes
	 * @since 05/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param pesquisaMineral
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarPesquisaMineralSubstanciaMineral(PesquisaMineral pesquisaMineral)  {
		if (!Util.isNullOuVazio(pesquisaMineral.getPesquisaMineralSubstanciaMinerals())) {
			pesquisaMineralSubstanciaMineralService.salvar(pesquisaMineral.getPesquisaMineralSubstanciaMinerals());
		}
	}

	/**
	 * Método que salva {@link PesquisaMineralUsoDaAgua}.
	 * 
	 * @author eduardo.fernandes
	 * @since 05/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param pesquisaMineral
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarPesquisaMineralUsoDaAgua(PesquisaMineral pesquisaMineral) {
		excluirPesquisaMineralUsoDaAgua(pesquisaMineral);

		if (!Util.isNullOuVazio(pesquisaMineral.getPesquisaMineralUsoDaAguas())) {
			
			for(PesquisaMineralUsoDaAgua usoAgua : pesquisaMineral.getPesquisaMineralUsoDaAguas()){
				usoAgua.setIdePesquisaMineralUsoDaAgua(null);
			}
			
			pesquisaMineralUsoDaAguaService.salvar(pesquisaMineral.getPesquisaMineralUsoDaAguas());
		}
		
	}

	/**
	 * Método que salva {@link PesquisaMineralResponsavelTecnico}.
	 * 
	 * @author eduardo.fernandes
	 * @since 05/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param pesquisaMineral
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarPesquisaMineralResponsavel(PesquisaMineral pesquisaMineral)  {
		pesquisaMineralResponsavelTecnicoService.salvar(pesquisaMineral.getPesquisaMineralResponsavelTecnicos());
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizar(PesquisaMineralController controller) {
		salvarParcialmente(controller);
		finalizarTramitacao(controller.getPesquisaMineral().getCadastroAtividadeNaoSujeitaLic());
	}

	/**
	 * Método para modificar o último status da tramitação do {@link CadastroAtividadeNaoSujeitaLic}.
	 * 
	 * @author eduardo.fernandes
	 * @since 05/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param controller
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void finalizarTramitacao(CadastroAtividadeNaoSujeitaLic cadastro) {
		cadastroFacade.finalizarCadastro(cadastro);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void tramitarCadastroIncompleto(CadastroAtividadeNaoSujeitaLic cadastro) {
		cadastroFacade.tramitarCadastroIncompleto(cadastro);
	}

	/**
	 * Método que salva os {@link CadastroAtividadeNaoSujeitaLicDocApensado} que foram enviados pelo usuário.
	 * 
	 * @author eduardo.fernandes 
	 * @since 05/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param controller
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarCadastroDocumentosApensados(PesquisaMineralController controller)  {
		for (CadastroAtividadeNaoSujeitaLicDocApensado doc : controller.getPesquisaMineral().getCadastroAtividadeNaoSujeitaLic().getCadastroAtividadeNaoSujeitaLicDocApensados()) {
			if (controller.isDocumentoEnviado(doc)) {
				cadastroFacade.salvarDocumentoApensado(doc);
			}
		}
	}

	/**
	 * Método que salva {@link PesquisaMineral}
	 * 
	 * @author eduardo.fernandes
	 * @since 05/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param pesquisaMineral
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarPesquisa(PesquisaMineral pesquisaMineral) {
		pesquisaMineralService.salvar(pesquisaMineral);
	}

	/**
	 * Método que salva {@link CadastroAtividadeNaoSujeitaLic}.
	 * 
	 * @author eduardo.fernandes
	 * @since 05/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param controller
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarCadastro(CadastroAtividadeNaoSujeitaLic cadastro)  {
		cadastroFacade.salvarCadastroAtividadeNaoSujeitaLicenciamento(cadastro);
	}
	/*
	 * 
	 * FIM PERSISTÊNCIA
	 */

	/*
	 * 
	 * EXCLUSÃO
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirPesquisaMineralResponsavelTecnico(PesquisaMineralResponsavelTecnico respTecnico){
		pesquisaMineralResponsavelTecnicoService.excluir(respTecnico);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirPesquisaMineralSubstanciaMineral(PesquisaMineralSubstanciaMineral substancia) {
		pesquisaMineralSubstanciaMineralService.excluir(substancia);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirPesquisaMineralUsoDaAgua(PesquisaMineral pesquisa)  {
		pesquisaMineralUsoDaAguaService.excluir(pesquisa);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirPesquisaMineralUsoDaAgua(PesquisaMineralUsoDaAgua usoAgua) {
		pesquisaMineralUsoDaAguaService.excluir(usoAgua);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirProcessoDnpm(ProcessoDnpm processoDnpm) {
		processoDnpmService.excluirProcessoDnpmByIdeProcessoDnpm(processoDnpm);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirProcessoDnpmProspeccao(ProcessoDnpmProspecao prospeccao) {
		processoDnpmProspeccaoService.excluir(prospeccao);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirProcessoDnpmProspeccao(ProcessoDnpm processo)  {
		processoDnpmProspeccaoService.excluir(processo);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirProspeccaoDetalhamento(ProspecaoDetalhamento detalhamento)  {
		prospeccaoDetalhamentoService.excluir(detalhamento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirProspeccaoDetalhamento(ProcessoDnpm processo)  {
		prospeccaoDetalhamentoService.excluir(processo);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirLocalizacaoGeografica(List<LocalizacaoGeografica> listaLocalizacaoGeografica) {
		if(!Util.isNullOuVazio(listaLocalizacaoGeografica)){
			for(LocalizacaoGeografica lg : listaLocalizacaoGeografica){
				excluirLocalizacaoGeografica(lg);
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica)  {
		localizacaoGeograficaFacade.excluirDadoGeografico(localizacaoGeografica);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirAccordionProcessoDnpm(ProcessoDnpm processoDnpm) {
		excluirProspeccaoDetalhamento(processoDnpm);
		excluirProcessoDnpmProspeccao(processoDnpm);
		excluirLocalizacaoGeografica(processoDnpm.getIdeLocalizacaoGeografica());
		excluirProcessoDnpm(processoDnpm);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirDocumentoApensadoAnteriormente(String caminhoArquivoAntigo) {
		if (!Util.isNullOuVazio(caminhoArquivoAntigo)) {
			cadastroFacade.excluirDocumentosApensados(caminhoArquivoAntigo);
		}
	}
	/*
	 * 
	 * FIM EXCLUSÃO
	 */

	/**
	 * Método que carrega a {@link PesquisaMineral} e monta sua listas.
	 * 
	 * @author eduardo.fernandes
	 * @since 07/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param cadastro
	 * @throws Exception
	 */
	public PesquisaMineral carregarPesquisaMineral(CadastroAtividadeNaoSujeitaLic cadastro) throws Exception {
		PesquisaMineral pesquisa = pesquisaMineralService.buscar(cadastro);
		pesquisa.setCadastroAtividadeNaoSujeitaLic(cadastroFacade.buscar(pesquisa));
		prepararPesquisaMineralResponsavelTecnico(pesquisa);
		pesquisa.setPesquisaMineralSubstanciaMinerals(pesquisaMineralSubstanciaMineralService.listar(pesquisa));
		prepararProcessoDnpm(pesquisa);
		prepararUsoAgua(pesquisa);
		return pesquisa;
	}

	/**
	 * @author eduardo.fernandes 
	 * @since 16/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/">#</a>
	 * @param pesquisa
	 * @throws Exception 
	 */
	private void prepararUsoAgua(PesquisaMineral pesquisa)  {
		pesquisa.setPesquisaMineralUsoDaAguas(pesquisaMineralUsoDaAguaService.listar(pesquisa));
		
		if(!Util.isNullOuVazio(pesquisa.getPesquisaMineralUsoDaAguas())){
			for(PesquisaMineralUsoDaAgua usoAgua : pesquisa.getPesquisaMineralUsoDaAguas()){
				usoAgua.setSelecionado(true);
				usoAgua.setListaPesquisaMineralDocumentoCaptacao(pesquisaMineralDocumentoCaptacaoService.listarPesquisaMineralDocumentoCaptacao());
				usoAgua.setPesquisaMineralDocumentoCaptacao(pesquisaMineralDocumentoCaptacaoService.buscar(usoAgua));
			}
		}
	}

	/**
	 * @author eduardo.fernandes 
	 * @since 12/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param pesquisa
	 * @throws Exception
	 */
	private void prepararProcessoDnpm(PesquisaMineral pesquisa)  {
		pesquisa.setProcessoDnpms(processoDnpmService.listarProcessoDnpmBy(pesquisa));
		if (!Util.isNullOuVazio(pesquisa.getProcessoDnpms())) {
			for (ProcessoDnpm processoDnpm : pesquisa.getProcessoDnpms()) {
				processoDnpm.setListaProcessoDnpmProspecao(processoDnpmProspeccaoService.listar(processoDnpm));
				if (!Util.isNullOuVazio(processoDnpm.getListaProcessoDnpmProspecao())) {
					for (ProcessoDnpmProspecao prospeccao : processoDnpm.getListaProcessoDnpmProspecao()) {
						prospeccao.setProspecaoDetalhamentos(prospeccaoDetalhamentoService.listar(prospeccao));
					}
				}
				prepararListaProcessoDnpmProspeccaoToSelect(processoDnpm);
			}
		}
	}

	/**
	 * @author eduardo.fernandes 
	 * @since 12/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param pesquisa
	 * @throws Exception
	 */
	private void prepararPesquisaMineralResponsavelTecnico(PesquisaMineral pesquisa) throws Exception {
		pesquisa.setPesquisaMineralResponsavelTecnicos(pesquisaMineralResponsavelTecnicoService.listar(pesquisa));
		for (PesquisaMineralResponsavelTecnico tecnico : pesquisa.getPesquisaMineralResponsavelTecnicos()) {
			tecnico.setListaFormacaoProfissional(listarFormacaoProfissional());
			tecnico.getIdePessoaFisicaResponsavelTecnico().getPessoa().setTelefoneCollection(listarTelefoneBy(tecnico.getIdePessoaFisicaResponsavelTecnico().getPessoa()));
			tecnico.setNumeroCarteiraConselho(obterNumeroCarteiraConselhoClasse(tecnico.getIdePessoaFisicaResponsavelTecnico().getPessoa()));
			if(Util.isNullOuVazio(tecnico.getNumeroCarteiraConselho())){
				tecnico.setNumeroCarteiraConselho("Não informado");	
			}
		}
	}

	/**
	 * @author eduardo.fernandes
	 * @since 12/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param processoDnpm
	 * @throws Exception
	 */
	public void prepararListaProcessoDnpmProspeccaoToSelect(ProcessoDnpm processoDnpm) {
		processoDnpm.setListaProcessoDnpmProspecaoToSelect(new ArrayList<ProcessoDnpmProspecao>());
		for (MetodoProspeccao metodoProspeccao : listarMetodoProspeccao()) {
			ProcessoDnpmProspecao newProspeccao = new ProcessoDnpmProspecao(processoDnpm, metodoProspeccao);
			if (!Util.isNullOuVazio(processoDnpm.getListaProcessoDnpmProspecao()) && processoDnpm.getListaProcessoDnpmProspecao().contains(newProspeccao)) {
				newProspeccao.getIdeMetodoProspeccao().setChecked(true);
			}
			processoDnpm.getListaProcessoDnpmProspecaoToSelect().add(newProspeccao);
		}
		
	}

	/**
	 * Método para fazer o <i>download</i> de um arquivo.
	 * 
	 * @author eduardo.fernandes
	 * @since 13/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8191">#8191</a>
	 * @param caminho
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public StreamedContent baixarArquivo(Object obj) {
		return cadastroFacade.baixarArquivo(obj);
	}

	/**
	 * Método para listar os {@link DocumentoIdentificacao} do usuário que está abrindo o {@link CadastroAtividadeNaoSujeitaLic}.
	 * 
	 * @author eduardo.fernandes 
	 * @since 16/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8191">#8191</a>
	 * @param usuarioRealizandoAcao
	 * @return
	 * @throws Exception 
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DocumentoIdentificacao> listarDocumentoIdentificacao(PessoaFisica usuarioRealizandoAcao) throws Exception {
		Pessoa pessoa = usuarioRealizandoAcao.getPessoa();
		if(Util.isNullOuVazio(pessoa)){
			pessoa = new Pessoa(usuarioRealizandoAcao.getIdePessoaFisica());
		}
		return documentoIdentificacaoService.listarDocumentosIdentificacaoPorPessoa(pessoa);
	}
	
	/**
	 * Método que busca as informações do Procurador que está abrindo o {@link CadastroAtividadeNaoSujeitaLic} da {@link PesquisaMineral}.
	 * 
	 * @author eduardo.fernandes 
	 * @since 16/12/2016
	 * @param cadastro
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProcuradorRepEmpreendimento buscarProcuradorRepresentanteEmpreendimento(CadastroAtividadeNaoSujeitaLic cadastro){
		try {
			PessoaFisica pessoaFisicaCadastro = cadastro.getIdePessoaFisicaCadastro();
			PessoaJuridica pessoaJuridica = new PessoaJuridica(cadastro.getIdePessoaRequerente().getIdePessoa());
			ProcuradorRepresentante procurador;
			procurador = procuradorService.buscarProcuradorRepresentante(pessoaFisicaCadastro, pessoaJuridica);
			return procuradorRepEmpreendimentoService.buscarPorProcuradorRepresentanteViaCriteria(procurador, cadastro.getIdeEmpreendimento().getIdeEmpreendimento());
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações do procurador.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public RepresentanteLegal buscarRepresentanteLegal(Integer pessoaFisicaCadastro, Integer pessoaJuridica){
		try {
			List<RepresentanteLegal> lista = representanteLegalService.buscarRepresentanteLegalByPessoaAndRequerente(pessoaFisicaCadastro, pessoaJuridica);
			if(!Util.isNullOuVazio(lista)){
				return lista.get(0);
			} 
			else {
				return null;
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações do representante legal.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		}
	}
	
	public List<CadastroAtividadeDocumentoIdentificacaoRepresentacao> obterListaDocumentoIdentificacaoRepresentacao(List<CadastroAtividadeDocumentoIdentificacaoRepresentacao> lista, Boolean isRepresentanteLegal){
		if(!Util.isNull(isRepresentanteLegal) && isRepresentanteLegal){
			return cadastroFacade.getListaDocumentoRepresentanteLegalFrom(lista);
		}
		else if(!Util.isNull(isRepresentanteLegal) && !isRepresentanteLegal){
			return cadastroFacade.getListaDocumentoProcuradorPJFrom(lista);
		}
		else {
			return cadastroFacade.getListaDocumentoIdentificacoFrom(lista);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isProcurador(PessoaFisica usuario, PessoaJuridica requerente){
		try {
			return procuradorService.verificaProcuradorRepresentante(usuario, new ProcuradorRepresentante(requerente)) == 1;
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações dos Procuradores do Requerente.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isRepresentante(PessoaFisica usuario, PessoaJuridica requerente){
		try {
			return representanteLegalService.verificaRepresentanteLegal(usuario, requerente) == 1;
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações dos Representante Legais do Requerente.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atulizarDocumento(CadastroAtividadeDocumentoIdentificacaoRepresentacao documento) throws Exception{
		cadastroFacade.atualizarDocumento(documento);
	}
	
	public boolean isMesmaCoordenada(ProspecaoDetalhamento detalhamentoA, ProspecaoDetalhamento detalhamentoB){
		try {
			return localizacaoGeograficaFacade.isTheGeomIgual(detalhamentoA.getIdeLocalizacaoGeografica(), detalhamentoB.getIdeLocalizacaoGeografica());
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("")); // ADICIONAR MSG DE ERRO
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		}
	}
	
}
