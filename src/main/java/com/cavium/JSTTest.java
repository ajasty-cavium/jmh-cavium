
package com.cavium;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class JSTTest {
	private static int nThreads = 96;
	public static AtomicInteger nThread;
	private static long longestPause = 0;
	private static int arraySize = 128;
	private String oldStore[] = new String[arraySize];
	private String newStore[] = new String[arraySize];
	 
	private void finished() {
			if (nThread.decrementAndGet() == 0)
				System.err.println("max pause: " + longestPause);
	}   
	 
	private void go() {
		long time=System.currentTimeMillis(), tPause;
		for(int i=0;i<arraySize; i++) oldStore[i] = new String("hi" + i);
		for(int i=0;i<200000;++i){
			if(i%1000==0){
				redoOld();
				time=System.currentTimeMillis();
			}
			for(int j=0;j<arraySize;++j){
				newStore[j] = newStore[j].concat(oldStore[j]);
			}
			
			//youngStore.clear();
			long t=System.currentTimeMillis();
			tPause = t - time;
			//System.out.println("\nX" + (tPause));
			if (tPause > longestPause) longestPause = tPause;
			time=t;
			//youngStore.clear();
			//system.gc();
		}
		finished();
	}
		  
	private void redoOld(){
		//newStore.clear();
		newStore = new String[arraySize];
		for(int j=0;j<arraySize;++j){
			//oldStore.add(((Integer)j).toString());
			newStore[j] = new String();
		}
	}
	public static void runTest(){
		  nThread = new AtomicInteger(nThreads);
		for(int i=0;i<nThreads;++i){

			new Thread(){
				public void run(){
					new JSTTest().go();
				} 
			}.start();
		}
	}
	
	public static void main(String[] args){
		runTest();
	}
}

