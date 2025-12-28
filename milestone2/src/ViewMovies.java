import java.util.Map;

public class ViewMovies {

    private final DataManager dataManager;

    public ViewMovies(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    /**
     * Displays the full movie list to the user.
     */
    public void displayAllMovies() {
        Map<Integer, Movie> movies = dataManager.getMovies();

        if (movies.isEmpty()) {
            System.out.println("No movies found in the list.");
            return;
        }

        System.out.println("\n--- MOVIE LIST ---");

        for (Movie m : movies.values()) {
            System.out.println("----------------------------------------");
            System.out.println("ID: " + m.getId());
            System.out.println("Title: " + m.getTitle());
            System.out.println("Director: " + m.getDirector());
            System.out.println("Year: " + m.getYear());
            System.out.println("Genres: " + String.join(", ", m.getGenres()));
            System.out.println("Average Rating: " + m.getAvgRating());
            System.out.println("Rating Count: " + m.getNumRatings());
        }

        System.out.println("----------------------------------------");
        System.out.println("End of movie list.\n");
    }
}