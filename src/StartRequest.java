
public class StartRequest {
	private int problem;

	public StartRequest(int problem) {
		super();
		this.problem = problem;
	}

	public int getProblem() {
		return problem;
	}

	public void setProblem(int problem) {
		this.problem = problem;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("problem=").append(problem);
		return builder.toString();
	}
}
