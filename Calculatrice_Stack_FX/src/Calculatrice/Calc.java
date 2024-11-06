package Calculatrice;

import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class Calc {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Choix entre affichage graphique ou console
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

    // Démarrage du mode Console
    private static void launchConsoleMode() {
        ICalcModel model = new CalcModel();
        ICalcController controller = new CalcController(model);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Calculatrice de Sami et Gaspard");
        while (true) {
            System.out.print("Entrez une commande (push, +, -, *, /, clear, drop, swap, quit): ");
            String input = scanner.next();
            scanner.useLocale(Locale.US);

            try {
                switch (input) {
                    case "push":
                        double value = 0;
                        boolean validInput = false;

                        while (!validInput) {
                            System.out.print("Entrez un nombre : ");
                            try {
                                value = scanner.nextDouble();
                                validInput = true;
                            } catch (InputMismatchException e) {
                                System.out.println("Erreur : vous devez entrer un nombre valide.");
                                scanner.next();
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
                System.out.println("Erreur : " + e.getMessage());
            }
        }
    }

    // Démarrage du mode JavaFX (Graphique)
    private static void launchJavaFXMode() {
        // Créez le modèle et le contrôleur ici
        ICalcModel model = new CalcModel(); // Remplacez par votre implémentation du modèle
        CalcController controller = new CalcController(model);

        // Démarrez l'application JavaFX
        CalcView.startApplication(controller);
    }
}
