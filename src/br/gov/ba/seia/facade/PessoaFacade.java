package br.gov.ba.seia.facade;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLic;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.service.CnaeService;
import br.gov.ba.seia.service.DocumentoIdentificacaoService;
import br.gov.ba.seia.service.ParticipacaoAcionariaService;
import br.gov.ba.seia.service.PessoaFisicaService;
import br.gov.ba.seia.service.PessoaJuridicaService;
import br.gov.ba.seia.service.RepresentanteLegalService;
import br.gov.ba.seia.service.TelefoneService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PessoaFacade {
	
	@EJB
	private EnderecoFacade enderecoService;
	
	@EJB
	private TelefoneService telefoneService;
	
	@EJB
	private DocumentoIdentificacaoService documentoIdentificacaoService;
	
	@EJB
	private CnaeService cnaeService;
	
	@EJB
	private RepresentanteLegalService representanteLegalService;
	
	@EJB
	private ParticipacaoAcionariaService participacaoAcionariaService;
	
	@EJB
	private PessoaFisicaService pessoaFisicaService;
	
	@EJB
	private PessoaJuridicaService pessoaJuridicaService;
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isPessoaCadastroCompleto(Pessoa pessoa) {
		
		boolean isPessoaFisicaValida = false;
		boolean isPessoaJuridicaValida = false;
		
		if(pessoa == null || (pessoa.getPessoaJuridica() == null && pessoa.getPessoaFisica()==null)){
			return false;
		}
		
		if (isPessoaFisicaValida){
			if(!isPessoaFisicaValida && pessoa.getPessoaJuridica() == null){
				MensagemUtil.erro(Util.getString("msg_generica_dados_pf_incompleto"));
				return false;
			}

			else  if(isPessoaJuridicaValida && pessoa.getPessoaFisica() == null){
				MensagemUtil.erro(Util.getString("msg_generica_dados_pj_incompleto"));
				return false;
			}
		}

		return true;
	}
	
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Boolean verificarCadastroCompletoPessoaFisica(Pessoa pessoa) throws Exception {
		if (pessoaFisicaService.validarDadosBasicosPessoaFisica(pessoa.getPessoaFisica()) && verificaEndereco(pessoa) && verificarDocumento(pessoa) && verificarTelefone(pessoa)) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Boolean verificarCadastroCompletoPessoaJuridica(Pessoa pessoa) throws Exception {
		if (pessoaJuridicaService.validarIdentificacaoPessoaJuridica(pessoa.getPessoaJuridica())
			&& verificarCnae(pessoa.getPessoaJuridica())
			&& verificarParticipacaoAcionaria(pessoa.getPessoaJuridica()) 
			&& verificarRepresentanteLegal(pessoa.getPessoaJuridica())
			&& verificarTelefone(pessoa) 
			&& verificaEndereco(pessoa)) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Boolean verificarCadastroCompletoPessoaJuridicaSemCnae(Pessoa pessoa) throws Exception {
		if (pessoaJuridicaService.validarIdentificacaoPessoaJuridica(pessoa.getPessoaJuridica())
			&& verificarParticipacaoAcionaria(pessoa.getPessoaJuridica()) 
			&& verificarRepresentanteLegal(pessoa.getPessoaJuridica())
			&& verificarTelefone(pessoa) 
			&& verificaEndereco(pessoa)) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Boolean verificarCadastroParcialPessoa(Pessoa pessoa) throws Exception {
		return verificarTelefone(pessoa) && verificaEndereco(pessoa);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Boolean verificarTelefone(Pessoa pessoa) throws Exception {
		if (Util.isNullOuVazio(telefoneService.buscarTelefonesPorPessoa(pessoa))) {
			JsfUtil.addErrorMessage("Os dados do cadastro de telefone estão incompletos, favor preencher todas as informações.");
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Boolean verificarDocumento(Pessoa pessoa) throws Exception {
		if (Util.isNullOuVazio(documentoIdentificacaoService.listarDocumentosIdentificacaoPorPessoa(pessoa))) {
			JsfUtil.addErrorMessage("Os dados do cadastro de documento estão incompletos, favor preencher todas as informações.");
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Boolean verificaEndereco(Pessoa pessoa) throws Exception {
		if (Util.isNullOuVazio(enderecoService.filtrarEnderecoByPessoa(pessoa))) {
			JsfUtil.addErrorMessage("Os dados do cadastro de endereço estão incompletos, favor preencher todas as informações.");
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Boolean verificarCnae(PessoaJuridica pessoaJuridica)  {
		if (Util.isNullOuVazio(cnaeService.listarCnaePorPessoaJuridica(pessoaJuridica))) {
			JsfUtil.addErrorMessage("Os dados do cadastro de CNAE estão incompletos, favor preencher todas as informações.");
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Boolean verificarRepresentanteLegal(PessoaJuridica pessoaJuridica)  {
		if (Util.isNullOuVazio(representanteLegalService.listarRepresentanteLegalPorPessoaJuridica(pessoaJuridica))) {
			JsfUtil.addErrorMessage("Os dados do cadastro de Representante Legal estão incompletos, favor preencher todas as informações.");
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Boolean verificarParticipacaoAcionaria(PessoaJuridica pessoaJuridica) {
		if(!participacaoAcionariaService.validateParticipacaoAcionariaRequerimentoUnico(pessoaJuridica)) {
			JsfUtil.addErrorMessage("Os dados do cadastro da composição acionária estão incompletos, favor preencher todas as informações.");
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<PessoaFisica> listarPessoaFisica(PessoaFisica pessoaFisicaSelecionada, int first, int pageSize) {
		return pessoaFisicaService.listarPorCriteriaDemanda(pessoaFisicaSelecionada, first, pageSize);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public int getRowCountPessoaFisica(PessoaFisica pessoaFisicaSelecionada) {
		return pessoaFisicaService.count(pessoaFisicaSelecionada);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<PessoaJuridica> listarPessoaJuridica(PessoaJuridica pessoaJuridicaSelecionada, int first, int pageSize) {
		return pessoaJuridicaService.listarPorCriteriaDemanda(pessoaJuridicaSelecionada, first, pageSize);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public int getRowCountPessoaJuridica(PessoaJuridica pessoaJuridicaSelecionada){
		return pessoaJuridicaService.count(pessoaJuridicaSelecionada);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaJuridica buscarPessoaJuridicaByCNPJ(PessoaJuridica pessoaJuridica) {
		return pessoaJuridicaService.filtrarPessoaFisicaByCnpj(pessoaJuridica); 
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaJuridica buscarPessoaJuridica(Integer idePessoa) {
		return pessoaJuridicaService.obterPessoaJuridicaId(new PessoaJuridica(idePessoa));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PessoaFisica buscarPessoaFisica(Integer idePessoa) {
		return pessoaFisicaService.buscarPessoaFisicaByIde(idePessoa);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Pessoa> listarPessoasResponsaveisBy(CadastroAtividadeNaoSujeitaLic cadastro) {
		return pessoaFisicaService.listarResponsaveisBy(cadastro);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Boolean verificarCadastroCompletoPessoaFisicaRepresentanteComunidade(Pessoa pessoa) throws Exception {
		if (pessoaFisicaService.validarDadosBasicosPessoaFisica(pessoa.getPessoaFisica())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
}