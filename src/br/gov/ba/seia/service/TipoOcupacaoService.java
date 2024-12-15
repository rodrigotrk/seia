package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Ocupacao;

/**
 * 
 * @author rubem.filho
 */

@Stateless
public class TipoOcupacaoService {

	@Inject
	IDAO<Ocupacao> daoOcupacao;

	public void salvarOcupacao(Ocupacao ocupacao) throws Exception {
		daoOcupacao.salvar(ocupacao);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Ocupacao> listarOcupacao() {
		return daoOcupacao.listarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Ocupacao> filtrarListaOcupacao(Ocupacao ocupacao) {
		return daoOcupacao.listarPorExemplo(ocupacao);
	}

	public void excluirOcupacao(Ocupacao Ocupacao) throws Exception {
		daoOcupacao.remover(Ocupacao);
	}

}
