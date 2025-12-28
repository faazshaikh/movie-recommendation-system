public class Exit {

    private final DataManager dataManager;

    public Exit(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    /**
     * Saves ratings and shuts down the application.
     */
    public void performExit() {
        System.out.println("\nSaving data before exit.");

        try {
            // Ensures all ratings are written to my_ratings.txt
            dataManager.saveRatingsToFile();
            System.out.println("Data saved successfully.");
        } catch (Exception e) {
            System.out.println("Warning: Could not save ratings properly.");
        }

        System.out.println("Exiting the Movie Recommendation System.");
        System.exit(0);
    }
}