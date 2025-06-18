package br.gov.ba.seia.facade;

import java.util.Collection;
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
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.CerhCaracterizacaoDAOImpl;
import br.gov.ba.seia.dao.CerhLocalizacaoGeograficaDAOImpl;
import br.gov.ba.seia.dao.CerhSituacaoTipoUsoDAOImpl;
import br.gov.ba.seia.dao.CerhTipoUsoDAOImpl;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.TipoCorpoHidricoDAOImpl;
import br.gov.ba.seia.entity.Cerh;
import br.gov.ba.seia.entity.CerhLocalizacaoGeografica;
import br.gov.ba.seia.entity.CerhProcesso;
import br.gov.ba.seia.entity.CerhSituacaoTipoUso;
import br.gov.ba.seia.entity.CerhTipoUso;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.TipoCorpoHidrico;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.TipoUsoRecursoHidrico;
import br.gov.ba.seia.enumerator.TipoUsoRecursoHidricoEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.interfaces.CerhCaracterizacaoInterface;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.service.TipoFinalidadeUsoAguaService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhAbasFacade {

	@EJB
	protected LocalizacaoGeograficaServiceFacade localizacaoGeograficaServiceFacade;
	
	@EJB
	protected TipoFinalidadeUsoAguaService tipoFinalidadeUsoAguaService;
	
	@EJB
	protected MunicipioService municipioService;
	
	@EJB
	protected CerhSituacaoTipoUsoDAOImpl cerhSituacaoTipoUsoDAO;
	
	@EJB
	protected CerhTipoUsoDAOImpl cerhTipoUsoDAO;
	
	@EJB
	protected CerhLocalizacaoGeograficaDAOImpl cerhLocalizacaoGeograficaDAO;
	
	@EJB
	protected CerhCaracterizacaoDAOImpl cerhCaracterizacaoDAO;
	
	@EJB
	private TipoCorpoHidricoDAOImpl corpoHidricoDAO;

	
	@Inject
	private IDAO<CerhLocalizacaoGeografica> cerhLocalizacaoGeograficaIDao;
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Municipio carregarMunicipio(CerhLocalizacaoGeografica cerhLocalizacaoGeografica) {
		return municipioService.carregarMunicipioByLocalizacaoGeografica(cerhLocalizacaoGeografica.getIdeLocalizacaoGeografica());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoCorpoHidrico> listarCorpoHidrico() {
		return corpoHidricoDAO.listar();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TipoCorpoHidrico carregarTipoCorpoHidrico(Integer ide)  {
		return corpoHidricoDAO.buscar(Restrictions.eq("ideTipoCorpoHidrico", ide));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhSituacaoTipoUso> listarCerhSituacaoTipoUso() {
		return cerhSituacaoTipoUsoDAO.listar();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhSituacaoTipoUso carregarCerhSituacaoTipoUso(Integer ideCerhSituacaoTipoUso)  {
		return cerhSituacaoTipoUsoDAO.buscar(new CerhSituacaoTipoUso(ideCerhSituacaoTipoUso));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhLocalizacaoGeografica> listarCerhLocalizacaoGeografica(CerhProcesso cerhProcesso, TipoUsoRecursoHidricoEnum tipoUsoRecursoHidricoEnum) {
		return cerhLocalizacaoGeograficaDAO.listar(cerhProcesso, tipoUsoRecursoHidricoEnum);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoFinalidadeUsoAgua> listarTipoFinalidadeUsoAgua(TipologiaEnum tipologiaEnum) throws Exception {
		return tipoFinalidadeUsoAguaService.listarTipoFinalidadeUsoAguaCerh(tipologiaEnum);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoFinalidadeUsoAgua> listarFinalidadesConcedidas(String numProcesso, TipoUsoRecursoHidricoEnum tipoUsoRecursoHidricoEnum) throws Exception{
		return tipoFinalidadeUsoAguaService.listarTipoFinalidadeUsoAguaBy(numProcesso, tipoUsoRecursoHidricoEnum);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void prepararLocalizacaoGeografica(CerhLocalizacaoGeografica cerhLocalizacaoGeografica) {
		if(Util.isNullOuVazio(cerhLocalizacaoGeografica.getNomeMunicipio())) {
			String nomes = "";
			Collection<Double> listaIbge = localizacaoGeograficaServiceFacade.listarCodIBGE(cerhLocalizacaoGeografica.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
			if(!Util.isNullOuVazio(listaIbge)){
				List<Municipio> muncipios = (List<Municipio>) municipioService.listarMunicipioPorListaIBGE(listaIbge);
				for (Municipio municipio : muncipios) {
					nomes = nomes.concat(municipio.getNomMunicipio());
					if(muncipios.size() > 1) {
						if(!muncipios.get(muncipios.size() - 1).equals(municipio)) {
							nomes = nomes.concat(", ");
						} 
						else {
							nomes = nomes.concat("e ");
						}
					} 
				}
			}
			cerhLocalizacaoGeografica.setNomeMunicipio(nomes);
			cerhLocalizacaoGeografica.getIdeLocalizacaoGeografica().setBaciaHidrografica(localizacaoGeograficaServiceFacade.getBacia(cerhLocalizacaoGeografica.getIdeLocalizacaoGeografica()));
			cerhLocalizacaoGeografica.getIdeLocalizacaoGeografica().setRpga(localizacaoGeograficaServiceFacade.getRPGA(cerhLocalizacaoGeografica.getIdeLocalizacaoGeografica()));
		}
	}

	public void tratarPonto(CerhLocalizacaoGeografica cerhLocalizacaoGeografica) {
		localizacaoGeograficaServiceFacade.tratarPonto(cerhLocalizacaoGeografica.getIdeLocalizacaoGeografica());
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isTheGeomPersistido(LocalizacaoGeografica localizacaoGeografica) {
		try {
			return !Util.isNullOuVazio(localizacaoGeograficaServiceFacade.retornarGeometriaShapeByLocalizacaoGeografica(localizacaoGeografica));
		} 
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações da localização geográfica.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCerhLocalizacaoGeografica(Cerh cerh, CerhLocalizacaoGeografica cerhLocalizacaoGeografica, TipoUsoRecursoHidricoEnum recursoHidricoEnum) {
		if(Util.isNull(cerhLocalizacaoGeografica.getIdeCerhTipoUso())) {
			if(!Util.isNull(cerhLocalizacaoGeografica.getIdeCerhProcesso())) {
				for (CerhTipoUso cerhTipoUso : cerhLocalizacaoGeografica.getIdeCerhProcesso().getCerhTipoUsoCollection()) {
					if(cerhTipoUso.getIdeTipoUsoRecursoHidrico().equals(new TipoUsoRecursoHidrico(recursoHidricoEnum))) {
						cerhLocalizacaoGeografica.setIdeCerhTipoUso(cerhTipoUso);
						break;
					}
				}
			}else {
				for (CerhTipoUso cerhTipoUso : cerh.getCerhTipoUsoCollection()) {
					if(cerhTipoUso.getIdeTipoUsoRecursoHidrico().equals(new TipoUsoRecursoHidrico(recursoHidricoEnum))) {
						cerhLocalizacaoGeografica.setIdeCerhTipoUso(cerhTipoUso);
						break;
					}
				}
			}
		}else {
			if(!Util.isNullOuVazio(cerh.getCerhTipoUsoCollection())) {
				for (CerhTipoUso cerhTipoUso : cerh.getCerhTipoUsoCollection()) {
					if(cerhTipoUso.getIdeTipoUsoRecursoHidrico().equals(new TipoUsoRecursoHidrico(recursoHidricoEnum))) {
						cerhLocalizacaoGeografica.setIdeCerhTipoUso(cerhTipoUso);
						break;
					}
				}
			}
		}
		cerhLocalizacaoGeograficaDAO.salvar(cerhLocalizacaoGeografica);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void excluirCerhLocGeo(Integer ideCerhLocalizacaoGeografica)  {
		cerhLocalizacaoGeograficaDAO.excluir(ideCerhLocalizacaoGeografica);
	}
	

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void  excluirCerhLocalizacaoGeografica(CerhLocalizacaoGeografica ideCerhLocalizacaoGeografica)  {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CerhLocalizacaoGeografica.class)
				.add(Restrictions.eq("ideLocalizacaoGeografica.ideLocalizacaoGeografica", ideCerhLocalizacaoGeografica.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica()));
		
		CerhLocalizacaoGeografica cerhLocalizacaoGeografica = cerhLocalizacaoGeograficaIDao.buscarPorCriteria(detachedCriteria);
		
		if(cerhLocalizacaoGeografica!=null){
			excluirCerhLocGeo(cerhLocalizacaoGeografica.getIdeCerhLocalizacaoGeografica());
		}
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void excluirLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica){
		localizacaoGeograficaServiceFacade.excluirDadoGeografico(ideLocalizacaoGeografica);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCerhCaracterizacao(CerhCaracterizacaoInterface caracterizacaoIF){
		cerhCaracterizacaoDAO.salvar(caracterizacaoIF);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizarCerhCaracterizacao(CerhCaracterizacaoInterface caracterizacaoIF){
		cerhCaracterizacaoDAO.salvarOuAtualizar(caracterizacaoIF);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCaracterizacaoHistorico(CerhCaracterizacaoInterface caracterizacaoIF) {
		cerhLocalizacaoGeograficaDAO.salvar(caracterizacaoIF.getIdeCerhLocalizacaoGeografica());
		cerhCaracterizacaoDAO.salvar(caracterizacaoIF);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void excluirCerhCaracterizacao(CerhCaracterizacaoInterface caracterizacaoIF){
		cerhCaracterizacaoDAO.excluir(caracterizacaoIF);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirCaracterizacao(CerhCaracterizacaoInterface caracterizacaoInterface){
		if(!Util.isNull(caracterizacaoInterface.getId())){
			excluirCerhCaracterizacao(caracterizacaoInterface);
		}
		excluirCerhLocGeo(caracterizacaoInterface.getIdeCerhLocalizacaoGeografica().getIdeCerhLocalizacaoGeografica());
		excluirLocalizacaoGeografica(caracterizacaoInterface.getIdeCerhLocalizacaoGeografica().getIdeLocalizacaoGeografica());
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public LocalizacaoGeografica duplicarLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica) throws Exception{
		return localizacaoGeograficaServiceFacade.duplicarLocalizacaoGeografica(localizacaoGeografica);		
	}	
	
	public void salvarCerhTipoUso(CerhTipoUso cerhTipoUso)  {
		cerhTipoUsoDAO.salvar(cerhTipoUso);
	}
	
	public void remover(CerhTipoUso cerhTipoUso)  {
		cerhTipoUsoDAO.remover(cerhTipoUso);
	}
	
	public void removerSemProcesso(CerhTipoUso cerhTipoUso)  {
		cerhTipoUsoDAO.removerSemProcesso(cerhTipoUso);
	}
	
	public Collection<CerhTipoUso> listarPorCerhProcesso(CerhProcesso cerhProcesso) {
		return cerhTipoUsoDAO.listar(cerhProcesso);
	}
	
	public Collection<CerhTipoUso> listarPorProcesso(CerhProcesso cerhProcesso) {
		return cerhTipoUsoDAO.listarPorProcesso(cerhProcesso);
	}
	
	public Collection<CerhTipoUso> listarTipoUso(Cerh cerh)  {
		return cerhTipoUsoDAO.listarCerhTipoUsoPor(cerh);
	}	

}