package br.gov.ba.seia.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.DeclaracaoIntervencaoAppDAOImpl;
import br.gov.ba.seia.entity.AtoDeclaratorio;
import br.gov.ba.seia.entity.DeclaracaoIntervencaoApp;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.facade.LocalizacaoGeograficaServiceFacade;
import br.gov.ba.seia.util.Util;

/**
 * @author eduardo.fernandes 
 * @since 11/01/2017
 * @see <a href="http://10.105.17.77/redmine/issues/8263">#8263</a>
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DeclaracaoIntervencaoAppService {

	@Inject
	private DeclaracaoIntervencaoAppDAOImpl dao;
	
	@EJB
	private DeclaracaoIntervencaoAppCaracteristcaService declaracaoIntervencaoAppCaracteristcaService;

	@EJB
	private LocalizacaoGeograficaServiceFacade locGeoFacade;
	
	/**
	 * Método para salvar/atualizar a {@link DeclaracaoIntervencaoApp}
	 * 
	 * @author eduardo.fernandes 
	 * @since 11/01/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8263">#8263</a>
	 * @param declaracaoIntervencaoApp
	 * @throws Exception 
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(DeclaracaoIntervencaoApp declaracaoIntervencaoApp) {
		dao.salvar(declaracaoIntervencaoApp);
	}

	/**
	 * Método para buscar a {@link DeclaracaoIntervencaoApp} de acordo com o parâmetro.
	 * 
	 * @author eduardo.fernandes 
	 * @since 12/01/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8263">#8263</a>
	 * @param requerimento
	 * @return
	 * @throws Exception 
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DeclaracaoIntervencaoApp buscar(Requerimento requerimento) {
		DeclaracaoIntervencaoApp diap = dao.buscar(requerimento);
		if(Util.isNull(diap)){
			return new DeclaracaoIntervencaoApp(new AtoDeclaratorio(requerimento, DocumentoObrigatorioEnum.FORMULARIO_DIAP));
		} 
		else {
			if(!Util.isNullOuVazio(diap.getIdeLocalizacaoGeografica())){
				locGeoFacade.tratarPonto(diap.getIdeLocalizacaoGeografica());
			}
			else {
				diap.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
			}
			diap.setDeclaracaoIntervencaoAppCaracteristcas(declaracaoIntervencaoAppCaracteristcaService.listar(diap));
			return diap;
		}
	}
}
