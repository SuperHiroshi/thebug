package com.hiroshiit.thebug.controller.bug;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * バグ THY-001: ${user.name} で user が null のとき NPE
 * パス: /bug/006
 */
@Controller
public class Bug006Controller {

    @GetMapping("/bug/006")
    public String showBug006(Model model) {
        model.addAttribute("bugCode", "THY-001");
        model.addAttribute("title", "${user.name} で user が null のとき NPE");
        model.addAttribute("category", "thymeleaf");
        model.addAttribute("occurrenceCondition", "コントローラーで user を渡し忘れた、または null のままテンプレートに渡した場合");
        model.addAttribute("errorMessage", "NullPointerException または Thymeleaf のエラー");
        model.addAttribute("wrongCode", "<span th:text=\"${user.name}\"></span>");
        model.addAttribute("causeAnalysis", "user が null の場合、user.name の評価で NPE が発生する。");
        model.addAttribute("correctCode", """
                <span th:text="${user?.name}"></span>
                // または コントローラーで必ずデフォルトオブジェクトを渡す
                """);
        model.addAttribute("correctComment", "テンプレート側では Safe Navigation（?.）や null チェックをし、コントローラー側では null を渡さないようにする。");
        model.addAttribute("prevention", "モデルに渡すオブジェクトは null にしない、または Optional と th:if で分岐する。");
        return "bugs/006";
    }
}
