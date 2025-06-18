package br.gov.ba.seia.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.faces.context.FacesContext;

import br.gov.ba.seia.controller.EnquadramentoController;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.DocumentoAto;
import br.gov.ba.seia.entity.Enquadramento;
import br.gov.ba.seia.entity.EnquadramentoAtoAmbiental;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.TramitacaoRequerimento;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.exception.SeiaTramitacaoRequerimentoRuntimeException;
import br.gov.ba.seia.service.EnquadramentoService;
import br.gov.ba.seia.service.TramitacaoRequerimentoService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EfetuarEnquadramentoServiceFacade {
	
	@EJB
	private EnquadramentoService enquadramentoService;
	
	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void efetuarEnquadramento(EnquadramentoController enquadramentoController) throws Exception {		
		Enquadramento enquadramento = enquadramentoController.getEnquadramento();
		if (isEnquadramentoValido(enquadramentoController)) {
			String urlRedirect = null;
			if(enquadramentoController.isReenquadramento()){
				
				if (exibirConfirmacao(enquadramentoController)){
					Html.executarJS("modalConfirmarAletaracaoDeEnquadramentoAtoAmbiental.show()");
					return;
				}
				
				enquadramento = prepararReenquadramento(enquadramentoController, enquadramento);
				urlRedirect = "/paginas/manter-processo/pautaReenquadramentoProcesso.xhtml";
			} 
			else {
				urlRedirect = "/paginas/novo-requerimento/consulta.xhtml";
			}
			enquadramento.setIdePessoa(getPessoaOperador());
			
			enquadramentoService.salvarTramitandoSemFlush(enquadramento, enquadramentoController.isReenquadramento());
			if(isRequerimentoFoiTramitadoPorOutroUsuario(enquadramentoController)) {
				throw new SeiaTramitacaoRequerimentoRuntimeException(Util.getString("requerimento_msg_ja_tramitado", enquadramentoController.getRequerimento().getNumRequerimento()));
			}
			
			enquadramentoService.salvarComunicacao(enquadramento);
			
			if (enquadramento.getIndEnquadramentoAprovado()) {
				ContextoUtil.getContexto().setObject("Enquadramento efetuado com sucesso!");
			} 
			else {
				ContextoUtil.getContexto().setObject("Um e-mail contendo a justificativa do não enquadramento do requerimento "
												   + "foi enviado à todos os interessados. Aguarde as alterações cabíveis para "
												   + "efetuar uma nova tentativa de enquadramento!");
			}	
			
			if("/paginas/manter-processo/pautaReenquadramentoProcesso.xhtml".equalsIgnoreCase(enquadramentoController.getCameFrom())){
				
				Html.executarJS("dialogEfetuarEnquadramento.hide()");
				Html.executarJS("atualizarPauta()");
				
			}else{
				if (!Util.isNullOuVazio(enquadramentoController.getProcessoReenquadramentoDTO())){
					SessaoUtil.adicionarObjetoSessao("numProcesso", enquadramentoController.getProcessoReenquadramentoDTO().getProcessoReenquadramento().getIdeProcesso().getNumProcesso());
				}
				
				FacesContext.getCurrentInstance().getExternalContext().redirect(urlRedirect);
			}
		}		
	}

	private boolean exibirConfirmacao(EnquadramentoController enquadramentoController) throws Exception {
		return enquadramentoController.isEnquadramentoAprovado() && !enquadramentoController.isConcluirSemAlterar() && enquadramentoService.verificarAletaracaoDeEnquadramentoAtoAmbiental(enquadramentoController);
	}

	private Enquadramento prepararReenquadramento(EnquadramentoController enquadramentoController, Enquadramento enquadramento) throws Exception {
		List<AtoAmbiental> listaAA = new ArrayList<AtoAmbiental>();
		for (EnquadramentoAtoAmbiental enquadramentoAtoAmbiental : enquadramentoService.listarEnquadramentoAtoAmbientalByEnquadramento(enquadramento)) {
			listaAA.add(enquadramentoAtoAmbiental.getAtoAmbiental());
		}
		
		List<EnquadramentoAtoAmbiental> listaDeEnquadramentoInalterado = new ArrayList<EnquadramentoAtoAmbiental>();
		
		for (EnquadramentoAtoAmbiental enqAA : enquadramento.getEnquadramentoAtoAmbientalCollection()) {
			boolean atoJaEnquadrado = listaAA.contains(enqAA.getAtoAmbiental());
			boolean novoDocumentoAdicionado = false;
			for (DocumentoAto documentoAto : enqAA.getListaDocumentosAtos()) {
				if(documentoAto.isCheckedReenquadramento()){
					enqAA.setIdeEnquadramentoAtoAmbiental(null);
					enqAA.setEnquadramento(enquadramento);
					documentoAto.setEnquadramentoAtoAmbiental(documentoAto.getReenquadramentoAtoAmbiental());
					novoDocumentoAdicionado = true;
				}
			}
			if(atoJaEnquadrado && !novoDocumentoAdicionado){
				listaDeEnquadramentoInalterado.add(enqAA);
			}
		}
		
		enquadramento.setIdeEnquadramento(null);
		enquadramento.setIdeRequerimentoUnico(null);
		enquadramento.setIndPassivelEiarima(null);
		enquadramento.setDscCaminhoArquivoRima(null);
		enquadramento.setIdeRequerimento(null);
		enquadramento.setIdeProcessoReenquadramento(enquadramentoController.getProcessoReenquadramentoDTO().getProcessoReenquadramento());
		enquadramento.getEnquadramentoAtoAmbientalCollection().removeAll(listaDeEnquadramentoInalterado);
		
		return enquadramento;
	}
	
	private boolean isEnquadramentoValido(EnquadramentoController enquadramentoController) throws Exception {
		
		boolean valido = true;
		Boolean dla = enquadramentoController.isDla();
		Boolean outrosAtos = enquadramentoController.getOutrosAtos();
		Enquadramento enquadramento = enquadramentoController.getEnquadramento();
		enquadramento.setEnquadramentoAtoAmbientalCollection(enquadramentoController.getEnquadramentoAtoAmbientalCollection());
		
		if(isRequerimentoFoiTramitadoPorOutroUsuario(enquadramentoController)) {
			valido = false;
			JsfUtil.addErrorMessage(Util.getString("requerimento_msg_ja_tramitado",enquadramentoController.getRequerimento().getNumRequerimento()));
			return valido;
		}
		
		if (Util.isNull(enquadramento.getIndEnquadramentoAprovado())) {
			JsfUtil.addErrorMessage("O campo * É possível realizar o enquadramento? é de preenchimento obrigatório");
			valido = false;
			return valido;
		}
		else {
			
			if (enquadramento.getIndEnquadramentoAprovado() || (dla && Util.isNull(outrosAtos))) {
				
				if (Util.isNull(enquadramento.getIdeProcessoReenquadramento()) && Util.isNull(enquadramento.getIndPassivelEiarima())) {
					JsfUtil.addErrorMessage("O Campo O empreendimento é passível de EIA/RIMA? é de preenchimento obrigatório");
					valido = false;
				}
				
				if (dla && Util.isNull(outrosAtos) && !enquadramentoController.isReenquadramento()) {
					JsfUtil.addErrorMessage("O campo Além da DLA, deseja enquadrar o requerimento em algum ato ambiental? é de preenchimento obrigatório.");
					valido = false;
					return valido;
				}
				
				for (EnquadramentoAtoAmbiental atos : enquadramento.getEnquadramentoAtoAmbientalCollection() ) {
					if(atos.getAtoAmbiental().isRlac() && Util.isNullOuVazio(atos.getTipologia())){
						JsfUtil.addErrorMessage("Para o Ato Ambiental Rlac é obrigatório a escolha de tipologia.");
						valido = false;
						return valido;	
					}	
				}
				
				if (Util.isNullOuVazio(enquadramento.getEnquadramentoAtoAmbientalCollection())) {
					JsfUtil.addErrorMessage("Nenhum Ato Ambiental foi selecionado");
					valido = false;
					return valido;
				}
				else {
					
					boolean existeAlgumDocumentoSisfaunaSelecionado = false;
					boolean existeAtoSisfauna = false;
					
					for (EnquadramentoAtoAmbiental enquadramentoAtoAmbiental : enquadramento.getEnquadramentoAtoAmbientalCollection()) {
						
						if (Util.isNullOuVazio(enquadramentoAtoAmbiental.getListaDocumentosAtos())){
							if(!enquadramentoController.isReenquadramento()){
								if(enquadramentoAtoAmbiental.getAtoAmbiental().getIdeAtoAmbiental().equals(AtoAmbientalEnum.SISFAUNA.getId())) {
									existeAtoSisfauna = true;
									continue;
								} else {
									valido = false;
									JsfUtil.addErrorMessage("Nenhum documento obrigatório foi selecionado para o ato '"+enquadramentoAtoAmbiental.getAtoAmbiental().getNomAtoAmbiental()+"'.");
									return valido;
								}
							}
						}
						boolean hasFormularioChecked = false;
						if(existeFormalarioNaLista(enquadramentoAtoAmbiental, enquadramentoController.isReenquadramento())){
							if(!enquadramentoAtoAmbiental.getAtoAmbiental().isOutorga() && enquadramentoController.isReenquadramento()) {
								hasFormularioChecked = false;
							}
							
							for (DocumentoAto documentoAto : enquadramentoAtoAmbiental.getListaDocumentosAtos()) {
								if(enquadramentoController.isReenquadramento()){
									if((documentoAto.getIdeDocumentoObrigatorio().isIndFormularioDigital() && (documentoAto.isChecked() || documentoAto.isCheckedReenquadramento()))) {
										hasFormularioChecked = true;										
										if(enquadramentoAtoAmbiental.getAtoAmbiental().getIdeAtoAmbiental().equals(AtoAmbientalEnum.SISFAUNA.getId())) {
											existeAlgumDocumentoSisfaunaSelecionado = true;
										}
									}
								} else {
									if(documentoAto.isChecked() && documentoAto.getIdeDocumentoObrigatorio().getIndFormulario()) {
										hasFormularioChecked = true;										
										if(enquadramentoAtoAmbiental.getAtoAmbiental().getIdeAtoAmbiental().equals(AtoAmbientalEnum.SISFAUNA.getId())) {
											existeAlgumDocumentoSisfaunaSelecionado = true;
										}
									}
								}
							}
							
							if (!hasFormularioChecked) {
								if(enquadramentoAtoAmbiental.getAtoAmbiental().getIdeAtoAmbiental().equals(AtoAmbientalEnum.SISFAUNA.getId())) {
									existeAtoSisfauna = true;
									continue;
								} else {
									String msg = enquadramentoController.isReenquadramento() ? "Nenhum formulário digital"  : "Nenhum formulário";
									msg += " foi selecionado para o a ato '"+enquadramentoAtoAmbiental.getAtoAmbiental().getNomAtoAmbiental()+"'." ;
									JsfUtil.addErrorMessage(msg);
									valido = false;
								}
							}
							
						}
						else {
							
							if(!enquadramentoController.isReenquadramento()){
								boolean hasChecked = false;
								for (DocumentoAto documentoAto : enquadramentoAtoAmbiental.getListaDocumentosAtos()) {
									if (documentoAto.isChecked()) {
										hasChecked = true;
										
										if(enquadramentoAtoAmbiental.getAtoAmbiental().getIdeAtoAmbiental().equals(AtoAmbientalEnum.SISFAUNA.getId())) {
											existeAlgumDocumentoSisfaunaSelecionado = true;
										}
									}
								}
								
								if (!hasChecked) {
									if(enquadramentoAtoAmbiental.getAtoAmbiental().getIdeAtoAmbiental().equals(AtoAmbientalEnum.SISFAUNA.getId())) {
										existeAtoSisfauna = true;
										continue;
									} else {
										JsfUtil.addErrorMessage("Nenhum documento obrigatório foi selecionado para o ato '"+enquadramentoAtoAmbiental.getAtoAmbiental().getNomAtoAmbiental()+"'.");
										valido = false;
									}
								}
							}
							
						}
					}
					
					if(existeAtoSisfauna && !existeAlgumDocumentoSisfaunaSelecionado) {
						valido = false;
						JsfUtil.addErrorMessage("Nenhum documento obrigatório foi selecionado para o ato Sisfauna.");
						return valido;
					}
				}
				
			}
			else {
				
				if (!enquadramento.getIndEnquadramentoAprovado() && Util.isNullOuVazio(enquadramento.getDscJustificativa())) {
					JsfUtil.addErrorMessage("O campo Justificativa é de preenchimento obrigatório");
					valido = false;
					return valido; 
				}
			}
		}
		
		return valido;
	}
	
	private boolean isRequerimentoFoiTramitadoPorOutroUsuario(EnquadramentoController enquadramentoController) throws Exception {
		Integer ideRequerimento = enquadramentoController.getRequerimento().getIdeRequerimento();
		TramitacaoRequerimento tramitacaoRequerimento = enquadramentoController.getTramitacaoRequerimento();
		return !tramitacaoRequerimentoService.isTamitacaoRequerimentoAtual(ideRequerimento, tramitacaoRequerimento);
	}
	
	public boolean existeFormalarioNaLista(EnquadramentoAtoAmbiental enquadramentoAtoAmbiental, boolean isReenquadramento) {
		if(!Util.isNullOuVazio(enquadramentoAtoAmbiental.getListaDocumentosAtos())) {
			for (DocumentoAto documentoAto : enquadramentoAtoAmbiental.getListaDocumentosAtos()) {
				if (isReenquadramento) {
					return documentoAto.getIdeDocumentoObrigatorio().isIndFormularioDigital();
				}
				return documentoAto.getIdeDocumentoObrigatorio().getIndFormulario();
			}
		}
		return false;
	}
	
	private Pessoa getPessoaOperador() {
		return new Pessoa(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica());
	}
}
