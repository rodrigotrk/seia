--Scripts em caráter prioritário--Scripts em caráter prioritário
--Data de geração 11/06/2025 11:02:22
--Versão 4.36.0

BEGIN;

update
        localizacao_geografica
set
        ide_classificacao_secao = 2
where
        ide_localizacao_geografica = 188851;
        
UPDATE
        parametro_calculo
SET
        ide_tipologia = 227
WHERE
        ide_parametro_calculo = 819;
        
update
        imovel_rural ir
set
        status_cadastro = 0
where
        ir.ide_imovel_rural = 1430003;
update
        public.requerimento_imovel
set
        ind_excluido = true
where
        ide_requerimento_imovel in (1204734,1204733);
update
        public.requerimento
set
        ind_excluido = true
where
        ide_requerimento in (1412274,1412275);
update
        imovel i
set
        ind_excluido = false
where
        i.ide_imovel in (863390, 658526, 1156427);
        
UPDATE
    tramitacao_requerimento
SET
    ide_status_requerimento = 8
WHERE
    ide_tramitacao_requerimento IN (
        1935371, 1949520, 1940164, 1937584, 1959815,
        1942469, 1943065, 1944226, 1957313, 1944298,
        1952574, 1947259, 1947262, 1951602, 1952677,
        1951817, 1952447, 1954176, 1954644, 1959831,
        1964362
    );
    
update
        usuario
set
        ide_perfil = 2,
        ind_tipo_usuario = false
where
        ide_pessoa_fisica = 2586;
update
        usuario
set
        ide_perfil = 2,
        ind_tipo_usuario = false
where
        ide_pessoa_fisica = 17921;
update
        usuario
set
        ide_perfil = 2,
        ind_tipo_usuario = false
where
        ide_pessoa_fisica = 2587;
        
INSERT INTO hist_historico (
    data_historico,
    acao_historico,
    ip_historico,
    ide_usuario
)
VALUES (
    now(),
    'UPD',
    '201.50.135.226',
    1
);
INSERT INTO hist_valor (
    val_valor,
    ide_campo,
    ide_registro,
    ide_historico
)
VALUES (
    1,
    195,
    1066410,
    currval('historico_ide_historico_seq')
);
INSERT INTO hist_historico (
    data_historico,
    acao_historico,
    ip_historico,
    ide_usuario
)
VALUES (
    now(),
    'UPD',
    '201.50.135.226',
    1
);
INSERT INTO hist_valor (
    val_valor,
    ide_campo,
    ide_registro,
    ide_historico
)
VALUES (
    8,
    195,
    1066410,
    currval('historico_ide_historico_seq')
);
UPDATE reserva_legal
SET
    ide_status = 8,
    ind_averbada = TRUE
WHERE
    ide_reserva_legal = 1066410;
INSERT INTO hist_historico (
    data_historico,
    acao_historico,
    ip_historico,
    ide_usuario
)
VALUES (
    now(),
    'UPD',
    '201.50.135.226',
    1
);
INSERT INTO hist_valor (
    val_valor,
    ide_campo,
    ide_registro,
    ide_historico
)
VALUES (
    1,
    195,
    3280,
    currval('historico_ide_historico_seq')
);
INSERT INTO hist_historico (
    data_historico,
    acao_historico,
    ip_historico,
    ide_usuario
)
VALUES (
    now(),
    'UPD',
    '201.50.135.226',
    1
);
INSERT INTO hist_valor (
    val_valor,
    ide_campo,
    ide_registro,
    ide_historico
)
VALUES (
    8,
    195,
    3280,
    currval('historico_ide_historico_seq')
);
UPDATE reserva_legal
SET
    ide_status = 8,
    ind_averbada = TRUE
WHERE
    ide_reserva_legal = 3280;
UPDATE hist_valor
SET val_valor = CASE 
    WHEN val_valor = '1' THEN '8'
    WHEN val_valor = '8' THEN '1'
    ELSE val_valor
END
WHERE ide_campo = 195
  AND ide_registro IN (1066410, 3280)
  AND val_valor IN ('1', '8');
  

COMMIT;
