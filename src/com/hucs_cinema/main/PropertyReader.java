package com.hucs_cinema.main;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertyReader {

    private int maxErrorNum; // maximum error number
    private String title; // title of the app
    private int discountPercentage; // discount percentage for the club members
    private int blockTime; // how many seconds will the user be banned after max incorrect login attempt

    /**
     * Constructor for "properties.dat" file.
     */
    public PropertyReader() {
        try (FileReader prop = new FileReader("./assets/data/properties.dat")) { 

            Properties p = new Properties(); // Properties object

            p.load(prop); // load the properties file

            this.maxErrorNum = Integer.parseInt(p.getProperty("maximum-error-without-getting-blocked")); // get the maximum error number
            this.title = p.getProperty("title"); // get the title of the app
            this.discountPercentage = Integer.parseInt(p.getProperty("discount-percentage")); // get the discount percentage
            this.blockTime = Integer.parseInt(p.getProperty("block-time")); // get the block time
        }
        catch (IOException e) { // if there is an error
            e.printStackTrace(); // print the error
        }
    }

    /**
     * @return Title of the app
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return How many incorrect login attempts can be made
     */
    public int getMaxErrorNum() {
        return maxErrorNum;
    }

    /**
     * @return Discount percentage for the club members
     */
    public int getDiscountPercentage() {
        return discountPercentage;
    }

    /**
     * @return How many seconds will the user be
     * banned after max incorrect login attempt
     */
    public int getBlockTime() {
        return blockTime;
    }
}
