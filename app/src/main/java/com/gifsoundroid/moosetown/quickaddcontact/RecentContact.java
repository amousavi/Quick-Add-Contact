package com.gifsoundroid.moosetown.quickaddcontact;

import java.io.Serializable;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by alexm_000 on 8/16/2015.
 */
public class RecentContact implements Serializable{

    private String name;
    private String number;
    private Date timestamp;
    private boolean hasBeenSaved;

    public String mainText(){
        return name + " " + number;
    }

    public String subText(){
        return getFormattedTimestamp();
    }

    public RecentContact(String name, String number) {

        this.timestamp = new Date();
        this.name = name;
        this.number = number;
        hasBeenSaved = false;
    }

    public Boolean getHasBeenSaved() { return hasBeenSaved; }

    public void setHasBeenSaved(Boolean status){
        this.hasBeenSaved = status;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getFormattedTimestamp(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy hh:mm a");
        String savedAppend = "";
        if(getHasBeenSaved()){
            savedAppend = " - In Contacts.";
        }
        return dateFormat.format(timestamp) + savedAppend;
    }

    public String toString(){
        return name + "\n" + number + "\n" + getFormattedTimestamp();
    }

}
