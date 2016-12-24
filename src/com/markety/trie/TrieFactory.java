/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.markety.trie;

/**
 *
 * @author doried
 */
public class TrieFactory {
    
  
     /**
     * Use this method to get a trie optimized for runtime. This trie uses a hashmap inside each node its childs. so it uses a little bit more memory.
     * @return an empty try
     */
    public static TrieMap createTrieMapOptimizedForExecutionTime(){
        return new TrieMap(TrieMap.CHILD_STORAGE_MODE_USING_HASHMAP);
    }
    
    /**
     * Use this method to get a trie optimized for memory usage. This trie uses a treemap inside each node its childs. so it consumes a little bit more time than a hashmap based trie.
     * @return an empty try
     */
    public static TrieMap createTrieMapOptimizedForMemory(){
        return new TrieMap(TrieMap.CHILD_STORAGE_MODE_USING_TREEMAP);
    }
    
     /**
     * Use this method to get a trie balancing between execution time and memory usage. This trie combines the usage of a treemap and a hashmap inside its nodes. some nodes use treemap while others uses hashmap (randomly). So it tries to balance memory usage against time consumption.
     * @return an empty try
     */
    public static TrieMap createTrieMapBalancingExecutionTimeAndMemory(){
        return new TrieMap(TrieMap.CHILD_STORAGE_MODE_USING_RANDOM);
    }
}
