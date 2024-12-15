package br.gov.ba.seia.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.AgroClassificacaoDefensivo;
import br.gov.ba.seia.entity.AgroClassificacaoToxicologica;
import br.gov.ba.seia.entity.AgroDefensivoAgricola;
import br.gov.ba.seia.entity.ClassificacaoDefensivoAgricola;
import br.gov.ba.seia.entity.ClassificacaoToxicologica;
import br.gov.ba.seia.entity.FceAgrossilvopastoril;
import br.gov.ba.seia.entity.TipoDefensivoAgricola;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DefensivoAgricolaService {

	@Inject
	private IDAO<TipoDefensivoAgricola> tipoDefensivoAgricolaDAO;
	@Inject
	private IDAO<ClassificacaoDefensivoAgricola> classificacaoAgricolaDAO;
	@Inject
	private IDAO<ClassificacaoToxicologica> classificacaoToxicologicaDAO;
	@Inject
	private IDAO<AgroDefensivoAgricola> agroDefensivoAgricolaDAO;
	@Inject
	private IDAO<AgroClassificacaoToxicologica> agroClassificacaoToxicologicaDAO;
	@Inject
	private IDAO<AgroClassificacaoDefensivo> agroClassificacaoDefensivoDAO;



	//LISTAR ELEMENTOS QUE PREENCHEM AS CHECKBOXES NA TELA
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoDefensivoAgricola> listarDefensivoAgricola () {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoDefensivoAgricola.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		return tipoDefensivoAgricolaDAO.listarPorCriteria(criteria, Order.asc("ideTipoDefensivoAgricola"));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ClassificacaoDefensivoAgricola> listarClassificacaoDefensivoAgricola ()  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ClassificacaoDefensivoAgricola.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		return classificacaoAgricolaDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ClassificacaoToxicologica> listarClassificacaoToxicologica ()  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ClassificacaoToxicologica.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		return classificacaoToxicologicaDAO.listarPorCriteria(criteria, Order.asc("ideClassificacaoToxicologica"));
	}
	//FIM DO LISTAR ELEMENTOS QUE PREENCHEM AS CHECKBOXES NA TELA


	//BUSCAR NAS TABELA ASSOCIATIVAS
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AgroDefensivoAgricola> buscarTipoDefensivoAgricolaByIdeFceAgro (FceAgrossilvopastoril fceAgrossilvopastoril) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AgroDefensivoAgricola.class);
		criteria.add(Restrictions.eq("ideFceAgrossilvopastoril", fceAgrossilvopastoril));
		criteria.setFetchMode("ideFceAgrossilvopastoril", FetchMode.JOIN);
		criteria.setFetchMode("ideTipoDefensivoAgricola", FetchMode.JOIN);
		return agroDefensivoAgricolaDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AgroClassificacaoDefensivo> buscarClassificacaoDefensivoAgricolaByIdeFceAgro (FceAgrossilvopastoril fceAgrossilvopastoril) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AgroClassificacaoDefensivo.class);
		criteria.add(Restrictions.eq("ideFceAgrossilvopastoril", fceAgrossilvopastoril));
		criteria.setFetchMode("ideFceAgrossilvopastoril", FetchMode.JOIN);
		criteria.setFetchMode("ideClassificacaoDefensivoAgricola", FetchMode.JOIN);
		return agroClassificacaoDefensivoDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AgroClassificacaoToxicologica> buscarClassificacaoToxicologicaByIdeFceAgro (FceAgrossilvopastoril fceAgrossilvopastoril) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AgroClassificacaoToxicologica.class);
		criteria.add(Restrictions.eq("ideFceAgrossilvopastoril", fceAgrossilvopastoril));
		criteria.setFetchMode("ideClassificacaoToxicologica", FetchMode.JOIN);
		criteria.setFetchMode("ideFceAgrossilvopastoril", FetchMode.JOIN);
		return agroClassificacaoToxicologicaDAO.listarPorCriteria(criteria);
	}
	//FOM DO BUSCAR NAS TABELA ASSOCIATIVAS


	//LISTAR OS OBJETOS ASSOCIADOS DE CADA TIPO
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ClassificacaoToxicologica>  listarClassificacaoToxicologicaByIdeFceAgro(FceAgrossilvopastoril fceAgrossilvopastoril)  {
		List<ClassificacaoToxicologica> listaClassificacaoToxicologica = new ArrayList<ClassificacaoToxicologica>();
		List<AgroClassificacaoToxicologica> tmpAgroClassificacaoToxicologica = buscarClassificacaoToxicologicaByIdeFceAgro(fceAgrossilvopastoril);
		for (AgroClassificacaoToxicologica agroClassificacaoToxicologica : tmpAgroClassificacaoToxicologica){
			listaClassificacaoToxicologica.add(agroClassificacaoToxicologica.getIdeClassificacaoToxicologica());
		}
		return listaClassificacaoToxicologica;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoDefensivoAgricola> listarTipoDefensivoAgricolaByIdeFceAgro (FceAgrossilvopastoril fceAgrossilvopastoril) {
		List<TipoDefensivoAgricola> tipoDefensivoAgricola = new ArrayList<TipoDefensivoAgricola>();
		List<AgroDefensivoAgricola> tmpAgroDefensivoAgricola = buscarTipoDefensivoAgricolaByIdeFceAgro(fceAgrossilvopastoril);
		for (AgroDefensivoAgricola agroDefensivoAgricola : tmpAgroDefensivoAgricola){
			tipoDefensivoAgricola.add(agroDefensivoAgricola.getIdeTipoDefensivoAgricola());
		}
		return tipoDefensivoAgricola;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ClassificacaoDefensivoAgricola> listarClassificacaoDefensivoAgricolaByIdeFceAgro (FceAgrossilvopastoril fceAgrossilvopastoril) {
		List<ClassificacaoDefensivoAgricola> listaClassificacaoDefensivoAgricola = new ArrayList<ClassificacaoDefensivoAgricola>();
		List<AgroClassificacaoDefensivo> tmpAgroClassificacaoDefensivo = buscarClassificacaoDefensivoAgricolaByIdeFceAgro(fceAgrossilvopastoril);
		for(AgroClassificacaoDefensivo agroClassificacaoDefensivo : tmpAgroClassificacaoDefensivo){
			listaClassificacaoDefensivoAgricola.add(agroClassificacaoDefensivo.getIdeClassificacaoDefensivoAgricola());
		}
		return listaClassificacaoDefensivoAgricola;
	}
	//FIM DO LISTAR OS OBJETOS ASSOCIADOS DE CADA TIPO


	//SALVAR AS TABELAS ASSOCIATIVAS
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAgroDefensivoAgricola(AgroDefensivoAgricola agroDefensivoAgricola) {
		agroDefensivoAgricolaDAO.salvarOuAtualizar(agroDefensivoAgricola);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAgroClassificacaoToxicologica (AgroClassificacaoToxicologica agroClassificacaoToxicologica)  {
		agroClassificacaoToxicologicaDAO.salvarOuAtualizar(agroClassificacaoToxicologica);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAgroClassificacaoDefensivoAgricola(AgroClassificacaoDefensivo agroClassificacaoDefensivo)  {
		agroClassificacaoDefensivoDAO.salvarOuAtualizar(agroClassificacaoDefensivo);
	}
	//FIM DO SALVAR AS TABELAS ASSOCIATIVAS


	//EXCLUIR AS LIGAÇÕES
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirLigacaoClassificacaoToxicologica(FceAgrossilvopastoril fceAgrossilvopastoril)  {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideFceAgrossilvopastoril", fceAgrossilvopastoril);
		agroClassificacaoToxicologicaDAO.executarNamedQuery("AgroClassificacaoToxicologica.removerByIdeFceAgro", params);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirLigacaoDefensivosAgricolas(FceAgrossilvopastoril fceAgrossilvopastoril)  {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideFceAgrossilvopastoril", fceAgrossilvopastoril);
		agroDefensivoAgricolaDAO.executarNamedQuery("AgroDefensivoAgricola.removerByIdeFceAgro", params);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirLigacaoClassificacao(FceAgrossilvopastoril fceAgrossilvopastoril) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideFceAgrossilvopastoril", fceAgrossilvopastoril);
		agroClassificacaoDefensivoDAO.executarNamedQuery("AgroClassificacaoDefensivo.removerByIdeFceAgro", params);
	}
	//FIM DO EXCLUIR AS LIGAÇÕES
}
