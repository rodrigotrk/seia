package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.primefaces.model.UploadedFile;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.DocumentoObrigatorioReenquadramento;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DocumentoObrigatorioReequerimentoService {
	
	@EJB
	private GerenciaArquivoService gerenciaArquivoService;
	
	@Inject
	private IDAO<DocumentoObrigatorioReenquadramento> documentoObrigatorioReenquadramentoDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(DocumentoObrigatorioReenquadramento documentoObrigatorioReenquadramento, UploadedFile arquivo) throws Exception {
		String diretorioArquivo = this.gerenciaArquivoService.uploadArquivoDocumentoObrigatorioReenquadramento(documentoObrigatorioReenquadramento);
		
		if(diretorioArquivo != null) {
			documentoObrigatorioReenquadramento.setDscCaminhoArquivo(diretorioArquivo);
			this.documentoObrigatorioReenquadramentoDAO.salvarOuAtualizar(documentoObrigatorioReenquadramento);
		} else {
			throw new Exception("Erro ao carregar o caminho do arquivo.");
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DocumentoObrigatorioReenquadramento> buscarDocumentosObrigatoriosReenquadramentoPorProcessoReenquadramento(Integer ideProcesso, Integer ideProcessoReenquadramento, Integer ideAtoAmbiental)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoObrigatorioReenquadramento.class)
				
				.createAlias("ideProcessoReenquadramento", "prr")
				.createAlias("prr.ideProcesso", "pro")
				
				.createAlias("pro.ideRequerimento", "requerimento")
				.createAlias("requerimento.requerimentoUnico", "unico",JoinType.LEFT_OUTER_JOIN)
				.createAlias("idePessoaUpload", "pessoa", JoinType.LEFT_OUTER_JOIN)
				.createAlias("idePessoaValidacao", "idePessoaValidacao", JoinType.LEFT_OUTER_JOIN)
				.createAlias("idePessoaValidacao.pessoaFisica", "pessoaFisica", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideDocumentoObrigatorio", "doc", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideDocumentoAto", "da", JoinType.LEFT_OUTER_JOIN)
				.createAlias("da.ideAtoAmbiental", "aa", JoinType.INNER_JOIN)
				.createAlias("da.ideDocumentoObrigatorio", "docObgDocAto", JoinType.LEFT_OUTER_JOIN);

		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("ideDocumentoObrigatorioReenquadramento"), "ideDocumentoObrigatorioReenquadramento")
				.add(Projections.property("dscCaminhoArquivo"), "dscCaminhoArquivo").add(Projections.property("indDocumentoValidado"), "indDocumentoValidado")
				.add(Projections.property("dtcValidacao"), "dtcValidacao")
				.add(Projections.property("ideDocumentoObrigatorioReenquadramento"), "ideDocumentoObrigatorioReenquadramento")
				.add(Projections.property("pessoa.idePessoa"), "idePessoaUpload.idePessoa")
				.add(Projections.property("idePessoaValidacao.idePessoa"), "idePessoaValidacao.idePessoa")
				.add(Projections.property("idePessoaValidacao.pessoaFisica.idePessoaFisica"), "idePessoaValidacao.pessoaFisica.idePessoaFisica")
				.add(Projections.property("pessoaFisica.nomPessoa"), "idePessoaValidacao.pessoaFisica.nomPessoa")
				.add(Projections.property("requerimento.ideRequerimento"), "ideRequerimento.ideRequerimento")

				.add(Projections.property("doc.ideDocumentoObrigatorio"), "ideDocumentoObrigatorio.ideDocumentoObrigatorio")
				.add(Projections.property("doc.nomDocumentoObrigatorio"), "ideDocumentoObrigatorio.nomDocumentoObrigatorio")
				.add(Projections.property("doc.numTamanho"), "ideDocumentoObrigatorio.numTamanho")
				.add(Projections.property("doc.indFormulario"), "ideDocumentoObrigatorio.indFormulario")
				.add(Projections.property("doc.indAtivo"), "ideDocumentoObrigatorio.indAtivo")

				.add(Projections.property("da.ideDocumentoAto"), "ideDocumentoAto.ideDocumentoAto")
				.add(Projections.property("da.indAtivo"), "ideDocumentoAto.indAtivo")
				.add(Projections.property("aa.ideAtoAmbiental"), "ideDocumentoAto.ideAtoAmbiental.ideAtoAmbiental")
				.add(Projections.property("doc.indAtivo"), "ideDocumentoAto.ideDocumentoObrigatorio.indAtivo")
				
				.add(Projections.property("docObgDocAto.ideDocumentoObrigatorio"), "ideDocumentoAto.ideDocumentoObrigatorio.ideDocumentoObrigatorio")
				.add(Projections.property("docObgDocAto.nomDocumentoObrigatorio"), "ideDocumentoAto.ideDocumentoObrigatorio.nomDocumentoObrigatorio")
				.add(Projections.property("docObgDocAto.numTamanho"), "ideDocumentoAto.ideDocumentoObrigatorio.numTamanho")
				.add(Projections.property("docObgDocAto.indFormulario"), "ideDocumentoAto.ideDocumentoObrigatorio.indFormulario"));

		criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(DocumentoObrigatorioReenquadramento.class));

		criteria.add(Restrictions.eq("this.indDocumentoValidado", Boolean.TRUE));
		criteria.add(Restrictions.le("prr.ideProcessoReenquadramento", ideProcessoReenquadramento));
		criteria.add(Restrictions.eq("pro.ideProcesso", ideProcesso));
		criteria.add(Restrictions.eq("aa.ideAtoAmbiental", ideAtoAmbiental));

		return documentoObrigatorioReenquadramentoDAO.listarPorCriteria(criteria, Order.asc("doc.ideDocumentoObrigatorio"));
	}
}
