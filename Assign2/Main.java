/*
 *  A sample program to create an Database, and then 
 *  populate the db with 1000 records, using Berkeley DB
 *  
 *  Author: Prof. Li-Yan Yuan, University of Alberta
 * 
 *  A directory named "/tmp/my_db" must be created before testing this program.
 *  You may replace my_db with user_db, where user is your user name, 
 *  as required.
 * 
 *  Modified on March 30, 2007 for Berkeley DB 4.3.28
 *
 *  Modified on April 2, 2016 for Project 2 of CMPUT 291 by Spencer Plant
 */
import com.sleepycat.db.*;
import java.io.*;
import java.util.*;


public class Main {
	
	private static final int CREATE_POPULATE_DATABASE_OPTION = 1;
	private static final int RETRIEVE_RECORD_WITH_GIVEN_KEY_OPTION = 2;
	private static final int RETRIEVE_RECORD_WITH_GIVEN_DATA_OPTION = 3;
	private static final int RETRIEVE_RECORDS_WITH_GIVEN_RANGE_OF_KEY_OPTION = 4;
	private static final int DESTROY_DATABASE_OPTION = 5;
	private static final int QUIT_OPTION = 6;

	private static final int NO_RECORDS = 1000;

	/*
	 *  the main function
	 */
	public static void main(String[] args) {
		boolean quit = false;
		
		String BTREE_option = "1";
		String HASHT_option = "2";

		MyDatabase myBTREEDatabase = new MyDatabase(BTREE_option);
		MyDatabase myHASHTDatabase = new MyDatabase(HASHT_option);

		Scanner in = new Scanner(System.in);
		
		while (!quit) {
			printMenu();
			
			String option = in.nextLine();
		 	try {
				switch(Integer.valueOf(option)){
				case CREATE_POPULATE_DATABASE_OPTION:
					myBTREEDatabase.create();
					myHASHTDatabase.create();
					MyDatabase.populateTables(myBTREEDatabase.getMyDB(), myHASHTDatabase.getMyDB(), NO_RECORDS);
					System.out.println("1000 records inserted into Btree");
					System.out.println("1000 records inserted into Hash");
					break;
				case RETRIEVE_RECORD_WITH_GIVEN_KEY_OPTION:
					System.out.println("Please enter the key and press enter");
					String key = in.nextLine();
					String valueBT = myBTREEDatabase.getValue(key);
					String valueHash = myHASHTDatabase.getValue(key);
					System.out.println("DATA from BTREE: " + valueBT);
					System.out.println("DATA from HASH: " + valueHash);
					break;
				case RETRIEVE_RECORD_WITH_GIVEN_DATA_OPTION:
					System.out.println("Please enter the value and press enter");
					String value1 = in.nextLine();
					String key1 = myBTREEDatabase.getKey(value1);
					System.out.println(key1);
					break;
				case RETRIEVE_RECORDS_WITH_GIVEN_RANGE_OF_KEY_OPTION:
					System.out.println("Please enter lower limit and upper limit of keys to retrieve seperated by space");
					String keys[] = in.nextLine().split(" ");
					String values[] = new String[keys.length];
					for (int i = 0; i < keys.length; i++){
						values[i] = myBTREEDatabase.getValue(keys[i]);
						System.out.println("key: " +  keys[i] + " value: " + values[i]);
					}
					break;
				case DESTROY_DATABASE_OPTION:
					// Used to print the entire database. FOR TESTING ONLY. NEEDS TO BE REMOVED
					myBTREEDatabase.printAll();
					break;
				case QUIT_OPTION:
					quit = true;
					break;
			 	default:
					System.out.println("That's not a valid option");
				}
			} catch (Exception e) {
				System.out.println("That's not a valid option");
			}
		}
	}

	private static void printMenu() {
		System.out.println("\n1. Create and populate a database\n"
				+ "2. Retrieve records with a given key\n"
				+ "3. Retrieve records with a given data\n"
				+ "4. Retrieve records with a given range of key values\n"
				+ "5. Destroy the database\n"
				+ "6. Quit\n");
		
	}
}
