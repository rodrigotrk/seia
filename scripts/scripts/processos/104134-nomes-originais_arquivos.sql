select dsc_caminho_arquivo from arquivo_processo where ide_processo = 10333 and dsc_caminho_arquivo is not null-- nome original 
--caminho /OPT/ARQUIVOS/PROCESSO/10333/

UPDATE arquivo_processo SET dsc_caminho_arquivo = remover_caracter_especial(dsc_caminho_arquivo) where ide_processo = 10333