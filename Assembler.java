import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Assembler {
	//symbol table
	private static List<Symbol> SymbolTable = new ArrayList<Symbol>();
	
	private static List<String> memory = new ArrayList<String>();
	
	
	private static String instruction;// 32 bits long
	private static String ignored = "00000000000"; // bits 32-21
	private static String opcode; // bits 20-16
	private static String operand;// bits 15-0

	private static String file;
	
	private static BufferedReader br;
	private static int lineNumber=0;
	private static int fromCode=-1;
	
	public static StackMachine SM;
	
	private static StringTokenizer st;
	
	
	
	public static void main(String[] args) throws IOException {

		// get file name
		Scanner sc = new Scanner(System.in);
		
		System.out.print("Enter The name of the file: ");
		file = sc.nextLine();
		
		
		
		fillSymbolTable();

		fillDataTable();
		
		SM = new StackMachine(memory, SymbolTable);
		
		printSymbolTable();
		printDataTable();

	}

	private static void printDataTable() {
		System.out.println("Data table");

		for(int i =0; i<memory.size();i++){
			System.out.println(memory.get(i));
		}
	}

	private static void printSymbolTable() {
		System.out.println("Symbol table");
		System.out.print("Lexeme"+"\t\t");
		System.out.print("Type"+"\t\t");
		System.out.println("Value"+"\t\t");
		for(int i =0; i<SymbolTable.size();i++){
			System.out.print(SymbolTable.get(i).getLexeme() + "\t\t");
			System.out.print(SymbolTable.get(i).getType() + "\t\t");
			System.out.println(SymbolTable.get(i).getValue() + "\t\t");
		}
	}

	private static void fillSymbolTable() throws FileNotFoundException, IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;
		    String token;
		    int value;
		    while ((line = br.readLine()) != null) {
		        // process the line.
		    	st = new StringTokenizer(line);
		    	lineNumber++;
		    		token=st.nextToken();
		    		
		    		switch(token){
		    			case "Section":
		    				token=st.nextToken();
		    				if (token.equals(".data") ){
		    					break;
		    				}
		    				if (token.equals(".code")){
		    					break;
		    				}
		    				else{
		    					System.out.println("token: " + token);
		    					error();
		    				}
		    			case "Section.data":break;
		    			case "Section.code":break;
		    			case "LABEL":
		    				fromCode++;
		    				token=st.nextToken();
		    				//add to Symbol table
		    				SymbolTable.add(new Symbol(token,"Code",fromCode));
		    				break;
		    			case "PUSH":fromCode++; break;
		    			case "RVALUE":fromCode++; break;
		    			case "LVALUE":fromCode++; break;
		    			case "POP":fromCode++; break;
		    			case ":=":fromCode++; break;
		    			case "COPY":fromCode++; break;
		    			case "ADD":fromCode++; break;
		    			case "SUB":fromCode++; break;
		    			case "MPY":fromCode++; break;
		    			case "DIV":fromCode++; break;
		    			case "MOD":fromCode++; break;
		    			case "OR":fromCode++; break;
		    			case "AND":fromCode++; break;
		    			case "GOTO":fromCode++; break;
		    			case "GOFALSE":fromCode++; break;
		    			case "GOTRUE":fromCode++; break;
		    			case "PRINT":fromCode++; break;
		    			case "HALT":fromCode++; break;
		    				
		    			default:
		    				st.nextToken();
		    				value=Integer.parseInt(st.nextToken());
		    				//id
		    				SymbolTable.add(new Symbol(token,"Int",value));
		    		}
		    	}
		    br.close();
		    }
		
	}
	
	private static void fillDataTable() throws FileNotFoundException, IOException  {
		
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;
		    String token;
		    int temp;
		    
		    br.readLine();
		    
		    boolean bool=true;
		    
		    do{
		    	
		    	line = br.readLine();
		    	st = new StringTokenizer(line);
		    	token=st.nextToken(); 
		    	
		    	if(token.equals("Section")){
		    		bool=false;
		    	}
		    	
		    	if(token.equals("Section.code") ){
		    		bool =false;
		    	}
		    	
		    }while(bool);
		    
		    while ((line = br.readLine()) != null) {
		        // process the line.
		    	st = new StringTokenizer(line);
		    	lineNumber++;
		    	token=st.nextToken();
		    		
		    	switch(token){
		    		case "Section":
		   				token=st.nextToken();
		   				if (token.equals(".data") ){
		   					break;
		   				}
	    				if (token.equals(".code")){
	    					break;		    				}
		    			else{
		    				error();
		    			}
		    		case "Section.data":break;
		    		case "Section.code":break;
		    		
		    		case "HALT":
		    			fromCode++;
		    			setOpcode(0);
		    			setOperand("");
		    			break;
		    		case "PUSH":
		    			fromCode++;
		    			setOpcode(1);
		    			token=st.nextToken();
		    			temp= Integer.parseInt(token);
		    			setOperand(Integer.toBinaryString(temp));
		    			break;
		    		case "RVALUE":
		   				fromCode++;
		   				setOpcode(2);
		    			token = st.nextToken();
		    			temp = findIndex(token);
		    			
		    			temp = SymbolTable.get(temp).getValue();

		    			setOperand(Integer.toBinaryString(temp));
		   				break;
		   			case "LVALUE":
		   				fromCode++;
		   				setOpcode(3);
		   				token = st.nextToken();
		   				temp = findIndex(token);
		   				setOperand(Integer.toBinaryString(temp));
		   				break;
		   			case "POP":
		   				fromCode++;
		  				setOpcode(4);
		   				setOperand("");
		   				break;
		    		case ":=":
		    			fromCode++;
		    			setOpcode(5);
		    			setOperand("");
		   				break;
		   			case "COPY":
		   				fromCode++;
		   				setOpcode(6);
		   				setOperand("");
		   				break;
		   			case "ADD":
		   				fromCode++;
		    			setOpcode(7);
		   				setOperand("");
		   				break;
		   			case "SUB":
		   				fromCode++;
		   				setOpcode(8);
		   				setOperand("");
		   				break;
		   			case "MPY":
		   				fromCode++;
		   				setOpcode(9);
		   				setOperand("");
		   				break;
		   			case "DIV":
		   				fromCode++;
		  				setOpcode(10);
		   				setOperand("");
		   				break;
		   			case "MOD":
		   				fromCode++;
		   				setOpcode(11);
		   				setOperand("");
		   				break;
		   			case "OR":
		   				fromCode++;
		   				setOpcode(12);
		   				setOperand("");
		   				break;
		   			case "AND":
		   				fromCode++;
		   				setOpcode(13);
		  				setOperand("");
		   				break;
		   			case "LABEL":
		   				fromCode++;
		   				setOpcode(14);
		   				token=st.nextToken();
		   				temp = findIndex(token);
		   				setOperand(Integer.toBinaryString(temp));
		   				break;
		   			case "GOTO":
		   				fromCode++;
		   				setOpcode(15);
		   				token=st.nextToken();
		   				temp = findIndex(token);
		   				setOperand(Integer.toBinaryString( SymbolTable.get(temp).getValue() ));
		   				break;
		   			case "GOFALSE":
		   				fromCode++;
		   				setOpcode(16);
		   				token=st.nextToken();
		   				temp = findIndex(token);
		   				setOperand(Integer.toBinaryString( SymbolTable.get(temp).getValue() ));
		   				break;
		   			case "GOTRUE":
		   				fromCode++;
		   				setOpcode(17);
		   				token=st.nextToken();
		   				temp = findIndex(token);
		   				setOperand(Integer.toBinaryString( SymbolTable.get(temp).getValue() ));
		   				break;
		   			case "PRINT":
		   				fromCode++;
		   				setOpcode(18);
		   				setOperand(findIndex(st.nextToken()) + "");
		   				break;
		   			
		   			default:
		   				break;
		   		}
		   		setInstruction();
		   		memory.add(instruction);
		   	}
		    br.close();
		    }
	}

	public static void setOpcode(int op){		
		/*
		 * Sets the opcode for the instruction string
		 * the opcode is located in bits 20-16 (5 bits)
		 */
		
		String bin = Integer.toBinaryString(op);
		
		while(bin.length()<5){
			bin = "0"+bin;
		}	
		opcode=bin;
		
	}
	
	public static void setOperand(String op){
		//bits 15-0 (16 bits)
		while(op.length()<16){
			op = "0"+op;
		}
		operand=op;
	}
	
	public static void setInstruction(){
		instruction = ignored+opcode+operand;
	}
	
	public static int getLineNumber(){
		return lineNumber;
	}
	
	public static void error(){
		System.out.println("An error occurred on line " + getLineNumber());
		System.out.println("token: " + st);
		System.exit(0);
	}
	
	private static int findIndex(String sym){
		//finds and returns the index of the location of symbol "sym"
		int index;
		for(index = 0; index<SymbolTable.size(); index++){
			if(sym.equals(SymbolTable.get(index).getLexeme() ) ){
				break;
			}
		}	
		return index;
	}	
}