package br.gov.ba.seia.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dto.CumprimentoReposicaoFlorestalDTO;
import br.gov.ba.seia.dto.DetalhamentoDaeDTO;
import br.gov.ba.seia.dto.GerarDaeDTO;
import br.gov.ba.seia.dto.MemoriaCalculoDTO;
import br.gov.ba.seia.dto.UnidadeMedidaCalculoDTO;
import br.gov.ba.seia.entity.ConsumidorReposicaoFlorestal;
import br.gov.ba.seia.entity.CumprimentoReposicaoFlorestal;
import br.gov.ba.seia.entity.Dae;
import br.gov.ba.seia.entity.DetentorReposicaoFlorestal;
import br.gov.ba.seia.entity.DevedorReposicaoFlorestal;
import br.gov.ba.seia.entity.MemoriaCalculoDae;
import br.gov.ba.seia.entity.MemoriaCalculoDaeParcela;
import br.gov.ba.seia.entity.Orgao;
import br.gov.ba.seia.entity.PagamentoReposicaoFlorestal;
import br.gov.ba.seia.entity.ParametroCalculo;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcuradorPessoaFisica;
import br.gov.ba.seia.entity.ProcuradorRepresentante;
import br.gov.ba.seia.entity.RepresentanteLegal;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoPessoa;
import br.gov.ba.seia.entity.Telefone;
import br.gov.ba.seia.entity.TipoPessoaRequerimento;
import br.gov.ba.seia.entity.TipoRequerimento;
import br.gov.ba.seia.entity.UnidadeMedida;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.OrgaoEmissorAutoEnum;
import br.gov.ba.seia.enumerator.OrgaoEnum;
import br.gov.ba.seia.enumerator.PagamentoReposicaoFlorestalEnum;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.enumerator.StatusProcessoAtoEnum;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.enumerator.TipoPessoaRequerimentoEnum;
import br.gov.ba.seia.enumerator.TipoRequerimentoEnum;
import br.gov.ba.seia.enumerator.UnidadeMedidaEnum;
import br.gov.ba.seia.facade.FinalizarRequerimentoServiceFacade;
import br.gov.ba.seia.facade.TipoSolicitacaoServiceFacade;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.CalculoFlorestalUtil;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.PessoaUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CumprimentoReposicaoFlorestalService {
	
	@EJB
	private TelefoneService telefoneService;
	
	@EJB
	private TipoSolicitacaoServiceFacade tipoSolicitacaoServiceFacade;
	
	@EJB
	private DeclaracaoTransporteService declaracaoTransporteService;
	
	@EJB
	private FinalizarRequerimentoServiceFacade finalizarRequerimentoServiceFacade;
	
	@EJB
	private ProcuradorPessoaFisicaService procuradorPessoaFisicaService;
	
	@EJB
	private ProcuradorRepresentanteService procuradorRepresentanteService;
	
	@EJB
	private RepresentanteLegalService representanteLegalService;
	
	@EJB
	private ParametroCalculoService parametroCalculoService;
	
	@EJB
	private ProcessoService processoService;
	
	@EJB
	private DetalhamentoDaeCrfService detalhamentoDaeCrfService;
	
	@EJB
	private DetentorReposicaoFlorestalService detentorReposicaoFlorestalService;
	
	@EJB
	private ConsumidorReposicaoFlorestalService consumidorReposicaoFlorestalService;
	
	@EJB
	private DevedorReposicaoFlorestalService devedorReposicaoFlorestalService;

	@EJB
	private RequerimentoService requerimentoService;
	
	@EJB
	private RequerimentoUnicoService requerimentoUnicoService;
	
	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;
	
	@Inject
	private IDAO<CumprimentoReposicaoFlorestal> cumprimentoReposicaoFlorestalDAO;
	
	@Inject
	private IDAO<DetentorReposicaoFlorestal> detentorReposicaoFlorestalDAO;
	
	@Inject
	private IDAO<ConsumidorReposicaoFlorestal> comsumidorReposicaoFlorestalDAO;
	
	@Inject
	private IDAO<DevedorReposicaoFlorestal> devedorReposicaoFlorestalDAO;
	
	@Inject
	private IDAO<MemoriaCalculoDaeParcela> memoriaCalculoDaeParcelaIDAO;
	
	@Inject
	private IDAO<MemoriaCalculoDae> memoriaCalculoDaeIDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(CumprimentoReposicaoFlorestal cumprimentoReposicaoFlorestal) {
		cumprimentoReposicaoFlorestalDAO.salvarOuAtualizar(cumprimentoReposicaoFlorestal);
	}
	
	public boolean isDetentor(CumprimentoReposicaoFlorestalDTO cumprimentoReposicaoFlorestalDTO) {
		return !Util.isNullOuVazio(cumprimentoReposicaoFlorestalDTO.getPagamentoReposicaoFlorestal()) 
				&& PagamentoReposicaoFlorestalEnum.DETENTOR.getId().equals(cumprimentoReposicaoFlorestalDTO.getPagamentoReposicaoFlorestal().getIdePagamentoReposicaoFlorestal());
	}
	
	public boolean isConsumidor(CumprimentoReposicaoFlorestalDTO cumprimentoReposicaoFlorestalDTO) {
		return !Util.isNullOuVazio(cumprimentoReposicaoFlorestalDTO.getPagamentoReposicaoFlorestal()) 
				&& PagamentoReposicaoFlorestalEnum.CONSUMIDOR.getId().equals(cumprimentoReposicaoFlorestalDTO.getPagamentoReposicaoFlorestal().getIdePagamentoReposicaoFlorestal());
	}
	
	public boolean isDevedor(CumprimentoReposicaoFlorestalDTO cumprimentoReposicaoFlorestalDTO) {
		return !Util.isNullOuVazio(cumprimentoReposicaoFlorestalDTO.getPagamentoReposicaoFlorestal()) 
				&& PagamentoReposicaoFlorestalEnum.DEVEDOR.getId().equals(cumprimentoReposicaoFlorestalDTO.getPagamentoReposicaoFlorestal().getIdePagamentoReposicaoFlorestal());
	}
	
	public boolean isASV(CumprimentoReposicaoFlorestalDTO cumprimentoReposicaoFlorestalDTO) {
		return !Util.isNullOuVazio(cumprimentoReposicaoFlorestalDTO.getPagamentoReposicaoFlorestalFilho()) 
				&& PagamentoReposicaoFlorestalEnum.ASV.getId().equals(cumprimentoReposicaoFlorestalDTO.getPagamentoReposicaoFlorestalFilho().getIdePagamentoReposicaoFlorestal());
	}
	
	public boolean isAML(CumprimentoReposicaoFlorestalDTO cumprimentoReposicaoFlorestalDTO) {
		return !Util.isNullOuVazio(cumprimentoReposicaoFlorestalDTO.getPagamentoReposicaoFlorestalFilho()) 
				&& PagamentoReposicaoFlorestalEnum.AML.getId().equals(cumprimentoReposicaoFlorestalDTO.getPagamentoReposicaoFlorestalFilho().getIdePagamentoReposicaoFlorestal());
	}
	
	public boolean isReconhecimentoFlorestalRemanescente(CumprimentoReposicaoFlorestalDTO cumprimentoReposicaoFlorestalDTO) {
		return !Util.isNullOuVazio(cumprimentoReposicaoFlorestalDTO.getPagamentoReposicaoFlorestalFilho()) 
				&& PagamentoReposicaoFlorestalEnum.RECONHECIMENTO_FLORESTAL_REMANESCENTE.getId().equals(cumprimentoReposicaoFlorestalDTO.getPagamentoReposicaoFlorestalFilho().getIdePagamentoReposicaoFlorestal());
	}
	
	public boolean isSecretariaMunicipal(CumprimentoReposicaoFlorestalDTO cumprimentoReposicaoFlorestalDTO) {
		return !Util.isNullOuVazio(cumprimentoReposicaoFlorestalDTO.getDevedorReposicaoFlorestal()) 
				&& !Util.isNullOuVazio(cumprimentoReposicaoFlorestalDTO.getDevedorReposicaoFlorestal().getIdeOrgaoEmissorAuto()) 
				&& OrgaoEmissorAutoEnum.SECRETARIA_MUNICIPAL.getId().equals(cumprimentoReposicaoFlorestalDTO.getDevedorReposicaoFlorestal().getIdeOrgaoEmissorAuto().getIdeOrgaoEmissorAuto());
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizar(CumprimentoReposicaoFlorestalDTO cumprimentoReposicaoFlorestalDTO, Requerimento requerimento) throws Exception {
		
		CumprimentoReposicaoFlorestal cumprimentoReposicaoFlorestal = cumprimentoReposicaoFlorestalDTO.getCumprimentoReposicaoFlorestal();
		
		if (Util.isNullOuVazio(cumprimentoReposicaoFlorestal)) {
			cumprimentoReposicaoFlorestal = new CumprimentoReposicaoFlorestal();
		}
		
		PagamentoReposicaoFlorestal pagamentoReposicaoFlorestal = cumprimentoReposicaoFlorestalDTO.getPagamentoReposicaoFlorestal();
		
		if (!Util.isNullOuVazio(cumprimentoReposicaoFlorestalDTO.getPagamentoReposicaoFlorestalFilho())) {
			pagamentoReposicaoFlorestal = cumprimentoReposicaoFlorestalDTO.getPagamentoReposicaoFlorestalFilho();
		}
		
		cumprimentoReposicaoFlorestal.setIdePagamentoReposicaoFlorestal(pagamentoReposicaoFlorestal);
		cumprimentoReposicaoFlorestal.setIndCiente(cumprimentoReposicaoFlorestalDTO.getIndCiente());
		cumprimentoReposicaoFlorestal.setRequerimento(cumprimentoReposicaoFlorestalDTO.getRequerimento());
		cumprimentoReposicaoFlorestal.setVlrPecuniario(BigDecimal.valueOf(cumprimentoReposicaoFlorestalDTO.getValorPecuniario()));
		
		salvarOuAtualizar(cumprimentoReposicaoFlorestal);
		
		/*
		 * Remove os dados que não estão sendo cadastrado ou editados
		 */
		removerDadosReposicaoFlorestala(cumprimentoReposicaoFlorestalDTO);
				
		if (isDetentor(cumprimentoReposicaoFlorestalDTO)) {
			cumprimentoReposicaoFlorestalDTO.getDetentorReposicaoFlorestal().setIdeCumprimentoReposicaoFlorestal(cumprimentoReposicaoFlorestal);
			detentorReposicaoFlorestalDAO.salvarOuAtualizar(cumprimentoReposicaoFlorestalDTO.getDetentorReposicaoFlorestal());
		}
		else if (isConsumidor(cumprimentoReposicaoFlorestalDTO)) {
			ConsumidorReposicaoFlorestal consumidorReposicaoFloresta = cumprimentoReposicaoFlorestalDTO.getConsumidorReposicaoFlorestal();
			consumidorReposicaoFloresta.setIdeCumprimentoReposicaoFlorestal(cumprimentoReposicaoFlorestal);
			consumidorReposicaoFloresta.setIndPossuiNumeroCAR(false);
			comsumidorReposicaoFlorestalDAO.salvarOuAtualizar(consumidorReposicaoFloresta);
			
		}
		else if (isDevedor(cumprimentoReposicaoFlorestalDTO)) {
			DevedorReposicaoFlorestal devedorReposicaoFlorestal =  cumprimentoReposicaoFlorestalDTO.getDevedorReposicaoFlorestal();
			devedorReposicaoFlorestal.setIdeCumprimentoReposicaoFlorestal(cumprimentoReposicaoFlorestal);
			
			if (!isSecretariaMunicipal(cumprimentoReposicaoFlorestalDTO)) {
				devedorReposicaoFlorestal.setIdeMunicipio(null);
			}
			devedorReposicaoFlorestalDAO.salvarOuAtualizar(devedorReposicaoFlorestal);
		}
			
		finalizarRequerimentoServiceFacade.finalizarRequerimentoCRF(cumprimentoReposicaoFlorestalDTO);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void removerDadosReposicaoFlorestala(CumprimentoReposicaoFlorestalDTO cumprimentoReposicaoFlorestalDTO) throws Exception {
		if (!Util.isNullOuVazio(cumprimentoReposicaoFlorestalDTO.getPagamentoReposicaoFlorestalAnterior())) {
			
			PagamentoReposicaoFlorestal pagamentoReposicaoFlorestal =  cumprimentoReposicaoFlorestalDTO.getPagamentoReposicaoFlorestalAnterior();
			
			if (!Util.isNullOuVazio(pagamentoReposicaoFlorestal.getIdePagamentoReposicaoFlorestalPai())) {
				pagamentoReposicaoFlorestal = pagamentoReposicaoFlorestal.getIdePagamentoReposicaoFlorestalPai();
			}
			
			PagamentoReposicaoFlorestalEnum pagamentoReposicaoFlorestalEnum = PagamentoReposicaoFlorestalEnum.getEnum(pagamentoReposicaoFlorestal);
			
			switch (pagamentoReposicaoFlorestalEnum) {
				case DETENTOR:
					DetentorReposicaoFlorestal detentorReposicaoFlorestal = detentorReposicaoFlorestalService.obterDetentorReposicaoFlorestalPorCumprimentoReposicaoFlorestal(cumprimentoReposicaoFlorestalDTO.getCumprimentoReposicaoFlorestal().getIdeCumprimentoReposicaoFlorestal());
					if (!Util.isNullOuVazio(detentorReposicaoFlorestal)) {
						detentorReposicaoFlorestalDAO.remover(detentorReposicaoFlorestal);
					}
					break;
				case CONSUMIDOR:
					ConsumidorReposicaoFlorestal consumidorReposicaoFlorestal = consumidorReposicaoFlorestalService.obterConsumidorReposicaoFlorestalPorCumprimentoReposicaoFlorestal(cumprimentoReposicaoFlorestalDTO.getCumprimentoReposicaoFlorestal().getIdeCumprimentoReposicaoFlorestal());
					if (!Util.isNullOuVazio(consumidorReposicaoFlorestal)) {
						comsumidorReposicaoFlorestalDAO.remover(consumidorReposicaoFlorestal);
					}
					break;
				case DEVEDOR:
					DevedorReposicaoFlorestal devedorReposicaoFlorestal = devedorReposicaoFlorestalService.obterDevedorReposicaoFlorestalCumprimentoReposicaoFlorestal(cumprimentoReposicaoFlorestalDTO.getCumprimentoReposicaoFlorestal().getIdeCumprimentoReposicaoFlorestal());
					if (!Util.isNullOuVazio(devedorReposicaoFlorestal)) {
						devedorReposicaoFlorestalDAO.remover(devedorReposicaoFlorestal);
					}
					break;
			}
		}
		
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Requerimento salvarNovoRequerimentoCRF(Pessoa pessoaRequerente) throws Exception {
		Pessoa requerente = pessoaRequerente;
		
		Requerimento requerimento = new Requerimento();
		requerimento.setDtcCriacao(new Date());
		requerimento.setIdeTipoRequerimento(new TipoRequerimento(TipoRequerimentoEnum.REQUERIMENTO_ATO_DECLARATORIO));
		//INEMA
		requerimento.setIdeOrgao(new Orgao(OrgaoEnum.INEMA.getId()));
		requerimento.setIndExcluido(false);
		
		List<Telefone> listaTel = null;

		try {
			listaTel = telefoneService.buscarTelefonesPorPessoa(requerente);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		if (!Util.isNullOuVazio(requerente.getPessoaFisica()) && !Util.isNullOuVazio(requerente.getPessoaFisica().getNomPessoa()) && Util.isNullOuVazio(requerimento.getNomContato())) {
			requerimento.setNomContato(requerente.getPessoaFisica().getNomPessoa());

			if (!Util.isNullOuVazio(requerente.getPessoaFisica().getPessoa().getDesEmail()) && Util.isNullOuVazio(requerimento.getDesEmail())) {
				requerimento.setDesEmail(requerente.getPessoaFisica().getPessoa().getDesEmail());
			}
		} else if (!Util.isNullOuVazio(requerente.getPessoaJuridica()) && Util.isNullOuVazio(requerimento.getNomContato())) {

			requerimento.setNomContato(requerente.getPessoaJuridica().getNomRazaoSocial());

			Integer tipoSolicit = ContextoUtil.getContexto().getTipoSolicitante();
			ContextoUtil.getContexto().setTipoSolicitante(null);

			if (Util.isNullOuVazio(tipoSolicit) || tipoSolicit.equals(1)) {
				PessoaJuridica pJuridica = ContextoUtil.getContexto().getSolicitanteRequerimento().getPessoaJuridica();

				if(!Util.isNullOuVazio(pJuridica) && !Util.isNullOuVazio(pJuridica.getPessoa()) && !Util.isNullOuVazio(pJuridica.getPessoa().getDesEmail())) {
					requerimento.setDesEmail(pJuridica.getPessoa().getDesEmail());
				}
			} else if (tipoSolicit.equals(4) || tipoSolicit.equals(2)) {

				if (!Util.isNullOuVazio(ContextoUtil.getContexto().getSolicitanteRequerimento().getPessoaFisica())) {
					requerimento.setDesEmail(ContextoUtil.getContexto().getSolicitanteRequerimento().getPessoaFisica().getPessoa().getDesEmail());
				} else {
					requerimento.setDesEmail(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa().getDesEmail());
				}
			}
		}
		
		if (listaTel!=null && !listaTel.isEmpty() && Util.isNullOuVazio(requerimento.getNumTelefone())) {
			try {
				if (listaTel.size() > 1) {
					requerimento.setNumTelefone(PessoaUtil.getTelefoneParaRequerimento(listaTel).getNumTelefone());
				} else {
					requerimento.setNumTelefone(listaTel.get(0).getNumTelefone());
				}
			} catch (Exception e) {
				requerimento.setNumTelefone(listaTel.get(0).getNumTelefone());
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
		}
		
		gerarRequerimentoPessoaCollection(requerimento);
		
		gerarRequerimento(requerimento, requerente);
		
		return requerimento;
	}

	private void gerarRequerimentoPessoaCollection(Requerimento requerimento) throws Exception {
		try {

			List<RequerimentoPessoa> pessoasRequerimentos = new ArrayList<RequerimentoPessoa>();
			RequerimentoPessoa reqPessoaAtendente = ContextoUtil.getContexto().getReqPapeisDTO().getAtendente();
			RequerimentoPessoa reqPessoaRequerente = ContextoUtil.getContexto().getReqPapeisDTO().getRequerente();
			RequerimentoPessoa reqPessoaSolicitante = ContextoUtil.getContexto().getReqPapeisDTO().getSolicitante();

			pessoasRequerimentos.add(reqPessoaRequerente);

			if (!reqPessoaRequerente.isIndSolicitante()) {
				pessoasRequerimentos.add(reqPessoaSolicitante);
			}

			if (!reqPessoaRequerente.isIndUsuarioLogado() && !reqPessoaSolicitante.isIndUsuarioLogado()) {
				pessoasRequerimentos.add(reqPessoaAtendente);
			}

			
			if(!Util.isNull(reqPessoaRequerente.getPessoa().getPessoaFisica()) && !Util.isNull(reqPessoaRequerente.getPessoa().getPessoaFisica().getIdePessoaFisica())) {
				
				ProcuradorPessoaFisica donoProcurador = new ProcuradorPessoaFisica();
				donoProcurador.setIdePessoaFisica(reqPessoaRequerente.getPessoa().getPessoaFisica());
			
				List<ProcuradorPessoaFisica> listaProcuradorPessoaFisica = (List<ProcuradorPessoaFisica>) procuradorPessoaFisicaService.listarProcuradorPessoaFisica(donoProcurador);
				for (ProcuradorPessoaFisica procuradorPessoaFisica : listaProcuradorPessoaFisica) {
					RequerimentoPessoa requerimentoPessoa = new RequerimentoPessoa();
					requerimentoPessoa.setPessoa(procuradorPessoaFisica.getIdeProcurador().getPessoa());
					TipoPessoaRequerimento tipoPessoaRequerimento = new TipoPessoaRequerimento();
					tipoPessoaRequerimento.setIdeTipoPessoaRequerimento(TipoPessoaRequerimentoEnum.PROCURADOR.getId());
					requerimentoPessoa.setIdeTipoPessoaRequerimento(tipoPessoaRequerimento);
					pessoasRequerimentos.add(requerimentoPessoa);
				}
			}
			
			ProcuradorRepresentante procuradoRepresentante = new ProcuradorRepresentante();
			procuradoRepresentante.setIdePessoaJuridica(new PessoaJuridica());
			procuradoRepresentante.getIdePessoaJuridica().setIdePessoaJuridica(reqPessoaRequerente.getPessoa().getIdePessoa());
			
			List<ProcuradorRepresentante> listaProcuradorRepresentante = (List<ProcuradorRepresentante>) procuradorRepresentanteService.getListaProcuradorRepresentante(procuradoRepresentante);
			for (ProcuradorRepresentante procuradorRepresentante : listaProcuradorRepresentante) {
				RequerimentoPessoa requerimentoPessoa = new RequerimentoPessoa();
				requerimentoPessoa.setPessoa(procuradorRepresentante.getIdeProcurador().getPessoa());
				TipoPessoaRequerimento tipoPessoaRequerimento = new TipoPessoaRequerimento();
				tipoPessoaRequerimento.setIdeTipoPessoaRequerimento(TipoPessoaRequerimentoEnum.PROCURADOR.getId());
				requerimentoPessoa.setIdeTipoPessoaRequerimento(tipoPessoaRequerimento);

				boolean temPessoaRequerimento = false;

				for (RequerimentoPessoa reqPes : pessoasRequerimentos) {
					if (reqPes.getIdeTipoPessoaRequerimento().equals(requerimentoPessoa.getIdeTipoPessoaRequerimento())
							&& reqPes.getPessoa().equals(requerimentoPessoa.getPessoa())) {
						temPessoaRequerimento = true;
						break;
					}
				}

				if (!temPessoaRequerimento) {
					pessoasRequerimentos.add(requerimentoPessoa);
				}
			}

			PessoaJuridica pessoaJuridica = new PessoaJuridica(reqPessoaRequerente.getPessoa().getIdePessoa());
			
			List<RepresentanteLegal> collRepresentanteLegal = representanteLegalService.getListaRepresentanteLegalByPessoa(pessoaJuridica);
			
			for (RepresentanteLegal representantelegal : collRepresentanteLegal) {

				RequerimentoPessoa requerimentoPessoa = new RequerimentoPessoa();
				requerimentoPessoa.setPessoa(representantelegal.getIdePessoaFisica().getPessoa());
				TipoPessoaRequerimento tipoPessoaRequerimento = new TipoPessoaRequerimento();
				tipoPessoaRequerimento.setIdeTipoPessoaRequerimento(TipoPessoaRequerimentoEnum.REPRESENTANTE_LEGAL.getId());
				requerimentoPessoa.setIdeTipoPessoaRequerimento(tipoPessoaRequerimento);

				boolean temPessoaRequerimento = false;

				for (RequerimentoPessoa reqPes : pessoasRequerimentos) {
					if (reqPes.getIdeTipoPessoaRequerimento().equals(requerimentoPessoa.getIdeTipoPessoaRequerimento())
							&& reqPes.getPessoa().equals(requerimentoPessoa.getPessoa())) {

						temPessoaRequerimento = true;
						break;
					}
				}

				if (!temPessoaRequerimento) {
					pessoasRequerimentos.add(requerimentoPessoa);
				}
			}

			for (RequerimentoPessoa reqPess : pessoasRequerimentos) {
				reqPess.setRequerimento(requerimento);
			}

			requerimento.setRequerimentoPessoaCollection(pessoasRequerimentos);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Erro ao associar a pessoa ao requerimento.");
			throw e;
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void gerarRequerimento(Requerimento requerimento, Pessoa requerente) {
		try {
			Collection<RequerimentoPessoa> collRequerimentoPessoa = requerimento.getRequerimentoPessoaCollection();
			requerimento.setRequerimentoPessoaCollection(new ArrayList<RequerimentoPessoa>());
			
			requerimentoService.inserirRequerimento(requerimento);
			requerimentoUnicoService.salvarPessoasRequerimento(requerimento, collRequerimentoPessoa);
			tramitacaoRequerimentoService.tramitar(requerimento, StatusRequerimentoEnum.REQUERIMENTO_INCOMPLETO, requerente);
		} catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CumprimentoReposicaoFlorestal obterCumprimentoReposicaoFlorestalPorRequerimento(Requerimento requerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CumprimentoReposicaoFlorestal.class);
		
		criteria.createAlias("requerimento", "req", JoinType.INNER_JOIN);
		criteria.createAlias("idePagamentoReposicaoFlorestal", "prf", JoinType.INNER_JOIN);
		criteria.createAlias("prf.idePagamentoReposicaoFlorestalPai", "prfp", JoinType.LEFT_OUTER_JOIN);
		
		criteria.add(Restrictions.eq("req.ideRequerimento", requerimento.getIdeRequerimento()));
		
		criteria.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideCumprimentoReposicaoFlorestal"), "ideCumprimentoReposicaoFlorestal")
					.add(Projections.property("req.ideRequerimento"), "requerimento.ideRequerimento")
					.add(Projections.property("prf.idePagamentoReposicaoFlorestal"), "idePagamentoReposicaoFlorestal.idePagamentoReposicaoFlorestal")
					.add(Projections.property("prf.nomPagamentoReposicaoFlorestal"), "idePagamentoReposicaoFlorestal.nomPagamentoReposicaoFlorestal")
					.add(Projections.property("prfp.idePagamentoReposicaoFlorestal"), "idePagamentoReposicaoFlorestal.idePagamentoReposicaoFlorestalPai.idePagamentoReposicaoFlorestal")
					.add(Projections.property("prfp.nomPagamentoReposicaoFlorestal"), "idePagamentoReposicaoFlorestal.idePagamentoReposicaoFlorestalPai.nomPagamentoReposicaoFlorestal")
					.add(Projections.property("indCiente"), "indCiente")
					.add(Projections.property("vlrPecuniario"), "vlrPecuniario")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(CumprimentoReposicaoFlorestal.class));
		
		return cumprimentoReposicaoFlorestalDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CumprimentoReposicaoFlorestal obterCumprimentoReposicaoFlorestalPorDAE(Dae dae) {
		DetachedCriteria criteria = DetachedCriteria.forClass(MemoriaCalculoDaeParcela.class);
		
		criteria.createAlias("ideMemoriaCalculoDae", "mcd", JoinType.INNER_JOIN);
		
		criteria.add(Restrictions.eq("ideDae.ideDae", dae.getIdeDae()));
		
		criteria.setProjection(
				Projections.projectionList()
					.add(Projections.property("mcd.ideMemoriaCalculoDae"), "ideMemoriaCalculoDae.ideMemoriaCalculoDae")
					.add(Projections.property("mcd.ideCumprimentoReposicaoFlorestal"), "ideMemoriaCalculoDae.ideCumprimentoReposicaoFlorestal")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(MemoriaCalculoDaeParcela.class));
		
		
		MemoriaCalculoDaeParcela memoria = memoriaCalculoDaeParcelaIDAO.buscarPorCriteria(criteria);
		
		CumprimentoReposicaoFlorestal florestal = new CumprimentoReposicaoFlorestal();
		
		if(!Util.isNullOuVazio(memoria) && !Util.isNullOuVazio(memoria.getIdeMemoriaCalculoDae())){
			
				
				DetachedCriteria criteriaRepo = DetachedCriteria.forClass(CumprimentoReposicaoFlorestal.class)
						.createAlias("requerimento", "req", JoinType.INNER_JOIN);
				
				criteriaRepo.add(Restrictions.eq("ideCumprimentoReposicaoFlorestal", memoria.getIdeMemoriaCalculoDae().getIdeCumprimentoReposicaoFlorestal().getIdeCumprimentoReposicaoFlorestal()));
				
				criteriaRepo.setProjection(
						Projections.projectionList()
						.add(Projections.property("ideCumprimentoReposicaoFlorestal"), "ideCumprimentoReposicaoFlorestal")
						.add(Projections.property("req.ideRequerimento"), "requerimento.ideRequerimento")
						).setResultTransformer(new AliasToNestedBeanResultTransformer(CumprimentoReposicaoFlorestal.class));
				
				
				florestal = cumprimentoReposicaoFlorestalDAO.buscarPorCriteria(criteriaRepo);
			
		}
		
		return florestal;
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Double calcularM3(UnidadeMedida unidadeMedida, BigDecimal valor) {
		if (!Util.isNullOuVazio(unidadeMedida) && !Util.isNullOuVazio(valor)) {
			if (UnidadeMedidaEnum.MDC.getId().equals(unidadeMedida.getIdeUnidadeMedida())) {
				return CalculoFlorestalUtil.converterMDCParaM3(valor.doubleValue());
			} 
			else if (UnidadeMedidaEnum.ST.getId().equals(unidadeMedida.getIdeUnidadeMedida())) {
				return CalculoFlorestalUtil.converterEstereoParaM3(valor.doubleValue());
			}
			return valor.doubleValue();
		}
		return 0.0;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Double calcularValorPecuniario(CumprimentoReposicaoFlorestalDTO cumprimentoReposicaoFlorestalDTO) {
		Collection<ParametroCalculo> listaParametroCalculo = parametroCalculoService.listarParametrosDAE(AtoAmbientalEnum.CRF.getId());
		
		Double valorPecuniario = 0.0;
		
		for (ParametroCalculo parametroCalculo : listaParametroCalculo) {
			if (isDevedor(cumprimentoReposicaoFlorestalDTO)) {
				BigDecimal vlrAreaSuprimida  = cumprimentoReposicaoFlorestalDTO.getDevedorReposicaoFlorestal().getVlrAreaSuprimida();
				
				if (!Util.isNullOuVazio(cumprimentoReposicaoFlorestalDTO.getDevedorReposicaoFlorestal().getIdeBioma()) && !Util.isNullOuVazio(vlrAreaSuprimida)) {
					BigDecimal valorBioma = cumprimentoReposicaoFlorestalDTO.getDevedorReposicaoFlorestal().getIdeBioma().getMetrosCubicos();
					valorPecuniario = vlrAreaSuprimida.multiply(valorBioma).multiply(parametroCalculo.getValorTaxa()).doubleValue();
				}
			} else {
				BigDecimal volume = BigDecimal.ZERO;
				
				if (isDetentor(cumprimentoReposicaoFlorestalDTO)) {
					volume = cumprimentoReposicaoFlorestalDTO.getDetentorReposicaoFlorestal().getVolumeAutorizado();
					volume =  BigDecimal.valueOf(calcularM3(cumprimentoReposicaoFlorestalDTO.getDetentorReposicaoFlorestal().getIdeUnidadeMedida(), volume));
				}
				else if (isConsumidor(cumprimentoReposicaoFlorestalDTO)) {
					volume = cumprimentoReposicaoFlorestalDTO.getConsumidorReposicaoFlorestal().getVlmMaterialLenhosoConsumido();
					volume = BigDecimal.valueOf(calcularM3( cumprimentoReposicaoFlorestalDTO.getConsumidorReposicaoFlorestal().getIdeUnidadeMedida(), volume));
				}
				
				if (!Util.isNullOuVazio(volume)){
					valorPecuniario = volume.multiply(parametroCalculo.getValorTaxa()).doubleValue();
				}
			}
		}
		
		
		return valorPecuniario;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void verificarProcessoSEIAPorAtoAmbiental(CumprimentoReposicaoFlorestalDTO cumprimentoReposicaoFlorestalDTO, String tipoAtoRelacionado, String numProcessoAsvAML) {
		String numProcesso = "";
		AtoAmbientalEnum atoAmbientalEnum = null;
		if (isDetentor(cumprimentoReposicaoFlorestalDTO)) {
			numProcesso = cumprimentoReposicaoFlorestalDTO.getDetentorReposicaoFlorestal().getNumProcesso();
			
			if (isASV(cumprimentoReposicaoFlorestalDTO)) {
				atoAmbientalEnum = AtoAmbientalEnum.ASV;
			}
			else if (isAML(cumprimentoReposicaoFlorestalDTO)) {
				atoAmbientalEnum = AtoAmbientalEnum.AML;
			}
			else {
				atoAmbientalEnum = AtoAmbientalEnum.RVFR;
			}
			
			if (!Util.isNullOuVazio(numProcessoAsvAML)) {
				numProcesso = numProcessoAsvAML.replace(String.valueOf((char) 160), " ").trim();
			}
			else if (!Util.isNullOuVazio(numProcesso)) {
				numProcesso = numProcesso.replace(String.valueOf((char) 160), " ").trim();
			}
			
			if (!Util.isNullOuVazio(numProcesso)) {
				Processo processo = processoService.buscarProcessoPorCriteria(numProcesso);
				
				if (!Util.isNullOuVazio(processo)) {
					Boolean processoSEIAConcluido = processoService.isProcessoNoStatus(processo.getIdeProcesso(), StatusFluxoEnum.CONCLUIDO.getStatus());
					
					if (processoSEIAConcluido && Util.isNullOuVazio(numProcessoAsvAML)) {
						Boolean retorno = processoService.isAtoAmbientalProcessoPorStatus(processo, atoAmbientalEnum.getId(), StatusProcessoAtoEnum.DEFERIDO);
		
						if (!retorno) {
							JsfUtil.addWarnMessage("Favor infomar um processo de " + tipoAtoRelacionado + " válido concluído.");
						}
					}
					else if (!Util.isNullOuVazio(numProcessoAsvAML)) {
						Boolean retornoASV = processoService.isAtoAmbientalProcessoPorStatus(processo, AtoAmbientalEnum.ASV.getId(), StatusProcessoAtoEnum.DEFERIDO);
						Boolean retornoAML = processoService.isAtoAmbientalProcessoPorStatus(processo, AtoAmbientalEnum.AML.getId(), StatusProcessoAtoEnum.DEFERIDO);
						
						if (!retornoASV && !retornoAML) {
							JsfUtil.addWarnMessage("Favor infomar um processo de ASV ou AML válido concluído.");
						}
					}
					else {
						JsfUtil.addWarnMessage("Favor infomar um processo de " + tipoAtoRelacionado + " válido concluído.");
					}
				}
			}
		} 
		else if (isConsumidor(cumprimentoReposicaoFlorestalDTO)) {
			numProcesso = cumprimentoReposicaoFlorestalDTO.getConsumidorReposicaoFlorestal().getNumProcessoOriginal();
			numProcesso = numProcesso.replace(String.valueOf((char) 160), " ").trim();
			
			if (!Util.isNullOuVazio(numProcesso)) {
				Processo processo = processoService.buscarProcessoPorCriteria(numProcesso);
				
				if (!Util.isNullOuVazio(processo)) {
					Boolean processoSEIAConcluido = processoService.isProcessoNoStatus(processo.getIdeProcesso(), StatusFluxoEnum.CONCLUIDO.getStatus());
					
					if (!processoSEIAConcluido) {
						JsfUtil.addWarnMessage("Favor infomar um processo válido concluído.");
					}
				}
			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void calcularUnidadeMedida(CumprimentoReposicaoFlorestalDTO cumprimentoReposicaoFlorestalDTO, UnidadeMedida unidadeMedida, Double valor) {
		if (UnidadeMedidaEnum.M3.getId().equals(unidadeMedida.getIdeUnidadeMedida())) {
			cumprimentoReposicaoFlorestalDTO.getUnidadeMedidaCalculoDTO().add(new UnidadeMedidaCalculoDTO(UnidadeMedidaEnum.MDC, CalculoFlorestalUtil.converterM3ParaMDC(valor)));
			cumprimentoReposicaoFlorestalDTO.getUnidadeMedidaCalculoDTO().add(new UnidadeMedidaCalculoDTO(UnidadeMedidaEnum.ST, CalculoFlorestalUtil.converterM3ParaEstereo(valor)));
		} 
		else if (UnidadeMedidaEnum.ST.getId().equals(unidadeMedida.getIdeUnidadeMedida())) {
			cumprimentoReposicaoFlorestalDTO.getUnidadeMedidaCalculoDTO().add(new UnidadeMedidaCalculoDTO(UnidadeMedidaEnum.M3, CalculoFlorestalUtil.converterEstereoParaM3(valor)));
			cumprimentoReposicaoFlorestalDTO.getUnidadeMedidaCalculoDTO().add(new UnidadeMedidaCalculoDTO(UnidadeMedidaEnum.MDC, CalculoFlorestalUtil.converterEstereoParaMDC(valor)));
		}
		else {
			cumprimentoReposicaoFlorestalDTO.getUnidadeMedidaCalculoDTO().add(new UnidadeMedidaCalculoDTO(UnidadeMedidaEnum.M3, CalculoFlorestalUtil.converterMDCParaM3(valor)));
			cumprimentoReposicaoFlorestalDTO.getUnidadeMedidaCalculoDTO().add(new UnidadeMedidaCalculoDTO(UnidadeMedidaEnum.ST, CalculoFlorestalUtil.converterMDCParaEstereo(valor)));
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<MemoriaCalculoDTO> gerarMemoriaCalculo(CumprimentoReposicaoFlorestalDTO cumprimentoReposicaoFlorestalDTO) throws Exception {
		List<MemoriaCalculoDTO> lista = new ArrayList<MemoriaCalculoDTO>();
		
		PagamentoReposicaoFlorestal pagamentoReposicaoFlorestal = cumprimentoReposicaoFlorestalDTO.getPagamentoReposicaoFlorestal();
		
		if (!Util.isNullOuVazio(cumprimentoReposicaoFlorestalDTO.getPagamentoReposicaoFlorestal().getIdePagamentoReposicaoFlorestalPai())) {
			pagamentoReposicaoFlorestal = pagamentoReposicaoFlorestal.getIdePagamentoReposicaoFlorestalPai();
		}
		
		if (PagamentoReposicaoFlorestalEnum.DETENTOR.getId().equals(pagamentoReposicaoFlorestal.getIdePagamentoReposicaoFlorestal())) {
			DetentorReposicaoFlorestal detentorReposicaoFlorestal = cumprimentoReposicaoFlorestalDTO.getDetentorReposicaoFlorestal();
			BigDecimal volumeAutorizado = detalhamentoDaeCrfService.converterUnidadeMedidaParaM3(detentorReposicaoFlorestal.getIdeUnidadeMedida(), detentorReposicaoFlorestal.getVolumeAutorizado());
			
			if (!Util.isNullOuVazio(volumeAutorizado)) {
				lista.add(new MemoriaCalculoDTO(Util.getString("crf_volume_informado"), detalhamentoDaeCrfService.formatarNumeroVolume(volumeAutorizado)));
				lista.add(new MemoriaCalculoDTO(Util.getString("crf_calculo_volume"), detalhamentoDaeCrfService.formularDetentorConsumidor(detalhamentoDaeCrfService.formatarNumeroVolume(volumeAutorizado))));
			}
		}
		else if (PagamentoReposicaoFlorestalEnum.CONSUMIDOR.getId().equals(pagamentoReposicaoFlorestal.getIdePagamentoReposicaoFlorestal())) {
			ConsumidorReposicaoFlorestal consumidorReposicaoFlorestal = cumprimentoReposicaoFlorestalDTO.getConsumidorReposicaoFlorestal();
			BigDecimal vlmMaterial = detalhamentoDaeCrfService.converterUnidadeMedidaParaM3(consumidorReposicaoFlorestal.getIdeUnidadeMedida(), consumidorReposicaoFlorestal.getVlmMaterialLenhosoConsumido());
			
			if (!Util.isNullOuVazio(vlmMaterial)) {
				lista.add(new MemoriaCalculoDTO(Util.getString("crf_volume_informado"), detalhamentoDaeCrfService.formatarNumeroVolume(vlmMaterial)));
				lista.add(new MemoriaCalculoDTO(Util.getString("crf_calculo_volume"), detalhamentoDaeCrfService.formularDetentorConsumidor(detalhamentoDaeCrfService.formatarNumeroVolume(vlmMaterial))));
			}
		}
		else if (PagamentoReposicaoFlorestalEnum.DEVEDOR.getId().equals(pagamentoReposicaoFlorestal.getIdePagamentoReposicaoFlorestal())) {
			DevedorReposicaoFlorestal devedorReposicaoFlorestal = cumprimentoReposicaoFlorestalDTO.getDevedorReposicaoFlorestal();
			BigDecimal vlrAreaSuprimida = devedorReposicaoFlorestal.getVlrAreaSuprimida();
			
			if (!Util.isNullOuVazio(devedorReposicaoFlorestal.getIdeBioma()) && !Util.isNullOuVazio(vlrAreaSuprimida)) {
				lista.add(new MemoriaCalculoDTO(Util.getString("crf_area_informada"), detalhamentoDaeCrfService.formatarNumeroVolume(devedorReposicaoFlorestal.getVlrAreaSuprimida())));
				lista.add(new MemoriaCalculoDTO(Util.getString("crf_bioma"), devedorReposicaoFlorestal.getIdeBioma().getNomBioma()));
				lista.add(new MemoriaCalculoDTO(Util.getString("crf_calculo_a_vb_120"), detalhamentoDaeCrfService.formularDevedor(detalhamentoDaeCrfService.formatarNumeroVolume(devedorReposicaoFlorestal.getVlrAreaSuprimida()), devedorReposicaoFlorestal.getIdeBioma().getMetrosCubicos())));
			}
		}
		
		return lista;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<String> gerarLegenda(CumprimentoReposicaoFlorestalDTO cumprimentoReposicaoFlorestalDTO) {
		CumprimentoReposicaoFlorestal cumprimentoReposicaoFlorestal = new CumprimentoReposicaoFlorestal();
		cumprimentoReposicaoFlorestal.setIdePagamentoReposicaoFlorestal(cumprimentoReposicaoFlorestalDTO.getPagamentoReposicaoFlorestal());
		
		return detalhamentoDaeCrfService.gerarLegendas(cumprimentoReposicaoFlorestal);
	}
	
	public void removerMemoriaCalculoDaeParcela(List<MemoriaCalculoDaeParcela> memoriaCalculoDaeParcelaList, GerarDaeDTO gerarDaeDTO) {
		
		if(!Util.isNullOuVazio(memoriaCalculoDaeParcelaList)){
			for(MemoriaCalculoDaeParcela memoriaCalculoDaeParcela:memoriaCalculoDaeParcelaList){
				memoriaCalculoDaeParcela.setIndExcluido(Boolean.TRUE);
				memoriaCalculoDaeParcelaIDAO.salvarOuAtualizar(memoriaCalculoDaeParcela);
				gerarDaeDTO.setIdeMemoriaCalculoDae(memoriaCalculoDaeParcela.getIdeMemoriaCalculoDae().getIdeMemoriaCalculoDae());
			}
			
			if(!Util.isNullOuVazio(gerarDaeDTO.getParcelaUnica())){
				gerarDaeDTO.getParcelaUnica().clear();
			}else if(!Util.isNullOuVazio(gerarDaeDTO.getParcelaDaeDTO())){
				gerarDaeDTO.getParcelaDaeDTO().clear();
			}
		}
		
	}
	
	public List<MemoriaCalculoDaeParcela> listarMemoriaCalculoDaeParcela(Requerimento requerimento) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(MemoriaCalculoDaeParcela.class, "mCDP");
		
		criteria.createAlias("mCDP.ideMemoriaCalculoDae", "mCD");
		
		criteria.createAlias("mCDP.ideDae", "d", JoinType.LEFT_OUTER_JOIN);
		
		criteria.createAlias("mCD.ideCumprimentoReposicaoFlorestal", "cRF");
		
		criteria.add(Restrictions.eq("cRF.requerimento.ideRequerimento", requerimento.getIdeRequerimento()));
		
		criteria.add(Restrictions.eq("mCDP.indExcluido", Boolean.FALSE));
		
		return memoriaCalculoDaeParcelaIDAO.listarPorCriteria(criteria);
	}
	
	/**
	 * Recalcular valor do dae
	 * @param listaDetalhamentoDaeDTO
	 * @param gerarDaeDTO
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void recalcular(List<DetalhamentoDaeDTO> listaDetalhamentoDaeDTO,GerarDaeDTO gerarDaeDTO) throws Exception{
		StringBuilder formula = new StringBuilder();
		
		for(DetalhamentoDaeDTO daeDTO: listaDetalhamentoDaeDTO){
			
			if(!Util.isNullOuVazio(gerarDaeDTO.getValVolumeReferencia())){
				
				for(MemoriaCalculoDTO memoriaCalculoDTO: daeDTO.getListaMemoriaCalculoDTO()){
					if(Util.getString("crf_calculo_a_vb_120").equals(memoriaCalculoDTO.getNomTipo())){
						
						String [] split = memoriaCalculoDTO.getFormula().split("\\*");
						
						formula.append(split[0]
								.concat(" * ")
								.concat(Util.formatarNumero(gerarDaeDTO.getValVolumeReferencia()))
								.concat(" * ")
								.concat(split[2]));
						
						adicionarParametroVRB(daeDTO,formula);
						adicionarParametroVB(daeDTO,split);
						adicionarLegendaVRB(daeDTO);
						gerarDaeDTO.setValorPecuniario(calcularValorPecuniario(daeDTO, split, gerarDaeDTO));
						atualizarVolumeReferencia(gerarDaeDTO, daeDTO);
						limparCampos(memoriaCalculoDTO, gerarDaeDTO);
						removerMemoriaCalculoDaeParcela(listarMemoriaCalculoDaeParcela(gerarDaeDTO.getRequerimento()),gerarDaeDTO);
						atualizarMemoriaCalculoDae(gerarDaeDTO);
						break;
					}
				}
				
			}
		}
	}
	
	/**
	 * adicionar parametroVRB
	 * @param detalhamentoDaeDTO
	 * @param formula
	 */
	private void adicionarParametroVRB(DetalhamentoDaeDTO detalhamentoDaeDTO , StringBuilder formula){
		boolean adicionar = true;
		
		for(MemoriaCalculoDTO memoriaCalculoDTO: detalhamentoDaeDTO.getListaMemoriaCalculoDTO()){
			if(Util.getString("crf_calculo_a_vrb_120").equals(memoriaCalculoDTO.getNomTipo())){
				memoriaCalculoDTO.setFormula(formula.toString());
				adicionar = false;
			}
		}
		
		if(adicionar){
			detalhamentoDaeDTO.getListaMemoriaCalculoDTO().add(new MemoriaCalculoDTO(Util.getString("crf_calculo_a_vrb_120"), formula.toString()));
		}
	}
	
	/**
	 * adicionar parametroVB
	 * @param detalhamentoDaeDTO
	 * @param split
	 */
	private void adicionarParametroVB(DetalhamentoDaeDTO detalhamentoDaeDTO, String [] split){
		boolean adicionar = true;
		
		for(MemoriaCalculoDTO memoriaCalculoDTO: detalhamentoDaeDTO.getListaMemoriaCalculoDTO()){
			if(Util.getString("crf_vb_param").equals(memoriaCalculoDTO.getNomTipo())){
				adicionar = false;
			}
		}
		
		if(adicionar){
			detalhamentoDaeDTO.getListaMemoriaCalculoDTO().add(new MemoriaCalculoDTO(Util.getString("crf_vb_param"), split[1]));
		}
	}
	
	/**
	 * adicionar a legenda
	 * @param detalhamentoDaeDTO
	 */
	private void adicionarLegendaVRB(DetalhamentoDaeDTO detalhamentoDaeDTO){
		if(!detalhamentoDaeDTO.getListaLegenda().contains(Util.getString("crf_vrb"))){
			detalhamentoDaeDTO.getListaLegenda().add(Util.getString("crf_vrb"));
		}
	}
	
	/**
	 * Calcular o valor pecuniário
	 * @param detalhamentoDaeDTO
	 * @param split
	 * @param gerarDaeDTO
	 * @return
	 */
	public Double calcularValorPecuniario(DetalhamentoDaeDTO detalhamentoDaeDTO, String [] split, GerarDaeDTO gerarDaeDTO) {
		
		
		String area= split[0].replace(".", "").replace(',', '.').trim();
		
		String valorEmReais = split[2].replace(',', '.').trim();
		
		try {
			System.out.println("Valores de entrada corrigidos - Area: " + area + ", Valor em Reais: " + valorEmReais);
		
			BigDecimal areaConvertida = new BigDecimal(area);
			
			BigDecimal valorEmReaisConvertida = new BigDecimal(valorEmReais);
			
			Double valorPecuniario = areaConvertida.multiply(gerarDaeDTO.getValVolumeReferencia()).multiply(valorEmReaisConvertida).doubleValue();
			
			if(Util.isNullOuVazio(detalhamentoDaeDTO.getValorOld())){
				detalhamentoDaeDTO.setValorOld(detalhamentoDaeDTO.getValor());
		}

		detalhamentoDaeDTO.setValor(valorPecuniario);
		
		return valorPecuniario;
	} catch(NumberFormatException e){
		System.err.println("Erro ao converter strings para BigDecimal: " + e.getMessage());
        System.err.println("Valores problemáticos - Area: " + area + ", Valor em Reais: " + valorEmReais);
        throw e;
		}
	}
	
	/**
	 * limpar objetos
	 * @param memoriaCalculoDTO
	 * @param gerarDaeDTO
	 */
	private void limparCampos(MemoriaCalculoDTO memoriaCalculoDTO, GerarDaeDTO gerarDaeDTO){
		memoriaCalculoDTO.setRendered(false);
		
		if(!Util.isNullOuVazio(gerarDaeDTO.getParcelaUnica()) || !Util.isNullOuVazio(gerarDaeDTO.getParcelaDaeDTO())){
			gerarDaeDTO.setIndParcela(false);
			gerarDaeDTO.setQtdeParcelas(null);
		}
		gerarDaeDTO.setIndAlterarVolume(false);
		gerarDaeDTO.setValVolumeReferencia(null);
		gerarDaeDTO.setDscCaminhoAqruivoParecerTecnico(null);
		gerarDaeDTO.setFileName(null);
	}
	
	/**
	 * atualizar volume 
	 * @param gerarDaeDTO
	 * @param daeDTO
	 * @throws Exception
	 */
	private void atualizarVolumeReferencia(GerarDaeDTO gerarDaeDTO, DetalhamentoDaeDTO daeDTO) throws Exception{
		DevedorReposicaoFlorestal devedorReposicaoFlorestal = devedorReposicaoFlorestalService.obterDevedorPorRequerimento(gerarDaeDTO.getRequerimento().getIdeRequerimento());
		devedorReposicaoFlorestal.setValVolumeReferencia(gerarDaeDTO.getValVolumeReferencia());
		devedorReposicaoFlorestal.setNomeArquivo(gerarDaeDTO.getFileName());
		devedorReposicaoFlorestal.setDscCaminhoParecerTecnico(gerarDaeDTO.getDscCaminhoAqruivoParecerTecnico());
		devedorReposicaoFlorestal.setDtcGravado(new Date());
		daeDTO.getCumprimentoReposicaoFlorestal().setVlrPecuniario(BigDecimal.valueOf(gerarDaeDTO.getValorPecuniario()));
		devedorReposicaoFlorestal.setIdeCumprimentoReposicaoFlorestal(daeDTO.getCumprimentoReposicaoFlorestal());
		
		cumprimentoReposicaoFlorestalDAO.merge(devedorReposicaoFlorestal.getIdeCumprimentoReposicaoFlorestal());
		devedorReposicaoFlorestalService.salvarDevedorReposicaoFlorestal(devedorReposicaoFlorestal);
	}
	
	/**
	 * Atualizar memória de calculo
	 * @param gerarDaeDTO
	 */
	private void atualizarMemoriaCalculoDae(GerarDaeDTO gerarDaeDTO) {
		
		if(!Util.isNullOuVazio(gerarDaeDTO.getIdeMemoriaCalculoDae())){
			
			MemoriaCalculoDae memoriaCalculoDae = memoriaCalculoDaeIDAO.carregarLoad(gerarDaeDTO.getIdeMemoriaCalculoDae());
			
			memoriaCalculoDae.setIndParcelado(false);
			
			memoriaCalculoDae.setQtdParcelas(0);
			
			memoriaCalculoDaeIDAO.merge(memoriaCalculoDae);
		}
	}
}
