package br.gov.ba.seia.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.Bioma;
import br.gov.ba.seia.entity.BiomaRequerimento;
import br.gov.ba.seia.entity.ClassificacaoSecaoGeometrica;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.enumerator.BiomaEnum;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;
import br.gov.ba.seia.enumerator.PerguntaEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class BiomaRequerimentoDAOImpl {
	
	@Inject
	private IDAO<BiomaRequerimento> biomaRequerimentoDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(BiomaRequerimento biomaRequerimento)  {
		biomaRequerimentoDAO.salvar(biomaRequerimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(BiomaRequerimento biomaRequerimento)  {
		biomaRequerimentoDAO.atualizar(biomaRequerimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarEmLote(Collection<BiomaRequerimento> listaBiomaRequerimento)  {
		for(BiomaRequerimento br : listaBiomaRequerimento) {
			if(Util.isNull(br)) {
				salvar(br);
			}
			else {
				atualizar(br);
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(BiomaRequerimento biomaRequerimento)  {
		biomaRequerimentoDAO.salvarOuAtualizar(biomaRequerimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(BiomaRequerimento biomaRequerimento) {
		biomaRequerimentoDAO.remover(biomaRequerimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerPor(Integer ideRequerimento)  {
		String sql = "delete from BiomaRequerimento br where br.ideRequerimento.ideRequerimento=:ideRequerimento";
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideRequerimento", ideRequerimento);
		
		biomaRequerimentoDAO.executarQuery(sql, params);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<BiomaRequerimento> listarPor(Integer ideRequerimento){
		
		DetachedCriteria criteria = DetachedCriteria.forClass(BiomaRequerimento.class);
		
		criteria
			.createAlias("ideRequerimento", "r", JoinType.INNER_JOIN)
			.createAlias("ideBioma", "b", JoinType.INNER_JOIN)
			.createAlias("ideLocalizacaoGeografica", "l", JoinType.INNER_JOIN)
			.createAlias("biomaEnquadramentoAtoAmbiental", "beaa", JoinType.LEFT_OUTER_JOIN)
			.createAlias("beaa.ideEnquadramentoAtoAmbiental", "eaa", JoinType.LEFT_OUTER_JOIN)
			.createAlias("eaa.atoAmbiental", "aa", JoinType.LEFT_OUTER_JOIN)
			
			.add(Restrictions.eq("r.ideRequerimento", ideRequerimento))
			
			.addOrder(Order.asc("b.nomBioma"))
			
			.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideBiomaRequerimento"),"ideBiomaRequerimento")
					.add(Projections.property("valArea"),"valArea")
					.add(Projections.property("b.ideBioma"),"ideBioma.ideBioma")
					.add(Projections.property("b.nomBioma"),"ideBioma.nomBioma")
					.add(Projections.property("l.ideLocalizacaoGeografica"),"ideLocalizacaoGeografica.ideLocalizacaoGeografica")
					.add(Projections.property("r.ideRequerimento"),"ideRequerimento.ideRequerimento")
					.add(Projections.property("beaa.biomaEnquadramentoAtoAmbientalPK"),"biomaEnquadramentoAtoAmbiental.biomaEnquadramentoAtoAmbientalPK")
					.add(Projections.property("eaa.ideEnquadramentoAtoAmbiental"),"biomaEnquadramentoAtoAmbiental.ideEnquadramentoAtoAmbiental.ideEnquadramentoAtoAmbiental")
					.add(Projections.property("aa.ideAtoAmbiental"),"biomaEnquadramentoAtoAmbiental.ideEnquadramentoAtoAmbiental.atoAmbiental.ideAtoAmbiental")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(BiomaRequerimento.class))
		;
		
		return biomaRequerimentoDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<BiomaRequerimento> gerarListaBiomaRequerimento(Integer ideRequerimento, Integer ideLocalizacaoGeografica) {
		
		String sql = "SELECT * FROM sp_get_bioma(:ideLocalizacaoGeografica)";
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideLocalizacaoGeografica", ideLocalizacaoGeografica);
		
		@SuppressWarnings("rawtypes")
		List result = biomaRequerimentoDAO.buscarPorNativeQuery(sql, params);
		
		Collection<BiomaRequerimento> listaBiomaRequerimento = null;
		
		if(!Util.isNullOuVazio(result)) {
			
			listaBiomaRequerimento = new ArrayList<BiomaRequerimento>();
			
			for(Object resultElement : result) {
				
				Object[] item = (Object[]) resultElement;
				
				if (!Util.isNullOuVazio(item[0])){
					BiomaEnum biomaEnum = BiomaEnum.getEnum(item[0].toString());
					Bioma ideBioma = new Bioma(biomaEnum.getId());
					Double valArea = ((BigDecimal) item[1]).doubleValue();
					
					if(valArea.compareTo(new Double("0.0")) == 1) {
						BiomaRequerimento br = new BiomaRequerimento();
						br.setIdeRequerimento(new Requerimento(ideRequerimento));
						br.setIdeBioma(ideBioma);
						br.setValArea(valArea);
						br.setIdeLocalizacaoGeografica(new LocalizacaoGeografica(ideLocalizacaoGeografica));
						
						listaBiomaRequerimento.add(br);
					}
				}
			}
		}
		
		return listaBiomaRequerimento;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<BiomaRequerimento> gerarListaBiomaRequerimentoParaPonto(Integer ideRequerimento, LocalizacaoGeografica localizacaoGeografica){
		
		String sql = "SELECT * FROM sp_get_bioma(:ideLocalizacaoGeografica)";
		
		Map<String,Object> params = new HashMap<String, Object>();
		
		params.put("ideLocalizacaoGeografica", localizacaoGeografica.getIdeLocalizacaoGeografica());
		
		@SuppressWarnings("rawtypes")
		List result = biomaRequerimentoDAO.buscarPorNativeQuery(sql, params);
		
		Collection<BiomaRequerimento> listaBiomaRequerimento = null;
		
		if(!Util.isNullOuVazio(result)) {
			
			listaBiomaRequerimento = new ArrayList<BiomaRequerimento>();
			
			for(Object resultElement : result) {
				
				Object[] item = (Object[]) resultElement;
				
				if (!Util.isNullOuVazio(item[0])){
					BiomaEnum biomaEnum = BiomaEnum.getEnum(item[0].toString());
					Bioma ideBioma = new Bioma(biomaEnum.getId());
					Double valArea = ((BigDecimal) item[1]).doubleValue();
					
					ClassificacaoSecaoGeometrica ponto = new ClassificacaoSecaoGeometrica(ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId());
					if(valArea.compareTo(new Double("0.0")) == 1 || ponto.equals(localizacaoGeografica.getIdeClassificacaoSecao())) {
						BiomaRequerimento br = new BiomaRequerimento();
						br.setIdeRequerimento(new Requerimento(ideRequerimento));
						br.setIdeBioma(ideBioma);
						br.setValArea(valArea);
						br.setIdeLocalizacaoGeografica(localizacaoGeografica);
						
						listaBiomaRequerimento.add(br);
					}
				}
			}
		}
		
		return listaBiomaRequerimento;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Boolean incluirNoCalculoVistoria(Requerimento requerimento , LocalizacaoGeografica localizacaoGeografica) {
		
		String sql = 
				"select exists(select 1 from florestal f "
				+ "inner join florestal_caracteristica_floresta_producao fc on f.ide_florestal = fc.ide_florestal and fc.ide_caracteristica_floresta_producao = 4 "
				+ "inner join pergunta_requerimento  pr on f.ide_requerimento = pr.ide_requerimento and f.ide_imovel = pr.ide_imovel " 
				+ "inner join pergunta p on p.ide_pergunta = pr.ide_pergunta "
				+ "where p.cod_pergunta = :codPergunta "
				+ "and pr.ide_requerimento= :ideRequerimento "
				+ "and pr.ide_localizacao_geografica = :ideLocalizacaoGeografica);"
		;
		
		Map<String,Object> params = new HashMap<String, Object>();
		
		params.put("codPergunta", PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA5_D1_1p13.getCod());
		params.put("ideRequerimento", requerimento.getIdeRequerimento());
		params.put("ideLocalizacaoGeografica", localizacaoGeografica.getIdeLocalizacaoGeografica());
		
		Object result = biomaRequerimentoDAO.obterPorNativeQuery(sql, params);
		
		return ((Boolean) result) == false;
	}	
}