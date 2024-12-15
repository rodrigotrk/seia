package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.CadastroAtividadeNaoSujeitaLicStatusDAOImpl;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLic;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLicStatus;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.enumerator.CadastroAtividadeNaoSujeitaLicTipoStatusEnum;
import br.gov.ba.seia.enumerator.TipoAtividadeNaoSujeitaLicenciamentoEnum;

/**
 * @author eduardo.fernandes
 * @since 05/12/2016
 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CadastroAtividadeNaoSujeitaLicStatusService {

	@Inject
	private CadastroAtividadeNaoSujeitaLicStatusDAOImpl dao;
	
	/**
	 * 
	 * Método que vai retornar uma lista de {@link CadastroAtividadeNaoSujeitaLicStatus}.
	 * 
	 * @author eduardo.fernandes
	 * @since 05/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @return
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CadastroAtividadeNaoSujeitaLicStatus> listar(CadastroAtividadeNaoSujeitaLic cadastro)  {
		return dao.listar(cadastro);
	}
	
	/**
	 * 
	 * Método que vai o {@link CadastroAtividadeNaoSujeitaLicStatus} com Status Concluído.
	 * 
	 * @author eduardo.fernandes
	 * @since 05/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8192">#8192</a>
	 * @return
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CadastroAtividadeNaoSujeitaLicStatus buscarStatusConcluido(CadastroAtividadeNaoSujeitaLic cadastro)  {
		return dao.buscar(cadastro, CadastroAtividadeNaoSujeitaLicTipoStatusEnum.CONCLUIDO);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CadastroAtividadeNaoSujeitaLicStatus buscarStatusCadastroIncompleto(CadastroAtividadeNaoSujeitaLic cadastro)  {
		return dao.buscar(cadastro, CadastroAtividadeNaoSujeitaLicTipoStatusEnum.CADASTRO_INCOMPLETO);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(CadastroAtividadeNaoSujeitaLicStatus status)  {
		dao.salvar(status);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CadastroAtividadeNaoSujeitaLicStatus buscarUltimoStatusPorRequerente(Pessoa pessoaRequerente)  {
		return dao.buscarUltimoStatusPorRequerente(pessoaRequerente);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CadastroAtividadeNaoSujeitaLicStatus buscarUltimoStatusPorRequerenteEmpreendimento(Pessoa pessoaRequerente, Empreendimento empreendimento,TipoAtividadeNaoSujeitaLicenciamentoEnum tipoAtividadeNaoSujeitaLicenciamentoEnum)  {
		return dao.buscarUltimoStatusPorRequerenteEmpreendimento(pessoaRequerente, empreendimento, tipoAtividadeNaoSujeitaLicenciamentoEnum);
	}
}
