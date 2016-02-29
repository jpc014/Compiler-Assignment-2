import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class StackMachine {
	
	private static List<String> memory = new ArrayList<String>();
	private static int  memory_counter=0;
	
	private static List<Symbol> SymbolTable = new ArrayList<Symbol>();
	
	private static Stack<Integer> stack = new Stack<Integer>();
	
	private static int opcode;
	private static int operand;
	private static String Instruction;
	
	private static boolean done = true;
	
	public static void main(String[] args) {

	}
	
	public static void setMemory(List<String> l){
		memory=l;
	}
	
	public static void setSymbol(List<Symbol> l){
		SymbolTable = l;
	}
	
	public StackMachine(List<String> m,List<Symbol> s){
		memory = m;
		SymbolTable = s;
		
		start();
	}
	
	private static void start(){
		while(done){
			
			Instruction = memory.get(memory_counter);
			
			//System.out.println("Memory counmter: " + memory_counter);
			//System.out.println(Instruction);
			
			doOp(Instruction);
			memory_counter++;
		}
	}
	
	public static void doOp(String instruction){
		//get opcode
		// 00000000000 00000 0000000000000000
		//             |||||
		opcode=Integer.parseInt(instruction.substring(11, 16),2);
		
		
		//get operand
		// 00000000000 00000 0000000000000000
		//                   ||||||||||||||||
		operand= Integer.parseInt(instruction.substring(17, 32),2);
		
		//System.out.println("opcode" + opcode);
		//System.out.println("operand" + operand);

		switch (opcode){
			case(0):HALT();break;
			case(1):PUSH(operand);break;
			case(2):RVALUE(operand);break;
			case(3):LVALUE(operand);break;
			case(4):POP();break;
			case(5):EQUALS();break;
			case(6):COPY();break;
			case(7):ADD();break;
			case(8):SUB();break;
			case(9):MPY();break;
			case(10):DIV();break;
			case(11):MOD();break;
			case(12):OR();break;
			case(13):AND();break;
			case(14):LABEL(operand);break;
			case(15):GOTO(operand);break;
			case(16):GOFALSE(operand);break;
			case(17):GOTRUE(operand);break;
			case(18):PRINT(operand);break;
		}
	}

	//instructions
	public static void HALT(){
		System.out.println("HALT");
		done=false;
	}
	
	public static void PUSH(int op){
		System.out.println("PUSH " + op);
		
		stack.push(op);
	}
	
	public static void RVALUE(int op){
		System.out.println("RVALUE " + op);
		/*
		Symbol sym =  SymbolTable.get(op);
		int temp =sym.getValue();
		System.out.println("lexeme " + sym.getLexeme());
		System.out.println("temp " + temp);
		stack.push(temp);
		*/
		stack.push(op);
	}
	
	public static void LVALUE(int op){
		System.out.println("LVALUE " + op);
		stack.push(op);
	}
	
	public static void POP(){
		System.out.println("POP");
		stack.pop();
	}
	
	public static void EQUALS(){
		System.out.println("EQUALS");
		int rvalue=stack.pop();
		SymbolTable.get(stack.pop()).setValue(rvalue);;
	}
	
	public static void COPY(){
		System.out.println("COPY");
		int copy=stack.pop();
		stack.push(copy);
		stack.push(copy);
	}
	
	public static void ADD(){
		System.out.println("ADD");
		int a = stack.pop();
		int b = stack.pop();
		int result= a+b;
		
		System.out.println(b + " + " + a);
		
		stack.push(result);
	}
	
	public static void SUB(){
		System.out.println("SUB");
		int a = stack.pop();
		int b = stack.pop();
		int result = b-a;
		
		System.out.println(b + " - " + a);
		
		stack.push(result);
	}
	
	public static void MPY(){
		System.out.println("MPY");
		int a = stack.pop();
		int b = stack.pop();
		int result = a*b;
		
		System.out.println(b + " * " + a);
		
		stack.push(result);
	}
	
	public static void DIV(){
		System.out.println("DIV");
		int a = stack.pop();
		int b = stack.pop();
		int result = b/a;
		System.out.println(b + " / " + a);
		
		stack.push(result);
	}
	
	public static void MOD(){
		System.out.println("MOD");
		int a = stack.pop();
		int b = stack.pop();
		int result = a%b;
		
		
		System.out.println(b + " % " + a);
		
		stack.push(result);
	}
	
	public static void OR(){
		System.out.println("OR");
		int a = stack.pop();
		int b = stack.pop();
		int result= a|b;
		
		stack.push(result);
	}
	
	public static void AND(){
		System.out.println("AND");
		int a = stack.pop();
		int b = stack.pop();
		int result=a&b;
		
		stack.push(result);
	}
	
	public static void LABEL(int op){
		System.out.println("LABEL");
		//does nothing
	}
	
	public static void GOTO(int op){
		System.out.println("GOTO");
		memory_counter = op;
	}
	
	public static void GOFALSE(int op){
		System.out.println("GOFALSE");
		int temp = stack.pop();
		
		System.out.println("GOFALSE value:                            " + temp);
		
		if(temp == 0){
			memory_counter = op;
		}
	}
	
	public static void GOTRUE(int op){
		System.out.println("GOTRUE");
		int temp = stack.pop();
		
		if(temp != 0){
			memory_counter = op;
		}
	}
	
	public static void PRINT(int address){
		System.out.println("PRINT " + SymbolTable.get(address).getValue());
	}
}