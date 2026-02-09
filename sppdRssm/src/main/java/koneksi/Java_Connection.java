package koneksi;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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

	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	public String sendMultipart(
			String requestURL,
			HashMap<String, String> params,
			File file,
			String fileParam
	) {
		String boundary = "===" + System.currentTimeMillis() + "===";
		String LINE_FEED = "\r\n";

		try {
			URL url = new URL(requestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setUseCaches(false);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

			DataOutputStream out = new DataOutputStream(conn.getOutputStream());

			// TEXT PARAMS
			for (String key : params.keySet()) {
				out.writeBytes("--" + boundary + LINE_FEED);
				out.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + LINE_FEED);
				out.writeBytes(LINE_FEED);
				out.writeBytes(params.get(key));
				out.writeBytes(LINE_FEED);
			}

			// FILE
			if (file != null && file.exists()) {
				out.writeBytes("--" + boundary + LINE_FEED);
				out.writeBytes(
						"Content-Disposition: form-data; name=\"" + fileParam +
								"\"; filename=\"" + file.getName() + "\"" + LINE_FEED
				);
				out.writeBytes("Content-Type: image/jpeg" + LINE_FEED);
				out.writeBytes(LINE_FEED);

				FileInputStream fis = new FileInputStream(file);
				byte[] buffer = new byte[4096];
				int bytesRead;
				while ((bytesRead = fis.read(buffer)) != -1) {
					out.write(buffer, 0, bytesRead);
				}
				fis.close();
				out.writeBytes(LINE_FEED);
			}

			out.writeBytes("--" + boundary + "--" + LINE_FEED);
			out.flush();
			out.close();

			InputStream is = conn.getResponseCode() == HttpURLConnection.HTTP_OK
					? conn.getInputStream()
					: conn.getErrorStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) sb.append(line);
			br.close();

			return sb.toString();

		} catch (Exception e) {
			return null;
		}
	}

	public boolean downloadFile(
			String fileURL,
			File outputFile
	) {
		HttpURLConnection conn = null;
		InputStream is = null;
		FileOutputStream fos = null;

		try {
			URL url = new URL(fileURL);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(15000);
			conn.setReadTimeout(15000);
			conn.setRequestMethod("GET");

			// header aman
			conn.setRequestProperty("User-Agent", "Mozilla/5.0");
			conn.setRequestProperty("Accept", "application/pdf");

			int responseCode = conn.getResponseCode();
			if (responseCode != HttpURLConnection.HTTP_OK) {
				return false;
			}

			is = conn.getInputStream();
			fos = new FileOutputStream(outputFile);

			byte[] buffer = new byte[4096];
			int len;
			while ((len = is.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}

			fos.flush();
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (is != null) is.close();
				if (fos != null) fos.close();
			} catch (Exception ignored) {}
			if (conn != null) conn.disconnect();
		}
	}

	@RequiresApi(api = Build.VERSION_CODES.O)
    public boolean downloadFileWithProgress(
			String requestURL,
			File targetFile,
			ProgressCallback callback
	) {
		HttpURLConnection conn = null;

		try {
			URL url = new URL(requestURL);
			conn = (HttpURLConnection) url.openConnection();
			conn.connect();

			int fileLength = conn.getContentLength();

			InputStream input = new BufferedInputStream(conn.getInputStream());
			OutputStream output = Files.newOutputStream(targetFile.toPath());

			byte[] data = new byte[4096];
			long total = 0;
			int count;

			while ((count = input.read(data)) != -1) {
				total += count;
				output.write(data, 0, count);

				if (fileLength > 0 && callback != null) {
					int progress = (int) (total * 100 / fileLength);
					callback.onProgress(progress);
				}
			}

			output.flush();
			output.close();
			input.close();

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (conn != null) conn.disconnect();
		}
	}

	public interface ProgressCallback {
		void onProgress(int percent);
	}

}
