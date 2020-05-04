package patient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Patient {
	// A common method to connect to the DB
	private Connection connect() {
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");

			// Provide the correct details: DBServer/DBName, username, password
			con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/health_care", "root", "root");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}



	public String readPatients()
	{
		String output = "";
		
		try
		{
			Connection con = connect();
			if (con == null)
			{
				return "Error while connecting to the database for reading.";
			}
			// Prepare the html table to be displayed
			output = "<table border='1'><tr><th>Patient Name</th> <th>Patient Address</th><th>Patient Phone</th>" 
			+ "<th>Patient NIC</th> <th>Update</th><th>Delete</th></tr>";
			
			String query = "select * from patient";
			Statement s = con.createStatement();
			ResultSet rs = s.executeQuery(query);
	 
			// iterate through the rows in the result set
			while (rs.next())
			{
				String patientID = Integer.toString(rs.getInt("patientID"));
				String patientName = rs.getString("patientName");
				String patientAddress = rs.getString("patientAddress");
				String patientPhone = Integer.toString(rs.getInt("patientPhone"));
				String patientNIC = rs.getString("patientNIC"); 
	
				// Add into the html table
				output += "<tr><td><input id='hidPatientIDUpdate'name='hidPatientIDUpdate' type='hidden'value='" + patientID + "'>" + patientName + "</td>";
				output += "<td>" + patientAddress + "</td>";
				output += "<td>" + patientPhone + "</td>";
				output += "<td>" + patientNIC + "</td>";
	
				// buttons
				output += "<td><input name='btnUpdate' type='button'value='Update' class='btnUpdate btn btn-secondary'></td><td><input name='btnRemove' type='button'value='Delete'class='btnRemove btn btn-danger' data-patientid='"
						+ patientID + "'>" + "</td></tr>";
			}
	 
			con.close();
	 
			// Complete the html table
	 
			output += "</table>";
	 }
	catch (Exception e)
	 {
	 output = "Error while reading the patient details.";
	 System.err.println(e.getMessage());
	 }
	return output;
	}
	
	public String insertPatient(String name, String address, String phone, String NIC) {
		String output = "";
		try {
			Connection con = connect();
			if (con == null) {
				return "Error while connecting to the database for inserting.";
			}
			// create a prepared statement
			String query = " insert into patient(`patientID`,`patientName`,`patientAddress`,`patientPhone`,`patientNIC`)"
					+ " values (?, ?, ?, ?, ?)";
			PreparedStatement preparedS = con.prepareStatement(query);
			// binding values
			
			preparedS.setInt(1, 0); 
			preparedS.setString(2, name);
			preparedS.setString(3, address); 
			preparedS.setInt(4,Integer.parseInt(phone)); 
			  preparedS.setString(5, NIC);
			 
			
			
			// execute the statement
			  preparedS.execute();
			con.close();
			
			//output = "Inserted successfully";
			
			String newPatients = readPatients();
			 output = "{\"status\":\"success\", \"data\": \"" +
					 newPatients + "\"}"; 
			 
			 
		} catch (Exception e) {
		//	output = "Error while inserting the patient.";
			output = "{\"status\":\"error\", \"data\": \"Error while inserting the patient.\"}"; 
			System.err.println(e.getMessage());
		}
		return output;
	}
	
	public String updatePatient(String ID, String name, String address, String phone, String NIC) {
		String output = "";
		try {
			Connection con = connect();
			if (con == null) {
				return "Error while connecting to the database for updating.";
			}
			// create a prepared statement
			String query = "UPDATE patient SET patientName=?,patientAddress=?,patientPhone=?,patientNIC=? WHERE patientID=?";
			PreparedStatement preparedS = con.prepareStatement(query);
			// binding values
			preparedS.setString(1, name);
			preparedS.setString(2, address);
			preparedS.setInt(3, Integer.parseInt(phone));
			preparedS.setString(4, NIC);
			preparedS.setInt(5, Integer.parseInt(ID));
			// execute the statement
			preparedS.execute();
			con.close();
			//output = "Updated successfully";
			String newPatient = readPatients();
			 output = "{\"status\":\"success\", \"data\": \"" +
			 newPatient + "\"}"; 
		} catch (Exception e) {
			//output = "Error while updating the patient.";
			output = "{\"status\":\"error\", \"data\": \"Error while updating the patient.\"}"; 
			System.err.println(e.getMessage());
		}
		return output;
	}

	public String deletePatient(String patientID) {
		String output = "";
		try {
			Connection con = connect();
			if (con == null) {
				return "Error while connecting to the database for deleting.";
			}
			// create a prepared statement
			String query = "delete from patient where patientID=?";
			PreparedStatement preparedStmt = con.prepareStatement(query);
			// binding values
			preparedStmt.setInt(1, Integer.parseInt(patientID));
			// execute the statement
			preparedStmt.execute();
			con.close();
			//output = "Deleted successfully";
			String newPatients = readPatients();
			 output = "{\"status\":\"success\", \"data\": \"" +
					 newPatients + "\"}"; 
		} catch (Exception e) {
			//output = "Error while deleting the patient.";
			output = "{\"status\":\"error\", \"data\": \"Error while deleting the patient.\"}"; 
			System.err.println(e.getMessage());
		}
		return output;
	}
}
