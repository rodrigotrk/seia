package br.gov.ba.seia.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.type.BooleanType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import br.gov.ba.seia.dto.RequerimentoDTO;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EmpreendimentoRequerimento;
import br.gov.ba.seia.entity.EnderecoEmpreendimento;
import br.gov.ba.seia.entity.Funcionario;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.PagamentoReposicaoFlorestal;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Porte;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.StatusRequerimento;
import br.gov.ba.seia.entity.TipoEndereco;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.TipoPessoaRequerimento;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.entity.TramitacaoRequerimento;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.enumerator.TipoEnderecoEnum;
import br.gov.ba.seia.enumerator.TipoPessoaRequerimentoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;
import br.gov.ba.ws.entity.RequerimentoWS;

public class NovoRequerimentoDAOImpl implements NovoRequerimentoDAOIf {

	@Inject
	IDAO<AtoAmbiental> atoAmbientalDao;
	@Inject
	IDAO<RequerimentoDTO> requerimentoDAO;
	@Inject	
	IDAO<Empreendimento> empreendimentoDAO; 

	@Override
	public List<RequerimentoDTO> listarRequerimentoUnico(Map<String, Object> params) throws Exception {

		Integer first = (Integer) params.get("first");
		Integer pageSize = (Integer) params.get("pageSize");

		List<RequerimentoDTO> collRequerimentoUnicoDTO = requerimentoDAO.listarPorCriteriaDemanda(getCriteriaConsultaRequerimento(params), first, pageSize);

		List<AtoAmbiental> atosAmbientais = getListaAtosEnquadrados(collRequerimentoUnicoDTO);
		for (RequerimentoDTO requerimentoUnicoDTO : collRequerimentoUnicoDTO) {
			preencheAtosAmbientais(atosAmbientais, requerimentoUnicoDTO.getRequerimento());
		}
		
		List<Empreendimento> empreendimentos = 	getObterEmpreendimentos(collRequerimentoUnicoDTO);
		for (RequerimentoDTO requerimentoUnicoDTO : collRequerimentoUnicoDTO) {
			preencheEmpreendimentos(empreendimentos, requerimentoUnicoDTO);
		}

		return collRequerimentoUnicoDTO;
	}

	private void preencheEmpreendimentos(List<Empreendimento> empreendimentos, RequerimentoDTO requerimento) {
		for (Empreendimento empreendimento: empreendimentos) {
			for (EnderecoEmpreendimento enderecoEmpreendimento : empreendimento.getEnderecoEmpreendimentoCollection()){
				if(!Util.isNullOuVazio(enderecoEmpreendimento) && enderecoEmpreendimento.getIdeTipoEndereco().getIdeTipoEndereco().equals(TipoEnderecoEnum.LOCALIZACAO.getId())){
					for (EmpreendimentoRequerimento empreendimentoRequerimento : empreendimento.getEmpreendimentoRequerimentoCollection()){
						if(requerimento.getRequerimento().getIdeRequerimento().equals(empreendimentoRequerimento.getIdeRequerimento().getIdeRequerimento())){
							requerimento.getEmpreendimento().setMunicipio(enderecoEmpreendimento.getIdeEndereco().getIdeLogradouro().getIdeMunicipio());
						}
					}
				}
			}
		}
		
	}

	private List<Empreendimento> getObterEmpreendimentos(List<RequerimentoDTO> lista) throws Exception {
		List<Integer> ides = getIdesDTo(lista);
		
		if (ides.isEmpty()) {
			return new ArrayList<Empreendimento>();
		}

		DetachedCriteria criteria = DetachedCriteria.forClass(Empreendimento.class)
				.createAlias("empreendimentoRequerimentoCollection", "empReq", JoinType.INNER_JOIN)
				.createAlias("enderecoEmpreendimentoCollection", "enderecoEmp", JoinType.INNER_JOIN)
				.createAlias("enderecoEmp.ideEndereco", "endereco", JoinType.INNER_JOIN)
				.createAlias("endereco.ideLogradouro", "logradouro", JoinType.INNER_JOIN)
				.createAlias("logradouro.ideMunicipio", "municipio",JoinType.INNER_JOIN)
				.add(Restrictions.in("empReq.ideRequerimento.ideRequerimento", ides))
				.add(Restrictions.eq("enderecoEmp.ideTipoEndereco.ideTipoEndereco", 4));

		
		return this.empreendimentoDAO.listarPorCriteria(criteria);
	}

	private DetachedCriteria getCriteriaConsultaRequerimento(Map<String, Object> params) {

		Boolean isCount = getIsCount(params);
		Boolean isPagination = getIsPagination(params);

		DetachedCriteria criteria = DetachedCriteria.forClass(Requerimento.class, "req");
		
		adicionarJoins(criteria);
		adicionarFiltros(criteria, params);

		if (!isCount && isPagination) {
			criteria.addOrder(Order.desc("req.numRequerimento"));
			adicionarProjection(criteria);
			return criteria;
		}
		else {
			criteria.setProjection(Projections.groupProperty("req.ideRequerimento"));
			return DetachedCriteria.forClass(Requerimento.class)
					.add(Property.forName("ideRequerimento").in(criteria))
					.setProjection(Projections.rowCount())
					;
		}

	}


	private void adicionarJoins(DetachedCriteria criteria) {
		criteria
			.createAlias("req.ideOrgao","orgao", JoinType.INNER_JOIN)
			.createAlias("req.requerimentoPessoaCollection","reqPessoa", JoinType.INNER_JOIN)
			.createAlias("req.tramitacaoRequerimentoCollection","tran", JoinType.INNER_JOIN)
			.createAlias("tran.idePessoa","pTra", JoinType.INNER_JOIN)
			.createAlias("pTra.pessoaFisica","pTraF", JoinType.LEFT_OUTER_JOIN)
			.createAlias("pTra.pessoaJuridica","pTraJ", JoinType.LEFT_OUTER_JOIN)
			.createAlias("tran.ideStatusRequerimento","status", JoinType.INNER_JOIN)
			.createAlias("req.empreendimentoRequerimentoCollection","empReq", JoinType.LEFT_OUTER_JOIN)
			.createAlias("empReq.ideEmpreendimento","emp", JoinType.LEFT_OUTER_JOIN)
			.createAlias("empReq.idePorte","porte", JoinType.LEFT_OUTER_JOIN)
			.createAlias("emp.enderecoEmpreendimentoCollection","endem", JoinType.LEFT_OUTER_JOIN)
			.createAlias("endem.ideEndereco","ende", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ende.ideLogradouro","log", JoinType.LEFT_OUTER_JOIN)
			.createAlias("log.ideMunicipio","lm", JoinType.LEFT_OUTER_JOIN)
			.createAlias("log.ideBairro","br", JoinType.LEFT_OUTER_JOIN)
			.createAlias("br.ideMunicipio","bm", JoinType.LEFT_OUTER_JOIN)
			.createAlias("reqPessoa.pessoa","pessoa", JoinType.INNER_JOIN)
			.createAlias("pessoa.pessoaFisica","pf", JoinType.LEFT_OUTER_JOIN)
			.createAlias("pessoa.pessoaJuridica","pj", JoinType.LEFT_OUTER_JOIN)
			.createAlias("req.lac","lac", JoinType.LEFT_OUTER_JOIN)
			.createAlias("req.enquadramentoCollection","enquadramento", JoinType.LEFT_OUTER_JOIN)
			.createAlias("enquadramento.enquadramentoAtoAmbientalCollection","eaa", JoinType.LEFT_OUTER_JOIN)
			.createAlias("eaa.atoAmbiental","aa", JoinType.LEFT_OUTER_JOIN)
			.createAlias("req.processoCollection","proc", JoinType.LEFT_OUTER_JOIN)
			
			.createAlias("req.cumprimentoReposicaoFlorestal","crf", JoinType.LEFT_OUTER_JOIN)
			
			.createAlias("crf.idePagamentoReposicaoFlorestal","prf", JoinType.LEFT_OUTER_JOIN)
			
			.createAlias("eaa.tipologia", "tipologia", JoinType.LEFT_OUTER_JOIN)
			.createAlias("eaa.enquadramentoFinalidadeUsoAguaCollection", "ef", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ef.ideTipoFinalidadeUsoAgua", "tf", JoinType.LEFT_OUTER_JOIN)
		;
	}

	private void adicionarFiltros(DetachedCriteria criteria, Map<String, Object> params) {
		
		criteria
			.add(Restrictions.eq("req.indExcluido", false))
			
			.add(Restrictions.or(
					Restrictions.eq("endem.ideTipoEndereco", new TipoEndereco(TipoEnderecoEnum.LOCALIZACAO.getId())),
					Restrictions.isNull("emp.ideEmpreendimento")))
			
			.add(Restrictions.eq("reqPessoa.ideTipoPessoaRequerimento", new TipoPessoaRequerimento(TipoPessoaRequerimentoEnum.REQUERENTE.getId())))
			
			.add(Property.forName("tran.dtcMovimentacao").eq(
					DetachedCriteria.forClass(TramitacaoRequerimento.class, "tra")
						.createAlias("tra.ideRequerimento", "req2", JoinType.INNER_JOIN)
						.add(Restrictions.eqProperty("req2.ideRequerimento", "req.ideRequerimento"))
						.setProjection(Projections.max("dtcMovimentacao"))));
		
		@SuppressWarnings("unchecked")
		List<Integer> listaRequerimentoSemPermissao =  (List<Integer>) params.get("listReqSemPermissaoByRepLegal");
		if(!Util.isNullOuVazio(listaRequerimentoSemPermissao)) {
			criteria.add(Restrictions.not(Restrictions.in("req.ideRequerimento", listaRequerimentoSemPermissao)));
		}

		@SuppressWarnings("unchecked")
		List<Integer> listaPessoas = (List<Integer>) params.get("listaPessoas");
		if (listaPessoas != null) {
			criteria.add(Restrictions.in("reqPessoa.pessoa.idePessoa", listaPessoas));
		}

		Boolean isAtendente = (Boolean) params.get("isAtendente");
		if(!Util.isNull(isAtendente) && isAtendente){
			criteria.add(Restrictions.isNotNull("req.numRequerimento"));
		}

		filtrarPorRequerente(criteria, params);
		filtrarPorEmpreendimento(criteria, params);
		filtrarPorPorte(criteria, params);
		filtrarPorRequerimento(criteria, params);
		filtrarPorStatus(criteria, params);
		filtrarPorAto(criteria, params);
		filtrarPorMunicipio(criteria, params);
		filtrarPorPeriodo(criteria, params);
		filtrarPorTecnico(criteria, params);
		filtrarPorTipologiaAtividade(criteria, params);
		filtrarPorTipologia(criteria, params);
		filtrarPorFinalidade(criteria, params);
		filtrarPorCumprimentoReposicaoFlorestal(criteria, params);
	}
	

	/**
	 * Inclusão do filtro de {@link Tipologia} na consulta do {@link Requerimento}
	 * 
	 * @author eduardo.fernandes
	 * @param criteria
	 * @param params
	 * @see <a href="http://10.105.12.26/redmine/issues/7865">#7865</a>
	 * @since 20/09/2016
	 */
	private void filtrarPorTipologia(DetachedCriteria criteria, Map<String, Object> params) {
		Tipologia tipologia = (Tipologia) params.get("tipologia");
		if(!Util.isNullOuVazio(tipologia)) {
			criteria.add(Restrictions.eq("tipologia.ideTipologia", tipologia.getIdeTipologia()));
		}
	}

	/**
	 * Inclusão do filtro de {@link TipoFinalidadeUsoAgua} na consulta do {@link Requerimento}
	 * 
	 * @author eduardo.fernandes
	 * @param criteria
	 * @param params
	 * @see <a href="http://10.105.12.26/redmine/issues/7865">#7865</a>
	 * @since 20/09/2016
	 */
	private void filtrarPorFinalidade(DetachedCriteria criteria, Map<String, Object> params) {
		TipoFinalidadeUsoAgua finalidadeUsoAgua = (TipoFinalidadeUsoAgua) params.get("finalidade");
		if(!Util.isNullOuVazio(finalidadeUsoAgua)) {
			criteria.add(Restrictions.eq("tf.ideTipoFinalidadeUsoAgua", finalidadeUsoAgua.getIdeTipoFinalidadeUsoAgua()));
		}
	}
	
	private void filtrarPorCumprimentoReposicaoFlorestal(DetachedCriteria criteria, Map<String, Object> params) {
		PagamentoReposicaoFlorestal pagamentoReposicaoFlorestal = (PagamentoReposicaoFlorestal) params.get("pagamentoReposicaoFlorestal");
		if(!Util.isNullOuVazio(pagamentoReposicaoFlorestal)) {
				
			criteria.add(Restrictions.or(Restrictions.eq("crf.idePagamentoReposicaoFlorestal.idePagamentoReposicaoFlorestal", pagamentoReposicaoFlorestal.getIdePagamentoReposicaoFlorestal()), Restrictions.eq("prf.idePagamentoReposicaoFlorestalPai.idePagamentoReposicaoFlorestal", pagamentoReposicaoFlorestal.getIdePagamentoReposicaoFlorestal())));
		}
	}

	private void filtrarPorTecnico(DetachedCriteria criteria,
			Map<String, Object> params) {
		Funcionario tecnico = (Funcionario) params.get("tecnico");
		if(!Util.isNull(tecnico)) {
			criteria
			.createAlias("req.tramitacaoRequerimentoAuxCollection", "tranAux", JoinType.LEFT_OUTER_JOIN)
			.createAlias("tranAux.ideStatusRequerimento", "sTranAux", JoinType.LEFT_OUTER_JOIN)
			.add(Restrictions.disjunction()
					.add(Restrictions.eq("pTra.idePessoa", tecnico.getIdePessoaFisica()))
					.add(Restrictions.conjunction()
							.add(Restrictions.in("status.ideStatusRequerimento", new Integer[] {
									StatusRequerimentoEnum.EM_VALIDACAO_PREVIA.getStatus()
							})
									)
									.add(Restrictions.eq("tranAux.idePessoa", new Pessoa(tecnico.getIdePessoaFisica())))
									.add(Restrictions.eq("sTranAux.ideStatusRequerimento", StatusRequerimentoEnum.ENQUADRADO.getStatus()))
							)
					)
					;
		}
	}

	private void filtrarPorPorte(DetachedCriteria criteria,
			Map<String, Object> params) {
		Porte porte = (Porte) params.get("PORTE");
		if (!Util.isNull(porte)) {
			criteria.add(Restrictions.eq("empReq.idePorte", porte));
		}
	}

	private void filtrarPorPeriodo(DetachedCriteria criteria, Map<String, Object> params) {
		Date dataInicio = (Date) params.get("periodoInicio");
		Date dataFim = (Date) params.get("periodoFim");
	
	 if (!Util.isNullOuVazio(dataInicio)) {	
		dataInicio.setHours(00);
		dataInicio.setMinutes(00);
		dataInicio.setSeconds(01);
	 }		
		
	if (!Util.isNullOuVazio(dataFim)) {	
		dataFim.setHours(23);
		dataFim.setMinutes(59);
		dataFim.setSeconds(59);
	}	
		if (!Util.isNull(dataInicio) && !Util.isNull(dataFim)) {
			criteria.add(Restrictions.between("req.dtcFinalizacao", dataInicio, dataFim));
		}
	}

	private void filtrarPorMunicipio(DetachedCriteria criteria, Map<String, Object> params) {
		@SuppressWarnings("unchecked")
		ArrayList<Municipio> municipios = (ArrayList<Municipio>) params.get("municipios");
		if (!Util.isNullOuVazio(municipios)) { 
			ArrayList<Integer> idesMunicipios = new ArrayList<Integer>();
			for(Municipio municipio : municipios){
				idesMunicipios.add(municipio.getIdeMunicipio());
			}
			criteria.add( Restrictions.or( 
					Restrictions.in("lm.ideMunicipio", idesMunicipios), 
					Restrictions.in("bm.ideMunicipio", idesMunicipios)
				)
			); 
		}
		
		Municipio municipio = (Municipio) params.get("municipio");
		if (!Util.isNull(municipio)) {
			criteria.add(Restrictions.or(
					Restrictions.eq("lm.ideMunicipio", municipio.getIdeMunicipio()),
					Restrictions.eq("bm.ideMunicipio", municipio.getIdeMunicipio())
				)
			);	
		}
	}

	private void filtrarPorAto(DetachedCriteria criteria,
			Map<String, Object> params) {
		if (!Util.isNullOuVazio(params.get("TIPOATO"))) {
			@SuppressWarnings("unchecked")
			List<AtoAmbiental> listaAtoAmbiental = (List<AtoAmbiental>) params.get("LISTAATOS");
			if (!Util.isNullOuVazio(params.get("LISTAATOS"))) {
				criteria.add(Restrictions.in("eaa.atoAmbiental", listaAtoAmbiental));
			}
			else {
				AtoAmbiental atoAmbiental = (AtoAmbiental) params.get("ATO");
				if(!Util.isNull(atoAmbiental)) {
					criteria.add(Restrictions.eq("aa.ideAtoAmbiental", atoAmbiental.getIdeAtoAmbiental()));
				}
			}
		}
	}

	private void filtrarPorRequerente(DetachedCriteria criteria, Map<String, Object> params) {
		Pessoa requerente = (Pessoa) params.get("requerente");
		
		if(!Util.isNullOuVazio(requerente)) {
			criteria.add(Restrictions.eq("reqPessoa.pessoa.idePessoa", requerente.getIdePessoa()));
		}
	}
	
	private void filtrarPorStatus(DetachedCriteria criteria, Map<String, Object> params) {
		StatusRequerimento statusRequerimento = (StatusRequerimento) params.get("statusRequerimento");
		
		if(!Util.isNullOuVazio(statusRequerimento)) {
			criteria.add(Restrictions.eq("status.ideStatusRequerimento", statusRequerimento.getIdeStatusRequerimento()));
		}
	}
	
	private void filtrarPorEmpreendimento(DetachedCriteria criteria, Map<String, Object> params) {
		Empreendimento empreendimento = (Empreendimento) params.get("empreendimento");
		
		if(!Util.isNullOuVazio(empreendimento)) {
			criteria.add(Restrictions.eq("emp.ideEmpreendimento", empreendimento.getIdeEmpreendimento()));
		}
	}
	
	private void filtrarPorRequerimento(DetachedCriteria criteria, Map<String, Object> params) {
		Requerimento requerimento = (Requerimento) params.get("requerimento");
		
		if(!Util.isNullOuVazio(requerimento.getNumRequerimento())) {
			criteria.add(Restrictions.ilike("req.numRequerimento", requerimento.getNumRequerimento(), MatchMode.ANYWHERE));
		}
	}

	private void filtrarPorTipologiaAtividade(DetachedCriteria criteria, Map<String, Object> params) {

		Tipologia tipologiaAtividade = (Tipologia) params.get("tipologiaAtividade");
		@SuppressWarnings("unchecked")
		List<Integer> listaIdeTipologiaAtividade = (List<Integer>) params.get("listaIdeTipologiaAtividade");

		if (!Util.isNull(tipologiaAtividade) || !Util.isNullOuVazio(listaIdeTipologiaAtividade)) {
			criteria
			.createAlias("req.requerimentoTipologiaCollection","rt", JoinType.LEFT_OUTER_JOIN)
			.createAlias("rt.ideUnidadeMedidaTipologiaGrupo","umtg", JoinType.LEFT_OUTER_JOIN)
			.createAlias("umtg.ideTipologiaGrupo","tg", JoinType.LEFT_OUTER_JOIN)
			.createAlias("tg.ideTipologia","t", JoinType.LEFT_OUTER_JOIN)
			.add(Restrictions.eq("tg.indExcluido", false))
			.add(Restrictions.eq("t.indExcluido", false))
			;

			if(!Util.isNull(tipologiaAtividade)) {
				criteria.add(Restrictions.eq("t.ideTipologia", tipologiaAtividade.getIdeTipologia()));
			}
			else{
				criteria.add(Restrictions.in("t.ideTipologia", listaIdeTipologiaAtividade));

			}
		}
	}

	private void adicionarProjection(DetachedCriteria criteria) {
		String query = " (CASE WHEN proc24_.ide_processo is not null THEN "
				+ "(CASE WHEN (SELECT 1 FROM processo_reenquadramento WHERE ide_processo = proc24_.ide_processo order by ide_processo_reenquadramento desc limit 1) = 1 THEN true "
				+ "ELSE false END) "
				+ "ELSE false END) AS processoReenquadrado";
		
		
		Projection projection =
				Projections.projectionList()
				.add(Projections.groupProperty("req.ideRequerimento"),"requerimento.ideRequerimento")
				.add(Projections.groupProperty("req.numRequerimento"),"requerimento.numRequerimento")
				.add(Projections.groupProperty("req.dtcCriacao"),"requerimento.dtcCriacao")
				.add(Projections.groupProperty("req.dtcFinalizacao"),"requerimento.dtcFinalizacao")
				.add(Projections.groupProperty("req.indEstadoEmergencia"),"requerimento.indEstadoEmergencia")
				.add(Projections.groupProperty("req.desEmail"),"requerimento.desEmail")
				.add(Projections.groupProperty("req.nomContato"),"requerimento.nomContato")
				.add(Projections.groupProperty("req.numTelefone"),"requerimento.numTelefone")
				.add(Projections.groupProperty("req.ideEnderecoContato.ideEndereco"),"requerimento.ideEnderecoContato.ideEndereco")
				.add(Projections.groupProperty("proc.ideProcesso"),"processo.ideProcesso")
				.add(Projections.groupProperty("proc.numProcesso"),"processo.numProcesso")
				.add(Projections.groupProperty("orgao.ideOrgao"),"requerimento.ideOrgao.ideOrgao")
				.add(Projections.groupProperty("orgao.codOrgao"),"requerimento.ideOrgao.codOrgao")
				.add(Projections.groupProperty("pessoa.idePessoa"),"pessoa.idePessoa")
				.add(Projections.groupProperty("pf.idePessoaFisica"),"pessoa.pessoaFisica.idePessoaFisica")
				.add(Projections.groupProperty("pf.nomPessoa"),"pessoa.pessoaFisica.nomPessoa")
				.add(Projections.groupProperty("pf.numCpf"),"pessoa.pessoaFisica.numCpf")
				.add(Projections.groupProperty("pj.idePessoaJuridica"),"pessoa.pessoaJuridica.idePessoaJuridica")
				.add(Projections.groupProperty("pj.nomRazaoSocial"),"pessoa.pessoaJuridica.nomRazaoSocial")
				.add(Projections.groupProperty("pj.numCnpj"),"pessoa.pessoaJuridica.numCnpj")
				.add(Projections.groupProperty("emp.ideEmpreendimento"),"empreendimento.ideEmpreendimento")
				.add(Projections.groupProperty("emp.nomEmpreendimento"),"empreendimento.nomEmpreendimento")
				.add(Projections.groupProperty("emp.indConversaoTcraLac"),"empreendimento.indConversaoTcraLac")
				.add(Projections.groupProperty("emp.indCessionario"),"empreendimento.indCessionario")
				.add(Projections.groupProperty("empReq.indDla"),"indDla")
				.add(Projections.groupProperty("status.ideStatusRequerimento"),"statusRequerimento.ideStatusRequerimento")
				.add(Projections.groupProperty("status.nomStatusRequerimento"),"statusRequerimento.nomStatusRequerimento")
				.add(Projections.groupProperty("pTra.idePessoa"),"pessoaTramitacao.idePessoa")
				.add(Projections.groupProperty("pTraF.nomPessoa"),"pessoaTramitacao.pessoaFisica.nomPessoa")
				.add(Projections.groupProperty("pTraJ.nomRazaoSocial"),"pessoaTramitacao.pessoaJuridica.nomRazaoSocial")
				.add(Projections.groupProperty("lac.ideLac"),"lac.ideLac")
				.add(Projections.groupProperty("lac.ideDocumentoObrigatorio.ideDocumentoObrigatorio"),"lac.ideDocumentoObrigatorio.ideDocumentoObrigatorio")

				.add(Projections.sqlGroupProjection("coalesce(lm14_.ide_municipio, bm16_.ide_municipio) as ide_municipio_", "ide_municipio_", new String[] {"ide_municipio_"}, new Type[] {StandardBasicTypes.INTEGER}),"municipio.ideMunicipio")
				.add(Projections.sqlGroupProjection("coalesce(lm14_.nom_municipio, bm16_.nom_municipio) as nom_municipio_", "nom_municipio_", new String[] {"nom_municipio_"}, new Type[] {StandardBasicTypes.STRING}),"municipio.nomMunicipio")
				.add(Projections.sqlGroupProjection("coalesce(lm14_.ind_estado_emergencia, bm16_.ind_estado_emergencia) as ind_estado_emergencia_", "ind_estado_emergencia_", new String[] {"ind_estado_emergencia_"}, new Type[] {StandardBasicTypes.BOOLEAN}),"municipio.indEstadoEmergencia")
				.add(Projections.sqlGroupProjection("(SELECT coalesce(ide_tipo_solicitacao = 13, FALSE) FROM solicitacao_administrativo WHERE ide_requerimento = this_.ide_requerimento AND ide_tipo_solicitacao = 13 GROUP BY ide_tipo_solicitacao) AS ind_tla_", "ind_tla_", new String[] {"ind_tla_"}, new Type[] {StandardBasicTypes.BOOLEAN}),"indTla")
				
				.add(Projections.sqlProjection(query, new String[] {"processoReenquadrado"}, new Type[] {BooleanType.INSTANCE}));

		;
		
		criteria
		.setProjection(projection)
		.setResultTransformer(new AliasToNestedBeanResultTransformer(RequerimentoDTO.class))
		;
	}

	@Override
	public Boolean verificaRequerimentoEmpreendimentoExistente(Empreendimento empreendimento) throws Exception {
		StringBuilder lSql = new StringBuilder();
		lSql.append("SELECT req ");
		lSql.append("FROM Requerimento req ");
		lSql.append("INNER JOIN req.tramitacaoRequerimentoCollection tran ");
		lSql.append("INNER JOIN tran.ideStatusRequerimento status ");

		lSql.append("left JOIN req.empreendimentoRequerimentoCollection empReq ");
		lSql.append("left JOIN empReq.ideEmpreendimento emp ");

		lSql.append("WHERE req.indExcluido = :indExcluido and req.numRequerimento is not null ");

		lSql.append("AND tran.dtcMovimentacao = (SELECT MAX(req_tran.dtcMovimentacao) FROM req.tramitacaoRequerimentoCollection req_tran)");

		if (!Util.isNullOuVazio(empreendimento)) {
			lSql.append(" AND emp.ideEmpreendimento = :ideEmpreendimento");
		}

		lSql.append(" AND status.ideStatusRequerimento not in (8, 9, 16)");
		lSql.append(" ORDER BY req.dtcCriacao");

		EntityManager lEntityManager = DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createQuery(lSql.toString());

		lQuery.setParameter("indExcluido", Boolean.FALSE);

		if (!Util.isNullOuVazio(empreendimento)) {
			lQuery.setParameter("ideEmpreendimento", empreendimento.getIdeEmpreendimento());
		}

		final List<Requerimento> collRequerimento = new ArrayList<Requerimento>();
		@SuppressWarnings("unchecked")
		List<Object[]> result = lQuery.getResultList();

		for (Object resultElement : result) {
			Requerimento lRequerimento = (Requerimento) resultElement;
			collRequerimento.add(lRequerimento);
		}

		return !collRequerimento.isEmpty();
	}

	@Override
	public Integer countListarRequerimento(Map<String, Object> params) throws Exception {

		Integer qtdRequerimentos =  requerimentoDAO.count(getCriteriaConsultaRequerimento(params));

		return qtdRequerimentos;
	}


	/**
	 * [IMPORTADO]
	 *
	 * @param lista
	 * @return List AtoAmbiental
	 * @throws Exception
	 * @see RequerimentoUnicoDAOImpl
	 */
	private List<AtoAmbiental> getListaAtosEnquadrados(List<RequerimentoDTO> lista) throws Exception {

		List<Integer> ides = getIdesDTo(lista);

		if (ides.isEmpty()) {
			return new ArrayList<AtoAmbiental>();
		}

		DetachedCriteria criteria = DetachedCriteria.forClass(AtoAmbiental.class)
				.createAlias("enquadramentoCollection","enq", JoinType.INNER_JOIN)
				.createAlias("enq.ideRequerimento", "req",JoinType.INNER_JOIN)
				.createAlias("enq.ideRequerimentoUnico", "reqUnico",JoinType.LEFT_OUTER_JOIN)
				.setProjection(Projections.projectionList()
						.add(Projections.property("ideAtoAmbiental"),"ideAtoAmbiental")
						.add(Projections.property("indDeclaratorio"),"indDeclaratorio")
						.add(Projections.property("sglAtoAmbiental"),"sglAtoAmbiental")
						.add(Projections.property("nomAtoAmbiental"),"nomAtoAmbiental")
						.add(Projections.property("req.ideRequerimento"),"enquadramentoRequerimento.ideRequerimento.ideRequerimento")
						.add(Projections.property("reqUnico.ideRequerimentoUnico"),"enquadramentoRequerimento.ideRequerimentoUnico.ideRequerimentoUnico"));

		criteria.add(Restrictions.in("req.ideRequerimento", ides));
		criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(AtoAmbiental.class));
		return this.atoAmbientalDao.listarPorCriteria(criteria);
	}

	/**
	 * [IMPORTADO]
	 *
	 * @param lista
	 * @return Integer
	 * @see RequerimentoUnicoDAOImpl
	 */
	private List<Integer> getIdesDTo(List<RequerimentoDTO> lista) {
		List<Integer> ides = new ArrayList<Integer>();
		for (RequerimentoDTO req : lista) {
			ides.add(req.getRequerimento().getIdeRequerimento());
		}
		return ides;
	}

	/**
	 * [IMPORTADO]
	 *
	 * @param atosAmbientais
	 * @param req
	 * @see RequerimentoUnicoDAOImpl
	 */
	private void preencheAtosAmbientais(List<AtoAmbiental> atosAmbientais, Requerimento req) {
		for (AtoAmbiental atoAmbiental : atosAmbientais) {
			if(req.getIdeRequerimento().equals(atoAmbiental.getEnquadramentoRequerimento().getIdeRequerimento().getIdeRequerimento())){
				if(Util.isNullOuVazio(req.getAtosAmbientais())) {
					req.setAtosAmbientais(new ArrayList<AtoAmbiental>());
				}
				req.getAtosAmbientais().add(atoAmbiental);
			}
		}
	}

	/**
	 * [IMPORTADO]
	 *
	 * @return numeroDeRequerimentos
	 * @throws Exception
	 * @see RequerimentoUnicoDAOImpl
	 */
	public Integer countListarRequerimentoUnicoExterno(Map<String, Object> params) throws Exception {

		/*if (!Util.isNullOuVazio(requerente)) {
			listaPessoas.add(requerente.getIdePessoa());
		}

		Integer qtdRequerimentos =	requerimentoDAO.count(getCriteriaConsultaRequerimento(
				null, null,
				requerimento,
				dataInicio,
				dataFim,
				empreendimento,
				requerente,
				statusRequerimento,
				municipio,
				municipios, true, listaPessoas,
				false, params));*/
		Integer qtdRequerimentos =	requerimentoDAO.count(getCriteriaConsultaRequerimento(params));

		return qtdRequerimentos;

	}

	@Override
	public List<RequerimentoWS> consultaRequerimentoWs(Integer requerente, Integer ideEmpreendimento, String numRequerimento, Integer status, Date dtcInicial,Date dtcFinal, Integer first, Integer max, List<Integer> idesPessoas) throws Exception{
		StringBuilder lSql = new StringBuilder();
		lSql.append("SELECT new br.gov.ba.ws.entity.RequerimentoWS(req.ideRequerimento, req.numRequerimento, status.ideStatusRequerimento, status.nomStatusRequerimento, req.dtcCriacao, pf.idePessoaFisica, pf.nomPessoa,pf.numCpf, pj.idePessoaJuridica, pj.nomRazaoSocial, pj.numCnpj, emp.ideEmpreendimento, emp.nomEmpreendimento) from Requerimento req ");
		lSql.append("INNER JOIN req.tramitacaoRequerimentoCollection tran ");
		//lSql.append("LEFT JOIN req.empreendimentoCollection emp ");

		lSql.append("left JOIN req.empreendimentoRequerimentoCollection empReq ");
		lSql.append("left JOIN empReq.ideEmpreendimento emp ");

		lSql.append("INNER JOIN tran.ideStatusRequerimento status ");
		lSql.append("INNER JOIN req.requerimentoPessoaCollection reqPessoa ");
		lSql.append("INNER JOIN reqPessoa.pessoa pessoa ");
		lSql.append("left JOIN pessoa.pessoaFisica pf ");
		lSql.append("left JOIN pessoa.pessoaJuridica pj ");
		lSql.append("WHERE req.indExcluido = false and tran.dtcMovimentacao = (SELECT MAX(req_tran.dtcMovimentacao) FROM req.tramitacaoRequerimentoCollection req_tran) ");
		lSql.append(" AND reqPessoa.ideTipoPessoaRequerimento.ideTipoPessoaRequerimento = 1");
		if(!Util.isNullOuVazio(idesPessoas)){
			lSql.append(" AND reqPessoa.pessoa.idePessoa in (:idesPessoas)");
		}
		if (!Util.isNullOuVazio(numRequerimento)) {
			lSql.append(" AND lower(req.numRequerimento) LIKE lower(:numRequerimento)");
		}
		if (!Util.isNullOuVazio(requerente)) {
			lSql.append(" AND (pf.idePessoaFisica = :requerente");
			lSql.append(" OR  pj.idePessoaJuridica = :requerente)");
		}
		if (!Util.isNullOuVazio(ideEmpreendimento)) {
			lSql.append(" AND emp.ideEmpreendimento = :ideEmpreendimento");
		}
		if (!Util.isNullOuVazio(status)) {
			lSql.append(" AND status.ideStatusRequerimento = :status");
		}
		if (!Util.isNullOuVazio(dtcInicial) && !Util.isNullOuVazio(dtcFinal) ) {
			lSql.append(" AND req.dtcCriacao BETWEEN :dtcInicial AND :dtcFinal");
		}

		EntityManager lEntityManager = DAOFactory.getEntityManager();
		lSql.append(" order by req.ideRequerimento desc ");

		Query lQuery = lEntityManager.createQuery(lSql.toString());
		if (max!=null && first!=null) {
			lQuery.setMaxResults(max);
			lQuery.setFirstResult(first);
		}
		if (!Util.isNullOuVazio(numRequerimento)) {
			lQuery.setParameter("numRequerimento", "%" + numRequerimento + "%");
		}
		if (!Util.isNullOuVazio(requerente)) {
			lQuery.setParameter("requerente", requerente);
		}
		if (!Util.isNullOuVazio(ideEmpreendimento)) {
			lQuery.setParameter("ideEmpreendimento",  ideEmpreendimento);
		}
		if (!Util.isNullOuVazio(status)) {
			lQuery.setParameter("status", status);
		}
		if (!Util.isNullOuVazio(dtcInicial) && !Util.isNullOuVazio(dtcFinal) ) {
			lQuery.setParameter("dtcInicial",dtcInicial);
			lQuery.setParameter("dtcFinal",dtcFinal);
		}
		if(!Util.isNullOuVazio(idesPessoas)){
			lQuery.setParameter("idesPessoas", idesPessoas);
		}

		@SuppressWarnings("unchecked")
		List<RequerimentoWS> listRequerimento = lQuery.getResultList();
		return listRequerimento;
	}

	@Override
	public Long countConsultaRequerimentoWs(Integer requerente,	Integer ideEmpreendimento, String numRequerimento, Integer status,
			Date dtcInicial, Date dtcFinal,
			List<Integer> idesPessoas) {

		StringBuilder lSql = new StringBuilder();
		lSql.append("SELECT  count (req.ideRequerimento) from Requerimento req ");
		lSql.append("INNER JOIN req.tramitacaoRequerimentoCollection tran ");
		lSql.append("INNER JOIN req.requerimentoPessoaCollection reqPessoa ");
		//lSql.append("LEFT JOIN req.empreendimentoCollection emp ");

		lSql.append("left JOIN req.empreendimentoRequerimentoCollection empReq ");
		lSql.append("left JOIN empReq.ideEmpreendimento emp ");



		lSql.append("INNER JOIN tran.ideStatusRequerimento status ");
		lSql.append("INNER JOIN reqPessoa.pessoa pessoa ");
		lSql.append("left JOIN pessoa.pessoaFisica pf ");
		lSql.append("left JOIN pessoa.pessoaJuridica pj ");
		lSql.append("WHERE req.indExcluido = false and tran.dtcMovimentacao = (SELECT MAX(req_tran.dtcMovimentacao) FROM req.tramitacaoRequerimentoCollection req_tran) ");
		lSql.append(" AND reqPessoa.ideTipoPessoaRequerimento.ideTipoPessoaRequerimento = 1");
		if(!Util.isNullOuVazio(idesPessoas)){
			lSql.append(" AND reqPessoa.pessoa.idePessoa in (:idesPessoas)");
		}
		if (!Util.isNullOuVazio(numRequerimento)) {
			lSql.append(" AND lower(req.numRequerimento) LIKE lower(:numRequerimento)");
		}
		if (!Util.isNullOuVazio(requerente)) {
			lSql.append(" AND (pf.idePessoaFisica = :requerente");
			lSql.append(" OR  pj.idePessoaJuridica = :requerente)");
		}
		if (!Util.isNullOuVazio(ideEmpreendimento)) {
			lSql.append(" AND emp.ideEmpreendimento = :ideEmpreendimento");
		}
		if (!Util.isNullOuVazio(status)) {
			lSql.append(" AND status.ideStatusRequerimento = :status");
		}
		if (!Util.isNullOuVazio(dtcInicial) && !Util.isNullOuVazio(dtcFinal) ) {
			lSql.append(" AND req.dtcCriacao BETWEEN :dtcInicial AND :dtcFinal");
		}

		EntityManager lEntityManager = DAOFactory.getEntityManager();

		Query lQuery = lEntityManager.createQuery(lSql.toString());

		if (!Util.isNullOuVazio(numRequerimento)) {
			lQuery.setParameter("numRequerimento", "%" + numRequerimento + "%");
		}
		if (!Util.isNullOuVazio(requerente)) {
			lQuery.setParameter("requerente", requerente);
		}
		if (!Util.isNullOuVazio(ideEmpreendimento)) {
			lQuery.setParameter("ideEmpreendimento",  ideEmpreendimento);
		}
		if (!Util.isNullOuVazio(status)) {
			lQuery.setParameter("status", status);
		}
		if (!Util.isNullOuVazio(dtcInicial) && !Util.isNullOuVazio(dtcFinal) ) {
			lQuery.setParameter("dtcInicial",dtcInicial);
			lQuery.setParameter("dtcFinal",dtcFinal);
		}
		if(!Util.isNullOuVazio(idesPessoas)){
			lQuery.setParameter("idesPessoas", idesPessoas);
		}

		Long quantidadeRequerimento = (Long) lQuery.getSingleResult();
		return quantidadeRequerimento;

	}

	private boolean getIsPagination(Map<String, Object> params) {
		Boolean b = (Boolean) params.get("isPagination");
		return b == null ? false : b;
	}

	private boolean getIsCount(Map<String, Object> params) {
		Boolean b = (Boolean) params.get("isCount");
		return b == null ? false : b;
	}
}
