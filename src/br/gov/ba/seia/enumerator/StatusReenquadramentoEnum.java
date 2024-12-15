package br.gov.ba.seia.enumerator;

import java.util.ArrayList;
import java.util.List;

import br.gov.ba.seia.util.Util;

public enum StatusReenquadramentoEnum {
	
		AGUARDANDO_REENQUADRAMENTO(1l),
		AGUARDANDO_ENVIO_DOCUMENTACAO(2l),
		EM_VALIDACAO_PREVIA(3l),
		PENDENCIA_ENVIO_DOCUMENTACAO(4l),
		PENDENCIA_VALIDACAO_DOCUMENTO(5l),
		VALIDADO(6l),
		BOLETO_PAGAMENTO_LIBERADO(7l),
		COMPORVANTE_ENVIADO(8l),
		PENDENCIA_VALIDACAO_COMPROVANTE(9l),
		REENQUADRADO(10l),
		ENCAMINHADO_PARA_O_GESTOR(11l),
		BOLETO_VALIDADO(12l),
		BOLETO_EM_PROCESSAMENTO(13l),
		AGUARDANDO_EDICAO_REENQUADRAMENTO(14l),
		PENDENCIA_DE_REENQUADRAMENTO(15l),
		NAO_REENQUADRADO(16l),
		RETORNO_DE_STATUS(17l);
	
	private Long id;
	
	private StatusReenquadramentoEnum(Long id) {
		this.id=id;
	}
	
	public Long getId() {
		return id;
	}
	
	
	public static List<Long> getListAlterarStatus(){
		List<Long> lista = new ArrayList<Long>();
		lista.add(StatusReenquadramentoEnum.EM_VALIDACAO_PREVIA.getId());
		lista.add(StatusReenquadramentoEnum.VALIDADO.getId());
		lista.add(StatusReenquadramentoEnum.BOLETO_EM_PROCESSAMENTO.getId());
		lista.add(StatusReenquadramentoEnum.BOLETO_PAGAMENTO_LIBERADO.getId());
		lista.add(StatusReenquadramentoEnum.BOLETO_PAGAMENTO_LIBERADO.getId());
		lista.add(StatusReenquadramentoEnum.AGUARDANDO_ENVIO_DOCUMENTACAO.getId());
		return lista;
		
	}
	
	
	public static StatusReenquadramentoEnum getStatusById(Long id) {
		if(!Util.isNull(id)) {
			for (StatusReenquadramentoEnum status : StatusReenquadramentoEnum.values()) {
				if (status.getId().equals(id)){
					return status;
				}
			}
		}
		return null;
		
	}
}