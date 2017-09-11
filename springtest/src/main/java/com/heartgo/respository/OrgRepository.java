package com.heartgo.respository;

import com.heartgo.model.OrgEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by dzkan on 2016/3/8.
 */
@Repository
public interface OrgRepository extends JpaRepository<OrgEntity, Integer> {

    @Modifying      // 说明该方法是修改操作
    @Transactional  // 说明该方法是事务性操作
    // 定义查询
    // @Param注解用于提取参数
    @Query("update OrgEntity org set org.orgName=:qOrgName, org.orgType=:qOrgType where org.id=:qOrgId")
    public void updateOrg(@Param("qOrgName") String orgName,
                           @Param("qOrgType") String orgType, @Param("qOrgId") Integer id);

}
