package com.hiroshiit.thebug.util;

/**
 * バグIDからパスを生成するユーティリティ。
 * 規約: 1バグ = 1パス（/bug/001, /bug/002 等）
 */
public class BugPathUtil {

    /**
     * バグIDからパスを生成する
     * @param id バグID（1-8）
     * @return /bug/001 形式のパス
     */
    public static String getPath(Long id) {
        if (id == null || id < 1 || id > 8) {
            return "/";
        }
        return "/bug/" + String.format("%03d", id);
    }
}
