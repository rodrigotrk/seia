package br.gov.ba.seia.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Caepog;
import br.gov.ba.seia.entity.CaepogCampo;
import br.gov.ba.seia.entity.CaepogDefinicaoCampo;
import br.gov.ba.seia.exception.SeiaValidacaoRuntimeException;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CaepogDefinicaoCampoService {
	
	@Inject
	IDAO<CaepogDefinicaoCampo> caepogDefinicaoCampoDAO;

	@EJB
	private ProcessoService processoService;
	
	@EJB
	private ProcessoExternoService processoExternoService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CaepogDefinicaoCampo> listarPorCaepog(Caepog c)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(CaepogDefinicaoCampo.class, "def")
				.createAlias("def.ideCaepog", "caepog", JoinType.INNER_JOIN)
				.createAlias("def.ideCaepogCampo", "campo", JoinType.INNER_JOIN)
				.createAlias("def.ideLocalizacaoGeografica", "locGeo", JoinType.LEFT_OUTER_JOIN);
		
		if(!Util.isNullOuVazio(c)) {
			criteria.add(Restrictions.eq("def.ideCaepog", c));
		}
		
		return caepogDefinicaoCampoDAO.listarPorCriteria(criteria, Order.asc("def.ideCaepogDefinicaoCampo"));
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarValidando(CaepogDefinicaoCampo cfc) throws Exception  {
		isDialogCampoValidado(cfc, false);
		caepogDefinicaoCampoDAO.salvarOuAtualizar(cfc);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarValidando(CaepogDefinicaoCampo cfc) throws Exception  {
		isDialogCampoValidado(cfc, true);
		caepogDefinicaoCampoDAO.salvarOuAtualizar(cfc);
	}
	
	private void isDialogCampoValidado(CaepogDefinicaoCampo cfc, boolean isEditando) throws Exception  {
		
		if(isEditando) {
			if(Util.isNullOuVazio(cfc.getQtdPocos())) {
				throw new SeiaValidacaoRuntimeException("Favor preencher a quantidade de poços");
				
			} else if(Util.isNullOuVazio(cfc.getNumProcessoCaepogCampo())) {
				throw new SeiaValidacaoRuntimeException("Favor preencher o número do processo");
				
			} else if(Util.isNullOuVazio(cfc.getIdeCaepogCampo())) {
				throw new SeiaValidacaoRuntimeException("Favor selecionar o campo");
				
			} else if(!isProcessoExistente(cfc.getNumProcessoCaepogCampo())){
				throw new SeiaValidacaoRuntimeException("Número do processo inválido.");
			}
		} else {
			if(Util.isNullOuVazio(cfc.getQtdPocos())) {
				throw new SeiaValidacaoRuntimeException("Favor preencher a quantidade de poços");
				
			} else if(Util.isNullOuVazio(cfc.getNumProcessoCaepogCampo())) {
				throw new SeiaValidacaoRuntimeException("Favor preencher o número do processo");
				
			} else if(Util.isNullOuVazio(cfc.getIdeCaepogCampo())) {
				throw new SeiaValidacaoRuntimeException("Favor selecionar o campo");
				
			} else if(Util.isNullOuVazio(cfc.getIdeLocalizacaoGeografica())) {
				throw new SeiaValidacaoRuntimeException("Favor inserir os shapes da localização geográfica");
				
			} else if(!Util.isNullOuVazio(listarPorCaepogECampo(cfc.getIdeCaepog(), cfc.getIdeCaepogCampo()))) {
				throw new SeiaValidacaoRuntimeException("O campo selecionado já foi cadastrado.");
				
			} else if(!isProcessoExistente(cfc.getNumProcessoCaepogCampo())){
				throw new SeiaValidacaoRuntimeException("Número do processo inválido.");
			}
		}
	}
	
	public boolean isProcessoExistente(String numProcesso) throws Exception {
		try {
			numProcesso = numProcesso.replace(String.valueOf((char) 160), " ").trim();
			
			if (!Util.isNullOuVazio(processoService.buscarProcessoPorCriteria(numProcesso))) {
				return true;
			} else {
				return !Util.isNullOuVazio(processoExternoService.buscarProcessoExternoByNumeroProcesso(numProcesso));
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CaepogDefinicaoCampo> listarPorCaepogECampo(Caepog cg, CaepogCampo cc)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(CaepogDefinicaoCampo.class, "def")
				.createAlias("def.ideCaepog", "caepog", JoinType.INNER_JOIN)
				.createAlias("def.ideCaepogCampo", "campo", JoinType.INNER_JOIN);
		
		if(!Util.isNullOuVazio(cg) && !Util.isNullOuVazio(cc)) {
			criteria.add(Restrictions.eq("def.ideCaepog", cg));
			criteria.add(Restrictions.eq("def.ideCaepogCampo", cc));
		}
		
		return caepogDefinicaoCampoDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(CaepogDefinicaoCampo cfc)  {
		caepogDefinicaoCampoDAO.remover(cfc);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirPorCaepog(Caepog c)  {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideCaepog", c.getIdeCaepog());
		
		StringBuilder sql = new StringBuilder();
		sql.append("delete from CaepogDefinicaoCampo cdc where cdc.ideCaepog.ideCaepog = :ideCaepog");
		caepogDefinicaoCampoDAO.executarQuery(sql.toString(), params);
	}
}