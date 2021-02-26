
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingList<T>  {

  private class Node{
    T item;
    Node next;
    Node(T item){this.item = item;}
  }

  private final ReentrantLock lock = new ReentrantLock();

  private final Condition notFull = lock.newCondition();

  private final int capacity;

  private int size;

  private Node head;
  private Node last;


  private void enqueue(Node node) {
    last = last.next = node;
  }

  public BlockingList(int capacity) {
    if (capacity <= 0) throw new IllegalArgumentException();
    this.capacity = capacity;
    last = head = new Node(null);
    size = 0;
  }

  public BlockingList(){
    this(Integer.MAX_VALUE);
  }

  public int size() {
    return size;
  }

  public void add(T item) throws InterruptedException {
    if (item == null) throw new NullPointerException();
    final Node node = new Node(item);

    lock.lock();
    try {

      while (size == capacity) {
        notFull.await();
      }

      enqueue(node);
      size++;
      if(size==1)
        head = node;

      if (size < capacity)
        notFull.signal();
    } finally {
      lock.unlock();
    }
  }

  public T get(int index){
    final ReentrantLock lock = this.lock;
    lock.lock();
    try{
      if(index >= size)
        throw new IllegalArgumentException();

      Node node = head;
      for(int i = 0;i<index;i++)
        node = node.next;
      return node.item;

    }
    finally{
      lock.unlock();
    }
  }

  public void remove(int index){

    final ReentrantLock lock = this.lock;
    lock.lock();

    try{
      if(index >= size || index<0)
        throw new IllegalArgumentException();

      size--;

      if(size==0){
        head = last = new Node(null);
        return;
      }
      if(index == 0){
        head = head.next;

      }
      else{
        Node prev = null;
        Node node = head;


        for(int i = 0;i<index;i++){
          prev = node;
          node = node.next;
        }

        prev.next = node.next;

        if(index==size)
          last = prev;
      }
      if(capacity>=size)
        notFull.signal();
    }
    finally{
      lock.unlock();
    }
  }

  void order(){
    Node node = head;
    for(int i = 0;i<size;i++){
      System.out.print(node.item+" ");
      node = node.next;

    }
  }







}
