public class Doctor implements Runnable {
    private int threadNum;

    public int getThreadNum()
    {
        return this.threadNum;
    }

    public Doctor (int value)
    {
        this.threadNum = value;
    }
    public Doctor ()
    {
        this.threadNum = 0;
    }

    @Override
    public void run() {

          System.out.printf("\nhey I am a Doctor %d",this.threadNum);

    }
}
