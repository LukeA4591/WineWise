package seng202.team0.models;

/**
 * Wine model, used to represent an instance of a wine.
 */
public class Wine {
    String colour;
    String wineName;
    int score;
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
    public Wine(String type, String name, String wineryString, int vintage, int score, String region, String description)
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
     * The Getter method for the colour of wine
     * @return typeWine
     */
    public String getColor() {
        return colour;
    }

    /**
     * The Getter method for the volume of the wine
     * @return volume
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
    public int getScore() {
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
     * returns winerystring
     * @return wineryString
     */
    public String getWineryString() {
        return wineryString;
    }



}
