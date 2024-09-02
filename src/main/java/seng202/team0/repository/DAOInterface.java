package seng202.team0.repository;

import java.util.List;

public interface DAOInterface<T> {

    /**
     * Interface for Database Access Objects (DAOs) that provides common functionality for database access
     *
     * @author Morgan English
     */

    /**
     * Gets all of T from the database
     *
     * @return List of all objects type T from the database
     */
    List<T> getAll();
}