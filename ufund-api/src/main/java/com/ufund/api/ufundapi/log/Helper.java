package com.ufund.api.ufundapi.log;

import com.ufund.api.ufundapi.model.Need;

/**
 * Helper class to manage a collection of Needs using a Binary Search Tree (BST).
 * Provides methods to add and remove Needs efficiently while maintaining sorted order.
 * 
 * @author Sophia Le
 */
/**
 * Helper class to manage a collection of Needs using a Binary Search Tree (BST).
 * Provides methods to add and remove Needs efficiently while maintaining sorted order.
 */
public class Helper {
    /**
     * Represents a node in the Binary Search Tree (BST), storing a Need object.
     */
    private class Node {
        Need need;
        Node left, right;

        /**
         * Constructs a new Node containing the given Need object.
         *
         * @param need The Need object to be stored in the node.
         */
        Node(Need need) {
            this.need = need;
            this.left = this.right = null;
        }
    }

    private Node root;

    /**
     * Adds a Need to the BST while maintaining sorted order.
     *
     * @param need The Need object to be added.
     */
    public void addNeed(Need need) {
        root = addRecursive(root, need);
    }

    /**
     * Recursively adds a Need to the BST.
     *
     * @param node The current node in the recursive traversal.
     * @param need The Need object to be added.
     * @return The modified subtree with the new Need inserted.
     */
    private Node addRecursive(Node node, Need need) {
        if (node == null) {
            return new Node(need);
        }
        
        int cmp = need.getName().compareTo(node.need.getName());
        if (cmp < 0) {
            node.left = addRecursive(node.left, need);
        } else if (cmp > 0) {
            node.right = addRecursive(node.right, need);
        }
        // If names are equal, we assume no duplicate additions
        return node;
    }

    /**
     * Removes a Need from the BST by name.
     *
     * @param name The name of the Need to be removed.
     */
    public void removeNeed(String name) {
        root = removeRecursive(root, name);
    }

    /**
     * Recursively removes a Need from the BST.
     *
     * @param node The current node in the recursive traversal.
     * @param name The name of the Need to be removed.
     * @return The modified subtree after deletion.
     */
    private Node removeRecursive(Node node, String name) {
        if (node == null) {
            return null;
        }
        
        int cmp = name.compareTo(node.need.getName());
        if (cmp < 0) {
            node.left = removeRecursive(node.left, name);
        } else if (cmp > 0) {
            node.right = removeRecursive(node.right, name);
        } else {
            // Node to be deleted found
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            }
            
            // Node with two children: Get inorder successor (smallest in right subtree)
            node.need = findMin(node.right).need;
            node.right = removeRecursive(node.right, node.need.getName());
        }
        return node;
    }

    /**
     * Finds the node with the smallest Need (by name) in the BST.
     *
     * @param node The root of the subtree to search.
     * @return The node containing the smallest Need in the subtree.
     */
    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }
}
