package Calculatrice;

public class CalcController implements ICalcController {
    private final ICalcModel model;

    public CalcController(ICalcModel model) {
        this.model = model;
        this.model.setOnStackChanged(this::notifyStackChange); // Configure un rappel pour les changements de pile
    }

    @Override
    public void handlePush(double value) {
        model.push(value);
    }

    @Override
    public void handleAdd() {
        model.add();
    }

    @Override
    public void handleSubtract() {
        model.subtract();
    }

    @Override
    public void handleMultiply() {
        model.multiply();
    }

    @Override
    public void handleDivide() {
        model.divide();
    }

    @Override
    public void handleClear() {
        model.clear();
    }

    @Override
    public void handleDrop() {
        model.drop();
    }

    @Override
    public void handleSwap() {
        model.swap();
    }

    @Override
    public ICalcModel getModel() {
        return model;
    }

    // Méthode pour notifier des changements de pile
    private void notifyStackChange() {
        // Cette méthode peut rester vide car c'est `CalcView` qui observe les changements
    }
}
