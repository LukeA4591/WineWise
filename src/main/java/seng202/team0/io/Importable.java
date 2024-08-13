package seng202.team0.io;

import java.io.File;
import java.util.List;

/**
 * Interface for reading objects from file
 * @author Ryan Hamilton
 */
public interface Importable<T> {
    /**
     * Reads objects of type T from file
     * @param file File to read from
     * @return List of objects type T that are read from the file
     */
    List<T> readFromFile(File file);
}
