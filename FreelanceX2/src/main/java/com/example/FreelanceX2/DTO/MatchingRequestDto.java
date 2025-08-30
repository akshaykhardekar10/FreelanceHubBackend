package com.example.FreelanceX2.DTO;

import lombok.Data;

@Data
public class MatchingRequestDto {
    private String entityId; // User ID or Job ID
    private int limit = 10; // Default limit for results
    
    public String getEntityId() {
        return entityId;
    }
    
    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }
    
    public int getLimit() {
        return limit;
    }
    
    public void setLimit(int limit) {
        this.limit = limit;
    }
}
