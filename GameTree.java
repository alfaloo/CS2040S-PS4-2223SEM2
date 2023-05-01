/**
 * Game Tree class
 *
 * This class contains the basic code for implementing a Game Tree. It includes functionality to load a game tree.
 */

public class GameTree {

    // Standard enumerations for tic-tac-toe variations
    public enum Player {ONE, TWO}

    public enum Game {Regular, Misere, NoTie, Arbitrary}

    // Some constants for tic-tac-toe
    final static int bsize = 3;
    final static int btotal = bsize * bsize;
    final static char EMPTYCHAR = '_';

    // Specifies the values for the arbitrary variant
    final int[] valArray = {1, -2, 1, -3, -2, 2, -2, -1, 1};

    /**
     * Simple TreeNode class.
     *
     * This class holds the data for a node in a game tree.
     *
     * Note: we have made things public here to facilitate problem set grading/testing.
     * In general, making everything public like this is a bad idea!
     *
     */
    static class TreeNode {
        public String name = "";
        public TreeNode[] children = null;
        public int numChildren = 0;
        public int value = Integer.MIN_VALUE;
        public boolean leaf = false;

        // Empty constructor for building an empty node
        TreeNode() {}

        // Simple constructor for build a node with a name and a leaf specification
        TreeNode(String s, boolean l) {
            name = s;
            leaf = l;
            children = new TreeNode[btotal];
            for (int i = 0; i < btotal; i++) {
                children[i] = null;
            }
            numChildren = 0;
        }
    }

    // Root of the tree - public to facilitate grading
    public TreeNode root = null;

    //--------------
    // Utility Functions
    //--------------

    // Simple function for determining the other player
    private Player other(Player p) {
        if (p == Player.ONE) return Player.TWO;
        else return Player.ONE;
    }

    // Draws a board using the game state stored as the name of the node.
    public void drawBoard(String s) {
        System.out.println("-------");
        for (int j = 0; j < bsize; j++) {
            System.out.print("|");
            for (int i = 0; i < bsize; i++) {
                char c = s.charAt(i + 3 * j);
                if (c != EMPTYCHAR)
                    System.out.print(c);
                else System.out.print(" ");
                System.out.print("|");
            }
            System.out.println();
            System.out.println("-------");
        }
    }

    /**
     * readTree reads a game tree from the specified file. If the file does not exist, cannot be found, or there is
     * otherwise an error opening or reading the file, it prints out "Error reading file" along with additional error
     * information.
     *
     * @param fName name of file to read from
     */
    public void readTree(String fName) {
        try {
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(fName));
            root = readTree(reader);
        } catch (java.io.IOException e) {
            System.out.println("Error reading file: " + e);
        }
    }

    // Helper function: reads a game tree from the specified reader
    private TreeNode readTree(java.io.BufferedReader reader) throws java.io.IOException {
        String s = reader.readLine();
        if (s == null) {
            throw new java.io.IOException("File ended too soon.");
        }
        TreeNode node = new TreeNode();
        node.numChildren = Integer.parseInt(s.substring(0, 1));
        node.children = new TreeNode[node.numChildren];
        node.leaf = (s.charAt(1) == '1');
        node.value = Integer.MIN_VALUE;
        if (node.leaf) {
            char v = s.charAt(2);
            node.value = Character.getNumericValue(v);
            node.value--;
        }
        node.name = s.substring(3);

        for (int i = 0; i < node.numChildren; i++) {
            node.children[i] = readTree(reader);
        }
        return node;
    }

    /**
     * findValue determines the value for every node in the game tree and sets the value field of each node. If the root
     * is null (i.e., no tree exists), then it returns Integer.MIN_VALUE.
     *
     * @return value of the root node of the game tree
     */
    int findValue() {
        if (root == null) {
            return Integer.MIN_VALUE;
        }
        setTreeValue(root, Player.ONE);
        return root.value;
    }
    /**
     * Method to assign correct value to each node.
     *
     * @param node the node and whose children requires values to be set
     * @param player the player whose turn it is
     */
    void setTreeValue(TreeNode node, Player player) {
        if (node.leaf) {
            // leaf node already has value initialised
            return;
        } else if (player == Player.ONE) {
            // set value for all its children first
            for (TreeNode i : node.children) {
                setTreeValue(i, other(player));
            }
            // if its player one's turn, then value is the max of all its children
            node.value = maxValue(node.children);
        } else if (player == Player.TWO) {
            // set value for all its children first
            for (TreeNode i : node.children) {
                setTreeValue(i, other(player));
            }
            // if its player one's turn, then value is the min of all its children
            node.value = minValue(node.children);
        }
    }
    /**
     * Retrieves max value of its children
     *
     * @param arr array of nodes whose max value will be taken
     * @return maximum value
     */
    int maxValue(TreeNode[] arr) {
        int max = Integer.MIN_VALUE;
        for (TreeNode i : arr) {
            if (i.value > max) {
                max = i.value;
            }
        }
        return max;
    }
    /**
     * Retrieves min value of its children
     *
     * @param arr array of nodes whose min value will be taken
     * @return minimum value
     */
    int minValue(TreeNode[] arr) {
        int min = Integer.MAX_VALUE;
        for(TreeNode i : arr) {
            if (i.value < min) {
                min = i.value;
            }
        }
        return min;
    }


    // Simple main for testing purposes
    public static void main(String[] args) {
        GameTree tree1 = new GameTree();
        tree1.readTree("games/tictac_1_empty.txt");
        System.out.println("Game 1: " + tree1.findValue());
        GameTree tree31 = new GameTree();
        tree31.readTree("games/tictac_3_empty_1.txt");
        System.out.println("Game 3.1: " + tree31.findValue());
        GameTree tree32 = new GameTree();
        tree32.readTree("games/tictac_3_empty_2.txt");
        System.out.println("Game 3.2: " + tree32.findValue());
        GameTree tree33 = new GameTree();
        tree33.readTree("games/tictac_3_empty_3.txt");
        System.out.println("Game 3.3: " + tree33.findValue());
        GameTree tree5 = new GameTree();
        tree5.readTree("games/tictac_5_empty.txt");
        System.out.println("Game 5: " + tree5.findValue());
        GameTree tree9 = new GameTree();
        tree9.readTree("games/tictac_9_empty.txt");
        System.out.println("Game 9: " + tree9.findValue());
    }

}
