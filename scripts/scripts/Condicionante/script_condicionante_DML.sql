--Inclusão da secao de condicionantes ambientais
INSERT INTO secao(
             ide_tipo_secao,  dsc_secao, ind_excluido, 
            dtc_cricao)
    VALUES ( 1, 'Condicionantes Ambientais', false,now());

--- CRIANDO A FUNCIONALIDADE DE CONDICIONANTE
INSERT INTO funcionalidade(
            ide_secao, dsc_funcionalidade, ind_excluido, 
            dtc_criacao)
    VALUES (currval(('Secao_IDE_SECAO_seq'::text)::regclass), 'Consultas e Cadastro de Condicionante', false, 
            now());

-- Criando ações da funcionalidade Condicionantes
INSERT INTO funcionalidade_acao(
            ide_funcionalidade, ide_acao)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 1);
INSERT INTO funcionalidade_acao(
            ide_funcionalidade, ide_acao)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 2);
INSERT INTO funcionalidade_acao(
            ide_funcionalidade, ide_acao)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 3);
INSERT INTO funcionalidade_acao(
            ide_funcionalidade, ide_acao)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 4);        
INSERT INTO funcionalidade_acao(
            ide_funcionalidade, ide_acao)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 5);
    
-- Criando mapeando as url's das funcionalidades de Condicionantes

INSERT INTO funcionalidade_url(
            ide_funcionalidade, dsc_url, ind_principal)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), '/paginas/manter-condicionante/condicionanteLst.xhtml', false);
    
-------- DELEGANDO PODERES AO Administrador

INSERT INTO rel_grupo_perfil_funcionalidade(
            ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 1, 1);
INSERT INTO rel_grupo_perfil_funcionalidade(
            ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 2, 1);
 INSERT INTO rel_grupo_perfil_funcionalidade(
            ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 3, 1);       
INSERT INTO rel_grupo_perfil_funcionalidade(
            ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 4, 1);
INSERT INTO rel_grupo_perfil_funcionalidade(
            ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 5, 1);  
  
-------- Delegando poder aos DIRETORES     

INSERT INTO rel_grupo_perfil_funcionalidade(
            ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 1, 9);
INSERT INTO rel_grupo_perfil_funcionalidade(
            ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 2, 9);
 INSERT INTO rel_grupo_perfil_funcionalidade(
            ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 3, 9);       
INSERT INTO rel_grupo_perfil_funcionalidade(
            ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 4, 9);
INSERT INTO rel_grupo_perfil_funcionalidade(
            ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 5, 9);      
    
-------- Delegando poder aos COORDENADORES
            
INSERT INTO rel_grupo_perfil_funcionalidade(
            ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 1, 4);
INSERT INTO rel_grupo_perfil_funcionalidade(
            ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 2, 4);
 INSERT INTO rel_grupo_perfil_funcionalidade(
            ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 3, 4);       
INSERT INTO rel_grupo_perfil_funcionalidade(
            ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 4, 4);
INSERT INTO rel_grupo_perfil_funcionalidade(
            ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 5, 4);    
    
    
------------ Gerando a funcionalidade de Segmento
    
INSERT INTO funcionalidade(
            ide_secao, dsc_funcionalidade, ind_excluido, 
            dtc_criacao)
    VALUES ( 13, 'Consultas e Cadastro de Segmento', false, 
            now());

-- CRIANDO AÇÕES DA FUNCIONALIDADE DE SEGMENTO            
INSERT INTO funcionalidade_acao(
            ide_funcionalidade, ide_acao)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 1);
INSERT INTO funcionalidade_acao(
            ide_funcionalidade, ide_acao)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 2);
INSERT INTO funcionalidade_acao(
            ide_funcionalidade, ide_acao)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 3);
INSERT INTO funcionalidade_acao(
            ide_funcionalidade, ide_acao)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 4);        
INSERT INTO funcionalidade_acao(
            ide_funcionalidade, ide_acao)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 5);

----- Inserindo a url da funcionalidade    
INSERT INTO funcionalidade_url(
            ide_funcionalidade, dsc_url, ind_principal)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), '/paginas/manter-segmento/segmentoLst.xhtml', false);    
    
-------- DELEGANDO PODERES AO Administrador

INSERT INTO rel_grupo_perfil_funcionalidade(
            ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 1, 1);
INSERT INTO rel_grupo_perfil_funcionalidade(
            ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 2, 1);
INSERT INTO rel_grupo_perfil_funcionalidade(
            ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 3, 1);       
INSERT INTO rel_grupo_perfil_funcionalidade(
            ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 4, 1);
INSERT INTO rel_grupo_perfil_funcionalidade(
            ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 5, 1);  

   
-----  DELEGANDO PODERES aos Diretores
    
    
INSERT INTO rel_grupo_perfil_funcionalidade(
            ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 1, 9);
INSERT INTO rel_grupo_perfil_funcionalidade(
            ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 2, 9);
INSERT INTO rel_grupo_perfil_funcionalidade(
            ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 3, 9);       
INSERT INTO rel_grupo_perfil_funcionalidade(
            ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 4, 9);
INSERT INTO rel_grupo_perfil_funcionalidade(
            ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 5, 9);      

    
-----  DELEGANDO PODERES aos Coordenadores

INSERT INTO rel_grupo_perfil_funcionalidade(
            ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 1, 4);
INSERT INTO rel_grupo_perfil_funcionalidade(
            ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 2, 4);
INSERT INTO rel_grupo_perfil_funcionalidade(
            ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 3, 4);       
INSERT INTO rel_grupo_perfil_funcionalidade(
            ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 4, 4);
INSERT INTO rel_grupo_perfil_funcionalidade(
            ide_funcionalidade, ide_acao, ide_perfil)
    VALUES (currval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 5, 4);  
    
    
