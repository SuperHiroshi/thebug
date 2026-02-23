package com.hiroshiit.thebug.controller.bug;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * バグ SPR-001: @Autowired 注入失敗（コンポーネントがスキャン範囲外）
 * パス: /bug/001
 */
@Controller
public class Bug001Controller {

    @GetMapping("/bug/001")
    public String showBug001(Model model) {
        model.addAttribute("bugCode", "SPR-001");
        model.addAttribute("title", "@Autowired 注入失敗（コンポーネントがスキャン範囲外）");
        model.addAttribute("category", "spring");
        model.addAttribute("occurrenceCondition", "UserService が Spring のコンポーネントスキャン対象パッケージ外に存在する場合");
        model.addAttribute("errorMessage", "NoSuchBeanDefinitionException: No qualifying bean of type 'com.example.UserService'");
        model.addAttribute("wrongCode", """
                @Service
                public class UserService {
                }
                
                @RestController
                public class UserController {
                    @Autowired
                    private UserService userService;  // 注入されない
                }
                """);
        model.addAttribute("causeAnalysis", "UserService が @SpringBootApplication のスキャン基準パッケージ（例: com.hiroshiit.thebug）より上位や別パッケージにあると、Bean として登録されない。");
        model.addAttribute("correctCode", """
                // 正しい例: アプリケーションと同じ階層かそのサブパッケージに配置する
                package com.hiroshiit.thebug.service;
                
                @Service
                public class UserService { }
                
                @SpringBootApplication
                @ComponentScan(basePackages = {"com.hiroshiit.thebug", "com.example"})
                public class ThebugApplication { }
                """);
        model.addAttribute("correctComment", "コンポーネントは必ずベースパッケージかそのサブパッケージに配置する。");
        model.addAttribute("prevention", "パッケージ構造を規約に合わせ、スキャン範囲を意識する。");
        return "bugs/001";
    }
}
