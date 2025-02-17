/**
 * Class for creating needs.
 *
 * @author Alexandra Mantagas
 */

 package com.ufund.model; 

 public class Need {
    private String name;
    private double cost;
    private int quantity;
    private String type; 

    public Need(String name, double cost, int quantity, String type){
        this.name = name;
        this.cost = cost;
        this.quantity = quantity;
        this.type = type;
    }
 }

 //getters and setters 
