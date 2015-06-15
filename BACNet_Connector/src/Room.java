/**
Class: Room.java
Creator: Christian Koehler
Email: ckoehler@andrew.cmu.edu

This class encapsulates the BACNet room information needed to access sensors in the Gates Hillman BACNet.
**/

import java.util.Vector;

public class Room {
	
	private int roomID;
	private String ipv4Address;
	private int port;
	private int networkNumber;
	private int networkMAC;
	private int instanceID;
	private int flag;
	private Vector<Integer> objectTypes;
	private Vector<Integer> objectIDs;
	private Vector<String> objectDescription;
	
	public static int[] OCCUPANCY_SENSOR = {3,1};
	public static int[] COOL_OCCUPIED = {2,8};
	public static int[] HEAT_OCCUPIED = {2,9};
	public static int[] COOL_UNOCCUPIED = {2,10};
	public static int[] HEAT_UNOCCUPIED = {2,11};
	
	public Room(int roomID, String ipv4Address, int port, int networkNumber, int networkMAC,int flag) {
		this.setRoomID(roomID);
		this.setIpv4Address(ipv4Address);
		this.setPort(port);
		this.setNetworkNumber(networkNumber);
		this.setNetworkMAC(networkMAC);
		this.setFlag(flag);
		this.setInstanceID(new Integer("" + networkNumber + "" + networkMAC));
		this.objectTypes = new Vector<Integer>(0,1);
		this.objectIDs = new Vector<Integer>(0,1);
		this.objectDescription = new Vector<String>(0,1);
	}

	public void addObject(int type, int id, String description) {
		this.objectTypes.add(type);
		this.objectIDs.add(id);
		this.objectDescription.add(description);
	}
	
	public static int[] getActuator(int type) {
		switch(type) {
			case 0: return OCCUPANCY_SENSOR;
			case 1: return COOL_OCCUPIED;
			case 2: return HEAT_OCCUPIED;
			case 3: return COOL_UNOCCUPIED;
			case 4: return HEAT_UNOCCUPIED;
			default: return null;
		}
	}
	
	public int getRoomID() {
		return roomID;
	}

	public void setRoomID(int roomID) {
		this.roomID = roomID;
	}

	public String getIpv4Address() {
		return ipv4Address;
	}

	public void setIpv4Address(String ipv4Address) {
		this.ipv4Address = ipv4Address;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getNetworkNumber() {
		return networkNumber;
	}

	public void setNetworkNumber(int networkNumber) {
		this.networkNumber = networkNumber;
	}

	public int getNetworkMAC() {
		return networkMAC;
	}

	public void setNetworkMAC(int networkMAC) {
		this.networkMAC = networkMAC;
	}

	public int getInstanceID() {
		return instanceID;
	}

	public void setInstanceID(int instanceID) {
		this.instanceID = instanceID;
	}

	public Vector<Integer> getObjectTypes() {
		return objectTypes;
	}

	public void setObjectTypes(Vector<Integer> objectTypes) {
		this.objectTypes = objectTypes;
	}

	public Vector<Integer> getObjectIDs() {
		return objectIDs;
	}

	public void setObjectIDs(Vector<Integer> objectIDs) {
		this.objectIDs = objectIDs;
	}

	public Vector<String> getObjectDescription() {
		return objectDescription;
	}

	public void setObjectDescription(Vector<String> objectDescription) {
		this.objectDescription = objectDescription;
	}
	
	@Override
	public String toString() {
		String output = "" + this.roomID + "," + this.networkNumber + "," + this.networkMAC + "," + this.instanceID + "\n";
		
		for (int i=0; i < this.objectIDs.size(); i++)
			output = output + "(" + this.objectTypes.get(i) + "," + this.objectIDs.get(i) + "," + this.objectDescription.get(i) + ") ";
		
		return output;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
}