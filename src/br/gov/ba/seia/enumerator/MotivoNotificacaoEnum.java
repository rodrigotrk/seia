package br.gov.ba.seia.enumerator;

import br.gov.ba.seia.entity.NotificacaoMotivoNotificacao;
import br.gov.ba.seia.util.Util;

public enum MotivoNotificacaoEnum {
	DOCUMENTO_COMPLEMENTAR(1),
	DOCUMENTO_INCOMPLETO(2),
	CORRECAO_DO_PROJETO(3),
	INDEFERIR(4),
	ARQUIVAR(5),
	SHAPE_ASV(6),
	SHAPE(7,8,9,10,11,13, 16, 18, 19, 20, 21, 22, 23),
	ENVIO_CERTIFICADO(12),
	CANCELAR(14),
	CORRECAO_DE_RAZAO_SOCIAL(15),
	RELOCACAO_RESERVA_LEGAL(16),
	CONCLUSAO(17),
	SHAPE_TURISMO(18),
	SHAPE_CABRUCA(19),
	REENQUADRAMENTO_PROCESSO(21),
	SHAPE_LICENCIAMENTO_AQUICULTURA(22),
	SHAPE_GALPAO_ARMAZENAMENTO_AQUICULTURA(23),
	SHAPE_LINHA_TRANSMISSAO_DISTRIBUICAO_ENERGIA(24),
	SHAPE_EPMF(9)
	;

	private Integer[] id;

	private MotivoNotificacaoEnum(Integer...id) {
		this.id = id;
	}

	public Integer[] getIds() {
		return id;
	}
	
	public Integer getId() {
		return id[0];
	}

	public void setId(Integer[] id) {
		this.id = id;
	}
	
	public boolean equals(NotificacaoMotivoNotificacao notificacaoMotivoNotificacao) {
		
		if(Util.isNull(notificacaoMotivoNotificacao)) {
			return false;
		}
		
		return this.getId().equals(notificacaoMotivoNotificacao.getIdeMotivoNotificacao().getIdeMotivoNotificacao());
	}
	
	public static MotivoNotificacaoEnum getEnum(Integer id) {
		MotivoNotificacaoEnum[] enums = MotivoNotificacaoEnum.values();
		for(int i = 0; i < enums.length ; i++){
			if(enums[i].getId() == id) {
				return enums[i];
			}
		}
	    throw new IllegalArgumentException("Motivo não encontrado!");
	}
}
