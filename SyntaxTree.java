import java.util.*;

public class SyntaxTree {
	Node root;
	int place;
	final int length;
        final String original_expression;
	final String[] expression;
	static final String[] operators = {"+", "-", "/", "*"};

	public static boolean isOperator(String expression_) {
		for (String op: operators) {
			if (expression_.equals(op)) {
				return true;
			}
		}
		return false;
	}

        /* Main constructor for SyntaxTree */
	public SyntaxTree(String expression_, String notation) {
		this.root = null;
		this.place = 0;
                while (expression_.contains("  ")) expression_ = expression_.replace("  ", " ");
		this.original_expression = expression_;
		this.expression = original_expression.split("[ ]");
                this.length = expression.length;
		if (notation.equals("p")) {
                                if (expression.length == 1) { 
					this.root = new Node(expression[0]);
                                }
				else this.root = Prefix();
		}
	}


	/*
	 * Simple evaluation functions: String Evaluate(Node), boolean Evaluatable(Node)
         * double Evaluate(Node)
         *
	 */
        public String EvaluateString(Node start) {
            return "" + Evaluate(start);
        }

        public static boolean Evaluatable(Node start) {
            Node current = start;
            if (current != null) {
                if (!Evaluatable(start.left)) return false;
                if (current.expression.equals("x")) return false;
                if (!Evaluatable(start.right)) return false;
            } return true;
        }

	public static double Evaluate(Node start) {
		double a = 0;
		double b = 0;
                double result;
                if (start.left == null && start.right == null) return Double.parseDouble(start.expression);
		switch (start.expression) {
			case "+":
				if (isOperator(start.left.expression))
						a = Evaluate(start.left);
				else
					a = Double.parseDouble(start.left.expression);
				if (isOperator(start.right.expression))
					b = Evaluate(start.right);
				else
					b = Double.parseDouble(start.right.expression);
                                result = a + b;
                                start.expression = "" + result;
				return result;
			case "-":
				if (isOperator(start.left.expression))
                                        a = Evaluate(start.left);
				else {
                                        a = Double.parseDouble(start.left.expression);
                                        start.left = null;
                                }
                                if (isOperator(start.right.expression))
                                        b = Evaluate(start.right);
				else {
                                        b = Double.parseDouble(start.right.expression);
					start.right = null;
                                }
				result = a - b;
                                start.expression = "" + result;
                                return result;
			case "/":
                                if (isOperator(start.left.expression))
                                        a = Evaluate(start.left);
                                else {
                                        a = Double.parseDouble(start.left.expression);
              				start.left = null;
                                }
                                if (isOperator(start.right.expression))
                                        b = Evaluate(start.right);
				else {
                                        b = Double.parseDouble(start.right.expression);
					start.right = null;
                                }
				result = a/b;
                                start.expression = "" + result;
                                return result;
			case "*":
                                if (isOperator(start.left.expression))
                                        a = Evaluate(start.left);
				else {
                                        a = Double.parseDouble(start.left.expression);
             				start.left = null;
                                }
                                if (isOperator(start.right.expression))
                                        b = Evaluate(start.right);
				else {
                                        b = Double.parseDouble(start.right.expression);
                                        start.right = null;
                                }
				result = a*b;
				start.expression = "" + result;
                                return a*b;	
		}
		return 0;

	}
	
	public double Evaluate() {
                if (root.right == null && root.left == null) return Double.parseDouble(root.expression);
		return Evaluate(root);
	}

        public String S_Evaluate(Node start) {
                return "" + Evaluate(start);
        }


        /*
         * Methods for forming the tree from prefix, two argument notation
         */
	public Node Prefix() {
                Node prefix = new Node(expression[place++]);
		if (isOperator(expression[place])) {	
			prefix.left = Prefix();
		}
		else {
			prefix.left = new Node(expression[place++]);
		} if (isOperator(expression[place])) {
			prefix.right = Prefix();
		}
		else {
			prefix.right = new Node(expression[place++]);
		}
                return prefix;
        }


	// reads contents of tree using prefix notation
	public void readPrefix(Node focus) {
		Node focusNode = focus;
		if (focusNode != null) {
			System.out.print(focusNode.expression + " ");
			readPrefix(focusNode.left);
			readPrefix(focusNode.right);
		}
	}

        /*
         * Methods for reading the contents of the tree in each mathematical
         * notation
         */
	public void readPrefix() {
		readPrefix(root);
	}

	// reads contents of tree using postfix notation
	public void readPostfix(Node focus) {
		Node focusNode = focus;
		if (focusNode != null) {
			readPostfix(focusNode.left);
			readPostfix(focusNode.right);
			System.out.print(focusNode.expression + " ");
		}
	}
	
	public void readPostfix() {
		readPostfix(root);
	}

	// reads contents of tree using infix notation
	public void readInfix(Node focus) {
		Node focusNode = focus;
		if (focusNode != null) {
			readInfix(focusNode.left);
			System.out.print(focusNode.expression + " ");
			readInfix(focusNode.right);
		}
	}
  
        public static String PrefixString(Node focus) {
            if (focus != null) {
                if (isOperator(focus.expression))
                    return focus.expression + " " + PrefixString(focus.left) + PrefixString(focus.right);
                else return focus.expression + " ";
            } return "";
        } 

        public static String InfixString(Node focus) {
            return InfixStringHelp(focus);
        }

        private static String InfixStringHelp(Node focus) {
               Node focusNode = focus;
               if (focusNode != null) {
                     if (isOperator(focusNode.expression))
                         return InfixStringHelp(focusNode.left) + " " + focusNode.expression + " " + InfixStringHelp(focusNode.right);
                     else return focusNode.expression;
               } else return "";
        }
	
	public void readInfix() {
		readInfix(root);
	}

        /* Test Client for the SyntaxTree's ability to output numerical answers for expressions*/ 
	//must be run with a command line argument for mode
	public static void main(String[] args) {
		boolean maths = true;
		Scanner look = new Scanner(System.in);
		String mode = args[0];
		while (maths) {
			System.out.print("=> ");
			SyntaxTree test = new SyntaxTree(look.nextLine(), mode);
			System.out.print(":: ");
			System.out.println(test.Evaluate());
		}
	}


        /*
         * Node helper class to store expressions
         *
	private class Node {
		String expression;
		Node left;
		Node right;

		private Node(String expression_) {
			this.expression = expression_;
			this.left = null; this.right = null;
		}
	}*/
}
