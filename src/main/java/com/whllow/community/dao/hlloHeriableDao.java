package com.whllow.community.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class hlloHeriableDao implements hlloDao {
    @Override
    public String select() {
        return "Heriable";
    }
}
