
import com.markety.trie.TrieFactory;
import com.markety.trie.TrieMap;
import com.markety.trie.TrieNode;
import java.util.Iterator;


public class Demo {

    public static void main(String[] args) {

        // obtaining a trie
        TrieMap<String> addreses = TrieFactory.createTrieMapOptimizedForMemory();
        
        addreses.put("doried", "Syria");
        addreses.put("domass", "Lebanon");
        addreses.put("dooran", "USA");
        addreses.put("markety", "Latakia");
        addreses.put("market", "Aleppo");
        addreses.put("doried123", "U.K");
        addreses.put("diana", "UAE-Dubai");
        
        System.out.println("I have " + addreses.size() + " entries.");
        
        System.out.println("I have " + addreses.getSubTrie("do").size() + " keys starting with 'do'. They are:");
        for(String key:addreses.getKeysStartingWith("do"))
            System.out.println("----" + key);
        System.out.println("==========");
        
        System.out.println("Addresses contain key 'markety' ? " + addreses.containsKey("markety"));
        System.out.println("Addresses contain key 'marketo' ? " + addreses.containsKey("marketo"));
        System.out.println("Addresses contain value 'Syria' ? " + addreses.containsValue("Syria"));
        
        System.out.println("==========");
        
        System.out.println("Removing entry with key 'domass'.." );
        addreses.remove("domass");
        
        
        System.out.println("Now I have " + addreses.size() + " entries remaining.");
        
        System.out.println("==========");
        System.out.println("Iterating through all entries, printing them:");
        
        Iterator<TrieNode<String>> it = addreses.iterator();
        while(it.hasNext()){
            TrieNode<String> node = it.next();
            
            System.out.println("-----" + node.getKey() + " : " + node.getValue());
        }
        
        System.out.println("==========");
        
        System.out.println("Removing all entries..");
        addreses.clear();
        
        System.out.println("Number of entries now is " + addreses.size() + 
                " and isEmpty is " + addreses.isEmpty());
        
        
        
        
        
        

    }

}