package seng202.team7.models;

/**
 * Wine model, used to represent an instance of a wine.
 */
public class Wine {
    String colour;
    String wineName;
    Integer score;
    int vintage;
    String region;
    String wineryString;
    String description;

    /**
     * NEW CONSTRUCTOR FOR THE SQL DATABASE
     * @param type colour of wine
     * @param name name of wine
     * @param wineryString winery which produced wine
     * @param vintage year of wine
     * @param score critic score from wine
     * @param region region wine is from
     * @param description description of wine
     */
    public Wine(String type, String name, String wineryString, int vintage, Integer score, String region, String description)
    {
        this.colour = type;
        this.wineName = name;
        this.wineryString = wineryString;
        this.vintage = vintage;
        this.score = score;
        this.region = region;
        this.description = description;
    }

    /**
     * An override method for equals which allows two wine objects to be compared based on their wineName, vintage, and
     * winery. Since each wine with have a unique combination of these 3 attributes, if they both have the same then they
     * will be referring to the same wine.
     * @param o The wine that is being compared to the current wine
     * @return True if they are same and false if they are not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wine wine = (Wine) o;
        return wineName.equals(wine.wineName) && vintage == wine.vintage && wineryString.equals(wine.wineryString);
    }

    /**
     * The Getter method for the colour of wine
     * @return typeWine
     */
    public String getColor() {
        return colour;
    }

    /**
     * The Getter method for the region of the wine
     * @return region
     */
    public String getRegion() {
        return region;
    }

    /**
     * The Getter method for the name of the wine
     * @return wineName
     */
    public String getWineName() {
        return wineName;
    }

    /**
     * The Getter method for the critic score that the wine received
     * @return score
     */
    public Integer getScore() {
        return score;
    }

    /**
     * The Getter method for the vintage of the wine
     * @return vintage
     */
    public int getVintage() {
        return vintage;
    }

    /**
     * The Getter method for the description of the wine
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * The Getter method for the name of the wine's winery
     * @return wineryString
     */
    public String getWineryString() {
        return wineryString;
    }

    /**
     * The setter method for the critic score of the wine
     * @param rating score to set
     */
    public void setWineScore(Integer rating) {
        this.score = rating;
    }

}
