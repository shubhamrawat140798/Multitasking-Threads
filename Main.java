/*
Use a singleThreadExecutor to submit multiple threads.
Try shutdown() and shutdownNow() and observe the difference.
Use isShutDown() and isTerminated() with ExecutorService.
Return a Future from ExecutorService by using callable and use get(), isDone(), isCancelled() with the Future object to know the status of the task submitted.
Submit List of tasks to ExecutorService and wait for the completion of all the tasks.

Use the Synchronize method to enable synchronization between multiple threads trying to access the method at same time.
Use the Synchronize block to enable synchronization between multiple threads trying to access the method at same time.
Use Atomic Classes instead of Synchronize methods and blocks.
Coordinate 2 threads using wait() and notify().
Coordinate multiple threads using wait() and notifyAll()
Use Reentrantlock for coordinating 2 threads with signal(), signalAll() and wait().
Create a deadlock and Resolve it using tryLock().
*/

/*Synchronized method*/
class Work
{
    synchronized public void getLine()
    {
        for (int i = 0; i < 5; i++)
        {
            System.out.println(i);
            try
            {
                Thread.sleep(4);
            }
            catch (Exception e)
            {
                System.out.println(e);
            }
        }
    }
}
  
class Train extends Thread
{
    Work work;
    Train(Work work)
    {
        this.work = work;
    }
  
    @Override
    public void run()
    {
        work.getLine();
    }
}
  
public class Main
{
    public static void main(String[] args)
    {
        Work obj = new Work();
        Train train1 = new Train(obj);
        Train train2 = new Train(obj);
        train1.start();
        train2.start();
    }
}
}
}

/*Synchronized block*/
import java.io.*;
import java.util.*;
class Sender
{
    public void send(String txt)
    {
        System.out.println("Thread :\t"  + txt );
        try
        {
            Thread.sleep(100);
        }
        catch (Exception e)
        {
            System.out.println("Thread  interrupted.");
        }
        System.out.println("\nThread: " + txt + "Sent");
    }
}
  

class Control extends Thread
{
    private String txt;
    Sender  so;
  
    Control(String m,  Sender obj)
    {
        txt = m;
        so = obj;
    }
  
    public void run()
    {
        synchronized(so)
        {
            so.send(txt);
        }
    }
}
  

public class SyncDemo
{
    public static void main(String args[])
    {
        Sender snd = new Sender();
        Control t1 =
            new Control( " Thread 1 " , snd );
        Control t2 =
            new Control( " Thread 2 " , snd );
  
        t1.start();
        t2.start();
  
        try
        {
            t1.join();
            t2.join();
        }
        catch(Exception e)
        {
            System.out.println("Interrupted");
        }
    }
}



/*Atomic Classes*/
import java.util.concurrent.atomic.*;
class Update extends Thread {
  AtomicInteger aiCnt;
  
  Update(){
    aiCnt = new AtomicInteger();
  }
  
  public void run() {
    int max = 10;
    for(int i=0;i<max;i++)
      aiCnt.addAndGet(1);
  }
  
}
public class Atomic {
  public static void main(String[] args) throws InterruptedException {
    Update c = new Update();
    
    Thread t1 = new Thread(c, "Thread1");
    Thread t2 = new Thread(c, "Thread2");
    
    t1.start();
    t2.start();
    
    t1.join();
    t2.join();
    
    System.out.println(c.aiCnt);
  }
}

/*Wait and Notify*/

class WaitndNotify {
	volatile boolean p1 = false;
	synchronized void partFirst()
	{
		System.out.println("Thread t1 acquires a lock");
		p1 = true;
		System.out.println("Thread t1 about to surrender lock");
		notify();
	}
	synchronized void partSecond()
	{
		
		while (!p1) {
			try {
				System.out.println("Thread t2 is waiting for the lock to be released");
				wait();
				System.out.println("Thread t2 is running again");
			}
			catch (Exception e) {
				System.out.println(e.getClass());
			}
		}
		System.out.println("Work done!!");
	}
}

/*Multiple Threads using wait() and notifyall().*/
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class ProducerConsumerInJava {

    public static void main(String args[]) {
        System.out.println("Solving Producer Consumper Problem");
        Queue<Integer> buffer = new LinkedList<>();
        int maxSize = 10;
        
        Thread producer = new Producer(buffer, maxSize, "PRODUCER");
        producer.start();
    }
}

class Producer extends Thread {
    private Queue<Integer> queue;
    private int maxSize;
    
    public Producer(Queue<Integer> queue, int maxSize, String name){
        super(name);
        this.queue = queue;
        this.maxSize = maxSize;
    }
    
    @Override
    public void run() {
        while (true) {
            synchronized (queue) {
                while (queue.size() == maxSize) {
                    try {
                        System.out .println("Queue is full, "
                                + "Producer thread waiting for "
                                + "consumer to take something from queue");
                        queue.wait();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                Random random = new Random();
                int i = random.nextInt();
                System.out.println("Producing value : " + i);
                queue.add(i);
                queue.notifyAll();
            }

        }
    }
}

public class Main {

	public static void main(String[] args)
	{
		WaitndNotify obj = new WaitndNotify();
		Thread t1 = new Thread(new Runnable() {
			public void run() { obj.partFirst(); }
		});

		Thread t2 = new Thread(new Runnable() {
			public void run() { obj.partSecond(); }
		});

		t2.start();
		t1.start();
	}

/*wait() signal() and signalall()*/
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
public static void main(String[] args) {
final ReentrantLock re = new ReentrantLock();
final Condition co = re.newCondition();

new Thread(new Runnable() {
@Override
public void run() 
{
re.lock();
System.out.println(Thread.currentThread().getName() + " Lock obtained ");
System.out.println(Thread.currentThread().getName() + " Waiting for signal ");
try {
co.await();
} 
catch (InterruptedException e) {
e.printStackTrace();
}
System.out.println(Thread.currentThread().getName() + " Getting the signal ");
re.unlock();
}
}, " 1st Thread").start();

new Thread(new Runnable() {
@Override
public void run() {
re.lock();
System.out.println(Thread.currentThread().getName() + " Lock Obtained ");
try {
Thread.sleep(300);
} catch (InterruptedException e) {
e.printStackTrace();
}
System.out.println(Thread.currentThread().getName() + " Signal ");
co.signalAll();
re.unlock();
}
}, "2nd Thread").start();
}
}

/*DeadLock resolved using trylock*/
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
public class DeadLockExplicitLockTryLock {
  final static Lock lock1 = new ReentrantLock();
  final static Lock lock2 = new ReentrantLock();
  public static void main(String[] args) {
    Thread t1 = new Thread(new Runnable5());
    t1.setName("Thread 1");
    t1.start();
    Thread t2 = new Thread(new Runnable6());
    t2.setName("Thread 2");
    t2.start();
  }
}
class Runnable5 implements Runnable
{
  public void run() {
    boolean done = false;
    while (!done) {
      if (DeadLockExplicitLockTryLock.lock1.tryLock()) {
        try {
          System.out.println(Thread.currentThread().getName()
           + ": Got lockObject1. Trying for lockObject2");
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          if (DeadLockExplicitLockTryLock.lock2.tryLock()) {
            try {
              System.out.println(Thread.currentThread().getName()
                  + ": Got lockObject2.");
              done = true;
            } finally {
              DeadLockExplicitLockTryLock.lock2.unlock();
            }
          }
        } finally {
          DeadLockExplicitLockTryLock.lock1.unlock();
          try {
            Thread.sleep(500);
          } 
          catch (InterruptedException e) {

            e.printStackTrace();
          }
        }
      }
    }
  }
}

class Runnable6 implements Runnable
{
  public void run()
  {
    boolean done = false;
    while (!done) {
      if (DeadLockExplicitLockTryLock.lock2.tryLock()) {
        try {
          System.out.println(Thread.currentThread().getName()
              + ": Got lockObject1. Trying for lockObject2");
          try {
            Thread.sleep(1000);
          } 
          catch (InterruptedException e) {
            e.printStackTrace();
          }

          if (DeadLockExplicitLockTryLock.lock1.tryLock()) {
            try {
              System.out.println(Thread.currentThread().getName() + ": Got lockObject2.");
              done = true;
            } 
            finally {
              DeadLockExplicitLockTryLock.lock1.unlock();
            }
          }

        } 
        finally {
          DeadLockExplicitLockTryLock.lock2.unlock();
          try {

            Thread.sleep(750);

          } 
          catch (InterruptedException e) {

            e.printStackTrace();

          }
        }
      }
    }
  }
}
