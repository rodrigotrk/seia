
--[34509] - [CADASTRO] "O CEP informado não pertence ao município do seu cadastro"

--Imóvel - Fazenda Guanabara - MAT - 6682
UPDATE
	endereco
SET
	ide_logradouro = 3887998
WHERE
	ide_endereco = 2240052;
	
--Imóvel - FAZENDA LAGOA GRANDE
UPDATE
	endereco
SET
	ide_logradouro = 3887998
WHERE
	ide_endereco = 2306778;

--[34505] - [REQUERIMENTO] Falha ao tentar gerar a LAC
UPDATE
        licenca
SET
        ind_excluido = TRUE
WHERE
        ide_licenca = 48575;
UPDATE
        licenca
SET
        ind_excluido = FALSE
WHERE
        ide_licenca = 48442;

--[34513] -Processo sumiu do sistema

	UPDATE 
        controle_tramitacao
	SET 
        ind_fim_da_fila = TRUE  
	WHERE 
        ide_controle_tramitacao =574553;   

--[34515] -Processo sumiu do sistema

	UPDATE 
		controle_tramitacao
	SET 
		ind_fim_da_fila = TRUE  
	WHERE 
        ide_controle_tramitacao =574540;  

--[34516] -Processo duplicado
--N° do processo 1: 2021.001.008816
	UPDATE 
        controle_tramitacao
	SET 
        ind_fim_da_fila = FALSE  
	WHERE 
        ide_controle_tramitacao =528545;
--N° do processo 2: 2017.001.004508
	UPDATE 
        controle_tramitacao
	SET 
        ind_fim_da_fila = FALSE  
	WHERE 
        ide_controle_tramitacao =572539;  
        
--[34517] -Processo sumiu do sistema

UPDATE 
	controle_tramitacao
SET 
	ind_fim_da_fila = TRUE  
WHERE 
	ide_controle_tramitacao =573891;
        
--[34519] -Processo sumiu do sistema
UPDATE 
	controle_tramitacao
SET 
	ind_fim_da_fila = TRUE  
WHERE 
	ide_controle_tramitacao =574342;


BEGIN;

--[34516] -Processo duplicado
--N° do processo 1: 2021.001.008816

        UPDATE 
                controle_tramitacao
        SET 
                ind_fim_da_fila = FALSE  
        WHERE 
                ide_controle_tramitacao =528545;
--N° do processo 2: 2017.001.004508
        UPDATE 
                controle_tramitacao
        SET 
                ind_fim_da_fila = FALSE  
        WHERE 
                ide_controle_tramitacao =572539;  
        
--[34523] -Processo sumiu do sistema
        UPDATE 
                controle_tramitacao
        SET 
                ind_fim_da_fila = TRUE  
        WHERE 
                ide_controle_tramitacao =576089;   

--[34520] -Processo sumido do sistema

	UPDATE
		controle_tramitacao
	SET
		ind_fim_da_fila = TRUE
	WHERE
		ide_controle_tramitacao = 573292;
        
--[34480]- REQUERIMENTO Pagamento REPFLOR
        UPDATE
             tramitacao_requerimento
        SET
             ide_status_requerimento = 8
        WHERE
             ide_tramitacao_requerimento = 1324842;
       
--[34523] -Processo sumiu do sistema
	UPDATE 
        controle_tramitacao
	SET 
        ind_fim_da_fila = TRUE  
	WHERE 
        ide_controle_tramitacao =576089;        

        
--[34477]-Desmembramento de IR com RL realocada
        UPDATE 
                reserva_legal 
        SET 
                ide_tipo_arl = 3 
        WHERE 
                ide_reserva_legal = 9143; 
        
--[34526] -Processo sumiu do sistema

	UPDATE 
		controle_tramitacao
	SET 
		ind_fim_da_fila = TRUE  
	WHERE 
		ide_controle_tramitacao =575054; 

--[34519] -Processo sumiu do sistema
	UPDATE 
                controle_tramitacao
	SET 
                ind_fim_da_fila = TRUE  
	WHERE 
                ide_controle_tramitacao =574342;
--[34517] -Processo sumiu do sistema
	UPDATE 
                controle_tramitacao
	SET 
                ind_fim_da_fila = TRUE  
	WHERE 
                ide_controle_tramitacao =573891;        
        
--[34515]-Processo sumiu do sistema
                controle_tramitacao
        SET 
                ind_fim_da_fila = TRUE  
        WHERE 
                ide_controle_tramitacao =574540;  
--[34513] -Processo sumiu do sistema
        UPDATE 
                controle_tramitacao
        SET 
                ind_fim_da_fila = TRUE  
        WHERE 
                ide_controle_tramitacao =574553;   

--[34488] - [REQUERIMENTO] Erro ao inserir documentos
DELETE
FROM
        certificado c
WHERE
        ide_requerimento = 1148719;
DELETE
FROM
        requerimento_imovel
WHERE
        ide_requerimento = 1148719;
DELETE
FROM
        requerimento
WHERE
        ide_requerimento = 1148719
UPDATE
        requerimento
SET
        num_requerimento = '2022.001.236304/INEMA/REQ'
WHERE
        ide_requerimento = 1148718;
INSERT
        INTO
        public.certificado
(ide_certificado,
        dtc_emissao_certificado,
        ide_ato_ambiental,
        ide_orgao,
        ide_requerimento,
        num_certificado,
        num_token,
        ide_tipo_certificado)
VALUES(1251102,
'2022-06-03 10:13:20.075',
NULL,
1,
1148718,
'2022.001.465084/TC',
'3C03C4BE73F8593AAF0A18173F581D4D',
3);
INSERT
        INTO
        public.requerimento_imovel
(ide_requerimento,
        ide_imovel,
        ide_tipo_imovel_requerimento,
        vlr_area,
        dsc_ponto_referencia,
        dtc_criacao,
        ind_excluido,
        dtc_exclusao,
        ide_localizacao_geografica,
        ide_requerimento_imovel)
VALUES(1148718,
1189061,
2,
151.94,
NULL,
'2022-06-03 10:13:15.727',
FALSE,
NULL,
NULL,
1005571);                

--Imóvel - Fazenda Guanabara - MAT - 6682
        UPDATE
                endereco
        SET
                ide_logradouro = 3887998
        WHERE
                ide_endereco = 2240052;
	
--Imóvel - FAZENDA LAGOA GRANDE
        UPDATE
                endereco
        SET
                ide_logradouro = 3887998
        WHERE
                ide_endereco = 2306778;
--[34505] - [REQUERIMENTO] Falha ao tentar gerar a LAC
        UPDATE
                licenca
        SET
                ind_excluido = TRUE
        WHERE
                ide_licenca = 48575;
        UPDATE
                licenca
        SET
                ind_excluido = FALSE
        WHERE
                ide_licenca = 48442;
 
--[34480]- REQUERIMENTO Pagamento REPFLOR
        UPDATE
             tramitacao_requerimento
        SET
             ide_status_requerimento = 8
        WHERE
             ide_tramitacao_requerimento = 1324842;
             
--[34477]-Desmembramento de IR com RL realocada
        UPDATE 
                reserva_legal 
        SET 
                ide_tipo_arl = 3 
        WHERE 
                ide_reserva_legal = 9143;
  
--[34483] - [REQUERIMENTO] Pagamento REPFLOR
        UPDATE
                tramitacao_requerimento
        SET
                ide_status_requerimento = 8
        WHERE
                ide_tramitacao_requerimento = 1265495;
       
--[34532] - [PROCESSO] Processo sumiu do sistema

--[34555] - Erro ao editar empreendimento
	INSERT INTO
		imovel_urbano (ide_imovel_urbano,num_inscricao_iptu) 
	VALUES (1150001,'04065040640000');
--[34540] - Processo sumiu do sistema
		UPDATE 
       		controle_tramitacao 
		SET 
        	ind_fim_da_fila = TRUE 
		WHERE 
        	ide_controle_tramitacao = 573174;
--[34534] - [PROCESSO] Processo sumiu do sistema
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = TRUE
WHERE
        ide_controle_tramitacao IN (576475,576476);                
                
--[34536] - [PROCESSO] Processo sumiu do sistema 

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = TRUE
WHERE
	ide_controle_tramitacao = 576567;     

--[34534] - [PROCESSO] Processo sumiu do sistema
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = TRUE
WHERE
        ide_controle_tramitacao IN (576475,576476);                

--[34461] - [REQUERIMENTO] Requerimento sem número

DELETE
FROM
	certificado
WHERE
	ide_certificado = 1260773;

DELETE
FROM
	requerimento_imovel ri
WHERE
	ide_requerimento = 1156876;

DELETE
FROM
	requerimento
WHERE
	ide_requerimento = 1156876;

INSERT
	INTO
	public.certificado
(ide_certificado,
	dtc_emissao_certificado,
	ide_ato_ambiental,
	ide_orgao,
	ide_requerimento,
	num_certificado,
	num_token,
	ide_tipo_certificado)
VALUES(1260773,
'2022-07-14 15:37:19.751',
NULL,
1,
1156875,
'2022.001.469460/TC',
'15D901D334E5A8453AA893A6CCE50FD2',
3);

INSERT
	INTO
	public.requerimento_imovel
(ide_requerimento,
	ide_imovel,
	ide_tipo_imovel_requerimento,
	vlr_area,
	dsc_ponto_referencia,
	dtc_criacao,
	ind_excluido,
	dtc_exclusao,
	ide_localizacao_geografica,
	ide_requerimento_imovel)
VALUES(1156875,
1196850,
2,
3.34,
NULL,
'2022-07-14 15:37:13.101',
FALSE,
NULL,
NULL,
1011381);
	
UPDATE
	requerimento
SET
	num_requerimento = '2022.001.243631/INEMA/REQ'
WHERE
	ide_requerimento = 1156875; 
	
--[34532] - [PROCESSO] Processo sumiu do sistema

UPDATE
	controle_tramitacao
SET
	ind_fim_da_fila = TRUE
WHERE
	ide_requerimento = 1156875;  

--[34536] - [PROCESSO] Processo sumiu do sistema 

	UPDATE 
     	controle_tramitacao
	SET 
      	ind_fim_da_fila = TRUE  
	WHERE 
		ide_controle_tramitacao IN (577696, 577697);    
	
--[34530] - [PROCESSO] Processo sumiu do sistema
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = TRUE
WHERE
        ide_controle_tramitacao = 542313;                
        
--[34526] -Processo sumiu do sistema

	UPDATE 
		controle_tramitacao
	SET 
		ind_fim_da_fila = TRUE  
	WHERE 
		ide_controle_tramitacao =575054; 

-- [34518] - [PROCESSO] Processos que sumiram

        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 578555;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 578551;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 578548;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 578456;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 577974;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 577973;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 577816;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 577697;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 576872;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 576567;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 576476;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 576330;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 575472;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 575054;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 574782;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 574344;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 573936;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 573591;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 573581;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 573557;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 573222;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 573174;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 572985;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 572682;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 572075;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 571650;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 571629;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 571627;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 571540;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 571521;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 571520;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 571502;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 569151;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 568240;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 568153;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 567304;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 566955;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 566645;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 566065;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 565108;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 564696;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 564658;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 563735;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 563707;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 562833;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 562150;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 561964;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 561684;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 561282;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 557876;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 555497;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 555278;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 552746;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 550005;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 550003;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 549502;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 549484;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 548125;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 545633;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 545061;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 544228;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 544225;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 543768;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 543433;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 542767;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 542313;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 542150;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 540485;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 540464;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 539674;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 536680;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 535974;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 535085;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 534661;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 533261;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 532001;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 527926;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 527759;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 525648;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 523221;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 522846;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 521672;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 519730;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 519717;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 519474;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 519468;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 517757;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 516136;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 515912;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 515894;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 514808;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 513997;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 512956;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 510255;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 509288;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 508975;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 507863;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 507516;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 507278;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 505258;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 504933;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 504534;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 504304;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 503607;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 501874;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 501745;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 500057;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 498989;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 498390;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 496549;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 494652;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 494608;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 492402;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 492121;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 490869;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 490672;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 490163;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 488904;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 488413;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 486074;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 485889;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 485738;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 485380;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 484611;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 481210;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 481192;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 481168;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 479051;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 474682;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 473833;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 470047;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 469984;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 468360;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 462573;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 462538;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 459827;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 459615;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 443310;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 439414;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 436696;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 433074;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 431826;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 428052;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 427616;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 425652;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 425581;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 423437;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 417269;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 413786;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 412467;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 412323;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 409870;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 409816;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 407257;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 403253;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 398701;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 396430;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 395863;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 395330;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 394846;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 385104;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 383138;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 382829;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 382032;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 381507;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 381487;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 373288;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 373239;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 372384;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 359185;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 357498;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 352802;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 348000;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 339394;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 339393;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 338853;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 338850;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 334199;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 329823;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 329092;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 327676;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 327548;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 326604;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 321916;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 321913;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 320972;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 319190;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 316222;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 312755;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 307383;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 304192;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 299938;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 297466;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 294600;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 276597;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 270404;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 268516;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 264032;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 263204;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 259912;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 249387;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 240282;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 238193;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 234283;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 230695;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 221816;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 206385;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 201924;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 182605;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 164889;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 70469;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 69304;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 67600;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 66016;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 61094;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 60881;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 58767;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 55454;
        UPDATE controle_tramitacao SET ind_fim_da_fila = TRUE  WHERE ide_controle_tramitacao = 54539;

--[34555] - Erro ao editar empreendimento
	INSERT INTO
		imovel_urbano (ide_imovel_urbano,num_inscricao_iptu) 
	VALUES (1150001,'04065040640000');
                
--[34552] - Alteração da Razão Social e CNPJ
UPDATE 
        requerimento
SET 
        nom_contato = 'UNIÃO AGROPECUÁRIA NOVO HORIZONTE S.A'
WHERE 
        ide_requerimento = 516850;
UPDATE 
        requerimento_pessoa 
SET
        ide_pessoa = 1323
WHERE 
        ide_requerimento = 516850 AND ide_pessoa = 691117;               
                
--[34542] - [CADASTRO] Imóvel Rural sumiu do sistema
INSERT
        INTO
        public.pessoa_imovel
   (ide_pessoa,
        ide_imovel,
        dtc_criacao,
        ind_excluido,
        ide_tipo_vinculo_imovel,
        dtc_exclusao,
        ide_tipo_vinculo_pct,
        dsc_tipo_vinculo_pct_outros)
VALUES(982752,
641577,
now(),
FALSE,
1,
NULL,
NULL,
NULL);
UPDATE
        imovel_rural
SET
        ide_requerente_cadastro = 982752
WHERE
        ide_imovel_rural = 641577;             

--[34530] - [PROCESSO] Processo sumiu do sistema
UPDATE
        controle_tramitacao
SET
        ind_fim_da_fila = TRUE
WHERE
        ide_controle_tramitacao = 542313;                

--[34512] - REQUERIMENTO [erro ao consultar requerimento]

 UPDATE
        requerimento
SET
        num_requerimento = (
        SELECT
                CAST('2022.001.2' AS VARCHAR) || 
                CAST((CAST(split_part(split_part((
                        SELECT r.num_requerimento FROM requerimento r WHERE r.num_requerimento IS NOT NULL ORDER BY r.ide_requerimento DESC LIMIT 1), '.', 3), '/', 1) AS INTEGER) + 4 ) AS VARCHAR) || 
                CAST('/INEMA/INEXIG' AS VARCHAR) AS NUM_REQUERIMENTO),
        dtc_finalizacao = (
        SELECT
                dtc_movimentacao
        FROM
                tramitacao_requerimento
        WHERE
                ide_requerimento = 1166096
        ORDER BY
                dtc_movimentacao DESC
        LIMIT 1)
WHERE
        ide_requerimento = 1166096;
        
UPDATE
        comunicacao_requerimento
SET
        des_mensagem = 'Prezado(a),

O boleto de pagamento do Requerimento de nº 2022.001.2255024/INEMA/INEXIG já está disponível.
Favor acessar o SEIA para efetuar o download do boleto.

Atte.,
Central de Atendimento/INEMA.'
        ,assunto = '[SEIA] - Boleto/DAE do Requerimento de nº 2022.001.2255024/INEMA/INEXIG'
WHERE
        ide_comunicacao_requerimento = 398595;
UPDATE
        comunicacao_requerimento
SET
        des_mensagem = 'Prezado(a),


O Requerimento de n° 2022.001.2255024/INEMA/INEXIG foi enquadrado com sucesso.
Para regularização do empreendimento, os seguintes atos são necessários:
Declaração de Inexigibilidade.



 Atenciosamente,
 Central de Atendimento/INEMA'
        ,assunto = 'SEIA - Comunicado - Requerimento número 2022.001.2255024/INEMA/INEXIG'
WHERE
        ide_comunicacao_requerimento = 398541;               
  
--[34483] - [REQUERIMENTO] Pagamento REPFLOR
        UPDATE
                tramitacao_requerimento
        SET
                ide_status_requerimento = 8
        WHERE
                ide_tramitacao_requerimento = 1265495;
      
--[34461] - [REQUERIMENTO] Requerimento sem número

DELETE
FROM
	certificado
WHERE
	ide_certificado = 1260773;

DELETE
FROM
	requerimento_imovel ri
WHERE
	ide_requerimento = 1156876;

DELETE
FROM
	requerimento
WHERE
	ide_requerimento = 1156876;

INSERT
	INTO
	public.certificado
(ide_certificado,
	dtc_emissao_certificado,
	ide_ato_ambiental,
	ide_orgao,
	ide_requerimento,
	num_certificado,
	num_token,
	ide_tipo_certificado)
VALUES(1260773,
'2022-07-14 15:37:19.751',
NULL,
1,
1156875,
'2022.001.469460/TC',
'15D901D334E5A8453AA893A6CCE50FD2',
3);

INSERT
	INTO
	public.requerimento_imovel
(ide_requerimento,
	ide_imovel,
	ide_tipo_imovel_requerimento,
	vlr_area,
	dsc_ponto_referencia,
	dtc_criacao,
	ind_excluido,
	dtc_exclusao,
	ide_localizacao_geografica,
	ide_requerimento_imovel)
VALUES(1156875,
1196850,
2,
3.34,
NULL,
'2022-07-14 15:37:13.101',
FALSE,
NULL,
NULL,
1011381);

UPDATE
	requerimento
SET
	num_requerimento = '2022.001.243631/INEMA/REQ'
WHERE
	ide_requerimento = 1156875;  
	
--[34552] - Alteração da Razão Social e CNPJ
UPDATE 
        requerimento
SET 
        nom_contato = 'UNIÃO AGROPECUÁRIA NOVO HORIZONTE S.A'
WHERE 
        ide_requerimento = 516850;
UPDATE 
        requerimento_pessoa 
SET
        ide_pessoa = 1323
WHERE 
        ide_requerimento = 516850 AND ide_pessoa = 691117;               
                
--[34542] - [CADASTRO] Imóvel Rural sumiu do sistema
INSERT
        INTO
        public.pessoa_imovel
   (ide_pessoa,
        ide_imovel,
        dtc_criacao,
        ind_excluido,
        ide_tipo_vinculo_imovel,
        dtc_exclusao,
        ide_tipo_vinculo_pct,
        dsc_tipo_vinculo_pct_outros)
VALUES(982752,
641577,
now(),
FALSE,
1,
NULL,
NULL,
NULL);
UPDATE
        imovel_rural
SET
        ide_requerente_cadastro = 982752
WHERE
        ide_imovel_rural = 641577;             
      	
COMMIT;
