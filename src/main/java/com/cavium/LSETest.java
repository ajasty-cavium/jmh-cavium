
package com.cavium;

import java.util.ArrayList;
import java.util.List;

public class LSETest {
	private static final int youngArraySize = 100000;
	private static final int oldArraySize = 20000000;
	 private List<String> oldStore = new ArrayList<>(oldArraySize);
	 private List<String> youngStore = new ArrayList<>(youngArraySize);
	 private static volatile int nThread = 96;
	private static long longestPause = 0;
	private volatile static int update = 0;
	 
	private synchronized void finished() {
			if (--nThread == 0)
				System.err.println("max pause: " + longestPause);
	}   
	private synchronized int update() {
		return update++;
	}
	 
	private void go() {
		long time=System.currentTimeMillis(), tPause;
		while (update() < 100000);
		if (update() > 10) return;
		for(int i=0;i<2000000;++i){
			/*if(i%100==0){
				redoOld();
				time=System.currentTimeMillis();
			}*/
			for(int j=0;j<youngArraySize/nThread;++j){
				//youngStore.add(((Integer)j).toString().replace("0", "91"));
			}
			//youngStore.clear();
			long t=System.currentTimeMillis();
			tPause = t - time;
			//System.out.println("\nX" + (tPause));
			if (tPause > longestPause) longestPause = tPause;
			time=t;
			update();
			//youngStore.clear();
			//system.gc();
		}
		finished();
	}
		  
	private void redoOld(){
		oldStore.clear();
		for(int j=0;j<oldArraySize/nThread;++j){
			oldStore.add(((Integer)j).toString());
		}
	}
	public static void runTest() {
		  
		for(int i=0;i<nThread;++i){

			new Thread(){
				public void run(){
					new LSETest().go();
				} 
			}.start();
		}
	}
	
	public static void main(String[] args) {
		runTest();
	}
}

