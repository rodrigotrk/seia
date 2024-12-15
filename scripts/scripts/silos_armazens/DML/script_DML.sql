begin;

INSERT INTO funcionalidade_url(
            ide_funcionalidade, dsc_url, ind_principal)
    VALUES (69, '/paginas/manter-atividade-nao-sujeita-licenciamento/silos_armazens/silos_armazens_abas.xhtml', false);

--===========================================================================================================================

INSERT INTO operacao_desenvolvida_silos_armazens(
	    nom_operacao_desevolvida, 
            ind_excluido)
    VALUES ('Recebimento de grãos:(moegas)', 
            false);

INSERT INTO operacao_desenvolvida_silos_armazens(
	    nom_operacao_desevolvida, 
            ind_excluido)
    VALUES ('Pré-limpeza', 
            false);

INSERT INTO operacao_desenvolvida_silos_armazens(
	    nom_operacao_desevolvida, 
            ind_excluido)
    VALUES ('Secagem', 
            false);            

INSERT INTO operacao_desenvolvida_silos_armazens(
	    nom_operacao_desevolvida, 
            ind_excluido)
    VALUES ('Limpeza', 
            false);

INSERT INTO operacao_desenvolvida_silos_armazens(
	    nom_operacao_desevolvida, 
            ind_excluido)
    VALUES ('Beneficiamento', 
            false);

INSERT INTO operacao_desenvolvida_silos_armazens(
	    nom_operacao_desevolvida, 
            ind_excluido)
    VALUES ('Seleção/classificação de sementes', 
            false);

INSERT INTO operacao_desenvolvida_silos_armazens(
	    nom_operacao_desevolvida, 
            ind_excluido)
    VALUES ('Armazenagem', 
            false);

INSERT INTO operacao_desenvolvida_silos_armazens(
	    nom_operacao_desevolvida, 
            ind_excluido)
    VALUES ('Expedição', 
            false);

INSERT INTO operacao_desenvolvida_silos_armazens(
	    nom_operacao_desevolvida, 
            ind_excluido)
    VALUES ('Armazém de insumos', 
            false);
INSERT INTO operacao_desenvolvida_silos_armazens(
	    nom_operacao_desevolvida, 
            ind_excluido)
    VALUES ('Outro', 
            false);

INSERT INTO operacao_desenvolvida_silos_armazens(
	    nom_operacao_desevolvida, 
            ind_excluido, ide_operacao_desenvolvida_pai)
select 'Descasque', false, ide_operacao_desenvolvida_silos_armazens from operacao_desenvolvida_silos_armazens where nom_operacao_desevolvida  like 'Beneficiamento';
   

INSERT INTO operacao_desenvolvida_silos_armazens(
	    nom_operacao_desevolvida, 
            ind_excluido, ide_operacao_desenvolvida_pai)
select 'Polimento', false, ide_operacao_desenvolvida_silos_armazens from operacao_desenvolvida_silos_armazens where nom_operacao_desevolvida  like 'Beneficiamento';


INSERT INTO operacao_desenvolvida_silos_armazens(
	    nom_operacao_desevolvida, 
            ind_excluido, ide_operacao_desenvolvida_pai)
select 'Parboilização', false, ide_operacao_desenvolvida_silos_armazens from operacao_desenvolvida_silos_armazens where nom_operacao_desevolvida  like 'Beneficiamento';


INSERT INTO operacao_desenvolvida_silos_armazens(
	    nom_operacao_desevolvida, 
            ind_excluido, ide_operacao_desenvolvida_pai)
select 'Outro', false, ide_operacao_desenvolvida_silos_armazens from operacao_desenvolvida_silos_armazens where nom_operacao_desevolvida  like 'Beneficiamento';            

--======================================================================================================================================================

INSERT INTO tipo_combustivel_silo_armazens(
            nom_tipo_combustivel)
    VALUES ('GLP');

INSERT INTO tipo_combustivel_silo_armazens(
            nom_tipo_combustivel)
    VALUES ('Gás Natural');

INSERT INTO tipo_combustivel_silo_armazens(
            nom_tipo_combustivel)
    VALUES ('Óleo');

INSERT INTO tipo_combustivel_silo_armazens(
            nom_tipo_combustivel)
    VALUES ('Madeira');

INSERT INTO tipo_combustivel_silo_armazens(
            nom_tipo_combustivel)
    VALUES ('Outro');

INSERT INTO tipo_combustivel_silo_armazens(
            nom_tipo_combustivel, 
            ide_tipo_combustivel_silo_armazens_pai)
Select 'Madeira de origem Nativa',ide_tipo_combustivel_silo_armazens from tipo_combustivel_silo_armazens where nom_tipo_combustivel like 'Madeira';

INSERT INTO tipo_combustivel_silo_armazens(
            nom_tipo_combustivel, 
            ide_tipo_combustivel_silo_armazens_pai)
Select 'Madeira de origem Exótica',ide_tipo_combustivel_silo_armazens from tipo_combustivel_silo_armazens where nom_tipo_combustivel like 'Madeira';

--==================================================================================================================================================

INSERT INTO tipo_especie_armazem(
            nom_tipo_especie_armazem, ind_ativo)
    VALUES ('Convencional', true);

INSERT INTO tipo_especie_armazem(
            nom_tipo_especie_armazem, ind_ativo)
    VALUES ('Graneleiro', true);



INSERT INTO tipo_armazem(
            nom_tipo_armazem, ide_tipo_especie_armazem, 
            ind_ativo)
Select 'Convencional',ide_tipo_especie_armazem,true from tipo_especie_armazem  where nom_tipo_especie_armazem  like 'Convencional';
   
INSERT INTO tipo_armazem(
            nom_tipo_armazem, ide_tipo_especie_armazem, 
            ind_ativo)
Select 'Depósito',ide_tipo_especie_armazem,true from tipo_especie_armazem  where nom_tipo_especie_armazem  like 'Convencional';

INSERT INTO tipo_armazem(
            nom_tipo_armazem, ide_tipo_especie_armazem, 
            ind_ativo)
Select 'Estrutural',ide_tipo_especie_armazem,true from tipo_especie_armazem  where nom_tipo_especie_armazem  like 'Convencional';



INSERT INTO tipo_armazem(
            nom_tipo_armazem, ide_tipo_especie_armazem, 
            ind_ativo)
Select 'Graneleiro',ide_tipo_especie_armazem,true from tipo_especie_armazem  where nom_tipo_especie_armazem  like 'Graneleiro';

INSERT INTO tipo_armazem(
            nom_tipo_armazem, ide_tipo_especie_armazem, 
            ind_ativo)
Select 'Silo',ide_tipo_especie_armazem,true from tipo_especie_armazem  where nom_tipo_especie_armazem  like 'Graneleiro';

INSERT INTO tipo_armazem(
            nom_tipo_armazem, ide_tipo_especie_armazem, 
            ind_ativo)
Select 'Bateria de Silos',ide_tipo_especie_armazem,true from tipo_especie_armazem  where nom_tipo_especie_armazem  like 'Graneleiro';

INSERT INTO tipo_armazem(
            nom_tipo_armazem, ide_tipo_especie_armazem, 
            ind_ativo)
Select 'Chapéu chinês',ide_tipo_especie_armazem,true from tipo_especie_armazem  where nom_tipo_especie_armazem  like 'Graneleiro';

--=====================================================================================================================================================

INSERT INTO silos_armazens_origem_agua(
            nom_origem_agua, ind_ativo)
    VALUES ('Poço de captação', true);

INSERT INTO silos_armazens_origem_agua(
            nom_origem_agua, ind_ativo)
    VALUES ('Recurso hídrico superficial', true);

INSERT INTO silos_armazens_origem_agua(
            nom_origem_agua, ind_ativo)
    VALUES ('Concessionária', true);

--=====================================================================================================================================================

INSERT INTO tipo_concessionaria(
            nom_concessionarias, ind_ativo)
    VALUES ('Embasa', true);

INSERT INTO tipo_concessionaria(
            nom_concessionarias, ind_ativo)
    VALUES ('SAAE', true);

--=====================================================================================================================================================

INSERT INTO sistema_tratamento_agua(
            nom_sistema_tratamento_agua, ind_ativo)
    VALUES ('Digestor Anaeróbico de Fluxo Ascendente - DAFA', true);

INSERT INTO sistema_tratamento_agua(
            nom_sistema_tratamento_agua, ind_ativo)
    VALUES ('Estação de tratamento de efluentes dentro da empresa', true);

INSERT INTO sistema_tratamento_agua(
            nom_sistema_tratamento_agua, ind_ativo)
    VALUES ('Fossa séptica', true);

INSERT INTO sistema_tratamento_agua(
            nom_sistema_tratamento_agua, ind_ativo)
    VALUES ('Sumidouro', true);

INSERT INTO sistema_tratamento_agua(
            nom_sistema_tratamento_agua, ind_ativo)
    VALUES ('Caixa separadora de água e óleo - CSAO', true);

INSERT INTO sistema_tratamento_agua(
            nom_sistema_tratamento_agua, ind_ativo)
    VALUES ('Wetland', true);

INSERT INTO sistema_tratamento_agua(
            nom_sistema_tratamento_agua, ind_ativo)
    VALUES ('Filtros', true);

INSERT INTO sistema_tratamento_agua(
            nom_sistema_tratamento_agua, ind_ativo)
    VALUES ('Outros', true);

--====================================================================================================================================================

INSERT INTO silos_armazens_caracterizacao_atmosferica(
	    nom_caracterizacao_atmosferica, 
            ind_ativo)
    VALUES ('Gases de combustão oriundos dos equipamentos e dispositivos de queima de combustível', true);

INSERT INTO silos_armazens_caracterizacao_atmosferica(
	    nom_caracterizacao_atmosferica, 
            ind_ativo)
    VALUES ('Material Particulado oriundo das operações da unidade', true);

INSERT INTO silos_armazens_caracterizacao_atmosferica(
	    nom_caracterizacao_atmosferica, 
            ind_ativo)
    VALUES ('Outras fontes de poluição do ar', true);

INSERT INTO silos_armazens_caracterizacao_atmosferica(
	    nom_caracterizacao_atmosferica, 
            ind_ativo)
    VALUES ('Compostos orgânicos voláteis - VOC''s', true);

--===================================================================================================================================================

INSERT INTO equipamento_controle(
            nom_equipamento_controle, ind_ativo)
    VALUES ('Câmara de sedimentação gravitacional', true);


INSERT INTO equipamento_controle(
            nom_equipamento_controle, ind_ativo)
    VALUES ('Ciclone', true);


 INSERT INTO equipamento_controle(
            nom_equipamento_controle, ind_ativo)
    VALUES ('Filtros', true);

INSERT INTO equipamento_controle(
            nom_equipamento_controle, ind_ativo)
    VALUES ('Lavadores', true);

INSERT INTO equipamento_controle(
            nom_equipamento_controle, ind_ativo)
    VALUES ('Precipitadores eletrostáticos', true);

INSERT INTO equipamento_controle(
            nom_equipamento_controle, ind_ativo)
    VALUES ('Condensadores', true);

INSERT INTO equipamento_controle(
            nom_equipamento_controle, ind_ativo)
    VALUES ('Absorvedores', true);

INSERT INTO equipamento_controle(
            nom_equipamento_controle, ind_ativo)
    VALUES ('Outros', true);

--==================================================================================================================================================

INSERT INTO medida_controle_emissao(
            nom_medida_controle_emissao, ind_ativo)
    VALUES ('Implantação de barreira vegetal ou artificial no entorno da área operacional', true);

INSERT INTO medida_controle_emissao(
            nom_medida_controle_emissao, ind_ativo)
    VALUES ('Pavimentação de vias de acesso de propriedades ou uso exclusivo da empresa', true);

 INSERT INTO medida_controle_emissao(
            nom_medida_controle_emissao, ind_ativo)
    VALUES ('Enclausuramento de equipamentos', true);

 INSERT INTO medida_controle_emissao(
            nom_medida_controle_emissao, ind_ativo)
    VALUES ('Monitoramento da concentração de Partículas Totais em Suspensão ou de Partículas Inaláveis na área de principal impacto da unidade', true);

INSERT INTO medida_controle_emissao(
            nom_medida_controle_emissao, ind_ativo)
    VALUES ('Cobertura superior e laterais das correias transportadoras, que operarem a céu aberto', true);

INSERT INTO medida_controle_emissao(
            nom_medida_controle_emissao, ind_ativo)
    VALUES ('Aspersão de água nas vias internas de forma a diminuir a geração e dispersão de material particulado', true);

INSERT INTO medida_controle_emissao(
            nom_medida_controle_emissao, ind_ativo)
    VALUES ('Outros', true);

--===============================================================================================================================================

INSERT INTO classificacao_residuo(
            ind_ativo, nom_classificacao_residuo)
    VALUES (true, 'Classe I - Perigosos');

INSERT INTO classificacao_residuo(
            ind_ativo, nom_classificacao_residuo)
    VALUES (true, 'Classe II A - Não inertes');

INSERT INTO classificacao_residuo(
            ind_ativo, nom_classificacao_residuo)
    VALUES (true, 'Classe II B - Inertes');

--==============================================================================================================================================

INSERT INTO destinacao_final(
            ind_ativo, nom_destinacao_final)
    VALUES (true, 'Reutilização');

INSERT INTO destinacao_final(
            ind_ativo, nom_destinacao_final)
    VALUES (true, 'Reciclagem');

INSERT INTO destinacao_final(
            ind_ativo, nom_destinacao_final)
    VALUES (true, 'Compostagem');

INSERT INTO destinacao_final(
            ind_ativo, nom_destinacao_final)
    VALUES (true, 'Recuperação');

INSERT INTO destinacao_final(
            ind_ativo, nom_destinacao_final)
    VALUES (true, 'Aproveitamento energético');

INSERT INTO destinacao_final(
            ind_ativo, nom_destinacao_final)
    VALUES (true, 'Coprocessamento');

INSERT INTO destinacao_final(
            ind_ativo, nom_destinacao_final)
    VALUES (true, 'Aterro');

INSERT INTO destinacao_final(
            ind_ativo, nom_destinacao_final)
    VALUES (true, 'Incineração');

 INSERT INTO destinacao_final(
            ind_ativo, nom_destinacao_final)
    VALUES (true, 'Outros');
   
--============================================================================================================================================

INSERT INTO silos_armazens_sistema_seguranca(
            nom_sistema_segurana, ind_ativo)
    VALUES ('Sistema de ventilação para ambientes confinados e semiconfinados', true);

INSERT INTO silos_armazens_sistema_seguranca(
            nom_sistema_segurana, ind_ativo)
    VALUES ('Sistema de combate a incêndio', true);

INSERT INTO silos_armazens_sistema_seguranca(
            nom_sistema_segurana, ind_ativo)
    VALUES ('Indicador de gases ou detector de gases', true); 

INSERT INTO silos_armazens_sistema_seguranca(
            nom_sistema_segurana, ind_ativo)
    VALUES ('Controle de pragas e roedores', true);

INSERT INTO silos_armazens_sistema_seguranca(
            nom_sistema_segurana, ind_ativo)
    VALUES ('Sistema de proteção contra fenômenos naturais', true);

INSERT INTO silos_armazens_sistema_seguranca(
            nom_sistema_segurana, ind_ativo)
    VALUES ('PPRA - Programa de Prevenção de Riscos Ambientais', true);

INSERT INTO silos_armazens_sistema_seguranca(
            nom_sistema_segurana, ind_ativo)
    VALUES ('Programa de Gerenciamento de Risco (PGR) conforme Resolução CEPRAM n° 3965/2009', true);

INSERT INTO silos_armazens_sistema_seguranca(
            nom_sistema_segurana, ind_ativo)
    VALUES ('Outros', true);
    


--=============================================================================================================================================



update tipo_atividade_nao_sujeita_licenciamento set ind_ativo = true where ide_tipo_atividade_nao_sujeita_licenciamento = 1;
   
commit;


--============================================================================================================================================

begin;

INSERT INTO documento_obrigatorio(
            nom_documento_obrigatorio, num_tamanho, 
            ind_formulario, ind_publico, ide_tipo_documento_obrigatorio, 
            ind_ativo)
    VALUES ('Manifestação do(s) município(s) que demonstre(m) a conformidade da localização do empreendimento ou atividade com a legislação aplicável ao uso e ocupação do solo;', 0.000, 
            false, true, 1, true);


INSERT INTO atividade_nao_sujeita_licenciamento_documento(
            ide_documento_obrigatorio, 
            ide_tipo_atividade_nao_sujeita_licenciamento)
SELECT currval('DOCUMENTO_OBRIGATORIO_IDE_DOCUMENTO_OBRIGATORIO_seq'), 1 ;


INSERT INTO documento_obrigatorio(
            nom_documento_obrigatorio, num_tamanho, 
            ind_formulario, ind_publico, ide_tipo_documento_obrigatorio, 
            ind_ativo)
    VALUES ('Declaração de cessão de uso do proprietário superficiário ou autorização de passagem acompanhado do devido documento comprobatório de propriedade ou posse do imóvel quando o empreendimento implicar em intervenção ou passagem em áreas de terceiros', 0.000, 
            false, true, 1, true);

INSERT INTO atividade_nao_sujeita_licenciamento_documento(
             ide_documento_obrigatorio, 
             ide_tipo_atividade_nao_sujeita_licenciamento)
SELECT currval('DOCUMENTO_OBRIGATORIO_IDE_DOCUMENTO_OBRIGATORIO_seq'), 1 ;


INSERT INTO documento_obrigatorio(
            nom_documento_obrigatorio, num_tamanho, 
            ind_formulario, ind_publico, ide_tipo_documento_obrigatorio, 
            ind_ativo)
    VALUES ('Cópia da Portaria da outorga de direito de uso de recursos hídricos ou da Declaração de Dispensa de Outorga para captação a ser realizada', 0.000, 
            false, true, 1, true);

INSERT INTO atividade_nao_sujeita_licenciamento_documento(
             ide_documento_obrigatorio, 
             ide_tipo_atividade_nao_sujeita_licenciamento)
SELECT currval('DOCUMENTO_OBRIGATORIO_IDE_DOCUMENTO_OBRIGATORIO_seq'), 1 ;


INSERT INTO documento_obrigatorio(
            nom_documento_obrigatorio, num_tamanho, 
            ind_formulario, ind_publico, ide_tipo_documento_obrigatorio, 
            ind_ativo)
    VALUES ('Cópia da Portaria da outorga de direito de uso de recursos hídricos ou da Declaração de Dispensa de Outorga para o lançamento de efluentes a ser realizado', 0.000, 
            false, true, 1, true);

INSERT INTO atividade_nao_sujeita_licenciamento_documento(
             ide_documento_obrigatorio, 
             ide_tipo_atividade_nao_sujeita_licenciamento)
SELECT currval('DOCUMENTO_OBRIGATORIO_IDE_DOCUMENTO_OBRIGATORIO_seq'), 1 ;


INSERT INTO documento_obrigatorio(
            nom_documento_obrigatorio, num_tamanho, 
            ind_formulario, ind_publico, ide_tipo_documento_obrigatorio, 
            ind_ativo)
    VALUES ('Auto de Vistoria do Corpo de Bombeiros - AVCB expedido pelo Corpo de Bombeiros Militar da Bahia - CBMBA conforme disposto no Regulamento da Lei Estadual nº 12.929/2013 aprovado pelo Decreto nº 16.302/2015', 0.000, 
            false, true, 1, true);

INSERT INTO atividade_nao_sujeita_licenciamento_documento(
             ide_documento_obrigatorio, 
             ide_tipo_atividade_nao_sujeita_licenciamento)
SELECT currval('DOCUMENTO_OBRIGATORIO_IDE_DOCUMENTO_OBRIGATORIO_seq'), 1 ;


INSERT INTO atividade_nao_sujeita_licenciamento_documento(
            ide_documento_obrigatorio, 
            ide_tipo_atividade_nao_sujeita_licenciamento)
VALUES (124, 1);


INSERT INTO documento_obrigatorio(
            nom_documento_obrigatorio, num_tamanho, 
            ind_formulario, ind_publico, ide_tipo_documento_obrigatorio, 
            ind_ativo)
    VALUES ('Certificado Madeira Nativa', 0.000, 
            false, true, 1, true);

INSERT INTO atividade_nao_sujeita_licenciamento_documento(
             ide_documento_obrigatorio, 
             ide_tipo_atividade_nao_sujeita_licenciamento)
SELECT currval('DOCUMENTO_OBRIGATORIO_IDE_DOCUMENTO_OBRIGATORIO_seq'), 1 ;


INSERT INTO documento_obrigatorio(
            nom_documento_obrigatorio, num_tamanho, 
            ind_formulario, ind_publico, ide_tipo_documento_obrigatorio, 
            ind_ativo)
    VALUES ('Certificado Madeira Exótica', 0.000, 
            false, true, 1, true);

INSERT INTO atividade_nao_sujeita_licenciamento_documento(
             ide_documento_obrigatorio, 
             ide_tipo_atividade_nao_sujeita_licenciamento)
SELECT currval('DOCUMENTO_OBRIGATORIO_IDE_DOCUMENTO_OBRIGATORIO_seq'), 1 ;

--=============================================================================================================================================


INSERT INTO documento_obrigatorio(
            nom_documento_obrigatorio, num_tamanho, 
            ind_formulario, dsc_caminho_arquivo, ind_publico, ide_tipo_documento_obrigatorio, 
            ind_ativo)
    VALUES ('Relatório de Caracterização da Atividade e do Meio Ambiente acompanhada da respectiva ART - Silos e Armazéns', 0.000, 
            false, '/opt/ARQUIVOS/DOCUMENTOOBRIGATORIO/ART_SILOS_E_ARMAZÉNS.docx', true, 2, true);

INSERT INTO atividade_nao_sujeita_licenciamento_documento(
             ide_documento_obrigatorio, 
             ide_tipo_atividade_nao_sujeita_licenciamento)
SELECT currval('DOCUMENTO_OBRIGATORIO_IDE_DOCUMENTO_OBRIGATORIO_seq'), 1 ;


INSERT INTO documento_obrigatorio(
            nom_documento_obrigatorio, num_tamanho, 
            ind_formulario, ind_publico, ide_tipo_documento_obrigatorio, 
            ind_ativo)
    VALUES ('Planta georreferenciada de localização do empreendimento, em escala adequada, com indicação de área rural, urbana ou de expansão urbana; povoado, distrito, município, vias de acesso e quilometragem a partir da sede municipal mais próxima, bem como pontos de referência como fazenda(s), vila(s) ou povoado(s), corpos d’água próximos e áreas protegidas por lei, em meio digital', 0.000, 
            false, true, 2, true);

INSERT INTO atividade_nao_sujeita_licenciamento_documento(
             ide_documento_obrigatorio, 
             ide_tipo_atividade_nao_sujeita_licenciamento)
SELECT currval('DOCUMENTO_OBRIGATORIO_IDE_DOCUMENTO_OBRIGATORIO_seq'), 1 ;

INSERT INTO documento_obrigatorio(
            nom_documento_obrigatorio, num_tamanho, 
            ind_formulario, ind_publico, ide_tipo_documento_obrigatorio, 
            ind_ativo)
    VALUES ('Programa de Controle de Material Particulado', 0.000, 
            false, true, 2, true); 
             
INSERT INTO atividade_nao_sujeita_licenciamento_documento(
             ide_documento_obrigatorio, 
             ide_tipo_atividade_nao_sujeita_licenciamento)
SELECT currval('DOCUMENTO_OBRIGATORIO_IDE_DOCUMENTO_OBRIGATORIO_seq'), 1 ;

INSERT INTO documento_obrigatorio(
            nom_documento_obrigatorio, num_tamanho, 
            ind_formulario, ind_publico, ide_tipo_documento_obrigatorio, 
            ind_ativo)
    VALUES ('Plano de Gerenciamento de Resíduos Sólidos', 0.000, 
            false, true, 2, true);

INSERT INTO atividade_nao_sujeita_licenciamento_documento(
             ide_documento_obrigatorio, 
             ide_tipo_atividade_nao_sujeita_licenciamento)
SELECT currval('DOCUMENTO_OBRIGATORIO_IDE_DOCUMENTO_OBRIGATORIO_seq'), 1 ;


commit;   

--=============================================================================================================================================


begin;

insert into tipo_certificado values(9, 'CSA');

CREATE SEQUENCE tipo_certificado_csa_seq;

commit;





