package com.cvv.scm_link.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cvv.scm_link.dto.request.ReceivingNoteRequest;
import com.cvv.scm_link.dto.response.ReceivingNoteResponse;
import com.cvv.scm_link.entity.ReceivingNote;

@Mapper(componentModel = "spring")
public interface ReceivingNoteMapper
        extends BaseMapper<ReceivingNote, ReceivingNoteRequest, ReceivingNoteRequest, ReceivingNoteResponse> {
    @Override
    @Mapping(target = "warehouse", ignore = true)
    @Mapping(target = "supplier", ignore = true)
    ReceivingNote toEntity(ReceivingNoteRequest dto);
}
