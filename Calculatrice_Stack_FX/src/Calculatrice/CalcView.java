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

public class CalcView extends Application {

    private CalcController controller;
    private TextArea stackDisplay;  // Zone pour l'historique (plusieurs lignes)
    private TextField previewField; // Utile pour prévisualisation (une seul ligne) 
    private StringBuilder actuelInput = new StringBuilder(); //On stock le nb qu'on est en train de rentrer

    @Override
    public void start(Stage primaryStage) { // primaryStage = fenetre principale
        CalcModel model = new CalcModel();
        controller = new CalcController(model);

        primaryStage.setTitle("Calculatrice de Sami et Gaspard");

        VBox mainVBox = new VBox(10); // VBox = Verticale -> espacement de 10
        mainVBox.setPadding(new Insets(10));

        // Gestion TextArea
        stackDisplay = new TextArea();
        stackDisplay.setEditable(false); // Pas modifiable
        stackDisplay.setPrefHeight(150);
        stackDisplay.setMaxWidth(400); // Largeur max
        stackDisplay.getStyleClass().add("textarea-histo"); // Style CSS
        
        HBox stackDisplayConteneur = new HBox(stackDisplay); // centrage (sert au plein ecran)
        stackDisplayConteneur.setAlignment(Pos.CENTER); 
        mainVBox.getChildren().add(stackDisplayConteneur); 

        // Prévision du champ de texte + bouton de suppression
        HBox previewHBox = new HBox(10); // HBox = Horizontale
        previewHBox.setAlignment(Pos.CENTER);

        previewField = new TextField(); //fait uniquement l'affichage du texte (ne stock pas)
        previewField.setEditable(false);
        previewField.setPrefWidth(320);
        previewField.getStyleClass().add("textfield-preview");

        Button delButton = new Button("Del");
        delButton.setPrefWidth(60);
        delButton.setOnAction(e -> deleteLastDigit());

        previewHBox.getChildren().addAll(previewField, delButton); // Ajout des 2 elements
        mainVBox.getChildren().add(previewHBox); // Ajout de la HBox au VBox principal
        
        // Hbox pour les buttons
        HBox buttonHBox = new HBox(20);
        buttonHBox.setAlignment(Pos.CENTER);

        GridPane operationGrid = new GridPane(); // creation d'une grille permettant de situer les buttons
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

        operationGrid.add(addButton, 0, 0);
        operationGrid.add(subtractButton, 1, 0);
        operationGrid.add(multiplyButton, 0, 1);
        operationGrid.add(divideButton, 1, 1);
        operationGrid.add(pushButton, 0, 2);
        operationGrid.add(clearButton, 1, 2);
        operationGrid.add(dropButton, 0, 3);
        operationGrid.add(swapButton, 1, 3);

        GridPane numberGrid = new GridPane(); // Grilles des nombres
        numberGrid.setHgap(10);
        numberGrid.setVgap(10);

        for (int i = 1; i <= 9; i++) {
            Button numberButton = new Button(String.valueOf(i));
            numberButton.setPrefWidth(40);
            int finalI = i;
            numberButton.setOnAction(e -> appendToCurrentInput(finalI));
            numberGrid.add(numberButton, (i - 1) % 3, (i - 1) / 3); // position sur la grille (x,y)
        }

        Button virguleButton = new Button(",");
        virguleButton.setPrefWidth(40);
        virguleButton.setOnAction(e -> addVirgule());
        numberGrid.add(virguleButton, 0, 3); // En bas a gauche
        
        Button zeroButton = new Button("0");
        zeroButton.setPrefWidth(40);
        zeroButton.setOnAction(e -> appendToCurrentInput(0));
        numberGrid.add(zeroButton, 1, 3); //Au milleu en bas

        Button inverseButton = new Button("+/-");
        inverseButton.setPrefWidth(40);
        inverseButton.setOnAction(e -> inverseSigne());
        numberGrid.add(inverseButton, 2, 3); // En bas a droite

        numberGrid.setAlignment(Pos.CENTER_RIGHT);

        buttonHBox.getChildren().addAll(operationGrid, numberGrid);
        mainVBox.getChildren().add(buttonHBox);

        // HBox pour le button quitter (sert a le centrer)
        HBox quitButtonHBox = new HBox();
        quitButtonHBox.setAlignment(Pos.CENTER);

        Button quitButton = new Button("Quitter");
        quitButton.setPrefWidth(400);
        quitButton.setStyle("-fx-background-color: red; -fx-text-fill: white;"); //En rouge
        quitButton.setOnAction(e -> primaryStage.close());

        quitButtonHBox.getChildren().add(quitButton);
        mainVBox.getChildren().add(quitButtonHBox);

        Scene scene = new Scene(mainVBox, 400, 400); // taille de la fenetre
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm()); // charge le CSS
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    
    // Fonction pour ajouter un chiffre au nombre en cours
    private void appendToCurrentInput(int digit) {
        actuelInput.append(digit);
        previewField.setText(actuelInput.toString());
    }

    // Fonction de suppression
    private void deleteLastDigit() {
        if (actuelInput.length() > 0) {
            actuelInput.deleteCharAt(actuelInput.length() - 1); // Supprimer le dernier chiffre
            previewField.setText(actuelInput.toString()); // Mise à jour du champ de prévisu
        }
    }

    // Fonction inversement "+/-"
    private void inverseSigne() {
        if (actuelInput.length() > 0) {
            try {
                double value = Double.parseDouble(actuelInput.toString());
                value = -value;
                actuelInput.setLength(0);
                actuelInput.append(value);
                previewField.setText(actuelInput.toString());
            } catch (NumberFormatException e) {
                stackDisplay.setText("Erreur : Entrez un nombre valide.");
            }
        }
    }
    
    // Fonction de gestion de la virgule
    private void addVirgule() {
        if (!actuelInput.toString().contains(".")) { // Vérif si le nb a une virgule
            if (actuelInput.length() == 0) {
                actuelInput.append("0."); // Si champ vide
            } else {
                actuelInput.append("."); // Ajoute une virgule
            }
            previewField.setText(actuelInput.toString());
        }
    }

    // Gestion du push
    private void handlePush() {
        try {
            if (actuelInput.length() == 0) {
                stackDisplay.setText("Erreur : Entrez un nombre valide.");
                return;
            }
            double value = Double.parseDouble(actuelInput.toString());
            controller.handlePush(value);
            actuelInput.setLength(0); // on vide
            previewField.clear();
            updateStackDisplay();
        } catch (NumberFormatException e) {
            stackDisplay.setText("Erreur : Entrez un nombre valide.");
        }
    }

    // Gestion des opérations classiques
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
            updateStackDisplay();
        } catch (Exception e) {
            stackDisplay.setText("Erreur : " + e.getMessage());
        }
    }

 // Affichage historique
    private void updateStackDisplay() {
        List<Double> stack = controller.getModel().getStack();
        StringBuilder displayText = new StringBuilder();

        int maxDisplayCount = 7;
        int displayCount = Math.min(stack.size(), maxDisplayCount); // Nb réel a affiche

        for (int i = 0; i < maxDisplayCount - displayCount; i++) { 
            displayText.append("\n"); // Lignes vide en + pour afficher en bas de l'ecran
        }

        for (int i = stack.size() - displayCount; i < stack.size(); i++) {
            displayText.append(stack.get(i)).append("\n"); // Ajout des nb
        }

        if (displayText.length() > 0 && displayText.charAt(displayText.length() - 1) == '\n') { // verif si il y a un saut de ligne a la fin (inutile)
            displayText.deleteCharAt(displayText.length() - 1); // enleve le saut de ligne
        }

        stackDisplay.setText(displayText.toString());
    }


    public static void main(String[] args) {
        launch(args);
    }
}
