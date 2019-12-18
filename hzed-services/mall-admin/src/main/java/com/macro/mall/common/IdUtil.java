package com.macro.mall.common;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 32bit秒级时间戳+16bit自增+5bit机器标识组成
 * 53 bits unique id:53位的唯一ID（区别于雪花算法64位，是因为64位长度的id在于web前端交互时存在精度丢失的问题）
 * |--------|--------|--------|--------|--------|--------|--------|--------|
 * |00000000|00011111|11111111|11111111|11111111|11111111|11111111|11111111|
 * |--------|---xxxxx|xxxxxxxx|xxxxxxxx|xxxxxxxx|xxx-----|--------|--------|
 * |--------|--------|--------|--------|--------|---xxxxx|xxxxxxxx|xxx-----|
 * |--------|--------|--------|--------|--------|--------|--------|---xxxxx|
 *
 * Maximum ID = 11111_11111111_11111111_11111111_11111111_11111111_11111111
 *
 * Maximum TS = 11111_11111111_11111111_11111111_111
 *
 * Maximum NT = ----- -------- -------- -------- ---11111_11111111_111 = 65535
 *
 * Maximum SH = ----- -------- -------- -------- -------- -------- ---11111 = 31
 *
 * It can generate 64k unique id per IP and up to 2106-02-07T06:28:15Z.
 */

/**
 * @author dsy on 2019/11/18
 */
@Slf4j
public final class IdUtil {

    private static final Pattern PATTERN_LONG_ID = Pattern.compile("^([0-9]{15})([0-9a-f]{32})([0-9a-f]{3})$");

    private static final Pattern PATTERN_HOSTNAME = Pattern.compile("^.*\\D+([0-9]+)$");

    private static final long OFFSET = LocalDate.of(2000, 1, 1).atStartOfDay(ZoneId.of("Z")).toEpochSecond();

    private static final long MAX_NEXT = 0b11111_11111111_111L;

    private static long offset = 0;

    private static long lastEpoch = 0;

    private static final long SHARD_ID = getServerIdAsLong();

    public static long nextId() {
        return nextId(System.currentTimeMillis() / 1000);
    }

    private static synchronized long nextId(long epochSecond) {
        if (epochSecond < lastEpoch) {
            // warning: clock is turn back:
            log.warn("clock is back: " + epochSecond + " from previous:" + lastEpoch);
            epochSecond = lastEpoch;
        }
        if (lastEpoch != epochSecond) {
            lastEpoch = epochSecond;
            reset();
        }
        offset++;
        long next = offset & MAX_NEXT;
        if (next == 0) {
            //1秒钟之内达到最大值
            log.warn("maximum id reached in 1 second in epoch: " + epochSecond);
            return nextId(epochSecond + 1);
        }
        return generateId(epochSecond, next, SHARD_ID);
    }

    private static void reset() {
        offset = 0;
    }

    private static long generateId(long epochSecond, long next, long shardId) {
        return ((epochSecond - OFFSET) << 21) | (next << 5) | shardId;
    }

    private static long getServerIdAsLong() {
        long result = 0;
        try {
            //机器码设置成host-1格式
            String hostname = InetAddress.getLocalHost().getHostName();
            Matcher matcher = PATTERN_HOSTNAME.matcher(hostname);
            if (matcher.matches()) {
                long n = Long.parseLong(matcher.group(1));
                if (n >= 0 && n < 8) {
                    log.info("detect server id from host name {}: {}.", hostname, n);
                    return n;
                }
            }else{
                //如果服务器名称格式不匹配，则需从配置中读
                result = Long.parseLong(System.getProperty("idWorkerId", "0"));
            }
        } catch (UnknownHostException e) {
            log.warn("unable to get host name. set server id = 0.");
        }
        return result;
    }

    public static long stringIdToLongId(String stringId) {
        // a stringId id is composed as timestamp (15) + uuid (32) + serverId (000~fff).
        Matcher matcher = PATTERN_LONG_ID.matcher(stringId);
        if (matcher.matches()) {
            long epoch = Long.parseLong(matcher.group(1)) / 1000;
            String uuid = matcher.group(2);
            byte[] sha1 = HashUtil.sha1AsBytes(uuid);
            long next = ((sha1[0] << 24) | (sha1[1] << 16) | (sha1[2] << 8) | sha1[3]) & MAX_NEXT;
            long serverId = Long.parseLong(matcher.group(3), 16);
            return generateId(epoch, next, serverId);
        }
        throw new IllegalArgumentException("Invalid id: " + stringId);
    }

//    public static void main(String[] args) {
//        System.out.println(nextId());
//        System.out.println(nextId());
//        System.out.println(nextId());
//
//        test();
//    }
//    public static void test(){
//        //测试唯一性
//        //测试唯一性
//        int theadCount = 100;
//        CountDownLatch latch = new CountDownLatch(theadCount);
//        Set<Long> set = Sets.newConcurrentHashSet();//new HashSet<>();
//        int idCount = 10000;
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    latch.await();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                long t1 = System.currentTimeMillis();
//                for (int i = 0; i < idCount; i++) {
//                    set.add(nextId());
//                }
//                long t2 = System.currentTimeMillis();
//                System.out.println(Thread.currentThread().getName()+":"+(t2-t1)+"毫秒");
//            }
//        };
//        //计数器-1，并发启动
//        List<Thread> list = new ArrayList<>();
//        for (int i = 0; i < theadCount; i++) {
//            Thread t = new Thread(runnable);
//            t.start();
//            list.add(t);
//            latch.countDown();
//        }
//        //等待所有线程执行完
//        for (Thread thread: list) {
//            try {
//                thread.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        long p1=idCount*theadCount,p2=set.size();
//        System.out.println("预期值："+p1+",实际值：" + p2);
//        Assert.isTrue(p1 == p2 ,"不一致");
//    }
}