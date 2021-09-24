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
	private static List<Integer> singleCommand;
	private static int num;		//서비스 지역 크기
	private static int freq;	//자전거 대여 요청 빈도
	private static int bike;		//자전거 수
	private static int truckNum;		//트럭 수
	private static int initBike;		//초기 자전거 수
	private static List<Customer> order;
	private static int[][] rangeY = {{0, 2}, {2, 4}, {0, 2}, {2, 4}, {1, 3}};
	private static int[][] rangeX = {{0, 2}, {2, 4}, {2, 4}, {0, 2}, {1, 3}};
	
	public CallController(String auth_key, int problem) {
		if(problem==1){		//시나리오 1
			this.num=5;
			this.freq=2;
			this.bike=100;
			this.truckNum=5;
			this.initBike = 4;
		}
		else {		//시나리오 2
			this.num=36;
			this.freq=15;
			this.bike=10800;
			this.truckNum=10;
			this.initBike = 3;
		}
		
		//초기화
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
	
	private void printTruck() {
		for(int i=0;i<num;i++)
			System.out.println("id: "+truckList.get(i).getId()+" 트럭 위치 : "+truckList.get(i).getLocation_id()+" 보유한 자전거 수: "+truckList.get(i).getLoaded_bikes_count());
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
	
	private int findMin() {
		int mini = -1;
		for(int i=0;i<num*num;i++) {
			if(mini==-1 || bikeArr.get(mini).getLocated_bikes_count()>bikeArr.get(i).getLocated_bikes_count())
				mini = i;
		}
		return mini;
	}

	private int findMax() {
		int maxi = -1;
		for(int i=0;i<num*num;i++) {
			if(maxi==-1 || bikeArr.get(maxi).getLocated_bikes_count()<bikeArr.get(i).getLocated_bikes_count())
				maxi = i;
		}
		return maxi;
	}
	
	public void uploadBike() {
		singleCommand.add(5);
	}
	
	public void unloadBike() {
		singleCommand.add(6);
	}
	
	private int getY(int bId) {
		return num-(bId%num)-1;
	}
	
	private int getX(int bId) {
		return bId/num;
	}
	
	private int calDist(int id, int from, int to, int cnt) {
		int fromX = getX(from);
		int fromY = getY(from);
		int toX = getX(to);
		int toY = getY(to);
		//System.out.println("fromY, fromX, toY, toX, cnt: "+fromY+" "+fromX+" "+toY+" "+toX+" "+cnt);
		while(cnt<10) {
			while(fromX<toX) {		//오른쪽으로 이동
				singleCommand.add(2);
				fromX++;
				if(++cnt==10) break;
			}
			if(cnt==10) break;
			while(fromX>toX) {		//왼쪽으로 이동
				singleCommand.add(4);
				fromX--;
				if(++cnt==10) break;
			}
			if(cnt==10) break;
			while(fromY<toY) {		//아래로 이동
				singleCommand.add(3);
				fromY++;
				if(++cnt==10) break;
			}
			if(cnt==10) break;
			while(fromY>toY) {		//위로 이동
				singleCommand.add(1);
				fromY--;
				if(++cnt==10) break;
			}
			break;
		}
		truckList.get(id).setLocation_id(fromX*num+(num-1)-fromY);
		return cnt;
	}
	
	private void commandTruck(Truck truck) {
		int loadedBikeCnt = truck.getLoaded_bikes_count();
		boolean goMinFirst = false;
		if(loadedBikeCnt>0)	goMinFirst=true;
		int len = 0, idx=-1;
		while(len<10) {
			if(goMinFirst) {
				//최소 장소로 이동
				idx = findMin();
				len=calDist(truck.getId(), truck.getLocation_id(),idx, len);
				if(len>=10) break;
				//내린다
				while(loadedBikeCnt>0 && len<10) {
					len++;
					loadedBikeCnt--;
					unloadBike();
					bikeArr.get(idx).setLocated_bikes_count(bikeArr.get(idx).getLocated_bikes_count()+1);
					truck.setLoaded_bikes_count(loadedBikeCnt);
				}
				if(len==10) break;
				goMinFirst=false;
			}
			//최대장소로 이동
			idx = findMax();
			len=calDist(truck.getId(), truck.getLocation_id(),idx, len);
			if(len>=10) break;
			
			//싣는다
			int maxBike = bikeArr.get(idx).getLocated_bikes_count();
			int loadedBike = 0;
			while(loadedBike<maxBike && len<10) {
				len++;
				loadedBike++;
				uploadBike();
				bikeArr.get(idx).setLocated_bikes_count(bikeArr.get(idx).getLocated_bikes_count()-1);
				truck.setLoaded_bikes_count(loadedBike);
			}
			if(len==10) break;
			
			//최소 장소로 이동
			idx = findMin();
			len=calDist(truck.getId(), truck.getLocation_id(),idx, len);
			if(len==10) break;
			
			//내린다
			loadedBikeCnt = truck.getLoaded_bikes_count();
			while(loadedBikeCnt>0 && len<10) {
				len++;
				loadedBikeCnt--;
				unloadBike();
				bikeArr.get(idx).setLocated_bikes_count(bikeArr.get(idx).getLocated_bikes_count()+1);
				truck.setLoaded_bikes_count(loadedBikeCnt);
			}
			if(len==10) break;
		}
	}
	
	private void move(String auth_key) {
		//트럭의 명령
		for(int k=0;k<720;k++) {
			List<Simulate> command = new ArrayList<>();
			setBike(auth_key);
			setTruck(auth_key);
			
			for(int i=0;i<truckNum;i++) {
				singleCommand = new ArrayList<>();
				commandTruck(truckList.get(i));
				if(!singleCommand.isEmpty())
					command.add(new Simulate(i, singleCommand));
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
