--Scripts em caráter prioritário--Scripts em caráter prioritário
--Data de geração 30/04/2025
--Versão 4.36.6

BEGIN;

--Scripts em caráter prioritário--Scripts em caráter prioritário
--Data de geração 07/05/2025
--Versão 4.36.7

BEGIN;

--[37392] Imóvel Rural sumiu do sistema
INSERT
        INTO
        public.pessoa_imovel
(ide_pessoa_imovel,
        ide_pessoa,
        ide_imovel,
        dtc_criacao,
        ind_excluido,
        ide_tipo_vinculo_imovel,
        dtc_exclusao,
        ide_tipo_vinculo_pct,
        dsc_tipo_vinculo_pct_outros)
VALUES(
nextval('PESSOA_IMOVEL_IDE_PESSOA_IMOVEL_seq'::text::regclass),
1351630,
356254,
now(),
FALSE,
1,
NULL,
NULL,
NULL);
UPDATE
        imovel_rural
SET
        ide_requerente_cadastro = 1351630
WHERE
        ide_imovel_rural = 356254;
        
-- [37378] - Adicionar substituta - UR Senhor do Bonfim
INSERT INTO public.funcionalidade_acao_pessoa_fisica
(ide_funcionalidade_acao_pessoa_fisica, ide_funcionalidade, ide_acao, ide_pessoa_fisica)
VALUES (nextval('FUNCIONALIDADE_ACAO_PESSOA_FISICA_seq'), 53, 43, 13226);
 
INSERT INTO public.funcionalidade_acao_pessoa_fisica_pauta
(ide_funcionalidade_acao_pessoa_fisica, ide_pauta, ind_substituto)
VALUES (currval('FUNCIONALIDADE_ACAO_PESSOA_FISICA_seq'), 2903, true);

--[37366] - Erro ao finalizar DTRP
UPDATE 
        enquadramento 
SET 
        ide_requerimento = NULL
WHERE
        ide_enquadramento = 226693;
        
-- [37357] Erro no download do Termo de Compromisso CEFIR
update
        requerimento_imovel ri
set
        ind_excluido = true,
        dtc_exclusao = now()
where
        ide_requerimento_imovel = 1213008;
update
        requerimento r
set
        ind_excluido = true,
        dtc_exclusao = now()
where
        ide_requerimento = 1423508;
        
-- [37355] Erro ao finalizar o CEFIR - query did not return a unique result: 2
update
        requerimento_imovel
set
        ind_excluido = true,
        dtc_exclusao = now()
where
        ide_requerimento_imovel = 1201919;
        
        
update
        requerimento
set
        ind_excluido = true,
        dtc_exclusao = now()
where
        ide_requerimento = 1408439;
        

-- [37349] - Erro no download do Termo de Compromisso CEFIR
  UPDATE
        requerimento
SET
        ind_excluido = TRUE,
        dtc_exclusao = now()
WHERE
        ide_requerimento = 1415128;
UPDATE
        requerimento_imovel
SET
        ind_excluido = TRUE,
        dtc_exclusao = now()
WHERE
        ide_requerimento_imovel = 1206558;
        
-- [37322] - Erro ao finalizar o CEFIR
UPDATE requerimento_imovel ri SET ind_excluido = TRUE WHERE ide_requerimento = 1175966;

-- [37318] - Erro ao finalizar o CEFIR
UPDATE requerimento_imovel ri SET ind_excluido = TRUE WHERE ide_requerimento = 1411742;

-- [37298] - Erro GEOBAHIA
        update 
                 perfil_autorizacao_geobahia
        set
                ind_autorizacao = true
        where
                ide_perfil = 54;
                
--Scripts em caráter prioritário--Scripts em caráter prioritário
--Data de geração 08/05/2025
--Versão 4.36.8

BEGIN;

-- [] - DTRP
update
        certificado
set
        ind_excluido = true
where
        ide_certificado = 1595333

-- [37403] - Erro no download do Termo de Compromisso CEFIR
  UPDATE
        requerimento
SET
        ind_excluido = TRUE,
        dtc_exclusao = now()
WHERE
        ide_requerimento = 1424587;
UPDATE
        requerimento_imovel
SET
        ind_excluido = TRUE,
        dtc_exclusao = now()
WHERE
        ide_requerimento_imovel = 1213818;
        
 
--[37364] Imóvel Rural sumiu do sistema
INSERT
        INTO
        pessoa_imovel
(ide_pessoa_imovel,
        ide_pessoa,
        ide_imovel,
        dtc_criacao,
        ind_excluido,
        ide_tipo_vinculo_imovel,
        dtc_exclusao,
        ide_tipo_vinculo_pct,
        dsc_tipo_vinculo_pct_outros)
VALUES(
nextval('PESSOA_IMOVEL_IDE_PESSOA_IMOVEL_seq'::text::regclass),
1338751,
1174356,
now(),
FALSE,
1,
NULL,
NULL,
NULL);
UPDATE
        imovel_rural
SET
        ide_requerente_cadastro = 1338751
WHERE
        ide_imovel_rural = 1174356;
        
-- [37347] - Pendência de Envio de Documentação
insert
        into
        fce
(ide_fce,
        dtc_criacao,
        ide_requerimento,
        ide_documento_obrigatorio,
        ide_origem_fce,
        ind_concluido,
        ide_analise_tecnica,
        ide_pessoa_fisica,
        ide_processo_reenquadramento)
values(nextval('fce_ide_fce_seq'), now(), 1392348, 1444, 1, true, null, null, null);
                
COMMIT;

COMMIT;