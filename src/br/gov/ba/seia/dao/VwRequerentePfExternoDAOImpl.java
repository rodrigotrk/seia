package br.gov.ba.seia.dao;

import java.text.Normalizer;
import java.util.Collection;

import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.VwRequerentePfExterno;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

public class VwRequerentePfExternoDAOImpl {

	@Inject
	IDAO<VwRequerentePfExterno> vwRequerentePfExternoDAO;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Collection<PessoaFisica> listarVwRequerentePfExterno(PessoaFisica pPessoaFisica)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(VwRequerentePfExterno.class);

		criteria.add(Restrictions.or(Restrictions.eq("idePessoa", pPessoaFisica.getPessoa().getIdePessoa()),
				Restrictions.eq("idePessoaFisica", pPessoaFisica.getPessoa().getIdePessoa())));
		if (!Util.isNullOuVazio(pPessoaFisica.getNomPessoa()))
			criteria.add(Restrictions.ilike("nomPessoa", pPessoaFisica.getNomPessoa(), MatchMode.ANYWHERE));
		if (!Util.isNullOuVazio(pPessoaFisica.getNumCpf()))
			criteria.add(Restrictions.eq("numCpf", pPessoaFisica.getNumCpf()));

		criteria.setProjection(
				Projections.projectionList().add(Projections.groupProperty("idePessoa"), "pessoa.idePessoa")
						.add(Projections.groupProperty("dtcCriacao"), "pessoa.dtcCriacao").add(Projections.groupProperty("indExcluido"), "pessoa.indExcluido")
						.add(Projections.groupProperty("desEmail"), "pessoa.desEmail").add(Projections.groupProperty("dtcExclusao"), "pessoa.dtcExclusao")
						.add(Projections.groupProperty("idePessoaFisica"), "idePessoaFisica")
						.add(Projections.groupProperty("ideEstadoCivil"), "ideEstadoCivil")
						.add(Projections.groupProperty("ideEscolaridade"), "ideEscolaridade").add(Projections.groupProperty("ideOcupacao"), "ideOcupacao")
						.add(Projections.groupProperty("idePais"), "idePais").add(Projections.groupProperty("nomPessoa"), "nomPessoa")
						.add(Projections.groupProperty("tipSexo"), "tipSexo").add(Projections.groupProperty("dtcNascimento"), "dtcNascimento")
						.add(Projections.groupProperty("desNaturalidade"), "desNaturalidade").add(Projections.groupProperty("nomPai"), "nomPai")
						.add(Projections.groupProperty("nomMae"), "nomMae").add(Projections.groupProperty("numCpf"), "numCpf")
						.add(Projections.groupProperty("ideEstado"), "ideEstado")).setResultTransformer(
				new AliasToNestedBeanResultTransformer(PessoaFisica.class));

		return Util.sigletonList((Collection<PessoaFisica>) (Collection) vwRequerentePfExternoDAO.listarPorCriteria(criteria));
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Collection<PessoaFisica> listarVwRequerentePfExternoSemAcento(PessoaFisica pPessoaFisica)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(VwRequerentePfExterno.class);

		criteria.add(Restrictions.or(Restrictions.eq("idePessoa", pPessoaFisica.getPessoa().getIdePessoa()),
				Restrictions.eq("idePessoaFisica", pPessoaFisica.getPessoa().getIdePessoa())));
		if (!Util.isNullOuVazio(pPessoaFisica.getNomPessoa())){
			String parametro = Normalizer.normalize(pPessoaFisica.getNomPessoa(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toUpperCase(); 
			criteria.add(Restrictions.sqlRestriction("TRANSLATE(Upper ({alias}.nom_pessoa),'ÂÃÀÁÉÈÊÍÕÓÒÔÚÇ','AAAAEEEIOOOOUC') LIKE  '%" + parametro + "%'")) ;
		}
		if (!Util.isNullOuVazio(pPessoaFisica.getNumCpf()))
			criteria.add(Restrictions.eq("numCpf", pPessoaFisica.getNumCpf()));

		criteria.setProjection(
				Projections.projectionList().add(Projections.groupProperty("idePessoa"), "pessoa.idePessoa")
						.add(Projections.groupProperty("dtcCriacao"), "pessoa.dtcCriacao").add(Projections.groupProperty("indExcluido"), "pessoa.indExcluido")
						.add(Projections.groupProperty("desEmail"), "pessoa.desEmail").add(Projections.groupProperty("dtcExclusao"), "pessoa.dtcExclusao")
						.add(Projections.groupProperty("idePessoaFisica"), "idePessoaFisica")
						.add(Projections.groupProperty("ideEstadoCivil"), "ideEstadoCivil")
						.add(Projections.groupProperty("ideEscolaridade"), "ideEscolaridade").add(Projections.groupProperty("ideOcupacao"), "ideOcupacao")
						.add(Projections.groupProperty("idePais"), "idePais").add(Projections.groupProperty("nomPessoa"), "nomPessoa")
						.add(Projections.groupProperty("tipSexo"), "tipSexo").add(Projections.groupProperty("dtcNascimento"), "dtcNascimento")
						.add(Projections.groupProperty("desNaturalidade"), "desNaturalidade").add(Projections.groupProperty("nomPai"), "nomPai")
						.add(Projections.groupProperty("nomMae"), "nomMae").add(Projections.groupProperty("numCpf"), "numCpf")
						.add(Projections.groupProperty("ideEstado"), "ideEstado")).setResultTransformer(
				new AliasToNestedBeanResultTransformer(PessoaFisica.class));

		return Util.sigletonList((Collection<PessoaFisica>) (Collection) vwRequerentePfExternoDAO.listarPorCriteria(criteria));
	}

}
