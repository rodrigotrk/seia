package br.gov.ba.seia.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.primefaces.event.FileUploadEvent;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.DocumentoIdentificacao;
import br.gov.ba.seia.entity.DocumentoIdentificacaoRequerimento;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
public class DocumentoIdentificacaoRequerimentoService {

	@Inject
	private IDAO<DocumentoIdentificacaoRequerimento> docIdentificacaoRequerimentoDAO;
	@EJB
	private GerenciaArquivoService gerenciaArquivoService;
	@EJB
	private DocumentoIdentificacaoService documentoIdentificacaoService;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void gerenciarUploadDocumentoIdentificacao(DocumentoIdentificacaoRequerimento dir, FileUploadEvent event) {

		DocumentoIdentificacao diNovo = atualizarDocumentoIdentificacao(dir, event);

		dir.setIdeDocumentoIdentificacao(diNovo);
		dir.setDscCaminhoArquivo(diNovo.getDscCaminhoArquivo());
		
		if(!Util.isNull(dir.getIdeDocumentoIdentificacaoRequerimento())) {
			salvar(dir);
		}
	}

	private DocumentoIdentificacao atualizarDocumentoIdentificacao(DocumentoIdentificacaoRequerimento dir, FileUploadEvent event) {
		try{
			DocumentoIdentificacao di = dir.getIdeDocumentoIdentificacao();
			di.setIndExcluido(true);
			di.setDtcExclusao(new Date());
			documentoIdentificacaoService.salvarDocumentoIdentificacao(di);
			
			DocumentoIdentificacao diNovo = di.clone();
			diNovo.setIdeDocumentoIdentificacao(null);
			diNovo.setIndExcluido(false);
			diNovo.setDtcCriacao(new Date());
			diNovo.setDtcExclusao(null);
			documentoIdentificacaoService.salvarDocumentoIdentificacao(diNovo);
			
			String caminhoArquivo = gerenciaArquivoService.uploadArquivoDocumentoIdentificacao(diNovo, event.getFile());
			diNovo.setDscCaminhoArquivo(caminhoArquivo);
			
			documentoIdentificacaoService.salvarDocumentoIdentificacao(diNovo);
			return diNovo;
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(DocumentoIdentificacaoRequerimento doc, Boolean fazerUpload) throws Exception {
		if (doc.getArquivoChanged() != null && doc.getArquivoChanged()) {
			if (!Util.isNull(doc.getIdeDocumentoIdentificacao())
					&& !Util.isNull(doc.getIdeDocumentoIdentificacao().getDscCaminhoArquivo())) {
				if (new File(doc.getIdeDocumentoIdentificacao().getDscCaminhoArquivo()).exists()) {
					gerenciaArquivoService.deletarArquivo(doc.getIdeDocumentoIdentificacao().getDscCaminhoArquivo());
				}
			}
			if (fazerUpload) {
				String diretorio = gerenciaArquivoService.uploadArquivoDocumentoIdentificacaoRequerimento(doc);
				doc.getIdeDocumentoIdentificacao().setDscCaminhoArquivo(diretorio);
			}
		}
		docIdentificacaoRequerimentoDAO.salvarOuAtualizar(doc);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DocumentoIdentificacaoRequerimento> buscarDocsIdentificacao(Integer ideRequerimento, Pessoa ideRequerente)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoIdentificacaoRequerimento.class);
		
		criteria
			.createAlias("ideRequerimento", "req", JoinType.INNER_JOIN)
			.createAlias("ideDocumentoIdentificacao", "di", JoinType.INNER_JOIN)
			.createAlias("di.ideTipoIdentificacao", "tipo", JoinType.INNER_JOIN)
			.createAlias("di.ideOrgaoExpedidor", "orgao", JoinType.LEFT_OUTER_JOIN)
			.createAlias("di.idePessoa", "pessoa", JoinType.LEFT_OUTER_JOIN)
			.createAlias("di.ideEstado", "estado", JoinType.LEFT_OUTER_JOIN);
			
		if(!Util.isNullOuVazio(ideRequerente) && !Util.isNullOuVazio(ideRequerente.getIdePessoa())){
			
				criteria.createAlias("pessoa.pessoaFisica", "pf")
						.createAlias("pf.procuradorRepresentanteCollection", "procRep", JoinType.INNER_JOIN);
		}
		
		criteria.setProjection(
				Projections.distinct(
					Projections.projectionList()
					.add(Projections.property("ideDocumentoIdentificacaoRequerimento"),"ideDocumentoIdentificacaoRequerimento")
					.add(Projections.property("indDocumentoValidado"),"indDocumentoValidado")
					.add(Projections.property("dtcValidacao"),"dtcValidacao")
					.add(Projections.property("dscCaminhoArquivo"),"dscCaminhoArquivo")
					
					.add(Projections.property("req.ideRequerimento"),"ideRequerimento.ideRequerimento")
					
					.add(Projections.property("di.ideDocumentoIdentificacao"),"ideDocumentoIdentificacao.ideDocumentoIdentificacao")
					.add(Projections.property("di.numDocumento"),"ideDocumentoIdentificacao.numDocumento")
					.add(Projections.property("di.numSerie"),"ideDocumentoIdentificacao.numSerie")
					.add(Projections.property("di.dtcCriacao"),"ideDocumentoIdentificacao.dtcCriacao")
					.add(Projections.property("di.indExcluido"),"ideDocumentoIdentificacao.indExcluido")
					.add(Projections.property("di.dtcEmissao"),"ideDocumentoIdentificacao.dtcEmissao")
					.add(Projections.property("di.dtcValidade"),"ideDocumentoIdentificacao.dtcValidade")
					.add(Projections.property("di.dtcExclusao"),"ideDocumentoIdentificacao.dtcExclusao")
					.add(Projections.property("di.orgExpedidorOutros"),"ideDocumentoIdentificacao.orgExpedidorOutros")
					.add(Projections.property("di.dscCaminhoArquivo"),"ideDocumentoIdentificacao.dscCaminhoArquivo")
					
					.add(Projections.property("tipo.ideTipoIdentificacao"),"ideDocumentoIdentificacao.ideTipoIdentificacao.ideTipoIdentificacao")
					.add(Projections.property("tipo.nomTipoIdentificacao"),"ideDocumentoIdentificacao.ideTipoIdentificacao.nomTipoIdentificacao")
					
					.add(Projections.property("orgao.ideOrgaoExpedidor"),"ideDocumentoIdentificacao.ideOrgaoExpedidor.ideOrgaoExpedidor")
					
					.add(Projections.property("pessoa.idePessoa"),"ideDocumentoIdentificacao.idePessoa.idePessoa")
					
				)
			)
			
			.add(Restrictions.eq("req.ideRequerimento", ideRequerimento))
			.add(Restrictions.eq("di.indExcluido", false));
		
		if(!Util.isNullOuVazio(ideRequerente) && !Util.isNullOuVazio(ideRequerente.getIdePessoa())){
			if(ideRequerente.getPessoaJuridica() != null){
				ideRequerente.getPessoaJuridica().setIdePessoaJuridica(ideRequerente.getIdePessoa());
			}
			
			if(ideRequerente.getPessoaFisica() != null){
				ideRequerente.getPessoaFisica().setIdePessoaFisica(ideRequerente.getIdePessoa());
			}
			
			criteria.add(Restrictions.eq("procRep.indExcluido", false))
					.add(Restrictions.eq("procRep.idePessoaJuridica", ideRequerente.getPessoaJuridica()));
		}

			
			criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(DocumentoIdentificacaoRequerimento.class))
		;

		return docIdentificacaoRequerimentoDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<DocumentoIdentificacaoRequerimento> buscarDocsObrigatorioReqPorRequerimentoUnico(
			DocumentoIdentificacaoRequerimento requerimentoUnico)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoIdentificacaoRequerimento.class);
		criteria.add(Restrictions.eq("ideRequerimento.ideRequerimento", requerimentoUnico.getIdeRequerimento()))
				.setFetchMode("ideDocumentoObrigatorio", FetchMode.JOIN);
		
		return docIdentificacaoRequerimentoDAO.listarPorCriteria(criteria, Order.asc("ideDocumentoIdentificacao.ideDocumentoIdentificacao"));

	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DocumentoIdentificacaoRequerimento buscarDocsObrigatorioReqPorId(
			DocumentoIdentificacaoRequerimento documentoIdentificacaoRequerimento){
		return docIdentificacaoRequerimentoDAO.carregarGet(documentoIdentificacaoRequerimento
				.getIdeDocumentoIdentificacaoRequerimento());
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarEmLote(List<DocumentoIdentificacaoRequerimento> docs)  {
		docIdentificacaoRequerimentoDAO.salvarEmLote(docs);
	}

	public void salvar(DocumentoIdentificacaoRequerimento documentoIdentificacaoRequerimento)  {
		this.docIdentificacaoRequerimentoDAO.salvarOuAtualizar(documentoIdentificacaoRequerimento);
	}

	public Collection<DocumentoIdentificacaoRequerimento> gerarDocumentosIdentificacaoRequerente(Requerimento requerimento) throws Exception {
		
		Integer ideRequerimento = requerimento.getIdeRequerimento();

		Collection<DocumentoIdentificacaoRequerimento> docsIdentificacao = new ArrayList<DocumentoIdentificacaoRequerimento>();

		Collection<DocumentoIdentificacao> documentosIdentificacao = this.documentoIdentificacaoService.listarDocumentosIdentificacaoRequerente(ideRequerimento);

		for (DocumentoIdentificacao documentoIdentificacao : documentosIdentificacao) {
			DocumentoIdentificacaoRequerimento documentoIdentificacaoRequerimento = gerarDocumentoIdentificacaoRequerimento(requerimento, documentoIdentificacao);
			this.salvar(documentoIdentificacaoRequerimento);
			docsIdentificacao.add(documentoIdentificacaoRequerimento);
		}

		return docsIdentificacao;
	}

	public DocumentoIdentificacaoRequerimento gerarDocumentoIdentificacaoRequerimento(Requerimento requerimento, DocumentoIdentificacao documentoIdentificacao)  {
		DocumentoIdentificacaoRequerimento documentoIdentificacaoRequerimento = new DocumentoIdentificacaoRequerimento();
		documentoIdentificacaoRequerimento.setIdeDocumentoIdentificacao(documentoIdentificacao);
		documentoIdentificacaoRequerimento.setIdeRequerimento(requerimento);
		return documentoIdentificacaoRequerimento;
	}

	public void salvar(Collection<DocumentoIdentificacaoRequerimento> documentosIdentificacao) {
		this.docIdentificacaoRequerimentoDAO
				.salvarEmLote((List<DocumentoIdentificacaoRequerimento>) documentosIdentificacao);
	}
}
