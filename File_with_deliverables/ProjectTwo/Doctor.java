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
                  patient_obj = Main.nurseTodoctorMap.remove(this.threadNum);
                  Main.map_shield.release();


                  if(patient_obj == null)
                  {
                      // let new patients be ready since this doctor thread had no patientj
                     Main.patient_ready_Doctor.release();
                      // do nothing
                  }
                  else
                  {
                       System.out.printf("\nDoctor %d listens to Symptoms from patient %d",this.threadNum,patient_obj.getThreadNum());





                      Main.Doctor_Finished[patient_obj.getThreadNum()].release();
                      Main.doctor_ready.release(); // let new patients enter

                      Main.Doctor_ready_nurse[this.threadNum].release(); // let new patients enter the correct thread

                  }

              }
              catch (Exception e)
              {
                  e.printStackTrace();
              }

          }

    }
}
