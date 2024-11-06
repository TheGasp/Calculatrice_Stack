package Calculatrice;

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
import java.util.List;

public class CalcView extends Application implements ICalcView {
    private static ICalcController staticController;
    private ICalcController controller;
    private TextArea stackDisplay;  // Zone pour l'historique (plusieurs lignes)
    private TextField previewField; // Utile pour prévisualisation (une seule ligne)
    private StringBuilder actuelInput = new StringBuilder(); // On stocke le nombre qu'on est en train de rentrer

    // Méthode pour démarrer l'application avec un contrôleur
    public static void startApplication(ICalcController controller) {
        CalcView.staticController = controller;
        launch();
    }

    @Override
    public void init() {
        this.controller = staticController;
    }

    @Override
    public void start(Stage primaryStage) { // primaryStage = fenêtre principale
        primaryStage.setTitle("Calculatrice de Sami et Gaspard");

        VBox mainVBox = new VBox(10); // VBox = Verticale -> espacement de 10
        mainVBox.setPadding(new Insets(10));

        // Gestion de la zone d'historique
        stackDisplay = new TextArea();
        stackDisplay.setEditable(false); // Pas modifiable
        stackDisplay.setPrefHeight(150);
        stackDisplay.setMaxWidth(400); // Largeur max
        stackDisplay.getStyleClass().add("textarea-histo"); // Style CSS
        
        HBox stackDisplayConteneur = new HBox(stackDisplay); // centrage (sert au plein écran)
        stackDisplayConteneur.setAlignment(Pos.CENTER);
        mainVBox.getChildren().add(stackDisplayConteneur);

        // Prévision du champ de texte + bouton de suppression
        HBox previewHBox = new HBox(10); // HBox = Horizontale
        previewHBox.setAlignment(Pos.CENTER);

        previewField = new TextField(); // Affiche uniquement le texte (ne stocke pas)
        previewField.setEditable(false);
        previewField.setPrefWidth(320);
        previewField.getStyleClass().add("textfield-preview");

        Button delButton = new Button("Del");
        delButton.setPrefWidth(60);
        delButton.setOnAction(e -> deleteLastDigit());

        previewHBox.getChildren().addAll(previewField, delButton); // Ajout des deux éléments
        mainVBox.getChildren().add(previewHBox); // Ajout de la HBox au VBox principal

        // HBox pour les boutons
        HBox buttonHBox = new HBox(20);
        buttonHBox.setAlignment(Pos.CENTER);

        GridPane operationGrid = new GridPane(); // Création d'une grille permettant de situer les boutons
        operationGrid.setHgap(10);
        operationGrid.setVgap(10);

        // Boutons d'opérations
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

        // Positionnement des boutons d'opérations dans la grille
        operationGrid.add(addButton, 0, 0);
        operationGrid.add(subtractButton, 1, 0);
        operationGrid.add(multiplyButton, 0, 1);
        operationGrid.add(divideButton, 1, 1);
        operationGrid.add(pushButton, 0, 2);
        operationGrid.add(clearButton, 1, 2);
        operationGrid.add(dropButton, 0, 3);
        operationGrid.add(swapButton, 1, 3);

        // Grille des chiffres
        GridPane numberGrid = new GridPane();
        numberGrid.setHgap(10);
        numberGrid.setVgap(10);

        // Boutons des chiffres de 1 à 9
        for (int i = 1; i <= 9; i++) {
            Button numberButton = new Button(String.valueOf(i));
            numberButton.setPrefWidth(40);
            int finalI = i;
            numberButton.setOnAction(e -> appendToCurrentInput(finalI));
            numberGrid.add(numberButton, (i - 1) % 3, (i - 1) / 3); // Position sur la grille (x,y)
        }

        Button virguleButton = new Button(",");
        virguleButton.setPrefWidth(40);
        virguleButton.setOnAction(e -> addVirgule());
        numberGrid.add(virguleButton, 0, 3); // En bas à gauche

        Button zeroButton = new Button("0");
        zeroButton.setPrefWidth(40);
        zeroButton.setOnAction(e -> appendToCurrentInput(0));
        numberGrid.add(zeroButton, 1, 3); // Au milieu en bas

        Button inverseButton = new Button("+/-");
        inverseButton.setPrefWidth(40);
        inverseButton.setOnAction(e -> inverseSigne());
        numberGrid.add(inverseButton, 2, 3); // En bas à droite

        numberGrid.setAlignment(Pos.CENTER_RIGHT);

        buttonHBox.getChildren().addAll(operationGrid, numberGrid);
        mainVBox.getChildren().add(buttonHBox);

        // Bouton quitter (centré)
        HBox quitButtonHBox = new HBox();
        quitButtonHBox.setAlignment(Pos.CENTER);

        Button quitButton = new Button("Quitter");
        quitButton.setPrefWidth(400);
        quitButton.setStyle("-fx-background-color: red; -fx-text-fill: white;"); // En rouge
        quitButton.setOnAction(e -> primaryStage.close());

        quitButtonHBox.getChildren().add(quitButton);
        mainVBox.getChildren().add(quitButtonHBox);

        // Création de la scène
        Scene scene = new Scene(mainVBox, 400, 400); // Taille de la fenêtre
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm()); // Charge le CSS
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Méthodes pour gérer l'interface et l'interaction utilisateur

    private void appendToCurrentInput(int digit) {
        actuelInput.append(digit);
        previewField.setText(actuelInput.toString());
    }

    private void deleteLastDigit() {
        if (actuelInput.length() > 0) {
            actuelInput.deleteCharAt(actuelInput.length() - 1); // Supprime le dernier chiffre
            previewField.setText(actuelInput.toString()); // Mise à jour du champ de prévisu
        }
    }

    private void inverseSigne() {
        if (actuelInput.length() > 0) {
            try {
                double value = Double.parseDouble(actuelInput.toString());
                value = -value;
                actuelInput.setLength(0);
                actuelInput.append(value);
                previewField.setText(actuelInput.toString());
            } catch (NumberFormatException e) {
                showErrorInHistory("Entrez un nombre valide.");
            }
        }
    }

    private void addVirgule() {
        if (!actuelInput.toString().contains(".")) { // Vérifie si le nombre a une virgule
            if (actuelInput.length() == 0) {
                actuelInput.append("0."); // Si champ vide
            } else {
                actuelInput.append("."); // Ajoute une virgule
            }
            previewField.setText(actuelInput.toString());
        }
    }

    private void handlePush() {
        try {
            if (actuelInput.length() == 0) {
                showErrorInHistory("Entrez un nombre valide.");
                return;
            }
            double value = Double.parseDouble(actuelInput.toString());
            controller.handlePush(value);
            actuelInput.setLength(0); // Vide le champ
            previewField.clear();
            updateStackDisplay();
        } catch (NumberFormatException e) {
            showErrorInHistory("Entrez un nombre valide.");
        }
    }

    private void handleOperation(String operation) {
        try {
            switch (operation) {
                case "+" -> controller.handleAdd();
                case "-" -> controller.handleSubtract();
                case "*" -> controller.handleMultiply();
                case "/" -> controller.handleDivide();
                case "clear" -> controller.handleClear();
                case "drop" -> controller.handleDrop();
                case "swap" -> controller.handleSwap();
            }
            updateStackDisplay();
        } catch (Exception e) {
            showErrorInHistory(e.getMessage());
        }
    }

    @Override
    public void updateStackDisplay() {
        List<Double> stack = controller.getModel().getStack();
        StringBuilder displayText = new StringBuilder();

        int maxNb = 7;
        int displayCount = Math.min(stack.size(), maxNb); // Nb réel à afficher

        for (int i = 0; i < maxNb - displayCount; i++) {
            displayText.append("\n"); // Lignes vides pour afficher en bas de l'écran
        }

        for (int i = stack.size() - displayCount; i < stack.size(); i++) {
            displayText.append(stack.get(i)).append("\n"); // Ajout des nombres
        }

        if (displayText.length() > 0 && displayText.charAt(displayText.length() - 1) == '\n') { // vérif si il y a un saut de ligne à la fin (inutile)
            displayText.deleteCharAt(displayText.length() - 1); // enlève le saut de ligne
        }

        stackDisplay.setText(displayText.toString());
    }

    @Override
    public void showErrorInHistory(String errorMessage) {
        if (stackDisplay != null) {
            stackDisplay.setText("Erreur : " + errorMessage);
        } else {
            System.out.println("Erreur : " + errorMessage); 
        }
    }

    @Override
    public void setController(ICalcController controller) {
        this.controller = controller;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
