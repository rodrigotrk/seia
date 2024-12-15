package br.gov.ba.seia.facade;

import java.util.Collection;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.CerhHistEnvioNotificacaoImpl;
import br.gov.ba.seia.entity.CerhHistEnvioNotificacao;
import br.gov.ba.seia.entity.GeoBahia;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhHistEnvioNotificacaoServiceFacade {
	
	@EJB
	private CerhHistEnvioNotificacaoImpl CerhHistEnvioNotificacaoImpl;
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCerhHistoricoEnvioNotificacao(CerhHistEnvioNotificacao cerhHistEnvioNotificacao) throws Exception {
		CerhHistEnvioNotificacaoImpl.salvar(cerhHistEnvioNotificacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhHistEnvioNotificacao> ListarCerhHistoricoEnvioNotificacao(Map<String, Object> params) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhHistEnvioNotificacao.class,"historico");
		criteria
		.createAlias("historico.ideUsuarioAgua", "usuarioAgua", JoinType.LEFT_OUTER_JOIN)
		.createAlias("historico.ideEmpreendimento", "empreendimento",JoinType.LEFT_OUTER_JOIN);
		
		adicionarRestrictions(criteria, params);
	//	adicionarProjection(criteria);
		
		return CerhHistEnvioNotificacaoImpl.listar(criteria);
	}
	
	private void adicionarRestrictions(DetachedCriteria criteria, Map<String, Object> params) {
		
		if(!Util.isNull(params.get("rpgaSelecionado"))) {
			GeoBahia rpgaSelecionado = (GeoBahia) params.get("rpgaSelecionado");
			criteria.add(Restrictions.eq("historico.ideGeoRpga",rpgaSelecionado.getGid()));
		}
		
		if(!Util.isNull(params.get("requerente"))) {
			Pessoa usuarioAgua = (Pessoa) params.get("requerente");
			if(!Util.isNullOuVazio(usuarioAgua)) {
				criteria.add(Restrictions.eq("usuarioAgua.idePessoa", usuarioAgua.getIdePessoa()));
			}
		}
		
		if(!Util.isNull(params.get("nomEmpreendimento"))) {
			String nomEmpreendimento = (String) params.get("nomEmpreendimento");
			criteria.add(Restrictions.ilike("empreendimento.nomEmpreendimento", nomEmpreendimento,MatchMode.ANYWHERE));
		}
		
	}
	/*
	private void adicionarProjection(DetachedCriteria criteria) {
		criteria.setProjection(Projections.projectionList()
			.add(Projections.property("ideEnvioNotificacao"),"ideEnvioNotificacao")
			.add(Projections.property("usuarioAgua.idePessoa"),"historico.ideUsuarioAgua.idePessoa")
			.add(Projections.property("empreendimento.nomEmpreendimento"),"historico.ideEmpreendimento.nomEmpreendimento")
		);
	}
	 * */
	
}
