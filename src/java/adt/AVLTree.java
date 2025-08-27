package adt;

public class AVLTree<T extends Comparable<T>> implements TreeInterface<T> {

	private Node root;

	public AVLTree() {
		this.root = null;
	}

	@Override
	public void insert(T data) {
		root = insert(data, root);
	}

	private Node insert(T data, Node node) {
		if (node == null) {
			return new Node(data);
		}

		if (data.compareTo(node.data) < 0) {
			node.left = insert(data, node.left);
		} else if (data.compareTo(node.data) > 0) {
			node.right = insert(data, node.right);
		} else {
			// Handle duplicates. You can return the node or throw an exception.
			return node;
		}

		updateHeight(node);
		return applyRotation(node);
	}

	@Override
	public void delete(T data) {
		root = delete(data, root);
	}

	private Node delete(T data, Node node) {
		if (node == null) {
			return null;
		}

		if (data.compareTo(node.data) < 0) {
			node.left = delete(data, node.left);
		} else if (data.compareTo(node.data) > 0) {
			node.right = delete(data, node.right);
		} else {
			if (node.left == null) {
				return node.right;
			} else if (node.right == null) {
				return node.left;
			}

			node.data = getMax(node.left);
			node.left = delete(getMax(node.left), node.left);
		}

		// A null node after deletion does not need its height updated
		if (node == null) {
			return node;
		}

		updateHeight(node);
		return applyRotation(node);
	}

	private void updateHeight(Node node) {
		node.height = 1 + Math.max(height(node.left), height(node.right));
	}

	private int height(Node node) {
		return node != null ? node.height : 0;
	}

	private Node applyRotation(Node node) {
		int balance = balance(node);

		// Left-Heavy
		if (balance > 1) {
			// Left-Right
			if (balance(node.left) < 0) {
				node.left = rotateLeft(node.left);
			}
			// Left-Left
			return rotateRight(node);
		}

		//Right-Heavy
		if (balance < -1) {
			// Right-Left
			if (balance(node.right) > 0) {
				node.right = rotateRight(node.right);
			}
			// Right-Right
			return rotateLeft(node);
		}
		return node;
	}

	private int balance(Node node) {
		return node != null ? height(node.left) - height(node.right) : 0;
	}

	private Node rotateRight(Node node) {
		Node leftNode = node.left;
		Node centerNode = leftNode.right;

		// Perform the rotation
		leftNode.right = node;
		node.left = centerNode;

		// Update heights in the correct order
		updateHeight(node);
		updateHeight(leftNode);
		return leftNode;
	}

	private Node rotateLeft(Node node) {
		Node rightNode = node.right;
		Node centerNode = rightNode.left;

		// Perform the rotation
		rightNode.left = node;
		node.right = centerNode;

		// Update heights in the correct order
		updateHeight(node);
		updateHeight(rightNode);
		return rightNode;
	}

	@Override
	public T search(T data) {
            return search(data, root);
        }

        private T search(T data, Node node) {
            if (node == null) return null;

            int cmp = data.compareTo(node.data);
            if (cmp < 0) {
                return search(data, node.left);
            } else if (cmp > 0) {
                return search(data, node.right);
            } else {
                return node.data; // match found
            }
        }

        @Override
        public void traverseInOrder(){
            traverseInOrder(root);
        }
        
        private void traverseInOrder(Node node){
            if (node != null){
                traverseInOrder(node.left);
                System.out.println(node.data);
                traverseInOrder(node.right);
            }
        }
        
        @Override
        public void traversePreOrder(){
            traversePreOrder(root);
        }
        
        private void traversePreOrder(Node node){
            if (node != null){
                System.out.println(node.data);
                traversePreOrder(node.left);       
                traversePreOrder(node.right);
            }
        }
        
        @Override
        public void traversePostOrder(){
            traversePostOrder(root);
        }
        
        private void traversePostOrder(Node node){
            if (node != null){
                traversePostOrder(node.left);
                traversePostOrder(node.right);
                System.out.println(node.data);
            }
        }
        
	@Override
	public T getMin() {
		if (isEmpty()) {
			return null;
		}
		return getMin(root);
	}

	private T getMin(Node node) {
		if (node.left == null) {
			return node.data;
		}
		return getMin(node.left);
	}

	@Override
	public T getMax() {
		if (isEmpty()) {
			return null;
		}
		return getMax(root);
	}

	private T getMax(Node node) {
		if (node.right == null) {
			return node.data;
		}
		return getMax(node.right);
	}

	@Override
	public boolean isEmpty() {
		return root == null;
	}

	public class Node {

		private T data;
		private Node left;
		private Node right;
		private int height;

		public Node(T data) {
			this.data = data;
			this.left = null;
			this.right = null;
			this.height = 1;
		}
	}
        
        
        @Override
        public void clear() {
            root = null;
        }
}
