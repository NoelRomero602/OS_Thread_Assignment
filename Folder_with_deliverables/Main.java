import java.util.*;
import java.util.concurrent.Semaphore;

public class Main {
//  Determines if  the receptionist is available or occupied registering a patient
//Initially set to 1
    public static Semaphore receptionist_register;
//This semaphore indicates the amount of nurses that are available to walk the patients to their respective doctor offices
//Initial value = number of doctors
    public static Semaphore nurse_ready;
// Determinds the availability of doctor threads being available to service patients
//Initial value = number of doctors
    public static Semaphore doctor_ready ;
//This semaphore is used to signal from the patient to the receptionist
// that the patient thread obj is not avaible in the que line for the receptionist to access it.
    public static Semaphore patient_ready_receptionist = new Semaphore(0,true);

    // This Semaphore patient_ready_Nurse
    public static Semaphore patient_ready_Nurse = new Semaphore(0, true);


    public static Semaphore patient_ready_Doctor = new Semaphore(0,true);


    public static Semaphore[] Doctor_ready_nurse ;



    public static Semaphore[] Receptionist_Finished;

    public static Semaphore[] Nurse_Finished;

    public static Semaphore[] Doctor_Finished;

    public static ArrayList<Patient> patientArrayList;

    public static Semaphore queue_shield = new Semaphore(1);

    public static Semaphore map_shield = new Semaphore(1);

    public static Queue<Patient> Receptionist_line = new LinkedList<>();

    public static Queue<Patient> Nurse_line = new LinkedList<>();

    public static Map<Integer, Patient > nurseTodoctorMap = new HashMap<>();

    public static LinkedList<Thread> patiient_Th_list = new LinkedList<>();

    public static LinkedList<Thread> doctor_Th_list = new LinkedList<>();

    public static LinkedList<Thread> nurse_Th_list = new LinkedList<>();

    public static void main(String[] args) {
        Scanner myScanner = new Scanner(System.in);
        int num_Doctors = 0;
        int num_patients = 0;
        int num_nurses = 0;

// read in user inputs for quantity of doctors and patients
        while (true) {

            System.out.println("Enter the amount of Doctors");
            num_Doctors = myScanner.nextInt();
            if((num_Doctors > 0) && (num_Doctors <= 3) ) // if number of doctors is at max 3 end
            {
                break;
            }
            else
            {
                System.out.println("Enter an appropriate number of Doctors within range of 1-3");
            }

        }
        while (true)
        {
            System.out.println("Enter the quantity of patience you want");
            num_patients = myScanner.nextInt();
            if((num_patients > 0) && (num_patients <= 30 ))
            {
                break;
            }
            else
            {
                System.out.println("Enter the appropriate number of Patients within range of 1-30");
            }
        }

        // doctos and nurse are in the same quantity
        num_nurses = num_Doctors;
        // output the status of the room
        System.out.printf("\nRun with %d patients, %d nurses, %d doctor",num_patients,num_nurses,num_Doctors);
        Receptionist_Finished = new Semaphore [num_patients];


        Nurse_Finished = new  Semaphore [num_patients];

        Doctor_Finished = new  Semaphore[num_patients];

        Doctor_ready_nurse = new Semaphore[num_Doctors];

        patientArrayList = new ArrayList<>(num_patients);


        Receptionist receptionist_local = new Receptionist();

        Thread receptionist_Th = new Thread(receptionist_local);

         receptionist_register = new Semaphore(1, true);
         nurse_ready = new Semaphore(num_Doctors, true);
         doctor_ready = new Semaphore(num_Doctors,true);

        Thread patient_th;

        Patient Patient_obj;

        Nurse Nurse_obj;
        Thread Nurse_th;

        Doctor Doctor_obj;
        Thread Doctor_th;

        // start all patient threads and store them in a array list
         for(int i = 0; i < num_patients; i++)
           {
               Receptionist_Finished[i] = new Semaphore(0, true);
               Nurse_Finished[i] = new Semaphore(0,true);
               Doctor_Finished[i] = new Semaphore(0,true);

               Patient_obj = new Patient(i);
               patient_th = new Thread(Patient_obj);
               patient_th.start();  // start the Patient threads
                patiient_Th_list.add(patient_th);
               patientArrayList.add(Patient_obj); // add patient objects to arraylsit

           }

         // start all doctor threads  and nurse threads
         for (int i = 0; i < num_Doctors; i++)
          {
              Nurse_obj = new Nurse(i);
              Nurse_th = new Thread(Nurse_obj);

              Doctor_ready_nurse[i] = new Semaphore(1,true);

              Doctor_obj = new Doctor(i);
              Doctor_th = new Thread(Doctor_obj);

              Doctor_th.start();
              doctor_Th_list.add(Doctor_th);

              Nurse_th.start();
              nurse_Th_list.add(Nurse_th);
          }

        receptionist_Th.start(); // start Receptionist thread

        // join all the patient threads
         for(Thread patient_obj_th : patiient_Th_list)
         {
             try {
                 patient_obj_th.join();
             }
             catch (Exception e)
             {
                 e.printStackTrace();
             }
         }




         // end all threads

     System.exit(0);
    }
}
