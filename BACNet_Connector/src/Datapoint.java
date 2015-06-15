public class Datapoint {
    	
    	private int room_id;
    	private int object_type;
    	private int object_id;
    	private String description;
    	private long timestamp;
    	private String date;
    	private String time;
    	private double value;
    	
    	public Datapoint(int room_id, int object_type, int object_id, String description, long timestamp, String date, String time, double value) {
    		this.setRoom_id(room_id);
    		this.setObject_type(object_type);
    		this.setObject_id(object_id);
    		this.setDescription(description);
    		this.setTimestamp(timestamp);
    		this.setDate(date);
    		this.setTime(time);
    		this.setValue(value);
    	}    	
    	
		public int getRoom_id() {
			return room_id;
		}

		public void setRoom_id(int room_id) {
			this.room_id = room_id;
		}

		public int getObject_type() {
			return object_type;
		}

		public void setObject_type(int object_type) {
			this.object_type = object_type;
		}

		public int getObject_id() {
			return object_id;
		}

		public void setObject_id(int object_id) {
			this.object_id = object_id;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
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

		public double getValue() {
			return value;
		}

		public void setValue(double value) {
			this.value = value;
		}
		
		@Override
		public String toString() {
			return "" + this.value;
		}
    }