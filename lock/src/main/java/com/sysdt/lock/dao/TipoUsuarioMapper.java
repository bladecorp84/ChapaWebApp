package com.sysdt.lock.dao;

import com.sysdt.lock.model.TipoUsuario;
import com.sysdt.lock.model.TipoUsuarioExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TipoUsuarioMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tipo_usuario
     *
     * @mbggenerated Sat Jul 23 14:42:17 CDT 2016
     */
    int countByExample(TipoUsuarioExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tipo_usuario
     *
     * @mbggenerated Sat Jul 23 14:42:17 CDT 2016
     */
    int deleteByExample(TipoUsuarioExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tipo_usuario
     *
     * @mbggenerated Sat Jul 23 14:42:17 CDT 2016
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tipo_usuario
     *
     * @mbggenerated Sat Jul 23 14:42:17 CDT 2016
     */
    int insert(TipoUsuario record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tipo_usuario
     *
     * @mbggenerated Sat Jul 23 14:42:17 CDT 2016
     */
    int insertSelective(TipoUsuario record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tipo_usuario
     *
     * @mbggenerated Sat Jul 23 14:42:17 CDT 2016
     */
    List<TipoUsuario> selectByExample(TipoUsuarioExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tipo_usuario
     *
     * @mbggenerated Sat Jul 23 14:42:17 CDT 2016
     */
    TipoUsuario selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tipo_usuario
     *
     * @mbggenerated Sat Jul 23 14:42:17 CDT 2016
     */
    int updateByExampleSelective(@Param("record") TipoUsuario record, @Param("example") TipoUsuarioExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tipo_usuario
     *
     * @mbggenerated Sat Jul 23 14:42:17 CDT 2016
     */
    int updateByExample(@Param("record") TipoUsuario record, @Param("example") TipoUsuarioExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tipo_usuario
     *
     * @mbggenerated Sat Jul 23 14:42:17 CDT 2016
     */
    int updateByPrimaryKeySelective(TipoUsuario record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tipo_usuario
     *
     * @mbggenerated Sat Jul 23 14:42:17 CDT 2016
     */
    int updateByPrimaryKey(TipoUsuario record);
}