package br.gov.ba.seia.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.ParametroEnum;
import br.gov.ba.seia.enumerator.PerfilEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.security.SecurityService;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AlterarSenhaService {

	@Inject
	IDAO<Usuario> daoUsuario;
	
	@Inject
	SecurityService securityService;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void alterarSenha(Usuario pUsuario) throws Exception {

		Map<String, Object> lParametrosQuery = new HashMap<String, Object>();

		pUsuario.setDscSenha(Util.toMD5(pUsuario.getDscSenha()));
		pUsuario.setSenhaAntiga(Util.toMD5(pUsuario.getSenhaAntiga()));

		lParametrosQuery.put("dscSenha", pUsuario.getDscSenha());
		lParametrosQuery.put("idePessoaFisica", pUsuario.getIdePessoaFisica());
		lParametrosQuery.put("senhaAntiga", pUsuario.getSenhaAntiga());
		lParametrosQuery.put("dtcUltimaSenha", new Date());

		daoUsuario.executarNamedQuery("Usuario.updateSenha", lParametrosQuery, new Usuario[0]);
	}
	
	public List<Usuario> listarUsuariosParaInativacao() {
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.DATE, -(securityService.obterValorPorParametro(ParametroEnum.MAX_DIAS_INATIVACAO_USUARIO)));
		
		DetachedCriteria dc = DetachedCriteria.forClass(Usuario.class)
				
			.setProjection(Projections.projectionList()
					.add(Projections.property("idePessoaFisica"), "idePessoaFisica")
					.add(Projections.property("dtcUltimoLogin"), "dtcUltimoLogin"))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Usuario.class))
		
			.add(Restrictions.le("indExcluido", false))
			.add(Restrictions.le("dtcUltimoLogin", calendar.getTime()))
			.add(Restrictions.ne("idePerfil.idePerfil", PerfilEnum.USUARIO_EXTERNO.getId()));
		
		return daoUsuario.listarPorCriteria(dc);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void inativarUsuario(Integer idePessoaFisica) {
		StringBuilder sql = new StringBuilder();		
		sql.append("UPDATE Usuario ");
		sql.append("SET indExcluido = :indExcluido, dtcExclusao = :dtcExclusao ");
		sql.append("WHERE idePessoaFisica = :idePessoaFisica");
		
		Map<String,Object> params = new HashMap<String,Object>();
		
		params.put("idePessoaFisica", idePessoaFisica);
		params.put("indExcluido", true);
		params.put("dtcExclusao", new Date());
		
		daoUsuario.executarQuery(sql.toString(), params);
	}
}