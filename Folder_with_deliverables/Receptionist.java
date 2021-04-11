public class Receptionist implements Runnable{
    public void run(){

        int Patient_id;
        while(true)
        {
            try {

                Main.patient_ready_receptionist.acquire();

                // mutual exclusion for the Receptionist_line queue
                Main.queue_shield.acquire();
                    Patient_id = Main.Receptionist_line.remove().getThreadNum();
                Main.queue_shield.release();

                System.out.printf("\nReceptionist registers patient %d", Patient_id);

                Main.Receptionist_Finished[Patient_id].release();

                Main.receptionist_register.release();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }


    }
}
