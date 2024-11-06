package Calculatrice;

import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class Calc {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

      //choix entre afffichage graphique ou console (while -> force a faire un choix valide) 
        while (true) {
            System.out.println("Choisissez votre mode : ");
            System.out.println("1 - Console");
            System.out.println("2 - Graphique");
            String choix = scanner.nextLine();

            if (choix.equals("1")) {
                // Mode Console
                launchConsoleMode();
                break;
            } else if (choix.equals("2")) {
                // Mode Graphique
                launchJavaFXMode();
                break;
            } else {
                System.out.println("Choix invalide, veuillez entrer 1 ou 2.");
            }
        }
        scanner.close();
    }

 // Affichage console
    private static void launchConsoleMode() {
        ICalcModel model = new CalcModel();
        ICalcController controller = new CalcController(model);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Calculatrice de Sami et Gaspard");
        while (true) {
            System.out.print("Entrez une commande (push, +, -, *, /, clear, drop, swap, quit): ");
            String input = scanner.next();
            scanner.useLocale(Locale.US); // On pose ca pour utiliser le point a la place de la virgule (homogénise le tout)

            try {
                switch (input) {
                    case "push":
                        double value = 0;
                        boolean validInput = false;
                        
                        // On check qu'on rentre bien un nb
                        while (!validInput) {
                            System.out.print("Entrez un nombre : ");
                            try {
                                value = scanner.nextDouble();
                                validInput = true;//C'est un nb -> on sort
                            } catch (InputMismatchException e) {
                                System.out.println("Erreur : vous devez entrer un nombre valide.");
                                scanner.next(); //C'est pas un nb on retente
                            }
                        }
                        controller.handlePush(value);
                        break;
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
                    case "quit":
                        scanner.close();
                        return;
                    default:
                        System.out.println("Commande inconnue.");
                }
                System.out.println("Pile actuelle : " + model.getStack());
            } catch (IllegalStateException | ArithmeticException e) {
                System.out.println("Erreur : " + e.getMessage()); //affichage des error
            }
        }
    }

    // Affichage graphique
    private static void launchJavaFXMode() {
        // Modèle + Controlleur
        ICalcModel model = new CalcModel(); 
        CalcController controller = new CalcController(model);

        CalcView.startApplication(controller);
    }
}
