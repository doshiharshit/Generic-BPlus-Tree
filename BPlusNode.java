
import java.lang.reflect.Array;

public class BPlusNode<T extends Comparable<T>> {
	public Boolean leaf = true;
	public Data<T>[] data = null;
	public BPlusNode<T>[] siblings = null;
	public BPlusNode<T> left = null;
	public BPlusNode<T> right = null;
	public int size = 0;
	public Boolean full = false;
	public Boolean splited = false;
	
	@SuppressWarnings("unchecked")
	public BPlusNode(int len) {
		data = (Data<T>[]) Array.newInstance(Data.class, len);
		size = len;
	}
	
	@SuppressWarnings("unchecked")
	public void initSiblings(){
		siblings = (BPlusNode<T>[]) Array.newInstance(BPlusNode.class, size+1);
	}
	
	public Data<T>[] insertData(T value){
		@SuppressWarnings("unchecked")
		Data<T>[] firtsLastElem = (Data<T>[]) Array.newInstance(Data.class, 2);
		Data<T> tmp = new Data<T>(value);
		for(int i=0; i<size; i++){
			if(data[i] != null){
				if(data[i].compareTo(tmp.getT()) > 0 ){
					Data<T> ttt = data[i];
					data[i] = tmp;
					tmp = ttt;
				}else if(data[i].compareTo(value) == 0 ){
					data[i].incrementCount();
					firtsLastElem[0] = data[0];
					firtsLastElem[1] = null;
					return firtsLastElem;
				}
			}else{
				data[i] = tmp;
				if(i == size-1){
					full = true;
				}
				firtsLastElem[0] = data[0];
				firtsLastElem[1] = null;
				return firtsLastElem;
			}
		}
		
		firtsLastElem[0] = data[0];
		firtsLastElem[1] = tmp;
		return firtsLastElem;
	}
	
	public void insertAtIndex(int idx, Data<T> dataIn){
		data[idx] = dataIn;
	}
	
	@Override
	public String toString() {
		String message = "";
		message += "Data: {";
		for(int i=0; i<size; i++){
			message += data[i] + ", ";
		}
		message += "}\n";
		if(!leaf){
			for(int i=0; i<=size; i++){
				message += "Child: [" + i + " ";
				message += siblings[i] + ", ";
				message += "]\n";
			}
		}
		return message;
	}
}
