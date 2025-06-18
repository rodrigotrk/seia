package br.gov.ba.seia.util;

import br.gov.ba.seia.entity.Acao;
import br.gov.ba.seia.entity.Funcionalidade;
import br.gov.ba.seia.entity.Secao;
import br.gov.ba.seia.util.security.SecurityService;

public class PermissaoUtil {
	
	/**
	 * Método criado para verificar se o usuário logado tem permissão para reabrir processo.
	 * <li> {@link Secao} = 5 - Processos</li>  
	 * <li> {@link Funcionalidade} = 20 -Consulta de Processo</li>
	 * <li> {@link Acao} = 62 - Reabrir Processo</li>
	 * @return true se o usuário tem permissão para executar essa ação.
	 */
	public static boolean temPermissaoParaReabrirProcesso() {
		return SecurityService.getInstance().canAccessUsingCache("5.20.62");
	}
}
