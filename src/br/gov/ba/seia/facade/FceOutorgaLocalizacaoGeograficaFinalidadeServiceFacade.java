package br.gov.ba.seia.facade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import br.gov.ba.seia.entity.AnaliseTecnica;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceAquicultura;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeograficaFinalidade;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.enumerator.FceFinalidadeUsoAguaEnum;
import br.gov.ba.seia.service.FceOutorgaLocalizacaoGeograficaFinalidadeService;
import br.gov.ba.seia.service.FceService;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceOutorgaLocalizacaoGeograficaFinalidadeServiceFacade {
	
	@EJB
	private FceService fceService;
	@EJB
	private FceOutorgaLocalizacaoGeograficaFinalidadeService fceOutorgaLocalizacaoGeograficaFinalidadeService;
	@EJB
	private FceOutorgaAquiculturaServiceFacade fceOutorgaAquiculturaServiceFacade; 

	/**
	 * Método que busca o {@link Fce} daquela {@link AnaliseTecnica} pelo seu {@link DocumentoObrigatorio}.
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @param analiseTecnica
	 * @param doc
	 * @return {@link Fce}
	 * @throws Exception
	 * @since 01/03/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/">#</a> #ticket
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Fce buscarFce(AnaliseTecnica analiseTecnica, DocumentoObrigatorio doc) throws Exception{
		return fceService.buscarFcePor(analiseTecnica, doc);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceOutorgaLocalizacaoGeograficaFinalidade> listarTipoFinalidadeUsoAguaByFce(FceOutorgaLocalizacaoGeografica  fceOutorgaLocalizacaoGeografica) throws Exception{
		return fceOutorgaLocalizacaoGeograficaFinalidadeService.listarTipoFinalidadeUsoAguaByFce(fceOutorgaLocalizacaoGeografica);	 
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceOutorgaLocalizacaoGeograficaFinalidade> listarFceOutorgaLocalizacaoGeograficaFinalidadeByTipoFinalidade(TipoFinalidadeUsoAgua finalidadeUsoAgua, AnaliseTecnica analiseTecnica) throws Exception{
		return fceOutorgaLocalizacaoGeograficaFinalidadeService.listarTipoFinalidadeUsoAguaByFinalidade(finalidadeUsoAgua, analiseTecnica);	 
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceOutorgaLocalizacaoGeograficaFinalidade(FceOutorgaLocalizacaoGeograficaFinalidade fceOutorgaLocalizacaoGeograficaFinalidade)throws Exception{
		fceOutorgaLocalizacaoGeograficaFinalidadeService.excluir(fceOutorgaLocalizacaoGeograficaFinalidade);
	}

	/**
	 * Método que verifica qual {@link DocumentoObrigatorio} representa à {@link TipoFinalidadeUsoAgua} do parâmetro e agrega ao {@link Map} a função para exclusão do {@link Fce} correspondente.
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @param finalidadeUsoAgua
	 * @throws Exception 
	 * @since 29/02/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7437">#7437</a>
	 */
	public Map<String,Object> obterParametrosBy(TipoFinalidadeUsoAgua finalidadeUsoAgua){
		DocumentoObrigatorio doc = null;
		String function = " ";
		if(finalidadeUsoAgua.equals(FceFinalidadeUsoAguaEnum.ABASTECIMENTO_HUMANO.getIdeTipoFinalidadeUsoAgua())){
			doc = FceFinalidadeUsoAguaEnum.ABASTECIMENTO_HUMANO.getIdeDocumentoObrigatorio();
			function = "remover_fce_abastecimento_humano_by_fce(:pFce)";
		} 
		else if(finalidadeUsoAgua.equals(FceFinalidadeUsoAguaEnum.ABASTECIMENTO_INDUSTRIAL.getIdeTipoFinalidadeUsoAgua())){
			doc = FceFinalidadeUsoAguaEnum.ABASTECIMENTO_INDUSTRIAL.getIdeDocumentoObrigatorio();
			function = "remover_fce_abastecimento_industrial_by_fce(:pFce)";
		}
		else if(finalidadeUsoAgua.equals(FceFinalidadeUsoAguaEnum.IRRIGACAO.getIdeTipoFinalidadeUsoAgua())){
			doc = FceFinalidadeUsoAguaEnum.IRRIGACAO.getIdeDocumentoObrigatorio();
			function = "remover_fce_irrigacao_by_fce(:pFce)";
		}
		else if(finalidadeUsoAgua.equals(FceFinalidadeUsoAguaEnum.PULVERIZACAO.getIdeTipoFinalidadeUsoAgua())){
			doc = FceFinalidadeUsoAguaEnum.PULVERIZACAO.getIdeDocumentoObrigatorio();
			function = "remover_fce_pulverizacao_by_fce(:pFce)";
		}
		else if(finalidadeUsoAgua.equals(FceFinalidadeUsoAguaEnum.DESSEDENTACAO_ANIMAL.getIdeTipoFinalidadeUsoAgua())){
			doc = FceFinalidadeUsoAguaEnum.DESSEDENTACAO_ANIMAL.getIdeDocumentoObrigatorio();
			function = "remover_fce_dessedentacao_animal_by_fce(:pFce)";
		}
		else if(finalidadeUsoAgua.equals(FceFinalidadeUsoAguaEnum.AQUICULTURA_VIVEIRO_ESCAVADO.getIdeTipoFinalidadeUsoAgua())){
			doc = FceFinalidadeUsoAguaEnum.AQUICULTURA_VIVEIRO_ESCAVADO.getIdeDocumentoObrigatorio();
			function = "remover_fce_outorga_aquicultura_by_fce(:pFce)";
		}
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("doc", doc);
		map.put("function", function);
		return map;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void apagarFce(String function, Fce fceToExcluir) throws Exception{
		fceService.apagarFceByFunction("SELECT " + function, fceToExcluir);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceAquicultura> listarFceAquiculturaBy(Fce fce) throws Exception{
		return fceOutorgaAquiculturaServiceFacade.listarFceAquiculturaByIdeFce(fce); 
	}
	
}