package com.hiroshiit.thebug.controller;

import com.hiroshiit.thebug.entity.Bug;
import com.hiroshiit.thebug.mapper.BugMapper;
import com.hiroshiit.thebug.view.HttpStatusInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;

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

    @GetMapping("/http-status/{code}")
    public String httpStatusDetail(@PathVariable int code, Model model) {
        HttpStatusInfo statusInfo = buildHttpStatuses().stream()
                .filter(status -> status.getCode() == code)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("status", statusInfo);
        model.addAttribute("httpStatuses", buildHttpStatuses());
        model.addAttribute("pageTitle", statusInfo.getCode() + " " + statusInfo.getTitle());
        return "http-status-detail";
    }

    private List<HttpStatusInfo> buildHttpStatuses() {
        return List.of(
                status(200, "OK", "成功", "请求处理成功，服务器已经正常返回你要的内容。",
                        "最常见的成功响应。前端收到 200，通常表示接口调用、页面访问或数据读取都完成了。",
                        "像你去柜台办业务，工作人员当场把材料办好并交还给你。",
                        List.of("列表查询成功返回数据", "页面正常打开", "接口校验通过后返回结果"),
                        List.of("按正常成功流程渲染页面或提示", "需要时读取响应体并更新界面"),
                        List.of("确保返回结构稳定且字段完整", "成功场景也要保持统一响应格式"),
                        "bg-emerald-500/15 text-emerald-300"),
                status(201, "Created", "成功", "请求成功，而且服务器新建了资源。",
                        "常用于创建动作，比如新增用户、提交订单、创建文章。它比 200 更明确地表达“新东西已经生成”。",
                        "像你去申请银行卡，柜台告诉你：已经办好了，新卡已经开户成功。",
                        List.of("新建用户成功", "新增文章或评论完成", "创建订单后返回新资源 ID"),
                        List.of("创建成功后跳转详情页或刷新列表", "必要时使用返回的 ID 更新本地状态"),
                        List.of("创建接口返回新资源标识", "避免创建成功却仍返回模糊的错误信息"),
                        "bg-emerald-500/15 text-emerald-300"),
                status(301, "Moved Permanently", "重定向", "资源地址永久变更，之后请访问新地址。",
                        "搜索引擎优化、旧域名迁移时很常见。客户端和浏览器应该记住新的 URL。",
                        "像商店搬家后在老地址贴公告：以后请直接去新店，这里不再营业。",
                        List.of("旧页面永久迁移到新地址", "接口版本废弃后指向新版路径"),
                        List.of("跟随跳转并更新收藏地址", "避免把旧地址硬编码在前端"),
                        List.of("返回正确的 Location 头", "确认永久跳转不会影响登录态和缓存"),
                        "bg-sky-500/15 text-sky-300"),
                status(400, "Bad Request", "客户端错误", "请求格式有问题，服务器看不懂或不能接受。",
                        "常见于参数缺失、JSON 格式错误、字段类型不正确。不是服务器坏了，而是请求本身不合法。",
                        "像你填表时把身份证号写成了汉字，窗口人员只能让你重新填写。",
                        List.of("参数字段缺失", "JSON 结构不合法", "字段类型和接口约定不一致"),
                        List.of("先检查请求参数和请求体", "把后端返回的校验信息展示给用户"),
                        List.of("给出明确的字段级错误提示", "统一参数校验和异常处理入口"),
                        "bg-amber-500/15 text-amber-300"),
                status(401, "Unauthorized", "客户端错误", "请求缺少有效身份凭证，先登录或带上令牌再来。",
                        "通常表示没有登录、Token 过期、签名无效。它强调的是“你还没证明你是谁”。",
                        "像你到公司门禁口，却没有刷工牌，保安不会放你进去。",
                        List.of("未携带 Token", "Token 过期", "签名校验失败"),
                        List.of("跳转登录页或触发重新登录", "避免把 401 当成普通业务失败"),
                        List.of("确认认证过滤器和 Token 解析逻辑", "检查登录态刷新和过期策略"),
                        "bg-amber-500/15 text-amber-300"),
                status(403, "Forbidden", "客户端错误", "服务器知道你是谁，但你没有权限访问。",
                        "和 401 的区别是：认证通常已经通过，但角色、权限或访问策略不允许。",
                        "像你刷工牌进了大楼，但机房只有管理员能进，你在门口仍然会被拦下。",
                        List.of("普通用户访问管理员页面", "接口被角色权限拦截", "IP 白名单不满足"),
                        List.of("展示无权限提示页", "不要无意义重试同一个请求"),
                        List.of("检查角色映射和权限注解", "确认鉴权规则没有误伤正常用户"),
                        "bg-amber-500/15 text-amber-300"),
                status(404, "Not Found", "客户端错误", "服务器找不到你请求的资源。",
                        "可能是路径写错、资源已删除，或者后端根本没有这个路由。",
                        "像你拿着地址去找一家店，结果这条街上根本没有这家店。",
                        List.of("前端请求路径拼错", "资源已删除", "接口根本没有发布"),
                        List.of("核对 URL、参数和环境地址", "给用户提供返回首页或重试入口"),
                        List.of("确认路由注册和反向代理配置", "检查资源 ID 是否真实存在"),
                        "bg-amber-500/15 text-amber-300"),
                status(405, "Method Not Allowed", "客户端错误", "这个地址存在，但你用错了 HTTP 方法。",
                        "例如接口只允许 POST，你却发送 GET；或者只允许 GET，你却用了 DELETE。",
                        "像你到了正确窗口，但这个窗口只办取号业务，你却要在这里退款。",
                        List.of("删除接口误发成 GET", "表单提交到只支持 POST 的接口"),
                        List.of("检查 fetch/axios 的 method 配置", "必要时根据接口文档修正调用方式"),
                        List.of("确认控制器映射声明正确", "在文档里明确标注允许的方法"),
                        "bg-amber-500/15 text-amber-300"),
                status(409, "Conflict", "客户端错误", "请求本身没问题，但和当前资源状态冲突。",
                        "常见于重复提交、版本冲突、唯一键冲突，比如同一个用户名已被占用。",
                        "像你想注册一个昵称，系统告诉你这个名字已经被别人用了。",
                        List.of("用户名已存在", "乐观锁版本冲突", "订单重复提交"),
                        List.of("引导用户修改输入或刷新数据后重试", "避免无提示地重复提交"),
                        List.of("检查唯一索引和并发控制逻辑", "为冲突场景提供可理解的业务文案"),
                        "bg-amber-500/15 text-amber-300"),
                status(429, "Too Many Requests", "客户端错误", "请求太频繁，触发了限流。",
                        "接口在保护自己，防止刷接口、瞬时高并发或恶意请求压垮服务。通常会配合重试时间提示。",
                        "像奶茶店太忙了，店员说先别继续下单，稍后再排队。",
                        List.of("短信验证码请求过于频繁", "某 IP 或用户触发限流规则"),
                        List.of("节流按钮点击和自动重试", "尊重 Retry-After 等提示"),
                        List.of("检查限流阈值是否过严", "区分恶意流量与正常高峰"),
                        "bg-amber-500/15 text-amber-300"),
                status(500, "Internal Server Error", "服务器错误", "服务器内部出错了，但没有更具体的错误码。",
                        "这是一种兜底错误。常见原因有空指针、未处理异常、配置错误或代码逻辑缺陷。",
                        "像餐厅后厨突然乱成一团，前台只能说：店里出了问题，暂时做不了。",
                        List.of("空指针或数组越界", "数据库连接异常未处理", "配置缺失导致启动后运行报错"),
                        List.of("保留请求上下文并提示稍后重试", "不要把堆栈直接暴露给用户"),
                        List.of("先查日志和异常堆栈", "重点排查空值、配置、数据库和第三方依赖"),
                        "bg-rose-500/15 text-rose-300"),
                status(502, "Bad Gateway", "服务器错误", "网关或代理收到了上游返回的无效响应。",
                        "常见于 Nginx、网关、反向代理调用后端服务时，上游服务异常、挂掉，或者返回内容不符合预期。",
                        "像前台去后厨传菜，但后厨回了一句谁也听不懂的话，前台只能告诉你这单没法正常处理。",
                        List.of("Nginx 转发到已宕机服务", "网关连到错误端口", "上游服务返回损坏响应"),
                        List.of("先确认是不是网关层问题", "如果是多服务系统，记录请求链路方便排查"),
                        List.of("检查反向代理配置、上游健康状态和端口", "查看网关日志与后端服务日志是否对应"),
                        "bg-rose-500/15 text-rose-300"),
                status(503, "Service Unavailable", "服务器错误", "服务暂时不可用，通常是过载或维护中。",
                        "和 500 不同，503 更强调服务目前顶不住或者主动下线，稍后可能恢复。",
                        "像医院挂号处贴出通知：系统维护中，请稍后再来。",
                        List.of("系统维护窗口", "服务实例过载", "依赖资源不足主动熔断"),
                        List.of("提示稍后重试并保留当前操作", "避免用户反复猛点造成更高压力"),
                        List.of("检查实例负载、线程池、连接池和熔断状态", "维护场景下可返回明确公告文案"),
                        "bg-rose-500/15 text-rose-300"),
                status(504, "Gateway Timeout", "服务器错误", "上游服务响应太慢，网关等超时了。",
                        "多见于微服务、反向代理或 API 网关场景。当前服务在等别的服务，但一直没等到。",
                        "像前台帮你打电话联系仓库确认库存，结果仓库一直不接电话，前台只能让你稍后再来。",
                        List.of("下游接口响应超时", "数据库查询过慢拖垮链路", "网关超时阈值过短"),
                        List.of("提示超时并支持重试", "避免无限等待导致页面卡死"),
                        List.of("检查慢查询、远程调用耗时和超时配置", "结合链路追踪定位卡在哪一跳"),
                        "bg-rose-500/15 text-rose-300")
        );
    }

    private HttpStatusInfo status(int code, String title, String category, String summary,
                                  String explanation, String analogy, List<String> commonCases,
                                  List<String> frontendActions, List<String> backendChecks,
                                  String badgeClass) {
        return new HttpStatusInfo(code, title, category, summary, explanation, analogy,
                commonCases, frontendActions, backendChecks, badgeClass);
    }
}
