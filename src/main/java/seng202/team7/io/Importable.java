package seng202.team7.io;

import seng202.team7.models.Wine;

import java.io.File;
import java.util.List;

/**
 * Interface for reading objects from file
 * @param <T> Object type
 */
public interface Importable<T> {
    /**
     * Reads objects of type T from file
     * @param file File to read from
     * @param headerIndexes Indexes of the headers that the admin selected
     * @return List of objects type T that are read from the file
     */
    List<T> readFromFile(File file, List<Integer> headerIndexes);

    /**
     * Reads the first six lines from a file
     * @param file file to read from
     * @return list of the lines
     */
    List<String[]> readSixLinesFromFile(File file);

    /**
     * Read wine from line of csv
     * @param line current csv line as list of Strings
     * @param headerIndexes Indexes of the headers that the admin selected
     * @return Wine object parsed from line
     */
    Wine readWineFromLine(String[] line, List<Integer> headerIndexes);
}
