package koneksi;

import android.os.Build;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class JSONParser {

	private static final String TAG = "JSONParser_SAFE";

	public JSONObject makeHttpRequest(String url, String method, List<NameValuePair> params) {

		InputStream is = null;
		String json = "";

		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();

			// 🔴 FIX STRING COMPARISON
			if ("POST".equals(method)) {

				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
				httpPost.setHeader("User-Agent", "Mozilla/5.0");
				httpPost.setHeader("Accept", "application/json");

				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();

			} else if ("GET".equals(method)) {

				String paramString = URLEncodedUtils.format(params, "UTF-8");
				HttpGet httpGet = new HttpGet(url + "?" + paramString);
				httpGet.setHeader("User-Agent", "Mozilla/5.0");
				httpGet.setHeader("Accept", "application/json");

				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
			}

			if (is == null) {
				Log.e(TAG, "InputStream NULL (request gagal / diblok)");
				return null;
			}

            BufferedReader reader = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                reader = new BufferedReader(
                        new InputStreamReader(is, StandardCharsets.UTF_8)
                );
            }

            StringBuilder sb = new StringBuilder();
			String line;

			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}

			is.close();
			json = sb.toString();

			// 🔍 LOG RAW RESPONSE
			Log.d(TAG, "RAW RESPONSE: " + json);

			// 🔴 CEK APAKAH JSON
			if (!json.trim().startsWith("{")) {
				Log.e(TAG, "Response bukan JSON (kemungkinan HTML / WAF)");
				return null;
			}

			return new JSONObject(json);

		} catch (Exception e) {
			Log.e(TAG, "Exception: " + e.toString());
			return null;
		}
	}
}
