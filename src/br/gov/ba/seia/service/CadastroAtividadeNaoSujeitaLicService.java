package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.CadastroAtividadeNaoSujeitaLicDAOImpl;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLic;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLicStatus;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.PesquisaMineral;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.ProcuradorRepresentante;
import br.gov.ba.seia.enumerator.CadastroAtividadeNaoSujeitaLicTipoStatusEnum;
import br.gov.ba.seia.enumerator.TipoAtividadeNaoSujeitaLicenciamentoEnum;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Util;

/**
 * Serviço da classe {@link CadastroAtividadeNaoSujeitaLic}
 * 
 * @author eduardo.fernandes
 * @since 02/12/2016
 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CadastroAtividadeNaoSujeitaLicService {

	@Inject
	private CadastroAtividadeNaoSujeitaLicDAOImpl dao;
	@EJB
	private ProcuradorRepresentanteService procurador;
	@EJB
	private RepresentanteLegalService representante;
	@EJB
	private CadastroAtividadeNaoSujeitaLicStatusService statusService;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(CadastroAtividadeNaoSujeitaLic cadastro)  {
		dao.salvar(cadastro);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(CadastroAtividadeNaoSujeitaLic cadastro)  {
		cadastro.setIndExcluido(true);
		dao.salvar(cadastro);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String getUltimoNumeroCadastro()  {
		return dao.getUltimoNumeroCadastro();
	}

	/**
	 * @author eduardo.fernandes
	 * @since 07/12/2016
	 * @param map
	 * @return
	 * @throws Exception 
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CadastroAtividadeNaoSujeitaLic> consultarCadastro(Map<String, Object> map)   {
		
		List<CadastroAtividadeNaoSujeitaLic> atividadeNaoSujeitaLics = dao.consultar(map);
		return filtrarPorProcuradoresERepresentates(atividadeNaoSujeitaLics);
	}

	private List<CadastroAtividadeNaoSujeitaLic> filtrarPorProcuradoresERepresentates(List<CadastroAtividadeNaoSujeitaLic> atividadeNaoSujeitaLics) {
		List<CadastroAtividadeNaoSujeitaLic> novaAtividadeNaoSujeitaLics = new ArrayList<CadastroAtividadeNaoSujeitaLic>();
		PessoaFisica pessoaFisica = ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica();
		
		for (CadastroAtividadeNaoSujeitaLic cadAtivNaoSujLic : atividadeNaoSujeitaLics) {
			PessoaJuridica pessoaJuridica = new PessoaJuridica(cadAtivNaoSujLic.getIdePessoaRequerente().getIdePessoa());

			if(pessoaFisica.getUsuario().isUsuarioExterno()){
				ProcuradorRepresentante procRep = procurador.buscarProcuradorRepresentante(pessoaFisica,  pessoaJuridica);
				Integer representanteLegal = representante.verificaRepresentanteLegal(pessoaFisica, pessoaJuridica);
				
				if (!Util.isNullOuVazio(procRep) || (!Util.isNullOuVazio(representanteLegal) && representanteLegal.equals(1))
						|| cadAtivNaoSujLic.getIdePessoaFisicaCadastro().equals(pessoaFisica)){
					novaAtividadeNaoSujeitaLics.add(cadAtivNaoSujLic);
				}
			} else {

				novaAtividadeNaoSujeitaLics.add(cadAtivNaoSujLic);
			}
		}
		return novaAtividadeNaoSujeitaLics;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer consultarCount(Map<String, Object> map)   {
		List<CadastroAtividadeNaoSujeitaLic> atividadeNaoSujeitaLics = dao.consultarCount(map);
		return filtrarPorProcuradoresERepresentates(atividadeNaoSujeitaLics).size();
	}
	
	/**
	 * @author eduardo.fernandes
	 * @since 07/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param map
	 * @return
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer countConsultarCadastro(Map<String, Object> map)  {
		return dao.count(map);
	}

	/**
	 * @author eduardo.fernandes
	 * @since 07/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param pesquisa
	 * @return
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CadastroAtividadeNaoSujeitaLic buscar(PesquisaMineral pesquisa)  {
		return dao.buscar(pesquisa);
	}

	/**
	 * @author eduardo.fernandes 
	 * @since 15/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param ide
	 * @return
	 * @ 
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CadastroAtividadeNaoSujeitaLic buscar(Integer ide)  {
		return dao.buscar(ide);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CadastroAtividadeNaoSujeitaLic buscarCadastroIncompletoPorPessoa(Pessoa pessoaRequerente) {
		CadastroAtividadeNaoSujeitaLic cadastroIncompleto = null;
		CadastroAtividadeNaoSujeitaLicStatus status = statusService.buscarUltimoStatusPorRequerente(pessoaRequerente);
		if(status != null && 
		   status.getCadastroAtividadeNaoSujeitaLicTipoStatus().getId().equals(Long.valueOf(CadastroAtividadeNaoSujeitaLicTipoStatusEnum.CADASTRO_INCOMPLETO.getIde()))){
			cadastroIncompleto = status.getCadastroAtividadeNaoSujeitaLic();
		}
		return cadastroIncompleto;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CadastroAtividadeNaoSujeitaLic buscarCadastroPorId(Integer ideCadastroAtividadeNaoSujeitaLic)  {
		return dao.buscarCadastroPorId(ideCadastroAtividadeNaoSujeitaLic);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CadastroAtividadeNaoSujeitaLic buscarCadastroIncompletoPorPessoaEmpreendimento(Pessoa pessoaRequerente, Empreendimento empreendimento, TipoAtividadeNaoSujeitaLicenciamentoEnum tipoAtividadeNaoSujeitaLicenciamentoEnum) {
		CadastroAtividadeNaoSujeitaLic cadastroIncompleto = null;
		CadastroAtividadeNaoSujeitaLicStatus status = statusService.buscarUltimoStatusPorRequerenteEmpreendimento(pessoaRequerente, empreendimento, tipoAtividadeNaoSujeitaLicenciamentoEnum);
		if(status != null && 
		   status.getCadastroAtividadeNaoSujeitaLicTipoStatus().getId().equals(Long.valueOf(CadastroAtividadeNaoSujeitaLicTipoStatusEnum.CADASTRO_INCOMPLETO.getIde()))){
			cadastroIncompleto = status.getCadastroAtividadeNaoSujeitaLic();
		}
		return cadastroIncompleto;
	}
}
