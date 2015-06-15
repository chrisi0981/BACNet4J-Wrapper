public class Room_Datapoint {
	
	private int roomID;
	private long timestamp;
	private String date;
	private String time;
	private int occupancy;
	private double temperature;
	private double coolRequest;
	private double heatRequest;
	private double heatingUnoccupied;
	private double coolingUnoccupied;
	private double heatingOccupied;
	private double coolingOccupied;
	
	public Room_Datapoint(int room_id, long timestamp, String date, String time) {
		this.setRoomID(room_id);
		this.timestamp = timestamp;
		this.date = date;
		this.time = time;
	}

	public boolean setDataPoint(int objectType, int objectID, double value) {
		
		if (objectType == 0 && objectID == 2) {
			this.setTemperature(value);
			return true;
		}
		
		if (objectType == 2 && objectID == 4) {
			this.setCoolRequest(value);
			return true;
		}
		
		if (objectType == 2 && objectID == 6) {
			this.setHeatRequest(value);
			return true;
		}
		
		if (objectType == 2 && objectID == 8) {
			this.setCoolingOccupied(value);
			return true;
		}
		
		if (objectType == 2 && objectID == 9) {
			this.setHeatingOccupied(value);
			return true;
		}
		
		if (objectType == 2 && objectID == 10) {
			this.setCoolingUnoccupied(value);
			return true;
		}
		
		if (objectType == 2 && objectID == 11) {
			this.setHeatingUnoccupied(value);
			return true;
		}
		
		if (objectType == 3 && objectID == 1) {
			this.setOccupancy((int)value);
			return true;
		}
		
		return false;
	}

	public int getRoomID() {
		return roomID;
	}

	public void setRoomID(int roomID) {
		this.roomID = roomID;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getOccupancy() {
		return occupancy;
	}

	public void setOccupancy(int occupancy) {
		this.occupancy = occupancy;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public double getCoolRequest() {
		return coolRequest;
	}

	public void setCoolRequest(double coolRequest) {
		this.coolRequest = coolRequest;
	}

	public double getHeatRequest() {
		return heatRequest;
	}

	public void setHeatRequest(double heatRequest) {
		this.heatRequest = heatRequest;
	}

	public double getHeatingUnoccupied() {
		return heatingUnoccupied;
	}

	public void setHeatingUnoccupied(double heatingUnoccupied) {
		this.heatingUnoccupied = heatingUnoccupied;
	}

	public double getCoolingUnoccupied() {
		return coolingUnoccupied;
	}

	public void setCoolingUnoccupied(double coolingUnoccupied) {
		this.coolingUnoccupied = coolingUnoccupied;
	}

	public double getHeatingOccupied() {
		return heatingOccupied;
	}

	public void setHeatingOccupied(double heatingOccupied) {
		this.heatingOccupied = heatingOccupied;
	}

	public double getCoolingOccupied() {
		return coolingOccupied;
	}

	public void setCoolingOccupied(double coolingOccupied) {
		this.coolingOccupied = coolingOccupied;
	}

	public String getMySQLString(String database) {
		
		if (database.equals("GHC_Test_Data")) {
			int timeIndex = 0;
			String[] timeArray = this.time.split(":");
			timeIndex = (int) Math.floor((Double.valueOf(timeArray[0])*60 + Double.valueOf(timeArray[1]))/5);
			
			return "'" + this.roomID + "','" + this.timestamp + "','" + this.date + "','" + this.time + "','" + timeIndex + "','" + this.occupancy + "'"; 
		}
		
		return null;
	}
	
	@Override
	public String toString() {
		// MySQL String: (room_id,timestamp,date,time,occupancy,temperature,cool_request,heat_request,heating_unoccupied,cooling_unoccupied,heating_occupied,cooling_occupied)
		return  "'" + this.roomID + "','" + this.timestamp + "','" + this.date + "','" + this.time + "','" + this.occupancy + "','" +
				this.temperature + "','" + this.coolRequest + "','" + this.heatRequest + "','" +
				this.heatingOccupied + "','" + this.coolingOccupied + "','" +
				this.heatingUnoccupied + "','" + this.coolingUnoccupied + "'";
	}
	
}