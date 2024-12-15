DROP FUNCTION IF EXISTS remover_caracter_especial (TEXT);
CREATE OR REPLACE FUNCTION remover_caracter_especial (texto TEXT) RETURNS TEXT AS
$$
DECLARE
  textoNovo TEXT;
BEGIN
  -- Tabela ASCII
  -- Caracteres não imprimiveis, 33 ao total, menos o NUL ficam 32 
                                                    --  hex d abr ctl descricao  
                                                    --  00  0 NUL ^@ Null - Nulo desativado, pois o nulo no postgres funciona em forma de cascata 
  textoNovo := translate(texto, chr(1),'');       --  01  1 SOH ^A Start of Header - Início do cabeçalho
  textoNovo := translate(textoNovo, chr(2),'');   --  02  2 STX ^B Start of Text - Início do texto
  textoNovo := translate(textoNovo, chr(3),'');   --  03  3 ETX ^C End of Text - Fim do texto
  textoNovo := translate(textoNovo, chr(4),'');   --  04  4 EOT ^D End of Tape - Fim de fita
  textoNovo := translate(textoNovo, chr(5),'');   --  05  5 ENQ ^E Enquire - Interroga identidade do terminal
  textoNovo := translate(textoNovo, chr(6),'');   --  06  6 ACK ^F Acknowledge - Reconhecimento
  textoNovo := translate(textoNovo, chr(7),'');   --  07  7 BEL ^G Bell - Campainha
  textoNovo := translate(textoNovo, chr(8),'');   --  08  8 BS  ^H  Back-space - Espaço atrás
  textoNovo := translate(textoNovo, chr(9),'');   --  09  9 HT  ^I  Horizontal Tabulation - Tabulação horizontal
  textoNovo := translate(textoNovo, chr(10),'');  --  0A 10 LF  ^J Line-Feed - Alimenta linha
  textoNovo := translate(textoNovo, chr(11),'');  --  0B 11 VT  ^K  Vertical Tabulation - Tabulação vertical
  textoNovo := translate(textoNovo, chr(12),'');  --  0C 12 FF  ^L Form-Feed - Alimenta formulário
  textoNovo := translate(textoNovo, chr(13),'');  --  0D 13 CR  ^M Carriage-Return - Retorno do carro (enter)
  textoNovo := translate(textoNovo, chr(14),'');  --  0E 14 SO  ^N Shift-Out - Saída do shift (passa a usar caracteres de baixo da tecla - minúsculas, etc.)
  textoNovo := translate(textoNovo, chr(15),'');  --  0F 15 SI  ^O Shift-In-Ent. no shift (passa a usar carac. de cima da tecla: maiúsculas, carac. especiais, etc.)
  textoNovo := translate(textoNovo, chr(16),'');  --  10 16 DLE ^P Data-Link Escape
  textoNovo := translate(textoNovo, chr(17),'');  --  11 17 DC1 ^Q Device-Control 1
  textoNovo := translate(textoNovo, chr(18),'');  --  12 18 DC2 ^R Device-Control 2
  textoNovo := translate(textoNovo, chr(19),'');  --  13 19 DC3 ^S Device-Control 3
  textoNovo := translate(textoNovo, chr(20),'');  --  14 20 DC4 ^T Device-Control 4
  textoNovo := translate(textoNovo, chr(21),'');  --  15 21 NAK ^U Neg-Acknowledge - Não-reconhecimento
  textoNovo := translate(textoNovo, chr(22),'');  --  16 22 SYN ^V Synchronous Idle
  textoNovo := translate(textoNovo, chr(23),'');  --  17 23 vETB^W  End-of-Transmission Block
  textoNovo := translate(textoNovo, chr(24),'');  --  18 24 CAN ^X Cancel
  textoNovo := translate(textoNovo, chr(25),'');  --  19 25 EM  ^Y  End-Of-Medium
  textoNovo := translate(textoNovo, chr(26),'');  --  1A 26 SUB ^Z Substitute
  textoNovo := translate(textoNovo, chr(27),'');  --  1B 27 ESC ^[ Escape
  textoNovo := translate(textoNovo, chr(28),'');  --  1C 28 FS  ^\  File Separator
  textoNovo := translate(textoNovo, chr(29),'');  --  1D 29 GS  ^] Group Separator
  textoNovo := translate(textoNovo, chr(30),'');  --  1E 30 RS  ^^  Record Separator
  textoNovo := translate(textoNovo, chr(31),'');  --  1F 31 US  ^_ Unit Separator
  textoNovo := translate(textoNovo, chr(32),'_');  -- 20 32 SPACE space 
  textoNovo := translate(textoNovo, chr(127),''); --  7F127 DEL ^?  Delete
  textoNovo := translate(textoNovo, 'æ','');
  textoNovo := translate(textoNovo, 'Æ','');
  textoNovo := translate(textoNovo, 'ø','');
  textoNovo := translate(textoNovo, '£','');
  textoNovo := translate(textoNovo, 'Ø','');
  textoNovo := translate(textoNovo, 'ƒ','');
  textoNovo := translate(textoNovo, '¿','');
  textoNovo := translate(textoNovo, '®','');
  textoNovo := translate(textoNovo, '½','');
  textoNovo := translate(textoNovo, '¼','');
  textoNovo := translate(textoNovo, 'ß','');
  textoNovo := translate(textoNovo, 'µ','');
  textoNovo := translate(textoNovo, 'þ','');
  textoNovo := translate(textoNovo, 'ý','');
  textoNovo := translate(textoNovo, 'Ý','');
  textoNovo := translate(textoNovo, '€','');
  textoNovo := translate(textoNovo, '§','');
  textoNovo := translate(textoNovo, '¬','');
  textoNovo := translate(textoNovo, '©','');
  textoNovo := translate(textoNovo, '¢','');
  textoNovo := translate(textoNovo, '¥','');
  textoNovo := translate(textoNovo, '¤','');
  textoNovo := translate(textoNovo, 'ð','');
  textoNovo := translate(textoNovo, 'Ð','');
  textoNovo := translate(textoNovo, 'Þ','');
  textoNovo := translate(textoNovo, 'Þ','');
  textoNovo := translate(textoNovo, '¶','');
  textoNovo := translate(textoNovo, '̂','');
  textoNovo := translate(textoNovo, '̃','');
  textoNovo := translate(textoNovo, '̀','');
  textoNovo := translate(textoNovo, '̧','');
  textoNovo := translate(textoNovo, '̃','');
  textoNovo := translate(textoNovo, '́','');
  textoNovo := translate(textoNovo, 'ŠŽšžŸÁÇÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕËÜÏÖÑÝåáçéíóúàèìòùâêîôûãõëüïöñýÿ','SZszYACEIOUAEIOUAEIOUAOEUIONYaaceiouaeiouaeiouaoeuionyy');
 
  RETURN textoNovo;
END;
$$
LANGUAGE 'plpgsql' STABLE CALLED ON NULL INPUT SECURITY INVOKER;