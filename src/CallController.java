import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CallController {
	//
	public static List<Location> bikeArr;
	//private static int locId[][];
	//private static int bikeCnt[];
	private static List<Truck> truckList;
	private static int num;		//서비스 지역 크기
	private static int freq;	//자전거 대여 요청 빈도
	private static int bike;		//자전거 수
	private static int truck;		//트럭 수
	private static int initBike;		//초기 자전거 수
	private static List<Customer> order;
	private static int[][] rangeY = {{0, 2}, {2, 4}, {0, 2}, {2, 4}, {1, 3}};
	private static int[][] rangeX = {{0, 2}, {2, 4}, {2, 4}, {0, 2}, {1, 3}};
	
	public CallController(String auth_key, int problem) {
		if(problem==1){		//시나리오 1
			this.num=5;
			this.freq=2;
			this.bike=100;
			this.truck=5;
			this.initBike = 4;
		}
		else {		//시나리오 2
			this.num=36;
			this.freq=15;
			this.bike=10800;
			this.truck=10;
			this.initBike = 3;
		}
		
		//초기화
		//order = new ArrayList<>();
		//order 채우기
		//loadTxtFile();
		
		move(auth_key);
	}
	
	private void setBike(String auth_key) {
		bikeArr = new ArrayList<>();
		List<Location> bikeInfo = Connection.getInstance().getLocation(auth_key);
		for(Location lr : bikeInfo) {
			int bId = lr.getId();
			int cnt = lr.getLocated_bikes_count();
			bikeArr.add(new Location(bId,cnt));
		}
	}
	
	private void print() {
		for(int i=0;i<num*num;i++)
			System.out.println("id: "+bikeArr.get(i).getId()+" 자전거 수: "+bikeArr.get(i).getLocated_bikes_count());
	}
	
	private void setTruck(String auth_key) {
		truckList = new ArrayList<>();
		List<Truck> truckInfo = Connection.getInstance().getTruck(auth_key);
		for(Truck t : truckInfo) {
			int tId = t.getId();
			int tLocId = t.getLocation_id();
			int tCnt = t.getLoaded_bikes_count();
			truckList.add(new Truck(tId,tLocId,tCnt));
		}
	}
	
	private int findMin(int i, Truck t) {
		int mini = -1;
		for(int y=rangeY[i][0];y<=rangeY[i][1];y++) {
			for(int x = rangeX[i][0];x<=rangeX[i][1];x++) {
				int idx = x*num+y;
				if(mini==-1 || bikeArr.get(mini).getLocated_bikes_count()>bikeArr.get(idx).getLocated_bikes_count()) {
					mini = idx;
				}
			}
		}
		return mini;
	}
	
	private int findMax(int i, Truck t) {
		int maxi = -1;
		for(int y=rangeY[i][0];y<=rangeY[i][1];y++) {
			for(int x = rangeX[i][0];x<=rangeX[i][1];x++) {
				int idx = x*num+y;
				if(maxi==-1 || bikeArr.get(maxi).getLocated_bikes_count()<bikeArr.get(idx).getLocated_bikes_count()) {
					maxi = idx;
				}
			}
		}
		return maxi;
	}
	
	private boolean transfer(Truck truck, int mini, int maxi) {
		//maxi에서 싣고 mini로 운반
		int diff = (bikeArr.get(maxi).getLocated_bikes_count()-bikeArr.get(mini).getLocated_bikes_count())/2;
		if(!truck.check(num,mini,maxi,diff)) return false;
		truck.move(num, bikeArr.get(maxi).getId());
		truck.uploadBike();
		truck.move(num, bikeArr.get(mini).getId());
		truck.unloadBike();
		//System.out.println("Diff: "+diff);
		bikeArr.get(maxi).setLocated_bikes_count(bikeArr.get(maxi).getLocated_bikes_count()-diff);
		bikeArr.get(mini).setLocated_bikes_count(bikeArr.get(mini).getLocated_bikes_count()+diff);
		return true;
	}
	
	private void move(String auth_key) {
		//트럭의 명령
		for(int k=0;k<720;k++) {
			List<Simulate> command = new ArrayList<>();
			setBike(auth_key);
			setTruck(auth_key);
			//print();
			//System.out.println("K: "+k);
			
			for(int i=0;i<truck;i++) {
				int mini = findMin(i,truckList.get(i));
				int maxi = findMax(i,truckList.get(i));
				
				//System.out.println("mini, maxi: "+mini+" "+maxi);
				if(mini!=-1 && maxi!=-1 && bikeArr.get(mini).getLocated_bikes_count()<bikeArr.get(maxi).getLocated_bikes_count()) {
					if(transfer(truckList.get(i),mini,maxi)) {
						//System.out.println("If문 안");
						//System.out.println("mini, maxi: "+mini+" "+maxi);
						command.add(new Simulate(i, truckList.get(i).getCommand()));
					}
				}
			}
			
			SimulateRespond respond = Connection.getInstance().simulate(auth_key, command);
			if(respond.getStatus().equals("finished")){
				float score = Connection.getInstance().getScore(auth_key);
				System.out.println("Score: "+score);
				break;
			}
		}
	}
	/*
	private String loadTxtFile() {
		String token = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File("./res/1.txt")));
			StringBuilder sb = new StringBuilder();
			sb.append(br.readLine());
			JSONObject obj = new JSONObject(sb.toString());
			Map<Object, Object> map = getMapFromJsonObject(obj);
			Customer cu;
			
			for (Entry<Object, Object> entrySet : map.entrySet()) {
				int startTime = Integer.parseInt((String) entrySet.getKey());
				//System.out.println(entrySet.getKey() + " : " + entrySet.getValue());
				JSONArray arr = new JSONArray(entrySet.getValue().toString());
				for(int i=0;i<arr.length();i++) {
					JSONArray arr2 = new JSONArray(arr.get(i).toString());
					//System.out.println(arr.get(i));
					cu = new Customer(startTime, (int) arr2.get(0), (int) arr2.get(1), (int) arr2.get(2));
					order.add(cu);
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return token;
	}

	@SuppressWarnings("unchecked")
	public static Map<Object, Object> getMapFromJsonObject(JSONObject jsonObj) {
		Map<Object, Object> map = null;
		try {
			map = new ObjectMapper().readValue(jsonObj.toString(), Map.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	*/
}
