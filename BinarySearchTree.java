import java.util.ArrayList;
import java.util.List;

public class BinarySearchTree<K extends Comparable<K>, V> {
    private BSTNode<K, V> root;
    private int size;

    public BinarySearchTree() {
        this.root = null;
        this.size = 0;
    }

    public void put(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Key and value must not be null.");
        }
        root = put(root, key, value);
    }

    private BSTNode<K, V> put(BSTNode<K, V> node, K key, V value) {
        if (node == null) {
            size++;
            return new BSTNode<>(key, value);
        }

        int comparison = key.compareTo(node.getKey());

        if (comparison < 0) {
            node.setLeft(put(node.getLeft(), key, value));
        } else if (comparison > 0) {
            node.setRight(put(node.getRight(), key, value));
        } else {
            node.setValue(value);
        }

        return node;
    }

    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key must not be null.");
        }
        return get(root, key);
    }

    private V get(BSTNode<K, V> node, K key) {
        if (node == null) {
            return null;
        }

        int comparison = key.compareTo(node.getKey());

        if (comparison == 0) {
            return node.getValue();
        } else if (comparison < 0) {
            return get(node.getLeft(), key);
        } else {
            return get(node.getRight(), key);
        }
    }

    public List<V> rangeSearch(K min, K max) {
        List<V> result = new ArrayList<>();

        if (min.compareTo(max) > 0) {
            return result;
        }

        rangeSearch(root, min, max, result);
        return result;
    }

    private void rangeSearch(BSTNode<K, V> node, K min, K max, List<V> result) {
        if (node == null) {
            return;
        }

        if (node.getKey().compareTo(min) > 0) {
            rangeSearch(node.getLeft(), min, max, result);
        }

        if (node.getKey().compareTo(min) >= 0 && node.getKey().compareTo(max) <= 0) {
            result.add(node.getValue());
        }

        if (node.getKey().compareTo(max) < 0) {
            rangeSearch(node.getRight(), min, max, result);
        }
    }

    public boolean delete(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key must not be null.");
        }

        boolean[] removed = { false };
        root = delete(root, key, removed);

        if (removed[0]) {
            size--;
        }

        return removed[0];
    }

    private BSTNode<K, V> delete(BSTNode<K, V> node, K key, boolean[] removed) {
        if (node == null) {
            return null;
        }

        int comparison = key.compareTo(node.getKey());

        if (comparison < 0) {
            node.setLeft(delete(node.getLeft(), key, removed));
        } else if (comparison > 0) {
            node.setRight(delete(node.getRight(), key, removed));
        } else {
            removed[0] = true;

            if (node.getLeft() == null) {
                return node.getRight();
            }

            if (node.getRight() == null) {
                return node.getLeft();
            }

            BSTNode<K, V> successor = minNode(node.getRight());
            node.setKey(successor.getKey());
            node.setValue(successor.getValue());
            node.setRight(deleteMin(node.getRight()));
        }

        return node;
    }

    private BSTNode<K, V> minNode(BSTNode<K, V> node) {
        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        return node;
    }

    private BSTNode<K, V> deleteMin(BSTNode<K, V> node) {
        if (node.getLeft() == null) {
            return node.getRight();
        }
        node.setLeft(deleteMin(node.getLeft()));
        return node;
    }

    public List<V> inOrder() {
        List<V> result = new ArrayList<>();
        inOrder(root, result);
        return result;
    }

    private void inOrder(BSTNode<K, V> node, List<V> result) {
        if (node == null) {
            return;
        }
        inOrder(node.getLeft(), result);
        result.add(node.getValue());
        inOrder(node.getRight(), result);
    }

    public List<V> preOrder() {
        List<V> result = new ArrayList<>();
        preOrder(root, result);
        return result;
    }

    private void preOrder(BSTNode<K, V> node, List<V> result) {
        if (node == null) {
            return;
        }
        result.add(node.getValue());
        preOrder(node.getLeft(), result);
        preOrder(node.getRight(), result);
    }

    public List<V> postOrder() {
        List<V> result = new ArrayList<>();
        postOrder(root, result);
        return result;
    }

    private void postOrder(BSTNode<K, V> node, List<V> result) {
        if (node == null) {
            return;
        }
        postOrder(node.getLeft(), result);
        postOrder(node.getRight(), result);
        result.add(node.getValue());
    }

    public int height() {
        return height(root);
    }

    private int height(BSTNode<K, V> node) {
        if (node == null) {
            return -1;
        }

        int leftHeight = height(node.getLeft());
        int rightHeight = height(node.getRight());
        return 1 + Math.max(leftHeight, rightHeight);
    }
}
