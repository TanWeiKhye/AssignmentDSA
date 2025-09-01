package adt;

import java.util.Iterator;

public class AVLTree<T extends Comparable<T>> implements TreeInterface<T>, Iterable<T> {

	private Node root;
	private int size;

	public AVLTree() {
		this.root = null;
		this.size = 0;
	}

	@Override
	public void insert(T data) {
		root = insert(data, root);
	}

	private Node insert(T data, Node node) {
		if (node == null) {
			this.size++;
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
                // --- THIS IS THE START OF THE FIX ---
                this.size--; // Decrement size when the node is found

                if (node.left == null) {
                    return node.right;
                } else if (node.right == null) {
                    return node.left;
                }

                node.data = getMax(node.left);
                node.left = delete(getMax(node.left), node.left);
                // Because of the recursive call above, we must re-increment the size
                // to avoid a double count. The recursive call will handle the decrement.
                this.size++; 
                // --- THIS IS THE END OF THE FIX ---
            }

            // The node will not be null here, so no need for a null check before update
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
		// If the node is null, the data is not in the tree
		if (node == null) {
			return null;
		}

		// Compare the data with the current node's data
		int comparison = data.compareTo(node.data);

		// If the data is found, return it
		if (comparison == 0) {
			return node.data;
		} // If the data is less than the current node's data, search the left subtree
		else if (comparison < 0) {
			return search(data, node.left);
		} // If the data is greater, search the right subtree
		else {
			return search(data, node.right);
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

	@Override
	public int size() {
		return size;
	}

	@Override
	public Iterator<T> iterator() {
		return new InOrderIterator();
	}
        
        @Override
        public Iterator<T> reverseOrderIterator() {
            return new ReverseOrderIterator();
        }

	@Override
	public Iterator<T> preOrderIterator() {
		return new PreOrderIterator();
	}

	@Override
	public Iterator<T> postOrderIterator() {
		return new PostOrderIterator();
	}

	// Inner classes for each iterator type
	private class InOrderIterator implements Iterator<T> {

		private StackInterface<Node> stack;

		public InOrderIterator() {
			stack = new LinkedStack<>();
			pushLeftNodes(root);
		}

		@Override
		public boolean hasNext() {
			return !stack.isEmpty();
		}

		@Override
		public T next() {
			if (!hasNext()) {
				throw new java.util.NoSuchElementException();
			}
			Node node = stack.pop();
			pushLeftNodes(node.right);
			return node.data;
		}

		private void pushLeftNodes(Node node) {
			while (node != null) {
				stack.push(node);
				node = node.left;
			}
		}
	}
        
        
        private class ReverseOrderIterator implements Iterator<T> {

            private StackInterface<Node> stack;

            public ReverseOrderIterator() {
                stack = new LinkedStack<>();
                // Start by pushing the path to the LARGEST element (all the way right)
                pushRightNodes(root);
            }

            @Override
            public boolean hasNext() {
                return !stack.isEmpty();
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }

                // The node at the top of the stack is the next largest
                Node node = stack.pop(); 

                // Before returning, prepare for the next call by pushing the
                // right-most path of this node's LEFT child.
                pushRightNodes(node.left); 

                return node.data;
            }

            // Helper method to push the current node and all its right children
            private void pushRightNodes(Node node) {
                while (node != null) {
                    stack.push(node);
                    node = node.right; // The key change is here: we go right
                }
            }
        }

	private class PreOrderIterator implements Iterator<T> {

		private StackInterface<Node> stack;

		public PreOrderIterator() {
			stack = new LinkedStack<>();
			if (root != null) {
				stack.push(root);
			}
		}

		@Override
		public boolean hasNext() {
			return !stack.isEmpty();
		}

		@Override
		public T next() {
			if (!hasNext()) {
				throw new java.util.NoSuchElementException();
			}
			Node node = stack.pop();
			if (node.right != null) {
				stack.push(node.right);
			}
			if (node.left != null) {
				stack.push(node.left);
			}
			return node.data;
		}
	}

	private class PostOrderIterator implements Iterator<T> {

		private StackInterface<Node> stack;
		private Node lastVisited;

		public PostOrderIterator() {
			stack = new LinkedStack<>();
			lastVisited = null;
			pushLeftMost(root);
		}

		@Override
		public boolean hasNext() {
			return !stack.isEmpty();
		}

		@Override
		public T next() {
			if (!hasNext()) {
				throw new java.util.NoSuchElementException();
			}
			Node node = stack.peek();

			// Check if right child exists and hasn't been visited yet
			if (node.right != null && node.right != lastVisited) {
				pushLeftMost(node.right);
				return next(); // Recursive call to find the next element
			} else {
				lastVisited = stack.pop();
				return lastVisited.data;
			}
		}

		private void pushLeftMost(Node node) {
			while (node != null) {
				stack.push(node);
				if (node.left != null) {
					node = node.left;
				} else {
					node = node.right;
				}
			}
		}
	}
        
        // Add this method in your AVLTree class
        @Override
        public T get(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
            Counter count = new Counter(); // helper counter
            return getInOrder(root, index, count);
        }

        // Helper class to keep track of index during recursion
        private class Counter {
            int value = 0;
        }

        // Recursive in-order traversal to find the index-th node
        private T getInOrder(Node node, int index, Counter count) {
            if (node == null) return null;

            // Search left subtree
            T left = getInOrder(node.left, index, count);
            if (left != null) return left;

            // Check current node
            if (count.value == index) return node.data;
            count.value++;

            // Search right subtree
            return getInOrder(node.right, index, count);
        }
        
        @Override
        public void clear() {
            root = null;
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
}