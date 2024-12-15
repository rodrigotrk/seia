package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.PessoaJuridicaDAOImpl;
import br.gov.ba.seia.entity.Cnae;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.EnderecoPessoa;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.PessoaJuridicaCnae;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.RepresentanteLegal;
import br.gov.ba.seia.entity.Telefone;
import br.gov.ba.seia.exception.AppExceptionError;
import br.gov.ba.seia.facade.CadastroAtividadeFacade;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PessoaJuridicaService  extends PessoaService{	
	
	@Inject
	private PessoaJuridicaDAOImpl pessoaJuridicaDAOImpl;
	
	@EJB
	private PermissaoPerfilService permissaoPerfilService;
	
	@EJB
	private RequerimentoService requerimentoService;
	
	@Inject
	private IDAO<PessoaJuridica> pessoaJuridicalDAO;
	
	@EJB
	private EmpreendimentoService empreendimentoService;
	
	@EJB
	private ImovelRuralService imovelRuralService;
	
	@EJB
	private CnaeService cnaeService;
	
	@EJB
	private PessoaJuridicaCnaeService pessoaJuridicaCnaeService;
	
	@EJB
	private RepresentanteLegalService representanteLegalService;
	
	@EJB
	private CadastroAtividadeFacade cadastroFacade;
	
	@EJB
	private TelefoneService telefoneService;
	
	@EJB
	private EnderecoPessoaService enderecoPessoaService;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirPessoaJuridica(PessoaJuridica pessoaJuridica) {
		imovelRuralService.validarVinculoComImovel(pessoaJuridica.getPessoa());
		if (!requerimentoService.verificarRequerimentoPessoa(pessoaJuridica.getPessoa())) {
		
			if((empreendimentoService.listarEmpreendimento(pessoaJuridica.getPessoa()).isEmpty())) {
				
				pessoaJuridicaDAOImpl.excluirPessoaJuridica(pessoaJuridica);
				
			} else {
				throw new AppExceptionError("Não é permitida a exclusão, pois a pessoa possui um empreendimento em seu nome.");
			}
		} else {
			throw new AppExceptionError("Não é permitida a exclusão, pois a pessoa possui uma solicitação aberta em seu nome ou representa alguém em uma solicitação.");
		}
	}	
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Collection<PessoaJuridica> listarPessoaJuridica() {		
		return this.pessoaJuridicaDAOImpl.listarPessoaJuridica();	
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public PessoaJuridica buscarPessoaJuridicaByIde(Integer idePessoaJuridica) {		
		DetachedCriteria criteria = DetachedCriteria.forClass(PessoaJuridica.class);
		criteria.createAlias("ideNaturezaJuridica", "naturezaJuridica", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("idePessoaJuridica", idePessoaJuridica));
		return pessoaJuridicalDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<PessoaJuridica> filtrarPessoaJuridica(Integer firstPage,Integer pageSize, PessoaJuridica pessoaJuridica)  {
		return this.pessoaJuridicaDAOImpl.filtrarPessoaJuridica(firstPage, pageSize, pessoaJuridica);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaJuridica filtrarPessoaJuridicaByCnpj(PessoaJuridica pessoaJuridica)  {
		return this.pessoaJuridicaDAOImpl.filtrarPessoaJuridicaByCnpj(pessoaJuridica);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<PessoaJuridica> filtrarPessoaJuridicaSemAcento(PessoaJuridica pessoaJuridica)  {
		return this.pessoaJuridicaDAOImpl.filtrarPessoaJuridicaSemAcento(pessoaJuridica);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<PessoaJuridica> filtrarPJRequerenteExterno(PessoaJuridica pessoaJuridica)  {
		return this.pessoaJuridicaDAOImpl.filtrarPessoaJuridicaRequerenteExterno(pessoaJuridica);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<PessoaJuridica> listarPessoaJuridicaRepresentada(PessoaFisica pessoaFisica) {
		return this.pessoaJuridicaDAOImpl.listarPessoaJuridicaRepresentada(pessoaFisica);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<PessoaJuridica> filtrarPJRequerenteExternoSemAcento(PessoaJuridica pessoaJuridica)  {
		return this.pessoaJuridicaDAOImpl.filtrarPessoaJuridicaRequerenteExternoSemAcento(pessoaJuridica);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<PessoaJuridica> filtrarListaPessoaJuridica(PessoaJuridica pessoaJuridica) {
		return  this.pessoaJuridicaDAOImpl.filtrarListaPessoaJuridica(pessoaJuridica);	
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaJuridica filtrarPessoaFisicaByCnpj(PessoaJuridica pessoaJuridica)  {		
		return this.pessoaJuridicaDAOImpl.filtrarPessoaJuridicaByCnpj(pessoaJuridica);	
	}	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaJuridica filtrarPJbyCNPJlacTransporte(PessoaJuridica pessoaJuridica)  {		
		return this.pessoaJuridicaDAOImpl.filtrarPJbyCNPJlacTransporte(pessoaJuridica);	
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public PessoaJuridica salvarPessoaJuridica(PessoaJuridica pessoaJuridica)  {
		return this.pessoaJuridicaDAOImpl.salvarPessoaJuridica(pessoaJuridica);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public PessoaJuridica salvarOuAtualizarPessoaJuridica(PessoaJuridica pessoaJuridica)  {
		return this.pessoaJuridicaDAOImpl.salvarOuAtualizarPessoaJuridica(pessoaJuridica);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<PessoaJuridica> listarPorCriteriaDemanda(PessoaJuridica pessoaJuridica, Integer startPage, Integer maxPage) {	
		return pessoaJuridicaDAOImpl.listarPorCriteriaDemanda(pessoaJuridica, startPage, maxPage, permissaoPerfilService.listarIdesPessoaJuridicaAptos());
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Integer count(PessoaJuridica pessoaJuridica) {		
		return pessoaJuridicaDAOImpl.count(pessoaJuridica, permissaoPerfilService.listarIdesPessoaJuridicaAptos());
	}	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<PessoaJuridica> listarPorCriteriaDemandaSemRestricao(PessoaJuridica pessoaJuridica, Integer startPage, Integer maxPage) {	
		return pessoaJuridicaDAOImpl.listarPorCriteriaDemanda(pessoaJuridica, startPage, maxPage, null);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Integer countSemRestricao(PessoaJuridica pessoaJuridica) {		
		return pessoaJuridicaDAOImpl.count(pessoaJuridica, null);
	}	
	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private PessoaJuridica carregarPessoaJuridicaByIde(Integer idePessoaJuridica) throws Exception {
		return cadastroFacade.carregarPessoaJuridicaByIde(idePessoaJuridica);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private Endereco carregarEnderecoBy(Pessoa pessoa) {
		EnderecoPessoa ep = enderecoPessoaService.buscarEnderecoPorPessoa(pessoa);
		return ep.getIdeEndereco();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private List<Telefone> listarTelefoneBy(Pessoa pessoa) throws Exception  {
		return telefoneService.buscarTelefonesPorPessoa(pessoa);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private List<RepresentanteLegal> listarRepresentanteLegalBy(PessoaJuridica pessoaJuridica) throws Exception {
		List<RepresentanteLegal> repLegais = representanteLegalService.listarRepresentanteLegalPorPessoaJuridica(pessoaJuridica);
		for(RepresentanteLegal representanteLegal : repLegais){
			representanteLegal.getIdePessoaFisica().getPessoa().setTelefoneCollection(listarTelefoneBy(representanteLegal.getIdePessoaFisica().getPessoa()));
		}
		return repLegais;
	}

	
	/**
	 *
	 * @param idePessoaJuridica
	 * @return
	 * @author diegoraian
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
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private Collection<PessoaJuridicaCnae> listarPessoaJuridicaCNAE(PessoaJuridica pessoaJuridica)  {
		return pessoaJuridicaCnaeService.buscaPessoaJuridicaCnaePorPessoaJuridica(pessoaJuridica);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private Cnae obterSecaoCnae(Cnae cnae)  {
		while (!Util.isNull(cnae.getIdeCnaePai())) {
			cnae = cnaeService.obterCnaePaiPorCnae(cnae);
		}
		return cnae;
	}

	public PessoaJuridica obterPessoaJuridicaId(PessoaJuridica pessoaJuridica) {
		return pessoaJuridicaDAOImpl.obterPessoaJuridicaId(pessoaJuridica);
	}

	public int countFiltroPessoaJuridicaSolicitante(PessoaJuridica pessoaJuridica)  {
		return this.pessoaJuridicaDAOImpl.countFiltroPessoaJuridicaSolicitante(pessoaJuridica);	
	}
	
	public int countFiltroPessoaJuridicaSolicitanteExterno(PessoaJuridica pessoaJuridica)  {
		return this.pessoaJuridicaDAOImpl.countFiltroPessoaJuridicaSolicitanteExterno(pessoaJuridica);	
	}
	
	
	public Boolean existeRequerimentoPorPJ(PessoaJuridica pPessoaJuridica)  {		
		return this.pessoaJuridicaDAOImpl.existeRequerimentoPorPJ(pPessoaJuridica);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean validarIdentificacaoPessoaJuridica(PessoaJuridica pessoaJuridicaSelecionada){
		boolean valido = true;
		if(Util.isNullOuVazio(pessoaJuridicaSelecionada.getNomRazaoSocial())){
			JsfUtil.addErrorMessage("Preencha o campo Razão Social.");
			valido = false;
		}
		if(Util.isNullOuVazio(pessoaJuridicaSelecionada.getNomeFantasia())){
			JsfUtil.addErrorMessage("Preencha o campo Nome Fantasia.");
			valido = false;
		}
		if(Util.isNullOuVazio(pessoaJuridicaSelecionada.getNumInscricaoMunicipal())){
			JsfUtil.addErrorMessage("Preencha o campo Inscrição Municipal");
			valido = false;
		}
		if(Util.isNullOuVazio(pessoaJuridicaSelecionada.getNumInscricaoEstadual())){
			JsfUtil.addErrorMessage("Preencha o campo Inscrição Estadual.");
			valido = false;
		}
		if (Util.isNullOuVazio(pessoaJuridicaSelecionada.getDscCaminhoArquivo())) {
			JsfUtil.addErrorMessage("O campo Upload de Documento é de preenchimento obrigatório.");
			valido = false;
		}
		if (Util.isNull(pessoaJuridicaSelecionada.getIdeNaturezaJuridica()) || Util.isNull(pessoaJuridicaSelecionada.getIdeNaturezaJuridica().getIdeNaturezaJuridica())) {
			JsfUtil.addErrorMessage("O campo Natureza Jurídica é de preenchimento obrigatório.");
			valido = false;
		}
		return valido;
	}
}