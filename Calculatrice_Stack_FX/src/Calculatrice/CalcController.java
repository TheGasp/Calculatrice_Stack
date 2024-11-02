package Calculatrice;

public class CalcController {
    private CalcModel model;
    //Gestion des actions pour chaque handleur

    public CalcController(CalcModel model) {
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

    // Affichage (en mode console)
    private void displayStack() {
        System.out.println("Pile actuelle : " + model.getStack());
    }

    // Getteur du model
    public CalcModel getModel() {
        return model;
    }
}


