/**
 * 
 */
package org.mgwo.cloudsim;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

/**
 * 
 */
public class Main {
    public static void main(String[] args) {
        try {
            int numUsers = 1; // Hanya satu pengguna
            Calendar calendar = Calendar.getInstance();
            CloudSim.init(numUsers, calendar, false);
            
            // Buat Datacenter
            Datacenter datacenter = createDatacenter("Datacenter");
            
            // Buat Datacenter Broker
            DatacenterBroker broker = new DatacenterBroker("Broker");
            
            // Buat Virtual Machine (VM)
            List<Vm> vmList = new ArrayList<>();
            int vmCount = 5; // Jumlah VM
            for (int i = 0; i < vmCount; i++) {
                Vm vm = new Vm(i, broker.getId(), 1000, 1, 2048, 10000, 1000, "Xen", new CloudletSchedulerTimeShared());
                vmList.add(vm);
            }
            broker.submitVmList(vmList);
            
            // Buat Cloudlets (Tugas)
            List<Cloudlet> cloudletList = new ArrayList<>();
            int cloudletCount = 100; // Jumlah tugas (diubah juga)
            for (int i = 0; i < cloudletCount; i++) {
            	// Uncomment Cloudlet untuk uji coba
            	
            	// Cloudlet Kecil 
            	Cloudlet cloudletSmall = new Cloudlet(0, 1000, 1, 100, 50, new UtilizationModelFull(), new UtilizationModelFull(), new UtilizationModelFull());
            	cloudletSmall.setUserId(broker.getId());
            	cloudletList.add(cloudletSmall);

            	// Cloudlet Menengah
//            	Cloudlet cloudletMedium = new Cloudlet(1, 5000, 1, 500, 250, new UtilizationModelFull(), new UtilizationModelFull(), new UtilizationModelFull());
//            	cloudletMedium.setUserId(broker.getId());
//            	cloudletList.add(cloudletMedium);

            	// Cloudlet Besar
//            	Cloudlet cloudletLarge = new Cloudlet(2, 15000, 1, 1500, 750, new UtilizationModelFull(), new UtilizationModelFull(), new UtilizationModelFull());
//            	cloudletLarge.setUserId(broker.getId());
//            	cloudletList.add(cloudletLarge);
            }
            broker.submitCloudletList(cloudletList);

            // Gunakan MGWO Scheduler untuk optimasi
            TaskSchedulerMGWO scheduler = new TaskSchedulerMGWO(vmList, cloudletList);
            scheduler.optimize();

            // Jalankan Simulasi
            CloudSim.startSimulation();
            CloudSim.stopSimulation();

            // Hasil Akhir
            List<Cloudlet> finishedTasks = broker.getCloudletReceivedList();
            for (Cloudlet task : finishedTasks) {
                System.out.println("Cloudlet " + task.getCloudletId() + " selesai di VM " + task.getVmId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static Datacenter createDatacenter(String name) {
        List<Host> hostList = new ArrayList<>();
        int hostCount = 2;
        for (int i = 0; i < hostCount; i++) {
            List<Pe> peList = new ArrayList<>();
            int peCount = 4;
            for (int j = 0; j < peCount; j++) {
                peList.add(new Pe(j, new PeProvisionerSimple(1000)));
            }
            Host host = new Host(i, new RamProvisionerSimple(16384), new BwProvisionerSimple(100000), 1000000, peList, new VmSchedulerTimeShared(peList));
            hostList.add(host);
        }
        
        String arch = "x86";
        String os = "Linux";
        String vmm = "Xen";
        double timeZone = 10.0;
        double costPerSec = 3.0;
        double costPerMem = 0.05;
        double costPerStorage = 0.001;
        double costPerBw = 0.0;
        
        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, timeZone, costPerSec, costPerMem, costPerStorage, costPerBw);
        
        try {
            return new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), new LinkedList<>(), 0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
