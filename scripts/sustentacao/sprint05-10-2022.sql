-- [34600] - Alteração de Usuário

UPDATE
	usuario
SET
	ide_perfil = 2,
	ind_tipo_usuario = FALSE 
WHERE
	ide_pessoa_fisica = 28798;
-- [34072] - Update endereço de declaração
UPDATE
	declaracao_inexigibilidade_info_geral
SET
	ide_endereco = (
	SELECT
		ide_endereco
	FROM
		declaracao_inexigibilidade_info_unidade
	WHERE
		ide_declaracao_inexigibilidade = 10379)
WHERE
	ide_declaracao_inexigibilidade = 10379;
	
--[34607] - [REQUERIMENTO] Erro ao gerar requerimento (página quebra)

UPDATE
        empreendimento
SET
        nom_empreendimento = 'MP-CCOL-POV ALAGOINHA RURAL_X-0722251'
WHERE
        ide_empreendimento = 169428;
        
UPDATE
        empreendimento
SET
        nom_empreendimento = 'RR-CIND-FAZ DO SONO RURAL-JBD_X-0938700'
WHERE
        ide_empreendimento = 162046;
        
 UPDATE
        empreendimento
SET
        nom_empreendimento = 'RR-CCOL-FAZ EMPOEIRA CARINHANHA_X-0944545'
WHERE
        ide_empreendimento = 163134;
       
UPDATE
        empreendimento
SET
        nom_empreendimento = 'AC-REG-FV. RR-CCOL-POV. CALDEIRÃO RURAL (X-0987241)'
WHERE
        ide_empreendimento = 164475;
       
UPDATE
        empreendimento
SET
        nom_empreendimento = 'AC-PREF-POV GALINHO GALINHO-LEM_X-0994050'
WHERE
        ide_empreendimento = 168273;       
       
UPDATE
        empreendimento
SET
        nom_empreendimento = 'AC-PREF-POV GALINHO_GALINHO-LEM_X-0994050'
WHERE
        ide_empreendimento = 168276;       
       
UPDATE
        empreendimento
SET
        nom_empreendimento = 'MP-CCOL-POV ALAGOINHA_RURAL_X-0722251'
WHERE
        ide_empreendimento = 170141;
                
UPDATE
        empreendimento
SET
        nom_empreendimento = 'RR-CIND-FAZ DO SONO_RURAL-JBD_X-0938700'
WHERE
        ide_empreendimento = 162039;         
--[34418] - [CADASTRO] Imóvel Rural/CEFIR - erro na reserva legal
UPDATE
	reserva_legal
SET
	ide_imovel_rural = NULL
WHERE
	ide_reserva_legal = 341858;

--[33984] - [CADASTRO] Imóvel Rural/CEFIR - "Ocorreu um erro ao visualizar Termo de Compromisso."
UPDATE
        reserva_legal
SET
        ide_imovel_rural = NULL
WHERE
        ide_reserva_legal = 10947;	
	
--[34597] - [CADASTRO] "O CEP informado não pertence ao município do seu cadastro"

UPDATE
        endereco
SET
        ide_logradouro = 1617574
WHERE
        ide_endereco = 36142;	
