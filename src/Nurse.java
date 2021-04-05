public class Nurse implements Runnable{
    private int threadNum;

   public Nurse(int value)
   {
       this.threadNum = value;
   }

    public int getThreadNum()
    {
        return this.threadNum;
    }

    @Override
    public void run() {

        Patient local_patient = null;
        while (true)
        {
            try {
                Main.patient_ready_Nurse.acquire();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            try {
                Main.queue_shield.acquire(); // get patient
                local_patient = Main.Nurse_line.remove();
                Main.queue_shield.release();




                local_patient.setDoctorNum(this.threadNum);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

//            try {
//                Main.Nurse_ready_Doctor[threadNum].acquire();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            System.out.printf("\nNurse %d takes patient %d to the doctors office ",this.threadNum,local_patient.getThreadNum());

            try {
                Main.Nurse_Finished[local_patient.getThreadNum()].release(); // let the patient know the Nurse has Completed
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Main.nurse_ready.release();   // let new patient be checked in
            } catch (Exception e) {
                e.printStackTrace();
            }



        }


    }
}
