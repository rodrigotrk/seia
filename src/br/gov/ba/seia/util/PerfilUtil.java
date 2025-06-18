package br.gov.ba.seia.util;

import br.gov.ba.seia.enumerator.PerfilEnum;

public class PerfilUtil {

	public static boolean isCoordenador() {
		return ContextoUtil.getContexto().getUsuarioLogado().isPerfilCoordenador()
				|| ContextoUtil.getContexto().getUsuarioLogado().getIdePerfil().getIdePerfil().equals(PerfilEnum.COORD_CTGA.getId());
	}

	public static boolean isDiretor() {
		return ContextoUtil.getContexto().getUsuarioLogado().isPerfilDiretor();
	}

	public static boolean isFinanceiro() {
		return ContextoUtil.getContexto().getUsuarioLogado().isFinanceiro();
	}

	public static boolean isUsuarioExterno() {
		return ContextoUtil.getContexto().getUsuarioLogado().isUsuarioExterno();
	}

	public static boolean isMP() {
		return ContextoUtil.getContexto().getUsuarioLogado().isMP();
	}

	public static boolean isAtende() {
		return ContextoUtil.getContexto().getUsuarioLogado().isAtende();
	}

	public static boolean isTecnico() {
		return ContextoUtil.getContexto().getUsuarioLogado().isTecnico();
	}
	
	public static boolean isTecnicoCTGA() {
		return ContextoUtil.getContexto().getUsuarioLogado().isTecnicoCTGA();
	}

	public static boolean isTecnicoConveniado() {
		return ContextoUtil.getContexto().getUsuarioLogado().isTecnicoConveniado();
	}

}