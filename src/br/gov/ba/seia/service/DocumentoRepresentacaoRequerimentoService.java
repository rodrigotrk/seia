package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.DocumentoRepresentacaoRequerimento;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EmpreendimentoRequerimento;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.ProcuradorPfEmpreendimento;
import br.gov.ba.seia.entity.ProcuradorRepEmpreendimento;
import br.gov.ba.seia.entity.RepresentanteLegal;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoPessoa;
import br.gov.ba.seia.util.Util;

@Stateless
public class DocumentoRepresentacaoRequerimentoService {

	@Inject
	private IDAO<DocumentoRepresentacaoRequerimento> documentoRepresentacaoRequerimentoDAO;

	@EJB
	RequerimentoPessoaService requerimentoPessoaService;

	@EJB
	RepresentanteLegalService representanteLegalService;

	@EJB
	ProcuradorRepEmpreendimentoService procuradorRepresentanteService;

	@EJB
	ProcuradorPfEmpreendimentoService procuradorPessoaFisicaService;

	@EJB
	private GerenciaArquivoService gerenciaArquivoService;

	@EJB
	private EmpreendimentoService empreendimentoService;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(DocumentoRepresentacaoRequerimento docsRepresentacaoRequerimento)  {
		documentoRepresentacaoRequerimentoDAO.salvarOuAtualizar(docsRepresentacaoRequerimento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarEmLote(List<DocumentoRepresentacaoRequerimento> docsRepresentacaoRequerimento)  {
		documentoRepresentacaoRequerimentoDAO.salvarEmLote(docsRepresentacaoRequerimento);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DocumentoRepresentacaoRequerimento obterDocumentoRepReqCriteria(
			DocumentoRepresentacaoRequerimento docRepRequerimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoRepresentacaoRequerimento.class);
		criteria.add(Restrictions.eq("ideDocumentoRepresentacaoRequerimento",
				docRepRequerimento.getIdeDocumentoRepresentacaoRequerimento()));
		return documentoRepresentacaoRequerimentoDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Collection<DocumentoRepresentacaoRequerimento> gerarDocumentosRepresentacao(Requerimento requerimento) throws Exception {
		
		Integer ideRequerimento = requerimento.getIdeRequerimento();
		Collection<RequerimentoPessoa> pessoasRequerimento = this.requerimentoPessoaService.listarEnvolvidosRequerimento(ideRequerimento);

		Collection<DocumentoRepresentacaoRequerimento> docsRepresentacao = new ArrayList<DocumentoRepresentacaoRequerimento>();

		Pessoa requerente = getRequerente(pessoasRequerimento);
		Integer ideRequerente = requerente.getIdePessoa();
		Empreendimento emp = empreendimentoService.carregarByIdeRequerimento(ideRequerimento);
		
		if(Util.isNullOuVazio(emp)){
			emp = new Empreendimento();
		}

		for (RequerimentoPessoa requerimentoPessoa : pessoasRequerimento) {
			Integer idePessoa = requerimentoPessoa.getPessoa().getIdePessoa();
			this.adicionarDocRepresentacao(requerimento, docsRepresentacao, ideRequerente, requerimentoPessoa, idePessoa, emp.getIdeEmpreendimento());
		}
		
		this.documentoRepresentacaoRequerimentoDAO.salvarEmLote((List<DocumentoRepresentacaoRequerimento>) docsRepresentacao);

		return docsRepresentacao;

	}

	private void adicionarDocRepresentacao(Requerimento requerimento, Collection<DocumentoRepresentacaoRequerimento> docsRepresentacao, Integer ideRequerente,
		RequerimentoPessoa requerimentoPessoa, Integer idePessoa, Integer ideEmpreendimento)  {
		
		if (requerimentoPessoa.isProcurador()) {
			
			// procurar procurador_pessoa_fisica
			DocumentoRepresentacaoRequerimento procuradorPF = gerarProcuradorPF(requerimento, ideRequerente, idePessoa, ideEmpreendimento);

			if(!Util.isNull(procuradorPF)) {
				docsRepresentacao.add(procuradorPF);
			}
				
			// procurar procurador_represenante
			DocumentoRepresentacaoRequerimento procuradorRepresentante = gerarProcuradorRepresentante(requerimento, ideRequerente, idePessoa);
			
			if(!Util.isNull(procuradorRepresentante)) {
				docsRepresentacao.add(procuradorRepresentante);
			}
				
		} else if (requerimentoPessoa.isRepresentante()) {
			
			// procurar representante_legal
			List<DocumentoRepresentacaoRequerimento> listaRepresentanteLegal = gerarRepresentanteLegal(requerimento, ideRequerente, idePessoa);
			if(!Util.isNull(listaRepresentanteLegal)) {
				docsRepresentacao.addAll(listaRepresentanteLegal);
			}
		}
	}

	private Pessoa getRequerente(Collection<RequerimentoPessoa> pessoasRequerimento) {
		for (RequerimentoPessoa requerimentoPessoa : pessoasRequerimento) {
			if (requerimentoPessoa.isRequerente()) {
				return requerimentoPessoa.getPessoa();
			}
		}
		return null;
	}

	private DocumentoRepresentacaoRequerimento gerarProcuradorPF(Requerimento requerimento, Integer ideRequerente,
			Integer idePessoa, Integer ideEmpreendimento)  {
		ProcuradorPfEmpreendimento procuradorPFEmpreendimento = this.procuradorPessoaFisicaService
				.buscarProcuradorByPessoaAndRequerenteAndEmpreendimento(idePessoa, ideRequerente, ideEmpreendimento);

		if (!Util.isNull(procuradorPFEmpreendimento)) {
			DocumentoRepresentacaoRequerimento documentoRepresentacaoRequerimento = gerarDocumentoRepresentacao(
					requerimento, procuradorPFEmpreendimento.getDscCaminhoProcuracao());
			documentoRepresentacaoRequerimento.setIdeProcuradorPfEmpreendimento(procuradorPFEmpreendimento);
			return documentoRepresentacaoRequerimento;
		}
		return null;
	}

	private DocumentoRepresentacaoRequerimento gerarProcuradorRepresentante(Requerimento requerimento, Integer ideRequerente, Integer idePessoa)  {
		
		EmpreendimentoRequerimento empreendimentoRequerimento = empreendimentoService.buscarEmpreendimentoRequerimento(requerimento);
		
		Integer ideEmpreendimento = null;
		
		if(!Util.isNullOuVazio(empreendimentoRequerimento) && !Util.isNullOuVazio(empreendimentoRequerimento.getIdeEmpreendimento())
				&& !Util.isNullOuVazio(empreendimentoRequerimento.getIdeEmpreendimento().getIdeEmpreendimento())){
			
			ideEmpreendimento = empreendimentoRequerimento.getIdeEmpreendimento().getIdeEmpreendimento();
		}
		
		ProcuradorRepEmpreendimento procuradorRepresentanteEmpreendimento = this.procuradorRepresentanteService.buscarProcuradorByPessoaAndRequerente(idePessoa, ideRequerente, ideEmpreendimento);

		if (!Util.isNull(procuradorRepresentanteEmpreendimento)) {
			
			DocumentoRepresentacaoRequerimento documentoRepresentacaoRequerimento = 
					gerarDocumentoRepresentacao(requerimento, procuradorRepresentanteEmpreendimento.getDscCaminhoProcuracao());
			
			documentoRepresentacaoRequerimento.setIdeProcuradorRepEmpreendimento(procuradorRepresentanteEmpreendimento);
			
			return documentoRepresentacaoRequerimento;
		}

		return null;
	}

	private List<DocumentoRepresentacaoRequerimento> gerarRepresentanteLegal(Requerimento requerimento,Integer ideRequerente, Integer idePessoa)  {
		
		List<DocumentoRepresentacaoRequerimento> listaDocumentoRepresentacaoRequerimento = null;
		List<RepresentanteLegal> listaRepresentante = this.representanteLegalService.buscarRepresentanteLegalByPessoaAndRequerente(idePessoa, ideRequerente);

		if (!Util.isNullOuVazio(listaRepresentante)) {
			listaDocumentoRepresentacaoRequerimento = new ArrayList<DocumentoRepresentacaoRequerimento>();
			for(RepresentanteLegal rep : listaRepresentante) {
				DocumentoRepresentacaoRequerimento documentoRepresentacaoRequerimento = gerarDocumentoRepresentacao(requerimento, rep.getDscCaminhoRepresentacao());
				documentoRepresentacaoRequerimento.setIdeRepresentanteLegal(rep);
				listaDocumentoRepresentacaoRequerimento.add(documentoRepresentacaoRequerimento);				
			}
		}
		return listaDocumentoRepresentacaoRequerimento;
	}

	private DocumentoRepresentacaoRequerimento gerarDocumentoRepresentacao(Requerimento requerimento,
			String caminhoArquivo) {
		DocumentoRepresentacaoRequerimento documentoRepresentacaoRequerimento = new DocumentoRepresentacaoRequerimento();
		documentoRepresentacaoRequerimento.setIdeRequerimento(requerimento);
		documentoRepresentacaoRequerimento.setDscCaminhoArquivo(caminhoArquivo);
		return documentoRepresentacaoRequerimento;
	}

	public Collection<DocumentoRepresentacaoRequerimento> buscarDocsRepresentacaoByRequerimento(Integer ideRequerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoRepresentacaoRequerimento.class)
				.createAlias("ideRepresentanteLegal", "rl", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideRequerimento", "req")
				.createAlias("ideProcuradorRepEmpreendimento", "pre", JoinType.LEFT_OUTER_JOIN)
				.createAlias("pre.ideProcuradorRepresentante", "procRep", JoinType.LEFT_OUTER_JOIN)
				.createAlias("idePessoaValidacao", "pv", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideProcuradorPfEmpreendimento", "ppe", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ppe.ideProcuradorPessoaFisica","procPf", JoinType.LEFT_OUTER_JOIN);

		criteria.add(Restrictions.eq("req.ideRequerimento", ideRequerimento));

		return documentoRepresentacaoRequerimentoDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void uploadFiles(DocumentoRepresentacaoRequerimento doc) throws Exception {
		if (doc.getArquivoChanged() != null && doc.getArquivoChanged()) {

			String diretorio = "";
			if (doc.getFileUpload() != null) {
				diretorio = gerenciaArquivoService.uploadArquivoDocumentoRepresentacaoRequerimentoExperimental(doc);
			}

			doc.setDscCaminhoArquivo(diretorio);
			doc.setArquivoChanged(Boolean.FALSE);
		}

		documentoRepresentacaoRequerimentoDAO.salvarOuAtualizar(doc);

	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Collection<DocumentoRepresentacaoRequerimento> documentosRepresentacao) {
		this.documentoRepresentacaoRequerimentoDAO.salvarEmLote((List<DocumentoRepresentacaoRequerimento>) documentosRepresentacao);
	}

}
