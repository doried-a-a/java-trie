/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.markety.trie;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author doried
 */
public class TrieMap<T> implements Iterable<TrieNode<T>>,Map<String,T>{

    //================================CONSTANTS=================================
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
    
    //================================FIELDS====================================
    private TrieNode<T> root;
    private byte childStorageMode;
    private Set<String> keySet;
    private boolean readOnly=false;
    
    
    //================================CONSTRUCTORS==============================
    /**
     * Builds a trie using Random selection (hashmap or treemap)  as childs_storage-mode for each node (for balancing execution time and memory)
     */
    public TrieMap(){
        this.root = new TrieNode('0',null,CHILD_STORAGE_MODE_USING_RANDOM,null);
        this.childStorageMode=CHILD_STORAGE_MODE_USING_HASHMAP;
        this.readOnly=false;
        this.keySet= new HashSet<>();
    }
    
    /**
     * Build a trie using an entry
     * @param entry 
     */
    private TrieMap(TrieNode<T> entry,boolean readOnly){
        
        this.root = entry;
        this.childStorageMode = entry.getChildsStorageMode();
        this.readOnly=readOnly;
        this.keySet=new HashSet<>();
        
        Iterator<String> it = entry.getKeysIterator();
        while(it.hasNext())
            this.keySet.add(it.next());
    }
    
    /**
     * Builds a trie using the specified childs storage mode
     * @param childsStorageMode A parameter used to tell the trie which storage mode to use when storing childs. Use HashMap for better execution time, TreeMap for better memory usage, or Random selection for balancing.
     */
    public TrieMap(byte childsStorageMode){
        this.root = new TrieNode('0',null,childsStorageMode,null);
        this.childStorageMode=childsStorageMode;
        this.readOnly=false;
        this.keySet = new HashSet<>();
    }
    
    //================================CLASS METHODS=============================
    
    /**
     * puts or replaces the value specified by the key
     * @param key the key
     * @param value the value to store
     * @return the previous value if exist, null otherwise
     */
    @Override
    public T put(String key, T value){
        if(readOnly)
            throw new RuntimeException("Can not make modifications to read-only trie-view");
        
        T prev_val = root.put(key, value);
        if(prev_val==null){
            keySet.add(key);
        }
        return prev_val;
    }

    /**
     * @param key the key
     * @return the value associated with the key
     */
    public T get(Object key){
        if(key instanceof String)
            return root.get((String)key);
        else return null;
    }
    
    /**
     * @param key the key of the entry to remove
     * @return the value of the removed entry if exist
     */
    public T remove(Object key){
        if(readOnly)
            throw new RuntimeException("Can not make modifications to read-only trie-view");
        if(key instanceof String == false)
            return null;
        
        T prev_val = root.remove((String)key);
        keySet.remove((String)key);
        
        return prev_val;
    }
    
    /**
     * Creates a trie whose root is the node specified by the key.
     * Its useful in cases like when you want to find all the keys starting with a specified prefix
     * This operation initializes a new trie, explores its keys, stores them in trie.KeySet and returns the resulting object.
     * The resulting object is read only, Its parent will not know about any changes made to it.
     * Ex: if you tried to perform subtrie.put(key,val), the number of keys in the original trie would not be changed.
     * To avoid such situations, we return a read-only try.
     * @param key the key specifing the entry around which the trie will be created.
     * @return a trie whose root is the entry specified by key, null if such entry does not exist.
     */
    public TrieMap<T> getSubTrie(String key){
        TrieNode<T> entry = root.getNode(key);
        if(entry==null)
            return null;
        else return new TrieMap(entry,true);
                    
    }
    
    /**
     * Retrieves an iterator which iterates through all the entries in the TrieMap
     */
    @Override
    public Iterator<TrieNode<T>> iterator() {
        return root.iterator();
    }
    
    /**
     * Retrieves an iterator which iterates through all the keys in the TrieMap
     * @return Iterator which iterates on the keyset of this TrieMap in DFS order.
    */
    public Iterator<String> getKeySetIterator(){
        return root.getKeysIterator();
    }


    @Override
    public int size() {
        return keySet().size();
    }

    @Override
    public boolean isEmpty() {
        return this.size()==0;
    }
    
    @Override
    public boolean containsKey(Object key) {
        if(key instanceof String==false)
            return false;
        return keySet.contains((String)key);
    }

    @Override
    public boolean containsValue(Object value) {
        Iterator<TrieNode<T>> it = this.iterator();
        while(it.hasNext()){
            if(it.next().getValue()==value)
                return true;
        }
        return false;
    }



    /**
     * Retrieves a set of all keys starting with the specified prefix
     * @param prefix
     * @return a set of Strings containing all keys starting with the specified prefix
     */
    public LinkedList<String> getKeysStartingWith(String prefix){
        TrieMap<T> trie = getSubTrie(prefix);
        if(trie==null)
            return new LinkedList();
        Set<String> suffixes=trie.keySet();
        
        LinkedList<String> list = new LinkedList<String>();
        
        for(String suffix:suffixes)
            list.add(prefix+suffix);
        return list;
        
    }

    @Override
    public void putAll(Map<? extends String, ? extends T> m) {
        for( Map.Entry entry : m.entrySet()){
            this.put((String)entry.getKey(), (T)entry.getValue());
        }
    }

    @Override
    public void clear() {
        if(readOnly)
            throw new RuntimeException("Can not make modifications to read-only trie-view");
        this.root = new TrieNode('0',null,this.childStorageMode,null);
        this.keySet=new HashSet<>();
    }

    @Override
    public Set<String> keySet() {
        return this.keySet;
    }

    @Override
    public Collection<T> values() {
        ArrayList<T> values = new ArrayList<>();
        Iterator<TrieNode<T>> it = this.iterator();
        while(it.hasNext()){
            values.add(it.next().getValue());
        }
        return values;
    }

    @Override
    public Set<Map.Entry<String,T>> entrySet() {
        Set< Map.Entry<String,T> > set = new HashSet();
        
        Iterator<TrieNode<T>> it = this.root.iterator();
        int cnt=0;
        while(it.hasNext()){
            cnt++;
            TrieNode<T> node = it.next();
            set.add(new TrieMap.Entry(node.getKey(),node.getValue()));
        }
        return set;
    }
    
    public class Entry<T> implements Map.Entry<String, T>{

        private String key;
        private T value;
        public Entry(String key,T value){
            this.key=key;
            this.value=value;
        }
        
        @Override
        public String getKey() {
            return this.key;
        }

        @Override
        public T getValue() {
            return this.value;
        }

        @Override
        public T setValue(T value) {
            T prev = this.value;
            this.value=value;
            return prev;
        }
        
    }


}
