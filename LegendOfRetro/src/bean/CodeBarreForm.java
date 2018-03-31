/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.util.Vector;

/**
 *
 * @author Adrien Marchand
 */
public class CodeBarreForm extends Form
{
    private String codeBarre;
    
    public CodeBarreForm(String cb)
    {
        this.codeBarre = "";
    }
    
    public String getCodeBarre()      {return this.codeBarre;}
}
