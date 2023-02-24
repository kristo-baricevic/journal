package org.launchcode.journal.models;

import java.util.ArrayList;
import org.launchcode.journal.models.Entry;

// This is a change made in sandbox.

/**
 * Created by LaunchCode
 */
public class EntryData {


    /**
     * Returns the results of searching the Jobs data by field and search term.
     *
     * For example, searching for employer "Enterprise" will include results
     * with "Enterprise Holdings, Inc".
     *
     * @param column Job field that should be searched.
     * @param value Value of the field to search for.
     * @param allEntries The list of jobs to search.
     * @return List of all jobs matching the criteria.
     */
    public static ArrayList<Entry> findByColumnAndValue(String column, String value, Iterable<Entry> allEntries) {

        ArrayList<Entry> results = new ArrayList<>();
        try {
            if (value.toLowerCase().equals("all")) {
                return (ArrayList<Entry>) allEntries;
            }

            if (column.equals("all")) {
                results = findByValue(value, allEntries);
                return results;
            }
            for (Entry entry : allEntries) {
                if (column.equals("all")) {
                    results.add(entry);
                }
                if (column.equals("title")) {
                    if (entry.getTitle().toLowerCase().contains(value.toLowerCase())) {
                        results.add(entry);
                    }
                }
                if (column.equals("topic")) {
                    if (entry.getTopic() != null && entry.getTopic().getName().toLowerCase().contains(value.toLowerCase())) {
                        results.add(entry);
                    }
                }
                if (column.equals("mood")) {
                    if (entry.getMood() != null && entry.getMood().getName().toLowerCase().contains(value.toLowerCase())) {
                        results.add(entry);
                    }
                }
            }
        } catch (Exception e) {

        }
        return results;
    }

    public static String getFieldValue(Entry entry, String fieldName){
        String theValue;
        if (fieldName.equals("name")){
            theValue = entry.getTitle();
        } else if (fieldName.equals("category")){
            theValue = entry.getTopic().getName();
        } else {
            theValue = entry.getMood().getName();
        }


        return theValue;
    }

    /**
     * Search all Job fields for the given term.
     *
     * @param value The search term to look for.
     * @param allEntries The list of jobs to search.
     * @return      List of all jobs with at least one field containing the value.
     */
    public static ArrayList<Entry> findByValue(String value, Iterable<Entry> allEntries) {

        String lower_val = value.toLowerCase();

        ArrayList<Entry> results = new ArrayList<>();

        for (Entry entry : allEntries) {
            boolean matchFound = false;

            if (!matchFound && entry.getTitle().toLowerCase().contains(lower_val)) {
                results.add(entry);
            } else if (!matchFound && entry.getTopic() != null && entry.getTopic().getName().contains(lower_val)) {
                results.add(entry);
            } else if (!matchFound && entry.getMood() != null && entry.getMood().getName().contains(lower_val)) {
                results.add(entry);
            } else if (!matchFound && entry.toString().toLowerCase().contains(lower_val)) {
                results.add(entry);
            }
        }
        return results;
    }

}