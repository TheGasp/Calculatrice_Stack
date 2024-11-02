package Calculatrice;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Locale;

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
	            // Console
	            launchConsoleMode();
	            break;
	        } else if (choix.equals("2")) {
	            // Affichage graphique
	            launchJavaFXMode();
	            break;
	        } else {
	            System.out.println("Choix invalide");
	        }
	    }
	
	    scanner.close();
    }
    
    // Affichage console
	private static void launchConsoleMode() {
        CalcModel model = new CalcModel();
        CalcController controller = new CalcController(model);
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
                                validInput = true;  //C'est un nb -> on sort
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
            } catch (IllegalStateException e) {
                // Pas assez de nb dans la liste
                System.out.println("Erreur : " + e.getMessage());
			} catch (ArithmeticException e) {
                // Division par 0 
                System.out.println("Erreur : " + e.getMessage());
            }
        } 
    }

	// Affichage graphique
	private static void launchJavaFXMode() {
	    CalcView.launch(CalcView.class);
	}
}

