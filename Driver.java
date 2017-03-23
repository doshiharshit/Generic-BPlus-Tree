
public class Driver {

	public static void main(String[] args) {
		BPlusTree<Integer> bPlusTree = new BPlusTree<Integer>(2);
		bPlusTree.insert(10);
		bPlusTree.insert(20);
		bPlusTree.insert(40);
		bPlusTree.insert(30);
		bPlusTree.insert(5);
		bPlusTree.insert(6);
		bPlusTree.insert(15);
		bPlusTree.insert(12);
		bPlusTree.insert(17);
		System.out.println(bPlusTree);
		bPlusTree.insert(13);
		System.out.println("full in");
		System.out.println(bPlusTree);
		
	}