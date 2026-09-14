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
/*
SuspiciousCase s = new SuspiciousCase(
        "CASE-1",
        10001L,
        "单笔金额大于等于 5 万",
        LocalDateTime.of(2026, 9, 10, 10, 30),
        "NEW"
);
        System.out.println(s.getCaseNo());
        System.out.println(s.getTransactionId());
        System.out.println(s.getRuleName());
        System.out.println(s.getCreateTime());
        System.out.println(s.getStatus());
        s.setCaseNo("CASE-2");
        s.setTransactionId(10002L);
        s.setRuleName("单日累计大于等于 20 万");
        s.setCreateTime(LocalDateTime.of(2026, 9, 14, 16, 30));
        s.setStatus("CONFIRMED");
        System.out.println(s);
*/
