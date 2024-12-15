package br.gov.ba.seia.service;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.dao.PautaDAOImpl;
import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.Pauta;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.enumerator.TipoPautaEnum;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PautaService {

	@EJB
	private PautaDAOImpl pautaDAOImpl;
	

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Pauta pPauta)  {
		pautaDAOImpl.salvar(pPauta);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pauta retornarPautaDoTecnicoCriadorDaNotificacao(Integer ideProcesso, Integer ideArea)  {
		return pautaDAOImpl.retornarPautaDoTecnicoCriadorDaNotificacao(ideProcesso, ideArea);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Pauta> listarPautasGestorComAcessoPermitido(PessoaFisica pessoaFisica)  {
		return pautaDAOImpl.listarPautasGestorComAcessoPermitido(pessoaFisica);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Pauta> listarPautasAreaComAcessoPermitido(PessoaFisica pessoaFisica)  {
		return pautaDAOImpl.listarPautasAreaComAcessoPermitido(pessoaFisica);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pauta obtemPautaArea(Area pArea)  {
		return pautaDAOImpl.obtemPautaArea(pArea);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pauta obtemPautaAreaCoordenador(Integer pArea)  {
		return pautaDAOImpl.obtemPautaAreaCoordenador(pArea);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pauta obtemPautaArea(Integer ideArea)  {
		return pautaDAOImpl.obtemPautaArea(ideArea);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pauta obtemPautaCoordenadorArea(Integer ideArea, TipoPautaEnum tipoPautaEnum) throws Exception  {
		return pautaDAOImpl.obtemPautaCoordenadorArea(ideArea, tipoPautaEnum);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pauta obtemPautaPorIdeFuncionario(Integer ideFuncionario)  {
		return pautaDAOImpl.obtemPautaPorIdeFuncionario(ideFuncionario);
	} 

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pauta obtemPautaPorIdeFuncionario(Integer ideFuncionario, Integer ideTipoPauta)  {
		return pautaDAOImpl.obtemPautaPorIdeFuncionario(ideFuncionario,ideTipoPauta);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pauta obtemPautaPorIdeFuncionarioCriteria(Integer ideFuncionario, Integer ideTipoPauta)  {
		return pautaDAOImpl.obtemPautaPorIdeFuncionarioCriteria(ideFuncionario, ideTipoPauta);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pauta filtrarPauta(Pauta pPauta) {

		return pautaDAOImpl.getPauta(pPauta);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)	
	public Pauta buscarPorQuery(String sql, Map<String, Object> parametros)  {
		return  pautaDAOImpl.buscarPorQuery(sql, parametros);
		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(Pauta pauta)  {
		pautaDAOImpl.atualizar(pauta);		
	}
}