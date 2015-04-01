import java.util.Scanner;

public class AlgebraTree {
	SyntaxTree left;
        SyntaxTree right;
        String expression;
        
        public AlgebraTree(String expression) {
            while (expression.contains("  ")) expression = expression.replace("  ", " ");
            String[] two = expression.split(" = ");
            this.left = new SyntaxTree(two[0], "p");
            this.right = new SyntaxTree(two[1], "p");
        }

        /* Algebraic evaluation methods... */
        public String Evaluate() {
           boolean goingLeft = true;
           int i = 0;
           while (i++ < 100) {
               //changes inside
               Evaluate(left.root);
               Evaluate(right.root); 
               //changes on top -- EVERYTHING IS UNSOLVABLE AT THIS POINT
               if (left.root.expression.equals("/")) {
                   multiplyOutDivision(true);
               } else if (right.root.expression.equals("/")) {
                   multiplyOutDivision(false);
               }
               if (left.root.expression.equals("*")) {
                   if (!SyntaxTree.Evaluatable(left.root.right))
                       divideOutMultiplication(true, true);
                   else divideOutMultiplication(true, false); 
               } else if (right.root.expression.equals("*")) {
                   if (!SyntaxTree.Evaluatable(right.root.right))
                       divideOutMultiplication(false, true);
                   else divideOutMultiplication(false, false);
               } else if (left.root.expression.equals("+")) {
                   if (!SyntaxTree.Evaluatable(left.root.right)) {
                       subtractOutAddition(true, true);
                   } else subtractOutAddition(true, false);
               } else if (right.root.expression.equals("+")) {
                   if (!SyntaxTree.Evaluatable(right.root.right))
                       subtractOutAddition(false, true);
                   else subtractOutAddition(false, false);
               } else if (left.root.expression.equals("-")) {
                   addOutSubtraction(true);
               } else if (right.root.expression.equals("-")) {
                   addOutSubtraction(false);
               }
           } return SyntaxTree.PrefixString(left.root) + " = " + SyntaxTree.PrefixString(right.root);
        }

        public void Evaluate(Node start) {
            if (SyntaxTree.Evaluatable(start)) SyntaxTree.Evaluate(start);
            else {
                if (start.expression.equals("+") && !SyntaxTree.Evaluatable(start)) {
                    if (SyntaxTree.Evaluatable(start.left)) {
                         
                    } else if (SyntaxTree.Evaluatable(start.right)) {
                        
                    }
                    Evaluate(start.left);
                    Evaluate(start.right);
                }
                if (start.expression.equals("-") && !SyntaxTree.Evaluatable(start)) {
                    
                }
                if (start.expression.equals("*") && !SyntaxTree.Evaluatable(start)) {
                                        
                }
                if (start.expression.equals("/") && !SyntaxTree.Evaluatable(start)) {
                    
                }
                if (start.left != null)   
                    Evaluate(start.left);
                if (start.right != null)
                    Evaluate(start.right);
            }
        }

        /* Valid algebraic transformations */
        public String commutativeAddition(Node start) {
            if (start.expression.equals("+")) {
                if (SyntaxTree.Evaluatable(start.left)) {
                    start.left.expression = "" + SyntaxTree.Evaluate(start.left);
                    String toReturn = start.left.expression;
                    start.left.expression = "0";
                    return toReturn;
               } else if (SyntaxTree.Evaluatable(start.right)) {
                    SyntaxTree.Evaluate(start.left);
                    start.left.expression = "0";
                    String toReturn = start.right.expression;
                    return toReturn;
                }
            } return "0";
        }

        public void addOutSubtraction(boolean leftSideAlg) {
            if (leftSideAlg) {
                Node hold = left.root.right;
                left.root = left.root.left;
                Node newRoot = new Node("+");
                newRoot.right = hold;
                newRoot.left = right.root;
                right.root = newRoot;
            } else {
                Node hold = right.root.right;
                right.root = right.root.left;
                Node newRoot = new Node("+");
                newRoot.right = hold;
                newRoot.left = left.root;
                left.root = newRoot;
            }
        }

        public void subtractOutAddition(boolean leftSideAlg, boolean leftSideSyn) {
            if (leftSideAlg) {
                if (leftSideSyn) {
                    Node hold = left.root.left;
                    left.root = left.root.right;
                    Node newRoot = new Node("-");
                    newRoot.right = hold;
                    newRoot.left = right.root;
                    right.root = newRoot;        
                } else {
                    Node hold = left.root.right;
                    left.root = left.root.left;
                    Node newRoot = new Node("-");
                    newRoot.right = hold;
                    newRoot.left = right.root;
                    right.root = newRoot;
                }                
            } else {
                if (leftSideSyn) {
                    Node hold = right.root.left;
                    right.root = right.root.right;
                    Node newRoot = new Node("-");
                    newRoot.right = hold;
                    newRoot.left = left.root;
                    left.root = newRoot;
                } else {
                    Node hold = right.root.right;
                    right.root = right.root.left;
                    Node newRoot = new Node("-");
                    newRoot.left = left.root;
                    left.root = newRoot;
                }
            }
        }

        public void multiplyOutDivision(boolean leftSide) {
            if (leftSide) {
	        Node holdleft = left.root.right;
                left.root = left.root.left;
                Node newroot = new Node("*");
                newroot.left = right.root;
                newroot.right = holdleft;
                right.root = newroot;
            } else {
                Node holdright = right.root.right;
                right.root = right.root.left;
                Node newroot = new Node("*");
                newroot.left = left.root;
                newroot.right = holdright;
                left.root = newroot;
            }
        }
        
        public void divideOutMultiplication(boolean leftSideAlg, boolean leftSideSyn) {
            if (leftSideAlg) {
                Node hold;
                if (leftSideSyn) {
                    hold = left.root.left;
                    left.root = left.root.right;
                    Node newroot = new Node("/");
                    newroot.left = right.root;
                    newroot.right = hold;
                    right.root = newroot;
                }
                else { 
                    hold = left.root.right;
                    left.root = left.root.left;
                    Node newroot = new Node("/");
                    newroot.left = right.root;
                    newroot.right = hold;
                    right.root = newroot;
                }
            } else {
               Node hold;
                if (leftSideSyn) {
                    hold = right.root.left;
                    right.root = right.root.right;
                    Node newroot = new Node("/");
                    newroot.left = left.root;
                    newroot.right = hold;
                    left.root = newroot;
                }
                else {
                    hold = right.root.right;
                    right.root = right.root.left;
                    Node newroot = new Node("/");
                    newroot.left = left.root;
                    newroot.right = hold;
                    left.root = newroot;
                }

            }
        }

        public static void combineX(Node start) {
            if (!SyntaxTree.Evaluatable(start)) {
                if (start.expression == "+") {
                    
                } else if (start.expression == "-") {

                }
            }
        }

        /* Algebraic diagnostics */
        public boolean solved() {
            return ((left.root.expression == "x" && SyntaxTree.Evaluatable(right.root)) || (right.root.expression == "x" && (SyntaxTree.Evaluatable(left.root))));
        }   
   
        public static boolean equals(Node n1, Node n2) {
            String n1string = SyntaxTree.InfixString(n1).trim();
            String n2string = SyntaxTree.InfixString(n2).trim();
            return n1string.equals(n2string);
        }

        public static boolean division(Node start) {
            return (start.expression == "/");
        }

        // a * b
        public static boolean multiplication(Node start) {
            return (start.expression == "*");
        }

        // (a * b) * (c * d)
        private static boolean multiplicationOfTwoAdditions(Node start) {
            return (start.expression == "*" && start.left.expression == "+" && start.right.expression == "+");
        }

        // a * (c * b)
        private static boolean multlicationOfAddition(Node start) {
            return (start.expression == "*" && ((start.left.expression != "+" && start.right.expression == "+") || (start.right.expression == "+" && start.left.expression != "+")));
        }

        // (n * x) + (s * x)
        private static boolean additionOfLikeTerms(Node start) {
            boolean maybe = false;
            if (start.expression != "+") return false;
            if (start.left.expression != "*") return false;
            if (start.right.expression != "*") return false;
                return true;
        }


        public static void main(String[] args) {
                boolean maths = true;
                boolean boolean_expression = false;
                boolean solvable_expression = false;
                boolean solve_for_x = false;
                String output;
                System.out.printf("Welcome to the maybe solving some Algebra Program!\nPrefix notation will be used from here on out\n'+ 1 2' would evaluate to 3, '+ 1 2 = 3' would\nevaluate to true, and '* 2 x = / 2 3' should evaluate\nto 'x = 0.33333333'...\n");
                Scanner look = new Scanner(System.in);
                while (maths) {
                        System.out.print("> ");
                        // while (!look.hasNextLine()); // for debugging
                        String expression = look.nextLine();
                        if (expression.equals("")) {
                            continue;
                        }
                        if (expression.contains("=")) {
                                System.out.print("> ");
                        	AlgebraTree answer = new AlgebraTree(expression);
                                if (expression.contains("x")) { // case for solving for x
                                   System.out.println(answer.Evaluate());
                                } else {
                                   System.out.println(answer.left.Evaluate() == answer.right.Evaluate());
                                }
                        } else {
                                System.out.print("> ");
                        	SyntaxTree answer = new SyntaxTree(expression, "p");
                                System.out.println(answer.Evaluate());
                        }
                }
        }       
}
