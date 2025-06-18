package br.gov.ba.seia.enumerator;

public enum CrudOperationEnum {
	
	BROWSE(1), 
	INSERT(2), 
	UPDATE(3), 
	DELETE(4);

	private int operation;

	private CrudOperationEnum(int i) {
		operation = i;
	}

	public int getOperation() {
		return operation;
	}
}