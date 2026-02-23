package com.hiroshiit.thebug.mapper;

import com.hiroshiit.thebug.entity.Bug;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * バグ展示用Mapper。
 * ユーザーIDや分類でバグ一覧・詳細を取得する。
 */
@Mapper
public interface BugMapper {

    /**
     * 全バグを分類・表示順で取得する
     *
     * @return バグ一覧
     */
    List<Bug> selectAll();

    /**
     * 分類でバグ一覧を取得する
     *
     * @param category 分類（spring, mybatis, thymeleaf 等）
     * @return 当該分類のバグ一覧
     */
    List<Bug> selectByCategory(@Param("category") String category);

    /**
     * IDで1件取得する
     *
     * @param id バグID
     * @return バグ（存在しなければ null）
     */
    Bug selectById(@Param("id") Long id);

    /**
     * 分類一覧を取得する（重複なし）
     *
     * @return 分類名のリスト
     */
    List<String> selectCategories();

    /**
     * バグを1件登録する
     *
     * @param bug 登録するバグ
     * @return 挿入件数
     */
    int insert(Bug bug);
}
