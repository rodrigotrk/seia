package br.gov.ba.seia.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.model.StreamedContent;
import org.springframework.security.core.context.SecurityContextHolder;

import br.gov.ba.seia.entity.Cerh;
import br.gov.ba.seia.entity.CerhCobranca;
import br.gov.ba.seia.entity.CerhDaeTipoUso;
import br.gov.ba.seia.entity.Dae;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.SefazCodigoReceita;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.TipoArquivoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.CobrancaCerhUtil;
import br.gov.ba.seia.util.JasperUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.RecursosUtil;
import br.gov.ba.seia.util.Uri;
import br.gov.ba.seia.util.security.SecurityUser;

/**
 * @author rodrigo.santos
 *
 */
@Named("reposicaoFlorestalGerarDaeController")
@ViewScoped
public class ReposicaoFlorestalGerarDaeController {

	private CerhCobranca cerhCobranca;

	private CobrancaCerhUtil util;

	public ReposicaoFlorestalGerarDaeController() {

	}

	@PostConstruct
	public void init() throws Exception{

		Cerh cerh = new Cerh();

		Empreendimento empreendimento = new Empreendimento();

		empreendimento.setIdeEmpreendimento(1200);
		Pessoa pessoa = new Pessoa();
		pessoa.setPessoaFisica(new PessoaFisica("05343894577"));
		pessoa.setNomeRazao("Rodrigo Santos");

		empreendimento.setIdePessoa(pessoa);

		cerh.setIdeEmpreendimento(empreendimento);
		Dae cerhDae = new Dae();

		SefazCodigoReceita cerhCodigoReceita = new SefazCodigoReceita();
		cerhCodigoReceita.setDscCodigoReceita("teste");
		cerhCodigoReceita.setNumCodigoCeceita(2248);
		cerhDae.setCerhCodigoReceita(cerhCodigoReceita);
		cerhDae.setDtVencimento(new Date());
		cerhDae.setNumMesReferencia(10);
		cerhDae.setNumAnoReferencia(2017);

		List<CerhDaeTipoUso> cerhDaetipoUsos = new ArrayList<CerhDaeTipoUso>();

		CerhDaeTipoUso cerhDaeTipoUso = new CerhDaeTipoUso();
		cerhDaeTipoUso.setValorOriginal(new BigDecimal(10));
		cerhDaeTipoUso.setValorAcrescimo(new BigDecimal(5));

		cerhDaetipoUsos.add(cerhDaeTipoUso);

		cerhDae.setCerhDaetipoUsos(cerhDaetipoUsos);

	}
	/**
	 * Gerar dae
	 */
	public void gerarDae() {
		try{
			util.gerarDaesSefaz(cerhCobranca);
		}catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}
	/**
	 * Preparar impressão do boleto
	 */
	public StreamedContent getImprimirDae(Dae dae) throws Exception{
		Map<String,Object> parametros = new HashMap<String, Object>();
		parametros.put("PASTA", "cerh");
		parametros.put("LOGO_SEFAZ", RecursosUtil.retornaCaminho(TipoArquivoEnum.IMAGEM, Uri.LOGO_SEFAZ));
		parametros.put("NOME_RELATORIO", "cerh_dae.jasper");
		parametros.put("ide_dae", dae.getIdeDae());
		/*
		 * *IMAGEM Da sefaz
		 */
		JasperUtil jasper = new JasperUtil();
		StreamedContent stream = jasper.gerar(null, parametros, null);
		Usuario usuario = ((SecurityUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsuario();
		return stream;
	}
}
