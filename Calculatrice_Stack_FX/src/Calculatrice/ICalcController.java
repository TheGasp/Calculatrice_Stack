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

    ICalcModel getModel();
}

