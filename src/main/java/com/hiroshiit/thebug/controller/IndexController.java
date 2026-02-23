package com.hiroshiit.thebug.controller;

import com.hiroshiit.thebug.entity.Bug;
import com.hiroshiit.thebug.mapper.BugMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * トップページおよび分類別一覧を表示するコントローラー。
 * Bug知識庫の入口。
 */
@Controller
@RequestMapping
public class IndexController {

    private final BugMapper bugMapper;

    public IndexController(BugMapper bugMapper) {
        this.bugMapper = bugMapper;
    }

    /**
     * トップ: 分類一覧と全バグの概要を表示
     */
    @GetMapping("/")
    public String index(Model model) {
        List<String> categories = bugMapper.selectCategories();
        Map<String, List<Bug>> bugsByCategory = new LinkedHashMap<>();
        for (String c : categories) {
            bugsByCategory.put(c, bugMapper.selectByCategory(c));
        }
        model.addAttribute("categories", categories);
        model.addAttribute("bugsByCategory", bugsByCategory);
        return "index";
    }

    /**
     * 分類別一覧: /bug/spring, /bug/mybatis 等
     */
    @GetMapping("/bug/{category}")
    public String listByCategory(@PathVariable String category, Model model) {
        List<Bug> bugs = bugMapper.selectByCategory(category);
        model.addAttribute("category", category);
        model.addAttribute("bugs", bugs);
        return "bugs/list";
    }
}
