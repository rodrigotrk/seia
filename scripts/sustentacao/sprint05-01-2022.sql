

-- #33200 - Habilitando indice_fim_da_fila para "true" e tornando processo visível na tabela "controle_tramitacao"

BEGIN;

 UPDATE
 	controle_tramitacao set ind_fim_da_fila = true
 WHERE 
 	ide_controle_tramitacao = 499711;
 COMMIT;

END;

-- #33201 - Habilitando indice_fim_da_fila para "true" e tornando processo visível na tabela "controle_tramitacao"

BEGIN;

 UPDATE 
 	controle_tramitacao set ind_fim_da_fila = true
 WHERE
 	ide_controle_tramitacao = 489062;
 COMMIT;

END; 

-- #33210 - Inclusão de espécies
BEGIN;
--Croton glandulosus
INSERT INTO 
        especie_supressao (
                nom_especie_supressao, ind_ativo) 
VALUES 
        ('Croton glandulosus', 
        TRUE);

--Nome popular já registrado, ide = 751
       
INSERT INTO 
        especie_supressao_nome_popular_especie (
                ide_nome_popular_especie, 
                ide_especie_supressao) 
VALUES 
        (751, 
        (SELECT 
                currval ('especie_supressao_seq' ))
        );

--Myrciaria cuspidata
INSERT INTO 
        especie_supressao (
                nom_especie_supressao, ind_ativo) 
VALUES 
        ('Myrciaria cuspidata', 
        TRUE);

--Nome popular 477
     
INSERT INTO 
        especie_supressao_nome_popular_especie (
                ide_nome_popular_especie, 
                ide_especie_supressao) 

VALUES 
        (477, 
        (SELECT 
       		currval ('especie_supressao_seq' ))
        );
       
--Solanum paludosum   
INSERT INTO 
        especie_supressao (
                nom_especie_supressao, ind_ativo) 
VALUES 
        ('Solanum paludosum', 
        TRUE);

--Nome popular 3526
     
INSERT INTO 
        especie_supressao_nome_popular_especie (
                ide_nome_popular_especie, 
                ide_especie_supressao) 
VALUES 
        (3526, 
        (SELECT 
       		currval ('especie_supressao_seq' ))
        ); 

--Leucaena sp
BEGIN;
INSERT INTO 
        especie_supressao (
                nom_especie_supressao, ind_ativo) 
VALUES 
        ('Leucaena sp', 
        TRUE);

--Nome popular 9575
     

        ('Apterokarpos gardnerii', TRUE);
        ('Apterokarpos gardneri', TRUE);


INSERT INTO 
        especie_supressao_nome_popular_especie (
                ide_nome_popular_especie, 
                ide_especie_supressao) 
VALUES 
        (9575, 
        (SELECT 
       		currval ('especie_supressao_seq' ))
        ); 

COMMIT;

-- #33212 - Remanejamento de processos da pauta
BEGIN;
UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = false
WHERE
	ide_processo in (74244,
74243,
74237,
74236,
74235,
73440,
74103,
74151,
74114,
74110,
74109,
74107,
74104,
74102,
74101,
74100,
74088,
74056,
74022,
74019,
74018,
73524,
73444,
73443,
73441)
	and ide_pauta = 159
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
values (74244,12,1,now(),true,'Solicitação de remanejamento de processo através do chamado Redmine 33142 ',159,true,446);
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
values (74243,12,1,now(),true,'Solicitação de remanejamento de processo através do chamado Redmine 33142 ',159, true,446);
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
values (74237,12,1,now(),true,'Solicitação de remanejamento de processo através do chamado Redmine 33142 ',159, true,446);
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
values (74236,12,1,now(),true,'Solicitação de remanejamento de processo através do chamado Redmine 33142 ',159, true,446);
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
values (74235,12,1,now(),true,'Solicitação de remanejamento de processo através do chamado Redmine 33142 ',159, true,446);
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
values (73440,12,1,now(),true,'Solicitação de remanejamento de processo através do chamado Redmine 33142 ',159, true,446);
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
values (74103,12,1,now(),true,'Solicitação de remanejamento de processo através do chamado Redmine 33142 ',159, true,446);
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
values (74151,12,1,now(),true,'Solicitação de remanejamento de processo através do chamado Redmine 33142 ',159, true,446);
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
values (74114,12,1,now(),true,'Solicitação de remanejamento de processo através do chamado Redmine 33142 ',159,true,446);
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
values (74110,12,1,now(),true,'Solicitação de remanejamento de processo através do chamado Redmine 33142 ',159,true,446);
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
values (74109,12,1,now(),true,'Solicitação de remanejamento de processo através do chamado Redmine 33142 ',159, true,446);
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
values (74107,12,1,now(),true,'Solicitação de remanejamento de processo através do chamado Redmine 33142 ',159, true,446);
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
values (74104,12,1,now(),true,'Solicitação de remanejamento de processo através do chamado Redmine 33142 ',159, true,446);
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
values (74102,12,1,now(),true,'Solicitação de remanejamento de processo através do chamado Redmine 33142 ',159, true,446);
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
values (74101,12,1,now(),true,'Solicitação de remanejamento de processo através do chamado Redmine 33142 ',159, true,446);
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
values (74100,12,1,now(),true,'Solicitação de remanejamento de processo através do chamado Redmine 33142 ',159, true,446);
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
values (74088,12,1,now(),true,'Solicitação de remanejamento de processo através do chamado Redmine 33142 ',159, true,446);
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
values (74056,12,1,now(),true,'Solicitação de remanejamento de processo através do chamado Redmine 33142 ',159, true,446);
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
values (74022,12,1,now(),true,'Solicitação de remanejamento de processo através do chamado Redmine 33142 ',159, true,446);
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
values (74019,12,1,now(),true,'Solicitação de remanejamento de processo através do chamado Redmine 33142 ',159, true,446);
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
values (74018,12,1,now(),true,'Solicitação de remanejamento de processo através do chamado Redmine 33142 ',159, true,446);
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
values (73524,12,1,now(),true,'Solicitação de remanejamento de processo através do chamado Redmine 33142 ',159, true,446);
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
values (73444,12,1,now(),true,'Solicitação de remanejamento de processo através do chamado Redmine 33142 ',159, true,446);
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
values (73443,12,1,now(),true,'Solicitação de remanejamento de processo através do chamado Redmine 33142 ',159, true,446);
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
values (73441,12,1,now(),true,'Solicitação de remanejamento de processo através do chamado Redmine 33142 ',159, true,446);

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = false
WHERE
	ide_processo in (74244,
74243,
74237,
74236,
74235,
73440,
74103,
74151,
74114,
74110,
74109,
74107,
74104,
74102,
74101,
74100,
74088,
74056,
74022,
74019,
74018,
73524,
73444,
73443,
73441)
	AND
	ide_pauta = 31;
COMMIT;
END;

-- #33219 - Solução para o processo que sumiu

--#33225 - Script para solução de processo sumido.

-- #33008 - Retirando o bairro centro que associava ao município incorreto


begin;
	update 
		controle_tramitacao 
	set 
		ind_fim_da_fila = true


    where
   		ide_controle_tramitacao = 488859;
	commit;
end;

-- #29715 -- Deleção de imóveis
BEGIN;

DELETE
FROM
	pessoa_imovel
WHERE
	ide_pessoa = 960707
	AND ide_pessoa_imovel != 1738648;

DELETE
FROM
	imovel
WHERE
	ide_imovel IN (
	SELECT
		i.ide_imovel
	FROM
		pessoa_imovel pi2
	INNER JOIN imovel i 
	ON
		pi2.ide_imovel = i.ide_imovel
	WHERE
		pi2.ide_pessoa = 960707
	ORDER BY
		i.dtc_criacao DESC)
	AND ide_imovel != 1121283;

COMMIT;

END;

    	where 	
   		ide_controle_tramitacao = 502951;
 commit;

end;

-- #33224 - Solução para o processo que sumiu
begin;
	update 
		controle_tramitacao 
	set
		ind_fim_da_fila = true
    	where 
   		ide_controle_tramitacao = 509647;
 	commit;  
end;
 	(5709, (SELECT currval ('especie_supressao_seq' )));
        
COMMIT;
END;

    where 
   		ide_controle_tramitacao = 502951;
commit;

end;

