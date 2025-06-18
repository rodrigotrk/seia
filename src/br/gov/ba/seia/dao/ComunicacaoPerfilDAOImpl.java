package br.gov.ba.seia.dao;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.Comunicacao;
import br.gov.ba.seia.entity.ComunicacaoPerfil;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ComunicacaoPerfilDAOImpl {
	
	
	@Inject
	private IDAO<ComunicacaoPerfil> comunicacaoPerfilDAO;
	
	public void salvarOuAtualizarComunicacaoPerfil(ComunicacaoPerfil comunicacaoPerfil ) {
			comunicacaoPerfilDAO.salvarOuAtualizar(comunicacaoPerfil);
		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerPor(Comunicacao comunicacao)  {
		
				StringBuilder sql = new StringBuilder();
				sql.append("delete from ComunicacaoPerfil cp where cp.comunicacaoPerfilPK.ideComunicacao = :ideComunicacao");
				
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("ideComunicacao", comunicacao.getIdeComunicacao());
				comunicacaoPerfilDAO.executarQuery(sql.toString(), params);
		
	}
	
	

}
