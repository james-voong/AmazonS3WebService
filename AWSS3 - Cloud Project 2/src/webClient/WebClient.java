package webClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import net.java.dev.jaxb.array.StringArray;
import webservice.WebServiceInterface;

public class WebClient {

	private static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	private static WebServiceInterface obj;

	public static void main(String[] args) throws MalformedURLException {
		URL url = new URL("http://localhost:8080/WebService?wsdl");

		// this line takes the targetNameSpace and the name respectively
		QName qname = new QName("http://webService/", "WebServiceImplService");
		Service service = Service.create(url, qname);
		obj = service.getPort(WebServiceInterface.class);
		obj.instantiateClient();
		try {
			setUpMenu();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void setUpMenu() throws IOException {
		// Client for interaction with user
		while (true) {

			System.out.println("\nWhat do you wish to do? (Integer input)");
			System.out.println("1. List Bucket Contents");
			System.out.println("2. Move Objects");
			System.out.println("3. Merge Buckets");
			System.out.println("4. Split Buckets");

			int answer = 0;

			// Read the user input and save it
			try {
				answer = Integer.parseInt(input.readLine());
			} catch (Exception e) {
			}

			// Call method that user wanted
			if (answer == 1) {
				System.out.println("\nPlease wait...");
				StringArray contents = obj.listContents();
				for (int i = 0; i < contents.getItem().size(); i++) {
					if (contents.getItem().get(i) == null) {
						break;
					} else
						System.out.println(contents.getItem().get(i));
				}

			} else if (answer == 2) {
				moveObjectsClient();
			} else if (answer == 3) {
				mergeBucketsClient();
			} else if (answer == 4) {
				splitBucketClient();
			} else
				System.out.println("\nInvalid option. Try again.\n");
		}

	}

	/** Client for when moveObjects is to be used */
	public static void moveObjectsClient() throws IOException {
		System.out.println("\nPlease wait...");
		StringArray contents = obj.listContents();
		for (int i = 0; i < contents.getItem().size(); i++) {
			if (contents.getItem().get(i) == null) {
				break;
			} else
				System.out.println(contents.getItem().get(i));
		}

		// Get the total number of buckets
		int totalBucketNumber = obj.totalNumberOfBuckets();
		int totalNumberOfItems = 0;
		int bucketToMoveFrom = 0;
		int itemToMove = 0;
		int bucketToMoveTo = 0;
		boolean bucketToMoveFrom_Accepted = false;
		boolean itemToMove_Accepted = false;
		boolean bucketToMoveTo_Accepted = false;

		// Inside a while loop to prevent invalid bucket selection by user
		while (bucketToMoveFrom_Accepted == false) {
			System.out.println("\nPlease select which bucket to move an item from:");

			// Save the selected bucket number
			try {
				bucketToMoveFrom = Integer.parseInt(input.readLine());
			} catch (Exception e) {
			}

			// Get the total number of items inside bucket
			totalNumberOfItems = obj.totalNumberOfItemsInsideBucket(bucketToMoveFrom);

			// If invalid input selected
			if (bucketToMoveFrom <= 0 || bucketToMoveFrom > totalBucketNumber) {
				System.out.println("\nSelected bucket does not exist, try again.");
			}
			// If the bucket is empty
			else if (totalNumberOfItems <= 0) {
				System.out.println("\nBucket is empty. Select a different bucket.");
			} else
				bucketToMoveFrom_Accepted = true;
		}

		while (itemToMove_Accepted == false) {
			System.out.println("\nPlease select which item to move out of bucket " + bucketToMoveFrom);

			// Save the selected item number
			try {
				itemToMove = Integer.parseInt(input.readLine());
			} catch (Exception e) {
			}

			if (itemToMove <= 0 || itemToMove > totalNumberOfItems) {
				System.out.println("\nSelected item does not exist, try again.");
			} else
				itemToMove_Accepted = true;
		}

		while (bucketToMoveTo_Accepted == false) {
			System.out.println("\nWhich bucket do you wish to move it to?");

			// Save the new bucket number

			try {
				bucketToMoveTo = Integer.parseInt(input.readLine());
			} catch (Exception e) {
			}

			if (bucketToMoveTo <= 0 || bucketToMoveTo > totalBucketNumber) {
				System.out.println("\nSelected bucket does not exist, try again.");
			} else if (bucketToMoveTo == bucketToMoveFrom) {
				System.out.println("\nItem is already in that bucket, pick a different bucket.");
			}

			else {
				bucketToMoveTo_Accepted = true;
			}
		}

		System.out.println(obj.moveTheObjects(bucketToMoveFrom, itemToMove, bucketToMoveTo));

	}

	/** Client for when mergeBuckets is to be used */
	public static void mergeBucketsClient() throws IOException {
		System.out.println("\nPlease wait...");
		StringArray contents = obj.listContents();
		for (int i = 0; i < contents.getItem().size(); i++) {
			if (contents.getItem().get(i) == null) {
				break;
			} else
				System.out.println(contents.getItem().get(i));
		}
		int totalBuckets = obj.totalNumberOfBuckets();
		int bucketToRemain = 0;
		int bucketToDelete = 0;
		boolean bucketToRemain_Accepted = false;
		boolean bucketToDelete_Accepted = false;

		while (bucketToRemain_Accepted == false) {
			System.out.println("\nSelect which bucket will remain: ");

			try {
				bucketToRemain = Integer.parseInt(input.readLine());
			} catch (Exception e) {
			}

			if (bucketToRemain <= 0 || bucketToRemain > totalBuckets) {
				System.out.println("\nInvalid bucket selected. Try again.");
			} else
				bucketToRemain_Accepted = true;
		}
		while (bucketToDelete_Accepted == false) {
			System.out.println("\nSelect which bucket will be deleted: ");
			try {
				bucketToDelete = Integer.parseInt(input.readLine());
			} catch (Exception e) {
			}
			if (bucketToDelete <= 0 || bucketToDelete > totalBuckets) {
				System.out.println("\nInvalid bucket selected. Try again.");
			} else
				bucketToDelete_Accepted = true;
		}
		System.out.println(obj.mergeTheBuckets(bucketToRemain, bucketToDelete));
	}

	/** Client for when splitBucket is to be used */
	public static void splitBucketClient() {
		System.out.println("\nPlease wait...");
		StringArray contents = obj.listContents();
		for (int i = 0; i < contents.getItem().size(); i++) {
			if (contents.getItem().get(i) == null) {
				break;
			} else
				System.out.println(contents.getItem().get(i));
		}
		int totalBuckets = obj.totalNumberOfBuckets();
		int bucketToSplit = 0;
		int itemSplitPoint = 0;
		boolean bucketToSplit_Accepted = false;
		boolean itemSplitPoint_Accepted = false;

		while (bucketToSplit_Accepted == false) {
			System.out.println("\nSelect which bucket to split:");

			try {
				bucketToSplit = Integer.parseInt(input.readLine());
			} catch (Exception e) {
			}

			if (bucketToSplit <= 0 || bucketToSplit > totalBuckets) {
				System.out.println("\nInvalid bucket selected. Try again.");
			} else
				bucketToSplit_Accepted = true;
		}

		int totalNumberOfItems = obj.totalNumberOfItemsInsideBucket(bucketToSplit);

		while (itemSplitPoint_Accepted == false) {
			System.out.println("\nAt which item should the bucket be split? (Inclusive)");

			// Save the selected item number
			try {
				itemSplitPoint = Integer.parseInt(input.readLine());
			} catch (Exception e) {
			}

			if (itemSplitPoint <= 0 || itemSplitPoint > totalNumberOfItems) {
				System.out.println("\nSelected item does not exist, try again.");
			} else
				itemSplitPoint_Accepted = true;
		}
		System.out.println(obj.splitTheBuckets(bucketToSplit, itemSplitPoint));
	}

}
