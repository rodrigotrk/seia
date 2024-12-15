package br.gov.ba.seia.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.ReservaLegalDAOImpl;
import br.gov.ba.seia.entity.CronogramaEtapa;
import br.gov.ba.seia.entity.CronogramaRecuperacao;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.PessoaImovel;
import br.gov.ba.seia.entity.ReservaLegal;
import br.gov.ba.seia.entity.StatusReservaLegal;
import br.gov.ba.seia.entity.TipoEstadoConservacao;
import br.gov.ba.seia.enumerator.TemaGeoseiaEnum;
import br.gov.ba.seia.enumerator.TipoArlEnum;
import br.gov.ba.seia.enumerator.TipoEstadoConservacaoEnum;
import br.gov.ba.seia.exception.AreaDeclaradaInvalidaException;
import br.gov.ba.seia.exception.CampoObrigatorioException;
import br.gov.ba.seia.exception.LocalizacaoGeograficaException;
import br.gov.ba.seia.exception.ReservaCompensadaEmMaisDeUmImovelException;
import br.gov.ba.seia.exception.SeiaException;
import br.gov.ba.seia.exception.SobreposicaoAreasException;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ReservaLegalService {
	
	@Inject
	ReservaLegalDAOImpl daoImpl;
	
	@Inject
	IDAO<ReservaLegal> dao;
	
	@Inject
	IDAO<CronogramaRecuperacao> cronogramaRecuperacaoDAO;
	
	@Inject
	private ValidacaoGeoSeiaService validacaoGeoSeiaService;
	@Inject
	private LocalizacaoGeograficaService localizacaoGeograficaService;
	@Inject
	private CronogramaEtapaService cronogramaEtapaService;
	@Inject
	private CronogramaRecuperacaoService cronogramaRecuperacaoService;
	@Inject
	private AuditoriaService auditoriaService;
	@Inject
	private ReservaLegalAverbadaService reservaLegalAverbadaService;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ReservaLegal filtrarById(ReservaLegal pReservaLegal)  {
		return this.daoImpl.filtrarById(pReservaLegal);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ReservaLegal carregarTudo(ReservaLegal pReservaLegal)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ReservaLegal.class, "reservaLegal");
		
		criteria.createAlias("ideTipoArl", "tipoArl", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideLocalizacaoGeografica", "localizacaoGeografica", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideTipoEstadoConservacao", "tipoEstadoConservacao", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideTipoOrigemCertificado", "tipoOrigemCertificado", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideReservaLegalAverbada", "reservaLegalAverbada", JoinType.LEFT_OUTER_JOIN);		
		criteria.createAlias("ideStatus", "status", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("cronogramaRecuperacao", "cronogramaRecuperacao", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideImovelRural", "imovelRural", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ideReservaLegal", pReservaLegal.getIdeReservaLegal()));
		criteria.addOrder(Order.asc("ideReservaLegal"));
		
		ReservaLegal reservaLegal = dao.buscarPorCriteria(criteria);
		
		if(!Util.isNullOuVazio(reservaLegal) && !Util.isNullOuVazio(reservaLegal.getCronogramaRecuperacao())) {
			Hibernate.initialize(reservaLegal.getCronogramaRecuperacao().getCronogramaEtapaCollection());
		}
		
		if(!Util.isNullOuVazio(reservaLegal) && !Util.isNullOuVazio(reservaLegal.getIdeLocalizacaoGeografica())){
			Hibernate.initialize(reservaLegal.getIdeLocalizacaoGeografica().getDadoGeograficoCollection());
		}
		
		return reservaLegal;
	}

	public ReservaLegal buscaReservaLegalByImovelRural(ImovelRural imovelRural) {
		ReservaLegal reservaLegal = new ReservaLegal();
		DetachedCriteria criteria = DetachedCriteria.forClass(ReservaLegal.class, "reservaLegal");
		criteria.createAlias("ideTipoArl", "tipoArl", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideLocalizacaoGeografica", "localizacaoGeografica", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideTipoEstadoConservacao", "tipoEstadoConservacao", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideTipoOrigemCertificado", "tipoOrigemCertificado", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ideImovelRural", imovelRural));
		criteria.addOrder(Order.asc("ideReservaLegal"));
		reservaLegal = dao.buscarPorCriteria(criteria);
		return reservaLegal;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ReservaLegal pReservaLegal)  {
		validarStatusReserva(pReservaLegal);
		this.daoImpl.salvar(pReservaLegal);
	}

	private void validarStatusReserva(ReservaLegal pReservaLegal) {
		if(Util.isNullOuVazio(pReservaLegal.getIdeStatus()) 
				|| pReservaLegal.getIdeStatus().getIdeStatusReservaLegal().equals(7)
				|| ((Util.isNullOuVazio(pReservaLegal.getIndAverbada())	|| !pReservaLegal.getIndAverbada()) && (!Util.isNullOuVazio(pReservaLegal.getIdeStatus()) && pReservaLegal.getIdeStatus().getIdeStatusReservaLegal() == 4))){
			if(pReservaLegal.getIdeTipoArl().getIdeTipoArl() == 2 || pReservaLegal.getIdeTipoArl().getIdeTipoArl() == 3) {			
				//No próprio imóvel ou Em compensação entre imóveis de mesmo proprietário e com status de RL Cadastrada 
				pReservaLegal.setIdeStatus(new StatusReservaLegal(3));			
			}else {
				pReservaLegal.setIdeStatus(new StatusReservaLegal(7));
			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(ReservaLegal pReservaLegal)  {
		validarStatusReserva(pReservaLegal);
		this.daoImpl.atualizar(pReservaLegal);
	}
	
	public Boolean verificaProcessoCerberus(String processo, String certificado) {
		List<String> listaProcessos =  daoImpl.verificaProcessoCerberus(processo, certificado);
		if (!Util.isNullOuVazio(listaProcessos))
			return true;
		else
			return false;		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(ReservaLegal pReservaLegal)  {
		this.daoImpl.remover(pReservaLegal);
	}

	/**
	 * Método que valida os dados da reserva legal na finalização do cadastro do imóvel rural
	 * @param imovelRural
	 * @param listProprietariosImovel
	 * @param context
	 * @return void
	 * @throws Exception 
	 * @ 
	 * @author carlos.duarte (carlos.duarte@inema.ba.gov.br)
	 * @since 14/10/2015
	*/
	public void validaRlParaFinalizacao(ImovelRural imovelRural, Collection<PessoaImovel> listProprietariosImovel, RequestContext context) throws Exception  {
		if(!Util.isNullOuVazio(imovelRural.getReservaLegal()) 
				&& !Util.isNullOuVazio(imovelRural.getReservaLegal().getIdeTipoArl()) 
				&& (imovelRural.getReservaLegal().getIdeTipoArl().getIdeTipoArl().equals(2) 
						|| imovelRural.getReservaLegal().getIdeTipoArl().getIdeTipoArl().equals(3))){
			// Executa as validações espaciais em relação a geometria da RL fornecida
			validaTheGeomRl(imovelRural, listProprietariosImovel, context);
			
			// Executa as validações espaciais em relação a geometria da área degradada da RL fornecida
			if(!Util.isNullOuVazio(imovelRural.getReservaLegal().getCronogramaRecuperacao())) {
				validaTheGeomPradRl(imovelRural);
			}
		} else if(Util.isNullOuVazio(imovelRural.getReservaLegal()) && (imovelRural.isImovelCDA() || imovelRural.isImovelBNDES())){
			context.addCallbackParam("isRlMenorQueVintePorCento", true);
		}
	}
	
	/**
	 * Método que valida os dados geográficos da reserva legal
	 * @param pImovelRural
	 * @param listProprietariosImovel
	 * @param context
	 * @return void
	 * @throws Exception 
	 * @ 
	 * @author carlos.duarte (carlos.duarte@inema.ba.gov.br)
	 * @since 14/10/2015
	*/
	private void validaTheGeomRl(ImovelRural pImovelRural, Collection<PessoaImovel> listProprietariosImovel, RequestContext context) throws Exception  {
		try{
			String geometriaIm = null;
			String geometriaRl = null;
			
			if(ContextoUtil.getContexto().isPCT()) {
				if(pImovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getNovosArquivosShapeImportados()) {
					geometriaIm = validacaoGeoSeiaService.buscarGeometriaShapeTemporario(pImovelRural.getIdeImovelRural(), TemaGeoseiaEnum.LIMITE_TERRITORIO_PCT.getId(), null);
				} else {
					geometriaIm = validacaoGeoSeiaService.buscarGeometriaShape(pImovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getIdeLocalizacaoGeografica());
				}
			}else {
				if(pImovelRural.getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()) {
					geometriaIm = validacaoGeoSeiaService.buscarGeometriaShapeTemporario(pImovelRural.getIdeImovelRural(), TemaGeoseiaEnum.LIMITE_PROPRIEDADE.getId(), null);
				} else {
					geometriaIm = validacaoGeoSeiaService.buscarGeometriaShape(pImovelRural.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
				}
			}
			
			if(pImovelRural.getReservaLegal().getIdeLocalizacaoGeografica() == null 
					|| !localizacaoGeograficaService.existeTheGeom(pImovelRural.getReservaLegal().getIdeLocalizacaoGeografica())){
				throw new CampoObrigatorioException("Não existe localização geográfica da Reserva Legal.");
			}
			
			//Obtem a geometria da reserva legal através do arquivo shape temporário ou diretamente do banco
			if (pImovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()) {
				geometriaRl = validacaoGeoSeiaService.buscarGeometriaShapeTemporario(pImovelRural.getIdeImovelRural(), TemaGeoseiaEnum.RESERVA_LEGAL.getId(), null);					
			} else {
				geometriaRl = validacaoGeoSeiaService.buscarGeometriaShape(pImovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
			}
			
			//Aplica validação de área e sobreposição
			validacaoGeoSeiaService.validarAreaDeclaradaShapeTemporario(pImovelRural.getReservaLegal().getValArea(), geometriaRl);
			
			if (!pImovelRural.isImovelINCRA() && !pImovelRural.isImovelPCT()) {
				validacaoGeoSeiaService.validarSobreposicaoTemaShapeTemporario(geometriaRl, TemaGeoseiaEnum.RESERVA_LEGAL.getId(), pImovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());				
			} else if (!pImovelRural.isImovelINCRA() && pImovelRural.isImovelPCT()) {
				validacaoGeoSeiaService.validarRlComunidadePCT(geometriaRl, TemaGeoseiaEnum.RESERVA_LEGAL_PCT.getId(),pImovelRural);
			}
			
			//Se a Reserva Legal for do tipo No Próprio Imóvel
			if (pImovelRural.getReservaLegal().getIdeTipoArl().getIdeTipoArl().equals(2)) {
				//Aplica validação do tema em relação ao limite do imóvel
				validacaoGeoSeiaService.validarLocalizacaoGeografica(null, geometriaRl, null, geometriaIm);
				
				if(validacaoGeoSeiaService.isRlMenor20PorCento(pImovelRural)){
					if (!podeCadastrarRl(pImovelRural)){
						throw new CampoObrigatorioException(Util.getString("cefir_msg_A006"));
					} else {
						if(!ContextoUtil.getContexto().isPCT()) {
							context.addCallbackParam("isRlMenorQueVintePorCento", true);
						}
					}
				}
			}
			//Se a Reserva Legal for do tipo Em Compensação entre Imóveis do Mesmo Proprietário
			else if(pImovelRural.getReservaLegal().getIdeTipoArl().getIdeTipoArl().equals(3)) {
				//listar imoveis com certificado do proprietario e validar
				//Collection<PessoaImovel> lProprietarios = imovelRural.getthis.imovelRuralServiceFacade.filtrarPROPRIETARIOImovel(this.imovelRural.getImovel());
				
				if(!validacaoGeoSeiaService.isValidaCompensacaoReservaLegal(listProprietariosImovel, pImovelRural)) {
					throw new CampoObrigatorioException("A reserva legal não está em compensação entre imóveis cadastrados de mesmo proprietário.Favor cadastrar o imóvel onde será compensado!");
				}
				
				if(validacaoGeoSeiaService.isRlMenor20PorCento(pImovelRural)){
					if (!podeCadastrarRl(pImovelRural)){
						throw new CampoObrigatorioException(Util.getString("cefir_msg_A007"));
					} else {
						if(!ContextoUtil.getContexto().isPCT()) {
							context.addCallbackParam("isRlMenorQueVintePorCento", true);
						}
					}
				}
			}
			
			//Se a Reserva Legal for declarada como Aprovada
			if(!Util.isNullOuVazio(pImovelRural.getIndReservaLegal()) && pImovelRural.getIndReservaLegal()){
				//Se a origem do certificado da Reserva Legal for Estadual (2)
				if(!Util.isNullOuVazio(pImovelRural.getReservaLegal().getIdeTipoOrigemCertificado()) && pImovelRural.getReservaLegal().getIdeTipoOrigemCertificado().getIdeTipoOrigemCertificado().equals(2)){
					//Aplica validação da Reserva Legal com a aprovação do Cerberus
					if(validaAprovacaoRlProcessoCerberus(pImovelRural)){
						context.addCallbackParam("processoRlConcluidoCerberus", true);
					} else {
						context.addCallbackParam("processoRlConcluidoCerberus", false);
					}
				}
			}
		} catch(AreaDeclaradaInvalidaException a) {
			throw new AreaDeclaradaInvalidaException("A área informada da Reserva Legal ("+a.getAreaDeclarada()+" ha) não confere com a área do shapefile importado ("+a.getAreaCalculada()+" ha).");
		} catch(LocalizacaoGeograficaException l) {
			throw new LocalizacaoGeograficaException("A geometria da Reserva Legal não está dentro do Limite do Imóvel Rural cadastrado.");
		} catch (SobreposicaoAreasException s) {
			throw new Exception("Caro usuário, o shapefile importado da Reserva Legal sobrepõe outra(s) Reserva(s) Legal(is) cadastrada(s) em "+s.getPercentualSobreposicao()+"%.");
		} catch (ReservaCompensadaEmMaisDeUmImovelException r) {
			throw new ReservaCompensadaEmMaisDeUmImovelException(r.getMessage());
		} catch (CampoObrigatorioException c) {
			throw new CampoObrigatorioException(c.getMessage());
		} catch (SeiaException se) {
			throw new SeiaException(se.getMessage());
		} catch (Exception e) {
			throw new Exception("Erro na validação de geometria da Reserva Legal, contate o administrador do sistema.");
		}
	}
	
	private void validaTheGeomPradRl(ImovelRural pImovelRural) throws Exception  {
		try{
			String geometriaRl = null;
			String geometriaPradRl = null;
			
			if(pImovelRural.getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica() == null
						|| !localizacaoGeograficaService.existeTheGeom(pImovelRural.getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica())){
				throw new CampoObrigatorioException("Não existe localização geográfica da Área Degradada da Reserva Legal.");
			}
			
			//Obtem a geometria da reserva legal através do arquivo shape temporário ou diretamente do banco
			if (pImovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()) {
				geometriaRl = validacaoGeoSeiaService.buscarGeometriaShapeTemporario(pImovelRural.getIdeImovelRural(), TemaGeoseiaEnum.RESERVA_LEGAL.getId(), null);					
			} else {
				geometriaRl = validacaoGeoSeiaService.buscarGeometriaShape(pImovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
			}
			
			//Obtem a geometria da área degradada da reserva legal através do arquivo shape temporário ou diretamente do banco
			if(pImovelRural.getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().getNovosArquivosShapeImportados()) {
				geometriaPradRl = validacaoGeoSeiaService.buscarGeometriaShapeTemporario(pImovelRural.getIdeImovelRural(), TemaGeoseiaEnum.PRAD_RESERVA_LEGAL.getId(), null);
			} else {
				geometriaPradRl = validacaoGeoSeiaService.buscarGeometriaShape(pImovelRural.getReservaLegal().getCronogramaRecuperacao().getLocalizacaoGeografica().getIdeLocalizacaoGeografica());
			}
			
			//Aplica validação da poligonal da Área Degradada em relação a Reserva Legal
			validacaoGeoSeiaService.validarLocalizacaoGeografica(null, geometriaPradRl, null, geometriaRl);
		} catch(LocalizacaoGeograficaException l) {
			throw new LocalizacaoGeograficaException("A geometria da Área Degradada não está efetivamente contida na delimitação da Reserva Legal cadastrada.");
		} catch (CampoObrigatorioException c) {
			throw new CampoObrigatorioException(c.getMessage());
		} catch (Exception e) {
			throw new Exception("Erro na validação da geometria da Área Degradada de Reserva Legal, contate o administrador do sistema.");
		}
	}
	
	private Boolean podeCadastrarRl(ImovelRural imovelRural) {
		if((imovelRural.isMenorQueQuatroModulosFiscais() || ContextoUtil.getContexto().isPCT()) && (imovelRural.isImovelBNDES() || imovelRural.isImovelCDA() || imovelRural.getReservaLegal().getIdeTipoEstadoConservacao().getIdeTipoEstadoConservacao().equals(TipoEstadoConservacaoEnum.PRESERVADA.getId()))) {
			return true;
		}
		return false;
	}
	
	public Boolean validaAprovacaoRlProcessoCerberus(ImovelRural pImovelRural) {
		try{
			if(verificaProcessoCerberus(pImovelRural.getReservaLegal().getNumProcesso(), pImovelRural.getReservaLegal().getNumCertificado())){
				if(validacaoGeoSeiaService.validaAprovacaoRlProcessoCerberus(pImovelRural.getReservaLegal().getNumProcesso(), pImovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica())){
					return true;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	
	private void validaCronogramaRecuperacao(ImovelRural imovelRural, ReservaLegal objAntigoReservaLegal, CronogramaRecuperacao objAntigoCronogramaRecuperacaoRl) throws Exception{
		if (Util.isNullOuVazio(imovelRural.getReservaLegal().getCronogramaRecuperacao())) {
			if (!Util.isNullOuVazio(objAntigoReservaLegal)){ 
				excluirCronogramaRl(objAntigoReservaLegal);
			}
			
			cronogramaRecuperacaoService.salvarOuAtualizar(imovelRural.getReservaLegal().getCronogramaRecuperacao());
			auditoriaService.salvar(imovelRural.getReservaLegal().getCronogramaRecuperacao());
		} else {
			//Excluindo todas as etapas anteriores
			cronogramaEtapaService.excluirAllByIdeCronogramaRecuperacao(objAntigoCronogramaRecuperacaoRl.getIdeCronogramaRecuperacao());
			
			if(!Util.isNullOuVazio(imovelRural.getReservaLegal().getCronogramaRecuperacao().getCronogramaEtapaCollection())) {
				for(CronogramaEtapa etapa : imovelRural.getReservaLegal().getCronogramaRecuperacao().getCronogramaEtapaCollection()){
					etapa.setIdeCronogramaEtapa(null);
				}
			}
			cronogramaRecuperacaoService.salvarOuAtualizar(imovelRural.getReservaLegal().getCronogramaRecuperacao());
			auditoriaService.atualizar(objAntigoCronogramaRecuperacaoRl, imovelRural.getReservaLegal().getCronogramaRecuperacao());
		}		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void persistirReservaLegal(ImovelRural imovelRural, ReservaLegal objAntigoReservaLegal, CronogramaRecuperacao objAntigoCronogramaRecuperacaoRl) throws Exception  {
		validarReservaLegal(imovelRural);
		
		LocalizacaoGeografica localizacaoReserva = imovelRural.getReservaLegal().getIdeLocalizacaoGeografica();
		imovelRural.getReservaLegal().setIdeLocalizacaoGeografica(null);
		CronogramaRecuperacao cronogramaReserva = imovelRural.getReservaLegal().getCronogramaRecuperacao();
		imovelRural.getReservaLegal().setCronogramaRecuperacao(null);

		if(Util.isNullOuVazio(imovelRural.getReservaLegal().getIndAverbada()) || !imovelRural.getReservaLegal().getIndAverbada()){
			if(!Util.isNullOuVazio(imovelRural.getReservaLegal().getIdeReservaLegalAverbada()) 
					&& !Util.isNullOuVazio(imovelRural.getReservaLegal().getIdeReservaLegalAverbada().getIdeReservaLegalAverbada())){					
				reservaLegalAverbadaService.remover(imovelRural.getReservaLegal().getIdeReservaLegalAverbada());
				imovelRural.getReservaLegal().setIdeReservaLegalAverbada(null);
			}
		} else {
			imovelRural.getReservaLegal().getIdeReservaLegalAverbada().setIdeReservaLegal(imovelRural.getReservaLegal());							
		}
		
		if(Util.isNullOuVazio(imovelRural.getReservaLegal().getIdeReservaLegal())) {
			imovelRural.getReservaLegal().setIdeImovelRural(imovelRural);
			salvar(imovelRural.getReservaLegal());
			auditoriaService.salvar(imovelRural.getReservaLegal());
		}
		
		if(imovelRural.getReservaLegal().getIdeTipoArl().getIdeTipoArl() == TipoArlEnum.NPI.getId() 
				|| imovelRural.getReservaLegal().getIdeTipoArl().getIdeTipoArl() == TipoArlEnum.ECIP.getId()){
			imovelRural.getReservaLegal().setIdeLocalizacaoGeografica(localizacaoReserva);
			
			if(!Util.isNull(imovelRural.getReservaLegal().getIdeLocalizacaoGeografica())){
				if(imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getLocalizacaoExcluida()) {
					if(imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica() != null){
						localizacaoGeograficaService.excluirDadosPersistidos(imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
						imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().setDadoGeograficoCollection(null);
					}
					imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().setLocalizacaoExcluida(false);
				}
				
				localizacaoGeograficaService.salvarOuAtualizarLocalizacaoGeografica(imovelRural.getReservaLegal().getIdeLocalizacaoGeografica());
			}
			
			if ((!Util.isNullOuVazio(imovelRural.getReservaLegal().getIdeTipoEstadoConservacao()) 
					&& !imovelRural.getReservaLegal().getIdeTipoEstadoConservacao().getIdeTipoEstadoConservacao().equals(TipoEstadoConservacaoEnum.PRESERVADA.getId())) 
					|| (!Util.isNull(imovelRural.getReservaLegal().getIndDesejaCadPrad()) && ContextoUtil.getContexto().isPCT())) {
				imovelRural.getReservaLegal().setCronogramaRecuperacao(cronogramaReserva);
				
				if(ContextoUtil.getContexto().isPCT()) {
					if(!Util.isNull(imovelRural.getReservaLegal().getIndDesejaCadPrad())){
						if(imovelRural.getReservaLegal().getIndDesejaCadPrad()) {
							validaCronogramaRecuperacao(imovelRural, objAntigoReservaLegal, objAntigoCronogramaRecuperacaoRl);							
						}
						imovelRural.getReservaLegal().setDtcRespDesejaCadPrad(new Date());
					} else {
						validaCronogramaRecuperacao(imovelRural, objAntigoReservaLegal, objAntigoCronogramaRecuperacaoRl);
						imovelRural.getReservaLegal().setDtcRespDesejaCadPrad(null);
					}
				} else {
					validaCronogramaRecuperacao(imovelRural, objAntigoReservaLegal, objAntigoCronogramaRecuperacaoRl);
				}
			} else if (!Util.isNullOuVazio(objAntigoReservaLegal) 
					&& !Util.isNullOuVazio(objAntigoReservaLegal.getIdeTipoEstadoConservacao()) 
					&& !objAntigoReservaLegal.getIdeTipoEstadoConservacao().getIdeTipoEstadoConservacao().equals(TipoEstadoConservacaoEnum.PRESERVADA.getId())) {
	           excluirCronogramaRl(objAntigoReservaLegal);
			}
		} else {
			imovelRural.getReservaLegal().setIdeLocalizacaoGeografica(localizacaoReserva);
			
			if(imovelRural.getReservaLegal().getIdeLocalizacaoGeografica() != null 
					&& imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica() != null){
				localizacaoGeograficaService.excluirDadosPersistidos(imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
				imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().setDadoGeograficoCollection(null);
			}
			
			imovelRural.getReservaLegal().setIdeLocalizacaoGeografica(null);
			imovelRural.getReservaLegal().setIdeTipoEstadoConservacao(null);
			imovelRural.getReservaLegal().setNumSicarCompensacao(null);
			imovelRural.getReservaLegal().setCronogramaRecuperacao(cronogramaReserva);
			
			if(!Util.isNullOuVazio(imovelRural.getReservaLegal().getCronogramaRecuperacao())){
				cronogramaEtapaService.excluirAllByIdeCronogramaRecuperacao(imovelRural.getReservaLegal().getCronogramaRecuperacao().getIdeCronogramaRecuperacao());
				cronogramaRecuperacaoService.removerDocumentoPrad(imovelRural.getReservaLegal().getCronogramaRecuperacao());							
				cronogramaRecuperacaoService.excluir(imovelRural.getReservaLegal().getCronogramaRecuperacao());	                
				imovelRural.getReservaLegal().getCronogramaRecuperacao().setCronogramaEtapaCollection(null);
			}
		}
		
		//Atualizando dados da reserva legal
		atualizar(imovelRural.getReservaLegal());
		
		if(!Util.isNullOuVazio(objAntigoReservaLegal)) {
			auditoriaService.atualizar(objAntigoReservaLegal, imovelRural.getReservaLegal());
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void excluirCronogramaRl(ReservaLegal reservaLegal) throws Exception  {
		if(!Util.isNullOuVazio(reservaLegal.getCronogramaRecuperacao())  && !Util.isNullOuVazio(reservaLegal.getCronogramaRecuperacao().getIdeCronogramaRecuperacao())){
			cronogramaEtapaService.excluirAllByIdeCronogramaRecuperacao(reservaLegal.getCronogramaRecuperacao().getIdeCronogramaRecuperacao());
			reservaLegal.getCronogramaRecuperacao().setCronogramaEtapaCollection(null);
		    if(!Util.isNullOuVazio(reservaLegal.getCronogramaRecuperacao().getIdeDocumentoObrigatorio()) && !Util.isNullOuVazio(reservaLegal.getCronogramaRecuperacao().getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio())){
		    	List<CronogramaRecuperacao> lCronogramasCadastrados;							
		    }
		    /**Excluindo cronograma_recuperacao*/
		    cronogramaRecuperacaoService.excluir(reservaLegal.getCronogramaRecuperacao());	                
		}
	}
	
	/**
	 * Método que valida os campos obrigatórios para cadastro de reserva legal
	 * @param imovelRural
	 * @return void
	 * @throws CampoObrigatorioException 
	 * @author carlos.duarte (carlos.duarte@inema.ba.gov.br)
	 * @since 14/10/2015
	*/
	public void validarReservaLegal(ImovelRural imovelRural) throws CampoObrigatorioException {
		if(imovelRural.getReservaLegal().getIdeTipoArl().getIdeTipoArl() == 2 || imovelRural.getReservaLegal().getIdeTipoArl().getIdeTipoArl() == 3){
			if (!Util.isNullOuVazio(imovelRural.getReservaLegal().getIdeTipoEstadoConservacao()) && isReservaLegalDesejaCadastrarCronograma(imovelRural)) {
				if(ContextoUtil.getContexto().isPCT()) {
					if(Util.isNullOuVazio(imovelRural.getReservaLegal().getIndDesejaCadPrad())) {
						throw new CampoObrigatorioException("É necessário informar se deseja cadastrar o cronograma de recuperação de áreas degradadas.");	
					}
				}
				
				if(imovelRural.isImovelINCRA() || imovelRural.isImovelCDA() || imovelRural.isImovelBNDES()) {
					return;
				}
				
				if (Util.isNullOuVazio(imovelRural.getReservaLegal().getCronogramaRecuperacao().getCronogramaEtapaCollection())) {
					throw new CampoObrigatorioException("É necessário adicionar o cronograma de recuperação.");				
				}
				
				if (!imovelRural.getReservaLegal().getAceiteCondicoesRecuperacaoRl()) {
					throw new CampoObrigatorioException("É necessário aceitar o termo com as condições para recuperação de áreas degradadas.");				
				}
				
				if (Util.isNullOuVazio(imovelRural.getReservaLegal().getCronogramaRecuperacao().getCronogramaEtapaCollection()) || imovelRural.getReservaLegal().getCronogramaRecuperacao().getCronogramaEtapaCollection().isEmpty()) {
					throw new CampoObrigatorioException("É necessário adicionar o cronograma de recuperação.");
				}
				
				if (Util.isNullOuVazio(imovelRural.getReservaLegal().getCronogramaRecuperacao().getIndPradImportada())) {
					throw new CampoObrigatorioException("O campo O Plano de Recuperação Ambiental - PRA é o mesmo informado anteriormente? é de preenchimento obrigatório.");
				}
				
				if ((Util.isNullOuVazio(imovelRural.getReservaLegal().getCronogramaRecuperacao().getIdeDocumentoObrigatorio()))) {
					throw new CampoObrigatorioException("É necessário importar o arquivo PRA.");
				}
			}
		}
		
		if(!Util.isNullOuVazio(imovelRural.getReservaLegal().getIndAverbada()) && imovelRural.getReservaLegal().getIndAverbada()) {
			if(Util.isNullOuVazio(imovelRural.getReservaLegal().getIdeReservaLegalAverbada()) || Util.isNullOuVazio(imovelRural.getReservaLegal().getIdeReservaLegalAverbada().getIdeDocumentoAverbacao())) {
				if(!imovelRural.isImovelCDA() && !imovelRural.isImovelBNDES()) {
					throw new CampoObrigatorioException(Util.getString("cefir_lbl_documento_comprova_averbacao"));
				}
			}
		} 
		
		if((!Util.isNullOuVazio(imovelRural.getIndReservaLegal()) && imovelRural.getIndReservaLegal() 
			&& !(!Util.isNullOuVazio(imovelRural.getReservaLegal().getIdeStatus()) 
					&& !Util.isNullOuVazio(imovelRural.getReservaLegal().getIdeStatus().getdscStatus()) 
					&& imovelRural.getReservaLegal().getIdeStatus().getdscStatus().equals("Aprovada") 
					&& Util.isNullOuVazio(imovelRural.getReservaLegal().getDtcAprovacaoDeclarada())))
					
			&& (Util.isNullOuVazio(imovelRural.getReservaLegal().getIndAverbada()) || !imovelRural.getReservaLegal().getIndAverbada())) {
			
			if(Util.isLazyInitExcepOuNull(imovelRural.getReservaLegal().getIdeDocumentoAprovacao()) 
				|| Util.isNullOuVazio(imovelRural.getReservaLegal().getIdeDocumentoAprovacao())) {
				if(!imovelRural.isImovelCDA() && !imovelRural.isImovelBNDES()) {
					throw new CampoObrigatorioException(Util.getString("cefir_lbl_documento_comprova_aprovacao"));
				}
			}
		}
	}

	/**
	 * Método que verifica se é necessario preencher o {@link CronogramaEtapa} da {@link ReservaLegal} de acordo com o {@link TipoEstadoConservacao}.
	 *
	 * @param imovelRural
	 * @return
	 *
	 * @author eduardo.fernandes
	 * @since 27/09/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/7823">#7823</a> 
	 */
	private boolean isReservaLegalNecessitaCronograma(ImovelRural imovelRural) {
		return imovelRural.getReservaLegal().getIdeTipoEstadoConservacao().getIdeTipoEstadoConservacao().equals(TipoEstadoConservacaoEnum.PARCIALMENTE_DEGRADADA.getId()) 
				|| imovelRural.getReservaLegal().getIdeTipoEstadoConservacao().getIdeTipoEstadoConservacao().equals(TipoEstadoConservacaoEnum.DEGRADADA.getId());
	}
	
	/**
	 * Método que verifica se é necessario preencher o {@link CronogramaEtapa} da {@link ReservaLegal} de acordo com o {@link TipoEstadoConservacao}.
	 *
	 * @param imovelRural
	 * @return
	 *
	 * @author samuel.oliveira
	 * @since 27/09/2016
	 * @see <a href="http://redmine.prodeb.ba.gov.br/issues/132578">#132578</a> 
	 */	
	public boolean isReservaLegalDesejaCadastrarCronograma(ImovelRural imovelRural) {
		
		if(ContextoUtil.getContexto().isPCT()) {
			Boolean desejaCadPrad = null;
			
			if(!Util.isNullOuVazio(imovelRural)){
				desejaCadPrad = imovelRural.getReservaLegal().getIndDesejaCadPrad();
			}
			
			if(Util.isNullOuVazio(desejaCadPrad)) {
				return isReservaLegalNecessitaCronograma(imovelRural);		
			}else {			
				return desejaCadPrad;
			}			
		}else {
			return isReservaLegalNecessitaCronograma(imovelRural);
		}
	}
}
