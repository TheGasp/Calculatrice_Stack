package Calculatrice;

public interface ICalcView {
    void updateStackDisplay();
    void setController(ICalcController controller);
    void showErrorInHistory(String errorMessage);
}
