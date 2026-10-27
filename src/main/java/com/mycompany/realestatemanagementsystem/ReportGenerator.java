package com.mycompany.realestatemanagementsystem;

import java.util.ArrayList;
import java.util.HashMap;

public class ReportGenerator {

    // SORTS PROPERTIES BY A METRIC USING INSERTION SORT
    // metric = "Price", "Location", "Status" OR "ID"
    public static void sortProperties(ArrayList<Property> list, String metric, boolean ascending) {
        for (int i = 1; i < list.size(); i++) {
            Property key = list.get(i);
            int j = i - 1;

            // SHIFT BIGGER (OR SMALLER, WHEN DESCENDING) ITEMS ONE SPOT TO THE RIGHT
            while (j >= 0 && shouldMove(compareProperties(list.get(j), key, metric), ascending)) {
                list.set(j + 1, list.get(j));
                j--;
            }

            list.set(j + 1, key);
        }
    }

    // SORTS TRANSACTIONS BY A METRIC USING INSERTION SORT
    // metric = "Date", "Price", "Client" OR "ID"
    public static void sortTransactions(ArrayList<Transaction> list, HashMap<Integer, Property> propertyMap,
            HashMap<Integer, Client> clientMap, String metric, boolean ascending) {

        for (int i = 1; i < list.size(); i++) {
            Transaction key = list.get(i);
            int j = i - 1;

            while (j >= 0 && shouldMove(compareTransactions(list.get(j), key, propertyMap, clientMap, metric), ascending)) {
                list.set(j + 1, list.get(j));
                j--;
            }

            list.set(j + 1, key);
        }
    }

    // DECIDES IF THE ITEM ON THE LEFT MUST MOVE PAST THE ITEM BEING PLACED
    private static boolean shouldMove(int result, boolean ascending) {
        if (ascending) {
            return result > 0;
        }
        return result < 0;
    }

    private static int compareProperties(Property a, Property b, String metric) {
        if (metric.equals("Price")) {
            return Double.compare(a.getPrice(), b.getPrice());
        } else if (metric.equals("Location")) {
            return a.getLocation().compareToIgnoreCase(b.getLocation());
        } else if (metric.equals("Status")) {
            return a.getStatus().compareTo(b.getStatus());
        }
        return Integer.compare(a.getPropertyId(), b.getPropertyId());
    }

    private static int compareTransactions(Transaction a, Transaction b, HashMap<Integer, Property> propertyMap,
            HashMap<Integer, Client> clientMap, String metric) {

        if (metric.equals("Date")) {
            return a.getDate().compareTo(b.getDate());
        } else if (metric.equals("Price")) {
            return Double.compare(priceOf(a, propertyMap), priceOf(b, propertyMap));
        } else if (metric.equals("Client")) {
            return nameOf(a, clientMap).compareToIgnoreCase(nameOf(b, clientMap));
        }
        return Integer.compare(a.getTransactionId(), b.getTransactionId());
    }

    // PRICE OF THE PROPERTY IN A TRANSACTION (HASH MAP LOOKUP)
    private static double priceOf(Transaction transaction, HashMap<Integer, Property> propertyMap) {
        Property property = propertyMap.get(transaction.getPropertyId());
        if (property == null) {
            return 0;
        }
        return property.getPrice();
    }

    // NAME OF THE CLIENT IN A TRANSACTION (HASH MAP LOOKUP)
    private static String nameOf(Transaction transaction, HashMap<Integer, Client> clientMap) {
        Client client = clientMap.get(transaction.getClientId());
        if (client == null) {
            return "";
        }
        return client.getName();
    }

    // MARKET ANALYSIS: COUNTS, AVERAGES, HIGHEST AND LOWEST PRICES
    public static String marketSummary(ArrayList<Property> list) {
        if (list.isEmpty()) {
            return "No properties yet. Add properties in the Properties tab.";
        }

        int available = 0;
        int sold = 0;
        double totalAll = 0;
        double totalAvailable = 0;
        double totalSold = 0;
        Property highest = list.get(0);
        Property lowest = list.get(0);

        for (Property property : list) {
            totalAll += property.getPrice();

            if (property.getStatus().equals("SOLD")) {
                sold++;
                totalSold += property.getPrice();
            } else {
                available++;
                totalAvailable += property.getPrice();
            }

            if (property.getPrice() > highest.getPrice()) {
                highest = property;
            }
            if (property.getPrice() < lowest.getPrice()) {
                lowest = property;
            }
        }

        String averageAvailable = available == 0 ? "-" : money(totalAvailable / available);
        String averageSold = sold == 0 ? "-" : money(totalSold / sold);

        return "Total properties: " + list.size() + "   |   Available: " + available + "   |   Sold: " + sold + "\n"
                + "Average price: " + money(totalAll / list.size()) + "   |   Available average: " + averageAvailable
                + "   |   Sold average: " + averageSold + "\n"
                + "Highest price: " + money(highest.getPrice()) + " (" + highest.getLocation() + ")\n"
                + "Lowest price: " + money(lowest.getPrice()) + " (" + lowest.getLocation() + ")\n"
                + "Total sales value: " + money(totalSold);
    }

    // SALES REPORT: TOTALS, AVERAGE, BEST AND SMALLEST SALE, TOP CLIENT
    public static String salesSummary(ArrayList<Transaction> list, HashMap<Integer, Property> propertyMap,
            HashMap<Integer, Client> clientMap) {

        if (list.isEmpty()) {
            return "No sales yet. Record a sale in the Transactions tab.";
        }

        double total = 0;
        Property highest = null;
        Property lowest = null;
        HashMap<Integer, Integer> salesPerClient = new HashMap<>(); // KEY = clientId, VALUE = NUMBER OF SALES

        for (Transaction transaction : list) {
            Property property = propertyMap.get(transaction.getPropertyId());

            if (property != null) {
                total += property.getPrice();

                if (highest == null || property.getPrice() > highest.getPrice()) {
                    highest = property;
                }
                if (lowest == null || property.getPrice() < lowest.getPrice()) {
                    lowest = property;
                }
            }

            int clientId = transaction.getClientId();
            if (salesPerClient.containsKey(clientId)) {
                salesPerClient.put(clientId, salesPerClient.get(clientId) + 1);
            } else {
                salesPerClient.put(clientId, 1);
            }
        }

        // FINDS THE CLIENT WITH THE MOST PURCHASES
        int topClientId = -1;
        int topCount = 0;
        for (int clientId : salesPerClient.keySet()) {
            if (salesPerClient.get(clientId) > topCount) {
                topCount = salesPerClient.get(clientId);
                topClientId = clientId;
            }
        }

        Client topClient = clientMap.get(topClientId);
        String topClientName = topClient == null ? "(unknown)" : topClient.getName();

        String highestText = highest == null ? "-" : money(highest.getPrice()) + " (" + highest.getLocation() + ")";
        String lowestText = lowest == null ? "-" : money(lowest.getPrice()) + " (" + lowest.getLocation() + ")";

        return "Total sales: " + list.size() + "   |   Total sales value: " + money(total) + "\n"
                + "Average sale price: " + money(total / list.size()) + "\n"
                + "Highest sale: " + highestText + "\n"
                + "Lowest sale: " + lowestText + "\n"
                + "Top client: " + topClientName + " (" + topCount + " purchase" + (topCount == 1 ? "" : "s") + ")";
    }

    // ADDS COMMA EVERY 3 DIGITS
    private static String money(double amount) {
        return String.format("%,.2f", amount);
    }
}
