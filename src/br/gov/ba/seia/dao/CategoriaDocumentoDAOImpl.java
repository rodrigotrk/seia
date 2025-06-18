package br.gov.ba.seia.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.CategoriaDocumento;
import br.gov.ba.seia.entity.Perfil;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CategoriaDocumentoDAOImpl {
	
	@Inject
	IDAO<CategoriaDocumento> categoriaDocumentoDAO;
	
	public CategoriaDocumento carregarCategoriaDocumento(Integer ideCategoriaDocumento){
		return categoriaDocumentoDAO.carregarGet(ideCategoriaDocumento);
	}
	
	public List<CategoriaDocumento> listarCategoriaDocumentoPorPerfil(Perfil idePerfil) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("select cd ");
		sql.append("from CategoriaDocumento cd ");
		sql.append("     inner join cd.categoriaDocumentoPerfilAreaCollection cdpa ");
		sql.append("where cdpa.idePerfil = :idePerfil ");
		sql.append("order by cd.nomCategoria asc ");
		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("idePerfil", idePerfil);
		
		return categoriaDocumentoDAO.listarPorQuery(sql.toString(),param);
	}
	
	public List<CategoriaDocumento> listarCategoriaDocumentoPorArea(Area ideArea) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("select cd ");
		sql.append("from CategoriaDocumento cd ");
		sql.append("     inner join cd.categoriaDocumentoPerfilAreaCollection cdpa ");
		sql.append("where cdpa.ideArea = :ideArea ");
		sql.append("order by cd.nomCategoria asc ");
		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("ideArea", ideArea);
		
		return categoriaDocumentoDAO.listarPorQuery(sql.toString(),param);
	}
	
}