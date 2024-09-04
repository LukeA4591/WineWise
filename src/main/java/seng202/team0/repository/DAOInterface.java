package seng202.team0.repository;

import seng202.team0.exceptions.DuplicateExc;

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

    /**
     * Adds a single object of type T to database
     * @param toAdd object of type T to add
     * @throws DuplicateExc if the object already exists
     * @return object insert id if inserted correctly
     */
    int add(T toAdd) throws DuplicateExc;
}