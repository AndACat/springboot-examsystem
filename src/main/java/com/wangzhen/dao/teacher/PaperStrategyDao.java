package com.wangzhen.dao.teacher;
import com.wangzhen.models.PaperStrategy;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import java.util.List;
@Mapper
@Repository
public interface PaperStrategyDao {

    @Select("select * from paperStrategy where account = #{account} order by sort desc")
    List<PaperStrategy> selectMyAllPaperStrategy(@Param("account")String account);

    @Insert("insert into paperStrategy(uuid,account,paperStrategyName,allScore,problemStrategyList)" +
            "        values (#{paperStrategy.uuid},#{account},#{paperStrategy.paperStrategyName},#{paperStrategy.allScore},#{paperStrategy.problemStrategyList})")
    void insertPaperStrategy(@Param("paperStrategy")PaperStrategy paperStrategy, @Param("account") String account);

    @Delete("delete from paperStrategy where account = #{account} and uuid = #{uuid}")
    void deletePaperStrategy(@Param("account") String account, @Param("uuid") String uuid);

    @Update("update paperStrategy set paperStrategyName = #{paperStrategy.paperStrategyName}, allScore = #{paperStrategy.allScore}, problemStrategyList = #{paperStrategy.problemStrategyList} where account = #{account} and uuid = #{paperStrategy.uuid}")
    void updatePaperStrategyInfo(@Param("paperStrategy")PaperStrategy paperStrategy,@Param("account")String account);

    @Select("select * from paperStrategy where uuid = #{uuid}")
    PaperStrategy selectPaperStrategyByPaperStrategy_uuid(@Param("uuid") String uuid);
}
