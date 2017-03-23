
public class Data<T> implements Comparable<T>{
	private T t;
	private int count = 0;
	
	public Data() {
		
	}
	
	public Data(T t) {
		this.t = t;
		this.count = 1;
	}
	
	public int incrementCount(){
		count++;
		return count;
	}
	
	public int decrementCount(){
		count--;
		return count;
	}
	
    public int getCount() {
		return count;
	}
	
    public void setCount(int count) {
		this.count = count;
	}
	
	public void setT(T t) { 
		this.t = t; 
	}
    
	public T getT() { 
    	return t; 
	}

	@Override
	public int compareTo(T o) {
		if(this.t instanceof Integer){
			return Integer.compare((Integer) this.t, (Integer) o);
		}else if(this.t instanceof String){
			return ((String) this.t).compareTo((String) o);
		}else if(this.t instanceof Float){
			return Float.compare((Float) this.t, (Float) o);
		}else if(this.t instanceof Double){
			return Double.compare((Double) this.t, (Double) o);
		}
		return 0;
	}
	
	@Override
	public String toString() {
		return this.getT() + ":" + this.getCount();
	}
}
