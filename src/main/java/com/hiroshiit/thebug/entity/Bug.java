package com.hiroshiit.thebug.entity;

/**
 * バグ展示用エンティティ。
 * Bug知識庫の1件を表す。
 */
public class Bug {

    /** バグID */
    private Long id;
    /** 分類: spring, mybatis, thymeleaf, sql, security, concurrency, transaction, production-incident */
    private String category;
    /** バグ番号（表示用、例: SEC-001） */
    private String bugCode;
    /** タイトル（概要） */
    private String title;
    /** 発生条件 */
    private String occurrenceCondition;
    /** エラーメッセージ（発生時） */
    private String errorMessage;
    /** 誤ったコード（複数行可） */
    private String wrongCode;
    /** 原因分析 */
    private String causeAnalysis;
    /** 修正後コード（複数行、参照用） */
    private String correctCode;
    /** 修正コードの補足説明 */
    private String correctComment;
    /** 再発防止策 */
    private String prevention;
    /** 表示順（同一分類内） */
    private Integer sortOrder;

    public Bug() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getBugCode() { return bugCode; }
    public void setBugCode(String bugCode) { this.bugCode = bugCode; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getOccurrenceCondition() { return occurrenceCondition; }
    public void setOccurrenceCondition(String occurrenceCondition) { this.occurrenceCondition = occurrenceCondition; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public String getWrongCode() { return wrongCode; }
    public void setWrongCode(String wrongCode) { this.wrongCode = wrongCode; }
    public String getCauseAnalysis() { return causeAnalysis; }
    public void setCauseAnalysis(String causeAnalysis) { this.causeAnalysis = causeAnalysis; }
    public String getCorrectCode() { return correctCode; }
    public void setCorrectCode(String correctCode) { this.correctCode = correctCode; }
    public String getCorrectComment() { return correctComment; }
    public void setCorrectComment(String correctComment) { this.correctComment = correctComment; }
    public String getPrevention() { return prevention; }
    public void setPrevention(String prevention) { this.prevention = prevention; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }

    /**
     * バグIDからパスを生成する（規約: /bug/001 形式）
     */
    public String getPath() {
        if (id == null) {
            return "/";
        }
        return String.format("/bug/%03d", id);
    }
}
