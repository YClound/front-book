package com.longan.getway.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.longan.biz.utils.Utils;
import com.longan.getway.core.domain.SupplyResult;

public class SocketConnection {
	private static final Logger logger = LoggerFactory.getLogger(SocketConnection.class);
	private static SocketConnection sc = new SocketConnection();

	private SocketConnection() {

	}

	public final static SocketConnection getInstance() {
		return sc;
	}

	public SupplyResult<String> sendPackage(String requestIp, Integer requestPort, String requestPkg) {
		SupplyResult<String> result = new SupplyResult<String>();
		if (requestIp == null || requestPort == null || requestPkg == null) {
			result.setResultMsg("请求参数缺失");
			return result;
		}
		InetSocketAddress endpoint = new InetSocketAddress(requestIp, requestPort);
		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		socket = new Socket();
		try {
			socket.setSoTimeout(Integer.parseInt(Utils.getProperty("socketWaitTime")));
			socket.connect(endpoint, 30000);
			out = socket.getOutputStream();
			in = socket.getInputStream();
			// 输出请求
			byte[] datas = requestPkg.getBytes();
			logger.warn("socket request: " + requestIp + "  " + requestPort + "  param: "
					+ requestPkg);
			out.write(datas);
			out.flush();
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "GBK"));
			// 接收应答
			@SuppressWarnings("unused")
			int temp = -1;
			char[] c = new char[1];
			String responsePkg = "";
			while ((temp = br.read(c, 0, c.length)) != -1) {
				responsePkg += String.valueOf(c);
			}
			logger.warn("socket response: " + responsePkg);
			result.setModule(responsePkg);
			result.setStatus(SupplyResult.STATUS_SUCCESS);
		} catch (IOException e) {
			logger.error("socket error", e);
			if (e instanceof SocketTimeoutException) {
				result.setStatus(SupplyResult.STATUS_UNCONFIRM);
				result.setResultMsg("对方返回超时");
				return result;
			}
			if (e instanceof ConnectException) {
				result.setStatus(SupplyResult.STATUS_FAILED);
				result.setResultMsg("连接对方超时");
				return result;
			}
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					logger.error("out close error", e);
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					logger.error("in close error", e);
				}
			}
			if (socket != null) {
				try {
					socket.close();
				} catch (Exception e) {
					logger.error("socket close error", e);
				}
			}
		}
		return result;
	}
}
