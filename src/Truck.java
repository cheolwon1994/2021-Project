import java.util.ArrayList;
import java.util.List;

public class Truck {
	private int id;
	private int location_id;
	private int loaded_bikes_count;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLocation_id() {
		return location_id;
	}
	public void setLocation_id(int location_id) {
		this.location_id = location_id;
	}
	public int getLoaded_bikes_count() {
		return loaded_bikes_count;
	}
	public void setLoaded_bikes_count(int loaded_bikes_count) {
		this.loaded_bikes_count = loaded_bikes_count;
	}
	
	public Truck(int id, int location_id, int loaded_bikes_count) {
		super();
		this.id = id;
		this.location_id = location_id;
		this.loaded_bikes_count = loaded_bikes_count;
	}
	
	/*public int check(int num, int mini, int maxi) {
		int tid = this.id;
		int tx = getX(num,tid);
		int ty = getY(num,tid);
		int miniX = getX(num,mini);
		int miniY = getY(num,mini);
		int maxiX = getX(num,maxi);
		int maxiY = getY(num,maxi);
		int firstDist = Math.abs(maxiY-ty)+Math.abs(maxiX-tx);
		int SecDist = Math.abs(maxiY-miniY)+Math.abs(maxiX-miniX);
		int remain = 10 - (firstDist+SecDist);
		if(remain<2) return -1;
		return remain/2;
	}
	*/
}
