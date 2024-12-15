package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.DAOFactory;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Certificado;
import br.gov.ba.seia.entity.DeclaracaoTransporte;
import br.gov.ba.seia.entity.DeclaracaoTransporteDestinatarioResiduo;
import br.gov.ba.seia.entity.DeclaracaoTransporteDisposicaoFinalResiduo;
import br.gov.ba.seia.entity.DeclaracaoTransporteEntidadeTransportadora;
import br.gov.ba.seia.entity.DeclaracaoTransporteGeradorResiduo;
import br.gov.ba.seia.entity.DeclaracaoTransporteResiduo;
import br.gov.ba.seia.entity.DeclaracaoTransporteResiduoEndereco;
import br.gov.ba.seia.entity.DisposicaoFinalResiduo;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.EnderecoPessoa;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.ProcuradorPessoaFisica;
import br.gov.ba.seia.entity.ProcuradorRepresentante;
import br.gov.ba.seia.entity.RepresentanteLegal;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoPessoa;
import br.gov.ba.seia.entity.TramitacaoRequerimento;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.TipoEnderecoEnum;
import br.gov.ba.seia.facade.EnderecoFacade;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DeclaracaoTransporteService {

	@Inject
	private IDAO<DeclaracaoTransporte> declaracaoTransporteDAO;
	
	@Inject
	private IDAO<DeclaracaoTransporteGeradorResiduo> declaracaoTransporteGeradorResiduoDAO;
	
	@Inject
	private IDAO<DeclaracaoTransporteDestinatarioResiduo> declaracaoTransporteDestinatarioResiduoDAO; 
	
	@Inject
	private IDAO<DeclaracaoTransporteResiduoEndereco> declaracaoTransporteEnderecoResiduoDAO;
	
	@Inject
	private IDAO<DeclaracaoTransporteEntidadeTransportadora> declaracaoTransporteEntidadeTransportadoraDAO;
	
	@Inject
	private IDAO<DeclaracaoTransporteDisposicaoFinalResiduo> declaracaoTransporteDisposicaoFinalResiduoDAO;
	
	@Inject
	private IDAO<DisposicaoFinalResiduo> disposicaoFinalResiduoDAO;
	
	@Inject
	private IDAO<DeclaracaoTransporteResiduo> declaracaoTransporteResiduoDAO;
	
	@EJB
	private PessoaService pessoaService;
	
	@EJB
	private EnderecoFacade enderecoFacade;
	
	@EJB
	private EnderecoPessoaService enderecoPessoaService;
	
	@EJB
	private CertificadoService certificadoService;
	
	@EJB
	private RequerimentoService requerimentoService;
	
	@EJB
	private RequerimentoUnicoService requerimentoUnicoService;
	
	@Inject
	private IDAO<ProcuradorPessoaFisica> procuradorPessoaFisicaDAO;
	@Inject
	private IDAO<ProcuradorRepresentante> procuradorRepresentanteDAO;
	@Inject
	private IDAO<RepresentanteLegal> representanteLegalDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DeclaracaoTransporte obterDeclaracaoPorRequerimento(Requerimento requerimento){
		DetachedCriteria criteria = DetachedCriteria.forClass(DeclaracaoTransporte.class)
				.createAlias("atoDeclaratorio", "atoDeclaratorio")
				.createAlias("atoDeclaratorio.ideRequerimento", "requerimento")
				.add(Restrictions.eq("indExcluido", false))
				.add(Restrictions.eq("requerimento.ideRequerimento", requerimento.getIdeRequerimento()));
		
		DeclaracaoTransporte declaracao = null;
		List<DeclaracaoTransporte> declaracoes = null;
		declaracoes = declaracaoTransporteDAO.listarPorCriteria(criteria);
		Integer maxId = 0;
		DeclaracaoTransporte maxDec = null;
		DeclaracaoTransporte aceiteDec = null;
		for(DeclaracaoTransporte dec : declaracoes) {
			if(dec.getIndAceiteResponsabilidade()==true) {
				aceiteDec = dec;
			}
			if(dec.getAtoDeclaratorio().getIdeAtoDeclaratorio() > maxId) {
				maxId = dec.getAtoDeclaratorio().getIdeAtoDeclaratorio();
				maxDec = dec;
			}
		}
		if(aceiteDec!=null) {
			declaracao = aceiteDec;
		}else
			if(maxDec!=null) {
				declaracao = maxDec;
			}
		if(declaracao!=null){
			Hibernate.initialize(declaracao.getListaDeclaracaoTransporteDisposicaoFinalResiduo());
			
			if(!Util.isNullOuVazio(declaracao.getListaDeclaracaoTransporteDisposicaoFinalResiduo())) {
		
				for(DeclaracaoTransporteDisposicaoFinalResiduo dtdfr : declaracao.getListaDeclaracaoTransporteDisposicaoFinalResiduo()) {
					dtdfr.setDisposicaoFinalResiduo(disposicaoFinalResiduoDAO.carregarGet(dtdfr.getDisposicaoFinalResiduo().getIdeDisposicaoFinalResiduo()));
				}
			}
		}
		
		return declaracao;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarDeclaracaoTransporte(DeclaracaoTransporte declaracaoTransporte)  {
		declaracaoTransporteDAO.salvarOuAtualizar(declaracaoTransporte);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarGeradorResiduo(DeclaracaoTransporteGeradorResiduo declaracaoTransporteGeradorResiduo) {
		declaracaoTransporteGeradorResiduoDAO.salvarOuAtualizar(declaracaoTransporteGeradorResiduo);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarDestinatarioResiduo(DeclaracaoTransporteDestinatarioResiduo declaracaoTransporteDestinatarioResiduo) {
		declaracaoTransporteDestinatarioResiduoDAO.salvarOuAtualizar(declaracaoTransporteDestinatarioResiduo);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarEnderecoDestinatarioResiduo(DeclaracaoTransporteResiduoEndereco declaracaoTransporteResiduoEndereco){
		declaracaoTransporteEnderecoResiduoDAO.salvarOuAtualizar(declaracaoTransporteResiduoEndereco);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarEntidadeTransportadora(DeclaracaoTransporteEntidadeTransportadora declaracaoTransporteEntidadeTransportadora) {
		declaracaoTransporteEntidadeTransportadoraDAO.salvarOuAtualizar(declaracaoTransporteEntidadeTransportadora);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Integer consultarDestinatarioResiduo(DeclaracaoTransporte declaracaoTransporte) {
		Integer idePessoaJuridica = null;
		StringBuilder lSql = new StringBuilder();
		
		EntityManager lEntityManager = DAOFactory.getEntityManager();
		
		lSql.append(" SELECT pj.ide_pessoa_juridica FROM pessoa_juridica pj, declaracao_transporte_destinatario_residuo dtdr ");
		lSql.append(" WHERE dtdr.ide_declaracao_transporte =  " + declaracaoTransporte.getIdeDeclaracaoTransporte());
		lSql.append(" AND dtdr.ind_excluido != true ");
		lSql.append(" AND dtdr.ide_pessoa_juridica = pj.ide_pessoa_juridica ");
		
    	Query lQuery = lEntityManager.createNativeQuery(lSql.toString());
    	if(!Util.isNullOuVazio(lQuery.getResultList())){
    		idePessoaJuridica = (Integer) lQuery.getSingleResult();
    	}
		
		return idePessoaJuridica;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DeclaracaoTransporteEntidadeTransportadora> consultarEntidadeTransportadora(Integer firstPage,Integer pageSize,DeclaracaoTransporte declaracaoTransporte) throws Exception {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DeclaracaoTransporteEntidadeTransportadora.class)
				.createAlias("declaracaoTransporte", "declaracaoTransporte")
				.add(Restrictions.eq("declaracaoTransporte.ideDeclaracaoTransporte", declaracaoTransporte.getIdeDeclaracaoTransporte()));
		
		List<DeclaracaoTransporteEntidadeTransportadora> lista = declaracaoTransporteEntidadeTransportadoraDAO.listarPorCriteriaDemanda(detachedCriteria, firstPage, pageSize);
		
		for (DeclaracaoTransporteEntidadeTransportadora entidade : lista) {
			EnderecoPessoa enderecoPessoa = enderecoPessoaService.buscarEnderecoPorPessoa(entidade.getPessoa());
			// verificar endereço
			if(enderecoPessoa != null && enderecoPessoa.getIdeEndereco() != null) {
				Endereco endereco = this.enderecoFacade.carregar(enderecoPessoa.getIdeEndereco().getIdeEndereco());
				
				if(!Util.isNull(endereco)) {
					entidade.getPessoa().setEndereco(endereco);
				}
			}
		}
		
		return lista;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public int consultarEntidadeTransportadora(DeclaracaoTransporte declaracaoTransporte)  {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DeclaracaoTransporteEntidadeTransportadora.class)
				.createAlias("declaracaoTransporte", "declaracaoTransporte")
				.add(Restrictions.eq("declaracaoTransporte.ideDeclaracaoTransporte", declaracaoTransporte.getIdeDeclaracaoTransporte()));
		
		return declaracaoTransporteEntidadeTransportadoraDAO.count(detachedCriteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirEntidadeTransportadora(DeclaracaoTransporteEntidadeTransportadora declaracaoTransporteEntidadeTransportadora) {
		this.declaracaoTransporteEntidadeTransportadoraDAO.remover(declaracaoTransporteEntidadeTransportadora);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean existeTransportadora(Pessoa pessoaTransportadora, DeclaracaoTransporte declaracaoTransporte) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DeclaracaoTransporteEntidadeTransportadora.class)
				.createAlias("pessoa", "pessoa")
				.createAlias("declaracaoTransporte", "declaracaoTransporte")
				.add(Restrictions.eq("pessoa.idePessoa", pessoaTransportadora.getIdePessoa()))
				.add(Restrictions.eq("declaracaoTransporte.ideDeclaracaoTransporte", declaracaoTransporte.getIdeDeclaracaoTransporte()));
		
		List<DeclaracaoTransporteEntidadeTransportadora> lista = this.declaracaoTransporteEntidadeTransportadoraDAO.listarPorCriteria(detachedCriteria);
		
		return (!Util.isNullOuVazio(lista)) ? true : false;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DeclaracaoTransporteResiduoEndereco> consultarEnderecoGeracaoResiduo(Integer firstPage,Integer pageSize,DeclaracaoTransporte declaracaoTransporte) throws Exception {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DeclaracaoTransporteResiduoEndereco.class)
				.createAlias("declaracaoTransporte", "declaracaoTransporte")
				.createAlias("tipoEndereco", "tipoEndereco")
				.add(Restrictions.eq("declaracaoTransporte.ideDeclaracaoTransporte", declaracaoTransporte.getIdeDeclaracaoTransporte()))
				.add(Restrictions.eq("tipoEndereco.ideTipoEndereco", TipoEnderecoEnum.GERACAO_RESIDUO.getId()));
		
		
		List<DeclaracaoTransporteResiduoEndereco> lista = declaracaoTransporteEnderecoResiduoDAO.listarPorCriteriaDemanda(detachedCriteria, firstPage, pageSize);
		
		for (DeclaracaoTransporteResiduoEndereco entidade : lista) {
			Endereco endereco = this.enderecoFacade.carregar(entidade.getEndereco().getIdeEndereco());
			
			if(!Util.isNull(endereco)) {
				entidade.setEndereco(endereco);
			}
		}
		
		return lista;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public int consultarQuantidadeEnderecoResiduo(DeclaracaoTransporte declaracaoTransporte)  {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DeclaracaoTransporteResiduoEndereco.class)
				.createAlias("declaracaoTransporte", "declaracaoTransporte")
				.createAlias("tipoEndereco", "tipoEndereco")
				.add(Restrictions.eq("declaracaoTransporte.ideDeclaracaoTransporte", declaracaoTransporte.getIdeDeclaracaoTransporte()))
				.add(Restrictions.eq("tipoEndereco.ideTipoEndereco", TipoEnderecoEnum.GERACAO_RESIDUO.getId()));
		
		return declaracaoTransporteEntidadeTransportadoraDAO.count(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DeclaracaoTransporteResiduoEndereco buscarEnderecoDestinacaoResiduo(DeclaracaoTransporte declaracaoTransporte)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(DeclaracaoTransporteResiduoEndereco.class)
				.createAlias("declaracaoTransporte", "declaracaoTransporte")
				.createAlias("tipoEndereco", "tipoEndereco")
				.add(Restrictions.eq("declaracaoTransporte.ideDeclaracaoTransporte", declaracaoTransporte.getIdeDeclaracaoTransporte()))
				.add(Restrictions.eq("tipoEndereco.ideTipoEndereco", TipoEnderecoEnum.DESTINACAO_RESIDUO.getId()));
		
		return declaracaoTransporteEnderecoResiduoDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirEnderecoResiduo(DeclaracaoTransporteResiduoEndereco declaracaoTransporteResiduoEndereco)  {
		this.declaracaoTransporteEnderecoResiduoDAO.remover(declaracaoTransporteResiduoEndereco);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarDeclaracaoTransporteDisposicaoFinalResiduo(DeclaracaoTransporteDisposicaoFinalResiduo declaracaoTransporteDisposicaoFinalResiduo)  {
		this.declaracaoTransporteDisposicaoFinalResiduoDAO.salvarOuAtualizar(declaracaoTransporteDisposicaoFinalResiduo);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DeclaracaoTransporteDisposicaoFinalResiduo consultarDeclaracaoTransporteDisposicaoFinalResiduo(DeclaracaoTransporteDisposicaoFinalResiduo declaracaoTransporteDisposicaoFinalResiduo) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DeclaracaoTransporteDisposicaoFinalResiduo.class)
				.createAlias("declaracaoTransporte", "declaracaoTransporte")
				.createAlias("disposicaoFinalResiduo", "disposicaoFinalResiduo")
				.add(Restrictions.eq("declaracaoTransporte.ideDeclaracaoTransporte", declaracaoTransporteDisposicaoFinalResiduo.getDeclaracaoTransporte().getIdeDeclaracaoTransporte()))
				.add(Restrictions.eq("disposicaoFinalResiduo.ideDisposicaoFinalResiduo", declaracaoTransporteDisposicaoFinalResiduo.getDisposicaoFinalResiduo().getIdeDisposicaoFinalResiduo()));
		
		return declaracaoTransporteDisposicaoFinalResiduoDAO.buscarPorCriteria(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DeclaracaoTransporteDisposicaoFinalResiduo> consultarDeclaracaracaoDisposicaoFinalResiduo(DeclaracaoTransporte declaracaoTransporte){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DeclaracaoTransporteDisposicaoFinalResiduo.class)
				.createAlias("declaracaoTransporte", "declaracaoTransporte")
				.add(Restrictions.eq("declaracaoTransporte.ideDeclaracaoTransporte", declaracaoTransporte.getIdeDeclaracaoTransporte()));
		
		List<DeclaracaoTransporteDisposicaoFinalResiduo> lista = declaracaoTransporteDisposicaoFinalResiduoDAO.listarPorCriteria(detachedCriteria);
		
		for (DeclaracaoTransporteDisposicaoFinalResiduo entidade : lista) {
			entidade.setDisposicaoFinalResiduo(this.disposicaoFinalResiduoDAO.carregarGet(entidade.getDisposicaoFinalResiduo().getIdeDisposicaoFinalResiduo()));
		}
		
		return lista;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DeclaracaoTransporteResiduo> consultarDeclaracaoTransporteResiduo(DeclaracaoTransporte declaracaoTransporte)  {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DeclaracaoTransporteResiduo.class)
				.createAlias("declaracaoTransporte", "declaracaoTransporte")
				.add(Restrictions.eq("declaracaoTransporte.ideDeclaracaoTransporte", declaracaoTransporte.getIdeDeclaracaoTransporte()));
		
		return declaracaoTransporteResiduoDAO.listarPorCriteria(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DeclaracaoTransporteResiduoEndereco obterDeclaracaoTransporteResiduoEndereco(DeclaracaoTransporteResiduoEndereco declaracaoTransporteResiduoEndereco) throws Exception {
				
		DeclaracaoTransporteResiduoEndereco declaracao = declaracaoTransporteEnderecoResiduoDAO.carregarGet(declaracaoTransporteResiduoEndereco.getIdeDeclaracaoTransporteResiduo());
		
		Endereco endereco = this.enderecoFacade.carregar(declaracao.getEndereco().getIdeEndereco());
		
		if(!Util.isNull(endereco)) {
			declaracao.setEndereco(endereco);
		}
		
		return declaracao;
	}

	public List<ProcuradorPessoaFisica> recuperarProcuradorPessoaFisicaPorRequerente(Pessoa pessoa)  {
		Map<String, Object> parametros = new TreeMap<String, Object>();
		parametros.put("idePessoaFisica", pessoa.getIdePessoa());
		parametros.put("indExcluido", Boolean.FALSE);
		
		List<ProcuradorPessoaFisica> collProcuradorPessoaFisica = procuradorPessoaFisicaDAO.buscarPorNamedQuery("ProcuradorPessoaFisica.findByIdePessoaFisica", parametros);
		
		for (ProcuradorPessoaFisica p : collProcuradorPessoaFisica) {
			p.getIdeProcurador().setPessoa(pessoaService.carregarGet(p.getIdeProcurador().getIdePessoaFisica()));
		}
		
		return collProcuradorPessoaFisica;
	}

	public List<ProcuradorRepresentante> recuperarProcuradorRepresentantePorRequerente(Pessoa pessoa)  {
		Map<String, Object> parametros = new TreeMap<String, Object>();
		parametros.put("idePessoaJuridica", pessoa.getIdePessoa());
		parametros.put("indExcluido", Boolean.FALSE);
		
		List<ProcuradorRepresentante> collProcuradorRepresentante = procuradorRepresentanteDAO.buscarPorNamedQuery("ProcuradorRepresentante.findByIdePessoaJuridica", parametros);
		
		for (ProcuradorRepresentante p : collProcuradorRepresentante) {
			p.getIdeProcurador().setPessoa(pessoaService.carregarGet(p.getIdeProcurador().getIdePessoaFisica()));
		}
		
		return collProcuradorRepresentante;
	}

	public List<RepresentanteLegal> recuperarRepresentanteLegalPorRequerente(Pessoa pessoa)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(RepresentanteLegal.class)
				.setFetchMode("idePessoaFisica", FetchMode.JOIN)
				.setFetchMode("pf.pessoa", FetchMode.JOIN)
				
				.add(Restrictions.eq("idePessoaJuridica.idePessoaJuridica", pessoa.getIdePessoa()))
				.add(Restrictions.eq("indExcluido", Boolean.FALSE));
		return representanteLegalDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean hasCertificado(Integer ideRequerimento)  {
		return this.certificadoService.exists(ideRequerimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCertificadoDTRP(Certificado certificado)  {
		String numeroCertificao = this.certificadoService.gerarNumeroCertificado(certificado);
		certificado.setNumCertificado(numeroCertificao);
		this.certificadoService.salvar(certificado);
	}
	
	public boolean hasToken(Integer ideRequerimento, AtoAmbientalEnum atoEnum)  {
		return this.certificadoService.hasToken(ideRequerimento, atoEnum);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Certificado carregarCertificado(Integer ideRequerimento, AtoAmbientalEnum atoEnum)  {
		return this.certificadoService.carregarByIdRequerimentoAndAtoDTRP(ideRequerimento, atoEnum);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarTokenCertificado(Certificado certificado)  {
		this.certificadoService.gerarEValidarToken(certificado);
		this.certificadoService.atualizar(certificado);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DeclaracaoTransporte carregar(Integer ideDeclaracaoTransporte){
		DetachedCriteria criteria = DetachedCriteria.forClass(DeclaracaoTransporte.class)
			.add(Restrictions.eq("ideDeclaracaoTransporte", ideDeclaracaoTransporte));
		
		DeclaracaoTransporte declaracao = declaracaoTransporteDAO.buscarPorCriteria(criteria);
		
		Hibernate.initialize(declaracao.getListaDeclaracaoTransporteDisposicaoFinalResiduo());
		
		if(!Util.isNullOuVazio(declaracao.getListaDeclaracaoTransporteDisposicaoFinalResiduo())) {
			for(DeclaracaoTransporteDisposicaoFinalResiduo dtdfr : declaracao.getListaDeclaracaoTransporteDisposicaoFinalResiduo()) {
				dtdfr.setDisposicaoFinalResiduo(disposicaoFinalResiduoDAO.carregarGet(dtdfr.getDisposicaoFinalResiduo().getIdeDisposicaoFinalResiduo()));
			}
		}
		
		return declaracao;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerResiduo(DeclaracaoTransporteResiduo residuoExcluir)  {
		declaracaoTransporteResiduoDAO.remover(residuoExcluir);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void gerarRequerimento(Requerimento requerimento)  {
		Collection<RequerimentoPessoa> collRequerimentoPessoa = requerimento.getRequerimentoPessoaCollection();
		requerimento.setRequerimentoPessoaCollection(new ArrayList<RequerimentoPessoa>());

		Collection<TramitacaoRequerimento> collTramitacaoRequerimento = requerimento.getTramitacaoRequerimentoCollection();
		requerimento.setTramitacaoRequerimentoCollection(new ArrayList<TramitacaoRequerimento>());
		
		requerimentoService.inserirRequerimento(requerimento);
		requerimentoUnicoService.salvarTramitacaoRequerimento(requerimento, collTramitacaoRequerimento);
		requerimentoUnicoService.salvarPessoasRequerimento(requerimento, collRequerimentoPessoa);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DeclaracaoTransporteResiduo obterDeclaracaoTransporteResiduoPor(Integer ideDeclaracaoTransporteResiduo){
		return declaracaoTransporteResiduoDAO.carregarGet(ideDeclaracaoTransporteResiduo);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarDeclaracaoTransporteResiduo(DeclaracaoTransporteResiduo declaracaoTransporteResiduo)  {
		declaracaoTransporteResiduoDAO.salvarOuAtualizar(declaracaoTransporteResiduo);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerDisposicaoFinalResiduo(DeclaracaoTransporteDisposicaoFinalResiduo residuoExcluir)  {
		declaracaoTransporteDisposicaoFinalResiduoDAO.remover(residuoExcluir);
	}
}
