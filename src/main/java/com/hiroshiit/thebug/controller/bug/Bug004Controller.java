package com.hiroshiit.thebug.controller.bug;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * バグ MYB-001: Parameter 'xxx' not found（引数名不一致）
 * パス: /bug/004
 */
@Controller
public class Bug004Controller {

    @GetMapping("/bug/004")
    public String showBug004(Model model) {
        model.addAttribute("bugCode", "MYB-001");
        model.addAttribute("title", "Parameter 'xxx' not found（引数名不一致）");
        model.addAttribute("category", "mybatis");
        model.addAttribute("occurrenceCondition", "Mapper メソッドの引数名と XML の #{name} が一致していない場合");
        model.addAttribute("errorMessage", "Parameter 'name' not found. Available parameters are [username, param2]");
        model.addAttribute("wrongCode", """
                <select id="find" resultType="User">
                    SELECT * FROM user WHERE name = #{name}
                </select>
                // Java: User find(String username);
                """);
        model.addAttribute("causeAnalysis", "XML では #{name} と書いているが、Java の引数名は username のため、MyBatis が名前でバインドできずエラーになる。");
        model.addAttribute("correctCode", """
                <select id="find" resultType="User">
                    SELECT * FROM user WHERE name = #{username}
                </select>
                // または User find(@Param("name") String username);
                """);
        model.addAttribute("correctComment", "引数が複数ある場合は @Param を付ける。単一引数でも名前を XML と一致させるか @Param で明示する。");
        model.addAttribute("prevention", "Mapper インターフェースと XML のパラメータ名を規約で統一する。");
        return "bugs/004";
    }
}
