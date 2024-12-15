package br.gov.ba.seia.service;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.mail.EmailException;
import org.apache.log4j.Level;

import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoPessoa;
import br.gov.ba.seia.util.EmailUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
public class EmailService {
	
	@EJB
	private RequerimentoPessoaService requerimentoPessoaService;
	
	@EJB
	private EmpreendimentoService empreendimentoService;

	
	public static void enviarEmail(String pEmail, String nomeUsuario, String login, String pUrlAtivacao) throws EmailException {

		StringBuilder texto = new StringBuilder();


		texto
			.append("<html>")
			.append("<head></head>")
			.append("<body>")
			.append("<h1>Seia</h1>")
			.append("<h2>Ativação de usuário</h2>")
			.append("<p>Prezado(a) " +nomeUsuario+ ", saudações!")
			.append("<p>Você realizou o seu cadastro no sistema SEIA. O próximo passo é a ativação do seu usuário. <br>")
			.append("Para isso é necessário clicar no link abaixo para que o usuário " + login + " possa ser ativado no SEIA.</p>")
			.append("<h3><a href='"+ pUrlAtivacao+"'>Ativar usuário</a></h3>")
			.append("<br><br> Atenciosamente,<br>")			
			.append("<br><br><a href='http://sistema.seia.ba.gov.br'><img src='http://s17.postimage.org/9uau2tmlr/logo_Seia_Cabecalho.png' style='width: 110px;' /></a><br />")
			.append("<br> <b>Central de Atendimento - INEMA</b>")
			.append("</body>")
			.append("</html>");

		new EmailUtil().enviarEmailHtml(pEmail, "Ativação de sua conta de usuário no Seia.", texto.toString());
	}
	
	
	public void enviarEmailsAoRequerente(Requerimento requerimento, String assunto, String mensagemEmail) {
		try {
			new EmailUtil().enviarEmail(null, null, listarEmailsRequerente(requerimento), assunto, mensagemEmail);
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	public List<String> listarEmailsRequerente(Requerimento requerimento) {
		
		if(Util.isNull(requerimento)) {
			throw new IllegalArgumentException("O parametro requerimento não pode ser nulo.");
		}
		
		List<String> emails = new ArrayList<String>();
		
		if(!Util.isNullOuVazio(requerimento.getDesEmail())) {
			emails.add(requerimento.getDesEmail());
		}
		
		List<RequerimentoPessoa> listaRequerimentoPessoa = (List<RequerimentoPessoa>) requerimentoPessoaService.listarClientesDoRequerimento(requerimento.getIdeRequerimento());
		
		for (RequerimentoPessoa rp : listaRequerimentoPessoa) {
			if (!Util.isNullOuVazio(rp.getPessoa()) && !Util.isNullOuVazio(rp.getPessoa().getDesEmail())) {
				emails.add(rp.getPessoa().getDesEmail());
			}
		}
		
		Collection<Empreendimento> empreendimentos = empreendimentoService.buscarEmpreendimentoPorRequerimento(requerimento);
		
		for (Empreendimento emp : empreendimentos) {
			if(!Util.isNullOuVazio(emp.getDesEmail())) {
				emails.add(emp.getDesEmail());
			}
		}
		
		return emails;
	}
}