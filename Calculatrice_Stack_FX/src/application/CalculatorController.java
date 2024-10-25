package application;

public class CalculatorController {
    private CalculatorModel model;
    //Ici on controle les actions que doivent effectuer chaque handleur

    public CalculatorController(CalculatorModel model) {
        this.model = model;
    }

    public void handlePush(double value) {
        model.push(value);
        displayStack();
    }

    public void handleAdd() {
        model.add();
        displayStack();
    }

    public void handleSubtract() {
        model.subtract();
        displayStack();
    }

    public void handleMultiply() {
        model.multiply();
        displayStack();
    }

    public void handleDivide() {
        model.divide();
        displayStack();
    }

    public void handleClear() {
        model.clear();
        displayStack();
    }

    public void handleDrop() {
        model.drop();
        displayStack();
    }

    public void handleSwap() {
        model.swap();
        displayStack();
    }

    // Affichage
    private void displayStack() {
        System.out.println("Pile actuelle : " + model.getStack());
    }

    // Utile pour le main
    public CalculatorModel getModel() {
        return model;
    }
}


