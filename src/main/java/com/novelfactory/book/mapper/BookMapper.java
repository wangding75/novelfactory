package com.novelfactory.book.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.novelfactory.book.model.BookEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookMapper extends BaseMapper<BookEntity> {}
