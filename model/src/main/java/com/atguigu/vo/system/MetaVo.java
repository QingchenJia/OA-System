package com.atguigu.vo.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetaVo {
    // 设置该路由在侧边栏和面包屑中展示的名字
    private String title;

    // 设置该路由的图标，对应路径src/assets/icons/svg
    private String icon;
}
