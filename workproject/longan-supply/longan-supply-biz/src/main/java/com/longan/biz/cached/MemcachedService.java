package com.longan.biz.cached;

import net.spy.memcached.CASValue;

public interface MemcachedService {
	public Long getLongValue(String key);

	public Object get(String key);

	public void set(String key, int exp, Object object);

	/**
	 * 获取并且更新失效时间， 作用就是省去再set同样的值 注： 低版本memcached不支持touch操作
	 * 
	 * @param key
	 * @param exp
	 * @return
	 */
	public Object getAndTouch(String key, int exp);

	/**
	 * 是否冲突，如果不冲突返回false ,并且占用锁3秒 , 冲突则返回true，说明被其他进程占用。
	 * 
	 * @param key
	 * @return
	 */
	public boolean isMutex(String key);

	/**
	 * 是否冲突，如果不冲突返回false ,并且按时间占用锁 , 冲突则返回true，说明被其他进程占用。
	 * 
	 * @param key
	 * @param exp
	 * @return
	 */
	public boolean isMutex(String key, int exp);
	
	
	/** 删除冲突KEY
	 * @param key
	 */
	public void deleteMutex(String key);

	/**
	 * 计数 ++ 如果没有值，则默认值是1
	 * 
	 * @param key
	 * @param exp
	 * @param count
	 * @return
	 */
	public Long inc(String key, int exp, Integer count);

	/**
	 * 计数 ++ 如果没有值，按参数指定默认值
	 * 
	 * @param key
	 * @param exp
	 * @param count
	 * @return
	 */
	public Long inc(String key, int exp, Integer count, long def);

	/**
	 * 计数 --
	 * 
	 * @param key
	 * @param count
	 * @return
	 */
	public Long dec(String key, Integer count);

	/**
	 * 设置计数
	 * 
	 * @param key
	 * @param exp
	 * @param count
	 */
	public void initCount(String key, int exp, Long count);

	public void delete(String key);
	
	public CASValue<Object> gets(String key);
	
	public boolean  cas(String key,long casId,int exp, Object value);
}
