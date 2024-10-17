package com.zys.springbootinit.model.enums;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
public enum UpdateChartStatus {
    SUCCEED("成功", "succeed"),
    FAILED("失败", "failed"),
    RUNNING("运行中", "running");
    private String text;
    private String value;
    UpdateChartStatus(String text, String value) {
        this.text =text;
        this.value = value;
    }
}
