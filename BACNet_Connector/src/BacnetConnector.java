import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.service.confirmed.WritePropertyRequest;
import com.serotonin.bacnet4j.transport.Transport;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.enumerated.Segmentation;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import com.serotonin.bacnet4j.type.primitive.Real;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.bacnet4j.util.PropertyValues;
import com.serotonin.bacnet4j.util.RequestUtils;

public class BacnetConnector{
    static LocalDevice localDevice;
    private static String sourceIP = "128.2.207.3";
    
    private static Vector<Room> rooms = new Vector<Room>(0,1);    
    
    private static String USERNAME;
    private static String PASSWORD;

    public static void main(String[] args) throws Exception {        
        
    	if (args.length >= 3) {
	    	USERNAME = args[0];
	    	PASSWORD = args[1];
	    	
	    	int CHOICE = Integer.valueOf(args[2]);    	
	    	
	    	Database_Handler dbHandler = new Database_Handler(USERNAME,PASSWORD,null,null);
	    	
	    	switch(CHOICE) {
	    		case 0: rooms = dbHandler.getBACNetInformation();
			        	    	
			        	if (rooms != null)
			        		getCurrentValues(sourceIP,rooms);
			        	
			        	break;
	    		case 1: if (args.length == 6) {
	    					rooms = dbHandler.getBACNetInformationForRoom(Integer.valueOf(args[3]));		        	
			        	    	
				        	if (rooms != null)
				        		setCurrentValue(sourceIP,rooms,Integer.valueOf(args[4]),Double.valueOf(args[5]));
	    				}
	    				break;
	    		case 2: testRead();
	    				break;
	    	}
    	}
    }
    
    public static void getCurrentValues(String currentIP,Vector<Room> rooms) {    	    	    
        try {										                   	           
            for (int j=0; j < rooms.size(); j++) {            	
        		try {	 
            		IpNetwork network = new IpNetwork(rooms.get(j).getIpv4Address(),rooms.get(j).getPort(),currentIP,0);
        	        Transport transport = new Transport(network);
        	    	
        	    	localDevice = new LocalDevice(1234, transport);
        	    	localDevice.initialize();
            		
		            RemoteDevice rd = localDevice.getRemoteDeviceCreate(rooms.get(j).getInstanceID(),
		            													new Address(rooms.get(j).getNetworkNumber(),
		            																"" + rooms.get(j).getNetworkMAC()),
		            																new OctetString(rooms.get(j).getIpv4Address() + ":" + rooms.get(j).getPort()));
		            rd.setSegmentationSupported(Segmentation.noSegmentation);
		            rd.setMaxAPDULengthAccepted(1476);
		            
		            List<ObjectIdentifier> oids = new ArrayList<ObjectIdentifier>();
		            Vector<Integer> valid_rooms = new Vector<Integer>(0,1);
		            
		            for (int i=0; i < rooms.get(j).getObjectTypes().size(); i++) {			            			            			            
		            	oids.add(new ObjectIdentifier(new ObjectType(rooms.get(j).getObjectTypes().get(i)),rooms.get(j).getObjectIDs().get(i)));
		            	valid_rooms.add(i);		            				            	
		            }
		            
		            PropertyValues pvs = RequestUtils.readOidPresentValues(localDevice, rd, oids, null);
		            
		            Vector<Datapoint> datapoints = new Vector<Datapoint>(0,1);
		            
		            Calendar mydate = Calendar.getInstance();
		            mydate.setTimeInMillis(System.currentTimeMillis());			            
		            
		            for (int i=0; i < oids.size(); i++) {
		            	Datapoint dp = new Datapoint(rooms.get(j).getRoomID(),
		            								 rooms.get(j).getObjectTypes().get(i),
		            								 rooms.get(j).getObjectIDs().get(i),
		            								 rooms.get(j).getObjectDescription().get(i),
		            								 System.currentTimeMillis()/1000,
		            								 mydate.get(Calendar.YEAR) + "-" + (mydate.get(Calendar.MONTH)+1) + "-" +  mydate.get(Calendar.DAY_OF_MONTH),
		            								 mydate.get(Calendar.HOUR_OF_DAY) + ":" + mydate.get(Calendar.MINUTE) + ":" + mydate.get(Calendar.SECOND),
		            								 Double.valueOf(pvs.get(oids.get(i),PropertyIdentifier.presentValue).toString()));
		            	datapoints.add(dp);
		            	//System.out.println("" + rooms.get(j).getObjectDescription().get(i) + ": " + pvs.get(oids.get(i),PropertyIdentifier.presentValue));
		            }
		            
		            Database_Handler dbHandler = new Database_Handler(USERNAME,PASSWORD,null,null);
		            dbHandler.saveBACNetData(datapoints);
		            
        		} catch (Exception e) { 
    				e.printStackTrace();
    			}
    	        finally {
    	            localDevice.terminate();
    	        }            	
            }
            // Write a value:
            //System.out.println(localDevice.send(rd,new WritePropertyRequest(ai3, PropertyIdentifier.presentValue, null, new Real(70), new UnsignedInteger(14))));
	            	        
		} catch (Exception e1) {
			e1.printStackTrace();
		}    	
    }
       
    public static void setCurrentValue(String currentIP, Vector<Room> rooms, int actuator, double value) {
    	
    	int[] object = Room.getActuator(actuator);
    	
    	if (object != null && rooms.size() == 1) {
    		
	    	try {
	    		IpNetwork network = new IpNetwork(rooms.get(0).getIpv4Address(),rooms.get(0).getPort(),currentIP,0);
		        Transport transport = new Transport(network);
		    	
		    	localDevice = new LocalDevice(1234, transport);
	    		
	    		localDevice.initialize();
	    		
	    		RemoteDevice rd = localDevice.getRemoteDeviceCreate(rooms.get(0).getInstanceID(),
						new Address(rooms.get(0).getNetworkNumber(),
									"" + rooms.get(0).getNetworkMAC()),
									new OctetString(rooms.get(0).getIpv4Address() + ":" + rooms.get(0).getPort()));
				rd.setSegmentationSupported(Segmentation.noSegmentation);
				rd.setMaxAPDULengthAccepted(1476);
				
				if (actuator > 0)
					localDevice.send(rd,new WritePropertyRequest(new ObjectIdentifier(new ObjectType(object[0]),object[1]), PropertyIdentifier.presentValue, null, new Real((float) value), new UnsignedInteger(7)));
				else
					localDevice.send(rd,new WritePropertyRequest(new ObjectIdentifier(new ObjectType(object[0]),object[1]), PropertyIdentifier.presentValue, null, new UnsignedInteger((int) value), new UnsignedInteger(7)));
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	finally {
	            localDevice.terminate();
	        }
            
    	}
    }
    
    public static void testRead() {
    	IpNetwork network = new IpNetwork("128.2.113.232",47808,"128.237.221.11",0);
        Transport transport = new Transport(network);
    	
    	localDevice = new LocalDevice(1234, transport);
    	try {
			localDevice.initialize();
			
			RemoteDevice rd = localDevice.getRemoteDeviceCreate(240307,
					new Address(24030,
								"7"),
								new OctetString("128.2.113.232" + ":" + 47808));
			rd.setSegmentationSupported(Segmentation.noSegmentation);
			rd.setMaxAPDULengthAccepted(1476);
			
			List<ObjectIdentifier> oids = new ArrayList<ObjectIdentifier>();
			oids.add(new ObjectIdentifier(new ObjectType(0),2));
			
			PropertyValues pvs = RequestUtils.readOidPresentValues(localDevice, rd, oids, null);
			
			System.out.println(pvs.get(oids.get(0),PropertyIdentifier.presentValue));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        
        
    }
}