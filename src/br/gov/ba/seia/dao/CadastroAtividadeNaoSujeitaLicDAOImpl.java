package br.gov.ba.seia.dao;

import java.util.Calendar;
import java.util.Date;
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
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLic;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLicStatus;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLicTipoStatus;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.PesquisaMineral;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.TipoAtividadeNaoSujeitaLicenciamento;
import br.gov.ba.seia.entity.TipoEndereco;
import br.gov.ba.seia.enumerator.TipoEnderecoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Util;

/**
 * @author eduardo.fernandes
 * @since 02/12/2016
 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CadastroAtividadeNaoSujeitaLicDAOImpl {

	@Inject
	private IDAO<CadastroAtividadeNaoSujeitaLic> dao;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(CadastroAtividadeNaoSujeitaLic cadastro) {
		dao.salvarOuAtualizar(cadastro);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String getUltimoNumeroCadastro() {
		Map<String, Object> params = new HashMap<String, Object>();

		StringBuilder sql = new StringBuilder();
		sql.append("select max(substr(num_cadastro, 10, 6)) "
				+ "from cadastro_atividade_nao_sujeita_lic "
				+ "where substr(num_cadastro, 0, 5) = '" + Calendar.getInstance().get(Calendar.YEAR) + "'");

		return dao.executarFuncaoNativeQuery(sql.toString(), params);
	}

	private boolean getIsCount(Map<String, Object> params) {
		Boolean b = (Boolean) params.get("isCount");
		return b == null ? false : b;
	}

	/**
	 * Método que irá retonar os {@link CadastroAtividadeNaoSujeitaLic} que se encaixam nos filtros selecionados previamente.
	 * 
	 * @author eduardo.fernandes
	 * @since 07/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param map
	 * @return
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CadastroAtividadeNaoSujeitaLic> consultar(Map<String, Object> map) {
		return dao.listarPorCriteriaDemanda(getCriteria(map), ((Integer)map.get("first")), ((Integer)map.get("pageSize")));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public  List<CadastroAtividadeNaoSujeitaLic> consultarCount(Map<String, Object> map) {
		return dao.listarPorCriteria(getCriteria(map));
	}
	
	/**
	 * Método que irá montar a {@link DetachedCriteria} da consulta de {@link CadastroAtividadeNaoSujeitaLic}.
	 * 
	 * @author eduardo.fernandes
	 * @param map
	 * @since 07/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @return
	 */
	private DetachedCriteria getCriteria(Map<String, Object> map) {


		DetachedCriteria criteria = DetachedCriteria.forClass(CadastroAtividadeNaoSujeitaLic.class, "cad");

		adicionarJoins(criteria);
		adicionarFiltros(criteria, map);

		criteria.addOrder(Order.desc("cad.numCadastro"));
		adicionarProjection(criteria);
		return criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(CadastroAtividadeNaoSujeitaLic.class));


	}


	/**
	 * Método que irá retonar a quantidade de registros encontrados.
	 * 
	 * @author eduardo.fernandes
	 * @since 07/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer count(Map<String, Object> map)  {
		return dao.count(getCriteria(map));
	}

	/**
	 * Método responsável por manipular a {@link DetachedCriteria} e adicionar os JOINs a consulta.
	 * 
	 * @author eduardo.fernandes
	 * @since 07/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param criteria
	 */
	private void adicionarJoins(DetachedCriteria criteria) {
		criteria
				.createAlias("cad.idePessoaFisicaCadastro", "pdCadastro", JoinType.INNER_JOIN)
				.createAlias("cad.idePessoaRequerente", "req", JoinType.INNER_JOIN)
				.createAlias("req.pessoaFisica", "pf", JoinType.LEFT_OUTER_JOIN)
				.createAlias("req.pessoaJuridica", "pj", JoinType.LEFT_OUTER_JOIN)
				.createAlias("cad.tipoAtividadeNaoSujeitaLicenciamento", "atividade", JoinType.INNER_JOIN)
				.createAlias("cad.cadastroAtividadeNaoSujeitaLicStatus", "cadStatus", JoinType.INNER_JOIN)
				.createAlias("cadStatus.cadastroAtividadeNaoSujeitaLicTipoStatus", "status", JoinType.INNER_JOIN)
				.createAlias("cad.ideEmpreendimento", "emp", JoinType.INNER_JOIN)
				.createAlias("emp.enderecoEmpreendimentoCollection", "endem", JoinType.LEFT_OUTER_JOIN)
				.createAlias("endem.ideEndereco", "ende", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ende.ideLogradouro", "log", JoinType.LEFT_OUTER_JOIN)
				.createAlias("log.ideMunicipio", "lm", JoinType.LEFT_OUTER_JOIN)
				.createAlias("cad.ideCertificado", "cert", JoinType.LEFT_OUTER_JOIN)
				;
	}

	/**
	 * Método que irá adicionar os filtros a {@link DetachedCriteria}.
	 * 
	 * @author eduardo.fernandes
	 * @since 07/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param criteria
	 * @param map
	 */
	private void adicionarFiltros(DetachedCriteria criteria, Map<String, Object> map) {

		if (ContextoUtil.getContexto().getUsuarioLogado().isUsuarioExterno()) {
			criteria.add(Restrictions.in("req.idePessoa", (List<Integer>) map.get("listaPessoas")));
		} 
		else {
			criteria.add(Restrictions.isNotNull("cad.numCadastro"));
		}
		
		criteria
				.add(Restrictions.eq("cad.indExcluido", false))

				.add(Restrictions.eq("endem.ideTipoEndereco", new TipoEndereco(TipoEnderecoEnum.LOCALIZACAO.getId()))) // ??

				.add(Property.forName("cadStatus.dtcStatus").eq(
						DetachedCriteria.forClass(CadastroAtividadeNaoSujeitaLicStatus.class, "cadStatus")
								.createAlias("cadStatus.cadastroAtividadeNaoSujeitaLic", "cad2", JoinType.INNER_JOIN)
								.add(Restrictions.eqProperty("cad2.ideCadastroAtividadeNaoSujeitaLic", "cad.ideCadastroAtividadeNaoSujeitaLic"))
								.setProjection(Projections.max("dtcStatus"))
						)
				);

		filtrarPorRequerente(criteria, map);
		filtrarPorNumeroCadastro(criteria, map);

		filtrarPorAtividade(criteria, map);
		filtrarPorStatus(criteria, map);
		filtrarPorPeriodo(criteria, map);

		filtrarPorLocalidade(criteria, map);
		filtrarPorNomEmpreendimento(criteria, map);
	}

	/**
	 * Método que irá adicionar a {@link Restrictions} de {@link Pessoa} à {@link DetachedCriteria}.
	 * 
	 * @author eduardo.fernandes
	 * @since 07/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param criteria
	 * @param map
	 */
	private void filtrarPorRequerente(DetachedCriteria criteria, Map<String, Object> map) {
		Pessoa req = (Pessoa) map.get("requerente");
		if (!Util.isNullOuVazio(req)) {
			criteria.add(Restrictions.or(
					Restrictions.eq("pf.idePessoaFisica", req.getIdePessoa()),
					Restrictions.eq("pj.idePessoaJuridica", req.getIdePessoa())
					));
		}
	}

	/**
	 * Método que irá adicionar a {@link Restrictions} de número do cadastro à {@link DetachedCriteria}.
	 * 
	 * @author eduardo.fernandes
	 * @since 07/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param criteria
	 * @param map
	 */
	private void filtrarPorNumeroCadastro(DetachedCriteria criteria, Map<String, Object> map) {
		String num = (String) map.get("numeroCadastro");
		if (!Util.isNullOuVazio(num)) {
			criteria.add(Restrictions.ilike("cad.numCadastro", num, MatchMode.ANYWHERE));
		}
	}

	/**
	 * Método que irá adicionar a {@link Restrictions} de {@link TipoAtividadeNaoSujeitaLicenciamento} à {@link DetachedCriteria}.
	 * 
	 * @author eduardo.fernandes
	 * @since 07/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param criteria
	 * @param map
	 */
	private void filtrarPorAtividade(DetachedCriteria criteria, Map<String, Object> map) {
		TipoAtividadeNaoSujeitaLicenciamento atividade = (TipoAtividadeNaoSujeitaLicenciamento) map.get("atividade");
		if (!Util.isNullOuVazio(atividade)) {
			criteria.add(Restrictions.eq("atividade.ideTipoAtividadeNaoSujeitaLicenciamento", atividade.getIdeTipoAtividadeNaoSujeitaLicenciamento()));
		}
	}

	/**
	 * Método que irá adicionar a {@link Restrictions} de {@link CadastroAtividadeNaoSujeitaLicTipoStatus} à {@link DetachedCriteria}.
	 * 
	 * @author eduardo.fernandes
	 * @since 07/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param criteria
	 * @param map
	 */
	private void filtrarPorStatus(DetachedCriteria criteria, Map<String, Object> map) {
		CadastroAtividadeNaoSujeitaLicTipoStatus status = (CadastroAtividadeNaoSujeitaLicTipoStatus) map.get("cadStatus");
		if (!Util.isNullOuVazio(status)) {
			criteria.add(Restrictions.eq("status.ideCadastroAtividadeNaoSujeitaLicTipoStatus", status.getIdeCadastroAtividadeNaoSujeitaLicTipoStatus()));
		}
	}

	/**
	 * Método que irá adicionar a {@link Restrictions} de período inicial e/ou final à {@link DetachedCriteria}.
	 * 
	 * @author eduardo.fernandes
	 * @since 07/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param criteria
	 * @param map
	 */
	private void filtrarPorPeriodo(DetachedCriteria criteria, Map<String, Object> map) {
		Date dataFim = (Date) map.get("periodoFim");
		Date dataInicio = (Date) map.get("periodoInicio");
		if (!Util.isNull(dataInicio)) {
			if (Util.isNull(dataFim)) {
				dataFim = new Date();
			}
			criteria.add(Restrictions.between("cad.dtcCadastro", dataInicio, dataFim));
		}
	}

	/**
	 * Método que irá adicionar a {@link Restrictions} de {@link Municipio} à {@link DetachedCriteria}.
	 * 
	 * @author eduardo.fernandes
	 * @since 07/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param criteria
	 * @param map
	 */
	private void filtrarPorLocalidade(DetachedCriteria criteria, Map<String, Object> map) {
		Municipio municipio = (Municipio) map.get("localidade");
		if (!Util.isNull(municipio)) {
			criteria.add(Restrictions.eq("lm.ideMunicipio", municipio.getIdeMunicipio()));
		}
	}

	/**
	 * Método que irá adicionar a {@link Restrictions} de nome do {@link Empreendimento} à {@link DetachedCriteria}.
	 * 
	 * @author eduardo.fernandes
	 * @since 07/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param criteria
	 * @param map
	 */
	private void filtrarPorNomEmpreendimento(DetachedCriteria criteria, Map<String, Object> map) {
		String nome = (String) map.get("nomEmpreendimento");
		if (!Util.isNullOuVazio(nome)) {
			nome = nome.toLowerCase().trim();
			criteria.add(Restrictions.ilike("emp.nomEmpreendimento", nome, MatchMode.ANYWHERE));
		}
	}

	/**
	 * Método que irá montar a {@link Projections}  à {@link DetachedCriteria} para listar as seguintes informações na tela de CONSULTA:
	 * 
	 * <ul>
	 * 	<li> CadastroAtividadeNaoSujeitaLic
	 * 	<li> ideCadastroAtividadeNaoSujeitaLic
	 * 	<li> numCadastro
	 * 	<li> dtcCadastro
	 * 	<li> indExcluido </li>
	 * </ul>
	 * 
	 * <ul>
	 * 	<li> TipoAtividadeNaoSujeitaLicenciamento
	 * 	<li> ideTipoAtividadeNaoSujeitaLicenciamento
	 * </ul>
	 * 
	 * <ul>
	 * 	<li> PessoaFisica
	 * 	<li> idePessoaFisica
	 * 	<li> nomPessoa
	 * 	<li> numCpf
	 * </ul>
	 * 
	 * <ul>
	 * 	<li> Pessoa
	 * 	<li> idePessoa
	 * 	<li> nome_razaoSocial
	 * 	<li> cpf_cnpj
	 * </ul>
	 * 
	 * <ul>
	 * 	<li> Empreendimento
	 * 	<li> ideEmpreendimento
	 * 	<li> nomEmpreendimento
	 * </ul>
	 * 
	 * <ul>
	 * 	<li> Municipio
	 * 	<li> ideMunicipio
	 * 	<li> nomMunicipio
	 * </ul>
	 * 
	 * <ul>
	 * 	<li> CadastroAtividadeNaoSujeitaLicTipoStatus
	 * 	<li> ideCadastroAtividadeNaoSujeitaLicTipoStatus
	 * 	<li> nomTipoStatus
	 * </ul>
	 * 
	 * <ul>
	 * 	<li> Certificado
	 * 	<li> ideCertificado
	 * </ul>
	 * 
	 * @author eduardo.fernandes
	 * @since 07/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param criteria
	 */
	private void adicionarProjection(DetachedCriteria criteria) {
		Projection projection =
				Projections
						.projectionList()
						.add(Projections.groupProperty("cad.ideCadastroAtividadeNaoSujeitaLic"), "ideCadastroAtividadeNaoSujeitaLic")
						.add(Projections.groupProperty("cad.numCadastro"), "numCadastro")
						.add(Projections.groupProperty("cad.dtcCadastro"), "dtcCadastro")
						.add(Projections.groupProperty("cad.indExcluido"), "indExcluido")
						.add(Projections.groupProperty("cad.indPossuiCefir"),"indPossuiCefir")

						.add(Projections.groupProperty("cad.tipoAtividadeNaoSujeitaLicenciamento.ideTipoAtividadeNaoSujeitaLicenciamento"), "tipoAtividadeNaoSujeitaLicenciamento.ideTipoAtividadeNaoSujeitaLicenciamento")

						.add(Projections.groupProperty("pdCadastro.idePessoaFisica"), "idePessoaFisicaCadastro.idePessoaFisica")
						.add(Projections.groupProperty("pdCadastro.nomPessoa"), "idePessoaFisicaCadastro.nomPessoa")
						.add(Projections.groupProperty("pdCadastro.numCpf"), "idePessoaFisicaCadastro.numCpf")

						.add(Projections.groupProperty("req.idePessoa"), "idePessoaRequerente.idePessoa")

						.add(Projections.groupProperty("emp.ideEmpreendimento"), "ideEmpreendimento.ideEmpreendimento")
						.add(Projections.groupProperty("emp.nomEmpreendimento"), "ideEmpreendimento.nomEmpreendimento")

						.add(Projections.groupProperty("lm.ideMunicipio"), "ideEmpreendimento.municipio.ideMunicipio")
						.add(Projections.groupProperty("lm.nomMunicipio"), "ideEmpreendimento.municipio.nomMunicipio")

						.add(Projections.groupProperty("status.ideCadastroAtividadeNaoSujeitaLicTipoStatus"), "tipoStatus.ideCadastroAtividadeNaoSujeitaLicTipoStatus")
						.add(Projections.groupProperty("status.nomTipoStatus"), "tipoStatus.nomTipoStatus")
						
						.add(Projections.groupProperty("cert.ideCertificado"), "ideCertificado.ideCertificado")

						.add(Projections.sqlGroupProjection("coalesce(pf3_.nom_pessoa,pj4_.nom_razao_social) as nom_requerente_", "nom_requerente_", new String[] { "nom_requerente_" }, new Type[] { StandardBasicTypes.STRING }), "nomRequerente")
						.add(Projections.sqlGroupProjection("coalesce(pf3_.num_cpf,pj4_.num_cnpj) as num_cpf_cnpj_requerente_", "num_cpf_cnpj_requerente_", new String[] { "num_cpf_cnpj_requerente_" }, new Type[] { StandardBasicTypes.STRING }),
								"numCpfcnpjRequerente");
		criteria.setProjection(projection);
	}

	public CadastroAtividadeNaoSujeitaLic buscar(PesquisaMineral pesquisaMineral)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(PesquisaMineral.class, "pes")
				.createAlias("pes.cadastroAtividadeNaoSujeitaLic", "cad")
				.createAlias("cad.tipoAtividadeNaoSujeitaLicenciamento", "tipo")
				.createAlias("cad.ideEmpreendimento", "emp")
				.add(Restrictions.eq("cad.ideCadastroAtividadeNaoSujeitaLic", pesquisaMineral.getCadastroAtividadeNaoSujeitaLic().getIdeCadastroAtividadeNaoSujeitaLic()))
				.setProjection(getProjection()
				)
				.setResultTransformer(new AliasToNestedBeanResultTransformer(CadastroAtividadeNaoSujeitaLic.class));
		return dao.buscarPorCriteria(criteria);
	}

	/**
	 * PROJECTION como todas as informações de {@link CadastroAtividadeNaoSujeitaLic}
	 *  <ul>
	 *  {@link Empreendimento}
	 *  	<li> ideEmpreendimento </li> 
	 *  	<li> nomEmpreendimento </li>
	 *  {@link TipoAtividadeNaoSujeitaLicenciamento}
	 *  	<li> ideTipoAtividadeNaoSujeitaLicenciamento </li> 
	 *  	<li> nomAtividade </li>
	 * </ul>
	 * @author eduardo.fernandes 
	 * @since 15/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8193">#8193</a> 
	 * @return
	 */
	private ProjectionList getProjection() {
		return Projections.projectionList()
				.add(Projections.property("cad.ideCadastroAtividadeNaoSujeitaLic"), "ideCadastroAtividadeNaoSujeitaLic")
				.add(Projections.property("cad.dtcCadastro"), "dtcCadastro")
				.add(Projections.property("cad.idePessoaFisicaCadastro.idePessoaFisica"), "idePessoaFisicaCadastro.idePessoaFisica")
				.add(Projections.property("cad.idePessoaRequerente.idePessoa"), "idePessoaRequerente.idePessoa")
				.add(Projections.property("cad.numCadastro"), "numCadastro")
				.add(Projections.property("cad.indExcluido"), "indExcluido")
				.add(Projections.property("emp.ideEmpreendimento"), "ideEmpreendimento.ideEmpreendimento")
				.add(Projections.property("emp.nomEmpreendimento"), "ideEmpreendimento.nomEmpreendimento")
				.add(Projections.property("tipo.ideTipoAtividadeNaoSujeitaLicenciamento"), "tipoAtividadeNaoSujeitaLicenciamento.ideTipoAtividadeNaoSujeitaLicenciamento")
				.add(Projections.property("tipo.nomAtividade"), "tipoAtividadeNaoSujeitaLicenciamento.nomAtividade");
	}

	/**
	 * @author eduardo.fernandes 
	 * @since 15/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/">#</a>
	 * @param ide
	 * @return
	 * @throws Exception 
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CadastroAtividadeNaoSujeitaLic buscar(Integer ide) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CadastroAtividadeNaoSujeitaLic.class, "cad")
				.createAlias("cad.tipoAtividadeNaoSujeitaLicenciamento", "tipo")
				.createAlias("cad.ideEmpreendimento", "emp")
				.add(Restrictions.eq("cad.ideCadastroAtividadeNaoSujeitaLic", ide))
				.setProjection(getProjection()
				).setResultTransformer(new AliasToNestedBeanResultTransformer(CadastroAtividadeNaoSujeitaLic.class));
		return dao.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CadastroAtividadeNaoSujeitaLic buscarCadastroPorId(Integer ideCadastroAtividadeNaoSujeitaLic) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CadastroAtividadeNaoSujeitaLic.class);
		criteria.createAlias("ideEmpreendimento", "empreendimento");
		criteria.createAlias("idePessoaRequerente", "pessoaRequerente", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("pessoaRequerente.pessoaJuridica", "pessoaJuridica", JoinType.LEFT_OUTER_JOIN);
		
		criteria.add(Restrictions.eq("ideCadastroAtividadeNaoSujeitaLic", ideCadastroAtividadeNaoSujeitaLic));
		
		return dao.buscarPorCriteria(criteria);
		
	}
}
