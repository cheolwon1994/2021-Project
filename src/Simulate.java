import java.util.List;

public class Simulate {
	private int truckId;
	private List<Integer> command;
	
	public int getTruckId() {
		return truckId;
	}
	public void setTruckId(int truckId) {
		this.truckId = truckId;
	}
	public List<Integer> getCommand() {
		return command;
	}
	public void setCommand(List<Integer> command) {
		this.command = command;
	}
	
	public Simulate(int truckId, List<Integer> command) {
		super();
		this.truckId = truckId;
		this.command = command;
	}
}
