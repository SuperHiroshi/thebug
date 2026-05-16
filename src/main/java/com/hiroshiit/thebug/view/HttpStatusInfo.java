package com.hiroshiit.thebug.view;

import java.util.List;

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
    private final List<String> commonCases;
    private final List<String> frontendActions;
    private final List<String> backendChecks;
    private final String badgeClass;

    public HttpStatusInfo(int code, String title, String category, String summary,
                          String explanation, String analogy, List<String> commonCases,
                          List<String> frontendActions, List<String> backendChecks,
                          String badgeClass) {
        this.code = code;
        this.title = title;
        this.category = category;
        this.summary = summary;
        this.explanation = explanation;
        this.analogy = analogy;
        this.commonCases = commonCases;
        this.frontendActions = frontendActions;
        this.backendChecks = backendChecks;
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

    public List<String> getCommonCases() {
        return commonCases;
    }

    public List<String> getFrontendActions() {
        return frontendActions;
    }

    public List<String> getBackendChecks() {
        return backendChecks;
    }

    public String getBadgeClass() {
        return badgeClass;
    }
}
