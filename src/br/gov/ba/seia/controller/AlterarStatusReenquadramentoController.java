package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.dto.ProcessoReenquadramentoDTO;
import br.gov.ba.seia.entity.StatusReenquadramento;
import br.gov.ba.seia.enumerator.PaginaEnum;
import br.gov.ba.seia.enumerator.StatusReenquadramentoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.StatusReenquadramentoService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;
/***
 * Classe criada para controlar a view de alterar status do reenquadramento
 * @author diegoraian
 */
@Named("alterarStatusReenquadramentoController")
@ViewScoped
public class AlterarStatusReenquadramentoController {

	
	private ProcessoReenquadramentoDTO processoReenquadramento;
	private String numProcesso;
	private String numCpfCnpj;
	private String nomePessoaEmpresa;
	private Long statusSelecionado;
	
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("/Bundle");
	
	@EJB
	private StatusReenquadramentoService statusReenquadramentoService;
	
	/**
	 * Método responsável por carregar os dados que irão alterar
	 * @param processoReenquadramento
	 */
	public void carregarAlterarStatus(ProcessoReenquadramentoDTO processoReenquadramento) {
		limparStatus();
		if(!Util.isNull(processoReenquadramento)) {
			this.processoReenquadramento = processoReenquadramento;
			this.numProcesso = processoReenquadramento.getProcessoReenquadramento().getIdeProcesso().getNumProcesso();
			
			
			if (!Util.isNull(processoReenquadramento.getRequerente().getPessoaFisica())) {
				this.nomePessoaEmpresa = processoReenquadramento.getRequerente().getPessoaFisica().getNomPessoa();
				this.numCpfCnpj =Util.format("###.###.###-##",processoReenquadramento.getRequerente().getPessoaFisica().getNumCpf());
			} else if(!Util.isNull(processoReenquadramento.getRequerente().getPessoaJuridica())) {
				this.nomePessoaEmpresa = processoReenquadramento.getRequerente().getPessoaJuridica().getNomRazaoSocial();
				this.numCpfCnpj = Util.format("##.###.###/####-##",processoReenquadramento.getRequerente().getPessoaJuridica().getNumCnpj());	
			}
			getListaStatusReenquadramento();
			
		}	
	}
	/**
	 * Limpa os campos da tela
	 * @author diegoraian
	 */
	public void limparStatus() {
		this.numProcesso = null;
		this.numCpfCnpj = null;
		this.nomePessoaEmpresa = null;
		this.statusSelecionado = null;
	}
	
	
	/**
	 * Função principal do controller, responsável por realizar a alteração do status 
	 * @author diegoraian
	 */
	public void  alterarStatus() {
		try {

			statusReenquadramentoService.alterarStatus(processoReenquadramento, StatusReenquadramentoEnum.getStatusById(statusSelecionado));
			if (!Util.isNullOuVazio(processoReenquadramento)){
				SessaoUtil.adicionarObjetoSessao("numProcesso", processoReenquadramento.getProcessoReenquadramento().getIdeProcesso().getNumProcesso());
			}
			FacesContext.getCurrentInstance().getExternalContext().redirect(PaginaEnum.PAUTA_REENQUADRAMENTO.getUrl());
			
			MensagemUtil.sucesso("Reenquadramento alterado com sucesso");
		} catch (Exception e) {
			Log4jUtil.log(AceiteReenquadramentoController.class.getSimpleName(), Level.ERROR, e);
			MensagemUtil.erro("Não foi possível alterar o status para esse reenquadramento!");
		}
	}
	/**
	 * Valida se o cambo foi selecionado
	 * @author diegoraian
	 */
	public void validarStatus() {
		if(Util.isNullOuVazio(statusSelecionado)) {
			MensagemUtil.alerta("O campo \"Voltar status para\" é obrigatório");
		}else {
			RequestContext.getCurrentInstance().execute("dialogConfirmarAlteracaoStatusReenquadramento.show()");
		}
	}
	
	

	/**
	 * Retorna a lista dos status possíveis para uma determinada lista
	 * @return
	 */
	public Collection<StatusReenquadramento> getListaStatusReenquadramento(){
		try {
			List<Long> listaStatus = new ArrayList<Long>();
			
			Long status = processoReenquadramento.getStatusAtual().getId();
			if ((status.equals( StatusReenquadramentoEnum.VALIDADO.getId())) 
					|| (status.equals(StatusReenquadramentoEnum.EM_VALIDACAO_PREVIA.getId())) 
					|| (status.equals(StatusReenquadramentoEnum.AGUARDANDO_ENVIO_DOCUMENTACAO.getId()))) {
				
					listaStatus.add(StatusReenquadramentoEnum.AGUARDANDO_EDICAO_REENQUADRAMENTO.getId());
					
			} else if ((status.equals(StatusReenquadramentoEnum.BOLETO_EM_PROCESSAMENTO.getId())) 
					|| (status.equals(StatusReenquadramentoEnum.BOLETO_PAGAMENTO_LIBERADO.getId()))) {
				
				listaStatus.add(StatusReenquadramentoEnum.AGUARDANDO_EDICAO_REENQUADRAMENTO.getId());
				listaStatus.add(StatusReenquadramentoEnum.VALIDADO.getId());
				
			}
			
			return statusReenquadramentoService.listarStatusRequerimentoQueRealizamAlteracaoDeStatus(listaStatus);
		} catch (Exception e) {
			JsfUtil.addWarnMessage(BUNDLE.getString("MSG-007"));
			Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		return null;
		
	}
	public ProcessoReenquadramentoDTO getProcessoReenquadramento() {
		return processoReenquadramento;
	}

	public void setProcessoReenquadramento(ProcessoReenquadramentoDTO processoReenquadramento) {
		this.processoReenquadramento = processoReenquadramento;
	}

	public String getNumProcesso() {
		return numProcesso;
	}

	public void setNumProcesso(String numProcesso) {
		this.numProcesso = numProcesso;
	}

	public String getNumCpfCnpj() {
		return numCpfCnpj;
	}

	public void setNumCpfCnpj(String numCpfCnpj) {
		this.numCpfCnpj = numCpfCnpj;
	}

	public String getNomePessoaEmpresa() {
		return nomePessoaEmpresa;
	}

	public void setNomePessoaEmpresa(String nomePessoaEmpresa) {
		this.nomePessoaEmpresa = nomePessoaEmpresa;
	}

	public Long getStatusSelecionado() {
		return statusSelecionado;
	}

	public void setStatusSelecionado(Long statusSelecionado) {
		this.statusSelecionado = statusSelecionado;
	}
	
}
