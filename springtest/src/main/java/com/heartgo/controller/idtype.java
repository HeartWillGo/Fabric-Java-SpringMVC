package com.heartgo.controller;

public class idtype {
    public String idtype;        //      `json:"idtype"`     //idtype "1"表示用户
    public String[] idstring;    //          string[] `json:"idstring"`  //idstring

    public String getIdtype() {
        return idtype;
    }

    public void setIdtype(String idtype) {
        this.idtype = idtype;
    }

    public String[] getIdstring() {
        return idstring;
    }

    public void setIdstring(String[] idstring) {
        this.idstring = idstring;
    }
}
