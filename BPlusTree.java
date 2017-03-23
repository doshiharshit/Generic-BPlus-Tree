

public class BPlusTree<T extends Comparable<T>> {
	public BPlusNode<T> root;
	public int len;
	
	public BPlusTree(int lenIn) {
		len = lenIn;
	}
	
	public void insert(T value){
		if(root == null){
			root = new BPlusNode<T>(len);
		}
		root = insertData(root, value);
	}
	
	private BPlusNode<T> insertData(BPlusNode<T> node, T value){
		Data<T>[] firtsLastElem = null;
//		if(!node.full){
			if(node.leaf){
				firtsLastElem = node.insertData(value);
				if(firtsLastElem[1] != null){
					if(node.left != null && !node.left.full){
						node.left.insertData(firtsLastElem[0].getT());
						for(int i=0; i<len-1; i++){
							node.insertAtIndex(i, node.data[i+1]);
						}
						node.insertAtIndex(len-1, firtsLastElem[1]);
					}else if(node.right != null && !node.right.full){
						node.right.insertData(firtsLastElem[1].getT());
					}else{
						return splitAndMakeNewParent(firtsLastElem[1], node);
					}
				}
			}else{
				Boolean parentFound = false; int idx = 0;
				BPlusNode<T> nodeInserted = null;
				for (int i=0; i<=len; i++) {
					if(i<len && node.data[i]!=null){
						if(node.data[i].compareTo(value) > 0 ){
							nodeInserted = insertData(node.siblings[i], value);
							if(nodeInserted.splited){
								// parent formed
								parentFound = true;
								idx = i;
							}
							break;
						}else if(node.data[i].compareTo(value) == 0 ){
							insertData(node.siblings[i+1], value);
							break;
						}
					}else{
						nodeInserted = insertData(node.siblings[i], value);
						if(nodeInserted.splited){
							// parent formed
							parentFound = true;
							idx = i;
						}
						break;
					}
				}
				if(parentFound){
					BPlusNode<T> nextNode = nodeInserted.siblings[1];
					node.siblings[idx] = nodeInserted.siblings[0];
					if(idx>0){
						
					}
					nodeInserted = nextNode;
					for(int i=idx+1; i<=len && nodeInserted!=null; i++){
						nextNode = node.siblings[i]; // curr -> next
						node.siblings[i] = nodeInserted;
						if(idx>0){
							node.data[i-1] = node.siblings[i].data[0];
						}
						nodeInserted = nextNode; //node.siblings[idx + 1];
						if(i==len){
							node.full = true;
						}
					}
					if(nodeInserted != null && node.full){
						//extra
						if(node.left != null && !node.left.full){
							for(int i=0; i<=len; i++){
								if(node.left.siblings[i] == null){
									node.left.siblings[i] = nodeInserted;
									if(i==len){
										node.left.full = true;
									}
									break;
								}
							}
						}else if(node.right != null && !node.right.full){
							BPlusNode<T> tempNode = null;
							for(int i=0; i<=len && nodeInserted!=null; i++){
								tempNode = node.right.siblings[i];
								node.right.siblings[i] = nodeInserted;
								nodeInserted = tempNode;
								if(i==len){
									node.right.full = true;
								}
							}
						}else{
							return splitSiblings(node, nodeInserted);
						}
					}
				}
				for(int i=1; i<=len && node.siblings[i] != null; i++){
					node.data[i-1] = node.siblings[i].data[0];
				}
				if(node.siblings[1]!=null && !node.siblings[1].leaf){
					node.data[0] = getDataZero(node.siblings[1]);
				}
			}
//		}else{
			
//		}
		return node;
	}
	
	private BPlusNode<T> splitSiblings(BPlusNode<T> node, BPlusNode<T> nodeInserted){
		BPlusNode<T> parent = new BPlusNode<T>(len);
		BPlusNode<T> second = new BPlusNode<T>(len);
		BPlusNode<T> first  = new BPlusNode<T>(len);
		first.leaf = false;  first.initSiblings();
		second.leaf = false; second.initSiblings();
		parent.leaf = false; parent.initSiblings();
		
		for(int i=0; i<(len/2)+1; i++){
			first.siblings[i] = node.siblings[i];
		}
		for(int i=1; i<=len && first.siblings[i] != null; i++){
			first.data[i-1] = first.siblings[i].data[0];
		}
		if(first.siblings[1]!=null && !first.siblings[1].leaf){
			first.data[0] = getDataZero(first.siblings[1]);
		}
		int j=0;
		for(int i=(len/2)+1; i<=len; i++){
			second.siblings[j++] = node.siblings[i];
		}
		second.siblings[j] = nodeInserted;
		for(int i=1; i<=len && second.siblings[i] != null; i++){
			second.data[i-1] = second.siblings[i].data[0];
		}
		if(second.siblings[1]!=null && !second.siblings[1].leaf){
			second.data[0] = getDataZero(second.siblings[1]);
		}
		
		first.left = node.left;
		first.right = second;
		second.left = first;
		second.right = node.right;
		
		if(first.left != null){
			first.left.right = first;
		}
		if(second.right != null){
			second.right.left = second;
		}
		
		if(first.data[len-1]!=null){
			first.full = true;
		}
		if(second.data[len-1]!=null){
			second.full = true;
		}
		
		parent.siblings[0] = first;
		parent.siblings[1] = second;
		parent.splited = true;
		return parent;
	}
	
	private Data<T> getDataZero(BPlusNode<T> node){
		if(node != null && node.leaf){
			return node.data[0];
		}else{
			return getDataZero(node.siblings[0]);
		}
	}
	
	private BPlusNode<T> splitAndMakeNewParent(Data<T> data, BPlusNode<T> node){
		BPlusNode<T> first  = new BPlusNode<T>(len);
		BPlusNode<T> second = new BPlusNode<T>(len);
		BPlusNode<T> parent = new BPlusNode<T>(len);
		for(int i=0; i<len/2; i++){
			first.insertAtIndex(i, node.data[i]);
		}
		int j=0;
		for(int i=len/2; i<len; i++){
			second.insertAtIndex(j++, node.data[i]);
		}second.insertAtIndex(j, data);
		first.left = node.left;
		first.right = second;
		second.left = first;
		second.right = node.right;
		
		if(first.left != null){
			first.left.right = first;
		}
		if(second.right != null){
			second.right.left = second;
		}
		
		if(first.data[len-1]!=null){
			first.full = true;
		}
		if(second.data[len-1]!=null){
			second.full = true;
		}
		
		parent.leaf = false;
		parent.data[0] = second.data[0];
		parent.initSiblings();
		parent.siblings[0] = first;
		parent.siblings[1] = second;
		parent.splited = true;
		return parent;
	}
	
	@Override
	public String toString() {
		return root.toString();
	}
}
