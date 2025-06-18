package br.gov.ba.seia.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public final class XLSUtil {

	private static HSSFWorkbook workBook;
	private static int indiceLinha;
	
	private static void carregarPlanilha(String caminhoNomeComFormato, int numSheet) throws IOException {
		InputStream inp = new FileInputStream(caminhoNomeComFormato);
		workBook = new HSSFWorkbook(new POIFSFileSystem(inp));
	}
	
	public static List<Object[]> getTodosValoresPorLinha(String caminhoNomeComFormato, int numSheet) throws IOException {
		carregarPlanilha(caminhoNomeComFormato, numSheet);
		Sheet sheet1 = carregarSheet(numSheet);
		
		List<Object[]> listValoresPorLinha = new ArrayList<Object[]>();
		Object[] linha = null;
		
		
		for (Row row : sheet1) {
	    	if(row.getRowNum() == 0) {
	    		continue;
	    		
	    	} else if(row.getLastCellNum() > 0) {
	    		indiceLinha = 0;
	    		
	    		linha = new Object[row.getLastCellNum()];
	    		
	    		for(int i = 0; i < row.getLastCellNum(); i++) {
	    			obterValorCelula(linha, row.getCell(i));
	    		}
	    		
	    		if(indiceLinha != 0 && !Util.isNullOuVazio(linha[0])) {
	    			listValoresPorLinha.add(linha);
	    		}
	    	}
	    }
		
		return listValoresPorLinha;
	}

	private static void obterValorCelula(Object[] linha, Cell cell) {
		if(Util.isNull(cell)) {
			linha[indiceLinha++] = null;
			System.out.println();
		} else {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				linha[indiceLinha++] = cell.getRichStringCellValue().getString();
			    System.out.println(cell.getRichStringCellValue().getString());
			    break;
			case Cell.CELL_TYPE_NUMERIC:
			    if (DateUtil.isCellDateFormatted(cell)) {
			    	linha[indiceLinha++] = new SimpleDateFormat("dd/MM/yyyy").format(cell.getDateCellValue());
			        System.out.println(cell.getDateCellValue());
			    } else {
			    	cell.setCellType(Cell.CELL_TYPE_STRING);
			    	System.out.println(cell.getStringCellValue());
			    	linha[indiceLinha++] = cell.getStringCellValue();
			    }
			    break;
			case Cell.CELL_TYPE_BOOLEAN:
				linha[indiceLinha++] = cell.getBooleanCellValue();
			    System.out.println(cell.getBooleanCellValue());
			    break;
			case Cell.CELL_TYPE_FORMULA:
				linha[indiceLinha++] = cell.getBooleanCellValue();
			    System.out.println(cell.getCellFormula());
			    break;
			case Cell.CELL_TYPE_BLANK:
				linha[indiceLinha++] = null;
				System.out.println();
				break;
			default:			
			    System.out.println();
	         }
		}
	}
	
	public static void inserirDadosPlanilhaExistente(String caminhoArquivo, List<Object[]> dados, int linhaInicial, int colunaInicial) throws IOException, FileNotFoundException {
	    FileInputStream file = new FileInputStream(new File(caminhoArquivo));
	 
	    HSSFWorkbook workbook = new HSSFWorkbook(file);	    
	    HSSFSheet sheet = workbook.getSheetAt(0);
	    int linha = linhaInicial;
	    for (Object[] dado : dados) {
	    	sheet.createRow(linha);
	    	for (int i = colunaInicial; i < dado.length + colunaInicial; i++) {
	    		sheet.getRow(linha).createCell(i);
	    		if(!Util.isNullOuVazio(dado[i])){	    			
		    		if(dado[i].getClass().equals(Double.class)){
			    		sheet.getRow(linha).getCell(i).setCellType(Cell.CELL_TYPE_NUMERIC);		    		
			    		sheet.getRow(linha).getCell(i).setCellValue((Double) dado[i]);
			    	} else if(dado[i].getClass().equals(Integer.class)){
			    		sheet.getRow(linha).getCell(i).setCellType(Cell.CELL_TYPE_NUMERIC);
			    		sheet.getRow(linha).getCell(i).setCellValue((Integer) dado[i]);
			    	} else {
			    		sheet.getRow(linha).getCell(i).setCellType(Cell.CELL_TYPE_STRING);
			    		sheet.getRow(linha).getCell(i).setCellValue((String) dado[i]);
			    	}
	    		} else {
	    			sheet.getRow(linha).getCell(i).setCellType(Cell.CELL_TYPE_BLANK);
		    		sheet.getRow(linha).getCell(i).setCellValue("");
	    		}
	    	}
	    	linha++;
		}
	    
	    file.close();
	     
	    FileOutputStream outFile =new FileOutputStream(new File(caminhoArquivo));
	    workbook.write(outFile);
	    outFile.close();
	}
	
	public static void atualizarColunaLocalizacaoObservacaoImportacaoCda(String caminhoArquivo, List<Object[]> dados) throws IOException, FileNotFoundException {
	    FileInputStream file = new FileInputStream(new File(caminhoArquivo));
	 
	    HSSFWorkbook workbook = new HSSFWorkbook(file);	    
	    HSSFSheet sheet = workbook.getSheetAt(0);
	    int linha = 1;
	    for (Object[] dado : dados) {	    	
	    		sheet.getRow(linha).createCell(6);
	    		sheet.getRow(linha).createCell(7);
	    		if(!Util.isNullOuVazio(dado[0])){    			
	    			sheet.getRow(linha).getCell(6).setCellType(Cell.CELL_TYPE_NUMERIC);
		    		sheet.getRow(linha).getCell(6).setCellValue(Integer.valueOf((String) dado[0]));
	    		} else {
	    			sheet.getRow(linha).getCell(6).setCellType(Cell.CELL_TYPE_BLANK);
		    		sheet.getRow(linha).getCell(6).setCellValue("");
	    		}
	    		if(!Util.isNullOuVazio(dado[1])){    			
	    			sheet.getRow(linha).getCell(7).setCellType(Cell.CELL_TYPE_STRING);
		    		sheet.getRow(linha).getCell(7).setCellValue((String) dado[1]);
	    		} else {
	    			sheet.getRow(linha).getCell(7).setCellType(Cell.CELL_TYPE_BLANK);
		    		sheet.getRow(linha).getCell(7).setCellValue("");
	    		}
	    	linha++;
		}
	    
	    file.close();
	     
	    FileOutputStream outFile =new FileOutputStream(new File(caminhoArquivo));
	    workbook.write(outFile);
	    outFile.close();
	}

	private static HSSFSheet carregarSheet(int numSheet) {
		return workBook.getSheetAt(numSheet);
	}

}
