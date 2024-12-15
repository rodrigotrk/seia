package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.VwRequerentePfExternoDAOImpl;
import br.gov.ba.seia.entity.PessoaFisica;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class VwRequerentePfExternoService {

	@Inject
	private VwRequerentePfExternoDAOImpl vwRequerentePfExternoImpl;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<PessoaFisica> listarVwRequerentePfExterno(PessoaFisica pPessoaFisica) throws Exception {
		Collection<PessoaFisica> vwRequerentePfExterno = vwRequerentePfExternoImpl.listarVwRequerentePfExterno(pPessoaFisica);
		return vwRequerentePfExterno;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<PessoaFisica> listarVwRequerentePfExternoSemAcento(PessoaFisica pPessoaFisica) throws Exception {
		Collection<PessoaFisica> vwRequerentePfExterno = vwRequerentePfExternoImpl.listarVwRequerentePfExternoSemAcento(pPessoaFisica);
		return vwRequerentePfExterno;
	}
}
