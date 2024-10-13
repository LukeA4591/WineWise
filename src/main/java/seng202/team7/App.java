package seng202.team7;

import seng202.team7.gui.MainWindow;

import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * Default entry point class
 * @author seng202 teaching team
 */
public class App {

    /**
     * Default constructor for the App class
     */
    public App() {

    }

    /**
     * Entry point which runs the javaFX application
     * @param args program arguments from command line
     */
    public static void main(String[] args) {
       try {
           PrintStream fileout = new PrintStream("logs.txt");
           System.setOut(fileout);
           MainWindow.main(args);
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       }
    }
}