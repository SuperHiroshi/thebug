package com.hiroshiit.thebug.controller.bug;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * バグ SPR-003: @Transactional が同一クラス内呼び出しで効かない
 * パス: /bug/003
 */
@Controller
public class Bug003Controller {

    @GetMapping("/bug/003")
    public String showBug003(Model model) {
        model.addAttribute("bugCode", "SPR-003");
        model.addAttribute("title", "@Transactional が同一クラス内呼び出しで効かない");
        model.addAttribute("category", "spring");
        model.addAttribute("occurrenceCondition", "process() から同一クラス内の save() を呼ぶ場合、プロキシを経由しないためトランザクションが開始されない");
        model.addAttribute("errorMessage", "（例外時でもロールバックされない・コミットされない）");
        model.addAttribute("wrongCode", """
                @Service
                public class OrderService {
                    @Transactional
                    public void save() { }
                    public void process() {
                        save();  // プロキシを経由しないため @Transactional が効かない
                    }
                }
                """);
        model.addAttribute("causeAnalysis", "Spring の @Transactional は AOP プロキシで実現される。同一オブジェクト内の this.save() 呼び出しはプロキシを経由しない。");
        model.addAttribute("correctCode", """
                @Service
                public class OrderService {
                    @Autowired
                    private OrderService self;
                    @Transactional
                    public void save() { }
                    public void process() {
                        self.save();  // プロキシ経由でトランザクションが有効
                    }
                }
                """);
        model.addAttribute("correctComment", "トランザクション境界は「別 Bean の public メソッド」に付与する。");
        model.addAttribute("prevention", "トランザクションが必要な処理は別 Service に分離する設計を推奨。");
        return "bugs/003";
    }
}
