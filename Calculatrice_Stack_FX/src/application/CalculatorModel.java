package application;

import java.util.Stack;

public class CalculatorModel {
    private Stack<Double> stack;

    public CalculatorModel() {
        stack = new Stack<>(); //On crée une pile
    }

    // Empiler une valeur
    public void push(double value) {
        stack.push(value);
    }

    // Récupérer la valeur du sommet de la pile
    public double pop() {
        if (stack.isEmpty()) {
            throw new IllegalStateException("La pile est vide");
        }
        return stack.pop();
    }

    // Addition
    public void add() {
        checkStackSize(2);
        double b = pop();
        double a = pop();
        push(a + b);
    }

    // Soustraction
    public void subtract() {
        checkStackSize(2);
        double b = pop();
        double a = pop();
        push(a - b);
    }

    // Multiplication
    public void multiply() {
        checkStackSize(2);
        double b = pop();
        double a = pop();
        push(a * b);
    }

    // Division
    public void divide() {
        checkStackSize(2);
        double b = pop();
        double a = pop();
        if (b == 0) {
        	push(a);
        	push(b); //SI on peut pas div on remet les nb
        	
            throw new ArithmeticException("Division par zéro"); //Le throw stop la suite
        }
        push(a / b);
    }

    // Vider la pile
    public void clear() {
        stack.clear();
    }

    // Drop
    public void drop() {
        if (stack.isEmpty()) {
            throw new IllegalStateException("La pile est vide, impossible de supprimer.");
        }
        stack.pop();
    }

    // Swap
    public void swap() {
        checkStackSize(2);
        double top1 = pop();
        double top2 = pop();
        push(top1);
        push(top2);
    }

    // Vérifier la taille de la pile
    private void checkStackSize(int size) {
        if (stack.size() < size) {
            throw new IllegalStateException("Pas assez de nombres sur la pile");
        }
    }

    // Getteur de la pile
    public Stack<Double> getStack() {
        return stack;
    }
}
