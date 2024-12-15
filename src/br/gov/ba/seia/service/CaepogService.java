package br.gov.ba.seia.service;

import java.util.Calendar;
import java.util.Date;
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
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Caepog;
import br.gov.ba.seia.entity.CaepogCampo;
import br.gov.ba.seia.entity.CaepogStatus;
import br.gov.ba.seia.entity.CaepogTipoStatus;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.enumerator.CaepogTipoStatusEnum;
import br.gov.ba.seia.enumerator.TipoEnderecoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CaepogService {
	
	@Inject
	IDAO<Caepog> caepogDAO;

	@EJB
	private PermissaoPerfilService permissaoPerfilService;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCaepog(Caepog c)  {
		caepogDAO.salvarOuAtualizar(c);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(Caepog c)  {
		c.setIndExcluidoCaepog(true);
		caepogDAO.atualizar(c);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Caepog> listarPorCriteriaDemanda(Pessoa requerenteFiltro, Empreendimento empreendimentoFiltro, CaepogCampo caepogCampoSelecionadoFiltro,
			Municipio municipioSelecionadoFiltro, String numCaepogFiltro, CaepogTipoStatus caepogTipoStatusSelecionadoFiltro, Date periodoInicioFiltro,
			Date periodoFimFiltro, int first, int pageSize) {
		
		return caepogDAO.listarPorCriteriaDemanda(getCriteria(requerenteFiltro, empreendimentoFiltro, caepogCampoSelecionadoFiltro,
				municipioSelecionadoFiltro, numCaepogFiltro, caepogTipoStatusSelecionadoFiltro, periodoInicioFiltro, periodoFimFiltro), first, pageSize);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer count(Pessoa requerenteFiltro, Empreendimento empreendimentoFiltro, CaepogCampo caepogCampoSelecionadoFiltro,
			Municipio municipioSelecionadoFiltro, String numCaepogFiltro, CaepogTipoStatus caepogTipoStatusSelecionadoFiltro, Date periodoInicioFiltro,
			Date periodoFimFiltro) {
		
		List<Caepog> l = caepogDAO.listarPorCriteria(getCriteria(requerenteFiltro, empreendimentoFiltro, caepogCampoSelecionadoFiltro,
				municipioSelecionadoFiltro, numCaepogFiltro, caepogTipoStatusSelecionadoFiltro, periodoInicioFiltro, periodoFimFiltro));
		
		return l.size(); 
	}
	
	private DetachedCriteria getCriteria(Pessoa requerenteFiltro, Empreendimento empreendimentoFiltro, CaepogCampo caepogCampoSelecionadoFiltro,
			Municipio municipioSelecionadoFiltro, String numCaepogFiltro, CaepogTipoStatus caepogTipoStatusSelecionadoFiltro, Date periodoInicioFiltro,
			Date periodoFimFiltro) {
			
			DetachedCriteria criteria = DetachedCriteria
					.forClass(Caepog.class, "caepog")
					
					.createAlias("caepog.idePessoaRequerente", "pessoa", JoinType.INNER_JOIN)
					.createAlias("pessoa.pessoaFisica", "pessoaFisica", JoinType.LEFT_OUTER_JOIN)
					.createAlias("pessoa.pessoaJuridica", "pessoaJuridica", JoinType.LEFT_OUTER_JOIN)
					
					.createAlias("caepog.idePessoaFisicaCadastro", "pessoaFisicaCadastro", JoinType.INNER_JOIN)
					
					.createAlias("caepog.ideEmpreendimento", "empreendimento", JoinType.INNER_JOIN)
					.createAlias("empreendimento.enderecoEmpreendimentoCollection", "ee", JoinType.LEFT_OUTER_JOIN)
					.createAlias("ee.ideEndereco", "endereco", JoinType.LEFT_OUTER_JOIN)
					.createAlias("ee.ideTipoEndereco", "tipoEndereco", JoinType.LEFT_OUTER_JOIN)
					.createAlias("endereco.ideLogradouro", "logradouro", JoinType.LEFT_OUTER_JOIN)
					.createAlias("logradouro.ideMunicipio", "municipio", JoinType.LEFT_OUTER_JOIN)
					
					.createAlias("caepog.caepogStatusCollection", "status", JoinType.LEFT_OUTER_JOIN)
					.createAlias("status.ideCaepogTipoStatus", "tipoStatus", JoinType.LEFT_OUTER_JOIN);

			DetachedCriteria subCriteria = DetachedCriteria.forClass(CaepogStatus.class, "subStatus")
					.add(Property.forName("caepog.ideCaepog").eqProperty("subStatus.ideCaepog.ideCaepog"))
					.setProjection(Projections.max("ideCaepogStatus"));
			criteria.add(Property.forName("status.ideCaepogStatus").in(subCriteria));
			
			ProjectionList projecao = Projections.projectionList()
					.add(Projections.property("caepog.ideCaepog"), "ideCaepog")
					.add(Projections.property("caepog.indExcluidoCaepog"), "indExcluidoCaepog")
					.add(Projections.property("caepog.numCaepog"), "numCaepog")
					.add(Projections.property("caepog.dtcCriacao"), "dtcCriacao")
					
					.add(Projections.property("pessoa.idePessoa"), "idePessoaRequerente.idePessoa")
					.add(Projections.property("pessoaFisica.idePessoaFisica"), "idePessoaRequerente.pessoaFisica.idePessoaFisica")
					.add(Projections.property("pessoaFisica.nomPessoa"), "idePessoaRequerente.pessoaFisica.nomPessoa")
					.add(Projections.property("pessoaFisica.numCpf"), "idePessoaRequerente.pessoaFisica.numCpf")
					.add(Projections.property("pessoaJuridica.idePessoaJuridica"), "idePessoaRequerente.pessoaJuridica.idePessoaJuridica")
					.add(Projections.property("pessoaJuridica.nomRazaoSocial"), "idePessoaRequerente.pessoaJuridica.nomRazaoSocial")
					.add(Projections.property("pessoaJuridica.numCnpj"), "idePessoaRequerente.pessoaJuridica.numCnpj")
					
					.add(Projections.property("pessoaFisicaCadastro.idePessoaFisica"), "idePessoaFisicaCadastro.idePessoaFisica")
					.add(Projections.property("pessoaFisicaCadastro.nomPessoa"), "idePessoaFisicaCadastro.nomPessoa")
					
					.add(Projections.property("empreendimento.ideEmpreendimento"), "ideEmpreendimento.ideEmpreendimento")
					.add(Projections.property("empreendimento.nomEmpreendimento"), "ideEmpreendimento.nomEmpreendimento")
					
					.add(Projections.property("municipio.ideMunicipio"), "ideMunicipioTransient.ideMunicipio")
					.add(Projections.property("municipio.nomMunicipio"), "ideMunicipioTransient.nomMunicipio")
					
					.add(Projections.property("status.ideCaepogStatus"), "ideUltimoStatus.ideCaepogStatus")
					.add(Projections.property("status.dtcCaepogStatus"), "ideUltimoStatus.dtcCaepogStatus")
					.add(Projections.property("tipoStatus.ideCaepogTipoStatus"), "ideUltimoStatus.ideCaepogTipoStatus.ideCaepogTipoStatus")
					.add(Projections.property("tipoStatus.nomCaepogTipoStatus"), "ideUltimoStatus.ideCaepogTipoStatus.nomCaepogTipoStatus");
				
			criteria.setProjection(projecao)
					.setResultTransformer(new AliasToNestedBeanResultTransformer(Caepog.class));
		
		if(!Util.isNullOuVazio(requerenteFiltro)) {
			criteria.add(Restrictions.eq("pessoa.idePessoa", requerenteFiltro.getIdePessoa()));
			
		} else if(ContextoUtil.getContexto().getUsuarioLogado().isUsuarioExterno()) {
			criteria.add(Restrictions.in("pessoa.idePessoa", permissaoPerfilService.listarIdesPessoasAptas(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica())));
		}
		
		if(empreendimentoFiltro != null && !Util.isNullOuVazio(empreendimentoFiltro.getNomEmpreendimento())) {
			criteria.add(Restrictions.ilike("empreendimento.nomEmpreendimento", empreendimentoFiltro.getNomEmpreendimento(), MatchMode.ANYWHERE));
		}
		
		if(!Util.isNullOuVazio(caepogCampoSelecionadoFiltro)) {
			criteria.createAlias("caepog.caepogDefinicaoCampoCollection", "def", JoinType.LEFT_OUTER_JOIN)
					.createAlias("def.ideCaepogCampo", "campo", JoinType.LEFT_OUTER_JOIN)
					.add(Restrictions.eq("campo.ideCaepogCampo", caepogCampoSelecionadoFiltro.getIdeCaepogCampo()));
		}
		
		if(!Util.isNullOuVazio(municipioSelecionadoFiltro)) {
			criteria.add(Restrictions.eq("municipio.ideMunicipio", municipioSelecionadoFiltro.getIdeMunicipio()));
		}

		if(!Util.isNullOuVazio(numCaepogFiltro)) {
			criteria.add(Restrictions.ilike("caepog.numCaepog", numCaepogFiltro, MatchMode.ANYWHERE));
		}
		
		if(!Util.isNullOuVazio(caepogTipoStatusSelecionadoFiltro)) {
			criteria.add(Restrictions.eq("tipoStatus.ideCaepogTipoStatus", caepogTipoStatusSelecionadoFiltro.getIdeCaepogTipoStatus()));
		}
		
		//#7661 - Melhoria - Não exibir cadastros incompletos para o perfil "Atendente"
		if(ContextoUtil.getContexto().getUsuarioLogado().isAtende()) {
			criteria.add(Restrictions.ne("tipoStatus.ideCaepogTipoStatus", CaepogTipoStatusEnum.CADASTRO_INCOMPLETO.getId()));
		}
		
		if(!Util.isNullOuVazio(periodoInicioFiltro) && !Util.isNullOuVazio(periodoFimFiltro)) {
			
			periodoFimFiltro = Util.adicionarUmDia(periodoFimFiltro);
			
			criteria.add(Restrictions.between("caepog.dtcCriacao", periodoInicioFiltro, periodoFimFiltro));
		}
		
			criteria.add(Restrictions.eq("indExcluidoCaepog", false));
			criteria.add(Restrictions.eq("tipoEndereco.ideTipoEndereco", TipoEnderecoEnum.LOCALIZACAO.getId()));
			
			criteria.addOrder(Order.desc("numCaepog"));
			criteria.addOrder(Order.desc("ideCaepog"));
		
		return criteria;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String getUltimoNumeroCaepog() {
		Map<String,Object> params = new HashMap<String, Object>();
		
		StringBuilder sql = new StringBuilder();
		sql.append("select max(substr(num_caepog, 10, 6)) "
				+ "from caepog "
				+ "where substr(num_caepog, 0, 5) = '" + Calendar.getInstance().get(Calendar.YEAR) + "'");
		
		return caepogDAO.executarFuncaoNativeQuery(sql.toString(), params);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Caepog carregarPessoaRequerente(Caepog caepog) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Caepog.class)
			.createAlias("idePessoaRequerente", "idePessoaRequerente", JoinType.INNER_JOIN)
			.createAlias("idePessoaRequerente.pessoaFisica", "pessoaFisica", JoinType.LEFT_OUTER_JOIN)
			.createAlias("idePessoaRequerente.pessoaJuridica", "pessoaJuridica",JoinType.LEFT_OUTER_JOIN )
			.add(Restrictions.eq("ideCaepog", caepog.getIdeCaepog()));
				
		return caepogDAO.buscarPorCriteria(detachedCriteria);
	}
}