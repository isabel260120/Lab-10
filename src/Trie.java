import java.util.*; //permite importar todas las clases e interfaces

public class Trie {
    private final TrieNode root;
    private long globalOrder;

    public Trie() {
        root = new TrieNode();
        globalOrder = 0;
    }

    // Parte 1: insertar palabra
    public void insert(String word) {
        if (word == null || word.isEmpty()) {
            return;
        }

        TrieNode current = root;

        for (char c : word.toCharArray()) {
            current.children.putIfAbsent(c, new TrieNode());
            current = current.children.get(c);
        }

        if (!current.isWord) {
            current.isWord = true;
            current.insertionOrder = globalOrder++;
        }

        current.frequency++;
    }

    // Parte 1: buscar palabra completa
    public boolean search(String word) {
        TrieNode node = getNode(word);
        return node != null && node.isWord;
    }

    // Parte 1: buscar prefijo
    public boolean startsWith(String prefix) {
        return getNode(prefix) != null;
    }

    // Método auxiliar para llegar al nodo final de una cadena
    private TrieNode getNode(String str) {
        if (str == null) {
            return null;
        }

        TrieNode current = root;

        for (char c : str.toCharArray()) {
            if (!current.children.containsKey(c)) {
                return null;
            }
            current = current.children.get(c);
        }

        return current;
    }

    // Parte 2: recorrido preorder -> mostrar palabras almacenadas
    public List<String> preorder() {
        List<String> result = new ArrayList<>();
        preorderDFS(root, new StringBuilder(), result);
        return result;
    }

    private void preorderDFS(TrieNode node, StringBuilder currentWord, List<String> result) {
        if (node.isWord) {
            result.add(currentWord.toString());
        }

        List<Character> keys = new ArrayList<>(node.children.keySet());
        Collections.sort(keys);

        for (char c : keys) {
            currentWord.append(c);
            preorderDFS(node.children.get(c), currentWord, result);
            currentWord.deleteCharAt(currentWord.length() - 1);
        }
    }

    // Parte 2: recorrido postorder -> recorrido de nodos
    public List<String> postorder() {
        List<String> result = new ArrayList<>();
        postorderDFS(root, new StringBuilder(), result);
        return result;
    }

    private void postorderDFS(TrieNode node, StringBuilder currentWord, List<String> result) {
        List<Character> keys = new ArrayList<>(node.children.keySet());
        Collections.sort(keys);

        for (char c : keys) {
            currentWord.append(c);
            postorderDFS(node.children.get(c), currentWord, result);
            currentWord.deleteCharAt(currentWord.length() - 1);
        }

        if (node.isWord) {
            result.add(currentWord.toString());
        }
    }

    // Parte 2: BFS -> mostrar nodos por niveles
    public List<String> bfsLevels() {
        List<String> result = new ArrayList<>();
        Queue<NodeLevel> queue = new LinkedList<>();

        queue.offer(new NodeLevel(root, ""));

        while (!queue.isEmpty()) {
            NodeLevel current = queue.poll();
            TrieNode node = current.node;
            String word = current.word;

            if (node.isWord) {
                result.add(word);
            }

            List<Character> keys = new ArrayList<>(node.children.keySet());
            Collections.sort(keys);

            for (char c : keys) {
                queue.offer(new NodeLevel(node.children.get(c), word + c));
            }
        }

        return result;
    }

    private static class NodeLevel {
        TrieNode node;
        String word;

        NodeLevel(TrieNode node, String word) {
            this.node = node;
            this.word = word;
        }
    }

    // Parte 3: autocomplete simple
    public List<String> autocomplete(String prefix) {
        List<String> result = new ArrayList<>();
        TrieNode node = getNode(prefix);

        if (node == null) {
            return result;
        }

        collectWords(node, new StringBuilder(prefix), result);
        return result;
    }

    private void collectWords(TrieNode node, StringBuilder currentWord, List<String> result) {
        if (node.isWord) {
            result.add(currentWord.toString());
        }

        List<Character> keys = new ArrayList<>(node.children.keySet());
        Collections.sort(keys);

        for (char c : keys) {
            currentWord.append(c);
            collectWords(node.children.get(c), currentWord, result);
            currentWord.deleteCharAt(currentWord.length() - 1);
        }
    }

    // Parte 4: autocomplete top-k por frecuencia
    public List<String> autocomplete(String prefix, int k) {
        List<WordData> words = new ArrayList<>();
        TrieNode node = getNode(prefix);

        if (node == null || k <= 0) {
            return new ArrayList<>();
        }

        collectWordData(node, new StringBuilder(prefix), words);

        words.sort((a, b) -> {
            if (b.frequency != a.frequency) {
                return Integer.compare(b.frequency, a.frequency); // mayor frecuencia primero
            }
            return Long.compare(a.insertionOrder, b.insertionOrder); // más antigua primero
        });

        List<String> result = new ArrayList<>();
        for (int i = 0; i < Math.min(k, words.size()); i++) {
            result.add(words.get(i).word);
        }

        return result;
    }

    private void collectWordData(TrieNode node, StringBuilder currentWord, List<WordData> words) {
        if (node.isWord) {
            words.add(new WordData(currentWord.toString(), node.frequency, node.insertionOrder));
        }

        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            currentWord.append(entry.getKey());
            collectWordData(entry.getValue(), currentWord, words);
            currentWord.deleteCharAt(currentWord.length() - 1);
        }
    }

    private static class WordData {
        String word;
        int frequency;
        long insertionOrder;

        WordData(String word, int frequency, long insertionOrder) {
            this.word = word;
            this.frequency = frequency;
            this.insertionOrder = insertionOrder;
        }
    }

    // Parte 5: mejora -> búsqueda con comodín '.'
    public boolean searchWildcard(String pattern) {
        if (pattern == null) {
            return false;
        }
        return searchWildcardDFS(root, pattern, 0);
    }

    private boolean searchWildcardDFS(TrieNode node, String pattern, int index) {
        if (index == pattern.length()) {
            return node.isWord;
        }

        char c = pattern.charAt(index);

        if (c == '.') {
            for (TrieNode child : node.children.values()) {
                if (searchWildcardDFS(child, pattern, index + 1)) {
                    return true;
                }
            }
            return false;
        } else {
            if (!node.children.containsKey(c)) {
                return false;
            }
            return searchWildcardDFS(node.children.get(c), pattern, index + 1);
        }
    }

}
