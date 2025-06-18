package br.gov.ba.seia.util;

import java.util.List;

import br.gov.ba.seia.entity.Telefone;
import br.gov.ba.seia.enumerator.TipoTelefoneEnum;

public class PessoaUtil {

	/**
	 * Retona o telefone segundo a regra: primeiramente o celular, se não tiver
	 * celular, retorna o comercial e por último o residencial.
	 *
	 * @param collectionTel
	 * @return Telefone
	 */
	public static Telefone getTelefoneParaRequerimento(List<Telefone> collectionTel) {
		Telefone tel = null;
		Telefone telRequerimento = null;
		Boolean isComercial = Boolean.FALSE;
		// Loop para pegar primeiramente o celular, se não tiver cel, pega o
		// comercial e por ultimo o residencial.
		for (Telefone telefone : collectionTel) {
			tel = telefone;
			if (tel.getIdeTipoTelefone().getIdeTipoTelefone().equals(TipoTelefoneEnum.CELULAR.getId())) {
				telRequerimento = tel;
				break;
			}
			if (!isComercial && tel.getIdeTipoTelefone().getIdeTipoTelefone().equals(TipoTelefoneEnum.RESIDENCIAL.getId())) {
				telRequerimento = tel;
			}
			if (tel.getIdeTipoTelefone().getIdeTipoTelefone().equals(TipoTelefoneEnum.COMERCIAL.getId())) {
				telRequerimento = tel;
				isComercial = true;
			}
		}
		return telRequerimento;
	}
}
