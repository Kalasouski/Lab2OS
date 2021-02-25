import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

public class ListTest {

  BlockingList<Integer> testList = null;

  class TestThread extends Thread {

    int[] arr;
    TestThread(int[] arr){ this.arr = arr;}
    TestThread(){this.arr = null;}


    @Override
    public void run() {
      if(arr==null){
        try {
          sleep(100);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        testList.remove(4);
        testList.remove(0);
        testList.remove(5);
        return;
      }
      for(int num : arr) {
        try {
          testList.add(num);
        } catch (InterruptedException e) {
          System.out.println("Thread "+this.getName()+" was interrupted");
        }
      }
    }
  }


  @Test
  public void addGetTest() throws InterruptedException {

    testList = new BlockingList<>(10);
    TestThread thread1 = new TestThread(new int[]{1,2});
    TestThread thread2 = new TestThread(new int[]{2,3,4});
    TestThread thread3 = new TestThread(new int[]{4,5});

    thread1.start(); thread2.start(); thread3.start();
    thread1.join(); thread2.join(); thread3.join();

    int[] rightAnswer = new int[]{1,2,1,2,1};

    int[] answer = new int[5];

    for(int i = 0;i< testList.size();i++)
      answer[testList.get(i)-1]++;

    assertArrayEquals(rightAnswer,answer);

  }

  @Test
  public void sizeTest() throws InterruptedException {

    testList = new BlockingList<>(100);
    TestThread thread1 = new TestThread(new int[]{1,-22,2,4,6,5,7,88,45,24,67,35,-35});
    TestThread thread2 = new TestThread(new int[]{2,3,4,5,77,2,57,88,554});
    TestThread thread3 = new TestThread(new int[]{4,5,-567,446,45,-3});

    thread1.start(); thread2.start(); thread3.start();
    thread1.join(); thread2.join(); thread3.join();

    assertEquals(28,testList.size());

  }

  @Test
  public void removeTest() throws InterruptedException {


    testList = new BlockingList<>(20);
    TestThread thread1 = new TestThread(new int[]{1,2,-10});
    TestThread thread2 = new TestThread(new int[]{2,3,4,11,56});
    TestThread thread3 = new TestThread(new int[]{4,5,8,7});

    TestThread threadRemove = new TestThread();

    thread1.start(); thread2.start(); thread3.start(); threadRemove.start();
    thread1.join(); thread2.join(); thread3.join(); threadRemove.join();

    assertEquals(9,testList.size());


  }
}