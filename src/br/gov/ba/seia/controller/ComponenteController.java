package br.gov.ba.seia.controller;

import javax.faces.bean.RequestScoped;
import javax.inject.Named;

import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;


@RequestScoped
@Named("componenteController")
public class ComponenteController {

	public ClassificacaoSecaoEnum getEnum(ClassificacaoSecaoEnum classificacaoSecaoEnum){
		return classificacaoSecaoEnum;
	}
	
	
}
