package com.whllow.community.Dao;

import org.springframework.stereotype.Repository;

@Repository("MyBaits")
public class hlloMyBaitsDao implements hlloDao {
    @Override
    public String select() {
        return "MyBaits";
    }
}
