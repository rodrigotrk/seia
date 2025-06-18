--AUTORIZACAO
UPDATE tipo_finalidade_uso_agua SET nom_tipo_finalidade_uso_agua = 'Dragagem para limpeza e desassoreamento de curso d água' where ide_tipo_finalidade_uso_agua = 18;
UPDATE tipo_finalidade_uso_agua SET nom_tipo_finalidade_uso_agua = 'Aquicultura', ind_ativo = false where ide_tipo_finalidade_uso_agua = 1;
UPDATE tipo_finalidade_uso_agua SET nom_tipo_finalidade_uso_agua = 'Aquicultura em viveiros escavados, raceway ou similares' where ide_tipo_finalidade_uso_agua = 25;
UPDATE tipo_finalidade_uso_agua SET nom_tipo_finalidade_uso_agua = 'Infraestrutura (sistema viário e energia)' where ide_tipo_finalidade_uso_agua = 12;

-- INSERT INTO ato_ambiental_tipologia_finalidade VALUES ((select max(ide_ato_ambiental_tipologia_finalidade) from ato_ambiental_tipologia_finalidade) + 1, 1, 1, true);
-- INSERT INTO ato_ambiental_tipologia_finalidade VALUES ((select max(ide_ato_ambiental_tipologia_finalidade) from ato_ambiental_tipologia_finalidade) + 1, 2, 1, true);
-- INSERT INTO ato_ambiental_tipologia_finalidade VALUES ((select max(ide_ato_ambiental_tipologia_finalidade) from ato_ambiental_tipologia_finalidade) + 1, 13, 1, true);
-- INSERT INTO ato_ambiental_tipologia_finalidade VALUES ((select max(ide_ato_ambiental_tipologia_finalidade) from ato_ambiental_tipologia_finalidade) + 1, 14, 1, true);

select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Intervenção', 'Aquicultura em tanques-rede', null, null, null, 2500);
select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Intervenção', 'Construção de ponte', null, null, null, 2500);
select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Intervenção', 'Canalização e retificação', null, null, null, 2500);
select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Intervenção', 'Construção de barragem', null, null, null, 2500);
select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Intervenção', 'Construção de travessia', null, null, null, 2500);
select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Intervenção', 'Construção de píer, dique, cais', null, null, null, 2500);
select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Intervenção', 'Drenagem de águas pluviais com deságue em manancial', null, null, null, 2500);
select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Intervenção', 'Dragagem para limpeza e desassoreamento de curso d água', null, null, null, 2500);
select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Intervenção', 'Extração/explotação mineral em recurso hídrico', null, null, null, 2500);
select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Intervenção', 'Travessia de duto', null, null, null, 2500);

select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Lançamento de efluentes', 'Criação de animais', null, null, null, 2500);
select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Lançamento de efluentes', 'Aquicultura em viveiros escavados, raceway ou similares', null, null, null, 2500);
select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Lançamento de efluentes', 'Esgoto doméstico', null, null, null, 2500);
select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Lançamento de efluentes', 'Efluente industrial', null, null, null, 2500);
select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Lançamento de efluentes', 'Mineração', null, null, null, 2500);

select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Captação subterrânea', 'Aquicultura em viveiros escavados, raceway ou similares', null, null, null, 2500);
select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Captação subterrânea', 'Abastecimento humano', null, null, null, 1000);
select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Captação subterrânea', 'Mineração', null, null, null, 5000);
select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Captação subterrânea', 'Lazer e turismo', null, null, null, 2500);
select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Captação subterrânea', 'Infraestrutura (sistema viário e energia)', null, null, null, 2500);
select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Captação subterrânea', 'Irrigação', NULL, null, null, 500);
select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Captação subterrânea', 'Pulverização agrícola', NULL, null, null, 500);
select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Captação subterrânea', 'Abastecimento industrial', 1, null, null, 10000);
select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Captação subterrânea', 'Abastecimento industrial', 2, null, null, 20000);
select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Captação subterrânea', 'Dessedentação animal', null, null, 100, 500);
select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Captação subterrânea', 'Dessedentação animal', null, 100.01, 500, 2500);
select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Captação subterrânea', 'Dessedentação animal', null, 500.01, 1000, 5000);
select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Captação subterrânea', 'Dessedentação animal', null, 1000.01, null, 10000);

select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Captação superficial', 'Mineração', null, null, null, 5000);
select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Captação superficial', 'Aquicultura em viveiros escavados, raceway ou similares', null, null, null, 2500);
select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Captação superficial', 'Abastecimento humano', null, null, null, 1000);
select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Captação superficial', 'Lazer e turismo', null, null, null, 2500);
select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Captação superficial', 'Infraestrutura (sistema viário e energia)', null, null, null, 2500);
select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Captação superficial', 'Irrigação', NULL, null, null, 500);
select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Captação superficial', 'Pulverização agrícola', NULL, null, null, 500);
select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Captação superficial', 'Abastecimento industrial', 1, null, null, 10000);
select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Captação superficial', 'Abastecimento industrial', 2, null, null, 20000);
select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Captação superficial', 'Dessedentação animal', null, null, 100, 500);
select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Captação superficial', 'Dessedentação animal', null, 100.01, 500, 2500);
select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Captação superficial', 'Dessedentação animal', null, 500.01, 1000, 5000);
select atualizar_taxas_parametro_calculo('Outorga de uso de recurso hídrico', 'Captação superficial', 'Dessedentação animal', null, 1000.01, null, 10000);

select atualizar_taxas_parametro_calculo('Outorga preventiva de uso de recurso hídrico', 'Intervenção', 'Construção de ponte', null, null, null, 2500);
select atualizar_taxas_parametro_calculo('Outorga preventiva de uso de recurso hídrico', 'Intervenção', 'Travessia de duto', null, null, null, 2500);
select atualizar_taxas_parametro_calculo('Outorga preventiva de uso de recurso hídrico', 'Intervenção', 'Construção de píer, dique, cais', null, null, null, 2500);
select atualizar_taxas_parametro_calculo('Outorga preventiva de uso de recurso hídrico', 'Intervenção', 'Aquicultura em tanques-rede', null, null, null, 2500);
select atualizar_taxas_parametro_calculo('Outorga preventiva de uso de recurso hídrico', 'Intervenção', 'Extração/explotação mineral em recurso hídrico', null, null, null, 2500);
select atualizar_taxas_parametro_calculo('Outorga preventiva de uso de recurso hídrico', 'Intervenção', 'Dragagem para limpeza e desassoreamento de curso d água', null, null, null, 2500);
select atualizar_taxas_parametro_calculo('Outorga preventiva de uso de recurso hídrico', 'Intervenção', 'Drenagem de águas pluviais com deságue em manancial', null, null, null, 2500);
select atualizar_taxas_parametro_calculo('Outorga preventiva de uso de recurso hídrico', 'Intervenção', 'Construção de barragem', null, null, null, 2500);
select atualizar_taxas_parametro_calculo('Outorga preventiva de uso de recurso hídrico', 'Intervenção', 'Canalização e retificação', null, null, null, 2500);

select atualizar_taxas_parametro_calculo('Outorga preventiva de uso de recurso hídrico', 'Lançamento de efluentes', 'Criação de animais', null, null, null, 2500);
select atualizar_taxas_parametro_calculo('Outorga preventiva de uso de recurso hídrico', 'Lançamento de efluentes', 'Aquicultura em viveiros escavados, raceway ou similares', null, null, null, 2500);
select atualizar_taxas_parametro_calculo('Outorga preventiva de uso de recurso hídrico', 'Lançamento de efluentes', 'Esgoto doméstico', null, null, null, 2500);
select atualizar_taxas_parametro_calculo('Outorga preventiva de uso de recurso hídrico', 'Lançamento de efluentes', 'Efluente industrial', null, null, null, 2500);
select atualizar_taxas_parametro_calculo('Outorga preventiva de uso de recurso hídrico', 'Lançamento de efluentes', 'Mineração', null, null, null, 2500);

select atualizar_taxas_parametro_calculo('Outorga preventiva de uso de recurso hídrico', 'Captação subterrânea', 'Mineração', null, null, null, 5000);
select atualizar_taxas_parametro_calculo('Outorga preventiva de uso de recurso hídrico', 'Captação subterrânea', 'Aquicultura em viveiros escavados, raceway ou similares', null, null, null, 2500);
select atualizar_taxas_parametro_calculo('Outorga preventiva de uso de recurso hídrico', 'Captação subterrânea', 'Abastecimento humano', null, null, null, 1000);
select atualizar_taxas_parametro_calculo('Outorga preventiva de uso de recurso hídrico', 'Captação subterrânea', 'Lazer e turismo', null, null, null, 2500);
select atualizar_taxas_parametro_calculo('Outorga preventiva de uso de recurso hídrico', 'Captação subterrânea', 'Infraestrutura (sistema viário e energia)', null, null, null, 2500);
select atualizar_taxas_parametro_calculo('Outorga preventiva de uso de recurso hídrico', 'Captação subterrânea', 'Irrigação', NULL, null, null, 500);
select atualizar_taxas_parametro_calculo('Outorga preventiva de uso de recurso hídrico', 'Captação subterrânea', 'Pulverização agrícola', NULL, null, null, 500);
select atualizar_taxas_parametro_calculo('Outorga preventiva de uso de recurso hídrico', 'Captação subterrânea', 'Abastecimento industrial', 1, null, null, 10000);
select atualizar_taxas_parametro_calculo('Outorga preventiva de uso de recurso hídrico', 'Captação subterrânea', 'Abastecimento industrial', 2, null, null, 20000);
select atualizar_taxas_parametro_calculo('Outorga preventiva de uso de recurso hídrico', 'Captação subterrânea', 'Dessedentação animal', null, null, 100, 500);
select atualizar_taxas_parametro_calculo('Outorga preventiva de uso de recurso hídrico', 'Captação subterrânea', 'Dessedentação animal', null, 100.01, 500, 2500);
select atualizar_taxas_parametro_calculo('Outorga preventiva de uso de recurso hídrico', 'Captação subterrânea', 'Dessedentação animal', null, 500.01, 1000, 5000);
select atualizar_taxas_parametro_calculo('Outorga preventiva de uso de recurso hídrico', 'Captação subterrânea', 'Dessedentação animal', null, 1000.01, null, 10000);

select atualizar_taxas_parametro_calculo('Outorga preventiva de uso de recurso hídrico', 'Captação superficial', 'Mineração', null, null, null, 5000);
select atualizar_taxas_parametro_calculo('Outorga preventiva de uso de recurso hídrico', 'Captação superficial', 'Aquicultura em viveiros escavados, raceway ou similares', null, null, null, 2500);
select atualizar_taxas_parametro_calculo('Outorga preventiva de uso de recurso hídrico', 'Captação superficial', 'Abastecimento humano', null, null, null, 1000);
select atualizar_taxas_parametro_calculo('Outorga preventiva de uso de recurso hídrico', 'Captação superficial', 'Lazer e turismo', null, null, null, 2500);
select atualizar_taxas_parametro_calculo('Outorga preventiva de uso de recurso hídrico', 'Captação superficial', 'Infraestrutura (sistema viário e energia)', null, null, null, 2500);
select atualizar_taxas_parametro_calculo('Outorga preventiva de uso de recurso hídrico', 'Captação superficial', 'Irrigação', NULL, null, null, 500);
select atualizar_taxas_parametro_calculo('Outorga preventiva de uso de recurso hídrico', 'Captação superficial', 'Pulverização agrícola', NULL, null, null, 500);
select atualizar_taxas_parametro_calculo('Outorga preventiva de uso de recurso hídrico', 'Captação superficial', 'Abastecimento industrial', 1, null, null, 10000);
select atualizar_taxas_parametro_calculo('Outorga preventiva de uso de recurso hídrico', 'Captação superficial', 'Abastecimento industrial', 2, null, null, 20000);
select atualizar_taxas_parametro_calculo('Outorga preventiva de uso de recurso hídrico', 'Captação superficial', 'Dessedentação animal', null, null, 100, 500);
select atualizar_taxas_parametro_calculo('Outorga preventiva de uso de recurso hídrico', 'Captação superficial', 'Dessedentação animal', null, 100.01, 500, 2500);
select atualizar_taxas_parametro_calculo('Outorga preventiva de uso de recurso hídrico', 'Captação superficial', 'Dessedentação animal', null, 500.01, 1000, 5000);
select atualizar_taxas_parametro_calculo('Outorga preventiva de uso de recurso hídrico', 'Captação superficial', 'Dessedentação animal', null, 1000.01, null, 10000);

UPDATE parametro_calculo SET fator_multiplicador = 2.50 
where ind_ativo = true and ide_ato_ambiental in (97,108) and ide_tipologia in (302,303) and ide_tipo_finalidade_uso_agua in (5,9);