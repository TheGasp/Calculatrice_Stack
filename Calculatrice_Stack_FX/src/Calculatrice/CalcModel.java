package Calculatrice;

import java.util.Stack;

public class CalcModel implements ICalcModel {
    private Stack<Double> stack = new Stack<>();
    private Runnable onStackChanged;

    @Override
    public void push(double value) {
        stack.push(value);
        notifyStackChanged();
    }

    @Override
    public double pop() {
        if (stack.isEmpty()) {
            throw new IllegalStateException("La pile est vide");
        }
        return stack.pop();
    }

    @Override
    public void add() {
        checkStackSize(2);
        double b = pop();
        double a = pop();
        push(a + b);
    }

    @Override
    public void subtract() {
        checkStackSize(2);
        double b = pop();
        double a = pop();
        push(a - b);
    }

    @Override
    public void multiply() {
        checkStackSize(2);
        double b = pop();
        double a = pop();
        push(a * b);
    }

    @Override
    public void divide() {
        checkStackSize(2);
        double b = pop();
        double a = pop();
        if (b == 0) {
            push(a);
            push(b);
            throw new ArithmeticException("Division par zéro");
        }
        push(a / b);
    }

    @Override
    public void clear() {
        stack.clear();
        notifyStackChanged();
    }

    @Override
    public void drop() {
        if (stack.isEmpty()) {
            throw new IllegalStateException("La pile est vide, impossible de supprimer.");
        }
        pop();
        notifyStackChanged();
    }

    @Override
    public void swap() {
        checkStackSize(2);
        double top1 = pop();
        double top2 = pop();
        push(top1);
        push(top2);
        notifyStackChanged();
    }

    @Override
    public Stack<Double> getStack() {
        return stack;
    }

    @Override
    public void setOnStackChanged(Runnable callback) {
        this.onStackChanged = callback;
    }

    private void notifyStackChanged() {
        if (onStackChanged != null) {
            onStackChanged.run();
        }
    }

    private void checkStackSize(int size) {
        if (stack.size() < size) {
            throw new IllegalStateException("Pas assez de nombres sur la pile");
        }
    }
}
