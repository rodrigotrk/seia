package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.ReservaLegal;
import br.gov.ba.seia.entity.ReservaLegalBloqueio;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ReservaLegalBloqueioService {

	@Inject
	private IDAO<ReservaLegalBloqueio> rLBloqueioIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ReservaLegalBloqueio obterUltimaReservaLegalBloqueioBy(ReservaLegal reservaLegal) {
		List<ReservaLegalBloqueio> lista = listarReservaLegalBloqueioBy(reservaLegal);
		if(!Util.isNullOuVazio(lista)){
			return lista.get(lista.size() - 1);
		}
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer obterIdeUltimaReservaLegalBy(ReservaLegal reservaLegal) {
		return obterUltimaReservaLegalBloqueioBy(reservaLegal).getIdeReservaLegalBloqueio();
	}

	/**
	 * Criteria genérica para se carregar o objeto {@link ReservaLegalBloqueio}
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @return
	 * @since 20/07/2015
	 */
	protected DetachedCriteria obterCriteriaComProjection(ReservaLegal reservaLegal) {
		return DetachedCriteria.forClass(ReservaLegalBloqueio.class)
				.createAlias("reservaLegal", "rl")
				.createAlias("usuario", "usr")
				.createAlias("usr.idePerfil", "perfil")
				.createAlias("usr.pessoaFisica", "pf")
				.add(Restrictions.eq("rl.ideReservaLegal", reservaLegal.getIdeReservaLegal()))
				.setProjection(
						Projections.projectionList()
						.add(Projections.property("ideReservaLegalBloqueio"), "ideReservaLegalBloqueio")
						.add(Projections.property("dtcCriacao"), "dtcCriacao")
						.add(Projections.property("indBloqueado"), "indBloqueado")
						.add(Projections.property("rl.ideReservaLegal"), "reservaLegal.ideReservaLegal")
						.add(Projections.property("usr.idePessoaFisica"), "usuario.idePessoaFisica")
						.add(Projections.property("perfil.idePerfil"), "usuario.idePerfil.idePerfil")
						.add(Projections.property("pf.idePessoaFisica"), "usuario.pessoaFisica.idePessoaFisica")
						.add(Projections.property("pf.nomPessoa"), "usuario.pessoaFisica.nomPessoa")
						)
						.setResultTransformer(new AliasToNestedBeanResultTransformer(ReservaLegalBloqueio.class));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ReservaLegalBloqueio> listarReservaLegalBloqueioBy(ReservaLegal reservaLegal) {
		DetachedCriteria criteria = obterCriteriaComProjection(reservaLegal);
		return rLBloqueioIDAO.listarPorCriteria(criteria, Order.asc("ideReservaLegalBloqueio"));
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarReservaLegalBloqueio(ReservaLegalBloqueio reservaLegalBloqueio)  {
		rLBloqueioIDAO.salvar(reservaLegalBloqueio);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOrAtualizarReservaLegalBloqueio(ReservaLegalBloqueio reservaLegalBloqueio)  {
		rLBloqueioIDAO.salvarOuAtualizar(reservaLegalBloqueio);
	}
}
