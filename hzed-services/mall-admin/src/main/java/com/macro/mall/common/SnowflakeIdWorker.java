//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.macro.mall.common;

/**
 * @author
 */
public class SnowflakeIdWorker {

    /** 开始时间截 (2017-01-01) */
    private final long twepoch = 1503631173676L;

    /** 机器id所占的位数 */
    private final long workerIdBits = 5L;

    /** 数据标识id所占的位数 */
    private final long dataCenterIdBits = 5L;

    /** 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数) */
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

    /** 支持的最大数据标识id，结果是31 */
    private final long maxDataCenterId = -1L ^ (-1L << dataCenterIdBits);

    /** 序列在id中占的位数 */
    private final long sequenceBits = 12L;

    /** 机器ID向左移12位 */
    private final long workerIdShift = 12L;

    /** 数据标识id向左移17位(12+5) */
    private final long dataCenterIdShift = 17L;

    /** 时间截向左移22位(5+5+12) */
    private final long timestampLeftShift = 22L;

    /** 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095) */
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    /** 工作机器ID(0~31) */
    private long workerId;

    /** 数据中心ID(0~31) */
    private long dataCenterId;

    /** 毫秒内序列(0~4095) */
    private long sequence = 0L;

    /** 上次生成ID的时间截 */
    private long lastTimestamp = -1L;

    public SnowflakeIdWorker(long workerId, long dataCenterId) {
        if (workerId <= maxDataCenterId && workerId >= 0L) {
            if (dataCenterId <= maxDataCenterId && dataCenterId >= 0L) {
                this.workerId = workerId;
                this.dataCenterId = dataCenterId;
            } else {
                throw new IllegalArgumentException(String.format("dataCenterId can't be greater than %d or less than 0", 31L));
            }
        } else {
            throw new IllegalArgumentException(String.format("workerId can't be greater than %d or less than 0", 31L));
        }
    }

    public synchronized long nextId() {
        long timestamp = this.timeGen();
        if (timestamp < this.lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", this.lastTimestamp - timestamp));
        } else {
            if (this.lastTimestamp == timestamp) {
                this.sequence = this.sequence + 1L & 4095L;
                if (this.sequence == 0L) {
                    timestamp = this.tilNextMillis(this.lastTimestamp);
                }
            } else {
                this.sequence = 0L;
            }

            this.lastTimestamp = timestamp;
            return timestamp - 1503631173676L << 22 | this.dataCenterId << 17 | this.workerId << 12 | this.sequence;
        }
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp;
        for(timestamp = this.timeGen(); timestamp <= lastTimestamp; timestamp = this.timeGen()) {
        }

        return timestamp;
    }

    protected long timeGen() {
        return System.currentTimeMillis();
    }

//    public static void main(String[] args) {
//        System.out.println(System.currentTimeMillis());
//        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(1L, 1L);
//        long startTime = System.nanoTime();
//
//        for(int i = 0; i < 50000; ++i) {
//            long id = idWorker.nextId();
//            System.out.println(id);
//        }
//
//        System.out.println((System.nanoTime() - startTime) / 1000000L + "ms");
//        System.out.println(System.currentTimeMillis());
//    }
}
