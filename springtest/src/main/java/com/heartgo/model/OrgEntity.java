package com.heartgo.model;

import javax.persistence.*;

@Entity
@Table(name = "org", schema = "springmvcdemo", catalog = "")
public class OrgEntity {
    
    private String orgId ;
    private String orgName ;
    private int orgType;


    @Id
    @Column(name = "orgId", nullable = true, length = 45)
    public String getId() {

        return orgId;
    }

    public void setId(String orgId) {
        this.orgId = orgId;
    }

    @Basic
    @Column(name = "orgName", nullable = true, length = 45)
    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }



    @Basic
    @Column(name = "orgType", nullable = true, length = 45)
    public int  getOrgType() {
        return orgType;
    }

    public void setOrgType(int orgType) {
        this.orgType = orgType;
    }

}
