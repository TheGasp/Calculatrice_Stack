package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.List;

public class CalculatorView extends Application {

    private CalculatorController controller;
    private TextArea stackDisplay;  // Zone pour l'historique
    private TextField previewField; // Zone de prévisualisation
    private List<String> lastFiveStacks; // On stock les 5 dernières piles
    private StringBuilder currentInput = new StringBuilder(); 

    @Override
    public void start(Stage primaryStage) {
        CalculatorModel model = new CalculatorModel();
        controller = new CalculatorController(model);
        lastFiveStacks = new LinkedList<>();

        primaryStage.setTitle("Calculatrice de Sami et Gaspard");

        // Grille principale pour l'historique et la prévisualisation
        VBox mainVBox = new VBox(10);
        mainVBox.setPadding(new Insets(10));

        // Zone d'affichage de la pile et de l'historique (en haut)
        stackDisplay = new TextArea();
        stackDisplay.setEditable(false);  // Zone non modifiable
        stackDisplay.setPrefHeight(200);  
        mainVBox.getChildren().add(stackDisplay);

        // Zone de prévisu
        previewField = new TextField();
        previewField.setEditable(false);
        previewField.setPrefWidth(400);
        mainVBox.getChildren().add(previewField);

        // HBox pour boutons + nombres
        HBox buttonHBox = new HBox(20);
        buttonHBox.setAlignment(Pos.CENTER);

        // Grille pour les boutons d'opérations (Gauche)
        GridPane operationGrid = new GridPane();
        operationGrid.setHgap(10);
        operationGrid.setVgap(10);

        Button addButton = new Button("+");
        addButton.setPrefWidth(60);
        addButton.setOnAction(e -> handleOperation("+"));

        Button subtractButton = new Button("-");
        subtractButton.setPrefWidth(60);
        subtractButton.setOnAction(e -> handleOperation("-"));

        Button multiplyButton = new Button("*");
        multiplyButton.setPrefWidth(60);
        multiplyButton.setOnAction(e -> handleOperation("*"));

        Button divideButton = new Button("/");
        divideButton.setPrefWidth(60);
        divideButton.setOnAction(e -> handleOperation("/"));

        Button pushButton = new Button("Push");
        pushButton.setPrefWidth(60);
        pushButton.setOnAction(e -> handlePush());

        Button clearButton = new Button("Clear");
        clearButton.setPrefWidth(60);
        clearButton.setOnAction(e -> handleOperation("clear"));

        Button dropButton = new Button("Drop");
        dropButton.setPrefWidth(60);
        dropButton.setOnAction(e -> handleOperation("drop"));

        Button swapButton = new Button("Swap");
        swapButton.setPrefWidth(60);
        swapButton.setOnAction(e -> handleOperation("swap"));

        // On met les buttons dans la grille
        operationGrid.add(addButton, 0, 0);
        operationGrid.add(subtractButton, 1, 0);
        operationGrid.add(multiplyButton, 0, 1);
        operationGrid.add(divideButton, 1, 1);
        operationGrid.add(pushButton, 0, 2);
        operationGrid.add(clearButton, 1, 2);
        operationGrid.add(dropButton, 0, 3);
        operationGrid.add(swapButton, 1, 3);

        // Grille pour les boutons numériques (à droite)
        GridPane numberGrid = new GridPane();
        numberGrid.setHgap(10);
        numberGrid.setVgap(10);

        for (int i = 1; i <= 9; i++) {
            Button numberButton = new Button(String.valueOf(i));
            numberButton.setPrefWidth(40);
            int finalI = i;
            numberButton.setOnAction(e -> appendToCurrentInput(finalI));
            numberGrid.add(numberButton, (i - 1) % 3, (i - 1) / 3);
        }

        // Bouton 0 a part
        Button zeroButton = new Button("0");
        zeroButton.setPrefWidth(40);
        zeroButton.setOnAction(e -> appendToCurrentInput(0));
        numberGrid.add(zeroButton, 1, 3);

        // Ajout du bouton +/- pour inverser le signe
        Button toggleSignButton = new Button("+/-");
        toggleSignButton.setPrefWidth(40);
        toggleSignButton.setOnAction(e -> toggleSign());
        numberGrid.add(toggleSignButton, 2, 3); // Ajout du bouton à la droite du bouton 0

        numberGrid.setAlignment(Pos.CENTER_RIGHT); // Le nb sont a droite

        // Ajouter les grilles dans le HBox
        buttonHBox.getChildren().addAll(operationGrid, numberGrid);

        // Ajouter HBox dans le VBox principal
        mainVBox.getChildren().add(buttonHBox);

        // Boutton Quitter
        Button quitButton = new Button("Quitter");
        quitButton.setPrefWidth(400);
        quitButton.setStyle("-fx-background-color: red; -fx-text-fill: white;"); // Bouton en rouge
        quitButton.setOnAction(e -> primaryStage.close());
        mainVBox.getChildren().add(quitButton);

        // Créer la scène avec le VBox principal
        Scene scene = new Scene(mainVBox, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Fonction pour ajouter un chiffre au nombre en cours
    private void appendToCurrentInput(int digit) {
        currentInput.append(digit);
        previewField.setText(currentInput.toString());
    }

    // Fonction pour inverser le signe du nombre courant
    private void toggleSign() {
        if (currentInput.length() > 0) {
            try {
                double value = Double.parseDouble(currentInput.toString());
                value = -value; // Inverser le signe
                currentInput.setLength(0); // Réinitialiser l'entrée actuelle
                currentInput.append(value); // Mettre la valeur inversée
                previewField.setText(currentInput.toString()); // Mettre à jour le champ de prévisualisation
            } catch (NumberFormatException e) {
                stackDisplay.setText("Erreur : Entrez un nombre valide.");
            }
        }
    }

    // Gestion du Push 
    private void handlePush() {
        try {
            if (currentInput.length() == 0) {
                stackDisplay.setText("Erreur : Entrez un nombre valide.");
                return;
            }
            double value = Double.parseDouble(currentInput.toString());
            controller.handlePush(value);
            currentInput.setLength(0); // Réinitialiser l'entrée actuelle après le push
            previewField.clear();
            updateStackDisplay();  // Mettre à jour l'affichage de la pile
        } catch (NumberFormatException e) {
            stackDisplay.setText("Erreur : Entrez un nombre valide.");
        }
    }

    // Gérer les opérations
    private void handleOperation(String operation) {
        try {
            switch (operation) {
                case "+":
                    controller.handleAdd();
                    break;
                case "-":
                    controller.handleSubtract();
                    break;
                case "*":
                    controller.handleMultiply();
                    break;
                case "/":
                    controller.handleDivide();
                    break;
                case "clear":
                    controller.handleClear();
                    break;
                case "drop":
                    controller.handleDrop();
                    break;
                case "swap":
                    controller.handleSwap();
                    break;
            }
            updateStackDisplay();  // Mettre à jour l'affichage de la pile après chaque opération
        } catch (Exception e) {
            stackDisplay.setText("Erreur : " + e.getMessage());
        }
    }

    // Gestion affichage de l'historique + pile actuelle
    private void updateStackDisplay() {
        // Afficher uniquement la pile actuelle sans l'inclure dans l'historique
        StringBuilder displayText = new StringBuilder();
        displayText.append("Pile actuelle : ").append(controller.getModel().getStack().toString()).append("\n\n");

        // Ajouter la pile actuelle à l'historique uniquement si elle est différente de la dernière pile historique
        String currentStack = controller.getModel().getStack().toString();
        if (lastFiveStacks.isEmpty() || !lastFiveStacks.get(lastFiveStacks.size() - 1).equals(currentStack)) {
            if (lastFiveStacks.size() == 6) { // On garde 6 éléments car on affiche pas l'élément actuel
                lastFiveStacks.remove(0);  
            }
            lastFiveStacks.add(currentStack);  // Ajouter la pile précédente à la fin
        }

        // Construire l'affichage de l'historique uniquement s'il y a des éléments
        if (!lastFiveStacks.isEmpty()) {
            displayText.append("Historique des 5 dernières piles :\n");
            for (int i = lastFiveStacks.size() - 2; i >= 0; i--) {  // Commencer par la fin, excluant la pile actuelle
                displayText.append("Pile ").append(lastFiveStacks.size() - 1 - i).append(" : ").append(lastFiveStacks.get(i)).append("\n");
            }
        }

        stackDisplay.setText(displayText.toString());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
