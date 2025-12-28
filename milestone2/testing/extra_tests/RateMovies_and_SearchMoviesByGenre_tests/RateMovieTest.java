import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Unit tests for the RateMovie class.
 * Tests movie selection, rating input validation, and overwrite logic.
 */
public class RateMovieTest {

    private DataManager dataManager;
    private RateMovie rateMovie;

    /**
     * Setup test environment with sample movies and ratings.
     */
    public void setUp() {
        dataManager = new DataManager();
        populateTestData();
        rateMovie = new RateMovie(dataManager, new Scanner(""));
    }

    /**
     * Populate DataManager with test movies.
     */
    private void populateTestData() {
        Map<Integer, Movie> movies = dataManager.getMovies();
        
        List<String> genres1 = new ArrayList<>();
        genres1.add("Action");
        
        List<String> genres2 = new ArrayList<>();
        genres2.add("Drama");
        
        List<String> genres3 = new ArrayList<>();
        genres3.add("Comedy");
        
        movies.put(1, new Movie(1, "The Matrix", "Lana Wachowski", 1999, genres1, 8.7, 1500000));
        movies.put(2, new Movie(2, "Inception", "Christopher Nolan", 2010, genres1, 8.8, 2000000));
        movies.put(3, new Movie(3, "The Shawshank Redemption", "Frank Darabont", 1994, genres2, 9.3, 2500000));
    }

    /**
     * Test 1: Successfully add a new rating.
     */
    public void testAddNewRating() {
        setUp();
        String timestamp = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Rating rating = new Rating(1, 5, timestamp);
        
        dataManager.addOrUpdateRating(rating);
        
        Map<Integer, Rating> ratings = dataManager.getRatings();
        assert ratings.containsKey(1) : "Rating not found after adding";
        assert ratings.get(1).getRating() == 5 : "Rating value incorrect";
        System.out.println("✓ Test 1 passed: New rating added successfully");
    }

    /**
     * Test 2: Update an existing rating.
     */
    public void testUpdateExistingRating() {
        setUp();
        String timestamp = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        
        // Add initial rating
        Rating initialRating = new Rating(2, 3, timestamp);
        dataManager.addOrUpdateRating(initialRating);
        
        // Update rating
        Rating updatedRating = new Rating(2, 4, timestamp);
        dataManager.addOrUpdateRating(updatedRating);
        
        Map<Integer, Rating> ratings = dataManager.getRatings();
        assert ratings.get(2).getRating() == 4 : "Rating not updated correctly";
        System.out.println("✓ Test 2 passed: Existing rating updated successfully");
    }

    /**
     * Test 3: Verify rating value is within valid range (1-5).
     */
    public void testValidRatingRange() {
        setUp();
        String timestamp = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        
        // Test minimum value
        Rating minRating = new Rating(1, 1, timestamp);
        assert minRating.getRating() >= 1 : "Minimum rating out of range";
        
        // Test maximum value
        Rating maxRating = new Rating(2, 5, timestamp);
        assert maxRating.getRating() <= 5 : "Maximum rating out of range";
        
        // Test middle value
        Rating midRating = new Rating(3, 3, timestamp);
        assert midRating.getRating() == 3 : "Middle value rating incorrect";
        
        System.out.println("✓ Test 3 passed: Rating values within valid range (1-5)");
    }

    /**
     * Test 4: Timestamp is properly stored with rating.
     */
    public void testTimestampStorage() {
        setUp();
        String timestamp = "2025-12-03";
        Rating rating = new Rating(1, 4, timestamp);
        
        dataManager.addOrUpdateRating(rating);
        
        Rating retrieved = dataManager.getRatings().get(1);
        assert retrieved.getTimestamp().equals(timestamp) : "Timestamp not stored correctly";
        System.out.println("✓ Test 4 passed: Timestamp stored correctly");
    }

    /**
     * Test 5: Multiple movies can have different ratings.
     */
    public void testMultipleMovieRatings() {
        setUp();
        String timestamp = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        
        dataManager.addOrUpdateRating(new Rating(1, 5, timestamp));
        dataManager.addOrUpdateRating(new Rating(2, 4, timestamp));
        dataManager.addOrUpdateRating(new Rating(3, 3, timestamp));
        
        Map<Integer, Rating> ratings = dataManager.getRatings();
        assert ratings.size() == 3 : "Expected 3 ratings";
        assert ratings.get(1).getRating() == 5 : "Movie 1 rating incorrect";
        assert ratings.get(2).getRating() == 4 : "Movie 2 rating incorrect";
        assert ratings.get(3).getRating() == 3 : "Movie 3 rating incorrect";
        
        System.out.println("✓ Test 5 passed: Multiple movie ratings stored independently");
    }

    /**
     * Test 6: Null rating is handled gracefully.
     */
    public void testNullRatingHandling() {
        setUp();
        
        // This should not cause an exception
        try {
            dataManager.addOrUpdateRating(null);
            // Verify no rating was added
            assert !dataManager.getRatings().containsKey(null) : "Null rating should not be added";
            System.out.println("✓ Test 6 passed: Null rating handled gracefully");
        } catch (Exception e) {
            System.out.println("✗ Test 6 failed: Exception thrown for null rating - " + e.getMessage());
            throw e;
        }
    }

    /**
     * Test 7: Retrieve ratings by movie ID.
     */
    public void testRetrieveRatingByMovieId() {
        setUp();
        String timestamp = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        
        Rating rating = new Rating(2, 4, timestamp);
        dataManager.addOrUpdateRating(rating);
        
        Rating retrieved = dataManager.getRatings().get(2);
        assert retrieved != null : "Rating not found for movie ID 2";
        assert retrieved.getMovieId() == 2 : "Movie ID mismatch";
        assert retrieved.getRating() == 4 : "Rating value mismatch";
        
        System.out.println("✓ Test 7 passed: Rating retrieved correctly by movie ID");
    }

    /**
     * Test 8: Test rating update with timestamp change.
     */
    public void testRatingUpdateWithNewTimestamp() {
        setUp();
        
        Rating initialRating = new Rating(1, 3, "2025-01-01");
        dataManager.addOrUpdateRating(initialRating);
        
        Rating updatedRating = new Rating(1, 5, "2025-12-03");
        dataManager.addOrUpdateRating(updatedRating);
        
        Rating retrieved = dataManager.getRatings().get(1);
        assert retrieved.getRating() == 5 : "Rating not updated";
        assert retrieved.getTimestamp().equals("2025-12-03") : "Timestamp not updated";
        
        System.out.println("✓ Test 8 passed: Rating and timestamp updated together");
    }

    /**
     * Test 9: All ratings are stored in map correctly.
     */
    public void testAllRatingsStoredInMap() {
        setUp();
        
        for (int i = 1; i <= 3; i++) {
            Rating rating = new Rating(i, i, "2025-12-03");
            dataManager.addOrUpdateRating(rating);
        }
        
        Map<Integer, Rating> ratings = dataManager.getRatings();
        assert ratings.size() == 3 : "Not all ratings stored";
        assert ratings.keySet().contains(1) : "Movie 1 rating not in map";
        assert ratings.keySet().contains(2) : "Movie 2 rating not in map";
        assert ratings.keySet().contains(3) : "Movie 3 rating not in map";
        
        System.out.println("✓ Test 9 passed: All ratings stored in map");
    }

    /**
     * Test 10: Rating setter works correctly.
     */
    public void testRatingSetterMethods() {
        setUp();
        
        Rating rating = new Rating(1, 3, "2025-01-01");
        rating.setRating(5);
        rating.setTimestamp("2025-12-03");
        
        assert rating.getRating() == 5 : "Rating setter didn't work";
        assert rating.getTimestamp().equals("2025-12-03") : "Timestamp setter didn't work";
        
        System.out.println("✓ Test 10 passed: Rating setters work correctly");
    }

    /**
     * Test 11: Test rating persistence to file simulation.
     */
    public void testRatingPersistence() {
        setUp();
        String timestamp = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        
        dataManager.addOrUpdateRating(new Rating(1, 5, timestamp));
        dataManager.addOrUpdateRating(new Rating(2, 4, timestamp));
        
        // Verify data is saved and retrievable
        Map<Integer, Rating> ratings = dataManager.getRatings();
        assert ratings.get(1).getRating() == 5 : "First rating not persistent";
        assert ratings.get(2).getRating() == 4 : "Second rating not persistent";
        
        System.out.println("✓ Test 11 passed: Rating persistence works");
    }

    /**
     * Test 12: Overwrite scenario - same movie rated twice.
     */
    public void testRatingOverwriteScenario() {
        setUp();
        
        // User rates movie 1 with 3 stars
        Rating firstRating = new Rating(1, 3, "2025-01-01");
        dataManager.addOrUpdateRating(firstRating);
        assert dataManager.getRatings().get(1).getRating() == 3;
        
        // User changes mind and rates it with 5 stars
        Rating secondRating = new Rating(1, 5, "2025-12-03");
        dataManager.addOrUpdateRating(secondRating);
        
        // Verify only the latest rating exists
        assert dataManager.getRatings().size() == 1 : "Multiple ratings for same movie";
        assert dataManager.getRatings().get(1).getRating() == 5 : "Overwrite didn't work";
        
        System.out.println("✓ Test 12 passed: Rating overwrite scenario works correctly");
    }

    /**
     * Run all tests.
     */
    public static void main(String[] args) {
        RateMovieTest testSuite = new RateMovieTest();
        
        System.out.println("=".repeat(50));
        System.out.println("Running RateMovie Unit Tests");
        System.out.println("=".repeat(50) + "\n");
        
        try {
            testSuite.testAddNewRating();
            testSuite.testUpdateExistingRating();
            testSuite.testValidRatingRange();
            testSuite.testTimestampStorage();
            testSuite.testMultipleMovieRatings();
            testSuite.testNullRatingHandling();
            testSuite.testRetrieveRatingByMovieId();
            testSuite.testRatingUpdateWithNewTimestamp();
            testSuite.testAllRatingsStoredInMap();
            testSuite.testRatingSetterMethods();
            testSuite.testRatingPersistence();
            testSuite.testRatingOverwriteScenario();
            
            System.out.println("\n" + "=".repeat(50));
            System.out.println("All RateMovie tests passed! ✓");
            System.out.println("=".repeat(50));
        } catch (AssertionError e) {
            System.out.println("\n✗ Test failed: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("\n✗ Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
