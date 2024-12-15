/**
 * 
 */
package br.gov.ba.seia.service;

import java.util.List;

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

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.DadoGeografico;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceBarragLicencLocaGeo;
import br.gov.ba.seia.entity.FceBarragem;
import br.gov.ba.seia.entity.FceBarragemLicenciamento;
import br.gov.ba.seia.entity.FceIntervencaoBarragem;
import br.gov.ba.seia.util.Util;

/**
 * @author lesantos
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceBarragemService {
	
	@Inject
	IDAO<FceBarragem> idao;
	
	@Inject
	IDAO<FceBarragLicencLocaGeo> idaoLicenLocaGeo;
	
	@Inject
	IDAO<FceBarragemLicenciamento> idaoLicenLoca;
	
	@Inject
	IDAO<FceIntervencaoBarragem> idaoIntervencaoBarragem;
	
	@EJB
	private FceIntervencaoBarragemService fceIntervencaoBarragemService;
	
	@EJB
	private LocalizacaoGeograficaService localizacaoGeograficaService;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(FceBarragem fceBarragem) {
		idao.salvar(fceBarragem);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(FceBarragem fceBarragem) {
		idao.salvarOuAtualizar(fceBarragem);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(FceBarragem fceBarragem) {
		idao.atualizar(fceBarragem);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceBarragem buscarFcebarragem(Fce fce)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceBarragem.class);
		criteria.createAlias("ideFce", "fce");
		criteria.createAlias("localizacaoGeografica", "localizacaoGeografica", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("usosReservatorio", "usosReservatorio", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("fceBarragemLicenciamento", "fceBarragemLicenciamento", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("fceBarragemLicenciamento.poligonalApp", "poligonalApp", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("fceBarragemLicenciamento.poligonalAreaSuprimida", "poligonalAreaSuprimida", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("fce.ideFce", fce.getIdeFce()));
		criteria.add(Restrictions.eq("fce.ideDocumentoObrigatorio.ideDocumentoObrigatorio", fce.getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio()));
		FceBarragem fceBarragem = idao.buscarPorCriteria(criteria);
		if(!Util.isNullOuVazio(fceBarragem)){
			//interveção barragem
			criteria = DetachedCriteria.forClass(FceIntervencaoBarragem.class);
			criteria.createAlias("ideOutorgaLocalizacaoGeografica", "ideOutorgaLocalizacaoGeografica", JoinType.LEFT_OUTER_JOIN);
			criteria.createAlias("ideFceOutorgaLocalizacaoGeografica", "ideFceOutorgaLocalizacaoGeografica", JoinType.LEFT_OUTER_JOIN);
			
			criteria.createAlias("ideOutorgaLocalizacaoGeografica.ideOutorga", "ideOutorgaLocalizacaoGeografica.ideOutorga", JoinType.LEFT_OUTER_JOIN);
			
			criteria.createAlias("ideOutorgaLocalizacaoGeografica.ideLocalizacaoGeografica", "ideLocalizacaoGeografica", JoinType.LEFT_OUTER_JOIN);
			criteria.createAlias("ideOutorgaLocalizacaoGeografica.ideLocalizacaoGeografica.dadoGeograficoCollection", "dadoGeograficoCollection", JoinType.LEFT_OUTER_JOIN);
			criteria.createAlias("ideTipoBarragem", "ideTipoBarragem", JoinType.LEFT_OUTER_JOIN);
			criteria.createAlias("ideDocumentoObrigatorioRequerimento", "ideDocumentoObrigatorioRequerimento", JoinType.LEFT_OUTER_JOIN);
			
			criteria.add(Restrictions.disjunction()
		            .add(Restrictions.eq("fceBarragem", fceBarragem))
		            .add(Restrictions.eq("ideFce", fce))
		    );
			List<FceIntervencaoBarragem> listFceIntervencaoBarragems = idaoIntervencaoBarragem.listarPorCriteria(criteria);
			for(FceIntervencaoBarragem i : listFceIntervencaoBarragems){
				i.setIdeFce(fce);
			}
			fceBarragem.setFceIntervencaoBarragems(listFceIntervencaoBarragems);
			//Licenciamento
			if(!Util.isNullOuVazio(fceBarragem.getFceBarragemLicenciamento())) {
				//Obras
				criteria = DetachedCriteria.forClass(FceBarragemLicenciamento.class);
				criteria.createAlias("obrasInfraComplementares", "obrasInfraComplementares", JoinType.LEFT_OUTER_JOIN);
				criteria.add(Restrictions.eq("fceBarragem", fceBarragem));
				fceBarragem.getFceBarragemLicenciamento().setObrasInfraComplementares(idaoLicenLoca.buscarPorCriteria(criteria).getObrasInfraComplementares());
				//Materiais 
				criteria = DetachedCriteria.forClass(FceBarragemLicenciamento.class);
				criteria.createAlias("materialUtilizados", "materialUtilizados", JoinType.LEFT_OUTER_JOIN);
				criteria.add(Restrictions.eq("fceBarragem", fceBarragem));
				fceBarragem.getFceBarragemLicenciamento().setMaterialUtilizados(idaoLicenLoca.buscarPorCriteria(criteria).getMaterialUtilizados());
				//localização Geografica
				criteria = DetachedCriteria.forClass(FceBarragLicencLocaGeo.class);
				
				criteria.createAlias("ideLocalGeoInicioEixo", "ideLocalGeoInicioEixo", JoinType.LEFT_OUTER_JOIN);
				criteria.createAlias("ideLocalGeoFimEixo", "ideLocalGeoFimEixo", JoinType.LEFT_OUTER_JOIN);
				criteria.add(Restrictions.eq("fceBarragemLicenciamento", fceBarragem.getFceBarragemLicenciamento()));
				
				List<FceBarragLicencLocaGeo> listaFceBarragLicencLocaGeo = idaoLicenLocaGeo.listarPorCriteria(criteria);
				
				if (!Util.isNullOuVazio(listaFceBarragLicencLocaGeo)){
					for (FceBarragLicencLocaGeo fceBarragLicencLocaGeo : listaFceBarragLicencLocaGeo) {
						List<DadoGeografico> listaInicioDadoGeografico = localizacaoGeograficaService.listarDadoGeografico(fceBarragLicencLocaGeo.getIdeLocalGeoInicioEixo(), fceBarragLicencLocaGeo.getIdeLocalGeoInicioEixo().getIdeClassificacaoSecao().getIdeClassificacaoSecao());
						List<DadoGeografico> listaFimDadoGeografico = localizacaoGeograficaService.listarDadoGeografico(fceBarragLicencLocaGeo.getIdeLocalGeoFimEixo(), fceBarragLicencLocaGeo.getIdeLocalGeoFimEixo().getIdeClassificacaoSecao().getIdeClassificacaoSecao());
				
						fceBarragLicencLocaGeo.getIdeLocalGeoInicioEixo().setDadoGeograficoCollection(listaInicioDadoGeografico);
						fceBarragLicencLocaGeo.getIdeLocalGeoFimEixo().setDadoGeograficoCollection(listaFimDadoGeografico);
					}
				}
				
				fceBarragem.getFceBarragemLicenciamento().setBarragemLicenciamentoLocalizacaoGeo(listaFceBarragLicencLocaGeo);
				fceBarragem.getFceBarragemLicenciamento().setFceBarragem(fceBarragem);
				fceBarragem.setIdeFce(fce);
			}
		}
		return fceBarragem;
	}
	
	
}
