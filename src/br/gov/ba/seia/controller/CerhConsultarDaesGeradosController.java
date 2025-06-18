package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.springframework.security.core.context.SecurityContextHolder;

import br.gov.ba.seia.dto.CerhConsultarDaeDto;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.GeoRpga;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.SituacaoDae;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.exception.SeiaValidacaoRuntimeException;
import br.gov.ba.seia.facade.CerhConsultarDaesGeradosFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MetodoUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.security.SecurityUser;

@Named("cerhConsultarDaesGeradosController")
@ViewScoped
public class CerhConsultarDaesGeradosController {

	private String anoBaseCobranca; 
	
	private List<String> anos;
	
	@EJB
	private CerhConsultarDaesGeradosFacade cerhConsultarDaesGeradosFacade;
	
	private List<GeoRpga> geoRpgas;
	
	private GeoRpga geoRpga;
	
	private List<SituacaoDae> cerhSituacaoDaes;
	
	private SituacaoDae cerhSituacaoDae;
	
	private Pessoa requerente;
	
	private MetodoUtil metodoExterno;
	
	private List<Empreendimento> empreendimentos;
	
	private Empreendimento empreendimento;
	
	private List<CerhConsultarDaeDto> cerhConsultarDaeDtoCollection;
	
	@PostConstruct
	public void init(){
		
		this.anos = new ArrayList<String>();
		// verificar ano de cobrança
		int anoAtual = Calendar.getInstance().get(Calendar.YEAR) -1;
		for(int x = anoAtual ; x > Calendar.getInstance().get(Calendar.YEAR) -2; x--){
			anos.add("" + x);
		}
		this.anoBaseCobranca = "" + anoAtual;
		
		try {
			
			this.geoRpgas = this.cerhConsultarDaesGeradosFacade.listarRpgaComPrecoPublicoUnitario();
			
			this.cerhSituacaoDaes = this.cerhConsultarDaesGeradosFacade.listarCerhSituacaoDae();
			
			Usuario usuario = ((SecurityUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsuario();
			//usuário Externo
			if(isUsuarioExterno()){
				PessoaFisica p =  this.cerhConsultarDaesGeradosFacade.buscarPessoaFisicaByIde(usuario.getIdePessoaFisica());
				usuario.setPessoaFisica(p);
				this.setEmpreendimentos(new ArrayList<Empreendimento>(this.cerhConsultarDaesGeradosFacade.listarEmpreendimento(p.getPessoa())));
				for(Empreendimento emp : this.getEmpreendimentos()){
					//carrega a localização do Empreendimento
					emp.setIdeLocalizacaoGeografica(this.cerhConsultarDaesGeradosFacade.carregarComLocalizacaoGeografica(emp).getIdeLocalizacaoGeografica());
				}
				this.requerente = usuario.getPessoaFisica().getPessoa();
			}
			
			setMetodoExterno(new MetodoUtil(this, "selecionarRequerente", Pessoa.class));
		} catch (Exception e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	
	}
	
	public boolean isUsuarioExterno(){
		Usuario usuario = ((SecurityUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsuario();
		if(usuario != null && usuario.getIdePerfil().getIdePerfil().equals(2)){
			return true;
		}
		return false;
	}

	public void selecionarRequerente(Pessoa requerente) {
		setRequerente(requerente);
		//busca os em empreendimentos
		setEmpreendimentos(new ArrayList<Empreendimento>(this.cerhConsultarDaesGeradosFacade.listarEmpreendimento(requerente)));
		for(Empreendimento emp : this.empreendimentos){
			//carrega a localização do Empreendimento
			emp.setIdeLocalizacaoGeografica(this.cerhConsultarDaesGeradosFacade.carregarComLocalizacaoGeografica(emp).getIdeLocalizacaoGeografica());
		}
		Html.atualizar("consultarDaeForm");
	}
	
	public void consultar(){
		
		final Map<String, Object> parametros = getParametros();
		
		validar(parametros);
		
		try {
			
			this.cerhConsultarDaeDtoCollection = (List<CerhConsultarDaeDto>) cerhConsultarDaesGeradosFacade.consultarDaeGerado(parametros);
			Html.atualizar("consultarDaeForm:pnlConsultarDaeDataTable");
			
		} catch (Exception e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
	}
	
	private Map<String, Object> getParametros() {
		Map<String, Object> params = new HashMap<String, Object>();
		if(!Util.isNullOuVazio(anoBaseCobranca) ) {
			params.put("numAnoCobranca", anoBaseCobranca);
		}
		
		if(!Util.isNullOuVazio(geoRpga)) {
			params.put("geoRpga", geoRpga);
		}
		
		if(!Util.isNullOuVazio(cerhSituacaoDae)) {
			params.put("cerhSitucaoDae", cerhSituacaoDae);
		}
		
		if(!Util.isNullOuVazio(empreendimento)) {
			params.put("empreendimento", empreendimento);
		}
		
		return params;
	}
	
	private void validar(final Map<String, Object> parametros) {
		if(parametros.isEmpty()) {
			throw new SeiaValidacaoRuntimeException("Nenhum filtro foi informado. Por favor, informe um filtro.");
		}
	}
	
	public void limpar(){
		cerhConsultarDaeDtoCollection = null;
		geoRpga = null;
		cerhSituacaoDae = null;
		empreendimento = null;
		requerente = null;
		Html.atualizar("consultarDaeForm");
	}
	
	public String getAnoBaseCobranca() {
		return anoBaseCobranca;
	}

	public void setAnoBaseCobranca(String anoBaseCobranca) {
		this.anoBaseCobranca = anoBaseCobranca;
	}

	public List<String> getAnos() {
		return anos;
	}

	public void setAnos(List<String> anos) {
		this.anos = anos;
	}

	public List<GeoRpga> getGeoRpgas() {
		return geoRpgas;
	}

	public void setGeoRpgas(List<GeoRpga> geoRpgas) {
		this.geoRpgas = geoRpgas;
	}

	public GeoRpga getGeoRpga() {
		return geoRpga;
	}

	public void setGeoRpga(GeoRpga geoRpga) {
		this.geoRpga = geoRpga;
	}

	public List<SituacaoDae> getCerhSituacaoDaes() {
		return cerhSituacaoDaes;
	}

	public void setCerhSituacaoDaes(List<SituacaoDae> cerhSituacaoDaes) {
		this.cerhSituacaoDaes = cerhSituacaoDaes;
	}

	public SituacaoDae getCerhSituacaoDae() {
		return cerhSituacaoDae;
	}

	public void setCerhSituacaoDae(SituacaoDae cerhSituacaoDae) {
		this.cerhSituacaoDae = cerhSituacaoDae;
	}

	public Pessoa getRequerente() {
		return requerente;
	}

	public void setRequerente(Pessoa requerente) {
		this.requerente = requerente;
	}

	public MetodoUtil getMetodoExterno() {
		return metodoExterno;
	}

	public void setMetodoExterno(MetodoUtil metodoExterno) {
		this.metodoExterno = metodoExterno;
	}

	public List<Empreendimento> getEmpreendimentos() {
		return empreendimentos;
	}

	public void setEmpreendimentos(List<Empreendimento> empreendimentos) {
		this.empreendimentos = empreendimentos;
	}

	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}

	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
	}

	public List<CerhConsultarDaeDto> getCerhConsultarDaeDtoCollection() {
		return cerhConsultarDaeDtoCollection;
	}

	public void setCerhConsultarDaeDtoCollection(List<CerhConsultarDaeDto> cerhConsultarDaeDtoCollection) {
		this.cerhConsultarDaeDtoCollection = cerhConsultarDaeDtoCollection;
	}
	
}
