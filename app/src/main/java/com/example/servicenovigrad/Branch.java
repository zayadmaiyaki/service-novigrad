package com.example.servicenovigrad;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Branch {
    private String branchId;
    private String  name ;
    private String phoneNumber ;
    String address;
    Map <String,String> workingTimes;

    List<Service> serviceOfferred;

    public Branch (String name , String phoneNumber , String adress , Map<String,String> workingTimes , List<Service> serviceOffered){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.workingTimes = workingTimes;
        this.serviceOfferred = serviceOffered;
    }

    public Branch (){

    }

    public Branch(String branchId, String name, String phone, String address, Map<String, String> workingTimes, List<Service> serviceOffered) {
        this.branchId=branchId;
        this.name = name;
        this.phoneNumber = phone;
        this.address = address;
        this.workingTimes = workingTimes;
        this.serviceOfferred = serviceOffered;
    }

    public String getId() {
        return branchId;
    }

    public void setId(String branchId) {
        this.branchId = branchId;
    }

    public void setName(String name ){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public void setPhoneNumber(String phoneNumber ){
        this.phoneNumber = phoneNumber;
    }
    public String getPhoneNumber(){
        return phoneNumber;
    }
    public void setAddress(String address ){
        this.address = address;
    }
    public String getAddress(){
        return address;
    }
    public void setWorkingHours(Map<String,String> workingTimes){
        this.workingTimes = workingTimes;
    }
    public Map<String,String> getWorkingHours(){
        return workingTimes;
    }

    public void setServiceOfferred(List<Service> serviceOfferred){
        this.serviceOfferred = serviceOfferred;
    }
    public List<Service>getServiceOfferred(){
        return serviceOfferred;
    }




}
