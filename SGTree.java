/**
 * ScapeGoat Tree class
 *
 * This class contains some of the basic code for implementing a ScapeGoat tree.
 * This version does not include any of the functionality for choosing which node
 * to scapegoat.  It includes only code for inserting a node, and the code for rebuilding
 * a subtree.
 */

public class SGTree {

    // Designates which child in a binary tree
    enum Child {LEFT, RIGHT}

    /**
     * TreeNode class.
     *
     * This class holds the data for a node in a binary tree.
     *
     * Note: we have made things public here to facilitate problem set grading/testing.
     * In general, making everything public like this is a bad idea!
     *
     */
    public static class TreeNode {
        int key;
        public TreeNode left = null;
        public TreeNode right = null;

        TreeNode(int k) {
            key = k;
        }
    }

    // Root of the binary tree
    public TreeNode root = null;

    /**
     * Counts the number of nodes in the specified subtree
     *
     * @param node  the parent node, not to be counted
     * @param child the specified subtree
     * @return number of nodes
     */
    public int countNodes(TreeNode node, Child child) {
        if (child == Child.LEFT) {
            if (node.left == null) {
                return 0;
            } else {
                // count the node itself plus all the nodes in its own left and right child trees
                return 1 + countNodes(node.left, child.LEFT) + countNodes(node.left, child.RIGHT);
            }
        } else if (child == Child.RIGHT) {
            if (node.right == null) {
                return 0;
            } else {
                // count the node itself plus all the nodes in its own left and right child trees
                return 1 + countNodes(node.right, child.LEFT) + countNodes(node.right, child.RIGHT);
            }
        }
        return 0;
    }

    /**
     * Builds an array of nodes in the specified subtree
     *
     * @param node  the parent node, not to be included in returned array
     * @param child the specified subtree
     * @return array of nodes
     */
    public TreeNode[] enumerateNodes(TreeNode node, Child child) {
        TreeNode[] arr = new TreeNode[countNodes(node, child)];
        if (child == Child.LEFT) {
            insertNodes(arr, node.left, 0);
        } else if (child == Child.RIGHT) {
            insertNodes(arr, node.right, 0);
        }
        return arr;
    }
    /**
     * Insert each node into the correct positions in the array
     *
     * @param arr  the array which the node and its children nodes are to be inserted into
     * @param node the particular node that has to be inserted
     * @param pos  position of the first node to be inserted into
     */
    public void insertNodes(TreeNode[] arr, TreeNode node, int pos) {
        if(node == null) {
            // no nodes to be inserted
            return;
        } else if (isLeaf(node)) {
            // just insert this leaf node
            arr[pos] = node;
        } else {
            // insert left child first, then node itself, then right child
            insertNodes(arr, node.left, pos);
            arr[pos + countNodes(node, Child.LEFT)] = node;
            insertNodes(arr, node.right, pos + countNodes(node, Child.LEFT) + 1);
        }
    }
    /**
     * Checks if a node is a leaf.
     *
     * @param node the node that is to be checked
     * @return true if it is a leaf, false otherwise
     */
    public boolean isLeaf(TreeNode node) {
        return node.left == null && node.right == null;
    }

    /**
     * Builds a tree from the list of nodes
     * Returns the node that is the new root of the subtree
     *
     * @param nodeList ordered array of nodes
     * @return the new root node
     */
    public TreeNode buildTree(TreeNode[] nodeList) {
        int len = nodeList.length;
        int mid = len / 2;
        if (len == 1) {
            // create tree with just one node, then return node
            nodeList[0].left = null;
            nodeList[0].right = null;
            return nodeList[0];
        } else if (len == 2) {
            // create tree with two nodes, then return a linear tree
            nodeList[0].left = null;
            nodeList[0].right = nodeList[1];
            nodeList[1].left = null;
            nodeList[1].right = null;
            return nodeList[0];
        } else {
            // attach left and right subtrees to main node
            nodeList[mid].left = construct(nodeList, 0, mid - 1);
            nodeList[mid].right = construct(nodeList, mid + 1, len - 1);
            return nodeList[mid];
        }
    }
    /**
     * Builds a tree from the list of nodes
     * Returns the node that is the new root of the subtree
     *
     * @param nodeList ordered array of nodes
     * @param start position of first element that is to be added to this new tree
     * @param end   position of last element that is to be added to this new tree
     * @return the new root node
     */
    public TreeNode construct(TreeNode[] nodeList, int start, int end) {
        if (start == end) {
            // create tree with just one node, then return node
            nodeList[start].left = null;
            nodeList[start].right = null;
            return nodeList[start];
        } else if (start + 1 == end) {
            // create tree with two nodes, then return a linear tree
            nodeList[start].left = null;
            nodeList[start].right = nodeList[end];
            nodeList[end].left = null;
            nodeList[end].right = null;
            return nodeList[start];
        } else {
            // recursively attach left and right subtrees to main node
            int mid = (start + end) / 2;
            nodeList[mid].left = construct(nodeList, start, mid - 1);
            nodeList[mid].right = construct(nodeList, mid + 1, end);
            return nodeList[mid];
        }
    }

    /**
    * Rebuilds the specified subtree of a node
    * 
    * @param node the part of the subtree to rebuild
    * @param child specifies which child is the root of the subtree to rebuild
    */
    public void rebuild(TreeNode node, Child child) {
        // Error checking: cannot rebuild null tree
        if (node == null) return;
        // First, retrieve a list of all the nodes of the subtree rooted at child
        TreeNode[] nodeList = enumerateNodes(node, child);
        // Then, build a new subtree from that list
        TreeNode newChild = buildTree(nodeList);
        // Finally, replace the specified child with the new subtree
        if (child == Child.LEFT) {
            node.left = newChild;
        } else if (child == Child.RIGHT) {
            node.right = newChild;
        }
    }

    /**
    * Inserts a key into the tree
    *
    * @param key the key to insert
    */
    public void insert(int key) {
        if (root == null) {
            root = new TreeNode(key);
            return;
        }

        TreeNode node = root;

        while (true) {
            if (key <= node.key) {
                if (node.left == null) break;
                node = node.left;
            } else {
                if (node.right == null) break;
                node = node.right;
            }
        }

        if (key <= node.key) {
            node.left = new TreeNode(key);
        } else {
            node.right = new TreeNode(key);
        }
    }


    // Simple main function for debugging purposes
    public static void main(String[] args) {
        SGTree tree = new SGTree();
        for (int i = 0; i < 100; i++) {
            tree.insert(i);
        }

        tree.rebuild(tree.root, Child.RIGHT);
    }
}
