package br.gov.ba.ws;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.log4j.Level;
import org.json.JSONArray;
import org.json.JSONObject;

import br.gov.ba.seia.entity.ComunicacaoRequerimento;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.StatusRequerimento;
import br.gov.ba.seia.service.ComunicacaoRequerimentoService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.EnderecoService;
import br.gov.ba.seia.service.NovoRequerimentoService;
import br.gov.ba.seia.service.PermissaoPerfilService;
import br.gov.ba.seia.service.StatusRequerimentoService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.ws.entity.RequerimentoWS;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;

@Path("sse/requerimento")
public class ServicoRequerimento {

	@EJB
	private NovoRequerimentoService novoRequerimentoService;

	@EJB 
	private PermissaoPerfilService permissaoPerfilService;

	@EJB 
	private EmpreendimentoService empreendimentoService;

	@EJB 
	private EnderecoService enderecoService;

	@EJB
	private ComunicacaoRequerimentoService comunicacaoRequerimentoService;

	@EJB
	private StatusRequerimentoService statusRequerimentoService;

	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("/Bundle");


	/**Busca os requerimentos de acordo com os parâmetros passados.
	 * 
	 * @param requerente requerente se preenchido só traz os requerimentos desse requerente
	 * @param usuarioExterno Indicador se o usu�rio logado � externo, se for externo só traz os requerimentos do usuário logado e dos requerentes que ele é procurador ou representante legal
	 * @param ideUsuarioLogado Id da pessoa Fisica logada
	 * @param numRequerimento número do requerimento para consulta
	 * @param ideEmpreendimento ideEmpreendimento para consulta
	 * @param status status atual dos requerimentos obtidos na busca
	 * @param dtcInicial Data Inicial dos requerimentos obtidos na busca
	 * @param dtcFinal Data Final dos requerimentos obtidos na busca
	 * @param first indice do primeiro registro buscado no Banco
	 * @param max número máximo de registros obtidos na busca
	 * @return
	 * @author jorge.ramos
	 */
	@POST
	@Path("busca")
	@Produces("text/html; charset=UTF-8")
	public String getRequerimentos(@FormParam("requerente") Integer requerente,@FormParam("usuarioExterno") boolean usuarioExterno, @FormParam("ideUsuarioLogado") Integer  ideUsuarioLogado, @FormParam("numRequerimento") String numRequerimento, @FormParam("ideEmpreendimento") Integer ideEmpreendimento, @FormParam("status") Integer status, @FormParam("dtcInicial") String dtcInicial, @FormParam("dtcFinal") String dtcFinal, @FormParam("first") Integer first, @FormParam("max") Integer max) {
		Hashtable<String, Object> parametros = new Hashtable<String, Object>(); 
		try {
			JSONSerializer serializer = new JSONSerializer(); 
			List<Integer>  idesPessoas = null; 
			if(usuarioExterno && Util.isNullOuVazio(requerente)){
				PessoaFisica usuarioLogado = new PessoaFisica(ideUsuarioLogado);
				idesPessoas =  permissaoPerfilService.listarIdesPessoasAptas(usuarioLogado);
			}
			ArrayList<RequerimentoWS> listaRequerimento = (ArrayList<RequerimentoWS>) novoRequerimentoService.consultaRequerimentoWs(requerente, ideEmpreendimento, numRequerimento, status,Util.formataDataComHora(dtcInicial + " 00:00:00"),Util.formataDataComHora(dtcFinal + " 23:59:59"), first,max, idesPessoas);
			Long quantidade = novoRequerimentoService.countConsultaRequerimentoWs(requerente, ideEmpreendimento, numRequerimento, status,Util.formataDataComHora(dtcInicial + " 00:00:00"),Util.formataDataComHora(dtcFinal + " 23:59:59"), idesPessoas);
			if(!Util.isNullOuVazio(listaRequerimento)){
				String lista = serializer.exclude("*.class").transform(new DateTransformer("dd/MM/yyyy"), "dtcRequerimento").serialize(listaRequerimento);
				parametros.put("REQUERIMENTOS", new JSONArray(lista));
				parametros.put("QTD", quantidade);
			}else{
				parametros.put("ERRO", "001");
				parametros.put("MSGERRO", BUNDLE.getString("nao_encontrado"));
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			parametros.put("ERRO", "999");
			parametros.put("MSGERRO", e.toString());
		}
		JSONObject objJson = new JSONObject(parametros);
		return objJson.toString();
	}

	/**
	 * Método para busca o detalhe do requerimento
	 * @param ideEmpreendimento id do empreendimento
	 * @return
	 * @author jorge.ramos
	 */
	@POST
	@Path("detalhe")
	@Produces("text/html; charset=UTF-8")
	public String getDetalheRequerimento(@FormParam("ideEmpreendimento") Integer empreendimento) {
		Hashtable<String, Object> parametros = new Hashtable<String, Object>(); 
		try {
			if(!Util.isNull(empreendimento)){
				Empreendimento empreendimentoEndereco = empreendimentoService.carregarPorIdComMunicipio(empreendimento);
				Endereco endereco = enderecoService.carregar(empreendimentoEndereco.getEndereco().getIdeEndereco());
				if(!Util.isNullOuVazio(endereco) && !Util.isNullOuVazio(endereco.getIdeLogradouro())){
					parametros.put("Logradouro", endereco.getIdeLogradouro().getNomLogradouro());
					parametros.put("Bairro", endereco.getIdeLogradouro().getIdeBairro().getNomBairro());
					parametros.put("Cidade", endereco.getIdeLogradouro().getIdeMunicipio().getNomMunicipio());
					parametros.put("Cep", endereco.getIdeLogradouro().getNumCep());
					parametros.put("Estado", endereco.getIdeLogradouro().getIdeMunicipio().getIdeEstado().getNomEstado());
				}
			} 
			else {
				parametros.put("Logradouro", "-");
				parametros.put("Bairro", "-");
				parametros.put("Cidade", "-");
				parametros.put("Cep", "-");
				parametros.put("Estado", "-");
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			parametros.put("ERRO", "999");
			parametros.put("MSGERRO", e.toString());
		}
		JSONObject objJson = new JSONObject(parametros);
		return objJson.toString();
	}

	/**
	 * Método para listar todos os status do requerimento.
	 * @return
	 * @author jorge.ramos
	 */
	@POST
	@Path("status")
	@Produces("text/html; charset=UTF-8")
	public String getStatusRequerimento() {
		Hashtable<String, Object> parametros = new Hashtable<String, Object>(); 
		try {
			Collection<StatusRequerimento> status = statusRequerimentoService.listarStatusRequerimento();
			if(!Util.isNullOuVazio(status) ){
				JSONSerializer serializer = new JSONSerializer(); 
				String lista = serializer.exclude("*.class").serialize(status);
				parametros.put("STATUS", new JSONArray(lista));
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			parametros.put("ERRO", "999");
			parametros.put("MSGERRO", e.toString());
		}
		JSONObject objJson = new JSONObject(parametros);
		return objJson.toString();
	}


	/**
	 * Método para busca o historico de comunicação do requerimento
	 * @param requerimento id do requerimento
	 * @return
	 * @author jorge.ramos
	 */
	@POST
	@Path("historico")
	@Produces("text/html; charset=UTF-8")
	public String getHistorico(@FormParam("ideRequerimento") Integer requerimento) {
		Hashtable<String, Object> parametros = new Hashtable<String, Object>(); 
		try {
			ArrayList<ComunicacaoRequerimento> historico =  (ArrayList<ComunicacaoRequerimento>) comunicacaoRequerimentoService.carregarComunicacaoDescendente(requerimento);
			if(!Util.isNullOuVazio(historico)){
				JSONSerializer serializer = new JSONSerializer(); 
				String lista = serializer.exclude("*.class").transform(new DateTransformer("dd/MM/yyyy"), "dtcComunicacao").serialize(historico);
				parametros.put("HISTORICO", new JSONArray(lista));
			}else{
				parametros.put("ERRO", "001");
				parametros.put("MSGERRO","Não existem registros de Histórico de Comunicação para este requerimento.");
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			parametros.put("ERRO", "999");
			parametros.put("MSGERRO", e.toString());
		}
		JSONObject objJson = new JSONObject(parametros);
		return objJson.toString();
	}

	/**
	 * Método para buscar o detalhe do requerimento pelo número, utilizado para chamar a tela de detalhe do requerimento através do alerta.
	 * @param numRequerimento
	 * @return
	 * @author jorge.ramos
	 */
	@POST
	@Path("detalhePorNumero")
	@Produces("text/html; charset=UTF-8")
	public String getDetalheRequerimento(@FormParam("numRequerimento") String numRequerimento) {


		Hashtable<String, Object> parametros = new Hashtable<String, Object>(); 
		try {
			JSONSerializer serializer = new JSONSerializer(); 
			List<RequerimentoWS> requerimento = novoRequerimentoService.consultaRequerimentoWs(null, null, numRequerimento, null, null, null, null, null, null);
			if(!Util.isNullOuVazio(requerimento)){
				String requerimentoJson = serializer.exclude("*.class").transform(new DateTransformer("dd/MM/yyyy"), "dtcRequerimento").serialize(requerimento.get(0));
				parametros.put("REQUERIMENTO", new JSONObject(requerimentoJson));
				if(!Util.isNull(requerimento.get(0).getIdeEmpreendimento())){
					Empreendimento empreendimentoEndereco = empreendimentoService.carregarPorIdComMunicipio(requerimento.get(0).getIdeEmpreendimento());
					Endereco endereco = enderecoService.carregar(empreendimentoEndereco.getEndereco().getIdeEndereco());
					if(!Util.isNullOuVazio(endereco) && !Util.isNullOuVazio(endereco.getIdeLogradouro())){
						parametros.put("Logradouro", endereco.getIdeLogradouro().getNomLogradouro());
						parametros.put("Bairro", endereco.getIdeLogradouro().getIdeBairro().getNomBairro());
						parametros.put("Cidade", endereco.getIdeLogradouro().getIdeMunicipio().getNomMunicipio());
						parametros.put("Cep", endereco.getIdeLogradouro().getNumCep());
						parametros.put("Estado", endereco.getIdeLogradouro().getIdeMunicipio().getIdeEstado().getNomEstado());
					}
				}
				else {
					parametros.put("Logradouro", "-");
					parametros.put("Bairro", "-");
					parametros.put("Cidade", "-");
					parametros.put("Cep", "-");
					parametros.put("Estado", "-");
				}
			}else{
				parametros.put("ERRO", "001");
				parametros.put("MSGERRO", BUNDLE.getString("nao_encontrado"));
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			parametros.put("ERRO", "999");
			parametros.put("MSGERRO", e.toString());
		}
		JSONObject objJson = new JSONObject(parametros);
		return objJson.toString();
	}

}

