import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Scanner;

/**
 * Handles movie rating functionality including movie selection,
 * rating input validation, and rating storage with overwrite capability.
 */
public class RateMovie {
    private final DataManager dataManager;
    private final Scanner scanner;
    private static final int MIN_RATING = 1;
    private static final int MAX_RATING = 5;

    /**
     * Constructor for RateMovie.
     * @param dataManager The DataManager instance
     * @param scanner The Scanner instance for user input
     */
    public RateMovie(DataManager dataManager, Scanner scanner) {
        this.dataManager = dataManager;
        this.scanner = scanner;
    }

    /**
     * Main entry point for rating a movie.
     * Guides the user through selecting a movie and providing a rating.
     */
    public void rateMovieFlow() {
        System.out.println("\n--- RATE A MOVIE ---");
        
        // Display all available movies with their IDs
        displayAvailableMovies();
        
        // Get movie ID from user with validation
        int movieId = getMovieIdFromUser();
        
        if (movieId == -1) {
            System.out.println("Invalid movie ID. Rating cancelled.");
            return;
        }
        
        // Get movie and confirm selection
        Movie selectedMovie = dataManager.getMovies().get(movieId);
        
        if (selectedMovie == null) {
            System.out.println("Error: Movie with ID " + movieId + " not found.");
            return;
        }
        
        // Display the selected movie for confirmation
        displayMovieForConfirmation(selectedMovie);
        
        // Confirm user's selection
        if (!confirmMovieSelection()) {
            System.out.println("Movie selection cancelled.");
            return;
        }
        
        // Get rating from user
        int rating = getRatingFromUser();
        
        if (rating == -1) {
            System.out.println("Rating cancelled.");
            return;
        }
        
        // Check if movie is already rated
        Map<Integer, Rating> ratings = dataManager.getRatings();
        boolean isUpdate = ratings.containsKey(movieId);
        
        if (isUpdate) {
            System.out.println("You have already rated this movie with a rating of " + 
                             ratings.get(movieId).getRating() + ".");
            if (!confirmOverwrite()) {
                System.out.println("Rating update cancelled.");
                return;
            }
        }
        
        // Save the rating
        String timestamp = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Rating newRating = new Rating(movieId, rating, timestamp);
        dataManager.addOrUpdateRating(newRating);
        
        String action = isUpdate ? "updated" : "saved";
        System.out.println("Rating successfully " + action + "!");
        System.out.println("Movie: " + selectedMovie.getTitle());
        System.out.println("Rating: " + rating + " out of 5");
        System.out.println("Timestamp: " + timestamp);
    }

    /**
     * Displays all available movies for the user to choose from.
     */
    private void displayAvailableMovies() {
        Map<Integer, Movie> movies = dataManager.getMovies();
        
        if (movies.isEmpty()) {
            System.out.println("No movies available in the system.");
            return;
        }
        
        System.out.println("\nAvailable movies:");
        for (Movie movie : movies.values()) {
            System.out.println("ID: " + movie.getId() + " | " + movie.getTitle() + 
                             " (" + movie.getYear() + ")");
        }
    }

    /**
     * Prompts the user to enter a movie ID and validates it.
     * @return The valid movie ID, or -1 if invalid
     */
    private int getMovieIdFromUser() {
        while (true) {
            System.out.print("\nEnter the ID of the movie you want to rate: ");
            
            try {
                String input = scanner.nextLine().trim();
                
                if (input.isEmpty()) {
                    System.out.println("Error: ID cannot be empty. Please try again.");
                    continue;
                }
                
                int movieId = Integer.parseInt(input);
                
                // Check if movie exists
                if (!dataManager.getMovies().containsKey(movieId)) {
                    System.out.println("Error: Movie with ID " + movieId + " does not exist. Please try again.");
                    continue;
                }
                
                return movieId;
                
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid integer ID.");
            }
        }
    }

    /**
     * Displays the selected movie for user confirmation.
     * @param movie The movie to display
     */
    private void displayMovieForConfirmation(Movie movie) {
        System.out.println("\n--- CONFIRM SELECTION ---");
        System.out.println("Title: " + movie.getTitle());
        System.out.println("Director: " + movie.getDirector());
        System.out.println("Year: " + movie.getYear());
        System.out.println("Genres: " + String.join(", ", movie.getGenres()));
        System.out.println("Average Rating: " + movie.getAvgRating());
    }

    /**
     * Asks the user to confirm their movie selection.
     * @return true if confirmed, false otherwise
     */
    private boolean confirmMovieSelection() {
        System.out.print("\nIs this the correct movie? (yes/no): ");
        
        String response = scanner.nextLine().trim().toLowerCase();
        return response.equals("yes") || response.equals("y");
    }

    /**
     * Prompts the user to enter a rating value and validates it.
     * Rating must be between 1 and 5.
     * @return The valid rating, or -1 if user cancels
     */
    private int getRatingFromUser() {
        System.out.println("\n--- ENTER RATING ---");
        System.out.println("Please rate this movie on a scale of " + MIN_RATING + " to " + MAX_RATING);
        System.out.println("(1 = Poor, 2 = Fair, 3 = Good, 4 = Very Good, 5 = Excellent)");
        
        while (true) {
            System.out.print("\nEnter your rating (1-5) or 0 to cancel: ");
            
            try {
                String input = scanner.nextLine().trim();
                
                if (input.isEmpty()) {
                    System.out.println("Error: Rating cannot be empty. Please try again.");
                    continue;
                }
                
                int rating = Integer.parseInt(input);
                
                // Check for cancel
                if (rating == 0) {
                    return -1;
                }
                
                // Validate range
                if (rating < MIN_RATING || rating > MAX_RATING) {
                    System.out.println("Error: Rating must be between " + MIN_RATING + " and " + MAX_RATING + ".");
                    continue;
                }
                
                return rating;
                
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid integer between 1 and 5.");
            }
        }
    }

    /**
     * Asks the user if they want to overwrite their previous rating.
     * @return true if user confirms overwrite, false otherwise
     */
    private boolean confirmOverwrite() {
        System.out.print("Do you want to overwrite your previous rating? (yes/no): ");
        
        String response = scanner.nextLine().trim().toLowerCase();
        return response.equals("yes") || response.equals("y");
    }
}
