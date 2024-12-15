package br.gov.ba.seia.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.dao.ImovelRuralRelatorioDAOIf;
import br.gov.ba.seia.dto.ColunasExibirImovelRuralRelatorioDTO;
import br.gov.ba.seia.dto.FiltroImovelRuralRelatorioDTO;
import br.gov.ba.seia.dto.ImovelRuralRelatorioDTO;
import br.gov.ba.seia.util.RelatorioUtil;
import br.gov.ba.seia.util.Util;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ImovelRuralRelatorioService {
	
	
	
	@Inject
	private ImovelRuralRelatorioDAOIf imovelRuralRelatorioDAOIf;
	
	public List<ImovelRuralRelatorioDTO> listarImovelRuralRelatorio(FiltroImovelRuralRelatorioDTO filtroImovelRuralRelatorioDTO,Map<String, Object> params, boolean consultaSimples) throws Exception{
		return imovelRuralRelatorioDAOIf.listarImovelRuralRelatorio(filtroImovelRuralRelatorioDTO, params, consultaSimples);
	}
	
	public StreamedContent imprimirRelatorioImovelRural(FiltroImovelRuralRelatorioDTO filtroImovelRuralRelatorioDTO, 
														ColunasExibirImovelRuralRelatorioDTO colunasExibirImovelRuralRelatorioDTO, 
														Map<String, String> mapFiltros) throws Exception {
		final Map<String, Object> lParametros = montarParametrosConsulta(
				filtroImovelRuralRelatorioDTO,
				colunasExibirImovelRuralRelatorioDTO, mapFiltros);
		return new RelatorioUtil("relatorioImovelRural.jasper", lParametros, "logoInemaRelatorio.png", "sema_vertical.png").gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
	}
	
	private Map<String, Object> montarParametrosConsulta(
			FiltroImovelRuralRelatorioDTO filtroImovelRuralRelatorioDTO,
			ColunasExibirImovelRuralRelatorioDTO colunasExibirImovelRuralRelatorioDTO,
			Map<String, String> mapFiltros) {
		Map<String, Object> lParametros = new HashMap<String, Object>();
		lParametros.put("consulta", imovelRuralRelatorioDAOIf.extrairConsultaTo(filtroImovelRuralRelatorioDTO,false).toString());
		lParametros.put("exibeNomImovel", colunasExibirImovelRuralRelatorioDTO.isNomImovelRural());
		lParametros.put("exibeNomMunicipio", colunasExibirImovelRuralRelatorioDTO.isNomMunicipio());
		lParametros.put("exibeNomStatusCadastro", colunasExibirImovelRuralRelatorioDTO.isNomStatusCadastro());
		lParametros.put("exibeDtaFinalizacao", colunasExibirImovelRuralRelatorioDTO.isDtaFinalizacao());
		lParametros.put("exibeDtaPrimeiraFinalizacao", colunasExibirImovelRuralRelatorioDTO.isDtaPrimeiraFinalizacao());
		lParametros.put("exibeNomTipoDocumento", colunasExibirImovelRuralRelatorioDTO.isNomTipoDocumento());
		lParametros.put("exibeValArea", colunasExibirImovelRuralRelatorioDTO.isValArea());
		lParametros.put("exibeQtdModuloFiscal", colunasExibirImovelRuralRelatorioDTO.isQtdModuloFiscal());
		lParametros.put("exibeNomStatusReservaLegal", colunasExibirImovelRuralRelatorioDTO.isNomStatusReservaLegal());
		lParametros.put("exibeNumContratoConvenio", colunasExibirImovelRuralRelatorioDTO.isNumContratoConvenio());
		lParametros.put("exibeNomRequerente", colunasExibirImovelRuralRelatorioDTO.isNomRequerente());
		lParametros.put("exibeNumCpfCnpjRequerente", colunasExibirImovelRuralRelatorioDTO.isNumCpfCnpjRequerente());
		lParametros.put("exibeDtaSincronizacao", colunasExibirImovelRuralRelatorioDTO.isDtaSincronizacao());
		lParametros.put("exibeProprietarioPossuidor", colunasExibirImovelRuralRelatorioDTO.isProprietario());
		lParametros.put("exibeResponsavelTecnico", colunasExibirImovelRuralRelatorioDTO.isResponsavelTecnico());
		lParametros.put("exibeNomeCadastrante", colunasExibirImovelRuralRelatorioDTO.isNomeCadastrante());
		lParametros.put("exibeCpfCadastrante", colunasExibirImovelRuralRelatorioDTO.isCpfCadastrante());
		lParametros.put("temResponsavelTecnico", !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getResponsavelTecnico()) ? true : false);

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
		lParametros.put("dscTotalImoveis", mapFiltros.get("dscTotalImoveis"));
		lParametros.put("tipoCadastro", mapFiltros.get("tipoCadastro"));
		lParametros.put("moduloCadastro", mapFiltros.get("moduloCadastro"));
		lParametros.put("questionario", mapFiltros.get("questionario"));
		lParametros.put("nomeResponsavelTecnico", mapFiltros.get("nomeResponsavelTecnico"));
		lParametros.put("nomeCadastrante", mapFiltros.get("nomeCadastrante"));
		lParametros.put("cpfCadastrante", mapFiltros.get("cpfCadastrante"));

		
		return lParametros;
	}

	public StreamedContent imprimirRelatorioImovelRuralXLS(FiltroImovelRuralRelatorioDTO filtroImovelRuralRelatorioDTO, 
			ColunasExibirImovelRuralRelatorioDTO colunasExibirImovelRuralRelatorioDTO, Map<String, String> mapFiltros) throws Exception {
		
		final Map<String, Object> lParametros = montarParametrosConsulta(filtroImovelRuralRelatorioDTO, colunasExibirImovelRuralRelatorioDTO, mapFiltros);
		
		return new RelatorioUtil("relatorioImovelRuralXLS.jasper", lParametros, "logoInemaRelatorio.png", "sema_vertical.png").
				gerarRelatorio(RelatorioUtil.RELATORIO_XLS, true);
	}
	
}