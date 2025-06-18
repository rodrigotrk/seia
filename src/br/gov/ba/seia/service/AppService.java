package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.EJB;
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

import br.gov.ba.seia.dao.AppDAOImpl;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.App;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.TipoEstadoConservacao;
import br.gov.ba.seia.enumerator.TemaGeoseiaEnum;
import br.gov.ba.seia.exception.AreaDeclaradaInvalidaException;
import br.gov.ba.seia.exception.CampoObrigatorioException;
import br.gov.ba.seia.exception.LocalizacaoGeograficaException;
import br.gov.ba.seia.exception.SeiaException;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AppService {
	
	@Inject
	IDAO<TipoEstadoConservacao> daoTipoEstadoConservacao;	
	
	@Inject
	AppDAOImpl daoImpl;

	@Inject
	IDAO<App> dao;	
	
	@EJB
	private ValidacaoGeoSeiaService validacaoGeoSeiaService;
	@EJB
	private LocalizacaoGeograficaService localizacaoGeograficaService;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<App> listarAppByImovelRural(ImovelRural imovelRural) {
		DetachedCriteria criteria = DetachedCriteria.forClass(App.class, "app");
		criteria.createAlias("ideTipoApp", "tipoApp", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideLocalizacaoGeografica", "localizacaoGeografica", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideTipoEstadoConservacao", "tipoEstadoConservacao", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ideImovelRural", imovelRural));
		criteria.addOrder(Order.asc("ideApp"));
		return dao.listarPorCriteria(criteria);
	}
		
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Collection<TipoEstadoConservacao> listarTipoEstadoConservacao() {
		return  daoTipoEstadoConservacao.listarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(App pApp)  {
		this.daoImpl.salvar(pApp);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(App pApp)  {
		this.daoImpl.salvarOuAtualizar(pApp);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(App pApp)  {
		this.daoImpl.atualizar(pApp);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(App pApp) {
		this.daoImpl.excluir(pApp);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public App carregarTudo(App pApp) {
		DetachedCriteria criteria = DetachedCriteria.forClass(App.class, "app");
		
		criteria.createAlias("ideTipoApp", "tipoApp", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideSubTipoApp", "subTipoApp", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideLocalizacaoGeografica", "localizacaoGeografica", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideTipoEstadoConservacao", "tipoEstadoConservacao", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("cronogramaRecuperacao", "cronogramaRecuperacao", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideImovelRural", "imovelRural", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ideApp", pApp.getIdeApp()));
		criteria.addOrder(Order.asc("ideApp"));
		
		App app = dao.buscarPorCriteria(criteria);
		
		if (!Util.isNullOuVazio(app) && !Util.isNullOuVazio(app.getCronogramaRecuperacao())){
			Hibernate.initialize(app.getCronogramaRecuperacao().getCronogramaEtapaCollection());
		}
		
		if(!Util.isNullOuVazio(app) && !Util.isNullOuVazio(app.getIdeLocalizacaoGeografica())){
			Hibernate.initialize(app.getIdeLocalizacaoGeografica().getDadoGeograficoCollection());
		}
		
		return app;
	}

	public void validaAppParaFinalizacao(ImovelRural imovelRural, RequestContext context) throws Exception {
		if(!Util.isNull(imovelRural.getIndApp()) && imovelRural.getIndApp()) {
			boolean listaVazia = true;
			
			if(!Util.isNullOuVazio(imovelRural.getAppCollection())){
				for (App app : imovelRural.getAppCollection()) {
					if (!app.getIndExcluido()){
						listaVazia = false;
						break;
					}
				}
			}
			
			if(listaVazia) {
				throw new SeiaException("Por favor cadastre pelo menos uma Área de Preservação Permanente.");
			} else {
				validaTheGeomApp(imovelRural, context);
				validaTheGeomPradApp(imovelRural);
			}
		}
	}
	
	private void validaTheGeomApp(ImovelRural pImovelRural, RequestContext context) throws Exception{
		try{
			String geometriaIm = null;
			String geometriaRl = null;
			String geometriaVn = null;
			String geometriaApp = null;
			
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
			
			if(!Util.isNullOuVazio(pImovelRural.getReservaLegal()) && !Util.isNull(pImovelRural.getReservaLegal().getIdeLocalizacaoGeografica())) {			
				if(pImovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()){
					geometriaRl = validacaoGeoSeiaService.buscarGeometriaShapeTemporario(pImovelRural.getIdeImovelRural(), TemaGeoseiaEnum.RESERVA_LEGAL.getId(), null);
				} else {
					geometriaRl = validacaoGeoSeiaService.buscarGeometriaShape(pImovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
				}
			}
			
			if(!Util.isNull(pImovelRural.getIndVegetacaoNativa()) && pImovelRural.getIndVegetacaoNativa() && !Util.isNullOuVazio(pImovelRural.getVegetacaoNativa())) {
				if(pImovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()){
					geometriaVn = validacaoGeoSeiaService.buscarGeometriaShapeTemporario(pImovelRural.getIdeImovelRural(), TemaGeoseiaEnum.VEGETACAO_NATIVA.getId(), null);
				} else {
					geometriaVn = validacaoGeoSeiaService.buscarGeometriaShape(pImovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
				}
			}
			
			for (App app : pImovelRural.getAppCollection()) {
				if(!app.getIndExcluido()){
					if(app.getIdeLocalizacaoGeografica() == null 
							|| !localizacaoGeograficaService.existeTheGeom(app.getIdeLocalizacaoGeografica())){
						throw new CampoObrigatorioException("Não existe localização geográfica da Área de Preservação Permanente.");
					}
					
					if(app.getIdeTipoApp().getIdeTipoApp().equals(1) && Util.isNullOuVazio(app.getIdeSubTipoApp())){
						throw new CampoObrigatorioException("Deve ser informada a largura do curso d'água natural para APP do tipo Faixa marginal de curso d’água natural.");
					}
					
					//Obtem a geometria da app através do arquivo shape temporário ou diretamente do banco
					if(app.getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()) {
						geometriaApp = validacaoGeoSeiaService.buscarGeometriaShapeTemporario(pImovelRural.getIdeImovelRural(), TemaGeoseiaEnum.APP.getId(), app.getCodigoPersistirShape());
					} else {
						geometriaApp = validacaoGeoSeiaService.buscarGeometriaShape(app.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
					}
					
					//Aplica validação de área e validação do tema em relação ao limite do imóvel
					validacaoGeoSeiaService.validarAreaDeclaradaShapeTemporario(app.getValArea(), geometriaApp);
					validacaoGeoSeiaService.validarLocalizacaoGeografica(null, geometriaApp, null, geometriaIm);
					
					if(!pImovelRural.isImovelINCRA()){
						//Aplica validação de sobreposição entre APP e Vegetação Nativa
						if(!Util.isNullOuVazio(geometriaVn) && validacaoGeoSeiaService.validaPercentualSobreposicao(geometriaApp, geometriaVn)){
							throw new CampoObrigatorioException("Não é possível finalizar o cadastro por ter sido detectada a sobreposição irregular de Área de Preservação Permanente com a Vegetação Nativa.");
						}
						
						//Verifica se a Reserva Legal é do tipo No Próprio Imóvel (2) ou Em Compensação (3)
						if(!Util.isNullOuVazio(pImovelRural.getReservaLegal()) && !Util.isNullOuVazio(pImovelRural.getReservaLegal().getIdeTipoArl()) 
								&& (pImovelRural.getReservaLegal().getIdeTipoArl().getIdeTipoArl().equals(2) 
										|| pImovelRural.getReservaLegal().getIdeTipoArl().getIdeTipoArl().equals(3))){
							//Aplica validação de sobreposição entre APP e Reserva Legal
							if(!Util.isNullOuVazio(geometriaRl) && validacaoGeoSeiaService.validaPercentualSobreposicao(geometriaApp, geometriaRl)){
								if(!validaSobreposicaoRlApp(app)){
									throw new CampoObrigatorioException(Util.getString("cefir_msg_A005"));
								}else{
									context.addCallbackParam("existeSobreposicaoRlApp", true);
								}
							}
						}
					}
				}
			}
		} catch(AreaDeclaradaInvalidaException a) {
			throw new Exception("A área informada da Área de Preservação Permanente ("+a.getAreaDeclarada()+" ha) não confere com a área do shapefile importado ("+a.getAreaCalculada()+" ha).");
		} catch(LocalizacaoGeograficaException l) {
			throw new Exception("Existe geometria de Área de Preservação Permanente que não está dentro do Limite do Imóvel Rural cadastrado.");
		} catch (CampoObrigatorioException c) {
			throw new Exception(c.getMessage());
		} catch (Exception e) {
			throw new Exception("Erro na validação de geometria da Área de Preservação Permanente, contate o administrador do sistema.");
		}
	}
	
	private void validaTheGeomPradApp(ImovelRural pImovelRural) throws Exception{
		try{
			String geometriaApp = null;
			String geometriaPradApp = null;
			
			for (App app : pImovelRural.getAppCollection()) {
				if(!app.getIndExcluido() && !Util.isNullOuVazio(app.getCronogramaRecuperacao())){
					if(app.getCronogramaRecuperacao().getLocalizacaoGeografica() == null 
							|| !localizacaoGeograficaService.existeTheGeom(app.getCronogramaRecuperacao().getLocalizacaoGeografica())){
						throw new CampoObrigatorioException("Não existe localização geográfica da Área Degradada da Área de Preservação Permanente.");
					}
					
					//Obtem a geometria da app através do arquivo shape temporário ou diretamente do banco
					if(app.getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()) {
						geometriaApp = validacaoGeoSeiaService.buscarGeometriaShapeTemporario(pImovelRural.getIdeImovelRural(), TemaGeoseiaEnum.APP.getId(), app.getCodigoPersistirShape());
					} else {
						geometriaApp = validacaoGeoSeiaService.buscarGeometriaShape(app.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
					}
					
					//Obtem a geometria da área degradada da app através do arquivo shape temporário ou diretamente do banco
					if(app.getCronogramaRecuperacao().getLocalizacaoGeografica().getNovosArquivosShapeImportados()) {
						geometriaPradApp = validacaoGeoSeiaService.buscarGeometriaShapeTemporario(pImovelRural.getIdeImovelRural(), TemaGeoseiaEnum.PRAD_APP.getId(), app.getCodigoPersistirShape());
					} else {
						geometriaPradApp = validacaoGeoSeiaService.buscarGeometriaShape(app.getCronogramaRecuperacao().getLocalizacaoGeografica().getIdeLocalizacaoGeografica());
					}
					
					//Aplica validação da poligonal da Área Degradada em relação a APP
					validacaoGeoSeiaService.validarLocalizacaoGeografica(null, geometriaPradApp, null, geometriaApp);
				}
			}
		} catch(LocalizacaoGeograficaException l) {
			throw new Exception("Existe geometria de Área Degradada que não está efetivamente contida na delimitação de Área de Preservação Permanente cadastrada.");
		} catch (CampoObrigatorioException c) {
			throw new Exception(c.getMessage());
		} catch (Exception e) {
			throw new Exception("Erro na validação da geometria de Área Degradada de Área de Preservação Permanente, contate o administrador do sistema.");
		}
	}
	
	private boolean validaSobreposicaoRlApp(App app) {
		if(!Util.isNullOuVazio(app.getIdeTipoEstadoConservacao()) && app.getIdeTipoEstadoConservacao().getIdeTipoEstadoConservacao() == 1)
			return true;
		else if(!Util.isNullOuVazio(app.getIndProcessoRecuperacao()) && app.getIndProcessoRecuperacao())
			return true;
		else
			return false;
	}
}
