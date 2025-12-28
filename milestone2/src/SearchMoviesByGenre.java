import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Handles searching for movies by genre with user input validation
 * and proper output formatting.
 */
public class SearchMoviesByGenre {
    private final DataManager dataManager;
    private final Scanner scanner;

    /**
     * Constructor for SearchMoviesByGenre.
     * @param dataManager The DataManager instance
     * @param scanner The Scanner instance for user input
     */
    public SearchMoviesByGenre(DataManager dataManager, Scanner scanner) {
        this.dataManager = dataManager;
        this.scanner = scanner;
    }

    /**
     * Prompts the user to enter a genre and displays matching movies.
     */
    public void searchByGenre() {
        System.out.println("\n--- SEARCH MOVIES BY GENRE ---");
        System.out.print("Enter the genre to search for: ");
        
        String genre = scanner.nextLine().trim();
        
        // Validate input
        if (genre.isEmpty()) {
            System.out.println("Error: Genre cannot be empty. Please try again.");
            return;
        }
        
        // Perform the search
        List<Movie> results = findMoviesByGenre(genre);
        
        // Display results
        displaySearchResults(genre, results);
    }

    /**
     * Searches the movie database for movies matching the given genre.
     * @param genre The genre to search for (case-insensitive)
     * @return List of movies that contain the specified genre
     */
    public List<Movie> findMoviesByGenre(String genre) {
        List<Movie> results = new ArrayList<>();
        Map<Integer, Movie> allMovies = dataManager.getMovies();
        
        // Normalize the search genre for case-insensitive comparison
        String normalizedGenre = genre.trim().toLowerCase();
        
        for (Movie movie : allMovies.values()) {
            // Check if any of the movie's genres match the search term
            for (String movieGenre : movie.getGenres()) {
                if (movieGenre.toLowerCase().equals(normalizedGenre)) {
                    results.add(movie);
                    break;
                }
            }
        }
        
        return results;
    }

    /**
     * Displays the search results in a formatted manner.
     * Handles both cases: results found and no results found.
     * @param genre The genre that was searched for
     * @param results List of movies found
     */
    public void displaySearchResults(String genre, List<Movie> results) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("Search Results for Genre: \"" + genre + "\"");
        System.out.println("=".repeat(50));
        
        if (results.isEmpty()) {
            System.out.println("No movies found for the genre: " + genre);
            System.out.println("Please try a different genre or check the spelling.");
        } else {
            System.out.println("Found " + results.size() + " movie(s):\n");
            
            for (int i = 0; i < results.size(); i++) {
                Movie movie = results.get(i);
                System.out.println("--- Movie " + (i + 1) + " ---");
                System.out.println("ID: " + movie.getId());
                System.out.println("Title: " + movie.getTitle());
                System.out.println("Director: " + movie.getDirector());
                System.out.println("Year: " + movie.getYear());
                System.out.println("Genres: " + String.join(", ", movie.getGenres()));
                System.out.println("Average Rating: " + movie.getAvgRating());
                System.out.println("Rating Count: " + movie.getNumRatings());
                System.out.println();
            }
        }
        
        System.out.println("=".repeat(50));
    }
}
