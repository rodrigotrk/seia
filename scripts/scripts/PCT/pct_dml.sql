--====================================================================================================
--118916
ALTER SEQUENCE gestor_financeiro_seq RESTART WITH 2;

INSERT INTO gestor_financeiro(ide_gestor_financeiro,
            nom_gestor_financeiro)
    VALUES ((nextval('gestor_financeiro_seq')),'Não se aplica');   

--====================================================================================================    

INSERT INTO funcionalidade_url VALUES (33,'/paginas/manter-tipo-cadastro/tipo-cadastro.xhtml',false);

INSERT INTO tipo_vinculo_pct(
           dsc_tipo_vinculo_pct, ind_excluido, dtc_criacao)
    VALUES ('Membro da comunidade, sem vínculo com associação ou cooperativa', false, now());

INSERT INTO tipo_vinculo_pct(
           dsc_tipo_vinculo_pct, ind_excluido, dtc_criacao)
    VALUES ('Prestador de serviço autônomo', false, now());

INSERT INTO tipo_vinculo_pct(
           dsc_tipo_vinculo_pct, ind_excluido, dtc_criacao)
    VALUES ('Membro de associação ou cooperativa', false, now());    

INSERT INTO tipo_vinculo_pct(
           dsc_tipo_vinculo_pct, ind_excluido, dtc_criacao)
    VALUES ('Servidor de instituição pública', false, now());    

INSERT INTO tipo_vinculo_pct(
           dsc_tipo_vinculo_pct, ind_excluido, dtc_criacao)
    VALUES ('Prestador de serviço de entidade não governamental', false, now()); 

INSERT INTO tipo_vinculo_pct(
           dsc_tipo_vinculo_pct, ind_excluido, dtc_criacao)
    VALUES ('Prestador de serviço de empresa privada', false, now()); 

INSERT INTO tipo_vinculo_pct(
           dsc_tipo_vinculo_pct, ind_excluido, dtc_criacao)
    VALUES ('Outros', false, now()); 

--==============================================================================

INSERT INTO tipo_territorio_pct(
            dsc_tipo_territorio_pct, ind_excluido, 
            dtc_criacao)
    VALUES ('Propriedade', false, now());

INSERT INTO tipo_territorio_pct(
            dsc_tipo_territorio_pct, ind_excluido, 
            dtc_criacao)
    VALUES ('Posse', false, now());

INSERT INTO tipo_territorio_pct(
            dsc_tipo_territorio_pct, ind_excluido, 
            dtc_criacao)
    VALUES ('Concessão', false, now());

INSERT INTO tipo_territorio_pct(
            dsc_tipo_territorio_pct, ind_excluido, 
            dtc_criacao)
    VALUES ('Outros', false, now());

--=================================================================================


INSERT INTO tipo_seguimento_pct(
            dsc_tipo_seguimento_pct, dtc_criacao, 
            ind_excluido)
    VALUES ('Povos indígenas', now(), false);

INSERT INTO tipo_seguimento_pct(
            dsc_tipo_seguimento_pct, dtc_criacao, 
            ind_excluido)
    VALUES ('Comunidades quilombolas', now(), false);

INSERT INTO tipo_seguimento_pct(
            dsc_tipo_seguimento_pct, dtc_criacao, 
            ind_excluido)
    VALUES ('Povos e comunidades de terreiro/Povos e comunidades de matriz africana', now(), false);

INSERT INTO tipo_seguimento_pct(
            dsc_tipo_seguimento_pct, dtc_criacao, 
            ind_excluido)
    VALUES ('Povos ciganos', now(), false);
    
INSERT INTO tipo_seguimento_pct(
            dsc_tipo_seguimento_pct, dtc_criacao, 
            ind_excluido)
    VALUES ('Pescadores artesanais e marisqueiras', now(), false);

INSERT INTO tipo_seguimento_pct(
            dsc_tipo_seguimento_pct, dtc_criacao, 
            ind_excluido)
    VALUES ('Extrativistas', now(), false);

INSERT INTO tipo_seguimento_pct(
            dsc_tipo_seguimento_pct, dtc_criacao, 
            ind_excluido)
    VALUES ('Extrativistas costeiros e marinhos', now(), false);

INSERT INTO tipo_seguimento_pct(
            dsc_tipo_seguimento_pct, dtc_criacao, 
            ind_excluido)
    VALUES ('Caiçaras', now(), false);

INSERT INTO tipo_seguimento_pct(
            dsc_tipo_seguimento_pct, dtc_criacao, 
            ind_excluido)
    VALUES ('Faxinalenses', now(), false);

INSERT INTO tipo_seguimento_pct(
            dsc_tipo_seguimento_pct, dtc_criacao, 
            ind_excluido)
    VALUES ('Benzedeiros', now(), false);

INSERT INTO tipo_seguimento_pct(
            dsc_tipo_seguimento_pct, dtc_criacao, 
            ind_excluido)
    VALUES ('Ilhéus', now(), false);

INSERT INTO tipo_seguimento_pct(
            dsc_tipo_seguimento_pct, dtc_criacao, 
            ind_excluido)
    VALUES ('Raizeiros', now(), false);
    
INSERT INTO tipo_seguimento_pct(
            dsc_tipo_seguimento_pct, dtc_criacao, 
            ind_excluido)
    VALUES ('Geraizeiros', now(), false);
    
INSERT INTO tipo_seguimento_pct(
            dsc_tipo_seguimento_pct, dtc_criacao, 
            ind_excluido)
    VALUES ('Caatingueiros', now(), false);

INSERT INTO tipo_seguimento_pct(
            dsc_tipo_seguimento_pct, dtc_criacao, 
            ind_excluido)
    VALUES ('Vazanteiros', now(), false);

INSERT INTO tipo_seguimento_pct(
            dsc_tipo_seguimento_pct, dtc_criacao, 
            ind_excluido)
    VALUES ('Veredeiros', now(), false);

INSERT INTO tipo_seguimento_pct(
            dsc_tipo_seguimento_pct, dtc_criacao, 
            ind_excluido)
    VALUES ('Apanhadores de flores sempre vivas', now(), false);

INSERT INTO tipo_seguimento_pct(
            dsc_tipo_seguimento_pct, dtc_criacao, 
            ind_excluido)
    VALUES ('Pantaneiros', now(), false);

INSERT INTO tipo_seguimento_pct(
            dsc_tipo_seguimento_pct, dtc_criacao, 
            ind_excluido)
    VALUES ('Morroquianos', now(), false);

INSERT INTO tipo_seguimento_pct(
            dsc_tipo_seguimento_pct, dtc_criacao, 
            ind_excluido)
    VALUES ('Povo pomerano', now(), false);

INSERT INTO tipo_seguimento_pct(
            dsc_tipo_seguimento_pct, dtc_criacao, 
            ind_excluido)
    VALUES ('Catadores de mangaba', now(), false);

INSERT INTO tipo_seguimento_pct(
            dsc_tipo_seguimento_pct, dtc_criacao, 
            ind_excluido)
    VALUES ('Quebradeiras de coco babaçu', now(), false);

INSERT INTO tipo_seguimento_pct(
            dsc_tipo_seguimento_pct, dtc_criacao, 
            ind_excluido)
    VALUES ('Retireiros do Araguaia', now(), false);

INSERT INTO tipo_seguimento_pct(
            dsc_tipo_seguimento_pct, dtc_criacao, 
            ind_excluido)
    VALUES ('Comunidades de fundos e fechos de pasto', now(), false);

INSERT INTO tipo_seguimento_pct(
            dsc_tipo_seguimento_pct, dtc_criacao, 
            ind_excluido)
    VALUES ('Ribeirinhos', now(), false);

INSERT INTO tipo_seguimento_pct(
            dsc_tipo_seguimento_pct, dtc_criacao, 
            ind_excluido)
    VALUES ('Cipozeiros', now(), false);

INSERT INTO tipo_seguimento_pct(
            dsc_tipo_seguimento_pct, dtc_criacao, 
            ind_excluido)
    VALUES ('Andirobeiros', now(), false);

INSERT INTO tipo_seguimento_pct(
            dsc_tipo_seguimento_pct, dtc_criacao, 
            ind_excluido)
    VALUES ('Caboclos', now(), false);

INSERT INTO tipo_seguimento_pct(
            dsc_tipo_seguimento_pct, dtc_criacao, 
            ind_excluido)
    VALUES ('Juventude de povos e comunidades tradicionais', now(), false);

INSERT INTO tipo_seguimento_pct(
            dsc_tipo_seguimento_pct, dtc_criacao, 
            ind_excluido)
    VALUES ('Outros', now(), false);

--==============================================================================

INSERT INTO tipo_vinculo_imovel(nom_tipo_vinculo_imovel)
    VALUES ('Representante de comunidade');
    
--==============================================================================

UPDATE tipo_documento_imovel_rural as tipo
SET ide_tipo_territorio_pct = (SELECT ide_tipo_vinculo_imovel
      FROM  tipo_documento_imovel_rural as tipo2 where tipo.ide_tipo_documento_imovel_rural = tipo2.ide_tipo_documento_imovel_rural);    
    
    
    
INSERT INTO tipo_documento_imovel_rural(
            sgl_tipo_documento_imovel_rural, 
            dsc_tipo_documento_imovel_rural, ide_tipo_vinculo_imovel, num_grupo_documento, 
            ind_excluido, dtc_criacao, ide_tipo_territorio_pct)
    VALUES ('EREG', 'Em regularização', 
            6, 1, false, 
            now(), 1);

INSERT INTO tipo_documento_imovel_rural(
            sgl_tipo_documento_imovel_rural, 
            dsc_tipo_documento_imovel_rural, ide_tipo_vinculo_imovel, num_grupo_documento, 
            ind_excluido, dtc_criacao, ide_tipo_territorio_pct)
    VALUES ('CERG', 'Certidão de registro', 
            6, 1, false, 
            now(), 1);

--==============================================================================================


INSERT INTO tipo_documento_imovel_rural(
            sgl_tipo_documento_imovel_rural, 
            dsc_tipo_documento_imovel_rural, ide_tipo_vinculo_imovel, num_grupo_documento, 
            ind_excluido, dtc_criacao, ide_tipo_territorio_pct)
    VALUES ('CAAN', 'Carta de Anuência', 
            6, 2, false, 
            now(), 2);



INSERT INTO tipo_documento_imovel_rural(
            sgl_tipo_documento_imovel_rural, 
            dsc_tipo_documento_imovel_rural, ide_tipo_vinculo_imovel, num_grupo_documento, 
            ind_excluido, dtc_criacao, ide_tipo_territorio_pct)
    VALUES ('CCDT', 'Contrato de concessão de domínio de terras públicas', 
            6, 2, false, 
            now(), 2);

INSERT INTO tipo_documento_imovel_rural(
            sgl_tipo_documento_imovel_rural, 
            dsc_tipo_documento_imovel_rural, ide_tipo_vinculo_imovel, num_grupo_documento, 
            ind_excluido, dtc_criacao, ide_tipo_territorio_pct)
    VALUES ('TDRL', 'Título definitivo, com reserva florestal, em condomínio', 
            6, 2, false, 
            now(), 2);

INSERT INTO tipo_documento_imovel_rural(
            sgl_tipo_documento_imovel_rural, 
            dsc_tipo_documento_imovel_rural, ide_tipo_vinculo_imovel, num_grupo_documento, 
            ind_excluido, dtc_criacao, ide_tipo_territorio_pct)
    VALUES ('TDSR', 'Título definitivo sujeito a re-ratificação', 
            6, 2, false, 
            now(), 2);

INSERT INTO tipo_documento_imovel_rural(
            sgl_tipo_documento_imovel_rural, 
            dsc_tipo_documento_imovel_rural, ide_tipo_vinculo_imovel, num_grupo_documento, 
            ind_excluido, dtc_criacao, ide_tipo_territorio_pct)
    VALUES ('TDTA', 'Título definitivo transferido, com anuência do Órgão Fundiário (Estadual ou Federal)', 
            6, 2, false, 
            now(), 2);
--====================================================================
INSERT INTO tipo_documento_imovel_rural(
            sgl_tipo_documento_imovel_rural, 
            dsc_tipo_documento_imovel_rural, ide_tipo_vinculo_imovel, num_grupo_documento, 
            ind_excluido, dtc_criacao, ide_tipo_territorio_pct)
    VALUES ('CAOF', 'Contrato de assentamento do Órgão Fundiário (Estadual ou Federal)', 
            6, 5, false, 
            now(), 2);

--====================================================================

INSERT INTO tipo_documento_imovel_rural(
            sgl_tipo_documento_imovel_rural, 
            dsc_tipo_documento_imovel_rural, ide_tipo_vinculo_imovel, num_grupo_documento, 
            ind_excluido, dtc_criacao, ide_tipo_territorio_pct)
    VALUES ('DSRT', 'Declaração do sindicato rural ou sindicato dos trabalhadores rurais', 
            6, 4, false, 
            now(), 2);

INSERT INTO tipo_documento_imovel_rural(
            sgl_tipo_documento_imovel_rural, 
            dsc_tipo_documento_imovel_rural, ide_tipo_vinculo_imovel, num_grupo_documento, 
            ind_excluido, dtc_criacao, ide_tipo_territorio_pct)
    VALUES ('DESM', 'Declaração de assentamento municipal', 
            6, 4, false, 
            now(), 2);

INSERT INTO tipo_documento_imovel_rural(
            sgl_tipo_documento_imovel_rural, 
            dsc_tipo_documento_imovel_rural, ide_tipo_vinculo_imovel, num_grupo_documento, 
            ind_excluido, dtc_criacao, ide_tipo_territorio_pct)
    VALUES ('TERA', 'Termo de autodeclaração', 
            6, 6, false, 
            now(), 2);

--=================================================================================

INSERT INTO tipo_documento_imovel_rural(
            sgl_tipo_documento_imovel_rural, 
            dsc_tipo_documento_imovel_rural, ide_tipo_vinculo_imovel, num_grupo_documento, 
            ind_excluido, dtc_criacao, ide_tipo_territorio_pct)
    VALUES ('CCUC', 'Contrato de Concessão de Uso – CCU', 
            6, 2, false, 
            now(), 3);

INSERT INTO tipo_documento_imovel_rural(
            sgl_tipo_documento_imovel_rural, 
            dsc_tipo_documento_imovel_rural, ide_tipo_vinculo_imovel, num_grupo_documento, 
            ind_excluido, dtc_criacao, ide_tipo_territorio_pct)
    VALUES ('CCDR', 'Contrato de Concessão de Direito Real de Uso – CCDRU', 
            6, 2, false, 
            now(), 3);

INSERT INTO tipo_documento_imovel_rural(
            sgl_tipo_documento_imovel_rural, 
            dsc_tipo_documento_imovel_rural, ide_tipo_vinculo_imovel, num_grupo_documento, 
            ind_excluido, dtc_criacao, ide_tipo_territorio_pct)
    VALUES ('TAAU', 'Termo de Autorização de Uso – TAU', 
            6, 2, false, 
            now(), 3);







      
      
UPDATE tipo_documento_imovel_rural as tipo
SET ide_tipo_territorio_pct = null where ide_tipo_documento_imovel_rural = 16;

--UPDATE tipo_documento_imovel_rural as tipo
--SET ide_tipo_territorio_pct = null where ide_tipo_documento_imovel_rural = 12;

UPDATE tipo_documento_imovel_rural as tipo
SET ide_tipo_territorio_pct = null where ide_tipo_documento_imovel_rural = 5;

UPDATE tipo_documento_imovel_rural as tipo
SET ide_tipo_territorio_pct = 3 where ide_tipo_documento_imovel_rural = 13;
      

--===============================================================================================
--====== sprint 5

--INSERT INTO parentesco(
--            dsc_parentesco, dtc_cadastro, ind_excluido)
--    VALUES ('Avô(ó)', now(), false);
--
--INSERT INTO parentesco(
--            dsc_parentesco, dtc_cadastro, ind_excluido)
--    VALUES ('Esposo(a)', now(), false);
--
--INSERT INTO parentesco(
--            dsc_parentesco, dtc_cadastro, ind_excluido)
--    VALUES ('Filho(a)', now(), false);
--
--INSERT INTO parentesco(
--            dsc_parentesco, dtc_cadastro, ind_excluido)
--    VALUES ('Neto(a)', now(), false);
--
--INSERT INTO parentesco(
--            dsc_parentesco, dtc_cadastro, ind_excluido)
--    VALUES ('Primo(a)', now(), false);
--
--INSERT INTO parentesco(
--            dsc_parentesco, dtc_cadastro, ind_excluido)
--    VALUES ('Sobrinho(a)', now(), false);
--
--INSERT INTO parentesco(
--            dsc_parentesco, dtc_cadastro, ind_excluido)
--    VALUES ('Tio(a)', now(), false);
--    
--INSERT INTO parentesco(
--            dsc_parentesco, dtc_cadastro, ind_excluido)
--    VALUES ('Pai', now(), false);
--
--INSERT INTO parentesco(
--            dsc_parentesco, dtc_cadastro, ind_excluido)
--    VALUES ('Mãe', now(), false);    

--======
--===============================================================================================    


insert into hist_tabela (ide_tabela, desc_tabela)
	values (31, 'pct_imovel_rural');
insert into hist_tabela (ide_tabela, desc_tabela)
	values (32, 'tipo_territorio_pct');
insert into hist_tabela (ide_tabela, desc_tabela)
	values (33, 'pct_imovel_rural_tipo_seguimento_pct');
insert into hist_tabela (ide_tabela, desc_tabela)
	values (34, 'pessoa_juridica_pct');
	
	
--===============================================================================================	
--====== sprint 7

INSERT INTO tipo_projeto(
            nom_tipo_projeto, dtc_criacao, ind_excluido)
    VALUES ('Território de Povos e Comunidades Tradicionais', now(), false);
--======
--===============================================================================================	
--====== sprint 8

INSERT INTO tipo_documento_imovel_rural(
            sgl_tipo_documento_imovel_rural, 
            dsc_tipo_documento_imovel_rural, num_grupo_documento, 
            ind_excluido, dtc_criacao, ide_tipo_territorio_pct)
    VALUES ('TDOMC','Título de domínio',2,FALSE,NOW(),2);

INSERT INTO tipo_documento_imovel_rural(
            sgl_tipo_documento_imovel_rural, 
            dsc_tipo_documento_imovel_rural, num_grupo_documento, 
            ind_excluido, dtc_criacao, ide_tipo_territorio_pct)
    VALUES ('TDER','Em regularização',2,FALSE,NOW(),3);
 
INSERT INTO tipo_pessoa_requerimento(nom_tipo_pessoa_requerimento)
    VALUES ('Conveniado');    
    
--======
--===============================================================================================
    
INSERT INTO tipo_pessoa_juridica_pct(
           dsc_tipo_pessoa_juridica_pct, ind_excluido, dtc_criacao)
    VALUES ('Associação', false, now()); 

INSERT INTO tipo_pessoa_juridica_pct(
           dsc_tipo_pessoa_juridica_pct, ind_excluido, dtc_criacao)
    VALUES ('Concedente', false, now());

INSERT INTO tipo_pessoa_juridica_pct(
           dsc_tipo_pessoa_juridica_pct, ind_excluido, dtc_criacao)
    VALUES ('Concessionario', false, now());
    
INSERT INTO tipo_cadastro_imovel_rural(ide_tipo_cadastro_imovel_rural,dsc_tipo_cadastro_imovel_rural)
    VALUES (5,'Povos e comunidades tradicionais');
    
