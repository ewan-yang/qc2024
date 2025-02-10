package com.tecpie.platform.common.enums;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcelVO {

    private String sheet;

    private List<List<String>> listsHead;

    private List<List<Object>> listsData;

}
