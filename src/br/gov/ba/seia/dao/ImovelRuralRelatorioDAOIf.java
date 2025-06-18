package br.gov.ba.seia.dao;

import java.util.List;
import java.util.Map;

import br.gov.ba.seia.dto.FiltroImovelRuralRelatorioDTO;
import br.gov.ba.seia.dto.ImovelRuralRelatorioDTO;

public interface ImovelRuralRelatorioDAOIf {
     public List<ImovelRuralRelatorioDTO> listarImovelRuralRelatorio(FiltroImovelRuralRelatorioDTO filtroImovelRuralRelatorioDTO, Map<String, Object> params, boolean consultaSimples) throws Exception;
     
     public StringBuilder extrairConsultaTo(FiltroImovelRuralRelatorioDTO filtroImovelRuralRelatorioDTO, boolean indRelatorioPDF);
     
     public StringBuilder montarConsultaImovelRuralRelatorio(FiltroImovelRuralRelatorioDTO filtroImovelRuralRelatorioDTO, boolean indRelatorioPDF);
     
     public StringBuilder montarConsultaImovelRuralRelatorioGEO(StringBuilder sql, FiltroImovelRuralRelatorioDTO filtroImovelRuralRelatorioDTO);

     StringBuilder montarConsultaImovelRuralRelatorio(
			FiltroImovelRuralRelatorioDTO filtroImovelRuralRelatorioDTO);

     StringBuilder extrairConsultaTo(
			FiltroImovelRuralRelatorioDTO filtroImovelRuralRelatorioDTO);

	StringBuilder montarConsultaImovelRuralRelatorioGEOSoImovel(StringBuilder sql,
			FiltroImovelRuralRelatorioDTO filtroImovelRuralRelatorioDTO);

}
