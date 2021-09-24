import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Connection {
	private static Connection instance = null;
	private HttpURLConnection conn;
	private JSONObject responseJson;
	private JSONArray responseJsonArray;
	private URL url;
	private String paramData;
	private BufferedReader br;
	private StringBuilder sb;

	public static Connection getInstance() {
		if (instance == null) {
			instance = new Connection();
		}
		return instance;
	}

	// POST /start/{BASE_URL}/start
	public StartRespond start(String authToken, int problem) {
		//System.out.println(">>>> api.start()");
		StartRespond respond = null;
		conn = null;
		responseJson = null;
		paramData = "?problem=" + problem;

		try {
			url = new URL(GlobalData.BASE_URL + GlobalData.START_API + paramData);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("X-Auth-Token", authToken);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);

			int responseCode = conn.getResponseCode();
			if (responseCode == 200) { // 성공
				br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				sb = new StringBuilder();
				String line = "";
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				responseJson = new JSONObject(sb.toString());
				respond = new StartRespond(responseJson.getString("auth_key"), responseJson.getInt("problem"),
						responseJson.getInt("time"), 200);
			} else {
				respond = new StartRespond(responseCode);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return respond;
	}

	// GET /locations
	public List<Location> getLocation(String authKey) {
		//System.out.println(">>>> getLocation.start()");
		List<Location> respondList = new ArrayList<>();
		conn = null;
		responseJson = null;
		responseJsonArray = null;

		try {
			URL url = new URL(GlobalData.BASE_URL + GlobalData.LOCATION_API);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Authorization", authKey);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);

			int responseCode = conn.getResponseCode();
			if (responseCode == 200) { // 성공
				br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				sb = new StringBuilder();
				String line = "";
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				responseJson = new JSONObject(sb.toString());
				responseJsonArray = (JSONArray) responseJson.get("locations");

				Location respond;
				for (int i = 0; i < responseJsonArray.length(); i++) {
					JSONObject obj = new JSONObject(responseJsonArray.get(i).toString());
					respond = new Location(obj.getInt("id"), obj.getInt("located_bikes_count"));
					respondList.add(respond);
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return respondList;
	}

	// GET /locations
	public List<Truck> getTruck(String authKey) {
		//System.out.println(">>>> getTruck.start()");
		List<Truck> respondList = new ArrayList<>();
		conn = null;
		responseJson = null;
		responseJsonArray = null;

		try {
			URL url = new URL(GlobalData.BASE_URL + GlobalData.TRUCK_API);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Authorization", authKey);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);

			int responseCode = conn.getResponseCode();
			if (responseCode == 200) { // 성공
				br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				sb = new StringBuilder();
				String line = "";
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				responseJson = new JSONObject(sb.toString());
				responseJsonArray = (JSONArray) responseJson.get("trucks");

				Truck respond;
				for (int i = 0; i < responseJsonArray.length(); i++) {
					JSONObject obj = new JSONObject(responseJsonArray.get(i).toString());
					respond = new Truck(obj.getInt("id"), obj.getInt("location_id"), obj.getInt("loaded_bikes_count"));
					respondList.add(respond);
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return respondList;
	}

	// PUT /simulate
	public SimulateRespond simulate(String authToken, List<Simulate> list) {
		//System.out.println(">>>> simulate.start()");
		SimulateRespond respond = null;
		conn = null;
		responseJson = null;
		responseJsonArray = new JSONArray();
		try {
			url = new URL(GlobalData.BASE_URL + GlobalData.SIMULATE_API);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("PUT");
			conn.setRequestProperty("Authorization", authToken);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);

			DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
			for (int i = 0; i < list.size(); i++) {
				int tid = list.get(i).getTruckId();
				List<Integer> li = list.get(i).getCommand();
				JSONObject obj = new JSONObject();
				obj.put("truck_id",tid);
				obj.put("command",li);
				responseJsonArray.put(obj);
			}
			JSONObject total = new JSONObject();
			total.put("commands", responseJsonArray);
			outputStream.writeBytes(total.toString());
			outputStream.flush();
			outputStream.close();

			int responseCode = conn.getResponseCode();
			if (responseCode == 200) { // 성공
				br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				sb = new StringBuilder();
				String line = "";
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				responseJson = new JSONObject(sb.toString());
				respond = new SimulateRespond(responseJson.getString("status"), responseJson.getInt("time"), (int)Float.parseFloat(responseJson.getString("failed_requests_count")), responseJson.getString("distance"));
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return respond;
	}

	// GET /score
	public float getScore(String authKey) {
		//System.out.println(">>>> getScore.start()");
		float score = 0.0f;
		conn = null;
		responseJson = null;

		try {
			URL url = new URL(GlobalData.BASE_URL + GlobalData.SCORE_API);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Authorization", authKey);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);

			int responseCode = conn.getResponseCode();
			if (responseCode == 200) { // 성공
				br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				sb = new StringBuilder();
				String line = "";
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				responseJson = new JSONObject(sb.toString());
				score = responseJson.getFloat("score");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return score;
	}

}
