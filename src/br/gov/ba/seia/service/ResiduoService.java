package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.Residuo;
/**
 * 
 * @author eduardo.fernandes
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ResiduoService {

	@Inject
	ResiduoDAOImpl residuoDAOImpl; 
	
	/**
	 * Método usado para retornar uma lista de resíduos cadastrados no banco.
	 * @author eduardo.fernandes
	 * @return lista de Residuos 
	 * @
	 */
	public List<Residuo> carregarListaResiduos() {
		return residuoDAOImpl.listarResiduos();
	}
	
	/**
	 * Método usado na [LacTransporteController] para filtrar a busca de Residuo no banco.
	 * @param produto
	 * @return List<Residuo>
	 * @author eduardo.fernandes
	 */
	public List<Residuo> pesquisarResiduos(Residuo residuo){
		return new ResiduoDAOImpl().filtrarResiduos(residuo);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Residuo> filtrarResiduos(String textFilter) {
		return residuoDAOImpl.filtrarResiduos(textFilter);
	}
	
	/**
	 * @Comentários Método que retorna um Residuo de acordo com o Id (Integer) passado pelo usuário.
	 * @param ide
	 * @return Residuo
	 * @
	 * @author eduardo.fernandes
	 */
	public Residuo obterResiduoByIde(Integer ide) {
		return residuoDAOImpl.obterResiduoByIdeResiduo(ide);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Residuo obterResiduoByCodigo(String codigo) {
		return residuoDAOImpl.obterResiduoByCodigoResiduo(codigo);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Residuo obterResiduoByNome(String nome) {
		return residuoDAOImpl.obterResiduoByNomeResiduo(nome);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Residuo obterResiduoByComposicao(String composicao) {
		return residuoDAOImpl.obterResiduoByComposicaoResiduo(composicao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Residuo pResiduo)  {
		this.residuoDAOImpl.salvar(pResiduo);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(Residuo pResiduo)  {
		this.residuoDAOImpl.atualizar(pResiduo);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(Residuo pResiduo)  {
		this.residuoDAOImpl.excluir(pResiduo);
	}
}
