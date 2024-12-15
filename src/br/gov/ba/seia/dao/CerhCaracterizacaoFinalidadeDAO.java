package br.gov.ba.seia.dao;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.CerhBarragemCaracterizacaoFinalidade;
import br.gov.ba.seia.entity.CerhCaptacaoAbastecimentoIndustrial;
import br.gov.ba.seia.entity.CerhCaptacaoAbastecimentoPublico;
import br.gov.ba.seia.entity.CerhCaptacaoCaracterizacaoFinalidade;
import br.gov.ba.seia.enumerator.TipoFinalidadeUsoAguaEnum;
import br.gov.ba.seia.entity.CerhCaptacaoDadosIrrigacao;
import br.gov.ba.seia.entity.CerhCaptacaoDadosMineracao;
import br.gov.ba.seia.entity.CerhCaptacaoMineracaoExtracaoAreia;
import br.gov.ba.seia.entity.CerhCaptacaoOutrosUsos;
import br.gov.ba.seia.entity.CerhCaptacaoTermoeletrica;
import br.gov.ba.seia.entity.CerhCaptacaoTransposicao;
import br.gov.ba.seia.interfaces.CerhFinalidadeUsoAguaInterface;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhCaracterizacaoFinalidadeDAO {

	@Inject
	private IDAO<CerhFinalidadeUsoAguaInterface> dao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(CerhFinalidadeUsoAguaInterface caracterizacaoIF)  {
		StringBuilder hql = new StringBuilder();
		
		if(caracterizacaoIF instanceof CerhBarragemCaracterizacaoFinalidade){
			String aproveitamento = "DELETE from CerhBarragemAproveitamentoHidreletrico where ideCerhBarragemCaracterizacaoFinalidade.ideCerhBarragemCaracterizacaoFinalidade = :ide_cerh_barragem_caracterizacao_finalidade ";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("ide_cerh_barragem_caracterizacao_finalidade", caracterizacaoIF.getId());
			dao.executarQuery(aproveitamento, params);
		}
		

		if(caracterizacaoIF instanceof CerhCaptacaoCaracterizacaoFinalidade){
			CerhCaptacaoCaracterizacaoFinalidade cerhCaptacaoCaracterizacaoFinalidade = (CerhCaptacaoCaracterizacaoFinalidade) caracterizacaoIF;
			
			if(cerhCaptacaoCaracterizacaoFinalidade.getIdeTipoFinalidadeUsoAgua().getId().equals(TipoFinalidadeUsoAguaEnum.MINERACAO_EXTRACAO_AREIA.getId())){
				hql.append("DELETE FROM CerhCaptacaoMineracaoExtracaoArea c WHERE c.ideCerhCaptacaoMineracaoExtracaoArea = :ideCerhCaptacaoMineracaoExtracaoArea");
			
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("ideCerhCaptacaoMineracaoExtracaoArea", cerhCaptacaoCaracterizacaoFinalidade.getIdeCerhCaptacaoMineracaoExtracaoAreia().getIde());
				
				dao.executarQuery(hql.toString(), params);
			}
			
		}

		if(caracterizacaoIF instanceof CerhCaptacaoCaracterizacaoFinalidade){
			deleteDependencias(CerhCaptacaoTermoeletrica.class,caracterizacaoIF.getId());
			deleteDependencias(CerhCaptacaoAbastecimentoIndustrial.class,caracterizacaoIF.getId());
			deleteDependencias(CerhCaptacaoDadosIrrigacao.class,caracterizacaoIF.getId());
			deleteDependencias(CerhCaptacaoAbastecimentoPublico.class,caracterizacaoIF.getId());
			deleteDependencias(CerhCaptacaoTransposicao.class,caracterizacaoIF.getId());
			deleteDependencias(CerhCaptacaoDadosIrrigacao.class,caracterizacaoIF.getId());
			deleteDependencias(CerhCaptacaoMineracaoExtracaoAreia.class,caracterizacaoIF.getId());
			deleteDependencias(CerhCaptacaoDadosMineracao.class,caracterizacaoIF.getId());
			deleteDependencias(CerhCaptacaoOutrosUsos.class,caracterizacaoIF.getId());
		}
		
		String entity = caracterizacaoIF.getClass().getSimpleName();
		hql.append("DELETE FROM "+entity+" c WHERE c.ide"+entity+" = :ide"+entity);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ide"+entity, caracterizacaoIF.getId());
		
		dao.executarQuery(hql.toString(), params);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deleteDependencias(Class<?> clazz, Integer id) {
		StringBuilder hql = new StringBuilder();
		hql.append("DELETE FROM "+clazz.getSimpleName()+" c WHERE c.ideCerhCaptacaoCaracterizacaoFinalidade.ideCerhCaptacaoCaracterizacaoFinalidade = :ideCerhCaptacaoCaracterizacaoFinalidade");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideCerhCaptacaoCaracterizacaoFinalidade", id);
		
		dao.executarQuery(hql.toString(), params);
	}
	
}
