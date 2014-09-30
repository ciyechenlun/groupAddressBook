/**
 * @author AMCC
 * <br /> 邮箱:zhouyusgs@ahmobile.com
 * <br /> 描述:ThreadPool.java
 * <br /> 版本：1.0.0
 * <br /> 日期：2013-9-16
 */
package com.cmcc.zysoft.groupaddressbook.util;

import java.util.LinkedList;

/**
 * @author 周瑜
 *com.cmcc.zysoft.groupaddressbook.util
 * 创建时间：2013-9-16
 */
public class ThreadPool extends ThreadGroup {

	private boolean isClosed = false; //线程池是否关闭
	@SuppressWarnings("rawtypes")
	private LinkedList workQueue;	//工作队列
	private static int threadPoolID = 1; //
	
	/**
	 * @param poolSize：线程工作池中的数量。每个纯种大约需要1MB的内存
	 */
	@SuppressWarnings("rawtypes")
	public ThreadPool(int poolSize) { 
		super(threadPoolID + "");	//指定threadGroup的名称
		setDaemon(true);	//继承到的方法，设置是否守护线程池
		workQueue = new LinkedList();	//创建工作队列
		for(int i = 0;i < poolSize;i++)
		{
			//创建并启动工作线程，线程池数量是多少就创建多少个工作线程
			new WorkThread(i).start();
		}
	}
	
	/**
	 * 向工作队列中新加入一个任务，由工作线程是执行该 任务
	 * @param task
	 */
	@SuppressWarnings("unchecked")
	public synchronized void execute(Runnable task)
	{
		if(isClosed)
		{
			throw new IllegalStateException();
		}
		if(task!=null)
		{
			workQueue.add(task);	//向队列中加入一个任务
			notify();	//唤醒一个正在getTask的方法中待任务的工作线程
		}
	}
	
	/**
	 * 从工作队列中取出一个任务，工作线程会调用此方法
	 * @param threadid
	 * @return
	 * @throws InterruptedException 
	 */
	public synchronized Runnable getTask(int threadid) throws InterruptedException
	{
		while(workQueue.size() == 0)
		{
			if(isClosed)
			{
				return null;
			}
			wait(); //如果工作线程中没有任务，就等待任务
		}
		//工作线程threadid开始执行任务
		return (Runnable) workQueue.removeFirst(); //返回列表中的第一个任务，并删除
	}
	
	/**
	 * 关闭线程池
	 */
	public synchronized void closePool()
	{
		if(!isClosed)
		{
			waitFinish();	//等待线程执行完毕
			isClosed = true;
			workQueue.clear();	//清空工作队列
			interrupt();	//中断线程池中的所有工作线程
		}
	}
	
	/**
	 * 等待工作线程把所有任务执行完毕
	 */
	public void waitFinish()
	{
		synchronized (this) {
			isClosed = true;
			notifyAll();	//唤醒所有在getTask()中等待的任务的工作线程
		}
		Thread[] threads = new Thread[activeCount()]; //activeCount() 返回该线程组中活动线程的估计值。  
		int count = enumerate(threads);	//enumerate()方法继承自ThreadGroup类，根据活动线程的估计值获得线程组中当前所有活动的工作线程 
		for(int i = 0;i < count; i++)
		{
			try
			{
				threads[i].join();	//等待工作线程结束
			}
			catch(InterruptedException  ex)
			{
				 ex.printStackTrace();
			}
		}
		
	}
	/**
	 * 内部类，工作线程，负责从工作队列中取出任务，并执行
	 * @author 周瑜
	 *com.cmcc.zysoft.groupaddressbook.util
	 * 创建时间：2013-9-16
	 */
	private class WorkThread extends Thread
	{
		private int id;
		public WorkThread(int id)
		{
			//父类构造方法，将线程加入到当前ThreadPool线程组中
			super(ThreadPool.this,id+"");
			this.id = id;
		}
		public void run()
		{
			while(!isInterrupted())
			{
				//isInterrupted()方法继承自Thread类，判断线程是否被中断  
				Runnable task = null;
				try
				{
					task = getTask(id);	//取出任务
				}
				catch(InterruptedException  ex)
				{
					ex.printStackTrace();
				}
				//如果getTask()返回null或者线程执行getTask()时被中断，则结束此线程  
				if(task == null)
				{
					return;
				}
				try
				{
					task.run(); //运行任务
				}
				catch(Throwable t)
				{
					t.printStackTrace();
				}
			}//end while
		}//end run
	}//end workThread

}
