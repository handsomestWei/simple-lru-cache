package com.wjy.simple.lru;

import java.util.HashMap;

// 使用自定义双端队列辅助。哈希表保存节点对象，操作节点的时间复杂度为O(1)
public class ImproveLRUCache {

    private int size;
    private int capacity;
    private HashMap<Integer, Node> nodeMap;
    private Node headNode;
    private Node tailNode;

    public ImproveLRUCache(int capacity) {
        this.size = 0;
        this.capacity = capacity;
        this.nodeMap = new HashMap<>();
        this.headNode = new Node();
        this.tailNode = new Node();
        // 双向链表，头尾指针互相指向
        this.headNode.preNode = tailNode;
        this.headNode.nextNode = tailNode;
        this.tailNode.preNode = headNode;
        this.tailNode.nextNode = headNode;
    }

    public int get(int key) {
        Node node = nodeMap.get(key);
        if (node != null) {
            // 节点被访问，移动到队尾
            move2Tail(node);
            return node.val;
        } else {
            return -1;
        }
    }

    public void put(int key, int value) {
        Node node = nodeMap.get(key);
        if (node != null) {
            // key已存在，更新节点的值
            node.val = value;
            // 节点被访问，移动到队尾
            move2Tail(node);
        } else {
            // 创建新节点
            node = new Node();
            node.key = key;
            node.val = value;
            if (size < capacity) {
                // 容量未满，新节点添加到队尾
                add2Tail(node);
                // 容量+1
                size++;
            } else {
                // 容量已满，删除队头节点，该节点为最久未使用
                int oldKey = removeHead();
                // 同时删除哈希表
                nodeMap.remove(oldKey);
                // 新节点添加到队尾
                add2Tail(node);
            }
            // 新key添加到哈希表
            nodeMap.put(key, node);
        }
    }

    // 队尾添加节点
    private void add2Tail(Node node) {
        node.preNode = tailNode.preNode;
        node.nextNode = tailNode;
        tailNode.preNode.nextNode = node;
        tailNode.preNode = node;
    }

    // 移动节点到队尾
    private void move2Tail(Node node) {
        node.preNode.nextNode = node.nextNode;
        node.nextNode.preNode = node.preNode;
        node.preNode = tailNode.preNode;
        node.nextNode = tailNode;
        tailNode.preNode.nextNode = node;
        tailNode.preNode = node;
    }

    // 删除节点，并返回节点key
    private int remove(Node node) {
        int key = node.key;
        node.preNode.nextNode = node.nextNode;
        node.nextNode.preNode = node.preNode;
        node = null;
        return key;
    }

    // 删除队首节点，并返回节点key
    private int removeHead() {
        Node node = headNode.nextNode;
        int key = node.key;
        node.nextNode.preNode = headNode;
        headNode.nextNode = node.nextNode;
        node = null;
        return key;
    }

    // 自定义双向链表节点
    private class Node {
        private int key;
        private int val;
        private Node preNode;
        private Node nextNode;
    }
}
