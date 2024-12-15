package br.gov.ba.seia.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.dao.DAOFactory;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.PessoaJuridicaDAOImpl;
import br.gov.ba.seia.dao.PessoaJuridicaHistoricoDAOImpl;
import br.gov.ba.seia.entity.Cnae;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.EnderecoPessoa;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.PessoaJuridicaCnae;
import br.gov.ba.seia.entity.PessoaJuridicaHistorico;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcuradorPessoaFisica;
import br.gov.ba.seia.entity.RepresentanteLegal;
import br.gov.ba.seia.entity.Telefone;
import br.gov.ba.seia.exception.AppExceptionError;
import br.gov.ba.seia.facade.CadastroAtividadeFacade;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.RelatorioUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PessoaJuridicaHistoricoService {	
	
	@Inject
	private IDAO<PessoaJuridicaHistorico> pessoaJuridicaHistoricoDAO;
	
	@EJB
	private PessoaJuridicaHistoricoDAOImpl pessoaJuridicaHistoricoDAOImpl;
	
	@EJB
	private PessoaJuridicaService pessoaJuridicaService;
	
	
	public void salvarPessoaJuridicaHistorico(PessoaJuridicaHistorico pessoaJuridicaHistorico)  {
		pessoaJuridicaHistoricoDAO.salvarOuAtualizar(pessoaJuridicaHistorico);	
	}
	
	public List<PessoaJuridicaHistorico> listarPessoaJuridicaHistoricoByPessoaJuridica(Integer idePj)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(PessoaJuridicaHistorico.class);
		criteria.createAlias("idePessoaJuridica", "pessoaJuridica");
		criteria.add(Restrictions.eq("pessoaJuridica.idePessoaJuridica", idePj));
		return pessoaJuridicaHistoricoDAO.listarPorCriteria(criteria);	
	}
	
	public List<PessoaJuridicaHistorico> listarPessoaJuridicaHistoricoByProcesso(Integer ideProcesso)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Processo.class)
		
		.createAlias("ideRequerimento", "req",JoinType.INNER_JOIN)
		.createAlias("req.requerimentoPessoaCollection","reqPessoa",JoinType.INNER_JOIN)
		.createAlias("reqPessoa.ideTipoPessoaRequerimento","tipoReqPessoa",JoinType.INNER_JOIN)
		.createAlias("reqPessoa.pessoa","pessoa",JoinType.INNER_JOIN)
		.createAlias("pessoa.pessoaJuridica","pj",JoinType.INNER_JOIN)
		.createAlias("pj.pessoaJuridicaHistoricoCollection","pjHistorico",JoinType.INNER_JOIN)
		.add(Restrictions.eq("ideProcesso", ideProcesso))
		.setProjection(Projections.projectionList()
							.add(Projections.property("pjHistorico.idePessoaJuridicaHistorico"),"idePessoaJuridicaHistorico")
							.add(Projections.property("pjHistorico.nomRazaoSocial"),"nomRazaoSocial")
							.add(Projections.property("pjHistorico.nomeFantasia"),"nomeFantasia")
							.add(Projections.property("pjHistorico.dtcAbertura"),"dtcAbertura")
							.add(Projections.property("pjHistorico.nomNaturezaJuridica"),"nomNaturezaJuridica")
							.add(Projections.property("pjHistorico.dtcHistorico"),"dtcHistorico")
							.add(Projections.property("pjHistorico.numInscricaoMunicipal"),"numInscricaoMunicipal")
							.add(Projections.property("pjHistorico.numInscricaoEstadual"),"numInscricaoEstadual")
							.add(Projections.property("pjHistorico.dscCaminhoArquivo"),"dscCaminhoArquivo"))
		.addOrder(Order.desc("dtcHistorico"))
		.setResultTransformer(new AliasToNestedBeanResultTransformer(PessoaJuridicaHistorico.class));
		
		
		return pessoaJuridicaHistoricoDAO.listarPorCriteria(criteria);	
	}
	
	public PessoaJuridicaHistorico buscarHistoricoAnterior(PessoaJuridica pessoaJuridica)  {
		return pessoaJuridicaHistoricoDAOImpl.buscarHistoricoAnterior(pessoaJuridica);
		
	}
	
	public StreamedContent imprimirHistorico(Integer idePessoaJuridica, Date dataInicioSet, Date dataFimSet) throws Exception {
		
		final Map<String, Object> lParametros = new HashMap<String, Object>();
		
		try {
			
			lParametros.put("idePessoaJuridica", idePessoaJuridica);
			
			Calendar cal = Calendar.getInstance();
			
	        if (dataInicioSet == null) {
	            cal.set(2012, Calendar.JANUARY, 1, 0, 0, 0);
	            cal.set(Calendar.MILLISECOND, 0);
	        } else {
	            cal.setTime(dataInicioSet);
	            cal.set(Calendar.HOUR_OF_DAY, 0);
	            cal.set(Calendar.MINUTE, 0);
	            cal.set(Calendar.SECOND, 0);
	            cal.set(Calendar.MILLISECOND, 0);
	        }
	        Timestamp dataInicio = new Timestamp(cal.getTimeInMillis());

	        if (dataFimSet == null) {
	            cal.setTime(new Date());
	        } else {
	            cal.setTime(dataFimSet);
	        }
	        cal.set(Calendar.HOUR_OF_DAY, 23);
	        cal.set(Calendar.MINUTE, 59);
	        cal.set(Calendar.SECOND, 59);
	        cal.set(Calendar.MILLISECOND, 999);
	        Timestamp dataFim = new Timestamp(cal.getTimeInMillis());
	        
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        
	        lParametros.put("dataInicio", new Timestamp(sdf.parse(sdf.format(dataInicio)).getTime()));
	        lParametros.put("dataFim", new Timestamp(sdf.parse(sdf.format(dataFim)).getTime()));
			
			return new RelatorioUtil("relatorio_historico_pessoa_juridica.jasper", lParametros, "logoInemaRelatorio.png",
					"sema_vertical.png").gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);

		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			return null;
		}
	}
	
	
}