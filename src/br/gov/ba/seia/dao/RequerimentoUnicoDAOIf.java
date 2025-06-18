package br.gov.ba.seia.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import br.gov.ba.seia.dto.RequerimentoUnicoDTO;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.RequerimentoUnico;
import br.gov.ba.seia.entity.StatusRequerimento;
import br.gov.ba.seia.entity.TipoPessoaRequerimento;
import br.gov.ba.seia.entity.Usuario;

public interface RequerimentoUnicoDAOIf { 

	public List<RequerimentoUnicoDTO> listaRequerimentoUnicoComParametro(RequerimentoUnico pRequerimentoUnico, Empreendimento empreendimentoRequerimento, Date pDataInicio,
			Date pDataFinal, StatusRequerimento statusRequerimento, Pessoa requerente, TipoPessoaRequerimento tipoPessoaRequerimento, List<Integer> idesPessoa) ;
	
	public List<RequerimentoUnicoDTO> listarRequerimentoUnicoPorRequerente(RequerimentoUnico requerimentoUnico, Date dataInicio, Date dataFim, Empreendimento empreendimento, Pessoa requerente, StatusRequerimento statusRequerimento, Usuario usuario) ;
	public List<RequerimentoUnicoDTO> listarRequerimentoUnicoPorRepresentanteLegal(RequerimentoUnico requerimentoUnico, Date dataInicio, Date dataFim, Empreendimento empreendimento, Pessoa requerente, StatusRequerimento statusRequerimento, Usuario usuario) ;
	public List<RequerimentoUnicoDTO> listarRequerimentoUnicoPorProcuradorPessoaFisica(RequerimentoUnico requerimentoUnico, Date dataInicio, Date dataFim, Empreendimento empreendimento, Pessoa requerente, StatusRequerimento statusRequerimento, Usuario usuario) ;
	public List<RequerimentoUnicoDTO> listarRequerimentoUnicoPorProcuradorPessoaJuridica(RequerimentoUnico requerimentoUnico, Date dataInicio, Date dataFim, Empreendimento empreendimento, Pessoa requerente, StatusRequerimento statusRequerimento, Usuario usuario) ;
	
	public List<RequerimentoUnicoDTO> listarRequerimentoUnico(RequerimentoUnico requerimentoUnico, Date dataInicio, Date dataFim, Empreendimento empreendimento, Pessoa requerente, StatusRequerimento statusRequerimento,Municipio municipio,List<Municipio> municipios, Usuario usuario,Integer firstPage,Integer pageSize, Map<String, Object> params) ;
	public Boolean listarRequerimentoUnicoAbertos(Empreendimento empreendimento) ;
	public Boolean listarRequerimentosAssociadosEmpreendimento(Empreendimento empreendimento) ;
	public RequerimentoUnico recuperarUltimoRequerimentoUnicoProcessado(Empreendimento empreendimento) ;
	
	
	public List<RequerimentoUnicoDTO>  listarRequerimentoUnicoExterno(RequerimentoUnico requerimentoUnico, Date dataInicio, Date dataFim, Empreendimento empreendimento,
			Pessoa requerente, StatusRequerimento statusRequerimento,Municipio municipio,List<Municipio> municipios, Usuario usuario,List<Integer> listaPessoas,Integer firstPage,Integer pageSize,Map<String, Object> params) ;
	
	public Integer countListarRequerimentoUnicoExterno(int first,int pageSize,RequerimentoUnico requerimentoUnico, Date dataInicio, Date dataFim, Empreendimento empreendimento,
			Pessoa requerente, StatusRequerimento statusRequerimento,Municipio municipio,List<Municipio> municipios, Usuario usuario,List<Integer> listaPessoas,Map<String, Object> params) ;
	
	
	public Integer countListarRequerimentoUnico(int start, int pageSize,RequerimentoUnico requerimentoUnico, Date dataInicio, Date dataFim, Empreendimento empreendimento,
			Pessoa requerente, StatusRequerimento statusRequerimento, Municipio municipio,List<Municipio> municipios, Usuario usuario,Map<String, Object> params) ;
	
	
	
	public RequerimentoUnico recuperarRequerimentoUnicoProcessoFormadoAnterior(Empreendimento empreendimento) ;
}