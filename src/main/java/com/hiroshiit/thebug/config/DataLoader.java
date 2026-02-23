package com.hiroshiit.thebug.config;

import com.hiroshiit.thebug.entity.Bug;
import com.hiroshiit.thebug.mapper.BugMapper;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 起動時に初期バグデータを投入する。
 * 複数行コードを安全に扱うため SQL ファイルではなく Java で投入する。
 */
@Component
public class DataLoader implements ApplicationRunner {

    private final BugMapper bugMapper;

    public DataLoader(BugMapper bugMapper) {
        this.bugMapper = bugMapper;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (bugMapper.selectAll().isEmpty()) {
            insertInitialBugs();
        }
    }

    private void insertInitialBugs() {
        List<Bug> bugs = List.of(
                bug("spring", "SPR-001", "@Autowired 注入失敗（コンポーネントがスキャン範囲外）",
                        "UserService が Spring のコンポーネントスキャン対象パッケージ外に存在する場合",
                        "NoSuchBeanDefinitionException: No qualifying bean of type 'com.example.UserService'",
                        """
                        @Service
                        public class UserService {
                        }
                        
                        @RestController
                        public class UserController {
                            @Autowired
                            private UserService userService;  // 注入されない
                        }
                        """,
                        "UserService が @SpringBootApplication のスキャン基準パッケージ（例: com.hiroshiit.thebug）より上位や別パッケージにあると、Bean として登録されない。",
                        """
                        // 正しい例: アプリケーションと同じ階層かそのサブパッケージに配置する
                        package com.hiroshiit.thebug.service;
                        
                        @Service
                        public class UserService { }
                        
                        @SpringBootApplication
                        @ComponentScan(basePackages = {"com.hiroshiit.thebug", "com.example"})
                        public class ThebugApplication { }
                        """,
                        "コンポーネントは必ずベースパッケージかそのサブパッケージに配置する。",
                        "パッケージ構造を規約に合わせ、スキャン範囲を意識する。",
                        10),
                bug("spring", "SPR-002", "循環依存（A → B → A）",
                        "AService が BService を注入し、BService が AService を注入する場合",
                        "BeanCurrentlyInCreationException: Error creating bean with name 'AService'",
                        """
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
                        """,
                        "二つの Bean が互いに参照し合うため、どちらを先に初期化するか決定できず、コンテナが失敗する。",
                        """
                        @Service
                        public class AService {
                            @Autowired
                            @Lazy
                            private BService bService;
                        }
                        // または共通ロジックを第三の Service に切り出し、循環を解消する
                        """,
                        "循環参照は設計上の問題。インターフェース分離やファサードの導入で依存の向きを一方向にする。",
                        "単一責任と依存の向きを意識し、循環ができない設計にする。",
                        20),
                bug("spring", "SPR-003", "@Transactional が同一クラス内呼び出しで効かない",
                        "process() から同一クラス内の save() を呼ぶ場合、プロキシを経由しないためトランザクションが開始されない",
                        "（例外時でもロールバックされない・コミットされない）",
                        """
                        @Service
                        public class OrderService {
                            @Transactional
                            public void save() { }
                            public void process() {
                                save();  // プロキシを経由しないため @Transactional が効かない
                            }
                        }
                        """,
                        "Spring の @Transactional は AOP プロキシで実現される。同一オブジェクト内の this.save() 呼び出しはプロキシを経由しない。",
                        """
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
                        """,
                        "トランザクション境界は「別 Bean の public メソッド」に付与する。",
                        "トランザクションが必要な処理は別 Service に分離する設計を推奨。",
                        30),
                bug("mybatis", "MYB-001", "Parameter 'xxx' not found（引数名不一致）",
                        "Mapper メソッドの引数名と XML の #{name} が一致していない場合",
                        "Parameter 'name' not found. Available parameters are [username, param2]",
                        """
                        <select id="find" resultType="User">
                            SELECT * FROM user WHERE name = #{name}
                        </select>
                        // Java: User find(String username);
                        """,
                        "XML では #{name} と書いているが、Java の引数名は username のため、MyBatis が名前でバインドできずエラーになる。",
                        """
                        <select id="find" resultType="User">
                            SELECT * FROM user WHERE name = #{username}
                        </select>
                        // または User find(@Param("name") String username);
                        """,
                        "引数が複数ある場合は @Param を付ける。単一引数でも名前を XML と一致させるか @Param で明示する。",
                        "Mapper インターフェースと XML のパラメータ名を規約で統一する。",
                        10),
                bug("mybatis", "MYB-002", "SQL インジェクション（${} の文字列結合）",
                        "ユーザー入力をそのまま ${} で SQL に埋め込む場合",
                        "攻撃者が ' OR '1'='1 等を入力すると、意図しない SQL が実行される",
                        """
                        <select id="findUsersUnsafe" resultType="User">
                            SELECT * FROM users WHERE name = '${name}'
                        </select>
                        """,
                        "${} は文字列置換のため、シングルクォート等をエスケープされず、SQL インジェクションの原因になる。",
                        """
                        <select id="findUsersSafe" resultType="User">
                            SELECT * FROM users WHERE name = #{name}
                        </select>
                        """,
                        "プレースホルダは必ず #{} を使う。動的カラム名・テーブル名などやむを得ない場合のみ ${} を使い、入力値はホワイトリスト検証する。",
                        "IPA 指針: 無効化（Bind Variables）を徹底する。",
                        20),
                bug("thymeleaf", "THY-001", "${user.name} で user が null のとき NPE",
                        "コントローラーで user を渡し忘れた、または null のままテンプレートに渡した場合",
                        "NullPointerException または Thymeleaf のエラー",
                        "<span th:text=\"${user.name}\"></span>",
                        "user が null の場合、user.name の評価で NPE が発生する。",
                        """
                        <span th:text="${user?.name}"></span>
                        // または コントローラーで必ずデフォルトオブジェクトを渡す
                        """,
                        "テンプレート側では Safe Navigation（?.）や null チェックをし、コントローラー側では null を渡さないようにする。",
                        "モデルに渡すオブジェクトは null にしない、または Optional と th:if で分岐する。",
                        10),
                bug("thymeleaf", "THY-002", "th:utext による XSS 脆弱性",
                        "DB に保存された HTML/スクリプトを th:utext でそのまま出力する場合",
                        "攻撃者が <script> 等を入力すると、そのまま実行される",
                        "<div th:utext=\"${content}\"></div>",
                        "th:utext は HTML をエスケープせずに出力するため、悪意のあるスクリプトが実行される。",
                        "<div th:text=\"${content}\"></div>\n// または 表示上 HTML が必要な場合はサニタイズライブラリで許可タグのみ許可する",
                        "ユーザー入力をそのまま表示する場合は th:text（エスケープ）を使う。th:utext は信頼できるコンテンツに限定する。",
                        "IPA 指針: 無害化（サニタイズ）。すべての出力をエスケープする。",
                        20),
                bug("security", "SEC-001", "管理画面に権限チェックなし",
                        "/admin に誰でもアクセスできる実装",
                        "権限のないユーザーが管理機能を実行できる",
                        """
                        @GetMapping("/admin")
                        public String admin() {
                            return "admin/index";
                        }
                        """,
                        "認可（Authorization）が実装されておらず、URL を知っていれば誰でもアクセスできる。",
                        """
                        @GetMapping("/admin")
                        @PreAuthorize("hasRole('ADMIN')")
                        public String admin() {
                            return "admin/index";
                        }
                        // かつ Spring Security で URL ベースの認可も設定する
                        """,
                        "認証だけでなく認可（ロール・権限）を必ず付与する。@PreAuthorize や設定で URL を保護する。",
                        "「デフォルト拒否」で、必要な URL だけ許可する設計にする。",
                        10)
        );
        for (Bug b : bugs) {
            bugMapper.insert(b);
        }
    }

    private static Bug bug(String category, String bugCode, String title,
                           String occurrenceCondition, String errorMessage, String wrongCode,
                           String causeAnalysis, String correctCode, String correctComment,
                           String prevention, int sortOrder) {
        Bug b = new Bug();
        b.setCategory(category);
        b.setBugCode(bugCode);
        b.setTitle(title);
        b.setOccurrenceCondition(occurrenceCondition);
        b.setErrorMessage(errorMessage);
        b.setWrongCode(wrongCode);
        b.setCauseAnalysis(causeAnalysis);
        b.setCorrectCode(correctCode);
        b.setCorrectComment(correctComment);
        b.setPrevention(prevention);
        b.setSortOrder(sortOrder);
        return b;
    }
}
