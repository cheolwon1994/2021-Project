
public class StartRespond{
	private String auth_key;
	private int problem;
	private int time;
	private int status;
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getAuth_key() {
		return auth_key;
	}
	public void setAuth_key(String auth_key) {
		this.auth_key = auth_key;
	}
	public int getProblem() {
		return problem;
	}
	public void setProblem(int problem) {
		this.problem = problem;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	
	public StartRespond(int status) {
		super();
		this.status = status;
	}
	
	public StartRespond(String auth_key, int problem, int time, int status) {
		super();
		this.auth_key = auth_key;
		this.problem = problem;
		this.time = time;
		this.status = status;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RestRespond [auth_key=").append(auth_key).append(", problem=").append(problem).append(", time=")
				.append(time).append("]");
		return builder.toString();
	}
}
