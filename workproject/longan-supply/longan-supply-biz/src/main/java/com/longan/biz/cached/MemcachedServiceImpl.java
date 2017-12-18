package com.longan.biz.cached;

import javax.annotation.Resource;

import net.spy.memcached.CASResponse;
import net.spy.memcached.CASValue;
import net.spy.memcached.MemcachedClient;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MemcachedServiceImpl implements MemcachedService {
	private static final Logger logger = LoggerFactory.getLogger(MemcachedServiceImpl.class);

	@Resource
	private MemcachedClient memcachedClient;

	public static final int MUTEX_EXP = 3;

	public static final String MUTEX_KEY_PREFIX = "MUTEX_";

	public Long getLongValue(String key) {
		Long result = null;
		try {
			Object object = memcachedClient.get(key);
			if (object != null && object instanceof String) {
				String str = (String) object;
				if (StringUtils.isNumeric(str.trim())) {
					result = Long.parseLong(str.trim());
				} else {
					logger.error("memcached get error key : " + key + " value is not num  value: "
							+ str);
				}
			} else if (object != null && object instanceof Long) {
				result = (Long) object;
			} else if (object != null && object instanceof Integer) {
				result = ((Integer) object).longValue();
			}
		} catch (Exception e) {
			logger.error("memcached get error key : " + key, e);
		}
		return result;
	}

	public Object get(String key) {
		Object object = null;
		try {
			object = memcachedClient.get(key);
		} catch (Exception e) {
			logger.error("memcached get error key : " + key, e);
		}
		return object;
	}

	@Override
	public void set(String key, int exp, Object object) {
		try {
			memcachedClient.set(key, exp, object);
		} catch (Exception e) {
			logger.error("memcached set error key : " + key, e);
		}
	}

	/*
	 * 低版本的memcached 不支持慎用
	 */
	public Object getAndTouch(String key, int exp) {
		Object object = null;
		try {
			object = memcachedClient.getAndTouch(key, exp);
		} catch (Exception e) {
			logger.error("memcached get error key : " + key, e);
		}
		return object;
	}

	public boolean isMutex(String key) {
		return isMutex(key, MUTEX_EXP);
	}

	@Override
	public boolean isMutex(String key, int exp) {
		boolean status = true;
		try {
			if (memcachedClient.add(MUTEX_KEY_PREFIX + key, exp, "mutex").getStatus().isSuccess()) {
				status = false;
			}
		} catch (Exception e) {
			logger.error("memcached isMutex error key : " + key, e);
			status = false;
		}
		return status;
	}
	
	@Override
	public void deleteMutex(String key) {
		if (StringUtils.isEmpty(key)) {
			logger.error("memcached deleteMutex error key is null");
			return;
		}
		try {
			memcachedClient.delete(MUTEX_KEY_PREFIX + key);
		} catch (Exception e) {
			logger.error("memcached deleteMutex error key : " + key, e);
		}
		
	}

	@Override
	public Long inc(String key, int exp, Integer count) {
		try {
			return memcachedClient.incr(key, count, 1, exp);
		} catch (Exception e) {
			logger.error("memcached inc error key : " + key, e);
		}
		return null;
	}

	@Override
	public Long dec(String key, Integer count) {
		try {
			return memcachedClient.decr(key, count);
		} catch (Exception e) {
			logger.error("memcached dec error key : " + key, e);
		}
		return null;

	}

	@Override
	public Long inc(String key, int exp, Integer count, long def) {
		try {
			return memcachedClient.incr(key, count, def, exp);
		} catch (Exception e) {
			logger.error("memcached inc error key : " + key, e);
		}
		return null;
	}

	@Override
	public void initCount(String key, int exp, Long count) {
		if (count == null) {
			return;
		}
		try {
			// 这里居然要 toString 才可以算计数，费解
			memcachedClient.set(key, exp, count.toString());
		} catch (Exception e) {
			logger.error("memcached set error key : " + key, e);
		}
	}

	@Override
	public void delete(String key) {
		if (StringUtils.isEmpty(key)) {
			logger.error("memcached remove error key is null");
			return;
		}
		try {
			memcachedClient.delete(key);
		} catch (Exception e) {
			logger.error("memcached delete error key : " + key, e);
		}
	}

	@Override
	public CASValue<Object> gets(String key) {
		CASValue<Object> result = null;
		if (StringUtils.isEmpty(key)) {
			logger.error("memcached gets error key is null");
			return result;
		}
		try {
			result = memcachedClient.gets(key);
		} catch (Exception e) {
			logger.error("memcached gets error key : " + key, e);
		}
		return result;
	}

	@Override
	public boolean cas(String key, long casId, int exp, Object value) {
		CASResponse response = null;
		try {
			// response = memcachedClient.cas(key, casId, value);
			response = memcachedClient.cas(key, casId, exp, value, memcachedClient.getTranscoder());
		} catch (Exception e) {
			logger.error("memcached cas error key : " + key, e);
		}
		if (response != null && response.equals(CASResponse.OK)) {
			return true;
		}
		return false;
	}

}
