package br.gov.ba.seia.service;

import java.io.File;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
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
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Level;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.ControleProcessoAtoDAOImpl;
import br.gov.ba.seia.dao.DAOFactory;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.ProcessoDAOImpl;
import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.ArquivoProcesso;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.ComunicacaoRequerimento;
import br.gov.ba.seia.entity.ControleProcessoAto;
import br.gov.ba.seia.entity.ControleTramitacao;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EnquadramentoAtoAmbiental;
import br.gov.ba.seia.entity.Estado;
import br.gov.ba.seia.entity.Funcionario;
import br.gov.ba.seia.entity.GrupoProcesso;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.Orgao;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.ProcessoTramiteImovelRural;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.SolicitacaoAdministrativo;
import br.gov.ba.seia.entity.SolicitacaoAdministrativoAtoAmbiental;
import br.gov.ba.seia.entity.StatusFluxo;
import br.gov.ba.seia.entity.StatusProcessoAto;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.entity.TransferenciaAmbiental;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.enumerator.AreaEnum;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.ParametroEnum;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.enumerator.StatusProcessoAtoEnum;
import br.gov.ba.seia.enumerator.TipoEnderecoEnum;
import br.gov.ba.seia.enumerator.TipoPautaEnum;
import br.gov.ba.seia.enumerator.TipoPessoaRequerimentoEnum;
import br.gov.ba.seia.enumerator.TipoSolicitacaoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.ws.entity.ProcessoWS;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProcessoService {
	
	@Inject
	IDAO<Processo> daoProcesso;
	@Inject
	IDAO<ProcessoTramiteImovelRural> processoTramiteImovelRuralDAO;
	
	@Inject
	private IDAO<ProcessoAto> processoAtoDAO;
	
	@EJB
	private ProcessoDAOImpl processoDAO;
	@EJB 
	private ControleProcessoAtoDAOImpl controleProcessoAtoDAOImpl;
	
	@EJB
	private ArquivoProcessoService arquivoProcessoService;
	@EJB
	private EmpreendimentoService empreendimentoService;
	@EJB
	private EnquadramentoService enquadramentoService;
	@EJB
	private AtoAmbientalService atoAmbientalService;
	@EJB
	private ControleTramitacaoService controleTramitacaoService;
	@EJB
	private ProcessoAtoService processoAtoService;
	@EJB
	private ParametroService parametroService;
	@EJB
	private OrgaoService orgaoService;
	@EJB
	private RequerimentoService requerimentoService;
	@EJB
	private PautaService pautaService;
	@EJB
	private ComunicacaoRequerimentoService comunicacaoRequerimentoService;
	@EJB
	private EmailService emailService;
	@EJB
	private TransferenciaAmbientalService transferenciaAmbientalService;
	@EJB 
	private SolicitacaoAdministrativoService solicitacaoAdministrativoService;
	@EJB
	private ControleProcessoAtoService controleProcessoAtoService;
	@EJB
	private TipologiaService tipologiaService;
	
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarProcesso(Processo processo)  {
		daoProcesso.salvar(processo);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarProcesso(Processo processo)  {
		daoProcesso.atualizar(processo);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Processo> listarProcesso() {
		return daoProcesso.listarTodos();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean verificarExistenciaDeTlaOuArls(Processo processo) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("select p ");
		sql.append("from Processo p ");
		sql.append("     inner join p.processoAtoCollection pAto ");
		sql.append("where p = :processo ");
		sql.append("      and (pAto.atoAmbiental.ideAtoAmbiental = :TLA or pAto.atoAmbiental.ideAtoAmbiental = :ARLS ) ");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("processo", processo);
		params.put("ARLS", AtoAmbientalEnum.ARLS.getId());
		params.put("TLA", AtoAmbientalEnum.TLA.getId());
		
		try{
			processo = daoProcesso.buscarPorQuery(sql.toString(), params);
		}
		catch(Exception e){
			processo = null;
		}
		
		if(processo != null){
			return true;
		}
		
		return false;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isAtoAmbientalProcessoPorStatus(Processo processo, Integer ideAtoAmbiental, StatusProcessoAtoEnum statusProcessoAtoEnum) {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ProcessoAto.class)
				.createAlias("processo", "p", JoinType.INNER_JOIN)
				.createAlias("tipologia", "tipologia", JoinType.LEFT_OUTER_JOIN)
				.createAlias("atoAmbiental", "ato", JoinType.INNER_JOIN)
				.createAlias("controleProcessoAtoCollection", "controleProcessoAto", JoinType.LEFT_OUTER_JOIN);
		
		
		if(!Util.isNullOuVazio(processo.getIdeProcesso())){
			detachedCriteria.add(Restrictions.eq("p.ideProcesso", processo.getIdeProcesso()));
		}else{
			detachedCriteria.add(Restrictions.eq("p.numProcesso", processo.getNumProcesso()));
		}
			
		detachedCriteria
		    .add(Restrictions.eq("ato.ideAtoAmbiental", ideAtoAmbiental))
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
			
		DetachedCriteria subCriteria = DetachedCriteria.forClass(ControleProcessoAto.class)
			.createAlias("ideStatusProcessoAto", "ideStatusProcessoAto")
			.add(Property.forName("ideProcessoAto").eqProperty("controleProcessoAto.ideProcessoAto.ideProcessoAto"))
			.setProjection(Projections.max("ideStatusProcessoAto"));
		
		detachedCriteria
			.add(Property.forName("controleProcessoAto.ideStatusProcessoAto").in(subCriteria))
			.add(Restrictions.or(
					Restrictions.isNull("controleProcessoAto.ideStatusProcessoAto"),
					Restrictions.eq("controleProcessoAto.ideStatusProcessoAto.ideStatusProcessoAto", statusProcessoAtoEnum.getId())
				)
			);
		
		return !Util.isNullOuVazio(processoAtoDAO.buscarPorCriteria(detachedCriteria));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Processo> buscarProcesso(String numProcesso) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("numProcesso", numProcesso);
		return daoProcesso.buscarPorNamedQuery("Processo.findByNumProcesso", parametros);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isProcessoNoStatus(int ideProcesso, int ideStatusFluxo )  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Processo.class);
		criteria
			.createAlias("controleTramitacaoCollection", "ct", JoinType.INNER_JOIN)
			.add(Restrictions.eq("ideProcesso", ideProcesso))
			.add(Restrictions.eq("ct.ideStatusFluxo.ideStatusFluxo",ideStatusFluxo));
		
		Processo p = daoProcesso.buscarPorCriteria(criteria);
		
		if(!Util.isNullOuVazio(p)){
			return true;
		}else {
			return false;
		}
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Processo buscarProcessoPorCriteria(String numProcesso)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Processo.class);
		criteria
			.createAlias("ideRequerimento", "req", JoinType.INNER_JOIN)
			.createAlias("req.requerimentoPessoaCollection", "pessoasReq", JoinType.INNER_JOIN)
			.createAlias("pessoasReq.pessoa", "pessoa", JoinType.INNER_JOIN)
			.add(Restrictions.ilike("numProcesso", numProcesso, MatchMode.EXACT))
			.add(Restrictions.eq("pessoa.indExcluido", false))
		;
		
		Processo p = daoProcesso.buscarPorCriteria(criteria);
		
		if(!Util.isNullOuVazio(p) && !Util.isNullOuVazio(p.getIdeRequerimento().getRequerimentoPessoaCollection()))
			p.getIdeRequerimento().getRequerimentoPessoaCollection().iterator().next();
		return p;
	}
	
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Processo buscarProcessoPorNumero(String numProcesso)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Processo.class)
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideProcesso"), "ideProcesso")
				.add(Projections.property("numProcesso"), "numProcesso")
				.add(Projections.property("indExcluido"), "indExcluido")
				.add(Projections.property("dtcExcluido"), "dtcExcluido")
				.add(Projections.property("dtcFormacao"), "dtcFormacao")
				.add(Projections.property("textoPortaria"), "textoPortaria")
				
				.add(Projections.property("ideRequerimento"), "ideRequerimento")
				.add(Projections.property("ideProcessoPai"), "ideProcessoPai")
				.add(Projections.property("ideOrgao"), "ideOrgao")
			).setResultTransformer(new AliasToNestedBeanResultTransformer(Processo.class))
			
			.add(Restrictions.ilike("numProcesso", numProcesso, MatchMode.EXACT));
		
		Processo p = daoProcesso.buscarPorCriteria(criteria);
		
		if(!Util.isNullOuVazio(p)){
			p.setProcessoAtoCollection(processoAtoService.listarAtosPorProcesso(p.getIdeProcesso()));
		
			for (ProcessoAto pa : p.getProcessoAtoCollection()) {
				pa.setControleProcessoAtoCollection(controleProcessoAtoService.listarPorProcessoAto(pa.getIdeProcessoAto()));
			}
		}
		return p;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Processo> buscarPorAnoECodigoOrgao(String ano, Orgao orgao) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("anoFormacao", ano);
		parametros.put("codOrgao", orgao.getCodOrgao());
		return daoProcesso.buscarPorNamedQuery("Processo.findByOrgaoAno", parametros);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Processo> filtrarListaProcessos(Processo processo) {
		return daoProcesso.listarPorExemplo(processo);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirProcessos(Processo processo)  {
		daoProcesso.remover(processo);
	}

	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	public Collection<Processo> filtrarListaProcessosPorEstado(Estado estado)  {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideEstado", estado);
		return daoProcesso.buscarPorNamedQuery("Processo.findByIdeEstado", params);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Processo filtrarProcessoById(Processo processo)  {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideProcesso", processo.getIdeProcesso());
		return daoProcesso.buscarEntidadePorNamedQuery("Processo.findByIdeProcesso", params);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Processo carregarProcesso(Integer id){
		Processo processo = daoProcesso.carregarGet(id);
		Hibernate.initialize(processo.getIdeOrgao());
		Hibernate.initialize(processo.getIdeRequerimento());
		return processo;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public VwConsultaProcesso getProcessoComArquivos(VwConsultaProcesso processo)  {
		List<ArquivoProcesso> listArquivoProcesso = (List<ArquivoProcesso>) arquivoProcessoService
				.listaArquivoProcessoPorIdeProcesso(processo.getIdeProcesso());
		for (ArquivoProcesso ap : listArquivoProcesso) {
			String caminhoArquivo = ap.getDscCaminhoArquivo().trim();
			File file = new File(caminhoArquivo);
			ap.setFileSize(file.length());
			ap.setFileName(file.getName());
		}
		processo.setArquivoProcessoList(listArquivoProcesso);
		return processo;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Processo buscarPorRequerimento(Integer ideRequerimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Processo.class)
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideProcesso"), "ideProcesso")
				.add(Projections.property("numProcesso"), "numProcesso")
				.add(Projections.property("indExcluido"), "indExcluido")
				.add(Projections.property("dtcFormacao"), "dtcFormacao")
				.add(Projections.property("dtcExcluido"), "dtcExcluido")
				.add(Projections.property("ideRequerimento"), "ideRequerimento")
				.add(Projections.property("ideProcessoPai"), "ideProcessoPai")
				.add(Projections.property("ideOrgao"), "ideOrgao")
				.add(Projections.property("textoPortaria"), "textoPortaria")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(Processo.class))
			.add(Restrictions.eq("ideRequerimento.ideRequerimento", ideRequerimento));
		return daoProcesso.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Processo buscarPorIdeProcesso(Processo processo)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Processo.class)
				.createAlias("ideRequerimento", "req", JoinType.LEFT_OUTER_JOIN)
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideProcesso"), "ideProcesso")
				.add(Projections.property("numProcesso"), "numProcesso")
				.add(Projections.property("indExcluido"), "indExcluido")
				.add(Projections.property("dtcFormacao"), "dtcFormacao")
				.add(Projections.property("dtcExcluido"), "dtcExcluido")
				.add(Projections.property("req.ideRequerimento"), "ideRequerimento.ideRequerimento")
				.add(Projections.property("ideProcessoPai"), "ideProcessoPai")
				.add(Projections.property("ideOrgao"), "ideOrgao")
				.add(Projections.property("textoPortaria"), "textoPortaria")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(Processo.class))
			.add(Restrictions.eq("ideProcesso", processo.getIdeProcesso()));
		return daoProcesso.buscarPorCriteria(criteria);
	}	

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Processo filtrarProcessoByNumero(Processo processo)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Processo.class);
		criteria.add(Restrictions.eq("numProcesso", processo.getNumProcesso()));
		return daoProcesso.buscarPorCriteria(criteria);
	}

	public boolean hasProcesso(Integer ideRequerimento)  {
		return !Util.isNull(this.buscarPorRequerimento(ideRequerimento));
	}

	public List<Processo> filtrarListaProcessosLai	(int first, int pageSize, String numeroProcesso, String numDocumento, String nome,	Municipio municipio, String empreendimento)  {
		DetachedCriteria detachedCriteria = getCriteriaLai(numeroProcesso, numDocumento, nome, municipio, empreendimento, false);
		detachedCriteria.addOrder(Order.desc("dtcFormacao"));
		return (List<Processo>) this.daoProcesso.listarPorCriteriaDemanda(detachedCriteria, first, pageSize);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ProcessoTramiteImovelRural> listarProcesTramitImovelRuralPorImovelRural(ImovelRural imovelRural) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideImovelRural", imovelRural);
		return processoTramiteImovelRuralDAO.buscarPorNamedQuery("ProcessoTramiteImovelRural.findByIdeImovelRural",
				params);
	}

	public Collection<AtoAmbiental> obterListaAtosAmbientais(Integer ideProcesso)  {
		return atoAmbientalService.listarAtosPorProcesso(ideProcesso);
	}

	private DetachedCriteria getCriteriaLai(String numeroProcesso, String numDocumento, String nome, Municipio municipio, String empreendimento,boolean count) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(Processo.class)
				.createAlias("ideRequerimento", "req")
				.createAlias("req.requerimentoUnico", "ru", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ru.enquadramento", "eq", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ru.idePorte", "pt", JoinType.LEFT_OUTER_JOIN)
				
				.createAlias("req.empreendimentoRequerimentoCollection","empReq")
				.createAlias("empReq.ideEmpreendimento","emp")
				
				.createAlias("emp.enderecoEmpreendimentoCollection", "end")
				.createAlias("end.ideEndereco", "e")
				.createAlias("e.ideLogradouro", "log")
				.createAlias("log.ideTipoLogradouro", "tl")
				.createAlias("log.ideBairro", "br")
				.createAlias("log.ideMunicipio", "mun")
				.createAlias("req.requerimentoPessoaCollection", "rp")
				.createAlias("rp.pessoa", "p")
				.createAlias("p.pessoaFisica", "pf", JoinType.LEFT_OUTER_JOIN)
				.createAlias("p.pessoaJuridica", "pj", JoinType.LEFT_OUTER_JOIN)
				.createAlias("controleTramitacaoCollection", "ct", JoinType.LEFT_OUTER_JOIN,
						Restrictions.eq("ct.indFimDaFila", true));

		if (!count) {
			criteria.setProjection(Projections
					.projectionList()
					.add(Projections.distinct(Projections.property("ideProcesso").as("ideProcesso")))
					.add(Projections.property("numProcesso"), "numProcesso")
					.add(Projections.property("dtcFormacao"), "dtcFormacao")

					.add(Projections.property("req.ideRequerimento"), "ideRequerimento.ideRequerimento")

					.add(Projections.property("emp.nomEmpreendimento"),
							"ideRequerimento.ultimoEmpreendimento.nomEmpreendimento")

					.add(Projections.property("log.numCep"),
							"ideRequerimento.ultimoEmpreendimento.endereco.ideLogradouro.numCep")

					.add(Projections.property("tl.nomTipoLogradouro"),
							"ideRequerimento.ultimoEmpreendimento.endereco.ideLogradouro.ideTipoLogradouro.nomTipoLogradouro")
					.add(Projections.property("tl.sglTipoLogradouro"),
							"ideRequerimento.ultimoEmpreendimento.endereco.ideLogradouro.ideTipoLogradouro.sglTipoLogradouro")

					.add(Projections.property("br.nomBairro"),
							"ideRequerimento.ultimoEmpreendimento.endereco.ideLogradouro.ideBairro.nomBairro")

					.add(Projections.property("mun.nomMunicipio"),
							"ideRequerimento.ultimoEmpreendimento.endereco.ideLogradouro.ideMunicipio.nomMunicipio")

					.add(Projections.property("pf.nomPessoa"), "ideRequerimento.requerente.pessoaFisica.nomPessoa")
					.add(Projections.property("pf.numCpf"), "ideRequerimento.requerente.pessoaFisica.numCpf")

					.add(Projections.property("pj.nomRazaoSocial"),
							"ideRequerimento.requerente.pessoaJuridica.nomRazaoSocial")
					.add(Projections.property("pj.numCnpj"), "ideRequerimento.requerente.pessoaJuridica.numCnpj")

					.add(Projections.property("pt.nomPorte"), "ideRequerimento.requerimentoUnico.idePorte.nomPorte"));
		} else {
			criteria.setProjection(Projections.projectionList().add(
					Projections.distinct(Projections.property("ideProcesso").as("ideProcesso"))));
		}
		criteria.add(Restrictions.eq("rp.ideTipoPessoaRequerimento.ideTipoPessoaRequerimento",
				TipoPessoaRequerimentoEnum.REQUERENTE.getId()));
		criteria.add(Restrictions.eq("end.ideTipoEndereco.ideTipoEndereco", TipoEnderecoEnum.LOCALIZACAO.getId()));

		if (!Util.isNullOuVazio(numeroProcesso)){
			criteria.add(Restrictions.ilike("numProcesso", numeroProcesso.trim(), MatchMode.ANYWHERE));
		}
			
		if (!Util.isNullOuVazio(numDocumento))
			criteria.add(Restrictions.or(Restrictions.eq("pj.numCnpj", numDocumento.trim()),
					Restrictions.eq("pf.numCpf", numDocumento.trim())));

		if (!Util.isNullOuVazio(nome)) {
			Disjunction disjunction = Restrictions.disjunction();
				disjunction
					.add(Restrictions.ilike("pj.nomRazaoSocial", nome.trim(), MatchMode.ANYWHERE))
					.add(Restrictions.ilike("pj.nomeFantasia", nome.trim(), MatchMode.ANYWHERE))
					.add(Restrictions.ilike("pf.nomPessoa", nome.trim(), MatchMode.ANYWHERE));
			criteria.add(disjunction);
		}
		
		if (!Util.isNullOuVazio(empreendimento))
			criteria.add(Restrictions.ilike("emp.nomEmpreendimento", empreendimento.trim(), MatchMode.ANYWHERE));

		if (!Util.isNullOuVazio(municipio))
			criteria.add(Restrictions.eq("mun.ideMunicipio", municipio.getIdeMunicipio()));

		criteria.add(Restrictions.in("ct.ideStatusFluxo.ideStatusFluxo",
				new Integer[] { StatusFluxoEnum.ARQUIVADO.getStatus(), StatusFluxoEnum.CONCLUIDO.getStatus(),
						StatusFluxoEnum.FORMADO.getStatus() }));

		criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(Processo.class));

		return criteria;
	}

	public int countLai(String numeroProcesso, String numDocumento, String nome, Municipio municipio, String empreendimento)  {
		
		List<Processo> listProcesso = this.daoProcesso.listarPorCriteria(this.getCriteriaLai(numeroProcesso, numDocumento, nome, municipio, empreendimento, true));
		
		if(!Util.isNullOuVazio(listProcesso)){
			return listProcesso.size();
		}
		else{
			return 0;
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarControleProcessoAto(Collection<ProcessoAto> listaProcessoAto) {
		for(ProcessoAto processoAto : listaProcessoAto) {
			ControleProcessoAto controleProcessoAto = new ControleProcessoAto();
			controleProcessoAto.setIdeProcessoAto(processoAto);
			
			if(processoAto.getAtoAmbiental().getIndDeclaratorio() || processoAto.getAtoAmbiental().isAPE()) {
				controleProcessoAto.setIdeStatusProcessoAto(new StatusProcessoAto(StatusProcessoAtoEnum.EMITIDO.getId()));
			}
			else{
				controleProcessoAto.setIdeStatusProcessoAto(new StatusProcessoAto(StatusProcessoAtoEnum.AGUARDANDO_ANALISE.getId()));
			}
			
			controleProcessoAto.setDtcControleProcessoAto(new Date());
			controleProcessoAto.setIndAprovado(true);
			controleProcessoAtoDAOImpl.salvar(controleProcessoAto);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void gerarProcesso(Requerimento requerimento, Pessoa operador, Integer ideArea, Boolean isDirucSecundaria) {
		try {
			Orgao inema = this.carregarOrgao();

			Processo processo = gerarProcesso(requerimento, inema);

			String ano = carregarAnoAtual();

			GrupoProcesso grupoProcesso = this.atoAmbientalService.carregarGrupoProcessoByRequerimento(requerimento);
			
			Processo ultimoProcessoGerado = this.carregarUltimoProcessoGeradoByAnoAndOrgaoAndGrupo(ano, inema,grupoProcesso);

			this.salvarProcesso(processo, ultimoProcessoGerado, grupoProcesso);
			
			Collection<ProcessoAto> listaProcessoAto = gerarProcessosAto(requerimento, processo);
			processo.setProcessoAtoCollection(listaProcessoAto);
			this.processoAtoService.salvarEmLote(processo.getProcessoAtoCollection());

			if(requerimentoService.isSolicitacaoAdminstrativoTla(requerimento)){
				salvarTransferenciaAmbiental(requerimento, processo);
			}
			
			salvarControleProcessoAto(processo.getProcessoAtoCollection());

			boolean isConcluido = isDeclaratorio(requerimento) || isAPE(requerimento) || isSomenteDla(requerimento);

			ControleTramitacao tramitacao = this.gerarTramitacao(processo, ideArea, isConcluido, isDirucSecundaria);
			this.controleTramitacaoService.salvar(tramitacao);

			if (isDeclaratorio(requerimento)) {
				this.salvarComunicacaoEmissaoCertificado(requerimento);
			} else {
				this.salvarComunicacaoFormacaoProcesso(requerimento, processo);
			}

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}

	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void gerarProcessoLoteBoleto(Requerimento requerimento, Pessoa operador, Integer ideArea, Boolean isDirucSecundaria) {
		try {
			Orgao inema = this.carregarOrgao();

			Processo processo = gerarProcesso(requerimento, inema);

			String ano = carregarAnoAtual();

			GrupoProcesso grupoProcesso = this.atoAmbientalService.carregarGrupoProcessoByRequerimento(requerimento);
			
			Processo ultimoProcessoGerado = this.carregarUltimoProcessoGeradoByAnoAndOrgaoAndGrupo(ano, inema,grupoProcesso);

			this.salvarProcesso(processo, ultimoProcessoGerado, grupoProcesso);
			
			Collection<ProcessoAto> listaProcessoAto = gerarProcessosAto(requerimento, processo);
			processo.setProcessoAtoCollection(listaProcessoAto);
			this.processoAtoService.salvarEmLote(processo.getProcessoAtoCollection());

			if(requerimentoService.isSolicitacaoAdminstrativoTla(requerimento)){
				salvarTransferenciaAmbiental(requerimento, processo);
			}
			
			salvarControleProcessoAto(processo.getProcessoAtoCollection());

			boolean isConcluido = isDeclaratorio(requerimento) || isAPE(requerimento) || isSomenteDla(requerimento);

			ControleTramitacao tramitacao = this.gerarTramitacao(processo, ideArea, isConcluido, isDirucSecundaria);
			this.controleTramitacaoService.salvar(tramitacao);

			if (isDeclaratorio(requerimento)) {
				this.salvarComunicacaoEmissaoCertificado(requerimento);
			} else {
				this.salvarComunicacaoFormacaoProcessoLoteBoleto(requerimento, processo);
			}

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}

	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarTransferenciaAmbiental(Requerimento requerimento, Processo processoTla ){
		SolicitacaoAdministrativo       solicitacaoAdminstrativo;
		List<TransferenciaAmbiental>    transferenciaAmbientalList = new ArrayList<TransferenciaAmbiental>();
		TransferenciaAmbiental 			transferenciaAmbiental; 
		ProcessoAto processoAto;
		Empreendimento emprendimento;	
		
		try {
			solicitacaoAdminstrativo = requerimentoService.getSolicitacaoAdminstrativo(requerimento, TipoSolicitacaoEnum.TRANSFERENCIA_LICENCA_AMBIENTAL);
			if(!Util.isNull(solicitacaoAdminstrativo)){

				emprendimento = empreendimentoService.getEmpreendimentoByRequerimento(requerimento);
							
				for (SolicitacaoAdministrativoAtoAmbiental saa : solicitacaoAdminstrativo.getSolicitacaoAdminstrativoAtoAmbientalCollection()) {
					transferenciaAmbiental = new TransferenciaAmbiental();
					transferenciaAmbiental.setIdeProcessoTla(processoTla);					
				
					Processo processoOriginal = new Processo(solicitacaoAdminstrativo.getNumProcesso());
					while(processoAtoService.isProcessoTla(processoOriginal)){
						processoOriginal = solicitacaoAdministrativoService.getProcessoPai(processoOriginal);
					}

					processoAto = processoAtoService.getProcessoAtoByProcessoByAtoAmbiental(processoOriginal, saa.getIdeAtoAmbiental(), saa.getIdeTipologia());	
				
					if(processoAto!=null){
						transferenciaAmbiental.setIdeProcessoAto(processoAto);													
					}

					if(solicitacaoAdminstrativo.getIndDetentorLicenca()){	
						transferenciaAmbiental.setIdeEmpreendimentoDestino(solicitacaoAdminstrativo.getIdeEmpreendimento());
						transferenciaAmbiental.setIdeEmpreendimentoOrigem(emprendimento);						
					}else{
						transferenciaAmbiental.setIdeEmpreendimentoOrigem(solicitacaoAdminstrativo.getIdeEmpreendimento());
						transferenciaAmbiental.setIdeEmpreendimentoDestino(emprendimento);	
					}
					transferenciaAmbientalList.add(transferenciaAmbiental);
				}

				transferenciaAmbientalService.salvar(transferenciaAmbientalList);
		
			}
 		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	
	
	public void atualizarProcessoAto(ProcessoAto processoAto, StatusProcessoAtoEnum statusProcessoAtoEnum) {
		
		try {
			ControleProcessoAto controleProcessoAto= new ControleProcessoAto();
			controleProcessoAto.setIdePessoaFisica(new Funcionario(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
			controleProcessoAto.setIdeStatusProcessoAto(new StatusProcessoAto(statusProcessoAtoEnum.getId()));
			controleProcessoAto.setDtcControleProcessoAto(new Date());
			controleProcessoAto.setIndAprovado(true);
			controleProcessoAto.setIdeProcessoAto(processoAto);
			controleProcessoAtoService.salvarOuAtualizar(controleProcessoAto);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
		
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void gerarControleTramitacaoParaAreaSecundaria(Requerimento requerimento, Pessoa operador, Integer ideArea, Boolean isDirucSecundaria) {
		try {
			Processo processo = buscarPorRequerimento(requerimento.getIdeRequerimento());
			boolean isConcluido = this.isDeclaratorio(requerimento) || this.isSomenteDla(requerimento);
			ControleTramitacao tramitacao = this.gerarTramitacao(processo, ideArea, isConcluido, isDirucSecundaria);
			this.controleTramitacaoService.salvar(tramitacao);

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}

	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private Orgao carregarOrgao() throws Exception  {
		return this.orgaoService.carregar(this.parametroService.obterValorInt(ParametroEnum.ORGAO_INEMA));
	}

	private Processo gerarProcesso(Requerimento requerimento, Orgao inema) {
		Processo processo = new Processo();
		processo.setIdeOrgao(inema);
		processo.setDtcFormacao(new Date());
		processo.setIdeRequerimento(requerimento);
		return processo;
	}

	private String carregarAnoAtual() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
		return simpleDateFormat.format(new Date());
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private Processo carregarUltimoProcessoGeradoByAnoAndOrgaoAndGrupo(String ano, Orgao inema, GrupoProcesso grupoProcesso) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("anoFormacao", ano);
		parametros.put("codOrgao", inema.getCodOrgao());
		parametros.put("grupo", "%"+grupoProcesso.getDscSiglaGrupoProcesso()+"%");
		return this.daoProcesso.obterPorNamedQuery("Processo.findLastByOrgaoAnoGrupo", parametros);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarProcesso(Processo processo, Processo ultimoProcessoGerado, GrupoProcesso grupoProcesso) {

		String sgl = "LIC";

		if (!Util.isNull(grupoProcesso))
			sgl = grupoProcesso.getDscSiglaGrupoProcesso();

		String numeroProcesso = this.gerarNumeroProcesso(processo, ultimoProcessoGerado, sgl);
		processo.setNumProcesso(numeroProcesso);

		this.salvarProcesso(processo);
	}

	private String gerarNumeroProcesso(Processo processo, Processo ultimoProcessoGerado, String grupoProcesso)
			 {

		StringBuilder numeroProcesso = new StringBuilder(32);

		numeroProcesso.append(this.carregarAnoAtual());
		numeroProcesso.append('.');
		String x = processo.getIdeOrgao().getCodOrgao().toString();
		x = Util.lpad(x, '0', 3);
		numeroProcesso.append(x);
		numeroProcesso.append('.');

		String y = null;
		String z = null;

		if (!Util.isNullOuVazio(ultimoProcessoGerado)) {
			String aux = ultimoProcessoGerado.getNumProcesso();
			if (aux.length() >= 16) {
				aux = aux.substring(9, 15);
				int i = Integer.parseInt(aux) + 1;
				y = "" + i;
			}
			aux = ultimoProcessoGerado.getNumProcesso();
			if (aux.length() > 0) {
				aux = aux.substring(aux.lastIndexOf('-') + 1);
				int i = Integer.parseInt(aux) + 1;
				z = "" + i;
			}
		}
		if (Util.isNullOuVazio(y)) {
			y = "1";
		}
		if (Util.isNullOuVazio(z)) {
			z = "1";
		}
		y = Util.lpad(y, '0', 6);
		numeroProcesso.append(y);
		numeroProcesso.append('/');
		String so = processo.getIdeOrgao().getDscSiglaOrgao();
		if (!Util.isNull(so)) {
			numeroProcesso.append(so);
		}
		numeroProcesso.append('/');

		if (!Util.isNull(grupoProcesso)) {
			numeroProcesso.append(grupoProcesso);
		}

		numeroProcesso.append('-');

		z = Util.lpad(z, '0', 5);
		numeroProcesso.append(z);
		return numeroProcesso.toString();
	}

	private Collection<ProcessoAto> gerarProcessosAto(Requerimento requerimento, Processo processo) {
		
		Collection<ProcessoAto> processoAtos = new ArrayList<ProcessoAto>();
		Collection<EnquadramentoAtoAmbiental> listaAtosEnquadrados = enquadramentoService.listarEnquadramentoAtoByRequerimento(requerimento.getIdeRequerimento());
		
		if(!Util.isNullOuVazio(listaAtosEnquadrados) && listaAtosEnquadrados.size() == 1 && listaAtosEnquadrados.iterator().next().getAtoAmbiental().isAPE()){
			Collection<Tipologia> tipologias = new ArrayList<Tipologia>();
			tipologias = empreendimentoService.buscarTipologias(empreendimentoService.getEmpreendimentoByRequerimento(requerimento),false,false);
			if(tipologias.size()>1){
				Collection<EnquadramentoAtoAmbiental> listaEnqAtosAmbientaisTemp = new ArrayList<EnquadramentoAtoAmbiental>();
				for (Tipologia tipologia : tipologias) {
					for (EnquadramentoAtoAmbiental enquadramentoAtoAmbiental: listaAtosEnquadrados) {
						if(!tipologia.equals(enquadramentoAtoAmbiental.getTipologia())){
							EnquadramentoAtoAmbiental enquadramentoAtoAmbientalTemp = new EnquadramentoAtoAmbiental(enquadramentoAtoAmbiental.getEnquadramento(), enquadramentoAtoAmbiental.getAtoAmbiental(), tipologia);
							listaEnqAtosAmbientaisTemp.add(enquadramentoAtoAmbientalTemp);
						}
					}
				}
				listaAtosEnquadrados.addAll(listaEnqAtosAmbientaisTemp);
				enquadramentoService.salvarEnquadramentoAtoAmbiental(listaEnqAtosAmbientaisTemp);
			}
			
			
		}
		
		for (EnquadramentoAtoAmbiental enquadramentoAtoAmbiental : listaAtosEnquadrados) {
			gerarProcessoAto(processo, processoAtos, enquadramentoAtoAmbiental);
		}
		
		return processoAtos;
	}

	private void gerarProcessoAto(Processo processo, Collection<ProcessoAto> processoAtos, EnquadramentoAtoAmbiental enquadramentoAtoAmbiental) {
		AtoAmbiental atoAmbiental = enquadramentoAtoAmbiental.getAtoAmbiental();
		ProcessoAto processoAto = new ProcessoAto(processo, enquadramentoAtoAmbiental.getAtoAmbiental());
		processoAto.setAtoAmbiental(atoAmbiental);
		processoAto.setProcesso(processo);
		processoAto.setIndExcluido(false);
		processoAto.setTipologia(enquadramentoAtoAmbiental.getTipologia());
		processoAtos.add(processoAto);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isSomenteDla(Requerimento requerimento)  {
		return this.requerimentoService.isSomenteDLA(requerimento.getIdeRequerimento());
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isDeclaratorio(Requerimento requerimento)  {
		Collection<AtoAmbiental> atos = this.atoAmbientalService.isTodosAtosDeclaratorios(requerimento
				.getIdeRequerimento());

		if (Util.isNullOuVazio(atos)) {
			return false;
		}

		for (AtoAmbiental atoAmbiental : atos) {
			if (!atoAmbiental.getIndDeclaratorio()) {
				return false;
			}
		}
		return true;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isAPE(Requerimento requerimento)  {
		
		Collection<AtoAmbiental> lista = this.atoAmbientalService.listarAtosEnquadradosByRequerimento(requerimento.getIdeRequerimento());
		
		if (!Util.isNullOuVazio(lista)) {
			for (AtoAmbiental atoAmbiental : lista) {
				if (atoAmbiental.isAPE()) {
					return true;
				}
			}
		}
		return false;
	}

	private ControleTramitacao gerarTramitacao(Processo pProcesso, Integer ideArea, boolean pIsConcluido, Boolean isIndAreaSecundaria)
			 {
		ControleTramitacao tramitacao = new ControleTramitacao();
		tramitacao.setDtcTramitacao(new Date());

		if (pIsConcluido) {
			tramitacao.setIdeStatusFluxo(new StatusFluxo(StatusFluxoEnum.CONCLUIDO.getStatus()));
		} else {
			tramitacao.setIdeStatusFluxo(new StatusFluxo(StatusFluxoEnum.FORMADO.getStatus()));
		}

		tramitacao.setIndFimDaFila(true);
		tramitacao.setIdeProcesso(pProcesso);
		tramitacao.setIndAreaSecundaria(isIndAreaSecundaria); 

		Area area = new Area(ideArea);
		tramitacao.setIdeArea(area);
		if(isIndAreaSecundaria){
			tramitacao.setIdePauta(this.pautaService.obtemPautaArea(new Area(AreaEnum.COGES.getId())));	
		} else {
			tramitacao.setIdePauta(this.pautaService.obtemPautaArea(area));
		}
		

		return tramitacao;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarComunicacaoEmissaoCertificado(Requerimento requerimento)  {
		Collection<AtoAmbiental> atos = this.atoAmbientalService.listarAtosEnquadradosByRequerimento(requerimento.getIdeRequerimento());
		String msg = this.gerarComunicacaoEmissaoCertificado(requerimento, atos);
		String assunto = "Certificado gerado " + requerimento.getNumRequerimento();
		ComunicacaoRequerimento comunicacao = this.gerarComunicacao(requerimento, assunto, msg);
		this.comunicacaoRequerimentoService.salvar(comunicacao);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarComunicacaoFormacaoProcesso(Requerimento requerimento, Processo processo) {
		String numProcesso = processo.getNumProcesso();
		String msg = this.gerarComunicacaoFormacaoProcesso(requerimento, numProcesso);
		String assunto = "Processo formado " + numProcesso;
		ComunicacaoRequerimento comunicacao = this.gerarComunicacao(requerimento, assunto, msg);
		this.comunicacaoRequerimentoService.salvar(comunicacao);
		emailService.enviarEmailsAoRequerente(requerimento, assunto, msg);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarComunicacaoFormacaoProcessoLoteBoleto(Requerimento requerimento, Processo processo)  {
		String numProcesso = processo.getNumProcesso();
		String msg = this.gerarComunicacaoFormacaoProcesso(requerimento, numProcesso);
		String assunto = "Processo formado " + numProcesso;
		ComunicacaoRequerimento comunicacao = this.gerarComunicacao(requerimento, assunto, msg);
		this.comunicacaoRequerimentoService.salvar(comunicacao);
	}

	private ComunicacaoRequerimento gerarComunicacao(Requerimento requerimento, final String assunto, final String lMsg) {
		ComunicacaoRequerimento comunicacaoRequerimento = new ComunicacaoRequerimento();
		comunicacaoRequerimento.setDtcComunicacao(new Date());
		comunicacaoRequerimento.setDesMensagem(lMsg);
		comunicacaoRequerimento.setIdeRequerimento(requerimento);
		comunicacaoRequerimento.setAssunto(assunto);
		return comunicacaoRequerimento;
	}

	private String gerarComunicacaoFormacaoProcesso(Requerimento requerimento, String numProcesso) {
		return "Prezado(a), \n" + "O Processo " + numProcesso + " solicitado por meio do requerimento nº "
				+ requerimento.getNumRequerimento() + "  foi formado no dia " + Util.formatData(new Date()) + " \n"
				+ "Para acompanhar o seu andamento acesse o SEIA.\n\n" + "Atte., \n" + "Central de Atendimento/INEMA.";
	}

	private String gerarComunicacaoEmissaoCertificado(Requerimento requerimento, Collection<AtoAmbiental> atos) {
		String msg = "Prezado(a) , \n\n" + "Informamos que o(s) certificado(s) de ";

		for (AtoAmbiental atoAmbiental : atos) {
			msg += "\t \u25CF " + atoAmbiental.getNomAtoAmbiental() + "\n";
		}

		msg += " referente(s) ao requerimento de número " + requerimento.getNumRequerimento()
				+ " está disponível no sistema SEIA.\nAcesse o sistema e consulte-o.\n\n" + "Atte., \n"
				+ "Central de Atendimento/INEMA.";
		return msg;
	}
	
	
	/**
	 * Metodo utilizado para buscar um processo pelo seu numero e trazer a pessoa ja carregada.
	 *
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @param {@link Processo} com o seu numero preenchido
	 * @return {@link Processo} com atributos e {@link Pessoa} ja carregada
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Processo buscarProcessoPeloNumero(Processo processo)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Processo.class, "proc")
				.createAlias("proc.ideRequerimento", "req")
				.createAlias("req.requerimentoPessoaCollection", "reqPess")
				.createAlias("reqPess.pessoa", "pessoa")
				.setFetchMode("reqPess.ideTipoPessoaRequerimento", FetchMode.JOIN)
				.setFetchMode("pessoa.pessoaJuridica", FetchMode.JOIN)
				.setFetchMode("pessoa.pessoaFisica", FetchMode.JOIN)
				.add(Restrictions.eq("proc.numProcesso", processo.getNumProcesso()));
				Processo proc = daoProcesso.buscarPorCriteria(criteria);
				
		if(proc != null && proc.getIdeRequerimento().getRequerimentoPessoaCollection() != null) {
			proc.getIdeRequerimento().getRequerimentoPessoaCollection().iterator().next(); 
		}
					
		return proc;
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public boolean isExisteProcesso(Processo processo)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Processo.class);
		
		if(processo.getIdeProcesso()!=null){
			criteria.add(Restrictions.eq("ideProcesso", processo.getIdeProcesso()));
		}
		else if (processo.getNumProcesso()!=null && !"".equals(processo.getNumProcesso())){
			criteria.add(Restrictions.eq("numProcesso", processo.getNumProcesso()));
		}
				
		return !Util.isNullOuVazio(daoProcesso.buscarPorCriteria(criteria));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isProcessoNaoEncontrado(Requerimento requerimento)  {
		return Util.isNull(buscarPorRequerimento(requerimento.getIdeRequerimento()));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoWS> consultarProcessoWs(Integer requerente, String numProcesso, Integer status, Date dtcInicial,Date dtcFinal, List<Integer> idesPessoas, Integer ato, Integer categoria, Integer municipio, String cpfcnpj, List<String> documentoPessoas, boolean consultaExterna) {
		StringBuilder lSql = new StringBuilder();
		lSql.append("SELECT distinct new br.gov.ba.ws.entity.ProcessoWS(p.ideProcesso,p.numProcesso,p.dtcFormacao, po.nomPorte,pf.nomPessoa, pj.nomRazaoSocial,st.dscStatusFluxo, st.dscStatusFluxoExterno, r.ideRequerimento) from Processo p ");
		lSql.append("INNER JOIN p.controleTramitacaoCollection ct ");
		if(!Util.isNullOuVazio(ato)|| !Util.isNullOuVazio(categoria)){
			lSql.append("INNER JOIN p.processoAtoCollection pa ");
			lSql.append("INNER JOIN pa.atoAmbiental aa ");
		}
		lSql.append("INNER JOIN p.ideRequerimento r ");
		lSql.append("LEFT JOIN r.empreendimentoRequerimentoCollection er ");
		if(!Util.isNullOuVazio(municipio)){
			lSql.append("INNER JOIN er.ideEmpreendimento.enderecoEmpreendimentoCollection ee ");	
			lSql.append("INNER JOIN ee.ideEndereco e ");
			lSql.append("INNER JOIN e.ideLogradouro l ");
			lSql.append("INNER JOIN l.ideMunicipio m ");
		}
		lSql.append("INNER JOIN r.requerimentoPessoaCollection rp ");
		lSql.append("INNER JOIN rp.pessoa pe ");
		lSql.append("INNER JOIN ct.ideStatusFluxo st ");
		lSql.append("LEFT JOIN er.idePorte po ");
		lSql.append("left JOIN pe.pessoaFisica pf ");
		lSql.append("left JOIN pe.pessoaJuridica pj ");
		lSql.append("WHERE p.indExcluido = false and ct.indFimDaFila = true  ");
		lSql.append(" AND rp.ideTipoPessoaRequerimento.ideTipoPessoaRequerimento = 1");
		if(isConsultaSemFiltros(requerente,numProcesso,status,dtcInicial,dtcFinal,idesPessoas,ato,categoria,municipio,cpfcnpj,documentoPessoas)){
			lSql.append(" AND st.ideStatusFluxo != 2");
		}
		if (!Util.isNullOuVazio(requerente)) {
			lSql.append(" AND (pf.idePessoaFisica = :requerente");
			lSql.append(" OR  pj.idePessoaJuridica = :requerente)");
		}
		if(!Util.isNullOuVazio(cpfcnpj)){
			lSql.append(" AND (pf.numCpf = :cpfcnpj");
			lSql.append(" OR  pj.numCnpj = :cpfcnpj)");
		}
		if (!Util.isNullOuVazio(numProcesso)) {
			lSql.append(" AND replace(p.numProcesso, '.', '') LIKE replace(:numProcesso, '.','')");
		}
		if (!Util.isNullOuVazio(status)) {
			lSql.append(" AND st.ideStatusFluxo = :status");
		}
		if (!Util.isNullOuVazio(dtcInicial) && !Util.isNullOuVazio(dtcFinal) ) {
			lSql.append(" AND p.dtcFormacao BETWEEN :dtcInicial AND :dtcFinal");
		}
	
		if(!Util.isNullOuVazio(idesPessoas)){
			lSql.append(" AND rp.pessoa.idePessoa in (:idesPessoas)");	
		}
		
		if (!Util.isNullOuVazio(ato)) {
			lSql.append(" AND aa.ideAtoAmbiental = :ato");
		}
		if (!Util.isNullOuVazio(categoria)) {
			lSql.append(" AND aa.ideTipoAto.ideTipoAto  = :categoria");
		}
		if(!Util.isNullOuVazio(municipio)){
			lSql.append(" AND m.ideMunicipio = :municipio");
			lSql.append(" AND ee.ideTipoEndereco.ideTipoEndereco = 4");
		}
		EntityManager lEntityManager =  DAOFactory.getEntityManager();
		lSql.append(" order by p.numProcesso desc ");
		
		Query lQuery = lEntityManager.createQuery(lSql.toString());
		if (!Util.isNullOuVazio(numProcesso)) {
			lQuery.setParameter("numProcesso", "%" + numProcesso.trim() + "%");
		}
		if (!Util.isNullOuVazio(requerente)) {
			lQuery.setParameter("requerente", requerente);
		}
	
		if (!Util.isNullOuVazio(status)) {
			lQuery.setParameter("status", status);
		}
		if (!Util.isNullOuVazio(ato)) {
			lQuery.setParameter("ato", ato);
		}
		if (!Util.isNullOuVazio(categoria)) {
			lQuery.setParameter("categoria", categoria);
		}
		if (!Util.isNullOuVazio(cpfcnpj)) {
			lQuery.setParameter("cpfcnpj", cpfcnpj);
		}
		if (!Util.isNullOuVazio(dtcInicial) && !Util.isNullOuVazio(dtcFinal) ) {
			lQuery.setParameter("dtcInicial",dtcInicial);
			lQuery.setParameter("dtcFinal",dtcFinal);
		}
		if(!Util.isNullOuVazio(idesPessoas)){
			lQuery.setParameter("idesPessoas", idesPessoas);
		}
		if(!Util.isNullOuVazio(municipio)){
			lQuery.setParameter("municipio", municipio);
		}
		
		@SuppressWarnings("unchecked")
		List<ProcessoWS> listProcesso = lQuery.getResultList();
		if(Util.isNullOuVazio(listProcesso)){
			listProcesso = new ArrayList<ProcessoWS>();
		}
		
		if(consultaExterna){
			listProcesso.addAll(consultaProcessoExterno(cpfcnpj, dtcInicial, dtcFinal, numProcesso,documentoPessoas));
		}
		
		return listProcesso;
	}
	
	private boolean isConsultaSemFiltros(Integer requerente,
			String numProcesso, Integer status, Date dtcInicial, Date dtcFinal,
			List<Integer> idesPessoas, Integer ato, Integer categoria,
			Integer municipio, String cpfcnpj, List<String> documentoPessoas) {
			if((Util.isNullOuVazio(requerente) && Util.isNullOuVazio(numProcesso) && Util.isNullOuVazio(status) && Util.isNullOuVazio(dtcInicial) &&  Util.isNullOuVazio(dtcFinal)
					&& Util.isNullOuVazio(idesPessoas) &&  Util.isNullOuVazio(ato) &&  Util.isNullOuVazio(categoria) && Util.isNullOuVazio(municipio) && Util.isNullOuVazio(cpfcnpj) && Util.isNullOuVazio(documentoPessoas))){
				return true;
				
			}
			
		return false;
	}

	@SuppressWarnings("unchecked")
	private List<ProcessoWS> consultaProcessoExterno(String cpfcnpj, Date dtcInicial, Date dtcFinal, String numProcesso, List<String> documentoPessoas){
		StringBuilder lSql = new StringBuilder();
		EntityManager lEntityManager =  DAOFactory.getEntityManager();
		lSql.append("SELECT distinct new br.gov.ba.ws.entity.ProcessoWS(p.processo,p.dtcFormacao,p.sistema,p.status) from ProcessoExterno p WHERE 1=1");
		if(!Util.isNullOuVazio(cpfcnpj)){
			lSql.append(" AND p.documentoCpfCnpj = :cpfcnpj");
		}
		
		if(!Util.isNullOuVazio(documentoPessoas)){
			lSql.append(" AND p.documentoCpfCnpj in (:documentoPessoas)");	
		}
		
		if(!Util.isNullOuVazio(dtcInicial) && !Util.isNullOuVazio(dtcFinal)){
			lSql.append(" AND p.dtcFormacao BETWEEN :dtcInicial AND :dtcFinal");
		}
		
		if(!Util.isNullOuVazio(numProcesso)){
			lSql.append(" AND lower(p.processo) LIKE lower(:processo)");
		}
		lSql.append(" order by p.processo desc ");
		Query lQuery = lEntityManager.createQuery(lSql.toString());
		if (!Util.isNullOuVazio(dtcInicial) && !Util.isNullOuVazio(dtcFinal) ) {
			lQuery.setParameter("dtcInicial",dtcInicial);
			lQuery.setParameter("dtcFinal",dtcFinal);
		}
		if (!Util.isNullOuVazio(cpfcnpj)) {
			lQuery.setParameter("cpfcnpj", cpfcnpj);
		}
		if (!Util.isNullOuVazio(numProcesso)) {
			lQuery.setParameter("processo", "%" + numProcesso.trim() + "%");
		}
		if (!Util.isNullOuVazio(documentoPessoas)) {
			lQuery.setParameter("documentoPessoas", documentoPessoas);
		}
		return  lQuery.getResultList();
	}
	
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProcessoWS detalheProcessoWs(Integer ideProcesso, String numProcesso) {
		StringBuilder lSql = new StringBuilder();
		lSql.append("SELECT distinct new br.gov.ba.ws.entity.ProcessoWS(p.ideProcesso,p.numProcesso,p.dtcFormacao, po.nomPorte,pf.nomPessoa, pj.nomRazaoSocial,st.dscStatusFluxo, st.dscStatusFluxoExterno, emp.nomEmpreendimento,m.nomMunicipio,emp.desEmail,r.numRequerimento,r.dtcCriacao,r.ideRequerimento,emp.ideEmpreendimento) from Processo p ");
		lSql.append("INNER JOIN p.controleTramitacaoCollection ct ");
		lSql.append("INNER JOIN p.processoAtoCollection pa ");
		lSql.append("INNER JOIN pa.atoAmbiental aa ");
		lSql.append("INNER JOIN p.ideRequerimento r ");
		lSql.append("LEFT JOIN r.empreendimentoRequerimentoCollection er ");
		lSql.append("LEFT JOIN er.ideEmpreendimento emp ");
		lSql.append("LEFT JOIN er.ideEmpreendimento.enderecoEmpreendimentoCollection ee ");	
		lSql.append("LEFT JOIN ee.ideEndereco e ");
		lSql.append("LEFT JOIN e.ideLogradouro l ");
		lSql.append("LEFT JOIN l.ideMunicipio m ");
		lSql.append("INNER JOIN r.requerimentoPessoaCollection rp ");
		lSql.append("INNER JOIN rp.pessoa pe ");
		lSql.append("INNER JOIN ct.ideStatusFluxo st ");
		lSql.append("LEFT JOIN er.idePorte po ");
		lSql.append("left JOIN pe.pessoaFisica pf ");
		lSql.append("left JOIN pe.pessoaJuridica pj ");
		lSql.append("WHERE p.indExcluido = false and ct.indFimDaFila = true  ");
		lSql.append(" AND rp.ideTipoPessoaRequerimento.ideTipoPessoaRequerimento = 1");
		lSql.append(" AND (ee.ideTipoEndereco.ideTipoEndereco = 4 or ee.ideTipoEndereco.ideTipoEndereco is null) ");
		if (!Util.isNullOuVazio(ideProcesso)) {
			lSql.append(" AND p.ideProcesso = :ideProcesso");
		}
		else if (!Util.isNullOuVazio(numProcesso)) {
			lSql.append(" AND p.numProcesso = :numProcesso");
		}
		EntityManager lEntityManager =  DAOFactory.getEntityManager();
		lSql.append(" order by p.numProcesso desc ");
		
		Query lQuery = lEntityManager.createQuery(lSql.toString());
		if (!Util.isNullOuVazio(ideProcesso)) {
			lQuery.setParameter("ideProcesso", ideProcesso);
		}
		else if (!Util.isNullOuVazio(numProcesso)) {
			lQuery.setParameter("numProcesso", numProcesso);
		}
			
		ProcessoWS processo = (ProcessoWS) lQuery.getSingleResult();
		return processo;
	}
	
	public Long calcularQtdDiasFormacaoDoProcesso(Date dtcFormcacao) {
		Long qtdDiasFormado = new Date().getTime() - dtcFormcacao.getTime();
		return (qtdDiasFormado / 86400000L);
	}
	
	
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoWS> listarProcessoPautaWs(Integer idePessoaFisica, Integer max, Integer first, Integer tipoPauta,Date dtcInicial, Date dtcFinal, String numProcesso) {
		StringBuilder lSql = new StringBuilder();
		lSql.append("SELECT distinct new br.gov.ba.ws.entity.ProcessoWS(p.ideProcesso,p.numProcesso,p.dtcFormacao, pf.nomPessoa, pj.nomRazaoSocial,st.dscStatusFluxo,r.ideRequerimento,ct.dtcTramitacao) from Processo p ");
		lSql.append("INNER JOIN p.controleTramitacaoCollection ct ");
		lSql.append("INNER JOIN ct.ideStatusFluxo st ");
		lSql.append("INNER JOIN p.ideRequerimento r ");
		lSql.append("INNER JOIN r.requerimentoPessoaCollection rp ");
		lSql.append("INNER JOIN rp.pessoa pe ");
		lSql.append("INNER JOIN ct.idePauta pa ");
		lSql.append("LEFT JOIN pa.idePessoaFisica pp ");
		if(tipoPauta.equals(TipoPautaEnum.PAUTA_AREA.getTipo()) || tipoPauta.equals(TipoPautaEnum.PAUTA_COORDENADOR_AREA.getTipo())){
			lSql.append("LEFT JOIN pa.ideArea a ");
		}
		lSql.append("left JOIN pe.pessoaFisica pf ");
		lSql.append("left JOIN pe.pessoaJuridica pj ");
		lSql.append("WHERE p.indExcluido = false and ct.indFimDaFila = true  ");
		if(tipoPauta.equals(TipoPautaEnum.PAUTA_COORDENADOR_AREA.getTipo())){
			lSql.append(" AND pa.ideTipoPauta in (1,2,4)");
			lSql.append(" AND (a.idePessoaFisica.idePessoaFisica = :idePessoaFisica or pp.idePessoaFisica = :idePessoaFisica)");
			lSql.append(" AND st.ideStatusFluxo in (4,8,12)");
		}
		if(tipoPauta.equals(TipoPautaEnum.PAUTA_TECNICA.getTipo())){
			lSql.append(" AND st.ideStatusFluxo = 6");
			lSql.append(" AND pa.ideTipoPauta = 3");
			lSql.append(" AND pp.idePessoaFisica = :idePessoaFisica");
		}
		if(tipoPauta.equals(TipoPautaEnum.PAUTA_AREA.getTipo())){
			lSql.append(" AND st.ideStatusFluxo in (1,5)");
			lSql.append(" AND pa.ideTipoPauta = 1");
			lSql.append("AND a.idePessoaFisica.idePessoaFisica = :idePessoaFisica ");
		}
		lSql.append(" AND rp.ideTipoPessoaRequerimento.ideTipoPessoaRequerimento = 1");
		if(!Util.isNullOuVazio(dtcInicial) && !Util.isNullOuVazio(dtcFinal)){
			lSql.append(" AND p.dtcFormacao BETWEEN :dtcInicial AND :dtcFinal");
		}
		if (!Util.isNullOuVazio(numProcesso)) {
			lSql.append(" AND lower(p.numProcesso) LIKE lower(:numProcesso)");
		}
		EntityManager lEntityManager =  DAOFactory.getEntityManager();
		lSql.append(" ORDER BY ct.dtcTramitacao DESC ");
		
		Query lQuery = lEntityManager.createQuery(lSql.toString());
		if (!Util.isNullOuVazio(idePessoaFisica)) {
			lQuery.setParameter("idePessoaFisica", idePessoaFisica);
		}
		if (!Util.isNullOuVazio(dtcInicial) && !Util.isNullOuVazio(dtcFinal) ) {
			lQuery.setParameter("dtcInicial",dtcInicial);
			lQuery.setParameter("dtcFinal",dtcFinal);
		}
		if (!Util.isNullOuVazio(numProcesso)) {
			lQuery.setParameter("numProcesso", "%" + numProcesso + "%");
		}
		lQuery.setMaxResults(max);
		lQuery.setFirstResult(first);
			
		return lQuery.getResultList();
	}
	
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoWS> consultarPublicaWS( Integer first, Integer max, Integer municipio, String cpfcnpj, String nomRequerente, String nomEmpreendimento, String numProcesso) {
		StringBuilder lSql = new StringBuilder();
		lSql.append("SELECT distinct new br.gov.ba.ws.entity.ProcessoWS(p.ideProcesso,p.numProcesso,p.dtcFormacao, po.nomPorte,pf.nomPessoa, pj.nomRazaoSocial,st.dscStatusFluxo, st.dscStatusFluxoExterno,r.ideRequerimento,ct.dtcTramitacao) from Processo p ");
		lSql.append("INNER JOIN p.controleTramitacaoCollection ct ");
		lSql.append("INNER JOIN p.ideRequerimento r ");
		lSql.append("LEFT JOIN r.empreendimentoRequerimentoCollection er ");
		if(!Util.isNullOuVazio(nomEmpreendimento)){
			lSql.append("INNER JOIN er.ideEmpreendimento emp ");
		}
		if(!Util.isNullOuVazio(municipio)){
			lSql.append("INNER JOIN er.ideEmpreendimento.enderecoEmpreendimentoCollection ee ");	
			lSql.append("INNER JOIN ee.ideEndereco e ");
			lSql.append("INNER JOIN e.ideLogradouro l ");
			lSql.append("INNER JOIN l.ideMunicipio m ");
		}
		lSql.append("INNER JOIN r.requerimentoPessoaCollection rp ");
		lSql.append("INNER JOIN rp.pessoa pe ");
		lSql.append("INNER JOIN ct.ideStatusFluxo st ");
		lSql.append("LEFT JOIN er.idePorte po ");
		lSql.append("left JOIN pe.pessoaFisica pf ");
		lSql.append("left JOIN pe.pessoaJuridica pj ");
		lSql.append("WHERE p.indExcluido = false and ct.indFimDaFila = true  ");
		lSql.append(" AND rp.ideTipoPessoaRequerimento.ideTipoPessoaRequerimento = 1");
		lSql.append(" AND st.ideStatusFluxo in (1,2,10)");
		
		if(!Util.isNullOuVazio(nomEmpreendimento)){
			lSql.append(" AND  TRANSLATE(Upper (emp.nomEmpreendimento),'ÂÃÀÁÉÈÍÕÓÒÔÚÇ','AAAAEEIOOOOUC') LIKE :nomEmpreendimento");
		}
		
		
		if (!Util.isNullOuVazio(nomRequerente)) {
			lSql.append(" AND (TRANSLATE(Upper (pf.nomPessoa),'ÂÃÀÁÉÈÍÕÓÒÔÚÇ','AAAAEEIOOOOUC') LIKE :nomRequerente");
			lSql.append(" OR  TRANSLATE(Upper (pj.nomRazaoSocial),'ÂÃÀÁÉÈÍÕÓÒÔÚÇ','AAAAEEIOOOOUC') LIKE :nomRequerente");
			lSql.append(" OR  TRANSLATE(Upper (pj.nomeFantasia),'ÂÃÀÁÉÈÍÕÓÒÔÚÇ','AAAAEEIOOOOUC') LIKE :nomRequerente)");
		}
		if(!Util.isNullOuVazio(cpfcnpj)){
			lSql.append(" AND (pf.numCpf = :cpfcnpj");
			lSql.append(" OR  pj.numCnpj = :cpfcnpj)");
		}
		
		if(!Util.isNullOuVazio(municipio)){
			lSql.append(" AND m.ideMunicipio = :municipio");
			lSql.append(" AND ee.ideTipoEndereco.ideTipoEndereco = 4");
		}
		
		if(!Util.isNullOuVazio(numProcesso)){
			lSql.append(" AND replace(p.numProcesso, '.', '') LIKE replace(:numProcesso, '.','')");
		}
		
		EntityManager lEntityManager =  DAOFactory.getEntityManager();
		lSql.append(" order by p.numProcesso desc ");
		
		Query lQuery = lEntityManager.createQuery(lSql.toString());
		lQuery.setMaxResults(max);
		lQuery.setFirstResult(first);
		
		if(!Util.isNullOuVazio(nomEmpreendimento)){
			String parametro = Normalizer.normalize(nomEmpreendimento, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toUpperCase();
			lQuery.setParameter("nomEmpreendimento", "%"+parametro+"%");
		}
		
		if (!Util.isNullOuVazio(nomRequerente)) {
			String parametro = Normalizer.normalize(nomRequerente, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toUpperCase();
			lQuery.setParameter("nomRequerente", "%"+parametro+"%");
		}
	
		if (!Util.isNullOuVazio(cpfcnpj)) {
			lQuery.setParameter("cpfcnpj", cpfcnpj);
		}
		
		if(!Util.isNullOuVazio(municipio)){
			lQuery.setParameter("municipio", municipio);
		}
		
		if (!Util.isNullOuVazio(numProcesso)) {
			lQuery.setParameter("numProcesso", "%"+numProcesso.trim()+"%");
		}
		
		@SuppressWarnings("unchecked")
		List<ProcessoWS> listProcesso = lQuery.getResultList();
		return listProcesso;
	}
	
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Long countListarProcessoPautaWs(Integer idePessoaFisica, Integer tipoPauta,Date dtcInicial, Date dtcFinal, String numProcesso) {
		StringBuilder lSql = new StringBuilder();
		lSql.append("SELECT distinct count (p.ideProcesso) from Processo p ");
		lSql.append("INNER JOIN p.controleTramitacaoCollection ct ");
		lSql.append("INNER JOIN ct.ideStatusFluxo st ");
		lSql.append("INNER JOIN p.ideRequerimento r ");
		lSql.append("INNER JOIN r.requerimentoPessoaCollection rp ");
		lSql.append("INNER JOIN rp.pessoa pe ");
		lSql.append("INNER JOIN ct.idePauta pa ");
		lSql.append("LEFT JOIN pa.idePessoaFisica pp ");
		if(tipoPauta.equals(TipoPautaEnum.PAUTA_AREA.getTipo()) || tipoPauta.equals(TipoPautaEnum.PAUTA_COORDENADOR_AREA.getTipo())){
			lSql.append("LEFT JOIN pa.ideArea a ");
		}
		lSql.append("left JOIN pe.pessoaFisica pf ");
		lSql.append("left JOIN pe.pessoaJuridica pj ");
		lSql.append("WHERE p.indExcluido = false and ct.indFimDaFila = true  ");
		if(tipoPauta.equals(TipoPautaEnum.PAUTA_COORDENADOR_AREA.getTipo())){
			lSql.append(" AND pa.ideTipoPauta in (1,2,4)");
			lSql.append(" AND (a.idePessoaFisica.idePessoaFisica = :idePessoaFisica or pp.idePessoaFisica = :idePessoaFisica)");
			lSql.append(" AND st.ideStatusFluxo in (4,8,12)");
		}
		if(tipoPauta.equals(TipoPautaEnum.PAUTA_TECNICA.getTipo())){
			lSql.append(" AND st.ideStatusFluxo = 6");
			lSql.append(" AND pa.ideTipoPauta = 3");
			lSql.append(" AND pp.idePessoaFisica = :idePessoaFisica");
		}
		if(tipoPauta.equals(TipoPautaEnum.PAUTA_AREA.getTipo())){
			lSql.append(" AND st.ideStatusFluxo in (1,5)");
			lSql.append(" AND pa.ideTipoPauta = 1");
			lSql.append("AND a.idePessoaFisica.idePessoaFisica = :idePessoaFisica ");
		}
		lSql.append(" AND rp.ideTipoPessoaRequerimento.ideTipoPessoaRequerimento = 1");
		if(!Util.isNullOuVazio(dtcInicial) && !Util.isNullOuVazio(dtcFinal)){
			lSql.append(" AND p.dtcFormacao BETWEEN :dtcInicial AND :dtcFinal");
		}
		if (!Util.isNullOuVazio(numProcesso)) {
			lSql.append(" AND replace(p.numProcesso, '.', '') LIKE replace(:numProcesso, '.','')");
		}
		EntityManager lEntityManager =  DAOFactory.getEntityManager();
		
		Query lQuery = lEntityManager.createQuery(lSql.toString());
		if (!Util.isNullOuVazio(idePessoaFisica)) {
			lQuery.setParameter("idePessoaFisica", idePessoaFisica);
		}
		if (!Util.isNullOuVazio(dtcInicial) && !Util.isNullOuVazio(dtcFinal) ) {
			lQuery.setParameter("dtcInicial",dtcInicial);
			lQuery.setParameter("dtcFinal",dtcFinal);
		}
		if (!Util.isNullOuVazio(numProcesso)) {
			lQuery.setParameter("numProcesso", "%" + numProcesso.trim() + "%");
		}
	
		return (Long) lQuery.getSingleResult();
	}
	
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Long countConsultarPublicaWS(Integer municipio, String cpfcnpj, String nomRequerente, String nomEmpreendimento, String numProcesso) {
		StringBuilder lSql = new StringBuilder();
		lSql.append("SELECT count (distinct p.ideProcesso) from Processo p ");
		lSql.append("INNER JOIN p.controleTramitacaoCollection ct ");
		lSql.append("INNER JOIN p.ideRequerimento r ");
		lSql.append("LEFT JOIN r.empreendimentoRequerimentoCollection er ");
		if(!Util.isNullOuVazio(nomEmpreendimento)){
			lSql.append("INNER JOIN er.ideEmpreendimento emp ");
		}
		if(!Util.isNullOuVazio(municipio)){
			lSql.append("INNER JOIN er.ideEmpreendimento.enderecoEmpreendimentoCollection ee ");	
			lSql.append("INNER JOIN ee.ideEndereco e ");
			lSql.append("INNER JOIN e.ideLogradouro l ");
			lSql.append("INNER JOIN l.ideMunicipio m ");
		}
		lSql.append("INNER JOIN r.requerimentoPessoaCollection rp ");
		lSql.append("INNER JOIN rp.pessoa pe ");
		lSql.append("INNER JOIN ct.ideStatusFluxo st ");
		lSql.append("LEFT JOIN er.idePorte po ");
		lSql.append("left JOIN pe.pessoaFisica pf ");
		lSql.append("left JOIN pe.pessoaJuridica pj ");
		lSql.append("WHERE p.indExcluido = false and ct.indFimDaFila = true  ");
		lSql.append(" AND rp.ideTipoPessoaRequerimento.ideTipoPessoaRequerimento = 1");
		lSql.append(" AND st.ideStatusFluxo in (1,2,10)");
		
		if(!Util.isNullOuVazio(nomEmpreendimento)){
			lSql.append(" AND  TRANSLATE(Upper (emp.nomEmpreendimento),'ÂÃÀÁÉÈÍÕÓÒÔÚÇ','AAAAEEIOOOOUC') LIKE :nomEmpreendimento");
		}
		
		
		if (!Util.isNullOuVazio(nomRequerente)) {
			lSql.append(" AND (TRANSLATE(Upper (pf.nomPessoa),'ÂÃÀÁÉÈÍÕÓÒÔÚÇ','AAAAEEIOOOOUC') LIKE :nomRequerente");
			lSql.append(" OR  TRANSLATE(Upper (pj.nomRazaoSocial),'ÂÃÀÁÉÈÍÕÓÒÔÚÇ','AAAAEEIOOOOUC') LIKE :nomRequerente");
			lSql.append(" OR  TRANSLATE(Upper (pj.nomeFantasia),'ÂÃÀÁÉÈÍÕÓÒÔÚÇ','AAAAEEIOOOOUC') LIKE :nomRequerente)");
		}
		if(!Util.isNullOuVazio(cpfcnpj)){
			lSql.append(" AND (pf.numCpf = :cpfcnpj");
			lSql.append(" OR  pj.numCnpj = :cpfcnpj)");
		}
		
		if(!Util.isNullOuVazio(municipio)){
			lSql.append(" AND m.ideMunicipio = :municipio");
			lSql.append(" AND ee.ideTipoEndereco.ideTipoEndereco = 4");
		}
		
		if(!Util.isNullOuVazio(numProcesso)){
			lSql.append(" AND replace(p.numProcesso, '.', '') LIKE replace(:numProcesso, '.','')");
		}
		EntityManager lEntityManager =  DAOFactory.getEntityManager();
		
		Query lQuery = lEntityManager.createQuery(lSql.toString());
		
		if(!Util.isNullOuVazio(nomEmpreendimento)){
			String parametro = Normalizer.normalize(nomEmpreendimento, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toUpperCase();
			lQuery.setParameter("nomEmpreendimento", "%"+parametro+"%");
		}
		
		if (!Util.isNullOuVazio(nomRequerente)) {
			String parametro = Normalizer.normalize(nomRequerente, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toUpperCase();
			lQuery.setParameter("nomRequerente", "%"+parametro+"%");
		}
	
		if (!Util.isNullOuVazio(cpfcnpj)) {
			lQuery.setParameter("cpfcnpj", cpfcnpj);
		}
		
		if(!Util.isNullOuVazio(municipio)){
			lQuery.setParameter("municipio", municipio);
		}
		
		if (!Util.isNullOuVazio(numProcesso)) {
			lQuery.setParameter("numProcesso", "%"+numProcesso.trim()+"%");
		}
		
		return (Long) lQuery.getSingleResult();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Processo getIdeProcessoTla(Processo processo) {
		return processoDAO.getIdeProcessoTla(processo);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Processo buscarProcessoComAtoAmbientalByNumProcesso(String numProcesso, AtoAmbiental atoAmbiental)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Processo.class)
				.createAlias("processoAtoCollection", "pa")
				.createAlias("pa.atoAmbiental", "aa")
				.add(Restrictions.like("numProcesso", numProcesso, MatchMode.EXACT))
				.add(Restrictions.eq("aa.ideAtoAmbiental", atoAmbiental.getIdeAtoAmbiental()))
					.setProjection(Projections.projectionList()
					.add(Projections.distinct(Projections.property("ideProcesso")),"ideProcesso")
					.add(Projections.property("numProcesso"), "numProcesso")
					.add(Projections.property("indExcluido"), "indExcluido")
					.add(Projections.property("dtcFormacao"), "dtcFormacao")
					.add(Projections.property("dtcExcluido"), "dtcExcluido")
					.add(Projections.property("ideRequerimento"), "ideRequerimento")
					.add(Projections.property("ideProcessoPai"), "ideProcessoPai")
					.add(Projections.property("ideOrgao"), "ideOrgao")
					.add(Projections.property("textoPortaria"), "textoPortaria"))
					.setResultTransformer(new AliasToNestedBeanResultTransformer(Processo.class)
					);
		return daoProcesso.buscarPorCriteria(criteria);
	}
}
