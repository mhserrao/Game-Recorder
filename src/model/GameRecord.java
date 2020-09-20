/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 * A class that represents a GameRecord object. A GameRecord has a name, minimum
 * number of player, maximum number of players, a price and a description.
 *
 * @author Maiziel Serrao
 */
public class GameRecord {

    private String gameName = "this Game";
    private int minPlayers = 1;
    private int maxPlayers = 5;
    private double price = 12;
    private String description = "This is an awesome Game.";

    Validator isValid = new Validator();

    /**
     * Default constructor of a GameRecord object.
     */
    public GameRecord() {
    }

    /**
     * Constructor of a GameRecord object. Sets the gameName, minPlayers,
     * maxPlayers, price and description parameters of the GameRecord.
     *
     * @param gameName represents the new name of the GameRecord object
     * @param minPlayers represents the new number of minimum players of the
     * GameRecord object
     * @param maxPlayers represents the new number of maximum players of the
     * GameRecord object
     * @param price represents the cost of the GameRecord object
     * @param description represents a small rationale behind the GameRecord
     * object
     */
    public GameRecord(String gameName, int minPlayers, int maxPlayers,
            double price, String description) {
        setGameName(gameName);
        setMinPlayers(minPlayers);
        setMaxPlayers(maxPlayers);
        setPrice(price);
        setDescription(description);
    }

    /**
     * Accessor method of the gameName parameter of the GameRecord object.
     *
     * @return the name of the GameRecord object
     */
    public String getGameName() {
        return gameName;
    }

    /**
     * Mutator method of the gameName parameter of the GameRecord object.
     *
     * @param gameName the new name of the GameRecord object
     * @throws IllegalArgumentException for null String gameName
     * @throws IllegalArgumentException for empty String gameName
     */
    public void setGameName(String gameName) throws IllegalArgumentException {
        if (gameName == null) {
            throw new IllegalArgumentException("Game name cannot be null!");
        } else if (isValid.isValidString(gameName)) {
            this.gameName = gameName;
        } else {
            throw new IllegalArgumentException("That's not a valid game name! "
                    + "It cannot be an empty string!");
        }
    }

    /**
     * Accessor method of the minPlayers parameter of the GameRecord object.
     *
     * @return the number of the minimum players of the GameRecord.
     */
    public int getMinPlayers() {
        return minPlayers;
    }

    /**
     * Mutator method of the minPlayers parameter of the GameRecord object.
     *
     * @param minPlayers the new number of the minimum players of the GameRecord
     * @throws IllegalArgumentException for int less than 1
     * @throws IllegalArgumentException for int greater than 4
     */
    public void setMinPlayers(int minPlayers) throws IllegalArgumentException {
        if (isValid.isInt(minPlayers)) {
            if ((minPlayers >= 1) && (minPlayers <= 4)) {
                this.minPlayers = minPlayers;
            } else if (minPlayers < 1) {
                throw new IllegalArgumentException("Minimum amount of players "
                        + "must be 1 or greater!");
            } else {
                throw new IllegalArgumentException("Minimum amount of players "
                        + "must be less than or equal to 4!");
            }
        }
    }

    /**
     * Accessor method of the maxPlayers parameter of the GameRecord object.
     *
     * @return the number of the maximum players of the GameRecord.
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * Mutator method of the maxPlayers parameter of the GameRecord object.
     *
     * @param maxPlayers the new number of the maximum players of the GameRecord
     * @throws IllegalArgumentException for int less than minPlayers
     * @throws IllegalArgumentException for int greater than 10
     */
    public void setMaxPlayers(int maxPlayers) throws IllegalArgumentException {
        if (isValid.isInt(maxPlayers)) {
            if ((maxPlayers >= this.minPlayers) && (maxPlayers <= 10)) {
                this.maxPlayers = maxPlayers;
            } else if (maxPlayers < this.minPlayers) {
                throw new IllegalArgumentException(String.format("Maximum "
                        + "amount of players must be %d or greater!",
                        this.getMinPlayers()));
            } else {
                throw new IllegalArgumentException("Maximum amount of players "
                        + "must be less than or equal to 10!");
            }
        }
    }

    /**
     * Accessor method of the price parameter of the GameRecord object.
     *
     * @return the cost of the GameRecord.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Mutator method of the maxPlayers parameter of the GameRecord object.
     *
     * @param price the new cost of the GameRecord
     * @throws IllegalArgumentException for double value less than or equal to 0
     */
    public void setPrice(double price) throws IllegalArgumentException {
        if (isValid.isPositiveDouble(price)) {
            this.price = price;
        } else {
            throw new IllegalArgumentException("Please enter a double value "
                    + "greater than 0!");
        }
    }

    /**
     * Accessor method of the description parameter of the GameRecord object.
     *
     * @return the rationale behind the GameRecord object.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Mutator method of the description parameter of the GameRecord object.
     *
     * @param description the new description of the GameRecord
     * @throws IllegalArgumentException for null description String
     * @throws IllegalArgumentException for empty description String
     */
    public void setDescription(String description)
            throws IllegalArgumentException {
        if (description == null) {
            throw new IllegalArgumentException("Description cannot be null!");
        } else if (isValid.isValidString(description)) {
            this.description = description;
        } else {
            throw new IllegalArgumentException("Description cannot be empty!");
        }
    }

    /**
     * A method to neatly organize specific information about the GameRecord
     * object within a String object.
     *
     * @return String containing the game name, the minimum number of players,
     * the maximum number of players, and the description of the GameRecord
     * object.
     */
    @Override
    public String toString() {
        String thisString = String.format("%s is for %d to %d players, %s", 
                this.getGameName(), this.getMinPlayers(), this.getMaxPlayers(),
                this.getDescription());
        
        String onePlayer = String.format("%s is for %d player, %s", 
                this.getGameName(), this.getMinPlayers(), 
                this.getDescription());
        
        if (this.maxPlayers == this.minPlayers) {
            return onePlayer;
        } else {
            return thisString;
        }
    }

    /**
     * A method to test whether an object is the same as a GameRecord object.
     *
     * @param o Object to be compared with a GameRecord object
     * @return true if the gameName, minPlayers, and maxPlayer, otherwise
     * returns false
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (this == o) {
            return true;
        }

        if (o instanceof GameRecord) {
            GameRecord gR = (GameRecord) o;
            return ((this.getGameName()).equals(gR.getGameName()))
                    && (this.getMinPlayers() == gR.getMinPlayers())
                    && (this.getMaxPlayers() == gR.getMaxPlayers());
        } else {
            return false;
        }
    }

}
