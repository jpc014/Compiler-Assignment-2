public class Symbol{
	private String lexeme;
	private String type;
	private int value;

	public Symbol(String lexeme, String type, int value) {
		super();
		this.lexeme = lexeme;
		this.type = type;
		this.value = value;
	}

	public String getLexeme() {
		return lexeme;
	}

	public void setLexeme(String lexeme) {
		this.lexeme = lexeme;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}