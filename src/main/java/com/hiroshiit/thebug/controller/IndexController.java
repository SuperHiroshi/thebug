package com.hiroshiit.thebug.controller;

import com.hiroshiit.thebug.entity.Bug;
import com.hiroshiit.thebug.mapper.BugMapper;
import com.hiroshiit.thebug.view.HttpStatusInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
        model.addAttribute("httpStatuses", buildHttpStatuses());
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

    private List<HttpStatusInfo> buildHttpStatuses() {
        return List.of(
                status(200, "OK", "成功", "请求处理成功，服务器已经正常返回你要的内容。",
                        "最常见的成功响应。前端收到 200，通常表示接口调用、页面访问或数据读取都完成了。",
                        "像你去柜台办业务，工作人员当场把材料办好并交还给你。", "bg-emerald-500/15 text-emerald-300"),
                status(201, "Created", "成功", "请求成功，而且服务器新建了资源。",
                        "常用于创建动作，比如新增用户、提交订单、创建文章。它比 200 更明确地表达“新东西已经生成”。",
                        "像你去申请银行卡，柜台告诉你：已经办好了，新卡已经开户成功。", "bg-emerald-500/15 text-emerald-300"),
                status(301, "Moved Permanently", "重定向", "资源地址永久变更，之后请访问新地址。",
                        "搜索引擎优化、旧域名迁移时很常见。客户端和浏览器应该记住新的 URL。",
                        "像商店搬家后在老地址贴公告：以后请直接去新店，这里不再营业。", "bg-sky-500/15 text-sky-300"),
                status(400, "Bad Request", "客户端错误", "请求格式有问题，服务器看不懂或不能接受。",
                        "常见于参数缺失、JSON 格式错误、字段类型不正确。不是服务器坏了，而是请求本身不合法。",
                        "像你填表时把身份证号写成了汉字，窗口人员只能让你重新填写。", "bg-amber-500/15 text-amber-300"),
                status(401, "Unauthorized", "客户端错误", "请求缺少有效身份凭证，先登录或带上令牌再来。",
                        "通常表示没有登录、Token 过期、签名无效。它强调的是“你还没证明你是谁”。",
                        "像你到公司门禁口，却没有刷工牌，保安不会放你进去。", "bg-amber-500/15 text-amber-300"),
                status(403, "Forbidden", "客户端错误", "服务器知道你是谁，但你没有权限访问。",
                        "和 401 的区别是：认证通常已经通过，但角色、权限或访问策略不允许。",
                        "像你刷工牌进了大楼，但机房只有管理员能进，你在门口仍然会被拦下。", "bg-amber-500/15 text-amber-300"),
                status(404, "Not Found", "客户端错误", "服务器找不到你请求的资源。",
                        "可能是路径写错、资源已删除，或者后端根本没有这个路由。",
                        "像你拿着地址去找一家店，结果这条街上根本没有这家店。", "bg-amber-500/15 text-amber-300"),
                status(405, "Method Not Allowed", "客户端错误", "这个地址存在，但你用错了 HTTP 方法。",
                        "例如接口只允许 POST，你却发送 GET；或者只允许 GET，你却用了 DELETE。",
                        "像你到了正确窗口，但这个窗口只办取号业务，你却要在这里退款。", "bg-amber-500/15 text-amber-300"),
                status(409, "Conflict", "客户端错误", "请求本身没问题，但和当前资源状态冲突。",
                        "常见于重复提交、版本冲突、唯一键冲突，比如同一个用户名已被占用。",
                        "像你想注册一个昵称，系统告诉你这个名字已经被别人用了。", "bg-amber-500/15 text-amber-300"),
                status(429, "Too Many Requests", "客户端错误", "请求太频繁，触发了限流。",
                        "接口在保护自己，防止刷接口、瞬时高并发或恶意请求压垮服务。通常会配合重试时间提示。",
                        "像奶茶店太忙了，店员说先别继续下单，稍后再排队。", "bg-amber-500/15 text-amber-300"),
                status(500, "Internal Server Error", "服务器错误", "服务器内部出错了，但没有更具体的错误码。",
                        "这是一种兜底错误。常见原因有空指针、未处理异常、配置错误或代码逻辑缺陷。",
                        "像餐厅后厨突然乱成一团，前台只能说：店里出了问题，暂时做不了。", "bg-rose-500/15 text-rose-300"),
                status(503, "Service Unavailable", "服务器错误", "服务暂时不可用，通常是过载或维护中。",
                        "和 500 不同，503 更强调服务目前顶不住或者主动下线，稍后可能恢复。",
                        "像医院挂号处贴出通知：系统维护中，请稍后再来。", "bg-rose-500/15 text-rose-300"),
                status(504, "Gateway Timeout", "服务器错误", "上游服务响应太慢，网关等超时了。",
                        "多见于微服务、反向代理或 API 网关场景。当前服务在等别的服务，但一直没等到。",
                        "像前台帮你打电话联系仓库确认库存，结果仓库一直不接电话，前台只能让你稍后再来。", "bg-rose-500/15 text-rose-300")
        );
    }

    private HttpStatusInfo status(int code, String title, String category, String summary,
                                  String explanation, String analogy, String badgeClass) {
        return new HttpStatusInfo(code, title, category, summary, explanation, analogy, badgeClass);
    }
}
