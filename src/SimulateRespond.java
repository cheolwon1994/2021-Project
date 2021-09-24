
public class SimulateRespond {
	private String status;
	private int time;
	private int failed_requests_count;
	private String distance;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getFailed_requests_count() {
		return failed_requests_count;
	}
	public void setFailed_requests_count(int failed_requests_count) {
		this.failed_requests_count = failed_requests_count;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	
	public SimulateRespond(String status, int time, int failed_requests_count, String distance) {
		super();
		this.status = status;
		this.time = time;
		this.failed_requests_count = failed_requests_count;
		this.distance = distance;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SimulateRespond [status=").append(status).append(", time=").append(time)
				.append(", failed_requests_count=").append(failed_requests_count).append(", distance=").append(distance)
				.append("]");
		return builder.toString();
	}
	
}
