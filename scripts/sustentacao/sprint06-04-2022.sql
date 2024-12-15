--[33665] - Processo sumido.

BEGIN;
	UPDATE 
		controle_tramitacao 
	SET
		ind_fim_da_fila = TRUE 
	WHERE 
		ide_controle_tramitacao in (533072,533073); 
COMMIT;   

--[33660] - 33660 - Processo sumiu do sistema.

BEGIN;
	UPDATE 
		controle_tramitacao 
	SET
		ind_fim_da_fila = TRUE 
	WHERE 
		ide_controle_tramitacao = 495195;
COMMIT;

--[33638] - [REQUERIMENTO] Emissão do Certificado de Pagamento REPFLOR
BEGIN;
        UPDATE 
                tramitacao_requerimento 
        SET  
                ide_status_requerimento = 8 
        WHERE  
                ide_tramitacao_requerimento = 1109469;
COMMIT;
-- [33634] - Processo sumiu 

BEGIN;

UPDATE
	public.controle_tramitacao
SET
	ind_fim_da_fila = TRUE
WHERE
	ide_controle_tramitacao = 508055;

COMMIT;
--[33627] - [REQUERIMENTO] Detalhes do processo em branco.

BEGIN;

	UPDATE 
		controle_tramitacao 
	SET 
		ind_fim_da_fila = TRUE
	WHERE 
		ide_controle_tramitacao = 381505;
	
COMMIT;

--[33606] - [CADASTRO] Erro na finalização do CEFIR
BEGIN;
        UPDATE 
                requerimento_imovel 
        SET         
                ind_excluido = true
        WHERE 
                ide_requerimento = 1118227;
                
                
        UPDATE 
                requerimento 
        SET         
                ind_excluido = true
        WHERE 
                ide_requerimento = 1118227;        
COMMIT;     

--[33393] - [CADASTRO] Erro ao finalizar o CEFIR
BEGIN;
        UPDATE 
                imovel 
        SET
                ind_excluido = TRUE 
        WHERE 
                ide_imovel IN (18631,15862);
COMMIT;                
         
--Reabilitando as flags dos 2 imóveis que estão com sobreposição para sincronizar FAZENDA MAEDA.
BEGIN;
        UPDATE 
                imovel 
        SET
                ind_excluido = FALSE
        WHERE 
                ide_imovel IN (18631,15862);
COMMIT;                

--[33532] - Cep divergente da localidade cadastrada

BEGIN;
UPDATE
    public.bairro
SET
    ide_municipio = 550,
    nom_bairro = 'Centro',
    ind_origem_correio = false
WHERE
    ide_bairro = 1186;
COMMIT;
 
BEGIN;
UPDATE
    public.bairro
SET
    ide_municipio = 1066,
    nom_bairro = 'Centro',
    ind_origem_correio = false
WHERE
    ide_bairro = 648;
COMMIT;


--[33499] - [REQUERIMENTO] CEP divergente da localidade
BEGIN;
        UPDATE 
                bairro 
        SET
                ide_municipio = 518
        WHERE 
                ide_bairro = 1024 and ide_municipio = 1034 AND ind_origem_correio = TRUE 
COMMIT;  

--[33735] - [PROCESSO] Processo sumiu do sistema

BEGIN;

	UPDATE 	
		controle_tramitacao 
	SET
		ind_fim_da_fila = TRUE 
	WHERE 
	 	ide_controle_tramitacao = 531356;
COMMIT;

--[33766] - [PROCESSO] Processo sumiu do sistema 	

BEGIN;

	UPDATE 
		controle_tramitacao 
	SET
		ind_fim_da_fila = TRUE 
	WHERE 
		ide_controle_tramitacao = 535850;
COMMIT;	
--[33757] - [PROCESSO] Processo sumiu do sistema

BEGIN;

UPDATE
	public.controle_tramitacao
SET
	ind_fim_da_fila = true
WHERE
	ide_controle_tramitacao = 507431;

COMMIT;
--[33753] - Alteração de Usuário Interno para Usuário Externo

BEGIN;
        UPDATE
                usuario 
        SET 
                ide_perfil = 2, ind_tipo_usuario = false
        WHERE 
                ide_pessoa_fisica = 12790;
COMMIT; 
--[33747] - [PROCESSO] Processo não consta na Pauta do Técnico/Líder.

BEGIN;
        UPDATE         
                controle_tramitacao 
        SET
                ind_fim_da_fila = TRUE 
        WHERE 
                 ide_controle_tramitacao = 525976;
COMMIT;
       
--[33702] - [PROCESSO] Processo sumiu do sistema
BEGIN;
        UPDATE 
                controle_tramitacao 
        SET
                ind_fim_da_fila = TRUE 
        WHERE 
                 ide_controle_tramitacao = 521957;
COMMIT;
                 
BEGIN;
        UPDATE 
                controle_tramitacao 
        SET
                ind_fim_da_fila = TRUE 
        WHERE 
                ide_controle_tramitacao IN (531421,531422);
COMMIT; 
--[33689] - Retorno de Status RL

UPDATE
    reserva_legal
SET
    ide_status = 3
WHERE
    ide_reserva_legal = 8978;
INSERT
    INTO
    hist_historico (data_historico,
    acao_historico,
    ip_historico,
    ide_usuario)
VALUES
(now(),
'UPD',
'138.219.97.195',
4260);
INSERT
    INTO
    hist_valor (val_valor,
    ide_campo,
    ide_registro,
    ide_historico)
VALUES 
(3,
195,
8978,
currval('historico_ide_historico_seq'));
COMMIT;

--[33577] - Erro no cadastro do Imóvel Rural
BEGIN;

INSERT
	INTO
	public.logradouro
(ide_tipo_logradouro,
	ide_bairro,
	nom_logradouro,
	ind_origem_correio,
	ide_municipio,
	num_cep,
	ind_origem_api)
VALUES(86,
565673,
'Centro',
TRUE,
450,
45213300,
NULL);

COMMIT;


BEGIN;
UPDATE
	public.bairro
SET
	ide_municipio = 450,
	nom_bairro = 'ORIENTE NOVO ',
	ind_origem_correio = true
WHERE
	ide_bairro = 565673;
COMMIT;


--[33773] - [PROCESSO] Processo sumiu do sistema

BEGIN;
        UPDATE 
                controle_tramitacao 
        SET
                ind_fim_da_fila = TRUE 
        WHERE 
                ide_controle_tramitacao = 534882;
        
COMMIT;       

--[33547] - Reabertura do ticket #33523

--Desabilitando as flags dos 2 imóveis que estão com sobreposição para sincronizar FAZENDA MAEDA.
BEGIN;
        UPDATE 
                imovel 
        SET
                ind_excluido = TRUE 
        WHERE 
                ide_imovel = 355373
COMMIT;   
