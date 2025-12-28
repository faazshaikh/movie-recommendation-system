import java.util.List;

/**
 * Represents a movie in the recommendation system.
 */
public class Movie {
    private int id;
    private String title;
    private String director;
    private int year;
    private List<String> genres;
    private double avgRating;
    private int numRatings;

    /**
     * Constructor for Movie.
     * @param id Movie ID
     * @param title Movie title
     * @param director Director name
     * @param year Release year
     * @param genres List of genres
     * @param avgRating Average rating
     * @param numRatings Number of ratings
     */
    public Movie(int id, String title, String director, int year, List<String> genres, double avgRating, int numRatings) {
        this.id = id;
        this.title = title;
        this.director = director;
        this.year = year;
        this.genres = genres;
        this.avgRating = avgRating;
        this.numRatings = numRatings;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDirector() {
        return director;
    }

    public int getYear() {
        return year;
    }

    public List<String> getGenres() {
        return genres;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public int getNumRatings() {
        return numRatings;
    }
}

