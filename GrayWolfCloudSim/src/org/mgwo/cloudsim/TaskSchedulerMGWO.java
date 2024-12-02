package org.mgwo.cloudsim;

import java.util.List;

import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.Cloudlet;

public class TaskSchedulerMGWO {
    private List<Vm> vmList;
    private List<Cloudlet> cloudletList;
    private FitnessFunction fitnessFunction;
    private MGWO mgwo;

    public TaskSchedulerMGWO(List<Vm> vmList, List<Cloudlet> cloudletList) {
        this.vmList = vmList;
        this.cloudletList = cloudletList;
        this.fitnessFunction = new FitnessFunction(vmList, cloudletList);
        this.mgwo = new MGWO(fitnessFunction);
    }

    public void optimize() {
        // Jalankan MGWO untuk mendapatkan alokasi terbaik
        int[] bestAllocation = mgwo.optimize();

        // Tetapkan Cloudlets ke VMs berdasarkan solusi terbaik
        for (int i = 0; i < cloudletList.size(); i++) {
            Cloudlet cloudlet = cloudletList.get(i);
            Vm vm = vmList.get(bestAllocation[i]);
            cloudlet.setVmId(vm.getId());
        }

        // Hitung metrik
        double makespan = fitnessFunction.calculateMakespan(bestAllocation);
        double resourceUtilization = fitnessFunction.calculateResourceUtilization(bestAllocation);
        double degreeOfImbalance = fitnessFunction.calculateDegreeOfImbalance(bestAllocation);

        // Cetak hasil
        System.out.println("\n==== Hasil Optimasi ====");
        System.out.println("Makespan: " + makespan);
        System.out.println("Resource Utilization: " + resourceUtilization);
        System.out.println("Degree of Imbalance: " + degreeOfImbalance);
        System.out.println("========================");
    }
}