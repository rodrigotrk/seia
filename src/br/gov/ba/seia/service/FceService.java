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
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.StatusProcessoAtoDAOImpl;
import br.gov.ba.seia.entity.AnaliseTecnica;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoUnico;
import br.gov.ba.seia.enumerator.DadoOrigemEnum;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceService {

	@Inject
	private IDAO<Fce> fceIDAO;
	
	@EJB
	private StatusProcessoAtoDAOImpl documentoAtoDAOImpl;

	/**
	 * @
	 * @INFO Busca a Fce pelo ID do requerimento e Documento Obrigatório
	 * @return Fce
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Fce buscarFceByIdReqAndDoc(Requerimento requerimento, DocumentoObrigatorio documentoObrigatorio, DadoOrigemEnum DadoOrigemEnum)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Fce.class)

			.createAlias("ideDocumentoObrigatorio", "doc")
			.createAlias("ideRequerimento", "req")
			.createAlias("ideDadoOrigem", "orig")
			.createAlias("ideAnaliseTecnica", "ana", JoinType.LEFT_OUTER_JOIN)
			.createAlias("idePessoaFisica", "func", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideProcessoReenquadramento", "pre", JoinType.LEFT_OUTER_JOIN)
			.createAlias("fceOutorgaLocalizacaoGeografica", "folg", JoinType.LEFT_OUTER_JOIN)
			.add(Restrictions.eq("ideRequerimento.ideRequerimento", requerimento.getIdeRequerimento()))
			.add(Restrictions.eq("ideDocumentoObrigatorio.ideDocumentoObrigatorio", documentoObrigatorio.getIdeDocumentoObrigatorio()))
			.add(Restrictions.eq("ideDadoOrigem.ideDadoOrigem", DadoOrigemEnum.getId()))
			;
		return fceIDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Fce buscarFceByIdeRequerimento(RequerimentoUnico requerimentoUnico)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Fce.class);
		criteria.add(Restrictions.eq("ideRequerimento", requerimentoUnico.getRequerimento()));
		return fceIDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Fce> listarFceByIdeRequerimento(Requerimento requerimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Fce.class)
				.createAlias("ideDocumentoObrigatorio", "doc")
				.createAlias("ideRequerimento", "req")
				.createAlias("ideDadoOrigem", "orig")
				.add(Restrictions.eq("orig.ideDadoOrigem", DadoOrigemEnum.REQUERIMENTO.getId()))
				.add(Restrictions.eq("req.ideRequerimento", requerimento.getIdeRequerimento()))
				.setProjection(Projections.projectionList()
						.add(Projections.property("ideFce"), "ideFce")
						.add(Projections.property("dtcCriacao"), "dtcCriacao")
						.add(Projections.property("doc.ideDocumentoObrigatorio"), "ideDocumentoObrigatorio.ideDocumentoObrigatorio")
						.add(Projections.property("doc.nomDocumentoObrigatorio"), "ideDocumentoObrigatorio.nomDocumentoObrigatorio")
						.add(Projections.property("req.ideRequerimento"), "ideRequerimento.ideRequerimento")
						.add(Projections.property("orig.ideDadoOrigem"), "ideDadoOrigem.ideDadoOrigem")
						)
						.setResultTransformer(new AliasToNestedBeanResultTransformer(Fce.class));
		return fceIDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Fce> listarFcePor(Integer ideProcesso, DadoOrigemEnum...listDadoOrigem)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Fce.class)
			.createAlias("ideDocumentoObrigatorio", "doc", JoinType.INNER_JOIN)
			.createAlias("ideDadoOrigem", "orig", JoinType.INNER_JOIN)
			.createAlias("ideAnaliseTecnica", "analiseTecnica", JoinType.LEFT_OUTER_JOIN)
			.createAlias("idePessoaFisica", "func", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideProcessoReenquadramento", "pre", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideRequerimento", "req", JoinType.INNER_JOIN)
			.createAlias("req.processoCollection", "pro", JoinType.INNER_JOIN)
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideFce"), "ideFce")
				.add(Projections.property("dtcCriacao"), "dtcCriacao")
				.add(Projections.property("indConcluido"), "indConcluido")
				.add(Projections.property("doc.ideDocumentoObrigatorio"), "ideDocumentoObrigatorio.ideDocumentoObrigatorio")
				.add(Projections.property("doc.nomDocumentoObrigatorio"), "ideDocumentoObrigatorio.nomDocumentoObrigatorio")
				.add(Projections.property("req.ideRequerimento"), "ideRequerimento.ideRequerimento")
				.add(Projections.property("orig.ideDadoOrigem"), "ideDadoOrigem.ideDadoOrigem")
				.add(Projections.property("analiseTecnica.ideAnaliseTecnica"), "ideAnaliseTecnica.ideAnaliseTecnica")
				.add(Projections.property("func.idePessoaFisica"), "idePessoaFisica.idePessoaFisica")
				.add(Projections.property("pre.ideProcessoReenquadramento"), "ideProcessoReenquadramento.ideProcessoReenquadramento")
			).setResultTransformer(new AliasToNestedBeanResultTransformer(Fce.class))
			
			.add(Restrictions.eq("pro.ideProcesso", ideProcesso));
			
			if(!Util.isNullOuVazio(listDadoOrigem)) {
				if(listDadoOrigem.length == 1) {
					criteria.add(Restrictions.eq("orig.ideDadoOrigem", listDadoOrigem[0].getId()));
					
				} else if(listDadoOrigem.length == 2) {
					criteria.add(Restrictions.or(
						Restrictions.eq("orig.ideDadoOrigem", listDadoOrigem[0].getId()), 
						Restrictions.eq("orig.ideDadoOrigem", listDadoOrigem[1].getId())));
					
				} else if(listDadoOrigem.length == 3) {
					criteria.add(
							Restrictions.or(
								Restrictions.eq("orig.ideDadoOrigem", listDadoOrigem[0].getId()), 
								Restrictions.or(
									Restrictions.eq("orig.ideDadoOrigem", listDadoOrigem[1].getId()),
									Restrictions.eq("orig.ideDadoOrigem", listDadoOrigem[2].getId()))));
				}
			}
		
		return fceIDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Fce> listarFcePorProcessoReenquadramento(Integer ideProcesso)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Fce.class)
			.createAlias("ideDocumentoObrigatorio", "doc", JoinType.INNER_JOIN)
			.createAlias("ideRequerimento", "req", JoinType.INNER_JOIN)
			.createAlias("req.processoCollection", "pro", JoinType.INNER_JOIN)
			.createAlias("ideDadoOrigem", "orig", JoinType.INNER_JOIN)
			.createAlias("ideAnaliseTecnica", "analiseTecnica", JoinType.LEFT_OUTER_JOIN)
			.createAlias("idePessoaFisica", "func", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideProcessoReenquadramento", "pre", JoinType.LEFT_OUTER_JOIN)
			.add(Restrictions.eq("pro.ideProcesso", ideProcesso))
			.add(Restrictions.eq("orig.ideDadoOrigem", DadoOrigemEnum.REENQUADRAMENTO.getId()))
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideFce"), "ideFce")
				.add(Projections.property("dtcCriacao"), "dtcCriacao")
				.add(Projections.property("indConcluido"), "indConcluido")
				.add(Projections.property("doc.ideDocumentoObrigatorio"), "ideDocumentoObrigatorio.ideDocumentoObrigatorio")
				.add(Projections.property("doc.nomDocumentoObrigatorio"), "ideDocumentoObrigatorio.nomDocumentoObrigatorio")
				.add(Projections.property("req.ideRequerimento"), "ideRequerimento.ideRequerimento")
				.add(Projections.property("orig.ideDadoOrigem"), "ideDadoOrigem.ideDadoOrigem")
				.add(Projections.property("analiseTecnica.ideAnaliseTecnica"), "ideAnaliseTecnica.ideAnaliseTecnica")
				.add(Projections.property("func.idePessoaFisica"), "idePessoaFisica.idePessoaFisica")
				.add(Projections.property("pre.ideProcessoReenquadramento"), "ideProcessoReenquadramento.ideProcessoReenquadramento")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Fce.class));
		
		return fceIDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Fce> listarFceComExcluido(AnaliseTecnica analiseTecnica, Boolean indAprovado)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Fce.class)
			.createAlias("ideDocumentoObrigatorio", "doc", JoinType.INNER_JOIN)
			.createAlias("ideRequerimento", "req", JoinType.INNER_JOIN)
			.createAlias("req.processoCollection", "pro", JoinType.INNER_JOIN)
			.createAlias("ideDadoOrigem", "orig", JoinType.INNER_JOIN)
			.createAlias("ideAnaliseTecnica", "analiseTecnica", JoinType.LEFT_OUTER_JOIN)
			.createAlias("idePessoaFisica", "func", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideProcessoReenquadramento", "pre", JoinType.LEFT_OUTER_JOIN)
			.add(Restrictions.eq("analiseTecnica.ideAnaliseTecnica", analiseTecnica.getIdeAnaliseTecnica()));
			if(!Util.isNull(indAprovado)){
				criteria.add(Restrictions.eq("analiseTecnica.indAprovado", indAprovado));
			}
			criteria.add(Restrictions.eq("orig.ideDadoOrigem", DadoOrigemEnum.TECNICO.getId()))
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideFce"), "ideFce")
				.add(Projections.property("dtcCriacao"), "dtcCriacao")
				.add(Projections.property("indConcluido"), "indConcluido")
				.add(Projections.property("doc.ideDocumentoObrigatorio"), "ideDocumentoObrigatorio.ideDocumentoObrigatorio")
				.add(Projections.property("doc.nomDocumentoObrigatorio"), "ideDocumentoObrigatorio.nomDocumentoObrigatorio")
				.add(Projections.property("req.ideRequerimento"), "ideRequerimento.ideRequerimento")
				.add(Projections.property("orig.ideDadoOrigem"), "ideDadoOrigem.ideDadoOrigem")
				.add(Projections.property("analiseTecnica.ideAnaliseTecnica"), "ideAnaliseTecnica.ideAnaliseTecnica")
				.add(Projections.property("analiseTecnica.indAprovado"), "ideAnaliseTecnica.indAprovado")
				.add(Projections.property("func.idePessoaFisica"), "idePessoaFisica.idePessoaFisica")
				.add(Projections.property("pre.ideProcessoReenquadramento"), "ideProcessoReenquadramento.ideProcessoReenquadramento")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Fce.class));
		
		return fceIDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Fce> listarFcePor(AnaliseTecnica analiseTecnica, Boolean indAprovado)  {
		
		//Adicionar novos joins sempre no final para não quebrar a consulta
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Fce.class)
			.createAlias("ideRequerimento", "req", JoinType.INNER_JOIN)
			.createAlias("req.processoCollection", "pro", JoinType.INNER_JOIN)
			.createAlias("pro.processoAtoCollection", "pa", JoinType.INNER_JOIN)
			.createAlias("pro.analiseTecnicaCollection", "a", JoinType.INNER_JOIN)
			.createAlias("pro.processoReenquadramentoCollection", "pr", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideDocumentoObrigatorio", "doc", JoinType.INNER_JOIN)
			.createAlias("doc.documentoAtoCollection", "da", JoinType.INNER_JOIN)
			.createAlias("da.enquadramentoDocumentoAtoCollection", "eda", JoinType.INNER_JOIN)
			.createAlias("eda.enquadramento", "enq", JoinType.INNER_JOIN)
			.createAlias("ideDadoOrigem", "orig", JoinType.INNER_JOIN)
			.createAlias("idePessoaFisica", "func", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideProcessoReenquadramento", "pre", JoinType.LEFT_OUTER_JOIN)
			
			.add(Restrictions.eq("a.ideAnaliseTecnica", analiseTecnica.getIdeAnaliseTecnica()))
			.add(Restrictions.eq("pa.indExcluido", false))
			.add(Restrictions.eq("orig.ideDadoOrigem", DadoOrigemEnum.TECNICO.getId()))
			.add(
				Restrictions.or(
					Restrictions.eqProperty("enq.ideRequerimento", "req.ideRequerimento"), 
					Restrictions.eqProperty("enq.ideProcessoReenquadramento", "pr.ideProcessoReenquadramento"))
			)
			
			.add(
				Restrictions.or(
					Restrictions.and(
						Restrictions.eqProperty("da.ideAtoAmbiental", "pa.atoAmbiental"),
						Restrictions.or(Restrictions.and(Restrictions.isNull("da.ideTipologia"), Restrictions.isNull("pa.tipologia")),
								Restrictions.and(Restrictions.isNotNull("da.ideTipologia"), Restrictions.isNull("pa.tipologia")))
					),
					
					Restrictions.sqlRestriction(
						"CASE "
							+ "WHEN da7_.ide_tipologia <> " + TipologiaEnum.CONSTRUCAO_LINHA.getId() + " "
							+ "THEN (da7_.ide_ato_ambiental = pa3_.ide_ato_ambiental AND da7_.ide_tipologia=pa3_.ide_tipologia) "
							+ "ELSE (da7_.ide_ato_ambiental = pa3_.ide_ato_ambiental) "
						+ "END")
				)
			);
			
			if(!Util.isNull(indAprovado)){
				criteria.add(Restrictions.eq("a.indAprovado", indAprovado));
			}
			
			criteria.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("ideFce"), "ideFce")
				.add(Projections.property("dtcCriacao"), "dtcCriacao")
				.add(Projections.property("indConcluido"), "indConcluido")
				.add(Projections.property("doc.ideDocumentoObrigatorio"), "ideDocumentoObrigatorio.ideDocumentoObrigatorio")
				.add(Projections.property("doc.nomDocumentoObrigatorio"), "ideDocumentoObrigatorio.nomDocumentoObrigatorio")
				.add(Projections.property("doc.indAtivo"), "ideDocumentoObrigatorio.indAtivo")
				.add(Projections.property("req.ideRequerimento"), "ideRequerimento.ideRequerimento")
				.add(Projections.property("orig.ideDadoOrigem"), "ideDadoOrigem.ideDadoOrigem")
				.add(Projections.property("a.ideAnaliseTecnica"), "ideAnaliseTecnica.ideAnaliseTecnica")
				.add(Projections.property("a.ideProcesso"), "ideAnaliseTecnica.ideProcesso")
				.add(Projections.property("a.indAprovado"), "ideAnaliseTecnica.indAprovado")
				.add(Projections.property("func.idePessoaFisica"), "idePessoaFisica.idePessoaFisica")
				.add(Projections.property("pre.ideProcessoReenquadramento"), "ideProcessoReenquadramento.ideProcessoReenquadramento")
				.add(Projections.property("pa.ideProcessoAto"), "processoAto.ideProcessoAto")
				.add(Projections.property("pro.ideProcesso"), "processoAto.processo.ideProcesso")
			))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Fce.class));
		
		return fceIDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Fce> listarFceParaASV(AnaliseTecnica analiseTecnica, Boolean indAprovado)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Fce.class)
				.createAlias("ideRequerimento", "req", JoinType.INNER_JOIN)
				.createAlias("req.processoCollection", "pro", JoinType.INNER_JOIN)
				.createAlias("pro.processoAtoCollection", "pa", JoinType.INNER_JOIN)
				.createAlias("pro.analiseTecnicaCollection", "a", JoinType.INNER_JOIN)
				.createAlias("pro.processoReenquadramentoCollection", "pr", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideDocumentoObrigatorio", "doc", JoinType.INNER_JOIN)
				.createAlias("doc.documentoAtoCollection", "da", JoinType.INNER_JOIN)
				.createAlias("da.enquadramentoDocumentoAtoCollection", "eda", JoinType.INNER_JOIN)
				.createAlias("eda.enquadramento", "enq", JoinType.INNER_JOIN)
				.createAlias("ideDadoOrigem", "orig", JoinType.INNER_JOIN)
				.createAlias("idePessoaFisica", "func", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideProcessoReenquadramento", "pre", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("a.ideAnaliseTecnica", analiseTecnica.getIdeAnaliseTecnica()))
				.add(Restrictions.eq("pa.indExcluido", false))
				.add(Restrictions.or(
							Restrictions.and(
									Restrictions.eqProperty("da.ideAtoAmbiental", "pa.atoAmbiental"),
									Restrictions.and(Restrictions.isNull("da.ideTipologia"), Restrictions.isNull("pa.tipologia"))
							)
						,
						
						Restrictions.and(Restrictions.eqProperty("da.ideAtoAmbiental", "pa.atoAmbiental"),
								Restrictions.eqProperty("da.ideTipologia", "pa.tipologia")
						)
					)
				)
				.add(
					Restrictions.or(
						Restrictions.eqProperty("enq.ideRequerimento", "req.ideRequerimento"), 
						Restrictions.eqProperty("enq.ideProcessoReenquadramento", "pr.ideProcessoReenquadramento"))
				);
		
			if(!Util.isNull(indAprovado)){
				criteria.add(Restrictions.eq("a.indAprovado", indAprovado));
			}
			criteria.add(Restrictions.eq("doc.ideDocumentoObrigatorio", DocumentoObrigatorioEnum.FCE_ASV.getId()));
			criteria.add(Restrictions.eq("orig.ideDadoOrigem", DadoOrigemEnum.TECNICO.getId()))
			.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("ideFce"), "ideFce")
				.add(Projections.property("dtcCriacao"), "dtcCriacao")
				.add(Projections.property("indConcluido"), "indConcluido")
				.add(Projections.property("doc.ideDocumentoObrigatorio"), "ideDocumentoObrigatorio.ideDocumentoObrigatorio")
				.add(Projections.property("doc.nomDocumentoObrigatorio"), "ideDocumentoObrigatorio.nomDocumentoObrigatorio")
				.add(Projections.property("doc.indAtivo"), "ideDocumentoObrigatorio.indAtivo")
				.add(Projections.property("req.ideRequerimento"), "ideRequerimento.ideRequerimento")
				.add(Projections.property("orig.ideDadoOrigem"), "ideDadoOrigem.ideDadoOrigem")
				.add(Projections.property("a.ideAnaliseTecnica"), "ideAnaliseTecnica.ideAnaliseTecnica")
				.add(Projections.property("a.indAprovado"), "ideAnaliseTecnica.indAprovado")
				.add(Projections.property("func.idePessoaFisica"), "idePessoaFisica.idePessoaFisica")
				.add(Projections.property("pre.ideProcessoReenquadramento"), "ideProcessoReenquadramento.ideProcessoReenquadramento")
			))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Fce.class));
		
		return fceIDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFce(Fce fce)  {
		fceIDAO.salvarOuAtualizar(fce);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceComFlushEClear(Fce fce)  {
		fceIDAO.sessionFlush();
		fceIDAO.sessionClear();
		fceIDAO.salvarOuAtualizar(fce);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFce(Fce fce)  {
		fceIDAO.remover(fce);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Fce buscarFcePor(AnaliseTecnica analiseTecnica, DocumentoObrigatorio documentoObrigatorio)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Fce.class, "f")
				.createAlias("f.ideDocumentoObrigatorio", "doc", JoinType.INNER_JOIN)
				.createAlias("f.ideAnaliseTecnica", "at", JoinType.INNER_JOIN)
				.add(Restrictions.eq("at.ideAnaliseTecnica", analiseTecnica.getIdeAnaliseTecnica()))
				.add(Restrictions.eq("doc.ideDocumentoObrigatorio", documentoObrigatorio.getIdeDocumentoObrigatorio()));
		return fceIDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void apagarFceByFunction(String sql, Fce fce)  {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("pFce", fce.getIdeFce());
		fceIDAO.buscarPorNativeQuery(sql, params);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Fce buscarFcePorIdeFce(Integer ideFce)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Fce.class, "fce")
				.add(Restrictions.eq("fce.ideFce", ideFce));
		return fceIDAO.buscarPorCriteria(criteria);
	}
	
}