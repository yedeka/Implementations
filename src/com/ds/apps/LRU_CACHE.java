package com.ds.apps;

import java.util.HashMap;

public class LRU_CACHE {
    private static class Node{
        int key;
        int value;
        Node prev;
        Node next;

        public Node(int key, int value){
            this.key= key;
            this.value = value;
        }
    }
    int capacity;
    HashMap<Integer, Node> cache;
    Node head;
    Node tail;

    public LRU_CACHE(int cap){
        this.capacity = cap;
        this.cache = new HashMap<>();
        this.head = new Node(-1, -1);
        this.tail = new Node(-1 , -1);
        // Create a doubly linked list by interleaving head and tail.
        head.next = tail;
        tail.prev = head;
    }
    // now we handle the get and add functions to complete the cache functionality
    public void put(int key, int value){
        Node newNode = cache.get(key);
        if(newNode != null){ // Node already exists that means an update for an existing node is coming
            // update the node value and move the node to the head of the list.
            newNode.value = value;
            moveFirst(newNode);
        } else {
            newNode = new Node(key, value);
            if(cache.size() < capacity){
                // We are within capacity hence add the node directly to the doubly linked list
                add(newNode);
            } else {
                // We are at full capacity hence addition here is dode in two ways
                // Remove the last node of the list
                Node removal = tail.prev;
                remove(removal);
                cache.remove(removal.key);
                add(newNode);
            }
            cache.put(key, newNode);
        }
    }

    public int get(int key){
        Node node = cache.get(key);
        int returnVal = -1;
        if(node != null){
            returnVal = node.value;
            moveFirst(node);
        }
        return returnVal;
    }

    // We would need to create 3 basic operations for implementation of LRU Cache add(adds node at the beginning of the list), delete(remove node form end)
    // moveFirst (call delete and add)
    private void add(Node node){
        // We always add at the beginning indicating a recently accessed node
        Node next = head.next;
        head.next = node;
        // Attach the node to head and head.next
        node.next = next;
        node.prev = head;
        // Attach older head.next.prev = node
        next.prev = node;
    }
    private void remove(Node node){
        Node prev = node.prev;
        Node next = node.next;
        // Bypass the node to conenct prev and next
        prev.next = next;
        next.prev = prev;
        //Now simply detach the node next and prev pointers for node to be eligible for garbage collection
        node.prev = null;
        node.next = null;
    }
    private void moveFirst(Node node){
        remove(node);
        add(node);
    }


    public static void main(String[] args){
        LRU_CACHE cache = new LRU_CACHE(2);
        cache.put(1, 1);
        cache.put(2, 2);
        System.out.println(cache.get(1));
        cache.put(3, 3);
        System.out.println(cache.get(2));
        cache.put(4, 4);
        System.out.println(cache.get(1));
        System.out.println(cache.get(3));
        System.out.println(cache.get(4));
    }
}
