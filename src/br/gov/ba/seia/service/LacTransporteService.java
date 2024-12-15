package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Lac;
import br.gov.ba.seia.entity.LacTransporte;
import br.gov.ba.seia.entity.LacTransporteProduto;
import br.gov.ba.seia.entity.LacTransporteProdutoPK;
import br.gov.ba.seia.entity.LacTransporteResiduo;
import br.gov.ba.seia.entity.LacTransporteResiduoPK;
import br.gov.ba.seia.entity.Produto;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.Residuo;
import br.gov.ba.seia.enumerator.LacTransporteOutroEnum;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class LacTransporteService {
	
	@Inject
	private IDAO<LacTransporte> lacTransportelDAO;
	@Inject
	private IDAO<Lac> laclDAO;
	@Inject
	private IDAO<LacTransporteProduto> lacTransporteProdutoDAO;
	@Inject
	private IDAO<LacTransporteResiduo> lacTransporteResiduoDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public LacTransporte buscarLacTransporteByIdeRequerimento(Requerimento requerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(LacTransporte.class);
		criteria.createAlias("ideLac","lac");
		criteria.add(Restrictions.eq("lac.ideRequerimento", requerimento));
		criteria.createAlias("idePessoaJuridica","pj",JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("pj.pessoa","p",JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideLocalizacaoGeografica","locGeo", JoinType.LEFT_OUTER_JOIN);
		return lacTransportelDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<LacTransporteProduto> buscarClasseRisco(Requerimento req) {
		DetachedCriteria criteria = DetachedCriteria.forClass(LacTransporteProduto.class);
		criteria.createAlias("ideLacTransporte", "lt");
		criteria.createAlias("lt.ideLac", "l");
		criteria.createAlias("ideProduto", "p");
		criteria.setFetchMode("ideProduto", FetchMode.JOIN);
		criteria.add(Restrictions.eq("l.ideRequerimento.ideRequerimento", req.getIdeRequerimento()));
		return lacTransporteProdutoDAO.listarPorCriteria(criteria,Order.asc("p.dscClasseRisco"));
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarLac(Lac lac) {
		laclDAO.salvarOuAtualizar(lac);		
	}
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarLacTransporte(LacTransporte lacTransporte) {
		lacTransportelDAO.salvarOuAtualizar(lacTransporte);
	}
	
	/**
	 * @Comentários Método generic usado pela [LacTransporteController] para salvar uma lista de LacTransporteProduto OU LacTransporteResiduo.
	 * @param lacTransporte
	 * @param list
	 * @
	 */	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaTransportadosLac(LacTransporte lacTransporte, List<?> list) {
		for (Object object : list) {
			if(object instanceof Produto){
				LacTransporteProduto lacTransporteProduto = new LacTransporteProduto(new LacTransporteProdutoPK(lacTransporte.getIdeLacTransporte(), ((Produto) object).getIdeProduto())); 
				lacTransporteProduto.setIdeLacTransporte(lacTransporte);
				lacTransporteProduto.setIdeProduto((Produto) object);
				lacTransporteProduto.setQtdMediaTransporteAnual(((Produto) object).getQtdTransporteAnual());
				salvarLacTransporteProdutoResiduo(lacTransporteProduto);
			} else if(object instanceof Residuo){
				LacTransporteResiduo lacTransporteResiduo = new LacTransporteResiduo(new LacTransporteResiduoPK(lacTransporte.getIdeLacTransporte(), ((Residuo) object).getIdeResiduo()));
				lacTransporteResiduo.setIdeLacTransporte(lacTransporte);
				lacTransporteResiduo.setIdeResiduo((Residuo) object);
				lacTransporteResiduo.setQtdMediaTransporteAnual(((Residuo) object).getQtdTransporteAnual());
				salvarLacTransporteProdutoResiduo(lacTransporteResiduo);
			}
		}
	}
	
	/**
	 * @Comentários Método generic usado para salvar LacTransporteProduto OU LacTransporteResiduo.
	 * @param object
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarLacTransporteProdutoResiduo(Object object) {
		if(object instanceof LacTransporteProduto){
			lacTransporteProdutoDAO.salvarOuAtualizar((LacTransporteProduto) object);
		} else if (object instanceof LacTransporteResiduo){
			lacTransporteResiduoDAO.salvarOuAtualizar((LacTransporteResiduo) object);
		}
	}
	
	/**
	 * @Comentários Método que busca a tabela associativa LacTransporteProduto de acordo com LacTransporte
	 * @param lacTransporte
	 * @return LacTransporteProduto
	 * @
	 * @author eduardo.fernandes
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<LacTransporteProduto> buscarLacTransporteProduto(LacTransporte lacTransporte) {
		DetachedCriteria criteria = DetachedCriteria.forClass(LacTransporteProduto.class);
		criteria.add(Restrictions.eq("ideLacTransporte", lacTransporte));
		criteria.setFetchMode("ideLacTransporte", FetchMode.JOIN);
		criteria.setFetchMode("ideProduto", FetchMode.JOIN);
		return lacTransporteProdutoDAO.listarPorCriteria(criteria);
	}
	
	/**
	 * @Comentários Método para preenchimento da lista de Produtos selecionados daquela LacTransporte
	 * @param lacTransporte
	 * @return listProduto
	 * @
	 * @author eduardo.fernandes
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Produto> carregarListaProdutoSelecionados(LacTransporte lacTransporte) {
		List<Produto> listProduto = new ArrayList<Produto>();
		List<LacTransporteProduto> tempProduto = buscarLacTransporteProduto(lacTransporte); 
		if(!Util.isNullOuVazio(tempProduto)){
			// Caso exista LacTransporteProduto no banco, devemos adicionar os Produtos na listProdutoSelected.  
			for(LacTransporteProduto transporteProduto : tempProduto){
				// Valor salvo em LacTransporteProduto, transietne de Produto
				transporteProduto.getIdeProduto().setQtdTransporteAnual(transporteProduto.getQtdMediaTransporteAnual());
				// Controladores booleanos
				transporteProduto.getIdeProduto().setDesabilitaQtd(true);
				listProduto.add(transporteProduto.getIdeProduto());
				if(transporteProduto.getIdeProduto().getIdeProduto().equals(LacTransporteOutroEnum.PRODUTO_OUTROS.getId())){
					transporteProduto.getIdeProduto().setOutro(true);
				}
			}
		}
		return listProduto;
	}
		
	/**
	 * @Comentários Método que busca a tabela associativa LacTransporteResiduo de acordo com LacTransporte
	 * @param lacTransporte
	 * @return LacTransporteResiduo
	 * @
	 * @author eduardo.fernandes
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<LacTransporteResiduo> buscarLacTransporteResiduo(LacTransporte lacTransporte) {
		DetachedCriteria criteria = DetachedCriteria.forClass(LacTransporteResiduo.class);
		criteria.add(Restrictions.eq("ideLacTransporte", lacTransporte));
		criteria.setFetchMode("ideLacTransporte", FetchMode.JOIN);
		criteria.setFetchMode("ideResiduo", FetchMode.JOIN);
		return lacTransporteResiduoDAO.listarPorCriteria(criteria);
	}
	
	/**
	 * @Comentários Método para preenchimento da lista de Residuos selecionados daquela LacTransporte
	 * @param lacTransporte
	 * @return listResiduo
	 * @
	 * @author eduardo.fernandes
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Residuo> carregarListaResiduoSelecionados(LacTransporte lacTransporte) {
		List<Residuo> listResiduo = new ArrayList<Residuo>();
		List<LacTransporteResiduo> tempResiduo = buscarLacTransporteResiduo(lacTransporte);
		if(!Util.isNullOuVazio(tempResiduo)){
			// Caso exista LacTransporteResiduo no banco, devemos adicionar os Resíduos na listResiduoSelected.  
			for(LacTransporteResiduo transporteResiduo : tempResiduo){
				// Valor salvo em LacTransporteResiduo, transietne de Residuo
				transporteResiduo.getIdeResiduo().setQtdTransporteAnual(transporteResiduo.getQtdMediaTransporteAnual());
				// Controladores booleanos
				transporteResiduo.getIdeResiduo().setDesabilitaQtd(true);
				listResiduo.add(transporteResiduo.getIdeResiduo());
				if(transporteResiduo.getIdeResiduo().getIdeResiduo().equals(LacTransporteOutroEnum.RESIDUO_OUTROS.getId())){
					transporteResiduo.getIdeResiduo().setOutro(true);
				}
			}
		}
		return listResiduo;
	}
	
	/**
	 * @Comentários método para remover os objetos da tabela associativa LacTransporteProduto buscando uma lista pelo IdeLacTransporte
	 * @param lacTransporte
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirLacTransporteProdutoByIdeLacTransporte(LacTransporte lacTransporte) {
		List<LacTransporteProduto> tempProduto = buscarLacTransporteProduto(lacTransporte);
		// Verifica se é edição
		if(!Util.isNullOuVazio(tempProduto)){
			for(LacTransporteProduto transporteProduto : tempProduto){
				lacTransporteProdutoDAO.remover(transporteProduto);
			}
		}
	}
	
	/**
	 * @Comentários método para remover os objetos da tabela associativa LacTransporteResiduo buscando uma lista pelo IdeLacTransporte
	 * @param lacTransporte
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirLacTransporteResiduoByIdeLacTransporte(LacTransporte lacTransporte) {
		List<LacTransporteResiduo> tempResiduo = buscarLacTransporteResiduo(lacTransporte);
		// Verifica se é edição
		if(!Util.isNullOuVazio(tempResiduo)){
			for(LacTransporteResiduo transporteResiduo : tempResiduo){
				lacTransporteResiduoDAO.remover(transporteResiduo);
			}
		}
	}

	public void excluirLac(Lac ideLac)  {
		laclDAO.remover(ideLac);
	}
	
	public Lac buscarLac(Requerimento req) {
		
		DetachedCriteria  criteria = DetachedCriteria.forClass(Lac.class, "lac");
		criteria.createAlias("lac.ideRequerimento", "req", JoinType.INNER_JOIN);
		criteria.createAlias("lac.ideDocumentoObrigatorio","doc",  JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("req.ideRequerimento", req.getIdeRequerimento()));
		
		return laclDAO.buscarPorCriteria(criteria);
	}
	
	public boolean existeLacTransportadora(Lac ideLac) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(LacTransporte.class, "lacT");
		criteria.createAlias("lacT.ideLac", "lac", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("lac.ideLac", ideLac.getIdeLac()));
		
		return lacTransportelDAO.isExiste(criteria);
	}
}
