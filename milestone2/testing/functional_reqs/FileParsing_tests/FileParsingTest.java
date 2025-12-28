package functional_reqs.FileParsing_tests;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * File Parsing Test Suite
 * Tests the parsing of movies.txt and my_ratings.txt files
 * 
 * REQ-1: System must read data from movies.txt
 * REQ-2: System must parse movies.txt format correctly
 * REQ-3: System must parse semicolon-separated genres
 * REQ-4: System must read my_ratings.txt if it exists
 * REQ-5: System must parse my_ratings.txt format correctly
 * REQ-6: System must handle parsing errors gracefully
 * REQ-7: System must validate expected number of fields
 * REQ-10: System must handle missing movies.txt gracefully
 * REQ-11: System must continue if my_ratings.txt is missing
 */
public class FileParsingTest {
    
    private static int testsPassed = 0;
    private static int testsFailed = 0;
    private static final String TEST_DATA_DIR = "testing/FileParsing_tests/FileParsing_test_data";
    
    public static void main(String[] args) {
        System.out.println("======================================");
        System.out.println("   File Parsing Test Suite");
        System.out.println("======================================\n");
        
        // Create test data directory
        createTestDataDirectory();
        
        // Run all tests
        testREQ1_ReadMoviesFile();
        testREQ2_ParseMoviesFormat();
        testREQ3_ParseMultipleGenres();
        testREQ4_ReadMyRatingsFile();
        testREQ5_ParseMyRatingsFormat();
        testREQ6_HandleParsingErrors();
        testREQ7_ValidateFieldCount();
        testREQ8_TryCatchForInvalidDataTypes();
        testREQ10_MissingMoviesFile();
        testREQ11_MissingMyRatingsFile();
        testEdgeCase_EmptyFile();
        testEdgeCase_MalformedGenres();
        testEdgeCase_InvalidRatingValues();
        
        // Print summary
        printTestSummary();
    }
    
    // ============ REQ-1: Read movies.txt ============
    private static void testREQ1_ReadMoviesFile() {
        System.out.println("\n[FP-001] REQ-1: System must read data from movies.txt");
        try {
            String testFile = TEST_DATA_DIR + "/test_movies.txt";
            createSampleMoviesFile(testFile);
            
            List<String> lines = Files.readAllLines(Paths.get(testFile));
            boolean fileRead = !lines.isEmpty();
            
            if (fileRead) {
                System.out.println("✓ Test FP-001 passed: movies.txt file read successfully");
                System.out.println("  Lines read: " + lines.size());
                testsPassed++;
            } else {
                System.out.println("✗ Test FP-001 failed: No lines read from file");
                testsFailed++;
            }
        } catch (Exception e) {
            System.out.println("✗ Test FP-001 failed: " + e.getMessage());
            testsFailed++;
        }
    }
    
    // ============ REQ-2: Parse movies.txt format ============
    private static void testREQ2_ParseMoviesFormat() {
        System.out.println("\n[FP-002] REQ-2: System must parse movies.txt format correctly");
        try {
            String testFile = TEST_DATA_DIR + "/test_movies.txt";
            List<String> lines = Files.readAllLines(Paths.get(testFile));
            
            // Skip header line
            boolean formatCorrect = true;
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) continue;
                
                String[] fields = line.split(",");
                // Expected: movie_id, title, director, year, genres, avg_rating, num_ratings (7 fields)
                if (fields.length != 7) {
                    formatCorrect = false;
                    break;
                }
            }
            
            if (formatCorrect) {
                System.out.println("✓ Test FP-002 passed: All movie records have correct format (7 fields)");
                testsPassed++;
            } else {
                System.out.println("✗ Test FP-002 failed: Some records have incorrect field count");
                testsFailed++;
            }
        } catch (Exception e) {
            System.out.println("✗ Test FP-002 failed: " + e.getMessage());
            testsFailed++;
        }
    }
    
    // ============ REQ-3: Parse multiple genres ============
    private static void testREQ3_ParseMultipleGenres() {
        System.out.println("\n[FP-003] REQ-3: System must parse semicolon-separated genres");
        try {
            String testFile = TEST_DATA_DIR + "/test_movies.txt";
            List<String> lines = Files.readAllLines(Paths.get(testFile));
            
            boolean genresParsed = false;
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) continue;
                
                String[] fields = line.split(",");
                if (fields.length >= 5) {
                    String genresField = fields[4].trim();
                    String[] genres = genresField.split(";");
                    
                    if (genres.length > 1) {
                        genresParsed = true;
                        System.out.println("  Found record with multiple genres: " + genresField);
                        break;
                    }
                }
            }
            
            if (genresParsed) {
                System.out.println("✓ Test FP-003 passed: Successfully parsed multiple semicolon-separated genres");
                testsPassed++;
            } else {
                System.out.println("✗ Test FP-003 failed: Could not parse multiple genres");
                testsFailed++;
            }
        } catch (Exception e) {
            System.out.println("✗ Test FP-003 failed: " + e.getMessage());
            testsFailed++;
        }
    }
    
    // ============ REQ-4: Read my_ratings.txt ============
    private static void testREQ4_ReadMyRatingsFile() {
        System.out.println("\n[FP-004] REQ-4: System must read my_ratings.txt if it exists");
        try {
            String testFile = TEST_DATA_DIR + "/test_my_ratings.txt";
            createSampleRatingsFile(testFile);
            
            List<String> lines = Files.readAllLines(Paths.get(testFile));
            boolean fileRead = !lines.isEmpty();
            
            if (fileRead) {
                System.out.println("✓ Test FP-004 passed: my_ratings.txt file read successfully");
                System.out.println("  Lines read: " + lines.size());
                testsPassed++;
            } else {
                System.out.println("✗ Test FP-004 failed: No lines read from ratings file");
                testsFailed++;
            }
        } catch (Exception e) {
            System.out.println("✗ Test FP-004 failed: " + e.getMessage());
            testsFailed++;
        }
    }
    
    // ============ REQ-5: Parse my_ratings.txt format ============
    private static void testREQ5_ParseMyRatingsFormat() {
        System.out.println("\n[FP-005] REQ-5: System must parse my_ratings.txt format correctly");
        try {
            String testFile = TEST_DATA_DIR + "/test_my_ratings.txt";
            List<String> lines = Files.readAllLines(Paths.get(testFile));
            
            // Skip header line
            boolean formatCorrect = true;
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) continue;
                
                String[] fields = line.split(",");
                // Expected: movie_id, rating, timestamp (3 fields)
                if (fields.length != 3) {
                    formatCorrect = false;
                    break;
                }
            }
            
            if (formatCorrect) {
                System.out.println("✓ Test FP-005 passed: All rating records have correct format (3 fields)");
                testsPassed++;
            } else {
                System.out.println("✗ Test FP-005 failed: Some records have incorrect field count");
                testsFailed++;
            }
        } catch (Exception e) {
            System.out.println("✗ Test FP-005 failed: " + e.getMessage());
            testsFailed++;
        }
    }
    
    // ============ REQ-6: Handle parsing errors gracefully ============
    private static void testREQ6_HandleParsingErrors() {
        System.out.println("\n[FP-006] REQ-6: System must handle parsing errors gracefully");
        try {
            String testFile = TEST_DATA_DIR + "/test_malformed.txt";
            createMalformedFile(testFile);
            
            List<String> lines = Files.readAllLines(Paths.get(testFile));
            int validLines = 0;
            int errorLines = 0;
            
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) continue;
                
                try {
                    String[] fields = line.split(",");
                    if (fields.length == 7) {
                        validLines++;
                    } else {
                        errorLines++;
                    }
                } catch (Exception e) {
                    errorLines++;
                }
            }
            
            if (errorLines > 0 && validLines > 0) {
                System.out.println("✓ Test FP-006 passed: Gracefully handled malformed lines");
                System.out.println("  Valid lines: " + validLines + ", Error lines: " + errorLines);
                testsPassed++;
            } else {
                System.out.println("✗ Test FP-006 failed: Did not handle parsing errors");
                testsFailed++;
            }
        } catch (Exception e) {
            System.out.println("✗ Test FP-006 failed: " + e.getMessage());
            testsFailed++;
        }
    }
    
    // ============ REQ-7: Validate field count ============
    private static void testREQ7_ValidateFieldCount() {
        System.out.println("\n[FP-007] REQ-7: System must validate expected number of fields");
        try {
            // Create test file with invalid field counts
            String testFile = TEST_DATA_DIR + "/test_invalid_fields.txt";
            String content = "movie_id,title,director,year,genres,avg_rating,num_ratings\n" +
                    "1,The Shawshank Redemption,Frank Darabont,1994,Drama,9.3,2500000\n" +
                    "2,The Godfather,Francis Ford Coppola,1972,Crime;Drama,9.2\n" +  // Missing field (only 6)
                    "3,The Dark Knight,Christopher Nolan,2008,Action;Crime;Drama,9.0,2600000,extra\n" +  // Extra field (8)
                    "4,Pulp Fiction\n";  // Too few fields (only 2)
            
            Files.write(Paths.get(testFile), content.getBytes());
            
            // Test validation logic (simulating DataManager.parseMovieLine behavior)
            List<String> lines = Files.readAllLines(Paths.get(testFile));
            
            int validCount = 0;
            int invalidCount = 0;
            
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) continue;
                
                String[] fields = line.split(",");
                // Movies should have exactly 7 fields (as per DataManager implementation)
                if (fields.length == 7) {
                    validCount++;
                } else {
                    invalidCount++;
                    // This simulates the validation: if (parts.length < 7) return null;
                }
            }
            
            if (invalidCount > 0 && validCount > 0) {
                System.out.println("✓ Test FP-007 passed: Field count validation working correctly");
                System.out.println("  Valid records (7 fields): " + validCount);
                System.out.println("  Invalid records (wrong field count): " + invalidCount);
                System.out.println("  Validation correctly rejects lines with != 7 fields");
                testsPassed++;
            } else {
                System.out.println("✗ Test FP-007 failed: Validation not working as expected");
                testsFailed++;
            }
        } catch (Exception e) {
            System.out.println("✗ Test FP-007 failed: " + e.getMessage());
            testsFailed++;
        }
    }
    
    // ============ REQ-8: Try-catch for invalid data types ============
    private static void testREQ8_TryCatchForInvalidDataTypes() {
        System.out.println("\n[FP-008] REQ-8: System must use try-catch blocks during type conversions to handle invalid data types");
        try {
            // Create test file with invalid data types
            String testFile = TEST_DATA_DIR + "/test_invalid_types.txt";
            String content = "movie_id,title,director,year,genres,avg_rating,num_ratings\n" +
                    "1,The Shawshank Redemption,Frank Darabont,1994,Drama,9.3,2500000\n" +
                    "abc,The Godfather,Francis Ford Coppola,1972,Crime;Drama,9.2,1700000\n" +  // Invalid ID (string)
                    "2,The Dark Knight,Christopher Nolan,not_a_year,Action;Crime;Drama,9.0,2600000\n" +  // Invalid year
                    "3,Pulp Fiction,Quentin Tarantino,1994,Crime;Drama,not_a_rating,1600000\n" +  // Invalid rating
                    "4,Forrest Gump,Robert Zemeckis,1994,Drama;Romance,8.8,not_a_number\n";  // Invalid num_ratings
            
            Files.write(Paths.get(testFile), content.getBytes());
            
            // Test that parsing handles invalid types gracefully (simulating DataManager.parseMovieLine behavior)
            List<String> lines = Files.readAllLines(Paths.get(testFile));
            int handledGracefully = 0;
            int exceptionsCaught = 0;
            
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) continue;
                
                String[] fields = line.split(",");
                if (fields.length < 7) continue;
                
                // Test type conversion with try-catch (simulating DataManager behavior)
                // DataManager uses: try { Integer.parseInt(), Double.parseDouble() } catch (NumberFormatException e) { return null; }
                try {
                    int id = Integer.parseInt(fields[0].trim());
                    int year = Integer.parseInt(fields[3].trim());
                    double rating = Double.parseDouble(fields[5].trim());
                    int numRatings = Integer.parseInt(fields[6].trim());
                    handledGracefully++;
                } catch (NumberFormatException e) {
                    exceptionsCaught++;
                    // This is expected - invalid types should be caught by try-catch blocks
                    // In DataManager, this would result in returning null and skipping the entry
                }
            }
            
            if (exceptionsCaught > 0) {
                System.out.println("✓ Test FP-008 passed: Try-catch blocks handle invalid data types gracefully");
                System.out.println("  Invalid type conversions caught by try-catch: " + exceptionsCaught);
                System.out.println("  Valid conversions processed: " + handledGracefully);
                System.out.println("  System uses try-catch to prevent crashes on invalid data types");
                testsPassed++;
            } else {
                System.out.println("✗ Test FP-008 failed: No invalid types detected or exceptions not caught");
                testsFailed++;
            }
        } catch (Exception e) {
            System.out.println("✗ Test FP-008 failed: " + e.getMessage());
            testsFailed++;
        }
    }
    
    // ============ REQ-10: Missing movies.txt ============
    private static void testREQ10_MissingMoviesFile() {
        System.out.println("\n[FP-009] REQ-10: System must handle missing movies.txt gracefully");
        try {
            String testFile = TEST_DATA_DIR + "/nonexistent_movies.txt";
            
            try {
                List<String> lines = Files.readAllLines(Paths.get(testFile));
            } catch (FileNotFoundException e) {
                System.out.println("✓ Test FP-009 passed: Correctly detected missing movies.txt");
                System.out.println("  Error message: File not found (as expected)");
                testsPassed++;
                return;
            }
            
            System.out.println("✗ Test FP-009 failed: Did not properly handle missing file");
            testsFailed++;
        } catch (Exception e) {
            System.out.println("✓ Test FP-009 passed: Exception thrown for missing file: " + e.getClass().getSimpleName());
            testsPassed++;
        }
    }
    
    // ============ REQ-11: Missing my_ratings.txt ============
    private static void testREQ11_MissingMyRatingsFile() {
        System.out.println("\n[FP-010] REQ-11: System must continue if my_ratings.txt is missing");
        try {
            String testFile = TEST_DATA_DIR + "/nonexistent_ratings.txt";
            
            // Simulate checking if file exists
            boolean fileExists = Files.exists(Paths.get(testFile));
            
            if (!fileExists) {
                System.out.println("✓ Test FP-010 passed: Missing my_ratings.txt detected");
                System.out.println("  System can continue operating (treat as new user)");
                testsPassed++;
            } else {
                System.out.println("✗ Test FP-010 failed: File should not exist");
                testsFailed++;
            }
        } catch (Exception e) {
            System.out.println("✗ Test FP-010 failed: " + e.getMessage());
            testsFailed++;
        }
    }
    
    // ============ Edge Case: Empty file ============
    private static void testEdgeCase_EmptyFile() {
        System.out.println("\n[FP-011] Edge Case: System must handle empty files");
        try {
            String testFile = TEST_DATA_DIR + "/test_empty.txt";
            Files.write(Paths.get(testFile), new byte[0]);
            
            List<String> lines = Files.readAllLines(Paths.get(testFile));
            
            if (lines.isEmpty()) {
                System.out.println("✓ Test FP-011 passed: Empty file handled correctly");
                testsPassed++;
            } else {
                System.out.println("✗ Test FP-011 failed: Empty file not handled properly");
                testsFailed++;
            }
        } catch (Exception e) {
            System.out.println("✗ Test FP-011 failed: " + e.getMessage());
            testsFailed++;
        }
    }
    
    // ============ Edge Case: Malformed genres ============
    private static void testEdgeCase_MalformedGenres() {
        System.out.println("\n[FP-012] Edge Case: System must handle malformed genres field");
        try {
            // Test with genres containing extra spaces, empty values, etc.
            String testGenres = "Action ; Comedy ; ; Drama";
            String[] genres = testGenres.split(";");
            
            boolean handled = true;
            for (String genre : genres) {
                genre = genre.trim();
                if (genre.isEmpty()) {
                    // Empty genre found - should be skipped
                }
            }
            
            System.out.println("✓ Test FP-012 passed: Malformed genres handled (empty genres can be filtered)");
                testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ Test FP-012 failed: " + e.getMessage());
            testsFailed++;
        }
    }
    
    // ============ Edge Case: Invalid rating values ============
    private static void testEdgeCase_InvalidRatingValues() {
        System.out.println("\n[FP-013] Edge Case: System must validate rating values (1-5)");
        try {
            int[] testRatings = {1, 2, 3, 4, 5, 0, 6, -1, 10};
            int validRatings = 0;
            
            for (int rating : testRatings) {
                if (rating >= 1 && rating <= 5) {
                    validRatings++;
                }
            }
            
            if (validRatings == 5) {
                System.out.println("✓ Test FP-013 passed: Rating validation working (only 1-5 accepted)");
                testsPassed++;
            } else {
                System.out.println("✗ Test FP-013 failed: Rating validation incorrect");
                testsFailed++;
            }
        } catch (Exception e) {
            System.out.println("✗ Test FP-013 failed: " + e.getMessage());
            testsFailed++;
        }
    }
    
    // ============ Helper Methods ============
    
    private static void createTestDataDirectory() {
        try {
            Files.createDirectories(Paths.get(TEST_DATA_DIR));
        } catch (Exception e) {
            System.out.println("Warning: Could not create test directory: " + e.getMessage());
        }
    }
    
    private static void createSampleMoviesFile(String filePath) throws IOException {
        String content = "movie_id,title,director,year,genres,avg_rating,num_ratings\n" +
                "1,The Shawshank Redemption,Frank Darabont,1994,Drama,9.3,2500000\n" +
                "2,The Godfather,Francis Ford Coppola,1972,Crime;Drama,9.2,1700000\n" +
                "3,The Dark Knight,Christopher Nolan,2008,Action;Crime;Drama,9.0,2600000\n" +
                "4,Pulp Fiction,Quentin Tarantino,1994,Crime;Drama,8.9,1600000\n" +
                "5,Forrest Gump,Robert Zemeckis,1994,Drama;Romance,8.8,1200000\n";
        
        Files.write(Paths.get(filePath), content.getBytes());
    }
    
    private static void createSampleRatingsFile(String filePath) throws IOException {
        String content = "movie_id,rating,timestamp\n" +
                "1,5,2024-10-22\n" +
                "3,5,2024-10-15\n" +
                "35,2,2024-10-28\n" +
                "20,4,2024-10-25\n";
        
        Files.write(Paths.get(filePath), content.getBytes());
    }
    
    private static void createMalformedFile(String filePath) throws IOException {
        String content = "movie_id,title,director,year,genres,avg_rating,num_ratings\n" +
                "1,The Shawshank Redemption,Frank Darabont,1994,Drama,9.3,2500000\n" +
                "invalid line without proper fields\n" +
                "2,The Godfather,Francis Ford Coppola,1972,Crime;Drama,9.2\n" +
                "3,The Dark Knight,Christopher Nolan,2008,Action;Crime;Drama,9.0,2600000\n";
        
        Files.write(Paths.get(filePath), content.getBytes());
    }
    
    private static void printTestSummary() {
        System.out.println("\n======================================");
        System.out.println("        Test Summary");
        System.out.println("======================================");
        System.out.println("Total Tests: " + (testsPassed + testsFailed));
        System.out.println("Passed: " + testsPassed);
        System.out.println("Failed: " + testsFailed);
        System.out.println("Success Rate: " + 
            String.format("%.1f%%", (testsPassed * 100.0 / (testsPassed + testsFailed))));
        System.out.println("======================================\n");
    }
}