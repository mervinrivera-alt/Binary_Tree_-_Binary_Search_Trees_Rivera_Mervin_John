# Binary_Tree_-_Binary_Search_Trees_Rivera_Mervin_John

# Ordered Inventory Index Using a Binary Search Tree

## Table of Contents

- [1. Project Description](#1-project-description)
- [2. Project Features](#2-project-features)
- [3. Project Structure](#3-project-structure)
- [4. How to Compile and Run](#4-how-to-compile-and-run)
- [5. Testing Results](#5-testing-results)
- [6. Complexity Audit: Balanced vs. Sorted Insertion](#6-complexity-audit-balanced-vs-sorted-insertion)
  - [Balanced Binary Search Tree](#balanced-binary-search-tree)
  - [Sorted Binary Search Tree](#sorted-binary-search-tree)
  - [Comparison Results](#comparison-results)
  - [Explanation](#explanation)
- [7. Limitations](#7-limitations)
- [8. Conclusion](#8-conclusion)

---

## 1. Project Description

This project implements an **Ordered Inventory Index** using a generic Binary Search Tree (BST) in Java. The tree organizes values according to comparable keys, such as product prices.

The purpose of this project is to demonstrate how a Binary Search Tree can organize data and perform operations such as insertion, searching, range searching, deletion, and tree traversal.

## 2. Project Features

The `BinarySearchTree<K, V>` class supports the following operations:

- **put(key, value)** - Inserts a new key-value pair or replaces the value if the key already exists.
- **get(key)** - Searches for a key and returns its associated value.
- **rangeSearch(min, max)** - Returns values within an inclusive key range in ascending order.
- **delete(key)** - Removes a node from the tree, handling leaf nodes, nodes with one child, and nodes with two children.
- **inOrder()** - Traverses the tree from left to right, returning values in ascending key order.
- **preOrder()** - Visits the current node before its left and right subtrees.
- **postOrder()** - Visits the left and right subtrees before the current node.
- **height()** - Calculates the height of the tree. An empty tree has a height of -1, while a leaf node has a height of 0.

The project uses two main classes:

- `BSTNode<K, V>` - Stores the key, value, and references to the left and right child nodes.
- `BinarySearchTree<K, V>` - Contains the tree structure and its operations.

## 3. Project Structure

The main Java files used in this project are:

- **BSTNode.java** - Defines the structure of each tree node.
- **BinarySearchTree.java** - Contains the implementation of the Binary Search Tree operations.
- **BinarySearchTreeTest.java** - Contains the test cases used to verify the BST implementation, including the complexity audit.
- **LabTestRunner.java** - Test runner provided for the laboratory, if included in the project.
- **AGENT_LOG.md** - Records the development process and interactions with the coding agent.
- **README.md** - Provides an overview, instructions, and test and audit results.

## 4. How to Compile and Run

If the three Java files are located in the same directory, open a terminal in that directory and execute:

```bash
javac BSTNode.java BinarySearchTree.java BinarySearchTreeTest.java
java BinarySearchTreeTest
```

These commands compile the Java files and execute the test program.

If the files are organized into separate folders or use package declarations, adjust the commands according to the project structure.

## 5. Testing Results

The latest execution of `BinarySearchTreeTest.java` produced the following result:

**133 tests passed, 0 failed.**

The tests verify the behavior of the Binary Search Tree, including insertion, searching, duplicate-key replacement, range searching, deletion, tree traversals, height calculation, and the balanced-versus-sorted complexity audit.

## 6. Complexity Audit: Balanced vs. Sorted Insertion

To demonstrate how insertion order affects the performance of a Binary Search Tree, two trees were created using the same seven keys but different insertion orders.

### Balanced Binary Search Tree

**Insertion order:** 40, 20, 60, 10, 30, 50, 70

- Number of nodes: 7
- Height: 2
- Lookup path for key 70: 40 → 60 → 70
- Search result: P70

### Sorted Binary Search Tree

**Insertion order:** 10, 20, 30, 40, 50, 60, 70

- Number of nodes: 7
- Height: 6
- Lookup path for key 70: 10 → 20 → 30 → 40 → 50 → 60 → 70
- Search result: P70

### Comparison Results

| Measurement | Balanced BST | Sorted BST |
|---|---:|---:|
| Number of nodes | 7 | 7 |
| Height | 2 | 6 |
| Nodes along lookup path for key 70 | 3 | 7 |
| Search result | P70 | P70 |

### Explanation

The balanced Binary Search Tree has a smaller height and a shorter lookup path. This means that fewer nodes need to be visited when searching for a value.

In comparison, inserting keys in ascending order creates a chain-shaped tree. As a result, searching for the last key requires following more nodes.

The time complexity of BST operations such as `put`, `get`, and `delete` depends on the height of the tree, represented by **O(h)**.

- **Balanced BST:** Typically has a time complexity of O(log n).
- **Worst-case BST:** Can have a time complexity of O(n) when the tree becomes chain-shaped due to sorted insertion.

This comparison demonstrates how the structure and height of a Binary Search Tree affect its performance.

*Note: The lookup paths shown above are based on the known tree structures. The current `get()` method returns the search result but does not count the number of visited nodes.*

## 7. Limitations

This project uses an ordinary Binary Search Tree that does not automatically balance itself. Its performance depends on the order in which keys are inserted.

When keys are inserted in sorted or nearly sorted order, the tree can become chain-shaped. This increases its height and can cause operations such as searching, insertion, and deletion to take O(n) time in the worst case.

## 8. Conclusion

This project demonstrates the implementation and functionality of a generic Binary Search Tree in Java. Through testing and the complexity audit, it shows how insertion, searching, range searching, deletion, and traversal work while maintaining the BST ordering property.

The comparison between balanced and sorted insertion also demonstrates the importance of tree height in determining the efficiency of Binary Search Tree operations.
