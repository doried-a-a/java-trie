package com.markety.trie;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;
import javafx.util.Pair;

/**
 * A TrieEntry is a node in the trie tree. It contains a key (chat), value and references to all of its
 * children
 * @author doried abd-allah
 * @param <T> Class type of the value to be stored in the trie
 */
public class TrieNode<T> implements Iterable<TrieNode<T>> {
    
    /**
     * use HashMap to store the children of a trie node
     */
    static final byte CHILD_STORAGE_MODE_USING_HASHMAP=0;
    /**
     * use TreeMap to store the children of a trie node
     */
    static final byte CHILD_STORAGE_MODE_USING_TREEMAP=1;
    
    /**
     * use TreeMap or HashMap (selected randomly) to store the children of a trie node
     * May be useful to balance between time and space
     */
    static final byte CHILD_STORAGE_MODE_USING_RANDOM=2;
    
    
    private char key;
    private T value;
    private Map<Character,TrieNode<T>> childs;
    private byte childs_storage_mode;
    private TrieNode<T> parent;

    /**
     * 
     * @param key the char defining this entry
     * @param value the value to be stored in this entry
     * @param childStorageMode  the storage mode of the children in this entry. CHILD_STORAGE_MODE_USING_HASHMAP or CHILD_STORAGE_MODE_USING_TREEMAP
     */
    TrieNode(char key, T value, byte childsStorageMode,TrieNode<T> parent) {
        this.key = key;
        this.value = value;
        this.parent=parent;
        this.childs_storage_mode=childsStorageMode;
       
        if(childsStorageMode==CHILD_STORAGE_MODE_USING_HASHMAP)
            this.childs = new HashMap<>();
        else if(childsStorageMode==CHILD_STORAGE_MODE_USING_TREEMAP)
            this.childs = new TreeMap<>();
        else{
            if(key%2==0)
                this.childs = new HashMap<>();
            else
                this.childs = new TreeMap<>();
            
        }
    }

    
    public char getChar() {
        return this.key;
    }
      
    /**
     * Builds and returns the full key of this node (its parent key + its char)
     * @return the full key of this node (its parent key + its char)
     */
    public String getKey() {
        // root node has no key. It just have pointers to other nodes, with keys
        if(parent==null)
            return "";
        else
            return "" + parent.getKey() + this.key;
    }

    public T getValue() {
        return this.value;
    }

    
    /**
     * finds a child of this node, with the specified char (key)
     * @param key a char representing the key you are searching for.
     * @return the node if found, null otherwise.
     */
    TrieNode findChild(char key) {
        return this.childs.get(key);
    }

    /**
     * Associates the value passed by parameter 'value' with the key, or replaces it if there was already a value
     * A path to the node is built if it does not exist.
     * @param word a string representing the key (directing for the path to the node).
     * @param value the value to be stored along with that key.
     * @return The previous value if the entry was already exist, null otherwise
     */
    T put(String word, T value) {
        if (word.length() == 0) {
            T prev = this.value;
            this.value = value;
            return prev;
        } else {
            char ch = word.charAt(0);
            TrieNode<T> child = findChild(ch);
            
            if (child == null) {
                child = new TrieNode(ch, null,childs_storage_mode,this);
                this.childs.put(ch,child);
            }
            return child.put(word.substring(1), value);
        }
    }
    
    
    /**
     * Removes the entry specified by the key from the trie. And prunes and resulting unused branches.
     * @param word the path to the entry to be removed
     * @return the removed value if exist.
     */
    T remove(String word) {
        TrieNode<T> node = getNode(word);
        if(node==null)
            return null;
        
        T prev_val = node.getValue();
        
        node.put("", null);

        while(node.parent != null && node.childs.isEmpty()){
            TrieNode<T> child = node;
            if(child.value!=null)
                break;
            node = node.parent;
            node.childs.remove(child.key);
        }
        
        return prev_val;
    }

    /**
     * finds and returns the node(Entry) specified by the passed key.
     * @param word the key, specifying the path to the node
     * @return the node specified by that key, null if the node does not exist.
     */
    TrieNode<T> getNode(String word) {
        if (word.length() == 0) {
            return this;
        } else {
            TrieNode child = findChild(word.charAt(0));
            if (child == null) {
                return null;
            } else {
                return child.getNode(word.substring(1));
            }
        }
    }

     /**
     * finds and returns the value specified by the passed key.
     * @param word the key, specifying the path to the node
     * @return the value specified by that key, null if the key does not exist.
     */
    T get(String word) {
        TrieNode<T> node = getNode(word);
        if (node == null) {
            return null;
        } else {
            return node.getValue();
        }
    }

    /**
     * Gets an iterator to iterate trough all the entries that can be accessed from the current node, in DFS order.
     * @return an iterator to iterate through the nodes set accessed from this key using DFS
     */
    @Override
    public Iterator<TrieNode<T>> iterator() {
        return new MyNodesIterator();
    }
    
    /**
     * Gets an iterator to iterate trough all the keys that can be formed from the current node, in DFS order. The char of the current node is not included in the resulting keys
     * @return an iterator to iterate through the key set that can be formed from this key using DFS
     */
    public Iterator<String> getKeysIterator() {
        return new MyKeysIterator();
    }

    
    class MyNodesIterator implements Iterator<TrieNode<T>>{
        
        private Stack< TrieNode > stack;
        private TrieNode next = null;
        
        public MyNodesIterator(){
            stack = new Stack<>();
            stack.add(TrieNode.this );
            findNext();
        }
        
        /**
         * finds the next node in the trie structure that has a value stored in.
         */
        private void findNext(){
            
            if(stack.isEmpty()){
                next=null;
                return;
            }
            
            TrieNode<T> trie = stack.pop();
            
            for(TrieNode<T> child:trie.childs.values())
               stack.add(child);
            
            while(!stack.isEmpty() && trie.getValue()==null){
               trie = stack.pop();
               for(TrieNode<T> child:trie.childs.values())
                   stack.add(child);
            }
            
            if(trie.getValue()==null)
                next=null;
            else
                next=trie;  
        }
        
        
        
        @Override
        public boolean hasNext() {
            return next!=null;
        }

        @Override
        public TrieNode next() {
            TrieNode next = this.next;
            findNext();
            return next;
        }   
    }
    
    class MyKeysIterator implements Iterator<String>{

        private Stack< Pair<String,TrieNode> > stack;
        private String next = null;
        
        public MyKeysIterator(){
            stack = new Stack<>();
            stack.add(new Pair("",TrieNode.this));
            findNext();
        }
        
        /**
         * finds the next node in the trie structure that has a value stored in.
         */
        private void findNext(){
            if(stack.isEmpty()){
                next=null;
                return;
            }
            
            Pair<String,TrieNode> top = stack.pop();
            TrieNode<T> trie = top.getValue();
            
            for(TrieNode<T> child:trie.childs.values())
               stack.add( new Pair(top.getKey()+child.getChar(),child));
            
            while(!stack.isEmpty() && trie.getValue()==null){
               top = stack.pop();
               trie = top.getValue();
               for(TrieNode<T> child:trie.childs.values())
                   stack.add( new Pair(top.getKey()+child.getChar(),child));
            }
            
            if(trie.getValue()==null)
                next=null;
            else
                next=top.getKey();  
        }
        
        
        
        @Override
        public boolean hasNext() {
            return next!=null;
        }

        @Override
        public String next() {
            String next = this.next;
            findNext();
            return next;
        }   
    }   
    
    public byte getChildsStorageMode(){
        return this.childs_storage_mode;
    }
}
