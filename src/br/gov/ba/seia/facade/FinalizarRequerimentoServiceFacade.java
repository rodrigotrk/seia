package br.gov.ba.seia.facade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.faces.context.FacesContext;

import org.apache.log4j.Level;

import br.gov.ba.seia.dto.CumprimentoReposicaoFlorestalDTO;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.Classe;
import br.gov.ba.seia.entity.DeclaracaoTransporte;
import br.gov.ba.seia.entity.DocumentoAto;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.EmpreendimentoRequerimento;
import br.gov.ba.seia.entity.Enquadramento;
import br.gov.ba.seia.entity.EnquadramentoAtoAmbiental;
import br.gov.ba.seia.entity.EnquadramentoDocumentoAto;
import br.gov.ba.seia.entity.Licenca;
import br.gov.ba.seia.entity.PagamentoReposicaoFlorestal;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.Porte;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoPessoa;
import br.gov.ba.seia.entity.RequerimentoTipologia;
import br.gov.ba.seia.entity.TipoRequerimento;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.entity.TramitacaoRequerimento;
import br.gov.ba.seia.entity.UnidadeMedidaTipologiaGrupo;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.PagamentoReposicaoFlorestalEnum;
import br.gov.ba.seia.enumerator.PerguntaEnum;
import br.gov.ba.seia.enumerator.PorteEnum;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.enumerator.TipoAreaProtegidaEnum;
import br.gov.ba.seia.enumerator.TipoPessoaRequerimentoEnum;
import br.gov.ba.seia.enumerator.TipoRequerimentoEnum;
import br.gov.ba.seia.service.AtoAmbientalService;
import br.gov.ba.seia.service.DeclaracaoTransporteService;
import br.gov.ba.seia.service.DocumentoAtoAmbientalService;
import br.gov.ba.seia.service.DocumentoObrigatorioRequerimentoService;
import br.gov.ba.seia.service.DocumentoObrigatorioService;
import br.gov.ba.seia.service.EnquadramentoService;
import br.gov.ba.seia.service.LicencaService;
import br.gov.ba.seia.service.NovoRequerimentoService;
import br.gov.ba.seia.service.PerguntaRequerimentoService;
import br.gov.ba.seia.service.PessoaService;
import br.gov.ba.seia.service.ProcessoService;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.service.RequerimentoTipologiaService;
import br.gov.ba.seia.service.RequerimentoUnicoService;
import br.gov.ba.seia.service.TramitacaoRequerimentoService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FinalizarRequerimentoServiceFacade {
	
	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;
	@EJB
	private EnquadramentoService enquadramentoService;
	@EJB
	private AtoAmbientalService atoAmbientalService;
	@EJB
	private PessoaService pessoaService;
	@EJB
	private DocumentoObrigatorioRequerimentoService documentoObrigatorioReqService;
	@EJB
	private DocumentoAtoAmbientalService documentoAtoAmbientalService;
	@EJB
	private NovoRequerimentoService novoRequerimentoService;
	@EJB
	private RequerimentoUnicoService requerimentoUnicoService;
	@EJB
	private PerguntaRequerimentoService perguntaRequerimentoService;
	@EJB
	private RequerimentoTipologiaService requerimentoTipologiaService;
	@EJB
	private LicencaService licencaService;
	@EJB
	private RequerimentoService requerimentoService;
	@EJB
	private DocumentoObrigatorioService documentoObrigatorioService;
	@EJB
	private ProcessoService processoService;
	
	@EJB
	private DeclaracaoTransporteService declaracaoTransporteService;
	
	private void finalizarRequerimentoAutomaticamenteBy(AtoAmbientalEnum atoEnum, Requerimento requerimento, Collection<Tipologia> listaTopologia, 
			boolean isSalvarEmpreendimentoRequerimento, boolean indDla){
		gerarNumeroRequerimento(requerimento, TipoRequerimentoEnum.REGULACAO_AMBIENTAL_DO_EMPREENDIMENTO);
		salvarRequerimentoTipologia(requerimento,listaTopologia);
		efetuarEnquadramentoAutomatico(requerimento, atoEnum, null);
		carregaMsgAberturaRequerimento(requerimento);
		if(isSalvarEmpreendimentoRequerimento){
			salvarEmpreendimentoRequerimento(requerimento, indDla);
		}
	}
	
	private void finalizarReenquadramentoAutomaticamenteBy(AtoAmbientalEnum atoEnum, Requerimento requerimento, Collection<Tipologia> listaTopologia, boolean isSalvarEmpreendimentoRequerimento, boolean indDla){
		salvarRequerimentoTipologia(requerimento,listaTopologia);
		if(isSalvarEmpreendimentoRequerimento){
			salvarEmpreendimentoRequerimento(requerimento, indDla);
		}
	}
	
	private void finalizarRequerimentoAutomaticamenteByAPE(AtoAmbientalEnum atoEnum, Requerimento requerimento, Collection<Tipologia> listaTopologia, boolean isSalvarEmpreendimentoRequerimento, boolean indDla){
		gerarNumeroRequerimento(requerimento, TipoRequerimentoEnum.REGULACAO_AMBIENTAL_DO_EMPREENDIMENTO);
		salvarRequerimentoTipologia(requerimento,listaTopologia);
		efetuarEnquadramentoAutomatico(requerimento, atoEnum, listaTopologia);
		carregaMsgAberturaRequerimento(requerimento);
		if(isSalvarEmpreendimentoRequerimento){
			salvarEmpreendimentoRequerimento(requerimento, indDla);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizarRequerimentoAutorizacaoEspecial(Requerimento requerimento, Collection<Tipologia> listaTopologia) {
		finalizarRequerimentoAutomaticamenteByAPE(AtoAmbientalEnum.APE, requerimento, listaTopologia, true, false);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizarRequerimentoAtoDeclaratorioDIAP(Requerimento requerimento, Collection<Tipologia> listaTopologia) {
		finalizarRequerimentoAutomaticamenteBy(AtoAmbientalEnum.DIAP, requerimento, listaTopologia, true, true);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizarRequerimentoAtoDeclaratorioAMC(Requerimento requerimento, Collection<Tipologia> listaTopologia) {
		finalizarRequerimentoAutomaticamenteBy(AtoAmbientalEnum.AMC, requerimento, listaTopologia, true, false);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizarReenquadramentoAtoDeclaratorioAMC(Requerimento requerimento, Collection<Tipologia> listaTopologia) {
		finalizarReenquadramentoAutomaticamenteBy(AtoAmbientalEnum.AMC, requerimento, listaTopologia, true, false);
	}
	
	private void salvarRequerimentoTipologia(Requerimento requerimento, Collection<Tipologia> listaTopologia) {
		try{
			for(Tipologia t :listaTopologia) {
				if(t.isTipologiaEspecial()) {
					RequerimentoTipologia requerimentoTipologia = gerarRequerimentoTipologia(requerimento, t);
					requerimentoTipologiaService.salvarOuAtualizar(requerimentoTipologia);
				}
			}
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private RequerimentoTipologia gerarRequerimentoTipologia(Requerimento requerimento, Tipologia tipologia) {
		
		RequerimentoTipologia requerimentoTipologia = new RequerimentoTipologia();
		requerimentoTipologia.setIdeRequerimento(requerimento);
		requerimentoTipologia.setIndTipologiaPrincipal(true);
		
		UnidadeMedidaTipologiaGrupo unidadeMedidaTipologiaGrupo = tipologia.getTipologiaGrupo().getUnidadeMedidaTipologiaGrupo();
		requerimentoTipologia.setIdeUnidadeMedidaTipologiaGrupo(unidadeMedidaTipologiaGrupo);
		
		if(tipologia.getOpcao()!= null){
			requerimentoTipologia.setIndAcaoTipologia(tipologia.getOpcao().getId());
		}else{
			requerimentoTipologia.setIndAcaoTipologia(null);
		}
		
		if (!Util.isNullOuVazio(tipologia.getValorAtividade()) && tipologia.isTipologiaEspecial()) {
			requerimentoTipologia.setValAtividade(tipologia.getValorAtividade());
		}
		
		return requerimentoTipologia;
	}
	
	private void gerarNumeroRequerimento(Requerimento requerimento, TipoRequerimentoEnum tipoRequerimentoEnum) {
		try{
			TipoRequerimento tipoRequerimento = new TipoRequerimento(tipoRequerimentoEnum.getIde());
			requerimento.setIdeTipoRequerimento(tipoRequerimento);
			requerimento.setDtcCriacao(new Date());
			requerimento.setDtcFinalizacao(new Date());
			requerimento.setIndDeclaracao(true);
			requerimento.setIndExcluido(false);
			
			if (Util.isNullOuVazio(requerimento.getNumRequerimento())) {
				requerimentoUnicoService.geraNumeroRequerimento(requerimento);
				novoRequerimentoService.inserirRequerimento(requerimento);
			}
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void gerarEnquadramentoDocumentoAto(AtoAmbiental atoAmbiental, Enquadramento enquadramento) {
		try {
			
			enquadramento.setEnquadramentoDocumentoAtoCollection(new ArrayList<EnquadramentoDocumentoAto>());
			
			Collection<DocumentoAto> listaDocumentoNecessario = null;
			
			if(atoAmbiental.equals(new AtoAmbiental(AtoAmbientalEnum.APE.getId()))){
				Collection<DocumentoObrigatorio> listaDocumentoObrigatorioNecessario = carregarListaDocumentoObrigatorio(enquadramento);
				listaDocumentoNecessario = documentoAtoAmbientalService.listarDocumentoAtoPorDocumentoObrigatorio(listaDocumentoObrigatorioNecessario);
			} 
			else {
				listaDocumentoNecessario = documentoAtoAmbientalService.listarDocumentoAtoByIdeAtoAmbiental(atoAmbiental.getIdeAtoAmbiental());
			}
			
			for (DocumentoAto documentoAto : listaDocumentoNecessario) {
					documentoAto.setChecked(true);
					EnquadramentoDocumentoAto enquadramentoDocumentoAto = new EnquadramentoDocumentoAto();
					enquadramentoDocumentoAto.setEnquadramento(enquadramento);
					enquadramentoDocumentoAto.setDocumentoAto(documentoAto);
					enquadramento.getEnquadramentoDocumentoAtoCollection().add(enquadramentoDocumentoAto);
			}
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private Collection<DocumentoObrigatorio> carregarListaDocumentoObrigatorio(
			Enquadramento enquadramento) throws Exception {
		ArrayList<DocumentoObrigatorio> listaDocumentoObrigatorioNecessario = new ArrayList<DocumentoObrigatorio>();
		
		List<PerguntaRequerimento> listarPerguntaRequerimento = perguntaRequerimentoService.listarPerguntaRequerimentoPor(
			enquadramento.getIdeRequerimento(),
			Arrays.asList(new String[] {
				PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA3_111.getCod(),
				PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA3_1111.getCod(),
				PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA3_1121.getCod(),
				PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA3_13.getCod()
			})
		);
		
		
		listaDocumentoObrigatorioNecessario.add(new DocumentoObrigatorio(DocumentoObrigatorioEnum.FORMULARIO_DE_CARACTERIZACAO_DO_EMPREENDIMENTO_AGROSSILVIPASTORIL.getId()));
		listaDocumentoObrigatorioNecessario.add(new DocumentoObrigatorio(DocumentoObrigatorioEnum.CERTIDAO_MUNICIPAL_DE_REGULAR_USO_E_OCUPACAO_DO_SOLO.getId()));
		listaDocumentoObrigatorioNecessario.add(new DocumentoObrigatorio(DocumentoObrigatorioEnum.TERMO_DE_COMPROMISSO_CERTIFICADO_DO_CEFIR_OU_RECIBO_DO_CAR.getId()));
		
		
		for(PerguntaRequerimento pr : listarPerguntaRequerimento) {
			if(pr.getIdePergunta().getCodPergunta().equals(PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA3_111.getCod())) {
				if(pr.getIndResposta()) {
					listaDocumentoObrigatorioNecessario.add(new DocumentoObrigatorio(DocumentoObrigatorioEnum.COMPROVACAO_DA_CONCESSAO_DE_AUTORIZACAO_DE_SUPRESSAO_DE_VEGETACAO_NATIVA_22_DE_JULHO_DE_2008.getId()));
				}
			}
			else if(pr.getIdePergunta().getCodPergunta().equals(PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA3_1111.getCod())) {
				if(pr.getIndResposta()) {
					listaDocumentoObrigatorioNecessario.add(new DocumentoObrigatorio(DocumentoObrigatorioEnum.DECLARACAO_DA_ATIVIDADE_ANTROPICA.getId()));
				} else {
					listaDocumentoObrigatorioNecessario.add(new DocumentoObrigatorio(DocumentoObrigatorioEnum.TERMO_COMPROMISSO_APE.getId()));
				}
				
			}
			else if(pr.getIdePergunta().getCodPergunta().equals(PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA3_1121.getCod())) {
				if(pr.getIndResposta()) {
					listaDocumentoObrigatorioNecessario.add(new DocumentoObrigatorio(DocumentoObrigatorioEnum.COMPROVACAO_DA_CONCESSAO_DE_OUTORGA_DE_DIREITO_DE_USO_DE_RECURSOS_HIDRICOS_OU_DECLARACAO_DE_DISPENSA.getId()));
				}
				
			}
			else if(pr.getIdePergunta().getCodPergunta().equals(PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA3_13.getCod())) {
				if(pr.getIndResposta()) {
					Licenca licenca = licencaService.buscarLicencaPor(enquadramento.getIdeRequerimento(), TipoAreaProtegidaEnum.UNIDADE_DE_CONSERVACAO);
					if(!Util.isNull(licenca)) {
						listaDocumentoObrigatorioNecessario.add(new DocumentoObrigatorio(DocumentoObrigatorioEnum.ANUENCIA_DO_ORGAO_GESTOR_DA_UC.getId()));
					}
				}
			}
		}
		return listaDocumentoObrigatorioNecessario;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void gerarEnquadramentoAtoAmbiental(AtoAmbiental atoAmbiental, Enquadramento enquadramento, Tipologia tipologia) {
		if (Util.isNullOuVazio(enquadramento.getEnquadramentoAtoAmbientalCollection()) && !Util.isNullOuVazio(tipologia)) {
			enquadramento.setEnquadramentoAtoAmbientalCollection(new ArrayList<EnquadramentoAtoAmbiental>());
		}
		EnquadramentoAtoAmbiental enquadramentoAtoAmbiental = new EnquadramentoAtoAmbiental(enquadramento,atoAmbiental, tipologia);
		
		enquadramento.setEnquadramentoAtoAmbientalCollection(new ArrayList<EnquadramentoAtoAmbiental>());
		enquadramento.getEnquadramentoAtoAmbientalCollection().add(enquadramentoAtoAmbiental);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void efetuarEnquadramentoAutomatico(Requerimento requerimento, AtoAmbientalEnum atoAmbientalEnum, Collection<Tipologia> listaTipologia)  {
		try {
			tramitacaoRequerimentoService.tramitarAutomaticamente(requerimento, StatusRequerimentoEnum.SENDO_ENQUADRADO);
			
			AtoAmbiental atoAmbiental = atoAmbientalService.carregarById(atoAmbientalEnum.getId());
			
			Enquadramento enquadramento = gerarEnquadramento(requerimento, atoAmbiental, listaTipologia);
			
			enquadramentoService.salvar(enquadramento);
			
			for(EnquadramentoAtoAmbiental eaa : enquadramento.getEnquadramentoAtoAmbientalCollection()) {
				documentoObrigatorioReqService.atualizarEnquadramentoDosDocumentosByRequerimento(enquadramento.getIdeRequerimento(), eaa);
			}
			
			enquadramentoService.salvarComunicacao(enquadramento);
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private Enquadramento gerarEnquadramento(Requerimento requerimento, AtoAmbiental atoAmbiental, Collection<Tipologia> listaTipologia) {
		try {
			Enquadramento enquadramento = new Enquadramento();
			enquadramento.setIdeRequerimento(requerimento);
			enquadramento.setIndEnquadramentoAprovado(true);
			enquadramento.setIndPassivelEiarima(false);
			enquadramento.setIdePessoa(pessoaService.carregarUsuarioSEIA());
			
			if (!Util.isNullOuVazio(listaTipologia)) {
				for (Tipologia tipologia : listaTipologia) {
					gerarEnquadramentoAtoAmbiental(atoAmbiental, enquadramento, tipologia);
				}
			} else {
				gerarEnquadramentoAtoAmbiental(atoAmbiental, enquadramento, null);
			}
			
			gerarEnquadramentoDocumentoAto(atoAmbiental, enquadramento);
			
			return enquadramento;
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private void carregaMsgAberturaRequerimento(Requerimento requerimento) {
		try {

			String mensagem = new String("O requerimento foi aberto com sucesso. O número para acompanhamento é: "  + requerimento.getNumRequerimento()
					+ ". ------------------------------------ "
					+ "Você receberá por e-mail a relação de documentos para abertura do processo. Após o envio da documentação você receberá "
					+ "o boleto para pagamento da taxa ambiental.");

			ContextoUtil.getContexto().setObject(mensagem);
			FacesContext.getCurrentInstance().getExternalContext().redirect("/paginas/novo-requerimento/consulta.xhtml");
		}
		catch (IOException e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			
		}
	}
	
	public TramitacaoRequerimento buscarUltimaTramitacaoPorRequerimento(Integer pRequerimento) {
		try {
			return tramitacaoRequerimentoService.buscarUltimaTramitacaoPorRequerimento(pRequerimento);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizarRequerimentoDTRP(DeclaracaoTransporte declaracaoTransporte, Requerimento requerimento, RequerimentoPessoa solicitante) throws Exception {
		declaracaoTransporteService.salvarDeclaracaoTransporte(declaracaoTransporte);
		
		requerimento.setIdeOrgao(requerimentoUnicoService.recuperarOrgao(requerimento.getIdeOrgao()));
		
		gerarNumeroRequerimentoDTRP(requerimento, TipoRequerimentoEnum.REQUERIMENTO_ATO_DECLARATORIO);
		efetuarEnquadramentoAutomaticoDTRP(requerimento, AtoAmbientalEnum.DTRP, solicitante);
		carregaMsgAberturaRequerimento(requerimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizarRequerimentoCRF(CumprimentoReposicaoFlorestalDTO cumprimentoReposicaoFlorestalDTO) throws Exception {
		
		cumprimentoReposicaoFlorestalDTO.getRequerimento().setIdeOrgao(requerimentoUnicoService.recuperarOrgao(cumprimentoReposicaoFlorestalDTO.getRequerimento().getIdeOrgao()));
		
		gerarNumeroRequerimentoTipo(cumprimentoReposicaoFlorestalDTO.getRequerimento(), TipoRequerimentoEnum.REQUERIMENTO_ATO_DECLARATORIO, "REPFLOR");
		efetuarEnquadramentoAutomaticoCRF(cumprimentoReposicaoFlorestalDTO, AtoAmbientalEnum.CRF);
		
			try {

				String mensagem = new String("O requerimento foi aberto com sucesso. O número para acompanhamento é: "  + cumprimentoReposicaoFlorestalDTO.getRequerimento().getNumRequerimento()
						+ ". ------------------------------------ "
						+ "Você receberá por e-mail as orientações para dar continuidade ao requerimento.");

				ContextoUtil.getContexto().setObject(mensagem);
				FacesContext.getCurrentInstance().getExternalContext().redirect("/paginas/novo-requerimento/consulta.xhtml");
			}
			catch (IOException e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				
			}
	}

	private void gerarNumeroRequerimentoDTRP(Requerimento requerimento, TipoRequerimentoEnum tipoRequerimentoEnum) {
		try{
			requerimento.setRequerimentoPessoaCollection(new ArrayList<RequerimentoPessoa>());
			
			TipoRequerimento tipoRequerimento = new TipoRequerimento(tipoRequerimentoEnum.getIde());
			requerimento.setIdeTipoRequerimento(tipoRequerimento);
			requerimento.setDtcFinalizacao(new Date());
			requerimento.setIndDeclaracao(true);
			requerimento.setIndExcluido(false);
			
			if (Util.isNullOuVazio(requerimento.getNumRequerimento())) {
				requerimentoUnicoService.geraNumeroRequerimentoDTRP(requerimento);
//				novoRequerimentoService.inserirRequerimento(requerimento);
				requerimentoService.atualizarRequerimento(requerimento);
			}
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void gerarNumeroRequerimentoTipo(Requerimento requerimento, TipoRequerimentoEnum tipoRequerimentoEnum, String tipo) throws Exception {
		requerimento.setRequerimentoPessoaCollection(new ArrayList<RequerimentoPessoa>());
		
		TipoRequerimento tipoRequerimento = new TipoRequerimento(tipoRequerimentoEnum.getIde());
		requerimento.setIdeTipoRequerimento(tipoRequerimento);
		requerimento.setDtcFinalizacao(new Date());
		requerimento.setIndDeclaracao(true);
		requerimento.setIndExcluido(false);
		
		if (Util.isNullOuVazio(requerimento.getNumRequerimento())) {
			requerimentoUnicoService.geraNumeroRequerimentoTipo(requerimento, tipo);
			requerimentoService.atualizarRequerimento(requerimento);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void efetuarEnquadramentoAutomaticoCRF(CumprimentoReposicaoFlorestalDTO cumprimentoReposicaoFlorestalDTO, AtoAmbientalEnum atoAmbientalEnum)  {
		try {
			tramitacaoRequerimentoService.tramitarAutomaticamente(cumprimentoReposicaoFlorestalDTO.getRequerimento(), StatusRequerimentoEnum.SENDO_ENQUADRADO);
			
			AtoAmbiental atoAmbiental = atoAmbientalService.carregarById(atoAmbientalEnum.getId());
			
			Enquadramento enquadramento = gerarEnquadramentoCRF(cumprimentoReposicaoFlorestalDTO, atoAmbiental);
			
			enquadramentoService.salvar(enquadramento);
			
			for(EnquadramentoAtoAmbiental eaa : enquadramento.getEnquadramentoAtoAmbientalCollection()) {
				documentoObrigatorioReqService.atualizarEnquadramentoDosDocumentosByRequerimento(enquadramento.getIdeRequerimento(), eaa);
			}
			
			enquadramentoService.salvarComunicacao(enquadramento);
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void efetuarEnquadramentoAutomaticoDTRP(Requerimento requerimento, AtoAmbientalEnum atoAmbientalEnum, RequerimentoPessoa requerente)  {
		try {
			tramitacaoRequerimentoService.tramitarAutomaticamente(requerimento, StatusRequerimentoEnum.SENDO_ENQUADRADO);
			
			AtoAmbiental atoAmbiental = atoAmbientalService.carregarById(atoAmbientalEnum.getId());
			
			Enquadramento enquadramento = gerarEnquadramentoDTRP(requerimento, atoAmbiental, requerente);
			
			enquadramentoService.salvar(enquadramento);
			
			for(EnquadramentoAtoAmbiental eaa : enquadramento.getEnquadramentoAtoAmbientalCollection()) {
				documentoObrigatorioReqService.atualizarEnquadramentoDosDocumentosByRequerimento(enquadramento.getIdeRequerimento(), eaa);
			}
			
			enquadramentoService.salvarComunicacao(enquadramento);
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void gerarEnquadramentoDocumentoAtoDTRP(AtoAmbiental atoAmbiental, Enquadramento enquadramento, RequerimentoPessoa requerente) {
		try {
			
			enquadramento.setEnquadramentoDocumentoAtoCollection(new ArrayList<EnquadramentoDocumentoAto>());
			
			Collection<DocumentoObrigatorio> listaDocumentoObrigatorioNecessario = carregarListaDocumentoObrigatorioDTRP(requerente, atoAmbiental);
			Collection<DocumentoAto> listaDocumentoNecessario = documentoAtoAmbientalService.listarDocumentoAtoPorDocumentoObrigatorio(listaDocumentoObrigatorioNecessario);
			
			for (DocumentoAto documentoAto : listaDocumentoNecessario) {
					documentoAto.setChecked(true);
					EnquadramentoDocumentoAto enquadramentoDocumentoAto = new EnquadramentoDocumentoAto();
					enquadramentoDocumentoAto.setEnquadramento(enquadramento);
					enquadramentoDocumentoAto.setDocumentoAto(documentoAto);
					enquadramento.getEnquadramentoDocumentoAtoCollection().add(enquadramentoDocumentoAto);
			}
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void gerarEnquadramentoDocumentoAtoCRF(AtoAmbiental atoAmbiental, Enquadramento enquadramento, CumprimentoReposicaoFlorestalDTO cumprimentoReposicaoFlorestalDTO) {
		try {
			
			enquadramento.setEnquadramentoDocumentoAtoCollection(new ArrayList<EnquadramentoDocumentoAto>());
			
			Collection<DocumentoObrigatorio> listaDocumentoObrigatorioNecessario = carregarListaDocumentoObrigatorioCRF(cumprimentoReposicaoFlorestalDTO, atoAmbiental);
			
			if (!Util.isNullOuVazio(listaDocumentoObrigatorioNecessario)) {
				Collection<DocumentoAto> listaDocumentoNecessario = documentoAtoAmbientalService.listarDocumentoAtoPorDocumentoObrigatorio(listaDocumentoObrigatorioNecessario);
				
				for (DocumentoAto documentoAto : listaDocumentoNecessario) {
						documentoAto.setChecked(true);
						EnquadramentoDocumentoAto enquadramentoDocumentoAto = new EnquadramentoDocumentoAto();
						enquadramentoDocumentoAto.setEnquadramento(enquadramento);
						enquadramentoDocumentoAto.setDocumentoAto(documentoAto);
						enquadramento.getEnquadramentoDocumentoAtoCollection().add(enquadramentoDocumentoAto);
				}
			}
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private Collection<DocumentoObrigatorio> carregarListaDocumentoObrigatorioDTRP(RequerimentoPessoa requerimentoPessoa, AtoAmbiental atoAmbiental) throws Exception {
		Collection<DocumentoAto> listaDocumentoAto = documentoAtoAmbientalService.listarDocumentoAtoByIdeAtoAmbiental(atoAmbiental.getIdeAtoAmbiental());
		
		List<DocumentoObrigatorio> retorno = new ArrayList<DocumentoObrigatorio>();
		
		for(DocumentoAto docAto : listaDocumentoAto) {
			retorno.add(docAto.getIdeDocumentoObrigatorio());
			if(requerimentoPessoa.isIndSolicitante() && 
					!TipoPessoaRequerimentoEnum.PROCURADOR.getId().equals(requerimentoPessoa.getIdeTipoPessoaRequerimento().getIdeTipoPessoaRequerimento())) {
				if(docAto.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio().equals(DocumentoObrigatorioEnum.DTRP_DOCUMENTO_5.getId()) || 
						docAto.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio().equals(DocumentoObrigatorioEnum.DTRP_DOCUMENTO_6.getId()) ||
						docAto.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio().equals(DocumentoObrigatorioEnum.PROCURACAO.getId()) ) 
//						|| docAto.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio().equals(DocumentoObrigatorioEnum.DTRP_DOCUMENTO_7.getId()))
					retorno.remove(docAto.getIdeDocumentoObrigatorio());
			} 
		}
		
		return retorno;
	}
	
	private Collection<DocumentoObrigatorio> carregarListaDocumentoObrigatorioCRF(CumprimentoReposicaoFlorestalDTO cumprimentoReposicaoFlorestalDTO, AtoAmbiental atoAmbiental) throws Exception {
		List<DocumentoObrigatorio> retorno = new ArrayList<DocumentoObrigatorio>();
		
		if (PagamentoReposicaoFlorestalEnum.DEVEDOR.getId().equals(cumprimentoReposicaoFlorestalDTO.getCumprimentoReposicaoFlorestal().getIdePagamentoReposicaoFlorestal().getIdePagamentoReposicaoFlorestal())) {
			retorno.add(new DocumentoObrigatorio(DocumentoObrigatorioEnum.CRF_DISPONIBILIZACAO_AUTO_INFRACAO.getId()));
		} else {
			
			PagamentoReposicaoFlorestal pagamentoReposicaoFlorestal = cumprimentoReposicaoFlorestalDTO.getCumprimentoReposicaoFlorestal().getIdePagamentoReposicaoFlorestal();
			if (!Util.isNullOuVazio(pagamentoReposicaoFlorestal.getIdePagamentoReposicaoFlorestalPai())) {
				pagamentoReposicaoFlorestal = pagamentoReposicaoFlorestal.getIdePagamentoReposicaoFlorestalPai();
			}
			
			
			if (PagamentoReposicaoFlorestalEnum.DETENTOR.getId().equals(pagamentoReposicaoFlorestal.getIdePagamentoReposicaoFlorestal())) {
				String numProcesso = cumprimentoReposicaoFlorestalDTO.getDetentorReposicaoFlorestal().getNumProcesso();
				
				Processo processo = processoService.buscarProcessoPorCriteria(numProcesso);
				
				if (Util.isNullOuVazio(processo)) {
					retorno.add(new DocumentoObrigatorio(DocumentoObrigatorioEnum.CRF_DISPONIBILIZACAO_PORTARIA_ATO_AUTORIZATIVO_FLORESTAL_ASV_AML.getId()));
				} else {
					retorno.add(new DocumentoObrigatorio(DocumentoObrigatorioEnum.CRF_DISPONIBILIZACAO_PORTARIA_ATO_AUTORIZATIVO_FLORESTAL.getId()));
				}

				
				if (!Util.isNullOuVazio(cumprimentoReposicaoFlorestalDTO.getDetentorReposicaoFlorestal().getNumProcessoAsvAml())) {
					Processo processoAsvAml = processoService.buscarProcessoPorCriteria(cumprimentoReposicaoFlorestalDTO.getDetentorReposicaoFlorestal().getNumProcessoAsvAml());
					
					if (Util.isNullOuVazio(processoAsvAml)) {
						retorno.add(new DocumentoObrigatorio(DocumentoObrigatorioEnum.CRF_DISPONIBILIZACAO_PORTARIA_ATO_AUTORIZATIVO_FLORESTAL_ASV_AML.getId()));
					}
				}
			} else {
				String numProcesso = cumprimentoReposicaoFlorestalDTO.getConsumidorReposicaoFlorestal().getNumProcessoOriginal();
				
				Processo processo = processoService.buscarProcessoPorCriteria(numProcesso);
				
				if (!Util.isNullOuVazio(cumprimentoReposicaoFlorestalDTO.getDetentorReposicaoFlorestal().getNumProcessoAsvAml())) {
					Processo processoAsvAml = processoService.buscarProcessoPorCriteria(cumprimentoReposicaoFlorestalDTO.getDetentorReposicaoFlorestal().getNumProcessoAsvAml());
					
					if (Util.isNullOuVazio(processoAsvAml)) {
						retorno.add(new DocumentoObrigatorio(DocumentoObrigatorioEnum.CRF_DISPONIBILIZACAO_PORTARIA_ATO_AUTORIZATIVO_FLORESTAL.getId()));
					}
				}
				
				if (Util.isNullOuVazio(processo)) {
					retorno.add(new DocumentoObrigatorio(DocumentoObrigatorioEnum.CRF_DISPONIBILIZACAO_PORTARIA_ATO_AUTORIZATIVO_FLORESTAL_ASV_AML.getId()));
				}
				else {
				     retorno.add(new DocumentoObrigatorio(DocumentoObrigatorioEnum.CRF_DISPONIBILIZACAO_PORTARIA_ATO_AUTORIZATIVO_FLORESTAL.getId()));
				}
			}
		}
		
		return retorno;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private Enquadramento gerarEnquadramentoDTRP(Requerimento requerimento, AtoAmbiental atoAmbiental, RequerimentoPessoa requerente) {
		try {
			Enquadramento enquadramento = new Enquadramento();
			enquadramento.setIdeRequerimento(requerimento);
			enquadramento.setIndEnquadramentoAprovado(true);
			enquadramento.setIndPassivelEiarima(false);
			enquadramento.setIdePessoa(pessoaService.carregarUsuarioSEIA());
			gerarEnquadramentoAtoAmbiental(atoAmbiental, enquadramento, null);
			gerarEnquadramentoDocumentoAtoDTRP(atoAmbiental, enquadramento, requerente);
			
			return enquadramento;
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private Enquadramento gerarEnquadramentoCRF(CumprimentoReposicaoFlorestalDTO cumprimentoReposicaoFlorestalDTO, AtoAmbiental atoAmbiental) {
		try {
			Enquadramento enquadramento = new Enquadramento();
			enquadramento.setIdeRequerimento(cumprimentoReposicaoFlorestalDTO.getRequerimento());
			enquadramento.setIndEnquadramentoAprovado(true);
			enquadramento.setIndPassivelEiarima(false);
			enquadramento.setIdePessoa(pessoaService.carregarUsuarioSEIA());
			gerarEnquadramentoAtoAmbiental(atoAmbiental, enquadramento,null);
			gerarEnquadramentoDocumentoAtoCRF(atoAmbiental, enquadramento, cumprimentoReposicaoFlorestalDTO);
			
			return enquadramento;
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private void salvarEmpreendimentoRequerimento(Requerimento requerimento, boolean indDla){
		
		try{
			for(EmpreendimentoRequerimento er : requerimento.getEmpreendimentoRequerimentoCollection()){
				EmpreendimentoRequerimento empreendimentoRequerimento = new EmpreendimentoRequerimento();
				empreendimentoRequerimento.setIdeRequerimento(requerimento);
				empreendimentoRequerimento.setIdeEmpreendimento(er.getIdeEmpreendimento());
				empreendimentoRequerimento.setIdePorte(new Porte(PorteEnum.NAO_IDENTIFICADO.getId()));
				empreendimentoRequerimento.setIndDla(indDla);
				empreendimentoRequerimento.setIdeClasse(new Classe(1));
				
				requerimentoService.atualizarEmpreendRequerimento(empreendimentoRequerimento);
			}
			
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
}