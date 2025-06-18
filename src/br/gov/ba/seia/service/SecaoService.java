package br.gov.ba.seia.service;

import java.util.Collection;
import java.util.Date;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.SecaoDAOImpl;
import br.gov.ba.seia.dao.TipoSecaoDAOImpl;
import br.gov.ba.seia.entity.Secao;
import br.gov.ba.seia.entity.TipoSecao;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SecaoService {

	@Inject
	IDAO<Secao> daoSecao;	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Secao filtrarSecaoById(Secao pSecao) {

		return daoSecao.buscarEntidadePorExemplo(pSecao);
	}

	public Collection<Secao> filtrarListaSecoes(Secao pSecao) {

		return new SecaoDAOImpl().getSecoes(pSecao);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarSecao(Secao pSecao)  {

		pSecao.setDtcCriacao(new Date());

		daoSecao.salvar(pSecao);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarSecao(Secao pSecao)  {

		daoSecao.atualizar(pSecao);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirSecao(Secao pSecao)  {

		pSecao.setIndExcluido(true);
		pSecao.setDtcExclusao(new Date());

		daoSecao.atualizar(pSecao);
	}

	//Depend�ncias
	public Collection<TipoSecao> filtrarListaTiposSecao(TipoSecao pTipoSecao) {

		return new TipoSecaoDAOImpl().getTiposSecao(pTipoSecao);
	}
}