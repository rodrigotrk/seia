package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.util.EmailUtil;
import br.gov.ba.seia.util.SenhaUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EsquecimentoSenhaService {

	@Inject
	IDAO<PessoaFisica> daoPessoaFisica;
	
	@Inject
	IDAO<Pessoa> daoPessoa;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public PessoaFisica enviarEsquecimentoSenha(PessoaFisica pPessoaFisica) throws Exception {

		Map<String, Object> lParametrosQuery = new HashMap<String, Object>();

		lParametrosQuery.put("numCpf", pPessoaFisica.getNumCpf());
		lParametrosQuery.put("dscLogin", pPessoaFisica.getUsuario().getDscLogin());

		PessoaFisica lPessoaFisica = daoPessoaFisica.buscarEntidadePorNamedQuery("PessoaFisica.findByNumCpfLoginUsuario", lParametrosQuery);

		if (Util.isNullOuVazio(lPessoaFisica)) throw new Exception("Não foi possível localizar o usuário através do login e cpf informados.");

		String lSenhaGerada = SenhaUtil.gerarSenha(6);

		lPessoaFisica.getUsuario().setDscSenha(Util.toMD5(lSenhaGerada));

		daoPessoa.salvarOuAtualizar(lPessoaFisica.getPessoa());
		daoPessoaFisica.salvarOuAtualizar(lPessoaFisica);

		EmailUtil lEmailASerEnviado = new EmailUtil();

		StringBuilder lMensagem = new StringBuilder();

		lMensagem.append("Prezado, ").append(lPessoaFisica.getNomPessoa()).append("<br>");
		lMensagem.append("Sua senha de acesso ao sistema Seia foi alterada. A partir de agora será necessário informar os dados abaixo para que você possa acessar o sistema.").append("<br><br>");
		lMensagem.append("Login: ").append("<br>").append(lPessoaFisica.getUsuario().getDscLogin()).append("<br>");
		lMensagem.append("Senha: ").append("<br>").append(lSenhaGerada).append("<br><br>");
		lMensagem.append("Obs.: Aconselhamos o(a) Sr(a) alterar a senha logo após o acesso ao sistema, pois assim, evita-se que a mesma seja perdida ou, até mesmo, esquecida.").append("<br>");
		lMensagem.append("Atte.,").append("<br>");
		lMensagem.append("Central de Atendimento/INEMA");

		List<String> pListaEmail = new ArrayList<String>();
		pListaEmail.add(lPessoaFisica.getPessoa().getDesEmail());

		lEmailASerEnviado.enviarEmailHtml(pListaEmail, null, null, "Alteração de senha no sistema Seia.", lMensagem.toString());

		return lPessoaFisica;
	}
}