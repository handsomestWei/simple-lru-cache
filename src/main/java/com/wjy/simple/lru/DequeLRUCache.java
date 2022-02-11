package com.wjy.simple.lru;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;

// 使用jdk双端队列辅助。每次操作节点需要遍历整个链表找到目标，时间复杂度O(n)
public class DequeLRUCache {

    private int capacity;
    private Deque<Integer> keyDeque;
    private HashMap<Integer, Integer> kv;

    public DequeLRUCache(int capacity) {
        this.capacity = capacity;
        // 双端队列保存key
        this.keyDeque = new LinkedList();
        // kv哈希表
        this.kv = new HashMap<>();
    }

    public int get(int key) {
        if (kv.containsKey(key)) {
            // key被访问，更新key的生命周期
            flushLifeCycle(key);
            return kv.get(key);
        } else {
            return -1;
        }
    }

    public void put(int key, int value) {
        if (kv.containsKey(key)) {
            // key被访问，更新key的生命周期
            flushLifeCycle(key);
            kv.put(key, value);
        } else {
            // 容量是否已满
            if (keyDeque.size() == capacity) {
                // 最久未被使用key
                int oldKey = keyDeque.removeFirst();
                kv.remove(oldKey);
            }
            // 最新添加的key从队尾插入
            keyDeque.addLast(key);
            kv.put(key, value);
        }
    }

    // key被使用时，从队列移出并插入队尾
    private void flushLifeCycle(int key) {
        // 每次操作需要遍历整个链表，找到目标节点
        keyDeque.remove(key);
        keyDeque.addLast(key);
    }
}
