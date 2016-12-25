# Java-Trie
Java-Trie is a java implementation of the **trie** data structure and **Trie HashMap**, with multiple features, few of them:
- Effecint trie implementation, allowing you to store strings, effecienty retrieve strings starting with a specified prefix (ex: for autocompletion).
-  effecient hashmap, utilizing the trie data structure.
- The possibility to get a sub-trie of the trie as a dependent trie. Where you can preform tasks you need.
- Three different subnodes storage schemes, to meet your needs. One for optimizing execution time, one for optimizing memory and the last for balancing execution time and memory usage. You can easilt choose between them using the ***TrieFactory*** class.

  [![N|Solid](https://upload.wikimedia.org/wikipedia/commons/thumb/b/be/Trie_example.svg/250px-Trie_example.svg.png)](https://nodesource.com/products/nsolid)

# Examples of usage

Here is some examples, for better experience, please review 'Demo.java'.
Fortunately, our TrieMap implementation implements ***java.util.Map*** interface, so you don't need to worry about using it!

#### Bulding memory-focused Trie-based map:
    TrieMap<Integer> trie =  TrieFactory.createTrieMapOptimizedForMemory();

#### Adding some staff to it:
    trie.put("somekey", 12345);
    
#### Getting some staff:
    int myNumber = trie.get("somekey");
    
#### Finding all keys starting with the prefix "so":
    List<String> keys = trie.getKeysStartingWith("so");
    
#### Finding all entries starting with the prefix "so":
    Set<Map.Entry> entries = trie.getSubTrie("so").entrySet();

#### Iterating though all entries:
    Iterator<TrieNode<Integer>> it = trie.iterator();



# USE TRIES FOR AUTOCOMPLETION

If your app need efficient autocompletion algorithm, you don't have to build it from scratch. Just come here and use this project.
A trie offers fast-autocompletion facilities:

### Autocompletion example
    TrieMap<Boolean> trie = TrieFactory.createTrieMapOptimizedForMemory();
    for(String phrase:phrases)  // caching possible phrases
        trie.put(phrase,true);
        
    // Now you can take this trie with you! and then, when a query comes:
    
    String query_prefix = {get query from somewhere, ex: search box}
    List<String> suggestions = trie.getKeysStartingWith(query_prefix);
    
    { Render Suggestion! }
        



