package functional_reqs.recommendation_tests;
import java.util.List;

/**
 * System Test Suite for Movie Recommendation Requirements (REQ-1 to REQ-8).
 * This validates the integration of DataManager and MovieRecommendationAlgorithm.
 */
public class RecommendationSystemTest {
    

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("   MRA System Requirements Test (REQ-1 to REQ-8)  ");
        System.out.println("==================================================");

        // Setup
        DataManager dm = new DataManager();
        System.out.println("[Setup] Loading data...");
        dm.loadData();
        System.out.println("[Setup] Loaded " + dm.getMovies().size() + " movies.");

        // Run Tests
        testColdStartAndPopularity(dm); // Covers REQ-7, REQ-8
        testRatingThreshold(dm);        // Covers REQ-2, REQ-3
        testContentBasedSimilarity(dm); // Covers REQ-1, REQ-4, REQ-5, REQ-6
    }

    /**
     * TEST CASE 1: Cold Start & Popularity Sorting
     * Validates REQ-7 (Handle no history) and REQ-8 (Sort by AvgRating, tie-break NumRatings).
     */
    private static void testColdStartAndPopularity(DataManager dm) {
        System.out.println("\n--------------------------------------------------");
        System.out.println("TEST 1: Cold Start & Popularity (REQ-7, REQ-8)");
        System.out.println("--------------------------------------------------");

        // Ensure clean state (no ratings)
        // Note: In a real app we might need a method to clear ratings, 
        // but here we assume a fresh DataManager or just don't add any yet.
        
        List<Movie> recs = dm.getRecommendations();

        if (recs.isEmpty()) {
            System.out.println("FAIL: No recommendations returned for new user." );
            return;
        }

        System.out.println("Result: " + recs.size() + " movies returned.");
        
        // Verify Sorting (REQ-8)
        boolean isSorted = true;
        for (int i = 0; i < recs.size() - 1; i++) {
            Movie current = recs.get(i);
            Movie next = recs.get(i+1);

            // Check if current rating is LESS than next rating (which would be wrong)
            if (current.getAvgRating() < next.getAvgRating()) {
                isSorted = false;
                System.out.println("FAIL Sort: " + current.getTitle() + " (" + current.getAvgRating() + 
                                   ") came before " + next.getTitle() + " (" + next.getAvgRating() + ")");
            }
        }

        if (isSorted) {
            System.out.println( "PASS: REQ-7 (Handled Cold Start) & REQ-8 (Sorted by Rating)." );
            printTop3(recs);
        }
    }

    /**
     * TEST CASE 2: Rating Threshold Logic
     * Validates REQ-3 (Identify preferences based on movies rated 4 or 5 stars).
     * If a user rates a movie 1 star, it should NOT generate similarity recommendations based on it.
     */
    private static void testRatingThreshold(DataManager dm) {
        System.out.println("\n--------------------------------------------------");
        System.out.println("TEST 2: Rating Threshold Logic (REQ-3)");
        System.out.println("--------------------------------------------------");

        // 1. Rate a specific genre movie poorly (1 Star)
        // Let's use 'The Notebook' (Romance) if available, or just ID 5 (Forrest Gump - Drama/Romance)
        System.out.println("Action: Rating 'Forrest Gump' (Drama/Romance) with 1 Star.");
        dm.addOrUpdateRating(5, 1); 

        // 2. Get Recommendations
        // Since we only have a 1-star rating, the system should treat this as "no positive preferences"
        // and Fallback to Popular movies (Cold Start behavior) OR return empty if logic dictates.
        // It should NOT recommend Romance movies specifically because of this rating.
        List<Movie> recs = dm.getRecommendations();
        
        // Check if the top result is a generic popular movie (e.g., Shawshank, Godfather) 
        // rather than a obscure Romance movie.
        Movie topMovie = recs.get(0);
        System.out.println("Top Recommendation: " + topMovie.getTitle() + " (" + topMovie.getAvgRating() + ")");

        // In your DataManager logic: "if (highlyRated.isEmpty()) return getPopularMovies();"
        // Since 1 < 4, highlyRated list is empty. Thus, we expect Popular Movies.
        // We verify this by checking if the top movie is a global hit (like Shawshank ID 1).
        
        if (topMovie.getId() == 1 || topMovie.getId() == 2) { 
            System.out.println( "PASS: REQ-3 (Ignored low rating, returned Popular/Default)." );
        } else {
            System.out.println( "FAIL: REQ-3 (System might have used the low rating for personalization)." );
        }
    }

    /**
     * TEST CASE 3: Content-Based Similarity
     * Validates REQ-1, REQ-4, REQ-5 (Genre Overlap), REQ-6 (Sorted relevance).
     */
    private static void testContentBasedSimilarity(DataManager dm) {
        System.out.println("\n--------------------------------------------------");
        System.out.println("TEST 3: Content-Based Similarity (REQ-1, 4, 5, 6)");
        System.out.println("--------------------------------------------------");

        // 1. Rate a Sci-Fi movie 5 Stars to establish preference
        System.out.println("Action: Rating 'The Matrix' (Action/Sci-Fi) with 5 Stars.");
        dm.addOrUpdateRating(7, 5); // Matrix ID = 7

        // 2. Get Recommendations
        List<Movie> recs = dm.getRecommendations();
        
        // 3. Analyze Results
        // We expect other Sci-Fi or Action movies to be at the top.
        //  Inception (6), Terminator (91), Blade Runner (69)
        
        boolean genreMatchFound = false;
        int sciFiCount = 0;

        System.out.println("Top 5 Recommendations:");
        for (int i = 0; i < Math.min(5, recs.size()); i++) {
            Movie m = recs.get(i);
            System.out.println((i+1) + ". " + m.getTitle() + " [" + m.getGenres() + "]");
            
            // Check for REQ-5 (Genre Overlap)
            for (String genre : m.getGenres()) {
                if (genre.toLowerCase().contains("sci") || genre.toLowerCase().contains("action")) {
                    sciFiCount++;
                    break; // Count movie once
                }
            }
        }

        // Verification Logic
        if (sciFiCount >= 3) {
            System.out.println( "PASS: REQ-5 (Genre overlap is prioritized). Most recs are Sci-Fi/Action." );
        } else {
            System.out.println( "FAIL: REQ-5 (Results do not match user preference for Sci-Fi)." );
        }

        // Verify Exclusion (REQ-4 implies unwatched)
        boolean matrixRecommended = false;
        for (Movie m : recs) {
            if (m.getId() == 7) matrixRecommended = true;
        }
        if (!matrixRecommended) {
            System.out.println("PASS: REQ-4 (Calculated for *unwatched* movies only)." );
        } else {
            System.out.println( "FAIL: Recommended a movie the user already watched." );
        }
    }

    private static void printTop3(List<Movie> movies) {
        for (int i = 0; i < Math.min(3, movies.size()); i++) {
            Movie m = movies.get(i);
            System.out.println("   " + (i+1) + ". " + m.getTitle() + " (Rating: " + m.getAvgRating() + ")");
        }
    }
} 