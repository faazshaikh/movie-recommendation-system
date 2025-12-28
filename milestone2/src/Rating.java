/**
 * Represents a user rating for a movie.
 */
public class Rating {
    private int movieId;
    private int rating;
    private String timestamp;

    /**
     * Constructor for Rating.
     * @param movieId Movie ID
     * @param rating Rating value (1-5)
     * @param timestamp Timestamp in YYYY-MM-DD format
     */
    public Rating(int movieId, int rating, String timestamp) {
        this.movieId = movieId;
        this.rating = rating;
        this.timestamp = timestamp;
    }

    // Getters
    public int getMovieId() {
        return movieId;
    }

    public int getRating() {
        return rating;
    }

    public String getTimestamp() {
        return timestamp;
    }

    // Setters (for future use in addOrUpdateRating)
    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

