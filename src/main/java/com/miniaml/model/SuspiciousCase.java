package com.miniaml.model;

import java.time.LocalDateTime;

public class SuspiciousCase {
    //字段
    private String caseNo;
    private Long transactionId;
    private String ruleName;
    private LocalDateTime createTime;
    private String status;
    //构造函数
    public SuspiciousCase(){

    }
    public SuspiciousCase(String caseNo,
                          Long transactionId,
                          String ruleName,
                          LocalDateTime createTime,
                          String status){
        this.caseNo =  caseNo;
        this.transactionId =  transactionId;
        this.ruleName =  ruleName;
        this.createTime =  createTime;
        this.status =  status;
    }
    //五个字段读取与修改函数
    public String getCaseNo(){
        return caseNo;
    }
    public Long getTransactionId(){
        return transactionId;
    }
    public String getRuleName(){
        return ruleName;
    }
    public LocalDateTime getCreateTime(){
        return createTime;
    }
    public String getStatus(){
        return status;
    }

    public void setCaseNo(String caseNo) {
        this.caseNo = caseNo;
    }
    public void setTransactionId(Long transactionId){
        this.transactionId = transactionId;
    }
    public void setRuleName(String ruleName){
        this.ruleName = ruleName;
    }
    public void setCreateTime(LocalDateTime createTime){
        this.createTime = createTime;
    }
    public void setStatus(String status){
        this.status = status;
    }
    //重写toString()
    @Override
    public String toString(){
        return "SuspiciousCase{" +
                "caseNo='" + caseNo +
                "', transactionId=" + transactionId +
                ", ruleName='" + ruleName +
                "', createTime=" + createTime +
                ", status='" + status +
                "'}";
    }
}
