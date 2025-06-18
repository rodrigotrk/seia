package br.gov.ba.seia.service;

import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Level;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.DAOFactory;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.ImovelDAOImpl;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EmpreendimentoRequerimento;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.ObjetivoRequerimentoLimpezaArea;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeograficaImovel;
import br.gov.ba.seia.entity.RegistroFlorestaProducaoImovel;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.enumerator.TipoImovelEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ImovelService {

	@Inject
	private IDAO<Imovel> imovelDAO;

	@Inject 
	private ImovelDAOImpl imovelDaoImpl;
	
	@EJB
	private EmpreendimentoService empreedimentoIDAO;

	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAtualizarImovel(Imovel pImovel) {
		imovelDAO.salvarOuAtualizar(pImovel);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Imovel> listarImovelPor(Integer ideRequerimento)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(EmpreendimentoRequerimento.class);
		criteria
			.createAlias("ideRequerimento", "r", JoinType.INNER_JOIN)
			.createAlias("ideEmpreendimento", "emp", JoinType.INNER_JOIN)
			.createAlias("emp.imovelEmpreendimentoCollection", "ie", JoinType.INNER_JOIN)
			.createAlias("ie.ideImovel", "i", JoinType.INNER_JOIN)
			.createAlias("i.ideTipoImovel", "ti", JoinType.INNER_JOIN)
			.createAlias("i.imovelRural", "ir", JoinType.INNER_JOIN)
			.add(Restrictions.eq("r.ideRequerimento", ideRequerimento))
			.setProjection(Projections.projectionList()
				.add(Projections.property("i.ideImovel"),"ideImovel")
				.add(Projections.property("i.dtcCriacao"),"dtcCriacao")
				.add(Projections.property("i.indExcluido"),"indExcluido")
				.add(Projections.property("i.dtcExclusao"),"dtcExclusao")
				.add(Projections.property("ti.ideTipoImovel"),"ideTipoImovel.ideTipoImovel")
				.add(Projections.property("ir.ideImovelRural"),"imovelRural.ideImovelRural")
				.add(Projections.property("ir.desDenominacao"),"imovelRural.desDenominacao")
			)
			.addOrder(Order.asc("ir.desDenominacao"))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Imovel.class))
		;

		return imovelDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Imovel> listarImovelPorOutorgaLocalizacaoGeografica(Integer ideOutorgaLocalizacaoGeografica)  {

		DetachedCriteria criteria = DetachedCriteria.forClass(OutorgaLocalizacaoGeograficaImovel.class);

		criteria
		.createAlias("ideOutorgaLocalizacaoGeografica", "olg", JoinType.INNER_JOIN)
		.createAlias("ideImovel", "i", JoinType.LEFT_OUTER_JOIN)
		.createAlias("i.imovelRural", "iRural", JoinType.LEFT_OUTER_JOIN)
		.add(Restrictions.eq("ideOutorgaLocalizacaoGeografica.ideOutorgaLocalizacaoGeografica", ideOutorgaLocalizacaoGeografica))
		.setProjection(Projections.projectionList()
				.add(Projections.property("i.ideImovel"),"ideImovel")
				.add(Projections.property("i.dtcCriacao"),"dtcCriacao")
				.add(Projections.property("iRural.ideImovelRural"),"imovelRural.ideImovelRural")
				.add(Projections.property("iRural.desDenominacao"),"imovelRural.desDenominacao")
				.add(Projections.property("i.indExcluido"),"indExcluido")
				.add(Projections.property("i.dtcExclusao"),"dtcExclusao")
				.add(Projections.property("i.dtcCriacao"),"dtcCriacao")
				)
				.setResultTransformer(new AliasToNestedBeanResultTransformer(Imovel.class));

		return imovelDAO.listarPorCriteria(criteria);
	}


	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Imovel buscarImovelPorIde(Imovel pImovel) {

		DetachedCriteria criteria = DetachedCriteria.forClass(Imovel.class, "imovel" );
		if(Util.isNullOuVazio(pImovel.getImovelRural())){
			criteria.createAlias("imovelUrbano", "imovelUrbano", JoinType.LEFT_OUTER_JOIN);
			
		}else{
			
			criteria.createAlias("imovelRural", "imovelRural", JoinType.LEFT_OUTER_JOIN);
		}
		
		criteria.createAlias("ideTipoImovel", "tipoImovel", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideEndereco", "endereco", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("endereco.ideLogradouro", "logradouro", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("logradouro.ideMunicipio", "municipio", JoinType.LEFT_OUTER_JOIN);	
		criteria.createAlias("empreendimentoCollection", "empreendimentoCollection", JoinType.LEFT_OUTER_JOIN);

		criteria.add(Restrictions.eq("ideImovel", pImovel.getIdeImovel()));
		
		criteria.addOrder(Order.desc("imovel.dtcCriacao"));

		return imovelDAO.buscarPorCriteriaMaxResult(criteria);
	}

	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Integer retornaTipoImovelPorIdeImovel(Integer ideImovel) {
		List<Integer> listaObj = null;

		StringBuilder lSql = new StringBuilder();
		lSql.append("SELECT ide_tipo_imovel FROM imovel WHERE ide_imovel = "+ideImovel);

		EntityManager lEntityManager = DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createNativeQuery(lSql.toString());

		listaObj  = lQuery.getResultList();

		return listaObj.get(0);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Imovel buscarImovelPorIdeSemImRural(Imovel pImovel) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Imovel.class, "imovel" );
		criteria.createAlias("imovelUrbano", "imovelUrbano", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideTipoImovel", "tipoImovel", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideEndereco", "endereco", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("endereco.ideLogradouro", "logradouro", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("logradouro.ideMunicipio", "municipio", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("empreendimentoCollection", "empreendimentoCollection", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ideImovel", pImovel.getIdeImovel()));

		return imovelDAO.listarPorCriteria(criteria, Order.asc("imovel.ideImovel")).get(0);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Imovel> filtrarListaImovelPorEmpreendimento(Empreendimento pEmpreendimento) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Imovel.class, "imovel")
				.createAlias("imovelUrbano", "urb", JoinType.LEFT_OUTER_JOIN)
				.createAlias("imovelRural", "rur", JoinType.LEFT_OUTER_JOIN)
				.createAlias("rur.ideMunicipioCartorio", "mun", JoinType.LEFT_OUTER_JOIN)
				.createAlias("rur.ideLocalizacaoGeografica", "loc", JoinType.LEFT_OUTER_JOIN)
				.createAlias("rur.imovel", "imovelRur", JoinType.LEFT_OUTER_JOIN)
				.createAlias("imovelRur.ideEndereco", "endereco", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideTipoImovel", "tipo", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideEndereco", "end", JoinType.LEFT_OUTER_JOIN)
				.createAlias("imovelEmpreendimentoCollection", "imovEmpree", JoinType.LEFT_OUTER_JOIN)
				.createAlias("imovEmpree.ideImovel", "imov", JoinType.LEFT_OUTER_JOIN)
				.createAlias("imovEmpree.ideEmpreendimento", "empree", JoinType.LEFT_OUTER_JOIN)
				
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideImovel"), "ideImovel")
				.add(Projections.property("dtcCriacao"), "dtcCriacao")
				.add(Projections.property("indExcluido"), "indExcluido")
				.add(Projections.property("dtcExclusao"), "dtcExclusao")
				
				.add(Projections.property("urb.ideImovelUrbano"), "imovelUrbano.ideImovelUrbano")
				.add(Projections.property("urb.numInscricaoIptu"), "imovelUrbano.numInscricaoIptu")
				
				.add(Projections.property("rur.ideImovelRural"), "imovelRural.ideImovelRural")
				.add(Projections.property("rur.desDenominacao"), "imovelRural.desDenominacao")
				.add(Projections.property("rur.valArea"), "imovelRural.valArea")
				.add(Projections.property("rur.desCartorio"), "imovelRural.desCartorio")
				.add(Projections.property("rur.desComarca"), "imovelRural.desComarca")
				.add(Projections.property("rur.numMatricula"), "imovelRural.numMatricula")
				.add(Projections.property("rur.numRegistro"), "imovelRural.numRegistro")
				.add(Projections.property("rur.numItr"), "imovelRural.numItr")
				.add(Projections.property("rur.desLivro"), "imovelRural.desLivro")
				.add(Projections.property("rur.numFolha"), "imovelRural.numFolha")
				.add(Projections.property("rur.numMatricula"), "imovelRural.numMatricula")
				.add(Projections.property("rur.qtdModuloFiscal"), "imovelRural.qtdModuloFiscal")
				.add(Projections.property("mun.ideMunicipio"), "imovelRural.ideMunicipioCartorio.ideMunicipio")
				.add(Projections.property("mun.nomMunicipio"), "imovelRural.ideMunicipioCartorio.nomMunicipio")
				.add(Projections.property("loc.ideLocalizacaoGeografica"), "imovelRural.ideLocalizacaoGeografica.ideLocalizacaoGeografica")
				.add(Projections.property("endereco.ideEndereco"), "imovelRural.imovel.ideEndereco.ideEndereco")
				.add(Projections.property("imovelRur.ideImovel"), "imovelRural.imovel.ideImovel")
				
				
				.add(Projections.property("tipo.ideTipoImovel"), "ideTipoImovel.ideTipoImovel")
				.add(Projections.property("tipo.nomTipoImovel"), "ideTipoImovel.nomTipoImovel")
				
				.add(Projections.property("end.ideEndereco"), "ideEndereco.ideEndereco")
				.add(Projections.property("end.numEndereco"), "ideEndereco.numEndereco")
				.add(Projections.property("end.dtcCriacao"), "ideEndereco.dtcCriacao")
				.add(Projections.property("end.indExcluido"), "ideEndereco.indExcluido")
				.add(Projections.property("end.dtcExclusao"), "ideEndereco.dtcExclusao")
				.add(Projections.property("end.desComplemento"), "ideEndereco.desComplemento")
				.add(Projections.property("end.desPontoReferencia"), "ideEndereco.desPontoReferencia")
				
				.add(Projections.property("imov.ideImovel"), "imovelEmpreendimentoCollection.ideImovel.ideImovel")
				.add(Projections.property("empree.ideEmpreendimento"), "imovelEmpreendimentoCollection.ideEmpreendimento.ideEmpreendimento"))
		
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Imovel.class));
		
		if (!Util.isNullOuVazio(pEmpreendimento.getIdeEmpreendimento())) {
			criteria.add(Restrictions.eq("empree.ideEmpreendimento", pEmpreendimento.getIdeEmpreendimento()));
		}
		
		List<Imovel> listImoveis = imovelDAO.listarPorCriteria(criteria);
		
	    Iterator<Imovel> iterator = listImoveis.iterator();
		    while (iterator.hasNext()) {
		        Imovel imovel = iterator.next();
				if (Util.isNullOuVazio(imovel.getImovelRural()) && Util.isNullOuVazio(imovel.getImovelUrbano())
						&& imovel.getIdeTipoImovel().getIdeTipoImovel() != TipoImovelEnum.CESSIONARIO.getId()) {
		            iterator.remove(); 
		        }
		    }
		
		return listImoveis;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Imovel> listarImovelRuralPorEmpreendimento(Empreendimento pEmpreendimento)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Imovel.class, "imovel")
				.createAlias("imovelRural", "rur", JoinType.INNER_JOIN)
				.createAlias("ideTipoImovel", "tipo", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideEndereco", "end", JoinType.LEFT_OUTER_JOIN)
				.createAlias("end.ideLogradouro", "log", JoinType.LEFT_OUTER_JOIN)
				.createAlias("log.ideBairro", "bai", JoinType.LEFT_OUTER_JOIN)
				.createAlias("log.ideMunicipio", "mun", JoinType.LEFT_OUTER_JOIN)
				.createAlias("imovelEmpreendimentoCollection", "imovEmpree", JoinType.LEFT_OUTER_JOIN)
				.createAlias("imovEmpree.ideImovel", "imov", JoinType.LEFT_OUTER_JOIN)
				.createAlias("imovEmpree.ideEmpreendimento", "empree", JoinType.LEFT_OUTER_JOIN)
				.createAlias("rur.imovelRuralSicar", "imovelRuralSicar", JoinType.LEFT_OUTER_JOIN)
				
				.setProjection(Projections.projectionList()
						.add(Projections.property("ideImovel"), "ideImovel")
						.add(Projections.property("dtcCriacao"), "dtcCriacao")
						.add(Projections.property("indExcluido"), "indExcluido")
						.add(Projections.property("dtcExclusao"), "dtcExclusao")
						
						.add(Projections.property("rur.ideImovelRural"), "imovelRural.ideImovelRural")
						.add(Projections.property("rur.desDenominacao"), "imovelRural.desDenominacao")
						.add(Projections.property("rur.valArea"), "imovelRural.valArea")
						.add(Projections.property("rur.desCartorio"), "imovelRural.desCartorio")
						.add(Projections.property("rur.desComarca"), "imovelRural.desComarca")
						.add(Projections.property("rur.numMatricula"), "imovelRural.numMatricula")
						.add(Projections.property("rur.numRegistro"), "imovelRural.numRegistro")
						.add(Projections.property("rur.numItr"), "imovelRural.numItr")
						.add(Projections.property("rur.desLivro"), "imovelRural.desLivro")
						.add(Projections.property("rur.numFolha"), "imovelRural.numFolha")
						.add(Projections.property("rur.numMatricula"), "imovelRural.numMatricula")
						
						.add(Projections.property("tipo.ideTipoImovel"), "ideTipoImovel.ideTipoImovel")
						.add(Projections.property("tipo.nomTipoImovel"), "ideTipoImovel.nomTipoImovel")
						
						.add(Projections.property("end.ideEndereco"), "ideEndereco.ideEndereco")
						.add(Projections.property("end.numEndereco"), "ideEndereco.numEndereco")
						.add(Projections.property("end.dtcCriacao"), "ideEndereco.dtcCriacao")
						.add(Projections.property("end.indExcluido"), "ideEndereco.indExcluido")
						.add(Projections.property("end.dtcExclusao"), "ideEndereco.dtcExclusao")
						.add(Projections.property("end.desComplemento"), "ideEndereco.desComplemento")
						.add(Projections.property("end.desPontoReferencia"), "ideEndereco.desPontoReferencia")
						
						.add(Projections.property("log.ideLogradouro"), "ideEndereco.ideLogradouro.ideLogradouro")
						
						.add(Projections.property("bai.ideBairro"), "ideEndereco.ideLogradouro.ideBairro.ideBairro")
						.add(Projections.property("bai.nomBairro"), "ideEndereco.ideLogradouro.ideBairro.nomBairro")
						
						.add(Projections.property("mun.ideMunicipio"), "ideEndereco.ideLogradouro.ideMunicipio.ideMunicipio")
						.add(Projections.property("mun.nomMunicipio"), "ideEndereco.ideLogradouro.ideMunicipio.nomMunicipio")
						
						.add(Projections.property("imov.ideImovel"), "imovelEmpreendimentoCollection.ideImovel.ideImovel")
						.add(Projections.property("empree.ideEmpreendimento"), "imovelEmpreendimentoCollection.ideEmpreendimento.ideEmpreendimento")
						.add(Projections.property("imovelRuralSicar.numSicar"), "imovelRural.imovelRuralSicar.numSicar"))
						
						.setResultTransformer(new AliasToNestedBeanResultTransformer(Imovel.class));
		
		if (!Util.isNullOuVazio(pEmpreendimento.getIdeEmpreendimento())) {
			criteria.add(Restrictions.eq("empree.ideEmpreendimento", pEmpreendimento.getIdeEmpreendimento()));
		}
		
		List<Imovel> listImoveis = imovelDAO.listarPorCriteria(criteria);
		
		for (Imovel imovel : listImoveis) {
			
			ImovelRural imovelRural = imovel.getImovelRural();
			
			if (!Util.isNull(imovelRural)) {
				Hibernate.initialize(imovelRural.getIdeMunicipioCartorio());
			}
			
			Hibernate.initialize(imovel.getIdeTipoImovel());
		}
		
		return listImoveis;
	}
	

	public Imovel getImovelByObjetivoRequerimentoLimpezaArea(ObjetivoRequerimentoLimpezaArea objetivoRequerimentoLimpezaArea,Integer imovel) {


		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Imovel.class)
				.createAlias("requerimentoImovelCollection", "ri")
				.createAlias("ri.imovel", "i")
				.createAlias("ri.requerimento", "r")
				.createAlias("ri.objetivoRequerimentoLimpezaAreaCollection", "objs")
				.createAlias("objs.ideObjetivoLimpezaArea", "obj")
				.setProjection(Projections.projectionList().add(Projections.property("ideImovel"),"ideImovel"))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(Imovel.class))
				.add(Restrictions.eq("obj.ideObjetivoLimpezaArea", objetivoRequerimentoLimpezaArea.getIdeObjetivoLimpezaArea().getIdeObjetivoLimpezaArea()))
				.add(Restrictions.eq("i.ideImovel", imovel))
				.add(Restrictions.eq("r.ideRequerimento", objetivoRequerimentoLimpezaArea.getIdeRequerimento().getIdeRequerimento()));

		return imovelDAO.buscarPorCriteria(detachedCriteria);

	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean verificarBloqueioDQC(Integer ideImovel)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Imovel.class)
			.createAlias("ideEndereco", "e", JoinType.INNER_JOIN)
			.createAlias("e.ideLogradouro", "l", JoinType.INNER_JOIN)
			.createAlias("l.ideMunicipio", "m", JoinType.INNER_JOIN)
			
			.add(Restrictions.eq("ideImovel", ideImovel))
			.add(Restrictions.eq("m.indBloqueioDQC", true))
			
			.setProjection(Projections.property("ideImovel").as("ideImovel"))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Imovel.class))
		;
		
		Imovel imovel = imovelDAO.buscarPorCriteria(criteria);
		 
		return !Util.isNull(imovel);
		 
	}

	public List<Imovel> buscarImoveisPorRequerimento(Requerimento requerimento) {
		List<Empreendimento> empreendimentos = (List<Empreendimento>) empreedimentoIDAO.buscarEmpreendimentoPorRequerimento(requerimento);
		if(!Util.isNull(empreendimentos)) {
			return filtrarListaImovelPorEmpreendimento(empreendimentos.get(0));
		}
		return null;
	}


	/**
	 * @author Alexandre Queiroz
	 * @ 
	 * @since 19/12/2016
	 * 
	 **/
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Imovel> listarRequerimentoImovelPor(Requerimento requerimento) {
		try{
			return imovelDaoImpl.listarRequerimentoImovelPor(requerimento);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	/**
	 * @author Alexandre Queiroz
	 * @ 
	 * @since 19/12/2016
	 * 
	 **/
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Imovel buscarImovelPorNumeroCar(ImovelRural imovelRural) throws  Exception {
		
		try{
			return imovelDaoImpl.buscarImovelPorNumeroCar(imovelRural);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * 
	 * @param numSicar
	 * @return
	 * @
	 * @author diegoraian
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Imovel buscarImovelPorNumeroCar(String numSicar){
		
		try{
			Imovel imovel = imovelDaoImpl.buscarImovelPorNumeroCar(numSicar);
			if(imovel != null){
				imovel = imovelDAO.carregarGet(imovel.getIdeImovel()); 
			}
			return imovel;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	/**
	 * @author Alexandre Queiroz
	 * @ 
	 * @since 19/12/2016
	 * 
	 **/
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Imovel carregarImovel(RegistroFlorestaProducaoImovel registroFlorestaProducaoImovel) {

		try{
			return imovelDaoImpl.carregarImovel(registroFlorestaProducaoImovel);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Imovel> carregarImoveisPorCadastroAtividadeNaoSujeitaLic(Integer ideCadastroAtividadeNaoSujeitaLic) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Imovel.class);
		criteria.createAlias("imovelRural", "imovelRural");
		criteria.createAlias("imovelRural.cadastroAtividadeNaoSujeitaLicImovelCollection", "cadAtividadeImovel");
		criteria.createAlias("cadAtividadeImovel.ideCadastroAtividadeNaoSujeitaLic", "cadastroAtividadeNaoSujeitaLic");
		criteria.add(Restrictions.eq("cadastroAtividadeNaoSujeitaLic.ideCadastroAtividadeNaoSujeitaLic", ideCadastroAtividadeNaoSujeitaLic));
		return imovelDAO.listarPorCriteria(criteria);
	}
	
	
}