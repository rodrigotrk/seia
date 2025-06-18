package br.gov.ba.seia.enumerator;

public enum AcaoControlePermissaoAcessoPautaEnum {
	
	ENCAMINHAR_PROCESSO(29), 
	APENSAR_DOCUMENTO(30),
	FORMAR_EQUIPE(31),
	ADICIONAR_TECNICO(32),
	REMOVER_TECNICO(33), 
	DISTRIBUIR(34), 
	VISUALIZAR_EQUIPE(35), 
	DEFINIR_CRONOGRAMA(36), 			
	NOTIFICAR(37),		
	ACOES_DA_NOTIFICACAO(38), 	
	APROVAR_NOTIFICACAO(39),
	ENVIAR_NOTIFICACAO_PARA_REVISAO(40),
	EDITAR_NOTIFICACAO(41),
	CANCELAR_NOTIFICACAO(42),
	ACESSAR_PAUTA_GESTOR(43),
	CONCLUIR_PROCESSO(44);
	
	private int id;
	
	private AcaoControlePermissaoAcessoPautaEnum(int id){
		this.id = id;
	}
	
	public int getId(){
		return this.id;	
	}

}
