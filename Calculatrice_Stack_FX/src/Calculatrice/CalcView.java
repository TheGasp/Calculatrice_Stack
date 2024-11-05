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
 private StringBuilder currentInput = new StringBuilder(); //On stock le nb qu'on est en train de rentrer

 @Override
 public void start(Stage primaryStage) { // primaryStage = fenetre principale
     CalcModel model = new CalcModel();
     controller = new CalcController(model);

     primaryStage.setTitle("Calculatrice de Sami et Gaspard");

     VBox mainVBox = new VBox(10); // VBox = Verticale -> espacement de 10
     mainVBox.setPadding(new Insets(10));

     stackDisplay = new TextArea();
     stackDisplay.setEditable(false); // Pas modifiable
     stackDisplay.setPrefHeight(200);
     mainVBox.getChildren().add(stackDisplay);

     // Previsu + button del
     HBox previewHBox = new HBox(10); // HBox = Horizontale
     previewHBox.setAlignment(Pos.CENTER);

     previewField = new TextField();
     previewField.setEditable(false);
     previewField.setPrefWidth(320); 

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
     primaryStage.setScene(scene);
     primaryStage.show();
 }

 // Fonction pour ajouter un chiffre au nombre en cours
 private void appendToCurrentInput(int digit) {
     currentInput.append(digit);
     previewField.setText(currentInput.toString());
 }

 // Fonction de suppression
 private void deleteLastDigit() {
     if (currentInput.length() > 0) {
         currentInput.deleteCharAt(currentInput.length() - 1); // Supprimer le dernier chiffre
         previewField.setText(currentInput.toString());        // Mettre à jour le champ de prévisu
     }
 }

 // Fonction inversement "+/-"
 private void inverseSigne() {
     if (currentInput.length() > 0) {
         try {
             double value = Double.parseDouble(currentInput.toString());
             value = -value;
             currentInput.setLength(0);
             currentInput.append(value);
             previewField.setText(currentInput.toString());
         } catch (NumberFormatException e) {
             stackDisplay.setText("Erreur : Entrez un nombre valide.");
         }
     }
 }
 
// Fonction de gestion de la virgule
private void addVirgule() {
  if (!currentInput.toString().contains(".")) { // Vérif si le nb a une virgule
      if (currentInput.length() == 0) {
          currentInput.append("0."); // Si champ vide
      } else {
          currentInput.append("."); // Ajoute une virgule
      }
      previewField.setText(currentInput.toString());
  }
}

// Gestion du push
 private void handlePush() {
     try {
         if (currentInput.length() == 0) {
             stackDisplay.setText("Erreur : Entrez un nombre valide.");
             return;
         }
         double value = Double.parseDouble(currentInput.toString());
         controller.handlePush(value);
         currentInput.setLength(0);
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

//Fonction qui gère l'affichage
private void updateStackDisplay() {
  List<Double> stack = controller.getModel().getStack();
  StringBuilder displayText = new StringBuilder();

  if (!stack.isEmpty()) {
      // Nb actuel
      double lastValue = stack.get(stack.size() - 1);
      displayText.append("Nombre actuel : ").append(lastValue).append("\n\n");

      // 5 derniers elements (sans compter le nb actuel)
      displayText.append("Historique des 5 derniers nombres :\n");
      int historyStart = Math.max(0, stack.size() - 6); //index de depart
      for (int i = stack.size() - 2; i >= historyStart; i--) { //stack.size - 1 = indice du remier element
          displayText.append(stack.get(i)).append("\n");
      }
  } 
  
  stackDisplay.setText(displayText.toString());
}

 public static void main(String[] args) {
     launch(args);
 }
}
