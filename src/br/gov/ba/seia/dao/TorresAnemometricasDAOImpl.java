/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Projeto: seia
 * Pacote: br.gov.ba.seia.dao
 * @autor: diegoraian em 29 de set de 2017
 * Objetivo: 	
	
 */
package br.gov.ba.seia.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.LocalizacaoAtividadeTorre;
import br.gov.ba.seia.entity.TorreAnemometrica;
import br.gov.ba.seia.entity.TorreAnemometricaLocalizacaoAtividadeTorre;
import br.gov.ba.seia.util.Util;

/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Classe: TorresAnemometricasDAOImpl.java
 * Projeto: seia
 * Pacote: br.gov.ba.seia.dao
 * @autor: diegoraian em 29 de set de 2017
 * Objetivo: 	
	
 */
public class TorresAnemometricasDAOImpl  extends BaseDAO<TorreAnemometrica>{

	@Inject
	IDAO<TorreAnemometrica> torreAnemometricaDAO;

	@Inject
	IDAO<TorreAnemometricaLocalizacaoAtividadeTorre> torreIdao;
	
	/* (non-Javadoc)
	 * @see br.gov.ba.seia.dao.BaseDAO#getDao()
	 */
	@Override
	protected IDAO<TorreAnemometrica> getDao() {
		return torreAnemometricaDAO;
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TorreAnemometrica> carregarPorCadastroAtividade(Integer ideCadastroAtividadeNaoSujeitaLic) throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(TorreAnemometrica.class);
		criteria.createAlias("ideLocalizacaoGeografica", "localizacaoGeografica");
		criteria.createAlias("localizacaoGeografica.dadoGeograficoCollection", "dadoGeografico");
		criteria.createAlias("ideTipoNaturezaTorre", "ideTipoNaturezaTorre", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideTipoUnidadeConservacaoTorre", "ideTipoUnidadeConservacaoTorre", JoinType.LEFT_OUTER_JOIN);
//		criteria.createAlias("ideLocalizacaoAtividadeTorre", "ideLocalizacaoAtividadeTorre", JoinType.LEFT_OUTER_JOIN);
//		criteria.createAlias("torreAnemometricaLocalizacaoAtividadeTorresList", "tALAT", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ideCadastroAtividadeNaoSugeitaLic.ideCadastroAtividadeNaoSujeitaLic", ideCadastroAtividadeNaoSujeitaLic));
		criteria.add(Restrictions.eq("indExcluido", Boolean.FALSE));
		List<TorreAnemometrica> listaTorres = torreAnemometricaDAO.listarPorCriteria(criteria);
		for(TorreAnemometrica torre : listaTorres){
			Hibernate.initialize(torre.getIdeLocalizacaoGeografica().getDadoGeograficoCollection());
			
			if(!Util.isNullOuVazio(torre.getIdeTorreAnemometrica())){
				
				torre.setTorreAnemometricaLocalizacaoAtividadeTorresList(carregarTorreAnemometricaLocalizacaoAtividadeTorre(ideCadastroAtividadeNaoSujeitaLic, 
						torre.getIdeTorreAnemometrica()));
			}
			
			
			if(!Util.isNullOuVazio(torre.getTorreAnemometricaLocalizacaoAtividadeTorresList())){
				
				torre.setListaLocalizacaoAtividadeTorres(new ArrayList<LocalizacaoAtividadeTorre>());
				
				for(TorreAnemometricaLocalizacaoAtividadeTorre torreLoc: torre.getTorreAnemometricaLocalizacaoAtividadeTorresList()){
					
					torre.getListaLocalizacaoAtividadeTorres().add(torreLoc.getIdeLocalizacaoAtividadeTorre());
				}
				
				torre.setTorreAnemometricaLocalizacaoAtividadeTorreListAuxiliar(new ArrayList<TorreAnemometricaLocalizacaoAtividadeTorre>(torre.getTorreAnemometricaLocalizacaoAtividadeTorresList()));
			}
		}
		
		return listaTorres;
	}

	public List<TorreAnemometricaLocalizacaoAtividadeTorre> carregarTorreAnemometricaLocalizacaoAtividadeTorre(Integer ideCadastroAtividadeNaoSujeitaLic, Long ideTorreAnemometrica) throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(TorreAnemometricaLocalizacaoAtividadeTorre.class);
		criteria.createAlias("ideLocalizacaoAtividadeTorre", "ideLocalizacaoAtividadeTorre", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideTorreAnemometrica", "ideTorreAnemometrica", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ideTorreAnemometrica.ideCadastroAtividadeNaoSugeitaLic.ideCadastroAtividadeNaoSujeitaLic", ideCadastroAtividadeNaoSujeitaLic));
		criteria.add(Restrictions.eq("ideTorreAnemometrica.indExcluido", Boolean.FALSE));
		criteria.add(Restrictions.eq("indExcluido", Boolean.FALSE));
		criteria.add(Restrictions.eq("ideTorreAnemometrica.ideTorreAnemometrica", ideTorreAnemometrica));
		
		return torreIdao.listarPorCriteria(criteria);
		
	}
}
