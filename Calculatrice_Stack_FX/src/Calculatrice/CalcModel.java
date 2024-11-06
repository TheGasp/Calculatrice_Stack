package Calculatrice;

import java.util.Stack;

public class CalcModel implements ICalcModel {
    private Stack<Double> stack = new Stack<>(); //On crée une pile
    private Runnable onPileChanged;

    @Override
    public void push(double value) { // Empiler une valeur (encapsulation)
        stack.push(value);
        notifPileChanged(); //changement de la pile
    }

    @Override
    public double pop() { // récupérer la valeur du sommet de la pile
        if (stack.isEmpty()) {
            throw new IllegalStateException("La pile est vide");
        }
        return stack.pop();
    }
    
    @Override
    public void add() { // Addition
        checkStackSize(2);
        double b = pop();
        double a = pop();
        push(a + b); // S'occupe aussi de faire la notfication
    }

    @Override
    public void subtract() { // Soustraction
        checkStackSize(2);
        double b = pop();
        double a = pop();
        push(a - b);
    }

    @Override
    public void multiply() { //Multiplication
        checkStackSize(2);
        double b = pop();
        double a = pop();
        push(a * b);
    }

    @Override
    public void divide() { //Division
        checkStackSize(2);
        double b = pop();
        double a = pop();
        if (b == 0) {
            push(a);
            push(b); //SI on peut pas div on remet les nb
            throw new ArithmeticException("Division par zéro");
        }
        push(a / b);
    }

    @Override
    public void clear() { // Vider la pile
        stack.clear();
        notifPileChanged();
    }

    @Override
    public void drop() { //Drop
        if (stack.isEmpty()) {
            throw new IllegalStateException("La pile est vide, impossible de supprimer.");
        }
        pop();
        notifPileChanged();
    }

    @Override
    public void swap() { // Swap (des deux denières valeurs)
        checkStackSize(2);
        double top1 = pop();
        double top2 = pop();
        push(top1);
        push(top2);
        notifPileChanged();
    }

    @Override
    public Stack<Double> getStack() { //Getteur de la pile
        return stack;
    }

    @Override
    public void setOnPileChanged(Runnable callback) {
        this.onPileChanged = callback;
    }

    private void notifPileChanged() {
        if (onPileChanged != null) {
            onPileChanged.run(); // signal le chgmt de la pile
        }
    }
    
    // Vérifier la taille de la pile
    private void checkStackSize(int size) {
        if (stack.size() < size) {
            throw new IllegalStateException("Pas assez de nombres sur la pile");
        }
    }
}
