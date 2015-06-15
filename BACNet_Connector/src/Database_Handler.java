/**
Class: Database_Handler.java
Creator: Christian Koehler
Email: ckoehler@andrew.cmu.edu


**/

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Logger;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import com.serotonin.bacnet4j.util.RequestUtils;

public class Database_Handler {
	
	private static final Logger LOG = Logger.getLogger(RequestUtils.class.toString());
	
	private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
	
    private String USERNAME = "";
    private String PASSWORD = "";
    private String HOSTNAME = "localhost";
    private int PORT = 3306; 
    
    public Database_Handler(String username, String password, String hostname, Integer port) {
    	this.USERNAME = username;
    	this.PASSWORD = password;
    	
    	if (hostname != null)
    		this.HOSTNAME = hostname;
    	
    	if (port != null)
    		this.PORT = port;
    }
    
	private boolean makeConnection(String database){
    	try {
	    	// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = (Connection) DriverManager.getConnection("jdbc:mysql://" + this.HOSTNAME + "/" + database + "?user=" + this.USERNAME + "&password=" + this.PASSWORD);
			return true;
    	}
    	catch (Exception e) {
    		return false;
    	}
    }
    
    public Vector<Room> getBACNetInformation() throws Exception {
    	if (makeConnection("BACNet_Study")) {
    	
    		Vector<Room> rooms = new Vector<Room>(0,1);
    		
	    	try {	    				 
	    		statement = (Statement) connect.createStatement();
	    		resultSet = statement.executeQuery("SELECT * FROM BACNet_Study.Room_Information ORDER BY room_id");
	    		
	    		Room last_room = null;
	    		
	    		if (resultSet.first()) {
	    			while (!resultSet.isAfterLast()) {
	    				
	    				if (last_room == null)
	    					last_room = new Room(resultSet.getInt("room_id"),resultSet.getString("ipv4_address"),resultSet.getInt("port"),resultSet.getInt("network"),resultSet.getInt("address"),resultSet.getInt("flag"));
	    				
	    				if (last_room.getRoomID() == resultSet.getInt("room_id")) {
	    					last_room.addObject(resultSet.getInt("object_type"), resultSet.getInt("object_id"), resultSet.getString("description"));
	    				}
	    				else {
	    					rooms.add(last_room);
	    					last_room = new Room(resultSet.getInt("room_id"),resultSet.getString("ipv4_address"),resultSet.getInt("port"),resultSet.getInt("network"),resultSet.getInt("address"),resultSet.getInt("flag"));
	    					last_room.addObject(resultSet.getInt("object_type"), resultSet.getInt("object_id"), resultSet.getString("description"));
	    				}
	    				
	    				resultSet.next();
	    			}
	    			
	    			rooms.add(last_room);	    				    			
	    		}	    			
			} catch (Exception e) {
			  throw e;
			} finally {
			  close();			  
			}
	    	return rooms;
    	}
    	else {
    		return null;
    	}
    }
    
    public Vector<Room> getBACNetInformationForRoom(int room) throws Exception {
    	if (makeConnection("BACNet_Study")) {
    	
    		Vector<Room> rooms = new Vector<Room>(0,1);
    		
	    	try {	    				 
	    		statement = (Statement) connect.createStatement();
	    		
	    		resultSet = statement.executeQuery("SELECT count(id) AS length FROM BACNet_Study.Room_Information WHERE flag = 1 AND room_id = " + room);
	    		
	    		if (resultSet.first())
	    			if (resultSet.getInt("length") == 0) {
	    				LOG.info("No active rooms!!");	    	    		
	    				return null;
	    			}
	    		
	    		resultSet = statement.executeQuery("SELECT * FROM BACNet_Study.Room_Information WHERE flag = 1 AND room_id = " + room);
	    		
	    		Room last_room = null;
	    			    		
	    		if (resultSet.first()) {
	    			while (!resultSet.isAfterLast()) {
	    				
	    				if (last_room == null)
	    					last_room = new Room(resultSet.getInt("room_id"),resultSet.getString("ipv4_address"),resultSet.getInt("port"),resultSet.getInt("network"),resultSet.getInt("address"),resultSet.getInt("flag"));
	    				
    					last_room.addObject(resultSet.getInt("object_type"), resultSet.getInt("object_id"), resultSet.getString("description"));	    				
	    				
	    				resultSet.next();
	    			}
	    			
	    			rooms.add(last_room);	    				    			
	    		}	    			
			} catch (Exception e) {
				close();
				throw e;
			} finally {
				close();			  
			}
	    	return rooms;
    	}
    	else {
    		return null;
    	}
    }
    
    public boolean saveBACNetData(Vector<Datapoint> values) {
    	if (makeConnection("BACNet_Study")) {
			try {
				String insert_statement = "INSERT INTO Room_Data (room_id,timestamp,date,time,occupancy,temperature,cool_request,heat_request,heating_occupied,cooling_occupied,heating_unoccupied,cooling_unoccupied) VALUES ";
				String occupancy_statement = "INSERT INTO GHC_Test_Data (user_id,timestamp,date,time,time_index,current_location) VALUES ";
				
				Vector<Room_Datapoint> room_data = new Vector<Room_Datapoint>(0,1);
				
				for (int i=0; i < values.size(); i++) {
					
					int room_index = -1;
					
					for (int j=0; j < room_data.size(); j++)
						if (room_data.get(j).getRoomID() == values.get(i).getRoom_id()) {
							room_index = j;
							break;
						}
					
					if (room_index == -1) {
						room_data.add(new Room_Datapoint(values.get(i).getRoom_id(),values.get(i).getTimestamp(),values.get(i).getDate(),values.get(i).getTime()));
						room_index = room_data.size()-1;
					}
					
					if (!room_data.get(room_index).setDataPoint(values.get(i).getObject_type(),values.get(i).getObject_id(),values.get(i).getValue()))
						LOG.info("Unknown object type or id!");
				}
				
				for (int i=0; i < room_data.size()-1; i++) {
					insert_statement = insert_statement + "(" + room_data.get(i) + "),";
					occupancy_statement = occupancy_statement + "(" + room_data.get(i).getMySQLString("GHC_Test_Data") + "),";
				}
				
				insert_statement = insert_statement + "(" + room_data.get(room_data.size()-1) + ")";
				occupancy_statement = occupancy_statement + "(" + room_data.get(room_data.size()-1).getMySQLString("GHC_Test_Data") + ")";
				
				statement = (Statement) connect.createStatement();
				statement.execute(insert_statement);				
				statement.execute(occupancy_statement);
				
			} catch (SQLException e) {			
				e.printStackTrace();
				close();
				return false;
	    	} finally {
			  close();			  
			}
			
	    	return true;
    	}
    	
    	return false;
    }
    	
	private void close() {
	    try {
	      if (resultSet != null)
	        resultSet.close();	    

	      if (statement != null)
	        statement.close();

	      if (connect != null)
	        connect.close();
	    } catch (Exception e) {}
	  }
}