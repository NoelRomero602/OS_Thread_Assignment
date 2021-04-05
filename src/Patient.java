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
 //       try {
     //       Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        try {
            Main.receptionist_register.acquire();

            Main.queue_shield.acquire();


            Main.Receptionist_line.add(this); // add patient to Receptionist Line

            Main.queue_shield.release();
            Main.patient_ready_receptionist.release();

            Main.Receptionist_Finished[this.threadNum].acquire();

            System.out.printf("\nPatient %d leaves receptionist and sits in waiting room", this.threadNum);
         //   Thread.sleep(1000);
            Main.nurse_ready.acquire();

            Main.queue_shield.acquire(); // add patient to Nurse Line
            Main.Nurse_line.add(this);
            Main.queue_shield.release();
            Main.patient_ready_Nurse.release();
            Main.Nurse_Finished[this.threadNum].acquire();

            Main.doctor_ready.acquire();

            Main.Doctor_ready_nurse[this.DoctorNum].acquire();

            Main.map_shield.acquire(); // put patient in map
            Main.nurseTodoctorMap.put(this.DoctorNum,this);
            Main.map_shield.release();

            System.out.printf("\nPatient %d enters doctor %d's office",this.threadNum, this.DoctorNum);
       //     Thread.sleep(1000);
            Main.patient_ready_Doctor.release();

            Main.Doctor_Finished[this.threadNum].acquire(); // wait for doctor to finish

            System.out.printf("\nPatient %d receives advice from doctor %d",this.threadNum,this.getDoctorNum());
              //  Thread.sleep(100);


            System.out.printf("\nPatient %d leaves",this.threadNum);



          //  System.exit(0);



        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
