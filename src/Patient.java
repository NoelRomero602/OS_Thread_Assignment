public class Patient implements Runnable{

    private int threadNum;
    private int DoctorNum;

    public int getThreadNum()
    {
        return this.threadNum;
    }

    public Patient (int value)
    {
        this.threadNum = value;
    }

    public void setDoctorNum(int value)
    {
        this.DoctorNum = value;
    }

    public int getDoctorNum()
    {
        return  this.DoctorNum;
    }

    @Override
    public void run() {
        System.out.printf("\nPatient %d enters waiting room, waits for receptionist",this.threadNum);
        try {
            Main.receptionist_register.acquire();

            Main.queue_shield.acquire();


            Main.Receptionist_line.add(this);

            Main.queue_shield.release();

            Main.patient_ready.release();

            Main.Receptionist_Finished[this.threadNum].acquire();

            System.out.printf("\nPatient %d leaves receptionist and sits in waiting room", this.threadNum);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
