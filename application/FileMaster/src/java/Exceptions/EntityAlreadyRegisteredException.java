/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exceptions;

/**
 *
 * @author Vlad
 */
public class EntityAlreadyRegisteredException extends Exception{
    public EntityAlreadyRegisteredException(String message)
    {
        super(message);
    }
}
