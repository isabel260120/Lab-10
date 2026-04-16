import java.util.List;
public class Main {
    public static void main(String[] args) {
        Trie trie = new Trie();

        //inserciones
        trie.insert("casa");
        trie.insert("canto");
        trie.insert("carro");
        trie.insert("carne");
        trie.insert("camino");
        trie.insert("carro");
        trie.insert("camino");

        // Search
        System.out.println("search(\"carro\") -> " + trie.search("carro"));
        System.out.println("search(\"carta\") -> " + trie.search("carta"));

        // StartsWith
        System.out.println("startsWith(\"car\") -> " + trie.startsWith("car"));

        // Recorridos
        System.out.println("\nPREORDER:");
        printList(trie.preorder());

        System.out.println("\nPOSTORDER:");
        printList(trie.postorder());

        System.out.println("\nBFS POR NIVELES:");
        printList(trie.bfsLevels());

        // Autocomplete simple
        System.out.println("\nautocomplete(\"ca\") -> " + trie.autocomplete("ca"));

        // Top k
        System.out.println("autocomplete(\"ca\", 2) -> " + trie.autocomplete("ca", 2));
        System.out.println("autocomplete(\"car\", 2) -> " + trie.autocomplete("car", 2));
        System.out.println("autocomplete(\"c\", 3) -> " + trie.autocomplete("c", 3));
        System.out.println("autocomplete(\"c\", 5) -> " + trie.autocomplete("c", 5));

        // Mejora con comodín
        System.out.println("\nsearchWildcard(\"c.sa\") -> " + trie.searchWildcard("c.sa"));
        System.out.println("searchWildcard(\"ca..o\") -> " + trie.searchWildcard("ca..o"));
    }

    private static void printList(List<String> list) {
        for (String s : list) {
            System.out.println(s);
        }
    }
}