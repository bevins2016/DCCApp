package com.example.dcc.helpers;

import java.io.Serializable;

/**
 * Object that represents a completed edaily object that is fetched from the server.
 * Created by harmonbc on 5/29/13.
 */
public class EDaily implements Serializable{
    private String body, color, date, submitted, firstname, lastname, proj;
    private int usr_id, hours, grade;

    public EDaily(){
    }

    /**
     * Returns the body of the edaily
     * @return
     */
    public String getBody() {
        return body;
    }

    /**
     * Set the body of the edaily
     * @param body
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Get the color of the grade
     * @return
     */
    public String getColor() {
        return color;
    }

    /**
     * Set the color of the grade
     * @param color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Return the date of the report
     * @return
     */
    public String getDate() {
        return date;
    }

    /**
     * set the date of the report
     * @param date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Get the submitted date
     * @return
     */
    public String getSubmitted() {
        return submitted;
    }

    /**
     * set the submitted date
     * @param submitted
     */
    public void setSubmitted(String submitted) {
        this.submitted = submitted;
    }

    /**
     * Get the user ID
     * @return
     */
    public int getUsr_id() {
        return usr_id;
    }

    /**
     * Set the user ID
     * @param usr_id
     */
    public void setUsr_id(int usr_id) {
        this.usr_id = usr_id;
    }

    /**
     * Returns the hours submitted for the report
     * @return
     */
    public int getHours() {
        return hours;
    }

    /**
     * Set the hours for this report
     * @param hours
     */
    public void setHours(int hours) {
        this.hours = hours;
    }

    /**
     * Returns the grade of the edaily
     * @return
     */
    public int getGrade() {
        return grade;
    }

    /**
     * Set the grade of the edaily
     * @param grade
     */
    public void setGrade(int grade) {
        this.grade = grade;
    }

    /**
     * Set the last name
     * @return
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * Set the last name
     * @param lastname
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * Returns the first name
     * @return
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Set the first name
     * @param firstname
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * Get the project name
     * @return
     */
    public String getProj() {
        return proj;
    }

    /**
     * Set the project name
     * @param proj
     */
    public void setProj(String proj) {
        this.proj = proj;
    }
}
