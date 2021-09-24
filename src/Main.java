
public class Main {
	private static CallController controller;
	/**
	 * 
	 * Start API
	 * Request
	 curl -X POST {BASE_URL}/start \
     -H 'X-Auth-Token: {X_AUTH_TOKEN}' \
     -H 'Content-Type: application/json' \
     -d '{
         "problem": 1
     }'
     
     Response
     {
	  "auth_key": "1fd74321-d314-4885-b5ae-3e72126e75cc",
	  "problem": 1,
	  "time": 0
	}
     
     Locations API
     Request
     curl -X GET {BASE_URL}/locations \
     -H 'Authorization: {AUTH_KEY}' \
     -H 'Content-Type: application/json'
     
     Response
     {
	  "locations": [
	    { "id": 0, "located_bikes_count": 3 },
	    { "id": 1, "located_bikes_count": 3 },
	    ...
	  ]
	}
	
	Trucks API
	Request
	curl -X GET {BASE_URL}/trucks \
     -H 'Authorization: {AUTH_KEY}' \
     -H 'Content-Type: application/json'
     
     Response
     {
	    "trucks": [
	        { "id": 0, "location_id": 0, "loaded_bikes_count": 0 },
	        { "id": 1, "location_id": 0, "loaded_bikes_count": 0 },
	        ...
	    ]
	}
	
	Simulate API
	현재 시각 ~ 현재 시각 + 1분 까지 각 트럭이 행할 명령을 담아 서버에 전달한다. 호출 시 서버에서는 다음과 같은 일이 진행된다.
	Request
	curl -X PUT {BASE_URL}/simulate \
     -H 'Authorization: {AUTH_KEY}' \
         -H 'Content-Type: application/json' \
     -d '{
       "commands": [
         { "truck_id": 0, "command": [2, 5, 4, 1, 6] }
         ...
       ]
     }'
     
     Response
     {
	  "status": "ready",
	  "time": 1,
	  "failed_requests_count": 5,
	  "distance": 1.2,
	}
	
	Score API
	Request
	curl -X GET {BASE_URL}/score \
     -H 'Authorization: {AUTH_KEY}' \
     -H 'Content-Type: application/json'
     
    Response
    {
	  "score": 75.7
	}
	 */
	
	static final String x_auth_token = "9c32d768a11b9f6d9ab53490b2354694";
	static final int problem = 1;
	//static final int problem = 2;
	
	public static void main(String[] args) {
		start();
	}
	
	private static void start() {
		System.out.println("Start 시작");
		StartRespond respond = TokenManager.getInstance().createToken(x_auth_token, problem);
		if(respond.getStatus()==200) {
			//토큰 정상 반환
			controller = new CallController(respond.getAuth_key(),problem);
		}
	}
}
