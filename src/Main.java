import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    static int WORD = 0;
    static int CLAS = 1;
    static int DESCRIPTION = 2;
    static String FILE_PATH = "/Users/leesanghoon/Desktop/학교생활/알고리즘/6주차/pa-05-dl57934/shuffled_dict.txt";

    public static void main(String[] args) throws FileNotFoundException, IOException {
        BST bst = new BST();
        String line;
        BufferedReader bfr = new BufferedReader(new FileReader(FILE_PATH));

        while((line = bfr.readLine()) != null){
            String[] array = new String[10];
            copyArray(array, splitLine(line));
            bst.appendNode(array[WORD], array[CLAS], array[DESCRIPTION]);

        }

//        bst.initRoot()

    }

    static String[] splitLine(String line){
        String[] word = line.split("\\(", 0);
        String[] classDescription = word[1].split("\\)", 0);
       try{
           return new String[]{
                   word[0],
                   classDescription[0],
                   classDescription[1]
           };
       }catch (Exception e) {
           return new String[]{
                   word[0],
                   classDescription[0],
                   ""
           };
       }
    }

    static void copyArray(String[] copiedArray, String[] array){
        System.arraycopy(array, 0, copiedArray, 0, array.length);
    }

}


class BST{
    int SAME = 0;

    class Node{
        Node left;
        Node right;
        String word;
        String clas;
        String description;

        Node(String word, String clas, String description){
            this.word =word;
            this.clas = clas;
            this.description = description;
            this.left = null;
            this.right = null;
        }
    }

    Node root = null;

    public void appendNode(String word, String clas, String description){
        addNode(root, word, clas, description);
    }

    public Node addNode(Node root, String word, String clas, String description){
        if(root == null) {
            root = new Node(word, clas, description);
            return root;
        }
        if(compareWord(root.word, word) > 0)
            root.right = addNode(root.right, word, clas, description);
        else
            root.left = addNode(root.left, word, clas, description);

        return root;
    }

    public Node search(Node root, String word){
        if(root ==null || compareWord(root.word, word) == SAME)
            return root;
        if(compareWord(root.word, word)  > 0)
            return search(root.right, word);

        return search(root.left, word);
    }

    public void deleteKey(String word){
        deleteNode(root, word);
    }

    public Node deleteNode(Node root, String word){
        if(root == null)
            return null;
        int compareResult =  compareWord(root.word, word);
        if(compareResult > 0)
            root.right = deleteNode(root.right, word);
        else if(compareResult < 0)
            root.left = deleteNode(root.left, word);
        else{
            if(root.left == null)
                return root.right;

            else if(root.right == null)
                return root.left;

            Node successor = getSuccesor(root.right);

            root.word = successor.word;

            root.right = deleteNode(root.right, successor.word);
        }
        return root;
    }

    public Node getSuccesor(Node root){
        Node current = root;
        while(current.left!=null)
            current = current.left;
        return current;
    }

    public int compareWord(String w1, String w2){
        return w1.compareTo(w2);
    }
}