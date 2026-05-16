package com.hiroshiit.thebug.view;

/**
 * HTTP 状态码展示用ビュー。
 */
public class HttpStatusInfo {

    private final int code;
    private final String title;
    private final String category;
    private final String summary;
    private final String explanation;
    private final String analogy;
    private final String badgeClass;

    public HttpStatusInfo(int code, String title, String category, String summary,
                          String explanation, String analogy, String badgeClass) {
        this.code = code;
        this.title = title;
        this.category = category;
        this.summary = summary;
        this.explanation = explanation;
        this.analogy = analogy;
        this.badgeClass = badgeClass;
    }

    public int getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getSummary() {
        return summary;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getAnalogy() {
        return analogy;
    }

    public String getBadgeClass() {
        return badgeClass;
    }
}
