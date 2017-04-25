package com.quality.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.stereotype.Service;

@Service
public class OpenId {

	private static String openid_server = "https://login.netease.com/openid/";
	/*
	 * 通常来说，在WEB应用中，这个时候你需要把assoc_handle/mac_key保存在一个固定的地方（可以是session或者后端文件，
	 * 又或者是数据库 ），但一定不能放在cookie里！
	 */
	private String assoc_handle = "";
	private Map<String, String> redirect_data;
	private Map<String, String> auth_response;

	@SuppressWarnings("rawtypes")
	public static String MaptoString_url_utf8(Map<String, String> map) {

		/*
		 * 将URL的参数以分段的方式进行URL utf8编码，并返回一个字符串
		 */
		String arguments = "?";
		Iterator iter = map.entrySet().iterator();
		boolean first_arg = true;
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			Object val = entry.getValue();
			String key_str = (String) key;
			String val_str = (String) val;

			try {
				key_str = URLEncoder.encode(key_str, "UTF-8");
				val_str = URLEncoder.encode(val_str, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			if (first_arg == false) {
				arguments = arguments + "&";
			}
			first_arg = false;

			arguments = arguments + key_str;
			arguments = arguments + "=";
			arguments = arguments + val_str;
		}
		return arguments;
	}

	public String generateAuthUrl(String url) {

		redirect_data = new HashMap<String, String>();
		redirect_data.put("openid.ns", "http://specs.openid.net/auth/2.0");
		redirect_data.put("openid.mode", "checkid_setup");
		redirect_data.put("openid.assoc_handle", assoc_handle);
		redirect_data.put("openid.return_to", "http://quality.qa.ms.netease.com" + url);
		redirect_data.put("openid.claimed_id", "http://specs.openid.net/auth/2.0/identifier_select");
		redirect_data.put("openid.identity", "http://specs.openid.net/auth/2.0/identifier_select");
		redirect_data.put("openid.realm", "http://quality.qa.ms.netease.com/");
		redirect_data.put("openid.ns.sreg", "http://openid.net/extensions/sreg/1.1");
		redirect_data.put("openid.sreg.required", "nickname,email,fullname");
		redirect_data.put("openid.ns.ax", "http://openid.net/srv/ax/1.0");
		redirect_data.put("openid.ax.mode", "fetch_request");
		redirect_data.put("openid.ax.type.empno", "https://login.netease.com/openid/empno/");
		redirect_data.put("openid.ax.type.dep", "https://login.netease.com/openid/dep/");
		redirect_data.put("openid.ax.required", "empno,dep");

		return openid_server + MaptoString_url_utf8(redirect_data);
	}

	/**
	 * @param serverResponse
	 *            ——OpenID server返回的URL
	 * @throws IOException
	 */
	public boolean check_authentication(String serverResponse) throws IOException {

		auth_response = new HashMap<String, String>();

		/* 将OpenID server返回的URL地址解析，获取参数，参数和值都需要UTF-8解码 */
		/*
		 * 注：这里最好不要先把整个URL进行UTF-8解码，然后再取出每个openid参数和值。
		 * 因为openid.return_to或者openid.realm指定的URL可能需要带参数，这样 会引入 ?, =,
		 * &等特殊符号，从而让openid的参数截取变得困难。
		 */

		String[] arrays = serverResponse.split("\\?|&");
		int size = arrays.length;
		for (int i = 0; i < size; i++) {
			int index = arrays[i].indexOf("=");
			if (index == -1)
				continue;
			String arg = URLDecoder.decode(arrays[i].substring(0, index), "UTF-8");
			String val = URLDecoder.decode(arrays[i].substring(index + 1, arrays[i].length()), "UTF-8");
			auth_response.put(arg, val);
		}

		/* 将openid.mode参数的值设置为check_authentication，其他参数和值不变，发回给OpenID server */
		auth_response.put("openid.mode", "check_authentication");

		String arguments = MaptoString_url_utf8(auth_response);
		URL url = null;
		try {

			url = new URL(openid_server + arguments);

			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			BufferedReader r = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			String str = "";
			String auth_result = "";

			do {
				try {
					str = r.readLine();

					if (str == null)
						break;
					String[] temp_arrays = str.split(":");
					if (temp_arrays[0].equals("is_valid")) {
						auth_result = temp_arrays[1];
					}

				} catch (IOException e) {
					e.printStackTrace();
				}

			} while (str != null);
			if (auth_result.equals("true")) {
				return true;
			} else {
				return false;
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return false;
	}
}