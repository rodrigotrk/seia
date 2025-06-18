INSERT INTO ato_sinaflor (ide_ato_sinaflor, nom_ato_sinaflor) VALUES (370, 'Uso Alternativo do Solo – UAS');

UPDATE ato_ambiental SET ide_ato_sinaflor=370 WHERE ide_ato_ambiental=12;

INSERT INTO parametro(ide_parametro, nom_parametro, dsc_valor) VALUES (39, 'Horário da execução da rotina do SINAFLOR', '0 0 3 * * ?');

UPDATE produto SET dsc_produto = 'Dormente' WHERE dsc_produto = 'Dormentes';
UPDATE produto SET dsc_produto = 'Tora' WHERE dsc_produto = 'Toros';
UPDATE produto SET dsc_produto = 'Toretes' WHERE dsc_produto = 'Tora torete';

--FUNCIONALIDADES E PERMISSOES
INSERT INTO funcionalidade VALUES(
nextval(('Funcionalidade_IDE_FUNCIONALIDADE_seq'::text)::regclass), 1, 
'Sincronia Sinaflor', false, now(), null);

INSERT INTO funcionalidade_url VALUES (
(SELECT ide_funcionalidade FROM funcionalidade WHERE dsc_funcionalidade = 'Sincronia Sinaflor'),
'/paginas/manter-sinaflor/consulta.xhtml', false);

INSERT INTO acao VALUES (nextval(('Acao_IDE_ACAO_seq'::text)::regclass), 'Sincronizar', false, now(), null);

INSERT INTO funcionalidade_acao VALUES(
nextval(('funcionalidade_acao_ide_funcionalidade_acao_seq'::text)::regclass), 
(SELECT ide_funcionalidade FROM funcionalidade WHERE dsc_funcionalidade = 'Sincronia Sinaflor'), 4);

INSERT INTO funcionalidade_acao VALUES(
nextval(('funcionalidade_acao_ide_funcionalidade_acao_seq'::text)::regclass), 
(SELECT ide_funcionalidade FROM funcionalidade WHERE dsc_funcionalidade = 'Sincronia Sinaflor'), 6);

INSERT INTO funcionalidade_acao VALUES(
nextval(('funcionalidade_acao_ide_funcionalidade_acao_seq'::text)::regclass), 
(SELECT ide_funcionalidade FROM funcionalidade WHERE dsc_funcionalidade = 'Sincronia Sinaflor'), 78);

INSERT INTO rel_grupo_perfil_funcionalidade VALUES ((SELECT ide_funcionalidade FROM funcionalidade WHERE dsc_funcionalidade = 'Sincronia Sinaflor'), 4, 1);
INSERT INTO rel_grupo_perfil_funcionalidade VALUES ((SELECT ide_funcionalidade FROM funcionalidade WHERE dsc_funcionalidade = 'Sincronia Sinaflor'), 6, 1);
INSERT INTO rel_grupo_perfil_funcionalidade VALUES ((SELECT ide_funcionalidade FROM funcionalidade WHERE dsc_funcionalidade = 'Sincronia Sinaflor'), 78, 1);
