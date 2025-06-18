--Scripts em caráter prioritário
--Data de geração 12/06/2024
--Versão 4.32.4

BEGIN;

--[36433] - [ADMINISTRAÇÃO DE SISTEMAS] Alteração de perfil de usuário interno para externo

update 
	usuario 
set
	ide_perfil = 2,
	ind_tipo_usuario = false
where 
	ide_pessoa_fisica = 1161973;
	
update 
	usuario 
set 
	ide_perfil = 2,
	ind_tipo_usuario = false 
where 
	ide_pessoa_fisica = 652522;

--[36045] - Erro na finalização do CEFIR - query did not return a unique result:2
UPDATE
        requerimento_imovel
SET
        ind_excluido = TRUE,
        dtc_exclusao = now()
WHERE
        ide_requerimento = 1296621;
        
--[36416] - Processo sumiu do sistema

update 
	controle_tramitacao 
set 
	ind_fim_da_fila = true 
where 
	ide_controle_tramitacao = 744935;
	
-- [36396] Processo sumiu do sistema
update
		controle_tramitacao
set
		ind_fim_da_fila = true
where
		ide_controle_tramitacao = 753520;

		
-- [36394] - Alteração de perfil de usuário interno para externo
update 
        usuario
set
        ide_perfil = 2, 
        ind_tipo_usuario = false 
where
        ide_pessoa_fisica = 1103498;

-- [36371] - Alteração de perfil de usuário interno para externo
UPDATE 
        usuario
SET
        ide_perfil = 2, 
        ind_tipo_usuario = FALSE 
WHERE
        ide_pessoa_fisica = 1080072;      
        
-- [36360] Erro na finalização do CEFIR - query did not return a unique result:2

	UPDATE
		requerimento_imovel
	SET
		ind_excluido = TRUE,
		dtc_exclusao = now()
	WHERE
		ide_requerimento_imovel = 1143215;
	        
	UPDATE
		requerimento
	SET
		ind_excluido = TRUE,
		dtc_exclusao = now()
	WHERE
		ide_requerimento = 1331446;
		
--[36397] - Alteração de perfil de usuário interno para externo 	
UPDATE
        usuario
SET
        ide_perfil = 2,
        ind_tipo_usuario = FALSE
WHERE
        ide_pessoa_fisica IN (
972839,
1076451,
973429,
974374,
19989
        );
        
--[36433] - [ADMINISTRAÇÃO DE SISTEMAS] Alteração de perfil de usuário interno para externo
update 
        usuario 
set
        ide_perfil = 2,
        ind_tipo_usuario = false
where 
        ide_pessoa_fisica = 1161973;
        
update 
        usuario 
set 
        ide_perfil = 2,
        ind_tipo_usuario = false 
where 
        ide_pessoa_fisica = 652522;
        
-- [36421] - Erro ao realizar análise técnica
INSERT
	INTO
	fce_irrigacao (
	ide_tipologia_irrigacao,
	ide_tipo_periodo_derivacao,
	num_tempo_captacao,
	num_volume_derivar,
	ide_fce,
	ind_outros_cultura)
VALUES(1,
1,
0,
0,
85119,
FALSE);   

-- [36418] - Retorno de status da RL

INSERT
	INTO
	hist_historico (data_historico,
	acao_historico,
	ip_historico,
	ide_usuario)
VALUES (now(),
'UPD',
'201.50.135.226',
1);

INSERT
	INTO
	hist_valor (val_valor,
	ide_campo,
	ide_registro,
	ide_historico)
VALUES (3,
195,
2874,
currval('historico_ide_historico_seq'));

UPDATE
	reserva_legal
SET
	ide_status = 3
WHERE
	ide_reserva_legal = 2874;

-- [36414] Erro ao finalizar requerimento - aba 6 em branco
UPDATE
        fauna
SET
        ind_excluido = TRUE
WHERE
        ide_fauna = 84398;

-- [36425] Tela em branco ao fazer login

	DELETE FROM public.funcionalidade_acao_pessoa_fisica_pauta
	WHERE ide_funcionalidade_acao_pessoa_fisica IN (10, 11, 12, 13, 14, 15) AND ide_pauta IN (6, 180);
	
	DELETE FROM public.funcionalidade_acao_pessoa_fisica
	WHERE ide_funcionalidade_acao_pessoa_fisica IN (10, 11, 12, 13, 14, 15);    
	
-- [36381] Erro ao retirar pendência

	UPDATE
		historico_suspensao_cadastro
	SET
		dtc_retirada_suspensao = NULL
	WHERE
		ide_suspensao_cadastro = 338;        
		
-- [36363] Erro na finalização do CEFIR

	UPDATE
		imovel_rural
	SET
		status_cadastro = 0
	WHERE
		ide_imovel_rural = 86073;        
        
COMMIT;