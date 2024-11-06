package Calculatrice;

import java.util.Stack;

public interface ICalcModel {
    void push(double value);
    double pop();
    void add();
    void subtract();
    void multiply();
    void divide();
    void clear();
    void drop();
    void swap();
    Stack<Double> getStack();
    void setOnStackChanged(Runnable callback); // Observateur pour notifier des changements
}
