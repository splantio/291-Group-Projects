/* Note: Java has the auto commit feature. Thus, all changes made are final and can be seen in the DB instantly.
 * javac CreateToffees.java
 * java CreateToffees
 * 
 * Based off of Java/SQL tutorial from CMPUT 291 Lab by Kriti Khare
 * 9 March 2016
 * Spencer Plant
 */

package bin;

import java.util.*;

import javax.swing.JOptionPane;

import java.sql.*; // Java package for accessing Oracle
import java.io.*; // Java package includes Console for getting password from user and printing to screen

// 
public class AutoTransaction extends GroupProject1 {
	private String transactionID;
	private String buyerSIN;
	private String sellerSIN;
	private String vehicleID;
	private String date;
	private float price;
	
	public AutoTransaction(String transactionId, String buyerSin, String sellerSin, String vehicleId, String date1, float price1) {
		super();
		transactionID = transactionId.trim();
		buyerSIN = buyerSin.trim();
		sellerSIN = sellerSin.trim();
		vehicleID = vehicleId.trim();
		date = date1.trim();
		price = price1;
	}
	
	// Method to make a transaction in the database. Checks to see if fields 
	// are filled, and checks database validity of queries. Returns 1 on success
	public int MakeTransaction() {
		// Check to make sure none of the essential fields are blank
		if ((buyerSIN.isEmpty()) || (sellerSIN.isEmpty()) || (vehicleID.isEmpty())){
			JOptionPane.showMessageDialog(frame, "Error. Please make sure buyer, seller, and vehicle are specified.");
			System.out.println("got here");
			return 0;
		}
		
		// Verify that Vehicle is in DB
		try {
			// check for vehicle
			String query = "SELECT serial_no " +
						   "FROM vehicle " +
						   "WHERE serial_no='"+vehicleID+"'";
			Statement stmt = m_con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = stmt.executeQuery(query);
			if (!rs.next()){
	            JOptionPane.showMessageDialog(frame, "Error. Vehicle serial number not found in database.");
	            return 0;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("checking for vehicle");
            return 0;
		}
		
		// Verify that Seller is the owner of the vehicle
		try {
			// check to see that seller owns vehicle
			String query = "SELECT owner_id " +
						   "FROM owner " +
						   "WHERE owner_id='"+sellerSIN+"' AND" +
						   		 "vehicle_id='"+vehicleID+"'";
			Statement stmt = m_con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = stmt.executeQuery(query);
			if (!rs.next()){
				JOptionPane.showMessageDialog(frame, "Error. Seller is not owner of specified vehicle.");
				return 0;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("checking for seller");
            return 0;
		}
		
		// Passed verifications. proceed with transaction.
		try {
			String insertAutoTransaction = "insert into auto_sale values(" +
					transactionID+", " +
					"'"+sellerSIN+"', " +
					"'"+buyerSIN+"', " +
					"'"+vehicleID+"', " +
					date+", " +
					price+")";
			Statement stmt = m_con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			stmt.executeUpdate(insertAutoTransaction);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("making transaction");
			return 0;
		}
		
		return 1;
	}
}
