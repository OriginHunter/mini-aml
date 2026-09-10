package com.miniaml.model;

public class Customer {
    private Long id;
    private String name;
    private String idCard;

    public Customer(){}
    public Customer(Long id, String name, String idCard)
    {
        this.id = id;
        this.name = name;
        this.idCard = idCard;
    }
    public Long getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getIdCard(){
        return idCard;
    }

    public void setId(Long id){
        this.id = id;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setIdCard(String idCard){
        this.idCard = idCard;
    }

    @Override
    public String toString(){
        return "Customer{" +
                "id=" + id +
                ", name='" + name +
                "', idCard='" + idCard +
                "'}";
    }

}
