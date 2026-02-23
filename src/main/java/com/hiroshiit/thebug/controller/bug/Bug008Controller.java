package com.hiroshiit.thebug.controller.bug;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * バグ SEC-001: 管理画面に権限チェックなし
 * パス: /bug/008
 */
@Controller
public class Bug008Controller {

    @GetMapping("/bug/008")
    public String showBug008(Model model) {
        model.addAttribute("bugCode", "SEC-001");
        model.addAttribute("title", "管理画面に権限チェックなし");
        model.addAttribute("category", "security");
        model.addAttribute("occurrenceCondition", "/admin に誰でもアクセスできる実装");
        model.addAttribute("errorMessage", "権限のないユーザーが管理機能を実行できる");
        model.addAttribute("wrongCode", """
                @GetMapping("/admin")
                public String admin() {
                    return "admin/index";
                }
                """);
        model.addAttribute("causeAnalysis", "認可（Authorization）が実装されておらず、URL を知っていれば誰でもアクセスできる。");
        model.addAttribute("correctCode", """
                @GetMapping("/admin")
                @PreAuthorize("hasRole('ADMIN')")
                public String admin() {
                    return "admin/index";
                }
                // かつ Spring Security で URL ベースの認可も設定する
                """);
        model.addAttribute("correctComment", "認証だけでなく認可（ロール・権限）を必ず付与する。@PreAuthorize や設定で URL を保護する。");
        model.addAttribute("prevention", "「デフォルト拒否」で、必要な URL だけ許可する設計にする。");
        return "bugs/008";
    }
}
