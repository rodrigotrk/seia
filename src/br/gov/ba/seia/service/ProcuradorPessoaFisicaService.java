package br.gov.ba.seia.service;

import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.hibernate.HibernateException;

import br.gov.ba.seia.dao.ProcuradorPessoaFisicaDAOImpl;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.ProcuradorPessoaFisica;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProcuradorPessoaFisicaService {

	@EJB
	private ProcuradorPessoaFisicaDAOImpl procuradorPessoaFisicaDAOImpl;


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isProcurarPessoaFisica(PessoaFisica pessoa,PessoaFisica procurador) {
		return procuradorPessoaFisicaDAOImpl.isProcurarPessoaFisica(pessoa, procurador);
	}


	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarProcuradorPessoaFisica(ProcuradorPessoaFisica procuradorPessoaFisica)  {
		procuradorPessoaFisicaDAOImpl.salvarProcuradorPessoaFisica(procuradorPessoaFisica);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isProcuradorPessoaFisica(Pessoa solicitante, Pessoa requerente) {
		return procuradorPessoaFisicaDAOImpl.isProcuradorPessoaFisica(solicitante, requerente);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ProcuradorPessoaFisica> listarProcuradorPessoaFisica(ProcuradorPessoaFisica procuradorPessoaFisica)  {
		if (!Util.isNull(procuradorPessoaFisica)
				&& !Util.isNull(procuradorPessoaFisica.getIdePessoaFisica())
				&& !Util.isNull(procuradorPessoaFisica.getIdePessoaFisica().getIdePessoaFisica())) {
			return procuradorPessoaFisicaDAOImpl.listarProcuradorPessoaFisica(procuradorPessoaFisica);
		}
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ProcuradorPessoaFisica> listarProcuradorPessoaFisicaComProjection(ProcuradorPessoaFisica procuradorPessoaFisica)  {
		if (!Util.isNull(procuradorPessoaFisica)
			&& !Util.isNull(procuradorPessoaFisica.getIdePessoaFisica())
			&& !Util.isNull(procuradorPessoaFisica.getIdePessoaFisica().getIdePessoaFisica())) {
			return procuradorPessoaFisicaDAOImpl.listarProcuradorPessoaFisicaComProjection(procuradorPessoaFisica);
		}
		return null;

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirProcuradorPessoaFisica(ProcuradorPessoaFisica procuradorPessoaFisica)  {
		procuradorPessoaFisicaDAOImpl.excluirProcuradorPessoaFisica(procuradorPessoaFisica);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcuradorPessoaFisica> filtrarProcuradorPessoaFisicaById(PessoaFisica procurador)  {
		return procuradorPessoaFisicaDAOImpl.filtrarProcuradorPessoaFisicaById(procurador);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProcuradorPessoaFisica buscarPorIdViaCriteria(ProcuradorPessoaFisica procurador)  {
		return procuradorPessoaFisicaDAOImpl.buscarPorIdViaCriteria(procurador);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean procuradorAlreadyProcuradorPF(ProcuradorPessoaFisica pProcuradorPessoaFisica)  {
		return procuradorPessoaFisicaDAOImpl.procuradorAlreadyProcuradorPF(pProcuradorPessoaFisica);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProcuradorPessoaFisica buscarProcuradorPessoaFisica(PessoaFisica procurador, PessoaFisica pessoaFisica)  {
		return procuradorPessoaFisicaDAOImpl.buscarProcuradorPessoaFisica(procurador, pessoaFisica);
	}





}