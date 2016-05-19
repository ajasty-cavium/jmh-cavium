
package com.cavium;

import java.util.ArrayList;
import java.util.List;

public class GCTest {
	 private static final int youngArraySize = 20000;
	 private static final int oldArraySize = 20000000;
	 private List<String> oldStore = new ArrayList<>(oldArraySize);
	 private List<String> youngStore = new ArrayList<>(youngArraySize);
	 private static int nThread =48;
	private static long longestPause = 0;
	    
	 
	private void go() {
		long time=System.currentTimeMillis(), tPause;
		for(int i=0;i<1000;++i){
			if(i%100==0){
				redoOld();
				time=System.currentTimeMillis();
			}
			for(int j=0;j<youngArraySize/nThread;++j){
				youngStore.add(((Integer)j).toString());
			}
			youngStore.clear();
			long t=System.currentTimeMillis();
			tPause = t - time;
			//System.out.println("" + (tPause));
			if (tPause > longestPause) longestPause = tPause;
			time=t;
			//system.gc();
		}
		if (--nThread == 0)
			System.out.println("max pause: " + longestPause);
	}
		  
	private void redoOld(){
		oldStore.clear();
		for(int j=0;j<oldArraySize/nThread;++j){
			oldStore.add(((Integer)j).toString());
		}
	}
	public static void runTest(){
		  
		for(int i=0;i<nThread;++i){

			new Thread(){
				public void run(){
					new GCTest().go();
				} 
			}.start();
		}
	}
	
	public static void main(String[] args){
		runTest();
	}
}

