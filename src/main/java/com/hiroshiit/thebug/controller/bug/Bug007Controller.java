package com.hiroshiit.thebug.controller.bug;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * バグ THY-002: th:utext による XSS 脆弱性
 * パス: /bug/007
 */
@Controller
public class Bug007Controller {

    @GetMapping("/bug/007")
    public String showBug007(Model model) {
        model.addAttribute("bugCode", "THY-002");
        model.addAttribute("title", "th:utext による XSS 脆弱性");
        model.addAttribute("category", "thymeleaf");
        model.addAttribute("occurrenceCondition", "DB に保存された HTML/スクリプトを th:utext でそのまま出力する場合");
        model.addAttribute("errorMessage", "攻撃者が <script> 等を入力すると、そのまま実行される");
        model.addAttribute("wrongCode", "<div th:utext=\"${content}\"></div>");
        model.addAttribute("causeAnalysis", "th:utext は HTML をエスケープせずに出力するため、悪意のあるスクリプトが実行される。");
        model.addAttribute("correctCode", """
                <div th:text="${content}"></div>
                // または 表示上 HTML が必要な場合はサニタイズライブラリで許可タグのみ許可する
                """);
        model.addAttribute("correctComment", "ユーザー入力をそのまま表示する場合は th:text（エスケープ）を使う。th:utext は信頼できるコンテンツに限定する。");
        model.addAttribute("prevention", "IPA 指針: 無害化（サニタイズ）。すべての出力をエスケープする。");
        return "bugs/007";
    }
}
