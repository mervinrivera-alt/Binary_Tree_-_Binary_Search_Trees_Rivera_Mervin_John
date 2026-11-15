import java.util.List;
import java.util.Objects;

/*
 * Simple, dependency-free test program for BSTNode.java and BinarySearchTree.java.
 *
 * Compile:
 *   javac BSTNode.java BinarySearchTree.java BinarySearchTreeTest.java
 *
 * Run:
 *   java BinarySearchTreeTest
 */
public class BinarySearchTreeTest {
    private static final List<String> LAB_INORDER = List.of(
            "P10", "P20", "P30", "P40", "P55", "P60", "P70", "P85");
    private static final List<String> LAB_PREORDER = List.of(
            "P40", "P20", "P10", "P30", "P70", "P55", "P60", "P85");
    private static final List<String> LAB_POSTORDER = List.of(
            "P10", "P30", "P20", "P60", "P55", "P85", "P70", "P40");

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        testEmptyTree();
        testInsertion();
        testRetrieval();
        testDuplicateKeyReplacement();
        testRangeSearch();
        testDeletionLeaf();
        testDeletionOneChild();
        testDeletionTwoChildren();
        testDeletionRoot();
        testTraversals();
        testHeight();
        testInvalidInputs();

        printSummary();
    }

    // Builds the tree used throughout the lab: 40, 20, 70, 10, 30, 55, 85, 60.
    private static BinarySearchTree<Integer, String> buildLabTree() {
        BinarySearchTree<Integer, String> tree = new BinarySearchTree<>();
        tree.put(40, "P40");
        tree.put(20, "P20");
        tree.put(70, "P70");
        tree.put(10, "P10");
        tree.put(30, "P30");
        tree.put(55, "P55");
        tree.put(85, "P85");
        tree.put(60, "P60");
        return tree;
    }

    // Empty trees must not crash, and every method must return its empty result.
    private static void testEmptyTree() {
        section("Empty tree");
        BinarySearchTree<Integer, String> tree = new BinarySearchTree<>();

        checkInt("empty tree node count", 0, tree.inOrder().size());
        checkValue("empty tree get", null, tree.get(40));
        checkTrue("empty tree delete returns false", !tree.delete(40));
        checkList("empty tree range search", List.of(), tree.rangeSearch(10, 20));
        checkList("empty tree in-order", List.of(), tree.inOrder());
        checkList("empty tree pre-order", List.of(), tree.preOrder());
        checkList("empty tree post-order", List.of(), tree.postOrder());
        checkInt("empty tree height", -1, tree.height());
    }

    // Insertion should create the root, keep BST order, and track the node count.
    private static void testInsertion() {
        section("Insertion");

        BinarySearchTree<Integer, String> single = new BinarySearchTree<>();
        single.put(40, "P40");
        checkInt("insert first node count", 1, single.inOrder().size());
        checkValue("insert first node value", "P40", single.get(40));
        checkList("insert first node in-order", List.of("P40"), single.inOrder());
        checkInt("insert first node height", 0, single.height());

        BinarySearchTree<Integer, String> lab = buildLabTree();
        checkInt("lab tree node count", 8, lab.inOrder().size());
        checkValue("lab tree root value", "P40", lab.get(40));
        checkValue("lab tree left leaf value", "P10", lab.get(10));
        checkValue("lab tree right leaf value", "P85", lab.get(85));
        checkList("lab tree in-order", LAB_INORDER, lab.inOrder());
        checkInt("lab tree height", 3, lab.height());

        // Sorted insertion should create a right-leaning chain.
        BinarySearchTree<Integer, String> rightChain = new BinarySearchTree<>();
        for (int key = 10; key <= 50; key += 10) {
            rightChain.put(key, "P" + key);
        }
        checkInt("sorted insertion node count", 5, rightChain.inOrder().size());
        checkInt("sorted insertion height", 4, rightChain.height());
        checkList("sorted insertion in-order", List.of(
                "P10", "P20", "P30", "P40", "P50"), rightChain.inOrder());

        // Reverse-sorted insertion should create a left-leaning chain.
        BinarySearchTree<Integer, String> leftChain = new BinarySearchTree<>();
        for (int key = 50; key >= 10; key -= 10) {
            leftChain.put(key, "P" + key);
        }
        checkInt("reverse insertion node count", 5, leftChain.inOrder().size());
        checkInt("reverse insertion height", 4, leftChain.height());
        checkList("reverse insertion in-order", List.of(
                "P10", "P20", "P30", "P40", "P50"), leftChain.inOrder());
    }

    // Retrieval should follow comparisons from the root and return null when absent.
    private static void testRetrieval() {
        section("Retrieval");
        BinarySearchTree<Integer, String> tree = buildLabTree();

        checkValue("get root", "P40", tree.get(40));
        checkValue("get left leaf", "P10", tree.get(10));
        checkValue("get right leaf", "P85", tree.get(85));
        checkValue("get deep node", "P60", tree.get(60));
        checkValue("get left branch", "P30", tree.get(30));
        checkValue("get successor node", "P55", tree.get(55));
        checkValue("get missing high key", null, tree.get(99));
        checkValue("get missing low key", null, tree.get(0));
        checkValue("get missing interior key", null, tree.get(41));
        checkValue("get on empty tree", null,
                new BinarySearchTree<Integer, String>().get(40));
    }

    // A duplicate key must replace the value, not add another node.
    private static void testDuplicateKeyReplacement() {
        section("Duplicate-key replacement");

        BinarySearchTree<Integer, String> tree = buildLabTree();
        tree.put(40, "P40-new");

        checkInt("duplicate replacement node count", 8, tree.inOrder().size());
        checkValue("duplicate replacement new value", "P40-new", tree.get(40));
        checkValue("duplicate replacement keeps other value", "P20", tree.get(20));
        checkInt("duplicate replacement height unchanged", 3, tree.height());
        checkList("duplicate replacement in-order", List.of(
                "P10", "P20", "P30", "P40-new", "P55", "P60", "P70", "P85"),
                tree.inOrder());

        BinarySearchTree<Integer, String> single = new BinarySearchTree<>();
        single.put(10, "A");
        single.put(10, "B");
        single.put(10, "C");
        checkInt("repeated replacement node count", 1, single.inOrder().size());
        checkValue("repeated replacement final value", "C", single.get(10));
        checkList("repeated replacement in-order", List.of("C"), single.inOrder());
        checkInt("repeated replacement height", 0, single.height());
    }

    // Range search must include both endpoints, stay ascending, and prune safely.
    private static void testRangeSearch() {
        section("Range search");
        BinarySearchTree<Integer, String> tree = buildLabTree();

        checkList("range includes both endpoints", List.of("P30", "P40", "P55"),
                tree.rangeSearch(30, 55));
        checkList("range lab trace [25,60]", List.of(
                "P30", "P40", "P55", "P60"), tree.rangeSearch(25, 60));
        checkList("range with no matching keys", List.of(),
                tree.rangeSearch(41, 54));
        checkList("range with one exact key", List.of("P40"),
                tree.rangeSearch(40, 40));
        checkList("range covering all keys", LAB_INORDER,
                tree.rangeSearch(10, 85));
        checkList("inverted range", List.of(), tree.rangeSearch(60, 25));
        checkList("range on empty tree", List.of(),
                new BinarySearchTree<Integer, String>().rangeSearch(10, 20));
        checkList("equal bounds with no key", List.of(),
                tree.rangeSearch(41, 41));
        checkList("range interior span", List.of(
                "P20", "P30", "P40", "P55", "P60", "P70"),
                tree.rangeSearch(20, 70));
    }

    // Deleting a leaf should remove it and leave the rest of the tree intact.
    private static void testDeletionLeaf() {
        section("Deletion: leaf");
        BinarySearchTree<Integer, String> tree = buildLabTree();

        checkTrue("delete leaf returns true", tree.delete(10));
        checkInt("delete leaf node count", 7, tree.inOrder().size());
        checkValue("delete leaf get returns null", null, tree.get(10));
        checkList("delete leaf in-order", List.of(
                "P20", "P30", "P40", "P55", "P60", "P70", "P85"),
                tree.inOrder());
        checkInt("delete leaf height", 3, tree.height());
    }

    // A one-child node should be replaced by its only child.
    private static void testDeletionOneChild() {
        section("Deletion: one child");

        // Node 55 has only a right child, 60.
        BinarySearchTree<Integer, String> rightOnly = buildLabTree();
        checkTrue("delete right-only node returns true", rightOnly.delete(55));
        checkInt("delete right-only node count", 7, rightOnly.inOrder().size());
        checkValue("delete right-only get deleted key", null, rightOnly.get(55));
        checkValue("delete right-only promoted child", "P60", rightOnly.get(60));
        checkList("delete right-only in-order", List.of(
                "P10", "P20", "P30", "P40", "P60", "P70", "P85"),
                rightOnly.inOrder());
        checkInt("delete right-only height", 2, rightOnly.height());

        // Node 30 has only a left child, 20.
        BinarySearchTree<Integer, String> leftOnly = new BinarySearchTree<>();
        leftOnly.put(50, "P50");
        leftOnly.put(30, "P30");
        leftOnly.put(20, "P20");
        checkTrue("delete left-only node returns true", leftOnly.delete(30));
        checkInt("delete left-only node count", 2, leftOnly.inOrder().size());
        checkValue("delete left-only get deleted key", null, leftOnly.get(30));
        checkValue("delete left-only promoted child", "P20", leftOnly.get(20));
        checkList("delete left-only in-order", List.of("P20", "P50"),
                leftOnly.inOrder());
        checkInt("delete left-only height", 1, leftOnly.height());
    }

    // A two-child node uses the smallest key in its right subtree as successor.
    private static void testDeletionTwoChildren() {
        section("Deletion: two children");

        // Deleting 20 uses successor 30, which has no right child.
        BinarySearchTree<Integer, String> tree = buildLabTree();
        checkTrue("delete two-child node returns true", tree.delete(20));
        checkInt("delete two-child node count", 7, tree.inOrder().size());
        checkValue("delete two-child get deleted key", null, tree.get(20));
        checkValue("delete two-child successor remains", "P30", tree.get(30));
        checkList("delete two-child in-order", List.of(
                "P10", "P30", "P40", "P55", "P60", "P70", "P85"),
                tree.inOrder());
        checkInt("delete two-child height", 3, tree.height());
    }

    // Root deletion must correctly replace the root and reconnect the successor's child.
    private static void testDeletionRoot() {
        section("Deletion: root");

        // Key case: deleting root 40 uses successor 55, and 55 has right child 60.
        BinarySearchTree<Integer, String> tree = buildLabTree();
        checkTrue("delete root 40 returns true", tree.delete(40));
        checkInt("delete root 40 node count", 7, tree.inOrder().size());
        checkValue("delete root 40 get deleted key", null, tree.get(40));
        checkValue("delete root 40 successor value", "P55", tree.get(55));
        checkValue("delete root 40 successor right child", "P60", tree.get(60));
        checkValue("delete root 40 right subtree", "P85", tree.get(85));
        checkList("delete root 40 in-order", List.of(
                "P10", "P20", "P30", "P55", "P60", "P70", "P85"),
                tree.inOrder());
        checkInt("delete root 40 height", 2, tree.height());

        // Root with two children where the successor has no right child.
        BinarySearchTree<Integer, String> rootNoRight = new BinarySearchTree<>();
        rootNoRight.put(50, "P50");
        rootNoRight.put(30, "P30");
        rootNoRight.put(70, "P70");
        rootNoRight.put(60, "P60");
        checkTrue("delete root with successor leaf returns true",
                rootNoRight.delete(50));
        checkInt("delete root with successor leaf node count", 3,
                rootNoRight.inOrder().size());
        checkValue("delete root with successor leaf get deleted key", null,
                rootNoRight.get(50));
        checkValue("delete root with successor leaf promoted value", "P60",
                rootNoRight.get(60));
        checkList("delete root with successor leaf in-order",
                List.of("P30", "P60", "P70"), rootNoRight.inOrder());
        checkInt("delete root with successor leaf height", 1,
                rootNoRight.height());

        // Deleting the only node should empty the tree.
        BinarySearchTree<Integer, String> single = new BinarySearchTree<>();
        single.put(40, "P40");
        checkTrue("delete only root returns true", single.delete(40));
        checkInt("delete only root node count", 0, single.inOrder().size());
        checkValue("delete only root get returns null", null, single.get(40));
        checkList("delete only root in-order", List.of(), single.inOrder());
        checkInt("delete only root height", -1, single.height());

        // Deleting a root with one child should promote that child.
        BinarySearchTree<Integer, String> rootOneChild = new BinarySearchTree<>();
        rootOneChild.put(50, "P50");
        rootOneChild.put(30, "P30");
        checkTrue("delete root with one child returns true",
                rootOneChild.delete(50));
        checkInt("delete root with one child node count", 1,
                rootOneChild.inOrder().size());
        checkValue("delete root with one child promoted value", "P30",
                rootOneChild.get(30));
        checkList("delete root with one child in-order", List.of("P30"),
                rootOneChild.inOrder());
        checkInt("delete root with one child height", 0,
                rootOneChild.height());

        // A missing key must return false and leave the tree unchanged.
        BinarySearchTree<Integer, String> missing = buildLabTree();
        checkTrue("delete missing key returns false", !missing.delete(99));
        checkInt("delete missing key node count", 8, missing.inOrder().size());
        checkList("delete missing key in-order", LAB_INORDER, missing.inOrder());
        checkInt("delete missing key height", 3, missing.height());
    }

    // Traversals must return values in the required order.
    private static void testTraversals() {
        section("Traversals");
        BinarySearchTree<Integer, String> tree = buildLabTree();

        checkList("in-order lab tree", LAB_INORDER, tree.inOrder());
        checkList("pre-order lab tree", LAB_PREORDER, tree.preOrder());
        checkList("post-order lab tree", LAB_POSTORDER, tree.postOrder());

        BinarySearchTree<Integer, String> single = new BinarySearchTree<>();
        single.put(40, "P40");
        checkList("single-node in-order", List.of("P40"), single.inOrder());
        checkList("single-node pre-order", List.of("P40"), single.preOrder());
        checkList("single-node post-order", List.of("P40"), single.postOrder());

        BinarySearchTree<Integer, String> rightChain = new BinarySearchTree<>();
        rightChain.put(10, "P10");
        rightChain.put(20, "P20");
        rightChain.put(30, "P30");
        checkList("right-chain in-order", List.of("P10", "P20", "P30"),
                rightChain.inOrder());
        checkList("right-chain pre-order", List.of("P10", "P20", "P30"),
                rightChain.preOrder());
        checkList("right-chain post-order", List.of("P30", "P20", "P10"),
                rightChain.postOrder());

        BinarySearchTree<Integer, String> leftChain = new BinarySearchTree<>();
        leftChain.put(30, "P30");
        leftChain.put(20, "P20");
        leftChain.put(10, "P10");
        checkList("left-chain in-order", List.of("P10", "P20", "P30"),
                leftChain.inOrder());
        checkList("left-chain pre-order", List.of("P30", "P20", "P10"),
                leftChain.preOrder());
        checkList("left-chain post-order", List.of("P10", "P20", "P30"),
                leftChain.postOrder());
    }

    // Height uses the lab convention: empty = -1 and leaf = 0.
    private static void testHeight() {
        section("Height");

        checkInt("empty tree height", -1,
                new BinarySearchTree<Integer, String>().height());

        BinarySearchTree<Integer, String> single = new BinarySearchTree<>();
        single.put(40, "P40");
        checkInt("single-node height", 0, single.height());

        BinarySearchTree<Integer, String> two = new BinarySearchTree<>();
        two.put(40, "P40");
        two.put(20, "P20");
        checkInt("two-node height", 1, two.height());

        BinarySearchTree<Integer, String> lab = buildLabTree();
        checkInt("lab tree height", 3, lab.height());

        BinarySearchTree<Integer, String> rightChain = new BinarySearchTree<>();
        for (int key = 10; key <= 50; key += 10) {
            rightChain.put(key, "P" + key);
        }
        checkInt("sorted-insertion height", 4, rightChain.height());

        BinarySearchTree<Integer, String> leftChain = new BinarySearchTree<>();
        for (int key = 50; key >= 10; key -= 10) {
            leftChain.put(key, "P" + key);
        }
        checkInt("reverse-insertion height", 4, leftChain.height());

        BinarySearchTree<Integer, String> afterDelete = buildLabTree();
        afterDelete.delete(40);
        checkInt("height after root deletion", 2, afterDelete.height());

        BinarySearchTree<Integer, String> afterReplacement = buildLabTree();
        afterReplacement.put(40, "P40-new");
        checkInt("height after duplicate replacement", 3,
                afterReplacement.height());
    }

    // Invalid null keys and values must be rejected without changing the tree.
    private static void testInvalidInputs() {
        section("Invalid input");
        BinarySearchTree<Integer, String> tree = buildLabTree();

        checkThrowsIllegalArgument("put null key is rejected",
                () -> tree.put(null, "X"));
        checkThrowsIllegalArgument("put null value is rejected",
                () -> tree.put(1, null));
        checkThrowsIllegalArgument("get null key is rejected",
                () -> tree.get(null));
        checkThrowsIllegalArgument("delete null key is rejected",
                () -> tree.delete(null));

        checkInt("tree unchanged after rejected inputs", 8,
                tree.inOrder().size());
        checkList("tree order unchanged after rejected inputs", LAB_INORDER,
                tree.inOrder());

        // Null range bounds are intentionally not tested because the lab
        // does not specify that behavior.
    }

    private static void section(String name) {
        System.out.println();
        System.out.println("--- " + name + " ---");
    }

    private static void checkInt(String name, int expected, int actual) {
        if (expected == actual) {
            pass(name);
        } else {
            fail(name, expected, actual);
        }
    }

    private static void checkValue(String name, Object expected, Object actual) {
        if (Objects.equals(expected, actual)) {
            pass(name);
        } else {
            fail(name, expected, actual);
        }
    }

    private static void checkList(String name, List<String> expected,
            List<String> actual) {
        if (Objects.equals(expected, actual)) {
            pass(name);
        } else {
            fail(name, expected, actual);
        }
    }

    private static void checkTrue(String name, boolean condition) {
        if (condition) {
            pass(name);
        } else {
            fail(name, true, false);
        }
    }

    private static void checkThrowsIllegalArgument(String name, Runnable action) {
        try {
            action.run();
            fail(name + " (no exception was thrown)", "IllegalArgumentException",
                    "no exception");
        } catch (IllegalArgumentException expected) {
            pass(name);
        } catch (RuntimeException unexpected) {
            fail(name + " (wrong exception type)", "IllegalArgumentException",
                    unexpected.getClass().getSimpleName());
        }
    }

    private static void pass(String name) {
        passed++;
        System.out.println("PASS: " + name);
    }

    private static void fail(String name, Object expected, Object actual) {
        failed++;
        System.out.println("FAIL: " + name);
        System.out.println("      expected: " + expected);
        System.out.println("      actual:   " + actual);
    }

    private static void printSummary() {
        System.out.println();
        System.out.println("========================================");
        System.out.println("Summary: " + passed + " passed, " + failed + " failed.");
        System.out.println("========================================");

        if (failed > 0) {
            System.exit(1);
        }
    }
}
