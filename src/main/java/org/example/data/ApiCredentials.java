package org.example.data;

/**
 * The credentials for the order API
 *
 * @param username the username
 * @param password the password for the user
 */
public record ApiCredentials(String username, String password) {
}
