package com.mycompany.realestatemanagementsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PropertySearch {

    // BINARY SEARCH BY PRICE (RETURNS EVERY PROPERTY WITH THAT EXACT PRICE)
    public static ArrayList<Property> searchByPrice(ArrayList<Property> list, double price) {
        ArrayList<Property> results = new ArrayList<>();

        // BINARY SEARCH ONLY WORKS ON SORTED DATA, SO SORT A COPY BY PRICE FIRST
        ArrayList<Property> sorted = new ArrayList<>(list);
        Collections.sort(sorted, new Comparator<Property>() {
            @Override
            public int compare(Property a, Property b) {
                return Double.compare(a.getPrice(), b.getPrice());
            }
        });

        int low = 0;
        int high = sorted.size() - 1;
        int found = -1;

        while (low <= high) {
            int mid = (low + high) / 2;
            int compare = Double.compare(sorted.get(mid).getPrice(), price);

            if (compare == 0) {
                found = mid;
                break;
            } else if (compare < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        if (found == -1) {
            return results; // NOT FOUND
        }

        // SEVERAL PROPERTIES CAN HAVE THE SAME PRICE, SO COLLECT THE NEIGHBORS TOO
        int start = found;
        while (start > 0 && sorted.get(start - 1).getPrice() == price) {
            start--;
        }

        int end = found;
        while (end < sorted.size() - 1 && sorted.get(end + 1).getPrice() == price) {
            end++;
        }

        for (int i = start; i <= end; i++) {
            results.add(sorted.get(i));
        }

        return results;
    }

    // BINARY SEARCH BY LOCATION (EXACT MATCH, NOT CASE SENSITIVE)
    public static ArrayList<Property> searchByLocation(ArrayList<Property> list, String location) {
        ArrayList<Property> results = new ArrayList<>();

        // SORT A COPY BY LOCATION FIRST
        ArrayList<Property> sorted = new ArrayList<>(list);
        Collections.sort(sorted, new Comparator<Property>() {
            @Override
            public int compare(Property a, Property b) {
                return a.getLocation().compareToIgnoreCase(b.getLocation());
            }
        });

        int low = 0;
        int high = sorted.size() - 1;
        int found = -1;

        while (low <= high) {
            int mid = (low + high) / 2;
            int compare = sorted.get(mid).getLocation().compareToIgnoreCase(location);

            if (compare == 0) {
                found = mid;
                break;
            } else if (compare < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        if (found == -1) {
            return results; // NOT FOUND
        }

        // COLLECT EVERY PROPERTY WITH THE SAME LOCATION
        int start = found;
        while (start > 0 && sorted.get(start - 1).getLocation().equalsIgnoreCase(location)) {
            start--;
        }

        int end = found;
        while (end < sorted.size() - 1 && sorted.get(end + 1).getLocation().equalsIgnoreCase(location)) {
            end++;
        }

        for (int i = start; i <= end; i++) {
            results.add(sorted.get(i));
        }

        return results;
    }
}