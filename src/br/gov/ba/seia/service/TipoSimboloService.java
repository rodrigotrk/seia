package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Simbolo;

/**
 * 
 * @author rubem.filho
 */

@Stateless
public class TipoSimboloService {

	@Inject
	IDAO<Simbolo> daoSimbolo;

	public void salvarOcupacao(Simbolo simbolo) throws Exception {
		this.daoSimbolo.salvar(simbolo);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Simbolo> listar() {
		return this.daoSimbolo.listarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Simbolo> filtrar(Simbolo simbolo) {
		return this.daoSimbolo.listarPorExemplo(simbolo);
	}

	public void excluirOrgaoExpedidor(Simbolo simbolo) throws Exception {
		this.daoSimbolo.remover(simbolo);
	}

}
