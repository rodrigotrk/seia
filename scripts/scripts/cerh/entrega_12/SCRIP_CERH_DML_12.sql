

﻿update cerh_pergunta_dados_gerais pergunta SET dsc_pergunta = 'O empreendimento faz outros usos além dos contemplados no(s) processos informado(s)?' 
	WHERE pergunta.ide_cerh_pergunta_dados_gerais = 3;

---


SELECT 
	setval('tipo_certificado_cefir_seq',(SELECT cast(max(substring(c1.num_certificado, 10, 6)) AS integer)
FROM certificado c1
	WHERE c1.num_certificado IS NOT null AND c1.num_certificado LIKE '%/CERH'
AND substring(c1.num_certificado, 0, 5) = to_char(now(),'YYYY')));

---


atualizar_num_certificado_inconsistentes();
