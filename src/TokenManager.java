import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TokenManager {
	private static TokenManager instance = null;
	private String token = "";

	public static TokenManager getInstance() {
		if (instance == null) {
			instance = new TokenManager();
		}
		return instance;
	}

	public String getToken() {
		return this.token;
	}

	public synchronized StartRespond createToken(String auth_token, int problem) {
		String token = null;
		StartRespond respond = Connection.getInstance().start(auth_token, problem);
		int status = respond.getStatus();

		if (status == 400) {
			System.out.println("400:: Parameter가 잘못됨 (범위, 값 등)");
		} else if (status == 401) {
			System.out.println("401:: 인증을 위한 Header가 잘못됨");
		} else if (status == 500) {
			System.out.println("500:: 서버 에러, 채팅으로 문의 요망");
		} else {
			// saveTokenFile(respond.getAuth_key()/* token */);
			token = respond.getAuth_key();
		}
		this.token = token;
		return respond;
	}
}
