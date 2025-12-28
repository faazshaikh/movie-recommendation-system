import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implements content-based similarity score for two movies.
 */
public class MovieRecommendationAlgorithm {
    private final double genreWeight;
    private final double ratingWeight;
    
    // Genre synonym map
    private static final Map<String, String> GENRE_SYNONYMS = initializeGenreSynonyms();

    /**
     * Default constructor that favors genre overlap as outlined in the SDD.
     */
    public MovieRecommendationAlgorithm() {
        this(0.7, 0.3);
    }

    /**
     * Constructor allowing callers to tweak how similarity is calculated.
     */
    public MovieRecommendationAlgorithm(double genreWeight, double ratingWeight) {
        double total = genreWeight + ratingWeight;
        if (total <= 0) {
            throw new IllegalArgumentException("Weights must sum to a positive value.");
        }
        this.genreWeight = genreWeight / total;
        this.ratingWeight = ratingWeight / total;
    }

    /**
     * Calculates the similarity score between two movies by combining genre overlap (content-based filtering) and rating affinity.
     */
    public double calculateSimilarity(Movie referenceMovie, Movie candidateMovie) {
        if (referenceMovie == null || candidateMovie == null) {
            throw new IllegalArgumentException("Movies cannot be null.");
        }

        double genreScore = calculateGenreSimilarity(referenceMovie.getGenres(), candidateMovie.getGenres());
        double ratingScore = calculateRatingAffinity(referenceMovie.getAvgRating(), candidateMovie.getAvgRating());

        return (genreWeight * genreScore) + (ratingWeight * ratingScore);
    }

    /**
     * Handles genre variations, synonyms, and edge cases for better matching.
     */
    private double calculateGenreSimilarity(List<String> firstGenres, List<String> secondGenres) {
        Set<String> normalizedFirst = normalizeGenres(firstGenres);
        Set<String> normalizedSecond = normalizeGenres(secondGenres);

        if (normalizedFirst.isEmpty() || normalizedSecond.isEmpty()) {
            return 0.0;
        }

        Set<String> intersection = new HashSet<>(normalizedFirst);
        intersection.retainAll(normalizedSecond);

        Set<String> union = new HashSet<>(normalizedFirst);
        union.addAll(normalizedSecond);

        // Avoid division by zero (shouldn't happen due to empty check above)
        if (union.isEmpty()) {
            return 0.0;
        }

        return (double) intersection.size() / union.size();
    }

    /**
     * Scores how close two average ratings are on the 1-5 scale.
     */
    private double calculateRatingAffinity(double firstRating, double secondRating) {
        double difference = Math.abs(firstRating - secondRating);
        return Math.max(0.0, 1.0 - (difference / 4.0));
    }

    /**
     * Normalizes genres by:
     * - Trimming whitespace
     * - Converting to lowercase
     * - Handling hyphen variations ("sci-fi" to "scifi")
     * - Mapping synonyms 
     */
    private Set<String> normalizeGenres(List<String> genres) {
        Set<String> normalized = new HashSet<>();
        if (genres == null) {
            return normalized;
        }
        
        for (String genre : genres) {
            if (genre != null) {
                String cleaned = normalizeGenreString(genre);
                if (!cleaned.isEmpty()) {
                    normalized.add(cleaned);
                }
            }
        }
        return normalized;
    }
    
    /**
     * Handles variations like "Sci-Fi" vs "Science Fiction", hyphens, spaces.
     */
    private String normalizeGenreString(String genre) {
        if (genre == null) return "";
        
        String cleaned = genre.trim().toLowerCase();
        
        // Handle the hyphen issue for Sci-Fi specifically
        if (cleaned.contains("-")) {
            cleaned = cleaned.replace("-", "");
        }
        
        // Check if this matches a known variation (synonym)
        if (GENRE_SYNONYMS.containsKey(cleaned)) {
            // Return the STANDARD version (e.g. "scifi" -> "Sci-Fi")
            return GENRE_SYNONYMS.get(cleaned);
        }
        
        return cleaned;
    }
        
    private static Map<String, String> initializeGenreSynonyms() {
        Map<String, String> map = new HashMap<>();
        
        // Handle "Sci-Fi" variations
        map.put("science fiction", "sci-fi");
        map.put("scifi", "sci-fi");
        map.put("sf", "sci-fi");
        map.put("sci fi", "sci-fi");
    
        return map;
    }
    
}

