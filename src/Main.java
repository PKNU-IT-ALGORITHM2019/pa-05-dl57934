import java.io.*;
import java.util.LinkedList;
import java.util.Queue;

public class Main {
    static int WORD = 0;
    static int SAME = 0;
    static int CLAS = 1;
    static int DESCRIPTION = 2;
    static String FILE_PATH = "/Users/leesanghoon/Desktop/학교생활/알고리즘/6주차/pa-05-dl57934/shuffled_dict.txt";
    static String DELETE_ALL_FILE_PATH =  "/Users/leesanghoon/Desktop/학교생활/알고리즘/6주차/pa-05-dl57934/to_be_deleted_words.txt";
    static Queue<BST.Node> qu = new LinkedList<>();

    public static void main(String[] args) throws IOException {
        BST bst = new BST();
        BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in));
        setTree(bst);


        while(true) {
            String command = bfr.readLine();
            System.out.println(command);
            if (command != null) {
                if (compareWord(command, "size") == SAME) {
                    System.out.println(bst.getSize());
                } else if (compareWord(command, "find") == SAME) {
                    String findWord = bfr.readLine();
                    bst.search(bst.root, findWord);
                } else if (compareWord(command, "add") == SAME) {
                    System.out.print("word: ");
                    String words = bfr.readLine();
                    System.out.print("class: ");
                    String clas = bfr.readLine();
                    System.out.print("meaning: ");
                    String meaning = bfr.readLine();
                    bst.appendNode(words, clas, meaning);
                } else if (compareWord(command, "deleteAll") == SAME) {
                   BufferedReader bfr2 = getDeleteWords();
                    String line;
                    int size = 0;
                    while((line = bfr2.readLine()) != null){
                        size+=1;
                        bst.deleteKey(line);
                    }
                    System.out.println("Complete Delete " +size);
                } else if (compareWord(command, "delete") == SAME) {
                    String deleteWord = bfr.readLine();
                    bst.deleteKey(deleteWord);
                }
            }
        }

    }

    public static BufferedReader getDeleteWords() throws FileNotFoundException{
        return new BufferedReader(new FileReader(DELETE_ALL_FILE_PATH));
    }

    public static BST setTree(BST bst) throws FileNotFoundException, IOException {
        String line;
        BufferedReader bfr = new BufferedReader(new FileReader(FILE_PATH));

        while((line = bfr.readLine()) != null){
            String[] array = new String[10];
            copyArray(array, splitLine(line));
            bst.appendNode(array[WORD], array[CLAS], array[DESCRIPTION]);
        }

        return bst;

    }

    public static void levelOrder(BST.Node bst){
        qu.offer(bst);
        while(!qu.isEmpty()){
            BST.Node node= qu.poll();
            System.out.println(node.word);

            if(node.left !=null)
                qu.offer(node.left);
            if(node.right != null)
                qu.offer(node.right);
        }
    }

    static String[] splitLine(String line){
        String[] word = line.split("\\(", 0);
        String[] classDescription = word[1].split("\\)", 0);
       try{
           return new String[]{
                   word[0].trim(),
                   classDescription[0],
                   classDescription[1]
           };
       }catch (Exception e) {
           return new String[]{
                   word[0].trim(),
                   classDescription[0],
                   ""
           };
       }
    }

    static void copyArray(String[] copiedArray, String[] array){
        System.arraycopy(array, 0, copiedArray, 0, array.length);
    }

    public static int compareWord(String w1, String w2){
        return w1.compareTo(w2);
    }

}


class BST{
    private int SAME = 0;
    private int size = 0;
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
    Node root;
    public BST(){
        root = null;
    }

    public void appendNode(String word, String clas, String description){
        root = addNode(root, word, clas, description);
    }

    public Node addNode(Node root, String word, String clas, String description){
        if(root == null) {
            size+=1;
            root = new Node(word, clas, description);
            return root;
        }
        if(compareWord(root.word, word) > 0)
            root.right = addNode(root.right, word, clas, description);
        else
            root.left = addNode(root.left, word, clas, description);

        return root;
    }

    public int getSize(){
        return size;
    }


    public Node search(Node root, String word){
        if(root ==null) {
            return null;
        }

        if(compareWord(root.word, word) == SAME) {
            System.out.println(word);
            System.out.println(root.description);
            return search(root.left, word);
        }

        if(compareWord(root.word, word)  > 0)
            return search(root.right, word);


        return search(root.left, word);
    }

    public void deleteKey(String word){
        root = deleteNode(root, word) ;

    }

    public Node deleteNode(Node root, String word){
        if(root == null) {
            System.out.println("Not Found");
            return null;
        }
        int compareResult =  compareWord(root.word, word);
        if(compareResult > 0)
            root.right = deleteNode(root.right, word);
        else if(compareResult < 0)
            root.left = deleteNode(root.left, word);
        else{
            size-=1;
            System.out.println("Deleted Successfully");
            if(root.left == null)
                return root.right;

            else if(root.right == null) {
                root.left = deleteNode(root.left, word);
                return root.left;
            }
            Node successor = getSuccesor(root.right);

            root.word = successor.word;

            root.right = deleteNode(root.right, successor.word);

            root.left = deleteNode(root.left, word);
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