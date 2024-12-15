package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.List;

import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.RegistroFlorestaProducaoResponsavelTecnico;
import br.gov.ba.seia.util.ExpressaoRegularUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.rfp.Componente;
import br.gov.ba.seia.util.rfp.Dialog;


public class RFPResponsavelTecnicoController {
	
	public void exibir(RFPController rfp){
		
		if(rfp.getRegistroFlorestaProducaoResponsavelTecnicoList() == null){
			rfp.setRegistroFlorestaProducaoResponsavelTecnicoList(new ArrayList<RegistroFlorestaProducaoResponsavelTecnico>());
		}
		
		rfp.getIdeRegistroFlorestaProducaoResponsavelTecnico();
		rfp.setIdeRegistroFlorestaProducaoResponsavelTecnico(new RegistroFlorestaProducaoResponsavelTecnico());
		rfp.getIdeRegistroFlorestaProducaoResponsavelTecnico().setIdePessoaFisica(new PessoaFisica());
		
		Html.getCurrency()
			.update(Componente.formResponsavelTecnico)
			.show(Dialog.dialogRfpResponsavelTecnico);
	}
	
	public void carregar(RFPController rfp){
		
		if(validar(rfp.getIdeRegistroFlorestaProducaoResponsavelTecnico().getIdePessoaFisica())){
			
			PessoaFisica pessoaFisica = rfp.getFacade().buscarPessoaFisicaPorCPF(rfp.getIdeRegistroFlorestaProducaoResponsavelTecnico().getIdePessoaFisica());
			
			if(pessoaFisica==null){
				JsfUtil.addErrorMessage("CPF não encontrado.");
			}else{
				pessoaFisica.setResponsavelTecnico(true);
				rfp.getIdeRegistroFlorestaProducaoResponsavelTecnico().setIdePessoaFisica(pessoaFisica);
			}
		}
		
		Html.atualizar(Componente.formResponsavelTecnico);
	
	}
	
	public boolean validar(PessoaFisica pessoaFisica){
		
		if(Util.isNullOuVazio(pessoaFisica.getNumCpf())){
			MensagemUtil.msg0003("CPF");
			return false;
		}
		
		return true;
	}
	
	public boolean validar(List<RegistroFlorestaProducaoResponsavelTecnico> tecnicos){
		
		if(tecnicos== null || tecnicos.isEmpty()){
			MensagemUtil.msg0003("resposável técnico pelo registro da floresta de procução plantada");
			return false;
		}
	
		return true;
	}
	
	public void adicionar(RFPController rfp){
		
		if(valido(rfp.getIdeRegistroFlorestaProducaoResponsavelTecnico())){
		
			PessoaFisica buscarPessoaPorCpf = rfp.getFacade().buscarPessoaPorCpf(rfp.getIdeRegistroFlorestaProducaoResponsavelTecnico().getIdePessoaFisica());	
			
			if(!Util.isNullOuVazio(buscarPessoaPorCpf)){
				rfp.getIdeRegistroFlorestaProducaoResponsavelTecnico().getIdePessoaFisica().setIdePessoaFisica(buscarPessoaPorCpf.getIdePessoaFisica());
			}
			
			for (RegistroFlorestaProducaoResponsavelTecnico responsavel : rfp.getRegistroFlorestaProducao().getRegistroFlorestaProducaoResponsavelTecnicoList()) {
				
				if(responsavel.getIdePessoaFisica().getNumCpf().equals(rfp.getIdeRegistroFlorestaProducaoResponsavelTecnico().getIdePessoaFisica().getNumCpf()) &&
				  (!rfp.getIdeRegistroFlorestaProducaoResponsavelTecnico().isEditar())){
					Html.atualizar("formResponsavelTecnico");
					JsfUtil.addErrorMessage("Esse responsável técnico já foi cadastrado.");
					return;
				}
			}

			if(!rfp.getIdeRegistroFlorestaProducaoResponsavelTecnico().isEditar()){
				rfp.getRegistroFlorestaProducao().getRegistroFlorestaProducaoResponsavelTecnicoList().add(rfp.getIdeRegistroFlorestaProducaoResponsavelTecnico());
			}
			
			rfp.getFacade().salvarAtoDeclaratorio(rfp.getIdeRegistroFlorestaProducao().getIdeAtoDeclaratorio());
			
			MensagemUtil.msg0010();
			
			Html.getCurrency()
				.update(Componente.abaRegistroFlorestaPlantada)
				.hide(Dialog.dialogRfpResponsavelTecnico);
			}
	}
	
	private boolean valido(RegistroFlorestaProducaoResponsavelTecnico tecnico){
		
		if(Util.isNullOuVazio(tecnico.getIdePessoaFisica().getNumCpf())){
			MensagemUtil.msg0003("CPF");
			return false;
		}
		else if(Util.isNullOuVazio(tecnico.getIdePessoaFisica().getNomPessoa())){
			MensagemUtil.msg0003("Nome");
			return false;
		}
		else if(Util.isNullOuVazio(tecnico.getIdePessoaFisica().getDtcNascimento())){
			MensagemUtil.msg0003("Data de nascimento");
			return false;
		}
		else if(Util.isNullOuVazio(tecnico.getIdePessoaFisica().getPessoa().getDesEmail())){
			MensagemUtil.msg0003("Email");
			return false;
		}
		
		else if(Util.isNullOuVazio(tecnico.getIdePessoaFisica().getNomMae())){
			MensagemUtil.msg0003("Nome da mãe");
			return false;
		}
		else if(Util.isNullOuVazio(tecnico.getIdePessoaFisica().getDesNaturalidade())){
			MensagemUtil.msg0003("Naturalidade");
			return false;
		}
		else if(Util.isNullOuVazio(tecnico.getIdePessoaFisica().getIdePais())){
			MensagemUtil.msg0003("Nacionalidade");
			return false;
		}
		
		else if(!ExpressaoRegularUtil.validar(tecnico.getIdePessoaFisica().getPessoa().getDesEmail(), ExpressaoRegularUtil.getRegexEmail())){
			JsfUtil.addWarnMessage("Email inválido");
			return false;
		}

		
		return true;
	}
	
	
	public void excluir(RFPController rfp){
		
		rfp.getRegistroFlorestaProducaoResponsavelTecnicoList()
			.remove(rfp.getIdeRegistroFlorestaProducaoResponsavelTecnico());
		
		if(rfp.getIdeRegistroFlorestaProducaoResponsavelTecnico().getIdeRegistroFlorestaProducaoResponsavelTecnico()!=null){
			rfp.getFacade().excluirRegistroFlorestaProducaoResponsavelTecnico(rfp.getIdeRegistroFlorestaProducaoResponsavelTecnico());
		}
		
		rfp.getFacade().salvarAtoDeclaratorio(rfp.getIdeRegistroFlorestaProducao().getIdeAtoDeclaratorio());
		
		Html.getCurrency()
			.update(Componente.panelRegistroFlorestaProducaoResponsavelTecnico)
			.hide(Dialog.confirmDialogExcluirResponsavelTecnico);
		
		MensagemUtil.msg0005();
	}
	
	
}

