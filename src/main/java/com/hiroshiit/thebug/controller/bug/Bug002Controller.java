package com.hiroshiit.thebug.controller.bug;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * バグ SPR-002: 循環依存（A → B → A）
 * パス: /bug/002
 */
@Controller
public class Bug002Controller {

    @GetMapping("/bug/002")
    public String showBug002(Model model) {
        model.addAttribute("bugCode", "SPR-002");
        model.addAttribute("title", "循環依存（A → B → A）");
        model.addAttribute("category", "spring");
        model.addAttribute("occurrenceCondition", "AService が BService を注入し、BService が AService を注入する場合");
        model.addAttribute("errorMessage", "BeanCurrentlyInCreationException: Error creating bean with name 'AService'");
        model.addAttribute("wrongCode", """
                @Service
                public class AService {
                    @Autowired
                    private BService bService;
                }
                @Service
                public class BService {
                    @Autowired
                    private AService aService;
                }
                """);
        model.addAttribute("causeAnalysis", "二つの Bean が互いに参照し合うため、どちらを先に初期化するか決定できず、コンテナが失敗する。");
        model.addAttribute("correctCode", """
                @Service
                public class AService {
                    @Autowired
                    @Lazy
                    private BService bService;
                }
                // または共通ロジックを第三の Service に切り出し、循環を解消する
                """);
        model.addAttribute("correctComment", "循環参照は設計上の問題。インターフェース分離やファサードの導入で依存の向きを一方向にする。");
        model.addAttribute("prevention", "単一責任と依存の向きを意識し、循環ができない設計にする。");
        return "bugs/002";
    }
}
