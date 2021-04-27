package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.EmptyStackException;
import hr.fer.oprpp1.custom.collections.ObjectStack;

public class StackDemo {

    public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("Received arguments are invalid. Should receive only one argument!");
            return;
        }

        String[] inputElements = args[0].trim().split(" ");

        ObjectStack stack = new ObjectStack();
        for(String s : inputElements) {
            try {
                stack.push(Integer.parseInt(s));
            } catch (NumberFormatException ex) { // if exception occurs then we encountered and operator
                int a, b;
                try {
                    b = (int) stack.pop();
                    a = (int) stack.pop();
                } catch (EmptyStackException stackErr) {
                    System.out.println("Expression is invalid!");
                    return;
                }
                int result;
                if(s.equals("+")) result = a+b;
                else if(s.equals("-")) result = a-b;
                else if(s.equals("/")) {
                    if (b == 0) {
                        System.out.println("Can't divide by 0!");
                        return;
                    }
                    result = a/b;
                }
                else if(s.equals("*")) result = a*b;
                else if(s.equals("%")) {
                    if (b == 0) {
                        System.out.println("Can't divide by 0!");
                        return;
                    }
                    result = a % b;
                }
                else {
                    System.out.println("Invalid operator: "+ s);
                    return;
                }
                stack.push(result);
            }
        }
        if(stack.size() != 1){
            System.out.println("Invalid expression!");
            return;
        }
        System.out.println(stack.pop());
    }
}
