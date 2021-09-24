
public class Customer {
	private int startTime;		//대여 시작 시간
	private int rentLocId;		//자전거 대여소 대여 ID
	private int rtnLocId;		//자전거 대여소 반납 ID
	private int rideTime;		//자전거 탈 시간
	
	public int getStartTime() {
		return startTime;
	}
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}
	public int getRentLocId() {
		return rentLocId;
	}
	public void setRentLocId(int rentLocId) {
		this.rentLocId = rentLocId;
	}
	public int getRtnLocId() {
		return rtnLocId;
	}
	public void setRtnLocId(int rtnLocId) {
		this.rtnLocId = rtnLocId;
	}
	public int getRideTime() {
		return rideTime;
	}
	public void setRideTime(int rideTime) {
		this.rideTime = rideTime;
	}
	
	public Customer(int startTime, int rentLocId, int rtnLocId, int rideTime) {
		super();
		this.startTime = startTime;
		this.rentLocId = rentLocId;
		this.rtnLocId = rtnLocId;
		this.rideTime = rideTime;
	}
}
