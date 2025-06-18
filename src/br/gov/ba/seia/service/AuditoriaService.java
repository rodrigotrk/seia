package br.gov.ba.seia.service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.Id;

import org.apache.log4j.Level;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.entity.auditoria.HistCampo;
import br.gov.ba.seia.entity.auditoria.HistHistorico;
import br.gov.ba.seia.entity.auditoria.HistTabela;
import br.gov.ba.seia.entity.auditoria.HistValor;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.RegexUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.auditoria.AuditoriaUtil;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AuditoriaService {

	@Inject
	private IDAO<HistHistorico> historicoTabelaDAO;

	@Inject
	private IDAO<HistTabela> tabelaDAO;

	@Inject
	private IDAO<HistValor> valorCampoTabelaDAO;

	@Inject
	private IDAO<HistCampo> campoTabelaDAO;
	
	@Inject
	private UsuarioService usuarioService;
	
	private static final String ACAO_INSERT = "INS";

	private static final String ACAO_UPDATE = "UPD";

	private static final String ACAO_DELETE = "DEL";
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public <T extends Object> void salvar(T obj)  {
		HistTabela lTabela = null;
		Exception erro = null;
		try {
			if (!Util.isNullOuVazio(AuditoriaUtil.obterUsuario())){
				lTabela = carregarTabela(obj);
			
				Field[] declaredFields = obj.getClass().getDeclaredFields();
				List<HistCampo> lCampoTabela = new ArrayList<HistCampo>();
				List<HistValor> lValorCampoTabela = new ArrayList<HistValor>();
				Long ideRegistro = obterRegistro(obj);
				for (Field field : declaredFields) {
					if(field.getName().contains("serialVersionUID")) {
						continue;
					}
					HistCampo lCampo = new HistCampo();
					lCampo.setIdeTabela(lTabela);
					lCampo.setNomeCampo(field.getName());
					lCampoTabela.add(lCampo);
					
					HistValor lValorCampo = new HistValor();
					lValorCampo.setIdeCampo(lCampo);
					lValorCampo.setIdeRegistro(ideRegistro);
					field.setAccessible(true);
					
					String valor = field.get(obj)==null?null:field.get(obj).toString();
					if(isNull(valor)) {
						lValorCampo.setValor("Não informado");
					}else if(isIdTabela(valor, lTabela.getNomeTabela())) {
						lValorCampo.setValor(formataId(valor));
					}else if(isNumero(valor)) {
						lValorCampo.setValor(formataNumero(valor));
					}else if(isData(valor)) {
						lValorCampo.setValor(formatData(valor));
					}else {
						Log4jUtil.log(this.getClass().getSimpleName(), Level.INFO, " Histórico " + valor);
						lValorCampo.setValor(valor);
					}
					
					if(!Util.isNullOuVazio(lValorCampo.getValor())) {
						lValorCampoTabela.add(lValorCampo);
					}
					
				}
				
				if(!Util.isNullOuVazio(lTabela.getCampoCollection())) {
					lValorCampoTabela = mesclarCamposNovosComValores(lValorCampoTabela, lTabela.getCampoCollection());
				}else {
					lTabela.setCampoCollection(lCampoTabela);
				}
		
				tabelaDAO.salvarOuAtualizar(lTabela);
				
				HistHistorico lHistorico = new HistHistorico();
				lHistorico.setAcao(ACAO_INSERT);
				lHistorico.setDtcCriacao(new Date());
				lHistorico.setIdeUsuario(usuarioService.carregar(AuditoriaUtil.obterUsuario()));
				lHistorico.setIpHistorico(AuditoriaUtil.obterIp());
				lHistorico.setValorCampoCollection(lValorCampoTabela);
		
				for (HistValor valorCampoTabela : lValorCampoTabela) {
					valorCampoTabela.setIdeHistorico(lHistorico);
				}
				historicoTabelaDAO.salvar(lHistorico);
				
				lHistorico = null;
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}

	private String formatData(String string) {
		try {
			return AuditoriaUtil.converteData(string);
		} catch (ParseException e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public <T extends Object> void atualizarPCT(T objAntigo, T objNovo) {
		Map<String, String> mapPersistentChanges = AuditoriaUtil.compararObjetosObterValoresNovos(objAntigo, objNovo, false);
		gerarAuditoria(objAntigo, objNovo, mapPersistentChanges);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public <T extends Object> void atualizar(T objAntigo, T objNovo) {
		Map<String, String> mapPersistentChanges = AuditoriaUtil.compararObjetosObterValoresNovos(objAntigo, objNovo, true);
		gerarAuditoria(objAntigo, objNovo, mapPersistentChanges);
	}
		
	public <T extends Object> void gerarAuditoria(T objAntigo, T objNovo, Map<String, String> mapPersistentChanges) {
		Exception erro = null;
		try {
		HistTabela lTabela = carregarTabela(objNovo);
		
		Set<String> keys = mapPersistentChanges.keySet();
		List<HistCampo> lCampoTabela = new ArrayList<HistCampo>();
		List<HistValor> lValorCampoTabela = new ArrayList<HistValor>();
		boolean isBloqueioLimite = false;
		Long registro = obterRegistro(objAntigo);
		for (String key : keys) {
			HistCampo lCampo = new HistCampo();
			lCampo.setIdeTabela(lTabela);
			lCampo.setNomeCampo(key);
			lCampoTabela.add(lCampo);
			
			HistValor lValorCampo = new HistValor();
			lValorCampo.setIdeCampo(lCampo);
			lValorCampo.setIdeRegistro(registro);
			if(key.equals("indBloqueioLimite") && lTabela.getNomeTabela().equals("imovel_rural")) {
				isBloqueioLimite = true;
			}
			if(isNull(mapPersistentChanges.get(key))) {
				lValorCampo.setValor("Não informado");
			}else if(isIdTabela(mapPersistentChanges.get(key), lTabela.getNomeTabela())) {
				lValorCampo.setValor(formataId(mapPersistentChanges.get(key)));
			}else if(isNumero(mapPersistentChanges.get(key))) {
				lValorCampo.setValor(formataNumero(mapPersistentChanges.get(key)));
			}else if(isData(mapPersistentChanges.get(key))) {
				lValorCampo.setValor(formatData(mapPersistentChanges.get(key)));
			}else {
				Log4jUtil.log(this.getClass().getSimpleName(),Level.INFO," Histórico " + mapPersistentChanges.get(key));
				lValorCampo.setValor(mapPersistentChanges.get(key));
			}
			lValorCampoTabela.add(lValorCampo);
		}
		
		if(!Util.isNullOuVazio(lTabela.getCampoCollection())) {
			lTabela.setCampoCollection(mesclarCamposNovosComAntigos(lTabela.getCampoCollection(), lCampoTabela));
			lValorCampoTabela = mesclarCamposNovosComValores(lValorCampoTabela, lTabela.getCampoCollection());
		}else {
			lTabela.setCampoCollection(lCampoTabela);
		}
		tabelaDAO.salvarOuAtualizar(lTabela);
		
		HistHistorico lHistorico = new HistHistorico();
		lHistorico.setAcao(ACAO_UPDATE);
		lHistorico.setDtcCriacao(new Date());
		if(!isBloqueioLimite) {
			lHistorico.setIdeUsuario(usuarioService.carregar(AuditoriaUtil.obterUsuario()));
		}else {
			Usuario u = new Usuario();
			u.setIdePessoaFisica(1232980);
			lHistorico.setIdeUsuario(usuarioService.carregar(u));
		}
		lHistorico.setIpHistorico(AuditoriaUtil.obterIp());
		lHistorico.setValorCampoCollection(lValorCampoTabela);

		for (HistValor valorCampoTabela : lValorCampoTabela) {
			valorCampoTabela.setIdeHistorico(lHistorico);
		}
		historicoTabelaDAO.salvar(lHistorico);
		lHistorico = null;
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	private boolean isIdTabela(String value, String nomeTabela) {
		return value.contains(nomeTabela);
	}

	private String formataId(String value) {
		return value.replaceAll("[^\\d]", "");
	}

	private String formataNumero(String string) {
		return string.replaceAll("\\.", "\\,");
	}

	private boolean isNull(String value) {
		if(Util.isNullOuVazio(value)) {
			return true;
		}
		return value.trim().equals("null");
	}

	private boolean isNumero(String value) {
		try {
			if(RegexUtil.isDouble(value)) {
				Double.parseDouble(value);
				return true;
			}
		} catch (NumberFormatException  e) {
			Log4jUtil.log(AuditoriaService.class.getName(),Level.ERROR,e);
		}
		return false;
	}

	private boolean isData(String value) {
		try {
			if(RegexUtil.isDateUSA(value) || RegexUtil.isDateBR(value)) {
				AuditoriaUtil.converteData(value);
				return true;
			}else if(RegexUtil.isDateNormalizada(value)) {
				return true;
			}
		} catch (ParseException e) {
			Log4jUtil.log(AuditoriaService.class.getName(),Level.ERROR,e);
		}
		return false;
	}

	private <T> HistTabela carregarTabela(T objNovo) {
		String nomeTabela = AuditoriaUtil.obterNomeTabelaPorEntidade(objNovo);
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("nomeTabela", nomeTabela);
		HistTabela lTabela = tabelaDAO.buscarEntidadePorNamedQuery("HistTabela.findByName", parametros);
		if(Util.isNullOuVazio(lTabela)) {
			lTabela  = new HistTabela();
			lTabela.setNomeTabela(nomeTabela);
		}
		return lTabela;
	}

	private Collection<HistCampo> mesclarCamposNovosComAntigos(Collection<HistCampo> lCampoTabelaAntigo, List<HistCampo> lCampoTabelaNovo) {
		Map<String,HistCampo> campos = new HashMap<String, HistCampo>();
		for (HistCampo campoTabelaAntigo : lCampoTabelaAntigo) {
			campos.put(campoTabelaAntigo.getNomeCampo(), campoTabelaAntigo);
		}
		for (HistCampo campoTabelaNovo : lCampoTabelaNovo) {
			if(!campos.containsKey(campoTabelaNovo.getNomeCampo())) {
				campos.put(campoTabelaNovo.getNomeCampo(), campoTabelaNovo);
			}
		}
		return campos.values();
	}
	
	private List<HistValor> mesclarCamposNovosComValores(List<HistValor> lValorCampoTabela, Collection<HistCampo> lCampoTabela) {
		for (HistCampo campoTabela : lCampoTabela) {
			for (HistValor valorCampoTabela : lValorCampoTabela) {
				if(campoTabela.getNomeCampo().equalsIgnoreCase(valorCampoTabela.getIdeCampo().getNomeCampo())) {
					valorCampoTabela.setIdeCampo(campoTabela);
				}
			}
		}
		return lValorCampoTabela;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public <T extends Object> void excluir(T obj) throws Exception {
		
		HistTabela lTabela = carregarTabela(obj);
		tabelaDAO.salvarOuAtualizar(lTabela);
		
		HistHistorico lHistorico = new HistHistorico();
		lHistorico.setAcao(ACAO_DELETE);
		lHistorico.setDtcCriacao(new Date());
		lHistorico.setIdeUsuario(usuarioService.carregar(AuditoriaUtil.obterUsuario()));
		lHistorico.setIpHistorico(AuditoriaUtil.obterIp());
		HistCampo lCampo = obterCampoChave(obj);
		Collection<HistValor> lValorCampoTabela = new ArrayList<HistValor>();
		if(!Util.isNull(lCampo)) {
			HistValor lValorCampo = obterValorCampoChave(obj);
			lValorCampo.setIdeHistorico(lHistorico);
			lValorCampo.setIdeCampo(lCampo);
			lValorCampo.setIdeRegistro(obterRegistro(obj));
			lValorCampoTabela.add(lValorCampo);
			lHistorico.setValorCampoCollection(lValorCampoTabela);
		}
		historicoTabelaDAO.salvar(lHistorico);

	}
	
	private HistCampo obterCampoChave(Object obj ) {
		Field[] field = obj.getClass().getDeclaredFields();
		HistCampo lCampo = new HistCampo();
		for (Field f : field) {
			if(f.isAnnotationPresent(Id.class)) {
				lCampo.setNomeCampo(f.getName());
				break;
			}
		}
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("nomeCampoTabela", lCampo.getNomeCampo());
		return campoTabelaDAO.buscarEntidadePorNamedQuery("HistCampo.findByName", parametros);
	}

	private HistValor obterValorCampoChave(Object obj ) throws IllegalArgumentException, IllegalAccessException   {
		Field[] field = obj.getClass().getDeclaredFields();
		HistValor lValorCampo = new HistValor();
		for (Field f : field) {
			if(f.isAnnotationPresent(Id.class)) {
				f.setAccessible(true);
				lValorCampo.setValor(f.get(obj).toString());
				break;
			}
		}
		return lValorCampo;
	}
	
	public List<HistValor> obterHistorico(HistTabela tabelaFiltro, HistCampo campoTabelaFiltro) {
		Map<String, Object> param = new HashMap<String, Object>();
		boolean flagConsultaTabela = false;
		boolean flagConsultaCampo = false;
		if(!Util.isNullOuVazio(tabelaFiltro)) {
			param.put("ideTabela", tabelaFiltro.getIdeTabela());
			flagConsultaTabela = true;
		}
		if(!Util.isNullOuVazio(campoTabelaFiltro)) {
			param.put("ideCampoTabela", campoTabelaFiltro.getIdeCampo());
			flagConsultaCampo = true;
		 }
		if(flagConsultaCampo && flagConsultaTabela) {
			return valorCampoTabelaDAO.buscarPorNamedQuery("HistValor.findByTabelaAndCampo", param);
		}else if(flagConsultaTabela) {
			return valorCampoTabelaDAO.buscarPorNamedQuery("HistValor.findByTabela", param);
		}else if(flagConsultaCampo) {
			return valorCampoTabelaDAO.buscarPorNamedQuery("HistValor.findByCampo", param);
		}
		return valorCampoTabelaDAO.buscarPorNamedQuery("HistValor.findAll");
	}
	
	public List<HistTabela> obterTabelas() {
		return tabelaDAO.buscarPorNamedQuery("HistTabela.findAll");
	}

	public List<HistCampo> obterCamposTabela(HistTabela tabela) {
		if(!Util.isNullOuVazio(tabela)) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("ideTabela", tabela.getIdeTabela());
			return campoTabelaDAO.buscarPorNamedQuery("HistCampo.findByIdeTabela", param);
		}
		return campoTabelaDAO.buscarPorNamedQuery("HistCampo.findAll");
	}
	
	private Long obterRegistro(Object objNovo) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		String methodName = "getIde"+objNovo.getClass().getSimpleName().replace("Hist", "");
		if(methodName.equals("getIdeDocumentoImovelRural"))
			methodName = "getIdeDocumentoObrigatorio";
		if("HistoricoSuspensaoCadastro".equals(objNovo.getClass().getSimpleName()))
			methodName = "getIdeSuspensaoCadastro";
		if(!Util.isNullOuVazio(methodName)) {
			Method m = objNovo.getClass().getMethod(methodName, new Class[] {});
			m.setAccessible(true);
			Object ret = m.invoke(objNovo, new Object[] {});
			if(ret != null) {
				return Long.valueOf(ret.toString());
			}
		}
		return 0L;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void salvarShape(Object obj, String caminhoArquivo, String nomeCampo) throws Exception {
		HistCampo campo = new HistCampo();
		campo.setIdeTabela(carregarTabela(obj));
		campo.setNomeCampo(nomeCampo);
		
		List<HistValor> valorCampoCollection = new ArrayList<HistValor>();
		
		HistValor valor = new HistValor();
		valor.setIdeCampo(campo);
		valor.setIdeRegistro(obterRegistro(obj));
		valor.setValor(caminhoArquivo);
		
		valorCampoCollection.add(valor);

		valorCampoCollection = mesclarCamposNovosComValores(valorCampoCollection, campo.getIdeTabela().getCampoCollection());
		
		HistHistorico historico = new HistHistorico();
		historico.setAcao(ACAO_INSERT);
		historico.setIdeUsuario(usuarioService.carregar(AuditoriaUtil.obterUsuario()));
		historico.setIpHistorico(AuditoriaUtil.obterIp());
		historico.setValorCampoCollection(valorCampoCollection);
		historico.setDtcCriacao(new Date());
		
		for (HistValor valorCampoTabela : valorCampoCollection) {
			valorCampoTabela.setIdeHistorico(historico);
		}
		historicoTabelaDAO.salvar(historico);
	}
}