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


          Patient patient_obj;

          while (true)
          {
              try {
                  Main.patient_ready_Doctor.acquire();
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }

              try {
                  Main.map_shield.acquire();
                  patient_obj = Main.nurseTodoctorMap.get(this.threadNum);
                  Main.map_shield.release();


                  if(patient_obj == null)
                  {
                      // do nothing
                  }
                  else
                  {
                       System.out.printf("\nDoctor %d listens to Symptoms from patient %d",this.threadNum,patient_obj.getThreadNum());


                      Main.map_shield.acquire();  // remove patient from Map
                     Main.nurseTodoctorMap.remove(patient_obj);
                      Main.map_shield.release();

                      System.out.printf("\nPatient %d receives advice from doctor %d",patient_obj.getThreadNum(),this.threadNum);

                      Main.Doctor_Finished[patient_obj.getThreadNum()].release();
                      Main.doctor_ready.release();
                  }

              }
              catch (Exception e)
              {
                  e.printStackTrace();
              }

          }

    }
}
