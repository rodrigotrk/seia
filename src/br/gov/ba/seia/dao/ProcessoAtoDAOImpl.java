package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.ControleProcessoAto;
import br.gov.ba.seia.entity.DadoOrigem;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.entity.TransferenciaAmbiental;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.DadoOrigemEnum;
import br.gov.ba.seia.enumerator.StatusProcessoAtoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProcessoAtoDAOImpl {

	@Inject
	private IDAO<ProcessoAto> processoAtoDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ProcessoAto> listarProcessoAtoPor(Integer ideProcesso, Integer ideAtoAmbiental, Integer idePessoaFisica)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcessoAto.class);

		criteria
			.createAlias("processo", "p", JoinType.INNER_JOIN)
			.createAlias("atoAmbiental", "at", JoinType.INNER_JOIN)
			.createAlias("tipologia", "t", JoinType.LEFT_OUTER_JOIN)
			.createAlias("processoAtoIntegranteEquipeCollection", "paie", JoinType.INNER_JOIN)
			.createAlias("paie.ideIntegranteEquipe", "ie", JoinType.INNER_JOIN)
			.createAlias("ie.idePessoaFisica", "fun", JoinType.LEFT_OUTER_JOIN)
	
			.add(Restrictions.eq("p.ideProcesso", ideProcesso))
			/*.add(Restrictions.eq("at.ideAtoAmbiental", ideAtoAmbiental))*/
		;

		if(!Util.isNull(idePessoaFisica)) {
			criteria.add(Restrictions.eq("fun.idePessoaFisica", idePessoaFisica));
		}

		criteria
			.setProjection(Projections.projectionList()
				.add(Projections.groupProperty("ideProcessoAto"), "ideProcessoAto")
				.add(Projections.groupProperty("indExcluido"), "indExcluido")
				.add(Projections.groupProperty("dtcExclusao"), "dtcExclusao")
				.add(Projections.groupProperty("at.ideAtoAmbiental"), "atoAmbiental.ideAtoAmbiental")
				.add(Projections.groupProperty("at.nomAtoAmbiental"), "atoAmbiental.nomAtoAmbiental")
				.add(Projections.groupProperty("t.ideTipologia"), "tipologia.ideTipologia")
				.add(Projections.groupProperty("t.desTipologia"), "tipologia.desTipologia")
				.add(Projections.groupProperty("t.codTipologia"), "tipologia.codTipologia")
				.add(Projections.groupProperty("p.ideProcesso"), "processo.ideProcesso")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ProcessoAto.class))
		;

		return processoAtoDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ProcessoAto> listarProcessoAtoPor(Integer ideProcesso, Integer idePessoaFisica)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcessoAto.class);

		criteria
			.createAlias("processo", "p", JoinType.INNER_JOIN)
			.createAlias("atoAmbiental", "at", JoinType.INNER_JOIN)
			.createAlias("tipologia", "t", JoinType.LEFT_OUTER_JOIN)
			.createAlias("processoAtoIntegranteEquipeCollection", "paie", JoinType.INNER_JOIN)
			.createAlias("paie.ideIntegranteEquipe", "ie", JoinType.INNER_JOIN)
			.createAlias("ie.idePessoaFisica", "fun", JoinType.LEFT_OUTER_JOIN)
		.add(Restrictions.eq("p.ideProcesso", ideProcesso))
		.add(Restrictions.eq("indExcluido", false))
		.add(Restrictions.eq("at.indDeclaratorio",false));

		if(!Util.isNull(idePessoaFisica)) {
			criteria.add(Restrictions.eq("fun.idePessoaFisica", idePessoaFisica));
		}

		criteria
			.setProjection(Projections.projectionList()
					.add(Projections.groupProperty("ideProcessoAto"), "ideProcessoAto")
					.add(Projections.groupProperty("indExcluido"), "indExcluido")
					.add(Projections.groupProperty("dtcExclusao"), "dtcExclusao")
					.add(Projections.groupProperty("at.ideAtoAmbiental"), "atoAmbiental.ideAtoAmbiental")
					.add(Projections.groupProperty("at.nomAtoAmbiental"), "atoAmbiental.nomAtoAmbiental")
					.add(Projections.groupProperty("at.ideTipoAto"), "atoAmbiental.ideTipoAto")
					.add(Projections.groupProperty("at.indPrazoIndeterminado"), "atoAmbiental.indPrazoIndeterminado")
					.add(Projections.groupProperty("t.ideTipologia"), "tipologia.ideTipologia")
					.add(Projections.groupProperty("t.desTipologia"), "tipologia.desTipologia")
					.add(Projections.groupProperty("t.codTipologia"), "tipologia.codTipologia")
					.add(Projections.groupProperty("p.ideProcesso"), "processo.ideProcesso")
				)
		.setResultTransformer(new AliasToNestedBeanResultTransformer(ProcessoAto.class));

		return processoAtoDAO.listarPorCriteria(criteria);
	}

	public List<ProcessoAto> getProcessoAto(ProcessoAto pProcessoAto)  {

		DetachedCriteria criteria = DetachedCriteria.forClass(ProcessoAto.class);

		criteria
		.createAlias("processo", "proc", JoinType.INNER_JOIN)
		.createAlias("atoAmbiental", "ato", JoinType.INNER_JOIN)
		.createAlias("tipologia", "tip", JoinType.LEFT_OUTER_JOIN)

		.add(Restrictions.eq("indExcluido", false))
		.add(Restrictions.eq("proc.ideProcesso", Util.isNullOuVazio(pProcessoAto.getProcesso()) ? null : pProcessoAto.getProcesso().getIdeProcesso()))
		.add(Restrictions.eq("ato.indDeclaratorio", false))

		.setProjection(Projections.projectionList()
				.add(Projections.property("ideProcessoAto"),"ideProcessoAto")
				.add(Projections.property("indExcluido"),"indExcluido")
				.add(Projections.property("proc.ideProcesso"),"processo.ideProcesso")
				.add(Projections.property("ato.ideAtoAmbiental"),"atoAmbiental.ideAtoAmbiental")
				.add(Projections.property("ato.nomAtoAmbiental"),"atoAmbiental.nomAtoAmbiental")
				.add(Projections.property("tip.ideTipologia"),"tipologia.ideTipologia")
				.add(Projections.property("tip.codTipologia"),"tipologia.codTipologia")
				.add(Projections.property("tip.desTipologia"),"tipologia.desTipologia")
				)
				.setResultTransformer(new AliasToNestedBeanResultTransformer(ProcessoAto.class))
				;

		return processoAtoDAO.listarPorCriteria(criteria, Order.asc("ato.nomAtoAmbiental"));
	}
	
	public List<ProcessoAto> getProcessoAtoReenquadramento(ProcessoAto pProcessoAto, Boolean isReenquadramento)  {

		DetachedCriteria criteria = DetachedCriteria.forClass(ProcessoAto.class);

		criteria
		.createAlias("processo", "proc", JoinType.INNER_JOIN)
		.createAlias("atoAmbiental", "ato", JoinType.INNER_JOIN)
		.createAlias("tipologia", "tip", JoinType.LEFT_OUTER_JOIN)

		.add(Restrictions.eq("indExcluido", false))
		.add(Restrictions.eq("proc.ideProcesso", Util.isNullOuVazio(pProcessoAto.getProcesso()) ? null : pProcessoAto.getProcesso().getIdeProcesso()))

		.setProjection(Projections.projectionList()
				.add(Projections.property("ideProcessoAto"),"ideProcessoAto")
				.add(Projections.property("indExcluido"),"indExcluido")
				.add(Projections.property("proc.ideProcesso"),"processo.ideProcesso")
				.add(Projections.property("ato.ideAtoAmbiental"),"atoAmbiental.ideAtoAmbiental")
				.add(Projections.property("ato.nomAtoAmbiental"),"atoAmbiental.nomAtoAmbiental")
				.add(Projections.property("tip.ideTipologia"),"tipologia.ideTipologia")
				.add(Projections.property("tip.codTipologia"),"tipologia.codTipologia")
				.add(Projections.property("tip.desTipologia"),"tipologia.desTipologia")
				);
		
		if (isReenquadramento){
			criteria.add(Restrictions.isNotNull("ideProcessoReenquadramento"));
		} else {
			criteria.add(Restrictions.isNull("ideProcessoReenquadramento"));
		}
		criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(ProcessoAto.class));
		return processoAtoDAO.listarPorCriteria(criteria, Order.asc("ato.nomAtoAmbiental"));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoAto> getProcessoAtoByProcessoPai(Processo processo)  {

		DetachedCriteria criteria = DetachedCriteria.forClass(TransferenciaAmbiental.class)
				.createAlias("ideProcessoAto", "ideProcessoAto", JoinType.INNER_JOIN)
				.createAlias("ideProcessoAto.atoAmbiental", "ideAtoAmbiental", JoinType.INNER_JOIN)
				.createAlias("ideProcessoAto.tipologia"   , "ideTipologia"   , JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("ideProcessoTla.ideProcesso",processo.getIdeProcesso()))
				.add(Restrictions.eq("ideAtoAmbiental.indVisivelSolicitacaoTla",true))
				.add(Restrictions.eq("ideProcessoAto.indExcluido", false))

				.setProjection(Projections.projectionList()
						.add(Projections.property("ideProcessoAto.ideProcessoAto"),"ideProcessoAto")
						.add(Projections.property("ideAtoAmbiental.ideAtoAmbiental"),"atoAmbiental.ideAtoAmbiental")
						.add(Projections.property("ideAtoAmbiental.nomAtoAmbiental"),"atoAmbiental.nomAtoAmbiental")
						.add(Projections.property("ideTipologia.ideTipologia"),"tipologia.ideTipologia")
						.add(Projections.property("ideTipologia.desTipologia"),"tipologia.desTipologia"))

						.setResultTransformer(new AliasToNestedBeanResultTransformer(ProcessoAto.class));

		return processoAtoDAO.listarPorCriteria(criteria, Order.asc("ideAtoAmbiental.nomAtoAmbiental"));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isProcessoTla(Processo processo) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Processo.class)
				.createAlias("processoAtoCollection", "processoAto")
				.createAlias("processoAto.atoAmbiental", "ideAtoAmbiental")

				.add(Restrictions.eq("ideAtoAmbiental.ideAtoAmbiental", AtoAmbientalEnum.TLA.getId()))
				.add(Restrictions.eq("processoAto.indExcluido", false));

		if(!Util.isNullOuVazio(processo.getIdeProcesso())){
			detachedCriteria.add(Restrictions.eq("ideProcesso", processo.getIdeProcesso()));
		}else{
			detachedCriteria.add(Restrictions.eq("numProcesso", processo.getNumProcesso()));
		}

		return !Util.isNullOuVazio(processoAtoDAO.listarPorCriteria(detachedCriteria));
	}

	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoAto> listarAtosPorProcessoComOuSemStatus(Integer ideProcesso , boolean comStatus, StatusProcessoAtoEnum spaEnum) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ProcessoAto.class)
			.createAlias("processo", "p", JoinType.INNER_JOIN)
			.createAlias("tipologia", "tipologia", JoinType.LEFT_OUTER_JOIN)
			.createAlias("atoAmbiental", "ato", JoinType.INNER_JOIN)
			.createAlias("controleProcessoAtoCollection", "controleProcessoAto", JoinType.LEFT_OUTER_JOIN)
			
		.add(Restrictions.eq("p.ideProcesso", ideProcesso))
		.add(Restrictions.eq("indExcluido", false))
			
		.setProjection(Projections.distinct(
			Projections.projectionList()
				.add(Property.forName("ideProcessoAto"), "ideProcessoAto")
				.add(Property.forName("p.ideProcesso"), "processo.ideProcesso")
				.add(Property.forName("ato.ideAtoAmbiental"), "atoAmbiental.ideAtoAmbiental")
				.add(Property.forName("ato.nomAtoAmbiental"), "atoAmbiental.nomAtoAmbiental")
				.add(Property.forName("tipologia.ideTipologia"), "tipologia.ideTipologia")
				.add(Property.forName("tipologia.desTipologia"), "tipologia.desTipologia"))
		).setResultTransformer(new AliasToNestedBeanResultTransformer(ProcessoAto.class));
		
		if(comStatus && (spaEnum != null)){
		
			DetachedCriteria subCriteria = DetachedCriteria.forClass(ControleProcessoAto.class)
				.createAlias("ideStatusProcessoAto", "ideStatusProcessoAto")
				.add(Property.forName("ideProcessoAto").eqProperty("controleProcessoAto.ideProcessoAto.ideProcessoAto"))
				.setProjection(Projections.max("ideStatusProcessoAto"));
			
			detachedCriteria
				.add(Property.forName("controleProcessoAto.ideStatusProcessoAto").in(subCriteria))
				.add(Restrictions.or(
						Restrictions.isNull("controleProcessoAto.ideStatusProcessoAto"),
						Restrictions.eq("controleProcessoAto.ideStatusProcessoAto.ideStatusProcessoAto", spaEnum.getId())
					)
				);
			
		}else{
			detachedCriteria.add(Restrictions.isNull("controleProcessoAto.ideControleProcessoAto"));
		}

		return processoAtoDAO.listarPorCriteria(detachedCriteria);
	}


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoAto> listarAtosPorProcesso(Processo ideProcesso) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ProcessoAto.class)
				.createAlias("processo", "processo", JoinType.INNER_JOIN)
				.createAlias("tipologia", "tipologia", JoinType.LEFT_OUTER_JOIN)
				.createAlias("atoAmbiental", "ato", JoinType.INNER_JOIN)
				.createAlias("controleProcessoAtoCollection", "controleProcessoAto", JoinType.LEFT_OUTER_JOIN)
				
				.add(Restrictions.eq("processo", ideProcesso))
				.add(Restrictions.eq("indExcluido", false))
				.setProjection(Projections.distinct(Projections.projectionList()
						.add(Property.forName("ideProcessoAto"), "ideProcessoAto")
						.add(Property.forName("processo.ideProcesso"), "processo.ideProcesso")
						.add(Property.forName("ato.ideAtoAmbiental"), "atoAmbiental.ideAtoAmbiental")
						.add(Property.forName("ato.nomAtoAmbiental"), "atoAmbiental.nomAtoAmbiental")
						.add(Property.forName("ato.indDeclaratorio"), "atoAmbiental.indDeclaratorio")
						.add(Property.forName("tipologia.ideTipologia"), "tipologia.ideTipologia")
						.add(Property.forName("tipologia.desTipologia"), "tipologia.desTipologia")
						)).setResultTransformer(new AliasToNestedBeanResultTransformer(ProcessoAto.class));

		return processoAtoDAO.listarPorCriteria(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoAto> listarProcessoAtoParaSinaflor(Integer ideProcesso) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcessoAto.class);
		
		criteria
			.createAlias("processo", "p", JoinType.INNER_JOIN)
			.createAlias("tipologia", "t", JoinType.LEFT_OUTER_JOIN)
			.createAlias("atoAmbiental", "a", JoinType.INNER_JOIN)
			.createAlias("a.ideAtoSinaflor", "as", JoinType.INNER_JOIN)
			
			.add(Restrictions.eq("p.ideProcesso", ideProcesso))
			.add(Restrictions.eq("indExcluido", false))
			
			.setProjection(Projections.distinct(Projections.projectionList()
					.add(Property.forName("ideProcessoAto"), "ideProcessoAto")
					.add(Property.forName("p.ideProcesso"), "processo.ideProcesso")
					.add(Property.forName("a.ideAtoAmbiental"), "atoAmbiental.ideAtoAmbiental")
					.add(Property.forName("a.nomAtoAmbiental"), "atoAmbiental.nomAtoAmbiental")
					.add(Property.forName("a.indDeclaratorio"), "atoAmbiental.indDeclaratorio")
					.add(Property.forName("as.ideAtoSinaflor"), "atoAmbiental.ideAtoSinaflor.ideAtoSinaflor")
					.add(Property.forName("as.nomAtoSinaflor"), "atoAmbiental.ideAtoSinaflor.nomAtoSinaflor")
					.add(Property.forName("t.ideTipologia"), "tipologia.ideTipologia")
					.add(Property.forName("t.desTipologia"), "tipologia.desTipologia")
				)
			)
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ProcessoAto.class))
		;
		
		return processoAtoDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoAto> listarProcessoAtoPorFce(Fce fce) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Fce.class)
				.createAlias("ideDocumentoObrigatorio", "doc", JoinType.INNER_JOIN)
				.createAlias("ideRequerimento", "req", JoinType.INNER_JOIN)
				.createAlias("req.processoCollection", "proc", JoinType.INNER_JOIN)
				.createAlias("req.enquadramentoCollection", "enq", JoinType.INNER_JOIN)
				.createAlias("enq.enquadramentoDocumentoAtoCollection", "eda", JoinType.INNER_JOIN)
				.createAlias("eda.documentoAto", "da", JoinType.INNER_JOIN)
				.createAlias("proc.processoAtoCollection", "pa", JoinType.INNER_JOIN)
				.createAlias("pa.atoAmbiental", "aa", JoinType.INNER_JOIN)
				.createAlias("pa.tipologia", "tipo", JoinType.LEFT_OUTER_JOIN)
				
				.add(Restrictions.eq("ideFce", fce.getIdeFce()))
				.add(Restrictions.eqProperty("doc.ideDocumentoObrigatorio", "da.ideDocumentoObrigatorio"))
				.add(Restrictions.eqProperty("da.ideAtoAmbiental", "pa.atoAmbiental"))
				
				.setProjection(Projections.distinct(Projections.projectionList()
						.add(Property.forName("pa.ideProcessoAto"), "ideProcessoAto")
						.add(Property.forName("pa.indExcluido"), "indExcluido")
						.add(Property.forName("pa.dtcExclusao"), "dtcExclusao")
						.add(Property.forName("proc.ideProcesso"), "processo.ideProcesso")
						.add(Property.forName("aa.ideAtoAmbiental"), "atoAmbiental.ideAtoAmbiental")
						.add(Property.forName("tipo.ideTipologia"), "tipologia.ideTipologia")
				)).setResultTransformer(new AliasToNestedBeanResultTransformer(ProcessoAto.class));
		
		return processoAtoDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ProcessoAto> listarAtosPor(Processo processo)  {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Processo.class)
			.createAlias("processoAtoCollection", "pa", JoinType.INNER_JOIN)
			.createAlias("pa.atoAmbiental", "aa", JoinType.INNER_JOIN)
			.createAlias("aa.ideTipoAto", "ta", JoinType.LEFT_OUTER_JOIN)
			.createAlias("pa.tipologia", "t", JoinType.LEFT_OUTER_JOIN)
			.createAlias("pa.controleProcessoAtoCollection", "cpa", JoinType.INNER_JOIN)
			.createAlias("cpa.ideStatusProcessoAto", "st", JoinType.INNER_JOIN)
			
			.add(Restrictions.in("aa.ideAtoAmbiental", new Integer[] {
					AtoAmbientalEnum.AOUT.getId(),
					AtoAmbientalEnum.COUT.getId(),
					AtoAmbientalEnum.DOUT.getId(),
					AtoAmbientalEnum.PPV_OUT.getId(),
					AtoAmbientalEnum.ROUT.getId(),
					AtoAmbientalEnum.OUTP.getId(),
					AtoAmbientalEnum.OUTORGA.getId(),
					
					// #9010
					AtoAmbientalEnum.CAPTACAO_ABASTECIMENTO_HUMANO.getId(),
					AtoAmbientalEnum.CAPTACAO_AQUICULTURA.getId(), 
					AtoAmbientalEnum.CAPTACAO_ABASTECIMENTO_INDUSTRIAL.getId(), 
					AtoAmbientalEnum.TRAVESSIA_DE_DUTO.getId(), 
					AtoAmbientalEnum.IRRIGACAO.getId(),
					AtoAmbientalEnum.PULVERIZACAO.getId(), 
					AtoAmbientalEnum.CANALIZACAO_E_RETIFICACAO.getId(), 
					AtoAmbientalEnum.CONSTRUCAO_DE_PONTE.getId(),
					AtoAmbientalEnum.DRENAGEM_DE_AGUAS_PLUVIAIS_COM_DESAGUE_EM_MANANCIAL.getId(),
					AtoAmbientalEnum.EXTRAÇÃO_EXPLOTAÇÃO_MINERAL_EM_LEITO_PESQUISA_E_LAVRA_LIMPEZA_DESASSOREAMENTO_E_DRAGAGEM.getId(),
					AtoAmbientalEnum.LANCAMENTO_DE_EFLUENTES_PARA_OS_DIVERSOS_FINS.getId(),
					AtoAmbientalEnum.CAPTACAO_DESSEDENTACAO_E_CRIACAO_ANIMAL.getId(),
					AtoAmbientalEnum.BARRAGEM.getId(),
					AtoAmbientalEnum.CONSTRUCAO_DE_PIER_DIQUE_CAIS.getId()
					
			}))
			.add(Restrictions.eq("ideProcesso", processo.getIdeProcesso()))
			.add(Restrictions.eq("indExcluido", false))
			.add(Restrictions.eq("pa.indExcluido", false))
			.add(Property.forName("cpa.ideControleProcessoAto").eq(
				DetachedCriteria.forClass(ControleProcessoAto.class)
					.createAlias("ideProcessoAto", "pa2", JoinType.INNER_JOIN)
					.add(Restrictions.eqProperty("pa2.ideProcessoAto", "pa.ideProcessoAto"))
					.setProjection(Projections.max("ideControleProcessoAto"))
			))
			
			.setProjection(Projections.projectionList()
				.add(Property.forName("pa.ideProcessoAto"), "ideProcessoAto")
				.add(Property.forName("ideProcesso"), "processo.ideProcesso")
				.add(Property.forName("aa.ideAtoAmbiental"), "atoAmbiental.ideAtoAmbiental")
				.add(Property.forName("aa.nomAtoAmbiental"), "atoAmbiental.nomAtoAmbiental")
				.add(Property.forName("ta.ideTipoAto"), "atoAmbiental.ideTipoAto.ideTipoAto")
				.add(Property.forName("t.ideTipologia"), "tipologia.ideTipologia")
				.add(Property.forName("t.desTipologia"), "tipologia.desTipologia")
				.add(Property.forName("st.ideStatusProcessoAto"), "statusProcessoAto.ideStatusProcessoAto")
				.add(Property.forName("st.nomStatusProcessoAto"), "statusProcessoAto.nomStatusProcessoAto")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ProcessoAto.class));
		
		return processoAtoDAO.listarPorCriteria(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ProcessoAto> listaProcessoAtoComDadosConcedidos(Processo processo) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Tipologia.class)
				
			.createAlias("atoAmbientalTipologiaCollection", "aat", JoinType.INNER_JOIN)
			.createAlias("aat.ideAtoAmbiental", "aa", JoinType.INNER_JOIN)
			.createAlias("aa.documentoAtoCollection", "da", JoinType.INNER_JOIN)
			
			.createAlias("processoAtoCollection", "pa", JoinType.INNER_JOIN)
			.createAlias("pa.processo", "p", JoinType.INNER_JOIN)
			.createAlias("p.analiseTecnicaCollection", "at", JoinType.INNER_JOIN)
			.createAlias("at.fceCollection", "fce", JoinType.INNER_JOIN)
			.createAlias("fce.fceOutorgaLocalizacaoGeografica", "folg", JoinType.INNER_JOIN)
			.createAlias("folg.ideLocalizacaoGeografica", "lg", JoinType.INNER_JOIN)
			.createAlias("lg.ideSistemaCoordenada", "sc", JoinType.INNER_JOIN)
			
			.createAlias("pa.controleProcessoAtoCollection", "cpa", JoinType.INNER_JOIN)
			.createAlias("cpa.ideStatusProcessoAto", "st", JoinType.INNER_JOIN)
			
			.add(Restrictions.eqProperty("aa.ideAtoAmbiental", "pa.atoAmbiental.ideAtoAmbiental"))
			.add(Restrictions.in("aa.ideAtoAmbiental", new Integer[] {
					AtoAmbientalEnum.AOUT.getId(),
					AtoAmbientalEnum.COUT.getId(),
					AtoAmbientalEnum.DOUT.getId(),
					AtoAmbientalEnum.PPV_OUT.getId(),
					AtoAmbientalEnum.ROUT.getId(),
					AtoAmbientalEnum.OUTP.getId(),
					AtoAmbientalEnum.OUTORGA.getId()
			}))
			.add(Restrictions.eqProperty("ideTipologia", "pa.tipologia.ideTipologia"))
			.add(Restrictions.eqProperty("fce.ideDocumentoObrigatorio", "da.ideDocumentoObrigatorio"))
			
			.add(Restrictions.eq("p.ideProcesso", processo.getIdeProcesso()))
			
			.add(Property.forName("cpa.ideControleProcessoAto").eq(
					DetachedCriteria.forClass(ControleProcessoAto.class)
					.createAlias("ideProcessoAto", "pa2", JoinType.INNER_JOIN)
					.createAlias("ideStatusProcessoAto", "st", JoinType.INNER_JOIN)
					.add(Restrictions.eqProperty("pa2.ideProcessoAto", "pa.ideProcessoAto"))
					.add(Restrictions.eq("st.ideStatusProcessoAto", StatusProcessoAtoEnum.DEFERIDO.getId()))
					.setProjection(Projections.max("ideControleProcessoAto"))
			))
			.add(Restrictions.eqProperty("fce.ideDocumentoObrigatorio", "da.ideDocumentoObrigatorio"))
			.add(Restrictions.eq("fce.ideDadoOrigem", new DadoOrigem(DadoOrigemEnum.TECNICO.getId())))
			
			.setProjection(Projections.distinct(
				Projections.projectionList()
					.add(Property.forName("pa.ideProcessoAto"), "ideProcessoAto")
					.add(Property.forName("p.ideProcesso"), "processo.ideProcesso")
					.add(Property.forName("aa.ideAtoAmbiental"), "atoAmbiental.ideAtoAmbiental")
					.add(Property.forName("aa.nomAtoAmbiental"), "atoAmbiental.nomAtoAmbiental")
					.add(Property.forName("ideTipologia"), "tipologia.ideTipologia")
					.add(Property.forName("desTipologia"), "tipologia.desTipologia")
				)
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ProcessoAto.class));
		
		return processoAtoDAO.listarPorCriteria(detachedCriteria, Order.asc("desTipologia"));
	}
	
	public ProcessoAto buscarProcessoAtoPorId(Integer ideProcessoAto) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcessoAto.class);
		criteria.add(Restrictions.eq("ideProcessoAto", ideProcessoAto));
		return processoAtoDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Object[]> listarFinalidadeReenquadramentoProcessoPorProceso(Integer ideProcesso){
		StringBuilder lSql = new StringBuilder();
		
		lSql.append("select distinct f.ide_processo_ato, c.nom_motivo_reenquadramento, f.ide_tipologia, f.ide_ato_ambiental, a.ide_reenquadramento_processo "
				.concat("from processo_ato f ")
				.concat("inner join processo pro on pro.ide_processo=f.ide_processo ")
				.concat("inner join notificacao noti ON pro.ide_processo=noti.ide_processo ")
				.concat("inner join reenquadramento_processo b on noti.ide_notificacao = b.ide_notificacao ")
				.concat("inner join finalidade_reenquadramento_processo a on b.ide_reenquadramento_processo=a.ide_reenquadramento_processo ")
				.concat("inner join finalidade_reenquadramento c on c.ide_finalidade_reenquadramento=a.ide_finalidade_reenquadramento ")
				.concat("where f.ide_processo=:ide_processo"));
		
		EntityManager lEntityManager = DAOFactory.getEntityManager();

		Session session = (Session) lEntityManager.getDelegate();
		SQLQuery query = session.createSQLQuery(lSql.toString());
		query.setParameter("ide_processo", ideProcesso);
		
		return query.list();
		
	}
}