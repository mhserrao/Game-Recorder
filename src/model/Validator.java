/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * A class representing methods that check inputs according to specific criteria
 * required by the program. Methods include but are not limited to checking for
 * positive integers, valid strings, and positive double values.
 *
 * @author Maiziel Serrao
 */
public class Validator {

    public Validator() {
    }

    /**
     * A method to check if an object is of class Integer.
     *
     * @param value the object that needs to be checked as an Integer object.
     * @return boolean true if value is of class Integer or false if not.
     */
    public boolean isInt(Object value) {
        boolean isInt = false;
        if (value instanceof Integer) {
            isInt = true;
        }
        return isInt;
    }

    /**
     * A method to check if an object is of class Double and is greater than
     * zero.
     *
     * @param value the object that needs to be checked as a positive Double
     * object.
     * @return boolean true if value is of class Double and is greater than zero
     * or false if not.
     */
    public boolean isPositiveDouble(Object value) {
        boolean isPositiveDouble = false;
        if (value instanceof Double) {
            double newVal = (double) value;
            if (newVal > 0) {
                isPositiveDouble = true;
            }
        }
        return isPositiveDouble;
    }

    /**
     * A method to check if an object is of class String which if it is, checks
     * if a String is null or empty.
     *
     * @param value the object that needs to be checked as a String object that
     * is neither null or empty.
     * @return boolean true if value is of class String and is not null or
     * empty. Else, returns false.
     */
    public boolean isValidString(Object value) {
        boolean isString = false;
        if (value instanceof String) {
            String newVal = (String) value;
            if (!newVal.trim().equals("")) {
                isString = true;
            }
        }
        return isString;
    }

}//end of class
