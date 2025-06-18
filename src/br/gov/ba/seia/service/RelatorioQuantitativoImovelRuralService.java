package br.gov.ba.seia.service;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.dao.ImovelRuralRelatorioDAOImpl;
import br.gov.ba.seia.dto.ColunasExibirRelatorioQuantitativoImoveisRuraisDTO;
import br.gov.ba.seia.dto.FiltroImovelRuralRelatorioDTO;
import br.gov.ba.seia.util.RelatorioUtil;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RelatorioQuantitativoImovelRuralService {
	
	@Inject
	ImovelRuralRelatorioDAOImpl imovelRuralRelatorioDAOImpl;
	
	public StreamedContent imprimirRelatorioQuantitativoImoveisRurais(FiltroImovelRuralRelatorioDTO filtroRelatorioQuantitativoImoveisRuraisDTO, 
																	  ColunasExibirRelatorioQuantitativoImoveisRuraisDTO colunasExibirRelatorioQuantitativoImoveisRuraisDTO, 
																	  Map<String, String> mapFiltros, String listaIdImovel, int listaImoveisSize) throws Exception {
		
		Map<String, Object> lParametros = new HashMap<String, Object>();
		
		lParametros.put("consulta", imovelRuralRelatorioDAOImpl.montarConsultaImovelRuralRelatorioQuantitativo(filtroRelatorioQuantitativoImoveisRuraisDTO, colunasExibirRelatorioQuantitativoImoveisRuraisDTO, listaIdImovel));
		lParametros.put("observacao", imovelRuralRelatorioDAOImpl.montarConsultaImovelRuralRelatorioQuantitativoObs(filtroRelatorioQuantitativoImoveisRuraisDTO, colunasExibirRelatorioQuantitativoImoveisRuraisDTO, listaIdImovel, listaImoveisSize));
		lParametros.put("exibeImoveisRurais", colunasExibirRelatorioQuantitativoImoveisRuraisDTO.isImoveisRurais());
		lParametros.put("exibeReservaLegal", colunasExibirRelatorioQuantitativoImoveisRuraisDTO.isReservaLegal());
		lParametros.put("exibeApp", colunasExibirRelatorioQuantitativoImoveisRuraisDTO.isApp());
		lParametros.put("exibeAreaProdutiva", colunasExibirRelatorioQuantitativoImoveisRuraisDTO.isAreaProdutiva());
		lParametros.put("exibeVegetacaoNativa", colunasExibirRelatorioQuantitativoImoveisRuraisDTO.isVegetacaoNativa());
		lParametros.put("exibeRppn", colunasExibirRelatorioQuantitativoImoveisRuraisDTO.isRppn());
		lParametros.put("exibeUsoAgua", colunasExibirRelatorioQuantitativoImoveisRuraisDTO.isUsoAgua());
		lParametros.put("exibeOutrosPassivos", colunasExibirRelatorioQuantitativoImoveisRuraisDTO.isOutrosPassivos());
		
		lParametros.put("nomeProprietario", mapFiltros.get("nomeProprietario"));
		lParametros.put("nomeProcurador", mapFiltros.get("nomeProcurador"));
		lParametros.put("nomeImovel", mapFiltros.get("nomeImovel"));
		lParametros.put("nomeMunicipio", mapFiltros.get("nomeMunicipio"));
		lParametros.put("filtroPorTipo", mapFiltros.get("filtroPorTipo"));
		lParametros.put("filtroPorSelecao", mapFiltros.get("filtroPorSelecao"));
		lParametros.put("statusCadastro", mapFiltros.get("statusCadastro"));
		lParametros.put("dtFinalizacao", mapFiltros.get("dtFinalizacao"));
		lParametros.put("dtSincronizacao", mapFiltros.get("dtSincronizacao"));
		lParametros.put("gestorFinanceiro", mapFiltros.get("gestorFinanceiro"));
		lParametros.put("contratoConvenio", mapFiltros.get("contratoConvenio"));
		lParametros.put("modulosFiscais", mapFiltros.get("modulosFiscais"));
		lParametros.put("tipoReservalegal", mapFiltros.get("tipoReservalegal"));
		lParametros.put("documentoFinal", mapFiltros.get("documentoFinal"));
		lParametros.put("numeroDocumentoFinal", mapFiltros.get("numeroDocumentoFinal"));
		lParametros.put("imoveisCom", mapFiltros.get("imoveisCom"));
		lParametros.put("nomeRT", mapFiltros.get("nomeResponsavelTecnico"));


		return new RelatorioUtil("relatorioQuantitativoImovelNovo.jasper", lParametros, "logoInemaRelatorio.png", "logoSemaRelatorio.png").gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);		
	}
}
