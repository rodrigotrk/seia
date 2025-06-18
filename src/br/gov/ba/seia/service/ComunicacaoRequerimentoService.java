package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.ComunicacaoRequerimento;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoPessoa;
import br.gov.ba.seia.enumerator.TipoPessoaRequerimentoEnum;
import br.gov.ba.seia.interfaces.ComunicacaoRequerenteInterface;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ComunicacaoRequerimentoService {

	@Inject
	IDAO<ComunicacaoRequerimento> daoComunicacao;
	
	@Inject
	IDAO<ComunicacaoRequerenteInterface> comunicacaoRequerenteInterfaceDAO;
	
	@EJB
	RequerimentoService requerimentoService;

	@EJB
	RequerimentoPessoaService requerimentoPessoaService;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Requerimento requerimento, String assunto, String email) {
		ComunicacaoRequerimento comunicacao = new ComunicacaoRequerimento();
		comunicacao.setIdeRequerimento(requerimento);
		comunicacao.setAssunto(assunto);
		comunicacao.setIndEnviado(false);
		comunicacao.setDtcComunicacao(new Date());
		comunicacao.setDesMensagem(email);
		salvar(comunicacao);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<String> carregarListaEmails(Integer ideRequerimento) {
		List<String> emails = new ArrayList<String>();
		Requerimento requerimento = requerimentoService.carregarEmail(ideRequerimento);

		if (!Util.isNullOuVazio(requerimento.getDesEmail())) {
			emails.add(requerimento.getDesEmail());
		}

		Collection<RequerimentoPessoa> listaPessoas = requerimentoPessoaService.listarClientesDoRequerimento(ideRequerimento);
		
		if(!Util.isNullOuVazio(listaPessoas)) {
			for (RequerimentoPessoa rp : listaPessoas) {
				if(!Util.isNullOuVazio(rp) && !Util.isNullOuVazio(rp.getPessoa()) && !Util.isNullOuVazio(rp.getPessoa().getDesEmail())
						&& !emails.contains(rp.getPessoa().getDesEmail())) {
					
						emails.add(rp.getPessoa().getDesEmail());
					
				}
			}
		}
		
		return emails;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<String> carregarListaEmails(Requerimento req) {
		List<String> emails = new ArrayList<String>();
		Requerimento requerimento = requerimentoService.carregar(req);
		if (!Util.isNullOuVazio(requerimento.getDesEmail()))
			emails.add(requerimento.getDesEmail());

		for (RequerimentoPessoa rp : requerimento.getRequerimentoPessoaCollection()) {

			if (!Util.isNullOuVazio(rp.getPessoa().getDesEmail())
					&& !emails.contains(rp.getPessoa().getDesEmail())
					&& !rp.getIdeTipoPessoaRequerimento().getIdeTipoPessoaRequerimento()
							.equals(TipoPessoaRequerimentoEnum.ATENDENTE.getId())) {

				emails.add(rp.getPessoa().getDesEmail());

			}
		}
		return emails;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ComunicacaoRequerimento> listarEmailsPendendes(Integer ideRequerimento)  {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ComunicacaoRequerimento.class)
			.createAlias("ideRequerimento", "req")
			.add(Restrictions.eq("indEnviado", false))
			.add(Restrictions.eq("req.ideRequerimento", ideRequerimento))
			.setProjection(
					Projections.projectionList()
							.add(Projections.property("ideComunicacaoRequerimento"), "ideComunicacaoRequerimento")
							.add(Projections.property("desMensagem"), "desMensagem")
							.add(Projections.property("indEnviado"), "indEnviado")
							.add(Projections.property("assunto"), "assunto")
							.add(Projections.property("req.ideRequerimento"), "ideRequerimento.ideRequerimento"))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ComunicacaoRequerimento.class))
		;
		return daoComunicacao.listarPorCriteria(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ComunicacaoRequerimento buscarUltimaComunicacaoRequerimento(Integer ideRequerimento) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ComunicacaoRequerimento.class)
			.add(Property.forName("ideComunicacaoRequerimento").eq(
					DetachedCriteria.forClass(ComunicacaoRequerimento.class)
					.createAlias("ideRequerimento", "req")
					.add(Restrictions.eq("req.ideRequerimento", ideRequerimento))
					.setProjection(Projections.max("ideComunicacaoRequerimento"))
				)
			)
			.setProjection(
					Projections.projectionList()
					.add(Projections.property("ideComunicacaoRequerimento"), "ideComunicacaoRequerimento")
					.add(Projections.property("desMensagem"), "desMensagem")
					.add(Projections.property("indEnviado"), "indEnviado")
					.add(Projections.property("assunto"), "assunto")
					.add(Projections.property("ideRequerimento.ideRequerimento"), "ideRequerimento.ideRequerimento"))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ComunicacaoRequerimento.class))
		;
		return daoComunicacao.buscarPorCriteria(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)	
	public void salvar(ComunicacaoRequerimento comunicacaoReq) {
		daoComunicacao.salvar(comunicacaoReq);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)	
	public void atualizar(ComunicacaoRequerimento comunicacaoRequerimento) {
		daoComunicacao.atualizar(comunicacaoRequerimento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)	
	public void atualizarStatusEnvioComunicacao(ComunicacaoRequerimento comunicacaoRequerimento) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideComunicacaoRequerimento", comunicacaoRequerimento.getIdeComunicacaoRequerimento());
		daoComunicacao
				.executarNativeQuery(
						"update comunicacao_requerimento set ind_enviado = true where ide_comunicacao_requerimento = :ideComunicacaoRequerimento",
						parametros);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ComunicacaoRequerenteInterface> carregarComunicacaoDemanda(Integer ideRequerimento, int startPage, int maxPage) {
		DetachedCriteria criteria = criteriaCarregaComunicacao(ideRequerimento)
			.addOrder(Order.desc("ideComunicacaoRequerimento"))
		;
		return comunicacaoRequerenteInterfaceDAO.listarPorCriteriaDemanda(criteria, startPage, maxPage);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer carregarComunicacaoCount(Integer ideRequerimento) {
		DetachedCriteria criteria = criteriaCarregaComunicacao(ideRequerimento);
		return daoComunicacao.count(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ComunicacaoRequerimento> carregarComunicacaoDescendente(Integer ideRequerimento) {
		return daoComunicacao.listarPorCriteria(criteriaCarregaComunicacao(ideRequerimento), Order.desc("ideComunicacaoRequerimento"));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ComunicacaoRequerimento> obterComunicacaoRequerimento(Integer ideRequerimento){
		return daoComunicacao.listarPorCriteria(criteriaCarregaComunicacao(ideRequerimento));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private DetachedCriteria criteriaCarregaComunicacao(Integer ideRequerimento){
		return  DetachedCriteria.forClass(ComunicacaoRequerimento.class)
				.createAlias("ideRequerimento", "req")
				.setProjection(
						Projections.projectionList()
								.add(Projections.property("ideComunicacaoRequerimento"), "ideComunicacaoRequerimento")
								.add(Projections.property("desMensagem"), "desMensagem")
								.add(Projections.property("indEnviado"), "indEnviado")
								.add(Projections.property("dtcComunicacao"), "dtcComunicacao")
								.add(Projections.property("assunto"), "assunto")
								.add(Projections.property("req.ideRequerimento"), "ideRequerimento.ideRequerimento"))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(ComunicacaoRequerimento.class))
				.add(Restrictions.eq("req.ideRequerimento", ideRequerimento));
	}

}
