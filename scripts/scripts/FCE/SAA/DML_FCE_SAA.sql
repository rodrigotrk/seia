INSERT INTO documento_obrigatorio (nom_documento_obrigatorio,num_tamanho,ind_formulario,ind_publico,ide_tipo_documento_obrigatorio,ind_ativo)
VALUES ('Formulário de Caracterização do Empreendimento - Sistema de Abastecimento de Água (SAA)',0,'t','t',2,'t');

INSERT INTO documento_ato (ide_documento_obrigatorio, ide_ato_ambiental,ide_tipologia, ind_ativo)
VALUES(CURRVAL('DOCUMENTO_OBRIGATORIO_IDE_DOCUMENTO_OBRIGATORIO_seq'),1,null,'t');

INSERT INTO documento_ato (ide_documento_obrigatorio, ide_ato_ambiental,ide_tipologia, ind_ativo)
VALUES(CURRVAL('DOCUMENTO_OBRIGATORIO_IDE_DOCUMENTO_OBRIGATORIO_seq'),2,null,'t');

INSERT INTO documento_ato (ide_documento_obrigatorio, ide_ato_ambiental,ide_tipologia, ind_ativo)
VALUES(CURRVAL('DOCUMENTO_OBRIGATORIO_IDE_DOCUMENTO_OBRIGATORIO_seq'),3,null,'t');

INSERT INTO documento_ato (ide_documento_obrigatorio, ide_ato_ambiental,ide_tipologia, ind_ativo)
VALUES(CURRVAL('DOCUMENTO_OBRIGATORIO_IDE_DOCUMENTO_OBRIGATORIO_seq'),4,null,'t');

INSERT INTO documento_ato (ide_documento_obrigatorio, ide_ato_ambiental,ide_tipologia, ind_ativo)
VALUES(CURRVAL('DOCUMENTO_OBRIGATORIO_IDE_DOCUMENTO_OBRIGATORIO_seq'),6,null,'t');

INSERT INTO documento_ato (ide_documento_obrigatorio, ide_ato_ambiental,ide_tipologia, ind_ativo)
VALUES(CURRVAL('DOCUMENTO_OBRIGATORIO_IDE_DOCUMENTO_OBRIGATORIO_seq'),8,null,'t');


--Excluir FCE - SAA
CREATE OR REPLACE FUNCTION remover_fce_saa_by_requerimento(prequerimento character varying)
  RETURNS text AS
$BODY$ 
DECLARE pRequerimento ALIAS FOR $1; 
BEGIN			

DELETE FROM eta_tipo_composicao where ide_eta_tipo_composicao in 
	(SELECT ide_eta_tipo_composicao FROM eta_tipo_composicao WHERE ide_fce_saa_localizacao_geografica_eta  in 
		(SELECT ide_fce_saa_localizacao_geografica_eta FROM fce_saa_localizacao_geografica_eta WHERE ide_fce_saa   in 
			(SELECT ide_fce_saa FROM fce_saa WHERE ide_fce in 
					(select ide_fce from fce where ide_requerimento in 
						(select ide_requerimento from requerimento where num_requerimento = $1) 
					and ide_documento_obrigatorio = 10071))));
				

DELETE FROM fce_saa_localizacao_geografica_eta  where ide_fce_saa_localizacao_geografica_eta in 
	(SELECT ide_fce_saa_localizacao_geografica_eta FROM fce_saa_localizacao_geografica_eta WHERE ide_fce_saa  in 
		(SELECT ide_fce_saa FROM fce_saa WHERE ide_fce in 
				(select ide_fce from fce where ide_requerimento in 
					(select ide_requerimento from requerimento where num_requerimento = $1) 
				and ide_documento_obrigatorio = 10071)));
				
DELETE FROM fce_saa_localizacao_geografica_reservatorio where ide_fce_saa_localizacao_geografica_reservatorio in 
	(SELECT ide_fce_saa_localizacao_geografica_reservatorio FROM fce_saa_localizacao_geografica_reservatorio WHERE ide_fce_saa  in 
		(SELECT ide_fce_saa FROM fce_saa WHERE ide_fce in 
				(select ide_fce from fce where ide_requerimento in 
					(select ide_requerimento from requerimento where num_requerimento = $1) 
				and ide_documento_obrigatorio = 10071)));
				
DELETE FROM fce_saa_localizacao_geografica_elevatoria_bruta where ide_fce_saa_localizacao_geografica_elevatoria_bruta in 
	(SELECT ide_fce_saa_localizacao_geografica_elevatoria_bruta FROM fce_saa_localizacao_geografica_elevatoria_bruta WHERE ide_fce_saa  in 
		(SELECT ide_fce_saa FROM fce_saa WHERE ide_fce in 
				(select ide_fce from fce where ide_requerimento in 
					(select ide_requerimento from requerimento where num_requerimento = $1) 
				and ide_documento_obrigatorio = 10071)));

DELETE FROM fce_saa_localizacao_geografica_elevatoria_tratada where ide_fce_saa_localizacao_geografica_elevatoria_tratada in 
	(SELECT ide_fce_saa_localizacao_geografica_elevatoria_tratada FROM fce_saa_localizacao_geografica_elevatoria_tratada WHERE ide_fce_saa  in 
		(SELECT ide_fce_saa FROM fce_saa WHERE ide_fce in 
				(select ide_fce from fce where ide_requerimento in 
					(select ide_requerimento from requerimento where num_requerimento = $1) 
				and ide_documento_obrigatorio = 10071)));

DELETE FROM fce_saa_localizacao_geografica_dados_concedidos where ide_fce_saa_localizacao_geografica_dados_concedidos in 
	(SELECT ide_fce_saa_localizacao_geografica_dados_concedidos FROM fce_saa_localizacao_geografica_dados_concedidos WHERE ide_fce_saa  in 
		(SELECT ide_fce_saa FROM fce_saa WHERE ide_fce in 
				(select ide_fce from fce where ide_requerimento in 
					(select ide_requerimento from requerimento where num_requerimento = $1) 
				and ide_documento_obrigatorio = 10071)));				


DELETE FROM fce_saa where ide_fce_saa in 
		(SELECT ide_fce_saa FROM fce_saa WHERE ide_fce in 
				(select ide_fce from fce where ide_requerimento in 
					(select ide_requerimento from requerimento where num_requerimento = $1) 
				and ide_documento_obrigatorio = 10071));

DELETE FROM fce where ide_requerimento in 
					(select ide_requerimento from requerimento where num_requerimento = $1) 
				and ide_documento_obrigatorio = 10071;
RETURN 'FCE - Sistema de abastecimento de água!';
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION remover_fce_saa_by_requerimento(character varying)
  OWNER TO postgres;