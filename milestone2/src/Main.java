import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Movie Recommendation System - Main CLI Menu
 * This class provides a command-line interface for users to interact with the system.
 */
public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static DataManager dataManager;
    private static ViewMovies viewMovies;
    private static RateMovie rateMovie;
    private static SearchMoviesByGenre searchMoviesByGenre;
    private static Exit exitHandler;

    /**
     * Initialize all components of the system.
     */
    private static void initialize() {
        dataManager = new DataManager();
        dataManager.loadData();
        
        viewMovies = new ViewMovies(dataManager);
        rateMovie = new RateMovie(dataManager, scanner);
        searchMoviesByGenre = new SearchMoviesByGenre(dataManager, scanner);
        exitHandler = new Exit(dataManager);
    }

    /**
     * Display the main menu options to the user.
     */
    public static void displayMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("Movie Recommendation System");
        System.out.println("=".repeat(50));
        System.out.println("1. View all movies");
        System.out.println("2. Rate a movie");
        System.out.println("3. Search movies by genre");
        System.out.println("4. Get movie recommendations");
        System.out.println("5. View my ratings");
        System.out.println("6. Exit");
        System.out.println("=".repeat(50));
    }

    /**
     * Get and validate user's menu selection, and handle the selection.
     * @return The user's choice as a string
     */
    public static String getUserChoice() {
        while (true) {
            try {
                System.out.print("\nPlease select an option (1-6): ");
                String choice = scanner.nextLine().trim();
                if (choice.equals("1") || choice.equals("2") || choice.equals("3") || 
                    choice.equals("4") || choice.equals("5") || choice.equals("6")) {
                    
                    return choice;
                } else {
                    System.out.println("Invalid option. Please enter a number between 1 and 6.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred. Please try again.");
            }
        }
    }

    /**
     * Handle the user's menu selection.
     * @param choice The user's selected option
     */
    public static void handleMenuChoice(String choice) {
        switch (choice) {
            case "1":
                viewMovies.displayAllMovies();
                break;
            case "2":
                rateMovie.rateMovieFlow();
                break;
            case "3":
                searchMoviesByGenre.searchByGenre();
                break;
            case "4":
                displayRecommendations();
                break;
            case "5":
                displayMyRatings();
                break;
            case "6":
                exitHandler.performExit();
                break;
            default:
                System.out.println("Unknown option.");
        }
    }

    /**
     * Displays movie recommendations to the user.
     */
    public static void displayRecommendations() {
        System.out.println("\n--- MOVIE RECOMMENDATIONS ---");
        
        List<Movie> recommendations = dataManager.getRecommendations();
        
        if (recommendations.isEmpty()) {
            System.out.println("No recommendations available at this time.");
            System.out.println("Try rating some movies first to get personalized recommendations!");
            return;
        }
        
        System.out.println("Here are your top " + recommendations.size() + " movie recommendations:\n");
        
        for (int i = 0; i < recommendations.size(); i++) {
            Movie movie = recommendations.get(i);
            System.out.println("--- Recommendation " + (i + 1) + " ---");
            System.out.println("ID: " + movie.getId());
            System.out.println("Title: " + movie.getTitle());
            System.out.println("Director: " + movie.getDirector());
            System.out.println("Year: " + movie.getYear());
            System.out.println("Genres: " + String.join(", ", movie.getGenres()));
            System.out.println("Average Rating: " + movie.getAvgRating());
            System.out.println("Rating Count: " + movie.getNumRatings());
            System.out.println();
        }
        
        System.out.println("=".repeat(50));
    }

    /**
     * Displays all the user's ratings.
     */
    public static void displayMyRatings() {
        System.out.println("\n--- MY RATINGS ---");
        
        Map<Integer, Rating> ratings = dataManager.getRatings();
        Map<Integer, Movie> movies = dataManager.getMovies();
        
        if (ratings.isEmpty()) {
            System.out.println("You haven't rated any movies yet.");
            System.out.println("Use option 2 to rate a movie!");
            return;
        }
        
        System.out.println("You have rated " + ratings.size() + " movie(s):\n");
        
        // Sort by movie ID for consistent display
        List<Integer> sortedMovieIds = new ArrayList<>(ratings.keySet());
        Collections.sort(sortedMovieIds);
        
        for (Integer movieId : sortedMovieIds) {
            Rating rating = ratings.get(movieId);
            Movie movie = movies.get(movieId);
            
            if (movie != null) {
                System.out.println("----------------------------------------");
                System.out.println("Movie ID: " + movieId);
                System.out.println("Title: " + movie.getTitle());
                System.out.println("Director: " + movie.getDirector());
                System.out.println("Year: " + movie.getYear());
                System.out.println("Your Rating: " + rating.getRating() + " out of 5");
                System.out.println("Rated on: " + rating.getTimestamp());
            } else {
                // Movie not found (shouldn't happen, but handle gracefully)
                System.out.println("----------------------------------------");
                System.out.println("Movie ID: " + movieId);
                System.out.println("Your Rating: " + rating.getRating() + " out of 5");
                System.out.println("Rated on: " + rating.getTimestamp());
                System.out.println("(Movie details not available)");
            }
        }
        
        System.out.println("----------------------------------------");
        System.out.println("End of ratings list.\n");
    }

    /**
     * Main function to run the CLI menu.
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("Welcome to the Movie Recommendation System!");
        
        initialize();
        
        while (true) {
            displayMenu();
            String choice = getUserChoice();
            
            if (choice.equals("6")) {
                exitHandler.performExit();
                break;
            }
            
            handleMenuChoice(choice);
            
            // Pause before showing menu again
            System.out.print("\nPress Enter to continue...");
            scanner.nextLine();
        }
        
        scanner.close();
    }
}

