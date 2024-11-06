package Calculatrice;

public class CalcController implements ICalcController {
    private final ICalcModel model;

    public CalcController(ICalcModel model) { //Constructeur
        this.model = model;
        this.model.setOnPileChanged(this::notifPileChanged); // CallBack -> changement de la pile
    }

    //Gestion des actions pour chaque handleur
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
    public ICalcModel getModel() { // acces au modèle
        return model;
    }

    // Méthode qui gere le chgmt de pile
    private void notifPileChanged() {
        //Vide car c'est la view qui s'en occupe
    }
}
