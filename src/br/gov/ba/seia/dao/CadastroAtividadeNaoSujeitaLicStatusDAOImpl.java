package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLic;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLicStatus;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.TipoAtividadeNaoSujeitaLicenciamento;
import br.gov.ba.seia.enumerator.CadastroAtividadeNaoSujeitaLicTipoStatusEnum;
import br.gov.ba.seia.enumerator.TipoAtividadeNaoSujeitaLicenciamentoEnum;
import br.gov.ba.seia.util.Util;

/**
 * 
 * @author eduardo.fernandes
 * @since 05/12/2016
 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CadastroAtividadeNaoSujeitaLicStatusDAOImpl {

	@Inject
	private IDAO<CadastroAtividadeNaoSujeitaLicStatus> dao;
	

	private DetachedCriteria getCriteria(CadastroAtividadeNaoSujeitaLic cadastro) {
		return DetachedCriteria.forClass(CadastroAtividadeNaoSujeitaLicStatus.class)
				.createAlias("idePessoaFisica", "pf")
				.createAlias("cadastroAtividadeNaoSujeitaLic", "cadastro")
				.createAlias("cadastroAtividadeNaoSujeitaLicTipoStatus", "tipoStatus")
				.add(Restrictions.eq("cadastro.ideCadastroAtividadeNaoSujeitaLic", cadastro.getIdeCadastroAtividadeNaoSujeitaLic()));
		 
	}
	
	/**
	 * 
	 * Método que vai retornar todos os {@link CadastroAtividadeNaoSujeitaLicStatus} inseridos no banco.
	 * 
	 * @author eduardo.fernandes
	 * @param cadastro
	 * @since 07/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8187">#8187</a>
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CadastroAtividadeNaoSujeitaLicStatus> listar(CadastroAtividadeNaoSujeitaLic cadastro) {
		return dao.listarPorCriteria(getCriteria(cadastro), Order.asc("dtcStatus"));
	}

	/**
	 * 
	 * 
	 * @author eduardo.fernandes
	 * @since 06/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param status
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(CadastroAtividadeNaoSujeitaLicStatus status) {
		dao.salvarOuAtualizar(status);
	}

	/**
	 * 
	 * @author eduardo.fernandes 
	 * @since 22/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/">#</a> 
	 * @param cadastro
	 * @param concluido
	 * @return
	 * @throws Exception 
	 */
	public List<CadastroAtividadeNaoSujeitaLicStatus> listar(CadastroAtividadeNaoSujeitaLic cadastro, CadastroAtividadeNaoSujeitaLicTipoStatusEnum concluido)  {
		DetachedCriteria criteria = getCriteria(cadastro)
				.add(Restrictions.eq("tipoStatus.ideCadastroAtividadeNaoSujeitaLicTipoStatus", concluido.getIde()));
		return dao.listarPorCriteria(criteria, Order.asc("dtcStatus"));
	}
	
	/**
	 * 
	 * @author eduardo.fernandes 
	 * @since 22/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/">#</a>  ADICIONAR TICKET
	 * @param cadastro
	 * @param tipoStatus
	 * @return
	 * @throws Exception 
	 */
	public CadastroAtividadeNaoSujeitaLicStatus buscar(CadastroAtividadeNaoSujeitaLic cadastro, CadastroAtividadeNaoSujeitaLicTipoStatusEnum tipoStatus) {
		DetachedCriteria criteria = getCriteria(cadastro)
				.add(Restrictions.eq("tipoStatus.ideCadastroAtividadeNaoSujeitaLicTipoStatus", tipoStatus.getIde()));
		return dao.buscarPorCriteria(criteria);
	}
	
	public CadastroAtividadeNaoSujeitaLicStatus buscarUltimoStatusPorRequerente(Pessoa pessoaRequerente) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CadastroAtividadeNaoSujeitaLicStatus.class);
		criteria.createAlias("cadastroAtividadeNaoSujeitaLic", "cadastro");
		criteria.createAlias("cadastro.ideEmpreendimento", "ideEmpreendimento");
		criteria.add(Restrictions.eq("cadastro.idePessoaRequerente", pessoaRequerente));
		criteria.add(Restrictions.eq("cadastro.indExcluido", Boolean.FALSE));
		List<CadastroAtividadeNaoSujeitaLicStatus> lista = dao.listarPorCriteria(criteria, Order.desc("dtcStatus"));
		if(!Util.isNullOuVazio(lista)){
			return lista.get(0);
		}
		return null;
	} 
	
	
	public CadastroAtividadeNaoSujeitaLicStatus buscarUltimoStatusPorRequerenteEmpreendimento(Pessoa pessoaRequerente, Empreendimento empreendimento, TipoAtividadeNaoSujeitaLicenciamentoEnum tipoAtividadeNaoSujeitaLicenciamentoEnum) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CadastroAtividadeNaoSujeitaLicStatus.class);
		criteria.createAlias("cadastroAtividadeNaoSujeitaLic", "cadastro");
		criteria.createAlias("cadastro.ideEmpreendimento", "ideEmpreendimento");
		criteria.add(Restrictions.eq("cadastro.idePessoaRequerente", pessoaRequerente));
		criteria.add(Restrictions.eq("cadastro.ideEmpreendimento", empreendimento));
		criteria.add(Restrictions.eq("cadastro.tipoAtividadeNaoSujeitaLicenciamento",  new TipoAtividadeNaoSujeitaLicenciamento(tipoAtividadeNaoSujeitaLicenciamentoEnum)));
		criteria.add(Restrictions.eq("cadastro.indExcluido", Boolean.FALSE));
		List<CadastroAtividadeNaoSujeitaLicStatus> lista = dao.listarPorCriteria(criteria, Order.desc("dtcStatus"));
		if(!Util.isNullOuVazio(lista)){
			return lista.get(0);
		}
		return null;
	} 
}
