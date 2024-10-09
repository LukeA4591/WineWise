package seng202.team7.repository;

import seng202.team7.exceptions.DuplicateExc;

import java.util.List;

/**
 * Interface for DAOs
 * @param <T> Object type
 */
public interface DAOInterface<T> {

    /**
     * Gets all of T items from the database
     * @return List of all objects of type T from the database
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