package com.aiyangniu.demo.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @description：
 * @author：Mr.Liu
 * @create: 2023-10-23
 */
@Slf4j
public class ReentrantLockDemo {

    /** 获取ReentrantLock对象 **/
    private static ReentrantLock lock = new ReentrantLock(true);

    /**
     * 基本语法
     */
//    public static void main(String[] args) {
//        // 加锁，获取不到锁一直等待直到获取锁
//        lock.lock();
//        try {
//            // 临界区
//            // 需要执行的代码
//        }finally {
//            // 释放锁，如果不释放其他线程就获取不到锁
//            lock.unlock();
//        }
//    }

    /**
     * 可重入
     */
    public static void main(String[] args) {
        lock.lock();
        try {
            log.debug("entry main...");
            m1();
        } finally {
            lock.unlock();
        }
    }

    private static void m1() {
        lock.lock();
        try {
            log.debug("entry m1...");
            m2();
        } finally {
            lock.unlock();
        }
    }

    private static void m2() {
        log.debug("entry m2...");
    }







    
}
