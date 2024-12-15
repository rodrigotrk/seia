package br.gov.ba.seia.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;

import br.gov.ba.seia.entity.DocumentoImovelRural;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.ImovelRuralMudancaStatusJustificativa;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.ProcuradorRepresentante;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.TipoDocumentoEnum;
import br.gov.ba.seia.facade.ImovelRuralFacade;
import br.gov.ba.seia.facade.auditoria.AuditoriaFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.ProcuradorRepresentanteService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

@Named("imovelRuralMudancaStatusJustificativaController")
@ViewScoped
public class ImovelRuralMudancaStatusJustificativaController extends SeiaControllerAb {
	
	private ImovelRuralMudancaStatusJustificativa IRmudancaStatusJustificativa;
	private List<DocumentoImovelRural> listDocumentoSolicitacaoAlteracao;
	private Boolean telaCpf;
	private String nmModal = "dlgAlterarStatusImovel";
	
	@EJB
	private ImovelRuralFacade imovelRuralServiceFacade;
	@EJB
	private AuditoriaFacade auditoriaFacade;
	@EJB
	private ProcuradorRepresentanteService procuradorRepresentanteService;
	
	@PostConstruct
	public void init() {
	}
	
	public void limparCampos() {
		this.IRmudancaStatusJustificativa = new ImovelRuralMudancaStatusJustificativa();
		this.listDocumentoSolicitacaoAlteracao = new ArrayList<DocumentoImovelRural>();
		this.setTelaCpf(Boolean.TRUE);
	}
	
	public void limparCamposTransferencia() {
		limparCampos();
		limparCamposRequerente();
	}
	
	public void validarCondicoesImovel() {
		/*
		 	i.	Se o imóvel estiver cedendo área para compensar reserva legal de outro imóvel do mesmo proprietário. 
			ii.	Se o imóvel  possuir vínculo com Empreendimento.<<<Retirado>>> #7715
			iii.Se a Reserva Legal estiver com status "Aprovada" ou "Averbada".<<<Retirado>>>
		 */
		//a pedido da astec responsabilidade Aline
		Integer[] idesXingu = {
				15641,
				8178,
				7019,
				8317,
				8320,
				8327,
				8154,
				8165,
				8166,
				7040,
				8168,
				7655,
				9961,
				12872	
		};
		
		try {
			if(!Arrays.asList(idesXingu).contains(getImovelRural().getIdeImovelRural())) { 
				this.imovelRuralServiceFacade.validarSeCedeAreaParaCompensarReservaLegal(getImovelRural().getIdeImovelRural());
//				this.imovelRuralServiceFacade.validarPossuiVinculoComEmpreendimento(getImovelRural());
//				this.imovelRuralServiceFacade.validarSeReservaAprovadaOuAverbada(getImovelRural());
			}
			this.IRmudancaStatusJustificativa.setIdeImovelRural(imovelRuralServiceFacade.carregarTudo(getImovelRural().getIdeImovelRural()));
			RequestContext.getCurrentInstance().addCallbackParam("valido", true);  
		} catch (Exception e) {
			RequestContext.getCurrentInstance().addCallbackParam("valido", false);
			JsfUtil.addSuccessMessage(e.getMessage());	
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	public ImovelRuralMudancaStatusJustificativa getIRmudancaStatusJustificativa() {
		return IRmudancaStatusJustificativa;
	}

	public void setIRmudancaStatusJustificativa(ImovelRuralMudancaStatusJustificativa iRmudancaStatusJustificativa) {
		IRmudancaStatusJustificativa = iRmudancaStatusJustificativa;
	}

	public List<DocumentoImovelRural> getListDocumentoSolicitacaoAlteracao() {
		return listDocumentoSolicitacaoAlteracao;
	}
	
	public void uploadDocumentoSolicitacaoTransferenciaTitularidade(FileUploadEvent event) {
		String nmArquivo = getImovelRural().getIdeImovelRural().toString()+"_"+"SOLICITACAO_TRANSFERENCIA_TITULARIDADE";
		String caminhoArquivo = FileUploadUtil.EnviarShape(event, DiretorioArquivoEnum.IMOVELRURAL.toString()+
				FileUploadUtil.getFileSeparator()+getImovelRural().getIdeImovelRural().toString()+FileUploadUtil.getFileSeparator(), nmArquivo);
		
		setIdeDocumentoResponsavel(new DocumentoImovelRural(null, nmArquivo, caminhoArquivo));
		File file = new File(caminhoArquivo.trim());
		getIdeDocumentoResponsavel().setFileSize(file.length());
		
		
		if(Util.isNullOuVazio(listDocumentoSolicitacaoAlteracao)) {
			listDocumentoSolicitacaoAlteracao = new ArrayList<DocumentoImovelRural>();
			listDocumentoSolicitacaoAlteracao.add(getIdeDocumentoResponsavel());
		}else {
			listDocumentoSolicitacaoAlteracao.clear();
			listDocumentoSolicitacaoAlteracao.add(getIdeDocumentoResponsavel());
		}
		
		JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");		
	}
	
	public void uploadDocumentoSolicitacaoAlteracao(FileUploadEvent event) {
		String nmArquivo = getImovelRural().getIdeImovelRural().toString()+"_"+"SOLICITACAO_ALTERACAO_STATUS";
		String caminhoArquivo = FileUploadUtil.EnviarShape(event, DiretorioArquivoEnum.IMOVELRURAL.toString()+
				FileUploadUtil.getFileSeparator()+getImovelRural().getIdeImovelRural().toString()+FileUploadUtil.getFileSeparator(), nmArquivo);
		
		setIdeDocumentoResponsavel(new DocumentoImovelRural(null, nmArquivo, caminhoArquivo));
		File file = new File(caminhoArquivo.trim());
		getIdeDocumentoResponsavel().setFileSize(file.length());
		
		
		if(Util.isNullOuVazio(listDocumentoSolicitacaoAlteracao)) {
			listDocumentoSolicitacaoAlteracao = new ArrayList<DocumentoImovelRural>();
			listDocumentoSolicitacaoAlteracao.add(getIdeDocumentoResponsavel());
		}else {
			listDocumentoSolicitacaoAlteracao.clear();
			listDocumentoSolicitacaoAlteracao.add(getIdeDocumentoResponsavel());
		}
		
		JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");		
	}
	
	private void setIdeDocumentoResponsavel(DocumentoImovelRural documentoImovelRural) {
		IRmudancaStatusJustificativa.setIdeDocumentoSolicitacao(documentoImovelRural);
	}

	private DocumentoImovelRural getIdeDocumentoResponsavel() {
		return IRmudancaStatusJustificativa.getIdeDocumentoSolicitacao();
	}
	
	private ImovelRural getImovelRural() {
		return IRmudancaStatusJustificativa.getIdeImovelRural();
	}

	public void setImovelRural(ImovelRural ideImovelRural) {
		IRmudancaStatusJustificativa.setIdeImovelRural(ideImovelRural);
	}
	
	public Boolean getTelaCpf() {
		return telaCpf;
	}

	public void setTelaCpf(Boolean telaCpf) {
		this.telaCpf = telaCpf;
	}

	public boolean isUsuarioExterno() {
		return ContextoUtil.getContexto().getUsuarioLogado().isUsuarioExterno();
	}
	
	public void salvarAlteracaoTitularidade() {
		try {
			IRmudancaStatusJustificativa.setIndAlterarProprietario(true);
			IRmudancaStatusJustificativa.setObservacao("Transferência de Titularidade de Imovel Rural");
			nmModal = "dlgAlterarStatusImovelRequerente";
			
			salvar();
			imovelRuralServiceFacade.enviarEmailTransferenciaTitularidade(getEmailProprietario(this.IRmudancaStatusJustificativa.getIdeProprietario()),IRmudancaStatusJustificativa.getIdeImovelRural().getDesDenominacao());
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			
		}
	}
	
	private List<String> getEmailProprietario(Pessoa pessoa) {
		List<String> emailL = new ArrayList();
		if (!Util.isNullOuVazio(pessoa.getPessoaFisica())) {
			emailL.add(pessoa.getPessoaFisica().getPessoa().getDesEmail());
		} else {
			List<ProcuradorRepresentante> prL = procuradorRepresentanteService
					.buscarProcuradorRepresentanteByPessoaJuridica(pessoa.getPessoaJuridica().getIdePessoaJuridica());
			for (ProcuradorRepresentante pr : prL) {
				emailL.add(pr.getIdeProcurador().getPessoa().getDesEmail());
			}
		}
		return emailL;
	}
	public void salvar() throws Exception {
		try {
			if(Util.isNullOuVazio(this.getListDocumentoSolicitacaoAlteracao())) {
				throw new Exception("Campo Documento de Solicitação de Alteração obrigatório!");
			}
			
			if(this.IRmudancaStatusJustificativa.isIndAlterarProprietario() && (Util.isNullOuVazio(this.IRmudancaStatusJustificativa.getIdeProprietario()) || Util.isNullOuVazio(this.IRmudancaStatusJustificativa.getIdeProprietario().getIdePessoa()))){
				throw new Exception("Favor informar o CPF ou CNPJ e pesquisar o novo requerente.");
			}
			
			ImovelRural objAntigo = getImovelRural().clone();

			// Alteração campos Histórico
			//ImovelRural objAntigo = new ImovelRural();
			//objAntigo.setIdeImovelRural(getImovelRural().getIdeImovelRural());
			//objAntigo.setStatusCadastro(getImovelRural().getStatusCadastro());
			//objAntigo.setIdeRequerenteCadastro(getImovelRural().getIdeRequerenteCadastro());
			
			this.IRmudancaStatusJustificativa.setDtcJustificativa(new Date());
			this.IRmudancaStatusJustificativa.setIdeUsuario(ContextoUtil.getContexto().getUsuarioLogado());
			this.imovelRuralServiceFacade.salvarJustificativaMudancaStatus(IRmudancaStatusJustificativa);
			
			ImovelRural objNovo = new ImovelRural();
			objNovo.setIdeImovelRural(getImovelRural().getIdeImovelRural());
			objNovo.setStatusCadastro(getImovelRural().getStatusCadastro());
			objNovo.setIdeRequerenteCadastro(getImovelRural().getIdeRequerenteCadastro());
			
			auditoriaFacade.atualizar(objAntigo, objNovo);
			auditoriaFacade.salvar(IRmudancaStatusJustificativa);
			
			RequestContext.getCurrentInstance().execute(nmModal+".hide()");
			JsfUtil.addSuccessMessage(Util.getString("cefir_msg_S002"));
			RequestContext.getCurrentInstance().execute("fecharModal('#statusDialog'); abrirModal('#statusDialog'); document.getElementById('filtroImoveis:btnConsulta').click();");
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void changeAlterarRequerente(ValueChangeEvent event) {
		this.IRmudancaStatusJustificativa.setIndAlterarProprietario((Boolean) event.getNewValue());
		if((Boolean) event.getNewValue()){
			limparCamposRequerente();
			setTelaCpf(Boolean.TRUE);
		} else {
			this.IRmudancaStatusJustificativa.setIdeProprietario(null);
		}
	}
	
	public void limparCamposRequerente() {
		this.IRmudancaStatusJustificativa.setIdeProprietario(new Pessoa());
		this.IRmudancaStatusJustificativa.getIdeProprietario().setPessoaFisica(new PessoaFisica());
		this.IRmudancaStatusJustificativa.getIdeProprietario().setPessoaJuridica(new PessoaJuridica());
	}
	
	public void pesquisarPessoa() throws Exception {
		if (telaCpf) {
			this.IRmudancaStatusJustificativa.getIdeProprietario().setPessoaFisica(imovelRuralServiceFacade.filtrarPessoaFisicaByCpf(this.IRmudancaStatusJustificativa.getIdeProprietario().getPessoaFisica()));
			if(!Util.isNull(this.IRmudancaStatusJustificativa.getIdeProprietario().getPessoaFisica())) {
				this.IRmudancaStatusJustificativa.setIdeProprietario(this.IRmudancaStatusJustificativa.getIdeProprietario().getPessoaFisica().getPessoa());
				if (!Util.isNull(this.getImovelRural().getIdeRequerenteCadastro().getPessoaFisica()) && this.getImovelRural().getIdeRequerenteCadastro().getPessoaFisica().getNumCpf().equals(this.IRmudancaStatusJustificativa.getIdeProprietario().getPessoaFisica().getNumCpf())) {
					limparCamposRequerente();
					JsfUtil.addErrorMessage(Util.getString("cefir_msg_A059", TipoDocumentoEnum.CPF.toString()));
				}
			} else {
				limparCamposRequerente();
				JsfUtil.addErrorMessage(Util.getString("cefir_msg_A012"));
			}
		} else {
			this.IRmudancaStatusJustificativa.getIdeProprietario().setPessoaJuridica(imovelRuralServiceFacade.filtrarPessoaJuridicaByCnpj(this.IRmudancaStatusJustificativa.getIdeProprietario().getPessoaJuridica()));
			if(!Util.isNull(this.IRmudancaStatusJustificativa.getIdeProprietario().getPessoaJuridica())) {
				this.IRmudancaStatusJustificativa.setIdeProprietario(this.IRmudancaStatusJustificativa.getIdeProprietario().getPessoaJuridica().getPessoa());
				if (!Util.isNull(this.getImovelRural().getIdeRequerenteCadastro().getPessoaJuridica()) && this.getImovelRural().getIdeRequerenteCadastro().getPessoaJuridica().getNumCnpj().equals(this.IRmudancaStatusJustificativa.getIdeProprietario().getPessoaJuridica().getNumCnpj())) {
					limparCamposRequerente();
					JsfUtil.addErrorMessage(Util.getString("cefir_msg_A059", TipoDocumentoEnum.CNPJ.toString()));
				}
			} else {
				limparCamposRequerente();
				JsfUtil.addErrorMessage(Util.getString("cefir_msg_A012"));
			}
		}
	}
	/*
	private TipoVinculoImovel carregarTipoVinculoImovel(ImovelRural imovelRural) throws Exception {
		if(!Util.isNullOuVazio(imovelRural)){
			Collection<PessoaImovel> proprietarios = imovelRuralServiceFacade.filtrarPessoasPorImovel(imovelRural.getImovel());
			for (PessoaImovel pessoaImovel : proprietarios) {									
				if(pessoaImovel.getIdeTipoVinculoImovel().getIdeTipoVinculoImovel().equals(TipoVinculoImovel.TIPO_VINCULO_JUSTO_POSSUIDOR)) {
					return new TipoVinculoImovel(2, "Justo Possuidor");					
				}				
			}
			return new TipoVinculoImovel(1, "Proprietário");
		}
		return null;
	}
	 * */

}
