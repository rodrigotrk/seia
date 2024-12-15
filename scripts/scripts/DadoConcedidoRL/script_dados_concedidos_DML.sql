UPDATE motivo_notificacao SET ind_envio_shape = false;
UPDATE motivo_notificacao SET ind_envio_shape = true  WHERE ide_motivo_notificacao in (12,9,11,8,10,7,13,6);
INSERT INTO motivo_notificacao(ide_motivo_notificacao, nom_motivo_notificacao, ind_notificacao_prazo, ind_notificacao_comunicacao, ind_notificacao_homologacao, ind_envio_shape)
	VALUES (16,'Envio de shape para relocação de reserva legal', true, false, false, true);
INSERT INTO motivo_notificacao(ide_motivo_notificacao, nom_motivo_notificacao, ind_notificacao_prazo, ind_notificacao_comunicacao, ind_notificacao_homologacao)
  	VALUES (17, 'Conclusão de processo', false, true, false);
INSERT INTO status_reserva_legal(dsc_status) VALUES ('Aprovada aguardando conclusão do processo');
INSERT INTO status_reserva_legal(dsc_status) VALUES ('Relocada');

