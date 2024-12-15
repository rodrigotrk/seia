package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.Collection;
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
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.controller.FceGeracaoEnergiaController;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceEnergia;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.facade.AdicionarMunicipiosServiceFacade;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceEnergiaService {
	
	@Inject
	private IDAO<FceEnergia> fceEnergiaIDAO;
	
	@Inject
	private IDAO<String> daoString;
	
	@EJB
	private MunicipioService municipioService;
	
	@EJB
	private AdicionarMunicipiosServiceFacade adicionarMunicipioServiceFacade;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(FceEnergia fceEnergia) {
		this.fceEnergiaIDAO.salvarOuAtualizar(fceEnergia);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceEnergia buscarByIdFce(Fce fce) {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(FceEnergia.class)
		.createAlias("ideLocalizacaoGeografica", "ideLocalizacaoGeografica",JoinType.LEFT_OUTER_JOIN)		
		.add(Restrictions.eq("ideFce.ideFce", fce.getIdeFce())); 
				
		return this.fceEnergiaIDAO.buscarPorCriteria(detachedCriteria);
	}
	
	public String obterMunicipioByLocalizacao(LocalizacaoGeografica loc) {
		
		String retorno = null;
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideLocalizacaoGeografica", loc.getIdeLocalizacaoGeografica());
		
		Collection<Municipio> listaMunicipioIBGE = new ArrayList<Municipio>();
		List<String> lista = daoString.buscarPorNativeQuery("select * from sp_get_municipio(:ideLocalizacaoGeografica)", parametros);
		if(!Util.isNullOuVazio(lista)) {
			Collection<Double> listarIBGE = new ArrayList<Double>();
			for(String s : lista) {
				listarIBGE.add(Double.valueOf(s));
			}
			
			listaMunicipioIBGE = municipioService.listarMunicipioPorListaIBGE(listarIBGE);
		}
		
		if(!Util.isNullOuVazio(listaMunicipioIBGE)){
			StringBuilder str = new StringBuilder();
			Integer tamanho = listaMunicipioIBGE.size();
			for(Municipio m: listaMunicipioIBGE){
				if(!Util.isNullOuVazio(m.getNomMunicipio())){
					str.append(m.getNomMunicipio());
					if(tamanho > 1){
						str.append("-");
						tamanho++;
					}
				}
			}
			
			if(!Util.isNullOuVazio(str)){
				retorno = str.toString();
			}
		}
		
		return retorno;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizar(FceGeracaoEnergiaController ctrl) throws Exception  {
		ctrl.prepararParaFinalizar();
	}
}
