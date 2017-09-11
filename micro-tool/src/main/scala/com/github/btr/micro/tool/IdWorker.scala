package com.github.btr.micro.tool

import akka.event.slf4j.SLF4JLogging

/**
	* 分布式全局ID生成器
	* tweeter的snowflake算法
	* id构成:42位的时间前缀+5位的数据节点标识+5位机器id+12位的sequence避免并发的数字(12位不够用时强制得到新的时间前缀)
	* 42位时间戳可以使用(2的42次方-1)/(1000360024*365)=139.5年
	* 10机器编码支持最多部署1024个节点
	* 12位sequence可以表示4096个数字,它是在time相同的情况下，递增该值直到为0，
	* 即一个循环结束，此时便只能等到下一个ms到来，一般情况下4096/ms的请求是不太可能出现的，所以足够使用了。
	* Created by ryze on 2017/5/11.
	*/
class IdWorker(workerId: Int, dataCenterId: Int) extends SLF4JLogging
{
	//开始时间
	private[this] val startEpoch = 1403854494756L
	//机器编码所占位数
	private[this] val workerIdBits       = 5L
	//数据中心标识所占位数
	private[this] val dataCenterIdBits   = 5L
	//支持的最大机器编码(这个移位算法可以很快计算出几位二进制数能表示的最大十进制数)
	private[this] val maxWorkerId        = -1L ^ (-1L << workerIdBits)
	//支持的最大数据标识
	private[this] val maxDataCenterId    = -1L ^ (-1L << dataCenterIdBits)
	private[this] val sequenceBits       = 12L
	//机器编码<<12
	private[this] val workerIdShift      = sequenceBits
	//数据中心标识<<12+5
	private[this] val dataCenterIdShift  = sequenceBits + workerIdBits
	//时间戳<<12+10
	private[this] val timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits
	//生成序列的掩码, 0xfff=4095
	private[this] val sequenceMask       = -1L ^ (-1L << sequenceBits)

	//毫秒内序列 0-4095
	private[this] var sequence      = 0L
	//生成Id的时间戳
	private[this] var lastTimestamp = -1L


	require(workerId <= maxWorkerId && workerId >= 0,
		s"工作Id不能大于${maxWorkerId }或者小于0")

	require(dataCenterId <= maxDataCenterId && dataCenterId >= 0,
		s"数据中心Id不能大于${maxDataCenterId }或者小于0")

	log.info(s"开始创建ID.时间戳左移${timestampLeftShift },数据中心位数${dataCenterIdBits },机器ID位数$workerIdBits,序列位数${sequenceBits },机器ID$workerId")

	/**
		* 生成以毫秒为单位的当前时间
		*
		* @return
		*/
	private[this] def timeGenerator: Long = System.currentTimeMillis

	/**
		* 阻塞到下一毫秒,获得新的时间戳
		*
		* @param lastTimeStamp
		* @return
		*/
	private[this] def blockNextMillis(lastTimeStamp: Long): Long =
	{
		val timestamp = timeGenerator
		if (timestamp <= lastTimestamp) timeGenerator
		else timestamp
	}

	/**
		* 获得下一个ID (该方法是线程安全的)
		*
		* @return SnowflakeId
		*/
	def nextId = synchronized
	{
		Option(timeGenerator)
		.map(t =>
		{
			//当前时间戳小于上一次Id生成的时间戳,说明系统时间回退过,这个时候应当抛出异常
			if (t < lastTimestamp) throw new RuntimeException(s"系统时间不正常,拒绝为${lastTimestamp - t }毫秒生成id")
			if (t equals lastTimestamp)
			{
				sequence = (sequence + 1) & sequenceMask
				//毫秒内序列溢出
				if (0 equals sequence) blockNextMillis(lastTimestamp)
			}
			//时间戳改变,毫秒内序列重置
			else sequence = 0L
			//上次生成ID的时间截
			lastTimestamp = t
			//移位并通过或运算拼到一起组成64位的ID
			((t - startEpoch) << timestampLeftShift) |
			(dataCenterId << dataCenterIdShift) |
			(workerId << workerIdShift) |
			sequence
		})
		.get
	}

	/**
		* 获取下一个ID(String)
		*
		* @return
		*/
	def nextSId: String = nextId + ""
}

object IdWorker
{
	//默认一台机器一个节点
	private val flowIdWorker = new IdWorker(1, 1)

	/**
		* 单Worker实例
		*
		* @return
		*/
	def getFlowIdWorkerInstance: IdWorker =
		flowIdWorker
}