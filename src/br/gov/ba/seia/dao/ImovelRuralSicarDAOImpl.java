package br.gov.ba.seia.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.ImovelRuralSicar;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

public class ImovelRuralSicarDAOImpl {
	
	@Inject
	IDAO<ImovelRuralSicar> dao;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ImovelRuralSicar filtrarById(ImovelRuralSicar pImovelRuralSicar) {
		return dao.buscarEntidadePorExemplo(pImovelRuralSicar);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ImovelRuralSicar pImovelRuralSicar)  {
		dao.salvar(pImovelRuralSicar);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(ImovelRuralSicar pImovelRuralSicar)  {
		dao.atualizar(pImovelRuralSicar);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarComMerge(ImovelRuralSicar pImovelRuralSicar)  {
		dao.merge(pImovelRuralSicar);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(ImovelRuralSicar pImovelRuralSicar)  {
		String deleteSQL = "delete from imovel_rural_sicar where ide_imovel_rural_sicar = :ideImovelRuralSicar";
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideImovelRuralSicar", pImovelRuralSicar.getIdeImovelRuralSicar());
		dao.executarNativeQuery(deleteSQL, params);	
	}
	
	@SuppressWarnings("unchecked")
	public List<ImovelRural> listarImoveisComFiltroeNativeQuery(ImovelRural imovelRuralPesquisa, Boolean temNumCar, int first, int pageSize)  {
		List<Object[]> listaObj = null;
		
		StringBuilder lSql = new StringBuilder();
		lSql.append("SELECT ir.ide_imovel_rural, ir.des_denominacao, irs.ide_imovel_rural_sicar, irs.dtc_ini_sincronia, irs.dtc_fim_sincronia, irs.num_sicar ");
		lSql.append("FROM imovel_rural ir ");
		lSql.append("INNER JOIN imovel i ON i.ide_imovel = ir.ide_imovel_rural ");
		lSql.append("INNER JOIN imovel_rural_sicar irs ON irs.ide_imovel_rural = ir.ide_imovel_rural ");
		lSql.append("WHERE i.ind_excluido = false ");
		lSql.append("AND irs.ind_sincronia = " + imovelRuralPesquisa.getImovelRuralSicar().isIndSicronia() + " ");
		
		if(!Util.isNullOuVazio(imovelRuralPesquisa.getDesDenominacao())){
			lSql.append("AND ir.des_denominacao ilike '%" + imovelRuralPesquisa.getDesDenominacao().replace("'", "''") + "%' ");
		}
		
		if(!Util.isNullOuVazio(imovelRuralPesquisa.getImovelRuralSicar().getDtcIniSicronia())){
			lSql.append("AND Date(irs.dtc_ini_sincronia) = Date('" + Util.formatData(imovelRuralPesquisa.getImovelRuralSicar().getDtcIniSicronia()) + "') ");
		}
		
		if(!Util.isNullOuVazio(imovelRuralPesquisa.getImovelRuralSicar().getNumProtocolo())){
			lSql.append("AND irs.num_protocolo like '%" + imovelRuralPesquisa.getImovelRuralSicar().getNumProtocolo() + "%' ");
		}
		
		if(!Util.isNullOuVazio(imovelRuralPesquisa.getImovelRuralSicar().getToken())){
			lSql.append("AND irs.token like '%" + imovelRuralPesquisa.getImovelRuralSicar().getToken() + "%' ");
		}
		
		if(!Util.isNullOuVazio(imovelRuralPesquisa.getImovelRuralSicar().getNumSicar())){
			lSql.append("AND irs.num_sicar like '%" + imovelRuralPesquisa.getImovelRuralSicar().getNumSicar() + "%' ");
		}
		
		if(!Util.isNullOuVazio(imovelRuralPesquisa.getImovelRuralSicar().getMsgRetornoSincronia())){
			lSql.append("AND Upper(irs.msg_retorno_sincronia) like Upper('%" + imovelRuralPesquisa.getImovelRuralSicar().getMsgRetornoSincronia() + "%') ");
		}
		
		if(!Util.isNullOuVazio(imovelRuralPesquisa.getImovelRuralSicar().getCodRetornoSincronia())){
			if(!imovelRuralPesquisa.getImovelRuralSicar().getCodRetornoSincronia().equals("TODOS"))
				lSql.append("AND irs.cod_retorno_sincronia = '" + imovelRuralPesquisa.getImovelRuralSicar().getCodRetornoSincronia() + "' ");			
		} else {
			lSql.append("AND irs.cod_retorno_sincronia is null ");
		}
		
		if(!Util.isNullOuVazio(temNumCar)){
			if(temNumCar)
				lSql.append("AND irs.num_sicar is Not null ");
			else
				lSql.append("AND irs.num_sicar is null ");
		}
				
		lSql.append("ORDER BY irs.ide_imovel_rural_sicar DESC OFFSET "+first+" limit "+pageSize+";");			
		
		EntityManager lEntityManager =  DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createNativeQuery(lSql.toString());
								
		listaObj  = lQuery.getResultList();
		
		List<ImovelRural> listaImoveis = new ArrayList<ImovelRural>();
		
		for (Object[] resultElement : listaObj) {
			ImovelRural imovelAdd = new ImovelRural();
			imovelAdd.setImovelRuralSicar(new ImovelRuralSicar());
			imovelAdd.setIdeImovelRural((Integer) resultElement[0]);
			imovelAdd.setDesDenominacao((String) resultElement[1]);
			imovelAdd.getImovelRuralSicar().setIdeImovelRuralSicar((Integer) resultElement[2]);
			imovelAdd.getImovelRuralSicar().setDtcIniSicronia((Date) resultElement[3]);
			imovelAdd.getImovelRuralSicar().setDtcFimSicronia((Date) resultElement[4]);
			imovelAdd.getImovelRuralSicar().setNumSicar((String) resultElement[5]);
			listaImoveis.add(imovelAdd);
		}
		return listaImoveis;
	}
	
	public Integer qtdImoveisComFiltroeNativeQuery(ImovelRural imovelRuralPesquisa, Boolean temNumCar, int first, int pageSize)  {
		BigInteger qtdRegistros;
		
		StringBuilder lSql = new StringBuilder();
		lSql.append("SELECT COUNT(DISTINCT ir.ide_imovel_rural)");
		lSql.append("FROM imovel_rural ir ");
		lSql.append("INNER JOIN imovel i ON i.ide_imovel = ir.ide_imovel_rural ");
		lSql.append("INNER JOIN imovel_rural_sicar irs ON irs.ide_imovel_rural = ir.ide_imovel_rural ");
		lSql.append("WHERE i.ind_excluido = false ");
		lSql.append("AND irs.ind_sincronia = " + imovelRuralPesquisa.getImovelRuralSicar().isIndSicronia() + " ");
		
		if(!Util.isNullOuVazio(imovelRuralPesquisa.getDesDenominacao())){
			lSql.append("AND ir.des_denominacao ilike '%" + imovelRuralPesquisa.getDesDenominacao().replace("'", "''") + "%' ");
		}
		
		if(!Util.isNullOuVazio(imovelRuralPesquisa.getImovelRuralSicar().getDtcIniSicronia())){
			lSql.append("AND Date(irs.dtc_ini_sincronia) = Date('" + Util.formatData(imovelRuralPesquisa.getImovelRuralSicar().getDtcIniSicronia()) + "') ");
		}
		
		if(!Util.isNullOuVazio(imovelRuralPesquisa.getImovelRuralSicar().getNumProtocolo())){
			lSql.append("AND irs.num_protocolo like '%" + imovelRuralPesquisa.getImovelRuralSicar().getNumProtocolo() + "%' ");
		}
		
		if(!Util.isNullOuVazio(imovelRuralPesquisa.getImovelRuralSicar().getToken())){
			lSql.append("AND irs.token like '%" + imovelRuralPesquisa.getImovelRuralSicar().getToken() + "%' ");
		}
		
		if(!Util.isNullOuVazio(imovelRuralPesquisa.getImovelRuralSicar().getNumSicar())){
			lSql.append("AND irs.num_sicar like '%" + imovelRuralPesquisa.getImovelRuralSicar().getNumSicar() + "%' ");
		}
		
		if(!Util.isNullOuVazio(imovelRuralPesquisa.getImovelRuralSicar().getMsgRetornoSincronia())){
			lSql.append("AND Upper(irs.msg_retorno_sincronia) like Upper('%" + imovelRuralPesquisa.getImovelRuralSicar().getMsgRetornoSincronia() + "%') ");
		}
		
		if(!Util.isNullOuVazio(imovelRuralPesquisa.getImovelRuralSicar().getCodRetornoSincronia())){
			if(!imovelRuralPesquisa.getImovelRuralSicar().getCodRetornoSincronia().equals("TODOS"))
				lSql.append("AND irs.cod_retorno_sincronia = '" + imovelRuralPesquisa.getImovelRuralSicar().getCodRetornoSincronia() + "' ");			
		} else {
			lSql.append("AND irs.cod_retorno_sincronia is null ");
		}
		
		if(!Util.isNullOuVazio(temNumCar)){
			if(temNumCar)
				lSql.append("AND irs.num_sicar is Not null ");
			else
				lSql.append("AND irs.num_sicar is null ");
		}
				
		lSql.append("OFFSET "+first+" limit "+pageSize+";");	
		
		EntityManager lEntityManager = DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createNativeQuery(lSql.toString());
								
		qtdRegistros  = (BigInteger) lQuery.getSingleResult();
		
		
		return qtdRegistros.intValue();
	}

	@SuppressWarnings("unchecked")
	public List<ImovelRural> listarImoveisSemSicar(boolean isCount)  {
		StringBuilder lSql = new StringBuilder();
		lSql.append("select ir.ide_imovel_rural "
				+ " from imovel_rural ir "
				+ " inner join imovel i on i.ide_imovel = ir.ide_imovel_rural "
				+ " where i.ind_excluido = False "
				+ " and ir.status_cadastro IN (1, 3) " // -- registrados e cadastrados
				+ " and ir.ide_tipo_cadastro_imovel_rural IN( 4,5) " // contrato BNDES
				+ " and ir.ide_imovel_rural not in (select irs.ide_imovel_rural from imovel_rural_sicar irs) "
				+ " and dtc_finalizacao > '2017-07-10 23:59:59' "
				+ " and ir.ide_imovel_rural not in (select ide_imovel_rural from reserva_legal where ide_imovel_rural in ( "
				+ "			select ir.ide_imovel_rural " 
				+ "			from imovel_rural ir "
				+ "			inner join imovel i on i.ide_imovel = ir.ide_imovel_rural " 
				+ "			where i.ind_excluido = False "
				+ "			and ir.status_cadastro IN (1, 3)  "
				+ "			and ir.ide_tipo_cadastro_imovel_rural IN( 4,5)  "
				+ "			and ir.ide_imovel_rural not in (select irs.ide_imovel_rural from imovel_rural_sicar irs) " 
				+ "			and dtc_finalizacao > '2017-07-10 23:59:59' " 
				+ "			) "
				+ "			and (ide_localizacao_geografica is null or ide_localizacao_geografica not in (select ide_localizacao_geografica from dado_geografico))) "
				+ " and ir.ide_imovel_rural not in (select ide_imovel from requerimento_imovel group by ide_imovel having count(ide_imovel) >= 2)"
				+ " order by dtc_finalizacao ");
		if(!isCount){
			lSql.append("limit 500");
		}
	
		EntityManager lEntityManager =  DAOFactory.getEntityManager();
		Query lQuery = lEntityManager.createNativeQuery(lSql.toString());
		
		List<ImovelRural> listaImovel = new ArrayList<ImovelRural>();
		List<Integer> listaIdeImovelRural = lQuery.getResultList();
		
		for (Integer ideImovel : listaIdeImovelRural) {
			listaImovel.add(new ImovelRural(ideImovel));
		}
		return listaImovel;
	}
	
	public List<ImovelRuralSicar> listarImovelRuralSicarPor(Integer ideEmpreendimento)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(ImovelRuralSicar.class);
		
		criteria
			.createAlias("ideImovelRural", "ir", JoinType.INNER_JOIN)
			.createAlias("ir.imovel", "i", JoinType.INNER_JOIN)
			.createAlias("i.imovelEmpreendimentoCollection", "ie", JoinType.INNER_JOIN)
			.createAlias("ie.ideEmpreendimento", "e", JoinType.INNER_JOIN)
			
			.add(Restrictions.eq("e.ideEmpreendimento", ideEmpreendimento))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideImovelRuralSicar"),"ideImovelRuralSicar")
				.add(Projections.property("numSicar"),"numSicar")
				.add(Projections.property("numProtocolo"),"numProtocolo")
				.add(Projections.property("json"),"json")
				.add(Projections.property("indSicronia"),"indSicronia")
				.add(Projections.property("dtcIniSicronia"),"dtcIniSicronia")
				.add(Projections.property("dtcFimSicronia"),"dtcFimSicronia")
				.add(Projections.property("token"),"token")
				.add(Projections.property("urlReciboInscricao"),"urlReciboInscricao")
				.add(Projections.property("dtcCriacao"),"dtcCriacao")
				.add(Projections.property("codRetornoSincronia"),"codRetornoSincronia")
				.add(Projections.property("msgRetornoSincronia"),"msgRetornoSincronia")
				.add(Projections.property("ir.ideImovelRural"),"ideImovelRural.ideImovelRural")
			)
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ImovelRuralSicar.class))
		;
		
		return dao.listarPorCriteria(criteria);
	}

}