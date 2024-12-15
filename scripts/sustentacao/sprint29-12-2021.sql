	--#33182 - Atualização ind_fim_da_fila dos processos.
BEGIN;

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = FALSE
WHERE
	ide_controle_tramitacao IN (364943, 364945, 367138, 364941, 453416, 364942, 48862, 364884, 364887, 364883, 364878, 425221);

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = TRUE
WHERE
	ide_controle_tramitacao = 453415;
	
COMMIT;

--#33183 - atualização na ind_excluido da tabela usuario. 
BEGIN;

UPDATE
	pessoa 
SET
	ind_excluido = false
WHERE
	ide_pessoa = 79;
COMMIT;  

--#33193 - Update de processos de pauta de usuarios
BEGIN;
UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = false
WHERE
	ide_processo in (53994, 13525)
	and ide_pauta = 2313
	and ind_fim_da_fila = true;
	insert
	into
	controle_tramitacao (ide_processo,
	ide_status_fluxo,
	ide_area,
	dtc_tramitacao,
	ind_fim_da_fila,
	dsc_comentario_interno,ide_pauta ,
	ind_responsavel,
	ide_pessoa_fisica)
values (53994,12,33,now(),true,'Solicitação de remanejamento de processo através do chamado Redmine 33193 ',2313,true,993435);
insert
	into
	controle_tramitacao (ide_processo,
	ide_status_fluxo,
	ide_area,
	dtc_tramitacao,
	ind_fim_da_fila,
	dsc_comentario_interno,ide_pauta ,
	ind_responsavel,
	ide_pessoa_fisica)
values (13525,12,33,now(),true,'Solicitação de remanejamento de processo através do chamado Redmine 33193 ',2313, true,993435);
END;
COMMIT;


BEGIN;
UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = false
WHERE
	ide_processo in (53994, 13525)
	AND
	ide_pauta = 728;
COMMIT;
END;

BEGIN;
UPDATE
	area 
SET
	ide_pessoa_fisica = 993435
WHERE
	ide_area = 33;
	
	
COMMIT;

-- #33195 - processo duplicado. 
BEGIN
	 
UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = false
WHERE
	ide_controle_tramitacao = 508119;
COMMIT

-- #33190 - Inclusão de espécies
BEGIN;

INSERT INTO 
        especie_supressao (
                nom_especie_supressao, ind_ativo) 
VALUES 
        ('Calycolpus legrandii', 
        TRUE);

INSERT INTO 
        nome_popular_especie (
                nom_popular_especie) 
VALUES 
        ('Araçá-de-Restinga');
       
INSERT INTO 
        especie_supressao_nome_popular_especie (
                ide_nome_popular_especie, 
                ide_especie_supressao) 
VALUES 
        (currval ('nome_popular_especie_seq'), 
        (SELECT 
                currval ('especie_supressao_seq' ))
        );
       
INSERT INTO 
        especie_supressao (
                nom_especie_supressao, ind_ativo) 
VALUES 
        ('Myrcia blanchetiana', 
        TRUE);

INSERT INTO 
        nome_popular_especie (
                nom_popular_especie) 
VALUES 
        ('Murta-Restinga');
       
INSERT INTO 
        especie_supressao_nome_popular_especie (
                ide_nome_popular_especie, 
                ide_especie_supressao) 
VALUES 
        (currval ('nome_popular_especie_seq'), 
        (SELECT 
                currval ('especie_supressao_seq' ))
        );
       
INSERT INTO 
        especie_supressao (
                nom_especie_supressao, ind_ativo) 
VALUES 
        ('Psidium bahianum', 
        TRUE);
       
INSERT INTO 
        especie_supressao_nome_popular_especie (
                ide_nome_popular_especie, 
                ide_especie_supressao) 
VALUES 
        (67, 
        (SELECT 
                currval ('especie_supressao_seq' ))
        );
    
INSERT INTO 
        especie_supressao (
                nom_especie_supressao, ind_ativo) 
VALUES 
        ('Ouratea suaveolens', 
        TRUE);

INSERT INTO 
        especie_supressao_nome_popular_especie (
                ide_nome_popular_especie, 
                ide_especie_supressao) 
VALUES 
        (4691, 
        (SELECT 
                currval ('especie_supressao_seq' ))
        );
       
INSERT INTO 
        especie_supressao (
                nom_especie_supressao, ind_ativo) 
VALUES 
        ('Helicteres laciniosa', 
        TRUE);

INSERT INTO 
        nome_popular_especie (
                nom_popular_especie) 
VALUES 
        ('Flor-Vermelha');
       
INSERT INTO 
        especie_supressao_nome_popular_especie (
                ide_nome_popular_especie, 
                ide_especie_supressao) 
VALUES 
        (currval ('nome_popular_especie_seq'), 
        (SELECT 
                currval ('especie_supressao_seq' ))
        );

INSERT INTO
		nome_popular_especie (
			nom_popular_especie)
VALUES 
		('Araçá-Vermelho');

INSERT INTO 
        especie_supressao_nome_popular_especie (
                ide_nome_popular_especie, 
                ide_especie_supressao) 
VALUES 
        (currval ('nome_popular_especie_seq'), 
        12376);

COMMIT;

END;




