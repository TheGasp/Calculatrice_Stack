package Calculatrice;

public interface ICalcController {
    void handlePush(double value);
    void handleAdd();
    void handleSubtract();
    void handleMultiply();
    void handleDivide();
    void handleClear();
    void handleDrop();
    void handleSwap();

    // Méthode pour accéder au modèle (pour récupérer la pile dans la vue)
    ICalcModel getModel();
}

