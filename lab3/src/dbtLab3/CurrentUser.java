package dbtLab3;

/**
 * CurrentUser represents the current user that has logged on to
 * the movie booking system. It is a singleton class.
 */
public class CurrentUser {
    /**
     * The single instance of this class
     */
    private static CurrentUser instance;
        
    /**
     * The name of the current user.
     */
    private String currentUserId;
        
    /**
     * Create a CurrentUser object.
     */
    private CurrentUser() {
        currentUserId = null;
    }
        
    /**
     * Returns the single instance of this class.
     *
     * @return The single instance of the class.
     */
    public static CurrentUser instance() {
        if (instance == null)
            instance = new CurrentUser();
        return instance;
    }
        
    /**
     * Check if a user has logged in.
     *
     * @return true if a user has logged in, false otherwise.
     */
    public boolean isLoggedIn() {
        return currentUserId != null;
    }
        
    /** 
     * Get the user id of the current user. Should only be called if
     * a user has logged in.
     *
     * @return The user id of the current user.
     */
    public String getCurrentUserId() {
        return currentUserId == null ? "<none>" : currentUserId;
    }
        
    /**
     * A new user logs in.
     *
     * @param userId The user id of the new user.
     */
    public void loginAs(String userId) {
        currentUserId = userId;
    }
}
