//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.macro.mall.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IdentifierGenerator {
    private static final Logger logger = LoggerFactory.getLogger(IdentifierGenerator.class);
    private static SnowflakeIdWorker idWorker;

    public IdentifierGenerator() {
    }

    public static long nextId() {
        long id = idWorker.nextId();
        return Long.valueOf(id);
    }

    static {
        try {
            long idWorkerId = Long.parseLong(System.getProperty("idWorkerId", "1"));
            long idDataCenterId = Long.parseLong(System.getProperty("idDataCenterId", "1"));
            idWorker = new SnowflakeIdWorker(idWorkerId, idDataCenterId);
        } catch (NumberFormatException var4) {
            logger.error(">>>>>>初始化ID生成器出错", var4);
        }

    }

    public static void main(String[] args) {
        System.out.println(nextId());
    }
}
