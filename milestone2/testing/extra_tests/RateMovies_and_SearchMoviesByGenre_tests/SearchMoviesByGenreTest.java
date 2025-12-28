import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Unit tests for the SearchMoviesByGenre class.
 * Tests genre search logic, invalid/no results handling, and output formatting.
 */
public class SearchMoviesByGenreTest {

    private DataManager dataManager;
    private SearchMoviesByGenre searchMovies;
    private Scanner scanner;

    /**
     * Setup test environment with sample movies.
     */
    public void setUp() {
        dataManager = new DataManager();
        populateTestData();
        scanner = new Scanner("");
        searchMovies = new SearchMoviesByGenre(dataManager, scanner);
    }

    /**
     * Populate DataManager with test movies.
     */
    private void populateTestData() {
        // Add test movies manually using reflection (since DataManager maps are private)
        Map<Integer, Movie> movies = dataManager.getMovies();
        
        List<String> actionGenres = new ArrayList<>();
        actionGenres.add("Action");
        actionGenres.add("Sci-Fi");
        
        List<String> dramaGenres = new ArrayList<>();
        dramaGenres.add("Drama");
        dramaGenres.add("Romance");
        
        List<String> comedyGenres = new ArrayList<>();
        comedyGenres.add("Comedy");
        
        List<String> horrorGenres = new ArrayList<>();
        horrorGenres.add("Horror");
        horrorGenres.add("Thriller");
        
        movies.put(1, new Movie(1, "The Matrix", "Lana Wachowski", 1999, actionGenres, 8.7, 1500000));
        movies.put(2, new Movie(2, "Inception", "Christopher Nolan", 2010, actionGenres, 8.8, 2000000));
        movies.put(3, new Movie(3, "The Shawshank Redemption", "Frank Darabont", 1994, dramaGenres, 9.3, 2500000));
        movies.put(4, new Movie(4, "Forrest Gump", "Robert Zemeckis", 1994, dramaGenres, 8.8, 2000000));
        movies.put(5, new Movie(5, "Monty Python and the Holy Grail", "Terry Gilliam", 1975, comedyGenres, 8.3, 500000));
    }

    /**
     * Test 1: Successfully find movies with a valid genre.
     */
    public void testFindMoviesByValidGenre() {
        setUp();
        List<Movie> results = searchMovies.findMoviesByGenre("Action");
        
        assert results.size() == 2 : "Expected 2 Action movies, got " + results.size();
        assert results.get(0).getTitle().equals("The Matrix") : "Expected 'The Matrix'";
        System.out.println("✓ Test 1 passed: Found valid genre movies");
    }

    /**
     * Test 2: Handle search with no matching results.
     */
    public void testNoMoviesFoundForGenre() {
        setUp();
        List<Movie> results = searchMovies.findMoviesByGenre("Fantasy");
        
        assert results.isEmpty() : "Expected empty list for non-existent genre";
        System.out.println("✓ Test 2 passed: No results handling works");
    }

    /**
     * Test 3: Test case-insensitive search.
     */
    public void testCaseInsensitiveSearch() {
        setUp();
        
        List<Movie> resultsLower = searchMovies.findMoviesByGenre("comedy");
        List<Movie> resultsUpper = searchMovies.findMoviesByGenre("COMEDY");
        List<Movie> resultsMixed = searchMovies.findMoviesByGenre("CoMeDy");
        
        assert resultsLower.size() == 1 : "Lowercase search failed";
        assert resultsUpper.size() == 1 : "Uppercase search failed";
        assert resultsMixed.size() == 1 : "Mixed case search failed";
        System.out.println("✓ Test 3 passed: Case-insensitive search works");
    }

    /**
     * Test 4: Test genre with multiple movies.
     */
    public void testGenreWithMultipleMovies() {
        setUp();
        List<Movie> dramaMovies = searchMovies.findMoviesByGenre("Drama");
        
        assert dramaMovies.size() == 2 : "Expected 2 Drama movies, got " + dramaMovies.size();
        assert dramaMovies.get(0).getTitle().equals("The Shawshank Redemption");
        assert dramaMovies.get(1).getTitle().equals("Forrest Gump");
        System.out.println("✓ Test 4 passed: Multiple movies for same genre found correctly");
    }

    /**
     * Test 5: Test with empty genre string.
     */
    public void testEmptyGenreString() {
        setUp();
        List<Movie> results = searchMovies.findMoviesByGenre("");
        
        assert results.isEmpty() : "Expected empty list for empty genre string";
        System.out.println("✓ Test 5 passed: Empty genre string handled correctly");
    }

    /**
     * Test 6: Test with whitespace-only genre string.
     */
    public void testWhitespaceGenreString() {
        setUp();
        List<Movie> results = searchMovies.findMoviesByGenre("   ");
        
        assert results.isEmpty() : "Expected empty list for whitespace-only genre string";
        System.out.println("✓ Test 6 passed: Whitespace-only genre string handled correctly");
    }

    /**
     * Test 7: Test partial genre name does not match.
     */
    public void testPartialGenreNameNoMatch() {
        setUp();
        List<Movie> results = searchMovies.findMoviesByGenre("Act");
        
        assert results.isEmpty() : "Partial genre name should not match";
        System.out.println("✓ Test 7 passed: Partial genre matching works correctly");
    }

    /**
     * Test 8: Test with genre that has leading/trailing spaces.
     */
    public void testGenreWithSpaces() {
        setUp();
        List<Movie> results = searchMovies.findMoviesByGenre("  Horror  ");
        
        assert results.size() == 1 : "Expected 1 Horror movie after trimming spaces";
        System.out.println("✓ Test 8 passed: Genre with leading/trailing spaces handled");
    }

    /**
     * Test 9: Verify all returned movies contain the searched genre.
     */
    public void testAllResultsContainSearchedGenre() {
        setUp();
        List<Movie> results = searchMovies.findMoviesByGenre("Sci-Fi");
        
        for (Movie movie : results) {
            boolean hasGenre = movie.getGenres().stream()
                    .anyMatch(g -> g.toLowerCase().equals("sci-fi"));
            assert hasGenre : "Movie " + movie.getTitle() + " doesn't contain Sci-Fi genre";
        }
        System.out.println("✓ Test 9 passed: All results contain searched genre");
    }

    /**
     * Test 10: Test with special characters in genre search.
     */
    public void testSpecialCharactersInGenre() {
        setUp();
        List<Movie> results = searchMovies.findMoviesByGenre("Sci-Fi");
        
        assert results.size() == 2 : "Sci-Fi genre with hyphen should work";
        System.out.println("✓ Test 10 passed: Special characters in genre name work");
    }

    /**
     * Run all tests.
     */
    public static void main(String[] args) {
        SearchMoviesByGenreTest testSuite = new SearchMoviesByGenreTest();
        
        System.out.println("=".repeat(50));
        System.out.println("Running SearchMoviesByGenre Unit Tests");
        System.out.println("=".repeat(50) + "\n");
        
        try {
            testSuite.testFindMoviesByValidGenre();
            testSuite.testNoMoviesFoundForGenre();
            testSuite.testCaseInsensitiveSearch();
            testSuite.testGenreWithMultipleMovies();
            testSuite.testEmptyGenreString();
            testSuite.testWhitespaceGenreString();
            testSuite.testPartialGenreNameNoMatch();
            testSuite.testGenreWithSpaces();
            testSuite.testAllResultsContainSearchedGenre();
            testSuite.testSpecialCharactersInGenre();
            
            System.out.println("\n" + "=".repeat(50));
            System.out.println("All SearchMoviesByGenre tests passed! ✓");
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
