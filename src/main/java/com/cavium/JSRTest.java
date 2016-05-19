import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class JSRTest {
	private static final int youngArraySize = 100000;
	private static final int oldArraySize = 20000000;
	 private List<String> oldStore = new ArrayList<>(oldArraySize);
	 private List<String> youngStore = new ArrayList<>(youngArraySize);
	// private static int nThread = 96;
	public static AtomicInteger nThread;
	private static long longestPause = 0;
	 
	private void finished() {
			if (nThread.decrementAndGet() == 0)
				System.err.println("max pause: " + longestPause);
	}   
	 
	private void go() {
		long time=System.currentTimeMillis(), tPause;
		for(int i=0;i<1000;++i){
			if(i%100==0){
				redoOld();
				time=System.currentTimeMillis();
			}
			for(int j=0;j<youngArraySize/96;++j){
				youngStore.add(((Integer)j).toString().replace("0", "91"));
			}
			//youngStore.clear();
			long t=System.currentTimeMillis();
			tPause = t - time;
			//System.out.println("\nX" + (tPause));
			if (tPause > longestPause) longestPause = tPause;
			time=t;
			youngStore.clear();
			//system.gc();
		}
		finished();
	}
		  
	private void redoOld(){
		oldStore.clear();
		for(int j=0;j<oldArraySize/96;++j){
			oldStore.add(((Integer)j).toString());
		}
	}
	public static void main(String[] args){
		  nThread = new AtomicInteger(96);
		for(int i=0;i<96;++i){

			new Thread(){
				public void run(){
					new JSRTest().go();
				} 
			}.start();
		}
	}
	
}

