package br.gov.ba.seia.facade;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.controller.SilosArmazensController;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.AtividadeNaoSujeitaLicenciamentoDocumento;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLic;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLicDocApensado;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLicStatus;
import br.gov.ba.seia.entity.CaracterizacaoAmbientalCaracterizacaoAtmosferica;
import br.gov.ba.seia.entity.CaracterizacaoAmbientalDestinacaoFinal;
import br.gov.ba.seia.entity.CaracterizacaoAmbientalEquipamentoControle;
import br.gov.ba.seia.entity.CaracterizacaoAmbientalMedidaControleEmissao;
import br.gov.ba.seia.entity.CaracterizacaoAmbientalOrigemAgua;
import br.gov.ba.seia.entity.CaracterizacaoAmbientalSilosArmazen;
import br.gov.ba.seia.entity.ClassificacaoResiduo;
import br.gov.ba.seia.entity.ClassificacaoResiduoCaracterizacaoAmbiental;
import br.gov.ba.seia.entity.DestinacaoFinal;
import br.gov.ba.seia.entity.DocumentoIdentificacao;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.EnderecoPessoa;
import br.gov.ba.seia.entity.EquipamentoControle;
import br.gov.ba.seia.entity.FormacaoProfissional;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.ImovelEmpreendimento;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.MedidaControleEmissao;
import br.gov.ba.seia.entity.OperacaoDesenvolvidaSilosArmazen;
import br.gov.ba.seia.entity.OrigemAguaTipoConcessionaria;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.PessoaJuridicaCnae;
import br.gov.ba.seia.entity.RepresentanteLegal;
import br.gov.ba.seia.entity.ResponsavelEmpreendimento;
import br.gov.ba.seia.entity.SilosArmazen;
import br.gov.ba.seia.entity.SilosArmazensCaracterizacaoAtmosferica;
import br.gov.ba.seia.entity.SilosArmazensImovel;
import br.gov.ba.seia.entity.SilosArmazensLancamentoEfluente;
import br.gov.ba.seia.entity.SilosArmazensOperacaoDesenvolvida;
import br.gov.ba.seia.entity.SilosArmazensOrigemAgua;
import br.gov.ba.seia.entity.SilosArmazensResponsavelTecnico;
import br.gov.ba.seia.entity.SilosArmazensSistemaSeguranca;
import br.gov.ba.seia.entity.SilosArmazensSistemaTratamentoAgua;
import br.gov.ba.seia.entity.SilosArmazensTipoCombustivel;
import br.gov.ba.seia.entity.SilosArmazensUnidadeArmazenadora;
import br.gov.ba.seia.entity.SistemaSegurancaSilosArmazen;
import br.gov.ba.seia.entity.SistemaTratamentoAgua;
import br.gov.ba.seia.entity.Telefone;
import br.gov.ba.seia.entity.TipoArmazem;
import br.gov.ba.seia.entity.TipoAtividadeNaoSujeitaLicenciamento;
import br.gov.ba.seia.entity.TipoCombustivelSiloArmazen;
import br.gov.ba.seia.entity.TipoConcessionaria;
import br.gov.ba.seia.entity.TipoEspecieArmazem;
import br.gov.ba.seia.entity.TipoIdentificacao;
import br.gov.ba.seia.enumerator.CadastroAtividadeNaoSujeitaLicTipoStatusEnum;
import br.gov.ba.seia.enumerator.TipoAtividadeNaoSujeitaLicenciamentoEnum;
import br.gov.ba.seia.enumerator.TipoEnderecoEnum;
import br.gov.ba.seia.enumerator.TipoImovelEnum;
import br.gov.ba.seia.service.AtividadeNaoSujeitaLicenciamentoDocumentoService;
import br.gov.ba.seia.service.CadastroAtividadeNaoSujeitaLicDocApensadoService;
import br.gov.ba.seia.service.CadastroAtividadeNaoSujeitaLicService;
import br.gov.ba.seia.service.CadastroAtividadeNaoSujeitaLicStatusService;
import br.gov.ba.seia.service.CaracterizacaoAmbientalCaracterizacaoAtmosfericaService;
import br.gov.ba.seia.service.CaracterizacaoAmbientalDestinacaoFinalService;
import br.gov.ba.seia.service.CaracterizacaoAmbientalEquipamentoControleService;
import br.gov.ba.seia.service.CaracterizacaoAmbientalMedidaControleEmissaoService;
import br.gov.ba.seia.service.CaracterizacaoAmbientalOrigemAguaService;
import br.gov.ba.seia.service.CaracterizacaoAmbientalSilosArmazenService;
import br.gov.ba.seia.service.ClassificacaoResiduoCaracterizacaoAmbientalService;
import br.gov.ba.seia.service.ClassificacaoResiduoService;
import br.gov.ba.seia.service.DestinacaoFinalService;
import br.gov.ba.seia.service.DocumentoIdentificacaoService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.EnderecoEmpreendimentoService;
import br.gov.ba.seia.service.EnderecoPessoaService;
import br.gov.ba.seia.service.EnderecoService;
import br.gov.ba.seia.service.EquipamentoControleService;
import br.gov.ba.seia.service.FormacaoProfissionalService;
import br.gov.ba.seia.service.ImovelRuralService;
import br.gov.ba.seia.service.ImovelService;
import br.gov.ba.seia.service.LocalizacaoGeograficaService;
import br.gov.ba.seia.service.MedidaControleEmissaoService;
import br.gov.ba.seia.service.OperacaoDesenvolvidaSilosArmazenService;
import br.gov.ba.seia.service.OrigemAguaTipoConcessionariaService;
import br.gov.ba.seia.service.PessoaFisicaService;
import br.gov.ba.seia.service.PessoaJuridicaCnaeService;
import br.gov.ba.seia.service.PessoaJuridicaService;
import br.gov.ba.seia.service.RepresentanteLegalService;
import br.gov.ba.seia.service.ResponsavelEmpreendimentoService;
import br.gov.ba.seia.service.SilosArmazensCaracterizacaoAtmosfericaService;
import br.gov.ba.seia.service.SilosArmazensImovelRuralService;
import br.gov.ba.seia.service.SilosArmazensLancamentoEfluenteService;
import br.gov.ba.seia.service.SilosArmazensOperacaoDesenvolvidaService;
import br.gov.ba.seia.service.SilosArmazensOrigemAguaService;
import br.gov.ba.seia.service.SilosArmazensResponsavelTecnicoService;
import br.gov.ba.seia.service.SilosArmazensService;
import br.gov.ba.seia.service.SilosArmazensSistemaSegurancaService;
import br.gov.ba.seia.service.SilosArmazensSistemaTratamentoAguaService;
import br.gov.ba.seia.service.SilosArmazensTipoCombustivelService;
import br.gov.ba.seia.service.SilosArmazensUnidadeArmazenadoraService;
import br.gov.ba.seia.service.SistemaTratamentoAguaService;
import br.gov.ba.seia.service.TelefoneService;
import br.gov.ba.seia.service.TipoArmazemService;
import br.gov.ba.seia.service.TipoCombustivelSiloArmazenService;
import br.gov.ba.seia.service.TipoConcessionariaService;
import br.gov.ba.seia.service.TipoEspecieArmazemService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SilosArmazensFacade {

	@EJB
	private PessoaJuridicaCnaeService pessoaJuridicaCaneService;
	
	@EJB
	private TelefoneService telefoneService ;
	
	@EJB
	private EnderecoPessoaService enderecoPessoaService;
	
	@EJB
	private EnderecoService enderecoService ;
	
	@EJB
	private RepresentanteLegalService representanteLegalService;
	
	@EJB
	private PessoaJuridicaService pessoaJuridicaService;
	
	@EJB
	private PessoaFisicaService pessoaFisicaService;
	
	@EJB
	private CadastroAtividadeFacade cadastroFacade;
	
	@EJB
	private ResponsavelEmpreendimentoService  responsavelEmpreendimentoService;
	
	@EJB
	private DocumentoIdentificacaoService documentoIdentificacaoService;
	
	@EJB
	private FormacaoProfissionalService formacaoProfissionalService;
	
	@EJB
	private ImovelService imovelService;
	
	@EJB
	private OperacaoDesenvolvidaSilosArmazenService operacaoDesenvolvidaSilosArmazenService;
	
	@EJB
	private TipoCombustivelSiloArmazenService tipoCombustivelSiloArmazenService;
	
	@EJB
	private SilosArmazensService silosArmazensService;
	
	@EJB
	private CadastroAtividadeNaoSujeitaLicService cadastroAtividadeNaoSujeitaLicService;
	
	@EJB
	private SilosArmazensResponsavelTecnicoService silosArmazensResponsavelTecnicoService ;
	
	@EJB
	private SilosArmazensImovelRuralService silosArmazensImovelRuralService;
	
	@EJB	
	private ImovelRuralService imovelRuralService;

	@EJB
	private SilosArmazensOperacaoDesenvolvidaService silosArmazensOperacaoDesenvolvidaService;
	
	@EJB
	private SilosArmazensTipoCombustivelService silosArmazensTipoCombustivelService;
	
	@EJB
	private TipoEspecieArmazemService tipoEspecieArmazemService;
	
	@EJB
	private TipoArmazemService tipoArmazemService;
	
	@EJB
	private LocalizacaoGeograficaService localizacaoGeograficaService;
	
	@EJB
	private SilosArmazensUnidadeArmazenadoraService  silosArmazensUnidadeArmazenadoraService;
	
	@EJB
	private SilosArmazensOrigemAguaService silosArmazensOrigemAguaService;
	
	@EJB
	private TipoConcessionariaService tipoConcessionariaService;

	@EJB
	private SistemaTratamentoAguaService sistemaTratamentoAguaService;

	@EJB
	private SilosArmazensCaracterizacaoAtmosfericaService silosArmazensCaracterizacaoAtmosfericaService;
	
	@EJB
	private EquipamentoControleService equipamentoControleService;

	@EJB
	private MedidaControleEmissaoService medidaControleEmissaoService;

	@EJB
	private ClassificacaoResiduoService classificacaoResiduoService;

	@EJB
	private DestinacaoFinalService destinacaoFinalService;

	@EJB
	private SilosArmazensSistemaSegurancaService silosArmazensSistemaSegurancaService;

	@EJB
	private CaracterizacaoAmbientalOrigemAguaService caracterizacaoAmbientalOrigemAguaService;

	@EJB
	private OrigemAguaTipoConcessionariaService origemAguaTipoConcessionariaService;

	@EJB
	private SilosArmazensSistemaTratamentoAguaService silosArmazensSistemaTratamentoAguaService;

	@EJB
	private SilosArmazensLancamentoEfluenteService silosArmazensLancamentoEfluenteService;

	@EJB
	private ClassificacaoResiduoCaracterizacaoAmbientalService classificacaoResiduoCaracterizacaoAmbientalService;

	@EJB
	private CaracterizacaoAmbientalMedidaControleEmissaoService caracterizacaoAmbientalMedidaControleEmissaoService;

	@EJB
	private CaracterizacaoAmbientalEquipamentoControleService caracterizacaoAmbientalEquipamentoControleService;

	@EJB
	private CaracterizacaoAmbientalCaracterizacaoAtmosfericaService caracterizacaoAmbientalCaracterizacaoAtmosfericaService;
	
	@EJB
	private CaracterizacaoAmbientalSilosArmazenService caracterizacaoAmbientalSilosArmazenService;

	@EJB
	private CaracterizacaoAmbientalDestinacaoFinalService caracterizacaoAmbientalDestinacaoFinalService;
	
	@EJB
	private AtividadeNaoSujeitaLicenciamentoDocumentoService atividadeNaoSujeitaLicenciamentoDocumentoService;
	
	@EJB
	private CadastroAtividadeNaoSujeitaLicDocApensadoService docApensadoService;
	
	@EJB
	private CadastroAtividadeNaoSujeitaLicStatusService cadastroStatusService;
	
	@EJB
	private EmpreendimentoService empreendimentoService;
	
	@EJB
	private EnderecoEmpreendimentoService enderecoEmpreendimentoService;
	
	@EJB
	private CadastroAtividadeNaoSujeitaLicStatusService cadastroAtividadeNaoSujeitaLicStatusService;
	
	@EJB
	private CadastroAtividadeFacade cadastroAtividadeFacade;
	
	@Inject
	private IDAO<ImovelEmpreendimento> imovelEmpreendimendoDao;
	
	@Inject
	private IDAO<Imovel> idaoImovel;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<PessoaJuridicaCnae> buscaPessoaJuridicaCnaePorPessoaJuridica(PessoaJuridica pessoajuridica) {
		return pessoaJuridicaCaneService.buscaPessoaJuridicaCnaePorPessoaJuridica(pessoajuridica);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Telefone> buscarTelefonesPorPessoa(Pessoa pessoa) throws Exception {
		return telefoneService.buscarTelefonesPorPessoa(pessoa);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public EnderecoPessoa buscarEnderecoPorPessoa(Pessoa pessoa) {
		return enderecoPessoaService.buscarEnderecoPorPessoa(pessoa);
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Endereco buscarEnderecoPorPessoa(Integer ideEndereco) {
		return enderecoService.carregar(ideEndereco);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<RepresentanteLegal> listarRepresentanteLegalPorPessoaJuridica(PessoaJuridica pessoaJuridica)  {
		return representanteLegalService.listarRepresentanteLegalPorPessoaJuridica(pessoaJuridica);
	}
	
	public PessoaJuridica obterPessoaJuridicaId(PessoaJuridica pessoaJuridica)  {
		return pessoaJuridicaService.obterPessoaJuridicaId(pessoaJuridica);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaFisica carregarPessoaFisicaGet(Integer pIdePessoaFisica)  {
		return pessoaFisicaService.carregarPessoaFisicaGet(pIdePessoaFisica);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Endereco carregarEnderecoCorrespondencia(Empreendimento empreendimento) throws Exception {
		return cadastroFacade.carregarEnderecoBy(empreendimento, TipoEnderecoEnum.CORRESPONDENCIA);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public StreamedContent baixarArquivo(Object obj) {
		return cadastroFacade.baixarArquivo(obj);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Collection<ResponsavelEmpreendimento> filtrarResponsaveisPorEmpreendimento(Empreendimento empreendimento)  {
		return responsavelEmpreendimentoService.listarResponsaveisPorEmpreendimento(empreendimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String obterNumeroCarteiraConselhoClasse(Pessoa pessoa) throws Exception{
		for(DocumentoIdentificacao doc : documentoIdentificacaoService.listarDocumentosIdentificacaoPorPessoa(pessoa)){
			if(doc.getIdeTipoIdentificacao().equals(new TipoIdentificacao(5))){
				return doc.getNumDocumento();
			}
		}
		return null; 
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FormacaoProfissional> listarFormacaoProfissional(){
		return formacaoProfissionalService.listarFormacaoProfissional();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Imovel buscarImovelPorNumeroCar(ImovelRural imovelRural) {
		try {
			return silosArmazensImovelRuralService.buscarImovelPorNumeroCar(imovelRural);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public List<OperacaoDesenvolvidaSilosArmazen> listarOperacaoDesenvolvidaSilosArmazen(){
		return operacaoDesenvolvidaSilosArmazenService.listarOperacaoDesenvolvidaSilosArmazen();
	}
	
	public List<TipoCombustivelSiloArmazen> listarTipoCombustivelSiloArmazen() throws Exception{
		return tipoCombustivelSiloArmazenService.listarTipoCombustivelSiloArmazen();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAbaDadosBasicosSilosArmazens(SilosArmazen silos) throws Exception{
		salvarCadastroAtividadeNaoSujeitaLic(silos.getIdeCadastroAtividadeNaoSujeitaLic());
		silosArmazensService.salvarSilosArmazens(silos);
		salvarSilosArmazensResponsavelTecnico(silos.getSilosArmazensResponsavelTecnicos());
		salvarSilosArmazensImovelRural(silos.getSilosArmazensImovelRurals());
		salvarSilosArmazensOperacaoDesenvolvida(silos.getSilosArmazensOperacaoDesenvolvidas());
		
		if(!Util.isNullOuVazio(silos.getSilosArmazensTipoCombustivels())){
			
			salvarSilosArmazensTipoCombustivel(silos.getSilosArmazensTipoCombustivels());
		}
		
		if(Util.isNullOuVazio(silos.getIdeCadastroAtividadeNaoSujeitaLic().getCadastroAtividadeNaoSujeitaLicStatus())){
			tramitarCadastroIncompleto(silos.getIdeCadastroAtividadeNaoSujeitaLic());
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCadastroAtividadeNaoSujeitaLic(CadastroAtividadeNaoSujeitaLic cadastro){
		cadastroAtividadeNaoSujeitaLicService.salvar(cadastro);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarSilosArmazensResponsavelTecnico(List<SilosArmazensResponsavelTecnico> armazensResponsavelTecnico) throws Exception{
		
		for(SilosArmazensResponsavelTecnico tecnico : armazensResponsavelTecnico){
			
			if(!Util.isNullOuVazio(tecnico.getSilosArmazen())){
				excluirResponsavelTecnico(tecnico.getSilosArmazen());
				break;
			}
		}
		
		for(SilosArmazensResponsavelTecnico tecnico : armazensResponsavelTecnico){
			tecnico.setDtcCriacao(new Date());
			tecnico.setIdeSilosArmazensResponsavelTecnico(null);
			silosArmazensResponsavelTecnicoService.salvarSilosArmazensResponsavelTecnico(tecnico);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarSilosArmazensImovelRural(List<SilosArmazensImovel> armazensImovelRural) throws Exception{
		
		for(SilosArmazensImovel imovelRural: armazensImovelRural){
			if(!Util.isNullOuVazio(imovelRural.getSilosArmazen())){
				excluirImovelRural(imovelRural.getSilosArmazen());
				break;
			}
		}
		
		for(SilosArmazensImovel imovelRural: armazensImovelRural){
			imovelRural.setIdeSilosArmazensImovel(null);
			silosArmazensImovelRuralService.salvarSilosArmazensImovelRural(imovelRural);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarSilosArmazensOperacaoDesenvolvida(List<SilosArmazensOperacaoDesenvolvida> armazensOperacaoDesenvolvida) throws Exception{
		
		for(SilosArmazensOperacaoDesenvolvida op: armazensOperacaoDesenvolvida){
			
			if(!Util.isNullOuVazio(op.getSilosArmazen())){
				excluirOperacaoDesenvolvida(op.getSilosArmazen());
				break;
			}
		}
		
		
		for(SilosArmazensOperacaoDesenvolvida op: armazensOperacaoDesenvolvida){
			op.setIdeSilosArmazensOperacaoDesenvolvida(null);
			silosArmazensOperacaoDesenvolvidaService.salvarSilosArmazensOperacaoDesenvolvida(op);
		}
		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarSilosArmazensTipoCombustivel(List<SilosArmazensTipoCombustivel> silosArmazensTipoCombustivel) throws Exception{
		
		for(SilosArmazensTipoCombustivel comb:silosArmazensTipoCombustivel ){
			
			if(!Util.isNullOuVazio(comb.getSilosArmazen())){
				excluirTipoCombustivel(comb.getSilosArmazen());
				break;
			}
		}
		
		for(SilosArmazensTipoCombustivel comb:silosArmazensTipoCombustivel ){
			comb.setIdeSilosArmazensTipoCombustivel(null);
			silosArmazensTipoCombustivelService.salvarSilosArmazensTipoCombustivel(comb);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirByIdLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica){
		localizacaoGeograficaService.excluirByIdLocalizacaoGeografica(localizacaoGeografica);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarSilosArmazensUnidadeArmazenadora(List<SilosArmazensUnidadeArmazenadora> unidadeArmazenadora) throws Exception{
		
		for(SilosArmazensUnidadeArmazenadora unidade:unidadeArmazenadora){
			excluirUnidadeArmazenadora(unidade.getSilosArmazen());
			break;
		}
		
		for(SilosArmazensUnidadeArmazenadora unidade:unidadeArmazenadora){
			unidade.setIdeSilosArmazensUnidadeArmazenadora(null);
			silosArmazensUnidadeArmazenadoraService.salvarSilosArmazensUnidadeArmazenadora(unidade);
		}
	}
	
	public List<TipoEspecieArmazem> listarTipoEspecieArmazem() throws Exception{
		return tipoEspecieArmazemService.listarTipoEspecieArmazem();
	}
	
	public List<TipoArmazem> listarTipoArmazem() throws Exception{
		return tipoArmazemService.listarTipoArmazem();
	}
	
	public Collection<Imovel> carregarImoveisByEmpreendimento(Empreendimento empreendimento)  {
		return imovelRuralService.carregarImoveisByEmpreendimento(empreendimento);
	}
	
	public Boolean existeImoveisRuraisByEmpreendimento(Empreendimento empreendimento) throws Exception {
		return silosArmazensImovelRuralService.existeImoveisRuraisByEmpreendimento(empreendimento);
	}
	
	public List<SilosArmazensOrigemAgua> listarSilosArmazensOrigemAgua() throws Exception{
		return silosArmazensOrigemAguaService.listarSilosArmazensOrigemAgua();
	}
	
	public List<TipoConcessionaria> listarTipoConcessionaria() throws Exception{
		return tipoConcessionariaService.listarTipoConcessionaria();
	}
	
	public List<SistemaTratamentoAgua> listarSistemaTratamentoAgua() throws Exception{
		return sistemaTratamentoAguaService.listarSistemaTratamentoAgua();
	}
	
	public List<SilosArmazensCaracterizacaoAtmosferica> listarSilosArmazensCaracterizacaoAtmosferica() throws Exception{
		return silosArmazensCaracterizacaoAtmosfericaService.listarSilosArmazensCaracterizacaoAtmosferica();
	}
	
	public List<EquipamentoControle> listarEquipamentoControle(){
		return equipamentoControleService.listarEquipamentoControle();
	}
	
	public List<MedidaControleEmissao> listarMedidaControleEmissao() {
		return medidaControleEmissaoService.listarMedidaControleEmissao();
	}
	
	public List<ClassificacaoResiduo> listarClassificacaoResiduo() {
		return classificacaoResiduoService.listarClassificacaoResiduo();
	}
	
	public List<DestinacaoFinal> listarDestinacaoFinal() throws Exception{
		return destinacaoFinalService.listarDestinacaoFinal();
	}
	
	public List<SilosArmazensSistemaSeguranca> listarSilosArmazensSistemaSeguranca() throws Exception{
		return silosArmazensSistemaSegurancaService.listarSilosArmazensSistemaSeguranca();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAbaSeguranca(List<SistemaSegurancaSilosArmazen> sistemaSegurancaSilosArmazens) throws Exception{
		
		
		for (SistemaSegurancaSilosArmazen seguranca : sistemaSegurancaSilosArmazens) {

			if (!Util.isNullOuVazio(seguranca.getSilosArmazen())) {
				SilosArmazen ideSilosArmazens = seguranca.getSilosArmazen();
						
				excluirSistemaSeguraSilosArmazens(ideSilosArmazens);
				break;
			}

		}

		for (SistemaSegurancaSilosArmazen seguranca : sistemaSegurancaSilosArmazens) {

			salvaSistemaSegurancaSilosArmazen(seguranca);
		}
		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvaSistemaSegurancaSilosArmazen(SistemaSegurancaSilosArmazen sistemaSegurancaSilosArmazen) throws Exception{
		silosArmazensSistemaSegurancaService.salvaSistemaSegurancaSilosArmazen(sistemaSegurancaSilosArmazen);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void tramitarCadastroIncompleto(CadastroAtividadeNaoSujeitaLic cadastro) {
		cadastroFacade.tramitarCadastroIncompleto(cadastro);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void finalizarTramitacao(CadastroAtividadeNaoSujeitaLic cadastro) {

		cadastroAtividadeFacade.finalizarCadastro(cadastro);
	}
	
	//abcacaracterizacao
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAbaCaracterizacaoAmbiental(CaracterizacaoAmbientalSilosArmazen caracterizacaoAmbientalSilosArmazens, CaracterizacaoAmbientalOrigemAgua excluirConcessionaria) throws Exception{
		
		if(!Util.isNullOuVazio(caracterizacaoAmbientalSilosArmazens.getIdeCaracterizacaoAmbientalSilosArmazens())){
			excluirCaracterizacao(caracterizacaoAmbientalSilosArmazens, excluirConcessionaria);
		}
		
		salvarCaracterizacaoAmbientalSilosArmazen(caracterizacaoAmbientalSilosArmazens);
		
		if(!Util.isNullOuVazio(caracterizacaoAmbientalSilosArmazens.getCaracterizacaoAmbientalOrigemAguas())){
			salvarCaracterizacaoAmbientalOrigemAgua(caracterizacaoAmbientalSilosArmazens.getCaracterizacaoAmbientalOrigemAguas());
		}
		
		if(!Util.isNullOuVazio(caracterizacaoAmbientalSilosArmazens.getSilosArmazensSistemaTratamentoAguas())){
			
			salvarSilosArmazensSistemaTratamentoAgua(caracterizacaoAmbientalSilosArmazens.getSilosArmazensSistemaTratamentoAguas());
		}
		
		if(!Util.isNullOuVazio(caracterizacaoAmbientalSilosArmazens.getSilosArmazensLancamentoEfluentes())){
			salvarSilosArmazensLancamentoEfluente(caracterizacaoAmbientalSilosArmazens.getSilosArmazensLancamentoEfluentes());
		}
		
		salvarCaracterizacaoAmbientalCaracterizacaoAtmosferica(caracterizacaoAmbientalSilosArmazens.getCaracterizacaoAmbientalCaracterizacaoAtmosfericas());
		
		salvarCaracterizacaoAmbientalEquipamentoControle(caracterizacaoAmbientalSilosArmazens.getCaracterizacaoAmbientalEquipamentoControles());
		
		if(!Util.isNullOuVazio(caracterizacaoAmbientalSilosArmazens.getCaracterizacaoAmbientalMedidaControleEmissaos())){
			salvarCaracterizacaoAmbientalMedidaControleEmissao(caracterizacaoAmbientalSilosArmazens.getCaracterizacaoAmbientalMedidaControleEmissaos());
		}
		
		salvarClassificacaoResiduoCaracterizacaoAmbiental(caracterizacaoAmbientalSilosArmazens.getClassificacaoResiduoCaracterizacaoAmbientals());
		
		salvarCaracterizacaoAmbientalDestinacaoFinal(caracterizacaoAmbientalSilosArmazens.getCaracterizacaoAmbientalDestinacaoFinals());
	}
	
	public void excluirCaracterizacao(CaracterizacaoAmbientalSilosArmazen caracterizacaoAmbientalSilosArmazen, CaracterizacaoAmbientalOrigemAgua excluirConcessionaria) throws Exception{
		
			
			if(!Util.isNullOuVazio(caracterizacaoAmbientalSilosArmazen.getCaracterizacaoAmbientalOrigemAguas())){
				
				if(excluirConcessionaria.getIdeCaracterizacaoAmbientalOrigemAgua() != null){
					
					excluirCaracterizacaoOrigemAguaTipoConcessionaria(excluirConcessionaria);
				}
				

				excluirCaracterizacaoOrigemAgua(caracterizacaoAmbientalSilosArmazen);
				
			}
			
			excluirSilosArmazensTratamentoAgua(caracterizacaoAmbientalSilosArmazen);
		
			excluirSilosArmazensLancamentoEfluente(caracterizacaoAmbientalSilosArmazen);
			
			excluirCaracterizacaoAtmosferica(caracterizacaoAmbientalSilosArmazen);
			
			excluirCaracterizacaoEquipamentoControle(caracterizacaoAmbientalSilosArmazen);
			
			excluirCaracterizacaoMedidaControleEmissao(caracterizacaoAmbientalSilosArmazen);
			
			excluirCaracterizacaoClassificacaoResiduo(caracterizacaoAmbientalSilosArmazen);
			
			excluirCaracterizacaoDestinacaoFinal(caracterizacaoAmbientalSilosArmazen);
		
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCaracterizacaoAmbientalSilosArmazen(CaracterizacaoAmbientalSilosArmazen caracterizacaoAmbientalSilosArmazen) {
		caracterizacaoAmbientalSilosArmazenService.salvarCaracterizacaoAmbientalSilosArmazen(caracterizacaoAmbientalSilosArmazen);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCaracterizacaoAmbientalOrigemAgua(List<CaracterizacaoAmbientalOrigemAgua> origemAgua){
		
		for(CaracterizacaoAmbientalOrigemAgua caracterizacao: origemAgua){
			
			caracterizacao.setIdeCaracterizacaoAmbientalOrigemAgua(null);
			caracterizacaoAmbientalOrigemAguaService.salvarCaracterizacaoAmbientalOrigemAgua(caracterizacao);
			if(!Util.isNullOuVazio(caracterizacao.getOrigemAguaTipoConcessionarias())){
				salvarOrigemAguaTipoConcessionaria(caracterizacao.getOrigemAguaTipoConcessionarias());
			}
		}
	}	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOrigemAguaTipoConcessionaria(List<OrigemAguaTipoConcessionaria> origemAguaTipoConcessionaria ){
		
		for(OrigemAguaTipoConcessionaria origem:origemAguaTipoConcessionaria){
			
			origem.setIdeOrigemAguaTipoConcessionaria(null);
			origemAguaTipoConcessionariaService.salvarOrigemAguaTipoConcessionaria(origem);
		}
		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarSilosArmazensSistemaTratamentoAgua(List<SilosArmazensSistemaTratamentoAgua> silosArmazensSistemaTratamentoAgua) throws Exception{
		
		for(SilosArmazensSistemaTratamentoAgua sistema:silosArmazensSistemaTratamentoAgua){
			
			silosArmazensSistemaTratamentoAguaService.salvarSilosArmazensSistemaTratamentoAgua(sistema);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarSilosArmazensLancamentoEfluente(List<SilosArmazensLancamentoEfluente> silosArmazensLancamentoEfluente) throws Exception{
		
		
		
		for(SilosArmazensLancamentoEfluente efluente: silosArmazensLancamentoEfluente){
			efluente.setIdeSilosArmazensLancamentoEfluente(null);
			silosArmazensLancamentoEfluenteService.salvarSilosArmazensLancamentoEfluente(efluente);
		}
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarClassificacaoResiduoCaracterizacaoAmbiental(List<ClassificacaoResiduoCaracterizacaoAmbiental> classificacaoResiduoCaracterizacaoAmbiental){
		
		for(ClassificacaoResiduoCaracterizacaoAmbiental residuo: classificacaoResiduoCaracterizacaoAmbiental){
			residuo.setIdeClassificacaoResiduoCaracterizacaoAmbiental(null);
			classificacaoResiduoCaracterizacaoAmbientalService.salvarClassificacaoResiduoCaracterizacaoAmbiental(residuo);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCaracterizacaoAmbientalMedidaControleEmissao(List<CaracterizacaoAmbientalMedidaControleEmissao> caracterizacaoAmbientalMedidaControleEmissao){
		
		for(CaracterizacaoAmbientalMedidaControleEmissao medida:caracterizacaoAmbientalMedidaControleEmissao){
			medida.setIdeCaracterizacaoAmbientalMedidaControleEmissao(null);
			caracterizacaoAmbientalMedidaControleEmissaoService.salvarCaracterizacaoAmbientalMedidaControleEmissao(medida);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCaracterizacaoAmbientalEquipamentoControle(List<CaracterizacaoAmbientalEquipamentoControle> caracterizacaoAmbientalEquipamentoControle){
		
		for(CaracterizacaoAmbientalEquipamentoControle equipamentoControle:caracterizacaoAmbientalEquipamentoControle){
			equipamentoControle.setIdeCaracterizacaoAmbientalEquipamentoControle(null);
			caracterizacaoAmbientalEquipamentoControleService.salvarCaracterizacaoAmbientalEquipamentoControle(equipamentoControle);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCaracterizacaoAmbientalCaracterizacaoAtmosferica(List<CaracterizacaoAmbientalCaracterizacaoAtmosferica> caracterizacaoAmbientalCaracterizacaoAtmosferica){
		
		for(CaracterizacaoAmbientalCaracterizacaoAtmosferica atmosferica: caracterizacaoAmbientalCaracterizacaoAtmosferica){
			atmosferica.setIdeCaracterizacaoAmbientalCaracterizacaoAtmosferica(null);
			caracterizacaoAmbientalCaracterizacaoAtmosfericaService.salvarCaracterizacaoAmbientalCaracterizacaoAtmosferica(atmosferica);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCaracterizacaoAmbientalDestinacaoFinal(List<CaracterizacaoAmbientalDestinacaoFinal> caracterizacaoAmbientalDestinacaoFinal){
		
		for(CaracterizacaoAmbientalDestinacaoFinal destinacaoFinal:caracterizacaoAmbientalDestinacaoFinal){
			destinacaoFinal.setIdeCaracterizacaoAmbientalDestinacaoFinal(null);
			caracterizacaoAmbientalDestinacaoFinalService.salvarCaracterizacaoAmbientalDestinacaoFinal(destinacaoFinal);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private List<AtividadeNaoSujeitaLicenciamentoDocumento> listarDocumentos(CadastroAtividadeNaoSujeitaLic cadastroAtividade) {
		if(cadastroAtividade.getTipoAtividadeNaoSujeitaLicenciamento().equals(new TipoAtividadeNaoSujeitaLicenciamento(TipoAtividadeNaoSujeitaLicenciamentoEnum.SILOS_E_ARMAZENS))){
			return atividadeNaoSujeitaLicenciamentoDocumentoService.listarAtividadesByTipoAtividade(TipoAtividadeNaoSujeitaLicenciamentoEnum.SILOS_E_ARMAZENS);
		} 
		
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CadastroAtividadeNaoSujeitaLicDocApensado> listarDocumentosApensados(CadastroAtividadeNaoSujeitaLic cadastroAtividade) {
		if (!Util.isNullOuVazio(cadastroAtividade)) {
			cadastroAtividade.setCadastroAtividadeNaoSujeitaLicDocApensados(docApensadoService.listar(cadastroAtividade));
		}
		for (AtividadeNaoSujeitaLicenciamentoDocumento docs : listarDocumentos(cadastroAtividade)) {
			CadastroAtividadeNaoSujeitaLicDocApensado docApensado = new CadastroAtividadeNaoSujeitaLicDocApensado(cadastroAtividade, docs.getIdeDocumentoObrigatorio());
				cadastroAtividade.addCadastroAtividadeNaoSujeitaLicDocApensado(docApensado);
		}
		return cadastroAtividade.getCadastroAtividadeNaoSujeitaLicDocApensados();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirDocumentoApensadoAnteriormente(Object caminhoArquivoAntigo) {
		if (!Util.isNullOuVazio(caminhoArquivoAntigo)) {
			cadastroFacade.excluirDocumentosApensados(caminhoArquivoAntigo);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAbaDocumentosEstudos(CadastroAtividadeNaoSujeitaLic cadastroAtividadeNaoSujeitaLic ){
		for (CadastroAtividadeNaoSujeitaLicDocApensado doc : cadastroAtividadeNaoSujeitaLic.getCadastroAtividadeNaoSujeitaLicDocApensados()) {
			if(Util.isNullOuVazio(doc.getExibir()) || doc.getExibir()){
				
				cadastroFacade.salvarDocumentoApensado(doc);
			}
		}
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAceiteFinal(SilosArmazensController silosArmazensController) throws Exception{
		
		silosArmazensController.salvarTodasAbas();
		finalizarTramitacao(silosArmazensController.getCadastro());
		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void tramitarCadastroFinal(CadastroAtividadeNaoSujeitaLic cadastro){
		
		tramitarCadastroConcluido(cadastro, CadastroAtividadeNaoSujeitaLicTipoStatusEnum.CONCLUIDO);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void tramitarCadastroConcluido(CadastroAtividadeNaoSujeitaLic cadastro, CadastroAtividadeNaoSujeitaLicTipoStatusEnum tipoStatusEnum){
		try {
			CadastroAtividadeNaoSujeitaLicStatus cadastroStatus = new CadastroAtividadeNaoSujeitaLicStatus(cadastro, tipoStatusEnum, ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica());
			if(!Util.isNullOuVazio(cadastro.getCadastroAtividadeNaoSujeitaLicStatus())){
				if (!cadastroStatus.getCadastroAtividadeNaoSujeitaLicTipoStatus().equals(cadastro.getCadastroUltimoStatus().getCadastroAtividadeNaoSujeitaLicTipoStatus())){
					cadastro.addCadastroAtividadeNaoSujeitaLicStatus(cadastroStatus);
					cadastroStatusService.salvar(cadastroStatus);
				}
			} 
			else {
				cadastro.addCadastroAtividadeNaoSujeitaLicStatus(cadastroStatus);
				cadastroStatusService.salvar(cadastroStatus);
			}
			
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " o novo status do Cadastro.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);

		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void tramitarCadastroCompleto(CadastroAtividadeNaoSujeitaLic cadastro) {
		try {
			if(Util.isNullOuVazio(cadastro.getNumCadastro())){
				cadastro.setNumCadastro(cadastroAtividadeFacade.gerarNumeroCadastro(cadastroAtividadeFacade.getSiglaCadastro(cadastro)));
			}
			tramitar(cadastro, CadastroAtividadeNaoSujeitaLicTipoStatusEnum.CONCLUIDO);
			
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " o número do Cadastro.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void tramitar(CadastroAtividadeNaoSujeitaLic cadastro, CadastroAtividadeNaoSujeitaLicTipoStatusEnum tipoStatusEnum) {
		try {
			CadastroAtividadeNaoSujeitaLicStatus cadastroStatus = new CadastroAtividadeNaoSujeitaLicStatus(cadastro, tipoStatusEnum, ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica());
			if(Util.isNullOuVazio(cadastro.getCadastroAtividadeNaoSujeitaLicStatus())){
				if (!cadastroStatus.getCadastroAtividadeNaoSujeitaLicTipoStatus().equals(cadastro.getCadastroUltimoStatus().getCadastroAtividadeNaoSujeitaLicTipoStatus())){
					cadastro.addCadastroAtividadeNaoSujeitaLicStatus(cadastroStatus);
					cadastroStatusService.salvar(cadastroStatus);
				}
			} 
			else {
				cadastro.addCadastroAtividadeNaoSujeitaLicStatus(cadastroStatus);
				cadastroStatusService.salvar(cadastroStatus);
			}
			
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " o novo status do Cadastro.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);

		}
	}
	
	public SilosArmazen carregarSilosArmazens(CadastroAtividadeNaoSujeitaLic cadastro) throws Exception{
		
		return silosArmazensService.buscarSilosArmazens(cadastro);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ResponsavelEmpreendimento> listarResponsaveisTecnicos(Empreendimento empreendimento) throws Exception{
		Collection<ResponsavelEmpreendimento> lista = responsavelEmpreendimentoService.listarResponsaveisPorEmpreendimento(empreendimento);
		for(ResponsavelEmpreendimento responsavelEmpreendimento : lista){
			responsavelEmpreendimento.getIdePessoaFisica().getPessoa().setTelefoneCollection(buscarTelefonesPorPessoa(responsavelEmpreendimento.getIdePessoaFisica().getPessoa()));
		}
		return lista;	
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Empreendimento carregarById(Integer id) {
		return empreendimentoService.buscarEmpreendimentoPorIde(id);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Endereco obterEnderecoEmpreendimento(Integer ideEmpreendimento) {
		return enderecoEmpreendimentoService.filtrarEnderecoByEnderecoEmpreendimento(enderecoEmpreendimentoService.obterEnderecoEmpreendimento(ideEmpreendimento));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaJuridica obterPessoaJuridicaMontada(Integer idePessoaJuridica){
		try {
			PessoaJuridica pessoaJuridica = carregarPessoaJuridicaByIde(idePessoaJuridica);
			return pessoaJuridica;
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações do Requerente.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaFisica obterPessoaFisicaMontada(Integer idePessoaFisica){
		try {
			PessoaFisica pessoaFisica = carregarPessoaFisicaByIde(idePessoaFisica);
			return pessoaFisica;
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações do Requerente.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private PessoaJuridica carregarPessoaJuridicaByIde(Integer idePessoaJuridica) throws Exception{
		return cadastroFacade.carregarPessoaJuridicaByIde(idePessoaJuridica);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private PessoaFisica carregarPessoaFisicaByIde(Integer idePessoaFisica) throws Exception{
		return cadastroFacade.carregarPessoaFisicaByIde(idePessoaFisica);
	}	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private Endereco carregarEnderecoBy(Pessoa pessoa){
		EnderecoPessoa ep = enderecoPessoaService.buscarEnderecoPorPessoa(pessoa);
		return ep.getIdeEndereco();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private List<Telefone> listarTelefoneBy(Pessoa pessoa) throws Exception {
		return telefoneService.buscarTelefonesPorPessoa(pessoa);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirSistemaSeguraSilosArmazens(SilosArmazen ideSilosArmazens) throws Exception{
		silosArmazensSistemaSegurancaService.excluirSistemaSeguraSilosArmazens(ideSilosArmazens);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirCaracterizacaoOrigemAgua(CaracterizacaoAmbientalSilosArmazen caracterizacaoAmbientalSilosArmazen){
		caracterizacaoAmbientalOrigemAguaService.excluirCaracterizacaoOrigemAgua(caracterizacaoAmbientalSilosArmazen);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirCaracterizacaoOrigemAguaTipoConcessionaria(CaracterizacaoAmbientalOrigemAgua caracterizacaoAmbientalOrigemAgua){
		origemAguaTipoConcessionariaService.excluirCaracterizacaoOrigemAguaTipoConcessionaria(caracterizacaoAmbientalOrigemAgua);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirSilosArmazensTratamentoAgua(CaracterizacaoAmbientalSilosArmazen caracterizacaoAmbientalSilosArmazen) throws Exception{
		silosArmazensSistemaTratamentoAguaService.excluirSilosArmazensTratamentoAgua(caracterizacaoAmbientalSilosArmazen);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirSilosArmazensLancamentoEfluente(CaracterizacaoAmbientalSilosArmazen caracterizacaoAmbientalSilosArmazen) throws Exception{
		silosArmazensLancamentoEfluenteService.excluirSilosArmazensLancamentoEfluente(caracterizacaoAmbientalSilosArmazen);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirCaracterizacaoAtmosferica(CaracterizacaoAmbientalSilosArmazen caracterizacaoAmbientalSilosArmazen) {
		caracterizacaoAmbientalCaracterizacaoAtmosfericaService.excluirCaracterizacaoAtmosferica(caracterizacaoAmbientalSilosArmazen);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirCaracterizacaoEquipamentoControle(CaracterizacaoAmbientalSilosArmazen caracterizacaoAmbientalSilosArmazen) {
		caracterizacaoAmbientalEquipamentoControleService.excluirCaracterizacaoEquipamentoControle(caracterizacaoAmbientalSilosArmazen);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirCaracterizacaoMedidaControleEmissao(CaracterizacaoAmbientalSilosArmazen caracterizacaoAmbientalSilosArmazen){
		caracterizacaoAmbientalMedidaControleEmissaoService.excluirCaracterizacaoMedidaControleEmissao(caracterizacaoAmbientalSilosArmazen);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirCaracterizacaoClassificacaoResiduo(CaracterizacaoAmbientalSilosArmazen caracterizacaoAmbientalSilosArmazen){
		classificacaoResiduoCaracterizacaoAmbientalService.excluirCaracterizacaoClassificacaoResiduo(caracterizacaoAmbientalSilosArmazen);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirCaracterizacaoDestinacaoFinal(CaracterizacaoAmbientalSilosArmazen caracterizacaoAmbientalSilosArmazen){
		caracterizacaoAmbientalDestinacaoFinalService.excluirCaracterizacaoDestinacaoFinal(caracterizacaoAmbientalSilosArmazen);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirResponsavelTecnico(SilosArmazen ideSilosArmazens) throws Exception{
		silosArmazensResponsavelTecnicoService.excluirResponsavelTecnico(ideSilosArmazens);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirImovelRural(SilosArmazen ideSilosArmazens) throws Exception{
		silosArmazensImovelRuralService.excluirImovelRural(ideSilosArmazens);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirOperacaoDesenvolvida(SilosArmazen ideSilosArmazens) throws Exception{
		silosArmazensOperacaoDesenvolvidaService.excluirOperacaoDesenvolvida(ideSilosArmazens);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirTipoCombustivel(SilosArmazen ideSilosArmazens) throws Exception{
		silosArmazensTipoCombustivelService.excluirTipoCombustivel(ideSilosArmazens);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirUnidadeArmazenadora(SilosArmazen ideSilosArmazens) throws Exception{
		silosArmazensUnidadeArmazenadoraService.excluirUnidadeArmazenadora(ideSilosArmazens);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CadastroAtividadeNaoSujeitaLicStatus> listar(CadastroAtividadeNaoSujeitaLic cadastro){
		return cadastroAtividadeNaoSujeitaLicStatusService.listar(cadastro);
	}
	
	public Boolean existemImovelRuralImpreendimento(Empreendimento emp){
		DetachedCriteria criteria = DetachedCriteria.forClass(ImovelEmpreendimento.class, "iE");
		
		criteria.createAlias("iE.ideImovel", "i", JoinType.INNER_JOIN)
		.createAlias("i.ideTipoImovel", "tI", JoinType.INNER_JOIN);
		
		criteria.add(Restrictions.eq("iE.ideEmpreendimento.ideEmpreendimento", emp.getIdeEmpreendimento()))
		.add(Restrictions.or(Restrictions.eq("tI.ideTipoImovel", TipoImovelEnum.RURAL.getId()), Restrictions.eq("tI.ideTipoImovel", TipoImovelEnum.CESSIONARIO.getId())));
		
		criteria.setProjection(Projections.projectionList().add(Projections.property("iE.ideImovel"), "ideImovel"));
		
//		imovelEmpreendimendoDao.count(criteria)
		return imovelEmpreendimendoDao.count(criteria) > 0 ? true : false ;
	}
	
	
	public List<Imovel> listarImoveisRurais(Empreendimento emp){
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Imovel.class);
		criteria.createAlias("imovelRural", "imovelR",JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("imovelUrbano", "imovelU",JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("empreendimentoCollection", "emprendimento");
		criteria.add(Restrictions.eq("emprendimento.ideEmpreendimento", emp.getIdeEmpreendimento()));
		criteria.add(Restrictions.eq("indExcluido", Boolean.FALSE));
		
		return idaoImovel.listarPorCriteria(criteria);
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CadastroAtividadeNaoSujeitaLic buscarCadastroIncompletoPorPessoa(Pessoa pessoaRequerente) throws Exception{
		return cadastroFacade.buscarCadastroIncompletoPorPessoa(pessoaRequerente);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CadastroAtividadeNaoSujeitaLic buscarCadastroIncompletoPorPessoaEmpreendimento(Pessoa pessoaRequerente, Empreendimento empreendimento, TipoAtividadeNaoSujeitaLicenciamentoEnum tipoAtividadeNaoSujeitaLicenciamentoEnum) throws Exception{
		return cadastroFacade.buscarCadastroIncompletoPorPessoaEmpreendimento(pessoaRequerente, empreendimento, tipoAtividadeNaoSujeitaLicenciamentoEnum);
	}
}
