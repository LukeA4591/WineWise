package seng202.team7.gui;

import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import seng202.team7.business.ReviewManager;
import seng202.team7.business.WineManager;
import seng202.team7.models.Wine;

import java.util.*;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import seng202.team7.services.WinePopupService;

/**
 * Controller class for the home page of the application.
 * Handles the display of top-rated wines and interacts with the user interface elements.
 * Provides functionality to display detailed information about selected wines in a popup.
 */
public class HomePageController {

    @FXML
    private ImageView imageView1;
    @FXML
    private ImageView imageView2;
    @FXML
    private ImageView imageView3;
    @FXML
    private ImageView imageView4;
    @FXML
    private ImageView imageView5;
    @FXML
    private ImageView imageView6;
    @FXML
    private ImageView nextImage;
    @FXML
    private ImageView prevImage;
    @FXML
    private Label rating1;
    @FXML
    private Label rating2;
    @FXML
    private Label rating3;
    @FXML
    private Label rating4;
    @FXML
    private Label rating5;
    @FXML
    private Label rating6;
    @FXML
    private Label desc1;
    @FXML
    private Label desc2;
    @FXML
    private Label desc3;
    @FXML
    private Label desc4;
    @FXML
    private Label desc5;
    @FXML
    private Label desc6;
    @FXML
    private Label wine1;
    @FXML
    private Label wine2;
    @FXML
    private Label wine3;
    @FXML
    private Label wine4;
    @FXML
    private Label wine5;
    @FXML
    private Label wine6;
    @FXML
    private Label pageLabel;
    @FXML
    private Label noReviewsLabel;
    @FXML
    private Button sortButton;
    @FXML
    private Pane pane1;
    @FXML
    private Pane pane2;
    @FXML
    private Pane pane3;
    @FXML
    private Pane pane4;
    @FXML
    private Pane pane5;
    @FXML
    private Pane pane6;
    @FXML
    Pane prevImagePane;
    @FXML
    Pane nextImagePane;

    private int page;
    private int totalPages;
    private WineManager wineManager;
    private ReviewManager reviewManager;
    private final WinePopupService wineService = new WinePopupService();
    boolean viewCritic;
    boolean finalPage;
    private List<Pane> panes;
    private List<Label> wineLabels;
    private List<Label> descs;
    private List<Label> ratings;
    private List<ImageView> images;
    private int size;
    private List<Wine> topUserWinesPage;

    /**
     * Default constructor for the home page controller
     */
    public HomePageController() {

    }

    /**
     * Initializes the HomePageController. Sets the stage and loads the top 3 rated wines to be displayed with their
     * information. If there aren't 3 wines in the database, it doesn't load any.
     */
    public void init() {
        viewCritic = true;
        wineManager = new WineManager();
        reviewManager = new ReviewManager();
        topUserWinesPage = new ArrayList<>();
        panes = new ArrayList<>(Arrays.asList(pane1, pane2, pane3, pane4, pane5, pane6));
        wineLabels = new ArrayList<>(Arrays.asList(wine1, wine2, wine3, wine4, wine5, wine6));
        descs = new ArrayList<>(Arrays.asList(desc1, desc2, desc3, desc4, desc5, desc6));
        ratings = new ArrayList<>(Arrays.asList(rating1, rating2, rating3, rating4, rating5, rating6));
        images = new ArrayList<>(Arrays.asList(imageView1, imageView2, imageView3, imageView4, imageView5, imageView6));
        prevImage.setVisible(false);
        initScreen();
    }

    /**
     * Sets the size of the wine collection depending on whether critic or user reviews are being viewed.
     * Retrieves the size from either the wineManager or reviewManager based on the viewCritic flag.
     */
    private void setSize() {
        if (viewCritic) {
            size = wineManager.getAll().size();
            wineManager.setTotalWinesInDB(size);
        } else {
            size = reviewManager.getNumWinesWithReviews();
        }
    }

    /**
     * Calculates the total number of pages based on the size of the wine collection.
     * Resets the finalPage flag to false.
     */
    private void calculateTotalPagesAndReset() {
        finalPage = false;
        if (viewCritic) {
            totalPages = (size / 6) + 1;
        } else {
            totalPages = (size / 6) + 1;
        }
    }

    /**
     * Sets the wines to be displayed on the current page.
     * Populates the screen with the wine names, wineries, ratings, and images.
     *
     * @param wines List of wines to be displayed.
     */
    private void setWines(List<Wine> wines) {
        displayWines(wines);
        displayWinery(wines);
        displayRatings(wines);
        setImage(wines);
    }

    /**
     * Initializes the screen with the correct number of wines based on the current page.
     * Determines whether to load critic or user reviews and initializes the panes with wine data.
     */
    private void initScreen() {
        page = 0;
        List<Wine> wines;
        getTopUsers();
        setSize();
        calculateTotalPagesAndReset();
        validateButtons();
        setPage();
        checkScreen(size);
        if (viewCritic) {
            wines = wineManager.getTopRated(page);
            initScreenPanes(wines);
        } else {
            initScreenPanes(topUserWinesPage);
        }
    }

    /**
     * Initializes the panes on the screen based on the number of wines available.
     *
     * @param wines List of wines to display.
     */
    private void initScreenPanes(List<Wine> wines) {
        if (wines.size() >= 6) {
            setWines(wines);
        } else {
            setPanes(wines.size());
            setWines(wines);
        }
    }

    /**
     * Checks if the screen should display reviews or if it should indicate that no reviews are available.
     * Hides elements if there are no wines to display.
     *
     * @param size The size of the wine collection to check.
     */
    private void checkScreen(int size) {
        if (size == 0) {
            if (viewCritic) {
                noReviewsLabel.setText("No wines to view");
            } else {
                noReviewsLabel.setText("No wines have been reviewed by users");
            }
            setPanes(0);
            pageLabel.setVisible(false);
            setPrev(false);
            setNext(false);
        } else {
            noReviewsLabel.setText("");
            setPanes(6);
            pageLabel.setVisible(true);
        }
    }

    /**
     * Handles the sort button finalPageaction to toggle between viewing critic reviews and user reviews.
     * Updates the view and buttons based on the selected sort type.
     */
    @FXML
    private void sortByButton() {
        if (viewCritic) {
            sortButton.setText("Customers");
            viewCritic = !viewCritic;
        } else {
            sortButton.setText("Critics");
            viewCritic = !viewCritic;
        }
        initScreen();
    }

    /**
     * Handles the next page button click event.
     * Moves to the next page of wines and updates the displayed information accordingly.
     */
    @FXML
    private void nextPage() {
        page++;
        validateButtons();
        setPage();
        List<Wine> wines = wineManager.getTopRated(page);
        getTopUsers();
        slidePanes(true);
        PauseTransition pause = new PauseTransition(Duration.millis(250));
        pause.setOnFinished( event -> {
            if (viewCritic) {
                checkNextPage(wines);
            } else {
                checkNextPage(topUserWinesPage);
            }
        });
        pause.play();
    }

    /**
     * Checks the wines for the next page and updates the screen accordingly.
     * If it's the final page, it adjusts the number of panes displayed.
     *
     * @param wines The list of wines to check for the next page.
     */
    private void checkNextPage(List<Wine> wines) {
        if (finalPage) {
            setPanes(wines.size());
        }
        setWines(wines);
    }

    /**
     * Sets the number of visible panes based on the number of wines to display.
     *
     * @param numPanes The number of panes to be made visible.
     */
    private void setPanes(int numPanes) {
        for (Pane pane : panes) {
            pane.setVisible(false);
        }
        for (int i = 0; i < numPanes; i++) {
            panes.get(i).setVisible(true);
        }
    }

    /**
     * Resets the panes to their visible state and toggles the finalPage flag.
     */
    private void resetPanes() {
        for (Pane pane : panes) {
            pane.setVisible(true);
        }
        finalPage = !finalPage;
    }
    /**
     * Handles the previous page button click event.
     * Moves to the previous page of wines and updates the displayed information accordingly.
     */
    @FXML
    private void prevPage() {
        page--;
        setPage();
        slidePanes(false);
        PauseTransition pause = new PauseTransition(Duration.millis(250));
        pause.setOnFinished( event -> {
            prevEvent();
        });
        pause.play();
    }

    /**
     * Delayed event when the previous button is pressed
     * Allows panes to transition off the screen, then change wines before transitioning on.
     */
    private void prevEvent() {
        if (finalPage) {
            resetPanes();
        }
        validateButtons();
        if (viewCritic) {
            List<Wine> wines = wineManager.getTopRated(page);
            setWines(wines);
        } else {
            getTopUsers();
            setWines(topUserWinesPage);
        }
    }
    /**
     * Sets the current page label based on the page number.
     */
    private void setPage() {
        pageLabel.setText("Page " + (page + 1));
    }

    /**
     * Sets the visibility of the "next page" button and the pane that contains it.
     *
     * @param setter Boolean value indicating whether the "next" button should be visible or not.
     */
    private void setNext(boolean setter) {
        nextImage.setVisible(setter);
        nextImagePane.setVisible(setter);
    }

    /**
     * Sets the visibility of the "previous page" button and the pane that contains it.
     *
     * @param setter Boolean value indicating whether the "previous" button should be visible or not.
     */
    private void setPrev(boolean setter) {
        prevImage.setVisible(setter);
        prevImagePane.setVisible(setter);
    }

    /**
     * Validates the state of the navigation buttons based on the current page number and total number of pages.
     * Enables or disables the next and previous buttons accordingly.
     */
    private void validateButtons() {
        if (page == 0) {
            setPrev(false);
        } else {
            setPrev(true);
        }
        if ((page + 2) * 6 <= size) {
            setNext(true);
        } else {
            if (!finalPage) {
                finalPageButton();
            } else {
                setNext(false);
            }
        }
    }

    /**
     * Updates the final page button state based on the total number of wines and total pages.
     */

    private void finalPageButton() {
        if ((size % 6) != 0 && totalPages > 1) {
            setNext(true);
            finalPage = true;
        } else {
            setNext(false);
            finalPage = false;
        }
    }

    /**
     * Retrieves the top user-rated wines from the review manager and stores them in a list.
     */
    private void getTopUsers() {
        topUserWinesPage = new ArrayList<>();
        Wine wine;
        LinkedHashMap<Integer, Integer> avgReviews = reviewManager.getAverageReviews(page);
        for (Map.Entry<Integer, Integer> entry : avgReviews.entrySet()) {
            wine = wineManager.getWineFromID(entry.getKey());
            wine.setWineScore(entry.getValue());
            topUserWinesPage.add(wine);
        }
    }

    /**
     * Displays the names of the top-rated wines on the labels.
     * @param wines A list of the top-rated wines.
     */
    public void displayWines(List<Wine> wines) {
        for (int i = 0; i < wines.size(); i++) {
            Label wineLabel = wineLabels.get(i);
            wineLabel.setText(wines.get(i).getWineName());
        }
    }

    /**
     * Displays the wineries of the top-rated wines on the description labels.
     * @param wines A list of the top-rated wines.
     */
    public void displayWinery(List<Wine> wines) {
        for (int i = 0; i < wines.size(); i++) {
            Label desc = descs.get(i);
            desc.setText("Winery: " + wines.get(i).getWineryString());
        }
    }

    /**
     * Displays the ratings of the top-rated wines on the rating labels.
     * @param wines A list of the top-rated wines.
     */
    public void displayRatings(List<Wine> wines) {
        for (int i = 0; i < wines.size(); i++) {
            Label rating = ratings.get(i);
            if (wines.get(i).getScore() == null) {
                rating.setText("No Critic Rating");
            } else {
                rating.setText(wines.get(i).getScore() + " / 100");
            }
        }
    }

    /**
     * Sets the images of the top-rated wines based on their color. The images are selected by using the colour of the
     * wine along with the WinePopUpService.
     * @param wines A list of the top-rated wines.
     */
    public void setImage(List<Wine> wines) {
        for (int i = 0; i < wines.size(); i++) {
            Image image = wineService.getImage(wines.get(i));
            ImageView imageView = images.get(i);
            imageView.setImage(image);
        }
    }

    /**
     * Fetches and displays the details of the selected wine when it is clicked.
     * The wine is displayed in a popup window, and the image is fetched using the WinePopupService.
     *
     * @param wineNum The index of the selected wine in the list of displayed wines.
     */
    public void winePressed(int wineNum) {
        List<Wine> wines;
        Wine wine;
        if (viewCritic) {
            wines = wineManager.getTopRated(page);
            wine = wines.get(wineNum);
        } else {
            wines = topUserWinesPage;
            wine = wines.get(wineNum);
            wine = wineManager.getWineFromID(wineManager.getWineID(wine));
        }
        Image image = wineService.getImage(wine);
        wineService.winePressed(wine, image, rating1);
    }

    /**
     * Event handler for pressing the first wine label. Fetches the first top-rated wine and displays its details in a
     * popup.
     */
    @FXML
    void wine1Pressed() {
        winePressed(0);
    }

    /**
     * Event handler for pressing the second wine label. Fetches the second top-rated wine and displays its details in
     * a popup.
     */
    @FXML
    void wine2Pressed() { winePressed(1); }

    /**
     * Event handler for pressing the third wine label. Fetches the third top-rated wine and displays its details in a
     * popup.
     */
    @FXML
    void wine3Pressed() { winePressed(2); }

    /**
     * Event handler for pressing the fourth wine label.
     * Displays the details of the fourth wine in a popup.
     */
    @FXML
    void wine4Pressed() { winePressed(3); }

    /**
     * Event handler for pressing the fifth wine label.
     * Displays the details of the fifth wine in a popup.
     */
    @FXML
    void wine5Pressed() { winePressed(4); }

    /**
     * Event handler for pressing the sixth wine label.
     * Displays the details of the sixth wine in a popup.
     */
    @FXML
    void wine6Pressed() { winePressed(5); }

    /**
     * Triggers the sliding animation for all panes.
     * Animates the panes to slide left if next is true, or right if next is false.
     * @param next Indicates the direction of the slide (true for left, false for right).
     */
    public void slidePanes(boolean next) {
        if (next) {
            for (Pane pane : panes) {
                animatePane(pane, true);
            }
        } else {
            for (Pane pane : panes) {
                animatePane(pane, false);
            }
        }
    }

    /**
     * Creates and plays a sliding animation for all panes to slide off the screen,
     * but only a specified number of panes slide back on.
     *
     * @param pane The pane to be animated.
     */
    private void animatePane(Pane pane, boolean next) {
        // Slide the pane off by it width * 1.75
        double width;
        if (next) {
            width = pane.getPrefWidth() * 1.75;
        } else {
            width = -(pane.getPrefWidth() * 1.75);
        }
        TranslateTransition slideOff = new TranslateTransition(Duration.seconds(0.25), pane);
        slideOff.setByX(-(width)); // Move the pane off
        slideOff.setOnFinished(event -> {
            pane.setTranslateX(width); // move pane to other side
            TranslateTransition slideIn = new TranslateTransition(Duration.seconds(0.25), pane);
            slideIn.setToX(0); // slide back to center
            slideIn.play();
        });
        slideOff.play();
    }
}
