package com.example.it.customlistview;

public class state {
    static boolean cc=false;
    private boolean state=false;
    static String ID;
    static String PASSWORD;

    public void t_get_state(){
        this.state=true;
    }
    public void f_get_state(){
        this.state=false;
    }
    public boolean check_state(boolean aa){
        if(this.state==true)
            return true;
        else
            return false;
    }

    static public void state_allset() {
        cc = false;
        ID = "";
        PASSWORD="";
    }


}
