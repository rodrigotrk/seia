package br.gov.ba.seia.facade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.EspecieFlorestalDAOImpl;
import br.gov.ba.seia.entity.EspecieFlorestal;
import br.gov.ba.seia.entity.Florestal;
import br.gov.ba.seia.entity.Produto;
import br.gov.ba.seia.service.FlorestalService;
import br.gov.ba.seia.service.ProdutoService;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ExecucaoPlanoManejoFlorestalService extends FceFlorestalAbstractService {
	
	@EJB
	private ProdutoService produtoService;
	
	@EJB
	private FlorestalService florestalService;
	
	@Inject
	private EspecieFlorestalDAOImpl florestalDAO;
	
	public BigDecimal obterValorTotalArea(Integer ideRequerimento) throws Exception {
		Florestal florestal = florestalService.obterFlorestalByRequerimento(ideRequerimento);
		return florestal.getNumAreaCorteUnidadeProducao();
	}
	
	public List<Produto> carregarListaProdutosByTipoProduto(int ideTipoProduto) throws Exception{
		return produtoService.carregarListaProdutosByTipoProduto(ideTipoProduto);
	}
	
	public List<EspecieFlorestal> listarEspecieFlorestal(Integer ideEspecieFlorestal) throws Exception{
		return florestalDAO.listarEspecieFlorestal(ideEspecieFlorestal);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EspecieFlorestal> pesquisarEspecieFlorestal(List<EspecieFlorestal> listaEspecieSupressao, List<EspecieFlorestal> listaEspecieSupressaoAll, String nomEspecieSupressao){
		listaEspecieSupressao = new ArrayList<EspecieFlorestal>();
		for (EspecieFlorestal tipoClassificacao : listaEspecieSupressaoAll) {
			if(tipoClassificacao.getNomEspecieFlorestal().toLowerCase().indexOf(nomEspecieSupressao.toLowerCase())!= -1){
				listaEspecieSupressao.add(tipoClassificacao);
			}
		}
		
		return listaEspecieSupressao;
	}
	
	
}
