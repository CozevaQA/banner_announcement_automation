package resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.opencsv.CSVReader;

public class CSVutility {
	private CSVParser parser;
	private String fileLocation;
//	private String fileName;
	
	public void readCSVwithHeader(String filelocation) throws FileNotFoundException, IOException {
		this.fileLocation = filelocation;
//		this.fileName = fileName;
		
		parser = new CSVParser(new FileReader(this.fileLocation), CSVFormat.DEFAULT.withHeader());
	}
	
	public HashMap<String, String> loadUserData(){
		HashMap<String, String> userData = new HashMap<String,String>();
		
		for(CSVRecord record: parser) {
			String key = record.get("KEY");
			String value = record.get("VALUE");
			
			userData.put(key, value);
		}
		
		return userData;
	}
	
	public HashMap<Integer, Object> getCustomerIDToCustomerDetails(){
		HashMap<Integer, Object> custData = new HashMap<Integer, Object>();
		
		for(CSVRecord record: parser) {
			List<String> custDetails = new ArrayList<String>();
			
			Integer custID = Integer.parseInt(record.get("Customer ID"));// record.get("Customer ID");
			
			String shore = record.get("Onshore/Offshore");//Onshore/Offshore
			String baModal = record.get("Banner/Announcement Modal");//Banner/Announcement Modal
			String userFilterData = record.get("Userlist Filter");//Userlist Filter
			
			custDetails.add(shore);
			custDetails.add(baModal);
			custDetails.add(userFilterData);
			
			custData.put(custID, custDetails);
		}
		
		return custData;
	}
	
	public List<Integer> getAllCustomerID() throws FileNotFoundException, IOException{
		List<Integer> allCustIDs = new ArrayList<Integer>();
		parser = new CSVParser(new FileReader(this.fileLocation), CSVFormat.DEFAULT.withHeader());
		for(CSVRecord record: parser) {
			Integer custID = Integer.parseInt(record.get("Customer ID"));// record.get("Customer ID");
			
			allCustIDs.add(custID);
		}
		
		return allCustIDs;
	}
	
	
	public void closeCSV() throws IOException {
		parser.close();
	}
	
	public HashMap<String, Object> getRoleAbbrToRole(){
		HashMap<String, Object> roleData = new HashMap<String, Object>();
		
		for(CSVRecord record: parser) {
			ArrayList<String> roleDetails = new ArrayList<String>();
			
			String roleAbbr = record.get("Role abbreviation");// Role abbreviation
			
			String roleAtBAModal = record.get("Rolename at banner announcement modal");//Rolename at banner announcement modal
			String roleAtUserlistFilter = record.get("Rolename at userlist filter");//Rolename at userlist filter
			
			roleDetails.add(roleAtBAModal);
			roleDetails.add(roleAtUserlistFilter);
			
			roleData.put(roleAbbr, roleDetails);
		}
		
		return roleData;
	}
}
