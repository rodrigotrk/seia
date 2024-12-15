package br.gov.ba.seia.service;

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

import org.apache.log4j.Level;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.DAOFactory;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dto.NotificacaoFinalDTO;
import br.gov.ba.seia.entity.ArquivoProcesso;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.Notificacao;
import br.gov.ba.seia.enumerator.CategoriaDocumentoEnum;
import br.gov.ba.seia.enumerator.MotivoNotificacaoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ArquivoProcessoService {

	@Inject
	IDAO<ArquivoProcesso> arquivoProcessoDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ArquivoProcesso arquivoProcesso) {
		arquivoProcessoDAO.salvar(arquivoProcesso);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarDocumentosNotificacao(NotificacaoFinalDTO dto) {
		try{
			if(dto.getNotificacaoComArquivosApensados()){
				for(ArquivoProcesso arq : dto.getListaDeDocumentosDaNotificacao()) {
					if(arq.getIdeArquivoProcesso()==null){
						arq.setIdeProcesso(dto.getNotificacaoFinal().getIdeProcesso());
						arq.setIdeNotificacao(dto.getNotificacaoFinal());
						salvar(arq);				
					}
					else{
						atualizar(arq);
					}
				}
			}
		}
		catch(Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(ArquivoProcesso arquivoProcesso) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("update ArquivoProcesso set dscArquivoProcesso = :dscArquivoProcesso ");
		sql.append("where ideArquivoProcesso = :ideArquivoProcesso ");
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("dscArquivoProcesso", arquivoProcesso.getDscArquivoProcesso());
		params.put("ideArquivoProcesso", arquivoProcesso.getIdeArquivoProcesso());
		
		arquivoProcessoDAO.executarQuery(sql.toString(), params);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaArquivos(List<ArquivoProcesso> listaArquivos) {
		
		List<ArquivoProcesso> listaDeArquivosAindaNaoSalvos = new ArrayList<ArquivoProcesso>();
		
		for(ArquivoProcesso arq : listaArquivos){
			if(arq.getIdeArquivoProcesso()==null){
				listaDeArquivosAindaNaoSalvos.add(arq);
			}
		}		
		
		arquivoProcessoDAO.salvarEmLote(listaDeArquivosAindaNaoSalvos);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deletarArquivo(ArquivoProcesso arquivo) {
		DAOFactory.getEntityManager().createNativeQuery("delete from arquivo_processo where ide_arquivo_processo = :ide")
			.setParameter("ide", arquivo.getIdeArquivoProcesso()).executeUpdate(); 
	}

	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ArquivoProcesso> listaArquivoProcessoPorIdeProcesso(Integer ideProcesso) {
		return carregarArquivosApensados(ideProcesso);
	}
	

	private Collection<ArquivoProcesso> carregarArquivosApensados(Integer ideProcesso) {
		DetachedCriteria criteriaArquivoProcesso = DetachedCriteria.forClass(ArquivoProcesso.class)
				.createAlias("ideProcesso","processo", JoinType.INNER_JOIN)
				.createAlias("localizacaoGeografica","localizacaoGeografica", JoinType.LEFT_OUTER_JOIN)
				.createAlias("motivoNotificacao","motivoNotificacao", JoinType.LEFT_OUTER_JOIN)
				.createAlias("localizacaoGeografica.ideSistemaCoordenada","ideSistemaCoordenada", JoinType.LEFT_OUTER_JOIN)
				.createAlias("categoriaDocumento","ideCategoriaDocumento", JoinType.LEFT_OUTER_JOIN)
				.createAlias("localizacaoGeografica.ideClassificacaoSecao","ideClassificacaoSecao", JoinType.LEFT_OUTER_JOIN);
		
		criteriaArquivoProcesso.add(Restrictions.eq("ideProcesso.ideProcesso", ideProcesso));
		criteriaArquivoProcesso.add(Restrictions.isNull("ideNotificacao"));
		criteriaArquivoProcesso.addOrder(Order.asc("this.dtcCriacao"));
		return arquivoProcessoDAO.listarPorCriteria(criteriaArquivoProcesso);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ArquivoProcesso> listaArquivoProcessoPorIdeProcessoPorNotificacao(Integer ideProcesso)  {
		return carregarArquivosApensadosNaNotificacao(ideProcesso);
	}

	private Collection<ArquivoProcesso> carregarArquivosApensadosNaNotificacao(Integer ideProcesso)  {
		
		DetachedCriteria criteriaArquivoProcesso = DetachedCriteria.forClass(ArquivoProcesso.class)
			.createAlias("ideProcesso","p", JoinType.INNER_JOIN)
			.createAlias("ideNotificacao","n", JoinType.INNER_JOIN)
			.createAlias("categoriaDocumento","cd", JoinType.LEFT_OUTER_JOIN)
			.createAlias("localizacaoGeografica","lg", JoinType.LEFT_OUTER_JOIN)
			
			.add(Restrictions.eq("p.ideProcesso", ideProcesso))
			
			.addOrder(Order.asc("this.dtcCriacao"))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideArquivoProcesso"),"ideArquivoProcesso")
				.add(Projections.property("dscCaminhoArquivo"),"dscCaminhoArquivo")
				.add(Projections.property("dtcCriacao"),"dtcCriacao")
				.add(Projections.property("indExcluido"),"indExcluido")
				.add(Projections.property("dtcExclusao"),"dtcExclusao")
				.add(Projections.property("dtcAlteracao"),"dtcAlteracao")
				.add(Projections.property("dscArquivoProcesso"),"dscArquivoProcesso")
				.add(Projections.property("p.ideProcesso"),"ideProcesso.ideProcesso")
				.add(Projections.property("lg.ideLocalizacaoGeografica"),"localizacaoGeografica.ideLocalizacaoGeografica")
			)
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ArquivoProcesso.class))
		;
		
		return arquivoProcessoDAO.listarPorCriteria(criteriaArquivoProcesso);
	}
	
	private ArquivoProcesso carregarUltimoParecer(Integer ideProcesso) {
		return carregarUltimaByCategoria(ideProcesso,CategoriaDocumentoEnum.PARECER);
	}

	private ArquivoProcesso carregarUltimoCertificado(Integer ideProcesso) {
		return carregarUltimaByCategoria(ideProcesso,CategoriaDocumentoEnum.CERTIFICADO);
	}
	
	private ArquivoProcesso carregarUltimaPortaria(Integer ideProcesso)  {
		return carregarUltimaByCategoria(ideProcesso,CategoriaDocumentoEnum.PORTARIA);
	}
	
	private ArquivoProcesso carregarUltimaPortariaCertificado(Integer ideProcesso) {
		return carregarUltimaByCategoria(ideProcesso,CategoriaDocumentoEnum.PORTARIA_E_CERTIFICADO);
	}
	
	public Collection<ArquivoProcesso> listarArquivoProcessoPorProcesso(Integer ideProcesso) {
		DetachedCriteria criteriaArquivoProcesso = DetachedCriteria.forClass(ArquivoProcesso.class)
			.createAlias("ideProcesso","proc", JoinType.INNER_JOIN)
			.createAlias("localizacaoGeografica","locGeo", JoinType.INNER_JOIN)
			.createAlias("locGeo.ideSistemaCoordenada","sis", JoinType.LEFT_OUTER_JOIN)
			.createAlias("locGeo.ideClassificacaoSecao","secao", JoinType.LEFT_OUTER_JOIN)
			.createAlias("categoriaDocumento","catDoc", JoinType.INNER_JOIN)
			.createAlias("motivoNotificacao","motivo", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideNotificacao","not", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideImovel","imov", JoinType.LEFT_OUTER_JOIN)
			.createAlias("imov.imovelRural","imovRural", JoinType.LEFT_OUTER_JOIN)
	
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideArquivoProcesso"),"ideArquivoProcesso")
				.add(Projections.property("dscCaminhoArquivo"),"dscCaminhoArquivo")
				.add(Projections.property("dtcCriacao"),"dtcCriacao")					
				.add(Projections.property("indExcluido"),"indExcluido")					
				.add(Projections.property("dtcExclusao"),"dtcExclusao")					
				.add(Projections.property("dtcAlteracao"),"dtcAlteracao")					
				.add(Projections.property("dscArquivoProcesso"),"dscArquivoProcesso")
				
				.add(Projections.property("proc.ideProcesso"),"ideProcesso.ideProcesso")
				
				.add(Projections.property("locGeo.ideLocalizacaoGeografica"),"localizacaoGeografica.ideLocalizacaoGeografica")
				.add(Projections.property("locGeo.desLocalizacaoGeografica"),"localizacaoGeografica.desLocalizacaoGeografica")
				
				.add(Projections.property("sis.ideSistemaCoordenada"),"localizacaoGeografica.ideSistemaCoordenada.ideSistemaCoordenada")
				.add(Projections.property("sis.nomSistemaCoordenada"),"localizacaoGeografica.ideSistemaCoordenada.nomSistemaCoordenada")
				.add(Projections.property("sis.srid"),"localizacaoGeografica.ideSistemaCoordenada.srid")
				
				.add(Projections.property("secao.ideClassificacaoSecao"),"localizacaoGeografica.ideClassificacaoSecao.ideClassificacaoSecao")
				.add(Projections.property("secao.nomClassificacaoSecao"),"localizacaoGeografica.ideClassificacaoSecao.nomClassificacaoSecao")
				
				.add(Projections.property("catDoc.ideCategoriaDocumento"),"categoriaDocumento.ideCategoriaDocumento")
				.add(Projections.property("catDoc.nomCategoria"),"categoriaDocumento.nomCategoria")
				
				.add(Projections.property("motivo.ideMotivoNotificacao"),"motivoNotificacao.ideMotivoNotificacao")
				.add(Projections.property("motivo.nomMotivoNotificacao"),"motivoNotificacao.nomMotivoNotificacao")
				
				.add(Projections.property("not.ideNotificacao"),"ideNotificacao.ideNotificacao")
				.add(Projections.property("not.dscNotificacao"),"ideNotificacao.dscNotificacao")
				.add(Projections.property("not.numNotificacao"),"ideNotificacao.numNotificacao")
				
				.add(Projections.property("imov.ideImovel"),"ideImovel.ideImovel")
				.add(Projections.property("imovRural.ideImovelRural"),"ideImovel.ideImovelRural.ideImovelRural")
				.add(Projections.property("imovRural.desDenominacao"),"ideImovel.ideImovelRural.desDenominacao")
				
			).setResultTransformer(new AliasToNestedBeanResultTransformer(ArquivoProcesso.class))
		
			.add(Restrictions.eq("proc.ideProcesso", ideProcesso))
			.add(Restrictions.eq("catDoc.ideCategoriaDocumento", CategoriaDocumentoEnum.RESPOSTA.getId()))
			
			.addOrder(Order.asc("this.dtcCriacao"));
	
		return this.arquivoProcessoDAO.listarPorCriteria(criteriaArquivoProcesso);
	}

	private ArquivoProcesso carregarUltimaByCategoria(Integer ideProcesso,CategoriaDocumentoEnum categoria) {
		DetachedCriteria criteriaArquivoProcesso = DetachedCriteria.forClass(ArquivoProcesso.class)
				.createAlias("ideProcesso","processo", JoinType.INNER_JOIN)				
				.createAlias("categoriaDocumento","ideCategoriaDocumento", JoinType.INNER_JOIN,Restrictions.eq("ideCategoriaDocumento.ideCategoriaDocumento", categoria.getId()));
		
		criteriaArquivoProcesso.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideArquivoProcesso"),"ideArquivoProcesso")
					.add(Projections.property("dscArquivoProcesso"),"dscArquivoProcesso")
					.add(Projections.property("dscCaminhoArquivo"),"dscCaminhoArquivo")
					.add(Projections.property("dtcCriacao"),"dtcCriacao")					
					.add(Projections.property("processo.ideProcesso"),"ideProcesso.ideProcesso")
					.add(Projections.property("ideCategoriaDocumento.ideCategoriaDocumento"),"categoriaDocumento.ideCategoriaDocumento")
					.add(Projections.property("ideCategoriaDocumento.nomCategoria"),"categoriaDocumento.nomCategoria")
		);
		
		criteriaArquivoProcesso.add(Restrictions.eq("processo.ideProcesso", ideProcesso));
		criteriaArquivoProcesso.addOrder(Order.asc("this.dtcCriacao"));
		
		DetachedCriteria subCriteria = DetachedCriteria.forClass(ArquivoProcesso.class,"ultimo")
				.createAlias("ultimo.ideProcesso", "processoUlt")
				.createAlias("ultimo.categoriaDocumento","ideCategoriaDocumento", JoinType.INNER_JOIN,Restrictions.eq("ideCategoriaDocumento.ideCategoriaDocumento", categoria.getId()));
		subCriteria.setProjection(Projections.projectionList()
				.add(Projections.max("ultimo.dtcCriacao")))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(ArquivoProcesso.class));
		subCriteria.add(Restrictions.eqProperty("processoUlt.ideProcesso", "processo.ideProcesso"));
		
		criteriaArquivoProcesso.add(Subqueries.propertyEq("this.dtcCriacao", subCriteria));
		
		criteriaArquivoProcesso.setResultTransformer(new AliasToNestedBeanResultTransformer(ArquivoProcesso.class));
		
		return this.arquivoProcessoDAO.buscarPorCriteria(criteriaArquivoProcesso);
	}
	
	public Collection<ArquivoProcesso> listaArquivoProcessoPorIdeProcessoAndIdePessoa(Integer ideProcesso, Integer idePessoa) { 
		
		DetachedCriteria criteriaArquivoProcesso = DetachedCriteria.forClass(ArquivoProcesso.class)
				.createAlias("localizacaoGeografica", "localizacaoGeografica",JoinType.LEFT_OUTER_JOIN)
				.createAlias("motivoNotificacao", "motivoNotificacao",JoinType.LEFT_OUTER_JOIN);
		
		criteriaArquivoProcesso.add(Restrictions.eq("ideProcesso.ideProcesso", ideProcesso));
		criteriaArquivoProcesso.add(Restrictions.eq("idePessoaUpload.idePessoa", idePessoa));
		criteriaArquivoProcesso.add(Restrictions.isNull("localizacaoGeografica.ideLocalizacaoGeografica"));		
		
		Collection<ArquivoProcesso> list = arquivoProcessoDAO.listarPorCriteria(criteriaArquivoProcesso,Order.desc("dtcCriacao"));
		
		for (ArquivoProcesso ap : list) {
			Hibernate.initialize(ap.getIdePessoaUpload());
		}
		
		return list;
	}

	public ArquivoProcesso carregarArquivoShape(Notificacao notificacao, Imovel imovel) {
		
		DetachedCriteria criteria = getCriteriaCarregarArquivoShape(notificacao);
		
		if(!Util.isNull(imovel)) {
			criteria.add(Restrictions.eq("ideImovel.ideImovel", imovel.getIdeImovel()));
		}
		
		criteria
			.add(Restrictions.eq("mt.ideMotivoNotificacao", notificacao.getMotivoNotificacaoSelecionado().getIdeMotivoNotificacao()))
			.add(Restrictions.eq("motivoNotificacao.ideMotivoNotificacao", notificacao.getMotivoNotificacaoSelecionado().getIdeMotivoNotificacao()))
		;
		
		List<ArquivoProcesso> listArquivoProcesso = arquivoProcessoDAO.listarPorCriteria(criteria);
		if(!Util.isNullOuVazio(listArquivoProcesso)) {
			return listArquivoProcesso.get(0);
		}else {
			return null;
		}
	}
	
	public ArquivoProcesso carregarArquivoShape(Notificacao notificacao, MotivoNotificacaoEnum motivoEnum, Imovel imovel) {
		
		DetachedCriteria criteria = getCriteriaCarregarArquivoShape(notificacao);
		
		if(!Util.isNull(imovel)) {
			criteria.add(Restrictions.sqlRestriction("(this_.ide_imovel="+imovel.getIdeImovel()+" or this_.ide_imovel is null)"));
		}
		
		criteria
			.add(Restrictions.eq("mt.ideMotivoNotificacao", motivoEnum.getId()))
			.add(Restrictions.eq("motivoNotificacao.ideMotivoNotificacao", motivoEnum.getId()))
		;
		
		List<ArquivoProcesso> listArquivoProcesso = arquivoProcessoDAO.listarPorCriteria(criteria);
		if(!Util.isNullOuVazio(listArquivoProcesso)) {
			return listArquivoProcesso.get(0);
		}else {
			return null;
		}
		
	}
	
	public List<ArquivoProcesso> carregarListaArquivoShapeComDadoGeografico(Notificacao notificacao) {
		
		DetachedCriteria criteria = getCriteriaCarregarArquivoShape(notificacao);
		criteria
			.createAlias("lg.dadoGeograficoCollection","dg",JoinType.INNER_JOIN)
			.add(Restrictions.eq("mt.indEnvioShape", true))
		;
		
		return arquivoProcessoDAO.listarPorCriteria(criteria);
		
	}

	private DetachedCriteria getCriteriaCarregarArquivoShape(Notificacao notificacao) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ArquivoProcesso.class);
		
		criteria
			.createAlias("ideNotificacao","not", JoinType.INNER_JOIN)
			
			.createAlias("not.notificacaoMotivoNotificacaoCollection", "nmn", JoinType.INNER_JOIN)
			.createAlias("nmn.ideMotivoNotificacao", "mt", JoinType.INNER_JOIN)
			
			.createAlias("not.ideProcesso", "p", JoinType.INNER_JOIN)
			.createAlias("p.ideRequerimento", "req",JoinType.INNER_JOIN)
			
			.createAlias("categoriaDocumento", "cd",JoinType.LEFT_OUTER_JOIN)
			.createAlias("localizacaoGeografica", "lg",JoinType.LEFT_OUTER_JOIN)
			
			.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideArquivoProcesso"),"ideArquivoProcesso")
					.add(Projections.property("dscCaminhoArquivo"),"dscCaminhoArquivo")
					.add(Projections.property("dtcCriacao"),"dtcCriacao")
					.add(Projections.property("indExcluido"),"indExcluido")
					.add(Projections.property("dtcExclusao"),"dtcExclusao")
					.add(Projections.property("dtcAlteracao"),"dtcAlteracao")
					.add(Projections.property("dscArquivoProcesso"),"dscArquivoProcesso")
					.add(Projections.property("p.ideProcesso"),"ideProcesso.ideProcesso")
					.add(Projections.property("req.ideRequerimento"),"ideProcesso.ideRequerimento.ideRequerimento")
					.add(Projections.property("idePessoaUpload"),"idePessoaUpload")
					.add(Projections.property("localizacaoGeografica"),"localizacaoGeografica")
					.add(Projections.property("cd.ideCategoriaDocumento"),"categoriaDocumento.ideCategoriaDocumento")
					.add(Projections.property("motivoNotificacao"),"motivoNotificacao")
					.add(Projections.property("ideNotificacao"),"ideNotificacao")
					.add(Projections.property("ideImovel.ideImovel"),"ideImovel.ideImovel")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ArquivoProcesso.class))
		;
		
		criteria
			.add(Restrictions.eq("not.ideNotificacao", notificacao.getIdeNotificacao()))
		;
		
		return criteria;
	}
	public List<ArquivoProcesso> listarArquivosApensosPublicosPorIdeProcesso(Integer ideProcesso) {
		List<ArquivoProcesso> list = new ArrayList<ArquivoProcesso>();
		ArquivoProcesso portaria = this.carregarUltimaPortaria(ideProcesso);
		if(!Util.isNull(portaria)){
			list.add(portaria);
		}
		ArquivoProcesso certificado = this.carregarUltimoCertificado(ideProcesso);
		if(!Util.isNull(certificado)){
			list.add(certificado);
		}
		ArquivoProcesso parecer = this.carregarUltimoParecer(ideProcesso);
		if(!Util.isNull(parecer)){
			list.add(parecer);
		}
		ArquivoProcesso portariaCertificado = this.carregarUltimaPortariaCertificado(ideProcesso);
		if(!Util.isNull(portariaCertificado)){
			list.add(portariaCertificado);
		}
		
		list.addAll(this.listarArquivoProcessoPorProcesso(ideProcesso));
		
		return list;
	}
	
	public List<ArquivoProcesso> listarArquivosProcessoPorNotificacao(Notificacao notificacao) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ArquivoProcesso.class)
				.createAlias("ideProcesso","p", JoinType.LEFT_OUTER_JOIN)
				.createAlias("idePessoaUpload","pu", JoinType.LEFT_OUTER_JOIN)
				.createAlias("localizacaoGeografica","lg", JoinType.LEFT_OUTER_JOIN)
				.createAlias("categoriaDocumento","cd", JoinType.LEFT_OUTER_JOIN)
				.createAlias("motivoNotificacao","mn", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideNotificacao","not", JoinType.INNER_JOIN)
				
				.setProjection(
						Projections.projectionList()
						.add(Projections.property("ideArquivoProcesso"), "ideArquivoProcesso")
						.add(Projections.property("dscCaminhoArquivo"), "dscCaminhoArquivo")
						.add(Projections.property("dtcCriacao"), "dtcCriacao")
						.add(Projections.property("indExcluido"), "indExcluido")
						.add(Projections.property("dtcExclusao"), "dtcExclusao")
						.add(Projections.property("dtcAlteracao"), "dtcAlteracao")
						.add(Projections.property("dscArquivoProcesso"), "dscArquivoProcesso")
						
						.add(Projections.property("p.ideProcesso"), "ideProcesso.ideProcesso")
						
						.add(Projections.property("pu.idePessoa"), "idePessoaUpload.idePessoa")
						
						.add(Projections.property("lg.ideLocalizacaoGeografica"), "localizacaoGeografica.ideLocalizacaoGeografica")
						
						.add(Projections.property("cd.ideCategoriaDocumento"), "categoriaDocumento.ideCategoriaDocumento")
						
						.add(Projections.property("mn.ideMotivoNotificacao"), "motivoNotificacao.ideMotivoNotificacao")
						
						.add(Projections.property("not.ideNotificacao"), "ideNotificacao.ideNotificacao")
						.add(Projections.property("not.numNotificacao"), "ideNotificacao.numNotificacao")
						
				).setResultTransformer(new AliasToNestedBeanResultTransformer(ArquivoProcesso.class))
				
				.add(Restrictions.eq("not.ideNotificacao", notificacao.getIdeNotificacao()))
				.add(Restrictions.eq("cd.ideCategoriaDocumento", CategoriaDocumentoEnum.DOCUMENTO_DA_NOTIFICACAO.getId()));

		List<ArquivoProcesso> arquivosProcessos = arquivoProcessoDAO.listarPorCriteria( criteria);
		
		if(!Util.isLazyInitExcepOuNull(arquivosProcessos)){
			for(ArquivoProcesso arquivoProcesso : arquivosProcessos){
				arquivoProcesso.setDscConfirmada(true);
			}
		}
		return arquivosProcessos;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirPorLocalizacaoGeografica(Integer ideLocGeo) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("ideLocalizacaoGeografica", ideLocGeo);
			
			String delSQL = "DELETE FROM arquivo_processo WHERE ide_localizacao_geografica = :ideLocalizacaoGeografica";
			arquivoProcessoDAO.executarNativeQuery(delSQL, params);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public ArquivoProcesso carregarUltimaPortariaCertificado(Integer ideProcesso, boolean indExcluido) {
		return carregarUltimaByCategoriaAndIndExcluido(ideProcesso,CategoriaDocumentoEnum.PORTARIA_E_CERTIFICADO, indExcluido);
	}
	
	private ArquivoProcesso carregarUltimaByCategoriaAndIndExcluido(Integer ideProcesso,CategoriaDocumentoEnum categoria, boolean indExcluido) {
		DetachedCriteria criteriaArquivoProcesso = DetachedCriteria.forClass(ArquivoProcesso.class)
				.createAlias("ideProcesso","processo", JoinType.INNER_JOIN)				
				.createAlias("categoriaDocumento","ideCategoriaDocumento", JoinType.INNER_JOIN,Restrictions.eq("ideCategoriaDocumento.ideCategoriaDocumento", categoria.getId()));
		
		criteriaArquivoProcesso.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideArquivoProcesso"),"ideArquivoProcesso")
					.add(Projections.property("dscArquivoProcesso"),"dscArquivoProcesso")
					.add(Projections.property("dscCaminhoArquivo"),"dscCaminhoArquivo")
					.add(Projections.property("dtcCriacao"),"dtcCriacao")					
					.add(Projections.property("processo.ideProcesso"),"ideProcesso.ideProcesso")
					.add(Projections.property("ideCategoriaDocumento.ideCategoriaDocumento"),"categoriaDocumento.ideCategoriaDocumento")
					.add(Projections.property("ideCategoriaDocumento.nomCategoria"),"categoriaDocumento.nomCategoria")
		);
		
		criteriaArquivoProcesso.add(Restrictions.eq("processo.ideProcesso", ideProcesso));
		criteriaArquivoProcesso.add(Restrictions.eq("this.indExcluido", indExcluido));
		criteriaArquivoProcesso.addOrder(Order.asc("this.dtcCriacao"));
		
		DetachedCriteria subCriteria = DetachedCriteria.forClass(ArquivoProcesso.class,"ultimo")
				.createAlias("ultimo.ideProcesso", "processoUlt")
				.createAlias("ultimo.categoriaDocumento","ideCategoriaDocumento", JoinType.INNER_JOIN,Restrictions.eq("ideCategoriaDocumento.ideCategoriaDocumento", categoria.getId()));
		subCriteria.setProjection(Projections.projectionList()
				.add(Projections.max("ultimo.dtcCriacao")))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(ArquivoProcesso.class));
		subCriteria.add(Restrictions.eqProperty("processoUlt.ideProcesso", "processo.ideProcesso"));
		
		criteriaArquivoProcesso.add(Subqueries.propertyEq("this.dtcCriacao", subCriteria));
		
		criteriaArquivoProcesso.setResultTransformer(new AliasToNestedBeanResultTransformer(ArquivoProcesso.class));
		
		return this.arquivoProcessoDAO.buscarPorCriteria(criteriaArquivoProcesso);
	}
}