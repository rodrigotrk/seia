package br.gov.ba.seia.enumerator;

/**
 * @author eduardo.fernandes
 * @since 28/11/2016
 * @see <a href="http://10.105.17.77/redmine/issues/8187">#8187</a>
 */
public enum CadastroAtividadeNaoSujeitaLicTipoStatusEnum {

	CADASTRO_INCOMPLETO(1),
	CADASTRO_COMPLETO(2),
	AGUARDANDO_VALIDACAO(3),
	SENDO_VALIDADO(4),
	PENDENCIA_VALIDACAO(5),
	CONCLUIDO(6);

	private int ide;

	private CadastroAtividadeNaoSujeitaLicTipoStatusEnum(int ide) {
		this.ide = ide;
	}

	public int getIde() {
		return ide;
	}

}
