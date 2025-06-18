package br.gov.ba.seia.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import br.gov.ba.seia.dto.RequerimentoDTO;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.ws.entity.RequerimentoWS;

public interface NovoRequerimentoDAOIf {

	public List<RequerimentoDTO> listarRequerimentoUnico(Map<String, Object> params) throws Exception;

	public Boolean verificaRequerimentoEmpreendimentoExistente(Empreendimento empreendimento) throws Exception;

	public Integer countListarRequerimento(Map<String, Object> params) throws Exception;

	public List<RequerimentoWS> consultaRequerimentoWs(Integer requerente, Integer ideEmpreendimento, String numRequerimento,
			Integer status, Date dtcInicial,Date dtcFinal, Integer first, Integer max, List<Integer> idesPessoas) throws Exception;

	public Long countConsultaRequerimentoWs(Integer requerente,
			Integer ideEmpreendimento, String numRequerimento, Integer status,
			Date dtcInicial, Date dtcFinal,
			List<Integer> idesPessoas);

}
