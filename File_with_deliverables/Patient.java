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
            // wait for receptionist thread to be ready
            Main.receptionist_register.acquire();

            // mutual exclusion for the Receptionist_line
            Main.queue_shield.acquire();


            Main.Receptionist_line.add(this); // add patient to Receptionist Line

            Main.queue_shield.release();

            // signal receptionist thread patient is in the line
            Main.patient_ready_receptionist.release();

            // patient waits for receptionist to finished
            Main.Receptionist_Finished[this.threadNum].acquire();

            System.out.printf("\nPatient %d leaves receptionist and sits in waiting room", this.threadNum);


            Main.nurse_ready.acquire();

            Main.queue_shield.acquire(); // add patient to Nurse Line with mutual exclusion
            Main.Nurse_line.add(this);
            Main.queue_shield.release();

            // tell nurse thread patient is in the line
            Main.patient_ready_Nurse.release();
            // make patient thread wait until nurse finishes
            Main.Nurse_Finished[this.threadNum].acquire();

            // wait for doctor
            Main.doctor_ready.acquire();
            // wait for specific doctor
            Main.Doctor_ready_nurse[this.DoctorNum].acquire();


            Main.map_shield.acquire(); // put patient in map with mutual exclusion

            Main.nurseTodoctorMap.put(this.DoctorNum,this);

            Main.map_shield.release();

            System.out.printf("\nPatient %d enters doctor %d's office",this.threadNum, this.DoctorNum);

            Main.patient_ready_Doctor.release();


            Main.Doctor_Finished[this.threadNum].acquire(); // wait for doctor to finish

            System.out.printf("\nPatient %d receives advice from doctor %d",this.threadNum,this.getDoctorNum());

            System.out.printf("\nPatient %d leaves",this.threadNum);


        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
