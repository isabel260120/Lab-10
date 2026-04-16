
import java.util.HashMap;
import java.util.Map;
public class TrieNode {
    Map<Character, TrieNode> children;
    boolean isWord;
    int frequency;
    long insertionOrder;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
        frequency = 0;
        insertionOrder = Long.MAX_VALUE;
    }
}
