package br.gov.ba.ws.service;

import java.util.HashMap;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.ws.entity.UsuarioDispositivo;
/**
 * Classe de serviço do usuário dispositivo
 * @author 
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class UsuarioDispositivoService {

	@Inject
	private IDAO<UsuarioDispositivo> usuarioDispositivoIDAO;
	/**
	 * Metodo salvar usuário no dispositivo
	 * @param usuarioDispositivo
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarUsuarioDispositivo(UsuarioDispositivo usuarioDispositivo){
		usuarioDispositivoIDAO.salvarOuAtualizar(usuarioDispositivo);
	}
	/**
	 * Metodo excluir usuário dispositivo
	 * @param usuarioDispositivo
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirUsuarioDispositivo(UsuarioDispositivo usuarioDispositivo) {
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideUsuario", usuarioDispositivo.getUsuario().getIdePessoaFisica());
		parametros.put("codDispositivo", usuarioDispositivo.getCodDispositivo());
		usuarioDispositivoIDAO.executarNamedQuery("UsuarioDispositivo.excluirByIdeUsuarioAndCodDispositivo", parametros);
	}
	/**
	 * Metodo excluir usuário dispositivo pelo codigo no dispositico
	 * @param usuarioDispositivo
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirUsuarioDispositivobyCodDispositivo(UsuarioDispositivo usuarioDispositivo){
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("codDispositivo", usuarioDispositivo.getCodDispositivo());
		usuarioDispositivoIDAO.executarNamedQuery("UsuarioDispositivo.excluirByCodDispositivo", parametros);
	}
}
