package com.ufund.api.ufundapi.model;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a need in the cupboard containing name, cost, quantity,
 *
 * @author Alexandra Mantagas
 */
 
 public class Need {
    private String name;
    private double cost;
    private int quantity;
    private String type; 

    /**
     * Creates Need object.
     * 
     * @param name The name of the need.
     * @param cost The cost of the need.
     * @param quantity The quantity of the need.
     * @param type The type of need.
     */
    public Need(@JsonProperty("name") String name, 
                @JsonProperty("cost") double cost, 
                @JsonProperty("quantity") int quantity, 
                @JsonProperty("type") String type){
        this.name = name;
        this.cost = cost;
        this.quantity = quantity;
        this.type = type;
    }

    /**
     * Gets the name of the need.
     * 
     * @return Name of the need.
     */

   public String getName(){
      return name;
   }

    /**
     * Sets the name of the need.
     * 
     * @param name New name of the need.
     */

   public void setName(String name){
      this.name = name;
   }

    /**
     * Gets the cost of the need.
     * 
     * @return Cost of the need.
     */

   public double getCost(){
      return cost;
   }

    /**
     * Sets the name of the need.
     * 
     * @param cost New cost of the need.
     */

   public void setCost(double cost){
      this.cost = cost;
   }

    /**
     * Gets the quantity of the need.
     * 
     * @return Quantity of the need.
     */

   public int getQuantity(){
      return quantity;
   }

    /**
     * Sets the quantity of the need.
     * 
     * @param quantity Need quantity of the need.
     */

   public void setQuantity(int quantity){
      this.quantity = quantity;
   }

    /**
     * Gets the type of the need.
     * 
     * @return Type of the need.
     */

   public String getType(){
      return type;
   }

    /**
     * Sets the type of the need.
     * 
     * @param type New type of the need.
     */
   
   public void setType(String type){
      this.type = type; 
   }
 }

