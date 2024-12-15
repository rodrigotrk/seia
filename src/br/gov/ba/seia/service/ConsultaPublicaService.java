package br.gov.ba.seia.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dto.ConsultaPublicaDTO;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.enumerator.TipoEnderecoEnum;
import br.gov.ba.seia.enumerator.TipoPessoaRequerimentoEnum;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ConsultaPublicaService {
	
	@Inject
	private IDAO<ConsultaPublicaDTO> consultaPublicaDtoDAO;
	
	@EJB
	AtoAmbientalService atoAmbientalService;
	
	@EJB
	private ProcessoService processoService;

	/**
	 * Método que retorna os registros da referentes a consulta pública (Requerimento + Processo do requerimento)
	 * 
	 * @param first - Registro inicial da paginação.
	 * @param pageSize - Numero de registros a ser exibido por vez.
	 * @param params - Objeto do tipo Map passando todos os parametros necessários para a consulta.
	 *                 (numCpfOuNumCnpj - String - numero do cpf da pessoa física ou numero do cnpj da pessoa jurídica),
	 *                 (nomPessoaOuNomRazaoSocialOunomeFantasia - String - nome da pessoa física, razão social, ou nome fantasia),
	 *                 (nomEmpreendimento - String - nome do empreendimento),
	 *                 (nomMunicipio - String - nome da localidade empreendimento)
	 * @return ConsultaPublicaDTO
	 */
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<ConsultaPublicaDTO> listarFiltrandoPorParametros(int first, int pageSize, Map<String,Object> params)  {		
		String sql = montarConsultaPublica(params);
		List<ConsultaPublicaDTO> consultaPublica = consultaPublicaDtoDAO.listarPorQueryPorDemanda(sql, first, pageSize, retornarParametrosConsultaPublica(params));
		
		for(ConsultaPublicaDTO c : consultaPublica){
			if(c.getProcesso()!=null){
				c.getProcesso().setAtosAmbientais(atoAmbientalService.listarAtosPorProcesso(c.getProcesso().getIdeProcesso()));
			}
		}
		
		return consultaPublica;
	}
	
	public Integer countListarFiltrandoPorParametros(Map<String,Object> params)  {		
		String sql = montarConsultaPublicaCount(params);
		return consultaPublicaDtoDAO.count(sql, retornarParametrosConsultaPublica(params));
	}


	private String montarConsultaPublica(Map<String, Object> params) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("select new br.gov.ba.seia.dto.ConsultaPublicaDTO( ");
		sql.append("	   r, ");
		sql.append("	   r.dtcCriacao, ");
		sql.append("	   coalesce(pfReq.nomPessoa,pjReq.nomRazaoSocial) as requerente, ");
		sql.append("	   emp.nomEmpreendimento, ");
		sql.append("	   str.nomStatusRequerimento, ");
		sql.append("	   processo ");
		sql.append(") ");
		sql.append("from Requerimento r ");
		sql.append("	 left join r.empreendimentoRequerimentoCollection empReq ");
		sql.append("	 inner join empReq.ideEmpreendimento emp ");
		sql.append("	 left join emp.enderecoEmpreendimentoCollection endEmp ");
		sql.append("	 left join endEmp.ideEndereco endereco ");
		sql.append("	 left join endereco.ideLogradouro log ");
		sql.append("	 left join log.ideMunicipio mun");
		sql.append("	 inner join r.requerimentoPessoaCollection reqPessoa ");
		sql.append("	 inner join reqPessoa.pessoa pessoa ");
		sql.append("	 left join pessoa.pessoaJuridica pjReq ");
		sql.append("	 left join pessoa.pessoaFisica pfReq ");
		sql.append("	 left join r.tramitacaoRequerimentoCollection tr ");
		sql.append("	 inner join tr.ideStatusRequerimento str ");
		sql.append("	 left join r.processoCollection processo ");
		sql.append("	 left join processo.processoAtoCollection procAto ");
		sql.append("	 left join processo.controleTramitacaoCollection ct ");
		
		sql.append("where r.numRequerimento is not null ");
		sql.append("	  and endEmp.ideTipoEndereco.ideTipoEndereco = :ideTipoEndereco ");
		sql.append("	  and reqPessoa.ideTipoPessoaRequerimento.ideTipoPessoaRequerimento = :ideTipoPessoaRequerimento ");
		sql.append("	  and tr.ideTramitacaoRequerimento = (select max(tr_sub.ideTramitacaoRequerimento) from TramitacaoRequerimento tr_sub where tr_sub.ideRequerimento=r) ");
		sql.append("	  and str.ideStatusRequerimento not in :statusRequerimentoNaoExibir ");		
		sql.append("	  and (ct.ideControleTramitacao is null or ct.ideControleTramitacao = (select max(ct_sub.ideControleTramitacao) from ControleTramitacao ct_sub where ct_sub.ideProcesso=processo and ct_sub.ideStatusFluxo = 2 and ct_sub.indFimDaFila = :fimDaFila)) ");
			
		if(params.get("numeroProcesso") != null) {
			sql.append("  and processo.numProcesso like :numeroProcesso  ");
		}

		if(params.get("numCpfOuNumCnpj") != null) {
			if(params.get("numCpfOuNumCnpj").toString().length() > 11){
				sql.append("  and pjReq.numCnpj = :numCpfOuNumCnpj  ");				
			}
			else{
				sql.append("  and pfReq.numCpf = :numCpfOuNumCnpj ");				
			}
		}
		
		if(params.get("nomPessoaOuNomRazaoSocialOunomeFantasia") != null) {
			sql.append("  and ( upper(pfReq.nomPessoa) like upper(:nomPessoaOuNomRazaoSocialOunomeFantasia) ");
			sql.append("        or upper(pjReq.nomeFantasia) like upper(:nomPessoaOuNomRazaoSocialOunomeFantasia) ");
			sql.append("        or upper(pjReq.nomRazaoSocial) like upper(:nomPessoaOuNomRazaoSocialOunomeFantasia) ");
			sql.append("      ) ");
		}
		
		if(params.get("nomEmpreendimento") != null) {
			sql.append("  and  upper(emp.nomEmpreendimento) like upper(:nomEmpreendimento) ");
		}
		
		if(params.get("municipio") != null) {
			sql.append("  and mun.ideMunicipio = :municipio ");
		}
		
		sql.append("group by processo.ideProcesso, r.ideRequerimento, r.dtcCriacao, pfReq.nomPessoa, pjReq.nomRazaoSocial, emp.nomEmpreendimento, str.nomStatusRequerimento ");
		sql.append("order by r.ideRequerimento desc ");
		
		return sql.toString();
	}
	

	private String montarConsultaPublicaCount(Map<String, Object> params) {
		
		String sql = montarConsultaPublica(params);
		StringBuilder parteParaRetirarDaConsulta = new StringBuilder();
		
		parteParaRetirarDaConsulta.append("select new br.gov.ba.seia.dto.ConsultaPublicaDTO( ");
		parteParaRetirarDaConsulta.append("	   r, ");
		parteParaRetirarDaConsulta.append("	   r.dtcCriacao, ");
		parteParaRetirarDaConsulta.append("	   coalesce(pfReq.nomPessoa,pjReq.nomRazaoSocial) as requerente, ");
		parteParaRetirarDaConsulta.append("	   emp.nomEmpreendimento, ");
		parteParaRetirarDaConsulta.append("	   str.nomStatusRequerimento, ");
		parteParaRetirarDaConsulta.append("	   processo ");
		parteParaRetirarDaConsulta.append(") ");
		
		sql=sql.replace(parteParaRetirarDaConsulta.toString(), "select count(r.ideRequerimento) ");
		
		parteParaRetirarDaConsulta = new StringBuilder();
		parteParaRetirarDaConsulta.append("group by processo.ideProcesso, r.ideRequerimento, r.dtcCriacao, pfReq.nomPessoa, pjReq.nomRazaoSocial, emp.nomEmpreendimento, str.nomStatusRequerimento ");
		parteParaRetirarDaConsulta.append("order by r.ideRequerimento desc ");
		
		sql=sql.replace(parteParaRetirarDaConsulta.toString(), " ");
		
		return sql;
	}
	
	private Map<String, Object> retornarParametrosConsultaPublica(Map<String, Object> params) {
		params.put("fimDaFila", true);
		params.put("statusRequerimentoNaoExibir",getStatusRequerimentoNaoExibir());
		params.put("ideTipoEndereco", TipoEnderecoEnum.LOCALIZACAO.getId());
		params.put("ideTipoPessoaRequerimento", TipoPessoaRequerimentoEnum.REQUERENTE.getId());
		
		return params;
	}
	
	private List<Integer> getStatusRequerimentoNaoExibir() {
		 Integer[] statusRequerimentoNaoExibir = {
				 StatusRequerimentoEnum.AGUARDANDO_ENQUADRAMENTO.getStatus(),
				 StatusRequerimentoEnum.CANCELADO.getStatus(),
				 StatusRequerimentoEnum.SENDO_ENQUADRADO.getStatus()
		};
		return Arrays.asList(statusRequerimentoNaoExibir);
	}
	
}