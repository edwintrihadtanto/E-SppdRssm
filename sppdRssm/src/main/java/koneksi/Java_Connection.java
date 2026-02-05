package koneksi;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Java_Connection {

	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	public String sendPostRequest(String requestURL,
								  HashMap<String, String> postDataParams) {

		HttpURLConnection conn = null;
		StringBuilder response = new StringBuilder();

		try {
			Log.e("JAVA_CONN", "REQUEST URL = " + requestURL);
			URL url = new URL(requestURL);
			conn = (HttpURLConnection) url.openConnection();

			conn.setReadTimeout(15000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);

			// HEADER WAJIB (AMAN WAF)
			conn.setRequestProperty("User-Agent", "Mozilla/5.0");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			OutputStream os = conn.getOutputStream();
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(os, StandardCharsets.UTF_8)
			);
			writer.write(getPostDataString(postDataParams));
			writer.flush();
			writer.close();
			os.close();

			int responseCode = conn.getResponseCode();

			BufferedReader br = new BufferedReader(
					new InputStreamReader(
							responseCode == HttpURLConnection.HTTP_OK
									? conn.getInputStream()
									: conn.getErrorStream(),
							StandardCharsets.UTF_8
					)
			);

			String line;
			while ((line = br.readLine()) != null) {
				response.append(line);
			}
			br.close();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (conn != null) conn.disconnect();
		}

		return response.toString();
	}
	/*private String getPostDataString(HashMap<String, String> params)
			throws UnsupportedEncodingException {

		StringBuilder result = new StringBuilder();
		boolean first = true;

		for (Map.Entry<String, String> entry : params.entrySet()) {
			if (!first) result.append("&");
			first = false;

			result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
		}

		return result.toString();
	}*/
	private String getPostDataString(HashMap<String, String> params)
			throws UnsupportedEncodingException {

		StringBuilder result = new StringBuilder();
		boolean first = true;

		for (Map.Entry<String, String> entry : params.entrySet()) {
			if (!first) result.append("&");
			first = false;

			String key = entry.getKey();
			String value = entry.getValue();

			// 🔒 ANTI NULL (INI PENTING)
			if (key == null) key = "";
			if (value == null) value = "";

			result.append(URLEncoder.encode(key, "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(value, "UTF-8"));
		}

		return result.toString();
	}


	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	public String sendGetRequest(String requestURL) {

		HttpURLConnection conn = null;
		StringBuilder response = new StringBuilder();

		try {
			Log.e("JAVA_CONN", "REQUEST URL = " + requestURL);

			URL url = new URL(requestURL);
			conn = (HttpURLConnection) url.openConnection();

			conn.setReadTimeout(15000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);

			// 🛡️ HEADER ANTI WAF
			conn.setRequestProperty("User-Agent", "Mozilla/5.0");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Connection", "close");

			int responseCode = conn.getResponseCode();
			Log.e("JAVA_CONN", "RESPONSE CODE = " + responseCode);

			BufferedReader br;

			if (responseCode == HttpURLConnection.HTTP_OK) {
				br = new BufferedReader(
						new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
			} else {
				br = new BufferedReader(
						new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
			}

			String line;
			while ((line = br.readLine()) != null) {
				response.append(line);
			}
			br.close();

			Log.e("JAVA_CONN", "RAW RESPONSE = " + response);

		} catch (Exception e) {
			Log.e("JAVA_CONN", "EXCEPTION", e);
			return null;
		} finally {
			if (conn != null) conn.disconnect();
		}

		if (response.length() == 0) {
			Log.e("JAVA_CONN", "RESPON KOSONG");
			return null;
		}

		return response.toString();
	}

}
