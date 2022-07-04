/*
Create and Run a Thread using Runnable Interface and Thread class.
Use sleep and join methods with thread.
Use a singleThreadExecutor to submit multiple threads.
Try shutdown() and shutdownNow() and observe the difference.
Use isShutDown() and isTerminated() with ExecutorService.
Return a Future from ExecutorService by using callable and use get(), isDone(), isCancelled() with the Future object to know the status of the task submitted.
Submit List of tasks to ExecutorService and wait for the completion of all the tasks.
Schedule task using schedule(), scheduleAtFixedRate() and scheduleAtFixedDelay()
Increase concurrency with Thread pools using newCachedThreadPool() and newFixedThreadPool().
Use the Synchronize method to enable synchronization between multiple threads trying to access the method at same time.
Use the Synchronize block to enable synchronization between multiple threads trying to access the method at same time.
Use Atomic Classes instead of Synchronize methods and blocks.
Coordinate 2 threads using wait() and notify().
Coordinate multiple threads using wait() and notifyAll()
Use Reentrantlock for coordinating 2 threads with signal(), signalAll() and wait().
Create a deadlock and Resolve it using tryLock().
*/

//A)
class Normal extends Thread{
    public void run()
    {
        System.out.println("Thread is Running... thread extend method");
    }
}
class Run implements Runnable{
    public void run()
    {
        System.out.println("Thread is running.. by Runnable Interface method");
    }
}

public class Main
{
    public static void main(String[] args)
    {
        Normal t1= new Normal();
        t1.start();
        Run r= new Run();
        Thread t2= new Thread(r);
        t2.start();
    }
}

//B)
import java.lang.*;

public class JoinExample 
{
    public static void main(String[] args) throws InterruptedException
    {
        Thread t= new Thread(new Runnable()
        {
            public void run()
            {
                System.out.println("First Task");
                System.out.println("Sleeping for 3 seconds");
                try
                {
                    Thread.sleep(3000);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                System.out.println("First Task is done");
            }
        });
        Thread t1= new Thread(new Runnable()
        {
            public void run()
            {
                System.out.println("Second Task is done");
            }
        });
        t.start();
        t.join();
        t1.start();
    }
}

//C) 

import java.util.concurrent.*;

public class FutureCancel {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        long sT = System.nanoTime();
        Future<String> future = executorService.submit(() -> {
            Thread.sleep(2000);
            return "Callable for future has returned";
        });

        while(!future.isDone()) {
            System.out.println("Task is still not done...");
            Thread.sleep(200);
            double eTS = (System.nanoTime() - sT)/1000000000.0;

            if(eTS> 1) {
                future.cancel(true);
            }
        }
if(!future.isCancelled()) {
    System.out.println("Task has been completed");
    String res = future.get();
    System.out.println(res);
} else {
    System.out.println("Task was cancelled");
}

        executorService.shutdown();
    }
}

/*Schedule a task using schedule*/
import java.util.concurrent.*;

public class Schedule{
    public static void main(String[] args)
    {
         ScheduledExecutorService scheduler=Executors.newSingleThreadScheduledExecutor();
         Runnable task= new Runnable() {
             public void run()
             {
                 System.out.println("Hi");
             }
         };
         int delay=3;
         scheduler.schedule(task,delay,TimeUnit.SECONDS);
         scheduler.shutdown();
    }
}

/*Schedule a task using scheduleatFixedRate*/
import java.util.concurrent.*;

public class BeepClock implements Runnable{
    public void run()
    {
        System.out.println("Schedule has started");
    }
    public static void main(String[] args)
    {
        ScheduledExecutorService scheduler= Executors.newSingleThreadScheduledExecutor();
        Runnable task= new BeepClock();
        int init=6,perd=3;
        scheduler.scheduleAtFixedRate(task,init,perd,TimeUnit.SECONDS);
    }
}

/*Schedule at Fixed Rate*/
import java.util.concurrent.*;
class CountDownClock extends Thread {
    private String clockName;
 
    public CountDownClock(String clockName) {
        this.clockName = clockName;
    }
 
    public void run() {
        String threadName = Thread.currentThread().getName();
 
        for (int i = 5; i >= 0; i--) {
 
            System.out.printf("%s -> %s: %d\n", threadName, clockName, i);
 
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
 public class ConcurrentScheduledTasksExample {
 
    public static void main(String[] args) {
 
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);
 
        CountDownClock clock1 = new CountDownClock("C1");
        CountDownClock clock2 = new CountDownClock("C2");
        CountDownClock clock3 = new CountDownClock("C3");
 
        scheduler.scheduleWithFixedDelay(clock1, 3, 10, TimeUnit.SECONDS);
        scheduler.scheduleWithFixedDelay(clock2, 3, 15, TimeUnit.SECONDS);
        scheduler.scheduleWithFixedDelay(clock3, 3, 20, TimeUnit.SECONDS);
 
    }
}

/*New Cached Thread Pool*/
 import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestThread {
	
   public static void main(final String[] arguments) throws InterruptedException {
      ExecutorService executor = Executors.newCachedThreadPool();
      ThreadPoolExecutor pool = (ThreadPoolExecutor) executor;
      executor.submit(new Task());
      executor.submit(new Task());
      executor.shutdown();
   }  

   static class Task implements Runnable {

      public void run() {
         
         try {
            Long dur = (long) (Math.random() * 5);
            System.out.println("Running Task! Thread Name: " +
               Thread.currentThread().getName());
               TimeUnit.SECONDS.sleep(dur);
            System.out.println("Task Completed! Thread Name: " +
               Thread.currentThread().getName());
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
      }
   }

/*new fixed thread */
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestThread {
	
   public static void main(final String[] arguments) throws InterruptedException {
      ExecutorService executor = Executors.newFixedThreadPool(2);
      ThreadPoolExecutor pool = (ThreadPoolExecutor) executor;
      executor.submit(new Task());
      executor.submit(new Task());
      executor.shutdown();
   }  

   static class Task implements Runnable {

      public void run() {
         
         try {
            Long dur = (long) (Math.random() * 5);
            System.out.println("Running Task! Thread Name: " +
               Thread.currentThread().getName());
               TimeUnit.SECONDS.sleep(dur);
            
            System.out.println("Task Completed! Thread Name: " +
               Thread.currentThread().getName());
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
      }
   }

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
