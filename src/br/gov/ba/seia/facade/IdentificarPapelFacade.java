package br.gov.ba.seia.facade;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.ContratoConvenioDAOImpl;
import br.gov.ba.seia.dao.ProcuradorPessoaFisicaDAOImpl;
import br.gov.ba.seia.dao.ProcuradorRepresentanteDAOImpl;
import br.gov.ba.seia.entity.ContratoConvenio;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.ProcuradorPessoaFisica;
import br.gov.ba.seia.entity.ProcuradorRepresentante;
import br.gov.ba.seia.entity.TipoVinculoPCT;
import br.gov.ba.seia.service.ProcuradorPessoaFisicaService;
import br.gov.ba.seia.service.ProcuradorRepresentanteService;
import br.gov.ba.seia.service.RepresentanteLegalService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class IdentificarPapelFacade {


	@Inject
	private ProcuradorPessoaFisicaDAOImpl procuradorPessoaFisicaDAOImpl;

	@Inject
	private ProcuradorRepresentanteDAOImpl procuradorRepresentanteDAOImpl;


	@Inject
	private ContratoConvenioDAOImpl contratoConvenioDAOImpl;



	@EJB
	private PessoaFacade pessoaFacade;

	@EJB
	private RepresentanteLegalService representanteLegalService;

	@EJB
	private ProcuradorPessoaFisicaService procuradorPessoaFisicaService;

	@EJB
	private ProcuradorRepresentanteService procuradorRepresentanteService;

	@EJB
	private CerhDadosGeraisServiceFacade cerhDadosGeraisFacade;

	@EJB
	private PctImovelRuralFacade pctImovelRuralFacade;
	
	/**
	 * <li> <b>return</b> 1 ::: caso a {@link PessoaFisica} seja um dos {@link ProcuradorRepresentante}</li>
	 * <li> <b>return</b> 0 ::: caso a {@link PessoaJuridica} ainda não tenha procuradores cadastrados</li>
	 * <li> <b>return</b> -1 :: caso a {@link PessoaFisica} não seja o {@link ProcuradorRepresentante}</li>
	 **/

	@EJB
	private ContratoConvenioDAOImpl contratoConvenioDao;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer retornarLigacaoProcuradorPJ(PessoaFisica pessoaValidar, ProcuradorRepresentante procuradorRepresentante){
		return procuradorRepresentanteService.verificaProcuradorRepresentante(pessoaValidar, procuradorRepresentante);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer retornarLigacaoRepresentanteLegal(PessoaFisica pessoaValidar, PessoaJuridica pessoaJuridica){
		return representanteLegalService.verificaRepresentanteLegal(pessoaValidar, pessoaJuridica);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isProcurarPessoaFisica(PessoaFisica pessoa, PessoaFisica procurador) {
		return procuradorPessoaFisicaService.isProcurarPessoaFisica(pessoa,procurador);
	}


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer retornarLigacaoProcuradorPF(PessoaFisica pessoaValidar, ProcuradorPessoaFisica procuradorPessoaFisica){
		List<ProcuradorPessoaFisica> listaProcuradoresPessoaFisica = (List<ProcuradorPessoaFisica>) procuradorPessoaFisicaService.listarProcuradorPessoaFisica(procuradorPessoaFisica);

		if(Util.isNull(listaProcuradoresPessoaFisica)){
			return null;
		}

		else if(!Util.isNullOuVazio(listaProcuradoresPessoaFisica)){
			for(ProcuradorPessoaFisica ppf : listaProcuradoresPessoaFisica){
				if(pessoaValidar.equals(ppf.getIdeProcurador())){
					return 1;
				}
			}
			return -1;
		}
		return 0;
	}



	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaJuridica retornarPessoaJuridica(PessoaJuridica pessoaJuridica) throws Exception{
		if(!Util.isNull(pessoaJuridica)
				&& Util.isNull(pessoaJuridica.getIdePessoaJuridica())
				&& !Util.isNull(pessoaJuridica.getNumCnpj())) {
			return pessoaFacade.buscarPessoaJuridicaByCNPJ(pessoaJuridica);
		}
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isCadastroPJvalido(PessoaJuridica pessoaJuridica, boolean isCadastroCompleto) throws Exception{
		if(isCadastroCompleto){
			return pessoaFacade.verificarCadastroCompletoPessoaJuridica(pessoaJuridica.getPessoa());
		}
		else {
			return pessoaFacade.verificarCadastroParcialPessoa(pessoaJuridica.getPessoa());
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isCadastroPJvalidoSemCnae(PessoaJuridica pessoaJuridica) throws Exception{
		return pessoaFacade.verificarCadastroCompletoPessoaJuridicaSemCnae(pessoaJuridica.getPessoa());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isCadastroPFvalido(PessoaFisica pessoaFisica, boolean isCadastroCompleto) throws Exception{
		if(isCadastroCompleto){
			pessoaFisica.getPessoa().setPessoaFisica(pessoaFisica);
			return pessoaFacade.verificarCadastroCompletoPessoaFisica(pessoaFisica.getPessoa());
		}
		else {
			return pessoaFacade.verificarCadastroParcialPessoa(pessoaFisica.getPessoa());
		}
	}



	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isUsuarioConvenio() {
		return contratoConvenioDao.isPessoaComContratoConvenio(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica());
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isUsuarioConsorcioCerh() {
		return cerhDadosGeraisFacade.isProcuradorConveniado();
	}

	public boolean isCadastroPFvalidoRepresentanteComunidade(PessoaFisica pessoaFisica, boolean isCadastroCompleto) throws Exception{
		pessoaFisica.getPessoa().setPessoaFisica(pessoaFisica);
		if(isCadastroCompleto){
			return pessoaFacade.verificarCadastroCompletoPessoaFisicaRepresentanteComunidade(pessoaFisica.getPessoa());
		} 
		else {
			return pessoaFacade.verificarCadastroParcialPessoa(pessoaFisica.getPessoa());
		}
	}
	
	public List<TipoVinculoPCT> listarTipoVinculoPCT() throws Exception{
		return pctImovelRuralFacade.listarTipoVinculoPCT();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isCompartilhaContratoConvenio(PessoaFisica pessoaValidar,PessoaFisica pessoaFisica) {
		return contratoConvenioDao.isCompartilhaContratoConvenio(pessoaValidar, pessoaFisica);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isPessoaComContratoConvenioPCT(Integer idePessoaFisica) throws Exception {
		return contratoConvenioDao.isPessoaComContratoConvenioPCT(idePessoaFisica);
	}
	
	public List<ContratoConvenio> listarContratoConvencioPctByPessoaFisica(Integer idePessoaFisica) throws Exception{
		return contratoConvenioDao.listarContratoConvencioPctByPessoaFisica(idePessoaFisica);
	}
}