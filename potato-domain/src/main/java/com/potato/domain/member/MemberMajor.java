package com.potato.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberMajor {

    IT_ICT(Department.IT, "ICT융합학과"),
    IT_COMPUTER(Department.IT, "컴퓨터공학과"),
    INDUSTRIAL_SECURITY(Department.IT, "산업보안학과"),
    VISUAL_DESIGN(Department.DESIGN, "시각정보디자인학과"),
    INTERIOR_DESIGN(Department.DESIGN, "실내건축디자인학과"),
    TEXTILE_FASHION_DESIGN(Department.DESIGN, "섬유패션디자인학과"),
    MUSIC(Department.ART, "음악학과"),
    PERFORMING_ARTS(Department.ART, "공연예술학과");

    private final Department department;
    private final String name;

    public String getDepartment() {
        return this.department.getDepartment();
    }

    @Getter
    @RequiredArgsConstructor
    private enum Department {
        IT("IT학부"),
        DESIGN("디자인학부"),
        ART("예술학부");

        private final String department;
    }

}
