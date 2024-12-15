package br.gov.ba.seia.dao;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import br.gov.ba.seia.dto.RelatorioQuantitativoDeProcessoDTO;
import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.ControleTramitacao;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.StatusFluxo;
import br.gov.ba.seia.entity.TipoAto;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.enumerator.TipoEnderecoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RelatorioQuantitativoDAOImpl {
	
	@Inject
	private IDAO<RelatorioQuantitativoDeProcessoDTO> dao;
	
	private Municipio municipioSelecionado;
	private StatusFluxo statusFluxoSelecionado;
	private TipoAto tipoAtoSelecionado;
	private AtoAmbiental atoAmbientalSelecionado;
	private Tipologia tipologiaSelecionada;
	private TipoFinalidadeUsoAgua finalidadeSelecionada;
	private Area diretoriaSelecionada;
	private Area areaSelecionada;
	private Date periodoDeFormacaoDE;
	private Date periodoDeFormacaoATE;
	private Date periodoStatusDE;
	private Date periodoStatusATE;
	private Tipologia tipologiaAtividade;
	private List<Tipologia> listaTipologiaAtividade;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<RelatorioQuantitativoDeProcessoDTO> listaDadosDoRelatorioQuantitativo(Map<String, Object> params)  {
		DetachedCriteria criteria = getCriteriaRelatorioQuatitativo(params);
		
		criteria
			.setProjection(Projections.projectionList()
				.add(Projections.property("ta.nomTipoAto"),"categoriaDoAto")
				.add(Projections.property("aa.sglAtoAmbiental"),"siglaDoAto")
				.add(Projections.property("aa.nomAtoAmbiental"),"nomeDoAto")
				.add(Projections.sqlGroupProjection(
						"count(1) as qtd_atos_",
						"aa2_.ide_ato_ambiental, aa2_.sgl_ato_ambiental, aa2_.nom_ato_ambiental, ta3_.nom_tipo_ato",
						new String[] {"qtd_atos_"}, 
						new Type[] {StandardBasicTypes.INTEGER}),"qtdAtos"
				)
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(RelatorioQuantitativoDeProcessoDTO.class))
		;
		
		return dao.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer totalDeProcessoAnalisados(Map<String, Object> params)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Processo.class);
		
		criteria
			.add(Property.forName("ideProcesso").in(
					getCriteriaRelatorioQuatitativo(params)
					.setProjection(Projections.groupProperty("ideProcesso"))
				)
			)
			.setProjection(Projections.rowCount())
		;
		
		Object retorno = dao.count(criteria);
		
		return (Integer) retorno; 
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private DetachedCriteria getCriteriaRelatorioQuatitativo(Map<String, Object> params) {
		
		carregarParametros(params);
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Processo.class);
		
		criteria
			.createAlias("processoAtoCollection", "pa", JoinType.INNER_JOIN)
			.createAlias("pa.atoAmbiental", "aa", JoinType.INNER_JOIN)
			.createAlias("aa.ideTipoAto", "ta", JoinType.INNER_JOIN)
			.createAlias("ideRequerimento", "r", JoinType.INNER_JOIN)			
		;
				
		if(statusFluxoSelecionado!=null){
			criteria.createAlias("controleTramitacaoCollection", "ct", JoinType.INNER_JOIN);
			
			if(periodoStatusDE!=null && periodoStatusATE!=null) {
				
				criteria.add(
					Property.forName("ct.ideControleTramitacao").in(
							DetachedCriteria.forClass(ControleTramitacao.class)
							.createAlias("ideProcesso", "p", JoinType.INNER_JOIN)
							.createAlias("ideStatusFluxo", "st", JoinType.INNER_JOIN)
							.add(Restrictions.eq("indFimDaFila", true))
							.add(Restrictions.eqProperty("p.ideProcesso", "ideProcesso"))
							.add(Restrictions.and(
									Restrictions.ge("dtcTramitacao", periodoStatusDE), 
									Restrictions.lt("dtcTramitacao", adicionarUmDia(periodoStatusATE))
								)
							)
							.add(Restrictions.eq("st.ideStatusFluxo", statusFluxoSelecionado.getIdeStatusFluxo()))
							.setProjection(Projections.property("ideControleTramitacao"))
					)
				);
			}
			else{
				criteria.add(
					Property.forName("ct.ideControleTramitacao").in(
							DetachedCriteria.forClass(ControleTramitacao.class)
							.createAlias("ideProcesso", "p", JoinType.INNER_JOIN)
							.createAlias("ideStatusFluxo", "st", JoinType.INNER_JOIN)
							.add(Restrictions.eq("indFimDaFila", true))
							.add(Restrictions.eqProperty("p.ideProcesso", "ideProcesso"))
							.add(Restrictions.eq("st.ideStatusFluxo", statusFluxoSelecionado.getIdeStatusFluxo()))
							.setProjection(Projections.property("ideControleTramitacao"))
					)
				);
			}
		}
		
		if(atoAmbientalSelecionado!=null){
			criteria.add(Restrictions.eq("aa.ideAtoAmbiental", atoAmbientalSelecionado.getIdeAtoAmbiental()));
		}
		
		if(tipoAtoSelecionado!=null){
			criteria.add(Restrictions.eq("ta.ideTipoAto", tipoAtoSelecionado.getIdeTipoAto()));
		}
		
		if(diretoriaSelecionada!=null || areaSelecionada != null){
			criteria
				.createAlias("controleTramitacaoFormacaoCollection", "ctf", JoinType.INNER_JOIN)
				.createAlias("ctf.ideArea","af", JoinType.INNER_JOIN)
				.createAlias("af.ideAreaPai","ap", JoinType.INNER_JOIN);
				
			if(diretoriaSelecionada != null){
				criteria.add(Restrictions.eq("ap.ideArea", diretoriaSelecionada.getIdeArea()));
			}
						
			if(areaSelecionada != null){
				criteria.add(Restrictions.eq("af.ideArea", areaSelecionada.getIdeArea()));
			}			
		}
		
		if(municipioSelecionado!=null){
			criteria
				.createAlias("r.empreendimentoRequerimentoCollection", "er", JoinType.INNER_JOIN)
				.createAlias("er.ideEmpreendimento", "emp", JoinType.INNER_JOIN)
				.createAlias("emp.enderecoEmpreendimentoCollection", "ee", JoinType.INNER_JOIN)
				.createAlias("ee.ideTipoEndereco", "te", JoinType.INNER_JOIN)
				.createAlias("ee.ideEndereco", "end", JoinType.INNER_JOIN)
				.createAlias("end.ideLogradouro", "l", JoinType.INNER_JOIN)
				.createAlias("l.ideMunicipio", "m", JoinType.INNER_JOIN)
				.add(Restrictions.eq("m.ideMunicipio", municipioSelecionado.getIdeMunicipio()))
				.add(Restrictions.eq("te.ideTipoEndereco", TipoEnderecoEnum.LOCALIZACAO.getId()))
			;			
		}
		
		if(periodoDeFormacaoDE!=null && periodoDeFormacaoATE!=null){
			criteria
				.add(Restrictions.ge("dtcFormacao", periodoDeFormacaoDE))
				.add(Restrictions.lt("dtcFormacao", adicionarUmDia(periodoDeFormacaoATE)))
			;
		}
		
		if(tipologiaSelecionada != null) {
			criteria
				.createAlias("pa.tipologia", "t", JoinType.INNER_JOIN)
				.add(Restrictions.eq("t.ideTipologia", tipologiaSelecionada.getIdeTipologia()))
			;
		}
		
		if(finalidadeSelecionada != null) {
			criteria
				.createAlias("r.outorgaCollection","o", JoinType.INNER_JOIN)
				.createAlias("o.outorgaLocalizacaoGeograficaCollection","olg", JoinType.INNER_JOIN)
				.createAlias("olg.outorgaLocalizacaoGeograficaFinalidadeCollection","olgf", JoinType.INNER_JOIN)
				.createAlias("olgf.ideTipoFinalidadeUsoAgua", "tf", JoinType.INNER_JOIN)
				.add(Restrictions.eq("tf.ideTipoFinalidadeUsoAgua", finalidadeSelecionada.getIdeTipoFinalidadeUsoAgua()))
			;
		}
		
		if (!Util.isNull(tipologiaAtividade) || !Util.isNullOuVazio(listaTipologiaAtividade)) {
			criteria
				.createAlias("r.requerimentoTipologiaCollection","rt", JoinType.LEFT_OUTER_JOIN)
				.createAlias("rt.ideUnidadeMedidaTipologiaGrupo","umtg", JoinType.LEFT_OUTER_JOIN)
				.createAlias("umtg.ideTipologiaGrupo","tg", JoinType.LEFT_OUTER_JOIN)
				.createAlias("tg.ideTipologia","tipo", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("tg.indExcluido", false))
				.add(Restrictions.eq("tipo.indExcluido", false))
			;
			
			if(!Util.isNull(tipologiaAtividade)) {
				criteria.add(Restrictions.eq("tipo.ideTipologia", tipologiaAtividade.getIdeTipologia()));
			} else{
				criteria.add(Restrictions.in("tipo.ideTipologia", listaTipologiaAtividade));
			}
		}
		
		return criteria;
	}

	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private void carregarParametros(Map<String, Object> params) {
		municipioSelecionado = (Municipio) params.get("municipioSelecionado");
		statusFluxoSelecionado = (StatusFluxo) params.get("statusFluxoSelecionado");
		tipoAtoSelecionado = (TipoAto) params.get("tipoAtoSelecionado");
		atoAmbientalSelecionado = (AtoAmbiental) params.get("atoAmbientalSelecionado");
		tipologiaSelecionada = (Tipologia) params.get("tipologiaSelecionada");
		finalidadeSelecionada = (TipoFinalidadeUsoAgua) params.get("finalidadeSelecionada");
		diretoriaSelecionada = (Area) params.get("diretoriaSelecionada");
		areaSelecionada = (Area) params.get("areaSelecionada");	
		periodoDeFormacaoDE = (Date) params.get("periodoDeFormacaoDE");
		periodoDeFormacaoATE = (Date) params.get("periodoDeFormacaoATE");	
		periodoStatusDE = (Date) params.get("periodoStatusDE");
		periodoStatusATE = (Date) params.get("periodoStatusATE");
		tipologiaAtividade = (Tipologia) params.get("atividade");
		listaTipologiaAtividade = (List<Tipologia>) params.get("listaAtividades");
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private Date adicionarUmDia(Date date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(Calendar.DAY_OF_YEAR, 1);
		return gc.getTime();
	}
	
}