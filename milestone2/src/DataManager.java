import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Comparator;


/**
 * Manages all core logic including data parsing, memory management of the movie catalog,
 * persistence to file, and the recommendation algorithm.
 */
public class DataManager {
    private Map<Integer, Movie> movies;
    private Map<Integer, Rating> ratings;
    private MovieRecommendationAlgorithm recommendationAlgorithm = new MovieRecommendationAlgorithm();

    /**
     * Constructor for DataManager.
     */
    public DataManager() {
        this.movies = new HashMap<>();
        this.ratings = new HashMap<>();
    }

    /**
     * Reads and parses movies.txt and my_ratings.txt and adds them to data maps.
     * Opens files using standard Java I/O libraries. Reads data line by line, creates Movie
     * and Rating objects, and inserts them into their places. Handles file errors
     * by logging and skipping broken lines, through try/catch methods, instead of breaking the program.
     */
    public void loadData() {
        loadMovies();
        loadRatings();
    }

    /**
     * Loads movies from movies.txt file.
     */
    private void loadMovies() {
        String filePath = "data/movies.txt";
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = reader.readLine()) != null) {
                // Skip header line
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                Movie movie = parseMovieLine(line);
                if (movie != null) {
                    movies.put(movie.getId(), movie);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading movies.txt: " + e.getMessage());
        }
    }

    /**
     * Loads ratings from my_ratings.txt file.
     * If the file does not exist, it is skipped.
     */
    private void loadRatings() {
        String filePath = "data/my_ratings.txt";
        Path path = Paths.get(filePath);
        
        // Check if file exists: if not, skip loading, meaning don't create it.
        if (!Files.exists(path)) {
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = reader.readLine()) != null) {
                // Skip header line
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                Rating rating = parseRatingLine(line);
                if (rating != null) {
                    ratings.put(rating.getMovieId(), rating);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading my_ratings.txt: " + e.getMessage());
        }
    }

    /**
     * Parses a single line from movies.txt and creates a Movie object.
     * @param line The line needed to parse
     * @return Movie object if parsing succeeds, null otherwise
     */
    private Movie parseMovieLine(String line) {
        try {
            String[] parts = line.split(",");
            
            // Expected format: id,title,director,year,genres,avgRating,numRatings
            // File format: movie_id,title,director,year,genres,avg_rating,num_ratings
            // Need at least 7 parts
            if (parts.length < 7) {
                System.err.println("Malformed movie entry: " + line);
                return null;
            }
            
            int id = Integer.parseInt(parts[0].trim());
            String title = parts[1].trim();
            String director = parts[2].trim();
            int year = Integer.parseInt(parts[3].trim());
            
            // Parse genres (semicolon-separated)
            String genresStr = parts[4].trim();
            List<String> genres = new ArrayList<>();
            if (!genresStr.isEmpty()) {
                String[] genreArray = genresStr.split(";");
                for (String genre : genreArray) {
                    genres.add(genre.trim());
                }
            }
            
            double avgRating = Double.parseDouble(parts[5].trim());
            int numRatings = Integer.parseInt(parts[6].trim());
            
            return new Movie(id, title, director, year, genres, avgRating, numRatings);
            
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Malformed movie entry: " + line);
            return null;
        }
    }

    /**
     * Parses a single line from my_ratings.txt and creates a Rating object.
     * @param line The line to parse
     * @return Rating object if parsing succeeds, null otherwise
     */
    private Rating parseRatingLine(String line) {
        try {
            String[] parts = line.split(",");
            
            // Expected format: movieId,rating,timestamp
            // File format: movie_id,rating,timestamp
            // Need exactly 3 parts
            if (parts.length != 3) {
                System.err.println("Malformed rating entry: " + line);
                return null;
            }
            
            int movieId = Integer.parseInt(parts[0].trim());
            int rating = Integer.parseInt(parts[1].trim());
            String timestamp = parts[2].trim();
            
            // Ensure the right rating range (1-5)
            if (rating < 1 || rating > 5) {
                System.err.println("Malformed rating entry: " + line);
                return null;
            }
            
            // Ensure the right format (YYYY-MM-DD)
            if (!timestamp.matches("\\d{4}-\\d{2}-\\d{2}")) {
                System.err.println("Malformed rating entry: " + line);
                return null;
            }
            
            return new Rating(movieId, rating, timestamp);
            
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Malformed rating entry: " + line);
            return null;
        }
    }

    /**
     * MRA_1.3: Gets popular movies for cold-start scenarios.
     * REQ-8: Sorts by Avg Rating, then Num Ratings.
     */
    private List<Movie> getPopularMovies() {
        // Create a copy of the list so we don't mess up the original map order
        List<Movie> popularMovies = new ArrayList<>(movies.values());
        
        popularMovies.sort(new Comparator<Movie>() {
            @Override
            public int compare(Movie m1, Movie m2) {
                int ratingCompare = Double.compare(m2.getAvgRating(), m1.getAvgRating());
                
                if (ratingCompare != 0) {
                    return ratingCompare;
                }

                // Tie-Breaker
                return Integer.compare(m2.getNumRatings(), m1.getNumRatings());
            }
        });
        
        // Return top 10 (or fewer if we don't have 10 movies yet)
        return popularMovies.subList(0, Math.min(10, popularMovies.size()));
    }


    // Getters for maps (for future use)
    public Map<Integer, Movie> getMovies() {
        return movies;
    }

    public Map<Integer, Rating> getRatings() {
        return ratings;
    }

/**
     * Adds a new rating or updates an existing rating for a movie.
     * Called by RateMovie class after validation.
     */
    public void addOrUpdateRating(Rating rating) {
        if (rating == null) return;
        ratings.put(rating.getMovieId(), rating);
    }

    // Used by RecommendationSystemTest.java 
    // This is a helper wrapper that creates the object for the test.
    public void addOrUpdateRating(int movieId, int rating) {
        // Validate specifically for the test since there is no UI checking it here
        if (rating < 1 || rating > 5) return; 
        
        String timestamp = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ISO_DATE);
        Rating newRating = new Rating(movieId, rating, timestamp);
        addOrUpdateRating(newRating); // Calls Method 1
    }

    /**
     * Saves all ratings to the my_ratings.txt file.
     * Creates the file if it doesn't exist, overwrites if it does.
     */
    public void saveRatingsToFile() {
        String filePath = "data/my_ratings.txt";
        
        try {
            // Create parent directories if they don't exist
            Path path = Paths.get(filePath);
            Files.createDirectories(path.getParent());
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                // Write header
                writer.write("movie_id,rating,timestamp\n");
                
                // Write all ratings
                for (Rating rating : ratings.values()) {
                    writer.write(rating.getMovieId() + "," + rating.getRating() + "," + rating.getTimestamp() + "\n");
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving ratings to file: " + e.getMessage());
        }
    }


    /**
     * MRA_1.5: The main public method for generating recommendations.
     * Integrates the user's rating data with the similarity algorithm to produce a ranked list.
     * How it works:
     * Check for Cold Start (New User) -> Return Popular Movies.
     * Filter for "Reference" movies (what the user likes).
     * Filter for "Candidate" movies (what the user hasn't seen).
     * Calculate similarity scores using the recommendation algorithm.
     * Sort and return the top 10 matches. Want to have a balanced amount of output in our CLI interface.
    */
    public List<Movie> getRecommendations() {
        if (ratings.isEmpty()) return getPopularMovies();
            List<Movie> highlyRated = getHighlyRatedMovies();
        if (highlyRated.isEmpty()) return getPopularMovies();
            List<Movie> unrated = getUnratedMovies();
        if (unrated.isEmpty()) return new ArrayList<>();
            Map<Movie, Double> scores = new HashMap<>();
        for (Movie candidate : unrated) {
            double maxSimilarity = 0.0;
            for (Movie ref : highlyRated) {
                double sim = recommendationAlgorithm.calculateSimilarity(ref, candidate);
                maxSimilarity = Math.max(maxSimilarity, sim);
            }
            if (maxSimilarity > 0) scores.put(candidate, maxSimilarity);
        }
        // Sort by score and return top 10
        List<Movie> results = new ArrayList<>(scores.keySet());

        results.sort(new Comparator<Movie>() {
            @Override
            public int compare(Movie m1, Movie m2) {
                // Compare the scores map values
                return Double.compare(scores.get(m2), scores.get(m1));
            }
        });

        return results.subList(0, Math.min(10, results.size()));
    }

    // Helpers
    private List<Movie> getHighlyRatedMovies() {
        List<Movie> list = new ArrayList<>();
        for (Rating r : ratings.values()) {
            if (r.getRating() >= 4) list.add(movies.get(r.getMovieId()));
        }
        return list;
    }

    private List<Movie> getUnratedMovies() {
        List<Movie> list = new ArrayList<>();
        for (Movie m : movies.values()) {
            if (!ratings.containsKey(m.getId())) list.add(m);
        }
        return list;
    }
}