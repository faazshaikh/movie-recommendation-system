import java.util.Scanner;

/**
 * Movie Recommendation System - Step 1: CLI Menu
 * This class provides a command-line interface for users to select options.
 */
public class Main {
    private static Scanner scanner = new Scanner(System.in);

    /**
     * Display the main menu options to the user.
     */
    public static void displayMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("Movie Recommendation System");
        System.out.println("=".repeat(50));
        System.out.println("1. View all movies");
        System.out.println("2. Rate a movie");
        System.out.println("3. Get movie recommendations");
        System.out.println("4. View my ratings");
        System.out.println("5. Exit");
        System.out.println("=".repeat(50));
    }

    /**
     * Get and validate user's menu selection, and handle the selection.
     * @return The user's choice as a string
     */
    public static String getUserChoice() {
        while (true) {
            try {
                System.out.print("\nPlease select an option (1-5): ");
                String choice = scanner.nextLine().trim();
                if (choice.equals("1") || choice.equals("2") || choice.equals("3") || 
                    choice.equals("4") || choice.equals("5")) {
                    
                    // Handle the selection (Step 1: no action, just acknowledge)
                    String optionName = switch (choice) {
                        case "1" -> "View all movies";
                        case "2" -> "Rate a movie";
                        case "3" -> "Get movie recommendations";
                        case "4" -> "View my ratings";
                        case "5" -> "Exit";
                        default -> "Unknown option";
                    };
                    
                    System.out.println("\nYou selected: " + optionName);
                    System.out.println("(No action taken - Step 1 implementation)");
                    
                    return choice;
                } else {
                    System.out.println("Invalid option. Please enter a number between 1 and 5.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred. Please try again.");
            }
        }
    }

    /**
     * Main function to run the CLI menu.
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("Welcome to the Movie Recommendation System!");
        
        while (true) {
            displayMenu();
            String choice = getUserChoice();
            
            if (choice.equals("5")) {
                System.out.println("\nThank you for using the Movie Recommendation System. Goodbye!");
                break;
            }
            
            // Pause before showing menu again
            System.out.print("\nPress Enter to continue...");
            scanner.nextLine();
        }
        
        scanner.close();
    }
}

