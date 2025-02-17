/**
 * Implementation of CupboardDAO.java
 *
 * @author Alexandra Mantagas
 */

 package main.java.com.ufund.api.ufundapi.dao;

 import main.java.com.ufund.api.ufundapi.model.Need;
 import java.util.*;
 import java.io.*;

 public class CupboardFileDAO implements CupboardDAO{
    public CupboardFileDAO(){
        loadFromFile();
    }
 }