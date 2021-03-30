import java.util.*;
import java.util.concurrent.Semaphore;

public class Main {

    public static Semaphore receptionist_register;

    public static Semaphore nurse_ready;

    public static Semaphore patient_ready = new Semaphore(0,true);

    public static Semaphore[] Receptionist_Finished;

    public static ArrayList<Patient> patientArrayList;

    public static Semaphore queue_shield = new Semaphore(1);

    public static Semaphore map_shield = new Semaphore(1);

    public static Queue<Patient> Receptionist_line = new LinkedList<>();

    public static Map<Integer, Patient > nurseTodoctorMap = new HashMap<>();

    public static void main(String[] args) {
        Scanner myScanner = new Scanner(System.in);
        int num_Doctors = 0;
        int num_patients = 0;
        int num_nurses = 0;
        int num_Receptionist = 1;

//        while (true) {
//
//            System.out.println("Enter the amount of Doctors");
//            num_Doctors = myScanner.nextInt();
//            if((num_Doctors > 0) && (num_Doctors <= 3) ) // if number of doctors is at max 3 end
//            {
//                break;
//            }
//            else
//            {
//                System.out.println("Enter an appropriate number of Doctors within range of 1-3");
//            }
//
//        }
//        while (true)
//        {
//            System.out.println("Enter the quantity of patience you want");
//            num_patients = myScanner.nextInt();
//            if((num_patients > 0) && (num_patients <= 30 ))
//            {
//                break;
//            }
//            else
//            {
//                System.out.println("Enter the appropriate number of Patients within range of 1-30");
//            }
//        }

        num_Doctors = 3 ;
        num_patients = 3 ;

        Receptionist_Finished = new Semaphore [3];

        num_nurses = num_Doctors;

        patientArrayList = new ArrayList<>(num_patients);


        Receptionist receptionist_local = new Receptionist();

        Thread receptionist_Th = new Thread(receptionist_local);

         receptionist_register = new Semaphore(1, true);
         nurse_ready = new Semaphore(num_Doctors, true);

        Thread patient_th;

        Patient Patient_obj;

         for(int i = 0; i < num_patients; i++)
           {
               Receptionist_Finished[i] = new Semaphore(0, true);
               Patient_obj = new Patient(i);
               patient_th = new Thread(Patient_obj);
               patient_th.start();  // start the Patient threads
               nurseTodoctorMap.put(i,Patient_obj);
               patientArrayList.add(Patient_obj); // add patient objects to arraylsit




           }



         receptionist_Th.start(); // start Receptionist thread


        try {
            receptionist_Th.join();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        for( Patient obj: patientArrayList)
        {
            patient_th = new Thread(obj);

            try {
                patient_th.join();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }



    }
}
