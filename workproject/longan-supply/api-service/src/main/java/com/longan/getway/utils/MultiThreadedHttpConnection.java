package com.longan.getway.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.longan.getway.core.domain.SupplyResult;

public class MultiThreadedHttpConnection {
	private static final Logger logger = LoggerFactory.getLogger(MultiThreadedHttpConnection.class);

	private static final String contextType = "text/html,application/xml;charset=UTF-8";
	 private static final String contextTypeJson ="application/json;charset=UTF-8";
	// private static final String contextTypeXml = "application/xml";

	private static PoolingHttpClientConnectionManager httpPool = new PoolingHttpClientConnectionManager();
	private static int maxTotal = 200;
	private static int maxPerRout = 50;
	private static int connecttimeOut = 10000;
	private static int readTimeOut = 30000;
	private static MultiThreadedHttpConnection mthc = new MultiThreadedHttpConnection();
	private static DefaultHttpRequestRetryHandler handler = new DefaultHttpRequestRetryHandler(1,
			false);
	private static RequestConfig requestConfig = RequestConfig.custom()
			.setSocketTimeout(readTimeOut).setConnectTimeout(connecttimeOut).build(); // 设置请求和传输超时时间

	static {
		httpPool.setMaxTotal(maxTotal);// 连接池最大并发连接数
		httpPool.setDefaultMaxPerRoute(maxPerRout);// 单路由最大并发数
	}

	private MultiThreadedHttpConnection() {

	}

	public static MultiThreadedHttpConnection getInstance() {
		return mthc;
	}

	public SupplyResult<Document> sendDataByGet(String url) {
		SupplyResult<Document> result = new SupplyResult<Document>();
		if (url == null) {
			result.setResultMsg("请求参数缺失");
			return result;
		}
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(httpPool)
				.setRetryHandler(handler).build();
		InputStream inputStream = null;
		Integer statusCode = -1;
		logger.warn("request: " + url);
		HttpGet get = null;
		try {
			get = new HttpGet(url);
		} catch (Exception e) {
			logger.error("url error " + url, e.getMessage());
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("url error " + url);
			return result;
		}
		get.setConfig(requestConfig);

		CloseableHttpResponse response = null;
		try {
			get.setHeader("Accept", contextType);
			response = httpClient.execute(get);
			statusCode = response.getStatusLine().getStatusCode();

			if (statusCode != 200) {
				logger.error("connect " + url + " error httpCode : " + statusCode);
				result.setStatus(SupplyResult.STATUS_FAILED);
				result.setResultMsg(statusCode + " error");
				return result;
			} else {
				SAXReader reader = new SAXReader();
				try {
					inputStream = response.getEntity().getContent();
					Document document = reader.read(response.getEntity().getContent());
					result.setStatus(SupplyResult.STATUS_SUCCESS);
					result.setModule(document);
				} catch (DocumentException e) {
					logger.error("parese document error", e);
					result.setStatus(SupplyResult.STATUS_FAILED);
					result.setResultMsg("parese document error msg : " + e.getMessage());
					return result;
				} finally {
					try {
						if (inputStream != null) {
							inputStream.close();
						}
					} catch (IOException e) {
						logger.error("inputStream close error", e);
					}
				}
			}
		} catch (Exception e) {
			logger.error("sendDataByGet IOException ", e);
			if (e instanceof SocketTimeoutException) {
				result.setStatus(SupplyResult.STATUS_UNCONFIRM);
				result.setResultMsg("请求超时");
			}
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				logger.error("closeableHttpResponse close error", e);
			}
		}
		return result;
	}

	public SupplyResult<String> sendDataByGetReturnString(String url) {
		SupplyResult<String> result = new SupplyResult<String>();
		if (url == null) {
			result.setResultMsg("请求参数缺失");
			return result;
		}
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(httpPool)
				.setRetryHandler(handler).build();
		InputStream inputStream = null;
		Integer statusCode = -1;
		logger.warn("request: " + url);
		HttpGet get = null;
		try {
			get = new HttpGet(url);
		} catch (Exception e) {
			logger.error("url error " + url, e.getMessage());
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("url error " + url);
			return result;
		}
		get.setConfig(requestConfig);

		CloseableHttpResponse response = null;
		try {
			get.setHeader("Accept", contextType);
			response = httpClient.execute(get);
			statusCode = response.getStatusLine().getStatusCode();

			if (statusCode != 200) {
				logger.error("connect " + url + " error httpCode : " + statusCode);
				result.setStatus(SupplyResult.STATUS_FAILED);
				result.setResultMsg(statusCode + " error");
				return result;
			} else {
				try {
					inputStream = response.getEntity().getContent();
					StringBuffer sb = new StringBuffer();
					int value = 0;
					while ((value = inputStream.read()) != -1) {
						sb.append((char) value);
					}

					String resultStr = new String(sb.toString().getBytes("ISO-8859-1"), "UTF-8");
					logger.warn("response : " + resultStr);
					result.setModule(resultStr);
					result.setStatus(SupplyResult.STATUS_SUCCESS);
				} catch (UnsupportedEncodingException e) {
					logger.error("parese response error", e);
					result.setStatus(SupplyResult.STATUS_FAILED);
					result.setResultMsg("parese response error msg : " + e.getMessage());
					return result;
				} finally {
					try {
						if (inputStream != null) {
							inputStream.close();
						}
					} catch (IOException e) {
						logger.error("inputStream close error", e);
					}
				}
			}
		} catch (Exception e) {
			logger.error("sendDataByGet IOException ", e);
			if (e instanceof SocketTimeoutException) {
				result.setStatus(SupplyResult.STATUS_UNCONFIRM);
				result.setResultMsg("请求超时");
			}
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				logger.error("closeableHttpResponse close error", e);
			}
		}
		return result;
	}
	
	public SupplyResult<String> sendDataByGetReturnJson(String url) {
		SupplyResult<String> result = new SupplyResult<String>();
		if (url == null) {
			result.setResultMsg("请求参数缺失");
			return result;
		}
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(httpPool)
				.setRetryHandler(handler).build();
		InputStream inputStream = null;
		Integer statusCode = -1;
		logger.warn("request: " + url);
		HttpGet get = null;
		try {
			get = new HttpGet(url);
		} catch (Exception e) {
			logger.error("url error " + url, e.getMessage());
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("url error " + url);
			return result;
		}
		get.setConfig(requestConfig);

		CloseableHttpResponse response = null;
		try {
			get.setHeader("Accept", contextTypeJson);
			response = httpClient.execute(get);
			statusCode = response.getStatusLine().getStatusCode();

			if (statusCode != 200) {
				logger.error("connect " + url + " error httpCode : " + statusCode);
				result.setStatus(SupplyResult.STATUS_FAILED);
				result.setResultMsg(statusCode + " error");
				return result;
			} else {
				try {
					inputStream = response.getEntity().getContent();
					StringBuffer sb = new StringBuffer();
					int value = 0;
					while ((value = inputStream.read()) != -1) {
						sb.append((char) value);
					}

					String resultStr = new String(sb.toString().getBytes("ISO-8859-1"), "UTF-8");
					logger.warn("response : " + resultStr);
					result.setModule(resultStr);
					result.setStatus(SupplyResult.STATUS_SUCCESS);
				} catch (UnsupportedEncodingException e) {
					logger.error("parese response error", e);
					result.setStatus(SupplyResult.STATUS_FAILED);
					result.setResultMsg("parese response error msg : " + e.getMessage());
					return result;
				} finally {
					try {
						if (inputStream != null) {
							inputStream.close();
						}
					} catch (IOException e) {
						logger.error("inputStream close error", e);
					}
				}
			}
		} catch (Exception e) {
			logger.error("sendDataByGet IOException ", e);
			if (e instanceof SocketTimeoutException) {
				result.setStatus(SupplyResult.STATUS_UNCONFIRM);
				result.setResultMsg("请求超时");
			}
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				logger.error("closeableHttpResponse close error", e);
			}
		}
		return result;
	}
	public SupplyResult<String> sendDataByGetReturnJson(String url,String encoding) {
		SupplyResult<String> result = new SupplyResult<String>();
		if (url == null) {
			result.setResultMsg("请求参数缺失");
			return result;
		}
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(httpPool)
				.setRetryHandler(handler).build();
		InputStream inputStream = null;
		Integer statusCode = -1;
		logger.warn("request: " + url);
		HttpGet get = null;
		try {
			get = new HttpGet(url);
		} catch (Exception e) {
			logger.error("url error " + url, e.getMessage());
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("url error " + url);
			return result;
		}
		get.setConfig(requestConfig);

		CloseableHttpResponse response = null;
		try {
			get.setHeader("Accept", contextTypeJson);
			response = httpClient.execute(get);
			statusCode = response.getStatusLine().getStatusCode();

			if (statusCode != 200) {
				logger.error("connect " + url + " error httpCode : " + statusCode);
				result.setStatus(SupplyResult.STATUS_FAILED);
				result.setResultMsg(statusCode + " error");
				return result;
			} else {
				try {
					inputStream = response.getEntity().getContent();
					StringBuffer sb = new StringBuffer();
					int value = 0;
					while ((value = inputStream.read()) != -1) {
						sb.append((char) value);
					}

					String resultStr = new String(sb.toString().getBytes("ISO-8859-1"), encoding);
					logger.warn("response : " + resultStr);
					result.setModule(resultStr);
					result.setStatus(SupplyResult.STATUS_SUCCESS);
				} catch (UnsupportedEncodingException e) {
					logger.error("parese response error", e);
					result.setStatus(SupplyResult.STATUS_FAILED);
					result.setResultMsg("parese response error msg : " + e.getMessage());
					return result;
				} finally {
					try {
						if (inputStream != null) {
							inputStream.close();
						}
					} catch (IOException e) {
						logger.error("inputStream close error", e);
					}
				}
			}
		} catch (Exception e) {
			logger.error("sendDataByGet IOException ", e);
			if (e instanceof SocketTimeoutException) {
				result.setStatus(SupplyResult.STATUS_UNCONFIRM);
				result.setResultMsg("请求超时");
			}else{
				result.setStatus(SupplyResult.STATUS_UNCONFIRM);
				result.setResultMsg("请求失败");
			}
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				logger.error("closeableHttpResponse close error", e);
			}
		}
		return result;
	}

	public SupplyResult<String> sendDataByGetReturnGBKStr(String url) {
		SupplyResult<String> result = new SupplyResult<String>();
		if (url == null) {
			result.setResultMsg("请求参数缺失");
			return result;
		}
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(httpPool)
				.setRetryHandler(handler).build();
		InputStream inputStream = null;
		Integer statusCode = -1;
		logger.warn("request: " + url);
		HttpGet get = null;
		try {
			get = new HttpGet(url);
		} catch (Exception e) {
			logger.error("url error " + url, e.getMessage());
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("url error " + url);
			return result;
		}
		get.setConfig(requestConfig);

		CloseableHttpResponse response = null;
		try {
			get.setHeader("Accept", contextType);
			response = httpClient.execute(get);
			statusCode = response.getStatusLine().getStatusCode();

			if (statusCode != 200) {
				logger.error("connect " + url + " error httpCode : " + statusCode);
				result.setStatus(SupplyResult.STATUS_FAILED);
				result.setResultMsg(statusCode + " error");
				return result;
			} else {
				try {
					inputStream = response.getEntity().getContent();
					StringBuffer sb = new StringBuffer();
					int value = 0;
					while ((value = inputStream.read()) != -1) {
						sb.append((char) value);
					}

					String resultStr = new String(sb.toString().getBytes("ISO-8859-1"), "GBK");
					logger.warn("response : " + resultStr);
					result.setModule(resultStr);
					result.setStatus(SupplyResult.STATUS_SUCCESS);
				} catch (UnsupportedEncodingException e) {
					logger.error("parese response error", e);
					result.setStatus(SupplyResult.STATUS_FAILED);
					result.setResultMsg("parese response error msg : " + e.getMessage());
					return result;
				} finally {
					try {
						if (inputStream != null) {
							inputStream.close();
						}
					} catch (IOException e) {
						logger.error("inputStream close error", e);
					}
				}
			}
		} catch (Exception e) {
			logger.error("sendDataByGet IOException ", e);
			if (e instanceof SocketTimeoutException) {
				result.setStatus(SupplyResult.STATUS_UNCONFIRM);
				result.setResultMsg("请求超时");
			}
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				logger.error("closeableHttpResponse close error", e);
			}
		}
		return result;
	}

	public SupplyResult<Boolean> sendDataByGetForCallBack(String url) {

		SupplyResult<Boolean> result = new SupplyResult<Boolean>();
		result.setModule(false);
		if (url == null) {
			result.setResultMsg("请求参数缺失");
			return result;
		}

		// 没有设置失败后，重试1次
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(httpPool)
				.build();
		Integer statusCode = -1;
		logger.warn("callback request: " + url);
		HttpGet get;
		try {
			get = new HttpGet(url);
		} catch (Exception e) {
			logger.error("url error " + url, e.getMessage());
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("url error " + url);
			return result;
		}
		get.setConfig(requestConfig);

		CloseableHttpResponse response = null;
		try {
			get.setHeader("Accept", contextType);
			response = httpClient.execute(get);
			statusCode = response.getStatusLine().getStatusCode();
			result.setStatus(SupplyResult.STATUS_SUCCESS);
			if (statusCode == 200) {
				logger.warn("callback response OK");
				result.setModule(true);
			} else {
				logger.error("connect " + url + " error httpCode : " + statusCode);
			}
			return result;

		} catch (Exception e) {
			logger.error("sendDataByGet IOException ", e);
			if (e instanceof SocketTimeoutException) {
				result.setStatus(SupplyResult.STATUS_UNCONFIRM);
				result.setResultMsg("请求超时");
			}
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				logger.error("closeableHttpResponse close error", e);
			}
		}
		return result;
	}

	public SupplyResult<String> sendDataByPost(String url, String param) {
		SupplyResult<String> result = new SupplyResult<String>();
		if (param == null || url == null) {
			result.setResultMsg("请求参数缺失");
			return result;
		}

		Integer statusCode = -1;
		HttpPost post;
		try {
			post = new HttpPost(url);
		} catch (Exception e) {
			logger.error("url error " + url, e.getMessage());
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("url error " + url);
			return result;
		}

		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(httpPool)
				.setRetryHandler(handler).build();
		logger.warn("post request: " + url + param);
		post.setConfig(requestConfig);

		CloseableHttpResponse response = null;

		InputStream is = null;
		try {
			post.setEntity(new StringEntity(param));
			post.setHeader("Accept", contextType);
			response = httpClient.execute(post);
			statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				logger.error("connect " + url + " error httpCode : " + statusCode);
				result.setResultMsg("请求失败  code : " + statusCode);
				return result;
			}
			is = response.getEntity().getContent();
			StringBuffer sb = new StringBuffer();
			int value = 0;
			while ((value = is.read()) != -1) {
				sb.append((char) value);
			}
			String resultStr = new String(sb.toString().getBytes("ISO-8859-1"), "UTF-8");
			logger.warn("post response : " + resultStr);
			result.setModule(resultStr);
			result.setStatus(SupplyResult.STATUS_SUCCESS);

		} catch (UnsupportedEncodingException e) {
			logger.error("sendDataByPost UnsupportedEncodingException ", e);
		} catch (IOException e) {
			logger.error("sendDataByPost IOException ", e);
			if (e instanceof SocketTimeoutException) {
				result.setStatus(SupplyResult.STATUS_UNCONFIRM);
				result.setResultMsg("请求超时");
				return result;
			}
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				logger.error("closeableHttpResponse close error", e);
			}

			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				logger.error("is close error", e);
			}
		}

		return result;
	}

	public SupplyResult<String> sendDataByPost(String url, String param, String cookie) {
		SupplyResult<String> result = new SupplyResult<String>();
		if (param == null || url == null) {
			result.setResultMsg("请求参数缺失");
			return result;
		}

		Integer statusCode = -1;
		HttpPost post;
		try {
			post = new HttpPost(url);
		} catch (Exception e) {
			logger.error("url error " + url, e.getMessage());
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("url error " + url);
			return result;
		}

		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(httpPool)
				.setRetryHandler(handler).build();
		logger.warn("post request: " + url + param);
		post.setConfig(requestConfig);

		CloseableHttpResponse response = null;

		InputStream is = null;
		try {
			post.setEntity(new StringEntity(param));
			post.setHeader("Accept", contextType);
			post.setHeader("Cookie", cookie);
			response = httpClient.execute(post);
			statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				logger.error("connect " + url + " error httpCode : " + statusCode);
				result.setResultMsg("请求失败  code : " + statusCode);
				return result;
			}
			is = response.getEntity().getContent();
			StringBuffer sb = new StringBuffer();
			int value = 0;
			while ((value = is.read()) != -1) {
				sb.append((char) value);
			}
			String resultStr = new String(sb.toString().getBytes("ISO-8859-1"), "UTF-8");
			logger.warn("post response : " + resultStr);
			result.setModule(resultStr);
			result.setStatus(SupplyResult.STATUS_SUCCESS);

		} catch (UnsupportedEncodingException e) {
			logger.error("sendDataByPost UnsupportedEncodingException ", e);
		} catch (IOException e) {
			logger.error("sendDataByPost IOException ", e);
			if (e instanceof SocketTimeoutException) {
				result.setStatus(SupplyResult.STATUS_UNCONFIRM);
				result.setResultMsg("请求超时");
				return result;
			}
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				logger.error("closeableHttpResponse close error", e);
			}

			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				logger.error("is close error", e);
			}
		}

		return result;
	}

	public SupplyResult<String> sendPostByMap(String url, Map<String, String> paramMap) {
		SupplyResult<String> result = new SupplyResult<String>();

		if (paramMap == null || url == null) {
			result.setResultMsg("请求参数缺失");
			return result;
		}

		Integer statusCode = -1;
		HttpPost post;

		try {
			post = new HttpPost(url);
		} catch (Exception e) {
			logger.error("url error " + url, e.getMessage());
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("url error " + url);
			return result;
		}

		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(httpPool)
				.setRetryHandler(handler).build();
		logger.warn("post request: " + url + "  params : " + mapToString(paramMap));
		post.setConfig(requestConfig);

		CloseableHttpResponse response = null;

		InputStream is = null;
		try {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			if (paramMap != null) {
				for (Map.Entry<String, String> m : paramMap.entrySet()) {
					nvps.add(new BasicNameValuePair(m.getKey(), m.getValue()));
				}
			}

			post.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
			post.setHeader("Accept", contextType);
			response = httpClient.execute(post);
			statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				logger.error("connect " + url + " error httpCode : " + statusCode);
				result.setResultMsg("请求失败  code : " + statusCode);
				return result;
			}
			is = response.getEntity().getContent();
			StringBuffer sb = new StringBuffer();
			int value = 0;
			while ((value = is.read()) != -1) {
				sb.append((char) value);
			}
			String resultStr = new String(sb.toString().getBytes("ISO-8859-1"), "UTF-8");
			logger.warn("post response : " + resultStr);
			result.setModule(resultStr);
			result.setStatus(SupplyResult.STATUS_SUCCESS);

		} catch (UnsupportedEncodingException e) {
			logger.error("sendDataByPost UnsupportedEncodingException ", e);
		} catch (IOException e) {
			logger.error("sendDataByPost IOException ", e);
			if (e instanceof SocketTimeoutException) {
				result.setStatus(SupplyResult.STATUS_UNCONFIRM);
				result.setResultMsg("请求超时");
				return result;
			}
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				logger.error("closeableHttpResponse close error", e);
			}

			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				logger.error("is close error", e);
			}
		}

		return result;
	}
	
/** ------------------------------------ 长时间请求----------------------------------- */
	
	
	private static int longReadTimeOut = 120000;
	private static RequestConfig longTimeoutConfig = RequestConfig.custom()
			.setSocketTimeout(longReadTimeOut).setConnectTimeout(connecttimeOut).build(); // 设置请求和传输超时时间
	
	public SupplyResult<String> sendDataByGetLongTimeout(String url) {
		SupplyResult<String> result = new SupplyResult<String>();
		if (url == null) {
			result.setResultMsg("请求参数缺失");
			return result;
		}
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(httpPool)
				.setRetryHandler(handler).build();
		InputStream inputStream = null;
		Integer statusCode = -1;
		logger.warn("request: " + url);
		HttpGet get = null;
		try {
			get = new HttpGet(url);
		} catch (Exception e) {
			logger.error("url error " + url, e.getMessage());
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("url error " + url);
			return result;
		}
		get.setConfig(longTimeoutConfig);

		CloseableHttpResponse response = null;
		try {
			get.setHeader("Accept", contextType);
			response = httpClient.execute(get);
			statusCode = response.getStatusLine().getStatusCode();

			if (statusCode != 200) {
				logger.error("connect " + url + " error httpCode : " + statusCode);
				result.setStatus(SupplyResult.STATUS_FAILED);
				result.setResultMsg(statusCode + " error");
				return result;
			} else {
				try {
					inputStream = response.getEntity().getContent();
					StringBuffer sb = new StringBuffer();
					int value = 0;
					while ((value = inputStream.read()) != -1) {
						sb.append((char) value);
					}

					String resultStr = new String(sb.toString().getBytes("ISO-8859-1"), "UTF-8");
					logger.warn("response : " + resultStr);
					result.setModule(resultStr);
					result.setStatus(SupplyResult.STATUS_SUCCESS);
				} catch (UnsupportedEncodingException e) {
					logger.error("parese response error", e);
					result.setStatus(SupplyResult.STATUS_FAILED);
					result.setResultMsg("parese response error msg : " + e.getMessage());
					return result;
				} finally {
					try {
						if (inputStream != null) {
							inputStream.close();
						}
					} catch (IOException e) {
						logger.error("inputStream close error", e);
					}
				}
			}
		} catch (Exception e) {
			logger.error("sendDataByGet IOException ", e);
			if (e instanceof SocketTimeoutException) {
				result.setStatus(SupplyResult.STATUS_UNCONFIRM);
				result.setResultMsg("请求超时");
			}
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				logger.error("closeableHttpResponse close error", e);
			}
		}
		return result;
	}
	
	
	/** ------------------------------------ 公用cookie请求----------------------------------- */

	private CloseableHttpClient cookieHttpClient = HttpClients.custom()
			.setConnectionManager(httpPool).setRetryHandler(handler).build();

	public SupplyResult<String> sendPostByMapLogin(String url, Map<String, String> paramMap) {
		SupplyResult<String> result = new SupplyResult<String>();

		if (url == null) {
			result.setResultMsg("请求参数缺失");
			return result;
		}

		Integer statusCode = -1;
		HttpPost post;

		try {
			post = new HttpPost(url);
		} catch (Exception e) {
			logger.error("url error " + url, e.getMessage());
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("url error " + url);
			return result;
		}

		logger.warn("post request: " + url + "  params : " + mapToString(paramMap));
		post.setConfig(requestConfig);

		CloseableHttpResponse response = null;

		InputStream is = null;
		try {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			if (paramMap != null) {
				for (Map.Entry<String, String> m : paramMap.entrySet()) {
					nvps.add(new BasicNameValuePair(m.getKey(), m.getValue()));
				}
			}

			post.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
			post.setHeader("Accept", contextType);
			response = cookieHttpClient.execute(post);
			statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				logger.error("connect " + url + " error httpCode : " + statusCode);
				result.setResultMsg("请求失败  code : " + statusCode);
				return result;
			}
			is = response.getEntity().getContent();
			StringBuffer sb = new StringBuffer();
			int value = 0;
			while ((value = is.read()) != -1) {
				sb.append((char) value);
			}
			String resultStr = new String(sb.toString().getBytes("ISO-8859-1"), "UTF-8");
			logger.warn("post response : " + resultStr);
			result.setModule(resultStr);
			result.setStatus(SupplyResult.STATUS_SUCCESS);

		} catch (UnsupportedEncodingException e) {
			logger.error("sendDataByPost UnsupportedEncodingException ", e);
		} catch (IOException e) {
			logger.error("sendDataByPost IOException ", e);
			if (e instanceof SocketTimeoutException) {
				result.setStatus(SupplyResult.STATUS_UNCONFIRM);
				result.setResultMsg("请求超时");
				return result;
			}
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				logger.error("closeableHttpResponse close error", e);
			}

			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				logger.error("is close error", e);
			}
		}

		return result;
	}

	public Map<String, Object> sendDataByGetReturnMap(String url) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		SupplyResult<String> result = new SupplyResult<String>();
		map.put("result", result);
		if (url == null) {
			result.setResultMsg("请求参数缺失");
			return map;
		}
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(httpPool)
				.setRetryHandler(handler).build();
		InputStream inputStream = null;
		Integer statusCode = -1;
		logger.warn("request: " + url);
		HttpGet get = null;
		try {
			get = new HttpGet(url);
		} catch (Exception e) {
			logger.error("url error " + url, e.getMessage());
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("url error " + url);
			return map;
		}
		get.setConfig(requestConfig);

		CloseableHttpResponse response = null;
		try {
			get.setHeader("Accept", contextType);
			response = httpClient.execute(get);
			statusCode = response.getStatusLine().getStatusCode();
			Header[] headers = response.getHeaders("Set-Cookie");
			String cookieStr = headers[0].getValue();
			String cookie = cookieStr.split(";")[0];
			map.put("cookie", cookie);
			if (statusCode != 200) {
				logger.error("connect " + url + " error httpCode : " + statusCode);
				result.setStatus(SupplyResult.STATUS_FAILED);
				result.setResultMsg(statusCode + " error");
				return map;
			} else {
				try {
					inputStream = response.getEntity().getContent();
					StringBuffer sb = new StringBuffer();
					int value = 0;
					while ((value = inputStream.read()) != -1) {
						sb.append((char) value);
					}

					String resultStr = new String(sb.toString().getBytes("ISO-8859-1"), "UTF-8");
					logger.warn("response : " + resultStr);
					result.setModule(resultStr);
					result.setStatus(SupplyResult.STATUS_SUCCESS);
				} catch (UnsupportedEncodingException e) {
					logger.error("parese response error", e);
					result.setStatus(SupplyResult.STATUS_FAILED);
					result.setResultMsg("parese response error msg : " + e.getMessage());
					return map;
				} finally {
					try {
						if (inputStream != null) {
							inputStream.close();
						}
					} catch (IOException e) {
						logger.error("inputStream close error", e);
					}
				}
			}
		} catch (Exception e) {
			logger.error("sendDataByGet IOException ", e);
			if (e instanceof SocketTimeoutException) {
				result.setStatus(SupplyResult.STATUS_UNCONFIRM);
				result.setResultMsg("请求超时");
			}
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				logger.error("closeableHttpResponse close error", e);
			}
		}
		return map;
	}
	public SupplyResult<String> sendDataByPost(String url, Map<String, String> paramMap,Map<String,String> headers) {
		SupplyResult<String> result = new SupplyResult<String>();

		if (paramMap == null || url == null) {
			result.setResultMsg("请求参数缺失");
			return result;
		}

		Integer statusCode = -1;
		HttpPost post;

		try {
			post = new HttpPost(url);
		} catch (Exception e) {
			logger.error("url error " + url, e.getMessage());
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("url error " + url);
			return result;
		}

		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(httpPool)
				.setRetryHandler(handler).build();
		logger.warn("post request: " + url + "  params : " + mapToString(paramMap));
		post.setConfig(requestConfig);

		CloseableHttpResponse response = null;

		InputStream is = null;
		try {
			for(String key : headers.keySet()){
				post.setHeader(key, headers.get(key));
			}
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			if (paramMap != null) {
				for (Map.Entry<String, String> m : paramMap.entrySet()) {
					nvps.add(new BasicNameValuePair(m.getKey(), m.getValue()));
				}
			}
			post.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
			post.setHeader("Accept", contextType);
			response = httpClient.execute(post);
			statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				logger.error("connect " + url + " error httpCode : " + statusCode);
				result.setResultMsg("请求失败  code : " + statusCode);
				return result;
			}
			is = response.getEntity().getContent();
			StringBuffer sb = new StringBuffer();
			int value = 0;
			while ((value = is.read()) != -1) {
				sb.append((char) value);
			}
			String resultStr = new String(sb.toString().getBytes("ISO-8859-1"), "UTF-8");
			logger.warn("post response : " + resultStr);
			result.setModule(resultStr);
			result.setStatus(SupplyResult.STATUS_SUCCESS);

		} catch (UnsupportedEncodingException e) {
			logger.error("sendDataByPost UnsupportedEncodingException ", e);
		} catch (IOException e) {
			logger.error("sendDataByPost IOException ", e);
			if (e instanceof SocketTimeoutException) {
				result.setStatus(SupplyResult.STATUS_UNCONFIRM);
				result.setResultMsg("请求超时");
				return result;
			}
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				logger.error("closeableHttpResponse close error", e);
			}

			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				logger.error("is close error", e);
			}
		}

		return result;
	}
	public SupplyResult<Document> sendDataByPostReturnXml(String url,String param, Map<String,String> headers) {
		SupplyResult<Document> result = new SupplyResult<Document>();
		if (url == null) {
			result.setResultMsg("请求参数缺失");
			return result;
		}
		InputStream inputStream = null;
		Integer statusCode = -1;
		logger.warn("request: " + url);
		HttpPost post;
		try {
			post = new HttpPost(url);
		} catch (Exception e) {
			logger.error("url error " + url, e.getMessage());
			result.setStatus(SupplyResult.STATUS_FAILED);
			result.setResultMsg("url error " + url);
			return result;
		}
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(httpPool).setRetryHandler(handler).build();
		logger.warn("post request: " + url + param);
		post.setConfig(requestConfig);

		CloseableHttpResponse response = null;
		try {
			if(headers != null && !headers.isEmpty()){
				for(String key : headers.keySet()){
					post.setHeader(key, headers.get(key));
				}
			}
			post.setEntity(new StringEntity(param));
			response = httpClient.execute(post);
			statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				logger.error("connect " + url + " error httpCode : " + statusCode);
				result.setStatus(SupplyResult.STATUS_FAILED);
				result.setResultMsg(statusCode + " error");
				return result;
			} else {
				SAXReader reader = new SAXReader();
				try {
					Document document = reader.read(response.getEntity().getContent());
					result.setStatus(SupplyResult.STATUS_SUCCESS);
					result.setModule(document);
				} catch (DocumentException e) {
					logger.error("parese document error", e);
					result.setStatus(SupplyResult.STATUS_FAILED);
					result.setResultMsg("parese document error msg : " + e.getMessage());
					return result;
				} finally {
					try {
						if (inputStream != null) {
							inputStream.close();
						}
					} catch (IOException e) {
						logger.error("inputStream close error", e);
					}
				}
			}
		} catch (Exception e) {
			logger.error("sendDataByGet IOException ", e);
			if (e instanceof SocketTimeoutException) {
				result.setStatus(SupplyResult.STATUS_UNCONFIRM);
				result.setResultMsg("请求超时");
			}
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				logger.error("closeableHttpResponse close error", e);
			}
		}
		return result;
	}
	
	
	
	private String mapToString(Map<String, String> paramMap) {
		if (paramMap == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer("");
		for (Entry<String, String> e : paramMap.entrySet()) {
			sb.append(e.getKey()).append("=").append(e.getValue()).append("&");
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		String url = "http://www.taobao.com";
		MultiThreadedHttpConnection.getInstance().sendDataByGet(url);
	}
	
}
