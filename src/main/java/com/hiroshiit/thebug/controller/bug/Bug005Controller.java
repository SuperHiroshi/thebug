package com.hiroshiit.thebug.controller.bug;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * バグ MYB-002: SQL インジェクション（${} の文字列結合）
 * パス: /bug/005
 */
@Controller
public class Bug005Controller {

    @GetMapping("/bug/005")
    public String showBug005(Model model) {
        model.addAttribute("bugCode", "MYB-002");
        model.addAttribute("title", "SQL インジェクション（${} の文字列結合）");
        model.addAttribute("category", "mybatis");
        model.addAttribute("occurrenceCondition", "ユーザー入力をそのまま ${} で SQL に埋め込む場合");
        model.addAttribute("errorMessage", "攻撃者が ' OR '1'='1 等を入力すると、意図しない SQL が実行される");
        model.addAttribute("wrongCode", """
                <select id="findUsersUnsafe" resultType="User">
                    SELECT * FROM users WHERE name = '${name}'
                </select>
                """);
        model.addAttribute("causeAnalysis", "${} は文字列置換のため、シングルクォート等をエスケープされず、SQL インジェクションの原因になる。");
        model.addAttribute("correctCode", """
                <select id="findUsersSafe" resultType="User">
                    SELECT * FROM users WHERE name = #{name}
                </select>
                """);
        model.addAttribute("correctComment", "プレースホルダは必ず #{} を使う。動的カラム名・テーブル名などやむを得ない場合のみ ${} を使い、入力値はホワイトリスト検証する。");
        model.addAttribute("prevention", "IPA 指針: 無効化（Bind Variables）を徹底する。");
        return "bugs/005";
    }
}
