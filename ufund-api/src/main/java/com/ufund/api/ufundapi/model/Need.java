/**
 * Class for creating needs.
 *
 * @author Alexandra Mantagas
 */

 package com.ufund.api.ufundapi.model;

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

 //getters and setters 
 public String getName(){
    return name;
 }

 public void setName(String name){
    this.name = name;
 }

 public double getCost(){
    return cost;
 }

 public void setCost(double cost){
    this.cost = cost;
 }

 public int getQuantity(){
    return quantity;
 }

 public void setQuantity(){
    this.quantity = quantity;
 }

 public String getType(){
    return type;
 }
 
 public void setType(String type){
    this.type = type; 
 }
 }

