import java.util.ArrayList;
import java.util.List;

public class Truck {
	private int id;
	private int location_id;
	private int loaded_bikes_count;
	private List<Integer> command;
	
	public List<Integer> getCommand() {
		return command;
	}
	public void setCommand(List<Integer> command) {
		this.command = command;
	}
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
		this.command = new ArrayList<>();
		this.id = id;
		this.location_id = location_id;
		this.loaded_bikes_count = loaded_bikes_count;
	}
	
	public boolean check(int num, int mini, int maxi, int diff) {
		int tid = this.id;
		int tx = getX(num,tid);
		int ty = getY(num,tid);
		int miniX = getX(num,mini);
		int miniY = getY(num,mini);
		int maxiX = getX(num,maxi);
		int maxiY = getY(num,maxi);
		int firstDist = Math.abs(maxiY-ty)+Math.abs(maxiX-tx);
		int SecDist = Math.abs(maxiY-miniY)+Math.abs(maxiX-miniX);
		if(firstDist+SecDist+2*diff>10) return false;
		return true;
	}
	
	public void move(int num, int idx) {
		int tid = this.id;
		int tx = getX(num,tid);
		int ty = getY(num,tid);
		int lx = getX(num,idx);
		int ly = getY(num,idx);
		
		while(tx<lx) {		//오른쪽으로 이동
			command.add(2);
			tx++;
		}
		while(tx>lx) {		//왼쪽으로 이동
			command.add(4);
			tx--;
		}
		
		while(ty<ly) {		//아래로 이동
			command.add(3);
			ty++;
		}
		while(ty>ly) {		//위로 이동
			command.add(1);
			ty--;
		}
		this.id = idx;
	}
	
	public void uploadBike() {
		command.add(5);
	}
	
	public void unloadBike() {
		command.add(6);
	}
	
	private int getY(int num, int bId) {
		return num-(bId%num)-1;
	}
	
	private int getX(int num, int bId) {
		return bId/num;
	}
}
