package br.gov.ba.seia.controller;


public class ValidarPapelSolicitacaoController extends IdentificarPapelSolicitacaoBaseController{
	/*
	public Boolean validar(){
		return 
			isValidarSolicitante() &&
			isValidarRequerente() && 
			isValidarTipoSolicitacao();
	}
	
	private boolean isValidarSolicitante(){
		if(super.getSolicitacao().getSolicitante() == null){
			MensagemUtil.alerta("Não foi possivel identificar o usuário da solicitação");
			return false;
		}
		
		return true;
	}
	
	private boolean isValidarRequerente(){
		if(super.getSolicitacao().getRequerente() == null){
			MensagemUtil.alerta("Não foi possivel identificar o usuário requerente");
			return false;
		}
		
		return true;
	}
	
	private boolean isValidarTipoSolicitacao(){
		try {
			if(super.getSolicitacao().getPapelSolicitacaoEnum() == PapelSolicitacaoEnum.REQUERENTE){
				return true;
			}
			else if(super.getSolicitacao().getPapelSolicitacaoEnum() == PapelSolicitacaoEnum.REPRESENTANTE_LEGAL_PJ){
				return 
					super.getFacade()
						.isRepresentanteLegalPessoaJuridica(super.getSolicitacao().getSolicitante(),super.getSolicitacao().getRequerente());
			}
			else if(super.getSolicitacao().getPapelSolicitacaoEnum() == PapelSolicitacaoEnum.PROCURADOR_PF){
				return
					super.getFacade()
						.isProcuradorPessoaFisica(super.getSolicitacao().getSolicitante(),super.getSolicitacao().getRequerente());
				
			}
			else if(super.getSolicitacao().getPapelSolicitacaoEnum() == PapelSolicitacaoEnum.PROCURADOR_PJ){
				return 
					super.getFacade()
						.isProcuradorPessoaJuridica(super.getSolicitacao().getSolicitante(), super.getSolicitacao().getRequerente());
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		throw new IllegalArgumentException("Erro ao validar");
	}
	 * */
}
	