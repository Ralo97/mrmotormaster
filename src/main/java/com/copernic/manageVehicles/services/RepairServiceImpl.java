/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.copernic.manageVehicles.services;

import com.copernic.manageVehicles.dao.RepairDAO;
import com.copernic.manageVehicles.dao.TasksDAO;
import com.copernic.manageVehicles.dao.VehicleDAO;
import com.copernic.manageVehicles.domain.Repair;
import com.copernic.manageVehicles.domain.Task;
import com.copernic.manageVehicles.domain.User;
import com.copernic.manageVehicles.domain.Vehicle;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author enricledo
 */
@Service
public class RepairServiceImpl implements RepairService {

    @Autowired
    private RepairDAO repairDAO;
    
    @Autowired
    private TasksDAO taskDAO;
    
    @Autowired
    private VehicleDAO vehicleDAO;

    @Override
    @Transactional
    public Repair saveRepair(Repair repair) {
        return repairDAO.save(repair);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Repair> getAllRepairs() {
        return repairDAO.findAll();
    }

    
    @Transactional(readOnly = true)
    public Repair findById(Long id) {
        return repairDAO.findById(id).orElse(null);
    }

    
    @Transactional
    public void deleteById(Long id) {
        Repair repair = repairDAO.findById(id).orElse(null);
        for(Task task : repair.getTasks()){
            task.getRepairs().remove(repair);
            System.out.println(task);
            taskDAO.save(task);
        }
        Vehicle vehicle = repair.getVehicle();
        vehicle.getRepairs().remove(repair);
        vehicleDAO.save(vehicle);
        System.out.println(vehicle);
        repairDAO.deleteById(id);
    }

    public boolean existsById(Long id) {
        return repairDAO.existsById(id);
    }

    @Override
    public List<Repair> findByVehicle(Vehicle vehicle) {
        return repairDAO.findByVehicle(vehicle);
    }

    @Override
    @Transactional
    public double getTotalPrice(Long id) {
        Repair repair = findById(id);
        if (repair!=null) {
            List<Task> tasks = repair.getTasks();
            double totalPrice = tasks.stream()
                    .mapToDouble(Task::getPrice)
                    .sum();
            return totalPrice;
        } else {
            throw new RuntimeException("Repair with ID " + id + " not found.");
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Repair> searchBar(String query) {
        
        List<Repair> repairs = repairDAO.findAll();
        List<Repair> result = new ArrayList<>();
        int km = 0;
        Long repairId = 0L;
        boolean canAccessKm = false;
        boolean canAccessRepairId = false;
        
        try {
            km = Integer.parseInt(query);
            canAccessKm = true;
            repairId = Long.parseLong(query);
            canAccessRepairId = true;
        } catch (NumberFormatException e) {
            System.out.println("Query cannot be converted to Km or RepairId.");
        }

        for (int i = 0; i < repairs.size(); i++) {
            if(canAccessKm){
                if(repairs.get(i).getKm() == km){
                    result.add(repairs.get(i));
                }
            }
            if(canAccessRepairId){
                if(repairs.get(i).getRepairId().equals(repairId)){
                    result.add(repairs.get(i));
                }
            }
            if(repairs.get(i).getVehicle().getNumberPlate().contains(query)){
                result.add(repairs.get(i));
            }
            if(repairs.get(i).getObservation().contains(query))
                result.add(repairs.get(i));
            
        }
        if(result.isEmpty())
            return repairs;
        else
            return result;
    }
}
